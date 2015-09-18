//
//  JBindModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBindModel.h"

@implementation JBindModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.freshToken = [ParserUtil stringValue:jsonDictionary key:@"new_token"];
        self.channelId = [ParserUtil stringValue:jsonDictionary key:@"channel_id"];
    }
    return self;
}

@end
