//
//  DataCache.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JCacheKeys.h"

/*
 存储工具
 */
@interface DataCache : NSObject

+ (void)writeCache:(id)data forKey:(NSString *)key;
+ (id)readCacheforKey:(NSString *)key;
+ (void)removeCacheForKey:(NSString *)key;

@end
