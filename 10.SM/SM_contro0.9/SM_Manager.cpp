#include "SM_Manager.h"
#include "VGW_VodStream.h"
#include "MSI_SMStream.h"
#include "Advertisement.h"
#include "Navigation.h"
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
	isfull = 0;
	printf("---create Manager\n");
	fflush(stdout);
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
	//	printf("---region %s \n",CstrRegion[i].c_str());
	//	strcpy(strregionid,CstrRegion[i].c_str());
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
		printf("--region %s , port %d \n",it->second,it->first);
		fflush(stdout);
		++it;
	}
	//ÇåÀíÍê»á»°ÐèÒª½«¶ÔÓÚÊý¾Ý×´Ì¬ÐÞ¸Ä
}
SM_Manager::~SM_Manager()
{

	if(m_Navigation)
		delete m_Navigation;

	//ÊÍ·Åmap setÖÐµÄ×ÊÔ´
	pthread_mutex_destroy(&m_BindStatuslocker);
	pthread_mutex_destroy(&m_VecNewNavlocker);
}


bool SM_Manager::CleanAllStream() //char * strSeesionId,char * strSerialno
{
	MapStreamStatus::iterator it = m_mapStreamStatus.begin();
	while(it != m_mapStreamStatus.end())
	{
		//ÇåÀíµ¼º½Á÷
		char strSID[128] = "1001";
		char strReSID[128] = "1001";
		char strAuthiName[128] = "cscs";
		char strAuthcode[128] = "123456";
		char strMsg[128] = "";
		int iTaskID = 111;


		StreamStatus* pTmp = it->second;
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->istreamID);

		//ÏÈÇåÀíÒÆ¶¯Íø¹Ø
		//if(strcmp(pTmp->strBind_userID, "") != 0)
		{
			m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strSeesionID); //seesionID×÷Îªserialno
			usleep(1000*200);
		}
		
		//¸ù¾ÝÊý¾ÝÀàÐÍ½øÐÐ²»Í¬Ä£¿éµÄÇåÀí
		//if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
		{
				//char strTaskID[64]={0};
			//	sprintf(strTaskID,"%d",pTmp->iTaskID);
				m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
				usleep(1000*100);
		}
	//	else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 || 	strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
		{
			m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,strSeesionID,strMsg);
			usleep(1000*200);
		}
	//	else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
		{
			m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
			usleep(1000*200);
		}
/*		else if (strcmp(pTmp->strStreamType, Status_abnormal) == 0)
		{
			m_Navigation->CleanNavStream(strSeesionID,strSID,strReSID,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
			//
		}
*/		
		it++;
	}

	return true;
}
bool SM_Manager::CleanAllTask()
{

return true;

}
bool SM_Manager::CleanStream(int iStreamID,ModuleType emodelType)
{
//Ö»ÇåÀí¹ã¸æµ¼º½vod
	bool bNeedWait = false;

#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;
#else
	MapStreamStatus::iterator it = m_mapStreamStatus.find(iStreamID);
	if(it != m_mapStreamStatus.end())	
	{
		StreamStatus* pTmp = it->second;
#endif			
		//Ê×ÏÈÇåÀíµôÉÏÒ»Â·
				//ÇåÀíµ¼º½Á÷
		char strSID[128] = "1001";
		char strReSID[128] = "1001";
		char strAuthiName[128] = "cscs";
		char strAuthcode[128] = "123456";
		char strMsg[128] = "";
		int iTaskID = 111;
		
		 if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
		 {
				//vodÖØ¸´µã²¥
				bNeedWait = true;
		 }
		if(emodelType == Other)
		{
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);

		//	if(NULL != strUserID)
		//		strcpy(strUserID,pTmp->strBind_userID);
			//¸ù¾ÝÊý¾ÝÀàÐÍ½øÐÐ²»Í¬Ä£¿éµÄÇåÀí
			//if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
			{
					//char strTaskID[64]={0};
				//	sprintf(strTaskID,"%d",pTmp->iTaskID);
					m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
					usleep(1000*100);
			}
			//else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 ||	strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
			{
				m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
				usleep(1000*100);	
			}
			//else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
			{
				//ÖØ¸´µã²¥²»Ìø×ª¹ã¸æÁ÷
	/*			MapVodPlay::iterator itfind= m_VGWVodPlayer->m_mapVod_player.find(atoi(strSeesionID));
				if(itfind != m_VGWVodPlayer->m_mapVod_player.end())
				{
					Stream *ptmp = itfind->second;
					m_VGWVodPlayer->m_mapVod_player.erase(itfind);
					delete ptmp;
				}
	*/
				m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
				usleep(1000*100);
			}
		}
		else
		{
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);

				//¸ù¾ÝÊý¾ÝÀàÐÍ½øÐÐ²»Í¬Ä£¿éµÄÇåÀí
			if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
			{
					//char strTaskID[64]={0};
				//	sprintf(strTaskID,"%d",pTmp->iTaskID);
					m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
					usleep(1000*100);
			}
			else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 ||	strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
			{
				m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
				usleep(1000*100);
			}
			else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
			{
				//ÖØ¸´µã²¥²»Ìø×ª¹ã¸æÁ÷
	/*			MapVodPlay::iterator itfind= m_VGWVodPlayer->m_mapVod_player.find(atoi(strSeesionID));
				if(itfind != m_VGWVodPlayer->m_mapVod_player.end())
				{
					Stream *ptmp = itfind->second;
					m_VGWVodPlayer->m_mapVod_player.erase(itfind);
					delete ptmp;
				}
	*/
				m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
				usleep(1000*100);
			}


		}
	}


	return bNeedWait;
}
bool SM_Manager::CleanTask(ModuleType emodelType)
{

 return true;

}

bool SM_Manager::ClearoneStream(int iStreamID,int type)
{
	printf("begin to clean stream %d\n",iStreamID);
	fflush(stdout);
	switch(type)
	{
		case 0:
		
			CleanStream(iStreamID,Advertisement);
			usleep(1000*2000);
			break;
		case 1:
			CleanStream(iStreamID,Navigation);
			usleep(1000*2000);
			break;
		case 2:
		
			CleanStream(iStreamID,VodStream);
			usleep(1000*2000);
			break;
		
		case 3:
			CleanStream(iStreamID,MobileGateservice);
			usleep(1000*2000);
			break;
		case 4:
			CleanStream(iStreamID,Other);
			usleep(1000*2000);
			break;	
		default:
		printf("wrong number\n");
		fflush(stdout);
		break;
	}
	return true;
}

//ÏÂ·¢µ¼º½Â·Êý¼°¹ã¸æÁ÷Â·Êý
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
/*	bool CheckVodStream();
bool CheckNavgationStream();
bool VODResouceRecovery();
bool NavResouceRecovery();
*/
//×°ÔØÁ÷Í¨µÀ·Ö×é
bool SM_Manager::LoadStreamResource()
{
	m_mapStreamResource.clear();
#ifndef TEST_DATA
	DBInterfacer::GetInstance()->LoadAllStreamResource(m_mapStreamResource);
#else
		int i =0;
		while(i++<11)
			{
				StreamResource *pTmpStream = new StreamResource;
				pTmpStream->iStreamID = i;
				pTmpStream->iOutPutPort = 50438+i*2;
				pTmpStream->iPidMaping = 123;
				pTmpStream->iProgramNum = 321;
				pTmpStream->iBitRate = 30;
				pTmpStream->iIPQAMNum = i/4+1;
				char strRegNum[32]={0};
				sprintf(strRegNum,"%d",(i-1)/2 + 1);
				memcpy(pTmpStream->strNetRegionNum,strRegNum,2);
				pTmpStream->iFreqPoint = 1234;
				memcpy(pTmpStream->strComment,"222",4);
				char testurl[64] = "www.baidu.com";
				memcpy(pTmpStream->strNav_url,testurl,strlen(testurl)+1);
				pTmpStream->iIs_Need_Key = 1;
				memcpy(pTmpStream->strMobile_url,testurl,strlen(testurl)+1);
				//memcpy(pTmpStream->strSessionID,sqlrow[12],strlen(sqlrow[12]));
				//printf("\n");			
				m_mapStreamResource.insert(MapStreamResource::value_type(pTmpStream->iStreamID, pTmpStream));
			}
#endif
	
	return true;
}

//ÔØÈëÍøÂç·Ö×éÊý¾Ý
bool SM_Manager::LoadNetWorkGroup()
{
#ifndef TEST_DATA
	DBInterfacer::GetInstance()->LoadNetWorkGroup(m_mapNetworkGroup);

	//¸ù¾Ý·Ö×éÐÅÏ¢ÔØÈë·Ö×éÊý¾Ý
	MapNetWorkGroup::iterator iter = m_mapNetworkGroup.begin();

	while(iter != m_mapNetworkGroup.end())
	{
			//²éÕÒÃ¿Ò»¸ö·Ö×éµÄÊý¾Ý£¬µ¼³övector
			ListStreamResource tmplistGroupStream;

			//tmplistGroupStream.clear();
			printf("get group info %s \n",iter->first);
			fflush(stdout);
			DBInterfacer::GetInstance()->LoadOneGroupStreamResource(iter->first,tmplistGroupStream);
			if(tmplistGroupStream.size() > 0)
			{
				m_mapStreamGroup.insert(MapStreamGroup::value_type(iter->first, tmplistGroupStream));
				printf("group %s  no data \n",iter->first);
				fflush(stdout);
			}
			++iter;

			//ÐèÒª¹ØÁª·Ö×é±àºÅÓëÁ÷Í¨µÀ
	}
#else
		int i=0;
	//	while(i++<3)
			{
					i++;
					NetworkGroup *pTmpStream = new NetworkGroup;

					char txt[32]={0};
					sprintf(txt,"%d",i);
				//	memcpy(pTmpStream->strNetRegionNum,txt,strlen(txt));
					strcpy(pTmpStream->strNetRegionNum,txt);
					//memcpy(pTmpStream->strNetRegionName,"aa",2);
					strcpy(pTmpStream->strNetRegionName,"aa");
					pTmpStream->iNavgationStreamNum = 1;
					pTmpStream->iAdvertisementStreamNum = 1;
					//memcpy(pTmpStream->strNetworkComment,"qqq",3);
					strcpy(pTmpStream->strNetworkComment,"qqq");
					m_mapNetworkGroup.insert(MapNetWorkGroup::value_type(pTmpStream->strNetRegionNum, pTmpStream));

					int iStreamID = i*2;
					ListStreamResource tmplistGroupStream;
					tmplistGroupStream.push_back( iStreamID);
					tmplistGroupStream.push_back( iStreamID-1);
					m_mapStreamGroup.insert(MapStreamGroup::value_type(pTmpStream->strNetRegionNum, tmplistGroupStream));
			}
					
#endif
	
	return true;
}


bool SM_Manager::LoadStreamStatus()
{
	m_mapStreamStatus.clear();
#ifndef TEST_DATA
	DBInterfacer::GetInstance()->LoadAllStreamStatus(m_mapStreamStatus);
#else
	{
/*		StreamStatus *pTmpStream = new StreamStatus;
		memset(pTmpStream,0,sizeof(StreamStatus));
		pTmpStream->istreamID = 1;
		memcpy(pTmpStream->strStreamType,Status_Adver,strlen(Status_Adver)+1);
		memcpy(pTmpStream->strBind_userID,"123098",7);
		char tmp[64]="2014-9-7";
		memcpy(pTmpStream->strBind_date,tmp,strlen(tmp)+1);
		memcpy(pTmpStream->strSwitch_task_id,"333221",7);		
		m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));

		StreamStatus *pTmpStream1 = new StreamStatus;
		memset(pTmpStream1,0,sizeof(StreamStatus));
		pTmpStream1->istreamID = 2;
		memcpy(pTmpStream1->strStreamType,Status_Nav,strlen(Status_Nav)+1);
		memcpy(pTmpStream1->strBind_userID,"123098",7);
		char tmp1[64]="2014-9-7";
		memcpy(pTmpStream1->strBind_date,tmp1,strlen(tmp1)+1);
		memcpy(pTmpStream1->strSwitch_task_id,"3332211",8);		
		m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream1->istreamID, pTmpStream1));


		
		StreamStatus *pTmpStream2 = new StreamStatus;
		memset(pTmpStream2,0,sizeof(StreamStatus));
		pTmpStream2->istreamID = 3;
		memcpy(pTmpStream2->strStreamType,Status_Vod,strlen(Status_Vod)+1);
		memcpy(pTmpStream2->strBind_userID,"123098",7);
		char tmp2[64]="2014-9-7";
		memcpy(pTmpStream2->strBind_date,tmp2,strlen(tmp2)+1);
		memcpy(pTmpStream2->strSwitch_task_id,"33322111",9);		
		m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream2->istreamID, pTmpStream2));
*/	}

#endif
	printf("---map size =%d \n",m_mapStreamStatus.size());
	fflush(stdout);
		//echo data;
#ifdef TEST_DATA		
	MapStreamStatus::iterator it = m_mapStreamStatus.begin();
	while(it != m_mapStreamStatus.end())
	{
		StreamStatus *ptmp = it->second;
		/*
		int istreamID;
		char strStreamType[32];
		char strStatus_date[128];
		char strBind_userID[128];
		char strBind_date[128];
		char strSwitch_task_id[128];
		char strSessionID[512];
		char strSerialNo[512];

		*/
		printf("%d, %s,%s,%s,%s,%s,%s,%s, \n ",ptmp->istreamID,ptmp->strStreamType,
				ptmp->strStatus_date,ptmp->strBind_userID,ptmp->strBind_date,
				ptmp->strSwitch_task_id,ptmp->strSessionID,ptmp->strSerialNo);
		fflush(stdout);
		++it;
	}
#endif	
	return true;
}

bool SM_Manager::LoadIPQAMInfo()
{
	m_mapIpqamInfo.clear();
#ifndef TEST_DATA
	DBInterfacer::GetInstance()->LoadALLIPQAMInfo(m_mapIpqamInfo);
#else
	IPQAMInfo *pTmpStream = new IPQAMInfo;
	memset(pTmpStream,0,sizeof(IPQAMInfo));

	char strIP[64] ="192.168.100.106";
	pTmpStream->iIPQAMNum = 1;
	memcpy(pTmpStream->strIpqamName,"name1",6);
	memcpy(pTmpStream->strIpqamIP,strIP,strlen(strIP)+1);
	pTmpStream->iIpqamManagerPort = 10000;
	memcpy(pTmpStream->strIpqamType,"211",3+1);
	memcpy(pTmpStream->strIpqamModel,"A",1+1);
	memcpy(pTmpStream->strIpqamManufacturers,"eqw",3+1);
	memcpy(pTmpStream->strIpqamComment,"442",3+1);
	pTmpStream->iIsSupportR6 = 0;
	m_mapIpqamInfo.insert(MapIPQAMInfo::value_type(pTmpStream->iIPQAMNum, pTmpStream));

	pTmpStream = new IPQAMInfo;
	memset(pTmpStream,0,sizeof(IPQAMInfo));
	//char strIP[64] ="192.168.1.1";
	pTmpStream->iIPQAMNum = 2;
	memcpy(pTmpStream->strIpqamName,"name1",5+1);
	memcpy(pTmpStream->strIpqamIP,strIP,strlen(strIP)+1);
	pTmpStream->iIpqamManagerPort = 11000;
	memcpy(pTmpStream->strIpqamType,"211",3+1);
	memcpy(pTmpStream->strIpqamModel,"A",1+1);
	memcpy(pTmpStream->strIpqamManufacturers,"eqw",3+1);
	memcpy(pTmpStream->strIpqamComment,"442",3+1);
	pTmpStream->iIsSupportR6 = 0;
	m_mapIpqamInfo.insert(MapIPQAMInfo::value_type(pTmpStream->iIPQAMNum, pTmpStream));


#endif

	
	return true;
}


int SM_Manager::AddOneStream(ModuleType emodelType,int iOldStreamID,int *bAddNewStream)
{

	char strnetWork_Reg[128]={0};
	int iNewStreamID = -1;
	int iLastTimeStreamID = -1;

	bool bInStatus = false;
	char strStreamType[64]={0};
	char TmpTime[64]={0};
	bool bNewStream = false;
	strcpy(TmpTime,"9976-10-13-3:31:18");
	if(iOldStreamID > 0)
	{	//²¹·¢
		printf("find other stream from stream %d \n",iOldStreamID);
		fflush(stdout);
#ifndef USEMAP
		//MapStreamResource::iterator itfid = m_mapStreamResource.find(iOldStreamID);
		//ÏÈÕÒµ½×ÊÔ´ÐÅÏ¢
		char strkey_value[64] = {0};
		sprintf(strkey_value,"%d",iOldStreamID);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* pTmp = &pTmpResource;

			//select st *,
			//´Ó·Ö×é±íÖÐÕÒµ½
			//×ÊÔ´±ínetnum,ÏàÍ¬µÄËùÓÐÊý¾Ý£¬
			printf("find group %s \n",pTmp->strNetRegionNum);
			fflush(stdout);

#ifdef NEWFINDWAY
			//Í³Ò»ÏòÇ°²éÕÒ
			SetGroupStream listTmp;
			iret = DBInterfacer::GetInstance()->FindSameGroupStream(pTmp->strNetRegionNum,listTmp);

			SetGroupStream::iterator iterList0 = listTmp.find(iOldStreamID);
			SetGroupStream::iterator  iterflag = iterList0;
			++iterList0;
			bool bhasAdv = false;
			while(iterList0 != listTmp.end())
			{
				int iStreamID = *iterList0;
				//
				StreamStatus pTmpstatus;
				char strkey_value1[64] = {0};
				sprintf(strkey_value1,"%d",iStreamID);
				memset(&pTmpstatus,0,sizeof(pTmpstatus));
				iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpstatus);
				if(iret)
				{
					StreamStatus *ptmpstat = &pTmpstatus;
					if(strcmp(ptmpstat->strStreamType,Status_Idle)==0)
					{
						iNewStreamID = iStreamID;
						break;
					}
					else if(!bhasAdv && strcmp(ptmpstat->strStreamType,Status_Adver)==0&&strcmp(ptmpstat->strBind_userID,"")==0)
					{
						iNewStreamID = iStreamID;
						bhasAdv = true;
					}
					if(!(strcmp(ptmpstat->strStreamType,Status_Vod)==0 || 
						strcmp(ptmpstat->strStreamType,Status_Nav_Home)==0))
					{
						//×îÔ¶Ê¹ÓÃµÄÁ÷
						printf("before time comper-----status date %s \n",ptmpstat->strStatus_date);
						fflush(stdout);
						if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
						{
							printf("-----status date %s \n",ptmpstat->strStatus_date);
							fflush(stdout);
							strcpy(TmpTime,ptmpstat->strStatus_date);
							iLastTimeStreamID = iStreamID;
							strcpy(strStreamType,ptmpstat->strStreamType);
						}		
					}
				}
				else
				{
				//×´Ì¬±íÖÐÃ»ÓÐ¸ÃÁ÷Í¨µÀ£¬ËµÃ÷¿ÕÏÐÁ÷
					iNewStreamID = iStreamID;
					printf("---stream %d is unuse \n",iNewStreamID);
					fflush(stdout);
					bNewStream = true;
					break;
				}
				printf("++++++++++list data %d \n",*iterList0);
				fflush(stdout);
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
					memset(&pTmpstatus,0,sizeof(pTmpstatus));
					iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpstatus);
					if(iret)
					{
						StreamStatus *ptmpstat = &pTmpstatus;
						if(strcmp(ptmpstat->strStreamType,Status_Idle)==0)
						{
							iNewStreamID = iStreamID;
							break;
						}
						else if(!bhasAdv && strcmp(ptmpstat->strStreamType,Status_Adver)==0 && strcmp(ptmpstat->strBind_userID,"")==0)
						{
							iNewStreamID = iStreamID;
							bhasAdv = true;
						}
						else if(!(strcmp(ptmpstat->strStreamType,Status_Vod)==0 && 
								strcmp(ptmpstat->strStreamType,Status_Nav_Home)==0))
						{
							//×îÔ¶Ê¹ÓÃµÄÁ÷
							printf("before time comper-----status date %s \n",ptmpstat->strStatus_date);
							fflush(stdout);
							if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
							{
								strcpy(TmpTime,ptmpstat->strStatus_date);
								iLastTimeStreamID = iStreamID;
								strcpy(strStreamType,ptmpstat->strStreamType);
								printf("time comper-----status date %s \n",ptmpstat->strStatus_date);
								fflush(stdout);
							}		
						}
					}
					else
					{
						//×´Ì¬±íÖÐÃ»ÓÐ¸ÃÁ÷Í¨µÀ£¬ËµÃ÷¿ÕÏÐÁ÷
						iNewStreamID = iStreamID;
						printf("---stream %d is unuse \n",iNewStreamID);
						fflush(stdout);
						bNewStream = true;
						break;
			
					}
					
					printf("++++++++++list data %d \n",*iterList0);	
					fflush(stdout);
					
					++iterList0;
				}
			}
			if(-1 == iNewStreamID && iLastTimeStreamID != -1)
			{
				//Ê¹ÓÃ×îÔ¶Ê±¼äµÄÁ÷£¬°üÀ¨
				iNewStreamID = iLastTimeStreamID;
				printf("----use last time stream \n");
				fflush(stdout);
			}

#else
			ListStreamResource listTmp;
			iret = DBInterfacer::GetInstance()->LoadOneGroupStreamResource(pTmp->strNetRegionNum,listTmp);
			ListStreamResource::iterator iterList = listTmp.begin();
			char TmpTime[64]={0};
			while(iterList != listTmp.end())
			{
				int iStreamID = *iterList;
				printf("----find stream %d is can user ?\n",iStreamID);
				fflush(stdout);
				//MapStreamStatus::iterator itFind = m_mapStreamStatus.find(iStreamID);
				StreamStatus pTmpstatus;
				char strkey_value1[64] = {0};
				sprintf(strkey_value1,"%d",iStreamID);
				memset(&pTmpstatus,0,sizeof(pTmpstatus));
				iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpstatus);
				if(iret)
				{
					StreamStatus *ptmpstat = &pTmpstatus;
					//Ê×ÏÈÕÒµ½¿ÕÏÐÁ÷
					if(strcmp(ptmpstat->strStreamType,Status_Idle)==0)
					{
						
					}
					else if(strcmp(ptmpstat->strStreamType,Status_Adver)==0 && emodelType == Navigation)
					{
						//¹ã¸æÁ÷¿ÉÊ¹ÓÃµÄÁ÷
						
						iNewStreamID = iStreamID;
						printf("---find one stream adver %d \n",iNewStreamID);
						fflush(stdout);
						bInStatus = true;
						strcpy(strStreamType,ptmpstat->strStreamType);
						break;
					}
					else if(!bInStatus)//if((strcmp(ptmpstat->strBind_userID,"NULL")==0 ||
						//	strcmp(ptmpstat->strBind_userID,"")==0 ))
					{
						//×îÔ¶Ê¹ÓÃµÄÁ÷
						printf("11111before time comper-----status date %s \n",ptmpstat->strStatus_date);
						fflush(stdout);
						if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
						{
							strcpy(TmpTime,ptmpstat->strStatus_date);
							iNewStreamID = iStreamID;
							strcpy(strStreamType,ptmpstat->strStreamType);
							printf("1111time comper-----status date %s \n",ptmpstat->strStatus_date);
							fflush(stdout);
						}		
					}
				}	

				++iterList;
			}
#endif
		}

#else
		MapStreamResource::iterator itfid = m_mapStreamResource.find(iOldStreamID);
		if(itfid != m_mapStreamResource.end())
		{
			StreamResource *pTmp = itfid->second;

			//´Ó·Ö×é±íÖÐÕÒµ½
			printf("find group %s streamgroup size=%d\n",pTmp->strNetRegionNum,m_mapStreamGroup.size());
			fflush(stdout);
			//MapStreamGroup::iterator itstremfind  = m_mapStreamGroup.find(pTmp->strNetRegionNum);
			MapStreamGroup::iterator itstremfind  = m_mapStreamGroup.begin();

			while(itstremfind != m_mapStreamGroup.end())
			{
				if(strcmp(itstremfind->first,pTmp->strNetRegionNum) == 0)
				{
					printf("find same group \n");
					fflush(stdout);
					ListStreamResource listTmp = itstremfind->second;
					ListStreamResource::iterator iterList = listTmp.begin();
					char TmpTime[64]={0};
					while(iterList != listTmp.end())
					{
						int iStreamID = *iterList;
						MapStreamStatus::iterator itFind = m_mapStreamStatus.find(iStreamID);
						if(itFind != m_mapStreamStatus.end())
						{
							StreamStatus *ptmpstat = itFind->second;
							if(strcmp(ptmpstat->strStreamType,Status_Adver)==0 && emodelType == Navigation)
							{
								//¹ã¸æÁ÷¿ÉÊ¹ÓÃµÄÁ÷
								printf("---find one stream adver %d \n",iNewStreamID);
								fflush(stdout);
								iNewStreamID = iStreamID;
								bInStatus = true;
								strcpy(strStreamType,ptmpstat->strStreamType);
								break;
							}
							else if(!bInStatus)  //((strcmp(ptmpstat->strBind_userID,"NULL")==0 ||
									//strcmp(ptmpstat->strBind_userID,"")==0 ))
							{
								//×îÔ¶Ê¹ÓÃµÄÁ÷
								printf("1111before time comper-----status date %s \n",ptmpstat->strStatus_date);
								fflush(stdout);
								if(strcmp(TmpTime,ptmpstat->strStatus_date) > 0)
								{
									strcpy(TmpTime,ptmpstat->strStatus_date);
									iNewStreamID = iStreamID;
									strcpy(strStreamType,ptmpstat->strStreamType);
									printf("1111time comper-----status date %s \n",TmpTime);
									fflush(stdout);
								}		

							}
						}	

						++iterList;
					}
					
				}
				++itstremfind;
			}

		}
#endif
	}

	if(iNewStreamID == -1)
	{
		printf("Add one stream failed emodelType=%d\n",emodelType);
		fflush(stdout);
		printf("----------before isfull = %d\n",isfull);
		fflush(stdout);
		isfull+=1;
		printf("-------isfull = %d\n",isfull);
		fflush(stdout);
		return false;
	}
	printf("---the last status Time = %s find stream id=%d \n",TmpTime,iNewStreamID);
	fflush(stdout);
	if(bAddNewStream != NULL)
		*bAddNewStream = bNewStream;
//	return iNewStreamID;

	switch(emodelType)
	{
		case Advertisement:
		{
/*			if(bInStatus)
			{
				MapStreamStatus::iterator it = m_mapStreamStatus.find(iNewStreamID);
				if(it != m_mapStreamStatus.end())
				{
					StreamStatus* ptmp = it->second;
					
								//ÏÂ·¢
					char strSeesionID[64] = {0};
					sprintf(strSeesionID,"%d",ptmp->istreamID);
					printf("Add one adv stream in status \n");
					//m_Advertiser->StartOneStream("1","udp://239.1.1.1:12000","udp://192.168.100.106:50442","e123214");
				
					
				}
			}
			else
			{

				//ÕÒµ½¶ÔÓ¦Á÷ÐÅÏ¢
				MapStreamResource::iterator iterStream = m_mapStreamResource.find(iNewStreamID);
				if(iterStream != m_mapStreamResource.end())
				{
					StreamResource *pStream = iterStream->second;
					//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pStream->iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64] ="192.168.30.160";
					char strSPort[32]="12000";
					char strAreaID[32] ="3301";
					
					int iIPQAMnum = pStream->iIPQAMNum;
					printf("---ipqam info num=%d \n",iIPQAMnum);
					MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
					if(itIpqam != m_mapIpqamInfo.end())
					{
						IPQAMInfo *pIpqam = itIpqam->second;
				
						char strOutPort[64]={0};
						sprintf(strOutPort,"%d",pStream->iOutPutPort);
						char strMsg[32]="";
						//µ¼º½Á÷µÄurlÊÇ·ñÐèÆ´½Ó
						m_Navigation->StartOneStream(strSeesionID,strSID,strReSID,pStream->strNav_url,pIpqam->strIpqamIP,
									strOutPort,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
						//listTmp.erase(iterList);
						printf("---start one nav stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
								pStream->strNetRegionNum,pStream->iIPQAMNum);
					}
					
						
				}

			}*/
		}
		break;
		case Navigation:
		{
			//if(bInStatus)
			{

#ifndef USEMAP
				char strkey_value[64] = {0};
				sprintf(strkey_value,"%d",iNewStreamID);
				StreamStatus pTmpResource;
				memset(&pTmpResource,0,sizeof(pTmpResource));
				int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
				printf("---==== iret =%d \n",iret);
				fflush(stdout);
				if(iret)
				{
					StreamStatus* pTmp = &pTmpResource;		
#else
				MapStreamStatus::iterator it = m_mapStreamStatus.find(iNewStreamID);
				if(it != m_mapStreamStatus.end())
				{
					StreamStatus* pTmp = it->second;
#endif					
					//Ê×ÏÈÇåÀíµôÉÏÒ»Â·
							//ÇåÀíµ¼º½Á÷
					char strSID[128] = "1001";
					char strReSID[128] = "1001";
					char strAuthiName[128] = "cscs";
					char strAuthcode[128] = "123456";
					char strMsg[128] = "";
					int iTaskID = 111;

					//½«ÐèÒª×ª·¢µÄµ¼º½Á÷´æÔÚvector
					printf("----find new streamid %d \n",iNewStreamID);
					fflush(stdout);
					pthread_mutex_lock(&m_VecNewNavlocker);								
					m_vecNewNav.insert(iNewStreamID);
					pthread_mutex_unlock(&m_VecNewNavlocker);	
					printf("-----set size=%d \n",m_vecNewNav.size());
					fflush(stdout);
					
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pTmp->istreamID);


					//ÏÈÇåÀíÒÆ¶¯Íø¹Ø
					if(strcmp(pTmp->strBind_userID, "") != 0)
					{
						m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strSeesionID); //seesionID×÷Îªserialno
						usleep(1000*100);
					}
					
					//¸ù¾ÝÊý¾ÝÀàÐÍ½øÐÐ²»Í¬Ä£¿éµÄÇåÀí
					//if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
					{
							//char strTaskID[64]={0};
						//	sprintf(strTaskID,"%d",pTmp->iTaskID);
							m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
							usleep(1000*100);
					}
					//else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 || 	strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
					{
						m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
						usleep(1000*100);
					}
					//else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
					{
						m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
						usleep(1000*100);
					}
				}
				else if(bNewStream)
				{
					printf("---new steam\n");
					fflush(stdout);
					AddOneNavStream(iNewStreamID);
				}
				//usleep(1000*2000);

					//´´½¨Ò»Â·µ¼º½Á÷
		
			}
				
			break;
		}
		break;
		default:
		break;
	}

	return true;
}

int SM_Manager::AddOneVodStream(int iStreamID,char* strUrl,int iFd,char *vodname,char *posterurl,char* strSerilno)
{
	char strUserID[128] ={0};
	char strkey_value1[64] = {0};
 	sprintf(strkey_value1,"%d",iStreamID);
 	StreamStatus pTmpResource1;
 	memset(&pTmpResource1,0,sizeof(pTmpResource1));
 	int iret1 = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value1,&pTmpResource1);
 	if(iret1)
 	{
 	 StreamStatus* pTmp1 = &pTmpResource1;
	 memcpy(strUserID,pTmp1->strBind_userID,strlen(pTmp1->strBind_userID)+1);
 	}
	printf("5555555555555555strUserID = %s\n",strUserID);
	fflush(stdout);
	
	//ÏÈÊÍ·Å×ÊÔ´
	bool bneedwait = CleanStream(iStreamID);
	//AddOneAdvStream(iStreamID);
	//CleanStream(iStreamID,strUserID);
//	if(bneedwait)
//		usleep(1000*7000);
//	else
		usleep(VOD_play_clean);
	
	printf("----get userid %s \n",strUserID);
	fflush(stdout);

#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamResource pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamResource* pTmp = &pTmpResource;
#else
	MapStreamResource::iterator it = m_mapStreamResource.find(iStreamID);
	if(it != m_mapStreamResource.end())
	{
		StreamResource* pTmp = it->second;
#endif
		Stream *ptmpRequest = new Stream;

		ptmpRequest->m_clientSocket = iFd;
		//cJSON *pRet_root;
		ptmpRequest->pRet_root = cJSON_CreateObject();
		ptmpRequest->Requst_Json_str(2,"serialno",strSerilno);
		m_VGWVodPlayer->m_mapVod_player.insert(MapVodPlay::value_type(iStreamID,ptmpRequest));

		char strRegion[128] = {0};
		int iRegionPort = 0;
		int idex  = 0;

#ifdef 	MAPREGION

		MapRegionID::iterator it = m_mapRegionID.begin();
		while(it != m_mapRegionID.end())
		{
			printf("--region %s , port %d \n",it->second,it->first);
			fflush(stdout);
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
						printf("---region id is port used %d \n",iRegioID);
						fflush(stdout);
						break;
					}
					++itused;
				}
				pthread_mutex_unlock(&m_lockerRegion);
				if(itused == m_SetUsedRegionID.end() )
				{
					//Ã»ÓÐÔÚÊ¹ÓÃ
					strcpy(strRegion,itfid->second);
					printf("*********region %s \n",itfid->second);
					fflush(stdout);
					iRegionPort = itfid->first;
					printf("find can use region %s %d \n",strRegion,iRegionPort);
					fflush(stdout);
					break;
				}
			}
			++itfid;
		}
		if(0==iRegionPort)
		{
			//Ã»ÓÐ¿ÉÓÃµÄregion
			//return -1£»
		
			MapRegionID::iterator itfid = m_mapRegionID.begin();
				strcpy(strRegion,itfid->second);
				iRegionPort = itfid->first;
				printf("----no find can used region\n");
				fflush(stdout);
		}

#else
		idex = m_icurrentidex;
		m_icurrentidex = !m_icurrentidex;
		
		strcpy(strRegion,CstrRegion[idex]);
#endif
		
		//½«regionID¼ÓÈëµ½ÖÐ
			pthread_mutex_lock(&m_lockerRegion);
			printf("---insert one stream %d  %d into used map \n",iStreamID,iRegionPort);
			m_SetUsedRegionID.insert(SetUsedRegionID::value_type(iStreamID,iRegionPort));
			fflush(stdout);
			pthread_mutex_unlock(&m_lockerRegion);
			printf("----insert over \n");
			fflush(stdout);
			m_idex = iRegionPort;
		printf("StartOneStream5555555555555%s\n",strUserID);
		fflush(stdout);
		if(m_VGWVodPlayer->StartOneStream(iStreamID,strUrl,strRegion,strUserID,vodname,posterurl))
		{
			//³É¹¦µã²¥
		//	return 0;
		}
		else
		{
			return -1;
		}

		//´Ó°ó¶¨×´Ì¬ÒÆ³ý
/*		pthread_mutex_lock(&m_BindStatuslocker);
		MapBindOverTime::iterator itbind = m_mapBindStatus.find(iStreamID);
		if(itbind != m_mapBindStatus.end())
		{
			//ÒÆ³ý
			m_mapBindStatus.erase(itbind);
		}
		pthread_mutex_unlock(&m_BindStatuslocker);
*/

		
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->iStreamID);

		
		int iIPQAMnum = pTmp->iIPQAMNum;
#ifndef USEMAP
		char strkey_value1[64] = {0};
		sprintf(strkey_value1,"%d",iIPQAMnum);
		IPQAMInfo pTmpResource1;
		memset(&pTmpResource1,0,sizeof(pTmpResource1));
		int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
		if(iret)
		{
			IPQAMInfo* pIpqam = &pTmpResource1;
#else		
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;
#endif
			char strOutputUrl[128]={0};
			sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pTmp->iOutPutPort);

			char strInputUrl[128] ={0};

#ifdef 	MAPREGION
			sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iRegionPort);  //Ð´ËÀ	
#else
			
	//		int idex = pTmp->iStreamID % (iMulitNum-1);
			sprintf(strInputUrl,"udp://%s:%d",strAdverIP,CIRegionID[idex]);  //Ð´ËÀ
#endif
			char strMsg[32]="";
			
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);

			printf("---start one adv stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
					pTmp->strNetRegionNum,pTmp->iIPQAMNum);
			fflush(stdout);
			usleep(1000*100);
		}
		//×éinputurl
	

	}
}


bool SM_Manager::GetTaskStatus()
{
	return true;

}

bool SM_Manager::InitStream()
{
	m_icurrentidex = 0;
	m_Navigation = new Navigation_Stream(this);
	m_Advertiser = new Advertisement_Stream(this);
	m_VGWVodPlayer = new VGW_Vod_Stream(this);
	m_Msi_SMInter = new MSI_SM_Stream(this);

	//ÏÈ¿ªÆôÇåÀíÄ£Ê½
	//Ê×ÏÈµ¼Èë×´Ì¬±íÊý¾Ý·Ç¿ÕÏÐ×´Ì¬
	if(LoadStreamStatus())
	{
		//¿ªÊ¼ÇåÀí
		CleanAllStream();
		//Ä£¿é»Ø¸´´¦ÀíÓÉ¸÷¸ö¶ÔÏó´¦Àí
		usleep(InitStream_clean);
	}
	else
		printf("---load clean data error \n");
		fflush(stdout);
		
	m_Advertiser->CleanALLAdverStream();
	
	//°´×é½øÐÐ³õÊ¼»¯
	//ÏÈµ¼Èë×ÊÔ´±í
	LoadStreamResource();
	//usleep(1000);
	LoadIPQAMInfo();
	//usleep(1000);
	printf("---begin init network group\n");
	fflush(stdout);
	LoadNetWorkGroup();
//	usleep(1000);
	printf("---load success \n");
	fflush(stdout);

#ifdef TEST_DATA
	//sleep(30);
	MapStreamStatus::iterator iter = m_mapStreamStatus.begin();
	FILE *fp = fopen("result.log","wb+");
	while(iter != m_mapStreamStatus.end())
	{
		StreamStatus *ptm = iter->second;
		/*
			int istreamID;
	char strStreamType[32];
	char strStatus_date[128];
	char strBind_userID[128];
	char strBind_date[128];
	char strSwitch_task_id[128];
	char strSessionID[512];
	char strSerialNo[512];
	*/
		fprintf(fp,"%d \t %s \t %s \t %s \t %s \t %s \n",ptm->istreamID,
			ptm->strStreamType,ptm->strStatus_date,ptm->strBind_userID,ptm->strBind_date,
			ptm->strSwitch_task_id);
		fflush(fp);
		++iter;
	}

	MapStreamResource::iterator itResource = m_mapStreamResource.begin();
	while(itResource != m_mapStreamResource.end())
	{
		StreamResource *ptmp = itResource->second;
		fprintf(fp,"%d \t %d \t %s \n",ptmp->iStreamID,ptmp->iIPQAMNum,
				ptmp->strNetRegionNum);
		fflush(fp);

		++itResource;
	}
	MapStreamGroup::iterator ittmp = m_mapStreamGroup.begin();
	while(ittmp != m_mapStreamGroup.end())
	{
		ListStreamResource listTmp1 = ittmp->second;
		ListStreamResource::iterator iterList1 = listTmp1.begin();
		while(iterList1 != listTmp1.end())
		{
			printf("---group %s stream id =%d \n ",ittmp->first,*iterList1);
			fflush(stdout);
			++iterList1;
			
		}
		++ittmp;
	}
	
#endif
	
	
	//×Ü¹²·Ö×ém_mapNetworkGroupÖÐ
	//¹ØÁªÁ÷ÐÅÏ¢×éÔÚm_mapStreamGroupÖÐ
	printf("StreamResoure size=%d ,status size=%d ,group size=%d ,streamGroup size=%d \n",
		m_mapStreamResource.size(),m_mapStreamStatus.size(),m_mapNetworkGroup.size(),m_mapStreamGroup.size());
	fflush(stdout);
	
	MapStreamGroup::iterator iterGroup = m_mapStreamGroup.begin();
	bool bHasCreateStream = false;
	while(iterGroup != m_mapStreamGroup.end() )
	{
		int iNavtaionNum = 0;
		int iAdverNum = 0;;
		MapNetWorkGroup::iterator itwork = m_mapNetworkGroup.find(iterGroup->first);
		if(itwork != m_mapNetworkGroup.end())
		{
			NetworkGroup *ptmp = itwork->second;
			iNavtaionNum = ptmp->iNavgationStreamNum;
			iAdverNum = ptmp->iAdvertisementStreamNum;

			//ListStreamResource *plistTmp = &(iterGroup->second);
			ListStreamResource listTmp = iterGroup->second;
			

			int iGroupSize = listTmp.size();
			iNavtaionNum =  (iNavtaionNum <= iGroupSize) ?  iNavtaionNum : iGroupSize;

			printf("----begin group %s -- add Nav num=%d listsize=%d\n",ptmp->strNetRegionNum,
					iNavtaionNum,iGroupSize);
			fflush(stdout);
			ListStreamResource::iterator iterList = listTmp.begin();
			//¿ªÊ¼ÏÂ·¢Á÷
			while(iNavtaionNum-- > 0 && iterList != listTmp.end())
			{

				//µ¼º½Á÷
				int iStreamID = *iterList;
				printf("----streamid=%d ready\n",iStreamID);
				fflush(stdout);
				//ÕÒµ½¶ÔÓ¦Á÷ÐÅÏ¢
				MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
				if(iterStream != m_mapStreamResource.end())
				{
					StreamResource *pStream = iterStream->second;
					//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pStream->iStreamID);
					char strSID[64] ="1001";
					char strReSID[64] ="2000"; //operid
					char strSIP[64];
					char strSPort[32];
					memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
					sprintf(strSPort,"%d",iMSIServerPort);
					char strAreaID[32] ="3301";
					
					int iIPQAMnum = pStream->iIPQAMNum;
					printf("---ipqam info num=%d \n",iIPQAMnum);
					fflush(stdout);
					MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
					if(itIpqam != m_mapIpqamInfo.end())
					{
						IPQAMInfo *pIpqam = itIpqam->second;

						char strOutPort[64]={0};
						sprintf(strOutPort,"%d",pStream->iOutPutPort);
						char strMsg[32]="";
						//µ¼º½Á÷µÄurlÊÇ·ñÐèÆ´½Ó
						m_Navigation->StartOneStream(strSeesionID,strSID,strReSID,pStream->strNav_url,pIpqam->strIpqamIP,
									strOutPort,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
						listTmp.erase(iterList);
						printf("---start one nav stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
								pStream->strNetRegionNum,pStream->iIPQAMNum);
						fflush(stdout);
						usleep(1000*100);
					}
					
						
				}
				//++iterList;
				iterList = listTmp.begin();
			}

			iGroupSize = listTmp.size();
			iAdverNum =	iAdverNum <= iGroupSize ?  iAdverNum : iGroupSize;

			iterList = listTmp.begin();
			printf("----begin group %s adv %d groupsize=%d \n",ptmp->strNetRegionNum,
					iAdverNum,iGroupSize);
			fflush(stdout);
			while(iAdverNum-- > 0 && iterList != listTmp.end())
			{
				//µ¼º½Á÷
				int iStreamID = *iterList;
				//ÕÒµ½¶ÔÓ¦Á÷ÐÅÏ¢
				MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
				if(iterStream != m_mapStreamResource.end())
				{
					StreamResource *pStream = iterStream->second;
					//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
					char strSeesionID[64] ={0};
					sprintf(strSeesionID,"%d",pStream->iStreamID);
					
					int iIPQAMnum = pStream->iIPQAMNum;
					MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
					if(itIpqam != m_mapIpqamInfo.end())
					{
						IPQAMInfo *pIpqam = itIpqam->second;

						char strOutputUrl[128]={0};
						sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pStream->iOutPutPort);

						char strInputUrl[128] ={0};
						int idex = pStream->iStreamID % (iMulitNum-1);
						strcpy(strInputUrl,strVLCMulitURL[idex]);
						char strMsg[32]="";
						m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);
						listTmp.erase(iterList);
						printf("---start one adv stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
								pStream->strNetRegionNum,pStream->iIPQAMNum);
						fflush(stdout);
						usleep(1000*100);
					}
					
						
				}
				//++iterList;
				iterList = listTmp.begin();
			}
			iterGroup++;
			//reload group
		//	char strGroupID[128]={0};
		//	strcpy(strGroupID,iterGroup->first);
		//	m_mapStreamGroup.erase(iterGroup++);
		//	m_mapStreamGroup.insert(MapStreamGroup::value_type(strGroupID, listTmp));
			
		}
		else
			++iterGroup;

	}

	//´´½¨°ó¶¨³¬Ê±¼ì²âÏß³Ì
//	pthread_t check_thread;
//	pthread_create(&check_thread, NULL, Check_BindOverTime_thread, this);
//	pthread_detach(check_thread);
	
	return true;
}

int SM_Manager::Bind_OneStream(int iStreamid,char *strUserID,char* strToken,int *iChannelInfo)
{
	printf("---begin bind onestream\n");
	fflush(stdout);
	//Ö÷ÒªÊÇ¸üÐÂÊý¾Ý¿â£¬²¹·¢µ¼º½Á÷
	//ÕÒµ½¶ÔÓ¦Êý¾Ý

#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamid);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;
#else	
 	MapStreamStatus::iterator itfind = m_mapStreamStatus.find(iStreamid);
	if(itfind != m_mapStreamStatus.end())
	{
		StreamStatus *pTmp = itfind->second;
#endif		
		
		if(strcmp(pTmp->strBind_userID,strUserID) ==0 )
		{
			printf("----bind again \n");
			fflush(stdout);
			return -1;
		}
		else if(strcmp(pTmp->strStreamType,Status_Adver)==0 || strcmp(pTmp->strStreamType,Status_Idle)==0 ||
			(strcmp(pTmp->strBind_userID,"NULL") !=0 && strcmp(pTmp->strBind_userID,"") !=0))
		{
			//ÒÑ¾­ÓÐ°ó¶¨ÓÃ»§
			//·µ»ØÊ§°Ü
			printf("***********bind error \n");
			fflush(stdout);
			return -1;
		}
		//
		time_t timep; 
		time (&timep); 
		struct tm* tmpTime = gmtime(&timep);
		char nowTime[128]={0};
		// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
		// ÔÂÈÕÊ±·ÖÃë
		sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
			tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
		printf("--time =%s \n",nowTime);
		fflush(stdout);

		//½«°ó¶¨Êý¾Ý·ÅÈë×´Ì¬±íÖÐ
/*		pthread_mutex_lock(&m_BindStatuslocker);
		m_mapBindStatus.insert(MapBindOverTime::value_type(iStreamid,timep));
		pthread_mutex_unlock(&m_BindStatuslocker);
*/		
		strcpy(pTmp->strBind_date,nowTime);
		strcpy(pTmp->strBind_userID,strUserID);
		strcpy(pTmp->strStreamType,Status_Nav_Home);
		//strcpy(pTmp->strSwitch_task_id,strToken);

		//×´Ì¬Ê±¼äÐÞ¸ÄÒ»´ÎÔò¸üÐÂ
		strcpy(pTmp->strStatus_date,nowTime);

		DBInterfacer::GetInstance()->update_table(1,pTmp);

		printf("bind one stream success\n",iStreamid);
		fflush(stdout);
		
//  xinzhen
#ifndef USEMAP
		char strkey_value[64] = {0};
		sprintf(strkey_value,"%d",iStreamid);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* tmpResource = &pTmpResource;
				
#else
		MapStreamResource::iterator itResource = m_mapStreamResource.find(iStreamid);
		if(itResource != m_mapStreamResource.end())
		{
			StreamResource *tmpResource = itResource->second;
#endif			
			tmpResource->iBind_state = 1;

			*iChannelInfo = tmpResource->iChannel_info;
			//°ó¶¨Ò»Â·Á÷
			//DBInterfacer::GetInstance()->update_table(4,tmpResource);
		}
		//³É¹¦Ö®ºóÐèÒª²¹·¢Ò»Â·µ¼º½Á÷
		//Ê×ÏÈÑ°ÕÒ¿ÉÓÃµÄ
	//	AddOneStream(Navigation);
	}
	else
	{
		//²»´æÔÚµÄÁ÷·µ»ØÒì³£
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
			//´Ó·Ö×é±íÖÐÕÒµ½
			MapStreamGroup::iterator itstremfind  = m_mapStreamGroup.find(pTmp->strNetRegionNum);
			if(itstremfind != m_mapStreamGroup.end())
			{
				ListStreamResource listTmp = itstremfind->second;
				//ListStreamResource::iterator iterList = listTmp.begin();
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
			//´Ó·Ö×é±íÖÐÕÒµ½
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
				//listTmp.push_back( iStreamID);
			}

		}
		
	}
	return true;
}

int SM_Manager::AddOneNavStream(int iStreamID)
{
	int ret = -1;
	printf("------add one nav \n");
	fflush(stdout);
		//´´½¨Ò»Â·µ¼º½Á÷
#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamResource pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamResource* pStream = &pTmpResource;

#else
	MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
	if(iterStream != m_mapStreamResource.end())
	{
		StreamResource *pStream = iterStream->second;
#endif
		//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pStream->iStreamID);
		char strSID[64] ="1001";
		char strReSID[64] ="2000"; //operid
		char strSIP[64];
		char strSPort[32];
		memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
		sprintf(strSPort,"%d",iMSIServerPort);
		char strAreaID[32] ="3301";
		
		int iIPQAMnum = pStream->iIPQAMNum;
		printf("---ipqam info num=%d \n",iIPQAMnum);
		fflush(stdout);
#ifndef USEMAP
		char strkey_value1[64] = {0};
		sprintf(strkey_value1,"%d",iIPQAMnum);
		IPQAMInfo pTmpResource1;
		memset(&pTmpResource1,0,sizeof(pTmpResource1));
		int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
		if(iret)
		{
			IPQAMInfo* pIpqam = &pTmpResource1;
#else
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;
#endif
			char strOutPort[64]={0};
			sprintf(strOutPort,"%d",pStream->iOutPutPort);
			char strMsg[32]="";
			//µ¼º½Á÷µÄurlÊÇ·ñÐèÆ´½Ó
			m_Navigation->StartOneStream(strSeesionID,strSID,strReSID,pStream->strNav_url,pIpqam->strIpqamIP,
						strOutPort,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
			//listTmp.erase(iterList);
			printf("---start one nav stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
					pStream->strNetRegionNum,pStream->iIPQAMNum);
			fflush(stdout);
		}
		ret = 0;
		
	}

	return ret;
}

bool SM_Manager::VodStreamOver(int iStreamID,char *username)
{
	//¿ªÆôÒ»Â·ÐÂµÄµ¼º½Á÷
	//ÏÈÇåÀí
	CleanStream(iStreamID);
	
	usleep(VodStreamOver_clean);
	ListStreamResource::iterator iterList = m_ispause.begin();
	while(iterList != m_ispause.end())
	{
		if(*iterList == iStreamID)
		{
			printf("---to erase the quit stream\n");
			fflush(stdout);
			m_ispause.erase(iterList);
			break;
		}
		++iterList;
	}
	int ret = -1;
	printf("------add one nav \n");
	fflush(stdout);
				//´´½¨Ò»Â·µ¼º½Á÷
#ifndef USEMAP
			char strkey_value[64] = {0};
			char newurl[128] = {0};
			sprintf(newurl,"http://192.168.100.11:8181/NS/subinter1.jsp?username=%s&streamid=%d&VodStreamOver=%s",username,iStreamID,"goback");
			sprintf(strkey_value,"%d",iStreamID);
			StreamResource pTmpResource;
			memset(&pTmpResource,0,sizeof(pTmpResource));
			int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
			if(iret)
			{
				StreamResource* pStream = &pTmpResource;
		
#else
			MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
			if(iterStream != m_mapStreamResource.end())
			{
				StreamResource *pStream = iterStream->second;
#endif
				//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
				char strSeesionID[64] ={0};
				sprintf(strSeesionID,"%d",pStream->iStreamID);
				char strSID[64] ="1001";
				char strReSID[64] ="2000"; //operid
				char strSIP[64];
				char strSPort[32];
				memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
				sprintf(strSPort,"%d",iMSIServerPort);
				char strAreaID[32] ="3301";
				
				int iIPQAMnum = pStream->iIPQAMNum;
				printf("---ipqam info num=%d \n",iIPQAMnum);
				fflush(stdout);
#ifndef USEMAP
				char strkey_value1[64] = {0};
				sprintf(strkey_value1,"%d",iIPQAMnum);
				IPQAMInfo pTmpResource1;
				memset(&pTmpResource1,0,sizeof(pTmpResource1));
				int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
				if(iret)
				{
					IPQAMInfo* pIpqam = &pTmpResource1;
#else
				MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
				if(itIpqam != m_mapIpqamInfo.end())
				{
					IPQAMInfo *pIpqam = itIpqam->second;
#endif
					char strOutPort[64]={0};
					sprintf(strOutPort,"%d",pStream->iOutPutPort);
					char strMsg[32]="";
					
					//µ¼º½Á÷µÄurlÊÇ·ñÐèÆ´½Ó
					printf("1111111111111%s\n",pStream->strNav_url);
					fflush(stdout);
					//memset(pStream->strNav_url,0,sizeif(pStream->strNav_url));
					strcpy(pStream->strNav_url,newurl);
					printf("2222222222222%s\n",pStream->strNav_url);
					fflush(stdout);
					m_Navigation->StartOneStream(strSeesionID,strSID,strReSID,pStream->strNav_url,pIpqam->strIpqamIP,
								strOutPort,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
					//listTmp.erase(iterList);
					printf("---start one nav stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
							pStream->strNetRegionNum,pStream->iIPQAMNum);
					fflush(stdout);
				}
				ret = 0;
				
			}
		//½«¹ã¸æÁ÷´æÈëset
		//m_SetPauseVod.insert(iStreamID);
		//Æô¶¯¹ã¸æÁ÷
		//AddOneAdvStream(iStreamID);
	
		return true;
}

int SM_Manager::AddOneAdvStream(int iStreamID)
{
	if(isfull>0)
	{
#ifndef USEMAP	
		printf("-------this is isfull = %d\n",isfull);
		fflush(stdout);
		StreamStatus pTmpResource;
		char username[128];
		char streamid[64];
		sprintf(streamid,"%d",iStreamID);
		int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",streamid,&pTmpResource);
		if(iret)
		{
			StreamStatus* pTmp = &pTmpResource;
#else
			MapStreamStatus::iterator it = m_mapStreamStatus.find(iStreamID);
			if(it != m_mapStreamStatus.end())
			{
				StreamStatus* pTmp = it->second;
#endif		
				m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,streamid);
				
			}
		AddOneNavStream(iStreamID);
		isfull--;
		return true;
	}
#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamResource pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamResource* pStream = &pTmpResource;

#else
	MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
	if(iterStream != m_mapStreamResource.end())
	{
		StreamResource *pStream = iterStream->second;
#endif
		//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pStream->iStreamID);
		
		int iIPQAMnum = pStream->iIPQAMNum;
#ifndef USEMAP
		char strkey_value1[64] = {0};
		sprintf(strkey_value1,"%d",iIPQAMnum);
		IPQAMInfo pTmpResource1;
		memset(&pTmpResource1,0,sizeof(pTmpResource1));
		int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
		if(iret)
		{
			IPQAMInfo* pIpqam = &pTmpResource1;
#else		
		MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
		if(itIpqam != m_mapIpqamInfo.end())
		{
			IPQAMInfo *pIpqam = itIpqam->second;
#endif
			char strOutputUrl[128]={0};
			sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pStream->iOutPutPort);

			char strInputUrl[128] ={0};
			int idex = pStream->iStreamID % (iMulitNum-1);
			strcpy(strInputUrl,strVLCMulitURL[idex]);
			char strMsg[32]="";
			m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID);

			printf("---start one adv stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
					pStream->strNetRegionNum,pStream->iIPQAMNum);
			fflush(stdout);
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
		usleep(1000*1000); // 1Ãë¼ì²â
		time_t tmNow;
		time(&tmNow);
		pthread_mutex_lock(&this0->m_BindStatuslocker);
		MapBindOverTime::iterator it = this0->m_mapBindStatus.begin();
		while(it != this0->m_mapBindStatus.end())
		{
			
			double difTime = difftime(tmNow,it->second);
			
			if(difTime > iBindOverTime)
			{
				//°ó¶¨³¬Ê±
				printf("*****bind dif time %f  ,stream =%d ,\n",difTime,it->first);
				fflush(stdout);
				this0->BindOverTime(it->first);
				//ÒÆ³ý
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
	//ÊÍ·Åµ¼º½Á÷
	CleanStream(iStreamID);
	//´´½¨¹ã¸æÁ÷ ´Ë¹ý³Ì²»±Ø½â°
	
	usleep(BindOverTime_clean);
	AddOneAdvStream(iStreamID);
	return true;
}
int SM_Manager::AfterBindAddStream(int iNewStreamID,bool bNewStream)
{
#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iNewStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	printf("---==== iret =%d \n",iret);
	fflush(stdout);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource; 	
#else
	MapStreamStatus::iterator it = m_mapStreamStatus.find(iNewStreamID);
	if(it != m_mapStreamStatus.end())
	{
		StreamStatus* pTmp = it->second;
#endif					
			//Ê×ÏÈÇåÀíµôÉÏÒ»Â·
			//ÇåÀíµ¼º½Á÷
			char strSID[128] = "1001";
			char strReSID[128] = "1001";
			char strAuthiName[128] = "cscs";
			char strAuthcode[128] = "123456";
			char strMsg[128] = "";
			int iTaskID = 111;

											//½«ÐèÒª×ª·¢µÄµ¼º½Á÷´æÔÚvector
			printf("----find new streamid %d \n",iNewStreamID);
			fflush(stdout);
			pthread_mutex_lock(&m_VecNewNavlocker); 							
			m_vecNewNav.insert(iNewStreamID);
			pthread_mutex_unlock(&m_VecNewNavlocker);	
			printf("-----set size=%d \n",m_vecNewNav.size());
			fflush(stdout);
			
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->istreamID);


			//ÏÈÇåÀíÒÆ¶¯Íø¹Ø
			if(strcmp(pTmp->strBind_userID, "") != 0)
			{
				m_Msi_SMInter->CleanMSIStream(pTmp->strBind_userID,strSeesionID); //seesionID×÷Îªserialno
				usleep(1000*100);
			}
			
			//¸ù¾ÝÊý¾ÝÀàÐÍ½øÐÐ²»Í¬Ä£¿éµÄÇåÀí
			//if (strcmp(pTmp->strStreamType, Status_Adver) == 0)
			{
					//char strTaskID[64]={0};
				//	sprintf(strTaskID,"%d",pTmp->iTaskID);
					m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
					usleep(1000*100);
			}
			//else if (strcmp(pTmp->strStreamType, Status_Nav) == 0 ||	strcmp(pTmp->strStreamType, Status_Nav_Home) == 0)
			{
				m_Navigation->CleanNavStream(strSeesionID,strSID,pTmp->strSwitch_task_id,strAuthiName,strAuthcode,pTmp->strSerialNo,strMsg);
				usleep(1000*100);
			}
			//else if (strcmp(pTmp->strStreamType, Status_Vod) == 0 )
			{
				m_VGWVodPlayer->CleanVODStream(strSeesionID,pTmp->strSwitch_task_id,strSeesionID);
				usleep(1000*100);
			}
		}
		else if(bNewStream)
		{
			printf("---new steam\n");
			fflush(stdout);
			AddOneNavStream(iNewStreamID);
		}
		//usleep(1000*2000);
	
						//´´½¨Ò»Â·µ¼º½Á÷


	return true;
}


bool SM_Manager::PauseVOD(int iStreamID,char *iUsername,char *vodname,char *posterurl,int temp)
{
	//Ê×ÏÈ¸ÃÁ÷³Ì²»Ó¦¸ÃÊÍ·Å  VOD  ×ÊÔ´£¬Ö»Ôö¼Ó¹ã¸æÁ÷×ÊÔ´
	//Ôö¼ÓµÄ¹ã¸æÁ÷ÐèÒªÔÚ»Ö¸´Ê±É¾³ý¡£²¢ÇÒ¸Ã¹ý³Ì×´Ì¬²»Ó¦¸ÃÐÞ¸Ä
	//ÊÍ·ÅÇÐÁ÷Æ÷×ÊÔ´
	
//	CleanOneAdvStream(iStreamID);
//	usleep(PauseVOD_clean);
	
	int ret = -1;
		printf("------add one nav \n");
		fflush(stdout);
			//´´½¨Ò»Â·µ¼º½Á÷
#ifndef USEMAP
		char strkey_value[64] = {0};
		char newurl[128] = {0};
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
					m_Advertiser->CleanAdverStream(istrkey_value,mpTmp->strSwitch_task_id,mpTmp->strSerialNo);
					m_SetPauseVod.insert(iStreamID);
				}
				else if(strcmp(mpTmp->strStreamType,Status_Nav_Home)==0)
				{
					char strSID[128] = "1001";
					char strReSID[128] = "1001";
					char strAuthiName[128] = "cscs";
					char strAuthcode[128] = "123456";
					char strMsg[128] = "";
					printf("----this is Status_Nav_Home\n");
					m_Navigation->CleanNavStream(istrkey_value,strSID,mpTmp->strSwitch_task_id,strAuthiName,strAuthcode,mpTmp->strSerialNo,strMsg);					
					if(1==temp)
					{
						printf("---to push back the quit stream\n");
						fflush(stdout);
						m_ispause.push_back(iStreamID);
					}
				}
			}
			usleep(PauseVOD_clean);
			printf("----this is the quit\n");
			fflush(stdout);
			//sprintf(newurl,"http://192.168.100.11:8181/NS/subinter1.jsp?username=%s&streamid=%d&VodStreamOver=%s",iUsername,iStreamID,"goback");
			if(1==temp)
			{	
				printf("---temp = %d\n",temp);
				fflush(stdout);
				sprintf(newurl,"http://192.168.100.11:8181/NS/exit.jsp");
			}
			else if(2==temp)
			{
				printf("---temp = %d\n",temp);
				fflush(stdout);
				sprintf(newurl,"http://192.168.100.11:8181/NS/pause.jsp?username=%s&streamid=%d&vodname=%s&posterurl=%s",iUsername,iStreamID,vodname,posterurl);
			}
		}
		else
		{
			CleanOneAdvStream(iStreamID);
			m_SetPauseVod.insert(iStreamID);
			usleep(PauseVOD_clean);
			printf("----now the vod is pause\n");
			fflush(stdout);
			sprintf(newurl,"http://192.168.100.11:8181/NS/pause.jsp?username=%s&streamid=%d&vodname=%s&posterurl=%s",iUsername,iStreamID,vodname,posterurl);
			//sprintf(newurl,"http://192.168.100.11:8181/NS/pause.jsp?username=%s&streamid=%d",iUsername,iStreamID);
		}
		sprintf(strkey_value,"%d",iStreamID);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* pStream = &pTmpResource;
	
#else
		MapStreamResource::iterator iterStream = m_mapStreamResource.find(iStreamID);
		if(iterStream != m_mapStreamResource.end())
		{
			StreamResource *pStream = iterStream->second;
#endif
			//µ÷ÓÃÏÂ·¢Ò»Â·µ¼º½Á÷
			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pStream->iStreamID);
			char strSID[64] ="1001";
			char strReSID[64] ="2000"; //operid
			char strSIP[64];
			char strSPort[32];
			memcpy(strSIP,strMyServerIP,strlen(strMyServerIP)+1);
			sprintf(strSPort,"%d",iMSIServerPort);
			char strAreaID[32] ="3301";
			
			int iIPQAMnum = pStream->iIPQAMNum;
			printf("---ipqam info num=%d \n",iIPQAMnum);
			fflush(stdout);
#ifndef USEMAP
			char strkey_value1[64] = {0};
			sprintf(strkey_value1,"%d",iIPQAMnum);
			IPQAMInfo pTmpResource1;
			memset(&pTmpResource1,0,sizeof(pTmpResource1));
			int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
			if(iret)
			{
				IPQAMInfo* pIpqam = &pTmpResource1;
#else
			MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
			if(itIpqam != m_mapIpqamInfo.end())
			{
				IPQAMInfo *pIpqam = itIpqam->second;
#endif
				char strOutPort[64]={0};
				sprintf(strOutPort,"%d",pStream->iOutPutPort);
				char strMsg[32]="";
				
				//µ¼º½Á÷µÄurlÊÇ·ñÐèÆ´½Ó
				printf("1111111111111%s\n",pStream->strNav_url);
				fflush(stdout);
				//memset(pStream->strNav_url,0,sizeif(pStream->strNav_url));
				strcpy(pStream->strNav_url,newurl);
				printf("2222222222222%s\n",pStream->strNav_url);
				fflush(stdout);
				m_Navigation->StartOneStream(strSeesionID,strSID,strReSID,pStream->strNav_url,pIpqam->strIpqamIP,
							strOutPort,strSIP,strSPort,strAreaID,strSeesionID,strMsg);
				//listTmp.erase(iterList);
				printf("---start one nav stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
						pStream->strNetRegionNum,pStream->iIPQAMNum);
				fflush(stdout);
			}
			ret = 0;
		}
	//½«¹ã¸æÁ÷´æÈëset
//	m_SetPauseVod.insert(iStreamID);
	//Æô¶¯¹ã¸æÁ÷
	//AddOneAdvStream(iStreamID);

	
	return true;
}

bool SM_Manager::RecoverVodPlay(int iStreamID,char *username,char *vodname,char *posterurl)
{
	ListStreamResource::iterator iterList = m_ispause.begin();
	while(iterList != m_ispause.end())
	{
		if(*iterList == iStreamID)
		{
			printf("---to recovery to the pause page\n");
			fflush(stdout);
			PauseVOD(iStreamID,username,vodname,posterurl,2);
			m_ispause.erase(iterList);
			return true;
			break;
		}
		++iterList;
	}
	//É¾³ýÔ­À´µÄµ¼º½Á÷
	CleanStream(iStreamID,Navigation);
	usleep(1000*2000);
	//ÇÐÁ÷Æ÷ÇÐ»»¶ÔÓ¦µÄVOD µã²¥
	//ÐèÒª×é×°³öÊäÈëµØÖ·£¬Êä³öµØÖ·
#ifndef USEMAP
		char strkey_value[64] = {0};
		sprintf(strkey_value,"%d",iStreamID);
		StreamResource pTmpResource;
		memset(&pTmpResource,0,sizeof(pTmpResource));
		int iret = DBInterfacer::GetInstance()->FindOneStream(4,"iStreamID",strkey_value,&pTmpResource);
		if(iret)
		{
			StreamResource* pTmp = &pTmpResource;
#else
		MapStreamResource::iterator it = m_mapStreamResource.find(iStreamID);
		if(it != m_mapStreamResource.end())
		{
			StreamResource* pTmp = it->second;
#endif
			Stream *ptmpRequest = new Stream;
	
			int idex = m_icurrentidex;
			m_icurrentidex = !m_icurrentidex;
			char strRegion[128] = {0};
			strcpy(strRegion,CstrRegion[idex]);

			char strSeesionID[64] ={0};
			sprintf(strSeesionID,"%d",pTmp->iStreamID);
	
			
			int iIPQAMnum = pTmp->iIPQAMNum;
#ifndef USEMAP
			char strkey_value1[64] = {0};
			sprintf(strkey_value1,"%d",iIPQAMnum);
			IPQAMInfo pTmpResource1;
			memset(&pTmpResource1,0,sizeof(pTmpResource1));
			int iret = DBInterfacer::GetInstance()->FindOneStream(5,"iIPQAMNum",strkey_value1,&pTmpResource1);
			if(iret)
			{
				IPQAMInfo* pIpqam = &pTmpResource1;
#else		
			MapIPQAMInfo::iterator itIpqam = m_mapIpqamInfo.find(iIPQAMnum);
			printf("1111111111111111111111 this is RecoverVodPlay\n");
			fflush(stdout);
			if(itIpqam != m_mapIpqamInfo.end())
			{
				IPQAMInfo *pIpqam = itIpqam->second;
#endif
				char strOutputUrl[128]={0};
				sprintf(strOutputUrl,"udp://%s:%d",pIpqam->strIpqamIP,pTmp->iOutPutPort);
				printf("-----12345678llll\n");
				fflush(stdout);
				SetUsedRegionID::iterator itregion = m_SetUsedRegionID.find(iStreamID);
				if(itregion != m_SetUsedRegionID.end())
				{
					printf("-------oh my god\n");
					fflush(stdout);
					int  iregionport = itregion->second;
					char strInputUrl[128] ={0};
	           	//	int idex = pTmp->iStreamID % (iMulitNum-1);
					sprintf(strInputUrl,"udp://%s:%d",strAdverIP,iregionport);
				//sprintf(strInputUrl,"udp://%s:%d",strAdverIP,CIRegionID[idex]);  //Ð´ËÀ
					char strMsg[32]="";
					m_Advertiser->StartOneStream(strSeesionID,strInputUrl,strOutputUrl,strSeesionID,1);
					
					printf("---start one adv stream %s,group id=%s,ipqamnum=%d\n",strSeesionID,
						pTmp->strNetRegionNum,pTmp->iIPQAMNum);
					fflush(stdout);
					usleep(1000*100);
				}
			}
			//×éinputurl
		}
	return true;
}


bool SM_Manager::CleanOneAdvStream(int iStreamID)
{
	
#ifndef USEMAP
	char strkey_value[64] = {0};
	sprintf(strkey_value,"%d",iStreamID);
	StreamStatus pTmpResource;
	memset(&pTmpResource,0,sizeof(pTmpResource));
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
	if(iret)
	{
		StreamStatus* pTmp = &pTmpResource;
#else
	MapStreamStatus::iterator it = m_mapStreamStatus.find(iStreamID);
	if(it != m_mapStreamStatus.end())	
	{
		StreamStatus* pTmp = it->second;
#endif			

		char strSeesionID[64] ={0};
		sprintf(strSeesionID,"%d",pTmp->istreamID);

		m_Advertiser->CleanAdverStream(strSeesionID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
		usleep(1000*100);

	}
	return true;
}

bool SM_Manager::Tellcums(char *username)
{
	m_Msi_SMInter->TellMI(username);
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
		printf("--time =%s \n",nowTime);
		fflush(stdout);
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
	printf("666666666666666666begin to insert\n");
	fflush(stdout);
	DBInterfacer::GetInstance()->insert_table(2,user);
}


bool SM_Manager::Bindagain(char *iUsername,int iiStreamid)
{
	StreamStatus pTmpResource;
	int iStreamid;
	int iret = DBInterfacer::GetInstance()->FindOneStream(1,"strBind_userID",iUsername,&pTmpResource);
	if(!iret)
	{
		printf("00000000000000first bind\n");
		fflush(stdout);
		return true;
	}
	else
	{
		StreamStatus* pTmp = &pTmpResource;
		if(pTmp->istreamID == iiStreamid)
		{
			printf("0000000000000000bind the sameone\n");
			fflush(stdout);
			return true;
		}
		iStreamid = pTmp->istreamID;
		printf("0000000000000000000000000istreamid = %d\n",iStreamid);
		fflush(stdout);
		CleanStream(iStreamid);
		usleep(1000*1500);
		strcpy(pTmp->strBind_userID,"");
		printf("pTmp->strBind_userID = %s\n",pTmp->strBind_userID);
		fflush(stdout);
		memset(pTmp->strBind_userID,0,128);
		DBInterfacer::GetInstance()->update_table(1,pTmp);
		usleep(1000*1000);
		AddOneAdvStream(iStreamid);
		return true;
	}
}

bool SM_Manager::Findcsaddr(char *csip,char *csport,int iStreamID)
{
	MapCsaddr::iterator itCsaddr = m_mapCsaddr.find(iStreamID);
	if(itCsaddr != m_mapCsaddr.end())
	{
		Csaddr *paddr = itCsaddr->second;
		printf("paddr->csip = %s\n",paddr->csip);
		fflush(stdout);
		printf("paddr->csport = %s\n",paddr->csport);
		fflush(stdout);
		memcpy(csip,paddr->csip,strlen(paddr->csip)+1);
		memcpy(csport,paddr->csport,strlen(paddr->csport)+1);
	}
		
}

bool SM_Manager::Checksession()
	
{
/*
	int i = 1;
	StreamStatus pTmpResource;
	StreamStatus *pTmp;
	char temp;
	char streamid[32];
	while(1)
	{
		sprintf(streamid,"%d",i);
		while(DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",streamid,&pTmpResource))
		{
			pTmp = &pTmpResource;
			printf("the stream type is pTmp->strStreamType = %s\n",pTmp->strStreamType);
			fflush(stdout);
			temp = pTmp->strStreamType[0];
			switch(temp)
			{
				case 'A':
					printf("this is free stream begin to check\n");
					break;
				case 'B':
					printf("this is Advertiser stream begin to check\n");
					m_Advertiser->CheckStatus(pTmp->istreamID,pTmp->strSerialNo);
					break;
				case 'C':
					printf("this is Navigation stream begin to check\n");
				//	m_Navigation->GetTaskStatus(pTmp->istreamID,1);
					break;
				case 'D':
					printf("this is Navigation stream2 begin to check\n");
				//	m_Navigation->GetTaskStatus(pTmp->istreamID);
					break;
				case 'E':
					printf("this is vod stream begin to check\n");
					m_VGWVodPlayer->CheckStatus(pTmp->istreamID,pTmp->strSwitch_task_id,pTmp->strSerialNo);
					break;
				default:
					printf("error status\n");
					break;
			}
			i++;
			sprintf(streamid,"%d",i);
		}
		i = 1;
		usleep(1000*10000);
	}
*/
}

bool SM_Manager::Unbind(char * usernam,char * streamid)
{
	printf("it is begin to unbind\n");
	fflush(stdout);
	m_Msi_SMInter->CleanMSIStream(usernam,streamid);
	printf("now it is unbind over\n");
	fflush(stdout);
}
