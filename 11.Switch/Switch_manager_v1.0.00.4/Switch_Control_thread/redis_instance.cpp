#include <strings.h>
#include <string.h>
#include <sys/time.h>

#include "redis_instance.h"

RedisInstance::RedisInstance()
{
	m_redis_context = NULL;
	m_enum_conn = REDIS_CONN_INIT;
	m_last_time = 0;
	m_use_flag = 0;
}

RedisInstance::~RedisInstance()
{
	close();
}

bool RedisInstance::open(const char *redis_ser_ip, int redis_ser_port, const char *redis_ser_password)
{
	if (m_redis_context != NULL) return false;

	struct timeval timeout = { 1, 500000 }; // 1.5 seconds
	m_redis_context = redisConnectWithTimeout(redis_ser_ip, redis_ser_port, timeout);
	if (m_redis_context->err) {
		return false;
	}

	/* Set read/write timeout on a blocking socket. */
    struct timeval tv = { 3, 0 };// 3.0 seconds
    if(redisSetTimeout(m_redis_context,tv) != REDIS_OK) return false;

	/* Authentication */
	if (!authRedis(redis_ser_password)) return false;

	/* PING server */
	if (!pingRedis()) return false;

	return true;
}

bool RedisInstance::authRedis(const char *redis_ser_password)
{
	if (strlen(redis_ser_password) == 0) return true;
	
	redisReply *reply = (redisReply*)redisCommand(m_redis_context, "AUTH %s", redis_ser_password);
	if (reply == NULL )	return false;
	
	if(reply->type == REDIS_REPLY_ERROR)/* Authentication failed */
	{
		return false;
	}
	freeReplyObject(reply);

	return true;
}

bool RedisInstance::pingRedis()
{
	redisReply *reply = (redisReply*)redisCommand(m_redis_context, "PING");
	if (reply == NULL )	return false;

	if(reply->type == REDIS_REPLY_STATUS && strcasecmp(reply->str,"pong") == 0)
	{
		m_enum_conn = REDIS_CONN_RUNNING;
		m_last_time = getSeconds();
		freeReplyObject(reply);

    	return true;
	}
	freeReplyObject(reply);
	
	return false;
}

void RedisInstance::close()
{
	if (m_redis_context != NULL)
	{
		redisFree(m_redis_context);
		m_redis_context = NULL;
	}
}

redisReply* RedisInstance::executeRedisCommand(const char *format, va_list va)
{
	if (m_redis_context == NULL || m_enum_conn != REDIS_CONN_RUNNING) return NULL;

	redisReply* reply = (redisReply*)redisCommand(m_redis_context, format, va);
	if (reply == NULL)
	{
		switch(m_redis_context->err)
		{
		case REDIS_ERR_PROTOCOL:
		case REDIS_ERR_IO:
		case REDIS_ERR_EOF:
		case REDIS_ERR_OTHER:
			m_enum_conn = REDIS_CONN_FAIL;
			close();
			break;
		default:
			break;
		}
	}

	m_last_time = getSeconds();

	return reply;
}

bool RedisInstance::reConnect(const char *redis_ser_ip, int redis_ser_port, const char *redis_ser_password)
{
	close();
	return open(redis_ser_ip, redis_ser_port, redis_ser_password);
}

ENUM_REDIS_CONN_STATUS RedisInstance::getConnStatus()
{
	return m_enum_conn;
}

int RedisInstance::getLastTime()
{
	return m_last_time;
}

int RedisInstance::getSeconds()
{
	struct timeval tv;
	gettimeofday(&tv, NULL);
	return tv.tv_sec;
}

int RedisInstance::getUseFlag()
{
	return m_use_flag;
}

int RedisInstance::setUseFlag(int flag)
{
	m_use_flag = flag;
	return 0;
}


