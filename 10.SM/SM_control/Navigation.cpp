#include "Navigation.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"


Navigation_Stream::Navigation_Stream(SM_Manager *pManager)
:Stream()
{
	//m_iNavPort = 33333;

	//¶Ô·½Ä£¿éµÄ¶Ë¿Ú£¬IP;
	//char dstIP[128] = "192.168.30.160";//;//"192.168.60.249";
	memcpy(m_strdstIP,strNavIP,strlen(strNavIP)+1);
	m_idstport = iNavPort;
	m_pManager = pManager;
	//Æô¶¯¿Í»§¶ËÁ´½Ó

	//printf("---connect over\n");
/*
	//Æô¶¯½ÓÊÕÏß³Ì
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, ts_recv_Nav_thread, this);
	pthread_detach(tcp_recv_thread1);
*/
}
Navigation_Stream::~Navigation_Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	
	m_clientSocket = -1;


}

bool Navigation_Stream::Connect_NavServer()
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
	printf("NAV server ip=%s port=%d \n",m_strdstIP,m_idstport);
	
    if( inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0 )
	{  
        printf("inet_pton error for %s\n",m_strdstIP);  
        exit(0);  
	}  

	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
        printf("NAV  connect error: (errno:)\n");  
        exit(0);  
    }  
	printf("NAV connect to server: \n");
	m_clientSocket = socket_fd;

		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Nav_thread, this);
		pthread_detach(tcp_recv_thread1);

//	CleanNavStream("12345","1001","1001","1","123456789","adfadfdafd123","hello");

	return true;
}

/*
bool Navigation_Stream::Send_Jsoon_str()
{


	char cJsonBuff[1024 * 2];
	char * m_tmp;
	m_tmp = cJSON_Print(pRet_root);
	memset(cJsonBuff, 0, sizeof(cJsonBuff));
	sprintf(cJsonBuff, "%sXXEE", m_tmp);
	free(m_tmp);
	printf("-----%s \n",cJsonBuff);
	cJSON_Delete(pRet_root);
	pRet_root = NULL;

	printf("---send len=%d\n",strlen(cJsonBuff));
	send(m_clientSocket, cJsonBuff, strlen(cJsonBuff), 0);

	printf("---send over\n");
}
*/

bool Navigation_Stream::CleanNavStream(char *strSeesionId,char *strSID,char* strReSID,
							char *strAuthiName,char *strAuthcode,char *strSerialno,char *strMsg)
{
	if(!Connect_NavServer())
	{
		printf("error can't connect to server \n");
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	//printf("--begin json\n");
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","logout");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(2,"sid",strSID);
	
	Requst_Json_str(2,"authname",strAuthiName);
	Requst_Json_str(2,"authcode",strAuthcode);
	Requst_Json_str(2,"resid",strReSID);
	Requst_Json_str(2,"operid",strSeesionId);
	Requst_Json_str(2,"serialno",strSeesionId); //»Ø¸´ÖÐÃ»ÓÐseesionID
	Requst_Json_str(2,"msg",strMsg);
	
	Send_Jsoon_str();
	return true;
}
bool Navigation_Stream::CleanNavTask()
{


	return true;
}
/*
bool Navigation_Stream::Parse_Json_str(char *cBuffRecv)
{
	cJSON *pItem = NULL;
	cJSON *pcmd = NULL;
	cJSON *pSerialNo = NULL;
	cJSON *pRet_root = NULL;
	cJSON* pRoot = cJSON_Parse(cBuffRecv);
	if (pRoot)
	{
		pSerialNo = cJSON_GetObjectItem(pRoot, "serialno");
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{

			//ÅÐ¶ÏÇëÇóÀàÐ
			if (strcmp(pcmd->valuestring, "login") == 0)
			{
				//»ñÈ¡·µ»ØÂë
				cJSON* pRetCode = cJSON_GetObjectItem(pRoot, "retcode");
				char strretcode[128]={0};
				memcpy(strretcode,pRetCode->valuestring,strlen(pRetCode->valuestring));
				if(atoi(strretcode) >= 0)
				{
					//success
					
				}
				else
				{
					//failed
				}
			
			}
			else if (strcmp(pcmd->valuestring, "logout") == 0)
			{

			}
			//×´Ì¬¼à²âÔÝÊ±ÎÞ
				
		}
	}
	
	return true;
}


bool Navigation_Stream::Requst_Json_str(int iType,char* strRequstType,char* strsecRequstContxt)
{
	if(NULL == pRet_root)
	{
		printf("Json error no create \n");
		return false;
	}
	switch(iType)
	{
		case 1:
		{
			//int 
			
			cJSON_AddNumberToObject(pRet_root, strRequstType, atoi(strsecRequstContxt));
		}
		break;
		case 2:
		{
			//string
			cJSON_AddStringToObject(pRet_root, strRequstType, strsecRequstContxt);
		}
		break;
		default:
		{
			//string
			cJSON_AddStringToObject(pRet_root, strRequstType, strsecRequstContxt);
		}
		break;
	}

}
*/

void *Navigation_Stream::ts_recv_Nav_thread(void *arg)
{
	Navigation_Stream *this0 = (Navigation_Stream*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;
	int len;
	char Rcv_buf[4096];

	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		return NULL;
	} else
		printf("create socket.\n\r");

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iNavPort);


	s_addr.sin_addr.s_addr = INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) {
		perror("bind");
		return NULL;
	}else
		printf("bind address to socket.\n\r");

	if(listen(sock,10)<0)
	{
		perror("listen");
		return NULL;
	}

	int accept_fd = -1;
	struct sockaddr_in remote_addr;
	memset(&remote_addr,0,sizeof(remote_addr));
	
	//while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		char cJsonBuff[1024 * 2];
		int iRecvLen = 0;
		while(1)
		{
			    memset(Rcv_buf,0,sizeof(Rcv_buf));
    			int length = 0;
				int	i_rc = 0, i_count = 0;
				do
				{
					i_rc = recv(accept_fd, Rcv_buf + i_count, 2000 - i_count, 0);
					if (i_rc <= 0)break;//Òì³£¹Ø±Õ
					i_count += i_rc;
				} while (strstr(Rcv_buf, "XXEE") == NULL);
				iRecvLen = i_count;
				if (iRecvLen <= 0) break;

				printf("recv:%s \n",Rcv_buf);

						//½âÎö±¨ÎÄÊý¾Ý
				replace(Rcv_buf, "XXEE", "");
				
		}
	}

}

bool Navigation_Stream::StartOneStream(char *strSeesionId,char *strSID,char* strReSID,
					char *strUrl,char *strIIP,char* strIPort,char* strISIP,char* strSPort,
					char* strAreaID,char *strSerialno,char *strMsg)
{
	if(!Connect_NavServer())
	{
		printf("error can't connect to server \n");
		return false;
	}

	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	printf("--begin json\n");
	char strAuthiName[128] = "11";
	char strAuthcode[128] = "22";
	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","login");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(2,"sid",strSID);
	Requst_Json_str(2,"operid",strSeesionId);
	Requst_Json_str(2,"authname","cscs");
	Requst_Json_str(2,"authcode","123456");
	Requst_Json_str(2,"vnctype","PORTAL");
	Requst_Json_str(2,"videotype","HD");
	Requst_Json_str(2,"vendor","BLUELINK");
	Requst_Json_str(2,"rate","4096");
	Requst_Json_str(2,"url",strUrl);
	Requst_Json_str(2,"iip",strIIP);
	Requst_Json_str(2,"iport",strIPort);
	Requst_Json_str(2,"sip",strISIP);
	Requst_Json_str(2,"sport",strSPort);
	Requst_Json_str(2,"areaid",strAreaID);
	Requst_Json_str(2,"serialno",strSerialno);
	Requst_Json_str(2,"msg",strMsg);
	

	
	Send_Jsoon_str();
	return true;

}
bool Navigation_Stream::FreeOneStream()
{


	return true;
}
bool Navigation_Stream::GetTaskStatus()
{

	return true;
}



//´¦Àíµ¼º½Á÷½Ó¿Ú
void *Navigation_Stream::Parse_recv_Nav_thread(void *arg)
{
	Navigation_Stream *this0 = (Navigation_Stream*)arg;

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
	
			printf("recv:%s \n",Rcv_buf);
	
					//½âÎö±¨ÎÄÊý¾Ý
			replace(Rcv_buf, "XXEE", "");

			cJSON* pRoot = cJSON_Parse(Rcv_buf);	
			pcmd = cJSON_GetObjectItem(pRoot, "cmd");
			if (pcmd)
			{
				//ÅÐ¶ÏÇëÇóÀàÐÍ
				if (strcmp(pcmd->valuestring, "login") == 0)
				{
					//³ÐÔØrsmµÄip¶Ë¿Ú
					cJSON* pCCip = cJSON_GetObjectItem(pRoot,"cip");
					char strcip[128] ={0};
					//printf("parse--%s\n",pSerialNo->valuestring);
					if(pCCip)
					memcpy(strcip,pCCip->valuestring,strlen(pCCip->valuestring)+1);

					cJSON* pCport = cJSON_GetObjectItem(pRoot,"cport");
					char strcport[128] ={0};
					//printf("parse--%s\n",pSerialNo->valuestring);
					if(pCport)
					memcpy(strcport,pCport->valuestring,strlen(pCport->valuestring)+1);

					
					cJSON* pResID = cJSON_GetObjectItem(pRoot,"resid");
					char strResid[128] ={0};
					//printf("parse--%s\n",pSerialNo->valuestring);
					if(pResID)
					memcpy(strResid,pResID->valuestring,strlen(pResID->valuestring)+1);

					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
					char strSerialNo[128] ={0};
					printf("parse--serialNo %s\n",pSerialNo->valuestring);
					if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
					
					
					cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
					int iRetCode = -1;
					if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
					printf("parse-retcode %d\n",iRetCode);

					int iSeessionID = atoi(strSerialNo);
					printf("--recv nav streamid=%d\n",iSeessionID);
											//ÕÒµ½Á÷¶ÔÓ¦Êý¾Ý£¬
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
						//ÐÞ¸ÄÄÚ´æÊý¾Ý
						
						StreamStatus *ptmp = itFind->second;
#endif
						printf("---find nav stream  retcode=%d \n",iRetCode);
						if(iRetCode >= 0)
						{
							printf("----add one nav success\n");
							if(strcmp(ptmp->strBind_userID,"") != 0)
							{
								strcpy(ptmp->strStreamType,Status_Nav_Home);
							}
							else
								strcpy(ptmp->strStreamType,Status_Nav);
							//char strSwitch_taskID[64] = {0};
							//sprintf(strSwitch_taskID,"%d",iTaskID);
							strcpy(ptmp->strSwitch_task_id,strResid);

								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								printf("--time =%s \n",nowTime);

								strcpy(ptmp->strStatus_date,nowTime);

							DBInterfacer::GetInstance()->update_table(1,ptmp);
							//
						}
						else
						{
							// retcode Ê§°ÜÐèÒª´¦Àí
						}					
						
						//ÐÞ¸Ä¶ÔÓ¦Êý¾Ý¿â
					}
					else if(iRetCode >= 0)
					{
						//ÐÂÔöÊý¾Ý
						StreamStatus *pTmpStream = new StreamStatus;
						memset(pTmpStream,0,sizeof(StreamStatus));

						int istreamID;
						pTmpStream->istreamID = iSeessionID;
						if(strcmp(pTmpStream->strBind_userID,"") != 0)
						{
							strcpy(pTmpStream->strStreamType,Status_Nav_Home);
						}
						else
							strcpy(pTmpStream->strStreamType,Status_Nav);
						time_t timep; 
						time (&timep); 
						struct tm* tmpTime = gmtime(&timep);
						char nowTime[128]={0};
						// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
						sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
							tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
						printf("--time =%s \n",nowTime);

							strcpy(pTmpStream->strStatus_date,nowTime);
							
							strcpy(pTmpStream->strSwitch_task_id,strResid);
							
							DBInterfacer::GetInstance()->insert_table(1,pTmpStream);
								

#ifndef USEMAP
							delete pTmpStream;	
#else
						this0->m_pManager->m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));
						//db in datebase;
						//this0->m_pManager->AddStream2GroupInfo(pTmpStream->istreamID,0);
#endif
						
						
					}
					
				}
				else if(strcmp(pcmd->valuestring, "logout") == 0)
				{
						//Í¨¹ý
					//	cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");

					//	int iSeessionID = 0;
					//	if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						//printf("parse--%s\n",pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						//printf("parse--%s\n",pSerialNo->valuestring);
						if(pSerialNo)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int iSeessionID = atoi(strSerialNo);

						
						//¸üÐÂÁ÷ÐÅÏ¢×´Ì¬
						//ÕÒµ½Á÷¶ÔÓ¦Êý¾Ý£¬
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
						printf("--del_ads_stream return \n");
						if(itFind != this0->m_pManager->m_mapStreamStatus.end())
						{
							printf("find data in map \n");
							//ÐÞ¸ÄÄÚ´æÊý¾Ý
							StreamStatus *ptmp = itFind->second;
#endif							
							if(iRetCode >= 0)
							{
								//db ÐÞ¸ÄÊý¾Ý×´Ì¬Îª¿ÕÏÐ¡£
								printf("updata  status \n");
								
							//	DBInterfacer::GetInstance()->delete_table(1,ptmp->istreamID);
								//ÐèÒª¸üÐÂ·Ö ×é±íÐÅÏ¢
							//	this0->m_pManager->AddStream2GroupInfo(ptmp->istreamID,1);
							//	this0->m_pManager->m_mapStreamStatus.erase(itFind);
							//	delete ptmp;
							//	ptmp = NULL;
								//ÐÞ¸Ä×´Ì¬
								strcpy(ptmp->strStreamType,Status_Idle);

								DBInterfacer::GetInstance()->update_table(1,ptmp);
								
							}
							else
							{
								// retcode Ê§°Ü±£ÁôÊý¾ÝÔÚmapÖÐ
							}
							//ÐÞ¸Ä¶ÔÓ¦Êý¾Ý¿â
							printf("NAV change status to over\n");
						}
						else
						{
							
						}
				}
			}
			else
			{
			
			}
			
	}

	close(accept_fd);
	//if(this0->m_clientSocket != -1)
	//	close(this0->m_clientSocket);
	//this0->m_clientSocket = -1;
}


