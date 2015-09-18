#include "tssmooth.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>

#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#include <pthread.h>


typedef struct
{
	char listen_udp_ip[32];
	int  listen_udp_port;
	char dst_udp_ip[32];
	int  dst_udp_port;

	int bit_rate;//b
	int buffer_max_size;

	char *buffer;

	int read_index;
	int write_index;

	pthread_t read_thread_id;
	pthread_t write_thread_id;
	pthread_mutex_t m_mutex;

	//for statistics
	unsigned long real_send_bytes;
}tssmooth_instanse;


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

void *ts_recv_thread(void *arg)
{
	tssmooth_instanse *param = NULL;
	param = (tssmooth_instanse *)arg;


	struct sockaddr_in servaddr;
	memset(&servaddr,0, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port = htons(param->listen_udp_port);
	
	int socket_id = socket(AF_INET, SOCK_DGRAM, 0);
	if(socket_id == -1)
		return NULL;
	
	int optval = 1;
	if ((setsockopt(socket_id,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(socket_id);
		return NULL;
	}
	
	int send_recv_size = 1024*1024;	
	int ret = setsockopt(socket_id, SOL_SOCKET, SO_RCVBUF, (const char *)&send_recv_size, sizeof(int));
	if(ret==-1)
		printf("======set tss recv buff error\n");

	if(bind(socket_id, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1)
	{
		close(socket_id);
		return NULL;
	}

	struct sockaddr_in cliaddr;
	int addr_len = sizeof(struct sockaddr_in);
	char recv_buffer[2048];
	int recv_len = -1;

	while (1)
	{
		memset(recv_buffer, 0, sizeof(recv_buffer));
		recv_len = recvfrom(socket_id, recv_buffer, sizeof(recv_buffer), 0, (struct sockaddr *)&cliaddr, (socklen_t*)&addr_len);
		if(recv_len <= 0)
			continue;

		pthread_mutex_lock(&param->m_mutex);
		if(param->write_index - param->read_index + recv_len > param->buffer_max_size)
		{
			pthread_mutex_unlock(&param->m_mutex);
			printf("%s %d Waring,buffer is too small,or send is slow,it will reset!\n",__FUNCTION__,__LINE__);

			//reset
			param->write_index = 0;
			param->read_index = 0;
			//
			continue;
		}

		if(param->buffer_max_size - param->write_index < recv_len)
		{
			memmove(param->buffer,param->buffer+param->read_index,param->write_index - param->read_index);

			param->write_index = param->write_index - param->read_index;
			param->read_index = 0;
		}

		memcpy(param->buffer+param->write_index,recv_buffer,recv_len);
		param->write_index += recv_len;
		
		pthread_mutex_unlock(&param->m_mutex);
	}
}


void *ts_send_thread(void *arg)
{
	tssmooth_instanse *param = NULL;
	param = (tssmooth_instanse *)arg;
	
	struct sockaddr_in servaddr;
	memset(&servaddr, 0,sizeof(servaddr));
	servaddr.sin_family = AF_INET;	
	servaddr.sin_addr.s_addr = inet_addr(param->dst_udp_ip);	
	servaddr.sin_port = htons(param->dst_udp_port);

	int socket_id = socket(AF_INET, SOCK_DGRAM, 0);
	if(socket_id == -1)
		return NULL;
	
	int send_buf_size = 0;	
	setsockopt(socket_id, SOL_SOCKET, SO_SNDBUF, (const char *)&send_buf_size, sizeof(int));
	
	char send_buffer[7*188];
	int send_buffer_size = -1;
	int send_len = -1;

	struct timeval tv1,tv2;
	long long time1,time2;
	long long nsendbytes = 0;
	long long totaltime = 0;
	long now_bit_rate = 0;

	gettimeofday(&tv1, NULL);
	time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
	while(1)
	{
		gettimeofday(&tv2, NULL);
		time2 = tv2.tv_sec*1000 + tv2.tv_usec / 1000;
		if(time2 - time1 > 0)
			now_bit_rate = nsendbytes*8*1000/(time2 - time1);
		else
			now_bit_rate = param->bit_rate;

		if(now_bit_rate >= param->bit_rate)
		{
			usleep(5*1000);	
			/*/printf("now_bit_rate is too large %d %d\n",now_bit_rate,bit_rate);*/
			continue;
		}
		
		if(time2 - time1 >= 300)
		{
			gettimeofday(&tv1, NULL);
			time1 = tv1.tv_sec*1000 + tv1.tv_usec / 1000;
			nsendbytes = 0;
		}
		
		pthread_mutex_lock(&param->m_mutex);
		if(param->write_index - param->read_index <= 0)
		{
			pthread_mutex_unlock(&param->m_mutex);
			usleep(20*1000);	
			continue;
		}
		
		if(param->write_index - param->read_index >= sizeof(send_buffer))
		{
			memcpy(send_buffer,param->buffer+param->read_index,sizeof(send_buffer));
			send_buffer_size = sizeof(send_buffer);
			param->read_index += sizeof(send_buffer);
		}
		else
		{
			memcpy(send_buffer,param->buffer+param->read_index,param->write_index - param->read_index);
			send_buffer_size = param->write_index - param->read_index;
			param->read_index = param->write_index;
		}
		pthread_mutex_unlock(&param->m_mutex);
		
		send_len = sendto(socket_id, send_buffer, send_buffer_size, 0, (struct sockaddr *)&servaddr, sizeof(servaddr));
		nsendbytes +=  send_len;
		if(send_len >= 0)
			param->real_send_bytes += send_len;

	}
}


ts_smooth_t* init_tssmooth(InputParams_tssmooth *pInputParams)
{
	if(pInputParams == NULL)
	{        
		fprintf(stderr ,"libtssmooth: Error paraments..\n");        
		return NULL;    
	}

	
	tssmooth_instanse *p_instanse = (tssmooth_instanse *)malloc(sizeof(tssmooth_instanse));
	if(p_instanse == NULL)
	{
		fprintf(stderr, "libtssmooth malloc tssmooth_instanse failed...\n");
		return NULL;
	}

	if(pInputParams->listen_udp_port <= 0)
	{
		strncpy(pInputParams->listen_udp_ip,"127.0.0.1",sizeof(pInputParams->listen_udp_ip));
		pInputParams->listen_udp_port = 0;
		int ret = applyforUDPPort(pInputParams->listen_udp_ip,&pInputParams->listen_udp_port);
		if(ret < 0)
		{
			fprintf(stderr, "+++libtssmooth applyforUDPPort is error %d ,ret = %d\n",pInputParams->listen_udp_port,ret);
			pInputParams->listen_udp_port = 40000 + pInputParams->index;
			printf("+++libtssmooth now use port is %d \n",pInputParams->listen_udp_port);
		}
		else
		{
			printf("+++libtssmooth applyforUDPPort is %d ,ret = %d\n",pInputParams->listen_udp_port,ret);
		}
	}

	memset(p_instanse,0,sizeof(tssmooth_instanse));
	strncpy(p_instanse->listen_udp_ip,pInputParams->listen_udp_ip,sizeof(p_instanse->listen_udp_ip));
	p_instanse->listen_udp_port = pInputParams->listen_udp_port;
	strncpy(p_instanse->dst_udp_ip,pInputParams->dst_udp_ip,sizeof(p_instanse->dst_udp_ip));
	p_instanse->dst_udp_port = pInputParams->dst_udp_port;
	p_instanse->bit_rate = pInputParams->bit_rate;
	p_instanse->buffer_max_size = pInputParams->buffer_max_size;
	p_instanse->read_index = 0;
	p_instanse->write_index = 0; 
	pthread_mutex_init(&p_instanse->m_mutex, 0);
	p_instanse->real_send_bytes = 0;
	
	p_instanse->buffer = (char *)malloc(p_instanse->buffer_max_size);

	if(p_instanse->buffer == NULL)
	{
		fprintf(stderr, "libtssmooth malloc buffer failed...\n");
		pthread_mutex_destroy(&p_instanse->m_mutex);
		free(p_instanse);
		return NULL;
	}

	pthread_attr_t attr;	
	pthread_attr_init(&attr);	
	pthread_attr_setstacksize(&attr,10*1024*1024);	
	
	if (pthread_create(&p_instanse->read_thread_id, &attr, &ts_recv_thread, p_instanse) != 0)		
	{
		fprintf(stderr, "libtssmooth pthread_create failed...\n");
		pthread_mutex_destroy(&p_instanse->m_mutex);
		free(p_instanse->buffer);
		free(p_instanse);
		return NULL;
	}
	if (pthread_create(&p_instanse->write_thread_id, &attr, &ts_send_thread, p_instanse) != 0)		
	{
		fprintf(stderr, "libtssmooth pthread_create failed...\n");
		pthread_cancel(p_instanse->read_thread_id);
		pthread_mutex_destroy(&p_instanse->m_mutex);
		free(p_instanse->buffer);
		free(p_instanse);
		return NULL;
	}
	
	return p_instanse;
	
}

unsigned long get_real_send_bytes(ts_smooth_t *smooth)
{
	if(smooth == NULL)
	{		 
		fprintf(stderr ,"libtssmooth: Error paraments..\n");		
		return -1;	  
	}
	tssmooth_instanse *p_instanse = (tssmooth_instanse *)smooth;

	
	return p_instanse->real_send_bytes;
}

int uninit_tssmooth(ts_smooth_t *smooth)
{
	if(smooth == NULL)
	{		 
		fprintf(stderr ,"libtssmooth: Error paraments..\n");		
		return -1;	  
	}
	tssmooth_instanse *p_instanse = (tssmooth_instanse *)smooth;

	pthread_cancel(p_instanse->read_thread_id);
	pthread_cancel(p_instanse->write_thread_id);
	pthread_mutex_destroy(&p_instanse->m_mutex);
	if(p_instanse->buffer != NULL)
		free(p_instanse->buffer);
	free(p_instanse);

	return 0;
}



