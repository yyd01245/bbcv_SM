//
//  UIFont+Default.m
//  health
//
//  Created by j on 14-2-28.
//  Copyright (c) 2014年 linkhealth. All rights reserved.
//

#import "UIFont+Default.h"

@implementation UIFont (Default)

+ (UIFont *)defaultGraphBoldFontWithSize:(float)size
{
    //NSString *name = [[NSBundle mainBundle] pathForResource:@"RTWSYueGoTrial-Light" ofType:@"otf"];//RTWSYueGoTrial-Light 悦黑常体
    //return [UIFont fontWithName:@"yhct" size:size];
    return [UIFont fontWithName:@"RTWSYueGoTrial-Light" size:size];
}

@end
