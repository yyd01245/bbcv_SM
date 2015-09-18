#include "VGW_VodStream.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "Base64.h"
#include "DBInterface.h"


VGW_Vod_Stream::VGW_Vod_Stream(SM_Manager *pManager)
:Stream()
{

	//对方模块的端口，IP;
	//char dstIP[128] = "192.168.30.160";;// "192.168.30.160";//"192.168.60.249";
	memcpy(m_strdstIP,strVodIP,strlen(strVodIP)+1);
	m_idstport = iVodPort;
	m_pManager = pManager;
	//启动客户端链接
//	if(!Connect_VODServer())
	{
//		printf("error can't connect to server \n");
	
	}
	//printf("---connect Adv over\n");
/*
	//启动接收线程
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, ts_recv_Nav_thread, this);
	pthread_detach(tcp_recv_thread1);
*/
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
	if( (socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0 ) 
	{  
			 printf("create socket error: (errno:)\n)");	
			 return false;
	 }	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	printf("server ip=%s port=%d \n",m_strdstIP,m_idstport);
	
    if( inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0 )
	{  
        printf("inet_pton error for %s\n",m_strdstIP);  
        exit(0);  
	}  

	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
        printf("connect error: (errno:)\n");  
        exit(0);  
    }  
	printf("connect to server: \n");
	m_clientSocket = socket_fd;

//	CleanVODStream("12345","1000000","123456789");

	
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Vod_thread, this);
	pthread_detach(tcp_recv_thread1);


	return true;
}

bool VGW_Vod_Stream::CleanVODStream(char *strSeesionId,char *strTaskID,char *strSerialno)
	
{
	if(!Connect_VODServer())
	{
		printf("error can't connect to server \n");
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}

	//char strappuserid[32]="010004003221900024C145A51A";
	//char strspuserid[32]="010004003221900024C145A51A";
	//printf("--begin json\n");
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","logout");
	Requst_Json_str(2,"sessionid",strSeesionId);

	Requst_Json_str(2,"appuserid",strSeesionId);
	Requst_Json_str(2,"spuserid",strSeesionId);
	//Requst_Json_str(2,"sid",strTaskID);
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
	
			printf("recv:%s \n",Rcv_buf);
	
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
						printf("--add_Vod_stream return \n");

						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						//通过
						cJSON* pCip = cJSON_GetObjectItem(pRoot, "cip");
						//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
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
						//找到流对应数据，
#ifndef USEMAP
						char strkey_value[64] = {0};
						sprintf(strkey_value,"%d",iSeessionID);
						StreamStatus pTmpResource;
						memset(&pTmpResource,0,sizeof(pTmpResource));
						int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
						if(iret)
						{
							StreamStatus* ptmp = &pTmpResource;
#else								
						MapStreamStatus::iterator itFind = this0->m_pManager->m_mapStreamStatus.find(iSeessionID);
						if(itFind != this0->m_pManager->m_mapStreamStatus.end())
						{
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
#endif
							if(iRetCode >= 0)
							{
								strcpy(ptmp->strStreamType,Status_Vod);
								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								printf("--time =%s \n",nowTime);


								strcpy(ptmp->strStatus_date,nowTime);
								//char strSwitch_taskID[64] = {0};
								//sprintf(strSwitch_taskID,"%d",iTaskID);
								//strcpy(ptmp->strSwitch_task_id,strSwitch_taskID);

								
								//修改对应数据库
								DBInterfacer::GetInstance()->update_table(1,ptmp);
								
							}
							else
							{
								// retcode 失败需要处理
							}					
							
							//查找需要回复给网关的
							MapVodPlay::iterator itfind= this0->m_mapVod_player.find(iSeessionID);
							if(itfind != this0->m_mapVod_player.end())
							{
								//找到对应信息
								Stream* ptmpRequest = itfind->second;
								//cJSON *pRet_root;
								ptmpRequest->Requst_Json_str(2,"cmd","vod_play");
							
								char txt[32] ={0};
								sprintf(txt,"%d",iRetCode);

								char strVodkey_addr[128] = {0};
								sprintf(strVodkey_addr,"%s:%d",strCip,icPort);
								printf("----vod key ipaddr %s \n",strVodkey_addr);
								ptmpRequest->Requst_Json_str(2,"ret_code",txt);
								ptmpRequest->Requst_Json_str(2,"key_addr",strVodkey_addr); //vod 地址
								ptmpRequest->Send_Jsoon_str();

								this0->m_mapVod_player.erase(itfind);
								delete ptmpRequest;
								ptmpRequest = NULL;
							}
						}

					}
					else if(strcmp(pcmd->valuestring, "logout") == 0)
					{
						
						//通过

					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
						//printf("parse--%s\n",pSeesid->valuestring);
						int iSeessionID = 0;
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						//printf("parse--%s\n",pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						//printf("parse--%s\n",pSerialNo->valuestring);
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						iSeessionID = atoi(strSerialNo);
						
						//更新流信息状态
						//找到流对应数据，
						printf("--del_vod_stream return \n");
#ifndef USEMAP
						char strkey_value[64] = {0};
						sprintf(strkey_value,"%d",iSeessionID);
						StreamStatus pTmpResource;
						memset(&pTmpResource,0,sizeof(pTmpResource));
						int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
						if(iret)
						{
							StreamStatus* ptmp = &pTmpResource;
#else								
						MapStreamStatus::iterator itFind = this0->m_pManager->m_mapStreamStatus.find(iSeessionID);
						
						if(itFind != this0->m_pManager->m_mapStreamStatus.end())
						{
							printf("find data in map \n");
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
#endif
							if(iRetCode >= 0)
							{
								//db 修改数据状态为空闲。或者补为广告流
								printf("change status to idle\n");
								//修改状态
								strcpy(ptmp->strStreamType,Status_Idle);
								//this0->m_pManager->AddOneAdvStream(iSeessionID);
								
								//DBInterfacer::GetInstance()->update_table(1,ptmp);
								//usleep(1000*100);

									pthread_mutex_lock(&this0->m_pManager->m_lockerRegion);
								SetUsedRegionID::iterator itused = this0->m_pManager->m_SetUsedRegionID.find(iSeessionID);
								if(itused != this0->m_pManager->m_SetUsedRegionID.end())
								{
									//有正在使用的等会释放
									
									char txt[128]={0};
									sprintf(txt,"%d",itused->second);
									printf("-----erase region %s success \n",txt);
									this0->m_pManager->m_SetUsedRegionID.erase(itused);
									this0->m_pManager->m_idex = itused->second;	
								}
								pthread_mutex_unlock(&this0->m_pManager->m_lockerRegion);
								
							}
							else
							{
								// retcode 失败保留数据在map中
							}
							//修改对应数据库
							printf("VGW change status to over\n");
						}	
					
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						printf("--check_session return \n");
						//通过
						cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));

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
	
					}
					
				}
			}
			
	}
	close(accept_fd);
	//this0->m_clientSocket = -1;
}

bool VGW_Vod_Stream::StartOneStream(int iStreamID,char *strUrl,char *strRegionid,char* strUserID)
{
	if(!Connect_VODServer())
	{
		m_mapVod_player.clear();
		printf("error can't connect to server \n");
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}

	char strSeesionId[32]={0};
	sprintf(strSeesionId,"%d",iStreamID);
	
	char strSID[32]="3002";
	char strstype[32]="3302";
	char strctype[32]="5";
	char strvtype[32]="2";

	
	//char strappuserid[32]="010004003221900024C145A51A";
	//char strspuserid[32]="010004003221900024C145A51A";
	char strareaid[32]="2";
	char strMsg[1024]={0};
	char striip[32]="3301";
	char striport[32]="3301";
	char strsip[32]={0};
	strcpy(strsip,strMyServerIP);
	char strsport[32]={0};
	sprintf(strsport,"%d",iMSIServerPort);
	

	char strRegionID[32]="0x0603"; //"0x0604"
	//char strOutUrl[512]= "rtsp://192.168.100.11:8845/yjy_ipqam/bjtv.ts";  //"rtsp://192.168.100.11:8845/yjy_ipqam/8081/gyc.ts";
//	unsigned long ilen= sizeof(strOutUrl);

	unsigned long ulInLen=strlen(strUrl);
	ZBase64 tmpEncode;
	std::string strencodemsg = tmpEncode.Encode((unsigned char*)strUrl,ulInLen);
	sprintf(strMsg,"%s|%s",strencodemsg.c_str(),strRegionid);
	printf("---%s \n",strUrl);
	printf("%s \n",strMsg);
	

	
	
	//需要得到返回值才可作为点播成功
	pRet_root = cJSON_CreateObject();
/*	Requst_Json_str(2,"appuserid",strappuserid);
	Requst_Json_str(2,"spuserid",strspuserid);
	Requst_Json_str(2,"areaid",strareaid);
	Requst_Json_str(2,"sid",strSID);
	Requst_Json_str(2,"iip",striip);
	Requst_Json_str(2,"iport",striport);
	Requst_Json_str(2,"sip",strsip);
	Requst_Json_str(2,"sport",strsport);	
	Requst_Json_str(2,"msg",strMsg);	
*/	
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
	Requst_Json_str(2,"appuserid",strSeesionId);
	Requst_Json_str(2,"spuserid",strUserID);

	
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
		printf("error can't connect to server \n");
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}

	//char strappuserid[32]="010004003221900024C145A51A";
	//char strspuserid[32]="010004003221900024C145A51A";
	//printf("--begin json\n");
	char streamid[32] = {0};
	sprintf(streamid,"%d",strSeesionId);
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","session_check");
	Requst_Json_str(2,"sessionid",streamid);

	Requst_Json_str(2,"appuserid",streamid);
	Requst_Json_str(2,"spuserid",streamid);
	//Requst_Json_str(2,"sid",strTaskID);
	//Requst_Json_str(2,"serialno",strSerialno);
	
	Send_Jsoon_str();
	return true;	

}




