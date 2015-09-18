#ifndef _CRECV_H_
#define _CRECV_H_

#include "cStartstream.h"
#include "cCommon.h"
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>

class Stream_manage;
using namespace std;
class SM_Recv
{
 public:
	SM_Recv()
	{
		advstream = new Stream_manage;
	}
	~SM_Recv()
	{

	}
	char * replace(char *strbuf, const char *src_str, const char *desc_str);
	static void *Parse_recv_thread(void * arg);
	static void *ts_recv_thread(void *p);
	static void *keep_alive_thread(void *p);
	static void *check_database_thread(void *arg);
	bool recv_func();
	int c_MulacceptSocket;
	Stream_manage* advstream;
};

#endif
