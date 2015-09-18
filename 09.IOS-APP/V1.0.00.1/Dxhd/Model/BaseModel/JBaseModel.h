//
//  JBaseModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ParserUtil.h"
#import "JConfigObject.h"

/*
 数据解析基类
 */
@interface JBaseModel : NSObject

//@property (nonatomic, copy)     NSString *cmdCode;        //响应指令字
//@property (nonatomic, copy)     NSString *sequence;       //本次通讯唯一编号
@property (nonatomic, assign) NSInteger return_code;    //返回是否成功

- (id)initWithDictionary:(NSDictionary *)jsonDictionary;

@end
