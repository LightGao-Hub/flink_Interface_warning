package com.shenque.warning.config

import java.io.{IOException, InputStream}
import java.util.Properties

import com.shenque.warning.util.{JavaReadProperties, SendEmailUtil}
import org.apache.kafka.common.serialization.StringDeserializer

/**
  * @author gl 
  */
object KafkaConfig extends Serializable {

  private val prop: Properties = new JavaReadProperties().read

  val properties:Properties = new Properties()

  {
    properties.setProperty("bootstrap.servers", prop.getProperty("kafka.brokers"))
    properties.setProperty("group.id", prop.getProperty("kafka.groupid"))
    properties.setProperty("key.deserializer", classOf[StringDeserializer].getCanonicalName);
    properties.setProperty("value.deserializer", classOf[StringDeserializer].getCanonicalName);
    properties.setProperty("enable.auto.commit", "false");
    properties.setProperty("session.timeout.ms", "60000");
    properties.setProperty("auto.offset.reset", "latest");
  }


}
