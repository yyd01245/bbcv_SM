#!/bin/bash
PID=`ps -ef|grep SM_main|grep -v "grep"|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping SM process with \"kill\""
        kill -9 ${PID}

fi
nohup ./SM_main >sm.log 2>&1 &

#echo "ok"
