#include "stdio.h"
#include "stdlib.h"
#include "unistd.h"
       #include <sys/types.h>
       #include <sys/stat.h>
#include <signal.h>

static void killfun(int id);

int main()
{
// ÐÅºÅ ºöÂÔ
// ÎÄ¼þ
	signal(SIGINT, SIG_IGN);
	signal(SIGPIPE, SIG_IGN);
	signal(SIGHUP, SIG_IGN);
	signal(SIGQUIT, SIG_IGN);
	signal(SIGFPE, SIG_IGN);
	signal(SIGSEGV, SIG_IGN);
//	signal(SIGKILL,SIG_IGN);
	
	if(signal(SIGKILL,killfun) == SIG_ERR)
	{
		perror("can't catch SIGKILL");
		
	}
	
	 
	if(signal(SIGTERM,killfun) == SIG_ERR)
	{
		perror("can't catch SIGKILL");
		
	}
	pid_t pid = fork();
	
	if(pid < 0)
	{
		exit(EXIT_FAILURE);
	}
	
	if(pid > 0)
	{
	//	parent process;
		printf("parent process kill\n");
		exit(EXIT_SUCCESS);
	}
	umask(0);
		/* Create a new SID for the child process */  
	pid_t sid = setsid();
	if(sid < 0)
	{
		exit(EXIT_FAILURE);
	}
	
	printf("-----this an daemon \n");
	chdir("/");
	
	//close(STDIN_FILENO);  
    //close(STDOUT_FILENO);  
   // close(STDERR_FILENO);  


	
	while(1)
	{
		printf("1111\n");
		sleep(10);
	}

}

static void killfun(int id)
{

	printf("want to kill me !no way !\n");
}
