//
//  JKeyModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JKeyModel.h"
#import "ParserUtil.h"

@implementation JKeyModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super init];
    if (self) {
        J_LOG(@"jsonDictionary = %@", jsonDictionary);
        //self.cmdCode = [JParserUtil stringValue:jsonDictionary key:@"cmd"];
        //self.sequence = [JParserUtil stringValue:jsonDictionary key:@"sequence"];
        //self.return_code = [ParserUtil stringValue:jsonDictionary key:@"return_code"];
        self.return_code = [ParserUtil intValue:jsonDictionary key:@"return_code"];
        self.message = [ParserUtil stringValue:jsonDictionary key:@"msg"];
        self.currentTime = [ParserUtil stringValue:jsonDictionary key:@"current_time"];
        self.totalTime = [ParserUtil stringValue:jsonDictionary key:@"total_time"];
        self.status = [ParserUtil stringValue:jsonDictionary key:@"status"];
    }
    return self;
}

@end
