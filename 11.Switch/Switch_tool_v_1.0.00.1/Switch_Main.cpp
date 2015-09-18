#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/file.h>
#include <unistd.h>
//#include "SM_Manager.h"
#include "Switch_Control.h"


//#include <my_basetype.h>

static int g_single_proc_inst_lock_fd = -1;

static void single_proc_inst_lockfile_cleanup(void)
{
    if (g_single_proc_inst_lock_fd != -1) {
        close(g_single_proc_inst_lock_fd);
        g_single_proc_inst_lock_fd = -1;
    }
}

bool is_single_proc_inst_running(const char *process_name)
{
	printf("-----into single \n");
    char lock_file[128];
    snprintf(lock_file, sizeof(lock_file), "/var/tmp/%s.lock", process_name);

    g_single_proc_inst_lock_fd = open(lock_file, O_CREAT|O_RDWR, 0644);
    if (-1 == g_single_proc_inst_lock_fd) {
        printf("Fail to open lock file(). Error: \n");
        return false;
    }

    if (0 == flock(g_single_proc_inst_lock_fd, LOCK_EX | LOCK_NB)) {
		printf("----succes lock\n");
		atexit(single_proc_inst_lockfile_cleanup);
        return true;
    }

	printf("can't lock file.\n");
    close(g_single_proc_inst_lock_fd);
    g_single_proc_inst_lock_fd = -1;
    return false;

}


//

int main(int argc ,char **argv)
{

	if(!is_single_proc_inst_running(argv[0]))
	{
		printf("---has pid \n");
		fflush(stdout);
		return -1;
	}

	char config_file[500];
	int ret;
   
	if(argc>1 && access(argv[1], R_OK) == 0)
	{
		strcpy(config_file,argv[1]);
	}
	else if (access("SW.config", R_OK) == 0)
	{
		strcpy(config_file,"SW.config");
	}
	else 
	{
		cout<<"no config file can access"<<endl;
	}

	//SM_Manager *pSM_Manager = new SM_Manager;
	Switch_Control *pSM_Manager = new Switch_Control;
	
	printf("----welcom to switch \n");
	fflush(stdout);
	pSM_Manager->init(config_file);
	
	while(1)
	{
		sleep(10000);
	}
	printf("----go to switch \n");
	
	return 0;

}
