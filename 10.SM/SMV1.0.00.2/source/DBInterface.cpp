#include "DBInterface.h"
#include <unistd.h>

DBInterfacer * DBInterfacer::theInstance = NULL;
#define USEMUTEX

DBInterfacer::DBInterfacer()
{
	conn_ptr = NULL;
	res_ptr = NULL;	
	sqlrow = NULL;	
	fd = NULL;
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin connect to db\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX	
	pthread_mutex_init(&locker, NULL);
#endif
	Connect2DB(dbuser,dbpass,dbname,dbip);
}

DBInterfacer::~DBInterfacer()
{
	if(conn_ptr)
		mysql_close(conn_ptr);
	conn_ptr = NULL;
#ifdef USEMUTEX	
	pthread_mutex_destroy(&locker);
#endif
}

bool DBInterfacer::Connect2DB(char* strUser,char* strpasswd,
					 char* strDataBase,char* strServer)
{
	if(conn_ptr)
		mysql_close(conn_ptr);
	conn_ptr = NULL;
	conn_ptr = mysql_init(NULL);
	unsigned int ulport = 0;
	if(!conn_ptr)	
	{		
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d mysql_inti faild\n",__FUNCTION__,__LINE__);
		return false;	
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to init mysql\n",__FUNCTION__,__LINE__);
	char value = 1;
 	mysql_options(conn_ptr, MYSQL_OPT_RECONNECT, (const char *)&value);
	conn_ptr = mysql_real_connect(conn_ptr,strServer,strUser,strpasswd,strDataBase,3306,NULL,0);
	usleep(500);
	if(!conn_ptr)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d mysql connect error\n",__FUNCTION__,__LINE__);
		return false;
	}
	m_bHasConnect = true;
	return true;
}

bool DBInterfacer::LoadALLIPQAMInfo(MapIPQAMInfo &mapIpqaminfo,int iIPQAMNum,int iStreamID)
{
#ifdef USEMUTEX
		pthread_mutex_unlock(&locker);
#endif
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ipqam_info where ipqamInfoId = %d",iIPQAMNum);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,strSQL);	
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			//j = mysql_num_fields(res_ptr);	 //列		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{							
				IPQAMInfo *pTmpStream = new IPQAMInfo;
	
				int istreamID;

				if(sqlrow[0] != NULL)
					pTmpStream->iIPQAMNum = atoi(sqlrow[0]);
			//	if(sqlrow[1] != NULL)
			//		memcpy(pTmpStream->strIpqamName,sqlrow[1],strlen(sqlrow[1])+1);
				if(sqlrow[2] != NULL)
					memcpy(pTmpStream->strIpqamIP,sqlrow[2],strlen(sqlrow[2])+1);
				if(sqlrow[3] != NULL)
					pTmpStream->iIpqamManagerPort = atoi(sqlrow[3]);
				mapIpqaminfo.insert(MapIPQAMInfo::value_type(iStreamID, pTmpStream));
			}
			mysql_free_result(res_ptr);
		}
	
	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
		return false;
	}
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
		return true;	
}

bool DBInterfacer::LoadNetWorkGroup(MapNetWorkGroup &mapNetWorkGroup)
{
#ifdef USEMUTEX
		pthread_mutex_lock(&locker);
#endif

	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_network_area");
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,strSQL);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
			pthread_mutex_unlock(&locker);
#endif
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
			
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			int j = mysql_num_fields(res_ptr);	 //列		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{				
				NetworkGroup *pTmpStream = new NetworkGroup;
				memset(pTmpStream,0,sizeof(NetworkGroup));
			//printf("---%s\n",sqlrow[0]);	
				if(sqlrow[0] != NULL)
					memcpy(pTmpStream->strNetRegionNum,sqlrow[0],strlen(sqlrow[0])+1);
				if(sqlrow[1] != NULL)
					memcpy(pTmpStream->strNetRegionName,sqlrow[1],strlen(sqlrow[1])+1);
				if(sqlrow[2] != NULL)
					pTmpStream->hdiNavgationStreamNum = atoi(sqlrow[2]);
					//printf("---%s\n",sqlrow[3]);
				if(sqlrow[3] != NULL)
					pTmpStream->sdiNavgationStreamNum = atoi(sqlrow[3]);
				if(sqlrow[4] != NULL)
					pTmpStream->hdiAdvertisementStreamNum = atoi(sqlrow[4]);
				if(sqlrow[5] != NULL)
					pTmpStream->sdiAdvertisementStreamNum = atoi(sqlrow[5]);
				if(sqlrow[6] != NULL)
					memcpy(pTmpStream->strNetworkComment,sqlrow[6],strlen(sqlrow[6])+1);
				mapNetWorkGroup.insert(MapNetWorkGroup::value_type(pTmpStream->strNetRegionNum, pTmpStream));	
				}
				mysql_free_result(res_ptr);
			}
	
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
			return false;
		}	
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif	
		return true;		
}



bool DBInterfacer::LoadAllStreamStatus(MapStreamStatus &mapStreamStatustmp)
{
#ifdef USEMUTEX
		pthread_mutex_lock(&locker);
#endif

	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from stream_session");
	
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{						
				StreamStatus *pTmpStream = new StreamStatus;
				memset(pTmpStream,0,sizeof(StreamStatus));

				int istreamID;
				if(sqlrow[0] != NULL)
					pTmpStream->istreamID = atoi(sqlrow[0]);
				if(sqlrow[1] != NULL)
					memcpy(pTmpStream->strStreamType,sqlrow[1],strlen(sqlrow[1])+1);
				if(sqlrow[2] != NULL)
					memcpy(pTmpStream->strStatus_date,sqlrow[2],strlen(sqlrow[2])+1);
				if(sqlrow[3] != NULL)
					memcpy(pTmpStream->strBind_userID,sqlrow[3],strlen(sqlrow[3])+1);
				if(sqlrow[4] != NULL)
					memcpy(pTmpStream->strBind_date,sqlrow[4],strlen(sqlrow[4])+1);
				if(sqlrow[5] != NULL)
					memcpy(pTmpStream->strSwitch_task_id,sqlrow[5],strlen(sqlrow[5])+1);		
				mapStreamStatustmp.insert(MapStreamStatus::value_type(pTmpStream->istreamID, pTmpStream));
			}
			mysql_free_result(res_ptr);
		}

	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
		pthread_mutex_unlock(&locker);
#endif
		return false;
	}
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
	return true;	
}


bool DBInterfacer::LoadAllStreamResource(MapStreamResource &mapStreamResourcetmp)
{
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
#endif
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_stream_resource");
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			//j = mysql_num_fields(res_ptr);	 //列		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{						
				StreamResource *pTmpStream = new StreamResource;
				if(sqlrow[0] != NULL)
					pTmpStream->iStreamID = atoi(sqlrow[0]);
				if(sqlrow[1] != NULL)
					pTmpStream->iIPQAMNum = atoi(sqlrow[1]);
				if(sqlrow[2] != NULL)
					memcpy(pTmpStream->strNetRegionNum,sqlrow[2],strlen(sqlrow[2])+1);
				if(sqlrow[3] != NULL)
					memcpy(pTmpStream->strNav_url,sqlrow[3],strlen(sqlrow[3])+1);
				if(sqlrow[4] != NULL)
					pTmpStream->iChannel_info = atoi(sqlrow[4]);
				if(sqlrow[5] != NULL)
					pTmpStream->iWherether_HD = atoi(sqlrow[5]);	
				mapStreamResourcetmp.insert(MapStreamResource::value_type(pTmpStream->iStreamID, pTmpStream));
			}	
			mysql_free_result(res_ptr);
		}
	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif
		return false;
	}
#ifdef USEMUTEX
		pthread_mutex_unlock(&locker);
#endif
	return true;
}



bool DBInterfacer::LoadOneGroupStreamResource(char *strNetworkNum,ListStreamResource &tmplistStreamResourcetmp)
{
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
#endif
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_stream_resource where strNetRegionNum=%s",strNetworkNum);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,strSQL);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		pthread_mutex_unlock(&locker);
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			//j = mysql_num_fields(res_ptr);	 //列		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{				
				int iStreamID = 0;
				if(sqlrow[0] != NULL)
				{
					iStreamID = atoi(sqlrow[0]);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d group stream id =%d \n",__FUNCTION__,__LINE__,iStreamID);
					tmplistStreamResourcetmp.push_back( iStreamID);
				}
			}		
			mysql_free_result(res_ptr);
		}

	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX		
		pthread_mutex_unlock(&locker);
#endif
		return false;
	}
#ifdef USEMUTEX	
	pthread_mutex_unlock(&locker);
#endif
	return true;

}

bool DBInterfacer::LoadAlladvinfo(SetAdvinfo &mapAdvinfo)
{
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_adv_info");
	
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	int streamid;
	
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{						
				Advinfo *pTadvinfo = new Advinfo;
				if(sqlrow[0] != NULL)
					streamid = atoi(sqlrow[0]);
				
				if(sqlrow[2] != NULL)
					memcpy(pTadvinfo->advip,sqlrow[2],strlen(sqlrow[2])+1);

				if(sqlrow[3] != NULL)
					memcpy(pTadvinfo->advport,sqlrow[3],strlen(sqlrow[3])+1);		
				mapAdvinfo.insert(SetAdvinfo::value_type(streamid, pTadvinfo));
			}
			mysql_free_result(res_ptr);
		}
	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
		return false;
	}

	return true;
}

bool DBInterfacer::insert_table(int table_num,void *table) //参数说明:表编号，输入表的值
{
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d insert into 1\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
#endif
	int res;
	StreamStatus *tab1;
	UserBehaviour *tab2;
	NetworkGroup *tab3;
	StreamResource *tab4;
	IPQAMInfo *tab5;
	char buf[2048];
	memset(buf,0,sizeof(buf));
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d insert into 2\n",__FUNCTION__,__LINE__);
	switch(table_num)
	{
		case 1:          // 1:代表stream_session values 表
		{
			tab1 = (StreamStatus *)table;

			sprintf(buf,"insert into stream_session values(%d,\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',NULL,NULL)",\
				tab1->istreamID,tab1->strStreamType,tab1->strStatus_date,tab1->strBind_userID,tab1->strBind_date,tab1->strSwitch_task_id); //,tab1->strSessionID,tab1->strSerialNo
			LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
			if(mysql_ping(conn_ptr)!=0)
			{
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
				pthread_mutex_unlock(&locker);
				return false;
			}
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
			res =  mysql_query(conn_ptr, buf);
			break;
		}	
		case 2:         // 2:代表ucs_user_action 表
		{
			tab2 = (UserBehaviour *)table;
			if(tab2->strUserName == NULL)
			{
				sprintf(buf,"insert into ucs_user_action(iStreamID,strUserName,strActionType,strActionDate,strNetworkComment,result)values(%d,\"\",\"%s\",\"%s\",\"%s\",%d)",\
				    tab2->iStreamID,tab2->strActionType,tab2->strActionDate,tab2->strNetworkComment,tab2->result);
			}
			else
			sprintf(buf,"insert into ucs_user_action(iStreamID,strUserName,strActionType,strActionDate,strNetworkComment,result)values(%d,\"%s\",\"%s\",\"%s\",\"%s\",%d)",\
				tab2->iStreamID,tab2->strUserName,tab2->strActionType,tab2->strActionDate,tab2->strNetworkComment,tab2->result);
			LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
			if(mysql_ping(conn_ptr)!=0)
			{
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
				pthread_mutex_unlock(&locker);
				return false;
			}
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
			res = mysql_query(conn_ptr,buf);
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d res = %d,this is tab2\n",__FUNCTION__,__LINE__,res);
			break;
		}	
		default:
		{
			LOG_WARN_FORMAT("%s %d wrong table number\n",__FUNCTION__,__LINE__);
			res = 1;
			break;
		}	
	}
	if(!res)
	{
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d Inserted %lu rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_affected_rows(conn_ptr));
	}	
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX		
		pthread_mutex_unlock(&locker);
#endif
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d success insert stream_session\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX	
	pthread_mutex_unlock(&locker);
#endif
	return true;
}

bool DBInterfacer::delete_table(int table_num,int pri_key)//参数说明:表编号，主键值
{
	return true;
}


bool DBInterfacer::update_table(int table_num,void *table)//参数说明:表编号，主键值，属性名称，属性值
{
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is update_table\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
#endif
	int pri_key;
	char *name;
	char *key;

	StreamStatus *tab1;
	char buf[2048];
	memset(buf,0,sizeof(buf));
	int res = 0;
	tab1 = (StreamStatus *)table;
	
	sprintf(buf,"update stream_session set strStreamType = \'%s\',strStatus_date = \'%s\',strBind_userID = \'%s\',strBind_date = \'%s\',strSwitch_task_id = \'%s\' where istreamID = %d",\
			tab1->strStreamType,tab1->strStatus_date,tab1->strBind_userID,tab1->strBind_date,tab1->strSwitch_task_id,tab1->istreamID); //,tab1->strSessionID,tab1->strSerialNo
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
	#ifdef USEMUTEX
		pthread_mutex_unlock(&locker);
	#endif
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res =  mysql_query(conn_ptr, buf);
	
#ifdef USEMUTEX
	pthread_mutex_unlock(&locker);
#endif

	if(res)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d replace streamid = %d error\n",__FUNCTION__,__LINE__,tab1->istreamID);
		return false;
	}
	return true;

}
bool DBInterfacer::select_table(int table_num,int pri_key)//:参数说明:表编号，主键值
{
	return true;
}


bool DBInterfacer::FindOneStream(int table_num,char* pri_key,char* key_value,void *table)
{
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d this is findonestream function\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d lock\n",__FUNCTION__,__LINE__);
#endif

	int res;
	char table_name[64]={0};
	char key_name[10];
	char buf[2048];
	int j,i;

	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d find one stream table=%d where %s\n",__FUNCTION__,__LINE__,table_num,key_value);
	//strcpy(key_name,pri_key);
	switch(table_num)
	{
		case 1:
		{	
			strcpy(table_name,"stream_session");
			//memcpy(key_name,"istreamID",sizeof(key_name));
			break;
		}
		case 2:
		{	
			strcpy(table_name,"ucs_user_action");
			//memcpy(key_name,"iID",sizeof(key_name));
			break;
		}
		case 3:
		{	
			strcpy(table_name,"ucs_network_area");
			//memcpy(key_name,"strNetRegionNum",sizeof(key_name));
			break;
		}
		case 4:
		{	
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d find Resource %s \n",__FUNCTION__,__LINE__,pri_key);
			strcpy(table_name,"ucs_stream_resource");
			//memcpy(key_name,"iStreamID",sizeof(key_name));

			break;
		}
		case 5:
		{	
			strcpy(table_name,"ucs_ipqam_info");
			//memcpy(key_name,"iIPQAMNum",sizeof(key_name));
			break;
		}
		default:
			res = 1;
			break;
	}
	if(key_value == NULL)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d the key is this is NULL\n",__FUNCTION__,__LINE__);
		return false;
	}
	
	if(strlen(key_value)>5)
		sprintf(buf,"select *from %s where %s = \"%s\"",table_name,pri_key,key_value);
	else
	    sprintf(buf,"select *from %s where %s = %s",table_name,pri_key,key_value);	
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		pthread_mutex_unlock(&locker);
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr, buf); //查询语句  
    if (res) 
	{         
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error:%s\n",__FUNCTION__,__LINE__,mysql_error(conn_ptr));
    } 
	else
	{        
    	res_ptr = mysql_store_result(conn_ptr);             //取出结果集  
        if(res_ptr) 
		{               
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			if((unsigned long)mysql_num_rows(res_ptr) <=0)
			{	
				LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d select false\n",__FUNCTION__,__LINE__);
				pthread_mutex_unlock(&locker);
				return false;
			}
          	
            while((sqlrow = mysql_fetch_row(res_ptr)))  
			{   //依次取出记录  
            //	for(i = 0; i < j; i++)         
            //    printf("%s\t", sqlrow[i]);              //输出  
            //    printf("\n");       
			            	
				switch(table_num)
				{
					case 1:          // 1:代表stream_session values 表
					{
						StreamStatus *pTmpStream = (StreamStatus *)table;
						memset(pTmpStream,0,sizeof(StreamStatus));

						int istreamID;
			
					
						if(sqlrow[0] != NULL)
							pTmpStream->istreamID = atoi(sqlrow[0]);
						if(sqlrow[1] != NULL)
							memcpy(pTmpStream->strStreamType,sqlrow[1],strlen(sqlrow[1])+1);
						if(sqlrow[2] != NULL)
							memcpy(pTmpStream->strStatus_date,sqlrow[2],strlen(sqlrow[2])+1);
						if(sqlrow[3] != NULL)
							memcpy(pTmpStream->strBind_userID,sqlrow[3],strlen(sqlrow[3])+1);
						if(pTmpStream->strBind_userID!=NULL)
						{
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d strBind_userID = %s\n",__FUNCTION__,__LINE__,pTmpStream->strBind_userID);
						}
						if(sqlrow[4] != NULL)
							memcpy(pTmpStream->strBind_date,sqlrow[4],strlen(sqlrow[4])+1);
						if(sqlrow[5] != NULL)
							memcpy(pTmpStream->strSwitch_task_id,sqlrow[5],strlen(sqlrow[5])+1);					
						break;
					}	
					case 4:       // 4:代表ucs_stream_resource 表
					{
						
						StreamResource *pTmpStream = (StreamResource *)table;
						if(sqlrow[0] != NULL)
							pTmpStream->iStreamID = atoi(sqlrow[0]);
						if(sqlrow[1] != NULL)
							pTmpStream->iIPQAMNum = atoi(sqlrow[1]);
						if(sqlrow[2] != NULL)
							memcpy(pTmpStream->strNetRegionNum,sqlrow[2],strlen(sqlrow[2])+1);
						if(sqlrow[3] != NULL)
							memcpy(pTmpStream->strNav_url,sqlrow[3],strlen(sqlrow[3])+1);
						if(sqlrow[4] != NULL)
						{
							pTmpStream->iChannel_info = atoi(sqlrow[4]);
							LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d iChannelInfo = %d\n",__FUNCTION__,__LINE__,pTmpStream->iChannel_info);
						}
						if(sqlrow[5] != NULL)
							pTmpStream->iWherether_HD = atoi(sqlrow[5]);
						break;
					}	
					default:
					{
						LOG_WARN_FORMAT("%s %d wrong table number\n",__FUNCTION__,__LINE__);
						res = 1;
						break;
					}	
				}	
             } 
			 LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to free\n",__FUNCTION__,__LINE__);
			 mysql_free_result(res_ptr);
             if (mysql_errno(conn_ptr)) 
			 {                      
             	fprintf(stderr,"Retrive error:s\n",mysql_error(conn_ptr));               
#ifdef USEMUTEX

				pthread_mutex_unlock(&locker);
#endif
				return false;
			 }        
          }               
     }  	
#ifdef USEMUTEX
	
	pthread_mutex_unlock(&locker);
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d unlock\n",__FUNCTION__,__LINE__);
#endif
	return true;
}

bool DBInterfacer::FindSameGroupStream(char *strNetworkNum,int ishd,SetGroupStream& setStreamTmp)
{
#ifdef USEMUTEX
	pthread_mutex_lock(&locker);
#endif
	int res;
	char strSQL[1024] ={0};
	sprintf(strSQL,"select * from ucs_stream_resource where strNetRegionNum=%s and strWhether_HD = %d",strNetworkNum,ishd);
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,strSQL);
	
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		pthread_mutex_unlock(&locker);
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res = mysql_query(conn_ptr,strSQL);
	if(!res)
	{
		res_ptr = mysql_store_result(conn_ptr);
		if(res_ptr)
		{			
			LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d %lu Rows\n",__FUNCTION__,__LINE__,(unsigned long)mysql_num_rows(res_ptr));
			//j = mysql_num_fields(res_ptr);	 //列		
			while((sqlrow = mysql_fetch_row(res_ptr)))	
			{				
	
				int iStreamID = 0;
				if(sqlrow[0] != NULL)
				{
					iStreamID = atoi(sqlrow[0]);
					LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d group stream id =%d\n",__FUNCTION__,__LINE__,iStreamID);
					setStreamTmp.insert( iStreamID);
				}
			}	
			mysql_free_result(res_ptr);
		}

	}
	else
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX		
		pthread_mutex_unlock(&locker);
#endif
		return false;
	}
#ifdef USEMUTEX	
	pthread_mutex_unlock(&locker);
#endif	
	return true;
}

bool DBInterfacer::update_table(int pri,int status)
{
	
}

bool DBInterfacer::FindUsername(int prikey,char *username)
{
#ifdef USEMUTEX
		pthread_mutex_lock(&locker);
#endif
		int res;
		char strSQL[1024] ={0};
		sprintf(strSQL,"select * from stream_session where istreamID = %d",prikey);
		printf("---1234 %s \n",strSQL);
		fflush(stdout);
		if(mysql_ping(conn_ptr)!=0)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		#ifdef USEMUTEX
			pthread_mutex_unlock(&locker);
		#endif
			return false;
		}
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
		res = mysql_query(conn_ptr,strSQL);
		if(!res)
		{
			res_ptr = mysql_store_result(conn_ptr);
			if(res_ptr)
			{				
				while((sqlrow = mysql_fetch_row(res_ptr)))	
				{				
		
					int iStreamID = 0;
					if(sqlrow[3] != NULL)
					{
						memcpy(username,sqlrow[3],strlen(sqlrow[3])+1);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d username =%s\n",__FUNCTION__,__LINE__,username);
					}
				}	
				mysql_free_result(res_ptr);
			}
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX		
			pthread_mutex_unlock(&locker);
#endif
			return false;
		}
#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
#endif	
		return true;
}

bool DBInterfacer::LoadALLIPQAMRes(int ipqaminid,int *port,int streamid)
{
#ifdef USEMUTEX
		pthread_mutex_lock(&locker);
#endif
		int res;
		char strSQL[1024] ={0};
		int ipqamID = 0;
		sprintf(strSQL,"select * from ipqam_resource where ipqamInfoId = %d and state = 0",ipqaminid);
		if(mysql_ping(conn_ptr)!=0)
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
		#ifdef USEMUTEX
			pthread_mutex_unlock(&locker);
		#endif
			return false;
		}
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
		res = mysql_query(conn_ptr,strSQL);
		if(!res)
		{
			res_ptr = mysql_store_result(conn_ptr);
			if(res_ptr)
			{				
				if((sqlrow = mysql_fetch_row(res_ptr)))	
				{				
					if(sqlrow[0] != NULL)
					{
						ipqamID = atoi(sqlrow[0]);
					}
					if(sqlrow[1] != NULL)
					{
						*port = atoi(sqlrow[1]);
						LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d port =%d\n",__FUNCTION__,__LINE__,*port);
					}
				}	
				mysql_free_result(res_ptr);
			}
		}
		else
		{
			LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d SELECT error\n",__FUNCTION__,__LINE__);
#ifdef USEMUTEX		
			pthread_mutex_unlock(&locker);
#endif
			return false;
		}
#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
#endif	
		UpdateIPQAMRes(ipqamID,streamid);
		LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d IPQAM resource return\n",__FUNCTION__,__LINE__);
		return true;
	
}

bool DBInterfacer::UpdateIPQAMRes(int myid,int streamid)
{
#ifdef USEMUTEX	
	pthread_mutex_lock(&locker);
#endif	

	int pri_key;
	char *name;
	char *key;

	StreamStatus *tab1;
	char buf[2048];
	memset(buf,0,sizeof(buf));
	int res = 0;
	
	sprintf(buf,"update ipqam_resource set state = 1,stream_id = %d where ipqamResId = %d",streamid,myid); //,tab1->strSessionID,tab1->strSerialNo
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
	#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
	#endif	
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res =  mysql_query(conn_ptr, buf);
	#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
	#endif
	if(res)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d replace error\n",__FUNCTION__,__LINE__);
		return false;
	}
	return true;
}

bool DBInterfacer::ResetIPQAMRes()
{
#ifdef USEMUTEX	
	pthread_mutex_lock(&locker);
#endif	

	char buf[1024];
	memset(buf,0,sizeof(buf));
	int res = 0;
	
	sprintf(buf,"update ipqam_resource set state = 0,stream_id = 0"); //,tab1->strSessionID,tab1->strSerialNo
	LOG_INFO_FORMAT("INFO  - [SM]:%s %d %s\n",__FUNCTION__,__LINE__,buf);
	if(mysql_ping(conn_ptr)!=0)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d ping error: -1004\n",__FUNCTION__,__LINE__);
	#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
	#endif	
		return false;
	}
	LOG_DEBUG_FORMAT("DEBUG  - [SM]:%s %d begin to query\n",__FUNCTION__,__LINE__);
	res =  mysql_query(conn_ptr, buf);
	#ifdef USEMUTEX	
		pthread_mutex_unlock(&locker);
	#endif
	if(res)
	{
		LOG_ERROR_FORMAT("ERROR  - [SM]:%s %d replace\n",__FUNCTION__,__LINE__);
		return false;
	}
	return true;
}


