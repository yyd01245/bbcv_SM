//
//  JUnbindServer.m
//  Dxhd
//
//  Created by j on 14-10-14.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JUnbindServer.h"

@implementation JUnbindServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_unbind_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username token:(NSString *)token sequence:(NSString *)sequence
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:3];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"sequence"] = sequence;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JUnbindMode *mode = [[JUnbindMode alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JUnbindServerDelegate>)self.delegate respondsToSelector:@selector(loadUnbindModel:)]) {
            [(id<JUnbindServerDelegate>)self.delegate loadUnbindModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JUnbindServerDelegate>)self.delegate respondsToSelector:@selector(loadUnbindFailed:)]){
        [(id<JUnbindServerDelegate>)self.delegate loadUnbindFailed:error];
    }
}

@end
