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

#include "cJSON.h"
#include "Switch_Stream.h"
#include "Switch_Manager.h"
#include "CommonData.h"
#include "CommonFun.h"



class Switch_Control
{
public:
	Switch_Control();
	~Switch_Control();

	bool Init();

	static void *ts_recv_Control_thread(void *arg);
	static void *Parse_recv_MSI_thread(void * arg);

	Switch_Manager*m_pSwitchManager;

	int m_MsiacceptSocket;

private:	

	int m_iMsiPort;
};


#endif

