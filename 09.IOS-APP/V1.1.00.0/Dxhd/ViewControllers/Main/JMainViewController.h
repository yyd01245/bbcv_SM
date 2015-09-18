//
//  JMainViewController.h
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TTSlidingPagesDataSource.h"
#import "TTScrollSlidingPagesController.h"
#import "JPageToolBar.h"
#import "JMenuView.h"

@interface JMainViewController : UIViewController <TTSlidingPagesDataSource, TTSlidingPagesDelegate, JPageToolBarDetegate, JMenuViewDelegate>

@property (nonatomic, retain) TTScrollSlidingPagesController *slidingPagesController;

@property (nonatomic, retain) JPageToolBar *pageToolBar;
@property (nonatomic, retain) JMenuView *menuView;

//跳转至索引页
- (void)scrollToPage:(NSInteger)page;

//show menu
- (void)showMenu;

//hide menu
- (void)hideMenu;

@end
