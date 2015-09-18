//
//  JUserManagerViewController.h
//  Dxhd
//
//  Created by j on 14-10-8.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JUserManagerViewController : UIViewController //<UITableViewDataSource, UITableViewDelegate>

//@property (nonatomic, strong) IBOutlet UITableView *userManagerTableView;

@property (nonatomic, strong) IBOutlet UILabel *usernameLabel;

- (IBAction)actionBack:(id)sender;

- (IBAction)actionChangeUser:(id)sender;
- (IBAction)actionExit:(id)sender;

@end
