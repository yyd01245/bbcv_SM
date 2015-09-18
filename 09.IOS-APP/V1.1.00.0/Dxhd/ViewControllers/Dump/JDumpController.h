//
//  JDumpController.h
//  Dxhd
//
//  Created by j on 14-9-28.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>

#define JDC ((JDumpController *)[JDumpController shareInstance])

@interface JDumpController : NSObject

//单例
+ (id)shareInstance;

//设置根视图控制器
- (void)setLoginRootViewController:(UIViewController *)rootViewController;

//弹出登录视图控制器
- (void)presentLoginViewControllerFormViewController:(UIViewController *)viewController;
//弹出注册视图控制器
- (void)presentResigterViewControllerFormViewController:(UIViewController *)viewController;
//弹出ip配置页面
- (void)presentIPConfigViewControllerFormViewController:(UIViewController *)viewController;
//弹出菜单视图控制器
//- (void)presentMenuViewControllerFormViewController:(UIViewController *)viewController;
//跳转至关于界面
- (void)presentAboutViewControllerFormViewController:(UIViewController *)viewController;
//跳转至帮助界面
- (void)presentHelpViewControllerFormViewController:(UIViewController *)viewController;
//跳转至用户管理界面
- (void)presentUserManagerViewControllerFormViewController:(UIViewController *)viewController;

@end
