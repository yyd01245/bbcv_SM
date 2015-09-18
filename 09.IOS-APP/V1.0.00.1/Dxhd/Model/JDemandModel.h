//
//  JDemandModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

/*
 点播
 */
@interface JDemandModel : JBaseModel

@property (nonatomic, copy) NSString *message;      //提示信息
@property (nonatomic, copy) NSString *freshToken;     //新的校验码

@end
