#!/bin/bash

PID=`ps -ef |grep 'com.bbcvision.msiAgent.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping MSIGW process with \"kill\""
        kill -9 ${PID}
fi

echo "Stop MSIGW process OK ......"