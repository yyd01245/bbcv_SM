#!/bin/bash

if [ ! -n "$1" ];then
echo "use $0 with wrong param";
exit 1
fi
PID=`ps -ef|grep "$1"|grep -v "$0"|grep -v "grep"|awk '{print $2}'`
if [ -z "${PID}" ];then
	echo ""$1" process is not running";
	exit 0;
fi
while [ ! -z "${PID}" ];do
	echo "Stopping  "$1" process with \"kill\"";
	kill -9 ${PID};
	sleep 2;
	PID=`ps -ef|grep "$1"|grep -v "$0"|grep -v "grep"|awk '{print $2}'`;
done;

echo ""$1" process \"killed\""
