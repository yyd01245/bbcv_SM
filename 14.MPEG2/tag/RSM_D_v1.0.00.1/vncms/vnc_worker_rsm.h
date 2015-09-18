#ifndef	VNC_WORKER_RSM_H_
#define	VNC_WORKER_RSM_H_

#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "vnc_config.h"
#include "vnc_worker_Xvnc.h"
#include <sys/wait.h>
#include <sys/time.h>
#include <unistd.h>
#include "ppmtest.h"


using namespace std;
/*
	因为示例,此处假设网关均采用短连接,且采用字符串方式！
	1、从socket读取数据
	2、解析数据，判断数据正确性
	3、按请求类型进行处理
	   3.1 网关注册：XXBBCOH_REGISTER_REQ|GW_TYPE|GW_IP|GW_IP|GW_PORTXXEE 回复：XXBBCOH_REGISTER_RSP|RETURN_CODEXXEE
	   3.2 用户登录：XXBBCOC_TERM_LOGIN_REQ|TERM_TYPE|TERM_ID|REGION_ID|CALLBACK_IP|CALLBACK_PORTXXEE 回复：XXBBCOH_REGISTER_RSP|RETURN_CODEXXEE
	   3.2 用户退出：XXBBCOC_TERM_LOGOUT_REQ|TERM_TYPE|TERM_ID|REGION_IDXXEE 回复：XXBBCOH_REGISTER_RSP|RETURN_CODEXXEE
	4、返回框架，因为是短连接，则返回-1，要求框架关闭socket
	
	发往cscs的报文格式暂时为：
	XXBBCOC_TERM_LOGIN_REQ|TERM_ID|TERM_TYPE|REGION_IDXXEE
	XXBBCOC_TERM_LOGOUT_REQ|TERM_ID|TERM_TYPE|REGION_IDXXEE
*/

class Prase_Packet
{
public:
	string cmd;
	string session_id;
	int time_out;
	string url;
	int size;

	bool init_para(char *str,const char * flag);
	bool Set_Para(int i,char * data);

};

class Rsm_AiM_Handler : public Socket_Svc_Handler
{
public:
	 FILE 	*fp;
	 int    handle_process();	 //逻辑主函数	
 
protected:
	 bool   createVnc(const Prase_Packet &p_Element,string &ret_string);
	 bool   freeVnc(const Prase_Packet &p_Element,string &ret_string);
	 
};


typedef Socket_Ractor<Socket_Selector,Rsm_AiM_Handler>  Rsm_AiM_Worker;

typedef Singleton<Rsm_AiM_Worker>  Vnc_sngton;

#endif 



