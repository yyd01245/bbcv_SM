#include "rsm_worker_down.h"
#include "rsm_worker_resource.h"
#include <iostream>

int  Rsm_Aim_Handler::handle_process()
{
	char	req_buf[4096];
	int		i_rc=0,i_count=0;
	string	session_id;
	string  ret_string;
	string	host;
	int		port;
	//step1:recv data
	do
	{
		i_rc=read(req_buf+i_count,4000-i_count,1,1);
	 	if(i_rc<=0)break;//异常关闭
	 		i_count+=i_rc;
	}while(strstr(req_buf,"XXEE")==NULL);	  
	if(i_count >0 )
		req_buf[i_count]='\0';
	else
		return -1;//异常处理
	get_peer(host,port);
	
	LOG_INFO_FORMAT("INFO  - [RSM]: tcp recv [%s:%d] thread=%lu socketid=%d ,recved %d bytes :[%s] \n",host.c_str(),port,pthread_self(),getHandle(),i_count,req_buf);
//	printf("INFO  - [RSM]: tcp recv [%s:%d] thread=%lu socketid=%d ,recved %d bytes :[%s] \n",host.c_str(),port,pthread_self(),getHandle(),i_count,req_buf);
	 	  
	//step2:parse data
	if(!strstr(req_buf,"XXEE")) return -1;
	Pubc::replace(req_buf,"XXEE","");
	
	//step3:send data
	opt cmd;
	memset(&cmd,0,sizeof(opt));
	strcpy(cmd.cmd,req_buf);
//	cmd.Sock.setHandle(getHandle());
	cmd.Sock = getHandle();
	gettimeofday(&(cmd.opt_start),NULL);
	RES_oprate::instance()->set_opt(&cmd);
	RES_oprate::instance()->resume_operate();
	
	return -2;
}

