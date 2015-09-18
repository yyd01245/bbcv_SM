//
//  JBaseServer.h
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ParserUtil.h"
#import "JServerUtil.h"

@class JBaseServer;
@class JServerContent;

@protocol JBaseServerDelegate <NSObject>
@optional
- (void)didLoadFromServer:(JBaseServer *)server;
- (void)errorWithIp:(JBaseServer *)server;

@end


/*
 基类请求
 */
@interface JBaseServer : NSObject

@property (nonatomic, copy)   NSString *linkName;//链接地址
@property (nonatomic, retain) NSMutableDictionary *postDictionary;//传递的参数
@property (nonatomic, assign) id<JBaseServerDelegate> delegate;

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate;
//载入(无参)
- (void)load;
//载入(有参)
- (void)loadServerContent:(JServerContent *)content;

//请求结束后可加入视图显示
- (void)didLoadFromServer;

@end

@interface JBaseServer (JBaseServerCallBack)
- (void)loadFinished:(id)object;
- (void)loadFailed:(NSError *)error;
@end
