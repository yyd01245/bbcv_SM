//
//  JQueryModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JQueryModel.h"

@implementation JQueryModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.status = [ParserUtil stringValue:jsonDictionary key:@"status"];
        self.message = [ParserUtil stringValue:jsonDictionary key:@"message"];
    }
    return self;
}

@end
