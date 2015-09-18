#!/bin/bash

if [ -z "$VRC_CONFIG_FILE" ]; then
        export VRC_CONFIG_FILE=../conf/config.properties
fi

if [ -z "$VRC_LOG_PATH" ]; then
        export VRC_LOG_PATH=../conf/log4j.properties
fi

export LANG=zh_CN.GBK

cd ../resource/

PID=`ps -ef |grep 'prod.nebula.vrc.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping VGW process with \"kill\""
        kill -9 ${PID}
fi

echo "Starting VGW process ......"

nohup java -Djava.ext.dirs=../libs -cp .:VGW.jar  \
prod.nebula.vrc.service.TCPServer >>/dev/null &

PID=`ps -ef |grep 'prod.nebula.vrc.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start VGW process"
else
	echo "fail to start VGW process"
fi