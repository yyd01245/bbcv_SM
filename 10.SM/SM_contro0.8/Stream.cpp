#include "Stream.h"
#include "CommonFun.h"
#include "cJSON.h"

Stream::Stream()
{

	pRet_root = NULL;
	m_clientSocket = -1;
	m_idstport = 0;
	memset(m_strdstIP,0,sizeof(m_strdstIP));
	memset(m_Url,0,sizeof(m_Url));
	
}
Stream::~Stream()
{
	if(m_clientSocket != -1)
		close(m_clientSocket);
	
	m_clientSocket = -1;
}


bool Stream::Send_Jsoon_str()
{


	char cJsonBuff[1024 * 2];
	char * m_tmp;
	m_tmp = cJSON_Print(pRet_root);
	memset(cJsonBuff, 0, sizeof(cJsonBuff));
	sprintf(cJsonBuff, "%sXXEE", m_tmp);
	free(m_tmp);
	printf("-----%s \n",cJsonBuff);
	cJSON_Delete(pRet_root);
	pRet_root = NULL;

	
	if(m_clientSocket != -1)
	{
		send(m_clientSocket, cJsonBuff, strlen(cJsonBuff), 0);
		printf("---send len=%d\n",strlen(cJsonBuff));
		printf("---send over\n");
	}
}

bool Stream::Parse_Json_str(char *cBuffRecv)
{
	cJSON *pItem = NULL;
	cJSON *pcmd = NULL;
	cJSON *pSerialNo = NULL;
	cJSON *pRet_root = NULL;
	cJSON* pRoot = cJSON_Parse(cBuffRecv);
	if (pRoot)
	{
		pSerialNo = cJSON_GetObjectItem(pRoot, "serialno");
		pcmd = cJSON_GetObjectItem(pRoot, "cmd");
		if (pcmd)
		{

			//ÅÐ¶ÏÇëÇóÀàÐ
			if (strcmp(pcmd->valuestring, "login") == 0)
			{
				//»ñÈ¡·µ»ØÂë
				cJSON* pRetCode = cJSON_GetObjectItem(pRoot, "retcode");
				char strretcode[128]={0};
				memcpy(strretcode,pRetCode->valuestring,strlen(pRetCode->valuestring)+1);
				if(atoi(strretcode) >= 0)
				{
					//success
					
				}
				else
				{
					//failed
				}
			
			}
			else if (strcmp(pcmd->valuestring, "logout") == 0)
			{

			}
			//×´Ì¬¼à²âÔÝÊ±ÎÞ
				
		}
	}
	
	return true;
}


bool Stream::Requst_Json_str(int iType,const char* strRequstType,const char* strsecRequstContxt)
{
	if(NULL == pRet_root)
	{
		printf("Json error no create \n");
		return false;
	}
	switch(iType)
	{
		case 1:
		{
			//int 
			
			cJSON_AddNumberToObject(pRet_root, strRequstType, atoi(strsecRequstContxt));
		}
		break;
		case 2:
		{
			//string
			cJSON_AddStringToObject(pRet_root, strRequstType, strsecRequstContxt);
		}
		break;
		default:
		{
			//string
			cJSON_AddStringToObject(pRet_root, strRequstType, strsecRequstContxt);
		}
		break;
	}

}


