#!/bin/bash

if [ -z "$VGW_CONFIG_FILE" ]; then
        export VGW_CONFIG_FILE=../conf/config.properties
fi

if [ -z "$VGW_LOG_PATH" ]; then
        export VGW_LOG_PATH=../conf/log4j.properties
fi

export LANG=zh_CN.GBK

cd ../resource/

PID=`ps -ef |grep 'prod.nebula.vgw.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping VGW process with \"kill\""
        kill -9 ${PID}
fi

echo "Starting VGW process ......"

nohup java -Djava.ext.dirs=../libs -cp .:VGW.jar  \
prod.nebula.vgw.service.TCPServer >>/dev/null &

PID=`ps -ef |grep 'prod.nebula.vgw.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start VGW process"
else
	echo "fail to start VGW process"
fi