//
//  JPageToolBar.m
//  Dxhd
//
//  Created by j on 14-9-28.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JPageToolBar.h"

@implementation JPageToolBar

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self setNeedsDisplay];
    }
    return self;
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
    //bar_tool
    if (!_backImageView) {
        self.backImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, rect.size.width, rect.size.height)];
        self.backImageView.backgroundColor = [UIColor clearColor];
        self.backImageView.image = [UIImage imageNamed:@"bar_tool"];
        [self addSubview:self.backImageView];
    }
    
    //bt
    if (!_menuButton) {
        self.menuButton = [UIButton buttonWithType:UIButtonTypeCustom];
        //[self.menuButton setFrame:CGRectMake(16., 5., 35., 34.)];
        [self.menuButton setFrame:CGRectMake(4., 1., 67., 42.)];
        [self.menuButton setImage:[UIImage imageNamed:@"bt_menu"] forState:UIControlStateNormal];
        [self.menuButton setImage:[UIImage imageNamed:@"bt_menu_sel"] forState:UIControlStateHighlighted];
        [self.menuButton setImageEdgeInsets:UIEdgeInsetsMake(2., 12., 2., 20.)];
        [self.menuButton addTarget:self action:@selector(actionClickMenu:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.menuButton];
    }
    
    if (!_userButton) {
        self.userButton = [UIButton buttonWithType:UIButtonTypeCustom];
        //[self.userButton setFrame:CGRectMake(264., 5., 35., 34.)];
        [self.userButton setFrame:CGRectMake(252., 1., 67., 42.)];
        [self.userButton setImage:[UIImage imageNamed:@"bt_wb_back"] forState:UIControlStateNormal];
        [self.userButton setImageEdgeInsets:UIEdgeInsetsMake(4., 18., 4, 20.)];
        [self.userButton addTarget:self action:@selector(actionClickUser:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.userButton];
        
        [self.userButton setHidden:YES];
    }
    
    if (!_pageControl) {
        self.pageControl = [[JRemotePageControl alloc] initWithFrame:CGRectMake(0, 0, 100., 36.)];
        [self.pageControl setCenter:CGPointMake(rect.size.width/2, rect.size.height/2)];
        [self.pageControl setNumberOfPages:3];
        [self.pageControl setCurrentPage:0];
        [self.pageControl setDefersCurrentPageDisplay: YES];
        [self.pageControl setType: JPageControlTypeOnFullOffFull];
        [self.pageControl setOnColor: [UIColor yellowColor]];
        [self.pageControl setOffColor: [UIColor whiteColor]];
        [self.pageControl setIndicatorDiameter: 13.0f];
        [self.pageControl setIndicatorSpace: 20.0f];
        [self addSubview:self.pageControl];
        [self.pageControl addTarget:self action:@selector(pageDidChange:) forControlEvents:UIControlEventValueChanged];
    }
}

- (void)updatePage:(NSInteger)page
{J_LOG(@"page = %d", page);
    [self.pageControl setCurrentPage:page];
    
    if (page == 1) {
        [self setWebViewBackButtonHidden:NO];
    }
    else {
        [self setWebViewBackButtonHidden:YES];
    }
}

- (void)setWebViewBackButtonHidden:(BOOL)hidden
{
    self.userButton.hidden = hidden;
}

#pragma mark -menu
- (void)actionClickMenu:(UIButton *)button
{
    if ([self.toolDelegate conformsToProtocol:@protocol(JPageToolBarDetegate)] && [self.toolDelegate respondsToSelector:@selector(pageToolBarDidMenuTap:)]) {
        [self.toolDelegate pageToolBarDidMenuTap:self];
    }
}

#pragma mark -user
- (void)actionClickUser:(UIButton *)button
{
    if ([self.toolDelegate conformsToProtocol:@protocol(JPageToolBarDetegate)] && [self.toolDelegate respondsToSelector:@selector(pageToolBarDidUserTap:)]) {
        [self.toolDelegate pageToolBarDidUserTap:self];
    }
}

#pragma mark -page control
- (void)pageDidChange:(id)sender
{
    if ([self.toolDelegate conformsToProtocol:@protocol(JPageToolBarDetegate)] && [self.toolDelegate respondsToSelector:@selector(pageToolBarDidPageTap:)]) {
        [self.toolDelegate pageToolBarDidPageTap:self];
    }
}

@end
