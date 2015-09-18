#ifndef _CDATABASE_H_
#define _CDATABASE_H_

#include <mysql/mysql.h>
#include <pthread.h>
#include <unistd.h> 
#include "cStartstream.h"
#include "cCommon.h"

class Stream_manage;

class DBinterfacer
{
  public:
	DBinterfacer();
	~DBinterfacer();
	static DBinterfacer * GetInstance()
	{
		if(!theInstance)
			theInstance = new DBinterfacer;
		return theInstance;
	}
	bool Connect2DB(char* strUser,char* srrpasswd,char* strDataBase,char* strServer=NULL);
	bool LoadAllAdvResource(Mapadv &Advres);
	int SelectAdvDb(int prikey,Mapadv &advmapinfo);
	int Selectsame(char *cadvname,char *caip,char *caport);
	int selectRepeat(char *cadvip,char *cadvport);
	Stream_manage *advmage;
	
   private:
	 static DBinterfacer *theInstance;
	 
	 bool m_bHasConnect;
	
	 MYSQL *conn_ptr;
	 MYSQL_RES *res_ptr;	
	 MYSQL_ROW sqlrow;	
	 MYSQL_FIELD *fd;
};

#endif