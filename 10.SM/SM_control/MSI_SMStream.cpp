#include "MSI_SMStream.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"


MSI_SM_Stream::MSI_SM_Stream(SM_Manager *pManager)
:Stream()
{

	//对方模块的端口，IP;
	//char dstIP[128] = "192.168.30.160";//"192.168.30.160";//"192.168.60.249";
	memcpy(m_strdstIP,strMsiIP,strlen(strMsiIP)+1);
	m_idstport = iMsiPort;
	m_pManager = pManager;
	//启动客户端链接
	//if(!Connect_MSIServer())
	{
//		printf("error can't connect to server \n");
	
	}
/*
	//启动接收线程
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, ts_recv_Nav_thread, this);
	pthread_detach(tcp_recv_thread1);
*/
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
			 printf("create socket error: (errno:)\n)");	
			 return false;
	 }	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	printf("MSI server ip=%s port=%d \n",m_strdstIP,m_idstport);
	
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
	printf("MSI connect to server: \n");
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
	Requst_Json_str(2,"cmd","stream_unbind");
	Requst_Json_str(2,"username",strUserId);
	Requst_Json_str(2,"serialno",strSerialno);
	
	Send_Jsoon_str();
	return true;
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
					if (strcmp(pcmd->valuestring, "stream_unbind") == 0)
					{
						printf("--stream_unbind return \n");
						//通过
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
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
								
								//strcpy(ptmp->strBind_userID,"NULL");
								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								printf("--time =%s \n",nowTime);


									//从绑定状态移除
							/*		pthread_mutex_lock(&this0->m_pManager->m_BindStatuslocker);
									printf("lock in msi bind status \n");
									MapBindOverTime::iterator itbind = this0->m_pManager->m_mapBindStatus.find(iSeessionID);
									if(itbind != this0->m_pManager->m_mapBindStatus.end())
									{
										//移除
										this0->m_pManager->m_mapBindStatus.erase(itbind);
									}
									pthread_mutex_unlock(&this0->m_pManager->m_BindStatuslocker);
									printf("unlock in msi bind status \n");
							*/		

								strcpy(ptmp->strStatus_date,nowTime);
								strcpy(ptmp->strBind_userID,"");
								printf("NAV change status to unbind\n");

								//修改资源表中的绑定信息
								DBInterfacer::GetInstance()->update_table(1,ptmp);
								
							}
							else
							{
								// retcode 失败需要处理
							}					
							
							//修改对应数据库
						}

					}
					else if(strcmp(pcmd->valuestring, "get_session") == 0)
					{
						printf("--del_ads_stream return \n");
						//通过
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
						
						//更新流信息状态
						//找到流对应数据，
						MapStreamStatus::iterator itFind = this0->m_pManager->m_mapStreamStatus.find(iSeessionID);
						if(itFind != this0->m_pManager->m_mapStreamStatus.end())
						{
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
							if(iRetCode >= 0)
							{
								//db 修改数据状态为空闲。
								
								//修改状态
								//strcpy(ptmp->strStreamType,Status_Idle);
							}
							else
							{
								// retcode 失败保留数据在map中
							}
							//修改对应数据库
						}	
					
					}
					
				}
			}
			
	}
	close(accept_fd);

}


