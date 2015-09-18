#ifndef	RSM_WORKER_UP_H_
#define	RSM_WORKER_UP_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "cJSON.h"

using namespace std;

class Rsm_Key_Handler : public Socket_Svc_Handler
{
public:
	int	handle_process();	
protected:
	bool	process_Key_Timeout(const Record &p_Element);
	bool	process_Session_Exist(const Record &p_Element);
	bool	send_Data(const char * buf,int length,char* ret_buf,int ret_len);
	int 	stop_encoder_system(int pid); 
};
   
//typedef Socket_Ractor<Socket_Epoller,Rsm_Key_Handler>  Rsm_Worker_Up;
typedef Socket_Ractor<Socket_Selector,Rsm_Key_Handler>  Rsm_Worker_Up;  
 

#endif 



