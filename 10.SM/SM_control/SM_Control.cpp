#include "SM_Control.h"

#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <unistd.h>
#include <time.h>
#include "CommonFun.h"
#include "cJSON.h"
#include "Stream.h"








SM_Control::SM_Control()
{
	m_iNavPort = 33333;
	m_iAdverPort = 33335;
	m_iVodPort = 33337;
	m_iMsiPort = iMSIServerPort;

	m_cSM_Manager = NULL;
	//启动接收线程
//	pthread_t tcp_recv_thread1;
//	pthread_create(&tcp_recv_thread1, NULL, ts_recv_Nav_thread, this);
//	pthread_detach(tcp_recv_thread1);

	//启动接收线程
/*	pthread_t tcp_recv_thread2;
	pthread_create(&tcp_recv_thread2, NULL, ts_recv_Adver_thread, this);
	pthread_detach(tcp_recv_thread2);
*/
	//启动接收线程
//	pthread_t tcp_recv_thread3;
//	pthread_create(&tcp_recv_thread3, NULL, ts_recv_Vod_thread, this);
//	pthread_detach(tcp_recv_thread3);

	//启动接收线程
	pthread_t tcp_recv_thread4;
	pthread_create(&tcp_recv_thread4, NULL, ts_recv_MSI_thread, this);
	pthread_detach(tcp_recv_thread4);

	m_cSM_Manager = new SM_Manager;
	
	//m_cSM_Manager->m_Advertiser->StartOneStream("1","udp://225.0.0.1:12000","udp://192.168.100.106:50442","e123214");
//	m_cSM_Manager->m_Navigation->StartOneStream("12321","1001","1001","udp://192.111.1l.1:10000","192.168.100.106","1000","120.1.1.11",
//						"11111","1","3dfaf12132213","12312qw1qw");

	//装载数据
//	m_cSM_Manager->LoadStreamStatus();
	m_cSM_Manager->InitStream();
	
	//清理
//	m_cSM_Manager->CleanAllStream();

//载入分组
//分组规则以网络区域编号相同为一组
//首先查找网络区域编号，对应再查找该编号下面的流ID 。
//将编号存放在set表中，再从set表中每个编号作为map first Value 导入到对应的数据到map中。


}
SM_Control::~SM_Control()
{
	if(m_cSM_Manager)
		delete m_cSM_Manager;
	m_cSM_Manager = NULL;

}

//处理导航流接口
void *SM_Control::Parse_recv_Nav_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_NavacceptSocket;
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
			pcmd = cJSON_GetObjectItem(pRoot, "cmd");
			if (pcmd)
			{
				//判断请求类型
				if (strcmp(pcmd->valuestring, "login") == 0)
				{
					
				}
				else if(strcmp(pcmd->valuestring, "logout") == 0)
				{
					
				}
			}
			else
			{
			
			}
			
	}

	if(this0->m_NavacceptSocket != -1)
		close(this0->m_NavacceptSocket);
	this0->m_NavacceptSocket = -1;
}


//接收数据端口
void *SM_Control::ts_recv_Nav_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


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

	this0->m_NavacceptSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		if(this0->m_NavacceptSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_NavacceptSocket = accept_fd;
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Nav_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}

	//接收接口回复的数据
	

}

//处理广告流接口
void *SM_Control::Parse_recv_Adver_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_AdvacceptSocket;
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
					if (strcmp(pcmd->valuestring, "add_ads_stream") == 0)
					{
						printf("--add_ads_stream return \n");
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
						MapStreamStatus::iterator itFind = this0->m_cSM_Manager->m_mapStreamStatus.find(iSeessionID);
						if(itFind != this0->m_cSM_Manager->m_mapStreamStatus.end())
						{
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
							if(iRetCode >= 0)
							{
								strcpy(ptmp->strStreamType,Status_Adver);
								if(strcmp(ptmp->strBind_userID,"") == 0)
								{
									//未绑定									
								}
								else
								{
									
								}

							}
							else
							{
								// retcode 失败需要处理
							}					
							
							//修改对应数据库
						}
						
					}
					else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
					{
						printf("--del_ads_stream return \n");
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
						
						//更新流信息状态
						//找到流对应数据，
						MapStreamStatus::iterator itFind = this0->m_cSM_Manager->m_mapStreamStatus.find(iSeessionID);
						if(itFind != this0->m_cSM_Manager->m_mapStreamStatus.end())
						{
							//修改内存数据
							StreamStatus *ptmp = itFind->second;
							if(iRetCode >= 0)
							{
								strcpy(ptmp->strStreamType,Status_Adver);
								if(strcmp(ptmp->strBind_userID,"") == 0)
								{
									//未绑定									
								}
								else
								{
									
								}
							}
							else
							{
								// retcode 失败需要处理
							}
							//修改对应数据库
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
	
					}
					
				}
			}
			
	}

	if(this0->m_AdvacceptSocket != -1)
		close(this0->m_AdvacceptSocket);
	this0->m_AdvacceptSocket = -1;
}

void *SM_Control::ts_recv_Adver_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		return NULL;
	} else
		printf("create socket.\n\r");

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iAdverPort);


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

	this0->m_AdvacceptSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		if(this0->m_AdvacceptSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_AdvacceptSocket = accept_fd;
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Adver_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}

}

//处理VOD接口
void *SM_Control::Parse_recv_Vod_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_VodacceptSocket;
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
	/*				if (strcmp(pcmd->valuestring, "add_ads_stream") == 0)
					{
						printf("--add_ads_stream return \n");
					}
					else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
					{
						printf("--del_ads_stream return \n");
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						printf("--check_session return \n");
					}
	*/				
				}
			}
			
	}

	if(this0->m_VodacceptSocket != -1)
		close(this0->m_VodacceptSocket);
	this0->m_VodacceptSocket = -1;
}

void *SM_Control::ts_recv_Vod_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		return NULL;
	} else
		printf("create socket.\n\r");

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iVodPort);


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

	this0->m_VodacceptSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		if(this0->m_VodacceptSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_VodacceptSocket = accept_fd;
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_Vod_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}


}


//处理MSI接口
void *SM_Control::Parse_recv_MSI_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_MsiacceptSocket;
	while(1)
	{
			if(accept_fd == -1)
			{
				printf("error accept socket in msi\n");
				break;
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
					if (strcmp(pcmd->valuestring, "stream_bind") == 0)
					{
						printf("--bind_stream request \n");
						//通过
						cJSON* pUserName = cJSON_GetObjectItem(pRoot, "username");
						char strUserName[128] ={0};
						if(pUserName)
							memcpy(strUserName,pUserName->valuestring,strlen(pUserName->valuestring)+1);
						cJSON* pToken = cJSON_GetObjectItem(pRoot, "token");
						char strToken[128] ={0};
						if(pToken) memcpy(strToken,pToken->valuestring,strlen(pToken->valuestring)+1);
						cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
						int iStreamID = -1;
						if(pStreamID) iStreamID = atoi(pStreamID->valuestring);

						cJSON* pRecallAddr = cJSON_GetObjectItem(pRoot, "recall_addr");
						char strRecallAddr[128] ={0};
						if(pRecallAddr) memcpy(strRecallAddr,pRecallAddr->valuestring,strlen(pRecallAddr->valuestring)+1);

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						printf("istreamid=%d usename=%s token=%s \n",iStreamID,strUserName,strToken);
					//	printf("---request parse \n");
						int iChannelInfo=0;
						//调manager 中绑定
						int ret = this0->m_cSM_Manager->Bind_OneStream(iStreamID,strUserName,strToken,&iChannelInfo);
						
						
						//回复
						/*
							{"cmd":"stream_bind","ret_code":"0","serialno":"c0e1758d697841fa8dad428c23b867a7"}XXEE
						*/
						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","stream_bind");
					
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						char strChannelInfo[32]={0};
						sprintf(strChannelInfo,"%d",iChannelInfo);
						ptmpRequest.Requst_Json_str(2,"ChannelInfo",strChannelInfo);
						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();

						//补发一路新导航流
						if(ret >= 0)
							this0->m_cSM_Manager->AddOneStream(Navigation,iStreamID);
						

						
						
					}
					else if(strcmp(pcmd->valuestring, "vod_play") == 0)
					{
						printf("--Vod_call_stream request \n");
						//通过
						cJSON* pUserName = cJSON_GetObjectItem(pRoot, "username");
						char strUserName[128] ={0};
						if(pUserName)
							memcpy(strUserName,pUserName->valuestring,strlen(pUserName->valuestring)+1);
						cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
						int iStreamID = -1;
						if(pStreamID) iStreamID = atoi(pStreamID->valuestring);

						cJSON* pUrlAddr = cJSON_GetObjectItem(pRoot, "url");
						char strUrlAddr[128] ={0};
						if(pUrlAddr) memcpy(strUrlAddr,pUrlAddr->valuestring,strlen(pUrlAddr->valuestring)+1);

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int ret=0;
						printf("======msi connect fd=%d \n",accept_fd);
						//调manager 中点播
						 ret = this0->m_cSM_Manager->AddOneVodStream(iStreamID,strUrlAddr,accept_fd,strSerialNo);//需要参数
						//int ret = this0->m_cSM_Manager->Bind_OneStream(iStreamID,strUserName,strToken);

						//等待vod回复码
						
						//报文回复
		/*				Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","vod_play");
					
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);
						ptmpRequest.Requst_Json_str(2,"key_addr",strVOD_KeyIP); //vod 地址
						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();
			*/		/*	cJSON *pRet_root;
						pRet_root = cJSON_CreateObject();
						cJSON_AddStringToObject(pRet_root,"cmd","vod_play");
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						cJSON_AddStringToObject(pRet_root,"ret_code",txt);
						cJSON_AddStringToObject(pRet_root,"key_addr",strVOD_KeyIP);
						cJSON_AddStringToObject(pRet_root,"serialno",strSerialNo);
					*/
						//send
						
					}
					else if (strcmp(pcmd->valuestring, "goback") == 0)
					{
						printf("--bind_stream request \n");
						//通过
						cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");

						int iSeessionID = 0;
						if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
	

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						//printf("parse--%s\n",pSerialNo->valuestring);
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						cJSON* pMsg = cJSON_GetObjectItem(pRoot,"msg");
						char strMsg[128] ={0};
						//printf("parse--%s\n",pSerialNo->valuestring);
						if(pMsg)
							memcpy(strMsg,pMsg->valuestring,strlen(pMsg->valuestring)+1);

						//iSeessionID = atoi(strSerialNo);

						//vod结束退出处理
						this0->m_cSM_Manager->VodStreamOver(iSeessionID);
						usleep(1000*8000);

						pthread_mutex_lock(&this0->m_cSM_Manager->m_lockerRegion);
						SetUsedRegionID::iterator itused = this0->m_cSM_Manager->m_SetUsedRegionID.find(iSeessionID);
						if(itused != this0->m_cSM_Manager->m_SetUsedRegionID.end())
						{
							//有正在使用的等会释放
							
							char txt[128]={0};
									sprintf(txt,"%d",itused->second);
									printf("-----erase region %s success \n",txt);
							this0->m_cSM_Manager->m_SetUsedRegionID.erase(itused);
						}
							pthread_mutex_unlock(&this0->m_cSM_Manager->m_lockerRegion);
						
					}

			
				}
			}
			
	}

		close(accept_fd);

}

void *SM_Control::ts_recv_MSI_thread(void *arg)
{

	SM_Control *this0 = (SM_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		return NULL;
	} else
		printf("create socket.\n\r");

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iMsiPort);


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

	this0->m_MsiacceptSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             	printf( "Accept error!\n");  
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		if(this0->m_MsiacceptSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_MsiacceptSocket = accept_fd;
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_MSI_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}


}

