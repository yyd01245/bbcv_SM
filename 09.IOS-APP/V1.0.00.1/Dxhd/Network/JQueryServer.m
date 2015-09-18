//
//  JQueryServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JQueryServer.h"

@implementation JQueryServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_sessionquery_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username token:(NSString *)token
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:2];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JQueryModel *mode = [[JQueryModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JQueryServerDelegate>)self.delegate respondsToSelector:@selector(loadQueryModel:)]) {
            [(id<JQueryServerDelegate>)self.delegate loadQueryModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JQueryServerDelegate>)self.delegate respondsToSelector:@selector(loadQueryFailed:)]){
        [(id<JQueryServerDelegate>)self.delegate loadQueryFailed:error];
    }
}

@end
