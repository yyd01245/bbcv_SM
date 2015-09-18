//
//  JMenuView.h
//  Dxhd
//
//  Created by j on 14-10-9.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol JMenuViewDelegate;

@interface JMenuView : UIView <UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, retain) UITableView *menuTableView;
@property (nonatomic, assign) id<JMenuViewDelegate> delegate;

//show menu with animation
- (void)showTableViewMenu;
//hide menu with animation
- (void)hideTableViewMenu;

@end

@protocol JMenuViewDelegate <NSObject>

- (void)menuViewDidSelectIndex:(NSInteger)index;

@end