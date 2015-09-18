#ifndef _DBINTERFACE_H_
#define _DBINTERFACE_H_

#include "CommonData.h"
#include <mysql/mysql.h>
#include <pthread.h>
#include <unistd.h> 
#include "Log.h"



class DBInterfacer
{
 public:
	//connect db
	DBInterfacer();
	~DBInterfacer();

	static DBInterfacer* GetInstance()
	{
		if(!theInstance)
			theInstance = new DBInterfacer;
		return theInstance;
	}
	
	bool Connect2DB(char* strUser,char* srrpasswd,
						char* strDataBase,char* strServer=NULL);

	bool LoadAllStreamResource(MapStreamResource &mapStreamResourcetmp);

	bool LoadAllStreamStatus(MapStreamStatus &mapStreamStatustmp);

	bool LoadALLIPQAMInfo(MapIPQAMInfo &mapIpqaminfo,int iIPQAMNum,int iStreamID);

	bool LoadNetWorkGroup(MapNetWorkGroup &mapNetWorkGroup);

	bool LoadAlladvinfo(SetAdvinfo &mapAdvinfo,int temp = 0);

	bool LoadOneGroupStreamResource(char *strNetworkNum,ListStreamResource &listStreamResourcetmp);

	bool FindOneStream(int table_num,char* pri_key,char* key_value,void *table);

	bool FindSameGroupStream(char *strNetworkNum,int ishd,SetGroupStream& setStreamTmp);

	bool FindUsername(int prikey);
	
	bool insert_table(int table_num,void *table);
	
	bool delete_table(int table_num,int pri_key);

	bool update_table(int pri,int status);

	bool update_table(int table_num,void *table);

	bool select_table(int table_num,int pri_key);

	bool FindUsername(int prikey,char *username);

	bool LoadALLIPQAMRes(int ipqaminid,int *port,int streamid);

	bool UpdateIPQAMRes(int myid,int streamid);

	bool ResetIPQAMRes();

	bool FindSameNavflag(int strtype,Setstrnavflag& setStreamTmp);

	bool FindNavurl(int flagid,char *naurl);
	bool Findtype(int streamid,int type,int flag = 0);   //查找当前流的类型
	bool FindlNavid(int strtype,int id); //查找合适的流id
 private:
	static DBInterfacer *theInstance;

	bool m_bHasConnect;

	pthread_mutex_t locker;
	
	MYSQL *conn_ptr;
	MYSQL_RES *res_ptr;	
	MYSQL_ROW sqlrow;	
	MYSQL_FIELD *fd;
};


#endif

