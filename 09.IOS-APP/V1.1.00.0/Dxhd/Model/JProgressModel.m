//
//  JProgressModel.m
//  Dxhd
//
//  Created by j on 14-10-17.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JProgressModel.h"

@implementation JProgressModel

- (id)initWithDictionary:(NSDictionary *)jsonDictionary
{
    self = [super initWithDictionary:jsonDictionary];
    if (self) {
        self.status = [ParserUtil stringValue:jsonDictionary key:@"status"];
    }
    return self;
}

@end
