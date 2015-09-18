#!/bin/bash

PID=`ps -ef |grep 'prod.nebula.vgw4vlc.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping VGW process with \"kill\""
        kill -9 ${PID}
fi

PIDs=`ps -ef |grep 'telnet'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PIDs}" ]; then
        kill -9 ${PIDs}
fi


echo "Stop VGW process OK ......"
