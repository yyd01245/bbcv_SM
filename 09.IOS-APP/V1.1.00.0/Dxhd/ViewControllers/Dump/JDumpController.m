//
//  JDumpController.m
//  Dxhd
//
//  Created by j on 14-9-28.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JDumpController.h"
#import "JLoginViewController.h"
#import "JRegisterViewController.h"
#import "JIPConfigViewController.h"
//#import "JMenuViewController.h"
#import "JAboutViewController.h"
#import "JHelpViewController.h"
#import "JUserManagerViewController.h"

@implementation JDumpController

//单例
+ (id)shareInstance
{
    static JDumpController *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[JDumpController alloc] init];
    });
    return instance;
}

//设置根视图控制器
- (void)setLoginRootViewController:(UIViewController *)rootViewController
{
    
}

//弹出登录视图控制器
- (void)presentLoginViewControllerFormViewController:(UIViewController *)viewController
{
    if (viewController) {
        if (IPHONE5) {
            JLoginViewController *loginViewController = [[JLoginViewController alloc] initWithNibName:@"JLoginViewController" bundle:[NSBundle mainBundle]];
            loginViewController.loginDelegate = (id<JLoginViewControllerDelegate>)viewController;
            [viewController presentViewController:loginViewController animated:YES completion:^{
                
            }];
        }
        else if (IPHONE4) {
            JLoginViewController *loginViewController = [[JLoginViewController alloc] initWithNibName:@"JLoginViewControllerForIphone4" bundle:[NSBundle mainBundle]];
            loginViewController.loginDelegate = (id<JLoginViewControllerDelegate>)viewController;
            [viewController presentViewController:loginViewController animated:YES completion:^{
                
            }];
        }
    }
}

//弹出注册视图控制器
- (void)presentResigterViewControllerFormViewController:(UIViewController *)viewController
{
    if (viewController) {
        if (IPHONE5) {
            JRegisterViewController *registerViewController = [[JRegisterViewController alloc] initWithNibName:@"JRegisterViewController" bundle:[NSBundle mainBundle]];
            [viewController presentViewController:registerViewController animated:YES completion:^{
                
            }];
        }
        else if (IPHONE4) {
            JRegisterViewController *registerViewController = [[JRegisterViewController alloc] initWithNibName:@"JRegisterViewControllerForIphone4" bundle:[NSBundle mainBundle]];
            [viewController presentViewController:registerViewController animated:YES completion:^{
                
            }];
        }
    }
}

//弹出ip配置页面
- (void)presentIPConfigViewControllerFormViewController:(UIViewController *)viewController
{
    if (viewController) {
        if (IPHONE5) {
            JIPConfigViewController *configViewController = [[JIPConfigViewController alloc] initWithNibName:@"JIPConfigViewController" bundle:[NSBundle mainBundle]];
            [viewController presentViewController:configViewController animated:YES completion:^{
                
            }];
        }
        else if (IPHONE4) {
            JIPConfigViewController *configViewController = [[JIPConfigViewController alloc] initWithNibName:@"JIPConfigViewControllerForIphone4" bundle:[NSBundle mainBundle]];
            [viewController presentViewController:configViewController animated:YES completion:^{
                
            }];
        }
    }
}

/*
//弹出菜单视图控制器
- (void)presentMenuViewControllerFormViewController:(UIViewController *)viewController
{
    if (viewController) {
        JMenuViewController *menuViewController = [[JMenuViewController alloc] initWithNibName:@"JMenuViewController" bundle:[NSBundle mainBundle]];
        UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:menuViewController];
        [viewController presentViewController:nav animated:NO completion:^{
            
        }];
    }
}*/

//跳转至关于界面
- (void)presentAboutViewControllerFormViewController:(UIViewController *)viewController
{
    if (IPHONE5) {
        JAboutViewController *aboutViewController = [[JAboutViewController alloc] initWithNibName:@"JAboutViewController" bundle:[NSBundle mainBundle]];
        [viewController presentViewController:aboutViewController animated:YES completion:^{
            
        }];
    }
    else {
        JAboutViewController *aboutViewController = [[JAboutViewController alloc] initWithNibName:@"JAboutViewControllerForIphone4" bundle:[NSBundle mainBundle]];
        [viewController presentViewController:aboutViewController animated:YES completion:^{
            
        }];
    }
}

//跳转至帮助界面
- (void)presentHelpViewControllerFormViewController:(UIViewController *)viewController
{
    JHelpViewController *helpViewController = [[JHelpViewController alloc] initWithNibName:@"JHelpViewController" bundle:[NSBundle mainBundle]];
    [viewController presentViewController:helpViewController animated:YES completion:^{
        
    }];
}

//跳转至用户管理界面
- (void)presentUserManagerViewControllerFormViewController:(UIViewController *)viewController
{
    JUserManagerViewController *userManagerViewController = [[JUserManagerViewController alloc] initWithNibName:@"JUserManagerViewController" bundle:[NSBundle mainBundle]];
    [viewController presentViewController:userManagerViewController animated:YES completion:^{
        
    }];
}

@end
