//
//  JUnderLineLabel.h
//  Dxhd
//
//  Created by j on 14-9-18.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

/*
 下划线点击标签
 */

@interface JUnderLineLabel : UILabel
{
    UIControl *_actionView;
    UIColor *_highlightedColor;
    BOOL _shouldUnderline;
}

@property (nonatomic, retain) UIColor *highlightedColor;
@property (nonatomic, assign) BOOL shouldUnderline;

- (void)setText:(NSString *)text andCenter:(CGPoint)center;
- (void)addTarget:(id)target action:(SEL)action;

@end
