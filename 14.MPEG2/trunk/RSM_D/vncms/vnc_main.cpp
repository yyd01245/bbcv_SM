#include <signal.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <pwd.h>
#include <grp.h>

#include "Log.h"
#include "vnc_config.h"
#include "vnc_worker_rsm.h"
#include "vnc_worker_Xvnc.h"


int main(int argc ,char **argv)
{
	char config_file[500];
	char log_name[1024];
	char home[128];
	char user[64];
	string vgl_display;
	int index,type,pa_amount,uid;
	
	
  	signal(SIGINT, SIG_IGN);
	signal(SIGPIPE, SIG_IGN);
	signal(SIGHUP, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
	signal(SIGFPE, SIG_IGN);
	signal(SIGSEGV, SIG_IGN);
	signal(SIGTERM, SIG_IGN);
	//read config
	if (argc < 3 )
	{ 
		return -1;
	}
	
	if (access(VNCCONFFILE, R_OK) == 0)
	{
 		strcpy(config_file,VNCCONFFILE);
	}
	else 
	{
		printf("[ERROR] no config file can access!\n\n");
   		return -1;
   	}
	if(!Vnc_Config::instance()->init(config_file))
	{
		printf("[ERROR]  init config fault : %s\n\n", config_file);
		return 0;
	}

	struct passwd *passwd;
	
	index = atoi(argv[1]);
	type = atoi(argv[2]);
	if(3 < argc)
	vgl_display = argv[3];

	pa_amount = Vnc_Config::instance()->pa_amount;
	sprintf(user,"%s%02d",Vnc_Config::instance()->username.c_str(),((index-1)/pa_amount));
	passwd = getpwnam (user);
	uid = passwd->pw_uid;
	sprintf(home,"/home/%s",user);
    setenv("HOME",home,1);
	
//	free(passwd);
	printf("vncms start...: index = %d, type = %d vgl_display = %s \n\n", index, type,vgl_display.c_str());
	sprintf(log_name,"%s%d",Vnc_Config::instance()->log_file.c_str(),index);
	//start log
	LogFactory::instance()->init(Vnc_Config::instance()->log_file_path.c_str(),
   															log_name,
   															Vnc_Config::instance()->log_level,
   															LOGOUT_FILE,
   															5,//per 5 M
   															2);//10 files
   	LogFactory::instance()->start();
   	LOG_INFO("INFO  - [VNCMS]:开始启动\n");
	LOG_INFO("INFO  - [VNCMS]:读取VNCMS配置文件\n");
	LOG_INFO_FORMAT("INFO  - [VNCMS]:程序版本[VNCMS.version] = %s\n",Vnc_Config::instance()->version.c_str());
	LOG_INFO_FORMAT("INFO  - [VNCMS]:服务端口[VNCMS.tcpserver.port] = %d\n",(Vnc_Config::instance()->listenport+index));

	LOG_INFO_FORMAT("INFO - [VNCMS]: UID id %d ,HOME is %s\n",getuid(),getenv("HOME"));
	LOG_INFO_FORMAT("INFO - [VNCMS]: index = %d, type = %d vgl_display = %s \n\n", index, type,vgl_display.c_str());
		

	//start Xvnc and vncms
  	VNC_State::instance()->init_vnc(index, type, vgl_display);
	setuid(uid);
	VNC_State::instance()->start(4);
	Vnc_sngton::instance()->open((Vnc_Config::instance()->listenport+index),Vnc_Config::instance()->process_threads_aim);//listen to vncmc 
	LOG_INFO("INFO  - [VNCMS]:启动成功 ........\n");
	Vnc_sngton::instance()->wait();
	VNC_State::instance()->wait();
   	LOG_ERROR_FORMAT("ERROR - [VNCMS]: An crash has occurred on vnc-ms %d\n",index);
	sleep(2);
	return 0;
}



