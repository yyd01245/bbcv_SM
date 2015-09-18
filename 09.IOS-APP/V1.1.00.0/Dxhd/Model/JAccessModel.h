//
//  JAccessModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

/*
 接入
 */
@interface JAccessModel : JBaseModel

@property (nonatomic, copy) NSString *serviceAddress;   //接入地址
@property (nonatomic, copy) NSString *token;            //校验码

@end
