#include "env_check.h"
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char *argv[])
{
	int i;
	i = EnvCheck::match_key(argv[1]);
	if(i)
	{
		printf("Check keKEY failed\n");
	//	system("shutdown");	
		system("init 0");
	}	
	return 0;
}

