#ifndef	_VNC_WORKER_XVNC_H_
#define	_VNC_WORKER_XVNC_H_

#include <string>
#include <map>
#include <pthread.h>
#include <unistd.h>
#include <fcntl.h>
#include "Socket.h"
#include "DateTime.h"
#include "Singleton.h"
#include "ppmtest.h"
#include "Log.h"
//#include "vnc_keygw.h"

using namespace std;


/*
  地址信息含义为：
  m_socket_id>0,则为长连接，否则为短连接.
*/

//struct AAAAA


/*
  存储ordergw的状态数据，包括：
  1、用户数据map，以type+term_id作为key,寻址信息作为value
  2、广播头端map,以type+region_id/area_id作为key,寻址信息作为value
  
  此部分后期由mdb代替
*/
/*
class vnc_Session_Imp
{
public:
	   vnc_Session_Imp(){};
	   ~vnc_Session_Imp();

		 bool addSession(string  session_id,const AAAAA &stru_session);	
		 bool delSession(string  session_id);	
		 bool getSession(string  session_id,AAAAA **stru_session);		  
protected:
		 map<string, AAAAA *> map_sessions;
 		 ThreadMutex  m_session_Mutex;
};

typedef Singleton<vnc_Session_Imp>  VNC_Session;
*/


class vnc_State_Imp: public  Thread_Base
{
public:
		#define READ 1
		#define WRITE 2

	int 	init_vnc(int index, int type, string &vgl_display);
	bool 	vnc_create(string sessionid,const int &time_out ,const string &url,const int sockid);
	bool 	vnc_free();
	bool   	stop_Timeout();
	bool	fresh_Timeout();
	int  	run()
 	{
 		static int i=0;
 		 i++;
 		 if(i==1)
 		 	 return forward_key();
 		 if(i==2)
 		 	 return checkTimeout();
		 if(i==3)
			 return wait_relase();
		 if(i==4)
		   return kill_chrome();

 		 return 0;
 	};
	
private:
	void 	set_fl(int fd, int flags);
	int 	mysystem( const char *web, int type,const int sockid);
	int		audiosystem(int index);
	int 	get_recordid(int index);
	int 	initLibclient();
	int 	initUDP();
	bool 	initvncclient();
	int 	get_key_value(BlcYunKeyIrrMsg* irrmsg);
	int		get_mouse_type(BlcYunKeyMouseMsg* mousemsg);
	int 	get_keyboard_value(BlcYunKeyKeyboardMsg* keboardmsg,int opt);
	int 	get_wheel_type(BlcYunKeyMouseMsg* mousemsg);
	int 	comb(BlcYunKeyIrrMsg* irrmsg);	
	int		forward_key();
	bool 	kill_chrome();
	bool	timeoutforecast();
	bool	ExistNotify();
	bool	TimeoutNotify();
	int 	checkTimeout();
	int 	wait_relase();
	int 	get_vnc_port_index(int index);
	int 	copy_chrome_data();	
	void	restartXnvc(int re_pid);
	int		operate_Xvnc_path(int operate,int pid);
	
	unsigned long 	timeout;
	unsigned int	existcheck;
	struct timeval 	last_key_time;
	struct timeval 	login_time;
	ThreadMutex clean_up_lock;
	int		pgid;
	
	char 	vnc_addr[64];	
	int 	status;
	int		vid_type;
	int 	vnc_port;
	int 	vnc_index;
	int		bvbcm_index;	
	int 	pa_amount;
	int 	keylistenport;
	int 	pid;//chrome pid
	int		aid;//audio pid
	char 	Xvnc_state_path[32];
	int 	vid;//Xvnc pid
	char	sessionid[64];
	string	vgl_index;
	string 	vnc_url;
	string	audiopath;//音频编码进程路径
	int 	sock_fd;
	rfbClient* vnc_client;
	
	
};

typedef Singleton<vnc_State_Imp>  VNC_State;


#endif 



