//
//  JUserManagerViewController.m
//  Dxhd
//
//  Created by j on 14-10-8.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JUserManagerViewController.h"
#import "JLoginViewController.h"
#import "JConfigObject.h"
#import "JDumpController.h"
#import "JKeyServer.h"

@interface JUserManagerViewController () <JLoginViewControllerDelegate, JKeyServerDelegate, UIAlertViewDelegate>

//@property (nonatomic, retain) NSMutableArray *listArray;

//send key request
- (void)sendKeyRequestWithKeyValue:(NSString *)keyValue;

- (void)reloadUserData;

@end

@implementation JUserManagerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.bounds = [[UIScreen mainScreen] bounds];
    
    /*
    if (!_listArray) {
        self.listArray = [NSMutableArray array];
    }*/
    
    //[self reloadUserData];
    
    self.usernameLabel.text = JCO.username;
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

- (IBAction)actionChangeUser:(id)sender
{
    [JDC presentLoginViewControllerFormViewController:self];
}

- (IBAction)actionExit:(id)sender
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"是否退出应用" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    [alert show];
}

//dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [alertView dismissWithClickedButtonIndex:0 animated:YES];
    J_LOG(@"buttonIndex = %d", buttonIndex);
    if (buttonIndex == 1) {
        if (JCO.isPlayChannel) {
            [self sendKeyRequestWithKeyValue:BlcIrrPropertyValue_EXIT];
        }
        else {
            exit(0);
        }
    }
}

#pragma mark -key request
- (void)sendKeyRequestWithKeyValue:(NSString *)keyValue
{
    if (JCO.username && JCO.token && keyValue) {
        JKeyServer *server = [[JKeyServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token sequence:SEQUENCE keyType:KEYTYPE keyValue:keyValue];
    }
}

#pragma mark -JKeyServerDelegate
- (void)loadKeyModel:(JKeyModel *)model
{
    if (model && model.return_code == 0) {
        exit(0);
    }
}

- (void)loadKeyFailed:(NSError *)error
{
    //[[JMessageHUD shareInstance] showWithMessage:REQUEST_FAIL];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

- (void)reloadUserData
{/*
    if (_listArray) {
        [self.listArray removeAllObjects];
    }
    
    NSArray *array = [JCO getAllUserName];
    if (array.count > 0) {
        for (int i = 0; i < array.count; i++) {
            NSString *string = [array objectAtIndex:i];
            if (![string isEqualToString:JCO.username]) {
                [self.listArray addObject:string];
            }
        }
    }
    J_LOG(@"self.listArray.count = %d", self.listArray.count);
    [self.userManagerTableView performSelector:@selector(reloadData) withObject:nil afterDelay:0.5];
  */
}
/*
#pragma mark -UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (self.listArray.count > 0) {
        return 2;
    }
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }
    return self.listArray.count;
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
    
    if (indexPath.section == 0) {
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.text = JCO.username;
    }
    else if (indexPath.section == 1) {
        cell.selectionStyle = UITableViewCellSelectionStyleGray;
        
        if (indexPath.row < self.listArray.count) {
            cell.textLabel.text = self.listArray[indexPath.row];
        }
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
    
    if (indexPath.section == 0) {
        
    }
    else if (indexPath.section == 1) {
        //J_LOG(@"indexPath.row = %d", indexPath.row);
        if (indexPath.row < self.listArray.count) {
            
        }
    }
}*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark -JLoginViewControllerDelegate
- (void)viewDidLogin:(JLoginViewController *)loginViewController
{
    if (loginViewController) {
        [loginViewController dismissViewControllerAnimated:YES completion:^{
            
        }];
        
        [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_SCAN_STATE_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_WEBVIEW_NOTIFICATION_NAME object:nil];
    }
}

@end
