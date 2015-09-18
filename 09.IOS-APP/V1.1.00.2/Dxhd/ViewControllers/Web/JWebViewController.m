//
//  JWebViewController.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JWebViewController.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "JDemandServer.h"
#import "JWebResultModel.h"

#define CUSTOM_KEY @"/getcustominfo/"
//#define CURRENT_URL @"http://10.169.8.29:880/bbcvcms/uploads/plus/list.php?tid=9"
#define CURRENT_URL @"http://218.108.50.246/bbcvcms/uploads/plus/list.php?tid=9"

@interface JWebViewController () <JDemandServerDelegate>
{
@private
    BOOL _isClickPlay;
}

//@property (nonatomic, copy) NSString *currentUrl;

// evaluate js
- (void)webViewEvaluatingJavaScript;

//发送点播请求
- (void)sendCodeRequestWithUrl:(NSString *)url;

@end

@implementation JWebViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_WEBVIEW_NOTIFICATION_NAME object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(loginToshowWebView) name:UPDATE_WEBVIEW_NOTIFICATION_NAME object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.frame = [[UIScreen mainScreen] bounds];

    [self webViewEvaluatingJavaScript];
    
    _isClickPlay = NO;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// evaluate js
- (void)webViewEvaluatingJavaScript
{
//    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"index" ofType:@"js"];
//    NSString *jsString = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
//    [self.displayWebView stringByEvaluatingJavaScriptFromString:jsString];
    
    [self.displayWebView stringByEvaluatingJavaScriptFromString:@"function kysxplay(rtsp){window.location = \"/getcustominfo/\"+rtsp;}"];
}

//点播成功后跳转到遥控器操作页面
- (void)dumpToRemoterAfterPlaySuccess
{
    if (JCO.isPlayChannel) {
        [[NSNotificationCenter defaultCenter] postNotificationName:DUMP_TO_REMOTER_NOTIFICATION_NAME object:nil];
    }
}

- (void)loginToshowWebView
{
    //J_LOG(@"登录通知web载入站点首页 url = %@", JCO.siteUrl);
    [self loadRequestWithUrl:JCO.siteUrl];
}

//返回首页
- (void)goFirstPage
{
    //resolution=0&username=null&streamid=null
    NSString *string = [NSString stringWithFormat:@"%@&resolution=%@&username=%@&streamid=%@", CURRENT_URL, JCO.vodR, JCO.username, JCO.streamId];
    [self loadRequestWithUrl:string];
}

//绑定后重定向web页面
- (void)updateWebView
{
    NSString *stringUrl = [JCO.webUrl stringByAppendingString:[NSString stringWithFormat:@"&username=%@", JCO.username]];
    //J_LOG(@"重定向 url = %@", stringUrl);
    [self loadRequestWithUrl:stringUrl];
}

//go back
- (void)webViewGoBack
{
    [self.displayWebView goBack];
    
//    if (!self.displayWebView.canGoBack) {
//        [[JMessageHUD shareInstance] showWithMessage:@"已经是首页！"];
//    }
}

- (void)loadRequestWithUrl:(NSString *)stringUrl
{
    if (stringUrl && ![stringUrl isEqualToString:@""]) {
        NSURL *url = [NSURL URLWithString:stringUrl];
        NSURLRequest *request =[NSURLRequest requestWithURL:url];
        [self.displayWebView loadRequest:request];
    }
}

#pragma mark -
#pragma mark UIWebViewDelegate
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    [self webViewEvaluatingJavaScript];
    //J_LOG(@"request.mainDocumentURL.absoluteURL = %@", request.mainDocumentURL.absoluteURL);
    NSString *relativePath = request.mainDocumentURL.relativePath;
    if([relativePath hasPrefix:CUSTOM_KEY])
    {J_LOG(@"absoluteURL222 = %@", request.mainDocumentURL.absoluteURL);
        //relativePath = [relativePath stringByReplacingOccurrencesOfString:@"%7B" withString:@""];
        NSArray *array = [relativePath componentsSeparatedByString:CUSTOM_KEY];
        if (array.count > 1) {
            NSString *suffix = array[1];J_LOG(@"suffix = %@", suffix);
            [self sendCodeRequestWithUrl:suffix];
        }

        return false;
    }
    
    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];

    [self webViewEvaluatingJavaScript];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}

//发送点播请求
- (void)sendCodeRequestWithUrl:(NSString *)url
{
    if (url && ![url isEqualToString:@""]) {
        //J_LOG(@"url = %@", url);
        if (!_isClickPlay) {
            _isClickPlay = YES;
            
//            JDemandServer *server = [[JDemandServer alloc] initWithDelegate:self];
//            [server loadUsername:JCO.username token:JCO.token url:url];
//            
            //{rstp:rtsp://192.168.100.11:8845/yjy_ipqam/8081/gyc.ts,vodname:关云长,posterurl:images/gyc.jpg}
            
            NSData *data = [url dataUsingEncoding:NSUTF8StringEncoding];
            NSDictionary *dic = [ParserUtil objectFromJsonData:data];
            JWebResultModel *model = [[JWebResultModel alloc] initWithDictionary:dic];
            if (model) {
                JDemandServer *server = [[JDemandServer alloc] initWithDelegate:self];
                [server loadUsername:JCO.username token:JCO.token url:model.rtsp vodName:model.vodName posterUrl:model.posterUrl sequence:SEQUENCE];
                //J_LOG(@"model.rtsp = %@", model.rtsp);
                if (model.vodName) {
                    JCO.vodName = model.vodName;
                }
            }
        /*
             //{"rtsp":"/home/x00/Penglei/ts/ts/clx_new.ts|4914","vodname":"测量学","posterurl":"/bbcvcms/uploads/uploads/jiaoyu/clx/clx.png"}
            //{rstp:rtsp://192.168.100.11:8845/yjy_ipqam/8081/gyc.ts,vodname:关云长,posterurl:images/gyc.jpg}
            url = [url stringByReplacingOccurrencesOfString:@"{" withString:@""];
            url = [url stringByReplacingOccurrencesOfString:@"}" withString:@""];
            
            NSArray *array = [url componentsSeparatedByString:@","];
            if (array.count > 2) {
                NSArray *rtspArr = [array[0] componentsSeparatedByString:@":"];
                NSArray *vodNameArr = [array[1] componentsSeparatedByString:@":"];
                NSArray *posterUrlArr = [array[2] componentsSeparatedByString:@":"];
                NSString *rtsp = rtspArr[1];
                NSString *vodName = vodNameArr[1];
                NSString *posterUrl = posterUrlArr[1];
                
                rtsp = [rtsp stringByReplacingOccurrencesOfString:@"rtsp:" withString:@""];
                vodName = [vodName stringByReplacingOccurrencesOfString:@"vodname:" withString:@""];
                posterUrl = [posterUrl stringByReplacingOccurrencesOfString:@"posterurl:" withString:@""];
                
                if (rtsp && vodName && posterUrl) {
                    JDemandServer *server = [[JDemandServer alloc] initWithDelegate:self];
                    [server loadUsername:JCO.username token:JCO.token url:rtsp vodName:vodName posterUrl:posterUrl sequence:SEQUENCE];
                    
                    if (vodName) {
                        JCO.vodName = vodName;
                    }
                }
            }*/
        }
    }
}

#pragma mark -JDemandServerDelegate
- (void)loadDemandModel:(JDemandModel *)model
{
    if (model && model.return_code == 0) {
        //{"cmd":"user_vodplay_req","message":"点播VOD成功，请跳转到播控界面。。。","new_token":"D1CTROIXRK","return_code":0,"status":"3"}
        JCO.token = model.freshToken;
        
        JCO.playChannel = YES;
        [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_OPEN_NOTIFICATION_NAME object:nil];
        
        [[JMessageHUD shareInstance] showWithMessage:@"点播成功！"];
        
        [self performSelector:@selector(dumpToRemoterAfterPlaySuccess) withObject:nil afterDelay:1.0];
    }
    else {
        if (model.return_code == -5) {
            [[JMessageHUD shareInstance] showWithMessage:@"用户未绑定，请扫描二维码绑定！"];
        }
        else {
            [[JMessageHUD shareInstance] showWithMessage:@"点播失败，请检查点播地址是否有效！"];
        }
        
        JCO.playChannel = NO;
        [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CLOSE_NOTIFICATION_NAME object:nil];
    }
    
    _isClickPlay = NO;
}

- (void)loadDemandFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
    
    JCO.playChannel = NO;
    [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CLOSE_NOTIFICATION_NAME object:nil];
    
    _isClickPlay = NO;
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

@end

