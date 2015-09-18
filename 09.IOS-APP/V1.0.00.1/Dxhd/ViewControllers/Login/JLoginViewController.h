//
//  JLoginViewController.h
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, LoginState) {
    FirstLoginState = 0,
    SecondLoginState
};

@protocol JLoginViewControllerDelegate;

@interface JLoginViewController : UIViewController <UITextFieldDelegate>

@property (nonatomic, assign) id<JLoginViewControllerDelegate> loginDelegate;

@property (nonatomic, strong) IBOutlet UIScrollView *loginScrollView;
@property (nonatomic, strong) IBOutlet UITextField *phoneField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;

@property (nonatomic, strong) IBOutlet UIButton *loginButton;
@property (nonatomic, strong) IBOutlet UIButton *registerButton;
@property (nonatomic, strong) IBOutlet UIButton *defaultLoginButton;

- (IBAction)actionLogin:(id)sender;
- (IBAction)actionRegister:(id)sender;
- (IBAction)actionForgetPassword:(id)sender;
- (IBAction)actionDefaultLogin:(id)sender;

@end

@protocol JLoginViewControllerDelegate <NSObject>

- (void)viewDidLogin:(JLoginViewController *)loginViewController;

@end
