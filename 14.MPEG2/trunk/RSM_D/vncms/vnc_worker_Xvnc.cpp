#include "vnc_worker_Xvnc.h"
#include "vnc_config.h"
#include "vnc_worker_rsm.h"
#include "Log.h"
#include <sys/types.h>
#include <sys/wait.h>
#include <time.h>
#include <signal.h>

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

int vnc_State_Imp::mysystem(const char *web, int type,const int sockid)
{//下一步改进-----用execve代替execl 能带入参数和环境变量
	pid_t pid;
	char msg[1024] = {0};
	char sid[128] = {0};	
	char geometry[128]={0};
	char display[32];
	char record_id[32];
	
	if ((pid = fork()) < 0)
	{
		status = -1;
	}
	else if (pid == 0)
	{
		sprintf(display,":%d",vnc_index);
		if (0 == type)
		{//下一步改进 ----将Xvnc启动放到rsm里面		
			if (vid_type == 1)
				sprintf(geometry,"%dx%d",Vnc_Config::instance()->sd_width,Vnc_Config::instance()->sd_height);
			else if (vid_type == 2)
				sprintf(geometry,"%dx%d",Vnc_Config::instance()->hd_width,Vnc_Config::instance()->hd_height);
			LOG_DEBUG("DEBUG - [VNCMS] :create BBCvnc begin\n");
			status = execl(Vnc_Config::instance()->vncname.c_str(),Vnc_Config::instance()->vncname.c_str(), display,"-desktop","X","-depth","24","-geometry", geometry, (char *)0);
			LOG_ERROR_FORMAT("ERROR - [VNCMS] :create BBCvnc fail index=%d\n", bvbcm_index);
		}
		else if(2 == type)
		{
			if(sockid >0)
				close(sockid);
			
			sprintf(record_id, "%d",  Vnc_Config::instance()->recordadd+get_recordid(bvbcm_index));
			LOG_DEBUG_FORMAT("[DEBUG] - [BVBCM]: Audio sink is %s \n",record_id);
			if(0 < vgl_index.size())
				setenv("VGL_DISPLAY",vgl_index.c_str(), 1);
			setenv("DISPLAY",display,1);//set envirmont that can make chrome konw which screen to display
			setenv("PULSE_SINK", record_id, 1);
			setenv("PULSE_LATENCY_MSEC", "0", 1);
			sprintf(msg, "--display=:%d", vnc_index);
			sprintf(sid, "--user-data-dir=chrome%d", bvbcm_index);				
			status = execl( "/opt/VirtualGL/bin/vglrun",	
							"/opt/VirtualGL/bin/vglrun",	
							"-c","proxy",	
							"google-chrome",	
							msg,	
							"--kiosk",	
							Vnc_Config::instance()->proxy.c_str(),	
							"--no-first-run",	
							"--in-process-gpu",	   /*修改为可选参数*/
							"--disable-accelerated-video",	
							"--disable-hang-monitor",	
							sid,
							web,
							(char *)0);//3D performance			
			LOG_ERROR_FORMAT("ERROR - [VNCMS] :start chrome fail index=%d\n", bvbcm_index);
		}
		else	if(3 == type)
		{
			if(sockid >0)
				close(sockid);
			sprintf(record_id, "%d",  Vnc_Config::instance()->recordadd+get_recordid(bvbcm_index));
			LOG_DEBUG_FORMAT("[DEBUG] - [BVBCM]: Audio sink is %s \n",record_id);
			setenv("VGL_DISPLAY",vgl_index.c_str(), 1);
			setenv("DISPLAY",display,1);//set envirmont that can make chrome konw which screen to display
			setenv("PULSE_SINK", record_id, 1);
			setenv("PULSE_LATENCY_MSEC", "0", 1);
			sprintf(msg, "--display=:%d", vnc_index);
			sprintf(sid, "--user-data-dir=chrome%d", bvbcm_index);				
			status = execl( "/usr/bin/google-chrome",	
							"google-chrome",	
							msg,	
							"--kiosk",	
							Vnc_Config::instance()->proxy.c_str(),	
							"--no-first-run",	
							"--in-process-gpu",	   /*修改为可选参数*/
							"--disable-accelerated-video",	
							"--disable-hang-monitor",	
							sid,
							web,
							(char *)0);//no 3D performance					
			LOG_ERROR_FORMAT("ERROR - [VNCMS] :start chrome fail index=%d\n", bvbcm_index);
		}
		//LOG_INFO("mysystem 4:start record and chrome ok\n\n");
		exit(1);
	}	
	
	if (status == -1)//execl error, return fail
	{
		pid = -1;
	}

	return pid;
}


int vnc_State_Imp::audiosystem(int index)
{
	pid_t pid;
	char display[32];

	sprintf(display,"%d",index);

	pid = fork();
	if(!pid)
	{
		execl(audiopath.c_str(),audiopath.c_str(),display,(char*)0);
	}
	return pid;
}

int vnc_State_Imp::copy_chrome_data()
{
	char vncseverinfo[128] = {0};
	const char *chrome_path = "./.chrome";
	const char *Internet_chrome_path = "./.Interchrome";
	
	if(Vnc_Config::instance()->TVnet)	
		sprintf(vncseverinfo,"cp -r %s ./chrome%d",Internet_chrome_path,vnc_index);
	else
		sprintf(vncseverinfo,"cp -r %s ./chrome%d",chrome_path,vnc_index);
	system(vncseverinfo);
	return 0;
}

int vnc_State_Imp::init_vnc(int index, int type,string &vgl_display)
{
	char buf[1024];
	static bool first_blood = true;
	if(first_blood)
	{
		pgid = getpgid(0);
		first_blood = false;
	}
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:GUID is %d before init \n",getpgid(0));
	setpgid(0,pgid);
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:GUID is %d  initing \n",getpgid(0));
	pa_amount = Vnc_Config::instance()->pa_amount;
	bvbcm_index = index;
	vnc_index = get_vnc_port_index(index);
	if(0  < vgl_display.size())
		vgl_index = ":" + vgl_display;
	audiopath = Vnc_Config::instance()->audioname;
	strcpy(vnc_addr,Vnc_Config::instance()->localhostip.c_str());
	
	vnc_port = vnc_index + Vnc_Config::instance()->vncserverport;
	keylistenport = bvbcm_index + Vnc_Config::instance()->keylistenport;
	vid_type = type;
	sprintf(Xvnc_state_path,".BBCvnc%d.data",bvbcm_index);
	vid = operate_Xvnc_path(READ,vid);
	if(vid>0)
	{
		sprintf(buf,"kill -9 %d",vid);
		system(buf);
	}
	vid = mysystem(NULL,0,-1);
	if(vid<0)
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS]: [-1532] init_vnc:create BBCvnc:%d fail\n\n",index);
		return -1;
	}
	LOG_INFO_FORMAT("INFO  - [VNCMS]: start Xvnc success vid is%d\n",vid);
	operate_Xvnc_path(WRITE,vid);
/*	sprintf(vncseverinfo,"gnome-session --display=:%d &",real_index);
	system(vncseverinfo);
	system("killall gnome-session "); 
*/	usleep(100*1000);
	if(!initvncclient())
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS]: [-1525] init_vnc:initvncclient: %d  fail\n\n",index);
		return -2;
	}
	SendPointerEvent(vnc_client,0,0,0);
	status = 0;
	setpgid(0,0);
	LOG_DEBUG_FORMAT("DEBUG- [VNCMS]:GUID is %d  after init \n",getpgid(0));
	return 0;
}


bool vnc_State_Imp::kill_chrome()
{	
	int ret = -1;
	char vncseverinfo[128] = {0};
	char dir_path[128]={0};
	char chrome_path[128];
	int i=0;

	getcwd(dir_path,128);
	sprintf(chrome_path,"%s/chrome%d",dir_path,vnc_index);
	sprintf(vncseverinfo,"rm -rf %s",chrome_path);
	if(!access(chrome_path,F_OK))
		system(vncseverinfo);
	while(1)
	{
		while(status ==2)
    	{
    		ThreadLocker locker(clean_up_lock);
			if(0 == pid)
			{
				status = 0;
				break;
			}
    		kill(0,SIGTERM);
			if(aid>0)
				kill(aid,SIGKILL);
			usleep(500*1000);
    		ret = kill(pid,0);
//			LOG_INFO_FORMAT("INFO  - [VNCMS]:KILL return %d pid :%d\n",ret,pid);
        	if(0 != ret )
        	{
        		ret =1;
        		status = 0;
        		pid = 0;
				while(!access(chrome_path,F_OK))
				{
					i++;
					system(vncseverinfo);
					usleep(100*1000);
					kill(pid,0);
					if(i ==5)
					{
						LOG_ERROR("ERROR - [VNCMS]chrome dir cant delete\n");
						break;
					}
				}
				SendPointerEvent(vnc_client,0,0,1);
				SendPointerEvent(vnc_client,0,0,0);
//				LOG_INFO_FORMAT("INFO - [VNCMS]clean dir use %d times\n",i);
				i = 0;
				break;
        	}
			else
				kill(pid,SIGKILL);
		}
	usleep(100*1000);
	}
}


bool vnc_State_Imp::vnc_free()
{
	status = 2;
	vnc_url = "";
	existcheck = 5;
	return true;
	
}

bool vnc_State_Imp::vnc_create(string session_id,const int &time_out ,const string &url,const int sockid)
{
	ThreadLocker locker(clean_up_lock);
	timeout = time_out - Vnc_Config::instance()->timeoutforecast;
	if(timeout >600000)
		timeout = 600000;
	strcpy(sessionid, session_id.c_str());
	vnc_url = url;
	existcheck = 5;
	if( 1 == status)
	{
		gettimeofday(&last_key_time,NULL);
		gettimeofday(&login_time,NULL);
		return true;
	}

	if(3 == status)
	{
		aid = audiosystem(vnc_index);
		if(aid <0)
		{
			TimeoutNotify();
			return false;
		}
		status = 1;
		gettimeofday(&last_key_time,NULL);
		gettimeofday(&login_time,NULL);
		return true;

	}	

	copy_chrome_data();
	usleep(10*1000);
	pid = mysystem(vnc_url.c_str(),(2+Vnc_Config::instance()->chrome_type),sockid);
	if(pid<0)
	{
		TimeoutNotify();
		return false;
	}

	gettimeofday(&last_key_time,NULL);
	gettimeofday(&login_time,NULL);
	status = 1;
//	SendPointerEvent(vnc_client,0,0,1);
//	SendPointerEvent(vnc_client,0,0,0);
	LOG_INFO_FORMAT("INFO - [VNCMS]:server chrome create ok pid=%d\n", pid);

	aid = audiosystem(get_vnc_port_index(vnc_index));
	if(aid <0)
	{
		TimeoutNotify();
		return false;
	}	

	LOG_INFO_FORMAT("INFO - [VNCMS]:server audio create ok aid=%d\n", aid);

	return true;
}

bool vnc_State_Imp::initvncclient()
{
	int i;
	
	for (i = 0; i < 10; i++)
	{
		if(initUDP() == -1)
		{
			LOG_ERROR_FORMAT("ERROR - [VNCMS] [-1521] initUDP fail times=%d\n\n", i+1);
			usleep(100*1000);
			i++;
		}
		else
		{
			break;
		}
	}
	if(i == 10)
	{
		return false;
	}
	
	if(!initLibclient())
	{
		LOG_ERROR_FORMAT("ERROR - [VNCMS] [-1525] initLibclient :connect BBCvnc:%d error\n",vnc_port);
		return false;
	}
	
	return true;
}


//设置udp 重用
void vnc_State_Imp::set_fl(int fd, int flags)
{
	int val;
	
    if ( (val = fcntl(fd, F_GETFL, 0)) < 0)    
       	perror("fcntl get");		
	val |= flags;
	if (fcntl(fd, F_SETFL, val) < 0)
		perror("fcntl set");
}


//初始化vnc client客户端
int vnc_State_Imp::initLibclient()
{
//	return 0;
	char *url[2];
	int i = 2;

	vnc_client = rfbGetClient(8,3,4);
	url[0] = (char*)"vnckey";
	url[1] = (char*)malloc(128);
	sprintf(url[1],"%s:%d",vnc_addr,vnc_port);
	LOG_INFO_FORMAT("INFO - [VNCMS]:initLibclient url %s\n",url[1]);
	
	if (!rfbInitClient(vnc_client,&i,url))
		return -1;	
	return 1;
}


//初始化UDP监听
int vnc_State_Imp::initUDP()
{
	struct sockaddr_in servaddr;
	int opt = 1;
	
	sock_fd = socket(AF_INET, SOCK_DGRAM, 0);
	if (sock_fd == -1)
		return -1;	
	
	//设置地址重用
	setsockopt(sock_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
	//设置fork 后子进程自动关闭套接字
	fcntl(sock_fd,F_SETFD, FD_CLOEXEC);
	//设置非阻塞
	//set_fl(sock_fd, O_NONBLOCK);
	
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port = htons(keylistenport);

	if(bind(sock_fd, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1)
	{
		return -1;
	}
	
	return 1;
}


int vnc_State_Imp::wait_relase()
{
	int repid;
	int count = 0;
	char msg[1024]={0};
	string ret_string;
	LOG_DEBUG("DEBUG - [VNCMS]: reduce thread start \n");
	while(1)
	{
		repid = waitpid(-1,NULL,0);
	
		LOG_DEBUG_FORMAT("DEBUG - [VNCMS] reduce child process PID: %d \n",repid);
		if(vid == repid)
			restartXnvc(repid);
		else if(pid == repid && 2!=status)
		{
			kill(0,SIGTERM);
			do
				{
					count++;
					pid = mysystem(vnc_url.c_str(),(2+Vnc_Config::instance()->chrome_type),0);
					if(count >1)
						break;
				}while(pid<0);
			if(count > 1)
			{				
				TimeoutNotify();
			}				
			count = 0;
		}
		else if(aid == repid)
		{			
			if( 1 == status )
			{
				LOG_INFO_FORMAT("audio process crashed pid %d\n",aid);
			}
			aid = 0;
		}
	}
	return 0;
}

void vnc_State_Imp::restartXnvc(int re_pid)
{
	int ret = 0;
	char vncseverinfo[128] = {0};
	LOG_ERROR_FORMAT("ERROR - [VNCM] [-1532] BBCvnc :%d crashed \n",vnc_index);
	sprintf(vncseverinfo,"rm -rf %s",Xvnc_state_path);
	system(vncseverinfo);
	if(1 == status ||3 ==status)
		vnc_free();
	while(1)
	{
		waitpid(-1,NULL,0);
		usleep(1000*100);
		if(0 == status )
			break;
  }
	LOG_DEBUG("DEBUG - [VNCMS]: kill vncms \n");
	kill(0,SIGKILL);
}

int vnc_State_Imp::operate_Xvnc_path(int operate,int pid)
{
	FILE* fp ;
	int Xvnc_pid = 0;
	char buf[1024]={0};
	
	if(READ == operate)
	{
		if(0 !=access(Xvnc_state_path, R_OK))
			return 0;
		fp = fopen(Xvnc_state_path,"r");
		fread(buf,1023,1,fp);
		fclose(fp);
		return atoi(buf);		
	}	
	else if(WRITE == operate)
	{
		fp = fopen(Xvnc_state_path,"w");
		sprintf(buf,"%d",pid);
		fwrite(buf,1,sizeof(buf),fp);
		fclose(fp);
		return 0;
	}
	return -1;
}

bool vnc_State_Imp::stop_Timeout()
{
	status = 3;
	if(aid>0)
		kill(aid,SIGKILL);
	return true;
}

bool vnc_State_Imp::fresh_Timeout()
{
	LOG_INFO("INFO - [VNCMS]: fresh timeout \n");
	gettimeofday(&last_key_time,NULL);
	return true;
}


bool vnc_State_Imp::timeoutforecast()
{
	struct timeval 	pr_timout;

	memcpy(&pr_timout,&last_key_time,sizeof(timeval));
	/*发送组合键到chrome 显示超时预警信息*/
	//SendPointerEvent(vnc_client,0,0,1);
	//SendPointerEvent(vnc_client,0,0,0);	
	SendKeyEvent(vnc_client,XK_Control_L,1);
	SendKeyEvent(vnc_client,XK_bracketright,1);
	SendKeyEvent(vnc_client,XK_bracketright,0);
	SendKeyEvent(vnc_client,XK_Control_L,0);
	LOG_INFO("INFO - [VNCMS]: Send timeout forecast \n");
	sleep(Vnc_Config::instance()->timeoutforecast);
	if(memcmp(&pr_timout,&last_key_time,sizeof(timeval))) //相同则返回预告失败，超时退出启动
		return true;
	return false;
}

bool vnc_State_Imp::ExistNotify()
{
	int rc;
	Socket sock;
	char buf[1024+1];
	char msg[1024];
	
	sprintf(msg, "XXBBAPP_VNC_EXIST_REQ|%s|%s|%dXXEE",
								sessionid,
								vnc_addr,
								vnc_port);
	Socket_Connector::connect(sock,Vnc_Config::instance()->rsm_ip.c_str(), Vnc_Config::instance()->keytimeoutport);
	rc = sock.write(msg,strlen(msg));
	if(rc <0)
	{
		sock.close();
		return false;
	}

	memset(buf, 0, sizeof(buf));
	if(0>sock.read(buf,1024,1,2))
	{
		sock.close();
		LOG_ERROR_FORMAT("ERROR - [VNCMS]:tcp client send[%s:%d] ExistNotify recv none vnc \n", 
			Vnc_Config::instance()->rsm_ip.c_str(),
			Vnc_Config::instance()->keytimeoutport);
		return false;
	}
	sock.close();
	
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:tcp client recv[%s:%d] ExistNotify recv ret_string=%s\n", 
		Vnc_Config::instance()->rsm_ip.c_str(),
		Vnc_Config::instance()->keytimeoutport,
		buf);
	return true;

}

bool vnc_State_Imp::TimeoutNotify()
{
	int rc;
	Socket sock;
	char buf[1024+1];
	char msg[1024];
	
	sprintf(msg, "XXBBAPP_VNC_TIMEOUT_REQ|%s|%s|%dXXEE",
							sessionid,
							vnc_addr,
							vnc_port);
	Socket_Connector::connect(sock,Vnc_Config::instance()->rsm_ip.c_str(), Vnc_Config::instance()->keytimeoutport);
	rc = sock.write(msg,strlen(msg));
	if(rc <0)
	{
		sock.close();
		return false;
	}

	memset(buf, 0, sizeof(buf));
	if(0>sock.read(buf,1024,1,2))
	{
		sock.close();
		LOG_ERROR_FORMAT("ERROR - [VNCMS]:tcp client send[%s:%d] TimeoutNotify recv none vnc \n", 
			Vnc_Config::instance()->rsm_ip.c_str(),
			Vnc_Config::instance()->keytimeoutport);
		return false;
	}
	sock.close();
	
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:tcp client recv[%s:%d] TimeoutNotify recv ret_string=%s\n", 
		Vnc_Config::instance()->rsm_ip.c_str(),
		Vnc_Config::instance()->keytimeoutport,
		buf);
	if(0 != strcmp(buf, "XXBBAPP_VNC_TIMEOUT_RESP|0XXEE"))
	{
		LOG_ERROR("ERROR - [VNCMS]: TimeoutNotify recv error !\n");
		return false;
	}
	return true;
}




int vnc_State_Imp::checkTimeout()//精确到毫秒级别
{
	int i;
	struct timeval now;
	 unsigned long mtime,etime;
	
	while(1)
	{
		if(status == 1 || status == 3)
		{
			gettimeofday(&now,NULL);
			mtime = (now.tv_sec - last_key_time.tv_sec)*1000*1000 + (now.tv_usec -last_key_time.tv_usec);
			etime = (now.tv_sec - login_time.tv_sec)*1000*1000 + (now.tv_usec -login_time.tv_usec);
			if(etime > existcheck*1000*1000)
			{
				ExistNotify();
				if(existcheck < 300)
					existcheck += existcheck;
				gettimeofday(&login_time,NULL);
			}
		}
		if(status == 1)
		{
			if(mtime >= timeout*1000*1000)
			{
				if(timeoutforecast())
					continue;
//				fresh_Timeout();//等下次超时再发送
				if (TimeoutNotify())
				{
					//sleep(timeout);
				}
			}
		}
		usleep(100*1000);
	}		

	return 0;
}


int vnc_State_Imp::forward_key()//键值接收转换
{
//	return 0;
	int revlen, i;
	char mesg[1024+1];
	
	int shift = 0;
	int caps_lock = 0;
	struct sockaddr_in cliaddr;
	socklen_t addrlen = sizeof(cliaddr);
	struct timeval now;
	int TVnet = Vnc_Config::instance()->TVnet;
	rfbBool send_key_fl = TRUE;
	
	LOG_INFO("INFO - [VNCMS]:forward_key start ok!\n\n");
	
	while(1)
	{
		memset(mesg,0,sizeof(mesg));
		revlen = recvfrom(sock_fd, mesg, sizeof(mesg)-1, 0, (struct sockaddr *)&cliaddr, &addrlen);	   
		if(revlen>0 && ((status == 1)||(status == 3)))
		{
			int key=0;
			gettimeofday(&now,NULL); 
			BlcYunKeyMsgHead* headmsg=(BlcYunKeyMsgHead*)mesg;
			switch (headmsg->dev_type)
			{
				case NblTermKeyDevType_IrrControl :
				{	
					BlcYunKeyIrrMsg* irrmsg = (BlcYunKeyIrrMsg*)mesg;
					key=get_key_value(irrmsg);
					if(0 == key)
						break;
					if(irrmsg->body.key_status==2)
					{
						if((now.tv_sec - last_key_time.tv_sec >= 1) || (Vnc_Config::instance()->key_ignore*1000 < now.tv_usec - last_key_time.tv_usec))
							send_key_fl = SendKeyEvent(vnc_client,key,1);
						else
							break;
					}
					else
						send_key_fl = SendKeyEvent(vnc_client,key,0);
					gettimeofday(&last_key_time,NULL); 
					LOG_DEBUG_FORMAT("DEBUG - [VNCMS]udp recv [%s:%d]:send irrmsg 0x%x , key_status %d  ,send_status %d \n",
														inet_ntoa(cliaddr.sin_addr),
														ntohs(cliaddr.sin_port),
														key,
														irrmsg->body.key_status,
														send_key_fl);
					break;
				}
	
				case NblTermKeyDevType_Mouse :
				{
					if(!TVnet)
						break;
					BlcYunKeyMouseMsg* mousemsg = (BlcYunKeyMouseMsg*)mesg;
					switch(mousemsg->body.key_status)
					{
						case BlcMousePropertyStat_MOUSEDEFAULT:
							break;
						case BlcMousePropertyStat_MOUSEUP:
							break;
						case BlcMousePropertyStat_MOUSEDOWN:
						{
							key = get_mouse_type(mousemsg);
							break;
						}
						case BlcMousePropertyStat_MOUSEWHEEL:
						{
							key=get_wheel_type(mousemsg);
							break;
						}
					}
					send_key_fl = SendPointerEvent(vnc_client,mousemsg->body.x,-mousemsg->body.y,key);
					LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:send key :%d   mouse type :%d   mouse status :%d    \n",key,mousemsg->body.key_value,mousemsg->body.key_status);
					break;
				}
				case NblTermKeyDevType_Keyboard:
				{
					if(!TVnet)
						break;
					BlcYunKeyKeyboardMsg* keboardmsg=(BlcYunKeyKeyboardMsg*)mesg;
					key = get_keyboard_value(keboardmsg,shift);
					LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:recv keygw %d   \n",keboardmsg->body.key_value);
					if(-1 == key)
						break;
					else if(XK_Shift_L == key && keboardmsg->body.key_status == 2)
						shift=1;
					else if(XK_Shift_L == key  && keboardmsg->body.key_status == 3)
						shift=0;
					send_key_fl = SendKeyEvent(vnc_client,key,(keboardmsg->body.key_status>2?0:1));
					LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:send key :%x  key stauts :%d  \n ",key,keboardmsg->body.key_status);
					break;
				}
				default :
					break;
			}
			if(FALSE == send_key_fl)
			{
				LOG_ERROR("ERROR - [VNCMS]: [-1526] vncclient break down \n");
				rfbClientCleanup(vnc_client);
				vnc_client=NULL;
				initLibclient();
			}
		}
	}
}

int vnc_State_Imp::comb(BlcYunKeyIrrMsg* irrmsg)//组合键
{
	int key = 0;
	if(3 == irrmsg->body.key_status)
		return 0;
	switch(irrmsg->body.key_value)
	{
		case 0xf2:	{	key = XK_L; break;}
		case 0xf0:	{	key = XK_T;	break;}
		case 0xec:	{	key = XK_B;	break;}
		case 0xee:	{	key = XK_O;	break;}
		case 0xed:	{	key = XK_G;	break;}
		case 0xf1:	{	key = XK_W;	break;}
		case 0xf5:	{	key = XK_R;	break;}
		case 0xeb:	{	key = XK_C;	break;}
		case 0xf3:	{	key = XK_N;	break;}
		case 0x76:	{	key = XK_M;	break;}
		case 0xe8:	{	key = XK_P;	break;}
		case 0xef:	{	key = XK_S;	break;}
		case 0xe9: 	{	key = XK_V;	break;}
		case 0xea:	{	key = XK_I;	break;}
		case 0x3a:	{	key = XK_X;	break;}
		case 0x3b:	{	key = XK_Y;	break;}
		case 0x3c:	{	key = XK_Z;	break;}
		case 0x3d:	{	key = XK_K;	break;}
		case 0xde:	{	key = XK_H;	break;}
		case 0xdf:	{	key = XK_A;	break;}	
		default :      return 0;
	}
	SendKeyEvent(vnc_client,XK_Alt_L,1);
	SendKeyEvent(vnc_client,key,1);
	SendKeyEvent(vnc_client,key,0);
	SendKeyEvent(vnc_client,XK_Alt_L,0);
	LOG_DEBUG_FORMAT("DEBUG - [VNCMS]:Send comb key ALT + %x  \n",key);
	return 0;
}

int vnc_State_Imp::get_key_value(BlcYunKeyIrrMsg* irrmsg)//遥控器及机顶盒前面板
{
	switch(irrmsg->body.key_value)
	{
		case 82 :		return XK_Up;
		case 81 :		return XK_Down;
		case 80 :		return XK_Left;
		case 79 :		return XK_Right;
		case 40 :		return XK_Return;
		case 0x58 :		return XK_Return;
		case 0x77 :		return XK_Return;
		case 158 :		return XK_Insert;
		case 0x27 :		return XK_0;
		case 0x29:		return XK_Escape;
		case 0x1e :		return XK_1;
		case 0x1f :		return XK_2;
		case 0x20 :		return XK_3;
		case 0x21 :		return XK_4;
		case 0x22 :		return XK_5;
		case 0x23 :		return XK_6;
		case 0x24 :		return XK_7;
		case 0x25 :		return XK_8;
		case 0x26 :		return XK_9;
		case 0x4a:		return XK_Home;
		case 0x4b:		return XK_Page_Up;
		case 0x4e:		return XK_Page_Down;
		case 0x34:		return XK_apostrophe;
		
	/********front panel keygw*********/
		case 0xf6:		return XK_Up;
        case 0xf7:		return XK_Down;
        case 0xf8:		return XK_Left;
        case 0xf9:		return XK_Right;
		case 0xfa:		return XK_Return;
		case 0xfc:		return XK_Insert;
		
		default :		return comb(irrmsg);
	}	
return 0;
}


int vnc_State_Imp::get_keyboard_value(BlcYunKeyKeyboardMsg* keboardmsg,int opt)//键盘
{
	switch(keboardmsg->body.key_value)
	{
		case 0x4 :		if(opt)		return XK_A;	return XK_a;
		case 0x5 :  	if(opt)		return XK_B;	return XK_b;
		case 0x6 :  	if(opt)		return XK_C;	return XK_c;
		case 0x7 :  	if(opt)		return XK_D;	return XK_d;
		case 0x8 :  	if(opt)		return XK_E;	return XK_e;
		case 0x9 :  	if(opt)		return XK_F;	return XK_f;
		case 0xa :  	if(opt)		return XK_G;	return XK_g;
		case 0xb :  	if(opt)		return XK_H;	return XK_h;
		case 0xc :  	if(opt)		return XK_I;	return XK_i;
		case 0xd :  	if(opt)		return XK_J;	return XK_j;
		case 0xe :  	if(opt)		return XK_K;	return XK_k;
		case 0xf :  	if(opt)		return XK_L;	return XK_l;
		case 0x10:  	if(opt)		return XK_M;	return XK_m;
		case 0x11:  	if(opt)		return XK_N;	return XK_n;
		case 0x12:  	if(opt)		return XK_O;	return XK_o;
		case 0x13:  	if(opt)		return XK_P;	return XK_p;
		case 0x14:  	if(opt)		return XK_Q;	return XK_q;
		case 0x15:  	if(opt)		return XK_R;	return XK_r;
		case 0x16:  	if(opt)		return XK_S;	return XK_s;
		case 0x17:  	if(opt)		return XK_T;	return XK_t;
		case 0x18:  	if(opt)		return XK_U;	return XK_u;
		case 0x19:  	if(opt)		return XK_V;	return XK_v;
		case 0x1a: 		if(opt)		return XK_W;	return XK_w;
		case 0x1b: 		if(opt)		return XK_X;	return XK_x;
		case 0x1c: 		if(opt)		return XK_Y;	return XK_y;
		case 0x1d:	 	if(opt)		return XK_Z;	return XK_z;
		case 0x1e: 		if(opt)		return XK_exclam;	return XK_1;
		case 0x1f: 		if(opt)		return XK_at;		return XK_2;
		case 0x20:		if(opt)		return XK_numbersign;	return XK_3;
		case 0x21:		if(opt)		return XK_dollar;	return XK_4;
		case 0x22:		if(opt)		return XK_percent;	return XK_5;
		case 0x23:		if(opt)		return XK_asciicircum;	return XK_6;
		case 0x24:		if(opt)		return XK_ampersand;	return XK_7;
		case 0x25:		if(opt)		return XK_asterisk;	return XK_8;
		case 0x26:		if(opt)		return XK_parenleft;	return XK_9;
		case 0x27:		if(opt)		return XK_parenright;	return XK_0;
		case 0x28:		return XK_KP_Enter;
		case 0x29:		return XK_Escape;
		case 0x2a:		return XK_BackSpace;
		case 0x2b:		return XK_Tab;
		case 0x2c:		return XK_space;
		case 0x2d:		if(opt)		return XK_underscore;	return XK_minus;
		case 0x2e:		if(opt)		return XK_plus;		return XK_equal;
		case 0x2f:		if(opt)		return XK_braceleft;	return XK_bracketleft;
		case 0x30:		if(opt)		return XK_braceright;	return XK_bracketright;
		case 0x31:		if(opt)		return XK_bar;		return XK_backslash;
		case 0x32:		if(opt)		return XK_quoteleft;	return XK_asciitilde;
		case 0x33:		if(opt)		return XK_colon;	return XK_semicolon;
		case 0x34:		if(opt)		return XK_quotedbl;	return XK_apostrophe;
		case 0x35:		if(opt)		return XK_asciitilde;	return XK_grave;
		case 0x36:		if(opt)		return XK_less;		return XK_comma;
		case 0x37:		if(opt)		return XK_greater;	return XK_period;
		case 0x38:		if(opt)		return XK_question;	return XK_slash;
		case 0x39:		return XK_Caps_Lock;
		case 0xaa:		return XK_Alt_L;
		case 0xa9:		return XK_Control_L;
		case 0xa8:		return XK_Shift_L;
		case 0x3a:		return XK_F1;
		case 0x3b:		return XK_F2;
		case 0x3c:		return XK_F3;
		case 0x3d:		return XK_F4;
		case 0x3e:		return XK_F5;
		case 0x3f:		return XK_F6;
		case 0x40:		return XK_F7;
		case 0x41:		return XK_F8;
		case 0x42:		return XK_F9;
		case 0x43:		return XK_F10;
		case 0x44:		return XK_F11;
		case 0x45:		return XK_F12;

		case 0x4a:		return XK_Home;
		case 0x4b:		return XK_Page_Up;
		case 0x4c:		return XK_Delete;
		case 0x4d:		return XK_End;
		case 0x4e:		return XK_Page_Down;
		case 0x4f:		return XK_Right;
		case 0x50:		return XK_Left;
		case 0x51:		return XK_Down;
		case 0x52:		return XK_Up;
		default:		return -1;
	}
}

int vnc_State_Imp::get_wheel_type(BlcYunKeyMouseMsg* mousemsg)//鼠标滚轮
{
	short type = (short)(mousemsg->body.key_value);
	if(type>0)
		return rfbButton4Mask;
	return rfbButton5Mask;


}
int vnc_State_Imp::get_mouse_type(BlcYunKeyMouseMsg* mousemsg)//鼠标
{
	switch(mousemsg->body.key_value)
	{
		case 336:		return rfbButton1Mask;
		case 337:		return rfbButton3Mask;
		case 339:		return 0;
		default :		return 0;
	}
}

