#include "Advertisement.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"


Advertisement_Stream::Advertisement_Stream(SM_Manager *pManager)
:Stream()
{

	//对方模块的端口，IP;
	//char dstIP[128] = "192.168.100.11";//"192.168.60.249"; "192.168.30.160"
	memcpy(m_strdstIP,strAdverIP,strlen(strAdverIP)+1);
	m_idstport = iAdverPort;

	m_pManager = pManager;
/*
	//启动接收线程
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, ts_recv_Nav_thread, this);
	pthread_detach(tcp_recv_thread1);
*/
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
	if( (socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0 ) 
	{  
			 printf("create socket error: (errno:)\n)");
			 fflush(stdout);
			 return false;
	 }	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	printf("Adver server ip=%s port=%d \n",m_strdstIP,m_idstport);
	fflush(stdout);
/*
	struct sockaddr_in host_addr; 
	memset(&host_addr,0,sizeof(host_addr));  
    host_addr.sin_family = AF_INET;  
    host_addr.sin_port = htons(33335);  
*/	
	//printf("server ip=%s port=%d \n",m_strdstIP,m_idstport);
	
    if( inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0 )
	{  
        printf("inet_pton error for %s\n",m_strdstIP); 
		fflush(stdout);
        exit(0);  
	}  

	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
        printf("connect error: (errno:)\n"); 
		fflush(stdout);
        exit(0);  
    }  
	printf("Adver connect to server: \n");
	fflush(stdout);
	m_clientSocket = socket_fd;

	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Adver_thread, this);
	pthread_detach(tcp_recv_thread1);

	//CleanAdverStream("12345","1001","123456789");

	return true;
}


bool Advertisement_Stream::CleanALLAdverStream()
{
		if(!Connect_AdvServer())
		{
			printf("error can't connect to server \n");
			fflush(stdout);
			return false;
		}
		//printf("---connect Adv success\n");
		if(pRet_root)
		{
			cJSON_Delete(pRet_root);
			pRet_root = NULL;
		}
	//	printf("--begin json\n");
		pRet_root = cJSON_CreateObject();
		Requst_Json_str(2,"cmd","reset_device");
		Requst_Json_str(2,"returnCode","0");
		Requst_Json_str(2,"serialno","11111111");
		
		Send_Jsoon_str();
		return true;

	return true;
}


bool Advertisement_Stream::CleanAdverStream(char *strSeesionId,char *strTaskID,char *strSerialno)
	
{
		//启动客户端链接
	if(!Connect_AdvServer())
	{
		printf("error can't connect to server\n");
		fflush(stdout);
		return false;
	}
	//printf("---connect Adv success\n");
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
//	printf("--begin json\n");
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
		printf("error can't connect to server \n");
		fflush(stdout);
	}
	printf("---connect Adv success\n");
	fflush(stdout);
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	m_isvod = temp;
	
	printf("--begin json\n");
	fflush(stdout);
	char strAuthiName[128] = "11";
	char strAuthcode[128] = "22";
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
			fflush(stdout);
	
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
						fflush(stdout);
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
								printf("----123update one Adver \n");
								fflush(stdout);
								if(this0->m_isvod==0)
								strcpy(ptmp->strStreamType,Status_Adver);
								else if(this0->m_isvod==1)
								strcpy(ptmp->strStreamType,Status_Vod);
								else if(this0->m_isvod==2)
								{
									printf("----add one nav success\n");
									fflush(stdout);
									printf("ptmp->strBind_userID = %s\n",ptmp->strBind_userID);
									fflush(stdout);
									if(strcmp(ptmp->strBind_userID,"") != 0)
									{
										printf("this is strBind_userID = %s\n",ptmp->strBind_userID);
										fflush(stdout);
										strcpy(ptmp->strStreamType,Status_Nav_Home);
									}
									else
									{	
										printf("---this is NULL\n");
										fflush(stdout);
										strcpy(ptmp->strStreamType,Status_Nav);
									}
								}
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
								printf("--time =%s \n",nowTime);
								fflush(stdout);
								strcpy(ptmp->strStatus_date,nowTime);
								//更新DB 将StreamStatus
								
								DBInterfacer::GetInstance()->update_table(1,ptmp);
							}
							else
							{
								// retcode 失败需要处理
							}					
							
							//修改对应数据库
						}
						else if(iRetCode >= 0)
						{
							//新增数据
							StreamStatus *pTmpStream = new StreamStatus;
							memset(pTmpStream,0,sizeof(StreamStatus));

							int istreamID;
							pTmpStream->istreamID = iSeessionID;
							strcpy(pTmpStream->strStreamType,Status_Adver);
							char strSwitch_taskID[64] = {0};
							sprintf(strSwitch_taskID,"%d",iTaskID);
							strcpy(pTmpStream->strSwitch_task_id,strSwitch_taskID);

							time_t timep; 
							time (&timep); 
							struct tm* tmpTime = gmtime(&timep);
							char nowTime[128]={0};
							// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
							sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
								tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
							printf("--time =%s \n",nowTime);
							fflush(stdout);

							strcpy(pTmpStream->strStatus_date,nowTime);

				/*			if(strcmp(pTmpStream->strBind_userID,"")==0)
								strcpy(pTmpStream->strBind_userID,"NULL");
							if(strcmp(pTmpStream->strBind_date,"")==0)
								strcpy(pTmpStream->strBind_date,"NULL");
				*/			//strcpy(pTmpStream->strBind_userID," ");
							//strcpy(pTmpStream->strBind_date," ");
							printf("----add one Adver \n");
				            fflush(stdout);
							
							DBInterfacer::GetInstance()->insert_table(1,pTmpStream);
#ifndef USEMAP
							delete pTmpStream;
#else
							this0->m_pManager->m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));
#endif
							//this0->m_pManager->AddStream2GroupInfo(pTmpStream->istreamID,0);
							//db in datebase;
							
							
						}

					}
					else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
					{
						
						//通过
						cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
						//printf("parse--%s\n",pSeesid->valuestring);
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

						printf("--del_ads_stream return \n");
						fflush(stdout);
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
							printf("find data in map \n");
							fflush(stdout);
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
#endif
							if(iRetCode >= 0)
							{
								//db 修改数据状态为空闲。
								printf("update  status \n");
								fflush(stdout);
							//	DBInterfacer::GetInstance()->delete_table(1,ptmp->istreamID);
								//需要更新分 组表信息
								//this0->m_pManager->AddStream2GroupInfo(ptmp->istreamID,1);
							//	this0->m_pManager->m_mapStreamStatus.erase(itFind);
							//	delete ptmp;
							//	ptmp = NULL;
								//修改状态
								strcpy(ptmp->strStreamType,Status_Idle);
								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								// -%d-%d ,tmpTime->tm_hour,tmpTime->tm_min
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
									tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								printf("--time =%s \n",nowTime);
								fflush(stdout);

								strcpy(ptmp->strStatus_date,nowTime);

								DBInterfacer::GetInstance()->update_table(1,ptmp);

								printf("----new nav index =%d size=%d\n",iSeessionID,this0->m_pManager->m_vecNewNav.size());
								fflush(stdout);
								usleep(1000*1500);
								pthread_mutex_lock(&this0->m_pManager->m_VecNewNavlocker);	
								VectorNewNav::iterator itfindset=this0->m_pManager->m_vecNewNav.find(iSeessionID);
								if(itfindset != this0->m_pManager->m_vecNewNav.end())
								{
									
									printf("----new nav create\n");
									fflush(stdout);
									//需要补发导航流
									this0->m_pManager->AddOneNavStream(iSeessionID);
									this0->m_pManager->m_vecNewNav.erase(iSeessionID);		
								}
								pthread_mutex_unlock(&this0->m_pManager->m_VecNewNavlocker);	
							}
							else
							{
								// retcode 失败保留数据在map中
							}
							//修改对应数据库
							printf("Adver change status to over\n");
							fflush(stdout);
						}	
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						printf("--check_session return \n");
						fflush(stdout);
						//通过
						cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");
						//char strSeesionID[128] ={0};
					//	memcpy(strSeesionID,pSeesid->valuestring,strlen(pSeesid->valuestring));
						int iSeessionID;
						if(pSeesid) 
						iSeessionID = atoi(pSeesid->valuestring);
						cJSON* pRetCOde = cJSON_GetObjectItem(pRoot,"ret_code");
						int iRetCode = -1;
						if(pRetCOde) iRetCode = atoi(pRetCOde->valuestring);
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						
						if(iRetCode<0)
						{
							this0->m_pManager->CleanOneAdvStream(iSeessionID);
							usleep(1000*1000);
							this0->m_pManager->AddOneAdvStream(iSeessionID);
						}
	
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
		printf("connect error\n");
		fflush(stdout);
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

