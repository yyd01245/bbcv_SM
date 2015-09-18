//
//  JPageToolBar.h
//  Dxhd
//
//  Created by j on 14-9-28.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JRemotePageControl.h"

@protocol JPageToolBarDetegate;

@interface JPageToolBar : UIToolbar

@property (nonatomic, retain) UIImageView *backImageView;
@property (nonatomic, retain) UIButton *menuButton;
@property (nonatomic, retain) UIButton *userButton;
@property (nonatomic, retain) JRemotePageControl *pageControl;
@property (nonatomic, assign) id<JPageToolBarDetegate> toolDelegate;

- (void)updatePage:(NSInteger)page;

- (void)setWebViewBackButtonHidden:(BOOL)hidden;

@end

@protocol JPageToolBarDetegate <NSObject>

- (void)pageToolBarDidMenuTap:(JPageToolBar *)bar;
- (void)pageToolBarDidUserTap:(JPageToolBar *)bar;
- (void)pageToolBarDidPageTap:(JPageToolBar *)bar;

@end