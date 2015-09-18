#include "rsm_worker_resource.h"
#include "Socket_Ractor.h"
#include "Record.h"
#include "Log.h"
#include "rsm_config.h"
#include "rsm_worker_vncms.h"
#include "rsm_store.h"
#include "env_check.h"
#include <unistd.h>
#include <sys/time.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#include <iostream>




bool resource_oprate::set_opt(opt *oprate)
{
//	cout<<"set opt !!"<<endl;
	ThreadLocker locker(queue_lock);
	opt_queue.push(*oprate);
}

bool resource_oprate::get_opt(opt *oprate)
{
//	cout<<"get opt !!"<<endl;
	ThreadLocker locker(queue_lock);
	memcpy(oprate,&(opt_queue.front()),sizeof(opt));
	opt_queue.pop();
	return true;
}

int resource_oprate::resume_operate()
{
	opt_cond.signal();
	return 0;
}

int resource_oprate::operating()
{
	opt opretion;
	struct timeval now;
	char *cmd;
	string ret_string;
	cJSON *root,*cmd_json;
	char Xvnc_amount[8];
	int ret;
	
	encoderpath = Rsm_Config::instance()->rsm_encodername;
	error = false;
	
	while(1)
	{
		if(0 >= opt_queue.size())// 这种做法，会在size = 0 时 多一次循环，才进入wait状态
			 opt_cond.wait();
		
		memset(&opretion,0,sizeof(opt));
		get_opt(&opretion);
		Socket Sock;
//		cout<<"######cmd:"<<opretion.cmd<<"socket_id:"<<opretion.Sock<<endl;
		Sock.setHandle(opretion.Sock);
		root = cJSON_Parse(opretion.cmd);
		
		if(!root)
		{
			ret_string = "{}XXEE";
			Sock.write(ret_string.c_str(),ret_string.size());
			Sock.close();
			cJSON_Delete(root);
			continue;
		}
		cmd_json = cJSON_GetObjectItem(root,"cmd");
		if(!cmd_json)
		{
			ret_string = "{}XXEE";
			Sock.write(ret_string.c_str(),ret_string.size());
			Sock.close();
			cJSON_Delete(root);
			continue;
		}
		cmd = cmd_json->valuestring;
/*暂时性放在这里处理，需要另起线程处理RSMW*/
		cJSON *tmp_ret;
		char *tmp_print;
		
		if(!strcmp(cmd,"chgcfg"))
		{
			Rsm_Config::instance()->init("resourcemag.config");
			
			tmp_ret = cJSON_CreateObject();

			cJSON_AddStringToObject(tmp_ret,"cmd",cmd);
			if(VNC_State::instance()->change_resouse(HD))
				cJSON_AddStringToObject(tmp_ret,"retcode","-1841");
			else
				cJSON_AddStringToObject(tmp_ret,"retcode","0");
			cJSON_AddStringToObject(tmp_ret,"serialno",cJSON_GetObjectItem(root,"serialno")->valuestring);

			tmp_print = cJSON_PrintUnformatted(tmp_ret);
			ret_string = tmp_print;
			ret_string+= "XXEE";
			cJSON_Delete(tmp_ret);
			free(tmp_print);
			tmp_print=NULL;

			Sock.write(ret_string.c_str(),ret_string.size(),1);	
			Sock.close();
			LOG_INFO_FORMAT("INFO  - [RSM]: tcp send %s \n",ret_string.c_str());
/*			while(atoi(Rsm_Config::instance()->num_hd.c_str()) != atoi(Xvnc_amount))
			{
				LOG_INFO_FORMAT("INFO  - [RSM]: %d Xvnc is ready, please wait for 30s to startup all Xvnc \n",atoi(Xvnc_amount));
				sleep(20);
				memset(Xvnc_amount,0,8);
				ret = EnvCheck::MyPopen(Xvnc_amount,8,"ps -ef|grep Xvnc |grep -v grep|wc -l");
			}
			
			LOG_INFO("INFO	- [RSM]: all Xvnc is ready\n");
*/			
			continue;

		}
		
/*****************************************************************/

		cmd = cmd+7;
		bool b_ret=false;
		switch(*cmd)
		{
			case 'n': //login
			{
				b_ret = process_App_Login(opretion.opt_start,root,ret_string);
				break;
			}
			case 'u': //logout
			{
				b_ret = process_App_Logout(opretion.opt_start,root,ret_string);
				break;
			}
			case 'e': //pause
			{
				b_ret = process_App_Notice_Req(root,ret_string);
				break;
			}
			case 't': //exist
			{
				b_ret = process_App_Exist_Req(root,ret_string);
				break;
			}
			default :
				ret_string="{}XXEE";
			LOG_ERROR_FORMAT("ERROR - [RSM] [-1804]: ReqType is unknown %s\n",opretion.cmd);
		}
		
		Sock.write(ret_string.c_str(),ret_string.size(),1);
		LOG_INFO_FORMAT("INFO  - [RSM]: tcp send %s \n",ret_string.c_str());
//		if(!b_ret)
			Sock.close();
		cJSON_Delete(root);
	}
}


bool resource_oprate::try_get_value(cJSON *root, const char * key,string &value)
{
	cJSON* JSON_value;

	JSON_value = cJSON_GetObjectItem(root,key);
	if(!JSON_value) return false;
	value = JSON_value->valuestring;
	return true;
}

bool resource_oprate::try_get_value(cJSON *root,const char * key,int &value)
{
	string tmp_value;
	if(!try_get_value(root,key,tmp_value)) return false;
	value = atoi(tmp_value.c_str());
	return true;
}

bool resource_oprate::JSON_print(const char cmd[16],const char retcode[8],const char sessionid[128],
									int s2qport,const char keyip[24],int keyport,
									int hdfreenum,int sdfreenum,cJSON *serialno,string &ret_string)
{
	char JSON_s2qport[8];
	char JSON_keyport[8];
	char JSON_hdfreenum[8];
	char JSON_sdfreenum[8];
	char JSON_ret_string[1024];
	cJSON* ret_root;
	char* JSON_print;

	sprintf(JSON_s2qport,"%d",s2qport);
	sprintf(JSON_keyport,"%d",keyport);
	sprintf(JSON_hdfreenum,"%d",hdfreenum);
	sprintf(JSON_sdfreenum,"%d",sdfreenum);
	ret_root = cJSON_CreateObject();
	cJSON_AddStringToObject(ret_root,"cmd",cmd);
	cJSON_AddStringToObject(ret_root,"retcode",retcode);
	if(sessionid)
		cJSON_AddStringToObject(ret_root,"resid",sessionid);
	if(-1 != s2qport)
		cJSON_AddStringToObject(ret_root,"s2qport",JSON_s2qport);
	if(keyip)
		cJSON_AddStringToObject(ret_root,"vncip",keyip);
	if(-1 != keyport)
		cJSON_AddStringToObject(ret_root,"keyport",JSON_keyport);
	if(-1 != hdfreenum)
		cJSON_AddStringToObject(ret_root,"hdfreenum",JSON_hdfreenum);
	if(-1 != sdfreenum)
		cJSON_AddStringToObject(ret_root,"sdfreenum",JSON_sdfreenum);
	if(serialno)
		cJSON_AddStringToObject(ret_root,"serialno",serialno->valuestring);
	JSON_print = cJSON_PrintUnformatted(ret_root);
	sprintf(JSON_ret_string,"%sXXEE",JSON_print);
	ret_string = JSON_ret_string;
	free(JSON_print);
	cJSON_Delete(ret_root);
	return true;
}

int resource_oprate::get_encoder_core(int index)
{
	return  (Rsm_Config::instance()->core_No + \
		(index % Rsm_Config::instance()->core_sum));
}
int resource_oprate::start_encoder_system(unsigned short vnc_port,int bitrate,char* dst_url)
{
	pid_t pid;
	int index_int;
	char index_str[32];
	char encoder_core[32];
	string pids;
	char bit[32];
	char *end = NULL;
	
	index_int = (int)vnc_port - Rsm_Config::instance()->vncserverport;

	sprintf(index_str,"%d",index_int);

	sprintf(bit,"%d",bitrate);
	pids = Rsm_Config::instance()->pmt_pid + ":" +\
		Rsm_Config::instance()->service_id + ":" +\
		Rsm_Config::instance()->pmt_vid + ":" +\
		Rsm_Config::instance()->pmt_aid;

	pid = fork();
	if(!pid)
	{
	/*./avencoder --index 0 --width 1280 --height 720 --bitrate 4000000 --core 0 --pids 1024:1:64:65(pmt_pid,service_id,pmt_vid,pmt_aid) --gopsize 25 --destaddr 192.168.60.248:14000 */
		if(Rsm_Config::instance()->core_sum)
		{
			sprintf(encoder_core,"%d",get_encoder_core(index_int));
			execl(encoderpath.c_str(),encoderpath.c_str(),\
				"--index",index_str,\
				"--width",Rsm_Config::instance()->width.c_str(),\
				"--height",Rsm_Config::instance()->height.c_str(),\
				"--bitrate",bit,\
				"--core",encoder_core,\
				"--pids",pids.c_str(),\
				"--gopsize",Rsm_Config::instance()->gop_size.c_str(),\
				"--destaddr",dst_url,\
				(char *)0);
		}
		else
		{
			execl(encoderpath.c_str(),encoderpath.c_str(),\
				"--index",index_str,\
				"--width",Rsm_Config::instance()->width.c_str(),\
				"--height",Rsm_Config::instance()->height.c_str(),\
				"--bitrate",bit,\
				"--pids",pids.c_str(),\
				"--gopsize",Rsm_Config::instance()->gop_size.c_str(),\
				"--destaddr",dst_url,\
				(char *)0);
		}
	}
	return pid;
}

int resource_oprate::stop_encoder_system(int pid)
{
	int reqs;
	do
	{
		reqs = kill(pid,0);//若进程不存在，直接退出
		if(reqs)
			break;
		reqs = kill(pid,SIGKILL);//否则杀死进程
	}while(reqs);
	if(!reqs)
		waitpid(pid,NULL,0);
	return 0;
}

bool  resource_oprate::process_App_Login(struct timeval start_time,cJSON *root,string &ret_string)
{
	string session_id,ipqam_ip,web_url;
	int    vid_rate,ipqam_port,over_p,vnc_add,key_add,record_add,record_user_id,num_hd_free,num_sd_free;
	
	int encoder_id;
	char cmd_buff[128];
	char dst_url[256];
	Rsm_Session_Msg session_msg;
	struct timeval now;
	struct timeval start_create;

	
	
	cJSON* serialno = cJSON_GetObjectItem(root,"serialno");
	if(!try_get_value(root,"resid",session_id)) return false;
	if(!try_get_value(root,"iip",ipqam_ip)) return false;
	if(!try_get_value(root,"iport",ipqam_port)) return false;	
	if(!try_get_value(root,"url",web_url)) return false;
	if(!try_get_value(root,"rate",vid_rate)) return false;

	if(error)
	{
		LOG_WARN_FORMAT("WARN  - [RSM]: server overload %s \n",session_id.c_str());
		JSON_print("vnclogin","-1837",session_id.c_str(),0,"0",0,0,0,serialno,ret_string);
		return false;
	}

	gettimeofday(&start_create,NULL);
	//检测超时
	if(start_create.tv_sec -  start_time.tv_sec >2)
	{
		LOG_WARN_FORMAT("WARN  - [RSM]: create session timeout %s \n",session_id.c_str());
		JSON_print("vnclogin","-1835",session_id.c_str(),0,"0",0,0,0,serialno,ret_string);
		return false;
	}

	over_p=VNC_State::instance()->timeout;
	//检测空余资源
	if(!VNC_State::instance()->vnc_check(num_hd_free,num_sd_free)) 
	{
		LOG_WARN_FORMAT("WARN  - [RSM]: check vnc fail%s \n",session_id.c_str());
		JSON_print("vnclogin","-1828",session_id.c_str(),0,"0",0,0,0,serialno,ret_string);
		return true;
	}

	//检测编码是否存在
	encoder_id = Rsm_Session::instance()->map_session[session_id].encoder_id;//若不存在，会造成对应session_id的encoder_id为0，map多一个元素
	if (encoder_id !=0)
	{
		LOG_WARN_FORMAT("WARN  - [RSM]: relogin%s \n",session_id.c_str());
		if(ipqam_ip != Rsm_Session::instance()->map_session[session_id].ipqam_ip 
			|| ipqam_port != Rsm_Session::instance()->map_session[session_id].ipqam_port)
		{
			stop_encoder_system(encoder_id);
			
			vnc_add = Rsm_Session::instance()->map_session[session_id].vnc_add;
			key_add = Rsm_Session::instance()->map_session[session_id].key_add;
			record_add = Rsm_Session::instance()->map_session[session_id].record_add;
			record_user_id = Rsm_Session::instance()->map_session[session_id].record_user_id;
			Rsm_Session::instance()->delUser(session_id.c_str(),encoder_id);
			goto end;
		}
		JSON_print("vnclogin","0",session_id.c_str(),
					Rsm_Session::instance()->map_session[session_id].vnc_add,
					Rsm_Config::instance()->rsm_ip.c_str(),
					Rsm_Session::instance()->map_session[session_id].key_add,
					num_hd_free,num_sd_free,serialno,ret_string);
		return true;

	}
	//启动浏览器资源
	
	if(!VNC_State::instance()->vnc_create(HD,session_id,web_url,over_p,vnc_add,key_add,record_add,record_user_id))
	{
		LOG_WARN_FORMAT("WARN  - [RSM]: create vncserver fail%s \n",session_id.c_str());
		
		if(0>=num_hd_free)
		{
			LOG_WARN("WARN  - [RSM]: hd resoure empty \n");
			JSON_print("vnclogin","-1833",session_id.c_str(),0,"0",0,0,0,serialno,ret_string);
			return true;
		}
		
		JSON_print("vnclogin","-1821",session_id.c_str(),0,"0",0,num_hd_free,num_sd_free,serialno,ret_string);
		return true;
	}
	if(num_hd_free>0)
		num_hd_free--;
	LOG_INFO_FORMAT("create vncserver sucess%s \n",session_id.c_str());

//启动编码
end:
	

	sprintf(dst_url,"%s:%d",ipqam_ip.c_str(),ipqam_port);
	LOG_INFO_FORMAT("INFO  - [RSM]: vncip= %s vncport = %d ipqam_addr = %s record_add= %d has_audio= %d vid_bitrate = %d \n",
			Rsm_Config::instance()->rsm_ip.c_str(),\
			(unsigned short)vnc_add,\
			dst_url,\
			record_add,\
			Rsm_Config::instance()->has_audio,\
			(vid_rate * 1024));

	encoder_id = start_encoder_system((unsigned short)vnc_add,\
										(vid_rate * 1024),\
										dst_url);
	if(0 >= encoder_id)
	{
		if(!VNC_State::instance()->vnc_relsease(session_id))
		{
			LOG_WARN_FORMAT("WARN  - [RSM]: release vncserver fail %s \n",session_id.c_str());
			JSON_print("vnclogin","-1823",session_id.c_str(),0,"0",0,num_hd_free,num_sd_free,serialno,ret_string);
		
			return true;
		}
		LOG_WARN_FORMAT("WARN  - [RSM]: create encoder fail%s \n",session_id.c_str());
		JSON_print("vnclogin","-1822",session_id.c_str(),0,"0",0,num_hd_free,num_sd_free,serialno,ret_string);
		
		return true;
	}
	LOG_INFO_FORMAT("+++++++++create encoder sucess%s ,encoder_id: %d  ++++++++++\n",session_id.c_str(),encoder_id);

	gettimeofday(&now,NULL);
	if(now.tv_sec - start_create.tv_sec >2)
	{
		VNC_State::instance()->vnc_relsease(session_id);
		stop_encoder_system(encoder_id);
		LOG_ERROR_FORMAT("ERROR - [RSM]: [-1834] creating session %s time out %ld sec\n",session_id.c_str(),(now.tv_sec - start_create.tv_sec));
		return false;
	}
	
	session_msg.session_id = session_id;
    session_msg.ipqam_ip = ipqam_ip;
    session_msg.encoder_id = encoder_id;
    session_msg.vnc_add = vnc_add;
    session_msg.ipqam_port = ipqam_port;
    session_msg.key_add = key_add;
    session_msg.record_add = record_add;
	session_msg.record_user_id = record_user_id;
	Rsm_Session::instance()->addUser(session_id.c_str(),encoder_id,session_msg);
	JSON_print("vnclogin","0",session_id.c_str(),vnc_add,
				Rsm_Config::instance()->rsm_ip.c_str(),key_add,num_hd_free,num_sd_free,serialno,ret_string);
	return true;
}

bool  resource_oprate::process_App_Logout(struct timeval start_time,cJSON *root,string &ret_string)
{
	string session_id;
	int encoder_id,reqs;	
	struct timeval now;
	struct timeval start_release;
	bool flag = true;


	cJSON* serialno = cJSON_GetObjectItem(root,"serialno");
	if(!try_get_value(root,"resid",session_id)) return false;
	encoder_id = Rsm_Session::instance()->map_session[session_id].encoder_id;	

	gettimeofday(&start_release,NULL);
	if(start_release.tv_sec - start_time.tv_sec >2)
	{
		flag = false;
		LOG_ERROR_FORMAT("ERROR - [RSM] [-1836]: release session %s timeout \n",session_id.c_str());
	}

	do
	{
		if(0 == encoder_id)
			break;
		reqs = stop_encoder_system(encoder_id);
		LOG_INFO_FORMAT("INFO  - [RSM]: encoder return %d ,encoder id  %d\n", reqs,encoder_id);
		if(-19 == reqs)//编码程序没有该流输出，编码流已经自己退出
			break;
		else if(reqs)
		{
			JSON_print("vnclogout","-1824",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
			LOG_ERROR("ERROR - [RSM] [-1824]: release encoder fail \n");
			return true;
		}
	}
	while(0);
	
	gettimeofday(&now,NULL);
	if(now.tv_sec - start_release.tv_sec >2)
	{
		flag = false;
		LOG_ERROR_FORMAT("ERROR - [RSM] [-1836]: releasing session %s timeout %ld sec \n",session_id.c_str(),(now.tv_sec - start_release.tv_sec));
	}	
	
	if(!VNC_State::instance()->vnc_relsease(session_id))
	{
		LOG_ERROR("ERROR - [RSM] [-1823]: release chrome fail \n");
		JSON_print("vnclogout","-1823",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
		return true;
	}
	LOG_INFO_FORMAT("INFO  - [RSM]: release vncserver sucess %s \n",session_id.c_str());
	Rsm_Session::instance()->delUser(session_id.c_str(),encoder_id);
	JSON_print("vnclogout","0",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);

	return flag;
}


/*bool  Rsm_Aim_Handler::process_App_Session_Req(cJSON *root,string &ret_string)
{
	string session_id,ipqam_ip,vid_type;
	int ipqam_port,key_add;
	Rsm_Session_Msg session_msg;
    char cmd_buff[128];
	session_msg = Rsm_Session::instance()->map_session[session_id];
	ipqam_ip = session_msg.ipqam_ip;
	ipqam_port = session_msg.ipqam_port;
	vid_type = session_msg.vid_type;
	key_add = session_msg.key_add;
	sprintf(cmd_buff,"XXBBAPP_SESSION_RESP|0|%s|%d|%s|%s|%dXXEE",
		ipqam_ip.c_str(),ipqam_port,vid_type.c_str(),Rsm_Config::instance()->rsm_ip.c_str(),key_add);
	LOG_INFO_FORMAT("send to aim %s \n",cmd_buff);
	write(cmd_buff,strlen(cmd_buff));
	return true;
}*/
bool  resource_oprate::process_App_Session_Req(cJSON *root,string &ret_string)
{
return true;
}

bool  resource_oprate::process_App_Notice_Req(cJSON *root,string &ret_string)
{
	string session_id;
	int encoder_id,reqs;
	cJSON* serialno = cJSON_GetObjectItem(root,"serialno");
	
	if(!try_get_value(root,"resid",session_id)) return false;
	if(!VNC_State::instance()->vnc_stop_timeout(session_id))
    {
		JSON_print("vncpause","-1832",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
		return true;
    }

	encoder_id = Rsm_Session::instance()->map_session[session_id].encoder_id;
	if(0 == encoder_id)
	{
		JSON_print("vncpause","0",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
		return true;
	}
	if(reqs = stop_encoder_system(encoder_id))
	{
		JSON_print("vncpause","-1824",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
		LOG_INFO_FORMAT("INFO  - [RSM]: encoder return %d ,encoder id  %d\n", reqs,encoder_id);
		LOG_ERROR("ERROR - [RSM] [-1824]: release encoder fail \n");
		return true;
	}
	LOG_INFO_FORMAT("INFO  - [RSM]: encoder return %d ,encoder id  %d\n", reqs,encoder_id);
	Rsm_Session::instance()->delUser(session_id.c_str(),encoder_id);

   	JSON_print("vncpause","0",NULL,-1,NULL,-1,-1,-1,serialno,ret_string);
	return true;
}
bool  resource_oprate::process_App_Exist_Req(cJSON *root,string &ret_string)
{
	cJSON *ret_root;
	char* JSON_ret;
	char retstring[128]={0};
	ret_root=cJSON_CreateObject();
	cJSON_AddStringToObject(ret_root,"cmd","vncexist");
	cJSON_AddStringToObject(ret_root,"retcode","0");
	JSON_ret = cJSON_PrintUnformatted(ret_root);
	sprintf(retstring,"%sXXEE",JSON_ret);
	ret_string = retstring;
	cJSON_Delete(ret_root);
	free(JSON_ret);
	return true;	
}


