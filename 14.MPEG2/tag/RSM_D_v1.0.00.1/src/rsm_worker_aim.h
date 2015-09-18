#ifndef	RSM_WORKER_AIM_H_
#define	RSM_WORKER_AIM_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"

#include "cJSON.h"


class Rsm_Manager_Handler : public Socket_Svc_Handler
{
	public:
		int handle_process();
	protected:
		bool process_change_config();
		bool process_rsm_restart();
		bool process_chromeplugin_update();
		bool process_server_detail();
};

typedef Socket_Ractor<Socket_Selector,Rsm_Manager_Handler>  Rsm_Worker_Aim;  

typedef Singleton<Rsm_Worker_Aim>  Rsm_AiM_sngton;


#endif

