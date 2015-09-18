#!/bin/bash
PID=`ps -ef|grep SM_main|grep -v "grep"|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping SM process with \"kill\""
        kill -9 ${PID}
	sleep 2
fi
nohup ./SM_main > /dev/null &

#echo "ok"
