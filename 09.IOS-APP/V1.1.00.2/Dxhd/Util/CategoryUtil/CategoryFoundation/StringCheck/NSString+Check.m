//
//  NSString+Check.m
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "NSString+Check.h"
#import "RegexKitLite.h"

@implementation NSString (Check)


// 判断字符串全是数字
- (BOOL)isAllNumber
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[0-9]+$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}

// 判断是否含有符号
- (BOOL)isContainSign
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[A-Za-z0-9\u4e00-\u9fa5]+$";
        return [self isMatchedByRegex:regex]? NO:YES;
    }
    return NO;
}

// 判断密码是否符合要求
- (BOOL)isConform
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[a-z0-9_]+$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}

// 判断是否含有大写字母
- (BOOL)isContainCapital
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^.*[A-Z].*$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// 真实姓名
- (BOOL)isTrueName
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[\\u4e00-\\u9fa5]{2,10}$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// 身份证号码
- (BOOL)isIdentityCardNumber
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^\\d{15}(\\d{3})?$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// 手机号码
- (BOOL)isPhoneNumber
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^(13|15|18){1}[0-9]{9}$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// 邮箱
- (BOOL)isEmail
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^([a-zA-Z0-9%_.+\\-]+)@([a-zA-Z0-9.\\-]+?\\.[a-zA-Z]{2,6})$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// 中国邮政编码
- (BOOL)isPostalCode
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[1-9]\\d{5}(?!\\d)$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}
// QQ号码
- (BOOL)isQQNumber
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^[1-9][0-9]{4,}$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}

// 生日
- (BOOL)isBirthday
{
    if (self && ![self isEqualToString:@""]) {
        NSString *regex = @"^\\d{8}?$";
        return [self isMatchedByRegex:regex];
    }
    return NO;
}

@end
