//
//  JUnbindMode.h
//  Dxhd
//
//  Created by j on 14-10-14.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

/*
 解除绑定
 */
@interface JUnbindMode : JBaseModel

@property (nonatomic, copy) NSString *freshToken;       //新生成的校验码
@property (nonatomic, copy) NSString *status;   //1未绑定 2已绑定未点播 3已绑定已点播

@end
