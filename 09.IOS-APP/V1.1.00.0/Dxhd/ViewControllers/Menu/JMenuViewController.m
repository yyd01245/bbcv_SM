//
//  JMenuViewController.m
//  Dxhd
//
//  Created by j on 14-9-28.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JMenuViewController.h"
#import "JDumpController.h"
#import "UIView+WhenTappedBlocks.h"

#define TABLEVIEW_MENU_WIDTH 100.

@interface JMenuViewController ()

@property (nonatomic, retain) NSArray *menuArray;
@property (nonatomic, retain) NSArray *imageArray;

//show menu with animation
- (void)showTableViewMenu;
//hide menu with animation
- (void)hideTableViewMenu;

@end

@implementation JMenuViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.navigationController.navigationBarHidden = YES;
    self.view.alpha = 0.1;
    self.view.opaque = NO;
    
    [self.view whenTapped:^{
        [self performSelector:@selector(dismissWhenTap:) withObject:nil];
    }];
    
    if (!_menuTableView) {
        self.menuTableView = [[UITableView alloc] initWithFrame:CGRectMake(-TABLEVIEW_MENU_WIDTH, DEFAULT_START_HEIGHT+DEFAULT_NAV_HEIGHT, TABLEVIEW_MENU_WIDTH, IOS_HEIGHT-DEFAULT_START_HEIGHT-DEFAULT_NAV_HEIGHT) style:UITableViewStylePlain];
        self.menuTableView.backgroundColor = [UIColor greenColor];
        self.menuTableView.delegate = self;
        self.menuTableView.dataSource = self;
        self.menuTableView.showsVerticalScrollIndicator = NO;
    }
    
    if (!_menuArray) {
        self.menuArray = [NSArray arrayWithObjects:@"用户", @"关于", nil];
    }
    
    if (!_imageArray) {
        self.imageArray = [NSArray arrayWithObjects:@"", @"", nil];
    }
    
    [self.menuTableView reloadData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self showTableViewMenu];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//dismiss view after tap other
- (void)dismissWhenTap:(id)sender
{
    [self hideTableViewMenu];
}

//show menu with animation
- (void)showTableViewMenu
{
    [self.view addSubview:self.menuTableView];
    [UIView animateWithDuration:CUSTOM_ANIMATION*3 animations:^{
        self.menuTableView.frame = CGRectMake(0, self.menuTableView.frame.origin.y, self.menuTableView.frame.size.width, self.menuTableView.frame.size.height);
    }completion:^(BOOL finished){
        
    }];
}

//hide menu with animation
- (void)hideTableViewMenu
{
    [UIView animateWithDuration:CUSTOM_ANIMATION*2 animations:^{
        self.menuTableView.frame = CGRectMake(-TABLEVIEW_MENU_WIDTH, self.menuTableView.frame.origin.y, self.menuTableView.frame.size.width, self.menuTableView.frame.size.height);
    }completion:^(BOOL finished){
        [self dismissViewControllerAnimated:NO completion:^{
            [self.menuTableView removeFromSuperview];
        }];
    }];
}

#pragma mark -UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.menuArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"kCell";
    
    UITableViewCell *cell = (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.backgroundColor = [UIColor clearColor];
        cell.textLabel.textColor = [UIColor whiteColor];
    }
    
    if (indexPath.row < self.menuArray.count) {
        cell.textLabel.text = self.menuArray[indexPath.row];
    }
    
    if (indexPath.row < self.imageArray.count) {
        //cell.imageView.image = self.imageArray[indexPath.row];
    }
    
    return cell;
}

#pragma mark -UITableViewDelegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return DEFAULT_ROW_HEIGHT;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    J_LOG(@"indexPath.row = %d", indexPath.row);
    if (indexPath.row < self.menuArray.count) {
        if (indexPath.row == 0) {
            
        }
        else if (indexPath.row == 1) {
            [JDC presentAboutViewControllerFormViewController:self];
        }
    }
}

@end
