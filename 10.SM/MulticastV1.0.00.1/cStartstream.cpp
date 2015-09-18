#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

#include "cStartstream.h"

Stream_manage::Stream_manage()
{
	iadvname[128] = {0};
	iadvip[128] = {0};
	iadvport[128] = {0};
	
	printf("to clear the mulicat\n");
	fflush(stdout);
//	mstrpid.clear();
}

void *Stream_manage::pthread_ts_startstream(void *arg)
{
	Stream_manage *this0 = (Stream_manage*)arg;
	char cmd[128],buff[128],pid[64];
	int iPID;
	
	memset(cmd,0,sizeof(cmd));
	memset(buff,0,sizeof(buff));
	memset(pid,0,sizeof(pid));
	
	sprintf(cmd,"cvlc -vvv %s --sout udp://%s:%s --loop",this0->iadvname,this0->iadvip,this0->iadvport);
	printf("%s\n",cmd);
	system(cmd);
}

bool Stream_manage::Initstart()
{
	char buff[128];
	//char *ref = NULL;
	int pid;
	char spid[64] = {0};
	DBinterfacer::GetInstance()->LoadAllAdvResource(advmapinfo);

	usleep(1000*1000);
	
	Mapadv::iterator itin = advmapinfo.begin();
	while(itin != advmapinfo.end())
	{
		FILE *pstr;
		char *ref = NULL;
		Dbadvstr *temp = itin->second;
		sprintf(buff, "ps -ef | grep %s|grep %s|grep -v \"grep\"|grep \"dummy\" |awk '{printf $2;printf\"\\n\"}'",temp->advip,temp->advport);
		printf("---buf = %s\n",buff);
		pstr = popen(buff,"r");
		ref = fgets(spid,512,pstr);
		pclose(pstr);
		if(!ref)
		{
			FILE *pstr1;
			memcpy(iadvname,temp->advname,strlen(temp->advname)+1);
			memcpy(iadvip,temp->advip,strlen(temp->advip)+1);
			memcpy(iadvport,temp->advport,strlen(temp->advport)+1);

			pthread_t Parse_recv_thread;
			pthread_create(&Parse_recv_thread,NULL,pthread_ts_startstream,this);
			pthread_detach(Parse_recv_thread);

			usleep(1000*2000);
			pstr1 = popen(buff,"r");
			printf("buff = %s\n",buff);
			ref = fgets(spid,512,pstr1);
			pid = atoi(spid);
			pclose(pstr1);			
			mstrpid.insert(Strpid::value_type(itin->first,pid));
			printf("pid = %d\n",pid);
		}
		else
		{
			printf("spid = %s\n",spid);
			pid = atoi(spid);
			mstrpid.insert(Strpid::value_type(itin->first,pid));
			printf("---this is the streaid = %d\n",itin->first);
			++itin;
			continue;
		}
		++itin;
	}
}
bool Stream_manage::startonestream(char *advname,char *advip,char *advport,int itemp)
{
	FILE *pstr;
	char spid[64];
	int pid;
	memcpy(iadvname,advname,strlen(advname)+1);

	memcpy(iadvip,advip,strlen(advip)+1);
	
	memcpy(iadvport,advport,strlen(advport)+1);
	char buff[128] = {0};
	
	pthread_t Parse_recv_thread1;
	pthread_create(&Parse_recv_thread1,NULL,pthread_ts_startstream,this);
    pthread_detach(Parse_recv_thread1);
	
	usleep(1000*1000);
	sprintf(buff, "ps -ef | grep %s|grep %s|grep -v \"grep\"|grep \"dummy\" |awk '{printf $2;printf\"\\n\"}'",advip,advport);

	pstr = popen(buff,"r");
	fgets(spid,512,pstr);
	pid = atoi(spid);
	pclose(pstr);

	Strpid::iterator itfid = mstrpid.find(itemp);
	if(itfid != mstrpid.end())
	{
		printf("----12344this is pid = %d\n",pid);
		itfid->second = pid;
	}
	else
	{
		printf("---this is error\n");
		fflush(stdout);
	}
	
	return true;	
}

bool Stream_manage::stoponestream(int temp)
{
	int pid; 
	printf("123456hello world\n");
	fflush(stdout);
	if(!temp)
	{
		Strpid::iterator iter = mstrpid.begin();
		printf("11111\n");
		while(iter != mstrpid.end())
		{
			//删除所有的广告流
			printf("get group info\n");
			fflush(stdout);
			char cmd[128] = {0};

			sprintf(cmd,"kill -9 %d",iter->second);
			
			printf("%s\n",cmd);
			fflush(stdout);
			
			system(cmd);
			
			usleep(1000*500);
			++iter;
		}
		mstrpid.clear();
		advmapinfo.clear();
	}
	else
	{	
		Strpid::iterator it = mstrpid.find(temp);
		if(it != mstrpid.end())
		{
			FILE *pstr;
			char buf[128] = {0};
			sprintf(buf,"kill -9 %d",it->second);
			printf("%s\n",buf);
			fflush(stdout);
			pstr= popen(buf, "r");
			pclose(pstr);
			//mstrpid.erase(it);
		}
		else
		{
			printf("there is no this procsss\n");
			return false;
		}
	}
	return true;
}

bool Stream_manage::keepalive()
{
	FILE *pstr;
	char cmd[128] = {0};
	char ipid[64] = {0};
	char *ref = NULL;
	Mapadv::iterator itadv = advmapinfo.begin();
	
	while(itadv != advmapinfo.end())
	{
		Dbadvstr *temp = itadv->second;
		sprintf(cmd,"ps -ef|grep %s|grep %s|grep %s|grep -v \"grep\"|grep \"dummy\"|awk '{printf $2;printf\"\\n\"}'",temp->advname,temp->advip,temp->advport);
		printf("---cmd = %s\n",cmd);
		pstr = popen(cmd,"r");
		ref = fgets(ipid,128,pstr);
		pclose(pstr);
		if(NULL == ref)
		{
			if(advmapinfo.begin() != advmapinfo.end())
			{	
				usleep(1000*2000);
				startonestream(temp->advname,temp->advip,temp->advport,itadv->first);
			}
		}
		else
		{
			Strpid::iterator sitfid = mstrpid.find(temp->streamid);
			if(sitfid != mstrpid.end())
			{
				int mpid = atoi(ipid);
				if(sitfid->second != mpid)
				    sitfid->second = atoi(ipid);
			}
		}
		++itadv;
		usleep(1000*1000);
	}
}

bool Stream_manage::CheckUpdate()
{
	int flag = 0;
	char cadvname[128]= {0};
	char caip[64] = {0};
	char caport[64] = {0};
	Mapadv::iterator itcp = advmapinfo.begin();
	
	while(itcp != advmapinfo.end())
	{
		Dbadvstr * temp = itcp->second;
		memcpy(cadvname,temp->advname,strlen(temp->advname)+1);
		memcpy(caip,temp->advip,strlen(temp->advip)+1);
		memcpy(caport,temp->advport,strlen(temp->advport)+1);
		
		printf("to check the streamid = %d,temp->advname = %s,temp->advip = %s,temp->advport = %s\n",temp->streamid,cadvname,caip,caip,caport);
			
		flag = DBinterfacer::GetInstance()->SelectAdvDb(itcp->first,advmapinfo);
		if(flag == 1)
		{
			printf("name = %s,ip = %s,advport = %s\n",temp->advname,temp->advip,temp->advport);
			Dealchange(itcp->second,cadvname,caip,caport);
			usleep(1000*2000);
		}
		++itcp;
	}
}

bool Stream_manage::Dealchange(Dbadvstr *pstr,char *cadvname,char *caip,char *caport)
{
	int frepeat = 0;
	int mpid = 0;
	int flagagin = 0;
	int streamid = pstr->streamid;
	Strpid::iterator strit = mstrpid.find(streamid);
	mpid = strit->second;
	printf("mpid = %d\n",mpid);
	Strpid::iterator iterflag = mstrpid.begin();
	
	while(iterflag != mstrpid.end())
	{
		if((mpid == iterflag->second)&&(streamid != iterflag->first))
		{
			flagagin = DBinterfacer::GetInstance()->Selectsame(cadvname,caip,caport);
			if(flagagin)
			{
				printf("this should not be killed\n");
				frepeat = 1;
				break;
			}
			else
			{
				printf("this should be killed\n");
				frepeat = 0;
				break;
			}
		}
		else
			++iterflag;
	}
	if(!frepeat)
	{
		printf("to stop stoponestream\n");
		stoponestream(streamid);
	}
}
