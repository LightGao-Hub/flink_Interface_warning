package com.shenque.warning.flink

import java.util.concurrent.TimeUnit

import com.alibaba.fastjson.JSON
import com.shenque.warning.config.KafkaConfig
import com.shenque.warning.entity.{Demo, LogEntity}
import com.shenque.warning.util.{EmailThread, JavaReadProperties}
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory

object ProcessFunctionTimers extends Serializable {

  private val logger = LoggerFactory.getLogger(classOf[ProcessFunctionTimers])

  private val checkpointPath = new JavaReadProperties().read.getProperty("hdfs.checkpoints.dir")

  private val topic = new JavaReadProperties().read.getProperty("kafka.topic")

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //处理时间语义
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    //配置检查点

    val config = new Configuration()
    config.setBoolean("state.backend.async", true)
    env.setStateBackend(new FsStateBackend(checkpointPath).configure(config))
    //设置检查点间隔
    env.enableCheckpointing(10000)
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(5000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    env.getCheckpointConfig.setFailOnCheckpointingErrors(false)
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
      3, // 重启尝试次数阿斯蒂芬
      org.apache.flink.api.common.time.Time.of(5, TimeUnit.SECONDS)
    ));

    val myConsumer = new FlinkKafkaConsumer010[String](
      topic
      , new SimpleStringSchema()
      , KafkaConfig.properties
    )
    val stream: DataStream[String] = env.addSource(myConsumer)

    val warnings = stream
      .map(JSON.parseObject(_, classOf[LogEntity]))
      .uid("map")
      .keyBy(_.getPROJECT_ACTION)
      .process(new TempIncreaseAlertFunction)
      .uid("process")

    env.execute("Monitor sensor temperatures.")
  }

  case class ProcessFunctionTimers()

}


class TempIncreaseAlertFunction
  extends KeyedProcessFunction[String, LogEntity, String] with Serializable {

  private val logger = LoggerFactory.getLogger(classOf[TempIncreaseAlertFunction])

  // 保存不同key的状态
  lazy val lastTemp: ValueState[Long] =
    getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("interface_of_time", Types.of[Long])
    )

  override def processElement(r: LogEntity,
                              ctx: KeyedProcessFunction[String, LogEntity, String]#Context,
                              out: Collector[String]): Unit = {
    val prevTemp = lastTemp.value()
    if (r.getOVERALL_ELAPSED >= 5000 && prevTemp == 0L) {
      logger.info(s"接口：${r.getPROJECT_ACTION}  响应时间 ：${r.getOVERALL_ELAPSED} 超时 且 lastTemp ：${prevTemp} 无状态, 更新lastTemp状态 ")
      lastTemp.update(r.getOVERALL_ELAPSED)
      //异步发送邮件
      try {
        new Thread(new EmailThread(
          "Interface alarm",
          s"Warning interface :${r.getPROJECT_ACTION} , Delay time : ${r.getOVERALL_ELAPSED}, Log information ：${r}"
        )).start()
        logger.info(s"异步邮件发送成功，内容：Warning interface :${r.getPROJECT_ACTION} , Delay time : ${r.getOVERALL_ELAPSED}, Log information ：${r}")
      } catch {
        case e: Exception => {
          logger.error("异步发送邮件异常 e:" + e.getMessage, e)
          e.printStackTrace()
        }
      }
      //注册半小时后的计时器
      logger.info(s" 注册 : ${r.getPROJECT_ACTION}  计时器，半小时后执行")
      val timerTs = ctx.timerService().currentProcessingTime() + 1000 * 60 * 30
      ctx.timerService().registerProcessingTimeTimer(timerTs)
    }
  }

  override def onTimer(
                        ts: Long,
                        ctx: KeyedProcessFunction[String, LogEntity, String]#OnTimerContext,
                        out: Collector[String]): Unit = {
    logger.info(s"定时器执行，lastTemp 状态重置为 0L ")
    lastTemp.update(0L)
  }
}


