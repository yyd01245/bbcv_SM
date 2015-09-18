//
//  JBaseServer.m
//  health
//
//  Created by j on 13-11-20.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "JBaseServer.h"
#import "JServerContent.h"
#import "JRequestQueue.h"
#import "ASIFormDataRequest.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"


@interface JBaseServer ()
{
    JServerContent *_content;
}

@property (nonatomic, retain) ASIFormDataRequest *httpRequest;

- (NSString *)HTTPBodyWithParameters:(NSDictionary *)parameters;

@end

@implementation JBaseServer
@synthesize linkName = _linkName;
@synthesize postDictionary = _postDictionary;
@synthesize delegate = _delegate;
@synthesize httpRequest = _httpRequest;


- (id)initWithDelegate:(id<JBaseServerDelegate>)delegate
{
	if (self = [super init]) {
		self.delegate = delegate;
        self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:1];
	}
	return self;
}

- (NSString *)HTTPBodyWithParameters:(NSDictionary *)parameters
{
    NSMutableArray *parametersArray = [[NSMutableArray alloc]init];
    
    for (NSString *key in [parameters allKeys]) {
        id value = [parameters objectForKey:key];
        if ([value isKindOfClass:[NSString class]]) {
            [parametersArray addObject:[NSString stringWithFormat:@"%@=%@",key,value]];
        }
    }
    
    return [parametersArray componentsJoinedByString:@"&"];
}

- (JServerContent *)serverContent
{
    if (!_content) {
        NSString *serverUrl;
        if (JCO.serverUrl && ![JCO.serverUrl isEqualToString:@""]) {
            serverUrl = [NSString stringWithFormat:@"http://%@", JCO.serverUrl];
        }
        else {
            if (JCO.hostUrl && ![JCO.hostUrl isEqualToString:@""]) {
                serverUrl = JCO.hostUrl;
            }
            else {
                serverUrl = HOST_URL;
            }
        }
        NSString *_url = [NSString stringWithFormat:@"%@%@", serverUrl, _linkName];
        if (!_postDictionary) {
            self.postDictionary = [NSMutableDictionary dictionaryWithCapacity:1];
        }
        J_LOG(@"url = %@", _url);
        _content = [[JServerContent alloc] initWithURL:_url
                                         postDictonary:_postDictionary];
    }
    return _content;
}

- (void)load
{
    [self loadServerContent:self.serverContent];
}

- (void)loadServerContent:(JServerContent *)content
{
    [[JRequestQueue shareInstance] cancelAllOperations];
    NSInvocationOperation *operation = [[NSInvocationOperation alloc] initWithTarget:self
                                                                            selector:@selector(requestLoad:)
                                                                              object:content];
    [[JRequestQueue shareInstance].operationQueue addOperation:operation];
}

- (void)requestLoad:(JServerContent *)content
{
    __weak ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:content.linkURL];
    //J_LOG(@"request.name = %@", request.name);
    if (request) {
        [request addRequestHeader:@"Content-Type" value:@"application/x-www-form-urlencoded"];
        
        if (content.post.count > 0) {
            NSString *postString = [self HTTPBodyWithParameters:content.post];
            J_LOG(@"postString = %@", postString);
            NSData *postData = [postString dataUsingEncoding:NSUTF8StringEncoding];
            [request setPostBody:(NSMutableData *)postData];
        }
        else {
            NSData *postData = [@"{}" dataUsingEncoding:NSUTF8StringEncoding];
            [request setPostBody:(NSMutableData *)postData];
        }
        
        [request setRequestMethod:@"POST"];
        [request setTimeOutSeconds:10];
        [request setDefaultResponseEncoding:NSUTF8StringEncoding];
        
        [request setCompletionBlock:^{J_LOG(@"requestSuccess %@", request.responseString);
            
            [self didLoadFromServer];
            
            [self loadFinished:request.responseData];
            
        }];
        [request setFailedBlock:^{J_LOG(@"requestFailed %@", request.responseString);
            
            [self didLoadFromServer];
            
            [self loadFailed:request.error];
        }];
        
        [request startAsynchronous];
    }
    else {
        J_LOG(@">>>");
        if ([self.delegate respondsToSelector:@selector(errorWithIp:)]) {
            [self.delegate errorWithIp:self];
        }
        
        [request clearDelegatesAndCancel];
    }
}

- (void)didLoadFromServer
{
    if ([_delegate respondsToSelector:@selector(didLoadFromServer:)]) {
        [_delegate didLoadFromServer:self];
    }
}

@end
