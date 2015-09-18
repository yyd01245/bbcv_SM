//
//  JKeyModel.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
//#import "JBaseModel.h"

@interface JKeyModel : NSObject

//@property (nonatomic, copy) NSString *return_code;    //返回是否成功
@property (nonatomic, assign) NSInteger return_code;    //返回是否成功
@property (nonatomic, copy) NSString *message;        //返回信息
@property (nonatomic, copy) NSString *currentTime;  //当前播放时间
@property (nonatomic, copy) NSString *totalTime;  //播放总时长
@property (nonatomic, copy) NSString *status;  //当前状态

- (id)initWithDictionary:(NSDictionary *)jsonDictionary;

@end
