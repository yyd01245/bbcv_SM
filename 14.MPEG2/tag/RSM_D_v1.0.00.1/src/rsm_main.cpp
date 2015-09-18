#include "rsm_config.h"
#include "res_register.h"
//#include "res_check.h"
#include "res_store.h"
//#include "res_send.h"
#include "Log.h"
#include "rsm_worker_down.h"
#include "rsm_worker_resource.h"
#include "rsm_worker_up.h"
#include "rsm_worker_vncms.h"
#include "env_check.h"
#include "rsm_checker_server.h"

#include <signal.h>
#include <sys/wait.h>
#include <iostream>
#include <sys/types.h>



static void handler(int num)
{
	int status;
    int index;
	int pid = waitpid(-1,&status,WNOHANG);
/*	index = VNC_State::instance()->get_vncms_index(pid);
	if(index)
		VNC_State::instance()->vncms_restart(index);
*/
/*	if(WIFEXITED(status))
	{
		if(WEXITSTATUS(status) != 0)
		{
			//child exit unormally, farther process should exit
			printf("The child %d exit with code %d\n", pid, WEXITSTATUS(status));
			printf("RSM should be restart!");
			exit(-1);
		}
	}*/
}
static void m_exit(int num)
{
	sleep(10);
	printf("22\n");
	exit(-1);
}

int main(int argc ,char **argv)
{
	// ignore some singnals
		
	signal(SIGINT, SIG_IGN);
	signal(SIGPIPE, SIG_IGN);
	signal(SIGHUP, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
	signal(SIGFPE, SIG_IGN);
	signal(SIGSEGV, SIG_IGN);
	signal(SIGCHLD,handler);
	signal(SIGTERM,m_exit);

	char config_file[500];
	char Xvnc_amount[8];
	int ret;
   
	if(argc>1 && access(argv[1], R_OK) == 0)
	{
		strcpy(config_file,argv[1]);
	}
	else if (access("resourcemag.config", R_OK) == 0)
	{
		strcpy(config_file,"resourcemag.config");
	}
	else 
	{
		cout<<"no config file can access"<<endl;
		return -1;
	}
	//demon
   	if(fork()) return 0;
	//load config
	if(!Rsm_Config::instance()->init(config_file))
	{
		cout<<" init config fault : "<<config_file<<endl;
		return 0;
	}
	//open log

/*	if(EnvCheck::match_key(Rsm_Config::instance()->rsm_netname.c_str()))
	{
		cout<<"kyKey incorrect"<<endl;
		return 0;
	}
*/	LogFactory::instance()->init(Rsm_Config::instance()->log_file_path.c_str(),
   					Rsm_Config::instance()->log_file.c_str(),
   					Rsm_Config::instance()->log_level,
   					LOGOUT_FILE,/*|LOGOUT_SCREEN,*/
   					Rsm_Config::instance()->log_size,//per 256 M
   					Rsm_Config::instance()->log_num);//10 files
	LogFactory::instance()->start();

	system("./PluKill &");

	//init setting 
	LOG_INFO("INFO  - [RSM]:开始启动\n");
	LOG_INFO("INFO  - [RSM]:读取RSM配置文件\n");
	LOG_INFO_FORMAT("INFO  - [RSM]:程序版本[RSM.version] = %s\n",Rsm_Config::instance()->rsm_version.c_str());
	LOG_INFO_FORMAT("INFO  - [RSM]:服务端口[RSM.tcpserver.port] = %s\n",Rsm_Config::instance()->rsm_aim_listentport.c_str());

	//init vnc
	VNC_State::instance()->vnc_init(atoi(Rsm_Config::instance()->num_hd.c_str()),atoi(Rsm_Config::instance()->num_sd.c_str())); 
	

	VNC_Check::instance()->start(1);

	//check weather all bvbcs are inited or not
	while(atoi(Rsm_Config::instance()->num_hd.c_str()) != atoi(Xvnc_amount))
	{
		LOG_INFO_FORMAT("INFO  - [RSM]: %d bvbcs is ready, please wait for 30s to startup all bvbcs \n",atoi(Xvnc_amount));
		sleep(20);
		memset(Xvnc_amount,0,8);
		ret = EnvCheck::MyPopen(Xvnc_amount,8,"ps -ef|grep bvbcs |grep -v grep|wc -l");
		if(ret <0)
		{
			printf("wo cao  ju ran %d\n",ret);
			exit (0);
		}
	}
	
	LOG_INFO("INFO  - [RSM]: all bvbcs is ready\n");
	LOG_INFO("INFO  - [RSM]: rsm is loading res_store........\n");

	VNC_State::instance()->pre_open_init();
	
	RES_oprate::instance()->start(1);

	/*Res_Store  res_store;
	res_store.setName("store");
	res_store.start();
	*/
	LOG_INFO("INFO  - [RSM]: rsm is loading res_register ........\n"); 

	RES_register::instance()->start();

//	Res_Checker  res_checker;
//	res_register.setName("res_checker");
//	res_checker.start();
//	LOG_INFO("INFO  - [RSM]: rsm is loading res_check ........\n");
//	Res_Send  res_sender;
//	res_sender.setName("sender_vncw");
//	res_sender.start();
	LOG_INFO("INFO  - [RSM]: rsm is loading rsm_worker_down ........\n");
	Rsm_Worker_Down worker_down;
	worker_down.setName("worker_aim");
	if(!worker_down.open(atoi(Rsm_Config::instance()->rsm_aim_listentport.c_str()),
   									Rsm_Config::instance()->rsm_aim_process_thread))
	return -1;
   
	LOG_INFO("INFO  - [RSM]: rsm is loading rsm_worker_up ........\n");
	Rsm_Worker_Up worker_up;
	worker_up.setName("worker_vnc");
	cout<<"lis"<<Rsm_Config::instance()->rsm_vnc_listentport<<endl;
	if(!worker_up.open(Rsm_Config::instance()->rsm_vnc_listentport,
   									Rsm_Config::instance()->rsm_vnc_process_thread))
	return -1;

	LOG_INFO("INFO  - [RSM]: 启动成功 ........\n");
	//res_store.wait();
	server_checker::instance()->start();
	server_checker::instance()->wait();
	RES_register::instance()->wait();
	//res_checker.wait();
	worker_down.wait();
	worker_up.wait();
	RES_oprate::instance()->wait();
	LogFactory::instance()->wait();
	return 0;

	
}



