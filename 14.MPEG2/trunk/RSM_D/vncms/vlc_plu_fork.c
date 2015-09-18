#include <stdio.h>
#include <string.h>
#include <net/if.h> 
#include <net/if_arp.h>
#include <pthread.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>
#include <stdlib.h>

#define MAXPROCESS 4096
#define BUFFERSIZE 1024
#define COUNTTIMES 20



int WriteLog(const char* buffer)
{
	time_t now;
	struct tm *timenow;
	char strtemp[255];
	FILE *fp;
	char *p;
	int len;
	char dir[128];


	time(&now);
	timenow = localtime(&now);
	printf("recent time is : %s \n", asctime(timenow)); 
	sprintf(strtemp,"\n %s (%s)\n",asctime(timenow),buffer);

	fp = fopen("./killlibvlc.log","a+");
	if(fp == NULL)
	{
		return -1;
	}
	len = fwrite(strtemp,sizeof(char),strlen(strtemp),fp);
	fclose(fp);
	return 1;
}

int KillProcess(int pid)
{
	char killvlc[BUFFERSIZE];						
	sprintf(killvlc,"find fault process id :%d \n",pid);
	kill(pid,SIGKILL);
	WriteLog(killvlc);	

}
int DoKill()
{
		int a[MAXPROCESS] = {0};
		int b[MAXPROCESS] = {0};
		int i,j,m,n;
		FILE* two;
		FILE* three;
		char buff[BUFFERSIZE];
		int count=COUNTTIMES;
		while(1)
		{
			if(0 == count)
			{
				system("ps -ef|grep bvbcm |grep -v grep|awk '{print $2}'>2.txt");
				system("ps -ef|grep bvbcm |grep -v grep|awk '{print $3}'>3.txt");
				count=COUNTTIMES;
			}
			else if(10 == count)
			{
				system("ps -ef|grep chrome |grep -v grep|awk '{print $2}'>2.txt");
				system("ps -ef|grep chrome |grep -v grep|awk '{print $3}'>3.txt");
			}
			else if(11 == count)
			{
				system("ps -ef|grep bvbcs |grep -v grep|awk '{print $2}'>2.txt");
				system("ps -ef|grep bvbcs |grep -v grep|awk '{print $3}'>3.txt");
			}
			else
			{
				system("ps -ef|grep libvlcplugin.so |grep -v grep|awk '{print $2}'>2.txt");		
				system("ps -ef|grep libvlcplugin.so |grep -v grep|awk '{print $3}'>3.txt");
			}
			two = fopen("2.txt","r");
			three = fopen("3.txt","r");
			memset(a,0,sizeof(a));
			memset(b,0,sizeof(b));
			i=j=n=m=0;
			while(fgets(buff,sizeof(buff),two))
			{
				if(MAXPROCESS == i)
					break;
				a[i]=atoi(buff);
				memset(buff,0,sizeof(buff));
				i++;
			}
			while(fgets(buff,sizeof(buff),three))
			{
				if(MAXPROCESS == j)
					break;
				b[j]=atoi(buff);	
				memset(buff,0,sizeof(buff));
				j++;
			}
			if(i==j)
			{
				for(m=0;m<i;m++)
				{
					if(1 == b[m])
						KillProcess(a[m]);
					for(n=0;n<j;n++)
					{
						if(0 == a[m] || 0 == b[n])
							break;
						if(a[m] == b[n] && 10 != count )//chrome不检测父子进程问题
						{
							KillProcess(a[n]);
						}
						
					}
				}
			}
			count--;
			fclose(two);
			fclose(three);
			sleep(1);
		}
	}

int main()
{
	int pid;
	int i=0;
	char buff[128];
	while(1)
	{
		pid = fork();
		if(pid <0)
			WriteLog("fork error \n");
		else if(0 == pid)
		{
			DoKill();
			exit(1);
		}
		else
		{
			wait(NULL);
			kill(pid,SIGKILL);
			i++;
			sprintf(buff,"kill plugin process exited No.%d \n",i);
			WriteLog(buff);
		}
	}
//long killed = 0;	
}



