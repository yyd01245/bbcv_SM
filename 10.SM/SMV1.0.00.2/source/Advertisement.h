#ifndef _ADVERTISEMENT_H_
#define _ADVERTISEMENT_H_

#include "Stream.h"
#include <stdio.h>
#include <stdlib.h>
#include "SM_Manager.h"


class Advertisement_Stream : public Stream
{
public:
	//程序启动之前组播工具已经发流，并设置好固定的地址
	Advertisement_Stream(SM_Manager *pManager);
	~Advertisement_Stream();

	bool CleanALLAdverStream();
	


	bool CleanAdverStream(char *strSeesionId,char *strTaskID,char *strSerialno);
	
	bool Connect_AdvServer();

	bool StartOneStream(char *strSeesionId,char  *strInputUrl,char* strOutputUrl,
					char *strSerialno,int temp=0);
	
	bool CheckStatus(int strSeesionId,char *strSerialno);

	static void *Parse_recv_Adver_thread(void *arg);

	bool Getaddr(char* strSeesionId,char *strSerialno);
private:
	//组播工具发出组播的地址如udp://:239.1.1.1:12345
	char m_MulticastIP[10][128];//10路组播流
	//ipqam的iP，Port
	char m_transmitIP[128];
	int m_iport;
	
	Streatype m_isvod;
		
	SM_Manager *m_pManager;

	Sockfd  connsocket;

};

#endif

