//
//  JAccessServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JAccessModel.h"

@protocol JAccessServerDelegate <JBaseServerDelegate>
- (void)loadAccessModel:(JAccessModel *)model;
- (void)loadAccessFailed:(NSError *)error;
@end

/*
 接入
 */
@interface JAccessServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

- (void)loadUsername:(NSString *)username password:(NSString *)password appname:(NSString *)appname licence:(NSString *)licence version:(NSString *)version;

@end
