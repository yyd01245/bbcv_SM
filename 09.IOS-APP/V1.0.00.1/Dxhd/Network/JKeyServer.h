//
//  JKeyServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JKeyModel.h"

@protocol JKeyServerDelegate <JBaseServerDelegate>
- (void)loadKeyModel:(JKeyModel *)model;
- (void)loadKeyFailed:(NSError *)error;
@end

/*
 键值
 */
@interface JKeyServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token sequence:(NSString *)sequence keyType:(NSString *)keyType keyValue:(NSString *)keyValue;

//- (void)loadCmd:(NSString *)cmd sequence:(NSString *)sequence auth_code:(NSString *)auth_code mobile_id:(NSString *)mobile_id stb_id:(NSString *)stb_id key_type:(NSString *)key_type key_value:(NSString *)key_value;

@end
