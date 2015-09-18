//
//  JMainViewController.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JMainViewController.h"
#import "TTSlidingPage.h"
#import "TTSlidingPageTitle.h"
#import "JDumpController.h"
#import "JScanViewController.h"
#import "JWebViewController.h"
#import "JRemoterViewController.h"
#import "JLoginViewController.h"

@interface JMainViewController () <JLoginViewControllerDelegate>

@end

@implementation JMainViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:BANDTODUMP_NOTIFICATION_NAME object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:REMOTERTODUMP_NOTIFICATION_NAME object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:DUMP_TO_REMOTER_NOTIFICATION_NAME object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dumpToWebFromScan) name:BANDTODUMP_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dumpToWebFromRemoter) name:REMOTERTODUMP_NOTIFICATION_NAME object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dumpToRemoter) name:DUMP_TO_REMOTER_NOTIFICATION_NAME object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.view.frame = [[UIScreen mainScreen] bounds];
    
    if (!_slidingPagesController) {
        self.slidingPagesController = [[TTScrollSlidingPagesController alloc] init];
        self.slidingPagesController.initialPageNumber = 0;
        self.slidingPagesController.dataSource = self;
        self.slidingPagesController.delegate = self;
        self.slidingPagesController.view.frame = CGRectMake(0, 0, IOS_WIDTH, IOS_HEIGHT);
        [self.view addSubview:self.slidingPagesController.view];
        [self addChildViewController:self.slidingPagesController];
    }
    
    if (!_pageToolBar) {
        self.pageToolBar = [[JPageToolBar alloc] initWithFrame:CGRectMake(0, IOS_HEIGHT-PAGE_TOOLBAR_HEIGHT, IOS_WIDTH, PAGE_TOOLBAR_HEIGHT)];
        self.pageToolBar.backgroundColor = [UIColor clearColor];
        self.pageToolBar.toolDelegate = self;
        [self.view addSubview:self.pageToolBar];
    }
    
    if (!_menuView) {
        self.menuView = [[JMenuView alloc] initWithFrame:CGRectMake(0, 0, IOS_WIDTH, IOS_HEIGHT-PAGE_TOOLBAR_HEIGHT)];
        self.menuView.delegate = self;
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//跳转至索引页
- (void)scrollToPage:(NSInteger)page
{
    [self.slidingPagesController scrollToPage:page animated:YES];
    [self.pageToolBar updatePage:page];
}

//show menu
- (void)showMenu
{
    [self.view addSubview:self.menuView];
    [self.menuView showTableViewMenu];
}

//hide menu
- (void)hideMenu
{
    [self.menuView hideTableViewMenu];
}

#pragma mark TTSlidingPagesDataSource methods
- (NSInteger)numberOfPagesForSlidingPagesViewController:(TTScrollSlidingPagesController *)source
{
    return 3;
}

- (TTSlidingPage *)pageForSlidingPagesViewController:(TTScrollSlidingPagesController*)source atIndex:(NSInteger)index{
    
    if (index == 0) {
        if (IPHONE5) {
            JScanViewController *scanViewController = [[JScanViewController alloc] initWithNibName:@"JScanViewController" bundle:[NSBundle mainBundle]];
            return [[TTSlidingPage alloc] initWithContentViewController:scanViewController];
        }
        else {
            JScanViewController *scanViewController = [[JScanViewController alloc] initWithNibName:@"JScanViewControllerForIphone4" bundle:[NSBundle mainBundle]];
            return [[TTSlidingPage alloc] initWithContentViewController:scanViewController];
        }
    }
    else if (index == 1) {
        JWebViewController *webViewController = [[JWebViewController alloc] initWithNibName:nil bundle:nil];
        return [[TTSlidingPage alloc] initWithContentViewController:webViewController];
    }
    else {
        if (IPHONE5) {
            JRemoterViewController *remoterViewController = [[JRemoterViewController alloc] initWithNibName:@"JRemoterViewController" bundle:[NSBundle mainBundle]];
            return [[TTSlidingPage alloc] initWithContentViewController:remoterViewController];
        }
        else {
            JRemoterViewController *remoterViewController = [[JRemoterViewController alloc] initWithNibName:@"JRemoterViewControllerForIphone4" bundle:[NSBundle mainBundle]];
            return [[TTSlidingPage alloc] initWithContentViewController:remoterViewController];
        }
    }
}

#pragma mark -TTSlidingPagesDelegate
- (void)TTScrollSlidingPagesController:(TTScrollSlidingPagesController *)controller withIndex:(NSInteger)index
{
    [self.pageToolBar updatePage:index];
}

//跳转至web页
- (void)dumpToWebFromScan
{
    if (self.slidingPagesController.insertControllers.count > 1) {
//        TTSlidingPage *slidingPage = [self.slidingPagesController.insertControllers objectAtIndex:0];
//        if (slidingPage) {
//            if ([slidingPage.contentViewController isKindOfClass:[JScanViewController class]]) {
//                
//                JScanViewController *scanViewController = (JScanViewController *)slidingPage.contentViewController;
//                [scanViewController afterBind];
//            }
//        }
        
        TTSlidingPage *webSlidingPage = [self.slidingPagesController.insertControllers objectAtIndex:1];
        if (webSlidingPage) {
            if ([webSlidingPage.contentViewController isKindOfClass:[JWebViewController class]]) {
                
                JWebViewController *webViewController = (JWebViewController *)webSlidingPage.contentViewController;
                [webViewController updateWebView];
            }
        }
        
        [self scrollToPage:1];
    }
}

//跳转至web页
- (void)dumpToWebFromRemoter
{
    [self scrollToPage:1];
}

//跳转至遥控页
- (void)dumpToRemoter
{
    [self scrollToPage:2];
}

#pragma mark -JPageToolBarDetegate
- (void)pageToolBarDidMenuTap:(JPageToolBar *)bar
{
    if ([self.view.subviews containsObject:self.menuView]) {
        [self hideMenu];
    }
    else {
        [self showMenu];
    }
}

- (void)pageToolBarDidUserTap:(JPageToolBar *)bar
{
    if (bar.pageControl.currentPage == 1) {
        if (self.slidingPagesController.insertControllers.count > 1) {

            TTSlidingPage *webSlidingPage = [self.slidingPagesController.insertControllers objectAtIndex:1];
            if (webSlidingPage) {
                if ([webSlidingPage.contentViewController isKindOfClass:[JWebViewController class]]) {
                    
                    JWebViewController *webViewController = (JWebViewController *)webSlidingPage.contentViewController;
                    [webViewController webViewGoBack];
                }
            }
        }
    }
}

- (void)pageToolBarDidPageTap:(JPageToolBar *)bar
{
    [self scrollToPage:bar.pageControl.currentPage];
}

#pragma mark -JMenuViewDelegate
- (void)menuViewDidSelectIndex:(NSInteger)index
{
    switch (index) {
        case 0:[JDC presentLoginViewControllerFormViewController:self];
            break;
        case 1:[JDC pushAboutViewControllerFormViewController:self];
            break;
        case 2:[JDC presentHelpViewControllerFormViewController:self];
            break;
        default:
            break;
    }
    
    [self hideMenu];
}

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
