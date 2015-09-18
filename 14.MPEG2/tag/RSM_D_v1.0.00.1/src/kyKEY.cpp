#include "env_check.h"
#include <stdio.h>
int main(int argc, char *argv[])
{
	int i;
	EnvCheck::get_key(argv[1]);
	i = EnvCheck::match_key(argv[1]);
	if(i)
		printf("create keKEY failed\n");
	return 0;
}

