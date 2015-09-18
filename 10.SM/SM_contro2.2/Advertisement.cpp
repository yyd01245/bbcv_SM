#include "Advertisement.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"
#include <sys/time.h>


Advertisement_Stream::Advertisement_Stream(SM_Manager *pManager)
:Stream()
{

	//对方模块的端口，IP;
	
	if(advflag == 0)
	{
		memcpy(m_strdstIP,strAdverIP,strlen(strAdverIP)+1);
		m_idstport = iAdverPort;
		printf("to use the ordinary switch\n");
		fflush(stdout);
	}
	else
	{
		memcpy(m_strdstIP,strblanIP,strlen(strblanIP)+1);
		m_idstport = iBlanport;
		printf("to use the blance switch\n");
		fflush(stdout);
	}
	m_pManager = pManager;
	connsocket.clear();
	
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
	if((socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0) 
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
   //将建立连接的套接字翻入队列尾端
	connsocket.push_back(socket_fd);
	printf("adv socket fd = %d\n",socket_fd);
	fflush(stdout);
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Adver_thread, this);
	usleep(100);
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
	if((temp == 0)||(temp == 3))
	{
		m_isvod.push_back(temp);
		printf("to start a advstream %d\n",temp);
		fflush(stdout);
	}
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
	int accept_fd = -1;
//	int accept_fd = this0->m_clientSocket;
	Sockfd::iterator itaccept = this0->connsocket.begin();
	if(itaccept != this0->connsocket.end())
	{
		accept_fd = *itaccept;
		this0->connsocket.erase(itaccept);
		printf("---the accept fd = %d\n",accept_fd);
		fflush(stdout);
	}
	
	while(1)
	{
			memset(Rcv_buf,0,sizeof(Rcv_buf));
			int length = 0;
			int i_rc = 0, i_count = 0;
			do
			{
				i_rc = recv(accept_fd, Rcv_buf + i_count, 2000 - i_count, 0);
				printf("recv accept_fd = %d, len = %d\n",accept_fd,i_rc);
				fflush(stdout);
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
						int iflag = 1;
						if(iRetCode < 0)
						{
							Streatype::iterator strList = this0->m_isvod.begin();
							if(strList != this0->m_isvod.end())
							{
								printf("---add adv error\n");
								fflush(stdout);
								this0->m_isvod.erase(strList);
							}
						}			
						else if(iRetCode >= 0)
						{
							printf("----123update one Adver \n");
							fflush(stdout);
							Streatype::iterator strList = this0->m_isvod.begin();
						    if(strList != this0->m_isvod.end())
							{
								iflag = *strList;
								printf("----iflag = %d\n",iflag);
								this0->m_isvod.erase(strList);
							 }
							 else
							 {
								printf("this is not advstream\n");
								fflush(stdout);
							 }
						}	
				      	if((iflag == 0)||(iflag == 3))
					  	{
						
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
								printf("1111----time =%s ,streamid = %d\n",nowTime,ptmp->istreamID);
								fflush(stdout);
								strcpy(ptmp->strStatus_date,nowTime);
										
								//更新DB 将StreamStatus
									
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
								strcpy(pTmpStream->strStreamType,Status_Adver);
								char strSwitch_taskID[64] = {0};
								sprintf(strSwitch_taskID,"%d",iTaskID);
								strcpy(pTmpStream->strSwitch_task_id,strSwitch_taskID);

								time_t timep; 
								time (&timep); 
								struct tm* tmpTime = gmtime(&timep);
								char nowTime[128]={0};
								sprintf(nowTime,"%d-%d-%d-%d:%d:%d",tmpTime->tm_year+1900,tmpTime->tm_mon+1,tmpTime->tm_mday,
										tmpTime->tm_hour,tmpTime->tm_min,tmpTime->tm_sec); //bind time
								printf("--time =%s \n",nowTime);
								fflush(stdout);

								strcpy(pTmpStream->strStatus_date,nowTime);

								printf("----add one Adver \n");
					            fflush(stdout);
								
								DBInterfacer::GetInstance()->insert_table(1,pTmpStream);
#ifndef USEMAP
								delete pTmpStream;
#else
								this0->m_pManager->m_mapStreamStatus.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));
#endif
						 }
					   }	
					   break;
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
								printf("--time = %s \n",nowTime);
								fflush(stdout);

								strcpy(ptmp->strStatus_date,nowTime);

								DBInterfacer::GetInstance()->update_table(1,ptmp);

								printf("----new nav index =%d size=%d\n",iSeessionID,this0->m_pManager->m_vecNewNav.size());
								fflush(stdout);
								/*
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
								*/
							
							}
							else
							{
								// retcode 失败保留数据在map中
							}
							//修改对应数据库
							printf("Adver change status to over\n");
							fflush(stdout);
						}
					 break;
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						printf("--check_session return\n");
						fflush(stdout);
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

						printf("---check stream %d over\n",iSeessionID);
						fflush(stdout);
						if(iRetCode<0)
						{
							printf("stream id = %d is error\n",iSeessionID);
							fflush(stdout);
							this0->m_pManager->Dealsession(iSeessionID);
						}
						break;
					}
					
					else if(strcmp(pcmd->valuestring, "req_ads_stream") == 0)
					{
						struct timeval tv1;
						long long time1;
						gettimeofday(&tv1, NULL);
						time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
						printf("******* 123 Send time =%ld *****\n",time1);
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
							printf("this stream id is %d\n",iSeessionID);
							fflush(stdout);
							memcpy(pmt->sadvip,strip,strlen(strip)+1);
							memcpy(pmt->sadvport,strport,strlen(strport)+1);
							this0->m_pManager->m_smstraddr.push_back(pmt);
						}
						printf("---get stream %d ip = %s,port = %s\n",iSeessionID,strip,strport);
						fflush(stdout);
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

bool Advertisement_Stream::Getaddr(char* strSeesionId,char *strSerialno)
{
	if(!Connect_AdvServer())
	{
		printf("connect error\n");
		fflush(stdout);
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
	printf("******* Send time =%ld *****\n",time1);
	
	Send_Jsoon_str();
}

