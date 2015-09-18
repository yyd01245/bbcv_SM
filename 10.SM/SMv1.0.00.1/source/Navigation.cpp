#include "Navigation.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"

Navigation_Stream::Navigation_Stream(SM_Manager *pManager)
:Stream()
{
	//m_iNavPort = 33333;

	//对方模块的端口，IP;
	//char dstIP[128] = "192.168.30.160";//;//"192.168.60.249";
	m_whereHD.clear();
	m_pManager = pManager;
}
Navigation_Stream::~Navigation_Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	m_clientSocket = -1;
}

bool Navigation_Stream::Connect_NavServer(int streamid,int mtemp)
{
	int socket_fd;	
	int temp;
	struct sockaddr_in server_addr; 
	
	SetUsedRegionID::iterator iterStream = m_whereHD.find(streamid);
	if(iterStream != m_whereHD.end())
	{
		temp = iterStream->second;
		if(!temp)
		{
			memcpy(m_strdstIP,strNavIP,strlen(strNavIP)+1);
			m_idstport = iNavPort;
		}
		else
		{
			memcpy(m_strdstIP,strsdNavIP,strlen(strsdNavIP)+1);
			m_idstport = isdNavPort;
		}
	}	
	else
	{
		if(1==mtemp)
		{
			memcpy(m_strdstIP,strNavIP,strlen(strNavIP)+1);
			m_idstport = iNavPort;
		}
		else if(2==mtemp)
		{
			memcpy(m_strdstIP,strsdNavIP,strlen(strsdNavIP)+1);
			m_idstport = isdNavPort;
		}
	}
	if((socket_fd = socket(AF_INET,SOCK_STREAM,0))<0) 
	{  
		 LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		 return false;
	}
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d NAV server ip=%s port=%d\n",__FUNCTION__,__LINE__,m_strdstIP,m_idstport);
    if(inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0)
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d inet_pton error for %s\n",__FUNCTION__,__LINE__,m_strdstIP);
        exit(0);  
	}  

	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d NAV connect error\n",__FUNCTION__,__LINE__);
        exit(0);  
    }  
	m_clientSocket = socket_fd;

	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Nav_thread, this);
	pthread_detach(tcp_recv_thread1);
	return true;
}

bool Navigation_Stream::CleanNavStream(char *strSeesionId,char *strSID,char* strReSID,
							char *strAuthiName,char *strAuthcode,char *strSerialno,char *strMsg)
{
	int streamid = atoi(strSeesionId);
	int i=0;
	SetUsedRegionID::iterator iterStream = m_whereHD.find(streamid);
	if(iterStream == m_whereHD.end())
	{
		for(i=0;i<2;i++)
		{
			if(!Connect_NavServer(streamid,i+1))
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
			Requst_Json_str(2,"sid",strSID);
	
			Requst_Json_str(2,"authname",strAuthiName);
			Requst_Json_str(2,"authcode",strAuthcode);
			Requst_Json_str(2,"resid",strReSID);
			Requst_Json_str(2,"operid",strSeesionId);
			Requst_Json_str(2,"serialno",strSeesionId); //回复中没有seesionID
			Requst_Json_str(2,"msg",strMsg);
	
			Send_Jsoon_str();
		}	
		return true;
	}
	
	if(!Connect_NavServer(streamid))
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
	Requst_Json_str(2,"cmd","logout");
	Requst_Json_str(2,"sessionid",strSeesionId);
	Requst_Json_str(2,"sid",strSID);
	
	Requst_Json_str(2,"authname",strAuthiName);
	Requst_Json_str(2,"authcode",strAuthcode);
	Requst_Json_str(2,"resid",strReSID);
	Requst_Json_str(2,"operid",strSeesionId);
	Requst_Json_str(2,"serialno",strSeesionId); //回复中没有seesionID
	Requst_Json_str(2,"msg",strMsg);
	
	Send_Jsoon_str();
	return true;
}
bool Navigation_Stream::CleanNavTask()
{

	return true;
}

void *Navigation_Stream::ts_recv_Nav_thread(void *arg)
{
	Navigation_Stream *this0 = (Navigation_Stream*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;
	int len;
	char Rcv_buf[4096];

	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) 
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create sockoret error\n",__FUNCTION__,__LINE__);
		return NULL;
	} 
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

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) 
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d bind error\n",__FUNCTION__,__LINE__);
		return NULL;
	}
	if(listen(sock,10)<0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d listen error\n",__FUNCTION__,__LINE__);
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
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Accept error\n",__FUNCTION__,__LINE__);
                //continue;  
	     }  
       
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d received a connection from %s\n",__FUNCTION__,__LINE__,(char*) inet_ntoa(remote_addr.sin_addr));
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
					if (i_rc <= 0)break;//异常关闭
					i_count += i_rc;
				} while (strstr(Rcv_buf, "XXEE") == NULL);
				iRecvLen = i_count;
				if (iRecvLen <= 0) break;

				LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
						//解析报文数据
				replace(Rcv_buf, "XXEE", "");	
		}
	}

}

bool Navigation_Stream::Utfurl(char *pin,char *pout)
{
	size_t inlen = strlen(pin)+1;
	size_t outlen = 1024;
	size_t retsize=0;
	char *pp = pin;
    char *poo = pout;

    iconv_t cd;
    cd = iconv_open("UTF-8","GB2312");
    if((iconv_t)-1 == cd) 
	{
          perror("iconv_open error");
		  fflush(stdout);
          return -1;
    }
    retsize = iconv(cd,&pp,&inlen,&poo,&outlen);
    if((size_t)-1 == retsize) {
            perror("iconv error");
			fflush(stdout);
            return -2;
    }
	
     if(outlen > 0) {
           ;
     }
 
     iconv_close(cd);
     return 0;
}
bool Navigation_Stream::StartOneStream(char *strSeesionId,char *strSID,char* strReSID,
					char *strUrl,char *strIIP,char* strIPort,char* strISIP,char* strSPort,
					char* strAreaID,char *strSerialno,char *strMsg,int temp)
{

	
	int streamid = atoi(strSeesionId);
	SetUsedRegionID::iterator iterStream = m_whereHD.find(streamid);
	if(iterStream == m_whereHD.end())
	{
		m_whereHD.insert(SetUsedRegionID::value_type(streamid,temp));
	}
	if(!Connect_NavServer(streamid))
	{	
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		return false;
	}
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
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
	
	if(!temp)
	Requst_Json_str(2,"rate",hdrate);
	else
	Requst_Json_str(2,"rate",sdrate);	
	
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
bool Navigation_Stream::GetTaskStatus(int isstreamid,int temp)
{
	
	return true;
}

//处理导航流接口
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
			if (i_rc <= 0)break;//异常关闭
			i_count += i_rc;
		} while (strstr(Rcv_buf, "XXEE") == NULL);
		iRecvLen = i_count;
		if (iRecvLen <= 0) break;
	
		LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
				//解析报文数据
		replace(Rcv_buf, "XXEE", "");

		cJSON* pRoot = cJSON_Parse(Rcv_buf);	
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{
			//判断请求类型
			if (strcmp(pcmd->valuestring, "login") == 0)
			{
				//承载rsm的ip端口
				cJSON* pCCip = cJSON_GetObjectItem(pRoot,"cip");
				char strcip[128] ={0};
				if(pCCip)
				memcpy(strcip,pCCip->valuestring,strlen(pCCip->valuestring)+1);

				cJSON* pCport = cJSON_GetObjectItem(pRoot,"cport");
				char strcport[128] ={0};
				if(pCport)
				memcpy(strcport,pCport->valuestring,strlen(pCport->valuestring)+1);
	
				cJSON* pResID = cJSON_GetObjectItem(pRoot,"resid");
				char strResid[128] ={0};
				if(pResID)
				memcpy(strResid,pResID->valuestring,strlen(pResID->valuestring)+1);

				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d parse--serialNo %s\n",__FUNCTION__,__LINE__,pSerialNo->valuestring);
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
					
				cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
				int iRetCode = -1;
				if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d parse-retcode %d\n",__FUNCTION__,__LINE__,iRetCode);
				int iSeessionID = atoi(strSerialNo);
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d recv nav streamid=%d\n",__FUNCTION__,__LINE__,iSeessionID);
				MapCsaddr::iterator itCsaddr = this0->m_pManager->m_mapCsaddr.find(iSeessionID);
				if(itCsaddr != this0->m_pManager->m_mapCsaddr.end())
				{
					Csaddr *p = itCsaddr->second;
					memcpy(p->csip,strcip,strlen(strcip)+1);
					memcpy(p->csport,strcport,strlen(strcport)+1);
				}
				else
				{
					Csaddr *p = new Csaddr;
					memcpy(p->csip,strcip,strlen(strcip)+1);
					memcpy(p->csport,strcport,strlen(strcport)+1);
					this0->m_pManager->m_mapCsaddr.insert(MapCsaddr::value_type(iSeessionID,p));
				}
				
					
					//找到流对应数据，
				char strkey_value[64] = {0};
				sprintf(strkey_value,"%d",iSeessionID);
				StreamStatus pTmpResource;
				memset(&pTmpResource,0,sizeof(pTmpResource));
				int iret = DBInterfacer::GetInstance()->FindOneStream(1,"istreamID",strkey_value,&pTmpResource);
				if(iret)
				{
					StreamStatus* ptmp = &pTmpResource;
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d find nav stream  retcode=%d\n",__FUNCTION__,__LINE__,iRetCode);
				
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d add one nav success\n",__FUNCTION__,__LINE__);
					if(strcmp(ptmp->strBind_userID,"") != 0)
					{
						strcpy(ptmp->strStreamType,Status_Nav_Home);
					}
					else
						strcpy(ptmp->strStreamType,Status_Nav);
							
					strcpy(ptmp->strSwitch_task_id,strResid);

					time_t timep; 
					time (&timep); 
					struct tm* tmpTime = gmtime(&timep);
					char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
					sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
							tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								//LOG_DEBUG_FORMAT("%s %d time =%s\n",__FUNCTION__,__LINE__,nowTime);
					strcpy(ptmp->strStatus_date,nowTime);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d ptmp->strStreamType = %s\n",__FUNCTION__,__LINE__,ptmp->strStreamType);
					DBInterfacer::GetInstance()->update_table(1,ptmp);
						//修改对应数据库
				}
				else if(iRetCode >= 0)
				{
						//新增数据
					StreamStatus *pTmpStream = new StreamStatus;
					memset(pTmpStream,0,sizeof(StreamStatus));

					int istreamID;
					pTmpStream->istreamID = iSeessionID;
					if(strcmp(pTmpStream->strBind_userID,"") != 0)
					{
						strcpy(pTmpStream->strStreamType,Status_Nav_Home);
					}
					else
					{
						strcpy(pTmpStream->strStreamType,Status_Nav);
						LOG_DEBUG_FORMAT("DEBUG - [SM]:%s %d this is Nav Status_Nav\n",__FUNCTION__,__LINE__);
					}
					time_t timep; 
					time (&timep); 
					struct tm* tmpTime = gmtime(&timep);
					char nowTime[128]={0};
						// %d-%d ,tmpTime->tm_hour,tmpTime->tm_min
					sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
						tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
						// LOG_DEBUG_FORMAT("%s %d time =%s\n",__FUNCTION__,__LINE__,nowTime);
					strcpy(pTmpStream->strStatus_date,nowTime);
							
					strcpy(pTmpStream->strSwitch_task_id,strResid);
					DBInterfacer::GetInstance()->insert_table(1,pTmpStream);	
						
					delete pTmpStream;			
				}
				break;
			}
			else if(strcmp(pcmd->valuestring,"logout")==0)
			{
						//通过
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
			}
			break;
		}
		else if(strcmp(pcmd->valuestring,"vncexist"))
		{
			cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"retcode");
			int iRetCode = -1;
			if(pRetCOde)
			iRetCode = atoi(pRetCOde->valuestring);
			if(iRetCode<0)
			{
				sleep(1000*1500);
			}
			break;
		}
			
	}
	close(accept_fd);
}


