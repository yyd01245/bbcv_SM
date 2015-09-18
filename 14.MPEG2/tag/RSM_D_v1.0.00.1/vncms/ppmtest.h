#ifndef		_PPM_H_
#define		_PPM_H_

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <net/if.h>
#include <netinet/in.h>
#include <linux/sockios.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include "rfbclient.h"
#define UDP_PORT_PLUS 40000

typedef enum {
	NblTermKeyDevType_IrrControl     	= 1001,//遥控器设备
	NblTermKeyDevType_WifiControl		= 1002,//WIFI设备
	NblTermKeyDevType_Console  		= 1003,//游戏手柄设备
	NblTermKeyDevType_Keyboard  		= 1004,//键盘设备
	NblTermKeyDevType_Mouse   			= 1005,//鼠标设备
} NblStbTermKeyDevType;

typedef enum {
    BlcMousePropertyStat_BUTTONLEFT     = 1,
    BlcMousePropertyStat_BUTTONMIDDLE   = 2,
    BlcMousePropertyStat_BUTTONRIGHT    = 3,
} BlcMousePropertyKey;

typedef enum {
    BlcMousePropertyStat_MOUSEDEFAULT   = 1,
    BlcMousePropertyStat_MOUSEDOWN      = 2,
    BlcMousePropertyStat_MOUSEUP        = 3,
    BlcMousePropertyStat_MOUSEWHEEL     = 4, //滚轮转动   
    BlcMousePropertyStat_MOUSELEFTDRAG  = 2, //左键拖动本来是5  现做成与down 相等
    BlcMousePropertyStat_MOUSERIGHTDRAG = 2, //右键拖动本来是6  现做成与down 相等
}BlcMousePropertyKeyStat;



/***********************发送给应用消息结构体定义*************************************************/
typedef struct {
unsigned int dev_type; // 键值设备类型 BlcYunKeyDevType
} BlcYunKeyMsgHead;

typedef struct {//红外
unsigned int sequence_num; // 序列号，由终端制定
unsigned int key_value; // 键值
unsigned int key_status; // 键值类型

unsigned int term_type; //BlcStbTermType
unsigned char term_id[32];
} BlcYunKeyIrrMsgBody;

typedef struct {
    BlcYunKeyMsgHead head; // 消息头
BlcYunKeyIrrMsgBody body; // 消息体
} BlcYunKeyIrrMsg;

typedef struct {//键盘
unsigned int sequence_num; // 序列号，由终端制定
unsigned int key_value; // 键值
unsigned int key_status; // 键值类型
} BlcYunKeyKeyboardMsgBody;

typedef struct {
    BlcYunKeyMsgHead head; // 消息头
BlcYunKeyKeyboardMsgBody body; // 消息体
} BlcYunKeyKeyboardMsg;

typedef struct {//游戏手柄
unsigned int sequence_num; // 序列号，由终端制定
unsigned int key_value; // 键值
unsigned int key_status; // 键值类型
short x; //系统坐标x
    short y; //系统坐标y
    short cursor_x; //屏幕光标x
    short cursor_y; //屏幕光标y
} BlcYunKeyConsoleMsgBody;

typedef struct {
    BlcYunKeyMsgHead head; // 消息头
BlcYunKeyConsoleMsgBody body; // 消息体
} BlcYunKeyConsoleMsg;

typedef struct {//鼠标
unsigned int sequence_num; // 序列号，由终端制定
unsigned int key_value; // 键值
unsigned int key_status; // 键值类型
short x; //系统坐标x
    short y; //系统坐标y
    short cursor_x; //屏幕光标x
    short cursor_y; //屏幕光标y
} BlcYunKeyMouseMsgBody;

typedef struct {
    BlcYunKeyMsgHead head; // 消息头
BlcYunKeyMouseMsgBody body; // 消息体
} BlcYunKeyMouseMsg;

/***********************发送给应用消息结构体定义*************************************************/


static int g_timeout;       //鎸夐敭瓒呮椂锛岄暱鏃堕棿鏈搷搴旀寜閿椂涓婃姤
static int g_keyfilt;       //閿�艰繃婊わ紝0.5绉掍箣鍐呯殑閲嶅鎸夐敭杩囨护

#endif

