//
//  JIPConfigViewController.h
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JIPConfigViewController : UIViewController <UITextFieldDelegate>

@property (nonatomic, strong) IBOutlet UITextField *ipField;
@property (nonatomic, strong) IBOutlet UITextField *portField;

- (IBAction)actionSure:(id)sender;
- (IBAction)actionCancle:(id)sender;

@end
