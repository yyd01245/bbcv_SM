#!/bin/bash

#以下为Java的程序

#redis 需要先启动
#redis
Redis_Dir_Path=/home/x00/Penglei/redis
#redis启动脚本
Redis_Start_sh=start.sh
#进程名 用来查找到该进程
Redis_Proc_Name=redis-server
#结束脚本
Redis_Stop_sh=stop.sh
#参数配置文件
Redis_Conf_File1=mag.redis.conf
Redis_Conf_File2=msi.redis.conf
Redis_Conf_File3=crsm.redis.conf
Redis_Conf_File4=swm.redis.conf

#MAG 
MAG_Dir_Path=/home/x00/Penglei/MAG/bin
#启动脚本名
MAG_Start_sh=mag-start.sh
#进程名
MAG_Proc_Name=MAG
#停止脚本名
MAG_Stop_sh=mag-stop.sh

#NS
NS_Dir_Path=/home/x00/Penglei/NSCS/apache-tomcat-6.0.24/bin
#启动脚本名
NS_Start_sh=startup.sh
#进程名
NS_Proc_Name=NSCS
#停止脚本名
NS_Stop_sh=shutdown.sh

#UCMS
UCMS_Dir_Path=/home/x00/Penglei/UCMS/bin
#启动脚本名
UCMS_Start_sh=ucms-start.sh
#进程名
UCMS_Proc_Name=UCMS
#停止脚本名
UCMS_Stop_sh=ucms-stop.sh

#VGW
VGW_Dir_Path=/home/x00/Penglei/VGW/bin
#启动脚本
VGW_Start_sh=vgw-start.sh
#进程名
VGW_Proc_Name=VGW
#停止脚本名
VGW_Stop_sh=vgw-stop.sh

#ADMIN
ADMIN_Dir_Path=/home/x00/Penglei/ADMIN/apache-tomcat-6.0.24/bin
#启动脚本
ADMIN_Start_sh=startup.sh
#进程名
ADMIN_Proc_Name=ADMIN
#停止脚本名
ADMIN_Stop_sh=shutdown.sh

#承载CRSM
CRSM_Dir_Path=/home/x00/Penglei/CRSM/bin
#启动脚本
CRSM_Start_sh=start.sh
#进程名
CRSM_Proc_Name=crsm-core
#停止脚本名
CRSM_Stop_sh=stop.sh

#以下为C程序
#RSM调度
RSM_Dir_Path=/home/x00/Penglei/rsm_mpeg2/rsm
#RSM的进程名
RSM_Proc_Name=resourcemag
#RSM的启动脚本
RSM_Start_sh=rsm-start.sh
#RSM的停止脚本
RSM_Stop_sh=rsm-stop.sh

#SM调度中心
SM_Dir_Path=/home/x00/Penglei/SM
#SM的进程名
SM_Proc_Name=SM_main 
#启动脚本名
SM_Start_sh=smstart.sh
#停止脚本名
SM_Stop_sh=smstop.sh

#切流器 调度
SWM_Dir_Path=/home/x00/Penglei/SWManger
#切流器调度的进程名
SWM_Proc_Name=SWM
#启动脚本名
SWM_Start_sh=swm_start.sh
#停止脚本名
SWM_Stop_sh=swm_stop.sh

#切流器
SWT_Dir_Path=/home/x00/Penglei/Switch
#切流器的进程名
SWT_Proc_Name=Switch_tool
#启动脚本名
SWT_Start_sh=swt_start.sh
#停止脚本名
SWT_Stop_sh=swt_stop.sh

#发广告流
GuangGao_Dir_Path=/home/x00/Penglei/ADV
#广告流的进程名
GuangGao_Proc_Name=Mul_main
#启动脚本名
GuangGao_Start_sh=mustart.sh
#停止脚本名
GuangGao_Stop_sh=mustop.sh 

#检测是否存在进程
function check_last_command(){
	result=$?
	good_value=0
	command=''
	config=''
	declare -i onCount
	pidlist=`ps -ef |egrep "$1"|sed /.*grep/d|awk '{print $8}'`

	onCount=0
    for pid in $pidlist; do
		onCount=$onCount+1
        #echo  proc $pid	$onCount	
    done
	
	if [ $onCount -lt 1 ]
	then
		echo "open process $1 error, please make sure $1 is ok"
		exit 
	fi
	
	if [ $# -eq 1 ]
	then
		echo "check $1 result good go to next "
		return 0
	fi
	
	conflist=`ps -ef |egrep "$1"|egrep "$2"|sed /.*grep/d|awk '{print $9}'`	
	onCount=0
	for conf in $conflist; do
		onCount=$onCount+1
        echo  config $conf $onCount	
    done
	
	if [ $onCount -lt 1 ]
	then
		echo "open process $1 $2 error, please make sure $1 $2 is ok"
		exit 
	fi

	echo "check $1  $2 result good go to next "

}

#杀进程
function kill_command(){
	result=$?
	good_value=0
	command=''
	config=''
	declare -i onCount
	pidlist=`ps -ef |egrep "$1"|sed /.*grep/d|awk '{print $2}'`

	onCount=0
    for pid in $pidlist; do
		echo kill pid $pid start
		kill -9 $pid
		onCount=$onCount+1
        echo kill pid $pid end
		
    done
	echo "kill process $1 number=$onCount "

}

#先运行停止脚本
cd $Redis_Dir_Path
sh $Redis_Stop_sh

cd $MAG_Dir_Path
sh $MAG_Stop_sh
cd $NS_Dir_Path
sh $NS_Stop_sh
cd $UCMS_Dir_Path
sh $UCMS_Stop_sh
cd $VGW_Dir_Path
sh $VGW_Stop_sh
cd $ADMIN_Dir_Path
sh $ADMIN_Stop_sh
cd $CRSM_Dir_Path
sh $CRSM_Stop_sh
echo stop java sh
sleep 3
cd $RSM_Dir_Path
sh $RSM_Stop_sh

cd $SWM_Dir_Path
sh $SWM_Stop_sh

cd $SWT_Dir_Path
sh $SWT_Stop_sh
cd $SM_Dir_Path
sh $SM_Stop_sh
cd $GuangGao_Dir_Path
sh $GuangGao_Stop_sh

echo stop sm sh 

#重新杀进程
kill_command $Redis_Proc_Name
kill_command $MAG_Proc_Name
kill_command $UCMS_Proc_Name
kill_command $VGW_Proc_Name
kill_command $ADMIN_Proc_Name
kill_command $CRSM_Proc_Name

kill_command $RSM_Proc_Name
kill_command $SWM_Proc_Name
kill_command $SWT_Proc_Name
kill_command $GuangGao_Proc_Name
kill_command $SM_Proc_Name


#启动脚本
cd $Redis_Dir_Path
sh $Redis_Start_sh >& /dev/null
sleep 5
check_last_command $Redis_Proc_Name $Redis_Conf_File1
check_last_command $Redis_Proc_Name $Redis_Conf_File2
check_last_command $Redis_Proc_Name $Redis_Conf_File3
check_last_command $Redis_Proc_Name $Redis_Conf_File4

cd $MAG_Dir_Path
sh $MAG_Start_sh >& /dev/null
echo start $MAG_Dir_Path/$MAG_Start_sh
cd $NS_Dir_Path
sh $NS_Start_sh >& /dev/null
echo start $NS_Dir_Path/$NS_Start_sh
cd $UCMS_Dir_Path
sh $UCMS_Start_sh >& /dev/null
echo start $UCMS_Dir_Path/$UCMS_Start_sh
sleep 1
cd $VGW_Dir_Path
sh $VGW_Start_sh >& /dev/null
echo start $VGW_Dir_Path/$VGW_Start_sh
cd $ADMIN_Dir_Path
sh $ADMIN_Start_sh >& /dev/null
echo start $ADMIN_Dir_Path/$ADMIN_Start_sh

sleep 5
cd $CRSM_Dir_Path
sh $CRSM_Start_sh >& /dev/null
echo start $CRSM_Dir_Path/$CRSM_Start_sh

sleep 2
cd $RSM_Dir_Path
sh $RSM_Start_sh >& /dev/null
echo start $RSM_Dir_Path/$RSM_Start_sh
sleep 10
cd $SWT_Dir_Path
sh $SWT_Start_sh >& /dev/null
echo start $SWT_Dir_Path/$SWT_Start_sh
cd $SWM_Dir_Path
sh $SWM_Start_sh >& /dev/null
echo start $SWM_Dir_Path/$SWM_Start_sh
cd $GuangGao_Dir_Path
sh $GuangGao_Start_sh >& /dev/null
echo start $GuangGao_Dir_Path/$GuangGao_Start_sh
sleep 10
cd $SM_Dir_Path
sh $SM_Start_sh >& /dev/null
echo start $SM_Dir_Path/$SM_Start_sh

sleep 2

#检测对应的进程是否开启

check_last_command $MAG_Proc_Name
check_last_command $NS_Proc_Name
check_last_command $UCMS_Proc_Name
check_last_command $VGW_Proc_Name
check_last_command $ADMIN_Proc_Name
check_last_command $CRSM_Proc_Name

check_last_command $RSM_Proc_Name
check_last_command $SWM_Proc_Name
check_last_command $SWT_Proc_Name
check_last_command $GuangGao_Proc_Name
check_last_command $SM_Proc_Name


echo "----------ALL success -------------"



