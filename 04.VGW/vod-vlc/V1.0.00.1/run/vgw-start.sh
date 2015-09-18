#!/bin/bash

if [ -z "$VGW_CONFIG_FILE" ]; then
        export VGW_CONFIG_FILE=../conf/config.properties
fi

if [ -z "$VGW_LOG_PATH" ]; then
        export VGW_LOG_PATH=../conf/log4j.properties
fi

export LANG=zh_CN.GBK

cd ../resource/

PID=`ps -ef |grep 'prod.nebula.vgw4vlc.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping VGW process with \"kill\""
        kill -9 ${PID}
fi

PIDs=`ps -ef |grep 'telnet'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PIDs}" ]; then
        kill -9 ${PIDs}
fi

nohup cvlc --intf telnet --telnet-host 192.168.100.56 --telnet-port 5000 --telnet-password bbcv  >>/dev/null &

echo "Starting VGW process ......"

nohup java -Djava.ext.dirs=../libs -cp .:VGW.jar  \
prod.nebula.vgw4vlc.service.TCPServer >>/dev/null &

PID=`ps -ef |grep 'prod.nebula.vgw4vlc.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start VGW process"
else
	echo "fail to start VGW process"
fi
