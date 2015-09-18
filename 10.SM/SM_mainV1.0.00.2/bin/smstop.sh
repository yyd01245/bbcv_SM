#! /dev/bash

PID=`ps -ef|grep SM_main|grep -v "grep"|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping SM process with \"kill\""
        kill -9 ${PID}

fi

#echo echo "stop ok"
