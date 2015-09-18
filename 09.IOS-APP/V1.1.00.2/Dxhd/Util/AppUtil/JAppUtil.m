//
//  JAppUtil.m
//  Dxhd
//
//  Created by j on 14-9-19.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JAppUtil.h"

@implementation JAppUtil

//扫描二维码的返回结果
+ (JQRCodeResult *)resultByScanQRCode:(NSString *)message
{
    if (message && [message rangeOfString:@"vp="].length) {
        JQRCodeResult *result = [[JQRCodeResult alloc] init];
        
        if ([message rangeOfString:@"vp="].length) {
            NSArray *array = [message componentsSeparatedByString:@"?"];
            if (array.count > 1) {
                NSString *content = [array[1] stringByReplacingOccurrencesOfString:@".html" withString:@""];
                J_LOG(@"content = %@", content);//id=1&vp=d1
                NSArray *contentArray = [content componentsSeparatedByString:@"&"];
                if (contentArray.count > 1) {
                    NSString *resultId = contentArray[0];
                    NSString *resultVp = contentArray[1];
                    
                    NSArray *idArray = [resultId componentsSeparatedByString:@"="];
                    if (idArray.count > 1) {
                        result.streamId = idArray[1];
                    }
                    NSArray *vpArray = [resultVp componentsSeparatedByString:@"="];
                    if (vpArray.count > 1) {
                        result.vodPage = vpArray[1];
                    }
                }
            }
        }
        return result;
    }
    return nil;
}

@end
