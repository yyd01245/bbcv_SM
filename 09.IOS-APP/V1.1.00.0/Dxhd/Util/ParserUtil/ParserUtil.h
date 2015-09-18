//
//  ParserUtil.h
//  linkhealth
//
//  Created by j on 14-8-4.
//  Copyright (c) 2014年 Linkhealth Corporation. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 json工具
 */
@interface ParserUtil : NSObject

+ (id)object:(NSDictionary *)dictionary key:(NSString *)key;
+ (int)intValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (long)longValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (double)doubleValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (float)floatValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (NSNumber *)numberValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (NSString *)stringValue:(NSDictionary *)dictionary key:(NSString *)key;
+ (NSArray *)array:(NSDictionary *)dictionary key:(NSString *)key;

+ (NSString *)jsonStringFromObject:(id)object;
+ (id)objectFromJsonData:(NSData *)data;

@end
