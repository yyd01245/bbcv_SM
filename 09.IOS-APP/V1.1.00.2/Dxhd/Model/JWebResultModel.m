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
        self.rtsp = [ParserUtil stringValue:jsonDictionary key:@"rtsp"];
        J_LOG(@"self.rtsp1 = %@", self.rtsp);
        /*
        NSArray *arr = [self.rtsp componentsSeparatedByString:@"&"];
        NSMutableString *mutableString = [NSMutableString string];
        for (int i = 0; i < arr.count; i++) {
            NSString *str = [arr objectAtIndex:i];
            [mutableString appendString:str];
            if (i != arr.count - 1) {
                [mutableString appendString:@"\\&"];
            }
        }
        self.rtsp = (NSString *)mutableString;
        J_LOG(@"self.rtsp2 = %@", self.rtsp);*/
        self.vodName = [ParserUtil stringValue:jsonDictionary key:@"vodname"];
        self.posterUrl = [ParserUtil stringValue:jsonDictionary key:@"posterurl"];
    }
    return self;
}

@end
