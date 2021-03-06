#!/bin/bash

#SM调度中心
SM_Dir_Path=/home/x00/Penglei/SM/SM_1017
#SM的进程名
SM_Proc_Name=SM_main 
#启动脚本名
SM_Start_sh=smstart.sh
#停止脚本名
SM_Stop_sh=smstop.sh

#redis 需要先启动
#redis
Redis_Dir_Path=/home/bbcv/redis
#redis启动脚本
Redis_Start_sh=start.sh

#切流器 调度
Switch_Manager_Dir_Path=/home/x00/Penglei/Switch_Manger
#切流器调度的进程名
Switch_Manager_Proc_Name=SWM
#启动脚本名
Switch_Manager_Start_sh=swm_start.sh
#停止脚本名
Switch_Manager_Stop_sh=swm_stop.sh

#切流器
Switch_Dir_Path=/home/x00/Penglei/Switch
#切流器的进程名
Switch_Proc_Name=Switch_tool
#启动脚本名
Switch_Start_sh=swt_start.sh
#停止脚本名
Switch_Stop_sh=swt_stop.sh

#发广告流
GuangGao_Dir_Path=/home/x00/Penglei/guangGao
#广告流的进程名
GuangGao_Proc_Name=Mul_main
#启动脚本名
GuangGao_Start_sh=mustart.sh
#停止脚本名
GuangGao_Stop_sh=mustop.sh 

#以下为Java的程序

#MAG 
MAG_Dir_Path=/home/penglei/MAG/bin
#启动脚本名
MAG_Start_sh=mag-start.sh
#进程名

#停止脚本名

#NS
NS_Dir_Path=/home/penglei/NS/apache-tomcat-6.0.24/bin/
#启动脚本名
NS_Start_sh=startup.sh
#进程名

#停止脚本名

#UCMS
UCMS_Dir_Path=/home/penglei/UCMS/bin
#启动脚本名
UCMS_Start_sh=ucms-start.sh
#进程名

#停止脚本名

#VGW
VGW_Dir_Path=/home/penglei/VGW/bin
#启动脚本
VGW_Start_sh=vgw-start.sh
#进程名

#停止脚本名

#ADMIN
ADMIN_Dir_Path=/home/penglei/ADMIN/apache-tomcat-6.0.24/bin
#启动脚本
ADMIN_Start_sh=startup.sh
#进程名

#停止脚本名

#承载CRSM
CRSM_Dir_Path=/home/penglei/CRSM/bin
#启动脚本
ADMIN_Start_sh=start.sh
#进程名

#停止脚本名

#检测是否存在进程

function check_last_command(){
	result=$?
	good_value=0
	command_info=``
	if [ $# -ne 1 ]
	then
		echo "check $1"
		command_info=$1
	fi
	if [ $# -ne 2 ]
	then
		echo "line $command_info fail"
		exit $result
	fi
	echo "line $command_info pass"
}


#先启动Java程序模块
#cd $Redis_Dir_Path
#sh $Redis_Start_sh

test_proc=SWM
test2=SWT
check_last_command $test_proc $SWT
sleep 1
echo "----------over success -------------"



