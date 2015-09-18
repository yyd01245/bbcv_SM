n=`ps -ef |grep resourcemag|grep -v "grep"`
if [ -n "$n" ];then 
echo "rsm进程存在"
echo "========================================="
else
echo "rsm进程不存在"
echo "请到目录/home/x00/yyd/yyd/rsm执行 sh rsm-start.sh命令"
echo "========================================="
fi

n=`ps -ef |grep Mul_main|grep -v "grep"`
if [ -n "$n" ];then 
echo "广告进程存在"
echo "========================================="
else
echo "广告进程不存在"
echo "请到目录/home/x00/Penglei/ADV执行sh mustart.sh 命令"
echo "========================================="
fi

n=`ps -ef |grep Switch_main|grep -v "grep"`
if [ -n "$n" ];then 
echo "切流器进程存在"
echo "========================================="
else
echo "切流器进程不存在"
echo "请到目录/home/x00/Penglei/Switch执行 sh switch.sh命令"
echo "========================================="
fi


n=`ps -ef |grep SM_main|grep -v "grep"`
if [ -n "$n" ];then 
echo "SM进程存在"
echo "========================================="
else
echo "SM进程不存在"
echo "请到目录/home/x00/Penglei/SM执行sh smstart.sh命令"
echo "========================================="
fi

n=`ps -ef |grep VGW|grep -v "grep"`
if [ -n "$n" ];then 
echo "VGW进程存在"
echo "========================================="
else
echo "VGW进程不存在"
echo "请到目录/home/x00/Penglei/VGW/bin执行sh vgw-start.sh命令"
echo "========================================="
fi