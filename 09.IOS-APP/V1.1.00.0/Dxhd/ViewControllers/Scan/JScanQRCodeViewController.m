//
//  JScanQRCodeViewController.m
//  Dxhd
//
//  Created by j on 14-9-25.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JScanQRCodeViewController.h"
//#import "JBindServer.h"
//#import "JConfigObject.h"
//#import "JMessageHUD.h"

@interface JScanQRCodeViewController () //<JBindServerDelegate>

//处理二维码解析的url
//- (void)dealWithStringUrl:(NSString *)stringUrl;
//绑定请求
//- (void)sendBindRequestWithStreamId:(NSString *)streamId vodPage:(NSString *)vodPage;

//- (void)beginScan;
//- (void)endScan;

@end

@implementation JScanQRCodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
/*
- (void)beginScan
{
    num = 0;
    upOrdown = NO;
    //初始话ZBar
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    //reader.view.frame = CGRectMake(0, 0, 320, 320);
    //设置代理
    reader.readerDelegate = self;
    //支持界面旋转
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    reader.showsHelpOnFail = NO;
    reader.scanCrop = CGRectMake(0.1, 0.2, 0.8, 0.8);//扫描的感应框
    ZBarImageScanner * scanner = reader.scanner;
    [scanner setSymbology:ZBAR_I25
                   config:ZBAR_CFG_ENABLE
                       to:0];
    UIView * view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 420)];
    view.backgroundColor = [UIColor clearColor];
    reader.cameraOverlayView = view;
    
    
    UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, 280, 40)];
    label.text = @"请将扫描的二维码至于下面的框内\n！";
    label.textColor = [UIColor whiteColor];
    label.textAlignment = 1;
    label.lineBreakMode = 0;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
    [view addSubview:label];
    
    UIImageView * image = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"pick_bg.png"]];
    image.frame = CGRectMake(20, 80, 280, 280);
    [view addSubview:image];
    
    
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, 220, 2)];
    _line.image = [UIImage imageNamed:@"line.png"];
    [image addSubview:_line];
    
    //定时器，设定时间过1.5秒，
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    [self presentViewController:reader animated:YES completion:^{
        
    }];
}

- (void)animation1
{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (2*num == 260) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
}

- (void)endScan
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [self endScan];
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
    }];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [self endScan];
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
        UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
        //初始化
        ZBarReaderController *read = [ZBarReaderController new];
        //设置代理
        read.readerDelegate = self;
        CGImageRef cgImageRef = image.CGImage;
        ZBarSymbol *symbol = nil;
        id <NSFastEnumeration> results = [read scanImage:cgImageRef];
        for (symbol in results) {
            break;
        }
        NSString *result;
        if ([symbol.data canBeConvertedToEncoding:NSShiftJISStringEncoding]) {
            result = [NSString stringWithCString:[symbol.data cStringUsingEncoding: NSShiftJISStringEncoding] encoding:NSUTF8StringEncoding];
        }
        else {
            result = symbol.data;
        }
        
        if (result) {
            [self dealWithStringUrl:result];
        }
        NSLog(@"result = %@", result);
    }];
}*/
/*
//处理二维码解析的url
- (void)dealWithStringUrl:(NSString *)stringUrl
{
    if (stringUrl) {
        //http://192.168.100.11:8181/AppProject/a?id=1&vp=d1.html?name=gyc
        //[self sendBindRequestWithBindType:@"2" bindId:_xmlMode.stbId];
        
        NSString *streamId = nil;
        NSString *vodPage = nil;
        
        if ([stringUrl rangeOfString:@"vp="].length) {
            NSArray *array = [stringUrl componentsSeparatedByString:@"?"];
            if (array.count > 1) {
                NSString *content = [array[1] stringByReplacingOccurrencesOfString:@".html" withString:@""];
                J_LOG(@"content = %@", content);//id=1&vp=d1
                NSArray *contentArray = [content componentsSeparatedByString:@"&"];
                if (contentArray.count > 1) {
                    NSString *resultId = contentArray[0];
                    NSString *resultVp = contentArray[1];
                    
                    NSArray *idArray = [resultId componentsSeparatedByString:@"="];
                    if (idArray.count > 1) {
                        streamId = idArray[1];
                    }
                    NSArray *vpArray = [resultVp componentsSeparatedByString:@"="];
                    if (vpArray.count > 1) {
                        vodPage = vpArray[1];
                    }
                }
            }
        }
        
        J_LOG(@"streamId = %@", streamId);
        J_LOG(@"vodPage = %@", vodPage);
        
        if (streamId && vodPage) {
            [self sendBindRequestWithStreamId:streamId vodPage:vodPage];
        }
    }
}

//绑定请求
- (void)sendBindRequestWithStreamId:(NSString *)streamId vodPage:(NSString *)vodPage
{
    JBindServer *server = [[JBindServer alloc] initWithDelegate:self];
    [server loadUsername:JCO.username token:JCO.token streamId:streamId vodPage:vodPage];
}

#pragma mark -JBindServerDelegate
- (void)loadBindModel:(JBindModel *)model
{
    if (model && model.return_code == 0) {
        
        if (model.freshToken) {
            JCO.token = model.freshToken;
        }
        
        [[JMessageHUD shareInstance] showWithMessage:@"绑定成功！"];
        
        [self performSelector:@selector(dumpToWeb) withObject:nil afterDelay:0.8];
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"绑定不成功，请重试一次！"];
    }
}

- (void)loadBindFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
    
    [self beginScan];
}*/
/*
#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}


- (void)dumpToWeb
{
    [[NSNotificationCenter defaultCenter] postNotificationName:BANDTODUMP_NOTIFICATION_NAME object:nil];
}*/

@end
