#include "initadvstream.h"
#include "CommonFun.h"
#include "cJSON.h"
#include "DBInterface.h"
#include <stdio.h>

Advmul_stream::Advmul_stream()
:Stream()
{
	memcpy(m_strdstIP,advip,strlen(advip)+1);
	m_idstport = muladvport;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d advport = %d\n",__FUNCTION__,__LINE__,muladvport);
}
bool Advmul_stream::CleanmulStream()
{
	//启动客户端链接
		
		if(!Connect_mulServer())
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d error can't connect to server\n",__FUNCTION__,__LINE__);
		}
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d connect Adv success\n",__FUNCTION__,__LINE__);
		if(pRet_root)
		{
			cJSON_Delete(pRet_root);
			pRet_root = NULL;
		}
		
	//	{"cmd":"start","advname":"advhuashu.ts","advip":"225.0.0.1","advport":"12000"}
	
		pRet_root = cJSON_CreateObject();
		Requst_Json_str(2,"cmd","stopall");
		
		Send_Jsoon_str();
	
		return true;
}
//处理导航流接口
void *Advmul_stream::Parse_recv_Mul_thread(void *arg)
{
	Advmul_stream *this0 = (Advmul_stream*)arg;
	
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
			if (strcmp(pcmd->valuestring, "start") == 0)
			{
					//承载rsm的ip端口
				cJSON* pCname = cJSON_GetObjectItem(pRoot,"advname");
				char cname[128] ={0};
				//printf("parse--%s\n",pSerialNo->valuestring);
				if(pCname)
				memcpy(cname,pCname->valuestring,strlen(pCname->valuestring)+1);

				cJSON* pCip = cJSON_GetObjectItem(pRoot,"advip");
				char strcip[128] ={0};
				//printf("parse--%s\n",pSerialNo->valuestring);
				if(pCip)
				memcpy(strcip,pCip->valuestring,strlen(pCip->valuestring)+1);
				
				cJSON* pCport = cJSON_GetObjectItem(pRoot,"advport");
				char advport[128] ={0};
				//printf("parse--%s\n",pSerialNo->valuestring);
				if(pCport)
				memcpy(advport,pCport->valuestring,strlen(pCport->valuestring)+1);

				cJSON* pCretcod = cJSON_GetObjectItem(pRoot,"ret_code");
				char ret_code[128] ={0};
				//printf("parse--%s\n",pSerialNo->valuestring);
				if(pCretcod)
				memcpy(ret_code,pCretcod->valuestring,strlen(pCretcod->valuestring)+1);
			}
			if (strcmp(pcmd->valuestring, "stopall") == 0)
			{
				//承载rsm的ip端口
				cJSON* pCretcod = cJSON_GetObjectItem(pRoot,"ret_code");
				char ret_code[64] ={0};
				//printf("parse--%s\n",pSerialNo->valuestring);
				if(pCretcod)
				memcpy(ret_code,pCretcod->valuestring,strlen(pCretcod->valuestring)+1);
			}
		}
		close(accept_fd);
	}
}
bool Advmul_stream::Connect_mulServer()
{
	int socket_fd;	
	struct sockaddr_in server_addr;  
	if( (socket_fd = socket(AF_INET,SOCK_STREAM,0)) < 0 ) 
	{  
	 	 LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d create socket error\n",__FUNCTION__,__LINE__);
		 return false;
	}	
    memset(&server_addr,0,sizeof(server_addr));  
    server_addr.sin_family = AF_INET;  
    server_addr.sin_port = htons(m_idstport);  
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d Adver server ip=%s port=%d\n",__FUNCTION__,__LINE__,m_strdstIP,m_idstport);
    if( inet_pton(AF_INET,m_strdstIP,&server_addr.sin_addr) <=0 )
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d inet_pton error for %s\n",__FUNCTION__,__LINE__,m_strdstIP);
        exit(0);  
	}  

	if( connect(socket_fd,(struct sockaddr*)&server_addr,sizeof(server_addr))<0) 
	{  
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d connect error\n",__FUNCTION__,__LINE__);
        exit(0);  
    }  
	m_clientSocket = socket_fd;

	return true;

}

bool Advmul_stream::StartadvStream()
{
	//启动客户端链接
	
	if(!Connect_mulServer())
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d connect error\n",__FUNCTION__,__LINE__);
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d connect Adv success\n",__FUNCTION__,__LINE__);
	if(pRet_root)
	{
		cJSON_Delete(pRet_root);
		pRet_root = NULL;
	}
	
//	{"cmd":"start","advname":"advhuashu.ts","advip":"225.0.0.1","advport":"12000"}

	pRet_root = cJSON_CreateObject();
	Requst_Json_str(2,"cmd","start");
	//Requst_Json_str(2,"advname",iadvname);
	//Requst_Json_str(2,"advip",iadvip);
	//Requst_Json_str(2,"advport",iadvport);
	
	Send_Jsoon_str();

	return true;	
}
