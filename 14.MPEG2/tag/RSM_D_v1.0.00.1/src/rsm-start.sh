#!/bin/bash

M_PATH=`pwd`
sudo chmod -R 777 ${M_PATH}
sudo cp ${M_PATH}/init.html /opt/
sudo sh rsm-stop.sh
sudo ./autoPluginUpdate.sh &
sleep 1
sudo /etc/init.d/dm816x start 63
sudo rm -rf /tmp/.X*

sudo ./resourcemag &

sleep 30
 
PID=`ps -e|grep 'resourcemag'|awk '{print $1}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start resourcemag process"
else
	echo "fail to start resourcemag process"
fi
