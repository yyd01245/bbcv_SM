//
//  JScanCodeViewController.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JScanCodeViewController.h"
#import "JBindServer.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "JAppUtil.h"
#import "JWeiXinQueryServer.h"

@interface JScanCodeViewController () <JBindServerDelegate, JWeiXinQueryServerDelegate>

//绑定请求
- (void)sendBindRequestWithStreamId:(NSString *)streamId vodPage:(NSString *)vodPage;
//开始扫描
- (void)beginScan;
//停止扫描
- (void)endScan;

@end

@implementation JScanCodeViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.view.backgroundColor = [UIColor grayColor];
    
    upOrdown = NO;
    num =0;
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(50, 171, 240, 2)];
    _line.image = [UIImage imageNamed:@"line.png"];
    [self.view addSubview:_line];
    
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation) userInfo:nil repeats:YES];
    
    [self setupCamera];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [self endScan];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)actionCancle:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        [timer invalidate];
    }];
}

- (void)animation
{
    if (upOrdown == NO) {
        num++;
        _line.frame = CGRectMake(50, 171+2*num, 220, 2);
        if (2*num == 200) {
            upOrdown = YES;
        }
    }
    else {
        num--;
        _line.frame = CGRectMake(50, 171+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
}

- (void)setupCamera
{
    // Device
    _device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    // Input
    _input = [AVCaptureDeviceInput deviceInputWithDevice:self.device error:nil];
    
    // Output
    _output = [[AVCaptureMetadataOutput alloc]init];
    [_output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    
    // Session
    _session = [[AVCaptureSession alloc]init];
    [_session setSessionPreset:AVCaptureSessionPresetHigh];
    if ([_session canAddInput:self.input])
    {
        [_session addInput:self.input];
    }
    
    if ([_session canAddOutput:self.output])
    {
        [_session addOutput:self.output];
    }
    
    // 条码类型 AVMetadataObjectTypeQRCode
    _output.metadataObjectTypes = @[AVMetadataObjectTypeQRCode];
    
    // Preview
    _preview =[AVCaptureVideoPreviewLayer layerWithSession:self.session];
    _preview.videoGravity = AVLayerVideoGravityResizeAspectFill;
    //_preview.frame =CGRectMake(20,110,280,280);
    //[self.view.layer insertSublayer:self.preview atIndex:0];
    _preview.frame = CGRectMake(40., 171. ,240., 200.);
    [self.view.layer insertSublayer:self.preview above:self.shapeImageView.layer];
    
    // Start
    [_session startRunning];
}

//开始扫描
- (void)beginScan
{
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation) userInfo:nil repeats:YES];
    [self.view addSubview:_line];
    [_session startRunning];
}

//停止扫描
- (void)endScan
{
    [timer invalidate];
    [_line removeFromSuperview];
    num = 0;
    upOrdown = NO;
}


#pragma mark AVCaptureMetadataOutputObjectsDelegate
- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
    NSString *stringValue;
    
    if ([metadataObjects count] > 0)
    {
        AVMetadataMachineReadableCodeObject * metadataObject = [metadataObjects objectAtIndex:0];
        stringValue = metadataObject.stringValue;
    }
    
    [_session stopRunning];
    [self endScan];
    
    J_LOG(@"stringValue = %@",stringValue);
    if (stringValue) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@""
                                                        message:stringValue
                                                       delegate:self
                                              cancelButtonTitle:@"绑定"
                                              otherButtonTitles:@"取消", nil];
        [alert show];
    }
    else {
        [self beginScan];
    }
}

#pragma mark -UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [alertView dismissWithClickedButtonIndex:0 animated:YES];

    if (buttonIndex == alertView.cancelButtonIndex) {
        if (alertView.message) {
            if ([alertView.message hasPrefix:@"http://weixin.qq.com"]) {
                //http://218.108.50.246/wechat/oneway/urlmapqr?url=http://weixin.qq.com/q/OExObn7k6UOQpjYDyGA5
                NSString *string = @"http://218.108.50.246/wechat/oneway/urlmapqr?url=";
                NSString *stringUrl = [string stringByAppendingString:alertView.message];
                J_LOG(@"stringUrl = %@", stringUrl);
                //[[UIApplication sharedApplication] openURL:[NSURL URLWithString:stringUrl]];
                
                JWeiXinQueryServer *server = [[JWeiXinQueryServer alloc] initWithDelegate:self];
                [server loadWithUrl:stringUrl];
            }
            else {
                //http://192.168.100.11:8181/NS/a?id=1&vp=gyc&r=0
                JCO.webUrl = alertView.message;
                JQRCodeResult *result = [JAppUtil resultByScanQRCode:JCO.webUrl];
                if (result) {
                    [self sendBindRequestWithStreamId:result.streamId vodPage:result.vodPage];
                }
            }
        }
    }
    else {
        //[self beginScan];
    }
}

- (void)loadWeiXinQueryModel:(JWeixinQueryModel *)model
{
    J_LOG(@">>>model.url = %@", model.url);
    if (model.retcode == 0) {
        JCO.webUrl = model.url;J_LOG(@">>>JCO.webUrl = %@", JCO.webUrl);
        JQRCodeResult *result = [JAppUtil resultByScanQRCode:JCO.webUrl];
        if (result) {
            [self sendBindRequestWithStreamId:result.streamId vodPage:result.vodPage];
        }
    }
    else {
        
    }
}

- (void)loadWeiXinQueryFailed:(NSError *)error
{
    
}

//绑定请求
- (void)sendBindRequestWithStreamId:(NSString *)streamId vodPage:(NSString *)vodPage
{J_LOG(@">>>绑定请求");
    if (streamId && vodPage) {
        JCO.streamId = streamId;
        JBindServer *server = [[JBindServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token streamId:streamId vodPage:vodPage];
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"绑定出错，请重新扫描二维码！"];
        [self performSelector:@selector(beginScan) withObject:nil afterDelay:1.0];
    }
}

#pragma mark -JBindServerDelegate
- (void)loadBindModel:(JBindModel *)model
{
    if (model && model.return_code == 0) {
        //{"channel_id":"13","cmd":"user_bind_req","new_token":"D1CTRL7QA8","return_code":0,"status":"2"}
        if (model.freshToken) {
            JCO.token = model.freshToken;
        }
        if (model.channelId) {
            JCO.channelNumber = model.channelId;
        }
        JCO.bind = YES;
        
        [[JMessageHUD shareInstance] showWithMessage:@"绑定成功！"];
        
        [self performSelector:@selector(updateScanState) withObject:nil afterDelay:0.7];
        //[self performSelector:@selector(dumpToWeb) withObject:nil afterDelay:0.8];
    }
    else {
        JCO.bind = NO;
        //根据服务端返回的信息，显示提示
        [[JMessageHUD shareInstance] showWithMessage:@"绑定不成功，请重试一次！"];
        
        [self performSelector:@selector(beginScan) withObject:nil afterDelay:1.0];
    }
}

- (void)loadBindFailed:(NSError *)error
{
    JCO.bind = NO;
    
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
    
    [self performSelector:@selector(beginScan) withObject:nil afterDelay:0.8];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

- (void)updateScanState
{
    [self dismissViewControllerAnimated:NO completion:^{
        [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_SCAN_STATE_AFTER_BIND_NOTIFICATION_NAME object:nil];
    }];
}

@end
