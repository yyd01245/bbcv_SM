#include "Advertisement.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"
#include <sys/time.h>

Advertisement_Stream::Advertisement_Stream(SM_Manager *pManager)
:Stream()
{

	//对方模块的端口IP;
	
	if(advflag == 0)
	{
		memcpy(m_strdstIP,strAdverIP,strlen(strAdverIP)+1);
		m_idstport = iAdverPort;
		
		//__FUNCTION__, __LINE__
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d use the ordinary switch machine\n",__FUNCTION__,__LINE__);
	}
	else
	{
		memcpy(m_strdstIP,strblanIP,strlen(strblanIP)+1);
		m_idstport = iBlanport;
		
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d use the balance switch machine\n",__FUNCTION__,__LINE__);
	}
	m_pManager = pManager;
	connsocket.clear();
}
Advertisement_Stream::~Advertisement_Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	
	m_clientSocket = -1;

}

bool Advertisement_Stream::Connect_AdvServer()
{
	int socket_fd;	
	struct sockaddr_in server_addr; 
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d BEGIN TO CONNECT AdvServer\n",__FUNCTION__,__LINE__);
	if((socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0) 
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Adver create socket error\n",__FUNCTION__,__LINE__);
		return false;
	}	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d Adver server ip=%s port=%d\n",__FUNCTION__,__LINE__,m_strdstIP,m_idstport);
	
    if(inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr)<=0)
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Adver inet_pton error for %s\n",__FUNCTION__,__LINE__,m_strdstIP);
        exit(0);  
	}  

	if(connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Adver connect error\n",__FUNCTION__,__LINE__);
        exit(0);  
    }  
	
	m_clientSocket = socket_fd;
   //将建立连接的套接字放入队列尾端
	connsocket.push_back(socket_fd);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d adv socket fd = %d\n",__FUNCTION__,__LINE__,socket_fd);

	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Adver_thread, this);
	usleep(100);
	pthread_detach(tcp_recv_thread1);
	
	return true;
}


bool Advertisement_Stream::CleanALLAdverStream()
{
	if(!Connect_AdvServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}	
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","reset_device");
	Requst_Json_str(2,"returnCode","0");
	Requst_Json_str(2,"serialno","11111111");
		
	Send_Jsoon_str();
	
	return true;
}


bool Advertisement_Stream::CleanAdverStream(char *strSeesionId,char *strTaskID,char *strSerialno)
	
{
		//启动客户端链接
	if(!Connect_AdvServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}
	
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}

	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","del_ads_stream");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(1,"task_id",strTaskID);
	Requst_Json_str(2,"serialno",strSerialno);
	
	Send_Jsoon_str();
	return true;
}

bool Advertisement_Stream::StartOneStream(char *strSeesionId,char *strInputUrl,char* strOutputUrl,
					char *strSerialno,int temp)
{
	//启动客户端链接
	
	if(!Connect_AdvServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	if((temp == 0)||(temp == 3))
	{
		m_isvod.push_back(temp);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d to start a advstream %d\n",__FUNCTION__,__LINE__,temp);
	}
	
	char strAuthiName[128] = "11";
	char strAuthcode[128] = "22";
	/*
	if(temp == 1)
	{
		pRet_root = cJSON_CreateObject();
		Requst_Json_str(2,"cmd","add_ads_stream");
		Requst_Json_str(2,"sessionid","9");
		Requst_Json_str(2,"input_url",strInputUrl);

		char output_url[64] = {0};
		int aid = atoi(strSeesionId);
		sprintf(output_url,"udp://192.168.30.109:%d",aid+12000);
		Requst_Json_str(2,"output_url",output_url);

		Requst_Json_str(2,"serialno",strSerialno);

		Send_Jsoon_str();
	}
	*/
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","add_ads_stream");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(2,"input_url",strInputUrl);
	Requst_Json_str(2,"output_url",strOutputUrl);
	Requst_Json_str(2,"serialno",strSerialno);

	Send_Jsoon_str();
	
	return true;
}

//处理广告流接口
void *Advertisement_Stream::Parse_recv_Adver_thread(void * arg)
{
	Advertisement_Stream *this0 = (Advertisement_Stream*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = -1;

	Sockfd::iterator itaccept = this0->connsocket.begin();
	if(itaccept != this0->connsocket.end())
	{
		accept_fd = *itaccept;
		this0->connsocket.erase(itaccept);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d the accept fd = %d\n",__FUNCTION__,__LINE__,accept_fd);
	}
	
	while(1)
	{
		memset(Rcv_buf,0,sizeof(Rcv_buf));
		int length = 0;
		int i_rc = 0, i_count = 0;
		do
		{
			i_rc = recv(accept_fd, Rcv_buf + i_count, 2000 - i_count, 0);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d recv accept_fd = %d, len = %d\n",__FUNCTION__,__LINE__,accept_fd,i_rc);
			if (i_rc <= 0)break;//异常关闭
			i_count += i_rc;
		} while (strstr(Rcv_buf, "XXEE") == NULL);
		iRecvLen = i_count;
		if (iRecvLen <= 0) break;
	
		LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n%s\n",__FUNCTION__,__LINE__,Rcv_buf);
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
				 //通过
					cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
					//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
					int iSeessionID = 0;
					if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
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
					int iflag = 1;
						/*
						if(iRetCode < 0)
						{
							Streatype::iterator strList = this0->m_isvod.begin();
							if(strList != this0->m_isvod.end())
							{
								LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d add adv error\n",__FUNCTION__,__LINE__);
								this0->m_isvod.erase(strList);
							}
						}
						*/
						
					Streatype::iterator strList = this0->m_isvod.begin();
					if(strList != this0->m_isvod.end())
					{
						iflag = *strList;
						this0->m_isvod.erase(strList);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d iflag = %d\n",__FUNCTION__,__LINE__,iflag);
					}
					else
					{
						LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d this is not advstream\n",__FUNCTION__,__LINE__);
								
					}
							 
				    if((iflag == 0)||(iflag == 3))
					{
						
						//更新流信息状态
						//找到流对应数据，
						char strkey_value[64] = {0};
						sprintf(strkey_value,"%d",iSeessionID);
						StreamStatus pTmpResource;
						memset(&pTmpResource,0,sizeof(pTmpResource));
						int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
						if(iret)
						{
							StreamStatus* ptmp = &pTmpResource;
									
							if(iflag==0)
								strcpy(ptmp->strStreamType,Status_Adver);	
							else if(iflag==3)
								strcpy(ptmp->strStreamType,Status_Vod);
							char strSwitch_taskID[64] = {0};
							sprintf(strSwitch_taskID,"%d",iTaskID);
							strcpy(ptmp->strSwitch_task_id,strSwitch_taskID);

							time_t timep; 
							time (&timep); 
							struct tm* tmpTime = gmtime(&timep);
							char nowTime[128]={0};
										// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
							sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								//LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d time =%s,streamid = %d\n",__FUNCTION__,__LINE__,nowTime,ptmp->istreamID);
							strcpy(ptmp->strStatus_date,nowTime);
										
								//更新DB 将StreamStatus	
							DBInterfacer::GetInstance()->update_table(1,ptmp);
								
								//修改对应数据库
						}
	
						else
						{
									//新增数据
							StreamStatus *pTmpStream = new StreamStatus;
							memset(pTmpStream,0,sizeof(StreamStatus));

							int istreamID;
							pTmpStream->istreamID = iSeessionID;
							//strcpy(pTmpStream->strStreamType,Status_Adver);
							if(iflag==0)
								strcpy(pTmpStream->strStreamType,Status_Adver);	
							else if(iflag==3)
								strcpy(pTmpStream->strStreamType,Status_Vod);

							char strSwitch_taskID[64] = {0};
							sprintf(strSwitch_taskID,"%d",iTaskID);
							strcpy(pTmpStream->strSwitch_task_id,strSwitch_taskID);

							time_t timep; 
							time (&timep); 
							struct tm* tmpTime = gmtime(&timep);
							char nowTime[128]={0};
							sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								
								//LOG_DEBUG_FORMAT("INFO  - [SM]:%s %d time =%s\n",__FUNCTION__,__LINE__,nowTime);

							strcpy(pTmpStream->strStatus_date,nowTime);
								
							DBInterfacer::GetInstance()->insert_table(1,pTmpStream);
							delete pTmpStream;
						}
					  }	
					   break;
				}
				else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
				
				{
						
						//通过
					cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
					
					int iSeessionID = 0;
					if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
					cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
					int iRetCode = -1;
					if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
					//printf("parse--%s\n",pRetCOde->valuestring);
					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
					char strSerialNo[128] ={0};
					//printf("parse--%s\n",pSerialNo->valuestring);
					if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
					
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d del_ads_stream return\n",__FUNCTION__,__LINE__);
					//更新流信息状态
					//找到流对应数据，
				
					char strkey_value[64] = {0};
					sprintf(strkey_value,"%d",iSeessionID);
					StreamStatus pTmpResource;
					memset(&pTmpResource,0,sizeof(pTmpResource));
					int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
					if(iret)
					{
						StreamStatus* ptmp = &pTmpResource;
						if(iRetCode >= 0)
						{
							//db 修改数据状态为空闲。
							
							strcpy(ptmp->strStreamType,Status_Idle);
							time_t timep; 
							time (&timep); 
							struct tm* tmpTime = gmtime(&timep);
							char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
							sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
								tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
						
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d time = %s\n",__FUNCTION__,__LINE__,nowTime);
								strcpy(ptmp->strStatus_date,nowTime);

							DBInterfacer::GetInstance()->update_table(1,ptmp);
								
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d new nav index =%d size=%d\n",__FUNCTION__,__LINE__,iSeessionID,this0->m_pManager->m_vecNewNav.size());
							
						}
						else
						{
								// retcode 失败保留数据在map中
						}
							//修改对应数据库
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Adver change status to over\n",__FUNCTION__,__LINE__);
					}
				     break;
				}
				else if(strcmp(pcmd->valuestring, "check_session") == 0)
				{
						
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d check_session return\n",__FUNCTION__,__LINE__);
						//通过
					cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						
					int iSeessionID;
					if(pSeesid) 
					iSeessionID = atoi(pSeesid->valuestring);

					cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
					int iRetCode = -1;
					if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);

					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
					char strSerialNo[128] ={0};
					if(pSerialNo->valuestring)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

					cJSON* ptaskid = cJSON_GetObjectItem(pRoot,"task_id");
					char strtaskid[128] ={0};
					if(ptaskid->valuestring)
						memcpy(strtaskid,ptaskid->valuestring,strlen(ptaskid->valuestring)+1);

					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d check stream %d over\n",__FUNCTION__,__LINE__,iSeessionID);
					//SetUsedRegionID::iterator istused = this0->m_pManager->m_SetUsedRegionID.find(iSessionid);
					StreamAbnormal::iterator stait = this0->m_pManager->m_strsta.find(iSeessionID);
					if(stait != this0->m_pManager->m_strsta.end())
					{
						if(iRetCode>=0)
						{
							stait->second = iRetCode;
							LOG_INFO_FORMAT("DEBUG  - [SM]:%s %d check stream %d is ok\n",__FUNCTION__,__LINE__,iSeessionID,iRetCode);
						}
						else 
						{
							if(stait->second<0)
							{
								LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d stream id = %d is error\n",__FUNCTION__,__LINE__,iSeessionID);
								this0->m_pManager->Dealsession(iSeessionID);	
								stait->second = 0;
							}
							else
							{
								LOG_INFO_FORMAT("DEBUG  - [SM]:%s %d check stream %d is error now\n",__FUNCTION__,__LINE__,iSeessionID);
								stait->second = iRetCode;	
							}
						}	
					}
					else
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d check stream %d status = %d\n",__FUNCTION__,__LINE__,iSeessionID,iRetCode);
						this0->m_pManager->m_strsta.insert(StreamAbnormal::value_type(iSeessionID,iRetCode));
					}	
					break;
				}
					
				else if(strcmp(pcmd->valuestring, "req_ads_stream") == 0)
				{
					struct timeval tv1;
					long long time1;
					gettimeofday(&tv1, NULL);
					time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
					
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d recv time =%ld\n",__FUNCTION__,__LINE__,time1);
						//通过
					cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						
					int iSeessionID;
					if(pSeesid) 
					iSeessionID = atoi(pSeesid->valuestring);

					cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
					int iRetCode = -1;
					if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);

					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
					char strSerialNo[128] ={0};
					if(pSerialNo->valuestring)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

					cJSON* ptaskip = cJSON_GetObjectItem(pRoot,"switch_ip");
					char strip[64] ={0};
					if(ptaskip->valuestring)
						memcpy(strip,ptaskip->valuestring,strlen(ptaskip->valuestring)+1);

					cJSON* ptaskport = cJSON_GetObjectItem(pRoot,"switch_port");
					char strport[64] ={0};
					if(ptaskport->valuestring)
						memcpy(strport,ptaskport->valuestring,strlen(ptaskport->valuestring)+1);
					if(iRetCode >=0)
					{
							//	this0->m_pManager->m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));
						Smstream *pmt = new Smstream;
							
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this stream id is %d\n",__FUNCTION__,__LINE__,iSeessionID);
						memcpy(pmt->sadvip,strip,strlen(strip)+1);
						memcpy(pmt->sadvport,strport,strlen(strport)+1);
						this0->m_pManager->m_smstraddr.push_back(pmt);
					}
						
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d get stream %d ip = %s,port = %s\n",__FUNCTION__,__LINE__,iSeessionID,strip,strport);
					break;
				}
					
			}
		}	
	}
	close(accept_fd);
}

bool Advertisement_Stream::CheckStatus(int iStreamID,char *serialno)
{
	if(!Connect_AdvServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Connect_AdvServer ERROR\n",__FUNCTION__,__LINE__);
		return false;
	}
	char strSeesionId[32]={0};
	sprintf(strSeesionId,"%d",iStreamID);
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","check_session");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(2,"serialno",serialno);
		
	Send_Jsoon_str();
}

bool Advertisement_Stream::Getaddr(char* strSeesionId,char *strSerialno)
{
	if(!Connect_AdvServer())
	{

		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Connect_AdvServer ERROR\n",__FUNCTION__,__LINE__);
		return false;
	}
	
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","req_ads_stream");
	Requst_Json_str(2,"sessionid","0");
	Requst_Json_str(2,"serialno",strSerialno);
	
	struct timeval tv1;
	long long time1;
	gettimeofday(&tv1, NULL);
	time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Send time =%ld\n",__FUNCTION__,__LINE__,time1);
	Send_Jsoon_str();
}

