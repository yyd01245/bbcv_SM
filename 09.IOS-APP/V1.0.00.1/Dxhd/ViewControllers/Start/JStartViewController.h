//
//  JStartViewController.h
//  dphd
//
//  Created by j on 13-12-5.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol JStartViewControllerDelegate;

@interface JStartViewController : UIViewController

@property (nonatomic, assign) id<JStartViewControllerDelegate> delegate;

- (IBAction)actionBind:(id)sender;
- (IBAction)actionIgnore:(id)sender;

@end

@protocol JStartViewControllerDelegate <NSObject>

- (void)ignoreWithViewController:(JStartViewController *)viewController;
- (void)dumpToBindWithViewController:(JStartViewController *)viewController;

@end