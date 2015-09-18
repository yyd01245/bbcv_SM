#ifndef	_RSM_CHECKER_SERVER_H_
#define	_RSM_CHECKER_SERVER_H_

#include <list>
#include <iostream>
#include "cJSON.h"
#include <unistd.h>
#include <sys/time.h>
#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "rsm_worker_vncms.h"


class check_server: public  Thread_Base
{
	public:

	int run()
	{
		return check_server_status();
	};

	protected:

	bool get_cpm_mem();//获取cpu和mem状态并储存

	int check_server_status();//检测服务器状态

	
	int send_status(int error);//服务器状态异常或恢复正常时发送，使用心跳接口

	
	int cJSON_Create(char buf[1024],int error);

	bool send_Data(const char * buf,int length,char* ret_buf,int ret_len,const char *ip,int port);

	bool check_error;

	typedef list<float> Data_List;

	Data_List cpu_list,mem_list,gpu_list,gpum_list;
	
	int cpu_set_count,mem_set_count,gpu_set_count,gpum_set_count;

	int check_interval;

	float cpu_usage_upper,cpu_usage_lower;

	float mem_usage_upper,mem_usage_lower;
	
	float gpu_usage_upper,gpu_usage_lower;

	float gpum_usage_upper,gpum_usage_lower;



	
};

typedef Singleton<check_server>  server_checker;


#endif

