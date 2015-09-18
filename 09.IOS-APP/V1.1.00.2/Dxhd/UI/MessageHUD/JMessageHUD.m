//
//  JMessageHUD.m
//  dphd
//
//  Created by j on 13-12-6.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JMessageHUD.h"
#import <QuartzCore/QuartzCore.h>

@interface JMessageHUD (Private)

//添加子视图
- (void)add;
//移除子视图
- (void)remove;

@end

@implementation JMessageHUD
@synthesize messageLabel = _messageLabel;
@synthesize yOffset = _yOffset;

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidHideNotification object:nil];
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillAppear:) name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillDisappear:) name:UIKeyboardDidHideNotification object:nil];
    }
    return self;
}

// 键盘弹出
- (void)keyboardWillAppear:(NSNotification *)notification
{
	NSDictionary *userInfo = [notification userInfo];
	NSValue *value = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
	CGRect keyRect = [value CGRectValue];
	CGRect windowRect = [UIApplication sharedApplication].keyWindow.bounds;
    
    self.yOffset = windowRect.size.height - keyRect.size.height;
}

// 键盘消失
- (void)keyboardWillDisappear:(NSNotification *)notification
{
    self.yOffset = 0;
}

- (void)loadSubviewWithMessage:(NSString *)message
{
    [self remove];
    
    UILabel *_label = [[UILabel alloc] initWithFrame:CGRectZero];
    _label.font = [UIFont boldSystemFontOfSize:16.];
    _label.textAlignment = NSTextAlignmentCenter;
    _label.textColor = [UIColor whiteColor];
    _label.backgroundColor = [UIColor blackColor];
    //_label.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.75f];
    _label.layer.cornerRadius = 5.0f;
    _label.numberOfLines = 0;
    _label.text = message;
    
    //...自定义实现方法
    CGSize size = [message sizeWithFont:[UIFont boldSystemFontOfSize:16.]
                      constrainedToSize:CGSizeMake(280., CGFLOAT_MAX)
                          lineBreakMode:NSLineBreakByCharWrapping];
    CGRect frame = _label.frame;
    frame.size.width = size.width + 20.0f;
    frame.size.height = size.height + 10.0f;
    frame.origin.x = (IOS_WIDTH - frame.size.width) / 2;
    
    /*
    [_label sizeToFit];
    
    CGRect frame = _label.frame;
    frame.size.width = frame.size.width + 20.0f;
    frame.size.height = frame.size.height + 10.0f;
    frame.origin.x = (IOS_WIDTH - frame.size.width) / 2;
    */
    if (_yOffset != 0) {
        frame.origin.y = _yOffset - 110.;
    }
    else {
        frame.origin.y = (IOS_HEIGHT - frame.size.height) / 2;
    }
    
    _label.frame = frame;
    self.messageLabel = _label;
    
    [self add];
    
    [self performSelector:@selector(dismiss) withObject:nil afterDelay:1.0];
}

- (void)initDefaultState
{
    self.yOffset = 0;
}

+ (JMessageHUD *)shareInstance
{
    static JMessageHUD *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[JMessageHUD alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    });
    return instance;
}

- (void)showWithMessage:(NSString *)message
{
    [self loadSubviewWithMessage:message];
}

- (void)showWithMessage:(NSString *)message networkIndicator:(BOOL)show
{
    if(show) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible = show;
    }
    
    [self showWithMessage:message];
}

- (void)add
{
    [self addSubview:self.messageLabel];
    [[UIApplication sharedApplication].keyWindow addSubview:self];
}

- (void)remove
{
    [self.messageLabel removeFromSuperview];
    [self removeFromSuperview];
}

- (void)dismiss
{
	[UIView animateWithDuration:0.15
						  delay:0
						options:UIViewAnimationCurveEaseIn | UIViewAnimationOptionAllowUserInteraction
					 animations:^{
						 [self remove];
					 }
					 completion:^(BOOL finished){
                         
                         if ([UIApplication sharedApplication].networkActivityIndicatorVisible) {
                             [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                         }
                     }];
}

@end
