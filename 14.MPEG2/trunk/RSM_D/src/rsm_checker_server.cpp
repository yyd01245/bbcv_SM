#include "rsm_checker_server.h"
#include "Pub_c.h"
#include "res_register.h"
#include "rsm_worker_resource.h"

int check_server::check_server_status()
{
	Data_List::iterator cpu_iter,mem_iter,gpu_iter,gpum_iter;

	check_error = true;

	bool ret = true;

/*初始化数据*/
	cpu_set_count = Rsm_Config::instance()->cpu_set_count;
	mem_set_count = Rsm_Config::instance()->mem_set_count;
	gpu_set_count = Rsm_Config::instance()->gpu_set_count;
	gpum_set_count = Rsm_Config::instance()->gpum_set_count;

	check_interval = Rsm_Config::instance()->check_interval;

	cpu_usage_upper = Rsm_Config::instance()->cpu_usage_upper;
	cpu_usage_lower = Rsm_Config::instance()->cpu_usage_lower;

	mem_usage_upper = Rsm_Config::instance()->mem_usage_upper;
	mem_usage_lower = Rsm_Config::instance()->mem_usage_lower;

	gpu_usage_upper = Rsm_Config::instance()->gpu_usage_upper;
	gpu_usage_lower = Rsm_Config::instance()->gpu_usage_lower;

	gpum_usage_upper = Rsm_Config::instance()->gpum_usage_upper;
	gpum_usage_lower = Rsm_Config::instance()->gpum_usage_lower;

	LOG_INFO_FORMAT("INFO  -[RSM]:server checker cpu_set_count %d\n",cpu_set_count);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker mem_set_count %d\n",mem_set_count);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpu_set_count %d\n",gpu_set_count);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpum_set_count %d\n",gpum_set_count);

	LOG_INFO_FORMAT("INFO  -[RSM]:server checker cpu_usage_upper %f\n",cpu_usage_upper);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker cpu_usage_lower %f\n",cpu_usage_lower);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker mem_usage_upper %f\n",mem_usage_upper);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker mem_usage_lower %f\n",mem_usage_lower);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpu_usage_upper %f\n",gpu_usage_upper);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpu_usage_lower %f\n",gpu_usage_lower);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpum_usage_upper %f\n",gpum_usage_upper);
	LOG_INFO_FORMAT("INFO  -[RSM]:server checker gpum_usage_lower %f\n",gpum_usage_lower);
	/*初始化数据*/


	for(;;)
	{
		sleep(check_interval);
		ret = get_cpm_mem();
		if(!ret)
			LOG_ERROR("ERROR - [RSM]: get cpu & mem fail\n");
		if(check_error)
		{
			if(cpu_list.size()<=0)
				continue;
			if(mem_list.size()<=0)
				continue;
			if(gpu_list.size()<=0)
				continue;
			if(gpum_list.size()<=0)
				continue;
			for(cpu_iter = cpu_list.begin();cpu_iter != cpu_list.end();cpu_iter++)
			{
			//	LOG_INFO_FORMAT("cup %f, upper%f\n",*cpu_iter,cpu_usage_upper);
				if(*cpu_iter < cpu_usage_upper)
					break;
			}
			if(cpu_list.end() == cpu_iter)
				LOG_INFO("INFO  - [SERVER]:cpu over load\n");
			for(mem_iter = mem_list.begin();mem_iter != mem_list.end();mem_iter++)
			{
				if(*mem_iter < mem_usage_upper)
					break;
			}
			if(mem_list.end() == mem_iter)
				LOG_INFO("INFO  - [SERVER]:mem over load\n");
			for(gpu_iter = gpu_list.begin();gpu_iter != gpu_list.end();gpu_iter++)
			{
				if(*gpu_iter < gpu_usage_upper)
					break;
			}
			if(gpu_list.end() == gpu_iter)
				LOG_INFO("INFO  - [SERVER]:gpu over load\n");
			for(gpum_iter = gpum_list.begin();gpum_iter != gpum_list.end();gpum_iter++)
			{
				if(*gpum_iter < gpum_usage_upper)
					break;
			}
			if(gpum_list.end() == gpum_iter)
				LOG_INFO("INFO  - [SERVER]:gpum over load\n");
			if((cpu_list.end() == cpu_iter) || (mem_list.end() == mem_iter)
			|| (gpu_list.end() == gpu_iter) || (gpum_list.end() == gpum_iter))
			{
				send_status(1);
				LOG_INFO("server over load \n");
				check_error = false;
			}
		}
		else
		{
			for(cpu_iter = cpu_list.begin();cpu_iter != cpu_list.end();cpu_iter++)
			{
				if(*cpu_iter > cpu_usage_lower)
					break;
			}
			for(mem_iter = mem_list.begin();mem_iter != mem_list.end();mem_iter++)
			{
				if(*mem_iter > mem_usage_lower)
					break;
			}
			for(gpu_iter = gpu_list.begin();gpu_iter != gpu_list.end();gpu_iter++)
			{
				if(*gpu_iter > gpu_usage_lower)
					break;
			}
			for(gpum_iter = gpum_list.begin();gpum_iter != gpum_list.end();gpum_iter++)
			{
				if(*gpum_iter > gpum_usage_lower)
					break;
			}
			if((cpu_list.end() == cpu_iter) && (mem_list.end() == mem_iter)
			&& (gpu_list.end() == gpu_iter) && (gpum_list.end() == gpum_iter))
			{	
				send_status(0);
				LOG_INFO("server cpu&mem became regular\n");
				check_error = true;
				}
		}
	}

	LOG_ERROR("ERROR - [RSM]: Thread check server exit \n");
	return 0;
}

bool check_server::get_cpm_mem()
{
	cJSON *root,*ret_root,*cpu_json,*mem_json,*gpu_json,*gpum_json,*detail;
	char *json_print,*find_end;
	char ret_buf[1024]={0};
	string str;
	float cpu_float,mem_float,gpu_float,gpum_float;
	bool ret = false;

/*组合查询报文*/
	root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"cmd","detail");
	cJSON_AddStringToObject(root,"serialno","hello");
	json_print = cJSON_PrintUnformatted(root);
	str = json_print;
	str += "XXEE";
	free(json_print);
	json_print = NULL;
	cJSON_Delete(root);

/*发送并接受消息*/
	if(!send_Data(str.c_str(),str.size(),ret_buf,1023,"127.0.0.1",(atoi(Rsm_Config::instance()->rsm_aim_listentport.c_str())+1)))
		return false;
	find_end = strstr(ret_buf,"XXEE");
	if(find_end)
		find_end = '\0';
	else
		return false;
/*处理返回的信息，并储存*/
	do
	{
		ret_root = cJSON_Parse(ret_buf);
		if(0 == ret_root)
			break;
		detail = cJSON_GetObjectItem(ret_root,"detail");
		if(0 == detail)
			break;
		cpu_json = cJSON_GetObjectItem(detail,"cpu");
		if(0 == cpu_json)
			break;
		cpu_float = atof(cpu_json->valuestring);
		while(cpu_list.size()>=cpu_set_count)
			cpu_list.pop_front();
		cpu_list.push_back(cpu_float);
		
		mem_json = cJSON_GetObjectItem(detail,"mem");
		if(0 == mem_json)
			break;
		mem_float= atof(mem_json->valuestring);
		while(mem_list.size()>=mem_set_count)
			mem_list.pop_front();
		mem_list.push_back(mem_float);

		gpu_json = cJSON_GetObjectItem(detail,"gpu");
		if(0 == gpu_json)
			break;
		gpu_float = atof(gpu_json->valuestring);
		while(gpu_list.size()>=gpu_set_count)
			gpu_list.pop_front();
		gpu_list.push_back(gpu_float);


		gpum_json= cJSON_GetObjectItem(detail,"gpu_mem");
		if(0 == gpum_json)
			break;
		gpum_float= atof(gpum_json->valuestring);
		while(gpum_list.size()>=gpum_set_count)
			gpum_list.pop_front();
		gpum_list.push_back(gpum_float);
	
		ret = true;
	}while(0);
	cJSON_Delete(ret_root);
	return ret;
	
}
int check_server::send_status(int error)
{
	char buf[1024]={0};
	char ret_buf[1024]={0};
	cJSON_Create(buf,error);
	send_Data(buf,1023,ret_buf,1023,Rsm_Config::instance()->aim_ip.c_str(),Rsm_Config::instance()->aim_port);
	if(error)
	{
		RES_register::instance()->error = true;
		RES_oprate::instance()->error = true;
	}else
	{
		RES_register::instance()->error = false;
		RES_oprate::instance()->error = false;
	}return 0;
}



int check_server::cJSON_Create(char buf[1024],int error)
{
	cJSON* root;
	char* JSON_ret;
	char rate[8]={0};
	char freeNo[8]={0};
	char streamNo[8]={0};
	char totolNo[8]={0};
	char mgmtport[8]={0};
	int num_hd_free,num_sd_free,num_totol,num_stream;
	
	const char* serialno = "HELLO";
	root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"cmd","vncalive");
	cJSON_AddStringToObject(root,"areaid",Rsm_Config::instance()->area_id.c_str());
	cJSON_AddStringToObject(root,"videotype",Rsm_Config::instance()->video_type.c_str());
	cJSON_AddStringToObject(root,"vnctype",Rsm_Config::instance()->ctype.c_str());
	cJSON_AddStringToObject(root,"vendor",Rsm_Config::instance()->vendor.c_str());

	VNC_State::instance()->vnc_check(num_hd_free,num_sd_free);
	num_stream = Rsm_Session::instance()->checkUser();
	if(error)
	{		
		num_totol = VNC_State::instance()->totol - num_hd_free;
		num_hd_free = 0;
	}
	else
	{
		num_totol = VNC_State::instance()->totol;
	}
			
	sprintf(freeNo,"%d",num_hd_free);
	cJSON_AddStringToObject(root,"freenum",freeNo);
	sprintf(streamNo,"%d",num_stream);
	cJSON_AddStringToObject(root,"streamnum",streamNo);
	
	sprintf(totolNo,"%d",num_totol);
	cJSON_AddStringToObject(root,"totalnum",totolNo);

	sprintf(rate,"%d",Rsm_Config::instance()->vid_rate * 1024);
	cJSON_AddStringToObject(root,"rate",rate);
	cJSON_AddStringToObject(root,"vncip",Rsm_Config::instance()->rsm_ip.c_str());
	cJSON_AddStringToObject(root,"vncport",Rsm_Config::instance()->rsm_aim_listentport.c_str());
	sprintf(mgmtport,"%d",(atoi(Rsm_Config::instance()->rsm_aim_listentport.c_str())+1));
	cJSON_AddStringToObject(root,"mgmtport",mgmtport);

	cJSON_AddStringToObject(root,"serialno",serialno);

	JSON_ret = cJSON_PrintUnformatted(root);
	sprintf(buf,"%sXXEE",JSON_ret);
	free(JSON_ret);
	cJSON_Delete(root);
	return 0;
}


bool check_server::send_Data(const char * buf,int length,char* ret_buf,int ret_len,const char *ip,int port)
{	 
	Socket data_sock;
	int reqs;
	if(!Socket_Connector::connect(data_sock,ip, port))
	{
		LOG_WARN_FORMAT("Warning :socket connnect error: %s:%d \n",ip, port);
		return false;
	}
	if(length!=data_sock.write(buf,length))
	{
		LOG_WARN_FORMAT("Warning :socket send data error: %s:%d \n",ip, port);
		return false;
	}
	LOG_DEBUG_FORMAT("INFO  - [RSM]:tcp send [%s:%d] thread=%lu socketid=%d %s \n",Rsm_Config::instance()->aim_ip.c_str(),Rsm_Config::instance()->aim_port,pthread_self(),data_sock.getHandle(),buf);
	data_sock.read(ret_buf,ret_len,1,5);
	LOG_DEBUG_FORMAT("INFO  - [RSM]:tcp recv [%s:%d] thread=%lu socketid=%d %s\n",Rsm_Config::instance()->aim_ip.c_str(),Rsm_Config::instance()->aim_port,pthread_self(),data_sock.getHandle(),ret_buf);
	data_sock.close();
	return true;
}	 


