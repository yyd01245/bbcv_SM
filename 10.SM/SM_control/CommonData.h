/*
	author yyd

*/


#ifndef _COMMONDATA_H_
#define _COMMONDATA_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <map>
#include <vector>
#include <list>
#include <semaphore.h>
#include <set>
#include <string>

//#define USEMAP


const char Status_Idle[32] ="A";  //空闲
const char Status_Adver[32] ="B"; //广告
const char Status_Nav[32] ="C";		//导航
const char Status_Nav_Home[32] ="D"; //导航首页
const char Status_Vod[32] ="E";	//vod
const char Status_abnormal[32] = "F";//异常
typedef enum _ModuleType
{
	Advertisement =0 ,
	Navigation    ,
	VodStream,
	MobileGateservice,
	Other
}ModuleType;

const int CIRegionID[3] = {50452,50450,50454};
const char CstrRegion[3][64]={"0x0601","0x0603","0x0602"};

const char strAdverIP[128] = "192.168.100.56";//"192.168.100.11";
const int iAdverPort = 20919;//18178;

const char strNavIP[128] = "192.168.100.11";
const int iNavPort = 15174;//15174;

const char strVodIP[128] = "192.168.100.11";
const int iVodPort = 8577;

const char strMsiIP[128] = "192.168.100.11";
const int iMsiPort = 18177;

const char strVOD_KeyIP[128] = "192.168.100.11:50732";

const int iMSIServerPort = 20909;
const char strMyServerIP[128]="192.168.70.240";


const int iMulitNum = 5;
const char strVLCMulitURL[5][128] = {"udp://225.0.0.1:12000","udp://225.0.0.1:12000","udp://225.0.0.1:12000",
					"udp://225.0.0.1:12000","udp://225.0.0.1:12000"};

const int iBindOverTime = 60; //sec

typedef struct _StreamStatus
{
	int istreamID;
	char strStreamType[32];
	char strStatus_date[128];
	char strBind_userID[128];
	char strBind_date[128];
	char strSwitch_task_id[128];
	char strSessionID[512];
	char strSerialNo[512];
	
	//int iTaskID;

}StreamStatus;

typedef struct _UserBehaviour
{

	int iID;
	int iStreamID;
	char strUserName[128];
	char strActionType[128];
	char strActionDate[128];
	char strNetworkComment[512];


}UserBehaviour;


typedef struct _NetworkGroup
{

	char strNetRegionNum[128];
	char strNetRegionName[128];
	int iNavgationStreamNum;
	int iAdvertisementStreamNum;
	char strNetworkComment[256];

}NetworkGroup;


typedef struct _StreamResource
{
	int iStreamID;
	int iOutPutPort;
	int iPidMaping;
	int iProgramNum;
	int iBitRate;
	int iIPQAMNum;
	char strNetRegionNum[128];
	int iFreqPoint;
	char strComment[256];
	char strNav_url[512];
	int iIs_Need_Key;
	char strMobile_url[512];
	int iBind_state;
	//char strSessionID[512];
	char strRegionID[64];
	int iChannel_info;
	int iRegionIdex;

}StreamResource;


typedef struct _IPQAMInfo
{
	int iIPQAMNum;
	char strIpqamName[128];
	char strIpqamIP[128];
	int iIpqamManagerPort;
	char strIpqamType[128];
	char strIpqamModel[128];
	char strIpqamManufacturers[128];
	char strIpqamComment[256];
	int iIsSupportR6;

}IPQAMInfo;

typedef std::map<int,StreamResource*> MapStreamResource;
typedef std::map<int,StreamStatus*> MapStreamStatus;
typedef std::map<int,IPQAMInfo*>  MapIPQAMInfo;
typedef std::map<char*,NetworkGroup*> MapNetWorkGroup;


typedef std::list<int> ListStreamResource;
typedef std::map< char*, ListStreamResource> MapStreamGroup;
typedef std::set<int> VectorNewNav;  

typedef std::set<int> SetGroupStream;  //一组的通道

typedef std::map<int,time_t> MapBindOverTime;//存放绑定状态

typedef std::set<int>  SetPauseVod;

typedef std::map<int,char*> MapRegionID;
typedef std::map<int,int> SetUsedRegionID;


#endif
