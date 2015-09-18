//
//  JDemandModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JDemandModel.h"

@implementation JDemandModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.message = [ParserUtil stringValue:jsonDictionary key:@"msg"];
        self.freshToken = [ParserUtil stringValue:jsonDictionary key:@"new_token"];
    }
    return self;
}

@end
