//
//  JProgressServer.m
//  Dxhd
//
//  Created by j on 14-10-17.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JProgressServer.h"

@implementation JProgressServer

- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
    self = [super initWithDelegate:delegate];
    if (self) {
        self.linkName = @"/msi/user_choosetime_req.do?";
    }
    return self;
}

- (void)loadUsername:(NSString *)username token:(NSString *)token streamId:(NSString *)streamId beginTime:(NSString *)beginTime sequence:(NSString *)sequence
{
    self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:5];
    self.postDictionary[@"username"] = username;
    self.postDictionary[@"token"] = token;
    self.postDictionary[@"stream_id"] = streamId;
    self.postDictionary[@"begintime"] = beginTime;
    self.postDictionary[@"sequence"] = sequence;
    
    [self load];
}

- (void)loadFinished:(id)object
{
    NSDictionary *dic = [ParserUtil objectFromJsonData:object];
    JProgressModel *mode = [[JProgressModel alloc] initWithDictionary:dic];
    if (mode) {
        if ([(id<JProgressServerDelegate>)self.delegate respondsToSelector:@selector(loadProgressModel:)]) {
            [(id<JProgressServerDelegate>)self.delegate loadProgressModel:mode];
        }
    }
}

- (void)loadFailed:(NSError *)error
{
    if ([(id<JProgressServerDelegate>)self.delegate respondsToSelector:@selector(loadProgressFailed:)]){
        [(id<JProgressServerDelegate>)self.delegate loadProgressFailed:error];
    }
}

@end
