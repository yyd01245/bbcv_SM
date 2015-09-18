//
//  JDirectionView.m
//  kysx
//
//  Created by j on 13-8-22.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JDirectionView.h"

#define IMAGE_DIRECTION_DEFAULT @"remote_control_plate_bg"
#define IMAGE_DIRECTION_UP      @"remote_control_plate_up"
#define IMAGE_DIRECTION_DOWN    @"remote_control_plate_down"
#define IMAGE_DIRECTION_LEFT    @"remote_control_plate_left"
#define IMAGE_DIRECTION_RIGHT   @"remote_control_plate_right"

@implementation JDirectionView
@synthesize directionview = _directionview;
@synthesize delegate = _delegate;
@synthesize direction = _direction;


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
    UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:(IMAGE_DIRECTION_DEFAULT)]];
    imageView.frame = CGRectMake(0, 0, rect.size.width, rect.size.height);
    imageView.backgroundColor = [UIColor clearColor];
    self.directionview = imageView;
    [self addSubview:imageView];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    
    if ((point.x>44. && point.x<176.) && (point.y>10. && point.y<70.)) {
        [self.directionview setImage:[UIImage imageNamed:(IMAGE_DIRECTION_UP)]];
        [self setDirection:JDirectionUp];
    }
    else if ((point.x>44. && point.x<176.) && (point.y>150. && point.y<self.frame.size.height)) {
        [self.directionview setImage:[UIImage imageNamed:(IMAGE_DIRECTION_DOWN)]];
        [self setDirection:JDirectionDown];
    }
    else if ((point.x>10. && point.x<70.) && (point.y>42. && point.y<168.)) {
        [self.directionview setImage:[UIImage imageNamed:(IMAGE_DIRECTION_LEFT)]];
        [self setDirection:JDirectionLeft];
    }
    else if ((point.x>150. && point.x<self.frame.size.width) && (point.y>42. && point.y<168.)) {
        [self.directionview setImage:[UIImage imageNamed:(IMAGE_DIRECTION_RIGHT)]];
        [self setDirection:JDirectionRight];
    }
    else {
        [self setDirection:JDirectionCenter];
    }
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    if (_direction != JDirectionCenter) {
        [self performSelector:@selector(afterHighlighted) withObject:nil afterDelay:0.3];
        
        if ([self.delegate respondsToSelector:@selector(directionView:touchWithDirection:)]) {
            [self.delegate directionView:self touchWithDirection:_direction];
        }
    }
}

- (void)afterHighlighted
{
    [self.directionview setImage:[UIImage imageNamed:(IMAGE_DIRECTION_DEFAULT)]];
}

- (void)reloadView
{
    [self performSelector:@selector(afterHighlighted) withObject:nil];
}

@end
