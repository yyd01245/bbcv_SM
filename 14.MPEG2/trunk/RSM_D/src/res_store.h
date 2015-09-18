#ifndef _RES_STORE_H_
#define _RES_STORE_H_

#include "Socket_Ractor.h"
#include "Log.h"
#include "rsm_store.h"

using namespace std;

class Res_Store : public  Thread_Base
{
public:
 	int  run()
	{
		LOG_INFO_FORMAT("INFO :Res_Store is begin runing,Thread id is %u!\n",pthread_self());
		string session_tmp_file = "session.tmp";
		SaveSessionMsg  node;
		Rsm_Session_Msg session_msg;
		//1. open or create file
		FILE *back_file = NULL;
		
		if(access(session_tmp_file.c_str(), R_OK) != 0)// if not exsist, create it
		{
			back_file = fopen(session_tmp_file.c_str(),"wb+");
			if(back_file == NULL)
			{
				LOG_ERROR_FORMAT("ERROR :fopen file %s fail!\n",session_tmp_file.c_str());
				return -1;
			}
			fclose(back_file);
		}
	
		back_file = fopen(session_tmp_file.c_str(),"rb+");
		if(back_file == NULL)
		{
			LOG_ERROR_FORMAT("ERROR :fopen file %s fail!\n",session_tmp_file.c_str());
			return -1;
		}
	
		//2. load data fist time
		while(1)
		{
			memset(&node,0,sizeof(SaveSessionMsg));
			if(fread(&node,sizeof(SaveSessionMsg),1,back_file) != 1)
				break;
			session_msg.session_id = node.session_id;
			session_msg.ipqam_ip = node.ipqam_ip;
            session_msg.encoder_id = node.encoder_id;
            session_msg.vnc_add = node.vnc_add;
            session_msg.ipqam_port = node.ipqam_port;
            session_msg.key_add = node.key_add;
            session_msg.record_add = node.record_add;
			Rsm_Session::instance()->map_session[node.session_id] = session_msg;
        		Rsm_Session::instance()->map_encoder[node.encoder_id] = session_msg;	
		}

		//3 read from map and store in file
		map<string, Rsm_Session_Msg>::iterator iter;
		Rsm_Session_Msg tmp_msg;
		while(1)
		{
			sleep(3);
			truncate(session_tmp_file.c_str(),0);
			fseek(back_file,0,SEEK_SET);
			
			for(iter = Rsm_Session::instance()->map_session.begin(); iter != Rsm_Session::instance()->map_session.end();iter++)
			{
				tmp_msg = iter->second;
				memset(&node,0,sizeof(SaveSessionMsg));
				strncpy(node.session_id,tmp_msg.session_id.c_str(),sizeof(node.session_id));
				strncpy(node.ipqam_ip,tmp_msg.ipqam_ip.c_str(),sizeof(node.ipqam_ip));
				node.encoder_id = tmp_msg.encoder_id;
				node.vnc_add = tmp_msg.vnc_add;
				node.ipqam_port = tmp_msg.ipqam_port;
				node.key_add = tmp_msg.key_add;
				node.record_add = tmp_msg.record_add;
			
				if(fwrite(&node,sizeof(SaveSessionMsg),1,back_file) != 1)
					break;
			}
		}
		return 0;
	}

};

#endif


