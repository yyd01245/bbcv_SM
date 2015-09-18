//
//  JBindServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JBindModel.h"

@protocol JBindServerDelegate <JBaseServerDelegate>
- (void)loadBindModel:(JBindModel *)model;
- (void)loadBindFailed:(NSError *)error;
@end

/*
 绑定
 */
@interface JBindServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token streamId:(NSString *)streamId vodPage:(NSString *)vodPage;

@end
