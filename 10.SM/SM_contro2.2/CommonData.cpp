char strAdverIP[128];
int iAdverPort;

char strNavIP[128];
int iNavPort;

char strVodIP[128];
int iVodPort;

char strMsiIP[128];
int iMsiPort;

char strMyServerIP[128];
int iMSIServerPort;

char strVOD_KeyIP[128];

char dbip[128];
char dbname[128];
char dbuser[128];
char dbpass[128];

char hdadvname[128];
char hdadvip[128];
char hdadvport[64];

char advip[128];
int  muladvport;

char pauseurl[256];
char quiturl[256];

char strsdNavIP[128];
int isdNavPort;

char hdrate[64];
char sdrate[64];

char navgoback[256];

char sdpauseurl[256];
char sdquiturl[256];
char sdnavgoback[256];

char sdadvname[128];
char sdadvip[128];
char sdadvport[64];

int iwaittime;

int frequency;
int pid;

int advflag;   //是否使用均衡切流器1:使用 0:不使用

char strblanIP[64];
int iBlanport;

int Istype = 1;                            //VGW的类型  0:ENRICH  1:SIHUA  2:VLC  
int VOD_play_clean = 1000*1000;              //释放： 广告流 或者 导航流  或者  广告+CDN流
int VodStreamOver_clean = 1000*1000;		//VodStreamOver （goback）调用  清掉：切流器，cdn的流
int BindOverTime_clean = 1000*1800;			//BindOverTime 释放：导航流
int RecoverVodPlay_clean = 1000*1000;		//RecoverVodPlay 释放：导航流 
int InitStream_clean = 1000*1000;			//InitStream  初始化释放所有流
int PauseVOD_clean = 1000*1000;				//PauseVOD 释放掉一路广告流 