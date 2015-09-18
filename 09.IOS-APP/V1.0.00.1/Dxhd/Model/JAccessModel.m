//
//  JAccessModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JAccessModel.h"

@implementation JAccessModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.serviceAddress = [ParserUtil stringValue:jsonDictionary key:@"service_url"];
        self.token = [ParserUtil stringValue:jsonDictionary key:@"token"];
    }
    return self;
}

@end
