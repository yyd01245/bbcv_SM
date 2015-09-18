//
//  JConfigObject.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JConfigObject.h"

@implementation JConfigObject

+ (id)shareInstance
{
    static JConfigObject *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[JConfigObject alloc] init];
    });
    return instance;
}

//应用启动初始化
- (void)initialize
{
    
}

//是否开启振动
- (void)setVibrate:(BOOL)vibrate
{
    [DataCache writeCache:[NSString stringWithFormat:@"%d", vibrate] forKey:KEY_VIBRATION];
}

- (BOOL)isVibrate
{
    if ([DataCache readCacheforKey:KEY_VIBRATION]) {
        return [[DataCache readCacheforKey:KEY_VIBRATION] boolValue];
    }
    return YES;
}

- (void)loadLoginUsername:(NSString *)username password:(NSString *)password
{
    [DataCache writeCache:username forKey:KEY_USERNAME];
    [DataCache writeCache:password forKey:KEY_PASSWORD];
    
    BOOL ret = [self haveSaveLogin:username];
    if (!ret) {
        [JLSB insertUserInfo:username];
    }
}

- (NSString *)lastUsername
{
    if ([DataCache readCacheforKey:KEY_USERNAME]) {
        return [DataCache readCacheforKey:KEY_USERNAME];
    }
    return @"13812345678";
}

- (NSString *)lastPassword
{
    if ([DataCache readCacheforKey:KEY_PASSWORD]) {
        return [DataCache readCacheforKey:KEY_PASSWORD];
    }
    return @"123456";
}

/***********************************************************/
//是否保存过登录信息
- (BOOL)haveSaveLogin:(NSString *)username
{
    if (username) {
        NSArray *array = [JLSB getAllUserInfoData];
        if (array) {
            if ([array containsObject:username]) {
                return YES;
            }
        }
    }
    return NO;
}

//获取登录列表
- (NSArray *)getAllUserName
{
    return [JLSB getAllUserInfoData];
}
/***********************************************************/

//可配置的服务端地址
- (NSString *)hostUrl
{
    if ([DataCache readCacheforKey:KEY_HOST_URL]) {
        return [DataCache readCacheforKey:KEY_HOST_URL];
    }
    return @"";
}

- (void)saveHostUrl:(NSString *)hostUrl
{
    [DataCache writeCache:hostUrl forKey:KEY_HOST_URL];
}

@end
