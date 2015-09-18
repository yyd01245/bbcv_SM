//
//  JWeiXinQueryServer.h
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JWeixinQueryModel.h"

@protocol JWeiXinQueryServerDelegate <NSObject>
- (void)loadWeiXinQueryModel:(JWeixinQueryModel *)model;
- (void)loadWeiXinQueryFailed:(NSError *)error;
@end

@interface JWeiXinQueryServer : NSObject

@property (nonatomic, assign) id<JWeiXinQueryServerDelegate> delegate;

- (id)initWithDelegate:(id<JWeiXinQueryServerDelegate>)delegate;
- (void)loadWithUrl:(NSString *)url;

@end
