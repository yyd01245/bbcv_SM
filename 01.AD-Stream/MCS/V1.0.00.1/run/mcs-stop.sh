PID=`ps -ef |grep 'prod.nebula.mcs.service.Server'|grep -v grep|awk '{print $2}'`

if [ ! -z "${PID}" ]; then
        echo "Stopping MCS Server process with \"kill\""
        echo "MCS Server process closed"
        kill -9 ${PID}
else
        echo "MCS Server process not running"
fi


