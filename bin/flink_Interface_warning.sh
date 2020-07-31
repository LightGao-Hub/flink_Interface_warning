#!/bin/bash
NAME=flink_Interface_warning
JAR=$NAME-1.0.jar
LOG=/datalog/$NAME/$NAME.log

case "$1" in
    start)
        nohup flink run -m yarn-cluster -p 3 -yjm 1024m -ytm 1024m -ys 1 -yn 3 -ynm flink_Interface_warning $JAR  > $LOG 2>&1 &
		if [ $? -eq 0 ];then
			echo "Started $NAME SUCCESS"
		else
			echo "Started $NAME ERROR"
		fi
    ;;
    stop)
        if [ $2 ]; then
			if [ $3 ]; then
				if [ $4 ]; then
					flink cancel -m yarn-cluster -s $2 $3  -yid $4
					yarn application -kill $4
					if [ $? -eq 0 ];then
						echo "stop $NAME SUCCESS"
					else
						echo "stop $NAME ERROR"
					fi
				else
					echo "缺少Yarn上Flink作业的jodID，例如：application_1587437317084_0004"
				fi
			else
				echo "缺少Flink作业的jodID，例如：32824a1d56830eea9eb5ff2e5a57540b"
			fi
		else 
			echo "缺少作业的保存点路径，例如：hdfs://nameservice1/flink/savepoints"
		fi
    ;;
	restore)
		if [ $2 ]; then
			nohup flink run -s $2 -m yarn-cluster -p 3 -yjm 1024m -ytm 1024m -ys 1 -yn 3 $JAR > $LOG 2>&1 &
			if [ $? -eq 0 ];then
				echo "restore $NAME SUCCESS"
			else
				echo "restore $NAME ERROR"
			fi
		else 
			echo "缺少作业恢复的保存点路径，例如：hdfs://nameservice1/flink/savepoints/savepoint-65a8b1-f12e504bd2db"
		fi
	;;	
    *)
    #echo "Usage: {start|stop|restart|reload|force-reload|status}"
    echo "Usage: {start|stop|restore}" >&2
    exit 1
    ;;
esac

exit 0
