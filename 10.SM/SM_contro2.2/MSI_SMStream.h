#ifndef _MSI_SMSTREAM_H_
#define _MSI_SMSTREAM_H_

#include "Stream.h"
#include <stdio.h>
#include <stdlib.h>
#include "SM_Manager.h"


class MSI_SM_Stream : public Stream
{
public:
	//程序启动之前组播工具已经发流，并设置好固定的地址
	MSI_SM_Stream(SM_Manager *pManager);
	~MSI_SM_Stream();


	bool CleanMSIStream(char *strUserId,char *strSerialno);
	
	bool Connect_MSIServer();
	bool TellMI(int isstreamid);
	SM_Manager *m_cSM_Manag;
	static void *Parse_recv_MSI_thread(void *arg);


	//bool StartMulticast();
	//bool DeleteMulticasst();
	//bool GetTaskStatus();
	
private:
	
	SM_Manager *m_pManager;

};

#endif

