//
//  JDemandServer.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseServer.h"
#import "JDemandModel.h"

@protocol JDemandServerDelegate <JBaseServerDelegate>
- (void)loadDemandModel:(JDemandModel *)model;
- (void)loadDemandFailed:(NSError *)error;
@end

/*
 点播
 */
@interface JDemandServer : JBaseServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;

//- (void)loadUsername:(NSString *)username token:(NSString *)token url:(NSString *)url;
- (void)loadUsername:(NSString *)username token:(NSString *)token url:(NSString *)url vodName:(NSString *)vodName posterUrl:(NSString *)posterUrl sequence:(NSString *)sequence;

@end
