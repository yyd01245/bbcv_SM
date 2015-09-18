#include "cRead.h"
#include "cConfig.h"
#include "cJSON.h"
#include "cDatabase.h"
#include "cCommon.h"

/*!
* \brief  将字符串中的一段用另外一段替换,注意strbuf必须有足够的空间来存储替换后的串,否则可能内存溢出
*
* \param
*      strbuf : 字符串(输入/输出)
*      src_str : 目标字符子串
*      desc_str     : 替换后的子串
* \return
*      替换后的字符串
*
*/
bool init(const char * ConfigFile)
{

	printf("---begin to loud configfile\n");
	fflush(stdout);
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false) return false;	
	//本机ip port
	strcpy(mulip,cfg.getValue("mulip").c_str());
	printf("mulip = %s\n",mulip);
	fflush(stdout);
	mulport= atoi(cfg.getValue("mulport").c_str());
	printf("mulport = %d\n",mulport);
	fflush(stdout);
	//SM ip port
	strcpy(smip,cfg.getValue("smip").c_str());
	printf("smip = %s\n",smip);
	fflush(stdout);
	smport = atoi(cfg.getValue("smport").c_str());
	printf("smport = %d\n",smport);
	fflush(stdout);
	//数据库
	strcpy(dbip,cfg.getValue("dbip").c_str());
	printf("dbip = %s\n",dbip);
	fflush(stdout);
	strcpy(dbname,cfg.getValue("dbname").c_str());
	printf("dbname = %s\n",dbname);
	fflush(stdout);
	strcpy(dbuser,cfg.getValue("dbuser").c_str());
	printf("dbuser = %s\n",dbuser);
	fflush(stdout);
	strcpy(dbpass,cfg.getValue("dbpass").c_str());
	printf("dbpass = %s\n",dbpass);
	fflush(stdout);

	printf("--lode is ok\n");
	fflush(stdout);

	
	return true;

}
char * replace(char *strbuf, const char *src_str, const char *desc_str)
{

	char *pos, *pos1;

	if (strbuf == NULL || src_str == NULL || desc_str == NULL) return strbuf;

	if (strlen(src_str) == 0) return strbuf;
	int ilen = strlen(strbuf);
	char *org = new char[ilen + 1];

	strcpy(org, strbuf);
	pos = org;
	strbuf[0] = 0;
	while (1)
	{
		pos1 = strstr(pos, src_str);
		if (pos1 == NULL)
			break;
		*pos1 = '\0';
		strcat(strbuf,  pos);
		strcat(strbuf, desc_str);
		pos = pos1 + strlen(src_str);
	}
	strcat(strbuf,  pos);
	delete org;
	return strbuf;
}

