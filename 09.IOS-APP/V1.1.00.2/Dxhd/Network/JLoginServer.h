//
//  JLoginServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JLoginModel.h"

@protocol JLoginServerDelegate <JBaseServerDelegate>
- (void)loadLoginModel:(JLoginModel *)model;
- (void)loadLoginFailed:(NSError *)error;
@end

/*
 登录
 */
@interface JLoginServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token;

@end
