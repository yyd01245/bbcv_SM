//
//  JLoginViewController.m
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JLoginViewController.h"
#import "JDumpController.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "NSString+Check.h"
#import "UIView+WhenTappedBlocks.h"
#import "JAccessServer.h"
#import "JLoginServer.h"

@interface JLoginViewController () <JAccessServerDelegate, JLoginServerDelegate>

- (void)beginTologin;

@end

@implementation JLoginViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:REGISTERTOLOGIN_NOTIFICATION_NAME object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(toLogin) name:REGISTERTOLOGIN_NOTIFICATION_NAME object:nil];
        
        //键盘的监听事件，获取高度
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self.passwordField setSecureTextEntry:YES];
    
    [self.loginScrollView whenTapped:^{
        for (UITextField *textField in self.loginScrollView.subviews) {
            [textField resignFirstResponder];
        }
        [self performSelector:@selector(keyboardWillHide:) withObject:nil];
    }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if (JCO.username && ![JCO.username isEqualToString:@""]) {
        self.phoneField.text = JCO.username;
    }
    if (JCO.password && ![JCO.password isEqualToString:@""]) {
        self.passwordField.text = JCO.password;
    }
}

- (void)toLogin
{
    self.phoneField.text = JCO.username;
    self.passwordField.text = JCO.password;
    
    [self beginTologin];
}

- (void)afterLogin
{
    if ([self.loginDelegate conformsToProtocol:@protocol(JLoginViewControllerDelegate)] && [self.loginDelegate respondsToSelector:@selector(viewDidLogin:)]) {
        [self.loginDelegate viewDidLogin:self];
    }
}

//键盘事件
- (void)keyboardWillShow:(NSNotification *)notification
{
    if (self.loginScrollView.contentOffset.y == 0) {
        [self.loginScrollView setContentSize:CGSizeMake(self.loginScrollView.frame.size.width, self.loginScrollView.contentSize.height+DEFAULT_KEYBOARD_HEIGHT)];
        [self.loginScrollView setContentOffset:CGPointMake(0, 160.) animated:YES];
    }
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    if (self.loginScrollView.contentOffset.y != 0) {
        [self.loginScrollView setContentSize:CGSizeMake(self.loginScrollView.frame.size.width, self.loginScrollView.contentSize.height-DEFAULT_KEYBOARD_HEIGHT)];
    }
}

- (IBAction)actionForgetPassword:(id)sender
{
    [[JMessageHUD shareInstance] showWithMessage:@"此功能暂未提供！"];
}

- (IBAction)actionLogin:(id)sender
{
    if (!self.phoneField.text || [self.phoneField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不能为空！"];
    }
    else if (![self.phoneField.text isPhoneNumber]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不符合！"];
    }
    else if (!self.passwordField.text || [self.passwordField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不能为空！"];
    }
    else if (![self.passwordField.text isConform]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不符合！"];
    }
    else {
        JCO.username = self.phoneField.text;
        JCO.password = self.passwordField.text;
        
        [self beginTologin];
    }
}

- (IBAction)actionRegister:(id)sender
{
    [JDC presentResigterViewControllerFormViewController:self];
}

- (IBAction)actionDefaultLogin:(id)sender
{
    self.phoneField.text = JCO.lastUsername;
    self.passwordField.text = JCO.lastPassword;
}

- (void)beginTologin
{
    JCO.serverUrl = nil;
    JCO.bind = 0;
    JCO.playChannel = 0;
    
    JAccessServer *accessServer = [[JAccessServer alloc] initWithDelegate:self];
    [accessServer loadUsername:JCO.username password:JCO.password appname:APPNAME licence:LICENCE version:@"1.0"];
    
    [[JMessageHUD shareInstance] showWithMessage:@"登录中..."];
}

- (void)loadAccessModel:(JAccessModel *)model
{
    if (model && model.return_code == 0) {
        //{"appname":"","cmd":"user_access_req","licence":"","passwd":"","return_code":0,"service_url":"192.168.100.11:18080","token":"D1CTSJIXQ3","username":"","version":""}
        JCO.serverUrl = model.serviceAddress;
        JCO.token = model.token;

        JLoginServer *server = [[JLoginServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token];
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"接入出错！"];
    }
}

- (void)loadAccessFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
}

- (void)loadLoginModel:(JLoginModel *)model
{
    if (model && model.return_code == 0) {
        //{"channel_id":"","cmd":"user_login_req","message":"用户还未绑定频道...","new_token":"D1CTOK05XK","return_code":0,"status":"1","url":"http://192.168.100.11/multiscreen/mobile/vod.html"}
        if (model.token) {
            JCO.token = model.token;
        }
        if (model.url) {
            JCO.siteUrl = model.url;
        }
        if (model.channelId) {
            JCO.channelNumber = model.channelId;
        }
        [JCO loadLoginUsername:JCO.username password:JCO.password];
        
        [[JMessageHUD shareInstance] showWithMessage:@"登录成功！"];
        [self performSelector:@selector(afterLogin) withObject:nil afterDelay:0.6];
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"登录失败，请检查用户名和密码！"];
    }
}

- (void)loadLoginFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
