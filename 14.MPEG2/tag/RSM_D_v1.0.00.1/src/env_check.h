#ifndef ENV_CHECK_H
#define ENV_CHECK_H

namespace EnvCheck
{
	extern int MyPopen(char *buff,const int maxlen,const char *cmd);
	extern int get_MAC(char *mac,const char *netname);
	extern int get_hardware(char* hdd,int len);
	extern int get_key(const char *netname);
	extern int match_key(const char *netname);
}


#endif

