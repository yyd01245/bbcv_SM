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

#include "vnc_config.h"

bool vnc_config::init(const char * ConfigFile)
{
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false)  return false;
	   
	//to do: 以下未做任何异常处理，请完成
	localhostip="127.0.0.1";
	version="V1.3.00.6";
	vncname=cfg.getValue("vncname");
	username=cfg.getValue("username");
	
	rsm_ip="127.0.0.1";
	use_proxy = atoi(cfg.getValue("use_proxy").c_str());
	if(use_proxy)
		proxy="--proxy-server="+cfg.getValue("proxy");
	else
		proxy="";
	audioname = cfg.getValue("audioname");
	sd_width=atoi(cfg.getValue("sd_width").c_str());
	sd_height=atoi(cfg.getValue("sd_height").c_str());
	hd_width=atoi(cfg.getValue("hd_width").c_str());
	hd_height=atoi(cfg.getValue("hd_height").c_str());
	chrome_type=atoi(cfg.getValue("chrome_type").c_str());
	TVnet=atoi(cfg.getValue("TVnet").c_str());

	vncserverport=atoi(cfg.getValue("vncserverport").c_str());
	keylistenport=atoi(cfg.getValue("keylistenport").c_str());
	recordadd = atoi(cfg.getValue("recordadd").c_str());
	key_ignore = atoi(cfg.getValue("key_ignore").c_str());
	timeoutforecast = atoi(cfg.getValue("timeoutforecast").c_str());
	//vncmport=atoi(cfg.getValue("vncmport").c_str());

	pa_amount = atoi(cfg.getValue("pa_amount").c_str());
	uid = atoi(cfg.getValue("uid").c_str());

	listenport=atoi(cfg.getValue("listenport").c_str());//listen to rsm
	keytimeoutport=atoi(cfg.getValue("keytimeoutport").c_str());

	log_file_path=cfg.getValue("log_file_path");
	log_file=cfg.getValue("log_file");
	log_level=atoi(cfg.getValue("log_level").c_str());

	process_threads_aim=atoi(cfg.getValue("process_threads_aim").c_str());
		  
/*
	cout<<endl<<"load config is  ----------------------"<<endl;
	cout<<"localhostip ="<<localhostip<<endl;
	cout<<"vncname ="<<vncname<<endl;  
	cout<<"sd_width ="<<sd_width<<endl;
	cout<<"sd_height ="<<sd_height<<endl;
	cout<<"hd_width ="<<hd_width<<endl;
	cout<<"hd_height ="<<hd_height<<endl;
	cout<<"vncserverport ="<<vncserverport<<endl;  
	cout<<"keylistenport ="<<keylistenport<<endl;
	cout<<"listenport ="<<listenport<<endl;

       cout<<"load config over      ----------------------"<<endl;
*/
	 return true;
}

