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
using namespace std;
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
	Other,
	vodplay
}ModuleType;

 const int CIRegionID[3] = {50452,50450,50454};
 const char CstrRegion[3][64]={"0x0601","0x0603","0x0602"};
 const int Baseport = 52100;
 
 extern char strAdverIP[128];
 extern int iAdverPort;

 extern char strNavIP[128];
 extern int iNavPort;

 extern char strVodIP[128];
 extern int iVodPort;

 extern char strMsiIP[128];
 extern int iMsiPort;

 extern char strMyServerIP[128];
 extern int iMSIServerPort;

 extern char strVOD_KeyIP[128];

 extern char dbip[128];
 extern char dbuser[128];
 extern char dbpass[128];
 extern char dbname[128];

 extern char hdadvname[128];
 extern char hdadvip[128];
 extern char hdadvport[64];

 extern char advip[128];
 extern int muladvport;

 extern char quiturl[256];
 extern char pauseurl[256];

 extern char navgoback[256];

 extern char strsdNavIP[128];
 extern int isdNavPort;

 extern char hdrate[64];
 extern char sdrate[64];

 extern char sdadvname[128];
 extern char sdadvip[128];
 extern char sdadvport[64];

extern char sdpauseurl[256];
extern char sdquiturl[256];
extern char sdnavgoback[256];

extern int iwaittime;

extern int VOD_play_clean;
extern int VodStreamOver_clean; 
extern int BindOverTime_clean;
extern int RecoverVodPlay_clean;
extern int InitStream_clean;
extern int PauseVOD_clean; 

extern int frequency;
extern int pid;

extern int Istype;
extern int advflag;

extern char strblanIP[64];
extern int iBlanport;

extern int iBaseport;

extern int Recovery;

extern int iMContrlport;

extern int checkflag;


const int iMulitNum = 5;
const char strVLCMulitURL[5][128] = {"udp://225.0.0.1:10000","udp://225.0.0.1:12000","udp://225.0.0.1:12000",
					"udp://225.0.0.1:10000","udp://225.0.0.1:12000"};

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
	int result;

}UserBehaviour;

typedef struct _NetworkGroup
{

	char strNetRegionNum[128];
	char strNetRegionName[128];
	int hdiNavgationStreamNum;
	int sdiNavgationStreamNum;
	int hdiAdvertisementStreamNum;
	int sdiAdvertisementStreamNum;
	char strNetworkComment[256];

}NetworkGroup;


typedef struct _StreamResource
{
	int iStreamID;
	int iIPQAMNum;
	char strNetRegionNum[64];
	char strNav_url[512];
	int iChannel_info;
	int iWherether_HD;
}StreamResource;

typedef struct _IPQAMInfo
{
	int iIPQAMNum;
	char strIpqamIP[128];
	int iIpqamManagerPort;
	
}IPQAMInfo;


typedef struct _Csaddr
{
	char csip[128];
	char csport[64];
}Csaddr;

typedef struct _Advinfo
{
	char advip[128];
	char advport[64];
}Advinfo;

typedef struct _Smstream
{
	char sadvip[64];
	char sadvport[64];
}Smstream;

typedef struct _SmNavurl
{
	int id;
	int times;
	int haflag;
}SmNavurl;

typedef std::list<SmNavurl*> ListStreamNavurl;

typedef std::map<int,StreamResource*> MapStreamResource;
typedef std::map<int,StreamStatus*> MapStreamStatus;
typedef std::map<int,IPQAMInfo*>  MapIPQAMInfo;
typedef std::map<char*,NetworkGroup*> MapNetWorkGroup;

typedef std::map<int,Csaddr*> MapCsaddr;

typedef std::list<int> ListStreamResource;
typedef std::map< char*, ListStreamResource> MapStreamGroup;
typedef std::set<int> VectorNewNav;  
typedef std::list<int> Sockfd;

typedef std::set<int> SetGroupStream;  //一组的通道

typedef std::map<int,time_t> MapBindOverTime;//存放绑定状态

typedef std::set<int>  SetPauseVod;

typedef std::map<int,char*> MapRegionID;
typedef std::map<int,int> SetUsedRegionID;

typedef std::map<int,Advinfo*> SetAdvinfo; //装载广告表的信息

typedef std::map<int,int> Navinfo;   //装载当前流对应的导航流标记

typedef std::map<int,ListStreamResource> Navnext;    //装载同一区域下即将下发的导航流的标记

typedef std::list<int> Streatype; //即将生成流的状态

typedef std::list<Smstream*> Smstrinfo;//用于记录当前切流器的ip和port

typedef std::map<int,Smstream*> Smvodaddr;//用于记录VOD流过切流器的地址

typedef std::map<int,int> StreamAbnormal;//用于存放check流过程中流的状态

typedef std::map<int,int> FailNav;//用于存同一区域下未下发成功的导航流的个数

typedef std::set<int> Setstrnavflag;//用于存放导航标志的临时缓存

typedef std::map<int,ListStreamNavurl> RegNavurl;//用于存放同一区域下导航流地址被重用的情况
#endif
