//
//  JHelpViewController.h
//  Dxhd
//
//  Created by j on 14-10-8.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JHelpViewController : UIViewController

@property (nonatomic, strong) IBOutlet UIScrollView *helpScrollView;

@property (nonatomic, strong) IBOutlet UILabel *stepFirstLabel;
@property (nonatomic, strong) IBOutlet UILabel *stepSecondLabel;
@property (nonatomic, strong) IBOutlet UILabel *stepThirdLabel;

@property (nonatomic, strong) IBOutlet UILabel *firstLabel;
@property (nonatomic, strong) IBOutlet UILabel *secondLabel;
@property (nonatomic, strong) IBOutlet UILabel *thirdLabel;
@property (nonatomic, strong) IBOutlet UILabel *fourLabel;
@property (nonatomic, strong) IBOutlet UILabel *fiveLabel;
@property (nonatomic, strong) IBOutlet UILabel *sixLabel;

@property (nonatomic, strong) IBOutlet UIImageView *firstImageView;
@property (nonatomic, strong) IBOutlet UIImageView *secondImageView;
@property (nonatomic, strong) IBOutlet UIImageView *thirdImageView;
@property (nonatomic, strong) IBOutlet UIImageView *fourImageView;
@property (nonatomic, strong) IBOutlet UIImageView *fiveImageView;
@property (nonatomic, strong) IBOutlet UIImageView *sixImageView;

- (IBAction)actionBack:(id)sender;

//load subviews
- (void)loadHelpSubviews;

@end
