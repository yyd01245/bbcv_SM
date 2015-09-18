#include "rsm_config.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <net/if.h>
#include <netinet/in.h>
#include <linux/sockios.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <string.h>


int rsm_config::GetHostIp(const char *name,char *addr)
{
	int ret =-1;
	int inet_sock;
	struct ifreq ifr;
	inet_sock = socket(AF_INET, SOCK_DGRAM, 0);
	strcpy(ifr.ifr_name, name);
	if (ioctl(inet_sock, SIOCGIFADDR, &ifr) < 0)
	{
		perror("ioctl");
		return ret;
	}
	sprintf(addr,"%s", inet_ntoa(((struct sockaddr_in*)&(ifr.ifr_addr))->sin_addr));
	return 0;
}


bool rsm_config::init(const char * ConfigFile)
{
	char ip[64];
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false)  return false;
	
	rsm_version = "V1.3.00.6";
	area_id = cfg.getValue("RSM.AREA_ID");
	video_type = cfg.getValue("RSM.VIDEO_TYPE");
	ctype = cfg.getValue("RSM.C_TYPE");
	vendor = cfg.getValue("RSM.VENDOR");
	rsm_netname = cfg.getValue("RSM.NETNAME");
	rsm_encodername = cfg.getValue("RSM.ENCODERNAME");
	GetHostIp(rsm_netname.c_str(),ip);
	rsm_ip = ip;
	rsm_aim_listentport = cfg.getValue("RSM.CRSM_LISTENTPORT");
	rsm_vnc_listentport = atoi(cfg.getValue("RSM.VNC_LISTENTPORT").c_str());
   
	aim_ip = cfg.getValue("CRSM.IP");
	aim_port = atoi(cfg.getValue("CRSM.PORT").c_str());
	
	vid_quota = cfg.getValue("RSM.VID.QUOTA");
	height = cfg.getValue("RSM.HEIGHT");
	width = cfg.getValue("RSM.WIDTH");
	num_hd = cfg.getValue("RSM.NUM.HD");
	num_sd = cfg.getValue("RSM.NUM.SD");
	has_audio = atoi(cfg.getValue("RSM.AUDIO").c_str());
	gop_size = cfg.getValue("RSM.GOP.SIZE");
	vid_rate = atoi(cfg.getValue("RSM.VID.RATE").c_str());
	service_id = cfg.getValue("RSM.SERVICEID");
	core_No = atoi(cfg.getValue("RSM.CORE.NUMBER").c_str());
	core_sum = atoi(cfg.getValue("RSM.CORE.SUM").c_str());
	pmt_pid = cfg.getValue("RSM.PMT.PID");
	pmt_vid = cfg.getValue("RSM.PMT.VID");
	pmt_aid = cfg.getValue("RSM.PMT.AID");

	pre_web = cfg.getValue("RSM.PRE.WEB");
	pre_amount = atoi(cfg.getValue("VNC.PRE.AMOUNT").c_str());

	cpu_set_count = atoi(cfg.getValue("CRSM.CPU.COUNT").c_str());
	mem_set_count = atoi(cfg.getValue("CRSM.MEM.COUNT").c_str());
	gpu_set_count = atoi(cfg.getValue("CRSM.GPU.COUNT").c_str());
	gpum_set_count = atoi(cfg.getValue("CRSM.GPUMEM.COUNT").c_str());
	check_interval = atoi(cfg.getValue("CRSM.CHECK.INTERVAL").c_str());
	cpu_usage_upper = atof(cfg.getValue("CRSM.CPU.USAGE.UPPER").c_str());
	cpu_usage_lower = atof(cfg.getValue("CRSM.CPU.USAGE.LOWER").c_str());
	mem_usage_upper = atof(cfg.getValue("CRSM.MEM.USAGE.UPPER").c_str());
	mem_usage_lower = atof(cfg.getValue("CRSM.MEM.USAGE.LOWER").c_str());
	gpu_usage_upper = atof(cfg.getValue("CRSM.GPU.USAGE.UPPER").c_str());
	gpu_usage_lower = atof(cfg.getValue("CRSM.GPU.USAGE.LOWER").c_str());
	gpum_usage_upper = atof(cfg.getValue("CRSM.GPUMEM.USAGE.UPPER").c_str());
	gpum_usage_lower = atof(cfg.getValue("CRSM.GPUMEM.USAGE.LOWER").c_str());


	vncserverport = atoi(cfg.getValue("RSM.VNCSERVER.PORT").c_str());
	keylistenport = atoi(cfg.getValue("RSM.KEYLISTEN.PORT").c_str());
	pa_amount = atoi(cfg.getValue("RSM.PA.AMOUNT").c_str());
	uid = atoi(cfg.getValue("RSM.UID").c_str());
	vncport = atoi(cfg.getValue("VNC.PORT").c_str());
   	
	log_file_path = cfg.getValue("RSM.LOG_FILE_PATH");
	log_file = cfg.getValue("RSM.LOG_FILE");
	log_level = atoi(cfg.getValue("RSM.LOG_LEVEL").c_str());
	log_size = atoi(cfg.getValue("RSM.LOG_SIZE").c_str());
	log_num = atoi(cfg.getValue("RSM.LOG_NUM").c_str());
   
	rsm_aim_process_thread = atoi(cfg.getValue("RSM.PROCESS_THREADS_AIM").c_str());
	rsm_vnc_process_thread = atoi(cfg.getValue("RSM.PROCESS_THREADS_VNC").c_str());

   
	cout<<endl<<"load config is  ----------------------"<<endl;
	cout<<"rsm_ip ="<<rsm_ip<<endl;
	cout<<"rsm_aim_listentport ="<<rsm_aim_listentport<<endl;
	cout<<"rsm_vnc_listentport ="<<rsm_vnc_listentport<<endl;
	cout<<"aim_ip ="<<aim_ip<<endl;
	cout<<"aim_port ="<<aim_port<<endl;
	cout<<"vid.quota ="<<vid_quota<<endl;
	cout<<"num_hd ="<<num_hd<<endl;
	cout<<"num_sd ="<<num_sd<<endl;
	cout<<"log_file_path ="<<log_file_path<<endl;
	cout<<"log_file ="<<log_file<<endl;
	cout<<"log_level ="<<log_level<<endl;
	cout<<"rsm_process_thread ="<<rsm_aim_process_thread<<endl;
	cout<<"pa_amount ="<<pa_amount<<endl;
	cout<<"uid ="<<uid<<endl;
	cout<<"load config over      ----------------------"<<endl;

	 return true;
}

