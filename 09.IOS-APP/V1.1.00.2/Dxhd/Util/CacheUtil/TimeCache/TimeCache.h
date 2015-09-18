//
//  TimeCache.h
//  linkhealth
//
//  Created by j on 14-8-5.
//  Copyright (c) 2014年 Linkhealth Corporation. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 缓存
 */
@interface TimeCache : NSObject

+ (void)resetCache;
+ (void)setCacheTime:(double)time;

+ (void)setObject:(NSData *)data forKey:(NSString*)key;
+ (id)objectForKey:(NSString *)key;

+ (void)clearAllTimeCache;

@end
