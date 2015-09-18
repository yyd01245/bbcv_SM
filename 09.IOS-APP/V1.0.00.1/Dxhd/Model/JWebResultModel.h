//
//  JWebResultModel.h
//  Dxhd
//
//  Created by j on 14-10-20.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JWebResultModel : NSObject

@property (nonatomic, copy) NSString *rtsp;         //
@property (nonatomic, copy) NSString *vodName;      //
@property (nonatomic, copy) NSString *posterUrl;    //

- (id)initWithDictionary:(NSDictionary *)jsonDictionary;

@end
