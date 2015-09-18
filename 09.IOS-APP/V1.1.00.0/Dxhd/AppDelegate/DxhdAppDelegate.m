//
//  DxhdAppDelegate.m
//  Dxhd
//
//  Created by j on 14-9-15.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "DxhdAppDelegate.h"
#import "JMainViewController.h"
#import "JStartViewController.h"
#import "JLoginViewController.h"
#import "JTestIntfViewController.h"
#import "JMessageHUD.h"
#import "JQueryServer.h"

@interface DxhdAppDelegate () <JStartViewControllerDelegate, JLoginViewControllerDelegate, JQueryServerDelegate>
{
    NSTimer *_timer;
}

//load start view
- (void)loadStartView;
//load login view
- (void)loadLoginView;
//load main view
- (void)loadMainViewAfterLogin;
//send query request
- (void)sendQueryRequest;

@end

@implementation DxhdAppDelegate

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:START_QUERY_STATE_NOTIFICATION_NAME object:nil];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.backgroundColor = [UIColor clearColor];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(startQueryState) name:START_QUERY_STATE_NOTIFICATION_NAME object:nil];
    
    //JTestIntfViewController *testIntfViewController = [[JTestIntfViewController alloc] initWithNibName:nil bundle:nil];
    //self.window.rootViewController = testIntfViewController;

    //[self loadMainViewAfterLogin];
    [self loadStartView];
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

//load start view
- (void)loadStartView
{
    if (IPHONE5) {
        JStartViewController *startViewController = [[JStartViewController alloc] initWithNibName:@"JStartViewController" bundle:[NSBundle mainBundle]];
        startViewController.delegate = self;
        self.window.rootViewController = startViewController;
    }
    else if (IPHONE4) {
        JStartViewController *startViewController = [[JStartViewController alloc] initWithNibName:@"JStartViewControllerForIphone4" bundle:[NSBundle mainBundle]];
        startViewController.delegate = self;
        self.window.rootViewController = startViewController;
    }
    else {
        [[JMessageHUD shareInstance] showWithMessage:@"未适配该设备！"];
    }
}

#pragma mark -JStartViewControllerDelegate
- (void)dumpToBindWithViewController:(JStartViewController *)viewController
{
    if (viewController) {
        [self loadLoginView];
    }
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

//start query
- (void)startQueryState
{
    if (!_timer) {
        _timer = [NSTimer scheduledTimerWithTimeInterval:10. target:self selector:@selector(sendQueryRequest) userInfo:nil repeats:YES];
        [[NSRunLoop currentRunLoop] addTimer:_timer forMode:NSDefaultRunLoopMode];
    }
}

//send query request
- (void)sendQueryRequest
{
    if (JCO.username && JCO.token) {
        JQueryServer *server = [[JQueryServer alloc] initWithDelegate:self];
        [server loadUsername:JCO.username token:JCO.token];
    }
}

#pragma mark -JQueryServerDelegate
- (void)loadQueryModel:(JQueryModel *)model
{
    if (model && model.return_code == 0) {
        JCO.currentState = model.status;
        
        if ([JCO.currentState isEqualToString:@"1"]) {
            JCO.playChannel = NO;
        }
        else if ([JCO.currentState isEqualToString:@"2"]) {
            JCO.playChannel = NO;
        }
        else if ([JCO.currentState isEqualToString:@"3"]) {
            JCO.playChannel = YES;
        }
        
        [[NSNotificationCenter defaultCenter] postNotificationName:QUERY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] postNotificationName:PLAY_STATE_CHANGE_NOTIFICATION_NAME object:nil];
    }
}

- (void)loadQueryFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:@"网络断开，请检查网络状况！"];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server
{
    
}

@end
