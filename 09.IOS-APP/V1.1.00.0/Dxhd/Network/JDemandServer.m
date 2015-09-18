//
//  JDemandServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JDemandServer.h"

@implementation JDemandServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_vodplay_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username token:(NSString *)token url:(NSString *)url vodName:(NSString *)vodName posterUrl:(NSString *)posterUrl sequence:(NSString *)sequence
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:6];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"url"] = url;
    self.postDictionary[@"vodname"] = vodName;
    self.postDictionary[@"posterurl"] = posterUrl;
    self.postDictionary[@"sequence"] = sequence;
    
    [self load];
}

/*
- (void)loadUsername:(NSString *)username token:(NSString *)token url:(NSString *)url
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:3];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"url"] = url;
    [self load];
}*/

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JDemandModel *mode = [[JDemandModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JDemandServerDelegate>)self.delegate respondsToSelector:@selector(loadDemandModel:)]) {
            [(id<JDemandServerDelegate>)self.delegate loadDemandModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JDemandServerDelegate>)self.delegate respondsToSelector:@selector(loadDemandFailed:)]){
        [(id<JDemandServerDelegate>)self.delegate loadDemandFailed:error];
    }
}

@end
