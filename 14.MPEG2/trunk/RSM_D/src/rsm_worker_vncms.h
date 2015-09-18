#ifndef	_RSM_WORKER_VNCMS_H_
#define	_RSM_WORKER_VNCMS_H_

#include <string>
#include <map>
#include <list>
#include <set>

#include <pthread.h>
#include <limits.h> 
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>

#include "Socket_Ractor.h"
#include "Record.h"
#include "Socket.h"
#include "DateTime.h"
#include "Singleton.h"
#include "Log.h"
#include "cJSON.h"

//#include "vnc_config.h"

using namespace std;

#define	MAX_TOTALL_NUM	512 //inlcude max_hd_num and max_sd_num
#define	SD	1 //vid_type:sd
#define	HD	2 //vid_type:hd
#define RSMNOLOG false
#define RSMLOG true

class http_server: public Socket_Svc_Handler
{
	public:

	int handle_process();	
	
	int	open_chrome(int vncport,string &url);

	private:

		bool prase_httpd(char *http,string& url);
		
};
typedef Socket_Ractor<Socket_Selector,http_server>  LOGIN_SERVER;
typedef Singleton<LOGIN_SERVER>  LOGIN_SERVER_sngton;

class vnc_Pre_Process: public  Thread_Base
{
//该类只支持单线程调用
	public:
/*需要实现2个线程，1.监测预启动的chrome状态是否正常，2.补足预启动数量*/
	int run();

	int init();

	int add_pre_open(int index);//使用前需要检查是否预启动chrome线程是否正在执行-----  使用is_starting()函数检查

	int get_pre_open_amount();
	
		
	int login_user_add(const string &sessionid,const string &url,int over_p);
	int login_user_check(int index,string &url);
	int login_user_delete(int index);

	private:

	int loginning_num;
	int logined_num;

	int starting_chrome;//在同一时间有且仅有1个启动chrome线程存在

	int m_beginning,m_end;
	typedef struct {
		timeval last_connect;
		bool inuse;
		bool is_register;
		ThreadMutex user_lock;
	}PRE_CHROME;
	typedef queue<int> OPEN_QUEUE;
	OPEN_QUEUE pre_open_queue;	//可用预启动队列
	OPEN_QUEUE pre_opened_queue;//预启动完成的队列
	typedef map<int,PRE_CHROME>		PRE_OPEN_MAP;
	PRE_CHROME pre_open_map[MAX_TOTALL_NUM];//预启动成功表
	typedef set<int> PRE_SET;
	PRE_SET pre_openning_set;//预启动中池
	PRE_SET pre_opened_set;//预启动成功池
	typedef map<int,std::string>	LOGIN_MAP;
	LOGIN_MAP login_user_map;//用户预登陆表

	ThreadMutex login_user_lock;
	ThreadMutex add_user_lock;

	
	int check_chrome();

	int start_chrome();
	
	int send_vncms_msg(const string &app_id,const int &over_p,const string &web_url,int index,int oprate);

};

typedef Singleton<vnc_Pre_Process>  Pre_Process;


class Send_Info_To_Vncms
{
	public:
		Send_Info_To_Vncms(){};
		static	bool 	send_info(int index,const string &msg,bool iflog);
};

class vnc_State_Imp 
{
public:
	int 	timeout;
	
	int 	totol;

	int		get_vncms_index(int vncmspid);
	int 	get_encoder_core(int index);
	
	void	set_timeout(int time){timeout = time;};	
	
	int		vnc_init(int max_hd_num, int max_sd_num);

	int 	pre_open_init();

	int		vncms_restart(int index);
	
	int 	change_resouse(int type);
	
	int 	check_free(int index);
	
	bool 	vnc_create(int vid_type, const string app_id ,const string web_url, int over_p,
		                      int &vnc_add, int &key_add, int &record_add, int &system_userid);

	bool 	vnc_relsease(const string &app_id);
	
	bool 	vnc_check(int &num_hd_free, int &num_sd_free);
	
	bool 	vnc_stop_timeout(const string &app_id);
	
	
	
private:
	//#define create_chrome 	1
	//#define free_chrome 	0


   
 	typedef map<std::string,int> 	USER_MAP;
	typedef map<int,int>			BAD_MAP;
	typedef list<int>				LIST;
	typedef queue<int>				QUEUE;
	typedef set<int>				SET;
	
	
	USER_MAP 					m_usermap;//正常 使用中的资源池
	BAD_MAP						bad_usermap;//出错用户的资源池
	QUEUE 						hd_count;//高清可用资源队列
	QUEUE 						sd_count;//标清可用资源队列
	SET							free_count;//备用资源队列

	int 						pa_amount;//音频序号,即SINK_NAME
	int 						vncms_pid[MAX_TOTALL_NUM]; //both hd and sd
	int 						vid_type[MAX_TOTALL_NUM];//both hd and sd
	
	ThreadMutex 				m_mutex_thread;
	

	int 	get_recordid(int index);
	int		start_vncms(int num,char *type);
	int 	check_vncms();

	bool	checksession(string id, USER_MAP::iterator &iter);
	bool	checksession(string &sessionid,int i);
	void 	set_fl(int fd, int flags);

	int 	check_info(int index,const string &msg);
	int 	get_vnc_port_index(int index);
	int 	set_value(int &vnc_add,int &key_add,int &record_add,int &system_userid,int index);
	int		get_index(QUEUE *queue);
	
		
};

typedef Singleton<vnc_State_Imp>  VNC_State;



class vnc_Check_Imp: public  Thread_Base
{
	public:

	int 	run()
		{
			static int i=0;
			 i++;
			 if(i==1)
				 return check_vncms();
			
			 return 0;
		};	

	int c_totol;//控制检测是否进程，以避免与修改承载路数冲突
	
	private:

	int check_vncms();
	
};

typedef Singleton<vnc_Check_Imp>  VNC_Check;


#endif 



