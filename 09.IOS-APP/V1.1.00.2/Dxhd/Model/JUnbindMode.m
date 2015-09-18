//
//  JUnbindMode.m
//  Dxhd
//
//  Created by j on 14-10-14.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JUnbindMode.h"

@implementation JUnbindMode

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.freshToken = [ParserUtil stringValue:jsonDictionary key:@"new_token"];
        self.status = [ParserUtil stringValue:jsonDictionary key:@"status"];
    }
    return self;
}

@end
