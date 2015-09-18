//
//  JKeyServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JKeyServer.h"

@implementation JKeyServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/key_send_req.do?";
    }
    return self;
}
/*
- (void)loadCmd:(NSString *)cmd sequence:(NSString *)sequence auth_code:(NSString *)auth_code mobile_id:(NSString *)mobile_id stb_id:(NSString *)stb_id key_type:(NSString *)key_type key_value:(NSString *)key_value
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:6];
    self.postDictionary[@"sequence"] = sequence;
    self.postDictionary[@"auth_code"] = auth_code;
    self.postDictionary[@"mobile_id"] = mobile_id;
    self.postDictionary[@"stb_id"] = stb_id;
    self.postDictionary[@"key_type"] = key_type;
    self.postDictionary[@"key_value"] = key_value;
    
    [self load];
}*/


- (void)loadUsername:(NSString *)username token:(NSString *)token sequence:(NSString *)sequence keyType:(NSString *)keyType keyValue:(NSString *)keyValue
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:5];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"sequence"] = sequence;
    self.postDictionary[@"key_type"] = keyType;
    self.postDictionary[@"key_value"] = keyValue;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JKeyModel *mode = [[JKeyModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JKeyServerDelegate>)self.delegate respondsToSelector:@selector(loadKeyModel:)]) {
            [(id<JKeyServerDelegate>)self.delegate loadKeyModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JKeyServerDelegate>)self.delegate respondsToSelector:@selector(loadKeyFailed:)]){
        [(id<JKeyServerDelegate>)self.delegate loadKeyFailed:error];
    }
}

@end
