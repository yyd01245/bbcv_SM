if [ -z "$MCS_CONF_FILE" ]; then
	export MCS_CONF_FILE=../conf/config.properties
fi

if [ -z "$MCS_LOG_FILE" ]; then
	export MCS_LOG_FILE=../conf/log4j.properties
fi

export LANG=zh_CN.utf-8

PRG="../resource/"
PRGDIR=`dirname "$PRG"`
#cd $PRGDIR
cd ../resource/

PID=`ps -ef |grep 'prod.nebula.mcs.service.Server'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping MCS Server process with \"kill\""
        kill -9 ${PID}
fi

echo "Starting MCS Server process ......"
 
nohup java -Xms512m -Xmx2048m -Djava.ext.dirs=../libs -cp .:MCS.jar  \
prod.nebula.mcs.service.Server >>/dev/null  &

PID=`ps -ef |grep 'prod.nebula.mcs.service.Server'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
 	echo "success to start MCS process"
else
	echo "fail to start MCS process"
fi
