//
//  JServerContent.h
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import <Foundation/Foundation.h>

/*
 请求的具体内容
 */
@interface JServerContent : NSObject

@property (nonatomic, retain, readonly) NSURL *linkURL;//链接
@property (nonatomic, retain, readonly) NSDictionary *post;//参数

- (id)initWithURL:(NSString *)url postDictonary:(NSDictionary *)post;

@end
