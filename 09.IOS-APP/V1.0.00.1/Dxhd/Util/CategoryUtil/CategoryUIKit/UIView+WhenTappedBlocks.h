//
//  UIView+WhenTappedBlocks.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#if NS_BLOCKS_AVAILABLE

#import <UIKit/UIKit.h>

typedef void (^JMWhenTappedBlock)();

/*
 可在 UIView 中为指定的区域处理触摸操作
 */
@interface UIView (JMWhenTappedBlocks) <UIGestureRecognizerDelegate>

- (void)whenTapped:(JMWhenTappedBlock)block;
- (void)whenDoubleTapped:(JMWhenTappedBlock)block;
- (void)whenTwoFingerTapped:(JMWhenTappedBlock)block;
- (void)whenTouchedDown:(JMWhenTappedBlock)block;
- (void)whenTouchedUp:(JMWhenTappedBlock)block;

@end

#endif
