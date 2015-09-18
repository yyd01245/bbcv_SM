//
//  JBaseModel.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBaseModel.h"

@implementation JBaseModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super init];
    if (self) {
        //J_LOG(@"jsonDictionary = %@", jsonDictionary);
        //self.cmdCode = [JParserUtil stringValue:jsonDictionary key:@"cmd"];
        //self.sequence = [JParserUtil stringValue:jsonDictionary key:@"sequence"];
        self.return_code = [ParserUtil intValue:jsonDictionary key:@"return_code"];
    }
    return self;
}

@end
