
#include "Switch_Manager.h"
#include <string>

Switch_Manager::Switch_Manager()
{
	m_iCurrentPort = iBeginPort;
	pthread_mutex_init(&m_lockerttTaskInfo, NULL);
}
Switch_Manager::~Switch_Manager()
{

	pthread_mutex_destroy(&m_lockerttTaskInfo);
}

bool Switch_Manager::Init(char *SW_ip) //需要管理发送 端口
{

	memset(m_SW_ip,0,sizeof(m_SW_ip));

	strcpy(m_SW_ip,SW_ip);
	printf("----used SW_IP is %s \n",m_SW_ip);
	fflush(stdout);
	return true;
}
//创建映射关联 

int Switch_Manager::DeleteAllWsitch()
{
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itlook = m_mapSwitchTaskInfo.begin();
	while(itlook != m_mapSwitchTaskInfo.end())
	{
		//printf("---map size %d  first %s \n",m_mapSwitchTaskInfo.size(),itlook->first.c_str());
		printf("---clean ALL stream \n");
		fflush(stdout);
		char strSeesid[128]={0};
		strcpy(strSeesid,itlook->first.c_str());
		//DeleteOneSwitch(strSeesid);

			MapSwitchInfo::iterator itfind = m_mapSwitchTaskInfo.find(strSeesid);
			if(itfind != m_mapSwitchTaskInfo.end())
			{
				//先停止转发
				printf("---clean one stream \n");
				fflush(stdout);
				SwitchStream *pTmp = itfind->second;
				Switch_Stream* pStream = NULL;
				if(pTmp)
				{
					printf("-----1 \n");
					pStream = pTmp->pSwitchStream;
					if(pStream)
					{
						printf("-----2\n");
						pStream->EndStream(pTmp->pSwitchInfo);
						printf("-----check delete \n");
						if(pStream->IsCanDelete())
						{
							printf("----delete 1\n");
							delete pStream;
							printf("----delete 2\n");
						}	
					}	
					if(pTmp->pSwitchInfo)
						delete pTmp->pSwitchInfo;
					delete pTmp;
					printf("------3\n");
				}
				printf("------4\n");
				fflush(stdout);
				m_mapSwitchTaskInfo.erase(itfind);
				//pthread_mutex_unlock(&m_lockerttTaskInfo);
				
			}
		
		++itlook;
	}
	pthread_mutex_unlock(&m_lockerttTaskInfo);

	return 0;
}

int Switch_Manager::CheckOneSwitch(char* strSessionID)
{
	int ret = -1;
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itlook = m_mapSwitchTaskInfo.find(strSessionID);
	if(itlook != m_mapSwitchTaskInfo.end())
	{
		printf("---check one switch find %s \n",strSessionID);
		fflush(stdout);
		SwitchStream *pTmp = itlook->second;
		Switch_Stream* pStream = NULL;
		if(pTmp)
			pStream = pTmp->pSwitchStream;
		
		pthread_mutex_unlock(&m_lockerttTaskInfo);
		if(pStream)
		{
			MapCheckSwitchInfo::iterator itcheck = m_mapCheckSwitchInfo.find(strSessionID);

			SwitchInfo *pCheckSwitchInfo =NULL;
			pCheckSwitchInfo = itcheck->second;

			if(pCheckSwitchInfo)
			{
				pStream->CheckOneSwitch(pCheckSwitchInfo);

				printf("---get status %d \n",pCheckSwitchInfo->iCheckStatus);
				ret = pCheckSwitchInfo->iCheckStatus;
			}
		}
		
	}
	else
	{
		pthread_mutex_unlock(&m_lockerttTaskInfo);
		printf("---check one switch not find %s \n",strSessionID);
		fflush(stdout);
		ret = -1;
	}
	
	return ret;
}



//返回的是关联的会话
int Switch_Manager::AddOneSwitch(char* inputUrl,char* outPutUrl,char* strSessionID)
{
	//接收指定IP
	//解析出ip 端口
	int iSrcPort = 0;
	int iDstPort = 0;

	char strSrcIP[128]={0};
	char strDstIP[128]={0};

	char strSrcPort[32]={0};
	FindDataFromString(inputUrl,strSrcIP,strSrcPort);
	char strDstPort[32]={0};
	FindDataFromString(outPutUrl,strDstIP,strDstPort);


	iSrcPort = atoi(strSrcPort);
	iDstPort = atoi(strDstPort);

	if(iSrcPort==0 || iDstPort == 0 ||strcmp(strDstIP,"")==0)
		return -1;

//	printf("udp://%s:%d\n",strSrcIP,iSrcPort);
//	printf("udp://%s:%d\n",strDstIP,iDstPort);

	//检查当前tosk是否在用
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itfind1 = m_mapSwitchTaskInfo.find(strSessionID);
	if(itfind1 != m_mapSwitchTaskInfo.end())
	{
		printf("------strseeionID %s is in used \n",strSessionID);
		pthread_mutex_unlock(&m_lockerttTaskInfo);
		return -2;
	}
	pthread_mutex_unlock(&m_lockerttTaskInfo);


	SwitchInfo* pCheckInfo = new SwitchInfo;
	memset(pCheckInfo,0,sizeof(SwitchInfo));

	
//	检测当前端口是否在使用
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itfind =  m_mapSwitchTaskInfo.begin();
	while(itfind != m_mapSwitchTaskInfo.end())
	{
		SwitchStream *ptmp = itfind->second;
		SwitchInfo *pInfo = ptmp->pSwitchInfo;
		
		char pTaskID[128]={0};
		strcpy(pTaskID,strSessionID);
		printf("----session iD %s \n",pTaskID);
		fflush(stdout);
		std::string strFirstID;
		strFirstID.assign(pTaskID);
		
		if(pInfo->iSRCPort == iSrcPort)
		{
			//组播转发多路 单播一对多问题
			//暂不支持同一端口多对多
			
			SwitchStream *pSwitchTasktmp = new SwitchStream;
			memset(pSwitchTasktmp,0,sizeof(SwitchStream));
			pSwitchTasktmp->pSwitchInfo = new SwitchInfo;

			memset(pSwitchTasktmp->pSwitchInfo,0,sizeof(SwitchInfo));
			pSwitchTasktmp->pSwitchInfo->iDstPort = iDstPort;
			strcpy(pSwitchTasktmp->pSwitchInfo->strDstIP,strDstIP);
			pSwitchTasktmp->pSwitchInfo->iSRCPort= iSrcPort;
			strcpy(pSwitchTasktmp->pSwitchInfo->strSRCIP,strSrcIP);

			pSwitchTasktmp->pSwitchStream = ptmp->pSwitchStream;

			pSwitchTasktmp->pSwitchStream->InitRecvStream(pSwitchTasktmp->pSwitchInfo);



			//pthread_mutex_lock(&m_lockerttTaskInfo);
			m_mapSwitchTaskInfo.insert(MapSwitchInfo::value_type(strFirstID,pSwitchTasktmp));
			//pthread_mutex_unlock(&m_lockerttTaskInfo);

			// insert check info
			memcpy(pCheckInfo,pSwitchTasktmp->pSwitchInfo,sizeof(SwitchInfo));
			pCheckInfo->iCheckStatus = -1;
			MapCheckSwitchInfo::iterator itFind3 = m_mapCheckSwitchInfo.find(strFirstID);
			if(itFind3 != m_mapCheckSwitchInfo.end())
			{
				SwitchInfo* pchecktmp = itFind3->second;
				m_mapCheckSwitchInfo.erase(itFind3);
				if(pchecktmp)
					delete pchecktmp;
				
			}
			m_mapCheckSwitchInfo.insert(MapCheckSwitchInfo::value_type(strFirstID,pCheckInfo));
			
			break;
			
		}

		++itfind;
	}
	pthread_mutex_unlock(&m_lockerttTaskInfo);

	if(itfind != m_mapSwitchTaskInfo.end())
	{
		//增加转发
		printf("----add switch same src port\n");
		fflush(stdout);

		
		return 0;
	}

	//增加端口管理

	SwitchStream *pSwitchTask = new SwitchStream;
	memset(pSwitchTask,0,sizeof(SwitchStream));
	pSwitchTask->pSwitchInfo = new SwitchInfo;

	memset(pSwitchTask->pSwitchInfo,0,sizeof(SwitchInfo));
	pSwitchTask->pSwitchInfo->iDstPort = iDstPort;
	strcpy(pSwitchTask->pSwitchInfo->strDstIP,strDstIP);
	pSwitchTask->pSwitchInfo->iSRCPort= iSrcPort;
	strcpy(pSwitchTask->pSwitchInfo->strSRCIP,strSrcIP);
	
	pSwitchTask->pSwitchStream = new Switch_Stream(m_iCurrentPort+2,m_SW_ip);

	//返回的是成功使用的端口
	int iret = pSwitchTask->pSwitchStream->InitRecvStream((pSwitchTask->pSwitchInfo));

	if(iret == -1)
	{
		//失败释放资源
		if(pSwitchTask->pSwitchInfo) delete pSwitchTask->pSwitchInfo;
		if(pSwitchTask->pSwitchStream) delete pSwitchTask->pSwitchStream;
		return -1;
	}
	//记录当前使用端口，下次以此端口为起始查找

	

	printf("====new stream create ");
	fflush(stdout);
	m_iCurrentPort = iret;
	char pTaskID[128]={0};
	strcpy(pTaskID,strSessionID);
	printf("----session iD %s \n",pTaskID);
	fflush(stdout);
	std::string strFirstID;
	strFirstID.assign(pTaskID);
	
	pthread_mutex_lock(&m_lockerttTaskInfo);
	m_mapSwitchTaskInfo.insert(MapSwitchInfo::value_type(strFirstID,pSwitchTask));

		// insert check info
	memset(pCheckInfo,0,sizeof(SwitchInfo));
	memcpy(pCheckInfo,pSwitchTask->pSwitchInfo,sizeof(SwitchInfo));
	pCheckInfo->iCheckStatus = -1;
	MapCheckSwitchInfo::iterator itFind4 = m_mapCheckSwitchInfo.find(strFirstID);
	if(itFind4 != m_mapCheckSwitchInfo.end())
	{
		SwitchInfo* pchecktmp = itFind4->second;
		m_mapCheckSwitchInfo.erase(itFind4);
		if(pchecktmp)
			delete pchecktmp;
		
	}
	m_mapCheckSwitchInfo.insert(MapCheckSwitchInfo::value_type(strFirstID,pCheckInfo));
	
	pthread_mutex_unlock(&m_lockerttTaskInfo);


	return 0;
}

int Switch_Manager::DeleteOneSwitch(char* strSessionID)
{
	//查找会话
	printf("---find sessionid %s map size=%d \n",strSessionID,m_mapSwitchTaskInfo.size());
	fflush(stdout);
	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itfind = m_mapSwitchTaskInfo.find(strSessionID);
	if(itfind != m_mapSwitchTaskInfo.end())
	{
		//先停止转发
		printf("---clean one stream \n");
		fflush(stdout);
		SwitchStream *pTmp = itfind->second;
		Switch_Stream* pStream = NULL;
		if(pTmp)
		{
			printf("-----1 \n");
			pStream = pTmp->pSwitchStream;
			if(pStream)
			{
				printf("-----2\n");
				pStream->EndStream(pTmp->pSwitchInfo);
				printf("-----check delete \n");
				if(pStream->IsCanDelete())
				{
					printf("----delete 1\n");
					delete pStream;
					printf("----delete 2\n");
				}	
			}	
			if(pTmp->pSwitchInfo)
				delete pTmp->pSwitchInfo;
			delete pTmp;
			printf("------3\n");
		}
		printf("------4\n");
		fflush(stdout);
		m_mapSwitchTaskInfo.erase(itfind);
		pthread_mutex_unlock(&m_lockerttTaskInfo);
		
	}
	else
	{
		printf("---erase switch map %s failed :not find \n",strSessionID);
		fflush(stdout);
	}

	MapCheckSwitchInfo::iterator itFind = m_mapCheckSwitchInfo.find(strSessionID);
	if(itFind != m_mapCheckSwitchInfo.end())
	{
		printf("---erase check info map %s success\n",strSessionID);
		fflush(stdout);
		SwitchInfo* pchecktmp = itFind->second;
		m_mapCheckSwitchInfo.erase(itFind);
		if(pchecktmp)
			delete pchecktmp;
		
	}
	else
	{
		printf("---erase check info map %s failed :not find\n",strSessionID);
		fflush(stdout);
	}
	
	pthread_mutex_unlock(&m_lockerttTaskInfo);

			
	return 0;
}


