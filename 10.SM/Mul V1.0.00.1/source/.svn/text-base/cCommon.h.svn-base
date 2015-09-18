#ifndef _CCOMMON_H_
#define _CCOMMON_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <map>
#include <vector>
#include <list>
#include <semaphore.h>
#include <set>
#include <string>

using namespace std;

extern int count;

extern char smip[128];
extern int smport;

extern char mulip[64];
extern int mulport;

extern char dbuser[64];
extern char dbpass[64];
extern char dbname[64];
extern char dbip[64];

typedef struct _advstream
{
	char advname[128];
	char advip[128];
	char advport[64];
	
}Advstream;

typedef struct _dbadvstr
{
	int streamid;
	char advname[128];
	char advip[128];
	char advport[64];
	
}Dbadvstr;

typedef std::map<int,Advstream*> Onestream;
typedef std::map<int,Dbadvstr*> Mapadv;
typedef std::map<int,int> Strpid;

	
#endif

