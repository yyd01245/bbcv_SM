#include "VGW_VodStream.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "Base64.h"
#include "DBInterface.h"


VGW_Vod_Stream::VGW_Vod_Stream(SM_Manager *pManager)
:Stream()
{
	//对方模块的端口，IP;
	memcpy(m_strdstIP,strVodIP,strlen(strVodIP)+1);
	m_idstport = iVodPort;
	m_pManager = pManager;
}
VGW_Vod_Stream::~VGW_Vod_Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	
	m_clientSocket = -1;
}

bool VGW_Vod_Stream::Connect_VODServer()
{
	int socket_fd;	
	struct sockaddr_in server_addr;  
	if( (socket_fd = socket(AF_INET,SOCK_STREAM,0))< 0) 
	{  
		 LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		 return false;
	}	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d server ip=%s port=%d\n",__FUNCTION__,__LINE__,m_strdstIP,m_idstport);
    if( inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0 )
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d inet_pton error for %s\n",__FUNCTION__,__LINE__,m_strdstIP);
        exit(0);  
	}  
	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  	
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d connect error\n",__FUNCTION__,__LINE__);
        exit(0);  
    }
	
	m_clientSocket = socket_fd;

	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1,NULL,Parse_recv_Vod_thread, this);
	pthread_detach(tcp_recv_thread1);
	return true;
}

bool VGW_Vod_Stream::CleanVODStream(char *strSeesionId,char *strTaskID,char *strSerialno)
	
{
	if(!Connect_VODServer())
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
	Requst_Json_str(2,"cmd","logout");
	Requst_Json_str(2,"sessionid",strSeesionId);

	Requst_Json_str(2,"appuserid",strSeesionId);
	Requst_Json_str(2,"spuserid",strSeesionId);
	Requst_Json_str(2,"serialno",strSerialno);
	
	Send_Jsoon_str();
	return true;
}

void *VGW_Vod_Stream::Parse_recv_Vod_thread(void * arg)
{
	VGW_Vod_Stream *this0 = (VGW_Vod_Stream*)arg;
	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_clientSocket;
	while(1)
	{
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
					if (strcmp(pcmd->valuestring, "login") == 0)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --add_Vod_stream return\n",__FUNCTION__,__LINE__);
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						//通过
						if(iRetCode<0)
						{
							cJSON* pSionid = cJSON_GetObjectItem(pRoot,"sessionid");
							int iSessionid = -1;
							if(pSionid) 
								iSessionid = atoi(pRetCOde->valuestring);
							if(Istype == 1)
							{
								SetUsedRegionID::iterator istused = this0->m_pManager->m_SetUsedRegionID.find(iSessionid);
								if(istused != this0->m_pManager->m_SetUsedRegionID.end())
								{
									//有正在使用的等会释放
									char txt[128]={0};
									sprintf(txt,"%d",istused->second);
									LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d erase region %s success\n",__FUNCTION__,__LINE__,txt);
									this0->m_pManager->m_SetUsedRegionID.erase(istused);
									this0->m_pManager->m_idex = istused->second;
								}
							}
						}
						else
						{
							cJSON* pCip = cJSON_GetObjectItem(pRoot, "cip");
							char strCip[128]={0};
							if(pCip)
								strcpy(strCip,pCip->valuestring);
							cJSON* pCPort = cJSON_GetObjectItem(pRoot, "cport");
							int icPort = -1;
							if(pCPort) icPort = atoi(pCPort->valuestring);

							cJSON* pMsg = cJSON_GetObjectItem(pRoot,"msg");
							char strMsg[128] ={0};
							if(pMsg)
							memcpy(strMsg,pMsg->valuestring,strlen(pMsg->valuestring)+1);

					
							cJSON* pTotalTime = cJSON_GetObjectItem(pRoot,"totaltime");
							char strTotalTime[128] ={0};
							if(pTotalTime)
								memcpy(strTotalTime,pTotalTime->valuestring,strlen(pTotalTime->valuestring)+1);
										
							cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
							char strSerialNo[128] ={0};
							if(pSerialNo)
								memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
										
							int iSeessionID = atoi(strSerialNo);
						//更新流信息状态
						//找到流对应数据
							char strkey_value[64] = {0};
							sprintf(strkey_value,"%d",iSeessionID);
							StreamStatus pTmpResource;
							memset(&pTmpResource,0,sizeof(pTmpResource));
							int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);

							if(iret)
							{
								StreamStatus* ptmp = &pTmpResource;
								strcpy(ptmp->strStreamType,Status_Vod);
								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
									// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
								tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								//	LOG_INFO_FORMAT("%s %d --time =%s\n",__FUNCTION__,__LINE__,nowTime);
								strcpy(ptmp->strStatus_date,nowTime);
									//修改对应数据库
								DBInterfacer::GetInstance()->update_table(1,ptmp);

								//查找需要回复给网关的
								MapVodPlay::iterator itfind= this0->m_mapVod_player.find(iSeessionID);
								if(itfind != this0->m_mapVod_player.end())
								{
									//找到对应信息
									Stream* ptmpRequest = itfind->second;
									//cJSON *pRet_root;
									LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --sock fd = %d\n",__FUNCTION__,__LINE__,ptmpRequest->m_clientSocket);
									ptmpRequest->Requst_Json_str(2,"cmd","vod_play");
									
									char txt[32] ={0};
									sprintf(txt,"%d",iRetCode);
									
									char strVodkey_addr[128] = {0};
									sprintf(strVodkey_addr,"%s:%d",strCip,icPort);
									LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --vod key ipaddr %s\n",__FUNCTION__,__LINE__,strVodkey_addr);
									ptmpRequest->Requst_Json_str(2,"ret_code",txt);
									ptmpRequest->Requst_Json_str(2,"key_addr",strVodkey_addr); //vod 地址
									ptmpRequest->Send_Jsoon_str();

									this0->m_mapVod_player.erase(itfind);
									delete ptmpRequest;
									ptmpRequest = NULL;
								}
							}
						}
						break;	
				    }		
					else if(strcmp(pcmd->valuestring, "logout") == 0)
					{
						//通过
						int iSeessionID = 0;
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
			
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						iSeessionID = atoi(strSerialNo);
						
						//更新流信息状态
						//找到流对应数据
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --del_vod_stream return\n",__FUNCTION__,__LINE__);
						if(iRetCode >= 0)
						{
							//db 修改数据状态为空闲。或者补为广告流
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d change status to idle\n",__FUNCTION__,__LINE__);
								
							if(Istype == 1)
							{
								pthread_mutex_lock(&this0->m_pManager->m_lockerRegion);
								SetUsedRegionID::iterator itused = this0->m_pManager->m_SetUsedRegionID.find(iSeessionID);
								if(itused != this0->m_pManager->m_SetUsedRegionID.end())
								{
									//有正在使用的等会释放
									char txt[128]={0};
									sprintf(txt,"%d",itused->second);
									LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ----erase region %s success\n",__FUNCTION__,__LINE__,txt);
									this0->m_pManager->m_SetUsedRegionID.erase(itused);
									this0->m_pManager->m_idex = itused->second;	
								}
								pthread_mutex_unlock(&this0->m_pManager->m_lockerRegion);
							 }	
						 }	
						 else
						 {
							LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ----logout error\n",__FUNCTION__,__LINE__);
						 }
						 LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d VGW change status to over\n",__FUNCTION__,__LINE__);
						 break;
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --check_session return\n",__FUNCTION__,__LINE__);
						//通过
						cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						int iSeessionID = 0;
						if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						if(iRetCode<0)
						{
							this0->m_pManager->CleanStream(iSeessionID);
							usleep(1000*1500);
							this0->m_pManager->AddOneAdvStream(iSeessionID);
						}
						break;
					}

					else if(strcmp(pcmd->valuestring, "query_type") == 0)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d --check query_type return\n",__FUNCTION__,__LINE__);
						//通过
						cJSON* pStype = cJSON_GetObjectItem(pRoot, "vgw_type");

						char stype[64] = {0};
						if(pStype)
						{
							memcpy(stype,pStype->valuestring,strlen(pStype->valuestring)+1);
						}
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						if(iRetCode>=0)
						{
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d stype = %s\n",__FUNCTION__,__LINE__,stype);
							if(strcmp(stype,"SIHUA")==0)
							{
								Istype = 1;
								LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is SIHUA\n",__FUNCTION__,__LINE__);
							}
							else if(strcmp(stype,"ENRICH")==0)
							{
								Istype = 0;	
								LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is ENRICH\n",__FUNCTION__,__LINE__);
							}
							else if(strcmp(stype,"VLC")==0)
							{
								LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is VLC\n",__FUNCTION__,__LINE__);
								Istype = 2;	
							}
						}
						break;
					}
				}
			}
			
	}
	close(accept_fd);
}

bool VGW_Vod_Stream::StartOneStream(int iStreamID,char *strUrl,char *strRegionid,char* strUserID,char *vodname,char *posterurl,int fd,char *outputid,char *outputport)
{
	if(fd)
	{
		MapVodPlay::iterator fdfind= m_mapVod_player.find(iStreamID);
		if(fdfind != m_mapVod_player.end())
		{
				//找到对应信息
			Stream* sptmpRequest = fdfind->second;			
			if(fd != sptmpRequest->m_clientSocket)
			{
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d the socket is changed,before = %d,after = %d\n",__FUNCTION__,__LINE__,sptmpRequest->m_clientSocket,fd);
				sptmpRequest->m_clientSocket = fd;	
			}
		}
	}
	
	if(!Connect_VODServer())
	{
		m_mapVod_player.clear();
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ---Istype = %d\n",__FUNCTION__,__LINE__,Istype);
	char strSeesionId[32]={0};
	sprintf(strSeesionId,"%d",iStreamID);
	
	char strSID[32]="3002";
	char strstype[32]="3302";
	char strctype[32]="5";
	char strvtype[32]="2";

	char strareaid[32]="2";
	char strMsg[2048]={0};
	char striip[32]="3301";
	char striport[32]="3301";
	char strsip[32]={0};
	strcpy(strsip,strMyServerIP);
	char strsport[32]={0};
	sprintf(strsport,"%d",iMSIServerPort);
	

	char strRegionID[32]="0x0603"; //"0x0604"
	//需要得到返回值才可作为点播成功
	pRet_root = cJSON_CreateObject();
  	if(Istype != 2)
  	{
		unsigned long ulInLen=strlen(strUrl);
		ZBase64 tmpEncode;
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,strUrl);
		if(Istype == 1)
		{
			std::string strencodemsg = tmpEncode.Encode((unsigned char*)strUrl,ulInLen);
			sprintf(strMsg,"%s|%s",strencodemsg.c_str(),strRegionid);
		}
		else
		{
			char tempbase[2048] = {0};
			tmpEncode.Encode((unsigned char*)strUrl,tempbase,ulInLen);
			sprintf(strMsg,"%s|123",tempbase);
		}
		Requst_Json_str(2,"cmd","login");
		Requst_Json_str(2,"sessionid",strSeesionId);
		Requst_Json_str(2,"sid",strSID);
		Requst_Json_str(2,"stype",strstype);
		Requst_Json_str(2,"ctype",strctype);
		Requst_Json_str(2,"vtype",strctype);

		Requst_Json_str(2,"url","");
		Requst_Json_str(2,"areaid",strareaid);
		Requst_Json_str(2,"sip",strsip);
		Requst_Json_str(2,"sport",strsport);	
		Requst_Json_str(2,"serialno",strSeesionId);
		Requst_Json_str(2,"msg",strMsg);
		Requst_Json_str(2,"vodname",vodname);
		Requst_Json_str(2,"posterurl",posterurl);
		Requst_Json_str(2,"appuserid",strSeesionId);
		Requst_Json_str(2,"spuserid",strUserID);

	//与enrich对接
		if(Istype == 0)
		{
			char sfrequency[64] = {0};
			char spid[64] = {0};
			char ipqamip[64] = {0};
			int iport;
			char ipqamport[64] = {0};
			if(advflag == 0)
			{
				memcpy(ipqamip,strAdverIP,strlen(strAdverIP)+1);
				sprintf(ipqamport,"%d",iBaseport+iStreamID);
			}
			else if(advflag == 1)
			{
				memcpy(ipqamip,outputid,strlen(outputid)+1);
				sprintf(ipqamport,"%s",outputport);
			}
			sprintf(sfrequency,"%d",frequency);
			sprintf(spid,"%d",pid);

			Requst_Json_str(2,"ipqamIP",ipqamip);
			Requst_Json_str(2,"ipqamPort",ipqamport);
			Requst_Json_str(2,"frequency",sfrequency);
			Requst_Json_str(2,"pid",spid);
		}
  	}	
    if(Istype == 2)
    {
		char flag[10] = "|";
		char totalTime[32] = {0};
		char *p = NULL;
		char output[64] = {0};
		p = strstr(strUrl,flag);
		if(p)
		{
			*p = 0;
			p = p+1;
			memcpy(totalTime,p,strlen(p)+1);
			if(advflag == 0)
			{
				sprintf(output,"udp://%s:%d",strAdverIP,iBaseport+iStreamID);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d totalTime = %s,posturl = %s,outputurl = %s\n",__FUNCTION__,__LINE__,totalTime,strUrl,output);
			}
			else if(advflag == 1)
			{
				sprintf(output,"udp://%s:%s",outputid,outputport);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d totalTime = %s,posturl = %s,outputurl = %s\n",__FUNCTION__,__LINE__,totalTime,strUrl,output);
				fflush(stdout);
			}
		}	
		Requst_Json_str(2,"cmd","login");
		Requst_Json_str(2,"sessionid",strSeesionId);
		
		Requst_Json_str(2,"input",strUrl);
		Requst_Json_str(2,"output",output);	
		Requst_Json_str(2,"serialno",strSeesionId);
		Requst_Json_str(2,"vodname",vodname);
		Requst_Json_str(2,"posterurl",posterurl);
		Requst_Json_str(2,"appuserid",strSeesionId);
		Requst_Json_str(2,"spuserid",strUserID);
		Requst_Json_str(2,"totalTime",totalTime);
		Requst_Json_str(2,"sip",strsip);
		Requst_Json_str(2,"sport",strsport);
    }
	
	Send_Jsoon_str();
	return true;
}

bool VGW_Vod_Stream::AddSemToStream(int iStreamID,void *psem)
{
	MapSem_Stream::iterator itfid = m_mapSem_Stream.find(iStreamID);
	if(itfid != m_mapSem_Stream.end())
	{
		//更新
		(itfid->second) = psem;
	}
	else
	{
		m_mapSem_Stream.insert(MapSem_Stream::value_type(iStreamID,psem));
	}
	return true;
}

bool VGW_Vod_Stream::CheckStatus(int strSeesionId,char *strTaskID,char *strSerialno)
{
	if(!Connect_VODServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}

	char streamid[32] = {0};
	sprintf(streamid,"%d",strSeesionId);
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","session_check");
	Requst_Json_str(2,"sessionid",streamid);

	Requst_Json_str(2,"appuserid",streamid);
	Requst_Json_str(2,"spuserid",streamid);

	Send_Jsoon_str();
	return true;	
}

bool VGW_Vod_Stream::Checktype()
{
	if(!Connect_VODServer())
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
	Requst_Json_str(2,"cmd","query_type");
	Requst_Json_str(2,"serialno","dsfjejhuse6789323sdf");
	
	Send_Jsoon_str();
	
	return true;	

}




