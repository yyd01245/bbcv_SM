#ifndef	_REDIS_CLIENT_H_
#define	_REDIS_CLIENT_H_

#include <stdlib.h>
#include <errno.h>
#include <ctype.h>
#include <time.h>
#include <sys/time.h>
#include <map>
#include <string>
#include <vector>

#include "redis_pool.h"

#include "Log.h"

using namespace std;

class Redis_Client
{
public:
	Redis_Client(){};

	bool init(const char* redis_ser_ip, int redis_ser_port, const char* redis_ser_password, int conn_num);
	int setvalue(const string &key, const string &value);
	int getvalue(const string &key, string &value);
	int delvalue(const string &key);
	int getkeysvalue(const string &key,vector<string> &value);
	int expireValue(const string &key, int seconds);
private:
	RedisPool redis_pool;
};

#endif 



