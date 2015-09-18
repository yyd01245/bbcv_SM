//
//  JProgressServer.h
//  Dxhd
//
//  Created by j on 14-10-17.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JProgressModel.h"

@protocol JProgressServerDelegate <JBaseServerDelegate>
- (void)loadProgressModel:(JProgressModel *)model;
- (void)loadProgressFailed:(NSError *)error;
@end

/*
 进度播放
 */
@interface JProgressServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token streamId:(NSString *)streamId beginTime:(NSString *)beginTime sequence:(NSString *)sequence;

@end
