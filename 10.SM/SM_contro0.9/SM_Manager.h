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



class Advertisement_Stream ;
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
	bool CleanTask(ModuleType emodelType);
	bool ClearoneStream(int iStreamID,int type);

	//下发导航路数及广告流路数
	bool InAdvanceStream(int iNavNum,int iAdverNum);

	bool CheckStreamStatus(ModuleType emodelType);
	bool ResouceRecovery(ModuleType emodelType);
/*	bool CheckVodStream();
	bool CheckNavgationStream();
	bool VODResouceRecovery();
	bool NavResouceRecovery();
*/
	//装载流通道分组
	bool LoadStreamResource();
	bool LoadStreamStatus();
	bool LoadIPQAMInfo();
	bool LoadNetWorkGroup();
	
	int  AddOneStream(ModuleType emodelType,int iOldStreamID,int *bAddNewStream=NULL);

	bool InitStream();
	bool Tellcums(char *username);
//	bool Tellcu(char *cip,char *port);

	int Bind_OneStream(int iStreamid,char *strUserID,char* strToken,int *iChannelInfo);
	
	int AddOneVodStream(int iStreamID,char* strUrl,int iFd,char *vodname,char *posterurl,char* strSerilno);
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

	bool Unbind(char *usernam,char *streamid);
	
	//bool ChangeStream_Status(int iStreamID,char *strType,char *bind_user)

	//检测绑定超时线程
	static void *Check_BindOverTime_thread(void *arg);

		//Advertisement_Stream *m_Advertisement;
	Navigation_Stream 	 *m_Navigation;
	Advertisement_Stream *m_Advertiser;
	VGW_Vod_Stream 	*m_VGWVodPlayer;
	MSI_SM_Stream	*m_Msi_SMInter;
	DBInterfacer *m_cSM_useract;

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
		
	int  m_idex;  //当前使用的id  
	SetUsedRegionID	  m_SetUsedRegionID; //正在使用的RegionID streamid regionport
	int isfull;
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
};

#endif
