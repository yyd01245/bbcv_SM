#ifndef _VGW_VOD_STREAM_H_
#define _VGW_VOD_STREAM_H_

#include "Stream.h"
#include <stdio.h>
#include <stdlib.h>
#include "SM_Manager.h"
#include "Base64.h"


typedef std::map<int,void*> MapSem_Stream;
typedef std::map<int,Stream*>MapVodPlay;  //与回复socket关联


class VGW_Vod_Stream : public Stream
{
public:
	//程序启动之前组播工具已经发流，并设置好固定的地址
	VGW_Vod_Stream(SM_Manager *pManager);
	~VGW_Vod_Stream();


	bool CleanVODStream(char *strSeesionId,char *strTaskID,char *strSerialno);
	
	bool Connect_VODServer();

	static void *Parse_recv_Vod_thread(void *arg);


	bool AddSemToStream(int iStreamID,void *psem);
	bool StartOneStream(int iStreamID,char *strUrl,char *strRegionID,char* strUserID);

	bool CheckStatus(int strSeesionId,char *strTaskID,char *strSerialno);

	MapVodPlay m_mapVod_player;
private:

	//ipqam的iP，Port
	char m_transmitIP[128];
	int m_iport;

	SM_Manager *m_pManager;

	MapSem_Stream m_mapSem_Stream;



};

#endif

