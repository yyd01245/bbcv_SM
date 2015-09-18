//
//  JRequestQueue.m
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "JRequestQueue.h"

#define KDATA_OPERATION_COUNT 10

@interface JRequestQueue ()
@property (nonatomic, retain, readwrite) NSOperationQueue *operationQueue;
@end

@implementation JRequestQueue
@synthesize operationQueue = _operationQueue;


+ (JRequestQueue *)shareInstance
{
    static JRequestQueue *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[JRequestQueue alloc] init];
        instance.operationQueue = [[NSOperationQueue alloc] init];
        [instance.operationQueue setMaxConcurrentOperationCount:KDATA_OPERATION_COUNT];
    });
    return instance;
}

@end
