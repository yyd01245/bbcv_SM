//
//  JLoadingView.m
//  Dxhd
//
//  Created by j on 14-11-20.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JLoadingView.h"

@implementation JLoadingView

- (void)awakeFromNib
{
    self.layer.opacity = 0.5;
}

- (void)loadMessage:(NSString *)message
{
    if (message) {
        self.messageLabel.text = message;
    }
}

@end
