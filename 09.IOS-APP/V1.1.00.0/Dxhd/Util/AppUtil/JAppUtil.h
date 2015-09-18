//
//  JAppUtil.h
//  Dxhd
//
//  Created by j on 14-9-19.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JQRCodeResult.h"

@interface JAppUtil : NSObject

//扫描二维码的返回结果
+ (JQRCodeResult *)resultByScanQRCode:(NSString *)message;

@end
