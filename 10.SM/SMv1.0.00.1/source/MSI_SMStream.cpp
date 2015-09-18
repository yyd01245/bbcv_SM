#include "MSI_SMStream.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"


MSI_SM_Stream::MSI_SM_Stream(SM_Manager *pManager)
:Stream()
{

	//¶Ô·½Ä£¿éµÄ¶Ë¿Ú£¬IP;
	//char dstIP[128] = "192.168.30.160";//"192.168.30.160";//"192.168.60.249";
	LOG_INFO_FORMAT("%s %d strMsiIP = %s\n",__FUNCTION__,__LINE__,strMsiIP);
	memcpy(m_strdstIP,strMsiIP,strlen(strMsiIP)+1);
	m_idstport = iMsiPort;
	m_pManager = pManager;
}
MSI_SM_Stream::~MSI_SM_Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	
	m_clientSocket = -1;
	
}

bool MSI_SM_Stream::Connect_MSIServer()
{
	int socket_fd;	
	struct sockaddr_in server_addr;  
	if( (socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0 ) 
	{  
		 LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		 return false;
	 }	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d MSI server ip=%s port=%d \n",__FUNCTION__,__LINE__,m_strdstIP,m_idstport);
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

//	CleanMSIStream("12345","123456789");
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_MSI_thread, this);
		pthread_detach(tcp_recv_thread1);
		
	return true;
}

bool MSI_SM_Stream::CleanMSIStream(char *strUserId,char *strSerialno)	
{
	if(!Connect_MSIServer())
	{	
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}
	
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	//printf("--begin json\n");
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","stream_unbind");
	Requst_Json_str(2,"username",strUserId);
	Requst_Json_str(2,"serialno",strSerialno);
	
	Send_Jsoon_str();					
	return true;
}

bool MSI_SM_Stream::TellMI(int istreamid)
{
	if(!Connect_MSIServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	char username[128] = {0};
	DBInterfacer::GetInstance()->FindUsername(istreamid,username);
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","vod_over");
	Requst_Json_str(2,"username",username);
	Requst_Json_str(2,"serialno","c0e1758d697841fa8dad428c23b867a7");

	Send_Jsoon_str();

}

void *MSI_SM_Stream::Parse_recv_MSI_thread(void * arg)
{
	MSI_SM_Stream *this0 = (MSI_SM_Stream*)arg;

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
				if (i_rc <= 0)break;//Òì³£¹Ø±Õ
				i_count += i_rc;
			} while (strstr(Rcv_buf, "XXEE") == NULL);
			iRecvLen = i_count;
			if (iRecvLen <= 0) break;
	
			LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
					//½âÎö±¨ÎÄÊý¾Ý
			replace(Rcv_buf, "XXEE", "");
			cJSON* pRoot = cJSON_Parse(Rcv_buf);	

			if (pRoot)
			{
				pcmd = cJSON_GetObjectItem(pRoot, "cmd");
				if (pcmd)
				{
					//ÅÐ¶ÏÇëÇóÀàÐÍ
					if (strcmp(pcmd->valuestring, "stream_unbind") == 0)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d stream_unbind return\n",__FUNCTION__,__LINE__);
						//Í¨¹ý
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						int iSeessionID = atoi(strSerialNo);
						//¸üÐÂÁ÷ÐÅÏ¢×´Ì¬
						//ÕÒµ½Á÷¶ÔÓ¦Êý¾Ý£
						if(iRetCode>=0)
						{
							this0->m_cSM_Manag->Userbehav(iSeessionID,"C","NULL","stream_unbind success",iRetCode,NULL);
						}
						else
							this0->m_cSM_Manag->Userbehav(iSeessionID,"C","NULL","stream_unbind failed",iRetCode,NULL);	
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
								//strcpy(ptmp->strBind_userID,"NULL");
								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time	
								strcpy(ptmp->strStatus_date,nowTime);
								strcpy(ptmp->strBind_userID,"");
								LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d NAV change status to unbind\n",__FUNCTION__,__LINE__);
								//ÐÞ¸Ä×ÊÔ´±íÖÐµÄ°ó¶¨ÐÅÏ¢
								DBInterfacer::GetInstance()->update_table(1,ptmp);
							}
							else
							{
								// retcode Ê§°ÜÐèÒª´¦Àí
							}					
							
							//ÐÞ¸Ä¶ÔÓ¦Êý¾Ý¿â
						}
						break;
					}
					else if(strcmp(pcmd->valuestring, "get_session") == 0)
					{
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d del_ads_stream return\n",__FUNCTION__,__LINE__);
						//Í¨¹ý
						cJSON* pISOnline = cJSON_GetObjectItem(pRoot, "isonline");
						//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
						int ISOnline = 0;
						if(pISOnline) ISOnline = atoi(pISOnline->valuestring);
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						if(pSerialNo)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int iSeessionID = atoi(strSerialNo);
						
						//¸üÐÂÁ÷ÐÅÏ¢×´Ì¬
						//ÕÒµ½Á÷¶ÔÓ¦Êý¾Ý£¬
						MapStreamStatus::iterator itFind = this0->m_pManager->m_mapStreamStatus.find(iSeessionID);
						if(itFind != this0->m_pManager->m_mapStreamStatus.end())
						{
							//ÐÞ¸ÄÄÚ´æÊý¾Ý
							StreamStatus *ptmp = itFind->second;
							if(iRetCode >= 0)
							{
								//db ÐÞ¸ÄÊý¾Ý×´Ì¬Îª¿ÕÏÐ¡£
								
								//ÐÞ¸Ä×´Ì¬
								//strcpy(ptmp->strStreamType,Status_Idle);
							}
							else
							{
								// retcode Ê§°Ü±£ÁôÊý¾ÝÔÚmapÖÐ
							}
							//ÐÞ¸Ä¶ÔÓ¦Êý¾Ý¿â
						}	
						break;
					}
					
				}
			}
			
	}
	close(accept_fd);

}


