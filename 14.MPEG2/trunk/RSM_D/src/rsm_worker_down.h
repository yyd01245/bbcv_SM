#ifndef	RSM_WORKER_DOWN_H_
#define	RSM_WORKER_DOWN_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "cJSON.h"
#include <unistd.h>
#include <sys/time.h>

using namespace std;

class Rsm_Aim_Handler : public Socket_Svc_Handler
{
public:
	int    handle_process();	 
};
   
//typedef Socket_Ractor<Socket_Epoller,Rsm_Aim_Handler>  Rsm_Worker_Down;
typedef Socket_Ractor<Socket_Selector,Rsm_Aim_Handler>  Rsm_Worker_Down;  
 

#endif 



