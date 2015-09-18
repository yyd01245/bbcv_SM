//
//  JStartViewController.m
//  dphd
//
//  Created by j on 13-12-5.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JStartViewController.h"

@interface JStartViewController ()
{
    NSTimer *_activityTimer;
    NSInteger _animationCount;
}

//- (void)sendAuthorizeRequest;

@end

@implementation JStartViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _animationCount = 3;
    
    _activityTimer = [NSTimer scheduledTimerWithTimeInterval:1. target:self selector:@selector(beginAnimation:) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:_activityTimer forMode:NSDefaultRunLoopMode];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self.activityIndicatorView startAnimating];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    
    [self.activityIndicatorView stopAnimating];
}

- (void)beginAnimation:(NSTimer *)timer
{
    _animationCount -= 1;
    if (_animationCount == 0) {
        [timer invalidate];
        timer = nil;
        
        if ([self.delegate respondsToSelector:@selector(dumpToBindWithViewController:)]) {
            [self.delegate dumpToBindWithViewController:self];
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/*
#pragma mark -authorize request
- (void)sendAuthorizeRequest
{
    
}*/

@end
