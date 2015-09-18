#!/bin/bash

PID=`ps -ef |grep 'prod.nebula.vgw4sida.service.TCPServer'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping VGW process with \"kill\""
        kill -9 ${PID}
fi

echo "Stop VGW process OK ......"