//
//  JWebViewController.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JWebViewController : UIViewController <UIWebViewDelegate>

@property (nonatomic, strong) IBOutlet UIWebView *displayWebView;

//返回首页
- (void)goFirstPage;
//绑定后重定向web页面
- (void)updateWebView;
//go back
- (void)webViewGoBack;

@end
