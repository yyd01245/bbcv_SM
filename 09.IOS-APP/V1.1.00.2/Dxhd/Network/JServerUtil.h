//
//  JServerUtil.h
//  dphd
//
//  Created by j on 13-11-28.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#ifndef _JSERVERUTIL_H
#define _JSERVERUTIL_H


/********************** host ************************/

#define HOST_URL (@"http://218.108.50.250:18080")
//#define DEFAULT_FLAG @"xxxxxx"

/********************** 返回结果 ***********************/
#define RETURN_SUCCESS            0           //启动成功
#define REQUEST_FAIL              @"请求失败"

/********************** cmd ************************/

#define CMD_AUTH_CODE_REQ           @"auth_code_req"          //接入授权
#define CMD_STB_LOGIN_REQ           @"stb_login_req"          //登录
#define CMD_BIND_STB_REQ            @"bind_stb_req"           //绑定STB
#define CMD_UNBIND_STB_REQ          @"unbind_stb_req"         //解绑STB
#define CMD_COMMAND_SEND_REQ        @"command_send_req"       //指令下发
#define CMD_KEY_SEND_REQ            @"key_send_req"           //键值下发
#define CMD_STB_LOGOUT_REQ          @"stb_logout_req"         //STB退出

/********************** sequence ************************/

#define SEQUENCE @"12345678"  //默认一个

/********************** key type ************************/

#define KEYTYPE @"1001"


/********************** app name ************************/

#define APPNAME @"KYSX"


/********************** key ************************/

#define BlcIrrPropertyValue_POWER             @"0x0a"     //待机
#define BlcIrrPropertyValue_MUTE              @"0x0c"     //静音
#define BlcIrrPropertyValue_DIGIT1            @"0x11"     //数字键1
#define BlcIrrPropertyValue_DIGIT2            @"0x12"     //数字键2
#define BlcIrrPropertyValue_DIGIT3            @"0x13"     //数字键3
#define BlcIrrPropertyValue_DIGIT4            @"0x14"     //数字键4
#define BlcIrrPropertyValue_DIGIT5            @"0x15"     //数字键5
#define BlcIrrPropertyValue_DIGIT6            @"0x16"     //数字键6
#define BlcIrrPropertyValue_DIGIT7            @"0x17"     //数字键7
#define BlcIrrPropertyValue_DIGIT8            @"0x18"     //数字键8
#define BlcIrrPropertyValue_DIGIT9            @"0x19"     //数字键9
#define BlcIrrPropertyValue_DIGIT0            @"0x10"     //数字键0
#define BlcIrrPropertyValue_ARROWUP           @"0x00"     //箭头上
#define BlcIrrPropertyValue_ARROWDOWN         @"0x01"     //箭头下
#define BlcIrrPropertyValue_ARROWLEFT         @"0x03"     //箭头左
#define BlcIrrPropertyValue_ARROWRIGHT        @"0x02"     //箭头右
#define BlcIrrPropertyValue_SELECT            @"0x1f"     //确定
#define BlcIrrPropertyValue_EXIT              @"0x1d"     //退出
#define BlcIrrPropertyValue_PAGEDOWN          @"0x08"     //快退
#define BlcIrrPropertyValue_PAGEUP            @"0x04"     //快进
#define BlcIrrPropertyValue_VOLUMEDOWN        @"0x0b"     //音量减小
#define BlcIrrPropertyValue_VOLUMEUP          @"0x06"     //音量增加



#endif
