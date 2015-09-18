#include "Switch_worker_Sever.h"

#include <iostream>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

int  Swm_Key_Handler::ParseMessage(char* strMesg,int iLen)
{
	
	cJSON *pcmd = NULL;

	char cJsonBuff[1024 * 2]={0};
	int iRecvLen = 0;

	char Req_buf[1024]={0};
	char Replay_buf[1024]={0};
	memcpy(Req_buf,strMesg,iLen);
	char *Rcv_buf=strMesg;
	Pubc::replace(Rcv_buf,"XXEE","");
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
				char input_url[256] ={0};
				if(pToken) memcpy(input_url,pToken->valuestring,strlen(pToken->valuestring)+1);
				
				char output_url[256] ={0};
				cJSON* pStreamID = cJSON_GetObjectItem(pRoot, "output_url");
				if(pStreamID) memcpy(output_url,pStreamID->valuestring,strlen(pStreamID->valuestring)+1);
				
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->AddOneSwitch(Req_buf,Replay_buf,strsessionid,input_url);
				
				write(Replay_buf,strlen(Replay_buf));
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);

				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
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
				
				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->DeleteOneSwitch(Req_buf,Replay_buf,strsessionid);

				write(Replay_buf,strlen(Replay_buf));
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
			}
			else if(strcmp(pcmd->valuestring, "reset_device") == 0)
			{
				printf("--Reset_stream request \n");

				fflush(stdout);
				
				//通过
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				ManagerFactory::instance()->DeleteAllWsitch(Req_buf,Replay_buf,NULL);
				write(Replay_buf,strlen(Replay_buf));
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);
			}
			else if (strcmp(pcmd->valuestring, "change_stream") == 0)
			{
				printf("--Change_stream request \n");
				fflush(stdout);

			}
			else if (strcmp(pcmd->valuestring, "req_ads_stream") == 0)
			{
				printf("--req_ads_stream request \n");
				fflush(stdout);
				cJSON* psessionid = cJSON_GetObjectItem(pRoot, "sessionid");
				char strsessionid[128] ={0};
				int iSeessionID = 0;
				if(psessionid)
				{
					memcpy(strsessionid,psessionid->valuestring,strlen(psessionid->valuestring)+1);
					//iSeessionID = atoi(strsessionid);
				}
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->ReqOneSwitchForPort(Req_buf,Replay_buf,strsessionid);

				write(Replay_buf,strlen(Replay_buf));
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);

				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);

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
					//iSeessionID = atoi(strsessionid);
				}
				cJSON* pSerialNo = cJSON_GetObjectItem(pRoot,"serialno");
				char strSerialNo[128] ={0};
				if(pSerialNo)
					memcpy(strSerialNo,pSerialNo->valuestring,strlen(pSerialNo->valuestring)+1);

				memset(Replay_buf,0,sizeof(Replay_buf));
				ManagerFactory::instance()->CheckOneSwitch(Req_buf,Replay_buf,strsessionid);

				write(Replay_buf,strlen(Replay_buf));
				LOG_INFO_FORMAT("INFO  - [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				struct timeval tv1;
				long long time1;
				gettimeofday(&tv1, NULL);
				time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
				printf("INFO	- [SWM]: tcp send  %d bytes :[%s] \n",strlen(Replay_buf),Replay_buf);
				
				printf("******* Rcv time =%ld *****\n",time1);

			}
		}	
	}
}

int  Swm_Key_Handler::handle_process()
{
	char req_buf[1024];
	string	host;
	int		port;
    int  	i_rc=0,i_count=0,exist;
    Record req_Element;

    //step1:recv data
    do
    {
    	i_rc=read(req_buf+i_count,1000-i_count,1,1);
        if(i_rc<=0) break;
        i_count+=i_rc;
    }while(strstr(req_buf,"XXEE")==NULL);

    if(i_count >0 )
        req_buf[i_count]='\0';
    else
        return -1;
	get_peer(host,port);
	LOG_INFO_FORMAT("INFO  - [SWM]: tcp recv [%s:%d] thread=%lu socketid=%d ,recved %d bytes :[%s] \n",host.c_str(),port,pthread_self(),getHandle(),i_count,req_buf);

	struct timeval tv1;
	long long time1;
	gettimeofday(&tv1, NULL);
	time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
	printf("INFO  - [SWM]: tcp recv [%s:%d] thread=%lu socketid=%d ,recved %d bytes :[%s] \n",host.c_str(),port,pthread_self(),getHandle(),i_count,req_buf);
	printf("******* Rcv time =%ld *****\n",time1);
	//step2:parse data
	if(!strstr(req_buf,"XXEE")) return -1;
	ParseMessage(req_buf,i_count);
	

	//close();
		 	  	 	  		 	  
	return 0;
}

int Swm_Key_Handler::stop_encoder_system(int pid)
{
	int reqs;
	do
	{
		reqs = kill(pid,0);//若进程不存在，直接退出
		if(reqs)
			break;
		reqs = kill(pid,SIGKILL);//否则杀死进程
	}while(reqs);
	if(!reqs)
		waitpid(pid,NULL,0);
	return 0;
}

bool  Swm_Key_Handler::process_Session_Exist(const Record &p_Element)
{

	return true;

}

bool  Swm_Key_Handler::process_Key_Timeout(const Record &p_Element)
{

	return true;
}

bool   Swm_Key_Handler::send_Data(const char * buf,int length,char* ret_buf,int ret_len)
{	 

	return true;
}	 


