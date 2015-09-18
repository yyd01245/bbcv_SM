#!/bin/bash

sudo sh kill_progress.sh autoPluginUpdate

if [ $? -ne 0 ] ;then
	exit 1;
fi

sudo sh kill_progress.sh resourcemag

if [ $? -ne 0 ] ;then
	exit 1;
fi

sudo sh kill_progress.sh PluKill

if [ $? -ne 0 ] ;then
	exit 1;
fi
sudo sh kill_progress.sh avencoder

if [ $? -ne 0 ] ;then
	exit 1;
fi

sudo sh stop_vnc.sh

sudo /etc/init.d/dm816x stop

sleep 5

rm -rf killlibvlc.log

echo "Stoping resourcemag process ......"

