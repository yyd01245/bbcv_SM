//
//  JRegisterServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JRegisterServer.h"

@implementation JRegisterServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_regist_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username password:(NSString *)password
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:2];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"passwd"] = password;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JRegisterModel *mode = [[JRegisterModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JRegisterServerDelegate>)self.delegate respondsToSelector:@selector(loadRegisterModel:)]) {
            [(id<JRegisterServerDelegate>)self.delegate loadRegisterModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JRegisterServerDelegate>)self.delegate respondsToSelector:@selector(loadRegisterFailed:)]){
        [(id<JRegisterServerDelegate>)self.delegate loadRegisterFailed:error];
    }
}

@end
