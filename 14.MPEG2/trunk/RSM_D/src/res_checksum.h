#ifndef _RES_CHECK_H_
#define _RES_CHECK_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
extern "C"
{
#include "xencoder_sdk.h"
}
#include "rsm_worker_down.h"
#include "rsm_store.h"

using namespace std;

class Res_Checker : public  Thread_Base
{
public:
 	int  run()
		{
			LOG_INFO_FORMAT("INFO :Res_Checker is begin runing,Thread id is %u!\n",pthread_self());
			int rc=0;
			int encoder_id;
			string session_id;
			Socket sock;
			char buf[128];
			while(true) 
			{
				xsdk_event_msg_t event;
				if(!xsdk_read_event(&event))
				{
					encoder_id = event.encoder_id;
					session_id = Rsm_Session::instance()->map_encoder[encoder_id].session_id;
					if (event.type == XSDK_EV_XVNC_HUP) {
						LOG_INFO_FORMAT("catch exception: vnc connect fail %s\n", session_id.c_str());
					}
					else if (event.type == XSDK_EV_PA_HUP) {
						LOG_INFO_FORMAT("catch exception: palseaudio connect fail %s\n", session_id.c_str());
					}
					if (event.type == XSDK_EV_DSP_ERROR) {
						LOG_INFO_FORMAT("catch exception: encoder fail %s\n", session_id.c_str());
					}
					if(Socket_Connector::connect(sock, Rsm_Config::instance()->aim_ip.c_str(), Rsm_Config::instance()->aim_port))
					{
						sprintf(buf,"XXBBAPP_QUIT_REQ|%sXXEE",session_id.c_str());
						LOG_INFO_FORMAT("send to AIM %s\n", buf);
                        			rc=sock.write(buf,strlen(buf));
					}
					
				} 
				sleep(10);
			}
		return 0;
		};

};

#endif


