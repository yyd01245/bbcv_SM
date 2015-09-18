#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>

#include "redis_pool.h"

RedisPool::RedisPool()
{
	memset(m_redis_ip, 0, sizeof(m_redis_ip));
	memset(m_redis_password, 0, sizeof(m_redis_password));
	m_redis_port = 0;

	m_redis_conn_num = 0;
	m_redis_conn_list = NULL;

	m_stop = true; 
	pthread_mutex_init(&m_mutex, 0);
}

RedisPool::~RedisPool()
{
	close();
	pthread_mutex_destroy(&m_mutex);
}

bool RedisPool::init(const char* redis_ser_ip, int redis_ser_port, const char *redis_ser_password, int conn_num)
{
	if (m_redis_conn_num != 0 && m_redis_conn_list != NULL) return false;

	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setstacksize(&attr,1048576);
	pthread_t id =	0;

	strncpy(m_redis_ip, redis_ser_ip, strlen(redis_ser_ip));
	strncpy(m_redis_password, redis_ser_password, sizeof(m_redis_password));
	m_redis_port = redis_ser_port;
	m_redis_conn_num = conn_num;

	m_redis_conn_list = new RedisInstance[m_redis_conn_num];
	if (m_redis_conn_list == NULL) return false;

	for (int i = 0; i < m_redis_conn_num; i ++)
	{
		if (!m_redis_conn_list[i].open(redis_ser_ip, redis_ser_port, m_redis_password))
		{
			return false;
		}
	}

	if (pthread_create(&id, &attr, &maintenancePool, this) != 0) 
		return false;

	return true;
}

int RedisPool::close()
{
	if (m_redis_conn_list != NULL)
	{
		delete [] m_redis_conn_list;
		m_redis_conn_list = NULL;
	}
	return 0;
}

int RedisPool::getInstancePos()
{
	pthread_mutex_lock(&m_mutex);
  	//srand (time(NULL));
	//return rand() % m_redis_conn_num;

	for (int i = 0; i < m_redis_conn_num; i++)
	{
		RedisInstance &redis_instance = m_redis_conn_list[i];
		if (redis_instance.getUseFlag() == 0 
			&& redis_instance.getConnStatus() == REDIS_CONN_RUNNING)
		{
			redis_instance.setUseFlag(1);
			pthread_mutex_unlock(&m_mutex);
			return i;
		}
	}
	pthread_mutex_unlock(&m_mutex);

	return -1;
}

void RedisPool::releaseInstancePos(int redis_index)
{
	pthread_mutex_lock(&m_mutex);

	RedisInstance &redis_instance = m_redis_conn_list[redis_index];
	redis_instance.setUseFlag(0);
	
	pthread_mutex_unlock(&m_mutex);
}

bool RedisPool::executeRedisCommand(redisReply** reply, int &redis_index, const char *format, ...)
{
	redis_index = getInstancePos();
	if (redis_index == -1) return false;

	va_list lap;
	va_start(lap, format);

	RedisInstance &redis_instance = m_redis_conn_list[redis_index];
	*reply = (redisReply*)redis_instance.executeRedisCommand(format, lap);
	//redis_instance.setUseFlag(0);

	va_end(lap);

	if (*reply == NULL) return false;

	return true;
}

void RedisPool::freeReply(redisReply* preply, int redis_index)
{
	if(NULL != preply)
	{
		freeReplyObject(preply);
		preply = NULL;
	}

	releaseInstancePos(redis_index);
}

void* RedisPool::maintenancePool(void* arg)
{
	RedisPool* pThread = static_cast<RedisPool*>(arg);
	if (pThread != NULL)
	{
		pThread->keepConntectionThread();
	}
}

int RedisPool::keepConntectionThread()
{
	m_stop = false;
	while (!m_stop)
	{
		sleep(30);
		for (int i = 0; i < m_redis_conn_num; i++)
		{
			usleep(10000);
			pthread_mutex_lock(&m_mutex);

			RedisInstance &redis_instance = m_redis_conn_list[i];
			ENUM_REDIS_CONN_STATUS conn_status = redis_instance.getConnStatus();
			switch(conn_status)
			{
			case REDIS_CONN_INIT:
			case REDIS_CONN_FAIL:
				redis_instance.reConnect(m_redis_ip, m_redis_port, m_redis_password);
				break;
			case REDIS_CONN_RUNNING:
				if (redis_instance.getUseFlag() == 0 
					&& (redis_instance.getSeconds() - redis_instance.getLastTime()) >= 30 )  //30s
				{
					redisReply *reply = (redisReply*)redis_instance.executeRedisCommand("PING", NULL);// not care PONG
					if (reply != NULL)
					{
						freeReplyObject(reply);
						reply = NULL;
					}
				}
				break;
			default:
				break;
			}
			pthread_mutex_unlock(&m_mutex);
		}
	}

	return 0;
}


