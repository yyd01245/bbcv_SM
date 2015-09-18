//
//  JDirectionView.h
//  kysx
//
//  Created by j on 13-8-22.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, JDirection) {
    JDirectionUp = 0,
    JDirectionDown,
    JDirectionLeft,
    JDirectionRight,
    JDirectionCenter
};

@protocol JDirectionViewDelegate;

@interface JDirectionView : UIView

@property (nonatomic, retain) UIImageView               *directionview;
@property (nonatomic, assign) id<JDirectionViewDelegate> delegate;
@property (nonatomic)         JDirection                 direction;

- (void)reloadView;

@end

@protocol JDirectionViewDelegate <NSObject>

- (void)directionView:(JDirectionView *)directionView touchWithDirection:(JDirection)direction;

@end
