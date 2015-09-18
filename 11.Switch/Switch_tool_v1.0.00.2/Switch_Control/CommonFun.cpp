#include "CommonFun.h"
#include <string.h>

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <string>


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



int applyforUDPPort(char *ip,int *port)
{
	struct sockaddr_in bindaddr;
	socklen_t len;
	
	struct sockaddr_in servaddr;
	memset(&servaddr,0, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = inet_addr(ip);
	servaddr.sin_port = htons(*port);
	
	int test_socket = socket(AF_INET, SOCK_DGRAM, 0);
	if(test_socket == -1)
		return -1;
	
	int optval = 1;
	if ((setsockopt(test_socket,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(test_socket);
		return -2;
	}

	if(bind(test_socket, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1)
	{
		close(test_socket);
		return -3;
	}
	
	if( 0 == getsockname( test_socket, ( struct sockaddr* )&bindaddr, &len ))
	{
		//printf("%s:%d\n",inet_ntoa(bindaddr.sin_addr),ntohs(bindaddr.sin_port));
		
		*port = ntohs(bindaddr.sin_port);
	}
	else
	{
		close(test_socket);
		return -4;
	}
	
	close(test_socket);
	return 0;
}



int FindDataFromString(const char* strinput,char* strData1,char* strData2)
{
	if(strData1 == NULL || strData2 == NULL || strinput == NULL)
		return -1;
	
	std::string strInputurl;
	strInputurl.assign(strinput);

	int size_type0 = strInputurl.find("//");
	int size_type1 = strInputurl.find(":");
	//printf("  %d  %d \n",size_type0,size_type1);

	std::string tmp = strInputurl.substr(size_type0+2);  //size_type1
	int size_type2 = tmp.find(":");
//	printf("--%d %s \n",size_type2,tmp.substr(0,size_type2).c_str());
	sprintf(strData1,"%s",tmp.substr(0,size_type2).c_str());
	std::string tmp1 = tmp.substr(size_type2+1);
	sprintf(strData2,"%s",tmp1.c_str());

//	printf("---data1=%s data2=%s \n",strData1,strData2);

	return 0;
}


