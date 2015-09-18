#ifndef _SM_MANAGER_H_
#define _SM_MANAGER_H_


#include <stdio.h>
#include <stdlib.h>
#include "DBInterface.h"

#include "CommonData.h"
//#include "VGW_VodStream.h"
//#include "MSI_SMStream.h"
//#include "Advertisement.h"
//#include "Navigation.h"


class Advertisement_Stream;
class Advmul_stream;
class MSI_SM_Stream ;
class Navigation_Stream ;
class VGW_Vod_Stream;

class SM_Manager
{
public:

	SM_Manager();
	~SM_Manager();
	
	bool CleanAllStream();
	bool CleanAllTask();
	bool CleanStream(int iStreamID,ModuleType emodelType=Other);
	bool Cleanonenav(char *strSeesionID);
	bool Cleanonevod(char *strSeesionID);
	bool ClearoneStream(int iStreamID,int type);

	//下发导航路数及广告流路数
	bool InAdvanceStream(int iNavNum,int iAdverNum);

	bool CheckStreamStatus(ModuleType emodelType);
	bool ResouceRecovery(ModuleType emodelType);

	//装载流通道分组
	bool LoadStreamResource();
	bool LoadStreamStatus();
	bool LoadIPQAMInfo();
	bool LoadNetWorkGroup();
	bool LoadAdvinfo();
	bool LoadIPQAMRes();
	int  AddOneStream(ModuleType emodelType,int iOldStreamID,int *bAddNewStream=NULL);

	bool InitStream();
	bool Tellcums(int istreamid);

	int Bind_OneStream(int iStreamid,char *strUserID,char* strToken,int *iChannelInfo);
	
	int AddOneVodStream(int iStreamID,char* strUrl,int iFd,char *username,char *vodname,char *posterurl,char* strSerilno);
	int AddOneNavStream(int iStreamID);
	int AddOneAdvStream(int iStreamID);
	int AfterBindAddStream(int iStreamID,bool bnewstream);
	
	bool Userbehav(int iStreamID,char *iUsertype,char *strUserName,char *Commit,int result,char *date);
	bool VodStreamOver(int iStreamID,char *username);
	bool PauseVOD(int iStreamID,char *iUsername,char *vodname,char *posterurl,int temp=0);//处理暂停
	bool RecoverVodPlay(int iStreamID,char *username,char *vodname,char *posterurl);//恢复VOD点播
	
	bool GetTaskStatus();

	bool AddStream2GroupInfo(int iStreamID,int iType);// 1为增加，0为删除

	bool BindOverTime(int iStreamID);

	bool CleanOneAdvStream(int iStreamID);

	bool Bindagain(char *iUsername,int iiStreamid);

	bool Findcsaddr(char *csip,char *csport,int iStreamID);

	bool Checksession();

	bool Dealsession(int streamid);

	bool Unbind(char *usernam,char *streamid);

	bool Navstream(char *strSeesionId,char *strSID,char* strReSID,char *strUrl,char* strISIP,char* strSPort,\
					char* strAreaID,char *strSerialno,char *strMsg);

	//检测绑定超时线程
	static void *Check_BindOverTime_thread(void *arg);

	//Advertisement_Stream *m_Advertisement;
	Navigation_Stream 	 *m_Navigation;
	Advertisement_Stream *m_Advertiser;
	VGW_Vod_Stream 	*m_VGWVodPlayer;
	MSI_SM_Stream	*m_Msi_SMInter;
	DBInterfacer * m_cSM_useract;

	Advmul_stream *m_Advmulstream;

	MapStreamResource m_mapStreamResource; //流信息资源表
	MapIPQAMInfo	  m_mapIpqamInfo;  //iPQAM信息表
	MapNetWorkGroup   m_mapNetworkGroup; //网络分组表

	MapStreamStatus   m_mapStreamStatus;  //装载的流数据暂时为所有数据
	MapStreamGroup    m_mapStreamGroup; //流通道分组信息表

	MapBindOverTime  m_mapBindStatus; //绑定状态

	VectorNewNav      m_vecNewNav;  //补发导航

	SetPauseVod 	  m_SetPauseVod;  //暂停点播超时

	MapRegionID  	  m_mapRegionID; //vod 的映射端口

	MapCsaddr		 m_mapCsaddr;

	SetAdvinfo   m_mapadvinfo;

	int  m_idex;  //当前使用的id  
	SetUsedRegionID	  m_SetUsedRegionID; //正在使用的RegionID streamid regionport

	Smstrinfo  m_smstraddr; //用于存切流器的ip 和port
	Smvodaddr  m_smvodaddr; //用于存放vod流过切流器的地址
	
	StreamAbnormal m_strsta; //用于存放当前check是流状态
	
	int hdisfull;
	int sdisfull;
	
	pthread_mutex_t m_lockerRegion;

	pthread_mutex_t m_BindStatuslocker;
	pthread_mutex_t m_VecNewNavlocker;
private:
	//输出端口ip
	char m_strdstIP[128];
	int m_iport;
	char m_NavUrl[512];
	
	//序号
	char m_strSerialNo[128];
	//键值端口
	int m_keyPort;
	int m_s2qPort;

	int m_icurrentidex;

	ListStreamResource m_ispause;

	Navinfo m_navinfo;      //用于装载所有流的导航流标志
	Navnext hdm_navnext;   //用于装载即将下发的高清导航流的标志
	Navnext sdm_navnext;   //用于装载即将下发的标清导航流的标志
	FailNav hd_failnav;   //用于存放同一区域下未下发成功的高清导航流的个数
	FailNav sd_failnav;   //用于存放同一区域下未下发成功的标清导航流的个数
};

#endif
