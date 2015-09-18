//
//  JLoginServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JLoginServer.h"

@implementation JLoginServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_login_req.do?";
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
    JLoginModel *mode = [[JLoginModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JLoginServerDelegate>)self.delegate respondsToSelector:@selector(loadLoginModel:)]) {
            [(id<JLoginServerDelegate>)self.delegate loadLoginModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JLoginServerDelegate>)self.delegate respondsToSelector:@selector(loadLoginFailed:)]){
        [(id<JLoginServerDelegate>)self.delegate loadLoginFailed:error];
    }
}

@end
