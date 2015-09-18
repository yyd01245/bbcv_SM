//
//  JWeiXinQueryServer.m
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JWeiXinQueryServer.h"
#import "ASIFormDataRequest.h"
#import "ParserUtil.h"

@implementation JWeiXinQueryServer

- (id)initWithDelegate:(id<JWeiXinQueryServerDelegate>)delegate
{
    self = [super init];
    if (self) {
        self.delegate = delegate;
    }
    return self;
}

- (void)loadWithUrl:(NSString *)url
{
    if (url) {
        __weak ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:[NSURL URLWithString:url]];
        [request addRequestHeader:@"Content-Type" value:@"application/x-www-form-urlencoded"];
        
        [request setRequestMethod:@"GET"];
        [request setTimeOutSeconds:10];
        [request setDefaultResponseEncoding:NSUTF8StringEncoding];
        
        [request setCompletionBlock:^{J_LOG(@"requestSuccess %@", request.responseString);
            
            NSDictionary *dic = [ParserUtil objectFromJsonData:request.responseData];
            if (dic) {
                JWeixinQueryModel *model = [[JWeixinQueryModel alloc] initWithDictionary:dic];
                
                if ([self.delegate respondsToSelector:@selector(loadWeiXinQueryModel:)]) {
                    [self.delegate loadWeiXinQueryModel:model];
                }
            }
        }];
        [request setFailedBlock:^{J_LOG(@"requestFailed %@", request.responseString);
            
            if ([self.delegate respondsToSelector:@selector(loadWeiXinQueryFailed:)]) {
                [self.delegate loadWeiXinQueryFailed:nil];
            }
        }];
        
        [request startAsynchronous];
    }
}

@end
