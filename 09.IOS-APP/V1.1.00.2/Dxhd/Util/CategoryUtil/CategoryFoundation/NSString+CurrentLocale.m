//
//  NSString+CurrentLocale.m
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "NSString+CurrentLocale.h"
//#include <sys/cdefs.h>
#import "sys/sysctl.h"

@implementation NSString (CurrentLocale)

/********************* system *************************/
//设备名称
+ (NSString *)deviceName
{
    return [[UIDevice currentDevice] systemName];
}

//手机系统版本
+ (NSString *)systemVersion
{
    return [[UIDevice currentDevice] systemVersion];
}

//手机型号
+ (NSString *)phoneModel
{
    return [[UIDevice currentDevice] model];
}

//当前所在地
+ (NSString *)currentLocale
{
    NSString *identifier = [[NSLocale currentLocale] localeIdentifier];
    NSString *displayName = [[NSLocale currentLocale] displayNameForKey:NSLocaleIdentifier value:identifier];
    return displayName;
}

/********************* app *************************/
//当前应用名
+ (NSString *)appCurrentName
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *appCurName = [infoDictionary objectForKey:@"CFBundleDisplayName"];
    return appCurName;
}

//当前版本号
+ (NSString *)appCurrentVersion
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *appCurVersion = [infoDictionary objectForKey:@"CFBundleShortVersionString"];
    return appCurVersion;
}

//版本号 (int)
+ (NSString *)appVersionNum
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *appCurVersionNum = [infoDictionary objectForKey:@"CFBundleVersion"];
    return appCurVersionNum;
}

@end
