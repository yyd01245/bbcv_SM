//
//  DxhdAppDelegate.m
//  Dxhd
//
//  Created by j on 14-9-15.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "DxhdAppDelegate.h"
#import "JMainViewController.h"
#import "JLoginViewController.h"
#import "JTestIntfViewController.h"
#import "JMessageHUD.h"

@interface DxhdAppDelegate () <JLoginViewControllerDelegate>

//load login view
- (void)loadLoginView;
//load main view
- (void)loadMainViewAfterLogin;

@end

@implementation DxhdAppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.backgroundColor = [UIColor clearColor];
    
    //JTestIntfViewController *testIntfViewController = [[JTestIntfViewController alloc] initWithNibName:nil bundle:nil];
    //self.window.rootViewController = testIntfViewController;

    //[self loadMainViewAfterLogin];
    [self loadLoginView];
    // Override point for customization after application launch.
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

//load login view
- (void)loadLoginView
{
    if (IPHONE5) {
        JLoginViewController *loginViewController = [[JLoginViewController alloc] initWithNibName:@"JLoginViewController" bundle:[NSBundle mainBundle]];
        loginViewController.loginDelegate = self;
        self.window.rootViewController = loginViewController;
    }
    else if (IPHONE4) {
        JLoginViewController *loginViewController = [[JLoginViewController alloc] initWithNibName:@"JLoginViewControllerForIphone4" bundle:[NSBundle mainBundle]];
        loginViewController.loginDelegate = self;
        self.window.rootViewController = loginViewController;
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"未适配该设备！"];
    }
}

#pragma mark -JLoginViewControllerDelegate
- (void)viewDidLogin:(JLoginViewController *)loginViewController
{
    if (loginViewController) {
        [self loadMainViewAfterLogin];
    }
}

//load main view
- (void)loadMainViewAfterLogin
{
    JMainViewController *mainViewController = [[JMainViewController alloc] initWithNibName:nil bundle:nil];
    self.window.rootViewController = mainViewController;
    //[self.window addSubview:mainViewController.view];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_WEBVIEW_NOTIFICATION_NAME object:nil];
}

@end
