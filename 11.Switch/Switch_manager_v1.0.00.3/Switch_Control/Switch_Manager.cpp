
#include "Switch_Manager.h"
#include <string>
#include "Log.h"


Switch_Manager::Switch_Manager()
{
	m_iCurrentPort = iBeginPort;
	pthread_mutex_init(&m_lockerttTaskInfo, NULL);
}
Switch_Manager::~Switch_Manager()
{

	pthread_mutex_destroy(&m_lockerttTaskInfo);
}

bool Switch_Manager::Init(char *SW_ip) //需要管理发送 端口
{

	memset(m_SW_ip,0,sizeof(m_SW_ip));

	strcpy(m_SW_ip,SW_ip);
	printf("----used SW_IP is %s \n",m_SW_ip);
	fflush(stdout);
	//LOG_INFO_FORMAT("----used SW_IP is %s \n",m_SW_ip);
	return true;
}
//创建映射关联 

int Switch_Manager::GetOneSwitchServer(SwitchSeverInfo** pSeverSwitch)
{
	//查找可用的sever  找使用率最小的
	//遍历切流器
	printf("---get one Switch server fun \n");
	LOG_DEBUG("DEBUG - [SWM]: - get one Switch server fun \n");
	int mindbUseRate = 65565; 
	pthread_mutex_lock(&m_mutexServerList);
	MapSwitchSeverInfo::iterator iterLook = m_mapSwitchServerInfo.begin();
	while(iterLook != m_mapSwitchServerInfo.end())
	{
		//
		SwitchSeverInfo *pTmpSeverInfo = iterLook->second;
		if(pTmpSeverInfo && pTmpSeverInfo->iRunStatus == 0 && pTmpSeverInfo->iStreamStatus == 0)  //在线可用的切流器
		{
			//运行状态可用最少路数服务器
			if(mindbUseRate >= pTmpSeverInfo->iCurrentSwitchNumber)
			{	
				mindbUseRate = pTmpSeverInfo->iCurrentSwitchNumber;
			
				*pSeverSwitch = pTmpSeverInfo;
				printf("get one sever \n");
				LOG_INFO_FORMAT("INFO  - [SWM]: get one sever ip %s\n",pTmpSeverInfo->strServerIPAddr);
			}
		}
		
		
		++iterLook;
	}
	
	pthread_mutex_unlock(&ManagerFactory::instance()->m_mutexServerList);
	

	return 0;
}

int Switch_Manager::FindOneSwitchServer(char* strSessionID,SwitchSeverInfo** ppSeverSwitch)
{
	if(ppSeverSwitch == NULL)
		return -2;
	printf("---find one SwitchServer \n");
	LOG_DEBUG("DEBUG SWT - find one SwitchServer \n");
	int ifindServerID = -1;
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSeverInfo::iterator itfind = m_mapSwitchTaskInfo.find(strSessionID);
	if(itfind != m_mapSwitchTaskInfo.end())
	{
		ifindServerID = itfind->second;
	}
	pthread_mutex_unlock(&m_lockerttTaskInfo);
	if(ifindServerID != -1)
	{
		//找到severinfo
		SwitchSeverInfo *pTmpSeverInfo = NULL;
		pthread_mutex_lock(&m_mutexServerList);
		
		MapSwitchSeverInfo::iterator itlook = m_mapSwitchServerInfo.find(ifindServerID);
		if(itlook != m_mapSwitchServerInfo.end())
		{
			pTmpSeverInfo = itlook->second;
			if(pTmpSeverInfo ==NULL || pTmpSeverInfo->iRunStatus!= 0 || pTmpSeverInfo->iStreamStatus != 0)
			{
				if(pTmpSeverInfo != NULL)
					printf("----find sever but sever status ip %s error \n",pTmpSeverInfo->strServerIPAddr);
				LOG_INFO("INFO  - [SWM]: find sever but sever status error \n");
				pthread_mutex_unlock(&m_mutexServerList);
				return -1;
			}

			pthread_mutex_unlock(&m_mutexServerList);
		}
		else
		{
			pthread_mutex_unlock(&m_mutexServerList);
			printf("---not find severID =%d map size=%d \n",ifindServerID,m_mapSwitchServerInfo.size());
			LOG_INFO_FORMAT("INFO  - [SWM]: not find severID =%d  map size=%d \n",ifindServerID,m_mapSwitchServerInfo.size());
			return -1;
		}
		
		*ppSeverSwitch = pTmpSeverInfo;
		
	}
	else
	{
		//删除或者检查状态，没有流也需要回复，直接选择第一个可用的sever
		printf("---not find sessionid %s map size=%d \n",strSessionID,m_mapSwitchTaskInfo.size());
		LOG_INFO_FORMAT("INFO  - [SWM]: not find sessionid %s map size=%d \n",strSessionID,m_mapSwitchTaskInfo.size());
		return -1;
	}


	return 0;
}

int Switch_Manager::FindSwitchServerFromIP(char* strIP,SwitchSeverInfo** ppSeverSwitch)
{
	if(ppSeverSwitch == NULL)
		return -2;
	printf("---find one SwitchServer from ip \n");
	LOG_DEBUG("DEBUG - [SWM]: - find one SwitchServer from ip \n");
//	if(ifindServerID != -1)
	{
		//找到severinfo
		SwitchSeverInfo *pTmpSeverInfo = NULL;
		pthread_mutex_lock(&m_mutexServerList);
		
		MapSwitchSeverInfo::iterator itlook = m_mapSwitchServerInfo.begin();
		while(itlook != m_mapSwitchServerInfo.end())
		{
			pTmpSeverInfo = itlook->second;
			if(strcmp(pTmpSeverInfo->strServerIPAddr,strIP)==0)
			{
				
				if(pTmpSeverInfo ==NULL || pTmpSeverInfo->iRunStatus!= 0 || pTmpSeverInfo->iStreamStatus != 0)
				{
					if(pTmpSeverInfo != NULL)
						printf("----find from ip sever but sever status ip %s error \n",pTmpSeverInfo->strServerIPAddr);
					LOG_INFO("INFO  - [SWM]: find from ip sever but sever status error \n");
					pthread_mutex_unlock(&m_mutexServerList);
					return -1;
				}
				else
				{
					printf("---find sever Ip =%s map size=%d \n",strIP,m_mapSwitchServerInfo.size());	
					break;
				}
			}
			else
			{
				pTmpSeverInfo = NULL;
			}

			++itlook;
			
		}
		pthread_mutex_unlock(&m_mutexServerList);
		if(pTmpSeverInfo ==NULL)
		{
			printf("---not find sever Ip =%s map size=%d \n",strIP,m_mapSwitchServerInfo.size());
			LOG_INFO_FORMAT("INFO  - [SWM]: not find severID =%s  map size=%d \n",strIP,m_mapSwitchServerInfo.size());
			return -1;
		}
		
		*ppSeverSwitch = pTmpSeverInfo;
		
	}


	return 0;
}


int Switch_Manager::DeleteAllWsitch(char* strUrl,char* outPutUrl,char* strSessionID)
{
	LOG_DEBUG("DEBUG - [SWM]: - DeleteAllWsitch fun \n");
	printf("---DeleteAllWsitch fun \n");
	bool bIsReplyflag = false;
		//遍历切流器 时间过长，不加锁
	pthread_mutex_lock(&m_mutexServerList);
	MapSwitchSeverInfo::iterator iterLook = m_mapSwitchServerInfo.begin();
	while(iterLook != m_mapSwitchServerInfo.end())
	{
		//
		SwitchSeverInfo *pFindSeverInfo = iterLook->second;
		if(pFindSeverInfo && pFindSeverInfo->iRunStatus == 0)  //在线的切流器
		{
				//连接server 
			//报文转发
			Stream ptmpRequest;
			int iret = ptmpRequest.ConnectServer(pFindSeverInfo->strServerIPAddr,pFindSeverInfo->iListenPort);
			if(iret != 0)
			{
				//连接失败		
				continue;
			}
			iret = ptmpRequest.Send_str(strUrl);
			if(iret != 0)
			{
				continue;
			}
			char Rcv_buf[1024]={0};
			int iLen = sizeof(Rcv_buf);
			iret = ptmpRequest.Recv_str(Rcv_buf,&iLen);
			if(iret !=0)
				continue;

			memcpy(outPutUrl,Rcv_buf,iLen);
			bIsReplyflag = true;
			outPutUrl[iLen] ='\0';

				cJSON *pcmd = NULL;

					//解析报文数据
			replace(Rcv_buf, "XXEE", "");
			cJSON* pRoot = cJSON_Parse(Rcv_buf);

			int iRet = -1;
			if (pRoot)
			{
				pcmd = cJSON_GetObjectItem(pRoot, "cmd");
				if (pcmd)
				{
					//判断请求类型
					if(strcmp(pcmd->valuestring, "reset_device") == 0)
					{
						iRet = 0;
					}
				}

			}
			if(iRet == 0)
			{
				LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流成功 \n",pFindSeverInfo->strServerIPAddr);
				pFindSeverInfo->iStreamStatus = 0;
				pFindSeverInfo->iCurrentSwitchNumber = 0;
			}
			else
			{
				
				LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流失败 \n",pFindSeverInfo->strServerIPAddr);
				pFindSeverInfo->iStreamStatus = 1;  //流状态异常
			}
		}
		else
			printf("---切流器 不在线 %s \n",pFindSeverInfo->strServerIPAddr);
		
		++iterLook;
	}

	pthread_mutex_unlock(&ManagerFactory::instance()->m_mutexServerList);

	if(!bIsReplyflag)
	{
		ReplyException(strUrl,outPutUrl,Error_ReSet_Device);
		LOG_INFO("INFO  - [SWM]: Delete ALL stream exception \n");
	}

	return 0;

}

int Switch_Manager::CheckOneSwitch(char* strUrl,char* outPutUrl,char* strSessionID)
{
	LOG_DEBUG("DEBUG - [SWM]: - CheckOneSwitch\n");
	printf("--CheckOneSwitch\n");
	int ret = -1;
//查找会话
//	printf("---find sessionid %s map size=%d \n",strSessionID,m_mapSwitchTaskInfo.size());

	SwitchSeverInfo** ppSeverSwitch = (SwitchSeverInfo**)malloc(sizeof(SwitchSeverInfo*));
	memset(ppSeverSwitch,0,sizeof(ppSeverSwitch));

	int iRet = FindOneSwitchServer(strSessionID,ppSeverSwitch);
	//找到对应服务器
	
	SwitchSeverInfo *pFindSeverInfo = *ppSeverSwitch;
	if(pFindSeverInfo ==NULL)
	{
		printf("---no find sever info or no sessiond \n");
		ReplyException(strUrl,outPutUrl,Error_NO_Stream);
		return -2;
	}

	//连接server 
	//报文转发
	Stream ptmpRequest;
	int iret = ptmpRequest.ConnectServer(pFindSeverInfo->strServerIPAddr,pFindSeverInfo->iListenPort);
	if(iret != 0)
	{
		//连接失败
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	iret = ptmpRequest.Send_str(strUrl);
	if(iret != 0)
	{
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	char Rcv_buf[1024]={0};
	int iLen = sizeof(Rcv_buf);
	iret = ptmpRequest.Recv_str(Rcv_buf,&iLen);
	if(iret !=0)
	{
		ReplyException(strUrl,outPutUrl,Error_Recv_Sever);
		return -1;
	}


	memcpy(outPutUrl,Rcv_buf,iLen);
	//LOG_INFO_FORMAT("check one switch status :%s");
	//cJSON *pcmd = NULL;
	//解析报文数据
	//replace(Rcv_buf, "XXEE", "");
	//cJSON* pRoot = cJSON_Parse(Rcv_buf);
	

	return 0;
}


int Switch_Manager::ReqOneSwitchForPort(char* strUrl,char* outPutUrl,char* strSessionID)
{
	//查找可用的sever  找使用率最小的
				//遍历切流器
	printf("-----ReqOneSwitchForPort \n");
	LOG_DEBUG("DEBUG - [SWM]: - ReqOneSwitchForPort \n");
	printf("-----add switch %s \n",strSessionID);
	SwitchSeverInfo **ppFindSeverInfo = (SwitchSeverInfo **)malloc(sizeof(SwitchSeverInfo *));
	memset(ppFindSeverInfo,0,sizeof(ppFindSeverInfo));

	int iRet = FindOneSwitchServer(strSessionID,ppFindSeverInfo);
	//找到对应服务器
	SwitchSeverInfo *pFindSeverInfo = NULL;
	
	if(iRet == 0 && *ppFindSeverInfo!= NULL)
	{
		//找到正在转发的流
		pFindSeverInfo = *ppFindSeverInfo;
	}
	else
	{
	
		iRet = GetOneSwitchServer(ppFindSeverInfo);
		if(*ppFindSeverInfo ==NULL)
		{
			printf("---no can used sever -----\n");
			
			ReplyException(strUrl,outPutUrl,Error_NO_Sever);
			
			return -1; //没有可以用的Switch_Sever;
		}
		pFindSeverInfo = *ppFindSeverInfo;
	}
	 
	//连接server 
	//报文转发
	Stream ptmpRequest;
	int iret = ptmpRequest.ConnectServer(pFindSeverInfo->strServerIPAddr,pFindSeverInfo->iListenPort);
	if(iret != 0)
	{
		//连接失败
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	iret = ptmpRequest.Send_str(strUrl);
	if(iret != 0)
	{
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	char Rcv_buf[1024]={0};
	int iLen = sizeof(Rcv_buf);
	iret = ptmpRequest.Recv_str(Rcv_buf,&iLen);
	if(iret !=0)
	{
		ReplyException(strUrl,outPutUrl,Error_Recv_Sever);
		return -1;
	}

	memcpy(outPutUrl,Rcv_buf,iLen);

	
	return 0;
}


//返回的是关联的会话
int Switch_Manager::AddOneSwitch(char* strUrl,char* outPutUrl,char* strSessionID,char* inputUrl)
{
	printf("-----AddOneSwitch\n");
	LOG_DEBUG("DEBUG - [SWM]: - AddOneSwitch\n");
	
	//查找可用的sever  找使用率最小的
				//遍历切流器
	printf("-----add switch %s \n",strSessionID);
	SwitchSeverInfo **ppFindSeverInfo = (SwitchSeverInfo **)malloc(sizeof(SwitchSeverInfo *));
	memset(ppFindSeverInfo,0,sizeof(ppFindSeverInfo));

	int iRet = FindOneSwitchServer(strSessionID,ppFindSeverInfo);
	//找到对应服务器
	SwitchSeverInfo *pFindSeverInfo = NULL;
	
	if(iRet == 0 && *ppFindSeverInfo!= NULL)
	{
		//找到正在转发的流
		pFindSeverInfo = *ppFindSeverInfo;
	}
	else
	{
		//新增的转发流
		//需要根据inputip来确定使用哪个sever
		//解析出ip 端口
		int iSrcPort = 0;
		char strSrcIP[128]={0};

		char strSrcPort[32]={0};
		FindDataFromString(inputUrl,strSrcIP,strSrcPort);
		iSrcPort = atoi(strSrcPort);
		//根据IP查找sever
		iRet = FindSwitchServerFromIP(strSrcIP,ppFindSeverInfo);
		if(*ppFindSeverInfo ==NULL)
		{
			//如果没有指定ip则由调度分配
		
			iRet = GetOneSwitchServer(ppFindSeverInfo);
			if(*ppFindSeverInfo ==NULL)
			{
				printf("---no can used sever -----\n");
				
				ReplyException(strUrl,outPutUrl,Error_NO_Sever);
				
				return -1; //没有可以用的Switch_Sever;
			}
		}

		pFindSeverInfo = *ppFindSeverInfo;
	}

	//连接server 
	//报文转发
	Stream ptmpRequest;
	int iret = ptmpRequest.ConnectServer(pFindSeverInfo->strServerIPAddr,pFindSeverInfo->iListenPort);
	if(iret != 0)
	{
		//连接失败
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	iret = ptmpRequest.Send_str(strUrl);
	if(iret != 0)
	{
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	char Rcv_buf[1024]={0};
	int iLen = sizeof(Rcv_buf);
	iret = ptmpRequest.Recv_str(Rcv_buf,&iLen);
	if(iret !=0)
	{
		ReplyException(strUrl,outPutUrl,Error_Recv_Sever);
		return -1;
	}

	memcpy(outPutUrl,Rcv_buf,iLen);
	cJSON *pcmd = NULL;
	//解析报文数据
	replace(Rcv_buf, "XXEE", "");
	cJSON* pRoot = cJSON_Parse(Rcv_buf);
	
	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
	
			//判断请求类型
			if (strcmp(pcmd->valuestring, "add_ads_stream") == 0)
			{
				printf("--add_ads_stream return \n");
				//通过
				cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strSeesionID[128] ={0};
				
				int iSeessionID = 0;
				if(pSeesid){
					iSeessionID = atoi(pSeesid->valuestring);
					memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring)+1);
				}
				cJSON* pTaskID = cJSON_GetObjectItem(pRoot, "task_id");
				int iTaskID = -1;
				if(pTaskID) iTaskID = pTaskID->valueint;
				cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
				int iRetCode = -1;
				if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
				if(iRetCode >= 0)
				{
					//添加成功增加关联sessionID 和severID 键值对
					
					pthread_mutex_lock(&m_lockerttTaskInfo);
					MapSeverInfo::iterator itlook = m_mapSwitchTaskInfo.find(strSeesionID);
					if(itlook != m_mapSwitchTaskInfo.end())
					{
						//出现两个一样会话ID 先删除 yyd
						m_mapSwitchTaskInfo.erase(itlook);
					}

					pFindSeverInfo->iCurrentSwitchNumber++;
					m_mapSwitchTaskInfo.insert(MapSeverInfo::value_type(strSeesionID,pFindSeverInfo->iServerID));
					printf("----insert to map %s ,size=%d \n",strSeesionID,m_mapSwitchTaskInfo.size());
					pthread_mutex_unlock(&m_lockerttTaskInfo);
					LOG_INFO_FORMAT("----insert to map %s ,size=%d \n",strSeesionID,m_mapSwitchTaskInfo.size());
				}
				
			}
		}
	}

	return 0;
}

int Switch_Manager::DeleteOneSwitch(char* strUrl,char* outPutUrl,char* strSessionID)
{
	printf("----DeleteOneSwitch fun \n");
	LOG_DEBUG("DEBUG - [SWM]: - DeleteOneSwitch fun \n");
	//查找会话
//	printf("---find sessionid %s map size=%d \n",strSessionID,m_mapSwitchTaskInfo.size());

	SwitchSeverInfo** ppSeverSwitch = (SwitchSeverInfo**)malloc(sizeof(SwitchSeverInfo*));
	memset(ppSeverSwitch,0,sizeof(ppSeverSwitch));
	int iRet = FindOneSwitchServer(strSessionID,ppSeverSwitch);
	//找到对应服务器
	
	SwitchSeverInfo *pFindSeverInfo = *ppSeverSwitch;
	if(pFindSeverInfo == NULL)
	{
		printf("---no find sever info or no sessiond \n");
		ReplyException(strUrl,outPutUrl,0);
		return -2;
	}
	//连接server 
	//报文转发
	Stream ptmpRequest;
	int iret = ptmpRequest.ConnectServer(pFindSeverInfo->strServerIPAddr,pFindSeverInfo->iListenPort);
	if(iret != 0)
	{
		//连接失败
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	iret = ptmpRequest.Send_str(strUrl);
	if(iret != 0)
	{
		ReplyException(strUrl,outPutUrl,Error_Connect_Sever);
		return -1;
	}
	char Rcv_buf[1024]={0};
	int iLen = sizeof(Rcv_buf);
	iret = ptmpRequest.Recv_str(Rcv_buf,&iLen);
	if(iret !=0)
	{
		ReplyException(strUrl,outPutUrl,Error_Recv_Sever);
		return -1;
	}

	printf("recv Del strem\n");
	fflush(stdout);
	memcpy(outPutUrl,Rcv_buf,iLen);
	cJSON *pcmd = NULL;
	//解析报文数据
	replace(Rcv_buf, "XXEE", "");
	cJSON* pRoot = cJSON_Parse(Rcv_buf);
	
	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
	
			//判断请求类型
			 if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
			{
				 cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
				 char strSeesionID[128] ={0};
				 
				 int iSeessionID = 0;
				 if(pSeesid){
					 iSeessionID = atoi(pSeesid->valuestring);
					 memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring)+1);
				 }

				 cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
				 int iRetCode = -1;
				 if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
				 //printf("parse--%s\n",pRetCOde->valuestring);
				 cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				 char strSerialNo[128] ={0};
				 //printf("parse--%s\n",pSerialNo->valuestring);
				 if(pSerialNo)
				 memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
			 
				 printf("--del_ads_stream return \n");		 

				if(iRetCode >= 0)
				{
					//添加成功增加关联sessionID 和severID 键值对
					
					pthread_mutex_lock(&m_lockerttTaskInfo);
					MapSeverInfo::iterator itlook = m_mapSwitchTaskInfo.find(strSeesionID);
					if(itlook != m_mapSwitchTaskInfo.end())
					{
						//出现两个一样会话ID 先删除 yyd
						m_mapSwitchTaskInfo.erase(itlook);
					}
					//存放sever的map不会在运行中做增删操作
					pFindSeverInfo->iCurrentSwitchNumber--;
					printf("----erase to map %s ,size=%d \n",strSeesionID,m_mapSwitchTaskInfo.size());
					pthread_mutex_unlock(&m_lockerttTaskInfo);
					LOG_INFO_FORMAT("----erase to map %s ,size=%d \n",strSeesionID,m_mapSwitchTaskInfo.size());
				}
				
			}
		}
	}
			
	return 0;
}


int Switch_Manager::ReplyException(char* strurl,char* strOutUrl,int iRetcode)
{
	printf("-----ReplyException \n");
	LOG_DEBUG("DEBUG - [SWM]: - ReplyException \n");
	if(strOutUrl==NULL)
		return -1;
	char *Rcv_buf=strurl;
		//报文回复
	Stream ptmpRequest;
		cJSON *pcmd = NULL;
			//解析报文数据
	replace(Rcv_buf, "XXEE", "");
	cJSON* pRoot = cJSON_Parse(Rcv_buf);

	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{

			//判断请求类型
			if (strcmp(pcmd->valuestring, "add_ads_stream") == 0)
			{
				printf("--Add_stream request exception \n");
				fflush(stdout);
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				if(psessionid)
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
				cJSON* pToken = cJSON_GetObjectItem(pRoot, "input_url");
				char input_url[128] ={0};
				if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);

				char output_url[128] ={0};
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
				if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");

				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","add_ads_stream");
				ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

				ptmpRequest.Requst_Json_str(1,"task_id",strsessionid);
				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				

			}
			else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
			{
				printf("--Del_stream request exception\n");
				fflush(stdout);
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				if(psessionid)
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");

				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
				
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","del_ads_stream");

				ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				
			}
			else if(strcmp(pcmd->valuestring, "reset_device") == 0)
			{
				printf("--reset_stream request exception\n");

				fflush(stdout);
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");

				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","reset_device");

				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				
			}
			else if (strcmp(pcmd->valuestring, "change_stream") == 0)
			{
				printf("--Change_stream request exception\n");
				fflush(stdout);
				cJSON* old_psessionid = cJSON_GetObjectItem(pRoot, "old_sessionid");
				char old_strsessionid[128] ={0};
				if(old_psessionid)
					memcpy(old_strsessionid,old_psessionid->valuestring,strlen(old_psessionid->valuestring)+1);
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				if(psessionid)
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
				cJSON* pInterval = cJSON_GetObjectItem(pRoot, "interval");
				char strInterval[128] ={0};
				if(pInterval)
					memcpy(strInterval,pInterval->valuestring,strlen(pInterval->valuestring)+1);


				cJSON* pToken = cJSON_GetObjectItem(pRoot, "input_url");
				char input_url[128] ={0};
				if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);

				char output_url[128] ={0};
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
				if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");

				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","change_stream");

				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				ptmpRequest.Send_Jsoon_str();


			}
			else if(strcmp(pcmd->valuestring, "check_session") == 0)
			{
				printf("--check_session exception\n");
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				int iSeessionID = 0;
				if(psessionid)
				{
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
					iSeessionID = atoi(strsessionid);
				}

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","check_session");

				ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

				ptmpRequest.Requst_Json_str(1,"task_id",strsessionid);
				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				
			}
			else if(strcmp(pcmd->valuestring, "req_ads_stream") == 0)
			{
				printf("--req stream_session exception\n");
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				int iSeessionID = 0;
				if(psessionid)
				{
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
					iSeessionID = atoi(strsessionid);
				}

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","req_ads_stream");

				ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

				char strnoip[128]={0};
				strcpy(strnoip,"1.1.1.1");
				char strport[32]={0};
				strcpy(strport,"1");
				ptmpRequest.Requst_Json_str(1,"switch_ip",strnoip);
				ptmpRequest.Requst_Json_str(1,"switch_port",strport);
				char txt[32] ={0};
				sprintf(txt,"%d",iRetcode);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
				
			}


		}
	}
	
	char cJsonBuff[1024 * 2];
	char * m_tmp;
	m_tmp = cJSON_Print(ptmpRequest.pRet_root);
	memset(cJsonBuff, 0, sizeof(cJsonBuff));
	sprintf(cJsonBuff, "%sXXEE", m_tmp);
	free(m_tmp);
	printf("-----%s \n",cJsonBuff);
	fflush(stdout);
	cJSON_Delete(ptmpRequest.pRet_root);
	//copy to output 

	strcpy(strOutUrl,cJsonBuff);
	
	return 0;
}


