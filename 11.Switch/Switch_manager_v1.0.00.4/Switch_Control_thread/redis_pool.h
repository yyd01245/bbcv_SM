#ifndef _REDIS_POOL_H_
#define _REDIS_POOL_H_

#include <pthread.h>
#include "redis_instance.h"

using namespace std;

class RedisPool{
public:
	RedisPool();
	~RedisPool();

	bool init(const char* redis_ser_ip, int redis_ser_port, const char *redis_ser_password, int conn_num);
	int close();
	int getInstancePos();
	bool executeRedisCommand(redisReply** reply, int &redis_index, const char *format, ...);
	void freeReply(redisReply* preply, int redis_index);
	static void* maintenancePool(void* arg);
	
private:
	int keepConntectionThread();
	void releaseInstancePos(int redis_index);

private:
	char m_redis_ip[16];
	int m_redis_port;
	char m_redis_password[128];

	RedisInstance	*m_redis_conn_list;
	int m_redis_conn_num;

	bool m_stop; 
	pthread_mutex_t m_mutex;
};


#endif

