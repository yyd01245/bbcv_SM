//
//  JQueryServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JQueryModel.h"

@protocol JQueryServerDelegate <JBaseServerDelegate>
- (void)loadQueryModel:(JQueryModel *)model;
- (void)loadQueryFailed:(NSError *)error;
@end

/*
 查询
 */
@interface JQueryServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username token:(NSString *)token;

@end
