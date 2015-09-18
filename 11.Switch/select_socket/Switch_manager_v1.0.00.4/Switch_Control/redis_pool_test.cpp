//g++   redis_instance.cpp  redis_pool.cpp  redis_pool_test.cpp  ./libhiredis.a -lpthread
#include <vector>
#include "redis_pool.h"

using namespace std;

int main()
{
	redisReply * reply = NULL;
	int index = 0;
	RedisPool redis_client;
	redis_client.init((char*)"192.168.60.238", 6379, "",10);

	if(redis_client.executeRedisCommand(&reply, index,"SET foo helloworld"))
	{
		printf("SET: %s, t=%d\n", reply->str, reply->type);
		redis_client.freeReply(reply, index);
	}

	if(redis_client.executeRedisCommand(&reply,index,"GET foo"))
	{
		printf("GET foo: %s, t=%d\n", reply->str, reply->type);
		redis_client.freeReply(reply, index);
	}

	if(redis_client.executeRedisCommand(&reply,index,"DEL foo"))
	{
		printf("DEL 1 foo: %s, t=%d, l=%d\n", reply->str, reply->type, reply->integer);
		redis_client.freeReply(reply, index);
	}
	
	if(redis_client.executeRedisCommand(&reply,index,"DEL foo"))
	{
		printf("DEL 2 foo: %s, t=%d, l = %d\n", reply->str, reply->type, reply->integer);
		redis_client.freeReply(reply, index);
	}

	while (1)
	{
	}

	return 0;
}

