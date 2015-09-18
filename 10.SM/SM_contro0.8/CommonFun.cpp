#include "CommonFun.h"
#include "Config.h"
#include "cJSON.h"
#include "DBInterface.h"
#include "CommonData.h"

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
	PropConfig cfg;
	if(cfg.init(ConfigFile)==false) return false;	
	strcpy(strAdverIP,cfg.getValue("strAdverIP").c_str());
	printf("strAdverIP = %s\n",strAdverIP);
	iAdverPort= atoi(cfg.getValue("iAdverPort").c_str());
	printf("iAdverPort = %d\n",iAdverPort);

	strcpy(strNavIP,cfg.getValue("strNavIP").c_str());
	printf("strNavIP = %s\n",strNavIP);
	iNavPort= atoi(cfg.getValue("iNavPort").c_str());
	printf("iNavPort = %d\n",iNavPort);

	strcpy(strVodIP,cfg.getValue("strVodIP").c_str());
	printf("strVodIP = %s\n",strVodIP);
	iVodPort= atoi(cfg.getValue("iVodPort").c_str());
	printf("iVodPort = %d\n",iVodPort);

	strcpy(strMsiIP,cfg.getValue("strMsiIP").c_str());
	printf("strMsiIP = %s\n",strMsiIP);
	iMsiPort= atoi(cfg.getValue("iMsiPort").c_str());
	printf("iMsiPort = %d\n",iMsiPort);

	strcpy(strMyServerIP,cfg.getValue("strMyServerIP").c_str());
	printf("strMyServerIP = %s\n",strMyServerIP);
	iMSIServerPort= atoi(cfg.getValue("iMSIServerPort").c_str());
	printf("iMSIServerPort = %d\n",iMSIServerPort);

	strcpy(strVOD_KeyIP,cfg.getValue("strVOD_KeyIP").c_str());
	printf("strVOD_KeyIP = %s\n",strVOD_KeyIP);
	
	strcpy(dbip,cfg.getValue("dbip").c_str());
	printf("dbip = %s\n",dbip);

	strcpy(dbname,cfg.getValue("dbname").c_str());
	printf("dbname = %s\n",dbname);

	strcpy(dbuser,cfg.getValue("dbuser").c_str());
	printf("dbuser = %s\n",dbuser);

	strcpy(dbpass,cfg.getValue("dbpass").c_str());
	printf("dbpass = %s\n",dbpass);

/*	
	i = atoi(cfg.getValue("num").c_str());
	for(j=0;j<i;j++)
	{
		char buf[32]={0};
		char buff[32] = {0};
		sprintf(buf,"CIRegionID%d",j);
		sprintf(buff,"Iport%d",j);
		
	}
*/	
	printf("--lode is ok\n");
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

