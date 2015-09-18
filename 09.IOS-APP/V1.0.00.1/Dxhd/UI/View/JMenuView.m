//
//  JMenuView.m
//  Dxhd
//
//  Created by j on 14-10-9.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JMenuView.h"
//#import "UIView+WhenTappedBlocks.h"

#define TABLEVIEW_MENU_WIDTH 130.
//#define TABLEVIEW_MENU_ROW_HEIGHT 50.

@interface JMenuView ()

@property (nonatomic, retain) NSArray *menuArray;
@property (nonatomic, retain) NSArray *imageArray;

@end

@implementation JMenuView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [UIColor clearColor];
        //self.alpha = 0.1;

        if (!_menuTableView) {
            self.menuTableView = [[UITableView alloc] initWithFrame:CGRectMake(-TABLEVIEW_MENU_WIDTH, 0, TABLEVIEW_MENU_WIDTH, IOS_HEIGHT-PAGE_TOOLBAR_HEIGHT) style:UITableViewStylePlain];
            self.menuTableView.backgroundColor = [UIColor clearColor];
            self.menuTableView.delegate = self;
            self.menuTableView.dataSource = self;
            self.menuTableView.showsVerticalScrollIndicator = NO;
            self.menuTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        }
        
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.menuTableView.frame.size.width, self.menuTableView.frame.size.height)];
        imageView.image = [UIImage imageNamed:@"bg_menu"];
        imageView.backgroundColor = [UIColor clearColor];
        self.menuTableView.backgroundView = imageView;
        
        if (!_menuArray) {
            //self.menuArray = [NSArray arrayWithObjects:@"用户", @"关于", nil];
            self.menuArray = [NSArray arrayWithObjects:@"用户", @"关于", @"帮助", nil];
        }
        
        if (!_imageArray) {
            //self.imageArray = [NSArray arrayWithObjects:@"bt_user", @"bt_about", nil];
            self.imageArray = [NSArray arrayWithObjects:@"bt_user", @"bt_about", @"bt_help", nil];
        }
        
        [self.menuTableView reloadData];
    }
    return self;
}

//dismiss view after tap other
- (void)dismissWhenTap:(id)sender
{
    [self hideTableViewMenu];
}

//show menu with animation
- (void)showTableViewMenu
{
    [self addSubview:self.menuTableView];
    [UIView animateWithDuration:CUSTOM_ANIMATION*2 animations:^{
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
        [self removeFromSuperview];
    }];
}

#pragma mark -UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }
    return self.menuArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"kCell";
    
    UITableViewCell *cell = (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.backgroundColor = [UIColor clearColor];
        //cell.imageView.frame = CGRectMake(20, 10., 24., 24.);
    }
    
    if (indexPath.section == 0) {
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    else if (indexPath.section == 1) {
        cell.selectionStyle = UITableViewCellSelectionStyleGray;
        cell.textLabel.textColor = [UIColor whiteColor];
        
        if (indexPath.row < self.menuArray.count) {
            cell.textLabel.text = self.menuArray[indexPath.row];
        }
        
        if (indexPath.row < self.imageArray.count) {
            NSString *imageName = self.imageArray[indexPath.row];
            UIImage *image = [UIImage imageNamed:imageName];
            if (image) {
                cell.imageView.image = image;
            }
        }
    }
    
    return cell;
}

#pragma mark -UITableViewDelegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        return DEFAULT_START_HEIGHT+DEFAULT_NAV_HEIGHT;
    }
    return DEFAULT_ROW_HEIGHT;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.section == 0) {
        [self performSelector:@selector(dismissWhenTap:) withObject:nil];
    }
    else if (indexPath.section == 1) {
        //J_LOG(@"indexPath.row = %d", indexPath.row);
        if (indexPath.row < self.menuArray.count) {
            if ([self.delegate respondsToSelector:@selector(menuViewDidSelectIndex:)]) {
                [self.delegate menuViewDidSelectIndex:indexPath.row];
            }
        }
    }
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    CGPoint endPoint = [touch locationInView:self];
    
    if (endPoint.x > TABLEVIEW_MENU_WIDTH) {
        [self performSelector:@selector(dismissWhenTap:) withObject:nil];
    }
    
//    endPoint = [touch locationInView:self.menuTableView];
//    if (endPoint.x <= TABLEVIEW_MENU_WIDTH && endPoint.y > (DEFAULT_START_HEIGHT+DEFAULT_NAV_HEIGHT+self.menuArray.count*DEFAULT_ROW_HEIGHT)) {
//        [self performSelector:@selector(dismissWhenTap:) withObject:nil];
//    }
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    
}

@end
