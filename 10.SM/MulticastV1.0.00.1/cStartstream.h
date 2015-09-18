#ifndef _CSTARTSTREAM_H_
#define _CSTARTSTREAM_H_

#include <stdio.h>
#include "cCommon.h"
#include "cDatabase.h"

class Stream_manage
{
public:
	Stream_manage();
	~Stream_manage()
	{
			
	}
	bool startonestream(char *advname,char *advip,char *advport,int itemp = 0);
	bool stoponestream(int temp=0);
	bool keepalive();
	bool Dealchange(Dbadvstr *pstr,char *cadvname,char *caip,char *caport);
	bool Initstart();
	bool CheckUpdate();
	static void *pthread_ts_startstream(void *arg);
	
	Onestream mulicat;
	Mapadv advmapinfo;
	Strpid mstrpid;
	char iadvname[128];
	char iadvip[128];
	char iadvport[128];
};

#endif