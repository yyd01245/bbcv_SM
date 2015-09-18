//
//  JAccessServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JAccessServer.h"

@implementation JAccessServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_access_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username password:(NSString *)password appname:(NSString *)appname licence:(NSString *)licence version:(NSString *)version
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:5];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"passwd"] = password;
    self.postDictionary[@"appname"] = appname;
    self.postDictionary[@"licence"] = licence;
    self.postDictionary[@"version"] = version;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JAccessModel *mode = [[JAccessModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JAccessServerDelegate>)self.delegate respondsToSelector:@selector(loadAccessModel:)]) {
            [(id<JAccessServerDelegate>)self.delegate loadAccessModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JAccessServerDelegate>)self.delegate respondsToSelector:@selector(loadAccessFailed:)]){
        [(id<JAccessServerDelegate>)self.delegate loadAccessFailed:error];
    }
}

@end
