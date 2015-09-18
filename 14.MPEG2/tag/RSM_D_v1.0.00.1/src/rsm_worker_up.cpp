#include "rsm_worker_up.h"
#include "rsm_worker_vncms.h"
#include "rsm_config.h"
#include "rsm_store.h"

#include <iostream>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

int  Rsm_Key_Handler::handle_process()
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
	LOG_INFO_FORMAT("INFO  - [RSM]: tcp recv [%s:%d] thread=%lu socketid=%d ,recved %d bytes :[%s] \n",host.c_str(),port,pthread_self(),getHandle(),i_count,req_buf);
		

    //step2:parse data
    if(!strstr(req_buf,"XXBB")|!strstr(req_buf,"XXEE")) return -1;
    Pubc::replace(req_buf,"XXBB","");
    Pubc::replace(req_buf,"XXEE","");
    if(!req_Element.from_String(req_buf,"|")) return -1;
    LOG_DEBUG_FORMAT("req_Element =:%s \n",req_Element.to_string().c_str());

    //step3:process logic	

	string cmd;
	bool b_ret;
	if(!req_Element.get(0,cmd)) return -1;
	if("APP_VNC_TIMEOUT_REQ"==cmd)
	{
		b_ret = process_Key_Timeout(req_Element);
		sprintf(req_buf,"XXBBAPP_VNC_TIMEOUT_RESP|0XXEE");
	}
	else if("APP_VNC_EXIST_REQ"==cmd)
	{
		b_ret = process_Session_Exist(req_Element);
		if(b_ret)
			exist = 0;
		else
			exist = -1;
		sprintf(req_buf,"XXBBAPP_VNC_EXIST_RESP|%dXXEE",exist);
		
	}
	else
		LOG_WARN_FORMAT("WARN  - [RSM]: ReqType is unknown:%s \n",cmd.c_str());

	//resp to vncms
		
		LOG_INFO_FORMAT("INFO  - [RSM]: send to vncm %s \n",req_buf);
		write(req_buf,strlen(req_buf));
		 	  	 	  		 	  
	return -1;
}

int Rsm_Key_Handler::stop_encoder_system(int pid)
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

bool  Rsm_Key_Handler::process_Session_Exist(const Record &p_Element)
{
	string session_id;
	char cmd_buff[128];
	char ret_buff[128];
	int encoder_id,reqs;
	
	if(!p_Element.get(1,session_id)) return false;
	if(!strcmp(session_id.c_str(),"pre"))
		return true;
	cJSON* root,*ret_root,*JSON_retcode;
	root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"cmd","vnccheck");
	cJSON_AddStringToObject(root,"resid",session_id.c_str());
	//cJSON_AddStringToObject(root,"vncip",Rsm_Config::instance()->rsm_ip.c_str());
	sprintf(cmd_buff,"%sXXEE",cJSON_PrintUnformatted(root));
	cJSON_Delete(root);
	root = NULL;
	if(!send_Data(cmd_buff,strlen(cmd_buff),ret_buff,127)) 
		return false;
	if(!strstr(ret_buff,"XXEE")) return false;
	Pubc::replace(ret_buff,"XXEE","");
	
	ret_root = cJSON_Parse(ret_buff);
	if(!ret_root)
		goto false_and_quit;
	JSON_retcode = cJSON_GetObjectItem(ret_root,"retcode");
	if(!JSON_retcode)
	{
		goto false_and_quit;
	}
	if(atoi(JSON_retcode->valuestring))
	{
		LOG_INFO_FORMAT("INFO  - [RSM]: want to release session %s\n",session_id.c_str());
		encoder_id = Rsm_Session::instance()->map_session[session_id].encoder_id;	
		if(0 != encoder_id)
		{
			LOG_INFO_FORMAT("INFO  - [RSM]: release encoder start %d\n",encoder_id);
			reqs = stop_encoder_system(encoder_id);
			LOG_INFO_FORMAT("INFO  - [RSM]: encoder return %d ,encoder id  %d\n", reqs,encoder_id);
			if((reqs != 0) && (-19 != reqs))
			{
				LOG_ERROR("ERROR - [RSM] [-1824] release encoder fail \n");
				goto false_and_quit;
			}
		}
		if(!VNC_State::instance()->vnc_relsease(session_id))
		{
			LOG_ERROR("ERROR - [RSM] [-1823] release chrome fail \n");
			goto false_and_quit;
		}
		Rsm_Session::instance()->delUser(session_id.c_str(),encoder_id);
	}
	cJSON_Delete(ret_root);	
	return true;
	
	false_and_quit:
		cJSON_Delete(ret_root);
		return false;

}

bool  Rsm_Key_Handler::process_Key_Timeout(const Record &p_Element)
{
	string session_id;
	char cmd_buff[128];
	char ret_buff[128];
	char mgmtport[8]={0};
	cJSON* root;

	if(!p_Element.get(1,session_id)) return false;
	root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"cmd","vnctimeout");
	cJSON_AddStringToObject(root,"resid",session_id.c_str());
	cJSON_AddStringToObject(root,"vncip",Rsm_Config::instance()->rsm_ip.c_str());
	cJSON_AddStringToObject(root,"vncport",Rsm_Config::instance()->rsm_aim_listentport.c_str());
	sprintf(mgmtport,"%d",(atoi(Rsm_Config::instance()->rsm_aim_listentport.c_str())+1));
	cJSON_AddStringToObject(root,"mgmtport",mgmtport);
	sprintf(cmd_buff,"%sXXEE",cJSON_PrintUnformatted(root));
	cJSON_Delete(root);
	root = NULL;
	if(strcmp( session_id.c_str() , "pre" ))
	{	
		if(!send_Data(cmd_buff,strlen(cmd_buff),ret_buff,127)) 
			return false;
	}
	return true;
}

bool   Rsm_Key_Handler::send_Data(const char * buf,int length,char* ret_buf,int ret_len)
{	 
	Socket sock;
	int reqs;
	if(!Socket_Connector::connect(sock,Rsm_Config::instance()->aim_ip.c_str(), Rsm_Config::instance()->aim_port))
	{
		LOG_WARN_FORMAT("Warning :socket connnect error: %s:%d \n",Rsm_Config::instance()->aim_ip.c_str(), Rsm_Config::instance()->aim_port);
		return false;
	}
	if(length!=sock.write(buf,length))
	{
		LOG_WARN_FORMAT("Warning :socket send data error: %s:%d \n",Rsm_Config::instance()->aim_ip.c_str(), Rsm_Config::instance()->aim_port);
		return false;
	}
	LOG_INFO_FORMAT("INFO  - [RSM]:tcp send [%s:%d] %s \n",Rsm_Config::instance()->aim_ip.c_str(),Rsm_Config::instance()->aim_port,buf);
	sock.read(ret_buf,ret_len,1,5);
	LOG_INFO_FORMAT("INFO  - [RSM]:tcp recv [%s:%d] %s\n",Rsm_Config::instance()->aim_ip.c_str(),Rsm_Config::instance()->aim_port,ret_buf);
	sock.close();
	return true;
}	 


