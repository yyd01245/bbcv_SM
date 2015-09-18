#include "env_check.h"

#include <stdio.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <net/if.h>
#include <time.h>
#include <stdlib.h>

#define MAC_LEN 32
#define HDDR_LEN 128
#define FORMAT 5  //加密方式   --采用原有数据每字节加上规定大小


int EnvCheck::MyPopen(char *buff,const int maxlen,const char *cmd)
{
	FILE	*read_fp;
	int		chars_read;	
	
    read_fp = popen(cmd, "r");
    if ( NULL == read_fp )
	{
		perror("popen error");
		return -1;
	}
	chars_read = fread(buff, sizeof(char), maxlen, read_fp);
	if (chars_read < 0)
	{
		printf("popen return NULL \n");
		return -2;
	}
	pclose(read_fp);
	return 0;
}



int EnvCheck::get_MAC(char *mac,const char *netname)//获取物理地址
{
	int i;
    
    /* get mac */
    struct ifreq ifreq;
    int sock;
    
    if((sock=socket(AF_INET,SOCK_STREAM,0))<0)
    {
        perror("error");
        return 2;
    }
    strcpy(ifreq.ifr_name,netname);
    if(ioctl(sock,SIOCGIFHWADDR,&ifreq)<0)
    {
        perror("error:");
        return 3;
    }
/*    printf("%02x%02x%02x%02x%02x%02x ",
                (unsigned char)ifreq.ifr_hwaddr.sa_data[0],
                (unsigned char)ifreq.ifr_hwaddr.sa_data[1],
                (unsigned char)ifreq.ifr_hwaddr.sa_data[2],
                (unsigned char)ifreq.ifr_hwaddr.sa_data[3],
                (unsigned char)ifreq.ifr_hwaddr.sa_data[4],
                (unsigned char)ifreq.ifr_hwaddr.sa_data[5]);
*/
    for (i=0; i<6; i++)
        sprintf(mac+2*i, "%02x", (unsigned char)ifreq.ifr_hwaddr.sa_data[i]);
//    printf("MAC: %s \n", mac);
    return 0;
}

int EnvCheck::get_hardware(char* hdd,int len)//硬盘
{
	
	MyPopen(hdd,len,"ls /dev/disk/by-uuid/");	
	return 0;
}

int EnvCheck::get_key(const char *netname)//获取密钥
{
	char key[MAC_LEN+HDDR_LEN+1];
	char MAC[MAC_LEN]={0};
	char HDDR[HDDR_LEN]={0};
	int i;
	FILE *fp;
	
	srand(time(NULL) + rand());
	
	get_MAC(MAC,netname);
	get_hardware(HDDR,512);
	
	printf("KEY:%s\n",key);
	
	for(i=0;i<(MAC_LEN+HDDR_LEN);i++)
	{
		if(i<MAC_LEN)
		{
			if('\0' == MAC[i])
				key[i] = '!' + rand()%92;
			else
				key[i]=MAC[i]+FORMAT;
		}
		else
		{
			if('\0' == HDDR[i-MAC_LEN])
				key[i] = '!' + rand()%92;
			else
				key[i]=HDDR[i-MAC_LEN]+FORMAT;
		}
	}
	key[i] = '\0';
	fp = fopen("/opt/kyKEY","w+");
	if(NULL == fp)
	{
		printf("open failed \n");
		return -1;
	}
	fwrite(key,sizeof(char),(MAC_LEN+HDDR_LEN),fp);
	fclose(fp);
	
	return 0;
}

int EnvCheck::match_key(const char *netname)//匹配密钥
{
	FILE *fp;
	char key[MAC_LEN+HDDR_LEN+1]={0};	
	char MAC[MAC_LEN]={0};
	char HDDR[HDDR_LEN]={0};

	int i;

	fp = fopen("/opt/kyKEY","r");
	if(NULL == fp)
	{
		printf("open kyKEY failed \n");
		return -1;
	}
	if(!fread(key,sizeof(char),(MAC_LEN+HDDR_LEN+1),fp))
	{
			printf("read kyKEY failed \n");
		return -1;
	}
	fclose(fp);

	get_MAC(MAC,netname);
	get_hardware(HDDR,512);
	
	printf("KEY:%s\n",key);

	for(i=0;i<(MAC_LEN+HDDR_LEN);i++)
	{
		if(i<MAC_LEN)
		{
			if('\0' == MAC[i])
				continue;
			else
			{
				if(key[i] != MAC[i]+FORMAT)
					return -1;
			}
		}
		else
		{
			if('\0' == HDDR[i-MAC_LEN])
				continue;
			else
			{
				if(key[i] != HDDR[i-MAC_LEN]+FORMAT)
					return -1;
			}
		}
	}
	return 0;
}



