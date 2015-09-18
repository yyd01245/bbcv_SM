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



typedef struct _SwitchInfo
{
	int iSRCPort;
	char strSRCIP[128];
	
	int iDstPort;
	char strDstIP[128];

	int iCheckStatus;
}SwitchInfo;

typedef unsigned char uint8_t;


const int iMSIServerPort = 20919;

const int iBeginPort = 14000;

#define MCAST_ADDR "225.0.0.1"     /*一个局部连接多播地址，路由器不进行转发*/



#endif
