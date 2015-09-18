#ifndef SWITCH_CONTROL_H_
#define SWITCH_CONTROL_H_

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <net/if.h>
#include <sys/time.h>
#include <sys/select.h>


#include <sys/ioctl.h>
#include <iostream>

#include "cJSON.h"
#include "Switch_Stream.h"
#include "Switch_Manager.h"
#include "CommonData.h"
#include "CommonFun.h"
#include <string>
#include "PropConfig.h"
#include "Switch_worker_Sever.h"
#include <queue>


using namespace std;


typedef std::queue<int> QueSocketID;
	
class Switch_Control
{
public:
	Switch_Control();
	~Switch_Control();

	bool Init();
	bool	init(const char * ConfigFile);

	int GetHostIp(const char *name,char *addr);

	int  ParseMessage(char* strMesg,int iLen,int accept_fd);
	static void * TCP_Accept_Control_thread(void *arg);

	static void *ts_recv_Control_thread(void *arg);
	static void *Parse_recv_MSI_thread(void * arg);

	int FindSeverInfo(const char *strSeverInfo);

	int ConnectToSever(const char* phostIp,int iPort,int *pCurrent_num,int *pMax_num);

	int ResetAllSwitch();

	int SendResetMessage(const char* phostIp,int iPort);
	
	Switch_Manager*m_pSwitchManager;

	pthread_mutex_t m_lockerQuesocket;
	QueSocketID m_QueSokcet;

	int m_MsiacceptSocket;

private:

	string SW_version;
	//string area_id;
//	string video_type;
//	string vendor;
//	string ctype;
	string SW_ip;
	string SW_netname;
	string SW_listentport;
	string log_file_path;
	string log_file;
	int log_level; // log level [ERROR   = 1;WARN    = 2;INFO    = 3;DEBUG   = 4;TRACE   = 5; ]
	int log_size;
	int log_num;
	int iAliveTickInterval;
	int swm_process_thread;

	

//	string rsm_encodername;
//	string rsm_aim_listentport; //local sourcemag ip:port

	int m_iMsiPort;
};


#endif

