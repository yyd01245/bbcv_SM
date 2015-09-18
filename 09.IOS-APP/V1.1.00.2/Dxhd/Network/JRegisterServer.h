//
//  JRegisterServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JRegisterModel.h"

@protocol JRegisterServerDelegate <JBaseServerDelegate>
- (void)loadRegisterModel:(JRegisterModel *)model;
- (void)loadRegisterFailed:(NSError *)error;
@end

/*
 注册
 */
@interface JRegisterServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username password:(NSString *)password;

@end
