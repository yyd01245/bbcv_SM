#include <iostream>

#include "vnc_worker_rsm.h"

bool Prase_Packet::Set_Para(int i,char * data)
{
	switch (i)
	{
		case 0:
			cmd = data;
			break;
		case 1:
			session_id = data;
			break;
		case 2:
			time_out = atoi(data);
			break;
		case 3:
			url = data;
			break;
		default:
			return false;
	}
	return true;
}

bool Prase_Packet::init_para(char *str,const char * flag)
{
	char *p = NULL;
	char *data = NULL;
	int i = 0;
	data = str;
	while(p = strstr(data,flag))
	{
		*p = '\0';
		if(!Set_Para(i,data))
			return false;
		data = p + 1;
		i++;
	}
	size = i+1;
	if(0 == i)
		return false;
	if(!Set_Para(i,data))
		return false;
	return true;
}

int  Rsm_AiM_Handler::handle_process()
{
	int  i_rc=0,i_count=0,port=0;
	bool b_ret=false;
	char req_buf[4096]={0};
	string cmd,host;
	string ret_string="XXBBAPP|0XXEE";
	Prase_Packet Session_data;
	//step1:recv data
	do
	{
	 	 i_rc = read(req_buf + i_count, 4000 - i_count, 1, 1);
	 	 if(i_rc<=0)  
		 {
		 	break;//异常关闭
	 	 }
		 
	 	  i_count += i_rc;
	 }while(strstr(req_buf,"XXEE")==NULL);	
	get_peer(host,port);
	
	LOG_TRACE_FORMAT("DEBUG - [VNCMS]:tcp server recv [%s:%d] handle_process   recv data : req_buf=%s recv size : %d \n",host.c_str(),port, req_buf,strlen(req_buf));

	if(i_count >0 )
	 	  	req_buf[i_count]='\0';
	else
	{
		LOG_ERROR("ERROR - [VNCMS]:  recv data error!\n");
	 	 return -1;//异常处理
	}


	//step2:parse data
	if(!strstr(req_buf,"XXBB")|!strstr(req_buf,"XXEE"))	
	{
		LOG_ERROR("ERROR - [VNCMS]:  parse data failed! can't get XXBB or XXEE\n");
		return -1;
	}

	Pubc::replace(req_buf,"XXBB","");
	Pubc::replace(req_buf,"XXEE","");

	if(!Session_data.init_para(req_buf,"|")) 	
	{
		LOG_ERROR("ERROR - [VNCMS]:  parse data failed! can't get | \n");
		return -1;
	}
	LOG_INFO_FORMAT("cmd: %s,session_id: %s,size: %d,url: %s,time_out: %d\n",
		Session_data.cmd.c_str(),
		Session_data.session_id.c_str(),
		Session_data.size,
		Session_data.url.c_str(),
		Session_data.time_out);
	//step3:process logic
	cmd = Session_data.cmd;
	if(0 >= cmd.size())
	{
		LOG_ERROR("ERROR - [VNCMS]:  process logic failed!\n");
		return -1;
	}
	
//	write(ret_string.c_str(),ret_string.size());//先返回成功，后执行启动

	if("APP_RFB_REQ"==cmd)
	{
		b_ret=createVnc(Session_data,ret_string);
	}
	else if("APP_VNC_RELEASE_REQ"==cmd)
	{
		b_ret=freeVnc(Session_data,ret_string);	
	}
	else if("APP_FORBID_TIMEOUT_REQ"==cmd)
	{
		b_ret=VNC_State::instance()->stop_Timeout();	
	}
	else if("APP_FRESH_TIMEOUT_REQ"==cmd)
	{
		b_ret=VNC_State::instance()->fresh_Timeout();	
	}
	else if("APP" == cmd)
		ret_string="XXBBAPP|0XXEE";
	else 
	{
		ret_string="XXBB|XXEE";
	}
	write(ret_string.c_str(),ret_string.size());
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]: return_msg=%s\n", ret_string.c_str());
	return -1;//断开连接
}

bool   Rsm_AiM_Handler::createVnc(const Prase_Packet &p_Element,string &ret_string)
{
	bool ret = true;

	if(p_Element.size != 4)
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS]: create vnc p_Element.size=%d\n", p_Element.size);
		return false;
	}

	if(0 >= p_Element.session_id.size()) return false;
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:aim_handler createVnc get sessionid=%s\n", p_Element.session_id.c_str());
		
	if(0 >= p_Element.time_out) return false;
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:aim_handler createVnc get timeout=%d\n", p_Element.time_out);

	if(0 >= p_Element.url.size()) return false;
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:aim_handler createVnc get url=%s\n", p_Element.url.c_str());


	//创建vnc，返回pid/port等信息
	VNC_State::instance()->vnc_create(p_Element.session_id, p_Element.time_out, p_Element.url, getHandle());
	
	return ret;
}


bool   Rsm_AiM_Handler::freeVnc(const Prase_Packet &p_Element,string &ret_string)
{
	//XXBBAPP_VNC_RELEASE_REQ|123XXEE
	//USER_MAP::iterator iter;
	bool ret;
	string session_id,url;
		
	if(p_Element.size != 2)
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS]: free vnc p_Element.size=%d\n", p_Element.size);
		return false;
	}

	if(0 >= p_Element.session_id.size()) 
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS]: freeVnc session_id = %s fail\n",  p_Element.session_id.c_str());
		return false;
	}
	
	ret = VNC_State::instance()->vnc_free();
	if(ret)
	{
		ret_string="XXBBAPP|0XXEE";
	}
	else
	{
		ret_string="XXBBAPP|-1XXEE";
	}	
	return ret;
}



