#include "SM_Manager.h"
#include "VGW_VodStream.h"
#include "MSI_SMStream.h"
#include "Advertisement.h"
#include "Navigation.h"
#include "initadvstream.h"
#include <string.h>

#include "DBInterface.h"

//#define TEST_DATA
#define NEWFINDWAY
#define MAPREGION

SM_Manager::SM_Manager()
{
   	m_Navigation = NULL;
   	m_Advertiser = NULL;
   	m_VGWVodPlayer = NULL;
   	m_Msi_SMInter = NULL;
	
	//hdisfull = 0;
	//sdisfull = 0;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---create Manager\n",__FUNCTION__,__LINE__);
	pthread_mutex_init(&m_BindStatuslocker, NULL);
	pthread_mutex_init(&m_VecNewNavlocker, NULL);
	pthread_mutex_init(&m_lockerRegion, NULL);

	char *strRegion1 = (char*)malloc(64);
	strcpy(strRegion1,"0x0601");
	char *strRegion2 = (char*)malloc(64);
	strcpy(strRegion2,"0x0603");
	char *strRegion3 = (char*)malloc(64);
	strcpy(strRegion3,"0x0602");

	for(int i=0;i<sizeof(CIRegionID);++i)
	{
		char strregionid[64] ={0};
		int iport = CIRegionID[i];
		if(i==0)
			m_mapRegionID.insert(MapRegionID::value_type(iport,strRegion1));
		else if (i==1)
			m_mapRegionID.insert(MapRegionID::value_type(iport,strRegion2));
		else if (i==2)
			m_mapRegionID.insert(MapRegionID::value_type(iport,strRegion3));
	}
	MapRegionID::iterator it = m_mapRegionID.begin();
	while(it != m_mapRegionID.end())
	{
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --region %s , port %d\n",__FUNCTION__,__LINE__,it->second,it->first);
		++it;
	}
	//清理完会话需要将对应数据状体修改
}
SM_Manager::~SM_Manager()
{

	if(m_Navigation)
		delete m_Navigation;

	//释放map set中的资源
	pthread_mutex_destroy(&m_BindStatuslocker);
	pthread_mutex_destroy(&m_VecNewNavlocker);
}


bool SM_Manager::CleanAllStream() //char * strSeesionId,char * strSerialno
{
	MapStreamStatus::iterator it = m_mapStreamStatus.begin();
	while(it != m_mapStreamStatus.end())
	{
		//清理导航流
		char strSID[128] = "1001";
		char strReSID[128] = "1001";
		char strAuthiName[128] = "cscs";
		char strAuthcode[128] = "123456";
		char strMsg[128] = "";
		int iTaskID = 111;


		StreamStatus* pTmp = it->second;
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->istreamID);

		//先清理移动网关
		//if(strcmp(pTmp->strBind_userID, "") != 0)
			m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strSeesionID); //seesionID作为serialno
			usleep(1000*200);
		
		//根据数据类型进行不同模块的清理
			m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
			usleep(1000*100);

			m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,strSeesionID,strMsg);
		//Cleanonenav(strSeesionID);
			usleep(1000*200);
			
		// else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )	
			m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
		//Cleanonevod(strSeesionID);
			usleep(1000*200);
		it++;
	}
	return true;
}
bool SM_Manager::Cleanonenav(char *strSeesionID)
{
	char strSID[128] = "1001";
	char strReSID[128] = "1001";
	char strAuthiName[128] = "cscs";
	char strAuthcode[128] = "123456";
	char strMsg[128] = "";
		
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strSeesionID,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;
		m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		usleep(1000*100);
		
		m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
		usleep(1000*100);	
	}
	return true;
}
bool SM_Manager::Cleanonevod(char *strSeesionID)
{
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strSeesionID,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;
		m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		usleep(1000*100);
		m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		usleep(1000*100);
	}
	return true;
}

bool SM_Manager::CleanStream(int iStreamID,ModuleType emodelType)
{
//只清理广告导航vod
	bool bNeedWait = false;

	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;		
		//首先清理掉上一路
		//清理导航流
		
		char strSID[128] = "1001";
		char strReSID[128] = "1001";
		char strAuthiName[128] = "cscs";
		char strAuthcode[128] = "123456";
		char strMsg[128] = "";
		int iTaskID = 111;
		
	 	if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
		{
				//vod重复点播
				bNeedWait = true;
		}
		if(emodelType == Other)
		{
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);

			m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		//	usleep(1000*100);
			
			m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
		//	usleep(1000*100);	
			
			m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
			usleep(1000*300);
			
		}
		else if(emodelType == vodplay)
		{
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);

			m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		//	usleep(1000*100);
			
			m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
		//	usleep(1000*100);	
			
			m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
			usleep(1000*100);
		}
		else
		{
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);

				//根据数据类型进行不同模块的清理
			if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
			{
					m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
					usleep(1000*100);
			}
			else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 || strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
			{
				//m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
				Cleanonenav(strSeesionID);
				usleep(1000*100);
			}
			else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
			{
				Cleanonevod(strSeesionID);
				usleep(1000*100);
			}
		}
	}
	return bNeedWait;
}


bool SM_Manager::ClearoneStream(int iStreamID,int type)
{
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to clean stream %d\n",__FUNCTION__,__LINE__,iStreamID);
	char streamid[64] = {0};
	char status[32] = {0};
	sprintf(streamid,"%d",iStreamID);
	StreamStatus pTmpResource;
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",streamid,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;	
		memcpy(status,pTmp->strStreamType,strlen(pTmp->strStreamType)+1);
		m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,streamid);		
	}
	CleanStream(iStreamID);
	usleep(1000*1800);
	if(strcmp(status,Status_Idle)!=0)
	{
		if(strcmp(status,Status_Nav)==0)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---after clean to add a Nav stream\n",__FUNCTION__,__LINE__);
			AddOneNavStream(iStreamID,2);
		}
		else
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---after clean to add a  Adver stream\n",__FUNCTION__,__LINE__);
			AddOneAdvStream(iStreamID);
		}
	}	
	return true;
}

//下发导航路数及广告流路数
bool SM_Manager::InAdvanceStream(int iNavNum,int iAdverNum)
{

	return true;

}

bool SM_Manager::CheckStreamStatus(ModuleType emodelType)
{
	return true;

}
bool SM_Manager::ResouceRecovery(ModuleType emodelType)
{
	return true;

}

//装载流通道分组
bool SM_Manager::LoadStreamResource()
{
	m_mapStreamResource.clear();
	DBInterfacer::GetInstance()->LoadAllStreamResource(m_mapStreamResource);	
	return true;
}

//载入网络分组数据
bool SM_Manager::LoadNetWorkGroup()
{
	DBInterfacer::GetInstance()->LoadNetWorkGroup(m_mapNetworkGroup);

	//根据分组信息载入分组数据
	MapNetWorkGroup::iterator iter = m_mapNetworkGroup.begin();

	while(iter != m_mapNetworkGroup.end())
	{
		//查找每一个分组的数据，导出vector
		ListStreamResource tmplistGroupStream;
		//tmplistGroupStream.clear();
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d get group info %s\n",__FUNCTION__,__LINE__,iter->first);
		DBInterfacer::GetInstance()->LoadOneGroupStreamResource(iter->first,tmplistGroupStream);
		if(tmplistGroupStream.size() > 0)
		{
			m_mapStreamGroup.insert(MapStreamGroup::value_type(iter->first,tmplistGroupStream));
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d group %s  no data\n",__FUNCTION__,__LINE__,iter->first);
		}
		//首先把不同区域下面未下发成功的导航流个数初始化为0
		int strnum = atoi(iter->first);
		hd_failnav.insert(FailNav::value_type(strnum,0));
		sd_failnav.insert(FailNav::value_type(strnum,0));	

		++iter;
	}	
	return true;
}


bool SM_Manager::LoadStreamStatus()
{
	m_mapStreamStatus.clear();
	DBInterfacer::GetInstance()->LoadAllStreamStatus(m_mapStreamStatus);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---map size =%d\n",__FUNCTION__,__LINE__,m_mapStreamStatus.size());
	return true;
}

bool SM_Manager::LoadIPQAMInfo()
{
	m_mapIpqamInfo.clear();
	MapStreamResource::iterator strit = m_mapStreamResource.begin();
	while(strit != m_mapStreamResource.end())
	{
		StreamResource * temp = strit->second;
		DBInterfacer::GetInstance()->LoadALLIPQAMInfo(m_mapIpqamInfo,temp->iIPQAMNum,temp->iStreamID);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d 55555 temp->iStreamID = %d\n",__FUNCTION__,__LINE__,temp->iStreamID);
		++strit;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d 66666\n",__FUNCTION__,__LINE__);
	return true;
}

bool SM_Manager::LoadIPQAMRes()
{
	MapStreamResource::iterator strit = m_mapStreamResource.begin();
	while(strit != m_mapStreamResource.end())
	{
		StreamResource * temp = strit->second;
		int ipqamport = 0;
		DBInterfacer::GetInstance()->LoadALLIPQAMRes(temp->iIPQAMNum,&ipqamport,temp->iStreamID);
		MapIPQAMInfo::iterator myit =  m_mapIpqamInfo.find(temp->iStreamID);
		if(myit != m_mapIpqamInfo.end())
		{
			IPQAMInfo *mytemp = myit->second;
			mytemp->iIpqamManagerPort = ipqamport;
		}
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d 6666 temp->iStreamID = %d,port = %d\n",__FUNCTION__,__LINE__,temp->iStreamID,ipqamport);
		++strit;
	}
}

bool SM_Manager::LoadAdvinfo()
{
	m_mapadvinfo.clear();
	DBInterfacer::GetInstance()->LoadAlladvinfo(m_mapadvinfo);
}

int SM_Manager::AddOneStream(ModuleType emodelType,int iOldStreamID,int *bAddNewStream)
{
	int strnetWork_Reg = 0;
	int iNewStreamID = -1;
	int iLastTimeStreamID = -1;

	bool bInStatus = false;
	char strStreamType[64]={0};
	char TmpTime[64]={0};
	bool bNewStream = false;
	int whetherhd = 0;
	strcpy(TmpTime,"9976-10-13-3:31:18");
	//将新的绑定的导航流去掉
	//将新的导航流加上
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d BEGIN TO LOCK\n",__FUNCTION__,__LINE__);
   pthread_mutex_lock(&m_BindStatuslocker);	
	if(iOldStreamID > 0)
	{	//补发
		//先找到资源信息
		char strkey_value[64] = {0};
		sprintf(strkey_value,"%d",iOldStreamID);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* pTmp = &pTmpResource;
			whetherhd = pTmp->iWherether_HD;
			//memcpy(strnetWork_Reg,pTmp->strNetRegionNum,strlen(pTmp->strNetRegionNum)+1);
			strnetWork_Reg = atoi(pTmp->strNetRegionNum);
			//select st *,
			//从分组表中找到
			//资源表netnum,相同的所有数据，
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d hd or sd this is find in %d\n",__FUNCTION__,__LINE__,whetherhd);
			//把即将下发的导航流的类型存入队列中
			Navinfo::iterator itnav = m_navinfo.find(iOldStreamID);
			if(itnav != m_navinfo.end())	
			{
				if(whetherhd == 0)
				{
					Navnext::iterator hdnit = hdm_navnext.find(strnetWork_Reg);
					if(hdnit == hdm_navnext.end())
					{
						ListStreamResource hdnavlist;
						hdnavlist.push_back(itnav->second);
						hdm_navnext.insert(Navnext::value_type(strnetWork_Reg,hdnavlist));
					}
					else
					{
						ListStreamResource ittemp = hdnit->second;
						ittemp.push_back(itnav->second);
					}
				}
				else if(whetherhd == 1)
				{
					Navnext::iterator sdnit = sdm_navnext.find(strnetWork_Reg);
					if(sdnit == sdm_navnext.end())
					{
						ListStreamResource sdnavlist;
						sdnavlist.push_back(itnav->second);
						sdm_navnext.insert(Navnext::value_type(strnetWork_Reg,sdnavlist));
					}
					else
					{	
						ListStreamResource iittemp = sdnit->second;
						iittemp.push_back(itnav->second);
					}
				}
				itnav->second = 0;
			}
			//统一向前查找
			SetGroupStream listTmp;
			listTmp.clear();
			//查找具有相同高(标)清且相同区域号的流的集合
			iret = DBInterfacer::GetInstance()->FindSameGroupStream(pTmp->strNetRegionNum,whetherhd,listTmp);

			SetGroupStream::iterator iterList0 = listTmp.find(iOldStreamID);
			SetGroupStream::iterator  iterflag = iterList0;
			++iterList0;
			bool bhasAdv = false;
			
			while(iterList0 != listTmp.end())
			{
				int iStreamID = *iterList0;
				StreamStatus pTmpstatus;
				char strkey_value1[64] = {0};
				sprintf(strkey_value1,"%d",iStreamID);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----stream id = %d\n",__FUNCTION__,__LINE__,iStreamID);
				memset(&pTmpstatus,0,sizeof(pTmpstatus));
				iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpstatus);
				if(iret)
				{
					StreamStatus *ptmpstat = &pTmpstatus;
					if((strcmp(ptmpstat->strStreamType,Status_Idle)==0)&&(strcmp(ptmpstat->strBind_userID,"")==0))
					{
						iNewStreamID = iStreamID;
						break;
					}
					else if(!bhasAdv && strcmp(ptmpstat->strStreamType,Status_Adver)==0&&strcmp(ptmpstat->strBind_userID,"")==0)
					{
						iNewStreamID = iStreamID;
						bhasAdv = true;
					}
					else if(!(strcmp(ptmpstat->strStreamType,Status_Vod)==0 || 
						strcmp(ptmpstat->strStreamType,Status_Nav_Home)==0||strcmp(ptmpstat->strStreamType,Status_Nav)==0))
					{
						//最远使用的流
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d before time comper-----status date %s\n",__FUNCTION__,__LINE__,ptmpstat->strStatus_date);
						if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
						{
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----status date %s\n",__FUNCTION__,__LINE__,ptmpstat->strStatus_date);
							strcpy(TmpTime,ptmpstat->strStatus_date);
							iLastTimeStreamID = iStreamID;
							strcpy(strStreamType,ptmpstat->strStreamType);
						}		
					}
				}
				else
				{
				//状态表中没有该流通道，说明空闲流
					iNewStreamID = iStreamID;
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---stream %d is unuse\n",__FUNCTION__,__LINE__,iNewStreamID);
					bNewStream = true;
					break;
				}
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ++++++++++list data %d\n",__FUNCTION__,__LINE__,*iterList0);
				++iterList0;
			}
			if(iterList0 == listTmp.end())
			{
				iterList0 = listTmp.begin();
				while(iterList0 != iterflag)
				{	
					int iStreamID = *iterList0;
					//
					StreamStatus pTmpstatus;
					char strkey_value1[64] = {0};
					sprintf(strkey_value1,"%d",iStreamID);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----this stream id = %d\n",__FUNCTION__,__LINE__,iStreamID);
					memset(&pTmpstatus,0,sizeof(pTmpstatus));
					iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpstatus);
					if(iret)
					{
						StreamStatus *ptmpstat = &pTmpstatus;
						if((strcmp(ptmpstat->strStreamType,Status_Idle)==0)&&(strcmp(ptmpstat->strBind_userID,"")==0))
						{
							iNewStreamID = iStreamID;
							break;
						}
						else if(!bhasAdv && strcmp(ptmpstat->strStreamType,Status_Adver)==0 && strcmp(ptmpstat->strBind_userID,"")==0)
						{
							iNewStreamID = iStreamID;
							bhasAdv = true;
						}
						else if(!(strcmp(ptmpstat->strStreamType,Status_Vod)==0 ||
								strcmp(ptmpstat->strStreamType,Status_Nav_Home)==0||strcmp(ptmpstat->strStreamType,Status_Nav)==0))
						{
							//最远使用的流
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d before time comper-----status date %s\n",__FUNCTION__,__LINE__,ptmpstat->strStatus_date);
							if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
							{
								strcpy(TmpTime,ptmpstat->strStatus_date);
								iLastTimeStreamID = iStreamID;
								strcpy(strStreamType,ptmpstat->strStreamType);
								LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d time comper-----status date %s\n",__FUNCTION__,__LINE__,ptmpstat->strStatus_date);
							}		
						}
					}
					
					else
					{
						//状态表中没有该流通道，说明空闲流
						iNewStreamID = iStreamID;
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---stream %d is unuse\n",__FUNCTION__,__LINE__,iNewStreamID);
						bNewStream = true;
						break;
					}
					
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ++++++++++list data %d\n",__FUNCTION__,__LINE__,*iterList0);
					++iterList0;
				}
			}
			if(-1 == iNewStreamID && iLastTimeStreamID != -1)
			{
				//使用最远时间的流，包括
				iNewStreamID = iLastTimeStreamID;
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----use last time stream\n",__FUNCTION__,__LINE__);
			}
		}
	}

	if(iNewStreamID == -1)
	{
		if(!whetherhd)
		{
			//将该区域下高清未下发成功导航流的数目加1
	
			FailNav::iterator hdit = hd_failnav.find(strnetWork_Reg);
			if(hdit != hd_failnav.end())
			{
				hdit->second = hdit->second + 1;
				LOG_INFO_FORMAT("INFO  - [SM]:%s %d hd_failnav = %d,region_num = %d\n",__FUNCTION__,__LINE__,hdit->second,strnetWork_Reg);
			}
		}
		else
		{
			//将该区域下标清未下发成功导航流的数目加1
			FailNav::iterator sdit = sd_failnav.find(strnetWork_Reg);
			if(sdit != sd_failnav.end())
			{
				sdit->second = sdit->second + 1;
				LOG_INFO_FORMAT("INFO  - [SM]:%s %d sd_failnav = %d,region_num = %d\n",__FUNCTION__,__LINE__,sdit->second,strnetWork_Reg);
			}
		}
        pthread_mutex_unlock(&m_BindStatuslocker);
		
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---the last status Time = %s find stream id=%d\n",__FUNCTION__,__LINE__,TmpTime,iNewStreamID);
	if(bAddNewStream != NULL)
	{	
		*bAddNewStream = bNewStream;
	}
	//bNewStream = true;

	//清理掉当前流
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iNewStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ==== iret =%d\n",__FUNCTION__,__LINE__,iret);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;							
		//首先清理掉上一路
		//清理导航流
		char strSID[128] = "1001";
		char strReSID[128] = "1001";
		char strAuthiName[128] = "cscs";
		char strAuthcode[128] = "123456";
		char strMsg[128] = "";
		int iTaskID = 111;

/*	
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----find new streamid %d\n",__FUNCTION__,__LINE__,iNewStreamID);

		pthread_mutex_lock(&m_VecNewNavlocker);								
			m_vecNewNav.insert(iNewStreamID);
		pthread_mutex_unlock(&m_VecNewNavlocker);	

		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----set size=%d\n",__FUNCTION__,__LINE__,m_vecNewNav.size());
	
 */	
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->istreamID);


		//先清理绑定关系
		if(strcmp(pTmp->strBind_userID, "") != 0)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----m_Msi_SMInter->CleanMSIStream\n",__FUNCTION__,__LINE__);
			m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strSeesionID); //seesionID作为serialno
			usleep(1000*100);
		}
		
		//根据数据类型进行不同模块的清理
		
		m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		
		m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
		
		m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
		usleep(1000*1500);
	}
	
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---new steam\n",__FUNCTION__,__LINE__);

	AddOneNavStream(iNewStreamID,whetherhd);
	pthread_mutex_unlock(&m_BindStatuslocker);

	return true;
}

int SM_Manager::AddOneVodStream(int iStreamID,char* strUrl,int iFd,char* username,char *vodname,char *posterurl,char* strSerilno)
{
	char strUserID[128] ={0};
	char strkey_value1[64] = {0};
 	sprintf(strkey_value1,"%d",iStreamID);
	StreamStatus pTmpResource1;
	memset(&pTmpResource1,0,sizeof(pTmpResource1));
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----this is addvod\n",__FUNCTION__,__LINE__);
	int iret1 = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpResource1);
	if(iret1)
	{
	    StreamStatus* pTmp1 = &pTmpResource1;
		memcpy(strUserID,pTmp1->strBind_userID,strlen(pTmp1->strBind_userID)+1);
	}
	
	//向均衡切流器申请ip和port
	if((Istype != 1)&&(advflag == 1))
	{
		m_Advertiser->Getaddr(strkey_value1,strSerilno);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d to get the adv ip and port\n",__FUNCTION__,__LINE__);
	}
	
	//先释放资源
	bool bneedwait = CleanStream(iStreamID,vodplay);
	usleep(VOD_play_clean*1000);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----get userid %s\n",__FUNCTION__,__LINE__,strUserID);

	Stream *ptmpRequest = new Stream;
	ptmpRequest->m_clientSocket = iFd;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---AddOneVodStream m_clientSocket fd = %d\n",__FUNCTION__,__LINE__,ptmpRequest->m_clientSocket);
	//cJSON *pRet_root;
	ptmpRequest->pRet_root = cJSON_CreateObject();
	ptmpRequest->Requst_Json_str(2,"serialno",strSerilno);
	m_VGWVodPlayer->m_mapVod_player.insert(MapVodPlay::value_type(iStreamID,ptmpRequest));
		
	char strRegion[128] = {0};
	int iRegionPort = 0;
	int idex  = 0;
	if(Istype == 1)
	{
		MapRegionID::iterator it = m_mapRegionID.begin();
		while(it != m_mapRegionID.end())
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --region %s , port %d\n",__FUNCTION__,__LINE__,it->second,it->first);
			++it;
		}

		MapRegionID::iterator itfid = m_mapRegionID.begin();
		while(itfid != m_mapRegionID.end())
		{
			if(itfid->first != m_idex)
			{
				//SetUsedRegionID::iterator itused = m_SetUsedRegionID.find(itfid->first);
				pthread_mutex_lock(&m_lockerRegion);
				SetUsedRegionID::iterator itused = m_SetUsedRegionID.begin();
				while(itused != m_SetUsedRegionID.end())
				{	
					int iRegportUsed = itused->second;
					int iRegioID = itfid->first;
					if(iRegioID == iRegportUsed)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---region id is port used %d\n",__FUNCTION__,__LINE__,iRegioID);
						break;
					}
					++itused;
				}
				pthread_mutex_unlock(&m_lockerRegion);
				if(itused == m_SetUsedRegionID.end() )
				{
							//没有在使用
					strcpy(strRegion,itfid->second);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d *********region %s\n",__FUNCTION__,__LINE__,itfid->second);
					iRegionPort = itfid->first;
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d find can use region %s %d\n",__FUNCTION__,__LINE__,strRegion,iRegionPort);
					break;
				}
			}
				++itfid;
		}	
		if(0==iRegionPort)
		{
			MapRegionID::iterator itfid = m_mapRegionID.begin();
			strcpy(strRegion,itfid->second);
			iRegionPort = itfid->first;
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----no find can used region\n",__FUNCTION__,__LINE__);
		}
				
		//将regionID加入到中
		pthread_mutex_lock(&m_lockerRegion);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---insert one stream %d  %d into used map\n",__FUNCTION__,__LINE__,iStreamID,iRegionPort);
		SetUsedRegionID::iterator itisused = m_SetUsedRegionID.find(iStreamID);
		if(itisused!=m_SetUsedRegionID.end())
		{
			m_SetUsedRegionID.erase(itisused);
		}
		m_SetUsedRegionID.insert(SetUsedRegionID::value_type(iStreamID,iRegionPort));
		pthread_mutex_unlock(&m_lockerRegion);
				
		m_idex = iRegionPort;
			
			//(int iStreamID,char *strUrl,char *strRegionid,char* strUserID,char *vodname,char *posterurl)、username
		if(m_VGWVodPlayer->StartOneStream(iStreamID,strUrl,strRegion,username,vodname,posterurl,iFd))
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d to start a vod stream success\n",__FUNCTION__,__LINE__);
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d start a vod stream error -1005\n",__FUNCTION__,__LINE__);
			return -1005;
		}
	}	
	char strSeesionID[64] ={0};
	sprintf(strSeesionID,"%d",iStreamID);

	MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iStreamID);
	if(itIpqam != m_mapIpqamInfo.end())
	{
		IPQAMInfo *pIpqam = itIpqam->second;
		char strOutputUrl[128]={0};
		sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);
		char strInputUrl[128] ={0};
		if((Istype != 1) && (advflag == 0))
		{
			m_VGWVodPlayer->StartOneStream(iStreamID,strUrl,strRegion,username,vodname,posterurl,iFd);
			//usleep(1000*300);
			sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iBaseport+iStreamID);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d strInputUrl =\n",__FUNCTION__,__LINE__,strInputUrl);
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,1);
			return true;
		}
		else if((Istype != 1) && (advflag == 1))
		{
			char smyip[32] = {0};
			char smyport[32] = {0};
			Smstrinfo::iterator itaddr = m_smstraddr.begin();
			if(itaddr != m_smstraddr.end())
			{
				Smstream *temp = *itaddr;
				memcpy(smyip,temp->sadvip,strlen(temp->sadvip)+1);
				memcpy(smyport,temp->sadvport,strlen(temp->sadvport)+1);
				m_smstraddr.erase(itaddr);
				delete temp;
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----smyip = %s,smyport = %s\n",__FUNCTION__,__LINE__,smyip,smyport);
				m_VGWVodPlayer->StartOneStream(iStreamID,strUrl,strRegion,username,vodname,posterurl,iFd,smyip,smyport);
				usleep(1000*300);
						
				Smvodaddr::iterator itaddr = m_smvodaddr.find(iStreamID);
				if(itaddr!= m_smvodaddr.end())
				{
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----to change the smip\n",__FUNCTION__,__LINE__);
					Smstream *vodtemp = itaddr->second;
					memcpy(vodtemp->sadvip,smyip,strlen(smyip)+1);
					memcpy(vodtemp->sadvport,smyport,strlen(smyport)+1);
				}
				else
				{
					Smstream *mytemp = new Smstream;
					memcpy(mytemp->sadvip,smyip,strlen(smyip)+1);
					memcpy(mytemp->sadvport,smyport,strlen(smyport)+1);
					m_smvodaddr.insert(Smvodaddr::value_type(iStreamID,mytemp));
				}
						
				int myport = atoi(smyport);
				sprintf(strInputUrl,"udp://%s:%d",smyip,myport);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d udp://%s:%d\n",__FUNCTION__,__LINE__,smyip,myport);
				m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,1);		
				return true;
			}
			else
			{
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d get the adv ip and port error -1005\n",__FUNCTION__,__LINE__);
				return -1005;
			}
		}
		sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iRegionPort);  //写死	
		char strMsg[32]="";
		
		m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,1);

		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv stream %s\n",__FUNCTION__,__LINE__,strSeesionID);
		usleep(1000*100);
		return 0;
	}
}

bool SM_Manager::GetTaskStatus()
{
	return true;
}

bool SM_Manager::InitStream()
{
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d \n        Begin to init\n",__FUNCTION__,__LINE__);
	m_icurrentidex = 0;
	
	m_Navigation = new Navigation_Stream(this);
	m_Advertiser = new Advertisement_Stream(this);
	m_VGWVodPlayer = new VGW_Vod_Stream(this);
	m_Msi_SMInter = new MSI_SM_Stream(this);
	m_Advmulstream = new Advmul_stream();

	Setstrnavflag hdflag; //用于存放当前高清导航流标志的临时队列
	Setstrnavflag sdflag; //用于存放当前标清导航流标志的临时队列
	//先开启清理模式
	//首先导入状态表数据非空闲状态
	if(LoadStreamStatus())
	{
		//开始清理
		CleanAllStream();
		DBInterfacer::GetInstance()->ResetIPQAMRes();
		//模块回复处理由各个对象处理
		usleep(InitStream_clean);
	}
	else
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ---load clean data error \n",__FUNCTION__,__LINE__);
		
	m_Advertiser->CleanALLAdverStream();
	
	//按组进行初始化
	//先导入资源表
	LoadStreamResource();
	//usleep(1000);
	LoadIPQAMInfo();
	//usleep(1000);
	LoadIPQAMRes();
	
	LoadAdvinfo();

	LoadNetWorkGroup();
//	usleep(1000);

	//分别装载当前高标清的导航标志
	DBInterfacer::GetInstance()->FindSameNavflag(0,hdflag);
	DBInterfacer::GetInstance()->FindSameNavflag(1,sdflag);

	//查询当前VGW 的类型
	m_VGWVodPlayer->Checktype();

	//开始开启组播流
	printf("222555522222\n");
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---begin to add advmulstream\n",__FUNCTION__,__LINE__);
	m_Advmulstream->CleanmulStream();
	usleep(1000*2500);
			
	m_Advmulstream->StartadvStream();
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is advmulstream\n",__FUNCTION__,__LINE__);
			

	//总共分组m_mapNetworkGroup中
	//关联流信息组在m_mapStreamGroup中
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d StreamResoure size=%d ,status size=%d ,group size=%d ,streamGroup size=%d\n",
		__FUNCTION__,__LINE__,m_mapStreamResource.size(),m_mapStreamStatus.size(),m_mapNetworkGroup.size(),m_mapStreamGroup.size());
	MapStreamGroup::iterator iterGroup = m_mapStreamGroup.begin();
	bool bHasCreateStream = false;
	while(iterGroup != m_mapStreamGroup.end())
	{
		int hdiNavtaionNum = 0;
		int sdiNavtaionNum = 0;
		int hdiAdverNum = 0;
		int	sdiAdverNum = 0;
		
		MapNetWorkGroup::iterator itwork = m_mapNetworkGroup.find(iterGroup->first);
		if(itwork != m_mapNetworkGroup.end())
		{
			NetworkGroup *ptmp = itwork->second;
			hdiNavtaionNum = ptmp->hdiNavgationStreamNum;
			sdiNavtaionNum = ptmp->sdiNavgationStreamNum;
			hdiAdverNum = ptmp->hdiAdvertisementStreamNum;
			sdiAdverNum = ptmp->sdiAdvertisementStreamNum;
			
		//分别加载高标清的流
			SetGroupStream sdlistTmps;
			SetGroupStream hdlistTmph;
			
			DBInterfacer::GetInstance()->FindSameGroupStream(ptmp->strNetRegionNum,1,sdlistTmps);
			DBInterfacer::GetInstance()->FindSameGroupStream(ptmp->strNetRegionNum,0,hdlistTmph);
			
			int iGroupSize = sdlistTmps.size();
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---before  sdiNavtaionNum=%d\n",__FUNCTION__,__LINE__,iGroupSize);
			sdiNavtaionNum =  (sdiNavtaionNum <= iGroupSize) ?  sdiNavtaionNum : iGroupSize;
			
			iGroupSize = hdlistTmph.size();
			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---before  sdiNavtaionNum=%d\n",__FUNCTION__,__LINE__,iGroupSize);
			hdiNavtaionNum =  (hdiNavtaionNum <= iGroupSize) ?  hdiNavtaionNum : iGroupSize;

			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----begin group %s ---add hdNav hdiNavtaionNum=%d,---add sdNav sdiNavtaionNum=%d\n\n",__FUNCTION__,__LINE__,ptmp->strNetRegionNum,
					hdiNavtaionNum,sdiNavtaionNum);
			//开始下发标清导航流
			SetGroupStream::iterator sditerList = sdlistTmps.begin();
			Setstrnavflag::iterator sdnavit = sdflag.begin();
			int regionnum = atoi(ptmp->strNetRegionNum);
			while(sdiNavtaionNum-- > 0 && sditerList != sdlistTmps.end())
			{
				//导航流
				int iStreamID = *sditerList; 
				//找到对应流信息
				m_allstrem.insert(Navinfo::value_type(iStreamID,1));
				if(sdnavit != sdflag.end())
				{
					int flagid = *sdnavit; //导航流标志
					//调用下发一路导航流
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64];
					char strSPort[32];
					memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
					sprintf(strSPort,"%d",iMSIServerPort);
					char strAreaID[32] ="3301";
					char strMsg[32]="";
					
					//导航流的url是否需拼接
					char initurl[500] = {0};
					char naurl[512] = {0};
					DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
					//拼接url地址
					sprintf(naurl,"%s&stream_id=%s",initurl,strSeesionID);
					Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);		
					//装载导航流的流id和标志
					m_navinfo.insert(Navinfo::value_type(iStreamID,flagid));
					sdlistTmps.erase(sditerList);
					++sdnavit;
					
					RegNavurl::iterator itmyurl = m_Navurl.find(regionnum);
					if(itmyurl != m_Navurl.end())
					{
						ListStreamNavurl templist = itmyurl->second;
						ListStreamNavurl::iterator listit = templist.begin();
						int i = 0;
						while(listit != templist.end())
						{
							SmNavurl *urlTemp = *listit;
							if(urlTemp->id == flagid)
							{
								urlTemp->times =  urlTemp->times +1;
								i = 1;		
								break;
							}
							else
							{
								++listit;
							}
						}
						if(i==0)
						{
							SmNavurl *Tempurl = new SmNavurl;
							Tempurl->haflag = 1;
							Tempurl->id = flagid;
							Tempurl->times = 0;
							templist.push_back(Tempurl);
						}
					}
					else
					{
						SmNavurl *Tempurl = new SmNavurl;
						Tempurl->haflag = 1;
						Tempurl->id = flagid;
						Tempurl->times = 0;
						ListStreamNavurl tempurllist;
						tempurllist.push_back(Tempurl);
						m_Navurl.insert(RegNavurl::value_type(regionnum,tempurllist));
					}
					
					usleep(1000*1000);	
				}
				else
				{  //如果地址数小于导航数就从头在重复导航地址
					sdnavit = sdflag.begin();
					int flagid = *sdnavit; //导航流标志
					//调用下发一路导航流
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64];
					char strSPort[32];
					memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
					sprintf(strSPort,"%d",iMSIServerPort);
					char strAreaID[32] ="3301";
					char strMsg[32]="";
					
					//导航流的url是否需拼接
					char initurl[500] = {0};
					char naurl[512] = {0};
					DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
					//拼接url地址
					sprintf(naurl,"%s&stream_id=%s",initurl,strSeesionID);
					Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);		
					//装载导航流的流id和标志
					m_navinfo.insert(Navinfo::value_type(iStreamID,flagid));
					sdlistTmps.erase(sditerList);
					++sdnavit;
					
					RegNavurl::iterator itmyurl = m_Navurl.find(regionnum);
					if(itmyurl != m_Navurl.end())
					{
						ListStreamNavurl templist = itmyurl->second;
						ListStreamNavurl::iterator listit = templist.begin();
						int i = 0;
						while(listit != templist.end())
						{
							SmNavurl *urlTemp = *listit;
							if(urlTemp->id == flagid)
							{
								urlTemp->times =  urlTemp->times +1;
								i = 1;		
								break;
							}
							else
							{
								++listit;
							}
						}
						if(i==0)
						{
							SmNavurl *Tempurl = new SmNavurl;
							Tempurl->haflag = 1;
							Tempurl->id = flagid;
							Tempurl->times = 0;
							templist.push_back(Tempurl);
						}
					}
					else
					{
						SmNavurl *Tempurl = new SmNavurl;
						Tempurl->haflag = 1;
						Tempurl->id = flagid;
						Tempurl->times = 0;
						ListStreamNavurl tempurllist;
						tempurllist.push_back(Tempurl);
						m_Navurl.insert(RegNavurl::value_type(regionnum,tempurllist));
					}
					
					usleep(1000*1000);	
				}
				sditerList = sdlistTmps.begin();
			}
		//开始下发高清导航流	
			SetGroupStream::iterator hditerList = hdlistTmph.begin();
			Setstrnavflag::iterator hdnavit = hdflag.begin();
			while(hdiNavtaionNum-- > 0 && hditerList != hdlistTmph.end())
			{
				//导航流
				int iStreamID = *hditerList;
				m_allstrem.insert(Navinfo::value_type(iStreamID,0));
				if(hdnavit != hdflag.end())
				{
					//调用下发一路导航流
					int flagid = *hdnavit; //导航流标志
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64];
					char strSPort[32];
					memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
					sprintf(strSPort,"%d",iMSIServerPort);
					char strAreaID[32] ="3301";
					char strMsg[32]="";
					
					char initurl[500] = {0};
					char naurl[512] = {0};
					DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
					//导航流的url是否需拼接
					sprintf(naurl,"%s&stream_id=%s",initurl,strSeesionID);
					
					Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
						//	装载导航流的流id和标志
					m_navinfo.insert(Navinfo::value_type(iStreamID,flagid));
					hdlistTmph.erase(hditerList);
					++hdnavit;

					RegNavurl::iterator itmyurl = m_Navurl.find(regionnum);
					if(itmyurl != m_Navurl.end())
					{
						ListStreamNavurl templist = itmyurl->second;
						ListStreamNavurl::iterator listit = templist.begin();
						int i = 0;
						while(listit != templist.end())
						{
							SmNavurl *urlTemp = *listit;
							if(urlTemp->id == flagid)
							{
								urlTemp->times =  urlTemp->times +1;
								i = 1;		
								break;
							}
							else
							{
								++listit;
							}
						}
						if(i==0)
						{
							SmNavurl *Tempurl = new SmNavurl;
							Tempurl->haflag = 1;
							Tempurl->id = flagid;
							Tempurl->times = 0;
							templist.push_back(Tempurl);
						}
					}
					else
					{
						SmNavurl *Tempurl = new SmNavurl;
						Tempurl->haflag = 1;
						Tempurl->id = flagid;
						Tempurl->times = 0;
						ListStreamNavurl tempurllist;
						tempurllist.push_back(Tempurl);
						m_Navurl.insert(RegNavurl::value_type(regionnum,tempurllist));
					}
					
					usleep(1000*1000);		
				}
				else
				{
					hdnavit = hdflag.begin();
					int flagid = *hdnavit; //导航流标志
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64];
					char strSPort[32];
					memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
					sprintf(strSPort,"%d",iMSIServerPort);
					char strAreaID[32] ="3301";
					char strMsg[32]="";
					
					char initurl[500] = {0};
					char naurl[512] = {0};
					DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
					//导航流的url是否需拼接
					sprintf(naurl,"%s&stream_id=%s",initurl,strSeesionID);
					Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
					//	装载导航流的流id和标志
					m_navinfo.insert(Navinfo::value_type(iStreamID,flagid));
					hdlistTmph.erase(hditerList);
					++hdnavit;

					RegNavurl::iterator itmyurl = m_Navurl.find(regionnum);
					if(itmyurl != m_Navurl.end())
					{
						ListStreamNavurl templist = itmyurl->second;
						ListStreamNavurl::iterator listit = templist.begin();
						int i = 0;
						while(listit != templist.end())
						{
							SmNavurl *urlTemp = *listit;
							if(urlTemp->id == flagid)
							{
								urlTemp->times =  urlTemp->times +1;
								i = 1;		
								break;
							}
							else
							{
								++listit;
							}
						}
						if(i==0)
						{
							SmNavurl *Tempurl = new SmNavurl;
							Tempurl->haflag = 0;
							Tempurl->id = flagid;
							Tempurl->times = 0;
							templist.push_back(Tempurl);
						}
					}
					else
					{
						SmNavurl *Tempurl = new SmNavurl;
						Tempurl->haflag = 0;
						Tempurl->id = flagid;
						Tempurl->times = 0;
						ListStreamNavurl tempurllist;
						tempurllist.push_back(Tempurl);
						m_Navurl.insert(RegNavurl::value_type(regionnum,tempurllist));
					}
					
					usleep(1000*1000);	
				}
				hditerList = hdlistTmph.begin();
			}
		//开始下发高清广告流	
			iGroupSize = hdlistTmph.size();
			hdiAdverNum =	hdiAdverNum <= iGroupSize ?  hdiAdverNum : iGroupSize;

			hditerList = hdlistTmph.begin();
			
			while(hdiAdverNum-- > 0 && hditerList != hdlistTmph.end())
			{
				//导航流
				int iStreamID = *hditerList;
				//找到对应流信息
				m_allstrem.insert(Navinfo::value_type(iStreamID,0));
				
				MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
				if(iterStream != m_mapStreamResource.end())
				{
					StreamResource *pStream = iterStream->second;
					//调用下发一路广告流
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pStream->iStreamID);
					
					int iIPQAMnum = pStream->iIPQAMNum;
				
					MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(pStream->iStreamID);
					
					if(itIpqam != m_mapIpqamInfo.end())
					{
						
						IPQAMInfo *pIpqam = itIpqam->second;
						
						char strOutputUrl[128]={0};
						
						sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);

						char strInputUrl[128] ={0};
						SetAdvinfo::iterator itadv = m_mapadvinfo.find(pStream->iStreamID);
						Advinfo *tempadv = itadv->second;
						sprintf(strInputUrl,"udp://%s:%s",tempadv->advip,tempadv->advport);
						char strMsg[32]="";

						m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);
						hdlistTmph.erase(hditerList);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv hdstream %s,group id=%s,ipqamnum=%d,hdlistTmph = %d\n",__FUNCTION__,__LINE__,strSeesionID,
								pStream->strNetRegionNum,pStream->iIPQAMNum,hdlistTmph.size());
						//	装载导航流的流id和标志
						m_navinfo.insert(Navinfo::value_type(pStream->iStreamID,0));
						usleep(1000*100);
					}	
				}
				//++iterList;
				
				hditerList = hdlistTmph.begin();
			}
		//开始下发标清广告
			iGroupSize = sdlistTmps.size();
			sdiAdverNum = sdiAdverNum <= iGroupSize ?  sdiAdverNum : iGroupSize;
			sditerList = sdlistTmps.begin();
			
			while(sdiAdverNum-- > 0 && sditerList != sdlistTmps.end())
			{
				//导航流
				int iStreamID = *sditerList;
				//找到对应流信息
				m_allstrem.insert(Navinfo::value_type(iStreamID,1));
				
				MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
				if(iterStream != m_mapStreamResource.end())
				{
					StreamResource *pStream = iterStream->second;
					//调用下发一路广告流
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pStream->iStreamID);
					
					int iIPQAMnum = pStream->iIPQAMNum;
					
					MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(pStream->iStreamID);
					if(itIpqam != m_mapIpqamInfo.end())
					{
						IPQAMInfo *pIpqam = itIpqam->second;

						char strOutputUrl[128]={0};
						sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);

						char strInputUrl[128] ={0};
						SetAdvinfo::iterator itadv = m_mapadvinfo.find(pStream->iStreamID);
						Advinfo *tempadv = itadv->second;
						sprintf(strInputUrl,"udp://%s:%s",tempadv->advip,tempadv->advport);
						char strMsg[32]="";
						m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);
						sdlistTmps.erase(sditerList);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv sdstream %s,group id=%s,ipqamnum=%d,sdlistTmps = %d\n",__FUNCTION__,__LINE__,strSeesionID,
								pStream->strNetRegionNum,pStream->iIPQAMNum,sdlistTmps.size());
						//	装载导航流的流id和标志
						m_navinfo.insert(Navinfo::value_type(pStream->iStreamID,0));
						usleep(1000*100);
					}	
				}
				sditerList = sdlistTmps.begin();
			}
			
			iterGroup++;	
		}
		else
			++iterGroup;
	}
	checkflag = 10;//开始check
	return true;
}

int SM_Manager::Bind_OneStream(int iStreamid,char *strUserID,char* strToken,int *iChannelInfo)
{
	//主要是更新数据库，补发导航流
	//找到对应数据
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamid);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;			
		if(strcmp(pTmp->strBind_userID,strUserID) == 0 )
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ----same user bind the same stream\n",__FUNCTION__,__LINE__);
			
			return -1001;
		}
		else if(strcmp(pTmp->strStreamType,Status_Adver)==0 || strcmp(pTmp->strStreamType,Status_Idle)==0 ||
			     strcmp(pTmp->strStreamType,Status_Nav_Home)==0)
		{
			//已经有绑定用户
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ***********wrong Status = %s\n",__FUNCTION__,__LINE__,pTmp->strStreamType);
			return -1002;
		}
		else if(strcmp(pTmp->strBind_userID,"")!=0)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ----this stream is already bind\n",__FUNCTION__,__LINE__);
			return -1003;
		}
		
		time_t timep; 
		time (&timep); 
		struct tm* tmpTime = gmtime(&timep);
		char nowTime[128]={0};
		// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
		// 月日时分秒
		sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
			tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -----time =%s\n",__FUNCTION__,__LINE__,nowTime);

		//将绑定数据放入状态表中	
		strcpy(pTmp->strBind_date,nowTime);
		strcpy(pTmp->strBind_userID,strUserID);
		strcpy(pTmp->strStreamType,Status_Nav_Home);
		//strcpy(pTmp->strSwitch_task_id,strToken);

		//状态时间修改一次则更新
		strcpy(pTmp->strStatus_date,nowTime);

		DBInterfacer::GetInstance()->update_table(1,pTmp);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d bind one stream success\n",__FUNCTION__,__LINE__,iStreamid);

		char strkey_value[64] = {0};
		sprintf(strkey_value,"%d",iStreamid);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* tmpResource = &pTmpResource;		
		//	tmpResource->iBind_state = 1;

			*iChannelInfo = tmpResource->iChannel_info;
		}
	}
	else
	{
		//不存在的流返回异常
		return -1;
	}

	return 0;
}

bool SM_Manager::AddStream2GroupInfo(int iStreamID,int iType)
{
	if(1==iType)
	{
		MapStreamResource::iterator itfid = m_mapStreamResource.find(iStreamID);
		if(itfid != m_mapStreamResource.end())
		{
			StreamResource *pTmp = itfid->second;
			//从分组表中找到
			MapStreamGroup::iterator itstremfind  = m_mapStreamGroup.find(pTmp->strNetRegionNum);
			if(itstremfind != m_mapStreamGroup.end())
			{
				ListStreamResource listTmp = itstremfind->second;
				listTmp.push_back( iStreamID);
			}

		}
	}	
	else if(0==iType)
	{
		MapStreamResource::iterator itfid = m_mapStreamResource.find(iStreamID);
		if(itfid != m_mapStreamResource.end())
		{
			StreamResource *pTmp = itfid->second;
			//从分组表中找到
			MapStreamGroup::iterator itstremfind  = m_mapStreamGroup.find(pTmp->strNetRegionNum);
			if(itstremfind != m_mapStreamGroup.end())
			{
				ListStreamResource listTmp = itstremfind->second;
				ListStreamResource::iterator iterList = listTmp.begin();
				while(iterList != listTmp.end())
				{
					if(*iterList == iStreamID)
					{
						listTmp.erase(iterList);
						break;
					}
					++iterList;
				}
			}

		}
		
	}
	return true;
}

int SM_Manager::AddOneNavStream(int iStreamID,int streamtype)
{
	//创建一路导航流
	int flagnavid;
	char strSeesionID[64] ={0};
	sprintf(strSeesionID,"%d",iStreamID);
	char strSID[64] ="1001";
	char strReSID[64] ="2000"; //operid
	char strSIP[64];
	char strSPort[32];
	memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
	sprintf(strSPort,"%d",iMSIServerPort);
	char strAreaID[32] ="3301";
	char strMsg[32]="";

	char initurl[500] = {0}; 
	
	StreamResource pTmpResource;
	StreamResource *PTmp;
	int regionnum;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strSeesionID,&pTmpResource);
	if(iret)
	{
		PTmp  = &pTmpResource;
		regionnum = atoi(PTmp->strNetRegionNum);
	}
	//如果是高清就在高清导航标志头端找
	if(streamtype == 0)
	{	
		Navnext::iterator naiterList = hdm_navnext.find(regionnum);
		if(naiterList != hdm_navnext.end())
		{
			ListStreamResource istemp = naiterList->second;
			ListStreamResource::iterator hditer = istemp.begin();
			flagnavid = *hditer;
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d the hdnext anv id is %s\n",__FUNCTION__,__LINE__,flagnavid);
			//删除掉最前端高清导航标志
			istemp.erase(hditer);
			DBInterfacer::GetInstance()->FindNavurl(flagnavid,initurl);
		}
	}
	//如果是标清就在标清导航标志的头端来找
	else if(streamtype == 1)
	{
		Navnext::iterator naiterList = sdm_navnext.find(regionnum);
		if(naiterList != sdm_navnext.end())
		{
			ListStreamResource istemp = naiterList->second;
			ListStreamResource::iterator sditer = istemp.begin();
			flagnavid = *sditer;
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d the sdnext anv id is %s\n",__FUNCTION__,__LINE__,flagnavid);
			//删除掉最前端的标清导航标志
			istemp.erase(sditer);
			DBInterfacer::GetInstance()->FindNavurl(flagnavid,initurl);
		}
	}	
	//如果是恢复就在m_navinfo 中来找导航标志
	else if(streamtype == 2)
	{
		Navinfo::iterator itnav = m_navinfo.find(iStreamID);
		if(itnav != m_navinfo.end())	
		{
			flagnavid = itnav->second;
		}
		DBInterfacer::GetInstance()->FindNavurl(flagnavid,initurl);
	}
	char naurl[512] = {0};
	sprintf(naurl,"%s&stream_id=%s",initurl,strSeesionID);
	
	Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
			
	//修改生成的导航流的标志
	Navinfo::iterator itnav = m_navinfo.find(iStreamID);
	if(itnav != m_navinfo.end())	
	{
		itnav->second = flagnavid;
	}
	return 0;
}

bool SM_Manager::VodStreamOver(int iStreamID,char *username)
{
	//开启一路新的导航流
	//先清理
	CleanStream(iStreamID);	
	usleep(VodStreamOver_clean*1000);
	
	//用于删除掉在退出时存放在m_ispause 中的流id
	ListStreamResource::iterator iterList = m_ispause.begin();
	while(iterList != m_ispause.end())
	{
		if(*iterList == iStreamID)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to erase the quit stream\n",__FUNCTION__,__LINE__);
			m_ispause.erase(iterList);
			break;
		}
		++iterList;
	}

	//删除掉暂停是存放在m_SetPauseVod 中的流id
	SetPauseVod::iterator itpausevod = m_SetPauseVod.begin();
	while(itpausevod !=m_SetPauseVod.end())
	{
		//查找该路流是否暂停
		if(*itpausevod == iStreamID)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to erase the itpausevod stream\n",__FUNCTION__,__LINE__);
			m_SetPauseVod.erase(itpausevod);
			break;
		}
		++itpausevod;
	}

	int ret = -1;
	//创建一路导航流
	char strkey_value[64] = {0};
	char newurl[1024] = {0};
	int ishd;
	
	StreamAbnormal::iterator ittype = m_allstrem.find(iStreamID);
	if(ittype != m_allstrem.end())
	{
		ishd = ittype->second;
	}

	char *p = NULL;
	char fig = '?';
	p = strstr(newurl,&fig);
	if(!p)
	{
		if(!ishd)
			sprintf(newurl,"%s?username=%s&streamid=%d&VodStreamOver=%s",navgoback,username,iStreamID,"goback");
		else
			sprintf(newurl,"%s?username=%s&streamid=%d&VodStreamOver=%s",sdnavgoback,username,iStreamID,"goback");	
	}
	else
	{
		if(!ishd)
			sprintf(newurl,"%s&username=%s&streamid=%d&VodStreamOver=%s",navgoback,username,iStreamID,"goback");
		else
			sprintf(newurl,"%s&username=%s&streamid=%d&VodStreamOver=%s",sdnavgoback,username,iStreamID,"goback");
	}

	//调用下发一路导航流
	char strSeesionID[64] ={0};
	sprintf(strSeesionID,"%d",iStreamID);
	char strSID[64] ="1001";
	char strReSID[64] ="2000"; //operid
	char strSIP[64];
	char strSPort[32];
	memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
	sprintf(strSPort,"%d",iMSIServerPort);
	char strAreaID[32] ="3301";		
	char strMsg[32]="";					
	//导航流的url是否需拼接			
	Navstream(strSeesionID,strSID,strReSID,newurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one nav stream %s\n",__FUNCTION__,__LINE__,strSeesionID);
	ret = 0;
	return true;
}

int SM_Manager::AddOneAdvStream(int iStreamID)
{
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamResource pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamResource* pStream = &pTmpResource;
		int shflag = pStream->iWherether_HD;
		int inum = atoi(pStream->strNetRegionNum);
		
		//首先判断是否需要下发导航流
		if(!shflag)
		{
			FailNav::iterator hdit = hd_failnav.find(inum);	
			if(hdit !=  hd_failnav.end())
			{
				LOG_DEBUG_FORMAT("DEBUG - [SM]:%s %d *****hdstream regionmun = %d and hdnav streamnum =%d\n",__FUNCTION__,__LINE__,inum,hdit->second);
				if(hdit->second >0)
				{
					StreamStatus pTmpStatus;
					//首先需要把相应区域下未成功下发的高清导航流数目减1
					hdit->second = hdit->second-1;
					LOG_INFO_FORMAT("INFO  - [SM]:%s %d *****regionmun = %d and rest hdnav streamnum =%d\n",__FUNCTION__,__LINE__,inum,hdit->second);
					//解除绑定
					int iiret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpStatus);
					if(iiret)
					{
						StreamStatus* pTmp = &pTmpStatus;	
						if(strcmp(pTmp->strBind_userID,"")!=0)	
						{	
							m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strkey_value);
							usleep(1000*1000);
						}
					}
					//将该路下发为新的导航流
					AddOneNavStream(iStreamID,0);
					return true;
				}
			}
		}
		else
		{
			FailNav::iterator sdit = sd_failnav.find(inum);	
			if(sdit !=  sd_failnav.end())
			{
				LOG_DEBUG_FORMAT("DEBUG - [SM]:%s %d *****sdstream regionmun = %d and sdnav streamnum =%d\n",__FUNCTION__,__LINE__,inum,sdit->second);
				if(sdit->second >0)
				{
					StreamStatus pTmpStatus;
					//首先把相应区域下未成功下发的标清导航流的数目减1
					sdit->second = sdit->second-1;
					LOG_INFO_FORMAT("INFO  - [SM]:%s %d *****regionmun = %d and rest sdnav streamnum =%d\n",__FUNCTION__,__LINE__,inum,sdit->second);
					int iiret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpStatus);
					if(iiret)
					{
						StreamStatus* pTmp = &pTmpStatus;	
						if(strcmp(pTmp->strBind_userID,"")!=0)	
						{	
							m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strkey_value);
							usleep(1000*1000);
						}
					}	
					//将该路下发为新的导航
					AddOneNavStream(iStreamID,1);
					return true;
				}
			}
		}
		//如果不需要下发导航流就正常下发广告
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pStream->iStreamID);
		
		int iIPQAMnum = pStream->iIPQAMNum;
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(pStream->iStreamID);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;
			char strOutputUrl[128]={0};
			sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);

			char strInputUrl[128] ={0};
			SetAdvinfo::iterator itadv = m_mapadvinfo.find(pStream->iStreamID);
			Advinfo *tempadv = itadv->second;
			sprintf(strInputUrl,"udp://%s:%s",tempadv->advip,tempadv->advport);
			char strMsg[32]="";
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv stream %s,group id=%s,ipqamnum=%d\n",__FUNCTION__,__LINE__,strSeesionID,
					pStream->strNetRegionNum,pStream->iIPQAMNum);
			usleep(1000*100);
		}
			
	}

	return true;
}

void *SM_Manager::Check_BindOverTime_thread(void *arg)
{
	SM_Manager* this0 = (SM_Manager*)arg;
	while(1)
	{
		usleep(1000*1000); // 1秒检测
		time_t tmNow;
		time(&tmNow);
		pthread_mutex_lock(&this0->m_BindStatuslocker);
		MapBindOverTime::iterator it = this0->m_mapBindStatus.begin();
		while(it != this0->m_mapBindStatus.end())
		{
			double difTime = difftime(tmNow,it->second);
			
			if(difTime > iBindOverTime)
			{
				//绑定超时
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d *****bind dif time %f  ,stream =%d\n",__FUNCTION__,__LINE__,difTime,it->first);
				this0->BindOverTime(it->first);
				//移除
				this0->m_mapBindStatus.erase(it++);
			}
			else
				++it;
		}
		pthread_mutex_unlock(&this0->m_BindStatuslocker);
	}
}


bool SM_Manager::BindOverTime(int iStreamID)
{
	//释放导航流
	CleanStream(iStreamID);
	//创建广告流 此过程不必解释
	usleep(BindOverTime_clean);
	AddOneAdvStream(iStreamID);
	return true;
}

int SM_Manager::AfterBindAddStream(int iNewStreamID,bool bNewStream)
{

}


bool SM_Manager::PauseVOD(int iStreamID,char *iUsername,char *vodname,char *posterurl,int temp)
{
	//首先该流程不应该释放  VOD  资源，只增加广告流资源
	//增加的广告流需要在恢复时删除。并且该过程状态不应该修改
	//释放切流器资源
	int ret = -1;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----iUsername = %s\n",__FUNCTION__,__LINE__,iUsername);
	//创建一路导航流
	char strkey_value[64] = {0};
	char newurl[1024] = {0};
	int ishd;
//	DBInterfacer::GetInstance()->Findtype(iStreamID,ishd);

	StreamAbnormal::iterator ittype = m_allstrem.find(iStreamID);
	if(ittype != m_allstrem.end())
	{
		ishd = ittype->second;
	}
	/*
	MapStreamResource::iterator iterhd = m_mapStreamResource.find(iStreamID);
	if(iterhd!=m_mapStreamResource.end())
	{
		StreamResource *pStreamhd = iterhd->second;
		ishd = pStreamhd->iWherether_HD;
	}
	*/
	if(temp)
	{
		char istrkey_value[64] = {0};
		sprintf(istrkey_value,"%d",iStreamID);
		StreamStatus mpTmpResource;
		memset(&mpTmpResource,0,sizeof(mpTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",istrkey_value,&mpTmpResource);
		if(iret)
		{
			StreamStatus* mpTmp = &mpTmpResource;
			if(strcmp(mpTmp->strStreamType,Status_Vod)==0)
			{
				CleanOneAdvStream(iStreamID);
				m_SetPauseVod.insert(iStreamID);
				usleep(PauseVOD_clean*1000);
			}
			else if(strcmp(mpTmp->strStreamType,Status_Nav_Home)==0)
			{
				char strSID[128] = "1001";
				char strReSID[128] = "1001";
				char strAuthiName[128] = "cscs";
				char strAuthcode[128] = "123456";
				char strMsg[128] = "";
				
				Cleanonenav(istrkey_value);
				if(1==temp)
				{
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to push back the quit stream\n",__FUNCTION__,__LINE__);
					m_ispause.push_back(iStreamID);
				}
				usleep(PauseVOD_clean*1000);
			}
		}
			//sprintf(newurl,"http://192.168.100.11:8181/NS/subinter1.jsp?username=%s&streamid=%d&VodStreamOver=%s",iUsername,iStreamID,"goback");
		if(1==temp)
		{	
			StreamStatus* spTmp = &mpTmpResource;
			char *p = NULL;
			char fig = '?';
			p = strstr(newurl,&fig);
			if(!p)
			{
				if(!ishd)
				sprintf(newurl,"%s?username=%s",quiturl,spTmp->strBind_userID);
				else
				sprintf(newurl,"%s?username=%s",sdquiturl,spTmp->strBind_userID);	
			}
			else
			{
				if(!ishd)
				sprintf(newurl,"%s&username=%s",quiturl,spTmp->strBind_userID);
				else
				sprintf(newurl,"%s&username=%s",sdquiturl,spTmp->strBind_userID);
			}
		}
		else if(2==temp)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d iUsername = %s\n",__FUNCTION__,__LINE__,iUsername);
			char *p = NULL;
			char fig = '?';
			p = strstr(newurl,&fig);
			if(!p)
			{
				if(!ishd)
				sprintf(newurl,"%s?username=%s&streamid=%d&vodname=%s&posterurl=%s",pauseurl,iUsername,iStreamID,vodname,posterurl);
				else
				{
					sprintf(newurl,"%s?username=%s&streamid=%d&vodname=%s&posterurl=%s",sdpauseurl,iUsername,iStreamID,vodname,posterurl);
				}
			}
			else
			{
				if(!ishd)
				sprintf(newurl,"%s&username=%s&streamid=%d&vodname=%s&posterurl=%s",pauseurl,iUsername,iStreamID,vodname,posterurl);
				else
				{
					sprintf(newurl,"%s&username=%s&streamid=%d&vodname=%s&posterurl=%s",sdpauseurl,iUsername,iStreamID,vodname,posterurl);
				}
			}
		}
	}
	else
	{
		CleanOneAdvStream(iStreamID);
		m_SetPauseVod.insert(iStreamID);
		usleep(PauseVOD_clean*1000);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----now the vod is pause\n",__FUNCTION__,__LINE__);
		//sprintf(newurl,"http://192.168.30.237:8080/NSCS/pause2.jsp?username=%s&streamid=%d&vodname=%s&posterurl=%s",iUsername,iStreamID,vodname,posterurl);
		if(!ishd)
		{
			sprintf(newurl,"%s?username=%s&streamid=%d&vodname=%s&posterurl=%s",pauseurl,iUsername,iStreamID,vodname,posterurl);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is pause hd\n",__FUNCTION__,__LINE__);
		}
		else
		{
			sprintf(newurl,"%s?username=%s&streamid=%d&vodname=%s&posterurl=%s",sdpauseurl,iUsername,iStreamID,vodname,posterurl);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is pause sd\n",__FUNCTION__,__LINE__);
		}
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d -posterurl = %s\n",__FUNCTION__,__LINE__,posterurl);
			//sprintf(newurl,"http://192.168.100.11:8181/NS/pause.jsp?username=%s&streamid=%d",iUsername,iStreamID);
	}
		//调用下发一路导航流
	char strSeesionID[64] ={0};
	sprintf(strSeesionID,"%d",iStreamID);
	char strSID[64] ="1001";
	char strReSID[64] ="2000"; //operid
	char strSIP[64];
	char strSPort[32];
	memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
	sprintf(strSPort,"%d",iMSIServerPort);
	char strAreaID[32] ="3301";
	char strMsg[32]="";	
	//导航流的url是否需拼接
	Navstream(strSeesionID,strSID,strReSID,newurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
	
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one nav stream %s\n",__FUNCTION__,__LINE__,strSeesionID);
	ret = 0;
	return true;
}

bool SM_Manager::RecoverVodPlay(int iStreamID,char *username,char *vodname,char *posterurl)
{
	ListStreamResource::iterator iterList = m_ispause.begin();
	while(iterList != m_ispause.end())
	{
		if(*iterList == iStreamID)
		{
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to recovery to the pause page username = %s\n",__FUNCTION__,__LINE__,username);
			PauseVOD(iStreamID,username,vodname,posterurl,2);
			m_ispause.erase(iterList);
			return true;
			break;
		}
		++iterList;
	}
	//删除原来的导航流
	CleanStream(iStreamID,Navigation);
	usleep(Recovery*1000);
	//切流器切换对应的VOD 点播
	//需要组装出输入地址，输出地址

	Stream *ptmpRequest = new Stream;

	/*
	int idex = m_icurrentidex;
	m_icurrentidex = !m_icurrentidex;
	char strRegion[128] = {0};
	strcpy(strRegion,CstrRegion[idex]);
	*/
	
	char strSeesionID[64] ={0};
	sprintf(strSeesionID,"%d",iStreamID);			
	MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iStreamID);
	if(itIpqam != m_mapIpqamInfo.end())
	{
		IPQAMInfo *pIpqam = itIpqam->second;
		char strOutputUrl[128]={0};
		sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);
		if(Istype != 1)
		{
			char strInputUrl[128] ={0};
			if(advflag == 0)
			{
				sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iBaseport+iStreamID);
			}
			else
			{
				Smvodaddr::iterator itaddr = m_smvodaddr.find(iStreamID);
				if(itaddr!= m_smvodaddr.end())
				{
					Smstream *vodtemp = itaddr->second;
						
					int advport = atoi(vodtemp->sadvport);
					sprintf(strInputUrl,"udp://%s:%d",vodtemp->sadvip,advport);
				}
				else
				{
					LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d there no ip and port\n",__FUNCTION__,__LINE__);
					return false;
				}
			}
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d strInputUrl = %s\n",__FUNCTION__,__LINE__,strInputUrl);
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,3);
			//删除掉存放在m_SetPauseVod中暂停的流
			SetPauseVod::iterator itpausevod = m_SetPauseVod.begin();
			while(itpausevod != m_SetPauseVod.end())
			{
				if(*itpausevod == iStreamID)
				{
					
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to erase the itpausevod stream\n",__FUNCTION__,__LINE__);
					m_SetPauseVod.erase(itpausevod);
					break;
				}
				++itpausevod;
			}
			usleep(1000*100);
			return true;
		}
		SetUsedRegionID::iterator itregion = m_SetUsedRegionID.find(iStreamID);
		if(itregion != m_SetUsedRegionID.end())
		{
			int  iregionport = itregion->second;
			char strInputUrl[128] ={0};
			sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iregionport);
			char strMsg[32]="";
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,3);
					
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv stream %s\n",__FUNCTION__,__LINE__,strSeesionID);
			SetPauseVod::iterator itpausevod = m_SetPauseVod.begin();
			while(itpausevod != m_SetPauseVod.end())
			{
				if(*itpausevod == iStreamID)
				{
					m_SetPauseVod.erase(itpausevod);
					break;
				}
				++itpausevod;
			}
			usleep(1000*100);
		}
	}
	return true;
}

bool SM_Manager::CleanOneAdvStream(int iStreamID)
{
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;				
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->istreamID);

		m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		usleep(1000*100);
	}
	return true;
}

bool SM_Manager::Tellcums(int istreamid)
{
	m_Msi_SMInter->TellMI(istreamid);
	return true;		
}
bool SM_Manager::Userbehav(int iStreamID,char *iUsertype,char *strUserName,char *Commit,int result,char *date)
{
	UserBehaviour *user = new UserBehaviour;
	time_t timep; 
	time (&timep); 
	struct tm* tmpTime = gmtime(&timep);
	char nowTime[128]={0};
	if(!date)
	{
		sprintf(nowTime,"%d-%d-%d %d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
			tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d time =%s\n",__FUNCTION__,__LINE__,nowTime);
	}
	else
	{
		memcpy(nowTime,date,strlen(date)+1);
	}
	user->iStreamID	= iStreamID;
	user->result = result;
	memcpy(user->strUserName,strUserName,strlen(strUserName)+1);
	memcpy(user->strActionType,iUsertype,strlen(iUsertype)+1);
	if(!Commit)
	{
		strcpy(user->strNetworkComment,"NULL");
	}
	else
	{
		memcpy(user->strNetworkComment,Commit,strlen(Commit)+1);
	}
	memcpy(user->strActionDate,nowTime,strlen(nowTime)+1);
	DBInterfacer::GetInstance()->insert_table(2,user);
}


bool SM_Manager::Bindagain(char *iUsername,int iiStreamid)
{
	StreamStatus pTmpResource;
	int iStreamid;
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"strBind_userID",iUsername,&pTmpResource);
	if(!iret)
	{
		return true;
	}
	else
	{
		StreamStatus* pTmp = &pTmpResource;
		if(pTmp->istreamID == iiStreamid)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d bind the sameone\n",__FUNCTION__,__LINE__);
			return -1001;
		}
		else
		{
			char sstreamid[32] = {0};
			sprintf(sstreamid,"%d",pTmp->istreamID);
			Unbind(iUsername,sstreamid);
			return true;
		}
	}
}

bool SM_Manager::Findcsaddr(char *csip,char *csport,int iStreamID)
{
	MapCsaddr::iterator itCsaddr = m_mapCsaddr.find(iStreamID);
	if(itCsaddr != m_mapCsaddr.end())
	{
		Csaddr *paddr = itCsaddr->second;
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d paddr->csip = %s,paddr->csport = %s\n",__FUNCTION__,__LINE__,paddr->csip,paddr->csport);
		memcpy(csip,paddr->csip,strlen(paddr->csip)+1);
		memcpy(csport,paddr->csport,strlen(paddr->csport)+1);
	}
		
}

bool SM_Manager::Unbind(char * usernam,char * streamid)
{
 // pthread_mutex_lock(&m_BindStatuslocker);
    LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to unbind\n",__FUNCTION__,__LINE__);
	m_Msi_SMInter->CleanMSIStream(usernam,streamid);
	usleep(1000*800);

	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",streamid,&pTmpResource);
	if(iret)
	{
		StreamStatus *pTmp = &pTmpResource;
		if(strcmp(pTmp->strStreamType,Status_Adver)==0)
		{
			pthread_mutex_unlock(&m_BindStatuslocker);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is Status_Adver stream\n",__FUNCTION__,__LINE__);
			return true;
		}
	}
	
	int istreamid = atoi(streamid);
	CleanStream(istreamid);
	usleep(1000*1500);
	AddOneAdvStream(istreamid);
  	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d stream unbind over\n",__FUNCTION__,__LINE__);
 // pthread_mutex_unlock(&m_BindStatuslocker);
}

bool SM_Manager::Navstream(char *strSeesionId,char *strSID,char* strReSID,char *strUrl,char* strISIP,char* strSPort,\
					char* strAreaID,char *strSerialno,char *strMsg)
{
	if(advflag == 0)
	{
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d to begin a Navstream\n",__FUNCTION__,__LINE__);
		int iStreamID = atoi(strSeesionId);
		int port = iStreamID + Baseport;
		char por2[64];
		sprintf(por2,"%d",port);
		char task_id[64] = "11111";

		int streamtype = 0;
		m_Advertiser->CleanAdverStream(strSeesionId,task_id,strSerialno);
		usleep(1000*800);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d to add the m_Navigation stream iip = %s,iport = %s\n",__FUNCTION__,__LINE__,strAdverIP,por2);
	
	//	DBInterfacer::GetInstance()->Findtype(iStreamID,streamtype);

		StreamAbnormal::iterator ittype = m_allstrem.find(iStreamID);
		if(ittype != m_allstrem.end())
		{
			streamtype = ittype->second;
		}
		
		//调用下发一路导航流
		m_Navigation->StartOneStream(strSeesionId,strSID,strReSID,strUrl,strAdverIP,por2,strISIP,strSPort,strAreaID,strSerialno,strMsg,streamtype);

		char strSeesionID[64] ={0};		
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iStreamID);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;
			char strOutputUrl[128]={0};
			sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);

			char strInputUrl[128] ={0};
			sprintf(strInputUrl,"udp://%s:%d",strAdverIP,port);
			m_Advertiser->StartOneStream(strSeesionId,strInputUrl,strOutputUrl,strSerialno,2);		
			usleep(1000*100);
		}
	}
	else if(advflag == 1)
	{
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---to begin a Navstream\n",__FUNCTION__,__LINE__);
		int iStreamID = atoi(strSeesionId);
		int ifrecv = 10;
		
		char smyip[64] = {0};
		char smyport[64] = {0};
		char task_id[64] = "11111";
		int streamtype;

	//	DBInterfacer::GetInstance()->Findtype(iStreamID,streamtype);
		StreamAbnormal::iterator ittype = m_allstrem.find(iStreamID);
		if(ittype != m_allstrem.end())
		{
			streamtype = ittype->second;
		}
		//调用下发一路导航流
		int i;
		for(i=0;i<2;i++)
		{
			m_Advertiser->Getaddr(strSeesionId,strSerialno);
			usleep(1000*10);
			while(ifrecv--)
			{
				Smstrinfo::iterator itaddr = m_smstraddr.begin();
				if(itaddr != m_smstraddr.end())
				{
					Smstream *temp = *itaddr;
					memcpy(smyip,temp->sadvip,strlen(temp->sadvip)+1);
					memcpy(smyport,temp->sadvport,strlen(temp->sadvport)+1);
					m_smstraddr.erase(itaddr);
					delete temp;
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----smyip = %s,smyport = %s\n",__FUNCTION__,__LINE__,smyip,smyport);
					break;
				}
				usleep(1000*10);
			}	
			if(ifrecv>0)
			{
				break;
			}
			else
			{
				if(i==1)
				{
					LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d --get addr error\n",__FUNCTION__,__LINE__);
					StreamStatus mpTmpResource;
					memset(&mpTmpResource,0,sizeof(mpTmpResource));
					int miret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strSeesionId,&mpTmpResource);
					if(miret)
					{
						StreamStatus* mptmp = &mpTmpResource;
						if(strcmp(mptmp->strBind_userID,"") != 0)
						{
							strcpy(mptmp->strStreamType,Status_Nav_Home);
						}
						else
							strcpy(mptmp->strStreamType,Status_Nav);
							
						strcpy(mptmp->strSwitch_task_id,strSeesionId);
						time_t timep; 
						time (&timep); 
						struct tm* tmpTime = gmtime(&timep);
						char nowTime[128]={0};
						// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
						sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
							tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
						strcpy(mptmp->strStatus_date,nowTime);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d mptmp->strStreamType = %s\n",__FUNCTION__,__LINE__,mptmp->strStreamType);
							//修改对应数据库
						DBInterfacer::GetInstance()->update_table(1,mptmp);
					}
					return false;
				}
			}
		}
		m_Navigation->StartOneStream(strSeesionId,strSID,strReSID,strUrl,smyip,smyport,strISIP,strSPort,strAreaID,strSerialno,strMsg,streamtype);

		char strSeesionID[64] ={0};
		
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iStreamID);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;

			char strOutputUrl[128]={0};
			sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pIpqam->iIpqamManagerPort);

			char strInputUrl[128] ={0};
			sprintf(strInputUrl,"udp://%s:%s",smyip,smyport);
			m_Advertiser->StartOneStream(strSeesionId,strInputUrl,strOutputUrl,strSerialno,2);
					
		//	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---start one adv stream %s,group id=%s,ipqamnum=%d\n",__FUNCTION__,__LINE__,strSeesionID,
		//	pStream->strNetRegionNum,pStream->iIPQAMNum);
			usleep(1000*100);
		}
	}
}

bool SM_Manager::Checksession()	
{
	StreamAbnormal::iterator iit = m_allstrem.begin();
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d m_mapStreamResource size = %d\n",__FUNCTION__,__LINE__,m_allstrem.size());
	while(iit != m_allstrem.end())
	{
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---stream id = %d\n",__FUNCTION__,__LINE__,iit->first);
		char serialno[] = "9348593dfjks";
		m_Advertiser->CheckStatus(iit->first,serialno);
		usleep(1000*1000);
		iit++;
	}
}

bool SM_Manager::Dealsession(int streamid)
{
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",streamid);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
   pthread_mutex_lock(&m_BindStatuslocker);
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus *pTmp = &pTmpResource;
		if(strcmp(pTmp->strStreamType,Status_Idle)==0)
		{
			 LOG_INFO_FORMAT("INFO  - [SM]:%s %d this is Status_Idle stream\n",__FUNCTION__,__LINE__);
		}
		else if(strcmp(pTmp->strStreamType,Status_Nav)==0)
		{
			Cleanonenav(strkey_value);
			LOG_INFO_FORMAT("INFO  - [SM]:%s %d this is Status_Nav stream\n",__FUNCTION__,__LINE__);
			usleep(1000*500);
			AddOneNavStream(streamid,2);
			usleep(1000*1000);
		}
		else
		{
			CleanStream(streamid);
			LOG_INFO_FORMAT("INFO  - [SM]:%s %d this is other stream\n",__FUNCTION__,__LINE__);
			usleep(1000*1000);
			AddOneAdvStream(streamid);
			usleep(1000*1000);
		}
	}
  pthread_mutex_unlock(&m_BindStatuslocker);
	return true;
}

bool SM_Manager::Navgatinsert(int flagid)
{
	int streamtype;
	//查询导航地址的高标清标志
	DBInterfacer::GetInstance()->Findtype(flagid,streamtype,1);
	//按照区域来查询
	MapNetWorkGroup::iterator itwork = m_mapNetworkGroup.begin();
	while(itwork != m_mapNetworkGroup.end())
	{
		NetworkGroup *ptmp = itwork->second;
		int regionnum = atoi(ptmp->strNetRegionNum);
		//查询该区域下的被重用的导航流标志id
		RegNavurl::iterator iTemp = m_Navurl.find(regionnum); 
		if(iTemp != m_Navurl.end())
		{
			//将该区域下导航流被重用的list
			ListStreamNavurl iTempurl = iTemp->second;
			ListStreamNavurl::iterator tempurl = iTempurl.begin();
			while(tempurl != iTempurl.end())
			{
				SmNavurl *pTemp = *tempurl;
				//找到被重用的导航流标志id
				if(pTemp->times >0 && (pTemp->haflag == streamtype))
				{
					pTemp->times = pTemp->times-1;  //将重用次数减去1
					if(streamtype==0)
					{
						Navnext::iterator naiterList = hdm_navnext.find(regionnum);
						int j = 0;
						//判断被重用的高清导航流是否被绑定
						if(naiterList != hdm_navnext.end())
						{	//如果被绑定把即将下发的导航流修标志修改为新增的导航流标志
							ListStreamResource myhdnal = naiterList->second;
							ListStreamResource::iterator itmyhdnal = myhdnal.begin();
							while(itmyhdnal != myhdnal.end())
							{
								if(pTemp->id == *itmyhdnal)
								{
									myhdnal.erase(itmyhdnal);
									myhdnal.push_back(flagid);
									j = 1;
									break;
								}
								else
								{
									++itmyhdnal;
								}
							}
						}
						if(j==1)
						   break;
						SetGroupStream hdlistTmph;
						//如果重用的导航流没有被绑定
						DBInterfacer::GetInstance()->FindSameGroupStream(itwork->first,0,hdlistTmph);
						SetGroupStream::iterator hdlist = hdlistTmph.begin();
						while(hdlist != hdlistTmph.end())
						{	
							int streamid = *hdlist;
							Navinfo::iterator ithdnav = m_navinfo.find(streamid);
							if((ithdnav != m_navinfo.end()) && (ithdnav->second == flagid))
							{
								
								//重新下发该路导航流
								char strSeesionID[64] ={0};
								sprintf(strSeesionID,"%d",streamid);
								char strSID[64] ="1001";
								char strReSID[64] ="2000"; //operid
								char strSIP[64];
								char strSPort[32];
								memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
								sprintf(strSPort,"%d",iMSIServerPort);
								char strAreaID[32] ="3301";
								char strMsg[32]="";
										
								char initurl[500] = {0};
								char naurl[512] = {0};
								DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
								sprintf(naurl,"%s&stream_id=%s",initurl,streamid);
								Cleanonenav(strSeesionID);
								usleep(1000*1300);
								Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
								//修改m_navinfo中的标志
								ithdnav->second = flagid;
								j=1;
								break;
							}
							++hdlist;
						}
						if(j==1)
						   break;
					}
					else if(streamtype == 1)
					{
						Navnext::iterator naiterList = sdm_navnext.find(regionnum);
						int j = 0;
						if(naiterList != sdm_navnext.end())
						{
							ListStreamResource myhdnal = naiterList->second;
							ListStreamResource::iterator itmyhdnal = myhdnal.begin();
							while(itmyhdnal != myhdnal.end())
							{
								if(pTemp->id == *itmyhdnal)
								{
									myhdnal.erase(itmyhdnal);
									myhdnal.push_back(flagid);
									j = 1;
									break;
								}
								else
								{
									++itmyhdnal;
								}
							}
						}
						if(j==1)
						  break;
						SetGroupStream sdlistTmph;
						DBInterfacer::GetInstance()->FindSameGroupStream(itwork->first,1,sdlistTmph);
						SetGroupStream::iterator sdlist = sdlistTmph.begin();
						while(sdlist != sdlistTmph.end())
						{	
							int streamid = *sdlist;
							Navinfo::iterator ithdnav = m_navinfo.find(streamid);
							if((ithdnav != m_navinfo.end()) && (ithdnav->second == flagid))
							{
								
								//重新下发该路导航流
								char strSeesionID[64] ={0};
								sprintf(strSeesionID,"%d",streamid);
								char strSID[64] ="1001";
								char strReSID[64] ="2000"; //operid
								char strSIP[64];
								char strSPort[32];
								memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
								sprintf(strSPort,"%d",iMSIServerPort);
								char strAreaID[32] ="3301";
								char strMsg[32]="";
										
								char initurl[500] = {0};
								char naurl[512] = {0};
								DBInterfacer::GetInstance()->FindNavurl(flagid,initurl);
								sprintf(naurl,"%s&stream_id=%s",initurl,streamid);
								Cleanonenav(strSeesionID);
								usleep(1000*1300);
								Navstream(strSeesionID,strSID,strReSID,naurl,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
								//修改m_navinfo中的标志
								ithdnav->second = flagid;
								j=1;
								break;
							}
							++sdlist;
						}
						if(j==1)
						   break;
					}
				}
				++tempurl;
			}
		}
		++itwork;
	}
}

bool SM_Manager::Strrouseinsert(int streamid)
{
	int ipqamport;
	int ipqaminid;
	int streamtype;
	int iret = 0; 
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",streamid);
	StreamResource pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamResource *restemp = &pTmpResource;
		ipqaminid = restemp->iIPQAMNum;
		streamtype = restemp->iWherether_HD;
	}
	//为该路流分配广告流的资源
	iret = DBInterfacer::GetInstance()->LoadAlladvinfo(m_mapadvinfo,streamid);
	if(!iret)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d this stream has no advinfo\n",__FUNCTION__,__LINE__);
		return false;
	}
	//为该路流分配ipqam资源
	DBInterfacer::GetInstance()->LoadALLIPQAMInfo(m_mapIpqamInfo,ipqaminid,streamid);
	iret = DBInterfacer::GetInstance()->LoadALLIPQAMRes(ipqaminid,&ipqamport,streamid);
	if(iret)
	{
		MapIPQAMInfo::iterator myit =  m_mapIpqamInfo.find(streamid);
		if(myit != m_mapIpqamInfo.end())
		{
			IPQAMInfo *mytemp = myit->second;
			mytemp->iIpqamManagerPort = ipqamport;
		}
	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d this stream has no ipqam resource\n",__FUNCTION__,__LINE__);
		return false;
	}
	//加入到高标清标志表中
	m_allstrem.insert(Navinfo::value_type(streamid,streamtype));
	//加入到当前各个流的导航状态标志表中
	m_navinfo.insert(Navinfo::value_type(streamid,0));
	
	AddOneAdvStream(streamid);
	sleep(1000*1000);
}

bool SM_Manager::Navgatdelete(int flagid)
{
	MapNetWorkGroup::iterator itwork = m_mapNetworkGroup.begin();
	while(itwork != m_mapNetworkGroup.end())
	{
		NetworkGroup *iworktemp = itwork->second;
		int regionum = atoi(iworktemp->strNetRegionNum);
		RegNavurl::iterator navit = m_Navurl.find(regionum);//查找各个region下各个导航流被使用情况
		if(navit != m_Navurl.end())
		{
			ListStreamNavurl temlist = navit->second;      
			ListStreamNavurl temlist2 = navit->second;
			ListStreamNavurl::iterator ittemp = temlist.begin(); //开始查找被删除的导航流是否被使用
			while(ittemp != temlist.end())
			{
				SmNavurl *natemp = *ittemp;
				if(natemp->id == flagid) //被删除的导航流被使用
				{
					//先分配一个新的导航流标取代被删除的导航流
					int newid;
					DBInterfacer::GetInstance()->FindlNavid(natemp->haflag,newid);
					if(natemp->haflag == 0) //如果被删除的导航地址是高清
					{
						Navnext::iterator iit = hdm_navnext.find(regionum); 
						if(iit != hdm_navnext.end())
						{
							ListStreamResource lisit =  iit->second;
							ListStreamResource::iterator itlisthd = lisit.begin(); //存放即将下发的导航流标志
							while(itlisthd != lisit.end())
							{
								int tempid = *itlisthd;
								if(tempid == flagid)     //被删除的导航流地址被绑定
								{
									*itlisthd = newid;   //将新的导航标志赋值给该导航流标志
									ListStreamNavurl::iterator slisiit= temlist2.begin();
									while(slisiit != temlist2.end())
									{
										SmNavurl *findtemp = *slisiit;
										if(findtemp->id == newid)
										{
											findtemp->times = findtemp->times +1; //将新增导航流标志对应的使用次数加1
											break;
										}
										else
										{
											++slisiit;
										}
									}
								}
								else
								{ 
									++itlisthd;          //判断下一个标志是否是被删除的导航流标志
								}
							}
			
							SetGroupStream hdlistTmph;
							char region[32] = {0};
							sprintf(region,"%d",regionum);
							DBInterfacer::GetInstance()->FindSameGroupStream(region,0,hdlistTmph);//寻找相同region下的高清流id的集合
							SetGroupStream::iterator hdtmphit = hdlistTmph.begin();
							while(hdtmphit != hdlistTmph.end())
							{
								int streamid = *hdtmphit;
								Navinfo::iterator mithd = m_navinfo.find(streamid); //寻找该路是否是被删除的导航流
								if(mithd != m_navinfo.end())
								{
									int nowid = mithd->second;
									if(nowid == flagid)                 //如果该路是被删除的导航流
									{
										mithd->second = newid;     //修改存放在该流下的导航流标志位为新分配的导航流标志
										ListStreamNavurl::iterator islisiit= temlist2.begin();
										while(islisiit != temlist2.end())
										{
											SmNavurl *findtemp = *islisiit;
											if(findtemp->id == newid)
											{
												findtemp->times = findtemp->times +1;  //将新使用的流的使用次数加1
												break;
											}
											else
											{
												++islisiit;
											}
										}
									}
									else
									{
										++mithd;
									}
								}
								++hdtmphit;                //查询本region 下面下一个高清流的情况
							}
						}
						
					}
					//如果是标清
					else
					{
						Navnext::iterator iit = sdm_navnext.find(regionum); //在标清中查找即将下发的导航流标志是否是被删除的导航流标志
						if(iit != sdm_navnext.end())
						{
							ListStreamResource lisit =  iit->second;
							ListStreamResource::iterator itlisthd = lisit.begin();//存放即将下发的标清导航流的标志
							while(itlisthd != lisit.end())
							{
								int tempid = *itlisthd;
								if(tempid == flagid)
								{
									*itlisthd = newid;
									ListStreamNavurl::iterator slisiit= temlist2.begin();
									while(slisiit != temlist2.end())
									{
										SmNavurl *findtemp = *slisiit;
										if(findtemp->id == newid)
										{
											findtemp->times = findtemp->times +1;
											break;
										}
										else
										{
											++slisiit;
										}
									}
								}
								else
								{
									++itlisthd;
								}
							}
							
							SetGroupStream sdlistTmph;
							char region[32] = {0};
							sprintf(region,"%d",regionum);
							DBInterfacer::GetInstance()->FindSameGroupStream(region,1,sdlistTmph);
							SetGroupStream::iterator sdtmphit = sdlistTmph.begin();
							while(sdtmphit != sdlistTmph.end())
							{
								int streamid = *sdtmphit;
								Navinfo::iterator mithd = m_navinfo.find(streamid);
								if(mithd != m_navinfo.end())
								{
									int nowid = mithd->second;
									if(nowid == flagid)
									{
										mithd->second = newid;
										ListStreamNavurl::iterator islisiit= temlist2.begin();
										while(islisiit != temlist2.end())
										{
											SmNavurl *findtemp = *islisiit;
											if(findtemp->id == newid)
											{
												findtemp->times = findtemp->times +1;
												break;
											}
											else
											{
												++islisiit;
											}
										}
									}
									else
									{
										++mithd;
									}
								}
								++sdtmphit;
							}
						}
						
					}					
				}
				else
				{
				  ++ittemp;
				} 
			}
		}
		++itwork;                      //查找下一个region
	}
}

bool SM_Manager::Strrousedelete(int streamid,char *strUsername)
{
	/*
	char str_id[32] = {0};
	sprintf(str_id,"%d",streamid);
	if(strUsername)
	{
		m_Msi_SMInter->CleanMSIStream(strUsername,str_id);
	}
	*/
	
}

