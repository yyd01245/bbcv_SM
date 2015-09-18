#ifndef _RES_SEND_H_
#define _RES_SEND_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "rsm_worker_vncms.h"

using namespace std;

class Res_Send : public  Thread_Base
{
public:
 	int  run()
		{
			LOG_INFO_FORMAT("INFO: Res_Send is begin runing,Thread id is %u!\n",pthread_self());
			Socket sock;
			char buf[1024];
			int num_hd_free,num_sd_free;
			int num_online;
			map<string, Rsm_Session_Msg>::iterator iter;
			Rsm_Session_Msg tmp_msg;	
			while(true)
			{
				if(Socket_Connector::connect(sock, Rsm_Config::instance()->vncw_ip.c_str(), Rsm_Config::instance()->vncw_port))
				{
					LOG_INFO_FORMAT("connetct to VNCW %s sucess \n",Rsm_Config::instance()->vncw_ip.c_str());
					for(iter = Rsm_Session::instance()->map_session.begin(); iter != Rsm_Session::instance()->map_session.end();iter++)
                    {
                                		tmp_msg = iter->second;
						VNC_State::instance()->vnc_check(num_hd_free,num_sd_free);
						num_online = atoi(Rsm_Config::instance()->num_hd.c_str()) - num_hd_free;
						int encoder_id = tmp_msg.encoder_id;
						xsdk_encoder_status_t status;
						xsdk_encoder_status(encoder_id,&status);
						sprintf(buf,"XXBBSTATUS_REQ|%d|%d|%d|%s|%d|%d|%s:%d|%sXXEE",
                                        atoi(Rsm_Config::instance()->num_hd.c_str()),
										num_online,
										num_hd_free,
										tmp_msg.session_id.c_str(),
										tmp_msg.vnc_add,
										status.venc_output_bitrate,	
										tmp_msg.ipqam_ip.c_str(),
										tmp_msg.ipqam_port,
                                        Rsm_Config::instance()->rsm_version.c_str());
						LOG_INFO_FORMAT("send to VNCEW =:%s \n",buf);
						sock.write(buf,strlen(buf));
						usleep(10);
			 		}
				}
				else 
				{
					LOG_WARN_FORMAT("connetct to VNCW %s fail \n",Rsm_Config::instance()->vncw_ip.c_str());
				}
				sleep(60);
			}
		return 0;
		};

};

#endif


