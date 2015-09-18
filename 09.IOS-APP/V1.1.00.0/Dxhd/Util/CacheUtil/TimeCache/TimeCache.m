//
//  TimeCache.m
//  linkhealth
//
//  Created by j on 14-8-5.
//  Copyright (c) 2014年 Linkhealth Corporation. All rights reserved.
//

#import "TimeCache.h"

static NSTimeInterval cacheTime = (double)604800; //缓存时间 7天

@implementation TimeCache

+ (void)resetCache
{
	[[NSFileManager defaultManager] removeItemAtPath:[TimeCache cacheDirectory] error:nil];
}

+ (void)setCacheTime:(double)time
{
    cacheTime = time;
}

+ (NSString *)cacheDirectory
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
	NSString *cacheDirectory = [paths objectAtIndex:0];
	cacheDirectory = [cacheDirectory stringByAppendingPathComponent:@"TimeCaches"];
	return cacheDirectory;
}

+ (NSData *)objectForKey:(NSString *)key
{
	NSFileManager *fileManager = [NSFileManager defaultManager];
	NSString *filename = [self.cacheDirectory stringByAppendingPathComponent:key];
	
	if ([fileManager fileExistsAtPath:filename])
	{
		NSDate *modificationDate = [[fileManager attributesOfItemAtPath:filename error:nil] objectForKey:NSFileModificationDate];
		if ([modificationDate timeIntervalSinceNow] > cacheTime && cacheTime != 0) {
			[fileManager removeItemAtPath:filename error:nil];
		} else {
			NSData *data = [NSData dataWithContentsOfFile:filename];
			return data;
		}
	}
	return nil;
}

+ (void)setObject:(NSData *)data forKey:(NSString *)key
{
	NSFileManager *fileManager = [NSFileManager defaultManager];
	NSString *filename = [self.cacheDirectory stringByAppendingPathComponent:key];
    
	BOOL isDir = YES;
	if (![fileManager fileExistsAtPath:self.cacheDirectory isDirectory:&isDir]) {
		[fileManager createDirectoryAtPath:self.cacheDirectory withIntermediateDirectories:NO attributes:nil error:nil];
	}
	
	NSError *error;
	@try {
		[data writeToFile:filename options:NSDataWritingAtomic error:&error];
	}
	@catch (NSException *exception) {
		//error
	}
}

+ (void)clearAllTimeCache
{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if ([fileManager fileExistsAtPath:self.cacheDirectory]) {
        NSError *error;
        BOOL success = [fileManager removeItemAtPath:self.cacheDirectory error:&error];
        if (!success && error) {
            //error
        }
    }
}

@end
