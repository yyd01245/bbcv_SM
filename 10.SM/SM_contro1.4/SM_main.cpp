#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/file.h>
#include <unistd.h>
#include <locale.h>

//#include "SM_Manager.h"
#include "SM_Control.h"
#include "CommonFun.h"
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
		return -1;
	}
	//setlocale(LC_ALL,"utf-8");
	//SM_Manager *pSM_Manager = new SM_Manager;
	init("./sm.config");
	SM_Control *pSM_Manager = new SM_Control;
	
	printf("----welcom to sm \n");

	
	while(1)
	{
		sleep(10000);
	}
	printf("----go to sm \n");
	return 0;

}
