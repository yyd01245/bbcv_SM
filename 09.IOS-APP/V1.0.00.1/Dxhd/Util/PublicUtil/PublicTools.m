//
//  PublicTools.m
//  linkhealth
//
//  Created by j on 14-7-31.
//  Copyright (c) 2014年 Linkhealth Corporation. All rights reserved.
//

#import "PublicTools.h"

@implementation PublicTools



//s--小时、分
+ (NSString *)unitWithNumber:(NSInteger)number
{
    NSInteger h = number/60/60;
    NSInteger m = number/60%60;
    NSInteger s = number%60;
    
    if (h > 0) {
        return [NSString stringWithFormat:@"%ld:%ld", h, m];
    }
    else if (h == 0 && m > 0) {
        return [NSString stringWithFormat:@"%ld:%ld", m, s];
    }
    else {
        return [NSString stringWithFormat:@"00:%ld", s];
    }
}

@end
