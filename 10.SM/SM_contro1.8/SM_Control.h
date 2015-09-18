#ifndef _SM_CONTROL_H_
#define _SM_CONTROL_H_

//接收各个模块的指令，并解析 成对应的功能实现
#include "SM_Manager.h"
//#include "Advertisement.h"
#include "DBInterface.h"
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>



class SM_Control
{
public:

	SM_Control();
	~SM_Control();
	
	//接收数据端口
	static void *Parse_recv_Nav_thread(void *arg);
	static void *ts_recv_Nav_thread(void *arg);

	static void *Parse_recv_Adver_thread(void *arg);
	static void *ts_recv_Adver_thread(void *arg);

	static void *Parse_recv_Vod_thread(void *arg);
	static void *ts_recv_Vod_thread(void *arg);

	static void *Parse_recv_MSI_thread(void *arg);
	static void *ts_recv_MSI_thread(void *arg);

	static void * Parse_recv_clear_thread(void *arg);
	static void * ts_recv_Clear_thread(void *arg);

	static void *ts_recv_useract_thread(void *arg);

	static void *ts_checksession_thread(void *arg);
	DBInterfacer *m_cSM_useract;
	SM_Manager *m_cSM_Manager;
	int m_NavacceptSocket;
	int m_AdvacceptSocket;
	int m_VodacceptSocket;
	int m_ClearstremSocket;
	int m_Useractsocket;
	private:

	int m_iNavPort;
	int m_iAdverPort;
	int m_iVodPort;
	int m_iMsiPort;
	int m_iCleanPort;
	int m_iUseractport;
	//输出端口ip
	char m_strdstIP[128];
	int m_iport;
	char m_NavUrl[512];

	//序号
	char m_strSerialNo[128];
	char m_strSeesionID[128];
	//键值端口
	int m_keyPort;
	int m_s2qPort;
	Sockfd m_MsiacceptSocket;
};

#endif

