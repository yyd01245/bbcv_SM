//
//  JLoginModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JLoginModel.h"

@implementation JLoginModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.status = [ParserUtil stringValue:jsonDictionary key:@"status"];
        self.message = [ParserUtil stringValue:jsonDictionary key:@"message"];
        self.url = [ParserUtil stringValue:jsonDictionary key:@"url"];
        self.token = [ParserUtil stringValue:jsonDictionary key:@"new_token"];
        self.channelId = [ParserUtil stringValue:jsonDictionary key:@"channel_id"];
    }
    return self;
}

@end
