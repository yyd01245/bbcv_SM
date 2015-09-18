//
//  JRequestQueue.h
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 多线程 请求
 */
@interface JRequestQueue : NSOperationQueue

@property (nonatomic, retain, readonly) NSOperationQueue *operationQueue;

+ (JRequestQueue *)shareInstance;

@end
