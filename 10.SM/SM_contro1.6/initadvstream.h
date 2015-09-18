#ifndef _INITADVSTREAM_H_
#define _INITADVSTREAM_H_

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <string.h>
#include <iconv.h>
#include <locale.h>

#include "cJSON.h"
#include <map>
#include "Stream.h"
#include "SM_Manager.h"



using namespace std;
class Advmul_stream : public Stream
{
public:
	Advmul_stream();
	~Advmul_stream()
	{

	}
	bool CleanmulStream();
	static void *ts_recv_Mul_thread(void *arg);
	bool Connect_mulServer();
	bool StartadvStream(char *iadvname,char *iadvip,char *iadvport);
	
	static void *Parse_recv_Mul_thread(void *arg);

private:
	//输出端口ip
	char m_NavUrl[512];

	//接收模块回复的端口
	int m_iNavPort;
	SM_Manager *m_pManager;
};

#endif
