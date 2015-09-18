//
//  NSString+CurrentLocale.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (CurrentLocale)

/********************* system *************************/
//设备名称
+ (NSString *)deviceName;
//手机系统版本
+ (NSString *)systemVersion;
//手机型号
+ (NSString *)phoneModel;
//当前所在地
+ (NSString *)currentLocale;

/********************* app *************************/
//当前应用名
+ (NSString *)appCurrentName;
//当前版本号 (1.0.0)
+ (NSString *)appCurrentVersion;
//版本号 (int)
+ (NSString *)appVersionNum;

@end
