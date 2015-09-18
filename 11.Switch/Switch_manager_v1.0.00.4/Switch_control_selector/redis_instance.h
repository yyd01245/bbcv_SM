#ifndef _REDIS_INSTANCE_H_
#define _REDIS_INSTANCE_H_
#include <cstdarg>
//#include <stdarg.h>
#include "hiredis.h"

using namespace std;

enum ENUM_REDIS_CONN_STATUS
{
	REDIS_CONN_INIT = 1,		
	REDIS_CONN_RUNNING,			
	REDIS_CONN_FAIL,
};

class RedisInstance{
public:
	RedisInstance();
	~RedisInstance();

	bool open(const char *redis_ser_ip, int redis_ser_port, const char *redis_ser_password);
	void close();

	redisReply* executeRedisCommand(const char *format, va_list va);
	bool reConnect(const char *redis_ser_ip, int redis_ser_port, const char *redis_ser_password);

	ENUM_REDIS_CONN_STATUS getConnStatus();
	int getLastTime();
	int getSeconds();
	int getUseFlag();
	int setUseFlag(int flag);
private:
	bool pingRedis();
	bool authRedis(const char *redis_ser_password);

	redisContext *m_redis_context;
	ENUM_REDIS_CONN_STATUS m_enum_conn;
	int m_last_time;
	int m_use_flag;
};


#endif

