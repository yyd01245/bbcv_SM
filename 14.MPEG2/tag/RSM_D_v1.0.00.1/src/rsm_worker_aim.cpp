#include <iostream>
int Rsm_Manager_Handler::handle_process()
{
	char	req_buf[4096];
	int		i_rc=0,i_count=0;
	string	session_id;
	string  ret_string;
	string	host;
	int		port;
	char*	cmd=NULL;
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

	//step2:parse data
	if(!strstr(req_buf,"XXEE")) return -1;
	Pubc::replace(req_buf,"XXEE","");
	cJSON *root = cJSON_Parse(req_buf);
	if(!root)
	{
		ret_string = "{}XXEE";
		write(ret_string.c_str(),ret_string.size());
		return -1;
	}
	cmd = cJSON_GetObjectItem(root,"cmd")->valuestring;
	cmd = cmd+7;
	//step3:process logic
	bool b_ret=false;
	switch(*cmd)
	{
		case 'f': //change config
		{
			b_ret = process_change_config(root,ret_string);
			break;
		}

	}

}
	
bool Rsm_Manager_Handler::process_change_config()
{

}

bool Rsm_Manager_Handler::process_rsm_restart()
{

}
bool Rsm_Manager_Handler::process_chromeplugin_update()
{

}

bool Rsm_Manager_Handler::process_server_detail()
{

}
