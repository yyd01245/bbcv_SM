#include "cDatabase.h"

using namespace std;

DBinterfacer * DBinterfacer::theInstance = NULL; 

DBinterfacer::DBinterfacer()
{
	conn_ptr = NULL;
	res_ptr = NULL;	
	sqlrow = NULL;	
	fd = NULL;
	advmage = new Stream_manage;
	printf("begin to connect the database\n");
	Connect2DB(dbuser,dbpass,dbname,dbip);
}

DBinterfacer::~DBinterfacer()
{
	if(conn_ptr)
		mysql_close(conn_ptr);
	conn_ptr = NULL;
}

bool DBinterfacer::Connect2DB(char* strUser,char* strpasswd,char* strDataBase,char* strServer)
{
	if(conn_ptr)
		mysql_close(conn_ptr);
	conn_ptr = NULL;
	conn_ptr = mysql_init(NULL);
	
	if(!conn_ptr)	
	{		
		printf("mysql_inti faild\n");
		fflush(stdout);
		return false;	
	}
	printf("init mysql \n");
	fflush(stdout);
	conn_ptr = mysql_real_connect(conn_ptr,strServer,strUser,strpasswd,strDataBase,3306,NULL,0);
	if(!conn_ptr)
	{
		printf("mysql connect error \n");
		fflush(stdout);
		return false;
	}
	printf("----connect mysql success\n");
	fflush(stdout);
	m_bHasConnect = true;
	return true;
}

bool DBinterfacer::LoadAllAdvResource(Mapadv &Advres)
{
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_adv_info");
	printf("strSQL = %s\n",strSQL);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{				
				Dbadvstr *pTmpadv = new Dbadvstr;				
				int istreamID;
				
				if(sqlrow[0] != NULL)
					pTmpadv->streamid = atoi(sqlrow[0]);
					printf("pTmpadv->streamid = %d\n",pTmpadv->streamid);
				if(sqlrow[1] != NULL)
					memcpy(pTmpadv->advname,sqlrow[1],strlen(sqlrow[1])+1);
					printf("pTmpadv->advname = %s\n",pTmpadv->advname);
				if(sqlrow[2] != NULL)
					memcpy(pTmpadv->advip,sqlrow[2],strlen(sqlrow[2])+1);
					printf("pTmpadv->advip = %s\n",pTmpadv->advip);
				if(sqlrow[3] != NULL)
					memcpy(pTmpadv->advport,sqlrow[3],strlen(sqlrow[2])+1);
					printf("pTmpadv->advport = %s\n",pTmpadv->advport);
				Advres.insert(Mapadv::value_type(pTmpadv->streamid,pTmpadv));
			}		
		}
	}
	else
	{
		printf("SELECT error:");
		fflush(stdout);
		return false;
	}
	return true;	
}

int DBinterfacer::SelectAdvDb(int prikey,Mapadv &advmapinfo)
{
	int res;
	char strSQL[1024] ={0};
	int flag = 0;
	Mapadv::iterator it = advmapinfo.find(prikey);
	if(it == advmapinfo.end())
	{
		printf("it is over\n");
		return -1;
	}
	Dbadvstr *itmp = it->second;
	sprintf(strSQL,"select * from ucs_adv_info where id = %d",prikey);
	printf("strSQL = %s\n",strSQL);
	res = mysql_query(conn_ptr,strSQL);	
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{				
				if(sqlrow[1] != NULL)
				{
					if(strcmp(sqlrow[1],itmp->advname)!=0)
					{
						memcpy(itmp->advname,sqlrow[1],strlen(sqlrow[1])+1);
						printf("itmp->advname = %s,sqlrow[1] = %s\n",itmp->advname,sqlrow[1]);
						if(0==flag)
						{
							flag = 1;
						}
					}
				}
				
				if(sqlrow[2] != NULL)
				{
					if(strcmp(sqlrow[2],itmp->advip)!=0)
					{
						memcpy(itmp->advip,sqlrow[2],strlen(sqlrow[2])+1);
						printf("itmp->advip = %s,sqlrow[2] = %s\n",itmp->advip,sqlrow[2]);
						if(0==flag)
						{
							flag = 1;
						}
					}
				}
				
				if(sqlrow[3] != NULL)
				{
					if(strcmp(sqlrow[3],itmp->advport)!=0)
					{
						memcpy(itmp->advport,sqlrow[3],strlen(sqlrow[3])+1);
						printf("itmp->advport = %s,sqlrow[3] = %s\n",itmp->advport,sqlrow[3]);
						if(0==flag)
						{
						 	flag = 1;
						}
					}
				}
			}		
		}
	}
	
	else
	{
		printf("SELECT error:");
		fflush(stdout);
		return -1;
	}
	if(flag)
	{
		return 1;
	}
	else
	  return 0;	
}

int DBinterfacer::Selectsame(char *cadvname,char *caip,char *caport)
{
	int res;
	char strSQL[1024] ={0};
	int flag = 0;
	
	sprintf(strSQL,"select * from ucs_adv_info where advname = \"%s\" and advip = \"%s\" and advport = %s",cadvname,caip,caport);
	printf("strSQL = %s\n",strSQL);
	res = mysql_query(conn_ptr,strSQL);	
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		
		if(res_ptr)
		{
			sqlrow = mysql_fetch_row(res_ptr);
			if(sqlrow==NULL)
			{
				printf("---there is null\n");
				return 0;
			}
			else
				
			 printf("find some same\n");
			 return 1;
		}
		else
		{
			printf("there is null\n");
			return 0;
		}
	}
}
int DBinterfacer::selectRepeat(char *cadvip,char *cadvport)
{
/*
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_adv_info where advip = \"%s\" and advport = \"%s\"",cadvip,cadvport);
	res = mysql_query(conn_ptr,strSQL);	
	res_ptr = mysql_store_result(conn_ptr);
	if(res_ptr == NULL)
	{
		return 0;
	}
	else 
	 	return 1;
*/
}
