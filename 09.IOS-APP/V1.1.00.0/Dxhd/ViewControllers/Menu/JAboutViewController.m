//
//  JAboutViewController.m
//  Dxhd
//
//  Created by j on 14-9-29.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JAboutViewController.h"

@interface JAboutViewController ()

@end

@implementation JAboutViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)actionBack:(id)sender
{
    //[self.navigationController popViewControllerAnimated:YES];
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

@end
