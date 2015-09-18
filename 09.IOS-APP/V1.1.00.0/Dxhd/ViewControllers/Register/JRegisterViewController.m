//
//  JRegisterViewController.m
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JRegisterViewController.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "NSString+Check.h"
#import "UIView+WhenTappedBlocks.h"
#import "JRegisterServer.h"

@interface JRegisterViewController () <JRegisterServerDelegate>

@end

@implementation JRegisterViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
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
    [self.surePasswordField setSecureTextEntry:YES];
    
    [self.registerScrollView whenTapped:^{
        for (UITextField *textField in self.registerScrollView.subviews) {
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

- (void)back:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

- (void)backToLogin:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        [[NSNotificationCenter defaultCenter] postNotificationName:REGISTERTOLOGIN_NOTIFICATION_NAME object:nil];
    }];
}

//键盘事件
- (void)keyboardWillShow:(NSNotification *)notification
{
    if (self.registerScrollView.contentOffset.y == 0) {
        [self.registerScrollView setContentSize:CGSizeMake(self.registerScrollView.frame.size.width, self.registerScrollView.contentSize.height+DEFAULT_KEYBOARD_HEIGHT)];
        [self.registerScrollView setContentOffset:CGPointMake(0, 150.) animated:YES];
    }
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    if (self.registerScrollView.contentOffset.y != 0) {
        [self.registerScrollView setContentSize:CGSizeMake(self.registerScrollView.frame.size.width, self.registerScrollView.contentSize.height-DEFAULT_KEYBOARD_HEIGHT)];
    }
}

//获取验证码
- (IBAction)actionGetCode:(id)sender
{
    if (!self.phoneField.text || [self.phoneField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不能为空！"];
    }
    else if (![self.phoneField.text isPhoneNumber]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不符合！"];
    }
    else {
        J_LOG(@">>>获取验证码");
        [[JMessageHUD shareInstance] showWithMessage:@"此功能暂未提供！"];
    }
}

- (IBAction)actionRegister:(id)sender
{
    if (!self.phoneField.text || [self.phoneField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不能为空！"];
    }
    else if (![self.phoneField.text isPhoneNumber]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不符合！"];
    }
    else if (!self.codeField.text || [self.codeField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"验证码不能为空！"];
    }
    else if (!self.passwordField.text || [self.passwordField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不能为空！"];
    }
    else if (![self.passwordField.text isConform]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不符合！"];
    }
    else if (!self.surePasswordField.text || [self.surePasswordField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"请确认密码！"];
    }
    else if (![self.passwordField.text isEqualToString:self.surePasswordField.text]) {
        [[JMessageHUD shareInstance] showWithMessage:@"两次密码不一致！"];
    }
    else {
        JRegisterServer *server = [[JRegisterServer alloc] initWithDelegate:self];
        [server loadUsername:self.phoneField.text password:self.passwordField.text];
    }
}

- (IBAction)actionCancle:(id)sender
{
    [self performSelector:@selector(back:) withObject:nil];
}

- (void)loadRegisterModel:(JRegisterModel *)model
{
    if (model && model.return_code == 0) {
        JCO.username = self.phoneField.text;
        JCO.password = self.passwordField.text;
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:@"注册成功,是否登录？" delegate:self cancelButtonTitle:@"马上" otherButtonTitles:@"取消", nil];
        [alert show];
    }
    else {
        if (model.return_code == -1007) {
            [[JMessageHUD shareInstance] showWithMessage:@"该手机号已经注册过，请换一个！"];
        }
        else {
            [[JMessageHUD shareInstance] showWithMessage:@"注册失败，请检查信息是否符合！"];
        }
    }
}

- (void)loadRegisterFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:@"连接失败，请检查网络状况！"];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [alertView dismissWithClickedButtonIndex:0 animated:YES];
    
    if (buttonIndex == alertView.cancelButtonIndex) {
        [self performSelector:@selector(backToLogin:) withObject:nil];
    }
    else {
        [self performSelector:@selector(back:) withObject:nil];
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
