//
//  NSString+Connection.m
//  health
//
//  Created by j on 14-1-2.
//  Copyright (c) 2014年 linkhealth. All rights reserved.
//

#import "NSString+Connection.h"
#import <SystemConfiguration/SystemConfiguration.h>

@implementation NSString (Connection)

+ (BOOL)isConnectionAvailable
{
    SCNetworkReachabilityFlags flags;
    BOOL receivedFlags;
    
    SCNetworkReachabilityRef reachability = SCNetworkReachabilityCreateWithName(CFAllocatorGetDefault(), [@"www.baidu.com" UTF8String]);
    receivedFlags = SCNetworkReachabilityGetFlags(reachability, &flags);
    CFRelease(reachability);
    
    if (!receivedFlags || (flags == 0)) {
        
        return FALSE;
    }
    else {
        return TRUE;
    }
}

@end
