#ifndef	_RSM_WORKER_RESOURCE_H_
#define	_RSM_WORKER_RESOURCE_H_

#include <string>
#include <queue>
//#include <pthread.h>
#include "Socket.h"
#include "DateTime.h"
#include "Singleton.h"
#include "Log.h"
#include "cJSON.h"


typedef struct
{
	struct timeval opt_start;
	char cmd[4096];
	int Sock;
}opt;


class resource_oprate: public  Thread_Base
{
	public:
		bool set_opt(opt *oprate);
		int resume_operate();
		bool error;

		int run()
 		{
 			static int i=0;
 			 i++;
 			 if(i==1)
 			 	 return operating();
	 		 return 0;
 	};
	protected:
		ThreadMutex queue_lock;
		queue<opt> opt_queue;
		string encoderpath;

		ThreadCondition opt_cond;


		bool get_opt(opt *oprate);
		
		int operating();
		int get_encoder_core(int index);
		int stop_encoder(int encoder_id);
		int start_vncms();
		int stop_vncms();
		int pause_vncms();
		int if_exist();
		
		bool	JSON_print(const char cmd[16],const char retcode[8],const char sessionid[128],
									int s2qport,const char keyip[24],int keyport,
									int hdfreenum,int sdfreenum,cJSON *serialno,string &ret_string);
		bool	process_App_Login(struct timeval start_time,cJSON *root,string &ret_string);
		bool	process_App_Logout(struct timeval start_time,cJSON *root,string &ret_string);
		bool	process_App_Session_Req(cJSON *root,string &ret_string);
		bool	process_App_Notice_Req(cJSON *root,string &ret_string);
		bool	process_App_Exist_Req(cJSON *root,string &ret_string);
		bool	try_get_value(cJSON *root, const char * key,string &value);
		bool	try_get_value(cJSON *root, const char * key,int &value);
	 
		int 	start_encoder_system(unsigned short vnc_port,int bitrate,char* dst_url);
	
		int 	stop_encoder_system(int pid);

};

typedef Singleton<resource_oprate>  RES_oprate;


#endif

