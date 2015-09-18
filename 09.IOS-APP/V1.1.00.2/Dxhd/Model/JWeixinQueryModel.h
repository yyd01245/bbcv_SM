//
//  JWeixinQueryModel.h
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JWeixinQueryModel : NSObject

@property (nonatomic, assign) NSInteger retcode;          //
@property (nonatomic, copy) NSString *url;              //

- (id)initWithDictionary:(NSDictionary *)jsonDictionary;

@end
