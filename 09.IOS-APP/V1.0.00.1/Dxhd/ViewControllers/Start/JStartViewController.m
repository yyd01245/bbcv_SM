//
//  JStartViewController.m
//  dphd
//
//  Created by j on 13-12-5.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JStartViewController.h"

@interface JStartViewController ()

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
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -authorize request
- (void)sendAuthorizeRequest
{
    
}


#pragma mark -action click
- (IBAction)actionBind:(id)sender
{
    if ([self.delegate respondsToSelector:@selector(dumpToBindWithViewController:)]) {
        [self.delegate dumpToBindWithViewController:self];
    }
}

- (IBAction)actionIgnore:(id)sender
{
    if ([self.delegate respondsToSelector:@selector(ignoreWithViewController:)]) {
        [self.delegate ignoreWithViewController:self];
    }
}

@end
