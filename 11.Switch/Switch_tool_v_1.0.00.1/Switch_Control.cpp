#include "Switch_Control.h"
#include "Stream.h"


Switch_Control::Switch_Control()
{
	m_pSwitchManager = NULL;

	m_iMsiPort = iMSIServerPort;


}
Switch_Control::~Switch_Control()
{
	if(m_pSwitchManager)
		delete m_pSwitchManager;
	m_pSwitchManager = NULL;

}

bool Switch_Control::Init()
{

	return true;
}


void *Switch_Control::ts_recv_Control_thread(void *arg)
{

	Switch_Control *this0 = (Switch_Control*)arg;

	int BUFFER_SIZE = 4096;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;


	if ( (sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))  == -1) {
		perror("socket");
		fflush(stderr);
		return NULL;
	} else
		printf("create socket.\n\r");
	fflush(stdout);

	memset(&s_addr, 0, sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;

	s_addr.sin_port = htons(this0->m_iMsiPort);


	s_addr.sin_addr.s_addr = inet_addr(this0->SW_ip.c_str());//INADDR_ANY;

	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) {
		perror("bind");
		fflush(stderr);
		return NULL;
	}else
		printf("bind address to socket.\n\r");
	fflush(stdout);

	if(listen(sock,10)<0)
	{
		perror("listen");
		fflush(stderr);
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
				fflush(stdout);
                //continue;  
	     }  
         printf("Received a connection from %s\n",(char*) inet_ntoa(remote_addr.sin_addr));  

		fflush(stdout);

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

void *Switch_Control::Parse_recv_MSI_thread(void * arg)
{
	Switch_Control *this0 = (Switch_Control*)arg;

	int len;
	char Rcv_buf[4096];

	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->m_MsiacceptSocket;
	//while(1)
	{
			if(accept_fd == -1)
			{
				printf("error accept socket in msi\n");
				fflush(stdout);
				//break;
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
			if (iRecvLen <= 0) return NULL;
	
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
						printf("--Add_stream request \n");
						fflush(stdout);
						//通过
						cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
						char strsessionid[128] ={0};
						if(psessionid)
							memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
						cJSON* pToken = cJSON_GetObjectItem(pRoot, "input_url");
						char input_url[128] ={0};
						if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);

						char output_url[128] ={0};
						cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
						if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						//printf("istreamid=%d usename=%s token=%s \n",iStreamID,strUserName,strToken);
					//	printf("---request parse \n");
						int ret = this0->m_pSwitchManager->AddOneSwitch(input_url,output_url,strsessionid);
						//回复
						/*
							{"cmd":"stream_bind","ret_code":"0","serialno":"c0e1758d697841fa8dad428c23b867a7"}XXEE
						*/
						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","add_ads_stream");
						ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

						ptmpRequest.Requst_Json_str(1,"task_id",strsessionid);
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();
						
					}
					else if(strcmp(pcmd->valuestring, "del_ads_stream") == 0)
					{
						printf("--Del_stream request \n");
						fflush(stdout);
						//通过
						cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
						char strsessionid[128] ={0};
						if(psessionid)
							memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int ret = this0->m_pSwitchManager->DeleteOneSwitch(strsessionid);
						//回复
						/*
							{"cmd":"stream_bind","ret_code":"0","serialno":"c0e1758d697841fa8dad428c23b867a7"}XXEE
						*/
						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","del_ads_stream");

						ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

						
						
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();
					}
					else if(strcmp(pcmd->valuestring, "reset_device") == 0)
					{
						printf("--Del_stream request \n");

						fflush(stdout);
						//{"cmd":"reset_device","returnCode":"0","serialno":"131117ac2d6843b9bf4774e258ab2248"}XXEE
						//通过
						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int ret = this0->m_pSwitchManager->DeleteAllWsitch();
						//回复
						/*
							{"cmd":"stream_bind","ret_code":"0","serialno":"c0e1758d697841fa8dad428c23b867a7"}XXEE
						*/
						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","reset_device");
			
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();
					}
					else if (strcmp(pcmd->valuestring, "change_stream") == 0)
					{
						printf("--Change_stream request \n");
						fflush(stdout);
						cJSON* old_psessionid = cJSON_GetObjectItem(pRoot, "old_sessionid");
						char old_strsessionid[128] ={0};
						if(old_psessionid)
							memcpy(old_strsessionid,old_psessionid->valuestring,strlen(old_psessionid->valuestring)+1);
						cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
						char strsessionid[128] ={0};
						if(psessionid)
							memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
						cJSON* pInterval = cJSON_GetObjectItem(pRoot, "interval");
						char strInterval[128] ={0};
						if(pInterval)
							memcpy(strInterval,pInterval->valuestring,strlen(pInterval->valuestring)+1);
						
						
						cJSON* pToken = cJSON_GetObjectItem(pRoot, "input_url");
						char input_url[128] ={0};
						if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);

						char output_url[128] ={0};
						cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
						if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int ret = this0->m_pSwitchManager->DeleteOneSwitch(old_strsessionid);
						//usleep(1000*500);
						int iInterval = atoi(strInterval);
						iInterval = iInterval*1000*1000;
						usleep(iInterval);
						ret = this0->m_pSwitchManager->AddOneSwitch(input_url,output_url,strsessionid);

						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","change_stream");
					
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();

						
					}
					else if(strcmp(pcmd->valuestring, "check_session") == 0)
					{
						printf("--check_session \n");
						//通过
						cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
						char strsessionid[128] ={0};
						int iSeessionID = 0;
						if(psessionid)
						{
							memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
							iSeessionID = atoi(strsessionid);
						}
						
						

						cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
						char strSerialNo[128] ={0};
						if(pSerialNo)
							memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

						int ret = this0->m_pSwitchManager->CheckOneSwitch(strsessionid);
						
						//报文回复
						Stream ptmpRequest;

						ptmpRequest.m_clientSocket = accept_fd;
						//cJSON *pRet_root;
						ptmpRequest.pRet_root = cJSON_CreateObject();
						ptmpRequest.Requst_Json_str(2,"cmd","check_session");
						
						ptmpRequest.Requst_Json_str(2,"sessionid",strsessionid);

						ptmpRequest.Requst_Json_str(1,"task_id",strsessionid);
						char txt[32] ={0};
						sprintf(txt,"%d",ret);
						ptmpRequest.Requst_Json_str(2,"ret_code",txt);

						ptmpRequest.Requst_Json_str(2,"serialno",strSerialNo);
						ptmpRequest.Send_Jsoon_str();
					}
					
			
				}
			}
			
	}

		close(accept_fd);

}

int Switch_Control::GetHostIp(const char *name,char *addr)
{
	int ret =-1;
	int inet_sock;
	struct ifreq ifr;
	inet_sock = socket(AF_INET, SOCK_DGRAM, 0);
	strcpy(ifr.ifr_name, name);
	if (ioctl(inet_sock, SIOCGIFADDR, &ifr) < 0)
	{
		perror("ioctl");
		return ret;
	}
	sprintf(addr,"%s", inet_ntoa(((struct sockaddr_in*)&(ifr.ifr_addr))->sin_addr));
	return 0;
}


bool Switch_Control::init(const char * ConfigFile)
{
	char ip[64];
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false)  return false;
	
	SW_version = "V1.0.00.1";
	SW_netname=cfg.getValue("SW.NETNAME");
	GetHostIp(SW_netname.c_str(),ip);
	SW_ip=ip;
	SW_listentport=cfg.getValue("SW.LISTENTPORT");
   
	cout<<endl<<"load config is  ----------------------"<<endl;
	cout<<"SW_Version ="<<SW_version<<endl;
	cout<<"SW_ip ="<<SW_ip<<endl;
	cout<<"SW_listentport ="<<SW_listentport<<endl;
	cout<<"load config over      ----------------------"<<endl;

	m_iMsiPort = atoi(SW_listentport.c_str());

		//启动接收线程
	pthread_t tcp_recv_thread4;
	pthread_create(&tcp_recv_thread4, NULL, ts_recv_Control_thread, this);
	pthread_detach(tcp_recv_thread4);

	m_pSwitchManager = new Switch_Manager;
	char SW_Net_ip[256]={0};
	strcpy(SW_Net_ip,SW_ip.c_str());
	m_pSwitchManager->Init(SW_Net_ip);
	 return true;
}


