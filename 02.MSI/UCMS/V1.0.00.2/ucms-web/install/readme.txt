1.解压.zip压缩包，并确保已经配置好了jdk环境变量
2.进入解压后的目录修改bin/*.sh文件中的tomcat启动脚本为msi-tomcat的绝对路径
3.进入解压后的目录修改 msi-tomcat/conf/server.xml如下部分docBase路径为当前msi-web所在路径
	<Context docBase="/root/pengs/msi/msi-web" path="">
	</Context>
	端口如有冲突请修改tomcat端口
4.根据msi-web\WEB-INF\classes\config.properties文件中的说明修改配置文件
5.根据日志输出要求修改msi-web\WEB-INF\classes\log4j.properties
6.进入解压后的目录bin，启动工程