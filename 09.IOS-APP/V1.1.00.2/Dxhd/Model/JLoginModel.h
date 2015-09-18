//
//  JLoginModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

/*
 登录
 */
@interface JLoginModel : JBaseModel

@property (nonatomic, copy) NSString *status;   //1未绑定 2已绑定未点播 3已绑定已点播
@property (nonatomic, copy) NSString *message;  //对应status分别描述提示
@property (nonatomic, copy) NSString *url;      //手机显示的门户地址
@property (nonatomic, copy) NSString *token;    //校验码
@property (nonatomic, copy) NSString *channelId;//已绑定的频道id

@end
