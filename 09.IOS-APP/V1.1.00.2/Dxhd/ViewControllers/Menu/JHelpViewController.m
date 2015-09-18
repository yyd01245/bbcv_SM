//
//  JHelpViewController.m
//  Dxhd
//
//  Created by j on 14-10-8.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JHelpViewController.h"

#define HELP_VIEW_HEIGTH 2800.

@interface JHelpViewController ()

@end

@implementation JHelpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self loadHelpSubviews];
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

//load subviews
- (void)loadHelpSubviews
{
    [self.helpScrollView setContentSize:CGSizeMake(IOS_WIDTH, HELP_VIEW_HEIGTH)];
    
    CGRect rect = self.stepFirstLabel.frame;
    rect.origin.y = 30.;
    self.stepFirstLabel.frame = rect;
    
    rect = self.firstLabel.frame;
    rect.origin.y = 75.;
    self.firstLabel.frame = rect;
    
    rect = self.firstImageView.frame;
    rect.origin.y = 105.;
    self.firstImageView.frame = rect;
    
    rect = self.secondLabel.frame;
    rect.origin.y = 300.;
    self.secondLabel.frame = rect;
    
    rect = self.secondImageView.frame;
    rect.origin.y = 330.;
    self.secondImageView.frame = rect;
    
    rect = self.thirdLabel.frame;
    rect.origin.y = 843.;
    self.thirdLabel.frame = rect;
    
    rect = self.thirdImageView.frame;
    rect.origin.y = 877.;
    self.thirdImageView.frame = rect;
    
    rect = self.stepSecondLabel.frame;
    rect.origin.y = 1090.;
    self.stepSecondLabel.frame = rect;
    
    rect = self.fourLabel.frame;
    rect.origin.y = 1125.;
    self.fourLabel.frame = rect;
    
    rect = self.fourImageView.frame;
    rect.origin.y = 1175.;
    self.fourImageView.frame = rect;
    
    rect = self.fiveLabel.frame;
    rect.origin.y = 1690.;
    self.fiveLabel.frame = rect;
    
    rect = self.fiveImageView.frame;
    rect.origin.y = 1740.;
    self.fiveImageView.frame = rect;
    
    rect = self.stepThirdLabel.frame;
    rect.origin.y = 2256.;
    self.stepThirdLabel.frame = rect;
    
    rect = self.sixLabel.frame;
    rect.origin.y = 2300.;
    self.sixLabel.frame = rect;
    
    rect = self.sixImageView.frame;
    rect.origin.y = 2330.;
    self.sixImageView.frame = rect;
}

@end
