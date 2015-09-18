#ifndef	_VNC_CONFIG_H_
#define	_VNC_CONFIG_H_

#include "Singleton.h"
#include "PropConfig.h"

#define VNCCONFFILE "vncconfig.ini"
class vnc_config
{
public:
	bool   init(const char * ConfigFile);
public:
	string  localhostip;
	string  vncname;
	string 	rsm_ip;
	string 	proxy;
	string	version;
	string 	audioname;
	string  username;
	
	int 	use_proxy;

	int 	sd_width;
	int 	sd_height;
	int 	hd_width;
	int 	hd_height;
	int 	chrome_type;
	int 	TVnet;

	
	int 	vncserverport;
	int 	keylistenport;
	int   	recordadd;
	int   	listenport;
	int 	vncmport;
	int 	keytimeoutport;
	int		timeoutforecast;
	int 	key_ignore;

	int 	pa_amount;
	int 	uid;
		
	int		process_threads_aim;

	string 	log_file_path;
  	string 	log_file;
  	int  	log_level;
	
	 // int 	getip(char *ip);
};

typedef Singleton<vnc_config>  Vnc_Config;

#endif 



