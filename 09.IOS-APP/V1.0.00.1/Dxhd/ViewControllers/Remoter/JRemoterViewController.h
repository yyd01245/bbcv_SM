//
//  JRemoterViewController.h
//  kysx
//
//  Created by j on 13-7-22.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JProgressView.h"

typedef NS_ENUM(NSInteger, CenterState) {
    StartState = 0,
    PauseState
};

/*
 遥控器控制
 */
@interface JRemoterViewController : UIViewController <JProgressViewDelegate>

@property (nonatomic, strong) IBOutlet UIView *channelView;
@property (nonatomic, strong) IBOutlet UILabel *channelPromptLabel;
@property (nonatomic, strong) IBOutlet UILabel *channelNumberLabel;

@property (nonatomic, strong) IBOutlet UIImageView *clickBackgroundView;
@property (nonatomic, strong) IBOutlet UIButton *leftButton;
@property (nonatomic, strong) IBOutlet UIButton *upButton;
@property (nonatomic, strong) IBOutlet UIButton *rightButton;
@property (nonatomic, strong) IBOutlet UIButton *downButton;
@property (nonatomic, strong) IBOutlet UIButton *pauseButton;

@property (nonatomic, strong) IBOutlet UIButton *quickBackButton;
@property (nonatomic, strong) IBOutlet UIButton *quickGoButton;
//@property (nonatomic, strong) IBOutlet UIButton *stopButton;

@property (nonatomic, retain) JProgressView *progressView;
@property (nonatomic, assign) CenterState centerState;

//向左
- (IBAction)actionLeft:(id)sender;
//向上
- (IBAction)actionUp:(id)sender;
//向右
- (IBAction)actionRight:(id)sender;
//向下
- (IBAction)actionDown:(id)sender;
//暂停
- (IBAction)actionPause:(id)sender;
//快退
- (IBAction)actionQuikBack:(id)sender;
//快进
- (IBAction)actionQuikGo:(id)sender;
//停止
//- (IBAction)actionStop:(id)sender;
//返回
- (IBAction)actionBack:(id)sender;
//退出
- (IBAction)actionQuit:(id)sender;

@end
