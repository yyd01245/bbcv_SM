#ifndef SWITCH_STREAM_H_
#define SWITCH_STREAM_H_

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#include "CommonData.h"
#include "CommonFun.h"


typedef std::vector<SwitchInfo*> VectSwitchInfo;
typedef std::vector<struct sockaddr_in*> VectSendAddr;


class Switch_Stream
{
public:
	Switch_Stream(int iUsedPort,char* SW_ip);
	~Switch_Stream();

	int InitRecvStream(SwitchInfo *cSwitchInfo);  //接收端口 发送端口

	static void *Parse_recv_thread(void *arg);

	static void *ts_send_thread(void *arg);
	
	bool EndStream(SwitchInfo *cSwitchInfo);

	bool PutInBuff(char* cData,int ilen);

	bool GetBuff(char* cData,int *ilen);

	bool IsCanDelete();

	int CheckOneSwitch(SwitchInfo *cSwitchInfo);

	int SetCheckFlag(int iFlag);

	//vector 
	VectSwitchInfo m_vectSwitchInfo;
	VectSendAddr   m_vectSendAddr;

	pthread_mutex_t m_mutexlocker;

	pthread_t m_iRcvThreadID;
	int m_iCheckflag;

private:	

	char m_SW_ip[256];

	int m_iSendRate;
	int m_iRecvPort;
	int m_iSendPort;

	int m_BindSocket;

	int m_iBeginPort;

	SwitchInfo m_cSwitchInfo;

	bool m_bSwitchFlag;
	

	int buffer_max_size;

	char *buffer;

	int read_index;
	int write_index;

	pthread_t read_thread_id;
	pthread_t write_thread_id;
	pthread_mutex_t m_mutex;	

	unsigned long real_send_bytes;

};


#endif
