//
//  JDefineUtil.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//



/********************** custom-view-parameter **********************/



/********************** custom-value-parameter **********************/



/********************** device **********************/

//#define IOS_5_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 5.0)
//#define IOS_6_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 6.0)
//#define IOS_7_OR_LATER ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)

#define IPHONE4 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 960), [[UIScreen mainScreen] currentMode].size) : NO) //判断是否是iphone4
#define IPHONE5 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO) //判断是否是iphone5
#define IPAD ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(768, 1024), [[UIScreen mainScreen] currentMode].size) : NO)
#define IPAD_RETAIN ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1536, 2048), [[UIScreen mainScreen] currentMode].size) : NO)

#define IOS_WIDTH (float)[[UIScreen mainScreen] bounds].size.width
#define IOS_HEIGHT (float)[[UIScreen mainScreen] bounds].size.height

#define IOS7 ([[[UIDevice currentDevice] systemVersion]floatValue]>=7)
#define IOS6ORLATER ([[[UIDevice currentDevice] systemVersion]floatValue]>=6)

/********************** defalut-value **********************/

#define DEFAULT_START_HEIGHT 20.0f  //状态栏高度
#define DEFAULT_NAV_HEIGHT 44.0f    //导航栏高度
#define DEFAULT_ROW_HEIGHT 44.0f    //表格行高
#define PAGE_TOOLBAR_HEIGHT 44.

#define DEFAULT_KEYBOARD_HEIGHT 256. //键盘高度

#define DEFARLT_VIEW_HEIGHT [UIScreen mainScreen].bounds.size.height-DEFAULT_START_HEIGHT-DEFAULT_NAV_HEIGHT

/********************** custom-flag-value **********************/

#define CUSTOM_FLAG 100 //自定义标记值

/********************** custom-number-value **********************/

//#define CUSTOM_PAGESIZE 10    //一次加载个数

/********************** custom-animate-value **********************/
#define CUSTOM_ANIMATION .3     //自定义动画时间

/********************** custom-image-name **********************/

//#define BACKGROUND_IMAGE @"1136_bg.jpg"     //默认背景图

/********************** show-message **********************/

#define ERROR_MESSAGE @"加载失败"

/********************** custom-localized-language **********************/

//#define CustomLocalizedString(key, comment) \
[[NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"%@",[[NSUserDefaults standardUserDefaults] objectForKey:@"key_language"]] ofType:@"lproj"]] localizedStringForKey:(key) value:@"" table:nil]

/********************** setting **********************/


/********************** custom-notification-name **********************/

#define UPDATE_SCAN_STATE_NOTIFICATION_NAME  @"update_scan"   //更新扫描状态
#define UPDATE_WEBVIEW_NOTIFICATION_NAME  @"update_web"   //更新站点url

#define UPDATE_SCAN_STATE_AFTER_BIND_NOTIFICATION_NAME  @"update_scan_after_bind"   //绑定后更新扫描状态

#define REGISTERTOLOGIN_NOTIFICATION_NAME  @"register_to_login"   //注册去登录

#define BANDTODUMP_NOTIFICATION_NAME  @"bind_to_dump"   //绑定后跳转到web页面
#define REMOTERTODUMP_NOTIFICATION_NAME  @"remoter_to_dump"    //从遥控页面返回点播页面


#define PLAY_STATE_CHANGE_NOTIFICATION_NAME @"play_state_change"
#define PLAY_STATE_OPEN_NOTIFICATION_NAME  @"play_state_open"   //点播状态改变
#define PLAY_STATE_CLOSE_NOTIFICATION_NAME  @"play_state_close"   //点播状态改变

#define DUMP_TO_REMOTER_NOTIFICATION_NAME  @"dump_to_remoter"   //点播成功后跳转至遥控页面

#define QUERY_STATE_CHANGE_NOTIFICATION_NAME @"query_state_change"  //查询状态改变
#define START_QUERY_STATE_NOTIFICATION_NAME @"start_query_state"  //开始循环查询状态


#define SMS_SUCCESS_NOTIFICATION_NAME @"sms_success"  //短信获取验证码成功通知





