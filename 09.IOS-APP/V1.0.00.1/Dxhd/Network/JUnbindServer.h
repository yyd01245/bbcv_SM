//
//  JUnbindServer.h
//  Dxhd
//
//  Created by j on 14-10-14.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JUnbindMode.h"

@protocol JUnbindServerDelegate <JBaseServerDelegate>
- (void)loadUnbindModel:(JUnbindMode *)model;
- (void)loadUnbindFailed:(NSError *)error;
@end

/*
 解除绑定
 */
@interface JUnbindServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token sequence:(NSString *)sequence;

@end
