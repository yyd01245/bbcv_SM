#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>
#include <stdlib.h>

#include "cJSON.h"
#include "cRecv.h"
#include "cStream.h"

char * SM_Recv::replace(char *strbuf, const char *src_str, const char *desc_str)
{

	char *pos, *pos1;

	if (strbuf == NULL || src_str == NULL || desc_str == NULL) return strbuf;

	if (strlen(src_str) == 0) return strbuf;
	int ilen = strlen(strbuf);
	char *org = new char[ilen + 1];

	strcpy(org, strbuf);
	pos = org;
	strbuf[0] = 0;
	printf("11111 begin to replace\n");
	fflush(stdout);
	while (1)
	{
		pos1 = strstr(pos, src_str);
		if (pos1 == NULL)
			break;
		*pos1 = '\0';
		strcat(strbuf,  pos);
		strcat(strbuf, desc_str);
		pos = pos1 + strlen(src_str);
	}
	strcat(strbuf,  pos);
	delete org;
	printf("22222222this is replace\n");
	fflush(stdout);
	return strbuf;
}

void *SM_Recv::Parse_recv_thread(void * arg)
{	
	SM_Recv* this0 = (SM_Recv*)arg;
	int len;
	char Rcv_buf[4096];
	
	cJSON *pcmd = NULL;
	
	char cJsonBuff[1024 * 2];
	int iRecvLen = 0;
	int accept_fd = this0->c_MulacceptSocket;	
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
		this0->replace(Rcv_buf, "XXEE", "");
		cJSON* pRoot = cJSON_Parse(Rcv_buf);	
		if(pRoot)
		{
			printf("---this is pRoot\n");
			fflush(stdout);
			pcmd = cJSON_GetObjectItem(pRoot, "cmd");
			if (strcmp(pcmd->valuestring, "start") == 0)
			{
				// adv name
				printf("12098fjp\n");
				fflush(stdout);
			//	this0->advstream->stoponestream(NULL,NULL);
			//	usleep(1000*800);
			/*	
				cJSON* pName = cJSON_GetObjectItem(pRoot, "advname");
				char advname[128] = {0};
				if(pName->valuestring)
				{
					memcpy(advname,pName->valuestring,strlen(pName->valuestring)+1);
					printf("---adv name = %s\n",advname);
					fflush(stdout);
				}
				// adv ip
				cJSON* pip = cJSON_GetObjectItem(pRoot,"advip");
				char advip[128]={0};
				if(pip->valuestring)
				{
					memcpy(advip,pip->valuestring,strlen(pip->valuestring)+1);
					printf("---adv ip = %s\n",advip);
					fflush(stdout);
				}

				//adv port 
				cJSON* pport = cJSON_GetObjectItem(pRoot,"advport");
				char advport[128]={0};
				if(pport->valuestring)
				{
					memcpy(advport,pport->valuestring,strlen(pport->valuestring)+1);
					printf("---adv port = %s\n",advport);
					fflush(stdout);
				}
				printf("2222 this is Parse_recv_thread\n");
				fflush(stdout);
*/
				printf("begin to start advstream\n");
				this0->advstream->Initstart();	
				
/*
				this0->advstream->startonestream(advname,advip,advport);

				//回复
				Stream ptmpRequest;			
				ptmpRequest.m_clientSocket = accept_fd;
				//cJSON *pRet_root;
				ptmpRequest.pRet_root = cJSON_CreateObject();
				
				ptmpRequest.Requst_Json_str(2,"cmd","start");	
				ptmpRequest.Requst_Json_str(2,"advname",advname);
				ptmpRequest.Requst_Json_str(2,"advip",advip);
				ptmpRequest.Requst_Json_str(2,"advport",advport);

				int ret = 0;
				char txt[32] ={0};
				sprintf(txt,"%d",ret);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);
				
				ptmpRequest.Send_Jsoon_str();
*/		
				
			}	
			else if(strcmp(pcmd->valuestring, "stopall")==0)
			{
				printf("123hello world\n");
				fflush(stdout);
				this0->advstream->stoponestream();
				
			/*
				Stream ptmpRequest;			
				ptmpRequest.m_clientSocket = accept_fd;
				//cJSON *pRet_root;

				ptmpRequest.pRet_root = cJSON_CreateObject();
				ptmpRequest.Requst_Json_str(2,"cmd","stopall");	
			
				int ret = 0;
				char txt[32] ={0};
				sprintf(txt,"%d",ret);
				ptmpRequest.Requst_Json_str(2,"ret_code",txt);	
				ptmpRequest.Send_Jsoon_str();
			*/
			}
			else if(strcmp(pcmd->valuestring, "stopone")==0)
			{	
				cJSON* pSport= cJSON_GetObjectItem(pRoot, "pid");
				int pid;
				if(pSport->valuestring)
				{
					pid = atoi(pSport->valuestring);
					printf("---adv port = %s\n",pid);
					fflush(stdout);
				}
				bool ret1;
				int ret_code1;
				ret1= this0->advstream->stoponestream(pid);

				if(ret1 == true)
				{
					ret_code1 = 0;
				}
				else
				{
					ret_code1 = -1;
				}
				//回复
				Stream ptmpRequest1;			
				ptmpRequest1.m_clientSocket = accept_fd;
				//cJSON *pRet_root;
				ptmpRequest1.pRet_root = cJSON_CreateObject();
				
				ptmpRequest1.Requst_Json_str(2,"cmd","stopone");
				
				char txt1[32] ={0};
				sprintf(txt1,"%d",ret_code1);
				ptmpRequest1.Requst_Json_str(2,"ret_code",txt1);
				ptmpRequest1.Send_Jsoon_str();
				
			}
		}
		
	}
}

void * SM_Recv::ts_recv_thread(void *arg)
{
	printf("this is ts_recv_thread\n");
	SM_Recv* this0 = (SM_Recv*)arg;
	struct sockaddr_in s_addr;
	int sock;
	socklen_t addr_len;
	int accept_fd;
	if((sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))==-1)
	{
		printf("create socket wrong\n");
		fflush(stdout);
	}
	
	memset(&s_addr,0,sizeof(struct sockaddr_in));
	s_addr.sin_family = AF_INET;
	s_addr.sin_port = htons(mulport);
	s_addr.sin_addr.s_addr = INADDR_ANY;
	
	int optval = 1;
	if ((setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(sock);
		return NULL;
	}

	if ( (bind(sock, (struct sockaddr*)&s_addr, sizeof(s_addr))) == -1 ) 
	{
		perror("bind");
		return NULL;
	}
	else
		printf("bind address to socket.\n\r");
	fflush(stdout);

	if(listen(sock,10)<0)
	{
		perror("listen");
		return NULL;
	}

	struct sockaddr_in remote_addr;
	memset(&remote_addr,0,sizeof(remote_addr));
	printf("111111111111hello world\n");
	fflush(stdout);
	this0->c_MulacceptSocket = -1;
	while( 1 )
	{  
	    socklen_t sin_size = sizeof(struct sockaddr_in); 
		if(( accept_fd = accept(sock,(struct sockaddr*) &remote_addr,&sin_size)) == -1 )  
         {  
             perror( "Accept error!\n");   
	     }  
		 if(this0->c_MulacceptSocket!=-1)
		 {
			//需要释放之前的链接
			//close(this0->c_MulacceptSocket);
		 }
		 this0->c_MulacceptSocket = accept_fd;
		 printf("1111111111111111this is recv proc\n");
		 fflush(stdout);
		 pthread_t Parse_recv_thread1;
		 pthread_create(&Parse_recv_thread1,NULL,Parse_recv_thread,this0);
		 pthread_detach(Parse_recv_thread1);
	}
}

void* SM_Recv::keep_alive_thread(void *arg)
{
	SM_Recv* this0 = (SM_Recv*)arg;
	printf("this is keep_alive_thread\n");
	while(1)
	{
		usleep(1000*15000);
		this0->advstream->keepalive();
	}
}

void* SM_Recv::check_database_thread(void *arg)
{
	SM_Recv* this0 = (SM_Recv*)arg;
	printf("this is check_database_thread\n");
	while(1)
	{
		usleep(1000*15000);
		printf("begin to check_database\n");
		this0->advstream->CheckUpdate();
	}
}
bool SM_Recv::recv_func()
{
	//开启接受外部指令线程
	pthread_t tcp_recv_thread1;
	pthread_create(&tcp_recv_thread1, NULL,ts_recv_thread,this);
	pthread_detach(tcp_recv_thread1);

	//开启守护组播流线程
	pthread_t tcp_recv_thread2;
	pthread_create(&tcp_recv_thread2, NULL,keep_alive_thread,this);
	pthread_detach(tcp_recv_thread2);

	//开启检查数据库更新线程
	pthread_t check_database;
	pthread_create(&check_database, NULL,check_database_thread,this);
	pthread_detach(check_database);
}

