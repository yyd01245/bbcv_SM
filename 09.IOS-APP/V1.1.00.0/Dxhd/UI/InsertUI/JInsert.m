//
//  JInsert.m
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JInsert.h"

#define BACKITEM_WIDTH  60.
#define BACKITEM_HEIGHT 30.

@implementation JInsert
/*
//返回
+ (UIBarButtonItem *)backItemWithTarget:(id)target action:(SEL)action
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setFrame:CGRectMake(0, 0, BACKITEM_WIDTH, BACKITEM_HEIGHT)];
    [button setBackgroundColor:[UIColor clearColor]];
    UIImage *imageNormal = [UIImage imageNamed:@"back_normal"];
    UIImage *imageSelect = [UIImage imageNamed:@"back_selected"];
    [button setImage:imageNormal forState:UIControlStateNormal];
    [button setImage:imageSelect forState:UIControlStateHighlighted];
    [button addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *barBtnItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    return barBtnItem;
}*/

+ (UIBarButtonItem *)backControlWithTarget:(id)target action:(SEL)action
{
    UIView *backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 70., 44.)];
    backView.backgroundColor = [UIColor blackColor];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20., 44.)];
    imageView.image = [UIImage imageNamed:@""];
    imageView.backgroundColor = [UIColor clearColor];
    [backView addSubview:imageView];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setFrame:CGRectMake(0, 0, 28., BACKITEM_HEIGHT)];
    [button setBackgroundColor:[UIColor clearColor]];
    UIImage *imageNormal = [UIImage imageNamed:@"remote_control_title_return"];
    [button setImage:imageNormal forState:UIControlStateNormal];
    [button addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    [backView addSubview:button];
    
    UIBarButtonItem *barBtnItem = [[UIBarButtonItem alloc] initWithCustomView:backView];
    return barBtnItem;
}

//取消
+ (UIBarButtonItem *)canclecontrolWithTarget:(id)target action:(SEL)action
{
    UIView *backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 70., 44.)];
    backView.backgroundColor = [UIColor blackColor];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20., 44.)];
    imageView.image = [UIImage imageNamed:@""];
    imageView.backgroundColor = [UIColor clearColor];
    [backView addSubview:imageView];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setFrame:CGRectMake(0, 0, 28., BACKITEM_HEIGHT)];
    [button setBackgroundColor:[UIColor clearColor]];
    UIImage *imageNormal = [UIImage imageNamed:@"remote_control_title_return"];
    [button setImage:imageNormal forState:UIControlStateNormal];
    [button addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    [backView addSubview:button];
    
    UIBarButtonItem *barBtnItem = [[UIBarButtonItem alloc] initWithCustomView:backView];
    return barBtnItem;
}

@end
