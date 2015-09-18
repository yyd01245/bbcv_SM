#ifndef _RES_REGISTER_H_
#define _RES_REGISTER_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "rsm_worker_vncms.h"
#include "cJSON.h"
#include "rsm_store.h"

using namespace std;

class Res_Register : public  Thread_Base
{
public:
	int	timeout;
	bool error; // 检测服务器cpu mem 正常与否，不正常则上报freenum 0 
 	int  run()
		{
			LOG_INFO_FORMAT("INFO: Res_Register is begin runing,Thread id is %u!\n",pthread_self());
			time_t start, end;
			Socket sock;			
			string	cmd;
			int	rc;
			int	beat =10;
			char buf[1024];	
			char aim_reqs[1024];
			bool b_register;			
			int reqs;
			cJSON* ret_root;
			cJSON* tmp_JSON;
			
			timeout =10;
			error = false;
			b_register = false;
			time(&start);
			while(true)
			{
				time(&end);
				if (end - start > beat - 1)
				{
				if(Socket_Connector::connect(sock, Rsm_Config::instance()->aim_ip.c_str(), Rsm_Config::instance()->aim_port))
				{
					if(!b_register)
					{
						cJSON_Create(buf,"vncregister");
					}
					else
					{
						cJSON_Create(buf,"vncalive");
					}
					LOG_INFO_FORMAT("INFO - [RSM] :tcp send [%s:%d] %s\n",
						 					Rsm_Config::instance()->aim_ip.c_str(),
						 					Rsm_Config::instance()->aim_port,buf);
					rc=sock.write(buf,strlen(buf));
					time(&start);
					memset(aim_reqs,0,strlen(aim_reqs));
	 	 	 		rc=sock.read(aim_reqs,1024,1,1);
					LOG_INFO_FORMAT("INFO - [RSM] :tcp recv [%s:%d] %s\n",
									Rsm_Config::instance()->aim_ip.c_str(),
						 			Rsm_Config::instance()->aim_port,aim_reqs);
	 	 	 		if (rc >0)
	 	 	 		{
						do
						{
							if(!strstr(aim_reqs,"XXEE")) 
							break;
							Pubc::replace(aim_reqs,"XXEE","");	
							ret_root = cJSON_Parse(aim_reqs);
							if(!ret_root)
								break;
							tmp_JSON = cJSON_GetObjectItem(ret_root,"retcode");
							if(!tmp_JSON)
								break;
							if(atoi(cJSON_GetObjectItem(ret_root,"retcode")->valuestring))
								break;
							tmp_JSON = cJSON_GetObjectItem(ret_root,"overtime");
							if(!tmp_JSON)
								break;
							VNC_State::instance()->set_timeout(atoi(cJSON_GetObjectItem(ret_root,"overtime")->valuestring));
							if(!b_register)
							{
								beat = atoi(cJSON_GetObjectItem(ret_root,"hearttime")->valuestring);
								b_register = true;
								LOG_INFO("INFO - [RSM] register 2 CRSM success \n");
							}
							}while(0);
						cJSON_Delete(ret_root);
	 	 	 		}
					sock.close();
				sleep(beat-5);
				}
			else
			{
				sleep(2);
				LOG_ERROR("ERROR - [RSM] [-1802] connect to CRSM failed \n");
			}
		} 
		usleep(10);
	}
	return 0;
};

int cJSON_Create(char buf[1024],const char cmd[8]);


};

typedef Singleton<Res_Register>  RES_register;


#endif


