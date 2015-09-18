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
	//m_iNavPort = 33333;
	//m_iAdverPort = 33335;
	//m_iVodPort = 33337;
	m_iCleanPort = 30910;
	m_iUseractport = 20915;
	m_iMsiPort = iMSIServerPort;
	m_iContrlport = iMContrlport;
	m_cSM_Manager = NULL;

	//启动接收线程

	//接收指令线程
	pthread_t tcp_recv_thread4;
	pthread_create(&tcp_recv_thread4, NULL, ts_recv_MSI_thread, this);
	pthread_detach(tcp_recv_thread4);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d  create ts_recv_MSI_thread OK \n",__FUNCTION__,__LINE__);
	
	
	//启动清理线程
	pthread_t tcp_recv_thread5;
	pthread_create(&tcp_recv_thread5, NULL, ts_recv_Clear_thread, this);
	pthread_detach(tcp_recv_thread5);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d  create ts_recv_Clear_thread OK \n",__FUNCTION__,__LINE__);

	//启动用户行为采集线程
	pthread_t tcp_recv_thread6;
	pthread_create(&tcp_recv_thread6, NULL, ts_recv_useract_thread, this);
	pthread_detach(tcp_recv_thread6);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d  create ts_recv_useract_thread OK \n",__FUNCTION__,__LINE__);
	
	//启动接收管理端消息线程
	pthread_t tcp_recv_thread8;
	pthread_create(&tcp_recv_thread8, NULL, ts_recv_Contrl_thread, this);
	pthread_detach(tcp_recv_thread8);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d  create ts_recv_Contrl_thread OK \n",__FUNCTION__,__LINE__);

	//启动流状态监测进程
		
	pthread_t tcp_recv_thread7;
	pthread_create(&tcp_recv_thread7, NULL, ts_checksession_thread,this);
	pthread_detach(tcp_recv_thread7);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d  create ts_checksession_thread OK \n",__FUNCTION__,__LINE__);
	
	m_cSM_Manager = new SM_Manager;

//装载数据
	m_cSM_Manager->InitStream();

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


//处理MSI接口
void *SM_Control::Parse_recv_MSI_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	int len;
	char Rcv_buf[4096];
	cJSON *pcmd = NULL;
	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = 0;
	Sockfd::iterator iterList = this0->m_MsiacceptSocket.begin();
	if(iterList != this0->m_MsiacceptSocket.end())
	{
		accept_fd = *iterList;
		this0->m_MsiacceptSocket.erase(iterList);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d accept_fd = \n",__FUNCTION__,__LINE__,accept_fd);
	}
	while(1)
	{
		if(accept_fd == -1)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error accept socket in msi\n",__FUNCTION__,__LINE__);
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
		LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
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
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d bind_stream request\n",__FUNCTION__,__LINE__);
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
		
					this0->m_cSM_Manager->Bindagain(strUserName,iStreamID);					

					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d istreamid=%d usename=%s token=%s\n",__FUNCTION__,__LINE__,iStreamID,strUserName,strToken);	
					int iChannelInfo=0;
					//调manager 中绑定
					int ret = this0->m_cSM_Manager->Bind_OneStream(iStreamID,strUserName,strToken,&iChannelInfo);
						
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
					char csip[128] = {0};
					char csport[64] = {0};
					this0->m_cSM_Manager->Findcsaddr(csip,csport,iStreamID);
				
					ptmpRequest.Requst_Json_str(2,"strNavIP",csip);
					ptmpRequest.Requst_Json_str(2,"iNavPort",csport);
									
					ptmpRequest.Send_Jsoon_str();
					if(ret>=0)
					{
						this0->m_cSM_Manager->Userbehav(iStreamID,"B",strUserName,"stream_bind sucess",ret,NULL);
					}
					else
						this0->m_cSM_Manager->Userbehav(iStreamID,"B",strUserName,"stream_bind faild",ret,NULL);
						//补发一路新导航流
						
					if(ret >= 0)
						this0->m_cSM_Manager->AddOneStream(Navigation,iStreamID);			
						
				}
				else if(strcmp(pcmd->valuestring, "vod_play") == 0)
				{
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Vod_call_stream request\n",__FUNCTION__,__LINE__);
					//通过
					cJSON* pUserName = cJSON_GetObjectItem(pRoot, "username");
					char strUserName[128] ={0};
					if(pUserName)
						memcpy(strUserName,pUserName->valuestring,strlen(pUserName->valuestring)+1);
					cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
					int iStreamID = -1;
					if(pStreamID) iStreamID = atoi(pStreamID->valuestring);

					cJSON* pUrlAddr = cJSON_GetObjectItem(pRoot, "url");
					char strUrlAddr[1024] ={0};
					if(pUrlAddr) memcpy(strUrlAddr,pUrlAddr->valuestring,strlen(pUrlAddr->valuestring)+1);

					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
					char strSerialNo[128] ={0};
					if(pSerialNo)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);
						
					//vodname
					cJSON* pVodename = cJSON_GetObjectItem(pRoot,"vodname");
						
					char vodname[128] ={0};
					if(pVodename->valuestring)
						memcpy(vodname,pVodename->valuestring,strlen(pVodename->valuestring)+1);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d vodname = %s\n",__FUNCTION__,__LINE__,vodname);
					//posterurl

					cJSON* pPosterurl = cJSON_GetObjectItem(pRoot,"posterurl");
					char posterurl[128] ={0};
					if(pPosterurl->valuestring)
					{
						memcpy(posterurl,pPosterurl->valuestring,strlen(pPosterurl->valuestring)+1);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d posterurl = \n",__FUNCTION__,__LINE__,posterurl);
					}
					int ret=0;
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d msi connect fd=%d\n",__FUNCTION__,__LINE__,accept_fd);
					//调manager 中点播
					if(Istype == 2)
					{
						char *ptr = NULL;
						ptr = strstr(strUrlAddr,"|");
						if(ptr)
						{
							 ret = this0->m_cSM_Manager->AddOneVodStream(iStreamID,strUrlAddr,accept_fd,strUserName,vodname,posterurl,strSerialNo);//需要参数
						}
						else
						{
							LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d recv total time is null\n",__FUNCTION__,__LINE__,accept_fd);
							ret = -1;
						}
					}
					else
					{
						ret = this0->m_cSM_Manager->AddOneVodStream(iStreamID,strUrlAddr,accept_fd,strUserName,vodname,posterurl,strSerialNo);//需要参数
					}	
					if(ret>=0)
					{
						this0->m_cSM_Manager->Userbehav(iStreamID,"D",strUserName,"vod_play success",ret,NULL);
					}
					else
						this0->m_cSM_Manager->Userbehav(iStreamID,"D",strUserName,"vod_play faild",ret,NULL);
						
				}
				else if (strcmp(pcmd->valuestring, "goback") == 0)
				{
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d goback request\n",__FUNCTION__,__LINE__);
					//通过
					cJSON* pSeesid = cJSON_GetObjectItem(pRoot, "sessionid");

					int iSeessionID = 0;
					if(pSeesid) iSeessionID = atoi(pSeesid->valuestring);
	
					cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
					char strSerialNo[128] ={0};
					if(pSerialNo)
						memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

					cJSON* pMsg = cJSON_GetObjectItem(pRoot,"msg");
					char strMsg[128] ={0};
					if(pMsg)
						memcpy(strMsg,pMsg->valuestring,strlen(pMsg->valuestring)+1);
					cJSON* pUsername = cJSON_GetObjectItem(pRoot,"username");
					char iUsername[64] = {0};
					if(pUsername)
						memcpy(iUsername,pUsername->valuestring,strlen(pUsername->valuestring)+1);
					//iSeessionID = atoi(strSerialNo);
						
					//vod结束退出处理
					this0->m_cSM_Manager->VodStreamOver(iSeessionID,iUsername);

					this0->m_cSM_Manager->Tellcums(iSeessionID);

					usleep(1000*5000);
					if(Istype == 1)
					{
						pthread_mutex_lock(&this0->m_cSM_Manager->m_lockerRegion);
						SetUsedRegionID::iterator itused = this0->m_cSM_Manager->m_SetUsedRegionID.find(iSeessionID);
						if(itused != this0->m_cSM_Manager->m_SetUsedRegionID.end())
						{
							//有正在使用的等会释放
							
							char txt[128]={0};
									sprintf(txt,"%d",itused->second);
									
							this0->m_cSM_Manager->m_SetUsedRegionID.erase(itused);
						}
						pthread_mutex_unlock(&this0->m_cSM_Manager->m_lockerRegion);
					}
					this0->m_cSM_Manager->Userbehav(iSeessionID,"E",iUsername,"vod_play_over",0,NULL);
						
				}
					
				//绑定超时
				else if (strcmp(pcmd->valuestring, "overtime") == 0)
				{	
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d overtime\n",__FUNCTION__,__LINE__);
					cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "streamid");
					if(pStreamID->valuestring!=NULL)
					{	
						int iStreamID = -1;
						if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
						this0->m_cSM_Manager->BindOverTime(iStreamID);
					}
				}
				//VOD暂停
				else if(strcmp(pcmd->valuestring,"vod_pause")==0)
				{
					cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
					cJSON* pUsername = cJSON_GetObjectItem(pRoot, "username");
					//vodname
					cJSON* pVodename = cJSON_GetObjectItem(pRoot,"vodname");
						
					char vodname[128] ={0};
					if(pVodename)
					memcpy(vodname,pVodename->valuestring,strlen(pVodename->valuestring)+1);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d vodname = %s\n",__FUNCTION__,__LINE__,vodname);
					//posterurl

					cJSON* pPosterurl = cJSON_GetObjectItem(pRoot,"posterurl");
					char posterurl[128] ={0};
					if(pPosterurl)
					memcpy(posterurl,pPosterurl->valuestring,strlen(pPosterurl->valuestring)+1);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d posterurl = %s\n",__FUNCTION__,__LINE__,posterurl);
					if(pStreamID->valuestring!=NULL)
					{
						int iStreamID = -1;
						if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
						char iUsername[32] = {0};
						strcpy(iUsername,pUsername->valuestring);
							
						this0->m_cSM_Manager->PauseVOD(iStreamID,iUsername,vodname,posterurl);
					}
						
					Stream ptmpRequest;

					ptmpRequest.m_clientSocket = accept_fd;
					//cJSON *pRet_root;
					ptmpRequest.pRet_root = cJSON_CreateObject();
					ptmpRequest.Requst_Json_str(2,"cmd","vod_pause");
					
					char txt[32] ={0};
					sprintf(txt,"%d",0);
					ptmpRequest.Requst_Json_str(2,"ret_code",txt);

					ptmpRequest.Requst_Json_str(2,"serialno","ade8976d88d76");
					ptmpRequest.Send_Jsoon_str();
						
			  }
					//暂停超时
			  else if(strcmp(pcmd->valuestring,"pause")==0)
			 {
					char iUsername[32] = {0};
					cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "streamid");
					cJSON* pSUserID = cJSON_GetObjectItem(pRoot, "username");
					if(pStreamID->valuestring!=NULL)
					{	
						int iStreamID = -1;
						if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
						strcpy(iUsername,pSUserID->valuestring);
						this0->m_cSM_Manager->BindOverTime(iStreamID);
						this0->m_cSM_Manager->Tellcums(iStreamID);
					}
			 }
			  else if(strcmp(pcmd->valuestring,"vod_resume")==0)
			 {
						
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
				if(pStreamID->valuestring!=NULL)
				{	
					int iStreamID = -1;
							
					if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
					this0->m_cSM_Manager->RecoverVodPlay(iStreamID,NULL,NULL,NULL);
				}
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","vod_resume");
						
				char txt[32] ={0};
				sprintf(txt,"%d",0);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);
				ptmpRequest.Requst_Json_str(2,"serialno","ade8976d88d76");
				ptmpRequest.Send_Jsoon_str();
			 }
			 else if (strcmp(pcmd->valuestring,"stream_unbind") == 0)
			 {	
				LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d unbind time\n",__FUNCTION__,__LINE__);	
				char iusernam[128] = {0};
				char ustreamid[32] = {0};
				int istreamid = 0;
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
				if(pStreamID)
				{
					if(pStreamID->valuestring)
					{
						strcpy(ustreamid,pStreamID->valuestring);
						istreamid = atoi(ustreamid);
					}
				}
				cJSON* pStreamuser = cJSON_GetObjectItem(pRoot, "username");
				if(pStreamuser)
				{
					if(pStreamuser->valuestring)
					strcpy(iusernam,pStreamuser->valuestring);
				}
				char iserialno[64]={0};
				cJSON* pSerialno = cJSON_GetObjectItem(pRoot, "iserialno");
				if(pSerialno)
				{
					if(pSerialno->valuestring)
					strcpy(iserialno,pSerialno->valuestring);
				}
	
				this0->m_cSM_Manager->Unbind(iusernam,ustreamid);

				Stream ptmpRequest;
				ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","stream_unbind");
					
				int ret = 0;
				char txt[32] ={0};
				sprintf(txt,"%d",ret);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);
				ptmpRequest.Requst_Json_str(2,"serialno",iserialno);
				ptmpRequest.Send_Jsoon_str();
					
			}		 
			else if(strcmp(pcmd->valuestring,"quit")==0)
			{
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
				cJSON* pUsername = cJSON_GetObjectItem(pRoot, "username");

				if(pStreamID->valuestring!=NULL)
				{
					int iStreamID = -1;
					if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
					char iUsername[32] = {0};
					strcpy(iUsername,pUsername->valuestring);
					this0->m_cSM_Manager->PauseVOD(iStreamID,iUsername,NULL,NULL,1);
				}
					
				Stream ptmpRequest;
				ptmpRequest.m_clientSocket = accept_fd;
				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","quit");
			
				char txt[32] ={0};
				sprintf(txt,"%d",0);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);

				ptmpRequest.Requst_Json_str(2,"serialno","ade8976d88d76");
				ptmpRequest.Send_Jsoon_str();
					
			}
				
			else if(strcmp(pcmd->valuestring,"recovery")==0)
		    {
						
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
				int iStreamID = -1;
				if(pStreamID->valuestring)
				{
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is Recovery streamid = %s\n",__FUNCTION__,__LINE__,pStreamID->valuestring);
					if(pStreamID) iStreamID = atoi(pStreamID->valuestring);
				}
				//username
				char iusername[64]={0};
				cJSON* pUsername = cJSON_GetObjectItem(pRoot, "username");
				if(pUsername)
				{
					memcpy(iusername,pUsername->valuestring,strlen(pUsername->valuestring)+1);
				}	
					
				//vodname
				char ivodname[128] = {0};
				cJSON* pVodname = cJSON_GetObjectItem(pRoot, "vodname");
				if(pVodname)
				{
					memcpy(ivodname,pVodname->valuestring,strlen(pVodname->valuestring)+1);
				}

					//posterurl
				char iposterurl[128] = {0};
				cJSON* pPosterurl = cJSON_GetObjectItem(pRoot, "posterurl");
				if(pPosterurl)
				{
					memcpy(iposterurl,pPosterurl->valuestring,strlen(pPosterurl->valuestring)+1);
				}
					
				this0->m_cSM_Manager->RecoverVodPlay(iStreamID,iusername,ivodname,iposterurl);
					
				Stream ptmpRequest;
				ptmpRequest.m_clientSocket = accept_fd;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","recovery");
						
				char txt[32] ={0};
				sprintf(txt,"%d",0);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);
				ptmpRequest.Requst_Json_str(2,"serialno","ade8976d88d76");
				ptmpRequest.Send_Jsoon_str();
			 }
			 else if(strcmp(pcmd->valuestring,"reportType")==0)
			 {		
				cJSON* ptyp= cJSON_GetObjectItem(pRoot, "type");
				char type[64];
				if(ptyp->valuestring)
				{	
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is reportType type = %s\n",__FUNCTION__,__LINE__,ptyp->valuestring);
					if(ptyp)
						memcpy(type,ptyp->valuestring,strlen(ptyp->valuestring)+1);	

					if(strcmp(type,"SIHUA")==0)
					{
						Istype = 1;
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is SIHUA\n",__FUNCTION__,__LINE__);
					}
					else if(strcmp(type,"ENRICH")==0)
					{
						Istype = 0;
						
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is ENRICH\n",__FUNCTION__,__LINE__);
					}
					else if(strcmp(type,"VLC")==0)
					{
						Istype = 2;
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is VLC\n",__FUNCTION__,__LINE__);
					}
				}
				char serialno[64]={0};
				cJSON* pSerialno = cJSON_GetObjectItem(pRoot, "serialno");
				if(pSerialno)
				{
					memcpy(serialno,pSerialno->valuestring,strlen(pSerialno->valuestring)+1);
				}
					//回复
				Stream ptmpRequest;

				ptmpRequest.m_clientSocket = accept_fd;
					
				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","reportType");
						
				char txt[32] ={0};
				sprintf(txt,"%d",0);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);
				ptmpRequest.Requst_Json_str(2,"serialno",serialno);
				ptmpRequest.Send_Jsoon_str();
			}	
		}
	}
			
  }
	close(accept_fd);
}

void *SM_Control::ts_recv_MSI_thread(void *arg)
{

	SM_Control *this0 = (SM_Control*)arg;

	//int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;

	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) 
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		return NULL;
	} 
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

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 )
	{
		perror("111bind error:\n");
		fflush(stdout);
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d bind error\n",__FUNCTION__,__LINE__);
		return NULL;
	}
	if(listen(sock,50)<0)
	{
		perror("listen");
		return NULL;
	}

	int accept_fd = -1;
	struct sockaddr_in remote_addr;
	memset(&remote_addr,0,sizeof(remote_addr));

	this0->m_MsiacceptSocket.clear();
	
	while( 1 )
	{  
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
        {  
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Accept error!\n",__FUNCTION__,__LINE__);
	    }  
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Received a connection from %s\n",__FUNCTION__,__LINE__,(char*) inet_ntoa(remote_addr.sin_addr));
		this0->m_MsiacceptSocket.push_back(accept_fd);
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_MSI_thread, this0);
		pthread_detach(tcp_recv_thread1);
	}
}

void *SM_Control::Parse_recv_clear_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	
	int len;
	char Rcv_buf[4096];
	
	cJSON *pcmd = NULL;
	
	//char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_ClearstremSocket;
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
		if(pRoot)
		{
			pcmd = cJSON_GetObjectItem(pRoot, "cmd");
			if (strcmp(pcmd->valuestring, "clean") == 0)
			{
							
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "streamid");
				cJSON* pStreamtype = cJSON_GetObjectItem(pRoot,"streamtype");
				if(pStreamID->valuestring!=NULL)
				{	
					int iStreamID = -1;
					int type = -1;
					if(pStreamID) 
					iStreamID = atoi(pStreamID->valuestring);
					if(pStreamtype) 
					type = atoi(pStreamtype->valuestring);
					this0->m_cSM_Manager->ClearoneStream(iStreamID,type);	
				}
			}
					
		}
		
		if(this0->m_ClearstremSocket != -1)
			close(this0->m_ClearstremSocket);
			this0->m_ClearstremSocket = -1;
	}
}

void *SM_Control::ts_recv_Clear_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	
	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))== -1)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		return NULL;
	}
	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iCleanPort);

	s_addr.sin_addr.s_addr = INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 )
	{
		perror("bind error:\n");
		fflush(stdout);
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

	this0->m_ClearstremSocket = -1;
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Accept error!\n",__FUNCTION__,__LINE__);
                //continue;  
	     }  
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Received a connection from %s\n",__FUNCTION__,__LINE__,(char*) inet_ntoa(remote_addr.sin_addr));
		if(this0->m_ClearstremSocket != -1)
		{
			//需要释放之前的链接
		}
		this0->m_ClearstremSocket = accept_fd;
		pthread_t tcp_recv_thread1;
		pthread_create(&tcp_recv_thread1, NULL, Parse_recv_clear_thread, this0);
		pthread_detach(tcp_recv_thread1);
		

	}
}


void *SM_Control::ts_recv_useract_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	
	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;
	
	if ((sock = socket(AF_INET,SOCK_DGRAM,0))  == -1)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		return NULL;
	} 
	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iUseractport);

	s_addr.sin_addr.s_addr = INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ((bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) 
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d bind error\n",__FUNCTION__,__LINE__);
		return NULL;
	}
		
	char Rcv_buf[4096];
	cJSON *pcmd = NULL;
	socklen_t address_size;	
	//	char cJsonBuff[1024*2];
	int iRecvLen = 0;
	struct sockaddr_in rin;
	int accept_fd = sock;		
	while(1)
	{
		memset(Rcv_buf,0,sizeof(Rcv_buf));
		int length = 0;
		int i_rc = 0, i_count = 0;
		address_size = sizeof(rin);
		do
		{
			i_rc = recvfrom(accept_fd, Rcv_buf + i_count, 2000 - i_count,NULL,(struct sockaddr *)&rin,&address_size);
			if (i_rc <= 0)break;//异常关闭
			i_count += i_rc;
		} while (strstr(Rcv_buf, "XXEE") == NULL);	
		iRecvLen = i_count;
		if (iRecvLen <= 0) break;
		
		LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
			//解析报文数据
		replace(Rcv_buf, "XXEE", "");	
		cJSON* pRoot = cJSON_Parse(Rcv_buf);
		if(pRoot)
		{
			pcmd = cJSON_GetObjectItem(pRoot, "cmd");
			if(strcmp(pcmd->valuestring,"user_action")==0)
			{
				char iUsername[32] = {0};
				char iUsertype[32] = {0};
				char iUserdate[64] = {0};
				char iUsermsg[32] = {0};
				int iStreamID;
				int result;
				cJSON* pUsertype = cJSON_GetObjectItem(pRoot, "action_type");
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "stream_id");
				cJSON* pSUserID = cJSON_GetObjectItem(pRoot, "username");
				cJSON* pUserdate = cJSON_GetObjectItem(pRoot, "action_date");
				cJSON* pUserresult = cJSON_GetObjectItem(pRoot, "action_result");
				cJSON* pUsermsg = cJSON_GetObjectItem(pRoot, "msg");
				if(pSUserID)
					strcpy(iUsername,pSUserID->valuestring);
				if(pUsertype)
					strcpy(iUsertype,pUsertype->valuestring);
				if(pUserdate)
					strcpy(iUserdate,pUserdate->valuestring);
				if(pUsermsg)
					strcpy(iUsermsg,pUsermsg->valuestring);
				if(pUserresult) result = pUserresult->valueint;
				if(pStreamID)
				{
					if(pStreamID->valuestring == NULL)
					{
						iStreamID = 0;
					}
					else
						iStreamID = atoi(pStreamID->valuestring);
				}
				else
				{
					iStreamID = 0;
				}
					
				this0->m_cSM_Manager->Userbehav(iStreamID,iUsertype,iUsername,iUsermsg,result,iUserdate);
			}
		}	
	}
}

void *SM_Control::ts_checksession_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is ts_checksession_thread\n",__FUNCTION__,__LINE__);
	while(1)
	{
		if(checkflag == 10)
		{
			usleep(iwaittime*1000*1000);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to check the session\n",__FUNCTION__,__LINE__);
			this0->m_cSM_Manager->Checksession();
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d check session ok\n",__FUNCTION__,__LINE__);
		}
		
	}
}

void *SM_Control::ts_recv_Contrl_thread(void *arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	
	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;

	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) 
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		return NULL;
	} 
	
	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iContrlport);

	s_addr.sin_addr.s_addr = INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 )
	{
		perror("2233bind error:\n");
		fflush(stdout);
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d bind error\n",__FUNCTION__,__LINE__);
		return NULL;
	}
	if(listen(sock,10)<0)
	{
		perror("listen");
		return NULL;
	}

	int accept_fd = -1;
	struct sockaddr_in remote_addr;
	memset(&remote_addr,0,sizeof(remote_addr));

	this0->m_MsiacceptSocket.clear();
	
	while( 1 )
	{  
		
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
        {  
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d Accept error!\n",__FUNCTION__,__LINE__);
	    }  
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Received a connection from %s\n",__FUNCTION__,__LINE__,(char*) inet_ntoa(remote_addr.sin_addr));
		this0->m_MconacceptSocket.push_back(accept_fd);
		pthread_t tcp_recv_thread;
		pthread_create(&tcp_recv_thread, NULL, Parse_recv_contrl_thread, this0);
		pthread_detach(tcp_recv_thread);
	}
}

void *SM_Control::Parse_recv_contrl_thread(void * arg)
{
	SM_Control *this0 = (SM_Control*)arg;
	int len;
	char Rcv_buf[4096];
	cJSON *pcmd = NULL;
	int iRecvLen = 0;
	int accept_fd = 0;
	Sockfd::iterator iterList = this0->m_MconacceptSocket.begin();
	if(iterList != this0->m_MconacceptSocket.end())
	{
		accept_fd = *iterList;
		this0->m_MconacceptSocket.erase(iterList);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d accept_fd = \n",__FUNCTION__,__LINE__,accept_fd);
	}
	while(1)
	{
		if(accept_fd == -1)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error accept socket in contrl\n",__FUNCTION__,__LINE__);
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
		LOG_INFO_FORMAT("INFO  - [SM]:%s %d recv:\n %s\n",__FUNCTION__,__LINE__,Rcv_buf);
		//解析报文数据
		replace(Rcv_buf, "XXEE", "");
		cJSON* pRoot = cJSON_Parse(Rcv_buf);	

		if (pRoot)
		{
			pcmd = cJSON_GetObjectItem(pRoot, "changeType");
			if (pcmd)
			{		
				cJSON* pChangeid = cJSON_GetObjectItem(pRoot,"id");
				int changeid = 0;
				if(pChangeid)
				{
					changeid = atoi(pChangeid->valuestring);
				}
				cJSON* pTablename = cJSON_GetObjectItem(pRoot, "changeTable");
				char strTableName[128] ={0};
				if(pTablename)
				    memcpy(strTableName,pTablename->valuestring,strlen(pTablename->valuestring)+1);
				//增加记录
				if (strcmp(pcmd->valuestring, "insert") == 0)
				{
					if(strcmp("ucs_navigate",strTableName)==0)
					{
						//暂时不做处理
						this0->m_cSM_Manager->Navgatinsert(changeid);
					}
					else if(strcmp("ucs_network_area",strTableName)==0)
					{
						//暂不处理
					}
					else if(strcmp("ucs_stream_resource",strTableName)==0)
					{
						
						this0->m_cSM_Manager->Strrouseinsert(changeid);
					
					}
					else if(strcmp("ipqam_resource",strTableName)==0)
					{
						//暂不处理
					}
				}
				//修改记录
				else if(strcmp(pcmd->valuestring, "update") == 0)
				{
					if(strcmp("ucs_navigate",strTableName)==0)
					{
						//暂不处理
					}
					else if(strcmp("ucs_network_area",strTableName)==0)
					{
						
					}
					else if(strcmp("ucs_stream_resource",strTableName)==0)
					{
						
					}
					else if(strcmp("ipqam_resource",strTableName)==0)
					{
						//暂不作处理
					}
	
				}
				//删除记录
				else if(strcmp(pcmd->valuestring, "delete") == 0)
				{
					cJSON* pUsername = cJSON_GetObjectItem(pRoot, "changeTable");
					char strUserName[128] ={0};
					if(pUsername)
				   		 memcpy(strUserName,pUsername->valuestring,strlen(pUsername->valuestring)+1);
					if(strcmp("ucs_navigate",strTableName)==0)
					{
						this0->m_cSM_Manager->Navgatdelete(changeid);
					}
					else if(strcmp("ucs_network_area",strTableName)==0)
					{
						//暂不做处理
					}
					else if(strcmp("ucs_stream_resource",strTableName)==0)
					{
						cJSON* pUsername = cJSON_GetObjectItem(pRoot,"username");
						char strUsername[128] ={0};
						if(pUsername)
				   			 memcpy(strUsername,pUsername->valuestring,strlen(pUsername->valuestring)+1);
						this0->m_cSM_Manager->Strrousedelete(changeid,strUsername);
					}
					else if(strcmp("ipqam_resource",strTableName)==0)
					{
						//暂不处理
					}
				}
			}
		}	
	}
	close(accept_fd);
}

