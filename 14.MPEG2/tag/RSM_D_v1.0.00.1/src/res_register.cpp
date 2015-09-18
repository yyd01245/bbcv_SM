#include "res_register.h"


int Res_Register::cJSON_Create(char buf[1024],const char cmd[8])
{
	cJSON* root;
	char* JSON_ret;
	char rate[8]={0};
	char freeNo[8]={0};
	char streamNo[8]={0};
	char totolNo[8]={0};
	char mgmtport[8]={0};
	int num_hd_free,num_sd_free,num_totol,num_stream;
	
	const char* serialno = "hello";
	root = cJSON_CreateObject();
	cJSON_AddStringToObject(root,"cmd",cmd);
	cJSON_AddStringToObject(root,"areaid",Rsm_Config::instance()->area_id.c_str());
	cJSON_AddStringToObject(root,"videotype",Rsm_Config::instance()->video_type.c_str());
	cJSON_AddStringToObject(root,"vnctype",Rsm_Config::instance()->ctype.c_str());
	cJSON_AddStringToObject(root,"vendor",Rsm_Config::instance()->vendor.c_str());

	VNC_State::instance()->vnc_check(num_hd_free,num_sd_free);
	num_stream = Rsm_Session::instance()->checkUser();
	if(error)
	{		
		num_totol = VNC_State::instance()->totol - num_hd_free;
		num_hd_free = 0;
	}
	else
	{
		num_totol = VNC_State::instance()->totol;
	}
	

	if(strcmp("vncregister",cmd))
	{		
		sprintf(freeNo,"%d",num_hd_free);
		cJSON_AddStringToObject(root,"freenum",freeNo);
		sprintf(streamNo,"%d",num_stream);
		cJSON_AddStringToObject(root,"streamnum",streamNo);
	}
	sprintf(totolNo,"%d",num_totol);
	cJSON_AddStringToObject(root,"totalnum",totolNo);

	sprintf(rate,"%d",Rsm_Config::instance()->vid_rate * 1024);
	cJSON_AddStringToObject(root,"rate",rate);
	cJSON_AddStringToObject(root,"vncip",Rsm_Config::instance()->rsm_ip.c_str());
	cJSON_AddStringToObject(root,"vncport",Rsm_Config::instance()->rsm_aim_listentport.c_str());
	sprintf(mgmtport,"%d",(atoi(Rsm_Config::instance()->rsm_aim_listentport.c_str())+1));
	cJSON_AddStringToObject(root,"mgmtport",mgmtport);

	cJSON_AddStringToObject(root,"serialno",serialno);

	JSON_ret = cJSON_PrintUnformatted(root);
	sprintf(buf,"%sXXEE",JSON_ret);
	free(JSON_ret);
	cJSON_Delete(root);
	return 0;
}


