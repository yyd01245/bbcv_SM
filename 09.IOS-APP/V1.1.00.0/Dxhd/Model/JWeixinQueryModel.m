//
//  JWeixinQueryModel.m
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JWeixinQueryModel.h"
#import "ParserUtil.h"

@implementation JWeixinQueryModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super init];
    if (self) {
        J_LOG(@"jsonDictionary = %@", jsonDictionary);
        self.retcode = [ParserUtil intValue:jsonDictionary key:@"retcode"];
        self.url = [ParserUtil stringValue:jsonDictionary key:@"url"];
    }
    return self;
}

@end
