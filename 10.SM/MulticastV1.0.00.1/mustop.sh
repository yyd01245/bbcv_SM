#!/bin/bash
PID=`ps -ef|grep Mul_main|grep -v "grep"|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping Mul process with \"kill\""
        kill -9 ${PID}

fi
killall vlc

#echo "ok"
