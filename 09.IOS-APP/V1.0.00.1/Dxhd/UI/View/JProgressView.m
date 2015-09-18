//
//  JProgressView.m
//  Dxhd
//
//  Created by j on 14-10-9.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JProgressView.h"
#import "PublicTools.h"

@implementation JProgressView

- (id)initWithFrame:(CGRect)frame withProgress:(float)progress
{
    self = [super initWithFrame:frame];
    if (self) {
        
        float yOffset = PROGRESS_HEIGHT;
        
        CGSize size = CGSizeMake(PROGRESS_WDITH, PROGRESS_HEIGHT);
         /*
        _trackView = [[UIImageView alloc] initWithFrame:CGRectMake((frame.size.width-size.width)/2, yOffset, size.width, size.height)];
        _trackView.image = [UIImage imageNamed:@"bg_progress"];//进度未填充部分显示的图像
        [self addSubview:_trackView];
        */
        /*
        UIView *progressViewBg = [[UIView alloc] initWithFrame:CGRectMake((frame.size.width-size.width)/2, yOffset, size.width, size.height)];
        progressViewBg.alpha = 0.85f;
        progressViewBg.clipsToBounds = YES;//当前view的主要作用是将出界了的_progressView剪切掉，所以需将clipsToBounds设置为YES
        [self addSubview:progressViewBg];*/
        /*
        _progressView = [[UIImageView alloc] initWithFrame:CGRectMake((frame.size.width-size.width)/2, 0, size.width, size.height)];
        _progressView.image = [UIImage imageNamed:@"bg_progress_sel"];//进度填充部分显示的图像
        //[self addSubview:_progressView];
        [progressViewBg addSubview:_progressView];
        */
        //设置初始进度
        //_progressView.frame = CGRectMake(-_progressView.frame.size.width, 0, _progressView.frame.size.width, _progressView.frame.size.height);
        
        self.slider = [[UISlider alloc] initWithFrame:CGRectMake((frame.size.width-size.width)/2, yOffset, size.width, size.height)];
        [self.slider addTarget:self action:@selector(sliderChange:) forControlEvents:UIControlEventValueChanged];
        [self addSubview:self.slider];
        
        if (!_beginLabel) {
            self.beginLabel = [[UILabel alloc] initWithFrame:CGRectMake(1., yOffset, 42., size.height)];
            self.beginLabel.backgroundColor = [UIColor clearColor];
            self.beginLabel.font = [UIFont systemFontOfSize:14.];
            self.beginLabel.textAlignment = NSTextAlignmentRight;
            self.beginLabel.textColor = [UIColor whiteColor];
            [self addSubview:self.beginLabel];
        }
        
        if (!_endLabel) {
            self.endLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width-44., yOffset, 42., size.height)];
            self.endLabel.backgroundColor = [UIColor clearColor];
            self.endLabel.font = [UIFont systemFontOfSize:14.];
            self.endLabel.textAlignment = NSTextAlignmentLeft;
            self.endLabel.textColor = [UIColor whiteColor];
            [self addSubview:self.endLabel];
        }
        
        if (!_progressLabel) {
            self.progressLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 42., size.height)];
            self.progressLabel.backgroundColor = [UIColor clearColor];
            self.progressLabel.font = [UIFont systemFontOfSize:14.];
            self.progressLabel.textAlignment = NSTextAlignmentCenter;
            self.progressLabel.textColor = [UIColor whiteColor];
            [self addSubview:self.progressLabel];
        }
    }
    return self;
}

- (void)sliderChange:(UISlider *)slider
{
    J_LOG(@"slider.value = %f", slider.value);
    float currentTime = slider.value * self.totalTime;
    [self setProgressByCurrentTime:currentTime totleTime:self.totalTime];
    
    if ([self.delegate respondsToSelector:@selector(progressViewAfterTouchMove:currentTime:)]) {
        [self.delegate progressViewAfterTouchMove:self currentTime:currentTime];
    }
}

- (void)setSliderValue:(float)value
{
//    float currentTime = value * self.totalTime;
//    [self setProgressByCurrentTime:currentTime totleTime:self.totalTime];
}

- (void)setProgressByCurrentTime:(float)currentTime totleTime:(float)totleTime
{
    if (totleTime != 0) {
        self.totalTime = totleTime;
        
        float progress = currentTime/totleTime;
        //_progressView.frame = CGRectMake(_progressView.frame.size.width*progress-_progressView.frame.size.width, 0, _progressView.frame.size.width, _progressView.frame.size.height);//image的宽和高不变，将x轴的坐标根据progress的大小左右移动即可显示出进度的大小，progress的值介于0.0至1.0之间。因为_progressView的父级view上clipsToBounds属性为YES，所以当_progressView的frame出界后不会被显示出来。
        self.beginLabel.text = @"00:00";
        self.endLabel.text = [PublicTools unitWithNumber:totleTime];
        self.progressLabel.text = [PublicTools unitWithNumber:currentTime];
        //移动游标坐标
        self.progressLabel.center = CGPointMake(_trackView.frame.origin.x+_progressView.frame.size.width*progress, self.progressLabel.frame.size.height/2-2);
    }
}

//- (void)showProgressViewAndHideAfterDuring:(float)during;
//{
//    [self performSelector:@selector(removeFromSuperview) withObject:nil afterDelay:during];
//}
/*
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    
    J_LOG(@"x1 = %f, y1 = %f", point.x, point.y);
    float value = point.x / self.slider.frame.size.width;
    [self setSliderValue:value];
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    
    J_LOG(@"x2 = %f, y2 = %f", point.x, point.y);
    float value = point.x / self.slider.frame.size.width;
    [self setSliderValue:value];
    
    float currentTime = value * self.totalTime;
    if ([self.delegate respondsToSelector:@selector(progressViewAfterTouchMove:currentTime:)]) {
        [self.delegate progressViewAfterTouchMove:self currentTime:currentTime];
    }
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    
}*/

@end
