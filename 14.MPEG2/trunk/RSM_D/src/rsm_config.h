#ifndef	_RSM_CONFIG_H_
#define	_RSM_CONFIG_H_

#include <stdlib.h>
#include "Singleton.h"
#include "PropConfig.h"

class rsm_config
{
public:
	bool	init(const char * ConfigFile);
	int 	GetHostIp(const char *name,char *addr);
public:
	string rsm_version;
	string area_id;
	string video_type;
	string vendor;
	string ctype;
	string rsm_ip;
	string rsm_netname;
	string rsm_encodername;
	string rsm_aim_listentport; //local sourcemag ip:port
	int    rsm_vnc_listentport; //local sourcemag ip:port
	
	int vncserverport;
	int keylistenport;
	int vncport;
	int pa_amount;
	int uid;

	string aim_ip;	          
	int    aim_port;
	string vncw_ip;	          
	int    vncw_port;
	
	string vid_quota;
	string height;
	string width;
	string num_hd;
	string num_sd;
	int has_audio;
	string gop_size;
	int vid_rate;
	string service_id;
	int core_No;
	int core_sum;
	string pmt_pid;
	string pmt_vid;
	string pmt_aid;

	string pre_web;
	int pre_amount;
	/*服务器检测参数*/
	int cpu_set_count,mem_set_count,gpu_set_count,gpum_set_count;

	int check_interval;

	float cpu_usage_upper,cpu_usage_lower;

	float mem_usage_upper,mem_usage_lower;
	float gpu_usage_upper,gpu_usage_lower;

	float gpum_usage_upper,gpum_usage_lower;


/*服务器检测参数*/
	
	string log_file_path;
	string log_file;
	int log_level; // log level [ERROR   = 1;WARN    = 2;INFO    = 3;DEBUG   = 4;TRACE   = 5; ]
	int log_size;
	int log_num;
	int rsm_aim_process_thread;
	int rsm_vnc_process_thread;

};

typedef Singleton<rsm_config>  Rsm_Config;


#endif 
