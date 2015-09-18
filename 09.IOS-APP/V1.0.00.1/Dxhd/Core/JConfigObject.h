//
//  JConfigObject.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DataCache.h"

#define JCO ((JConfigObject *)[JConfigObject shareInstance])

/*
 应用配置
 */
@interface JConfigObject : NSObject

@property (nonatomic, copy) NSString *username;     //用户名
@property (nonatomic, copy) NSString *password;     //密码
@property (nonatomic, copy) NSString *serverUrl;    //切换的服务端地址
@property (nonatomic, copy) NSString *token;        //每次更新的回调参数

//@property (nonatomic, copy) NSString        *stb_id;    //启用的stb_id
@property (nonatomic, copy) NSString      *siteUrl;   //站点主页
@property (nonatomic, copy) NSString      *webUrl;    //绑定后重定向url
@property (nonatomic, copy) NSString      *channelNumber;   //绑定后的频道
@property (nonatomic, copy) NSString      *vodName;     //频道名称
@property (nonatomic, copy) NSString      *streamId;     //
@property (nonatomic, getter = isEnabledControl) BOOL enabledControl;
@property (nonatomic, getter = isBind) BOOL bind;   //是否绑定
@property (nonatomic, getter = isPlayChannel) BOOL playChannel; //是否点播

+ (id)shareInstance;
//应用启动初始化
- (void)initialize;

//是否开启振动
- (void)setVibrate:(BOOL)vibrate;
- (BOOL)isVibrate;

- (void)loadLoginUsername:(NSString *)username password:(NSString *)password;
- (NSString *)lastUsername;
- (NSString *)lastPassword;

@end
