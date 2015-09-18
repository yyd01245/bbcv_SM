//
//  NSString+Check.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (Check)

// 判断字符串全是数字
- (BOOL)isAllNumber;
// 判断是否含有符号
- (BOOL)isContainSign;
// 判断是否含有大写字母
- (BOOL)isContainCapital;
// 判断密码是否符合要求
- (BOOL)isConform;
// 真实姓名
- (BOOL)isTrueName;
// 身份证号码
- (BOOL)isIdentityCardNumber;
// 手机号码
- (BOOL)isPhoneNumber;
// 邮箱
- (BOOL)isEmail;
// 中国邮政编码
- (BOOL)isPostalCode;
// QQ号码
- (BOOL)isQQNumber;
// 生日
- (BOOL)isBirthday;

@end
