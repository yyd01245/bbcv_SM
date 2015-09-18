//
//  JBindServer.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JBindServer.h"

@implementation JBindServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_bind_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username token:(NSString *)token streamId:(NSString *)streamId vodPage:(NSString *)vodPage
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:4];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"stream_id"] = streamId;
    self.postDictionary[@"vod_page"] = vodPage;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JBindModel *mode = [[JBindModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JBindServerDelegate>)self.delegate respondsToSelector:@selector(loadBindModel:)]) {
            [(id<JBindServerDelegate>)self.delegate loadBindModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JBindServerDelegate>)self.delegate respondsToSelector:@selector(loadBindFailed:)]){
        [(id<JBindServerDelegate>)self.delegate loadBindFailed:error];
    }
}

@end
