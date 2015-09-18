
#include "Switch_Manager.h"
#include <string>
#include "Log.h"


Switch_Manager::Switch_Manager()
{
	m_iCurrentPort = iBeginPort;
	m_iPortIndex = 0;
	m_iCurrentSwitchNumber = 0;
	pthread_mutex_init(&m_lockerttTaskInfo, NULL);
	pthread_mutex_init(&m_lockerIndexPort, NULL);
}
Switch_Manager::~Switch_Manager()
{
	pthread_mutex_destroy(&m_lockerIndexPort);

	m_iCurrentSwitchNumber = 0;
	m_iMaxSwitchNumber = 0;
	pthread_mutex_destroy(&m_lockerttTaskInfo);
}

bool Switch_Manager::Init(char *SW_ip,int iMaxSwitchNumber) //需要管理发送 端口
{

	memset(m_SW_ip,0,sizeof(m_SW_ip));

	//m_iPortIndex = 0;
	m_iMaxSwitchNumber = iMaxSwitchNumber;
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

	LOG_INFO("INFO  - [SWT]: Delete ALL Stream \n");
	m_iCurrentSwitchNumber=0;
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
		LOG_WARN_FORMAT("WARN  - [SWT]: seeionID %s is in used \n",strSessionID);
		return -2;
	}
	//pthread_mutex_unlock(&m_lockerttTaskInfo);


	SwitchInfo* pCheckInfo = new SwitchInfo;
	memset(pCheckInfo,0,sizeof(SwitchInfo));

	
//	检测当前端口是否在使用
	//pthread_mutex_lock(&m_lockerttTaskInfo);
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
			m_iCurrentSwitchNumber++;
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
		LOG_INFO_FORMAT("INFO  - [SWT]: add switch seesion %s success same src port %d\n",strSessionID,iSrcPort);
		
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
		LOG_WARN("WARN  - [SWT]: InitRecvStream return error\n");
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
	m_iCurrentSwitchNumber++;
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

	LOG_INFO_FORMAT("INFO  - [SWT]: add switch session %s success new port %d\n",strFirstID.c_str(),iSrcPort);

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
		m_iCurrentSwitchNumber--;
		m_mapSwitchTaskInfo.erase(itfind);
	//	pthread_mutex_unlock(&m_lockerttTaskInfo);
		
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
	LOG_INFO_FORMAT("INFO  - [SWT]: delete session %s switch \n",strSessionID);

			
	return 0;
}

int Switch_Manager::ReqOneSwitchForPort(char* strSessionID,char *pIp,int *pPort)
{
		//检查当前tosk是否在用
/*	pthread_mutex_lock(&m_lockerttTaskInfo);
	MapSwitchInfo::iterator itfind1 = m_mapSwitchTaskInfo.find(strSessionID);
	if(itfind1 != m_mapSwitchTaskInfo.end())
	{
		printf("------strseeionID %s is in used \n",strSessionID);
		pthread_mutex_unlock(&m_lockerttTaskInfo);
		LOG_WARN_FORMAT("WARN  - [SWT]: seeionID %s is in used \n",strSessionID);
		return -2;
	}
	pthread_mutex_unlock(&m_lockerttTaskInfo);
*/	
	int iPort = 0;
	int ret = -1;
	do
	{
		pthread_mutex_lock(&m_lockerIndexPort);
		iPort = m_iPortIndex + UDP_PORT_LOW;
		printf("---port =%d  iportIndex=%d \n",iPort,m_iPortIndex);
		ret = applyforUDPPort(m_SW_ip,&iPort);
		printf("---get port =%d \n",iPort);
		
		m_iPortIndex +=2;
		
		if(m_iPortIndex >= (UDP_PORT_HIGH - UDP_PORT_LOW))
			m_iPortIndex = 0;
		pthread_mutex_unlock(&m_lockerIndexPort);
		
	}while(ret < 0);

	
	LOG_INFO_FORMAT("INFO  - [SWT]: Req PORT get udp port %d \n",iPort);
	*pPort = iPort;
	strcpy(pIp,m_SW_ip);
	return 0;
}
/*
int Switch_Manager::applyforUDPPort(char *ip,int *port)
{
	struct sockaddr_in bindaddr;
	socklen_t len;
	
	struct sockaddr_in servaddr;
	memset(&servaddr,0, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = inet_addr(ip);
	servaddr.sin_port = htons(port);
	
	int test_socket = socket(AF_INET, SOCK_DGRAM, 0);
	if(test_socket == -1)
		return -1;
	
	int optval = 1;
	if ((setsockopt(test_socket,SOL_SOCKET,SO_REUSEADDR,&optval,sizeof(int))) == -1)
	{
		close(test_socket);
		return -2;
	}

	if(bind(test_socket, (struct sockaddr *)&servaddr, sizeof(servaddr)) == -1)
	{
		close(test_socket);
		return -3;
	}
	
	if( 0 == getsockname( test_socket, ( struct sockaddr* )&bindaddr, &len ))
	{
		//printf("%s:%d\n",inet_ntoa(bindaddr.sin_addr),ntohs(bindaddr.sin_port));
		
		*port = ntohs(bindaddr.sin_port);
	}
	else
	{
		close(test_socket);
		return -4;
	}
	
	close(test_socket);
	return 0;
}
*/

int Switch_Manager::GetCurrentSwitchNum(int *pCurentnum,int *pMaxNum)
{
	*pCurentnum = m_iCurrentSwitchNumber;
	*pMaxNum = m_iMaxSwitchNumber;

	return 0;
}


