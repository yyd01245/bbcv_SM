//
//  JScanViewController.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JScanBindCell.h"

@interface JScanViewController : UIViewController 

@property (nonatomic, strong) IBOutlet UIView *codePromptView;
@property (nonatomic, strong) IBOutlet UIView *scanView;
@property (nonatomic, strong) IBOutlet UIImageView *clickImageView;
@property (nonatomic, strong) IBOutlet UIButton *clickButton;
@property (nonatomic, strong) IBOutlet UILabel *scanChannelPromptLabel;
@property (nonatomic, retain) JScanBindCell *scanBindCell;

//点击来扫描频道的二维码
- (IBAction)actionClick:(id)sender;

//绑定前
- (void)beforeBind;
//绑定后
- (void)afterBind;

@end
