#!/bin/bash

sleepTime=1200
URL="http://192.168.60.60/chromeplugin.list"
while [ 1 ];do
	sudo ./pluginupdate.sh "$URL";
	sleep $sleepTime;
done;
