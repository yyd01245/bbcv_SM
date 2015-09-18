#!/bin/bash

if [ -z "$MSIGW_CONFIG_FILE" ]; then
        export MSIGW_CONFIG_FILE=../conf/config.properties
fi

if [ -z "$MSIGW_LOG_PATH" ]; then
        export MSIGW_LOG_PATH=../conf/log4j.properties
fi

export LANG=zh_CN.GBK

cd ../resource/

PID=`ps -ef |grep 'com.bbcvision.msiAgent.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping MSIGW process with \"kill\""
        kill -9 ${PID}
fi

echo "Starting MSIGW process ......"

nohup java -Djava.ext.dirs=../libs -cp .:MSIGW.jar  \com.bbcvision.msiAgent.service.TCPServer >>/dev/null &

PID=`ps -ef |grep 'com.bbcvision.msiAgent.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start MSIGW process"
else
	echo "fail to start MSIGW process"
fi