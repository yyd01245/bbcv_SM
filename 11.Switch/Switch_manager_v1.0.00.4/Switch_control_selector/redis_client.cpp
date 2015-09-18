#include "redis_client.h"

bool Redis_Client::init(const char* redis_ser_ip, int redis_ser_port, const char* redis_ser_password, int conn_num)
{
	if(!redis_pool.init(redis_ser_ip, redis_ser_port, redis_ser_password, conn_num))
	{
		LOG_TRACE("TRACE :Create redis_pool fail !\n");
		return false;
	}
	
	LOG_TRACE("TRACE :Create redis_pool ok !\n");
	return true;
}

int Redis_Client::setvalue(const string &key, const string &value)
{
	if(key.find(' ',0) != string::npos || value.find(' ',0) != string::npos)
	{
		return -1;
	}
	
	redisReply * reply = NULL;
	int index = 0;
	char cmd[4096];
	
	sprintf(cmd, "SET %s %s", key.c_str(), value.c_str());
	//if(!redis_pool.executeRedisCommand(&reply, index,"SET %s %s", key.c_str(), value.c_str()))
	if(!redis_pool.executeRedisCommand(&reply, index, cmd))//exe fail reply = null
	{
		LOG_TRACE_FORMAT("TRACE :setvalue error:reply=null, key = %s,value = %s\n", key.c_str(),value.c_str());
		redis_pool.freeReply(reply, index);
		return -2;
	}

	if (reply->type == REDIS_REPLY_STATUS && strcasecmp(reply->str,"ok") == 0)//set ok
	{
		LOG_TRACE_FORMAT("TRACE :setvalue ok, key = %s,value = %s\n", key.c_str(),value.c_str());
			redis_pool.freeReply(reply, index);
		return 0;
	}
	else//set fail, look at reply->type
	{
		LOG_TRACE_FORMAT("TRACE :setvalue error:status=%d, key = %s,value = %s\n", reply->type, key.c_str(),value.c_str());
			redis_pool.freeReply(reply, index);
		return -3;
	}
}

int  Redis_Client::getvalue(const string &key, string &value)
{
	if(key.find(' ',0) != string::npos)
	{
		return -1;
	}

	redisReply * reply = NULL;
	int index = 0;
	char cmd[4096];
	
	sprintf(cmd, "GET %s", key.c_str());
	if(!redis_pool.executeRedisCommand(&reply, index, cmd))//exe fail reply = null
	{
		LOG_TRACE_FORMAT("TRACE :getvalue error:reply=null, key = %s\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return -2;
	}

	if (reply->type == REDIS_REPLY_STRING)// && reply->str != NULL
	{
		value = reply->str;// include reply->str = ""
		LOG_TRACE_FORMAT("TRACE :getvalue ok, key = %s; value = %s\n", key.c_str(), value.c_str());
		redis_pool.freeReply(reply, index);
		return 0;
	}
	else if(reply->type == REDIS_REPLY_NIL)//get fail, null
	{
		LOG_TRACE_FORMAT("TRACE :getvalue error:REDIS_REPLY_NIL, key = %s; value = (nil)\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return -3;
	}
	else//get fail, look at reply->type
	{
		LOG_TRACE_FORMAT("TRACE :getvalue error:%d, key = %s\n", reply->type, key.c_str());
		redis_pool.freeReply(reply, index);
		return -4;
	}
}

int Redis_Client::delvalue(const string &key)
{
	if(key.find(' ',0) != string::npos)
	{
		return -1;
	}

	redisReply * reply = NULL;
	int index = 0;

	char cmd[4096];
	sprintf(cmd, "DEL %s", key.c_str());
	if(!redis_pool.executeRedisCommand(&reply, index, cmd))//exe fail reply = null
	{
		LOG_TRACE_FORMAT("TRACE :delvalue error:reply=null, key = %s\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return -2;
	}

	if(reply->type == REDIS_REPLY_INTEGER)//del ok(reply->integer = 1) or aready deleted(reply->integer = 0) 
	{
		LOG_TRACE_FORMAT("TRACE :delvalue ok, key = %s\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return 0;
	}
	else//del fail, look at reply->type
	{
		LOG_TRACE_FORMAT("TRACE :delvalue error:%d, key = %s\n", reply->type, key.c_str());
		redis_pool.freeReply(reply, index);
		return -3;
	}
}

int Redis_Client::getkeysvalue(const string &key,vector<string> &value)
{
	if(key.find(' ',0) != string::npos)
	{
		return -1;
	}

	redisReply * reply = NULL;
	int index = 0;
	char cmd[4096];
	
	sprintf(cmd, "KEYS %s", key.c_str());
	if(!redis_pool.executeRedisCommand(&reply, index, cmd))//exe fail reply = null
	{
		LOG_TRACE_FORMAT("TRACE :getkeysvalue error:reply=null, keys = %s\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return -2;
	}

	if (reply->type == REDIS_REPLY_ARRAY)// && reply->str != NULL
	{
		//value = reply->str;// include reply->str = ""
		for(int i = 0;i < reply->elements;i++)
			value.push_back(reply->element[i]->str);
		
		LOG_TRACE_FORMAT("TRACE :getkeysvalue ok, key = %s;value_num = %d \n", key.c_str(),reply->elements);
		redis_pool.freeReply(reply, index);
		return 0;
	}
	else if(reply->type == REDIS_REPLY_NIL)//get fail, null
	{
		LOG_TRACE_FORMAT("TRACE :getkeysvalue error:REDIS_REPLY_NIL, key = %s; value = (nil)\n", key.c_str());
		redis_pool.freeReply(reply, index);
		return -3;
	}
	else//get fail, look at reply->type
	{
		LOG_TRACE_FORMAT("TRACE :getkeysvalue error:%d, key = %s\n", reply->type, key.c_str());
		redis_pool.freeReply(reply, index);
		return -4;
	}
}

int Redis_Client::expireValue(const string &key, int seconds)
{
	if(key.find(' ',0) != string::npos || seconds < 0)
	{
		return -1;
	}

	redisReply * reply = NULL;
	int index = 0;
	char cmd[4096];
	
	sprintf(cmd, "EXPIRE %s %d", key.c_str(), seconds);
	//if(!redis_pool.executeRedisCommand(&reply, index,"EXPIRE %s %d", key.c_str(), seconds))
	if(!redis_pool.executeRedisCommand(&reply, index, cmd))//exe fail reply = null
	{
		LOG_TRACE_FORMAT("TRACE :expire error:reply=null, key = %s, seconds= %d\n", key.c_str(), seconds);
		redis_pool.freeReply(reply, index);
		return -2;
	}

	if (reply->type == REDIS_REPLY_INTEGER)// ok
	{
		LOG_TRACE_FORMAT("TRACE :expire ok, key = %s, seconds = %d\n", key.c_str(), seconds);
			redis_pool.freeReply(reply, index);
		return 0;
	}
	else//fail, look at reply->type
	{
		LOG_TRACE_FORMAT("TRACE :expire error:status=%d, key = %s, seconds= %d\n", reply->type, key.c_str(), seconds);
			redis_pool.freeReply(reply, index);
		return -3;
	}
}

