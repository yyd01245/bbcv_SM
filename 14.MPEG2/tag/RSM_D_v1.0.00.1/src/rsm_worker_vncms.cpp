#include <sys/types.h>
#include <sys/wait.h>
#include "rsm_worker_vncms.h"
#include "rsm_config.h"
#include "Log.h"


bool Send_Info_To_Vncms::send_info(int index,const string &msg,bool iflog)
{
	Socket data_sock;
	char buf[1024+1];
	bool ret = false;
	
	do
	{
		if(!Socket_Connector::connect(data_sock,"127.0.0.1",(index+Rsm_Config::instance()->vncport)))
		{
			LOG_ERROR_FORMAT("ERROR - [VNCMC] :[-1802]: connect  error vnc index=%d\n", index);
			break;
		}
		if(iflog)
			LOG_DEBUG_FORMAT("DEBUG - [VNCMC] send_info connect: p_Port=%d \n", (index+Rsm_Config::instance()->vncport));
		if(0>data_sock.write(msg.c_str(),msg.size(),2))
		{	
			LOG_ERROR_FORMAT("ERROR - [VNCMC] :[-1802]:send_info write error vnc index=%d sockid: %d\n", index,data_sock.getHandle());
			break;
		}
		if(iflog)
			LOG_INFO_FORMAT("INFO  - [VNCMC] send_info write: index=%d, msg=%s\n", index, msg.c_str());
		memset(buf, 0, sizeof(buf));
		if(0>data_sock.read(buf,1024,1,2))
		{		
			LOG_ERROR_FORMAT("ERROR - [VNCMC] :[-1802]:send_info recv none vnc index=%d sockid: %d\n",index,data_sock.getHandle());
			break;
		}
		if(iflog)
			LOG_INFO_FORMAT("INFO  - [VNCMC] send_info recv from vnc index=%d, msg=%s\n",index, buf);
		if(0 != strcmp(buf,"XXBBAPP|0XXEE"))
		{
			LOG_ERROR_FORMAT("ERROR - [VNCMC] :[-1803]:recv wrong vnc%d sockid: %d\n",index,data_sock.getHandle());
			break;
		}
		ret = true;	
	}
	while(0);
	data_sock.close();
	return ret;
}

bool http_server::prase_httpd(char *http,string& url)
{
	char *tmp = NULL;
	tmp = strstr(http,"GET");
	url = tmp;
	return true;
}

int http_server::handle_process()
{	
	char	req_buf[4096];	
	char	*tmp;	
	int		i_rc=0,i_count=0;	
	string	session_id;	
	string	url_string;	
	int		vncport,index;
	char 	*cmd=NULL;	
	//step1:recv data	
	do	
	{		
		i_rc=read(req_buf+i_count,4000-i_count,1,1);
		if(i_rc<=0)break;//异常关闭
		i_count+=i_rc;
	}while(strstr(req_buf,"XXEE")==NULL);	 	  	
	if(i_count >0 )
		req_buf[i_count]='\0';
	else
		return -1;//异常处理	
	LOG_DEBUG_FORMAT("DEBUG - [HTTP]: http recv %s\n ",req_buf);
	tmp = strstr(req_buf,"XXEE");	
	//*(tmp+4) = '\0';
	if(tmp == NULL)
	{
		LOG_INFO("INFO-[HTTP]:NO XXEE\n");
		return -1;
	}	
	*(tmp+4) = '\0';
	Pubc::replace(req_buf,"XXEE","");		
	if(!prase_httpd(req_buf,url_string))
	{		
		LOG_ERROR("ERROR - [HTTP]:  wrong http format !\n");
		return -1;
	}
	tmp = strstr(req_buf,"/");
//	LOG_DEBUG_FORMAT("DEBUG - [HTTP]: URL :%s\n",tmp);
	
	char callback[128];
	memset(callback,0,sizeof(callback));
	int ret = sscanf(tmp,"/%d?callback=%128s",&vncport,callback);
	if(ret != 2)
		return -1;
	index = vncport - Rsm_Config::instance()->vncport;
//	LOG_INFO_FORMAT("INFO  -[HTTP]:recv req from index %d\n",index);
	//printf("-------vncport=%d------callback=%s--------\n",vncport,callback);
	string web_url;
	int returncode = -1;
	if(!Pre_Process::instance()->login_user_check(index,web_url))//查看是否有用户登陆
		returncode = 0;
	char body[1024];
	memset(body,0,sizeof(body));
	snprintf(body,sizeof(body),"%s({\"returncode\":\"%d\",\"vncport\":\"%d\",\"url\":\"%s\"})\r\n",callback,returncode,vncport,web_url.c_str());
	char buffer[2048];
	memset(buffer,0,sizeof(buffer));
	snprintf(buffer,sizeof(buffer),"HTTP/1.0 200 OK\r\nContent-Type: text/javascript\r\ncontent-length: %d\r\n\r\n%s",(int)strlen(body),body);
	if(returncode)
		usleep(20*1000);
	else
		Pre_Process::instance()->login_user_delete(index);//删除已登录的用户
	int ret_count = 0;
	ret_count = write(buffer,strlen(buffer),1);
	if(ret_count != strlen(buffer))
		LOG_ERROR_FORMAT("ERROR - [HTTP]:send error return %d\n",ret_count);
	if(!returncode)
		LOG_INFO_FORMAT("INFO  - [HTTP]:SEND:%s\n",buffer);
	return -1;
	}

int vnc_Pre_Process::init()
{
	starting_chrome = 0;
	LOGIN_SERVER_sngton::instance()->open(8888,10);
	loginning_num = 0;
	logined_num = 0;
	memset(pre_open_map,0,sizeof(PRE_CHROME)*MAX_TOTALL_NUM);
}
int vnc_Pre_Process::run()
{
	static int i = 0;
	if(0 == i)
	{
		i++;
		return check_chrome();
	}
	else 
		return start_chrome();
}

int vnc_Pre_Process::check_chrome()
{
	int it;
	int output=0;
	int timeout;
	const char *buf = "XXBBAPP_VNC_RELEASE_REQ|0XXEE";
	struct timeval now;
	
	while(1)
	{
		sleep(1);
		output++;
/*		if(((Rsm_Config::instance()->pre_amount-2) >= pre_opened_queue.size())
			&& (!Pre_Process::instance()->is_starting())
			&& (10 >= pre_openning_set.size()))
*/

		if(((Rsm_Config::instance()->pre_amount-2) >= pre_opened_queue.size()))
			if(!starting_chrome)
				if(Rsm_Config::instance()->pre_amount >= pre_openning_set.size())
					Pre_Process::instance()->start(1);
		if(10 == output)
		{
			output =0;
			LOG_INFO_FORMAT("INFO  - [VNCMC]: starting_chrome %d,to pre_open %d,preopened %d,preopenning %d\n",starting_chrome,pre_open_queue.size(),pre_opened_queue.size(),pre_openning_set.size());
		}
		gettimeofday(&now,NULL);
		for(it = 1;it < MAX_TOTALL_NUM;it++)
		{
			if(false == pre_open_map[it].inuse)
				continue;
			if(false != pre_open_map[it].is_register)
				timeout = 2;
			else
				timeout = 30;
			
			if(now.tv_sec - pre_open_map[it].last_connect.tv_sec >timeout)
			{
				LOG_INFO_FORMAT("INFO  - [VNCMC]:chrome %d lost connect now %ld last %ld\n",it,now.tv_sec,pre_open_map[it].last_connect.tv_sec);
				if(!Send_Info_To_Vncms::send_info(it, buf,RSMNOLOG))
				{
					continue;
				}
				ThreadLocker locker(pre_open_map[it].user_lock);
				if(pre_opened_set.count(it))
					pre_opened_set.erase(it);//注销预启动成功池
				pre_open_map[it].inuse = false;//注销预启动表
				pre_openning_set.erase(it);//注销预启动中池
				add_pre_open(it);
				LOG_INFO_FORMAT("INFO  - [VNCMC]: chrome %d reuse add to to_pren_open_queue\n",it);
			}
		}
	}
	
	return 0;//启动检查chrome是否活跃的线程
}

int vnc_Pre_Process::send_vncms_msg(const string &app_id,const int &over_p,const string &web_url,int index,int oprate)
{
	char buf[4096];
	string msg;
	
	if(web_url.size()>4096)
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMC]: URL is too long ,the size of URL is %d\n",web_url.size());
		return -1;
	}
	/*增加vnc端口字段，实现chrome插件控制超时时间*/
	if(oprate ==1)
		sprintf(buf,"XXBBAPP_RFB_REQ|%s|%d|%s?&vncport=%d&vnckeyport=%d&vncip=%sXXEE",\
			app_id.c_str(),\
			over_p,\
			web_url.c_str(),\
			(index + Rsm_Config::instance()->vncport),\
			(index + Rsm_Config::instance()->keylistenport),\
			Rsm_Config::instance()->rsm_ip.c_str());
	else
		sprintf(buf,"XXBBAPP_FORBID_TIMEOUT_REQ|%dXXEE", 0);
	msg = buf;
	if(Send_Info_To_Vncms::send_info(index, msg,RSMNOLOG))
		return 0;
	return -1;
}

int vnc_Pre_Process::start_chrome()
{
//	if(1 == starting_chrome)
	//	return 0;
	starting_chrome = 1;

	PRE_CHROME tmp_chrome;
	int index;
	int i;

	LOG_INFO_FORMAT("INFO  - [VNCMC]:pre start chrome started %d, pre_queue %d\n",get_pre_open_amount(),pre_open_queue.size());
	for(i=0;i<(Rsm_Config::instance()->pre_amount - pre_opened_set.size());i++)
	{
		if(0 >= pre_open_queue.size())
		{
			starting_chrome = 0;
			LOG_INFO("INFO  - [VNCMC]: pre open thread exit\n");
			return -1;
		}
		/*启动chrome,并设置键值不超时*/
		ThreadLocker locker(add_user_lock);
		index = pre_open_queue.front();
		pre_open_queue.pop();
		pre_openning_set.insert(index);//注册预启动中池
		if(0 == send_vncms_msg("pre",10000,Rsm_Config::instance()->pre_web, index,1))
		{
			usleep(10*1000);
			//sleep(1);
			send_vncms_msg("NOTIMEOUT",5,Rsm_Config::instance()->pre_web, index,0);
			LOG_INFO_FORMAT("INFO  - [CHROME]: start chrome %d\n",index);
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR - [RSM]: pre open chrome failed No. %d\n",index);
		}
		ThreadLocker slocker(pre_open_map[index].user_lock);
		gettimeofday(&(pre_open_map[index].last_connect),NULL);
		pre_open_map[index].inuse = true;
	}

	starting_chrome = 0;
	LOG_INFO("INFO  - [VNCMC]: pre open thread exit\n");
	return 0;
	
}

int vnc_Pre_Process::add_pre_open(int index)
{
	ThreadLocker locker(add_user_lock);
	pre_open_queue.push(index);

	return 0;
}

int vnc_Pre_Process::get_pre_open_amount()
{
	return pre_open_queue.size();
}

int vnc_Pre_Process::login_user_add(const string &sessionid,const string &url,int over_p)
{
	
	loginning_num ++;
	
	int index;
		index = pre_opened_queue.size();
	if(index)
	{	/*绑定登陆URL 和资源id*/
		ThreadLocker locker(login_user_lock);
		index = pre_opened_queue.front();
		while(!pre_opened_set.count(index))
		{
			pre_opened_queue.pop();
			index = pre_opened_queue.front();
		}
		LOG_INFO_FORMAT("INFO  - [RSM] get pre index %d ssession_id %s\n",index,sessionid.c_str());
		pre_opened_queue.pop();//注销预启动成功队列
		pre_opened_set.erase(index);//注销预启动成功池
		ThreadLocker xlocker(pre_open_map[index].user_lock);
		pre_open_map[index].inuse = false;//注销预启动表
		pre_open_map[index].is_register = false;

		login_user_map[index] = url;
//		LOG_INFO_FORMAT("INFO  - [RSM]: %d tureuser loginning index %d url :%s\n",loginning_num,index,url.c_str());

		if(0 != send_vncms_msg(sessionid.c_str(),over_p,url, index,1))
			return -1;
	}
	return index;
}
int vnc_Pre_Process::login_user_check(int index,string &url)
{
//	struct timeval now;
	LOGIN_MAP::iterator it;
	

	it = login_user_map.find(index);
	if(login_user_map.end() != it)
	{
		url = it->second;
		return 0;
	}

	if(0 == pre_opened_set.count(index))//不存在于池内，需要注册
	{
		ThreadLocker locker(login_user_lock);
		pre_open_map[index].is_register = true;
		pre_opened_set.insert(index);//注册预启动成功池
		pre_openning_set.erase(index);//注销预启动中池
		pre_opened_queue.push(index);//注册预启动成功队列
		LOG_INFO_FORMAT("INFO  - [VNCMC]: chrome register No. %d\n",index);
	}
	ThreadLocker xlocker(pre_open_map[index].user_lock);
	gettimeofday(&(pre_open_map[index].last_connect),NULL);
	LOG_DEBUG_FORMAT("INFO  - [VNCMC]: chrome %d time update last %ld\n",index,pre_open_map[index].last_connect.tv_sec);
	
	return -1;

}

int vnc_Pre_Process::login_user_delete(int index)
{
	ThreadLocker locker(login_user_lock);
	logined_num++;
	//LOG_INFO_FORMAT("INFO  - [RSM]: %d tureuser logined index %d url :%s\n",logined_num,index,login_user_map[index].c_str());
	login_user_map.erase(index);
	return 0;
}


int vnc_State_Imp::get_vnc_port_index(int index)
{
	int real_index;
	
	if (index <= 100)
	{
		return index;
	}
	else if (index > 100 && index <= 200)
	{
		real_index = index + 100;
	}
	return real_index;
}

int vnc_State_Imp::get_recordid(int index)
{
        if(index>pa_amount)
                return get_recordid(index-pa_amount);
        return index;
}

int	vnc_State_Imp::get_vncms_index(int vncmspid)
{
	int i;
	for(i=0;i != MAX_TOTALL_NUM;i++)
	{
		if(vncms_pid[i] == vncmspid)
			return i;
		if(0 == vncms_pid[i])
			return 0;
	}
}

bool	vnc_State_Imp::checksession(string id, USER_MAP::iterator &iter)
{
	LOG_DEBUG_FORMAT("DEBUG - [VNCMC]: check session: %s\n",id.c_str());
	iter = m_usermap.find(id);
	if(iter == m_usermap.end())
	{
		return false;
	}
	return true;
}

bool	vnc_State_Imp::checksession(string &sessionid,int i)
{
	USER_MAP::iterator user_iter;
	for(user_iter=m_usermap.begin();user_iter!=m_usermap.end();user_iter++)
	{
		if(i == user_iter->second)
		{
			sessionid=user_iter->first;
			return true;
		}
	}
	return false;
}

int vnc_State_Imp::get_encoder_core(int index)
{
	return  (Rsm_Config::instance()->core_No + \
		(index % Rsm_Config::instance()->core_sum));
}

int vnc_State_Imp::start_vncms(int num,char *type)
{
	int pid;
	char char_num[64];
	char vgl_display[32]={0};

	sprintf(char_num,"%d",num);
	if(Rsm_Config::instance()->core_sum)
		sprintf(vgl_display,"%d",get_encoder_core(num));
	if ((pid = vfork()) < 0)//这儿该如何处理，如果继续的话可能导致挂住
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMC] init vnc %s failed fork error\n",char_num);
		return -1;
	}
	else if (pid == 0)
	{
		execl("bvbcm","bvbcm", char_num, type, vgl_display, (char *)0);
		LOG_ERROR_FORMAT("ERROR - [VNCMC] vnc_init fail: index=%s  start vncms error\n", char_num);
		_exit(1);
	}
	return pid;
}

int	vnc_State_Imp::vnc_init(int max_hd_num, int max_sd_num)
{
	int 	i;
	int 	vnc_index;
	int 	pid;
	char 	type[10];
	int 	ret;
	char 	config_file[500];

	
	totol = atoi(Rsm_Config::instance()->num_hd.c_str())+atoi(Rsm_Config::instance()->num_sd.c_str());

	memset(vncms_pid,0,sizeof(vncms_pid));
	memset(vid_type,0,sizeof(vid_type));
	pa_amount = Rsm_Config::instance()->pa_amount;
	for(i= 0; i < max_sd_num + max_hd_num ; i++)//create Xvnc
	{
		if  (i <  max_sd_num)
		{
			sprintf(type, "%d",SD);
		}
		else
		{
			sprintf(type, "%d",HD);
		}
		if(Rsm_Config::instance()->core_sum >0)
		vnc_index = i + Rsm_Config::instance()->core_No + \
			Rsm_Config::instance()->core_sum;
		else
			vnc_index = i +1;
		
		vncms_pid[vnc_index] = start_vncms(vnc_index,type);
	//	LOG_DEBUG_FORMAT("vncms :%d started pid is %d\n",i+1,vncms_pid[i+1])

		if (i <  max_sd_num)
		{
			vid_type[vnc_index] = SD;
			sd_count.push(vnc_index);//sd_video;
		}
		else
		{
			vid_type[vnc_index] = HD;
			hd_count.push(vnc_index);//hd_video;
		}
		sleep(1);
	}
	timeout = 600;
	cout<<"vnc init...ok!"<<endl;
	return 0;
}

int vnc_State_Imp::pre_open_init()
{
	Pre_Process::instance()->init();
	for(int i = 0;i<Rsm_Config::instance()->pre_amount;i++)
	{
		Pre_Process::instance()->add_pre_open(hd_count.front());
		hd_count.pop();
	}
	Pre_Process::instance()->start(2);
	return 0;
}

int vnc_State_Imp::set_value(int &vnc_add,int &key_add,int &record_add,int &system_userid,int index)
{	
	vnc_add = Rsm_Config::instance()->vncserverport+get_vnc_port_index(index);
	key_add = Rsm_Config::instance()->keylistenport+index;
	record_add = get_recordid(index);
	system_userid = Rsm_Config::instance()->uid+(index-1)/pa_amount;
	return 0;
}



int vnc_State_Imp::get_index(QUEUE *queue)
{
	int index = 0;
	if(0 >= (*queue).size())
	{
		return index;
	}	
	index = (*queue).front();
	return index;
}
	
bool vnc_State_Imp::vnc_create(int vid_type, const string app_id ,const string web_url, int over_p,
								int &vnc_add, int &key_add, int &record_add, int &system_userid)
{
	bool ret= true;
	USER_MAP::iterator iter;
	string msg;
	int num_hd_free, num_sd_free;
	int index,pre_open_rest,i;
	
	LOG_INFO_FORMAT("INFO  - [VNCMC]: vnc create...start:app_id=%s\n", app_id.c_str());
	ThreadLocker locker(m_mutex_thread);
	// 1.check session is exsit
	if(checksession(app_id,iter))
	{
		set_value(vnc_add,key_add,record_add,system_userid,iter->second);
	//	send_create_msg(app_id,over_p,web_url,iter->second);
		LOG_INFO_FORMAT("INFO  - [VNCMC]: vnc app_id=%s exsit !\n\n", app_id.c_str());
		return ret;
	}

	// 2.create vnc	

	if(HD != vid_type)
		return false;

	index = Pre_Process::instance()->login_user_add(app_id,web_url,over_p);
/*补足预启动数量*/
	pre_open_rest = Pre_Process::instance()->get_pre_open_amount();
	if((Rsm_Config::instance()->pre_amount)/2 > pre_open_rest)
	{
		for(i=0;i<((Rsm_Config::instance()->pre_amount)/2 - pre_open_rest);i++)
		{
			if(0 >= hd_count.size())
			{
				LOG_ERROR("ERROR - [VNCMC]: no resouse to pre open\n");
				break;
			}
			LOG_INFO_FORMAT("INFO  - [VNCMC]:add index %d to pre_open_queue\n",hd_count.front());
			Pre_Process::instance()->add_pre_open(hd_count.front());
			hd_count.pop();			
		}
	}
	if( 0 == index )
	{
		LOG_INFO_FORMAT("INFO  - [VNCMC]: %s resoure is empty \n",(vid_type>SD?"HD":"SD"));
		return false;
	}

	/*设置返回参数*/
	set_value(vnc_add,key_add,record_add,system_userid,index);
	/*添加到登陆用户表*/
	m_usermap[app_id] = index;

	LOG_INFO("INFO  - [VNCMC]: vnc create...end\n\n");
	return ret;
}


bool vnc_State_Imp::vnc_relsease(const string &app_id )
{
	FILE *fp;
	char buf[1024];
	bool ret=true;	
	USER_MAP::iterator iter;
	string msg;

	LOG_INFO_FORMAT("INFO  - [VNCMC]: vnc relsease...start, app_id=%s\n",app_id.c_str());
	ThreadLocker locker(m_mutex_thread);		
	// 1.check session is exsit
	if(checksession(app_id, iter))
	{
	// 2.send relsease req
		sprintf(buf,"XXBBAPP_VNC_RELEASE_REQ|%dXXEE", 0);
		msg = buf;
		if(Send_Info_To_Vncms::send_info(iter->second,msg,RSMLOG))
		{
			if (vid_type[iter->second] == SD)
			{
				sd_count.push(iter->second);
			}
			else if(vid_type[iter->second] == HD)
			{
				hd_count.push(iter->second);
			}
			
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR - [VNCMC]: send relase to BBCvnc %d falied!\n", iter->second);
			ret = true;
		}
		m_usermap.erase(app_id);
	}
	else
	{
		LOG_WARN_FORMAT("WARN  - [VNCMC]: vnc relsease failed! app_id=%s isn't exist!\n",app_id.c_str());
	}
	
	LOG_INFO("INFO  - [VNCMC]: vnc relsease...end\n\n");
	
	return ret;
}

int vnc_State_Imp::check_free(int index)
{
	return(free_count.count(index));
}

bool vnc_State_Imp::vnc_check(int &num_hd_free, int &num_sd_free)
{
    int i;
    bool ret = true;

	num_hd_free = atoi(Rsm_Config::instance()->num_hd.c_str()) - m_usermap.size();
	num_sd_free = sd_count.size();
	
   	LOG_INFO_FORMAT("INFO  - [VNCMC]: vnc check...ok:num_sd_free=%d, num_hd_free=%d\n\n", num_sd_free, num_hd_free);

	return ret;
}

bool vnc_State_Imp::vnc_stop_timeout(const string &app_id)
{
	char buf[1024];
	bool ret=true;
	USER_MAP::iterator iter;
	string msg;

	// 1.check session is exsit
	if(checksession(app_id,iter))
	{
		sprintf(buf,"XXBBAPP_FORBID_TIMEOUT_REQ|%dXXEE", 0);
		msg = buf;
		if(Send_Info_To_Vncms::send_info(iter->second,msg,RSMLOG))
		{
			ret = true;
		}
		else
		{
//			sprintf(buf,"XXBBAPP_FORBID_TIMEOUT_RESP|%dXXEE\n", 0);
			ret = false;
			LOG_ERROR_FORMAT("ERROR - [VNCMC]: send forbid to BBCvnc%d falied\n",iter->second);
		}
	}
	else
	{
		//
	}
	
	return ret;
}

int vnc_State_Imp::change_resouse(int type)
{
	int i;
	int index;
	char vnc_type[8];
	set<int>::iterator it;
	int num = atoi(Rsm_Config::instance()->num_hd.c_str());

	LOG_INFO_FORMAT("INFO  - [RSM]: change totolnum %d oldnum %d\n",num,totol);
	sprintf(vnc_type,"%d",type);

	if(num >=MAX_TOTALL_NUM)
		return -1;
	
	VNC_Check::instance()->c_totol = 1;//停止检测进程

	if(num == totol)
		return 0;
	
	else if(num > totol)
	{
		for(i = totol;i<num;i++)
		{
			if(free_count.size()>0)
			{
				it = free_count.begin();
				index = *it;
				LOG_DEBUG_FORMAT("freecount used %d\n",index);
				free_count.erase(index);
			}
			else
				index = i+1;
			
			usleep(10*1000);
			vncms_pid[index] = start_vncms(index,vnc_type);
			vid_type[index] = HD;
			hd_count.push(index);//hd_video;			
		}
	}

	else if(num < totol)
	{
		if(hd_count.size() < (totol - num))
			return -1;
		for(i = num;i<totol;i++)
		{
			usleep(10*1000);
			index = hd_count.front();
			hd_count.pop();
			free_count.insert(index);
			kill(vncms_pid[index],SIGKILL);
			
			LOG_DEBUG_FORMAT("free index :%d, pid :%d\n",index,vncms_pid[index]);
		}
	}
	totol = num;
	VNC_Check::instance()->c_totol = 0;//继续检测进程健康
	return 0;
}

int vnc_State_Imp::vncms_restart(int index)
{
	char type[64]={0};
	char buf[1024]={0};
	BAD_MAP::iterator user_iter;
	
	sprintf(type,"%d",vid_type[index]);
	/*删除旧vncms*/
	sprintf(buf,"kill -9 %d",vncms_pid[index]);
	system(buf);
	/*启动vncms*/
	ThreadLocker locker(m_mutex_thread);
	vncms_pid[index]=start_vncms(index,type);
	if(0 >=vncms_pid[index])
		return -1;
	/*异常队列处理*/
	user_iter = bad_usermap.find(index);
	if(bad_usermap.end()!= user_iter)
	{
		if(SD == user_iter->second)
		{
			sd_count.push(index);
		}
		else if(HD == user_iter->second)
		{
			hd_count.push(index);
		}
		else
			return -2;
		bad_usermap.erase(index);
	}
	return 0;
} 

int vnc_Check_Imp::check_vncms()
{

	int i,vnc_index;
	string checkmsg="XXBBAPP|1XXEE";
	int rc;
	Socket sock;
	char buf[128+1];
	char msg[128];
	string session;

	LOG_DEBUG("DEBUG - [VNCMC]: Check bvbvm thread started\n");
	c_totol = 0;
	while(1)
	{
		sleep(2);
	//	LOG_DEBUG("DEBUG - [VNCMC]: check vncms once \n");
		for(i = 0;i < (atoi(Rsm_Config::instance()->num_hd.c_str())+atoi(Rsm_Config::instance()->num_sd.c_str()));i++)
		{
			if(Rsm_Config::instance()->core_sum >0)
				vnc_index = i + Rsm_Config::instance()->core_No + \
				Rsm_Config::instance()->core_sum;
			else
				vnc_index = i +1;
			if(c_totol)
				continue;
			if(VNC_State::instance()->check_free(vnc_index))
				continue;
			if(!Send_Info_To_Vncms::send_info(vnc_index,checkmsg,RSMNOLOG))//使用sock检测进程是否正常
			{
				LOG_INFO_FORMAT("DEBUG - [VNCMC]: restarting bvbvm %d success\n",vnc_index);
				if(VNC_State::instance()->vncms_restart(vnc_index))
					LOG_ERROR_FORMAT("ERROR - [VNCMC]: [-1532] restart BBCvnc :%d fail\n",vnc_index);
				LOG_INFO_FORMAT("DEBUG - [VNCMC]: restart bvbvm %d success\n",vnc_index);
				/*if(checksession(session,*iter))
				{
					//调用超时退出
					sock.write(msg,strlen(msg));
					memset(buf, 0, sizeof(buf));
					sock.read(buf,128,1,2);
					sock.close();
				}*/
			}
		}
	}
}



