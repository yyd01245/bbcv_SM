//
//  JServerContent.m
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "JServerContent.h"

@interface JServerContent ()

@property (nonatomic, retain, readwrite) NSURL *linkURL;
@property (nonatomic, retain, readwrite) NSDictionary *post;

@end

@implementation JServerContent
@synthesize linkURL = _linkURL;
@synthesize post = _post;


- (id)initWithURL:(NSString *)url postDictonary:(NSDictionary *)post
{
    self = [super init];
    if (self) {
        self.linkURL = [NSURL URLWithString:url];
        self.post = post;
    }
    return self;
}

@end
