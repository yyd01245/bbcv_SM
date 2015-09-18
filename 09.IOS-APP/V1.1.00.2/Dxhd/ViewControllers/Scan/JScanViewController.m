//
//  JScanViewController.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JScanViewController.h"
#import "JScanCodeViewController.h"
#import "JQueryServer.h"
#import "JUnbindServer.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "UIFont+Default.h"

#define BEGIN_TO_SCAN @"开始扫描"
#define BIND_SUCCESS @"解除绑定"

@interface JScanViewController () <JQueryServerDelegate, JUnbindServerDelegate>

//send query request
- (void)sendQueryRequest;
//send unbind request
- (void)sendUnbindRequest;
//show bind or not
- (void)showViewByBindOrNot:(BOOL)isBind;
//update state
- (void)updateWithState:(NSString *)state;

@end

@implementation JScanViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_SCAN_STATE_NOTIFICATION_NAME object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UPDATE_SCAN_STATE_AFTER_BIND_NOTIFICATION_NAME object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:QUERY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(loginToUpdateScanState) name:UPDATE_SCAN_STATE_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(afterBindToUpdateScanState) name:UPDATE_SCAN_STATE_AFTER_BIND_NOTIFICATION_NAME object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateCurrentState) name:QUERY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.frame = [[UIScreen mainScreen] bounds];
    self.scanChannelPromptLabel.font = [UIFont defaultGraphBoldFontWithSize:15.];
    if (IOS6ORLATER) {
        //设置标签的行间距
        NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:self.scanChannelPromptLabel.text];
        NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
        [paragraphStyle setLineSpacing:6.0];//调整行间距
        [attributedString addAttribute:NSParagraphStyleAttributeName value:paragraphStyle range:NSMakeRange(0, [self.scanChannelPromptLabel.text length])];
        self.scanChannelPromptLabel.attributedText = attributedString;
    }
    
    if (!_scanBindCell) {
        self.scanBindCell = (JScanBindCell *)[[[NSBundle mainBundle] loadNibNamed:@"JScanBindCell" owner:self options:nil] objectAtIndex:0];
        if (IPHONE5) {
            self.scanBindCell.frame = CGRectMake(0, 340., IOS_WIDTH, SCAN_BIND_CELL_HEIGHT);
        }
        else if (IPHONE4) {
            self.scanBindCell.frame = CGRectMake(0, 327., IOS_WIDTH, SCAN_BIND_CELL_HEIGHT);
        }
    }
    
    [self sendQueryRequest];
    [[NSNotificationCenter defaultCenter] postNotificationName:START_QUERY_STATE_NOTIFICATION_NAME object:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//notice to update state
- (void)updateCurrentState
{
    [self updateWithState:JCO.currentState];
}

//update state
- (void)updateWithState:(NSString *)state
{
    /*
     1未绑定
     2已绑定未点播
     3已绑定已点播
     */
    if ([state isEqualToString:@"1"]) {
        JCO.playChannel = NO;
        JCO.bind = NO;
        [self showViewByBindOrNot:NO];
    }
    else if ([state isEqualToString:@"2"]) {
        JCO.playChannel = NO;
        JCO.bind = YES;
        [self showViewByBindOrNot:YES];
    }
    else if ([state isEqualToString:@"3"]) {
        JCO.playChannel = YES;
        JCO.bind = YES;
        [self showViewByBindOrNot:YES];
    }
}

//after login again update scan state
- (void)loginToUpdateScanState
{
    [self sendQueryRequest];
}

//after bind to update scan state
- (void)afterBindToUpdateScanState
{
    [self afterBind];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:BANDTODUMP_NOTIFICATION_NAME object:nil];
}

//send query request
- (void)sendQueryRequest
{
    if (JCO.username && JCO.token) {
        JQueryServer *server = [[JQueryServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token];
    }
}

#pragma mark -JQueryServerDelegate
- (void)loadQueryModel:(JQueryModel *)model
{
    if (model && model.return_code == 0) {
        //{"cmd":"user_sessionquery_req","message":"用户还未绑定频道...","return_code":0,"status":"1"}
        //{"cmd":"user_sessionquery_req","message":"正在观看VOD内容,绑定本地频道号13","return_code":0,"status":"3"}
        JCO.currentState = model.status;J_LOG(@"JCO.currentState = %@", JCO.currentState);
        [self updateWithState:model.status];
    }
}

- (void)loadQueryFailed:(NSError *)error
{
    
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

//send unbind request
- (void)sendUnbindRequest
{
    if (JCO.username && JCO.token) {
        [[JMessageHUD shareInstance] showWithMessage:@"解除绑定中..."];
        
        JUnbindServer *server = [[JUnbindServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token sequence:SEQUENCE];
    }
}

#pragma mark -JUnbindServerDelegate
- (void)loadUnbindModel:(JUnbindMode *)model
{
    if (model && model.return_code == 0) {
        if (model.freshToken) {
            JCO.token = model.freshToken;
        }
        
        [self updateWithState:model.status];

        [[JMessageHUD shareInstance] showWithMessage:@"解除绑定成功!"];
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"解除绑定失败!"];
    }
}

- (void)loadUnbindFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
}

//show bind or not
- (void)showViewByBindOrNot:(BOOL)isBind
{
    if (isBind) {
        [self afterBind];
    }
    else {
        [self beforeBind];
    }
}

- (void)setupCamera
{
    if (IOS7) {
        JScanCodeViewController *scanCodeViewController = [[JScanCodeViewController alloc] init];
        [self presentViewController:scanCodeViewController animated:YES completion:^{
            [self.clickImageView setHighlighted:NO];
        }];
    }
    else {
        
    }
}

//点击来扫描频道的二维码
- (IBAction)actionClick:(id)sender
{
    [self.clickImageView setHighlighted:YES];
    if (JCO.isBind) {
        if (JCO.isPlayChannel) {
            [[JMessageHUD shareInstance] showWithMessage:@"您正在点播中，请先退出点播的频道!"];
        }
        else {
            [self sendUnbindRequest];
        }
    }
    else {
        [self setupCamera];
    }
}

//绑定前
- (void)beforeBind
{
    if (IPHONE5) {
        self.scanView.frame = CGRectMake(24., 258., 273., 130.);
    }
    else if (IPHONE4) {
        self.scanView.frame = CGRectMake(24., 234., 273., 130.);
    }
    self.codePromptView.hidden = NO;
    self.scanChannelPromptLabel.hidden = NO;
    [self.clickButton setTitle:BEGIN_TO_SCAN forState:UIControlStateNormal];
    [self.clickImageView setHighlighted:NO];
    [self.scanBindCell removeFromSuperview];
}

//绑定后
- (void)afterBind
{
    if (IPHONE5) {
        self.scanView.frame = CGRectMake(24., 202., 273., 130.);
    }
    else if (IPHONE4) {
        self.scanView.frame = CGRectMake(24., 178., 273., 130.);
    }
    self.codePromptView.hidden = YES;
    self.scanChannelPromptLabel.hidden = YES;
    self.scanBindCell.hidden = NO;
    [self.clickButton setTitle:BIND_SUCCESS forState:UIControlStateNormal];
    [self.clickImageView setHighlighted:YES];
    
    if (![self.view.subviews containsObject:self.scanBindCell]) {
        [self.view addSubview:self.scanBindCell];
    }
    [self.scanBindCell loadChannelNumber:JCO.channelNumber];
}

@end
