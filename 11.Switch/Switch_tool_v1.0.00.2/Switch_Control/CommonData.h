#ifndef _COMMONDATA_H_
#define _COMMONDATA_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <map>
#include <vector>
#include <list>
#include <set>
#include <string>


//check status errono
const int Error_No = 0;
const int Error_NoSourceData = -1;
const int Error_NoSendData = -2;
const int Error_NoAll = -3;

const int Error_NO_Sever = -100;
const int Error_Connect_Sever = -101;
const int Error_Recv_Sever = -102;
const int Error_ReSet_Device = -103;
const int Error_NO_Stream = -104;




const int UDP_PORT_LOW = 30000;
const int UDP_PORT_HIGH = 40000;


typedef struct _SwitchServerInfo
{
    int  iServerID;
    char strServerIPAddr[256];
    int  iListenPort;
    int  iRunStatus;		 //运行状态异常濉>0    运行状态正常0
	int	 iStreamStatus;    //流状态异常1   流状态正常0
    int  iMaxSwitchNumber;
    int  iCurrentSwitchNumber;

}SwitchSeverInfo;


typedef struct _SwitchInfo
{
	int iSRCPort;
	char strSRCIP[128];

	int iDstPort;
	char strDstIP[128];

	int iCheckStatus;
}SwitchInfo;

typedef unsigned char uint8_t;

const int iAliveTickInterval = 5*1000; // 2 sec
const int iMSIServerPort = 20919;

const int iBeginPort = 14000;

#define MCAST_ADDR "225.0.0.1"     /*一个局部连接多播地址，路由器不进行转发*/



#endif
