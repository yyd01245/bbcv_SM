//
//  JWebResultModel.m
//  Dxhd
//
//  Created by j on 14-10-20.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JWebResultModel.h"
#import "ParserUtil.h"

@implementation JWebResultModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super init];
    if (self) {
        J_LOG(@"jsonDictionary = %@", jsonDictionary);
        self.rtsp = [ParserUtil stringValue:jsonDictionary key:@"rstp"];
        self.vodName = [ParserUtil stringValue:jsonDictionary key:@"vodname"];
        self.posterUrl = [ParserUtil stringValue:jsonDictionary key:@"posterurl"];
    }
    return self;
}

@end
