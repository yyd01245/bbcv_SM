//
//  JRegisterViewController.h
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JUnderLineLabel.h"

@interface JRegisterViewController : UIViewController <UITextFieldDelegate, UIAlertViewDelegate, UIAlertViewDelegate>

@property (nonatomic, strong) IBOutlet UIScrollView *registerScrollView;
@property (nonatomic, strong) IBOutlet UITextField *phoneField;
@property (nonatomic, strong) IBOutlet UITextField *codeField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;
@property (nonatomic, strong) IBOutlet UITextField *surePasswordField;

- (IBAction)actionRegister:(id)sender;
- (IBAction)actionCancle:(id)sender;
//获取验证码
- (IBAction)actionGetCode:(id)sender;

@end
