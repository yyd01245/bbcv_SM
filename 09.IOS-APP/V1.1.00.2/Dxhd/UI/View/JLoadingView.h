//
//  JLoadingView.h
//  Dxhd
//
//  Created by j on 14-11-20.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

#define LOADING_VIEW_HEIGHT_FOR_IPHONE5 519.
#define LOADING_VIEW_HEIGHT_FOR_IPHONE4 421.

@interface JLoadingView : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *messageLabel;

- (void)loadMessage:(NSString *)message;

@end
