//
//  ParserUtil.m
//  linkhealth
//
//  Created by j on 14-8-4.
//  Copyright (c) 2014年 Linkhealth Corporation. All rights reserved.
//

#import "ParserUtil.h"

@implementation ParserUtil

+ (id)object:(NSDictionary *)dictionary key:(NSString *)key
{
	if (![dictionary isKindOfClass:[NSDictionary class]]) {
		return nil;
	}
	id obj = [dictionary objectForKey:key];
	if ([obj isKindOfClass:[NSNull class]]) {
		return nil;
	}
	return obj;
}

+ (int)intValue:(NSDictionary *)dictionary key:(NSString *)key
{
	id obj = [self numberValue:dictionary key:key];
	return [obj intValue];
}

+ (long)longValue:(NSDictionary *)dictionary key:(NSString *)key
{
    id obj = [self numberValue:dictionary key:key];
	return [obj longValue];
}

+ (double)doubleValue:(NSDictionary *)dictionary key:(NSString *)key
{
    id obj = [self numberValue:dictionary key:key];
	return [obj doubleValue];
}

+ (float)floatValue:(NSDictionary *)dictionary key:(NSString *)key
{
	id obj = [self numberValue:dictionary key:key];
	return [obj floatValue];
}

+ (NSNumber *)numberValue:(NSDictionary *)dictionary key:(NSString *)key
{
	id obj = [self object:dictionary key:key];
	if (!obj) {
        return nil;
    }
	
	if ([obj isKindOfClass:[NSNumber class]]) {
		return obj;
	}
    else {
        if ([obj isKindOfClass:[NSString class]]) {
            NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
            NSNumber *num = [formatter numberFromString:(NSString *)obj];
            return num;
        }
    }
    return nil;
}

+ (NSString *)stringValue:(NSDictionary *)dictionary key:(NSString *)key
{
	id obj =  [self object:dictionary key:key];
	if (!obj) {
        return nil;
    }
    
	if ([obj isKindOfClass:[NSString class]]) {
		return obj;
	}
    else  if ([obj isKindOfClass:[NSNumber class]]) {
        return [obj stringValue];
    }
    
    return obj;
}

+ (NSArray *)array:(NSDictionary *)dictionary key:(NSString *)key
{
	id obj =  [self object:dictionary key:key];
	if (!obj || ![obj isKindOfClass:[NSArray class]]) {
		return nil;
	}
	return obj;
}

+ (NSString *)jsonStringFromObject:(id)object
{
    if ([NSJSONSerialization isValidJSONObject:object]) {
        NSError *error = nil;
        NSData *data = [NSJSONSerialization dataWithJSONObject:object options:NSJSONWritingPrettyPrinted error:&error];
        if (error) {
            J_LOG(@"jsonStringFromObject error: %@", error);
        }
        NSString *jsonString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        return jsonString;
    }
    return nil;
}

+ (id)objectFromJsonData:(NSData *)data
{
    NSError *error = nil;
    id object = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    if (error) {
        J_LOG(@"objectFromJsonData error: %@", error);
    }
	return object;
}

@end
