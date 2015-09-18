//
//  JRemoterViewController.m
//  kysx
//
//  Created by j on 13-7-22.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JRemoterViewController.h"
#import <AudioToolbox/AudioToolbox.h>
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "JKeyServer.h"
#import "JProgressServer.h"
#import "UIFont+Default.h"
#import "JLoadingView.h"

#define LABEL_PLAYING @"本地频道号:"
#define LABEL_NO_PLAY @"未点播频道，请先点播"

@interface JRemoterViewController () <JKeyServerDelegate, JProgressServerDelegate, UIAlertViewDelegate>
{
    //BOOL _isOperate;
    BOOL _isProgress;
    NSInteger _quitCount;
}

@property (nonatomic, retain) JLoadingView *loadingView;

@property (nonatomic, copy) NSString *currentKeyValue;
//load subviews
- (void)loadSubviews;
//vibrate when click key
- (void)startVibrate;
//send key request
- (void)sendKeyRequestWithKeyValue:(NSString *)keyValue;
//send progress request
- (void)sendProgressRequestWithStartTime:(NSString *)startTime;

@end

@implementation JRemoterViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updatePlayState) name:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.frame = [[UIScreen mainScreen] bounds];
    [self.view setUserInteractionEnabled:YES];

    [self loadSubviews];
    
    _isProgress = NO;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//更新点播状态
- (void)updatePlayState
{
    if (JCO.isPlayChannel) {
        if (IPHONE5) {
            self.channelPromptLabel.frame = CGRectMake(24., 8., 171., 39.);
        }
        else if (IPHONE4) {
            self.channelPromptLabel.frame = CGRectMake(25., 3., 171., 30.);
        }
        
        self.channelPromptLabel.text = LABEL_PLAYING;
        self.channelNumberLabel.text = JCO.channelNumber;
        self.channelNumberLabel.hidden = NO;
        self.vodNameLabel.hidden = NO;
        if (JCO.vodName) {
            self.vodNameLabel.text = [NSString stringWithFormat:@"正在点播,%@", JCO.vodName];
        }
        
        [self setCenterState:PauseState];
        /*
        //增加进度条
        if (![self.view.subviews containsObject:self.progressView]) {
            [self.view addSubview:self.progressView];
        }*/
        
        //去掉进度条
        if ([self.view.subviews containsObject:self.progressView]) {
            [self.progressView removeFromSuperview];
        }
    }
    else {
        if (IPHONE5) {
            self.channelPromptLabel.frame = CGRectMake(49., 13., 202., 40.);
        }
        else if (IPHONE4) {
            self.channelPromptLabel.frame = CGRectMake(49., 5., 202., 40.);
        }
        
        self.channelPromptLabel.text = LABEL_NO_PLAY;
        self.channelNumberLabel.hidden = YES;
        self.vodNameLabel.hidden = YES;
        
        [self setCenterState:StartState];
        
        //去掉进度条
        if ([self.view.subviews containsObject:self.progressView]) {
            [self.progressView setTotalTime:0];
            [self.progressView setSliderValue:0];
            
            [self.progressView removeFromSuperview];
        }
    }
}

- (void)setCenterState:(CenterState)centerState
{
    if (_centerState != centerState) {
        _centerState = centerState;
    }
    
    if (_centerState == StartState) {//开始
        [self.pauseButton setImage:[UIImage imageNamed:(@"bt_remoter_start")] forState:UIControlStateNormal];
        [self.pauseButton setImageEdgeInsets:UIEdgeInsetsMake(9., 7., 9., 0)];
    }
    else {//暂停
        [self.pauseButton setImage:[UIImage imageNamed:(@"bt_remoter_pause")] forState:UIControlStateNormal];
        [self.pauseButton setImageEdgeInsets:UIEdgeInsetsMake(5., 0, 5., 0)];
    }
}

//vibrate when click key
- (void)startVibrate
{
    AudioServicesPlaySystemSound (kSystemSoundID_Vibrate);
}

//load subviews
- (void)loadSubviews
{
    self.channelView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@""]];
    self.channelPromptLabel.font = [UIFont defaultGraphBoldFontWithSize:20.];
    [self performSelector:@selector(updatePlayState) withObject:nil];
    
    //方向键
    [self.leftButton setImage:[UIImage imageNamed:(@"bt_remoter_left")] forState:UIControlStateNormal];
    [self.leftButton setImage:[UIImage imageNamed:(@"bt_remoter_left_sel")] forState:UIControlStateHighlighted];
    [self.upButton setImage:[UIImage imageNamed:(@"bt_remoter_up")] forState:UIControlStateNormal];
    [self.upButton setImage:[UIImage imageNamed:(@"bt_remoter_up_sel")] forState:UIControlStateHighlighted];
    [self.rightButton setImage:[UIImage imageNamed:(@"bt_remoter_right")] forState:UIControlStateNormal];
    [self.rightButton setImage:[UIImage imageNamed:(@"bt_remoter_right_sel")] forState:UIControlStateHighlighted];
    [self.downButton setImage:[UIImage imageNamed:(@"bt_remoter_down")] forState:UIControlStateNormal];
    [self.downButton setImage:[UIImage imageNamed:(@"bt_remoter_down_sel")] forState:UIControlStateHighlighted];
    //快进、快退、停止
    [self.quickBackButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_back")] forState:UIControlStateNormal];
    [self.quickBackButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_back_sel")] forState:UIControlStateHighlighted];
    [self.quickGoButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_go")] forState:UIControlStateNormal];
    [self.quickGoButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_go_sel")] forState:UIControlStateHighlighted];
    //[self.stopButton setImage:[UIImage imageNamed:(@"bt_remoter_stop")] forState:UIControlStateNormal];
    //[self.stopButton setImage:[UIImage imageNamed:(@"bt_remoter_stop_sel")] forState:UIControlStateHighlighted];
    
    //进度提示
    if (!_progressView) {
        if (IPHONE5) {
            self.progressView = [[JProgressView alloc] initWithFrame:CGRectMake(0, 475., IOS_WIDTH, PROGRESS_HEIGHT*2) withProgress:0];
        }
        else if (IPHONE4) {
            self.progressView = [[JProgressView alloc] initWithFrame:CGRectMake(0, 400., IOS_WIDTH, PROGRESS_HEIGHT*2) withProgress:0];
        }
        [self.progressView setDelegate:self];
    }
    //...
    //[self.view.window addSubview:self.progressView];
    //[self.progressView setProgressByCurrentTime:0 totleTime:123];
    /*
    if (!_loadingView) {
        if (IPHONE5) {
            self.loadingView = (JLoadingView *)[[[NSBundle mainBundle] loadNibNamed:@"JLoadingView" owner:self options:nil] objectAtIndex:0];
            self.loadingView.frame = CGRectMake(0, 0, IOS_WIDTH, LOADING_VIEW_HEIGHT_FOR_IPHONE5);
        }
        else if (IPHONE4) {
            self.loadingView = (JLoadingView *)[[[NSBundle mainBundle] loadNibNamed:@"JLoadingViewForIphone4" owner:self options:nil] objectAtIndex:0];
            self.loadingView.frame = CGRectMake(0, 0, IOS_WIDTH, LOADING_VIEW_HEIGHT_FOR_IPHONE4);
        }
        
        [self.loadingView loadMessage:@"请求中，请稍后..."];
    }*/
}

#pragma mark -JProgressViewDelegate
- (void)progressViewAfterTouchMove:(JProgressView *)progressView currentTime:(float)currentTime
{
    if (progressView) {
//        if (!JCO.isPlayChannel) {
//            [[JMessageHUD shareInstance] showWithMessage:@"未点播频道，请先点播！"];
//        }

        [self sendProgressRequestWithStartTime:[NSString stringWithFormat:@"%f", currentTime]];
    }
}

#pragma mark -key request
- (void)sendKeyRequestWithKeyValue:(NSString *)keyValue
{
    if ([keyValue isEqualToString:BlcIrrPropertyValue_PAGEUP] || [keyValue isEqualToString:BlcIrrPropertyValue_PAGEDOWN]) {
        self.currentKeyValue = keyValue;
        
        if (JCO.username && JCO.token && keyValue && JCO.isBind) {
            JKeyServer *server = [[JKeyServer alloc] initWithDelegate:self];
            [server loadUsername:JCO.username token:JCO.token sequence:SEQUENCE keyType:KEYTYPE keyValue:keyValue];
            /*
            if (JCO.isPlayChannel) {
                if (![self.view.window.subviews containsObject:self.loadingView]) {
                    [self.view.window addSubview:self.loadingView];
                }
            }*/
            
            [self.progressView setHideProgress:NO];
        }
        else {
            [[JMessageHUD shareInstance] showWithMessage:@"操作失败，请检查用户是否已绑定!"];
        }
    }
    else {
        //if (!_isOperate) {
            //_isOperate = YES;
            
            self.currentKeyValue = keyValue;
            
            if (JCO.username && JCO.token && keyValue && JCO.isBind) {
                JKeyServer *server = [[JKeyServer alloc] initWithDelegate:self];
                [server loadUsername:JCO.username token:JCO.token sequence:SEQUENCE keyType:KEYTYPE keyValue:keyValue];
                /*
                if (JCO.isPlayChannel) {
                    if (![self.view.window.subviews containsObject:self.loadingView]) {
                        [self.view.window addSubview:self.loadingView];
                    }
                }*/
            }
            else {
                if ([keyValue isEqualToString:BlcIrrPropertyValue_EXIT]) {
                    if (_quitCount == 2) {
                        [[JMessageHUD shareInstance] showWithMessage:@"操作失败，请检查用户是否已绑定!"];
                    }
                }
                else {
                    [[JMessageHUD shareInstance] showWithMessage:@"操作失败，请检查用户是否已绑定!"];
                }
            }
        //}
        
        [self.progressView setHideProgress:NO];
    }
}

#pragma mark -JKeyServerDelegate
- (void)loadKeyModel:(JKeyModel *)model
{
    if (model && model.return_code == 0) {
        [[JMessageHUD shareInstance] showWithMessage:@"操作成功！"];
        
        if ([self.currentKeyValue isEqualToString:BlcIrrPropertyValue_EXIT]) {
            //退出
            [self setCenterState:StartState];
            //查询
            //[self sendQueryRequest];
            if (![model.status isEqualToString:@"3"]) {
                JCO.playChannel = NO;
                [self performSelector:@selector(updatePlayState) withObject:nil];
                
                //跳到web页面
                [[NSNotificationCenter defaultCenter] postNotificationName:REMOTERTODUMP_NOTIFICATION_NAME object:nil];
            }
        }
        else if ([self.currentKeyValue isEqualToString:BlcIrrPropertyValue_SELECT]) {
            //开始、暂停
            if (JCO.playChannel) {
                [self setCenterState:!self.centerState];
            }
        }
        else if ([self.currentKeyValue isEqualToString:BlcIrrPropertyValue_PAGEUP] || [self.currentKeyValue isEqualToString:BlcIrrPropertyValue_PAGEDOWN]) {
            //快进、快退
            if ([model.totalTime floatValue] != 0) {
                float para = [model.currentTime floatValue]/[model.totalTime floatValue];
                if (para <= 1 && para >= 0) {
                    [self.progressView setProgressByCurrentTime:[model.currentTime floatValue] totleTime:[model.totalTime floatValue]];
                }
                
                if (JCO.isPlayChannel) {
                    if (![self.view.subviews containsObject:self.progressView]) {
                        [self.view addSubview:self.progressView];
                    }
                    //self.progressView.hidden = NO;
                }
                [self.progressView showProgressViewAndHideAfterDuring:10.];
                
                if (para == 1) {//播放完
                    //查询
                    //[self sendQueryRequest];
                    if ([model.status isEqualToString:@"3"]) {
                        JCO.playChannel = NO;
                        [self performSelector:@selector(updatePlayState) withObject:nil];
                    }
                }
            }
        }
    }
    else  {
        if (model.return_code == -4) {
            [[JMessageHUD shareInstance] showWithMessage:@"未点播频道，请先点播！"];
        }
        else {
            [[JMessageHUD shareInstance] showWithMessage:@"响应失败，请检查频道是否绑定！"];
        }
    }
    
    //_isOperate = NO;
}

- (void)loadKeyFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
    
    //_isOperate = NO;
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server
{/*
    if ([self.view.window.subviews containsObject:self.loadingView]) {
        [self.loadingView removeFromSuperview];
    }*/
}

//send progress request
- (void)sendProgressRequestWithStartTime:(NSString *)startTime
{
    if (JCO.username && JCO.token && JCO.streamId) {
        if (!_isProgress) {J_LOG(@"startTime = %@", startTime);
            JProgressServer *server = [[JProgressServer alloc] initWithDelegate:self];
            [server loadUsername:JCO.username token:JCO.token streamId:JCO.streamId beginTime:startTime sequence:SEQUENCE];
            /*
            if (JCO.isPlayChannel) {
                if (![self.view.window.subviews containsObject:self.loadingView]) {
                    [self.view.window addSubview:self.loadingView];
                }
            }*/
            _isProgress = YES;
            
            [self.progressView setHideProgress:NO];
        }
    }
}

#pragma mark -JProgressServerDelegate
- (void)loadProgressModel:(JProgressModel *)model
{
    if (model && model.return_code == 0) {
        [[JMessageHUD shareInstance] showWithMessage:@"操作成功！"];
        
        
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"响应失败，请检查频道是否绑定！"];
    }
    
    _isProgress = NO;
    
    [self.progressView showProgressViewAndHideAfterDuring:10.];
}

- (void)loadProgressFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
    
    _isProgress = NO;
    
    [self.progressView showProgressViewAndHideAfterDuring:10.];
}

/******************** 按键 ********************/
//向左
- (IBAction)actionLeft:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_ARROWLEFT];
}

//向上
- (IBAction)actionUp:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_ARROWUP];
}

//向右
- (IBAction)actionRight:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_ARROWRIGHT];
}

//向下
- (IBAction)actionDown:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_ARROWDOWN];
}

//暂停
- (IBAction)actionPause:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_SELECT];
}

//快退
- (IBAction)actionQuikBack:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_PAGEDOWN];
    
    [self performSelector:@selector(makeQuickBackButtonHighlighted) withObject:nil];
}

//快进
- (IBAction)actionQuikGo:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_PAGEUP];
    
    [self performSelector:@selector(makeQuickGoButtonHighlighted) withObject:nil];
}
/*
//停止
- (IBAction)actionStop:(id)sender
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_SELECT];
}*/

//返回
- (IBAction)actionBack:(id)sender
{
    //[self startVibrate];
    
    //[self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_BACK];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:REMOTERTODUMP_NOTIFICATION_NAME object:nil];
}

//退出
- (IBAction)actionQuit:(id)sender
{
    if (!JCO.isBind) {
        [[JMessageHUD shareInstance] showWithMessage:@"响应失败，请检查频道是否绑定！"];
    }
    else if (!JCO.isPlayChannel) {
        [[JMessageHUD shareInstance] showWithMessage:@"未点播频道，请先点播！"];
    }
    else {
        _quitCount = 1;
        [self performSelector:@selector(quitAgain) withObject:nil afterDelay:0.1];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"是否退出点播" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
        [alert show];
    }
    
    //{"cmd":"key_send_req","current_time":"","return_code":0,"sequence":"12345678","status":"3","total_time":""}
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [alertView dismissWithClickedButtonIndex:0 animated:YES];
    
    if (buttonIndex == 0) {
        _quitCount = 2;
        [self performSelector:@selector(quitAgain) withObject:nil afterDelay:0.3];
    }
}

//二次退出
- (void)quitAgain
{
    [self startVibrate];
    
    [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_EXIT];
}

//高显
- (void)makeQuickBackButtonHighlighted
{
    [self.quickBackButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_back_sel")] forState:UIControlStateNormal];
    
    [self performSelector:@selector(makeQuickBackButtonNormal) withObject:nil afterDelay:1.];
}

- (void)makeQuickGoButtonHighlighted
{
    [self.quickGoButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_go_sel")] forState:UIControlStateNormal];
    
    [self performSelector:@selector(makeQuickGoButtonNormal) withObject:nil afterDelay:1.];
}

//恢复
- (void)makeQuickBackButtonNormal
{
    [self.quickBackButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_back")] forState:UIControlStateNormal];
}

- (void)makeQuickGoButtonNormal
{
    [self.quickGoButton setImage:[UIImage imageNamed:(@"bt_remoter_quick_go")] forState:UIControlStateNormal];
}

@end
