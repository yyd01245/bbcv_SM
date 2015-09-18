//
//  JMessageHUD.h
//  dphd
//
//  Created by j on 13-12-6.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JMessageHUD : UIView

@property (nonatomic, retain) UILabel  *messageLabel;
@property (nonatomic) float yOffset;

+ (JMessageHUD *)shareInstance;
- (void)initDefaultState;
- (void)loadSubviewWithMessage:(NSString *)message;

- (void)showWithMessage:(NSString *)message;
- (void)showWithMessage:(NSString *)message networkIndicator:(BOOL)show;

- (void)dismiss;

@end
