//
//  JIPConfigViewController.m
//  Dxhd
//
//  Created by j on 14-12-22.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JIPConfigViewController.h"
#import "JMessageHUD.h"
#import "JConfigObject.h"

@interface JIPConfigViewController ()

@end

@implementation JIPConfigViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)actionSure:(id)sender
{
    if (!self.ipField.text || [self.ipField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"请输入ip地址！"];
    }
    else if (!self.portField.text || [self.portField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"请输入端口！"];
    }
    else {
        NSString *hostUrl = [NSString stringWithFormat:@"http://%@:%@", self.ipField.text, self.portField.text];
        [JCO saveHostUrl:hostUrl];
        
        [self dismissViewControllerAnimated:YES completion:^{
            
        }];
    }
}

- (IBAction)actionCancle:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
