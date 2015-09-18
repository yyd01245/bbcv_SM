//
//  JProgressView.h
//  Dxhd
//
//  Created by j on 14-10-9.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

#define PROGRESS_WDITH 220.
#define PROGRESS_HEIGHT 12.

@protocol JProgressViewDelegate;

@interface JProgressView : UIView

- (id)initWithFrame:(CGRect)frame withProgress:(float)progress;

@property(nonatomic, retain) UISlider *slider;

@property(nonatomic, retain) UIImageView *progressView;//进度填充部分显示的图像
@property(nonatomic, retain) UIImageView *trackView;//进度未填充部分显示的图像

@property(nonatomic, retain) UILabel *beginLabel;
@property(nonatomic, retain) UILabel *endLabel;
@property(nonatomic, retain) UILabel *progressLabel;

@property(nonatomic, assign) float totalTime;

@property(nonatomic, assign) id<JProgressViewDelegate> delegate;

- (void)setProgressByCurrentTime:(float)currentTime totleTime:(float)totleTime;

- (void)setSliderValue:(float)value;

//- (void)showProgressViewAndHideAfterDuring:(float)during;

@end

@protocol JProgressViewDelegate <NSObject>

- (void)progressViewAfterTouchMove:(JProgressView *)progressView currentTime:(float)currentTime;

@end
