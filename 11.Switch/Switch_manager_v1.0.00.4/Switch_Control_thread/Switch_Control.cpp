#include "Switch_Control.h"
#include "Stream.h"
#include "Log.h"



Switch_Control::Switch_Control()
{
	m_pSwitchManager = NULL;

	m_iMsiPort = iMSIServerPort;


	pthread_mutex_init(&ManagerFactory::instance()->m_mutexServerList, NULL);
	pthread_mutex_init(&m_lockerQuesocket,NULL);
	
}
Switch_Control::~Switch_Control()
{
	if(m_pSwitchManager)
		delete m_pSwitchManager;
	m_pSwitchManager = NULL;

	pthread_mutex_destroy(&m_lockerQuesocket);
}

bool Switch_Control::Init()
{

	return true;
}

int Switch_Control::ConnectToSever(const char* phostIp,int iPort,int *pCurrent_num,int *pMax_num)
{
	struct sockaddr_in s_addr;
	int sockid;
	socklen_t addr_len;

    sockid=socket(AF_INET,SOCK_STREAM,0);
	    
    s_addr.sin_family = AF_INET ;

   	s_addr.sin_addr.s_addr = inet_addr(phostIp) ;
    s_addr.sin_port=htons((unsigned short)iPort);
    fcntl(sockid,F_SETFD, FD_CLOEXEC);
 
/*	 if (-1 == fcntl(sockid, F_SETFL, O_NONBLOCK))
	 {
		 printf("fcntl socket error!\n");
		 fflush(stdout);
		 return -1;
	 }	 
*/
	unsigned long ul = 1;
	ioctl(sockid, FIONBIO, &ul); //设置为非阻塞模式
	struct timeval timeout={10,1000*500}; //1秒
	int len = sizeof(timeout);
	setsockopt(sockid,SOL_SOCKET,SO_SNDTIMEO, &timeout,len);
	setsockopt(sockid,SOL_SOCKET,SO_RCVTIMEO,&timeout,len);

	fd_set set;
 	printf("---begin connect \n");
    //重复连接3次，防止中断等原因导致的异常
    for(int i=0;i<3;i++)
    {
    	bool ret = false;
		int iret = -1;
		iret = connect(sockid,(struct sockaddr *)&s_addr,(int)(sizeof(s_addr)));
  		if(iret == -1)
   		{
   			struct timeval tm;
     		tm.tv_sec  = 5;
     		tm.tv_usec = 1000*500;
     		FD_ZERO(&set);
     		FD_SET(sockid, &set);
			int error=-1;
     		if( select(sockid+1, NULL, &set, NULL, &tm) > 0)
     		{
       			getsockopt(sockid, SOL_SOCKET, SO_ERROR, &error, (socklen_t *)&len);
       			if(error == 0) 
				{
					ret = true;
					printf("---error no \n");
					//break;
   				}
 				else ret = false;
    		} else ret = false;
  		}
 		else if(iret==0)
		{	
			
			break;
		}
    	 if(i==2) 
    	 {
    	 	  //cout<<"connect Error "<<p_host<<":"<<p_Port<<endl;
    	 	  //perror("::connect");
    	 	  printf("--connect error \n");
			  LOG_ERROR("ERROR  - [SWM]: connect error  \n");
    	 	  ::close(sockid);
    	 	  return -1;
    	 }
		// printf("---sleep \n");
    	 usleep(1000);
    	 //cout<<"connect again: "<<p_host<<":"<<p_Port<<endl;
    }
	ul = 0;
	ioctl(sockid, FIONBIO, &ul); //设置为阻塞模式
	printf("---connect success \n");
    int     optval = 1;
   // setsockopt(sockid,SOL_SOCKET,SO_KEEPALIVE,(char *)(&optval),sizeof(optval));

	char alivetick[1024]={0};
	int iData=0;
	char strdata[32]={0};
	sprintf(strdata,"%d",iData);
  	//发送心跳
	Stream ptmpRequest;

	ptmpRequest.m_clientSocket = sockid;
	//cJSON *pRet_root;
	ptmpRequest.pRet_root = cJSON_CreateObject();
	ptmpRequest.Requst_Json_str(2,"cmd","alivetick");
	ptmpRequest.Requst_Json_str(2,"dataid",strdata);
	ptmpRequest.Send_Jsoon_str();

	//LOG_INFO("INFO  - [SWM]: send alive \n");
		
	memset(alivetick,0,sizeof(alivetick));
	int length = 0;
	int i_rc = 0, i_count = 0;
	int iRecvLen = 0;
	do
	{
		i_rc = recv(sockid, alivetick + i_count, 2000 - i_count, 0);
		if (i_rc <= 0)break;//异常关闭
		i_count += i_rc;
	} while (strstr(alivetick, "XXEE") == NULL);
	iRecvLen = i_count;
	
	if (iRecvLen <= 0) { 
		::close(sockid);
		LOG_ERROR("ERROR  - [SWM]: Recv error  \n");
		return -1;
	}
	alivetick[iRecvLen]='\0';

	LOG_INFO_FORMAT("INFO  - [SWM]: tcp recved %d bytes :[%s] \n",iRecvLen,alivetick);
		
	printf("recv:%s \n",alivetick);
	fflush(stdout);

	cJSON *pcmd = NULL;

			//解析报文数据
	replace(alivetick, "XXEE", "");
	cJSON* pRoot = cJSON_Parse(alivetick);

	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
			//判断请求类型
			if (strcmp(pcmd->valuestring, "alivetick") == 0)
			{
				//通过
				printf("--alive tick repay \n");
				cJSON* pDataid = cJSON_GetObjectItem(pRoot, "dataid");
				char strDataid[128] ={0};
				if(pDataid)
					memcpy(strDataid,pDataid->valuestring,strlen(pDataid->valuestring)+1);
				int iRecvData = atoi(strDataid);
				cJSON* pcurrent = cJSON_GetObjectItem(pRoot, "current_num");
				char strcurrent[128] ={0};
				if(pcurrent)
					memcpy(strcurrent,pcurrent->valuestring,strlen(pcurrent->valuestring)+1);
				int icurrentnum = atoi(strcurrent);
				cJSON* pmaxnum = cJSON_GetObjectItem(pRoot, "max_num");
				char strmax[128] ={0};
				if(pmaxnum)
					memcpy(strmax,pmaxnum->valuestring,strlen(pmaxnum->valuestring)+1);
				int imaxtnum = atoi(strmax);

				*pCurrent_num = icurrentnum;
				*pMax_num = imaxtnum;
				if(iRecvData == (iData +1))
				{	
					//正常心跳
					::close(sockid);
					//printf("---- alive keep \n");
					return 0;
				}
			}
		}

	}

	::close(sockid);

	return -1;
}

void *Switch_Control::ts_recv_Control_thread(void *arg)
{

	Switch_Control *this0 = (Switch_Control*)arg;

	struct timeval tv1,tv2;
	long long time1,time2;
	long long nsendbytes = 0;
	long long totaltime = 0;
	long now_bit_rate = 0;
	gettimeofday(&tv1, NULL);
	time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
	bool iFirstTime = true;

	//long bit_rate = this0->m_iSendRate;
	while(1)
	{
		if(!iFirstTime)
		{
			gettimeofday(&tv2, NULL);
			time2 = tv2.tv_sec*1000 + tv2.tv_usec / 1000;
			if(time2 - time1 < this0->iAliveTickInterval)
			{
				usleep(500*1000);	
				//printf("now_bit_rate is too large %d %d\n",now_bit_rate,this0->m_iSendRate);
				continue;
			}
			printf("------%ld \n",time2);
			time1 = time2;
		}


		printf("----switch sever size =%d \n",ManagerFactory::instance()->m_mapSwitchServerInfo.size());
		//遍历切流器

		MapSwitchSeverInfo::iterator iterLook = ManagerFactory::instance()->m_mapSwitchServerInfo.begin();
		while(iterLook != ManagerFactory::instance()->m_mapSwitchServerInfo.end())
		{
			printf("---send alive msg\n");
			//
			SwitchSeverInfo *pTmpSeverInfo = iterLook->second;
			if(pTmpSeverInfo)
			{
				int iCurrent_num = 0;
				int iMax_num = 0;
				int iRet = this0->ConnectToSever(pTmpSeverInfo->strServerIPAddr,pTmpSeverInfo->iListenPort,&iCurrent_num,&iMax_num);
				printf("---send %s alive end current=%d max =%d \n",pTmpSeverInfo->strServerIPAddr,iCurrent_num,iMax_num);

				pthread_mutex_lock(&ManagerFactory::instance()->m_mutexServerList);
				printf("---in lock \n");
				if(iRet == 0)
				{
					if(pTmpSeverInfo->iRunStatus > iKEEPALIVETIME)
					{
						//由掉线转为在线 需要进行重置流
						int iret = -1;
						printf("---need reset message \n");
						
						iret = this0->SendResetMessage(pTmpSeverInfo->strServerIPAddr,pTmpSeverInfo->iListenPort);
						if(iret == 0)
						{
							//重置成功
							printf("---success reset message \n");
							LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流成功 \n",pTmpSeverInfo->strServerIPAddr);
							pTmpSeverInfo->iRunStatus = 0;
							pTmpSeverInfo->iStreamStatus = 0;
							if(iCurrent_num != 0 || iMax_num != 0)
							{
								pTmpSeverInfo->iCurrentSwitchNumber = iCurrent_num;
								pTmpSeverInfo->iMaxSwitchNumber = iMax_num;
							}
								SwitchSeverInfo *pNewServer = pTmpSeverInfo;
								int ret= -1;
								string key;
								string value;
								char buf_key[1024];
								char buf_value[4096];
								snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
								snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
									pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus>0?1:0,
									pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
								key = buf_key;
								value = buf_value;
								
								ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
								if(ret == 0)
								{
									LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
									//return true;	
								}
								else
								{
									LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
									//return false;
								}
						}
						else
						{
							pTmpSeverInfo->iStreamStatus = 1;
							LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流失败 \n",pTmpSeverInfo->strServerIPAddr);
							if(iCurrent_num != 0 || iMax_num != 0)
							{
								pTmpSeverInfo->iCurrentSwitchNumber = iCurrent_num;
								pTmpSeverInfo->iMaxSwitchNumber = iMax_num;
							}
								SwitchSeverInfo *pNewServer = pTmpSeverInfo;
								int ret= -1;
								string key;
								string value;
								char buf_key[1024];
								char buf_value[4096];
								snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
								snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
									pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus>0?1:0,
									pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
								key = buf_key;
								value = buf_value;
								
								ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
								if(ret == 0)
								{
									LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
									//return true;	
								}
								else
								{
									LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
									//return false;
								}

						}
					}
					else
					{
						if(iCurrent_num != 0 || iMax_num != 0)
						{
							pTmpSeverInfo->iCurrentSwitchNumber = iCurrent_num;
							pTmpSeverInfo->iMaxSwitchNumber = iMax_num;
						}
						pTmpSeverInfo->iRunStatus = 0;
						SwitchSeverInfo *pNewServer = pTmpSeverInfo;
						//先获取之前的数据
						{
							int ret= -1;
							int istreamstatus = 0;
							string key;
							string value;
							char buf_key[1024];
							
							//SWM:switch_info:sessionid  serverid input_url output_url successstatus
							snprintf(buf_key,sizeof(buf_key),  "SWM:switch_serverlist:%d",pNewServer->iServerID);
							printf("---buf_key : %s \n",buf_key);
							key = buf_key;
							ret = ManagerFactory::instance()->m_redis_clients.getvalue(key,value);
							if(ret == 0)
							{
								LOG_TRACE_FORMAT("TRACE :get switchinfo mdb success key = [%s] value = [%s] \n", key.c_str(),value.c_str());
								//return true;	
							}
							else
							{
								LOG_TRACE_FORMAT("TRACE :get switchinfo mdb error:%d, key = [%s]\n", ret, key.c_str());
								//return false;
							}
							printf("---value : %s \n",value.c_str());
							//int iswitchserver_id =0;
							char strServerIPAddr[512] = {0};
							int iListenPort = 0;
							int iRunStatus = 0;
							int iStreamStatus = 0;
							int iMaxSwitchNumber = 0;
							int iCurrentSwitchNumber = 0;
							//printf("--get ");
							if(6 == sscanf(value.c_str(),"%[^|]|%d|%d|%d|%d|%d",
								strServerIPAddr,&iListenPort,&iRunStatus,&iStreamStatus,
								&iMaxSwitchNumber,&iCurrentSwitchNumber))
							{
								printf("---parse success \n");
								if(iCurrentSwitchNumber != pTmpSeverInfo->iCurrentSwitchNumber
									|| pTmpSeverInfo->iMaxSwitchNumber != iMaxSwitchNumber ||
									iRunStatus != pTmpSeverInfo->iRunStatus
									|| iStreamStatus!= pNewServer->iStreamStatus )
								{
									int ret= -1;
									string key;
									string value;
									char buf_key[1024];
									char buf_value[4096];
									snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
									snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
										pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus>0?1:0,
										pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
									key = buf_key;
									value = buf_value;
									
									ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
									if(ret == 0)
									{
										LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
										//return true;	
									}
									else
									{
										LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
										//return false;
									}
				
								}
							}
							else
								LOG_ERROR_FORMAT("ERROR-[SWM]:invald resource id [%s] ,info :[%s]\n", value.c_str(),key.c_str());
						}
	
					//	if(pTmpSeverInfo->iRunStatus != 0 || iFirstTime 
					//		|| iCurrent_num!= pTmpSeverInfo->iCurrentSwitchNumber
					//		|| pTmpSeverInfo->iMaxSwitchNumber != iMax_num)
			
					}
				}
				else
				{
				//	if(pTmpSeverInfo->iRunStatus <= iKEEPALIVETIME)
						pTmpSeverInfo->iRunStatus++;
					if(pTmpSeverInfo->iRunStatus <= iKEEPALIVETIME)
						LOG_WARN_FORMAT("WARN  - [SWM]: %s alive lost   \n",pTmpSeverInfo->strServerIPAddr);
					printf("----no alive %s \n",pTmpSeverInfo->strServerIPAddr);

					if(pTmpSeverInfo->iRunStatus == iKEEPALIVETIME+1)
					{
						pTmpSeverInfo->iCurrentSwitchNumber = 0;
						pTmpSeverInfo->iMaxSwitchNumber = 0;
						
						SwitchSeverInfo *pNewServer = pTmpSeverInfo;
						int ret= -1;
						string key;
						string value;
						char buf_key[1024];
						char buf_value[4096];
						snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
						snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
							pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus>0?1:0,
							pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
						key = buf_key;
						value = buf_value;
						
						ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
						if(ret == 0)
						{
							LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
							//return true;	
						}
						else
						{
							LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
							//return false;
						}

					}
				}
				
				if(pTmpSeverInfo->iRunStatus > iKEEPALIVETIME)
				{
					
					LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 掉线 \n",pTmpSeverInfo->strServerIPAddr);
					if(pTmpSeverInfo->iRunStatus > 200000) 
						pTmpSeverInfo->iRunStatus = 6;
					
					pTmpSeverInfo->iCurrentSwitchNumber = 0;
					pTmpSeverInfo->iMaxSwitchNumber = 0;
				}
				
				pthread_mutex_unlock(&ManagerFactory::instance()->m_mutexServerList);
				printf("---out lock \n");
			}
			
			++iterLook;
		}

		iFirstTime = false;
	}


}

int  Switch_Control::ParseMessage(char* strMesg,int iLen,int accept_fd)
{
	
	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2]={0};
	int iRecvLen = 0;

	char Req_buf[1024]={0};
	char Replay_buf[1024]={0};
	memcpy(Req_buf,strMesg,iLen);
	char *Rcv_buf=strMesg;
	Pubc::replace(Rcv_buf,"XXEE","");
	cJSON* pRoot = cJSON_Parse(Rcv_buf);
	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
			//判断请求类型
			if (strcmp(pcmd->valuestring, "add_ads_stream") == 0)
			{
				printf("--Add_stream request \n");
				fflush(stdout);
				
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				if(psessionid)
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
				cJSON* pToken = cJSON_GetObjectItem(pRoot, "input_url");
				char input_url[256] ={0};
				if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);
				
				char output_url[256] ={0};
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
				if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);
				
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				//ManagerFactory::instance()->AddOneSwitch(Req_buf,Replay_buf,strsessionid,input_url);
				int iServerID = -1;
				int iRetcode = ManagerFactory::instance()->AddOneSwitch(Req_buf,Replay_buf,strsessionid,
																input_url,&iServerID);
				
				
			//	write(Replay_buf,strlen(Replay_buf));
							//报文回复
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//ptmpRequest.Send_Jsoon_str();
				ptmpRequest.Send_str(Replay_buf);
				
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);

				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
				
				if(iRetcode >= 0)
				{
						
						int ret= -1;
						int istreamstatus = 1;
						string key;
						string value;
						char buf_key[1024];
						char buf_value[4096];
						//SWM:switch_info:sessionid  serverid input_url output_url streamstatus
						snprintf(buf_key,sizeof(buf_key), "SWM:switch_info:%s",strsessionid);
						snprintf(buf_value,sizeof(buf_value), "%d|%s|%s|%d",
							iServerID,input_url,output_url,istreamstatus);
						key = buf_key;
						value = buf_value;
						
						ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
						if(ret == 0)
						{
							LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
							//return true;	
						}
						else
						{
							LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
							//return false;
						}
				}
			}
			else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
			{
				printf("--Del_stream request \n");
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
				
				memset(Replay_buf,0,sizeof(Replay_buf));
				//ManagerFactory::instance()->DeleteOneSwitch(Req_buf,Replay_buf,strsessionid);

				
				int iRetCode = ManagerFactory::instance()->DeleteOneSwitch(Req_buf,Replay_buf,strsessionid);
				//write(Replay_buf,strlen(Replay_buf));
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//ptmpRequest.Send_Jsoon_str();
				ptmpRequest.Send_str(Replay_buf);
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
								//更改到数据库中
				if(iRetCode >=0)
				{
						
						int ret= -1;
						int istreamstatus = 0;
						string key;
						string value;
						char buf_key[1024];
						
						//SWM:switch_info:sessionid  serverid input_url output_url successstatus
						snprintf(buf_key,sizeof(buf_key), "SWM:switch_info:%s",strsessionid);
			
						key = buf_key;

						ret = ManagerFactory::instance()->m_redis_clients.delvalue(key);
						if(ret == 0)
						{
							LOG_TRACE_FORMAT("TRACE :del serverlist mdb success key = [%s]\n", key.c_str());
							//return true;	
						}
						else
						{
							LOG_TRACE_FORMAT("TRACE :del serverlist mdb error:%d, key = [%s]\n", ret, key.c_str());
							//return false;
						}
				}
			}
			else if(strcmp(pcmd->valuestring, "reset_device") == 0)
			{
				printf("--Reset_stream request \n");

				fflush(stdout);
				
				//通过
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				ManagerFactory::instance()->DeleteAllWsitch(Req_buf,Replay_buf,NULL);
			//	write(Replay_buf,strlen(Replay_buf));
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//ptmpRequest.Send_Jsoon_str();
				ptmpRequest.Send_str(Replay_buf);		
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
			}
			else if (strcmp(pcmd->valuestring, "change_stream") == 0)
			{
				printf("--Change_stream request \n");
				fflush(stdout);

			}
			else if (strcmp(pcmd->valuestring, "req_ads_stream") == 0)
			{
				printf("--req_ads_stream request \n");
				fflush(stdout);
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				int iSeessionID = 0;
				if(psessionid)
				{
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
					//iSeessionID = atoi(strsessionid);
				}
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->ReqOneSwitchForPort(Req_buf,Replay_buf,strsessionid);

				//write(Replay_buf,strlen(Replay_buf));
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//ptmpRequest.Send_Jsoon_str();
				ptmpRequest.Send_str(Replay_buf);		
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);

				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);

			}
			else if(strcmp(pcmd->valuestring, "check_session") == 0)
			{
				
				printf("--check_session \n");
				//通过
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				int iSeessionID = 0;
				if(psessionid)
				{
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
					//iSeessionID = atoi(strsessionid);
				}
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->CheckOneSwitch(Req_buf,Replay_buf,strsessionid);

				
			//	ManagerFactory::instance()->GetOneSwitchCheckStatus(Req_buf,Replay_buf,strsessionid);
			//	write(Replay_buf,strlen(Replay_buf));
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//ptmpRequest.Send_Jsoon_str();
				ptmpRequest.Send_str(Replay_buf);
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);

			}
		}	
	}
}

void *Switch_Control::Parse_recv_MSI_thread(void * arg)
{
	Switch_Control *this0 = (Switch_Control*)arg;

	int len;
	char Rcv_buf[4096];

		string	host;
	int		port=0;

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	//
	int accept_fd = -1;

	//取得队列头
	pthread_mutex_lock(&this0->m_lockerQuesocket);
	if(this0->m_QueSokcet.size()>0)
	{
		accept_fd = this0->m_QueSokcet.front();
		this0->m_QueSokcet.pop();
	}
	pthread_mutex_unlock(&this0->m_lockerQuesocket);
	
	//while(1)
	{
			if(accept_fd == -1)
			{
				printf("error accept socket in msi\n");
				fflush(stdout);
				//break;
				return NULL;
			}
			memset(Rcv_buf,0,sizeof(Rcv_buf));
			int length = 0;
			int i_rc = 0, i_count = 0;
			do
			{
				i_rc = recv(accept_fd, Rcv_buf + i_count, 2000 - i_count, 0);
				if (i_rc <= 0)break;//异常关闭
				i_count += i_rc;
			} while (strstr(Rcv_buf, "XXEE") == NULL);
			iRecvLen = i_count;
			if (iRecvLen <= 0) return NULL;

			printf("recv:%s \n",Rcv_buf);
			fflush(stdout);
			

			get_ClientPeer(accept_fd,host,port);
			
			LOG_INFO_FORMAT("INFO  - [SWT]: tcp recv [%s:%d] recved %d bytes :[%s] \n",host.c_str(),port,i_count,Rcv_buf);

			if(!strstr(Rcv_buf,"XXEE"))
			{
				close(accept_fd);
				return NULL;
			}
			this0->ParseMessage(Rcv_buf,iRecvLen,accept_fd);

	}

	//	close(accept_fd);

}

int Switch_Control::GetHostIp(const char *name,char *addr)
{
	int ret =-1;
	int inet_sock;
	struct ifreq ifr;
	inet_sock = socket(AF_INET, SOCK_DGRAM, 0);
	strcpy(ifr.ifr_name, name);
	if (ioctl(inet_sock, SIOCGIFADDR, &ifr) < 0)
	{
		perror("ioctl");
		return ret;
	}
	sprintf(addr,"%s", inet_ntoa(((struct sockaddr_in*)&(ifr.ifr_addr))->sin_addr));
	return 0;
}

int Switch_Control::FindSeverInfo(const char *strSeverInfo)
{
	  char strSw_ServerList[1024] = {0};
	  strcpy(strSw_ServerList,strSeverInfo);
	  int iParselen = 0;
	  int Totallen = strlen(strSw_ServerList);
	
	  int iServerID = 1;
	  while(iParselen < Totallen)
	  {
		   int iport = 0;
		   char pfindip[256] = {0};
		   char pfindport[32] = {0};
		   char* pCur = strSw_ServerList+iParselen;
		   printf("--%s \n",pCur);
		   char* pTmpfind = strstr(pCur,":");
		   if(NULL != pTmpfind)
		   {
	
			  int ilen = pTmpfind - pCur;
			  iParselen += (ilen);
			  printf("----parse len =%d \n",iParselen);
			  if(ilen < 5)
			  {
				  //不是正常的数据
					printf("continue 1---\n");
				  continue;
			  }
			  strncpy(pfindip,pCur,ilen);
			  pfindip[ilen] = '\0';
			  //查找port
			  if(strlen(pTmpfind)<=1)
			  {
				  //没找到端口
				  printf("endd 1---\n");
				  break;
			  }
			  char *pTmpdot = strstr(pTmpfind,",");
			  if(pTmpdot == NULL)
			  {
				  //最后一个
				  iParselen += strlen(pTmpfind);
				  iport = 0;
				  iport = atoi(pTmpfind+1);

				  if(iport <= 0)
			  	{
			  		
					break;
			  	}
				  //在这里认为是找到了一个正确的server 插入列表
				 SwitchSeverInfo *pNewServer = new SwitchSeverInfo;
				 memset(pNewServer,0,sizeof(SwitchSeverInfo));
				 strcpy(pNewServer->strServerIPAddr,pfindip);
				 pNewServer->iListenPort = iport;
				 pNewServer->iServerID = iServerID++;
	
				 printf("--find ip=%s port=%d ----\n",pNewServer->strServerIPAddr,pNewServer->iListenPort);
	
				 ManagerFactory::instance()->m_mapSwitchServerInfo.insert(MapSwitchSeverInfo::value_type(pNewServer->iServerID,pNewServer));
				//redis 已经初始化过，并创建了连接
				//将serverlist的信息加入到数据库中	
					int ret= -1;
					string key;
					string value;
					char buf_key[1024];
					char buf_value[4096];
					snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
					snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
						pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus,
						pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
					key = buf_key;
					value = buf_value;
					
					ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
					if(ret == 0)
					{
						LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
						//return true;	
					}
					else
					{
						LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
						//return false;
					}
				
				 break;
			  }
			  else
			  {
				  ilen = pTmpdot - pTmpfind;
				  iParselen += (ilen + 1);
				  printf("----parse len =%d \n",iParselen);
				  if(ilen<=1)
				  {
					  //没找到端口
					  continue ;
				  }
				  strncpy(pfindport,pTmpfind+1,ilen-1);
				  pfindport[ilen] = '\0';
				  iport =0;
				  iport = atoi(pfindport);

			 	if(iport <= 0)
					continue;
				  //在这里认为是找到了一个正确的server 插入列表
				 SwitchSeverInfo *pNewServer = new SwitchSeverInfo;
				 memset(pNewServer,0,sizeof(SwitchSeverInfo));
				 strcpy(pNewServer->strServerIPAddr,pfindip);
				 pNewServer->iListenPort = iport;
				 pNewServer->iServerID = iServerID++;
	
				 printf("--find ip=%s port=%d 1----\n",pNewServer->strServerIPAddr,pNewServer->iListenPort);
	
				 ManagerFactory::instance()->m_mapSwitchServerInfo.insert(MapSwitchSeverInfo::value_type(pNewServer->iServerID,pNewServer));

					int ret= -1;
					string key;
					string value;
					char buf_key[1024];
					char buf_value[4096];
					snprintf(buf_key,sizeof(buf_key), "SWM:switch_serverlist:%d",pNewServer->iServerID);
					snprintf(buf_value,sizeof(buf_value), "%s|%d|%d|%d|%d|%d",
						pNewServer->strServerIPAddr,pNewServer->iListenPort,pNewServer->iRunStatus,
						pNewServer->iStreamStatus,pNewServer->iMaxSwitchNumber,pNewServer->iCurrentSwitchNumber);
					key = buf_key;
					value = buf_value;
					
					ret = ManagerFactory::instance()->m_redis_clients.setvalue(key,value);
					if(ret == 0)
					{
						LOG_TRACE_FORMAT("TRACE :add serverlist mdb success key = [%s],value = [%s]\n", key.c_str(), value.c_str());
						//return true;	
					}
					else
					{
						LOG_TRACE_FORMAT("TRACE :add serverlist mdb error:%d, key = [%s],value = [%s]\n", ret, key.c_str(), value.c_str());
						//return false;
					}
			  }
		   }
		   else
	   		{	
				break;
		   	}
	  }
	return 0;
}

int Switch_Control::SendResetMessage(const char* phostIp,int iPort)
{
	struct sockaddr_in s_addr;
	int sockid;
	socklen_t addr_len;

    sockid=socket(AF_INET,SOCK_STREAM,0);
	    
    s_addr.sin_family = AF_INET ;

   	s_addr.sin_addr.s_addr = inet_addr(phostIp) ;
    s_addr.sin_port=htons((unsigned short)iPort);

   
/*	 if (-1 == fcntl(sockid, F_SETFL, O_NONBLOCK))
	 {
		 printf("fcntl socket error!\n");
		 fflush(stdout);
		 return -1;
	 }	 
*/
	 
	 unsigned long ul = 1;
	 ioctl(sockid, FIONBIO, &ul); //设置为非阻塞模式
	 struct timeval timeout={3,1000*500}; //1秒
	 int len = sizeof(timeout);
	 setsockopt(sockid,SOL_SOCKET,SO_SNDTIMEO, &timeout,len);
	 setsockopt(sockid,SOL_SOCKET,SO_RCVTIMEO,&timeout,len);

	fd_set set;
 	printf("---begin connect \n");
    //重复连接3次，防止中断等原因导致的异常
    for(int i=0;i<3;i++)
    {
    	bool ret = false;
		int iret = -1;
		iret = connect(sockid,(struct sockaddr *)&s_addr,(int)(sizeof(s_addr)));
  		if(iret == -1)
   		{
   			struct timeval tm;
     		tm.tv_sec  = 0;
     		tm.tv_usec = 1000*500;
     		FD_ZERO(&set);
     		FD_SET(sockid, &set);
			int error=-1;
     		if( select(sockid+1, NULL, &set, NULL, &tm) > 0)
     		{
       			getsockopt(sockid, SOL_SOCKET, SO_ERROR, &error, (socklen_t *)&len);
       			if(error == 0) 
				{
					ret = true;
					printf("---error no \n");
					//::close(sockid);
					//break;
   				}
 				else ret = false;
    		} else ret = false;
  		}
 		else if(iret==0)
		{	
			
			break;
		}
		if(i==2) 
    	 {
    	 	  //cout<<"connect Error "<<p_host<<":"<<p_Port<<endl;
    	 	  //perror("::connect");
    	 	  ::close(sockid);
    	 	  return -1;
    	 }
		 printf("---sleeep\n");
    	 usleep(1000);
    	 //cout<<"connect again: "<<p_host<<":"<<p_Port<<endl;
    }
    int     optval = 1;

	 ul = 0;
	 ioctl(sockid, FIONBIO, &ul); //设置为阻塞模式
	printf("---connect success \n");	

	char alivetick[1024]={0};
	int iData=0;
	char strdata[32]={0};
	sprintf(strdata,"%d",iData);
  	//发送心跳
	Stream ptmpRequest;

	ptmpRequest.m_clientSocket = sockid;
	//cJSON *pRet_root;
	ptmpRequest.pRet_root = cJSON_CreateObject();
	ptmpRequest.Requst_Json_str(2,"cmd","reset_device");
	ptmpRequest.Requst_Json_str(2,"returnCode","0");
	ptmpRequest.Requst_Json_str(2,"serialno","11111111");
	ptmpRequest.Send_Jsoon_str();

	memset(alivetick,0,sizeof(alivetick));
	int length = 0;
	int i_rc = 0, i_count = 0;
	int iRecvLen = 0;
	do
	{
		i_rc = recv(sockid, alivetick + i_count, 2000 - i_count, 0);
		if (i_rc <= 0)break;//异常关闭
		i_count += i_rc;
	} while (strstr(alivetick, "XXEE") == NULL);
	iRecvLen = i_count;
	if (iRecvLen <= 0){
		::close(sockid);
		return -1;
	}
	printf("recv:%s \n",alivetick);

	cJSON *pcmd = NULL;

			//解析报文数据
	replace(alivetick, "XXEE", "");
	cJSON* pRoot = cJSON_Parse(alivetick);

	if (pRoot)
	{
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
			//判断请求类型
			if(strcmp(pcmd->valuestring, "reset_device") == 0)
			{
				::close(sockid);
				return 0;
			}
		}

	}
	::close(sockid);
	return -1;
}


int Switch_Control::ResetAllSwitch()
{
			//遍历切流器
		pthread_mutex_lock(&ManagerFactory::instance()->m_mutexServerList);
		MapSwitchSeverInfo::iterator iterLook = ManagerFactory::instance()->m_mapSwitchServerInfo.begin();
		while(iterLook != ManagerFactory::instance()->m_mapSwitchServerInfo.end())
		{
			//
			SwitchSeverInfo *pTmpSeverInfo = iterLook->second;
			if(pTmpSeverInfo && pTmpSeverInfo->iRunStatus == 0)  //在线的切流器
			{
				int iRet = SendResetMessage(pTmpSeverInfo->strServerIPAddr,pTmpSeverInfo->iListenPort);
				if(iRet == 0)
				{
					LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流成功 \n",pTmpSeverInfo->strServerIPAddr);
					pTmpSeverInfo->iStreamStatus = 0;
				}
				else
				{
					
					LOG_INFO_FORMAT("INFO  - [SWM]:切流器[%s] 重置流失败 \n",pTmpSeverInfo->strServerIPAddr);
					pTmpSeverInfo->iStreamStatus = 1;  //流状态异常
				}
			}
			else
				printf("---切流器 不在线 %s \n",pTmpSeverInfo->strServerIPAddr);
			
			++iterLook;
		}

		pthread_mutex_unlock(&ManagerFactory::instance()->m_mutexServerList);
	return 0;
}

void *Switch_Control::TCP_Accept_Control_thread(void *arg)
{

	Switch_Control *this0 = (Switch_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		fflush(stderr);
		LOG_ERROR("ERROR  SWM : Create Socket ERROR \n");
		return NULL;
	} else
		printf("create socket.\n\r");
	fflush(stdout);

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iMsiPort);


	s_addr.sin_addr.s_addr = inet_addr(this0->SW_ip.c_str());//INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		LOG_ERROR("ERROR  SWM : Set Socket ReUSED ERROR \n");
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) {
		perror("bind");
		fflush(stderr);
		LOG_ERROR("ERROR  SWM : Bind Socket ERROR \n");
		return NULL;
	}else
		printf("bind address to socket.\n\r");
	fflush(stdout);

	if(listen(sock,10)<0)
	{
		perror("listen");
		fflush(stderr);
		LOG_ERROR("ERROR  SWM : Listen Socket ERROR \n");
		return NULL;
	}

	int accept_fd = -1;
	struct sockaddr_in remote_addr;
	memset(&remote_addr,0,sizeof(remote_addr));

	this0->m_MsiacceptSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
				fflush(stdout);
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		fflush(stdout);

		if(this0->m_MsiacceptSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_MsiacceptSocket = accept_fd;

		//将socket加入队列
		pthread_mutex_lock(&this0->m_lockerQuesocket);
		this0->m_QueSokcet.push(accept_fd);
		pthread_mutex_unlock(&this0->m_lockerQuesocket);
		
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_MSI_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}


}


bool Switch_Control::init(const char * ConfigFile)
{
	char ip[64];
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false)  return false;

	SW_version = "V1.0.00.4";
	SW_netname=cfg.getValue("SWM.NETNAME");
	GetHostIp(SW_netname.c_str(),ip);
	SW_ip=ip;
	SW_listentport=cfg.getValue("SWM.LISTENTPORT");
    string SW_SeverList;
    SW_SeverList=cfg.getValue("SWM_SERVER.LIST");

	char SW_Net_ip[256]={0};
	strcpy(SW_Net_ip,SW_ip.c_str());

		//redis config
	/*
	SWM.REDIS_SERVER_IP = 127.0.0.1
	SWM.REDIS_SERVER_PORT = 6379
	SWM.REDIS_SERVER_PASSWD = 123
	SWM.REDIS_POOL_NUM = 2
	*/
	SWM_redis_serverip = cfg.getValue("SWM.REDIS_SERVER_IP");
	SWM_redis_serverpasswd = cfg.getValue("SWM.REDIS_SERVER_PASSWD");
	SWM_redis_serverport = atoi(cfg.getValue("SWM.REDIS_SERVER_PORT").c_str());
	SWM_redis_poolnum = atoi(cfg.getValue("SWM.REDIS_POOL_NUM").c_str());

	//	ManagerFactory::instance()->Init(SW_Net_ip);

	ManagerFactory::instance()->Init(SW_Net_ip,
									SWM_redis_serverip.c_str(),
									SWM_redis_serverport,
									SWM_redis_serverpasswd.c_str(),
									SWM_redis_poolnum);

    //解析出所有的erver列表 格式：“ip:port,ip:port"
  	FindSeverInfo(SW_SeverList.c_str());

	swm_process_thread = 4;
	swm_process_thread = atoi(cfg.getValue("SWM.PROCESS_THREADS").c_str());
	log_file_path=cfg.getValue("SWM.LOG_FILE_PATH");
	log_file=cfg.getValue("SWM.LOG_FILE");
	log_level=atoi(cfg.getValue("SWM.LOG_SIZE").c_str());
	log_size=atoi(cfg.getValue("SWM.LOG_NUM").c_str());
	log_num=atoi(cfg.getValue("SWM.LOG_LEVEL").c_str());
	iAliveTickInterval = 0;
	iAliveTickInterval = atoi(cfg.getValue("SWM.ALIVE_INTERVAL").c_str());
	cout<<"ALIVE_INTERVAL ="<<iAliveTickInterval<<endl;
	iAliveTickInterval = iAliveTickInterval*1000;

	int iCheckstreamtime = 20;
	iCheckstreamtime = atoi(cfg.getValue("SWM.CHECK_INTERVAL").c_str());
	cout<<"SWM.CHECK_INTERVAL= "<<iCheckstreamtime<<endl;
	ManagerFactory::instance()->m_iCheckStreamTime = iCheckstreamtime*1000;


	if(iAliveTickInterval < MinAliveTickInterval || iAliveTickInterval > MaxAliveTickInterval)
		iAliveTickInterval = MinAliveTickInterval*3;
	
	cout<<endl<<"load config is  ----------------------"<<endl;
	cout<<"SWM_Version ="<<SW_version<<endl;
	cout<<"SWM_ip ="<<SW_ip<<endl;
	cout<<"SWM_listentport ="<<SW_listentport<<endl;
	cout<<"SWM.PROCESS_THREADS ="<<swm_process_thread<<endl;
	cout<<"log_file_path ="<<log_file_path<<endl;
	cout<<"log_file ="<<log_file<<endl;
	cout<<"log_level ="<<log_level<<endl;
	cout<<"ALIVE_INTERVAL ="<<iAliveTickInterval<<endl;
	cout<<"load config over      ----------------------"<<endl;

	LogFactory::instance()->init(log_file_path.c_str(),
   					log_file.c_str(),
   					log_level,
   					LOGOUT_FILE,/*|LOGOUT_SCREEN,*/
   					log_size,//per 256 M
   					log_num);//10 files
	LogFactory::instance()->start();

	
	//init setting 
	LOG_INFO("INFO  - [SWM]:开始启动\n");
	LOG_INFO("INFO  - [SWM]:读取SWM配置文件\n");
	LOG_INFO_FORMAT("INFO  - [SWM]:程序版本[SWM.version] = %s\n",SW_version.c_str());
	LOG_INFO_FORMAT("INFO  - [SWM]:服务端IP[SWM.tcpserver.ip] = %s\n",SW_ip.c_str());
	LOG_INFO_FORMAT("INFO  - [SWM]:服务端口[SWM.tcpserver.port] = %s\n",SW_listentport.c_str());

	LOG_INFO_FORMAT("INFO  - [SWM]:数据库服务IP[SWM.redis.ip] = %s\n",SWM_redis_serverip.c_str());
	LOG_INFO_FORMAT("INFO  - [SWM]:数据库服务密码[SWM.redis.passwd] = %s\n",SWM_redis_serverpasswd.c_str());
	LOG_INFO_FORMAT("INFO  - [SWM]:数据库服务端口[SWM.redis.port] = %d\n",SWM_redis_serverport);
	LOG_INFO_FORMAT("INFO  - [SWM]:数据库服务连接数[SWM.redis.poolnum] = %d\n",SWM_redis_poolnum);

	m_iMsiPort = atoi(SW_listentport.c_str());


	
//	Swm_Worker_Sever sever_Worker;
//	sever_Worker.setName("swm_sever");
//	if(!sever_Worker.open(m_iMsiPort,swm_process_thread))
//		return -1;
			LOG_INFO("INFO	- [SWM]: 启动成功 ........\n");



			//启动监听线程
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, TCP_Accept_Control_thread, this);
		pthread_detach(tcp_recv_thread1);

		
		//启动监测列表线程
	pthread_t tcp_recv_thread4;
	pthread_create(&tcp_recv_thread4, NULL, ts_recv_Control_thread, this);
	pthread_detach(tcp_recv_thread4);


		//Sleep
	sleep(1);
	printf("---send reset msg\n");
	//发送重置命令
	ResetAllSwitch();

	ManagerFactory::instance()->ClearAllRedisData();

//	sever_Worker.wait();
	LogFactory::instance()->wait();

		printf("---send reset msg1\n");
/*
	m_pSwitchManager = new Switch_Manager;
	char SW_Net_ip[256]={0};
	strcpy(SW_Net_ip,SW_ip.c_str());
	m_pSwitchManager->Init(SW_Net_ip);
*/

	



	 return true;
}


