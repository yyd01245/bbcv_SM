//
//  JBindModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

/*
 绑定
 */
@interface JBindModel : JBaseModel

@property (nonatomic, copy) NSString *freshToken;       //新生成的校验码
@property (nonatomic, copy) NSString *channelId;        //已绑定的频道id

@end
