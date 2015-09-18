#!/bin/bash
PID=`ps -ef|grep SM_main|grep -v "grep"|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping SM process with \"kill\""
        kill -9 ${PID}

fi
sleep 1

./SM_main &>sm.log &

#echo "ok"
