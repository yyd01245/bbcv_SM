//
//  TTSlidingPagesController.m
//  UIScrollViewSlidingPages
//
//  Created by Thomas Thorpe on 27/03/2013.
//  Copyright (c) 2013 Thomas Thorpe. All rights reserved.
//

/*
 Copyright (c) 2012 Tom Thorpe. All rights reserved.
 
 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 of the Software, and to permit persons to whom the Software is furnished to do
 so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

#import "TTScrollSlidingPagesController.h"
#import "TTSlidingPage.h"
#import "TTSlidingPageTitle.h"
#import <QuartzCore/QuartzCore.h>
#import "TTBlackTriangle.h"
#import "TTScrollViewWrapper.h"

@interface TTScrollSlidingPagesController ()

@end

@implementation TTScrollSlidingPagesController
@synthesize insertControllers = _insertControllers;
@synthesize delegate = _delegate;

/**
 Initalises the control and sets all the default values for the user-settable properties.
 */
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        //set defaults
        self.disableUIPageControl = NO;
        self.initialPageNumber = 0;
        self.pagingEnabled = YES;
        self.zoomOutAnimationDisabled = YES;
    }
    return self;
}

/**
 Initialse the top and bottom scrollers (but don't populate them with pages yet)
 */
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSInteger nextYPosition = 0;//20.
    
    NSInteger pageViewHeight = 0;
    if (!self.disableUIPageControl){
        //create and add the UIPageControl
        pageViewHeight = 20;
        pageControl = [[UIPageControl alloc] initWithFrame:CGRectMake(0, nextYPosition, self.view.frame.size.width, pageViewHeight)];
        pageControl.backgroundColor = [UIColor blackColor];
        pageControl.currentPage = 0;
        pageControl.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
        [pageControl addTarget:self action:@selector(pageControlChangedPage:) forControlEvents:UIControlEventValueChanged];
        //[self.view addSubview:pageControl];
        //nextYPosition += pageViewHeight;
    }
    
    //J_LOG(@"self.view.frame.size.width = %f", self.view.frame.size.width);
    //set up the bottom scroller (for the content to go in)
    bottomScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, nextYPosition, self.view.frame.size.width, self.view.frame.size.height-nextYPosition)];
    bottomScrollView.pagingEnabled = self.pagingEnabled;
    bottomScrollView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
    bottomScrollView.showsVerticalScrollIndicator = NO;
    bottomScrollView.showsHorizontalScrollIndicator = NO;
    //bottomScrollView.directionalLockEnabled = YES;
    bottomScrollView.bounces = NO;
    bottomScrollView.delegate = self;
    bottomScrollView.alwaysBounceVertical = NO;
    [self.view addSubview:bottomScrollView];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/**
 Goes through the datasource and finds all the pages, then populates the topScrollView and bottomScrollView with all the pages and headers.
 
 It clears any of the views in both scrollViews first, so if you need to reload all the pages with new data from the dataSource for some reason, you can call this method.
 */
-(void)reloadPages{
    if (self.dataSource == nil){
        return;
    }
    
    //remove any existing items from the subviews
    //[topScrollView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [bottomScrollView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    //remove any existing items from the view hierarchy
    for (UIViewController* subViewController in self.childViewControllers){
        [subViewController willMoveToParentViewController:nil];
        [subViewController removeFromParentViewController];
    }
    
    //get the number of pages
    NSInteger numOfPages = [self.dataSource numberOfPagesForSlidingPagesViewController:self];
    
    //keep track of where next to put items in each scroller
    NSInteger nextXPosition = 0;
    //NSInteger nextTopScrollerXPosition = 0;
    
    NSMutableArray *mutableArray = [NSMutableArray array];
    
    //loop through each page and add it to the scroller
    for (NSInteger i=0; i<numOfPages; i++){
        
        //bottom scroller add-----
        //set the default width of the page
        NSInteger pageWidth = bottomScrollView.frame.size.width;
        //if the datasource implements the widthForPageOnSlidingPagesViewController:atIndex method, use it to override the width of the page
        if ([self.dataSource respondsToSelector:@selector(widthForPageOnSlidingPagesViewController:atIndex:)] ){
            pageWidth = [self.dataSource widthForPageOnSlidingPagesViewController:self atIndex:i];
        }
        
        TTSlidingPage *page = [self.dataSource pageForSlidingPagesViewController:self atIndex:i];//get the page
        if (page == nil || ![page isKindOfClass:[TTSlidingPage class]]){
            
            J_LOG(@"one was returned that was either nil, or wasn't a TTSlidingPage");
        }
        UIView *contentView = page.contentView;
        
        //put it in the right position, y is always 0, x is incremented with each item you add (it is a horizontal scroller).
        contentView.frame = CGRectMake(nextXPosition, 0, pageWidth, bottomScrollView.frame.size.height);
        [bottomScrollView addSubview:contentView];
        nextXPosition = nextXPosition + contentView.frame.size.width;
        
        if (page.contentViewController != nil){
            [self addChildViewController:page.contentViewController];
            [page.contentViewController didMoveToParentViewController:self];
            
            [mutableArray addObject:page];
        }
    }
    
    if (mutableArray.count > 0) {
        self.insertControllers = (NSArray *)mutableArray;
    }
    
    //now set the content size of the scroller to be as wide as nextXPosition (we can know that nextXPosition is also the width of the scroller)
    bottomScrollView.contentSize = CGSizeMake(nextXPosition, bottomScrollView.frame.size.height);
    
    NSInteger initialPage = self.initialPageNumber;
    
    if (!self.disableUIPageControl){
        //set the number of dots on the page control, and set the initial selected dot
        pageControl.numberOfPages = numOfPages;
        pageControl.currentPage = initialPage;
        
        //fade in the page dots
        if (pageControl.alpha != 1.0){
            [UIView animateWithDuration:1.5 animations:^{
                pageControl.alpha = 1.0f;
            }];
        }
    }
    
    //scroll to the initialpage
    [self scrollToPage:initialPage animated:NO];
}


/**
 Gets number of the page currently displayed in the bottom scroller (zero based - so starting at 0 for the first page).
 
 @return Returns the number of the page currently displayed in the bottom scroller (zero based - so starting at 0 for the first page).
 */
-(NSInteger)getCurrentDisplayedPage{
    //sum through all the views until you get to a position that matches the offset then that's what page youre on (each view can be a different width)
    NSInteger page = 0;
    NSInteger currentXPosition = 0;
    while (currentXPosition <= bottomScrollView.contentOffset.x && currentXPosition < bottomScrollView.contentSize.width){
        currentXPosition += [self getWidthOfPage:page];
        
        if (currentXPosition <= bottomScrollView.contentOffset.x){
            page++;
        }
    }
    
    return page;
}

/**
 Gets the x position of the requested page in the bottom scroller. For example, if you ask for page 5, and page 5 starts at the contentOffset 520px in the bottom scroller, this will return 520.
 
 @param page The page number requested.
 @return Returns the x position of the requested page in the bottom scroller
 */
-(NSInteger)getXPositionOfPage:(NSInteger)page{
    //each view could in theory have a different width
    NSInteger currentTotal = 0;
    for (NSInteger curPage = 0; curPage < page; curPage++){
        currentTotal += [self getWidthOfPage:curPage];
    }
    
    return currentTotal;
}

/**
 Gets the width of a specific page in the bottom scroll view. Most of the time this will be the width of the scrollview itself, but if you have widthForPageOnSlidingPagesViewController implemented on the datasource it might be different - hence this method.
 
 @param page The page number requested.
 @return Returns the width of the page requested.
 */
-(NSInteger)getWidthOfPage:(NSInteger)page {
    NSInteger pageWidth = bottomScrollView.frame.size.width;
    if ([self.dataSource respondsToSelector:@selector(widthForPageOnSlidingPagesViewController:atIndex:)]){
        pageWidth = [self.dataSource widthForPageOnSlidingPagesViewController:self atIndex:page];
    }
    return pageWidth;
}


/**
 Scrolls the bottom scorller (content scroller) to a particular page number.
 
 @param page The page number to scroll to.
 @param animated Whether the scroll should be animated to move along to the page (YES) or just directly scroll to the page (NO)
 */
-(void)scrollToPage:(NSInteger)page animated:(BOOL)animated{
    //keep track of the current page (for the rotation if it ever happens)
    currentPageBeforeRotation = page;
    
    //scroll to the page
    [bottomScrollView setContentOffset: CGPointMake(IOS_WIDTH*page,0) animated:animated];
    //[bottomScrollView setContentOffset: CGPointMake([self getXPositionOfPage:page],0) animated:animated];
    
    //update the pagedots pagenumber
    if (!self.disableUIPageControl){
        pageControl.currentPage = page;
    }
    
//    J_LOG(@"page = %d", page);
//    J_LOG(@"pageControl.currentPage = %d", pageControl.currentPage);
//    J_LOG(@"x = %f", bottomScrollView.contentOffset.x);
}


#pragma mark Some delegate methods for handling rotation.

-(void)didRotate{
    currentPageBeforeRotation = [self getCurrentDisplayedPage];
}


-(void)viewDidLayoutSubviews{
    
    
    //reposition the subviews and set the new contentsize width
    CGRect frame;
    NSInteger nextXPosition = 0;
    NSInteger page = 0;
    for (UIView *view in bottomScrollView.subviews) {
        view.transform = CGAffineTransformIdentity;
        frame = view.frame;
        frame.size.width = [self getWidthOfPage:page];
        frame.size.height = bottomScrollView.frame.size.height;
        frame.origin.x = nextXPosition;
        frame.origin.y = 0;
        page++;
        nextXPosition += frame.size.width;
        view.frame = frame;
    }
    bottomScrollView.contentSize = CGSizeMake(nextXPosition, bottomScrollView.frame.size.height);
    
    //set it back to the same page as it was before (the contentoffset will be different now the widths are different)
    NSInteger contentOffsetWidth = [self getXPositionOfPage:currentPageBeforeRotation];
    bottomScrollView.contentOffset = CGPointMake(contentOffsetWidth, 0);
    
}

#pragma mark UIScrollView delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    NSInteger currentPage = [self getCurrentDisplayedPage];
    
    if (!self.zoomOutAnimationDisabled){
        //Do a zoom out effect on the current view and next view depending on the amount scrolled
        double minimumZoom = 0.93;
        double zoomSpeed = 1000;//increase this number to slow down the zoom
        UIView *currentView = [bottomScrollView.subviews objectAtIndex:currentPage];
        UIView *nextView;
        if (currentPage < [bottomScrollView.subviews count]-1){
            nextView = [bottomScrollView.subviews objectAtIndex:currentPage+1];
        }
        
        //currentView zooms out as scroll left
        NSInteger distanceFromPageOrigin = bottomScrollView.contentOffset.x - [self getXPositionOfPage:currentPage]; //find out how far the scroll is away from the start of the page, and use this to adjust the transform of the currentView
        if (distanceFromPageOrigin < 0) {distanceFromPageOrigin = 0;}
        double scaleAmount = 1-(distanceFromPageOrigin/zoomSpeed);
        if (scaleAmount < minimumZoom ){scaleAmount = minimumZoom;}
        currentView.transform = CGAffineTransformScale(CGAffineTransformIdentity, scaleAmount, scaleAmount);
        
        //nextView zooms in as scroll left
        if (nextView != nil){
            //find out how far the scroll is away from the start of the next page, and use this to adjust the transform of the nextView
            distanceFromPageOrigin = (bottomScrollView.contentOffset.x - [self getXPositionOfPage:currentPage+1]) * -1;//multiply by minus 1 to get the distance to the next page (because otherwise the result would be -300 for example, as in 300 away from the next page)
            if (distanceFromPageOrigin < 0) {distanceFromPageOrigin = 0;}
            scaleAmount = 1-(distanceFromPageOrigin/zoomSpeed);
            if (scaleAmount < minimumZoom ){scaleAmount = minimumZoom;}
            nextView.transform = CGAffineTransformScale(CGAffineTransformIdentity, scaleAmount, scaleAmount);
        }
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    NSInteger currentPage = [self getCurrentDisplayedPage];
    
    //store the page you were on so if you have a rotate event, or you come back to this view you know what page to start at. (for example from a navigation controller), the viewDidLayoutSubviews method will know which page to navigate to (for example if the screen was portrait when you left, then you changed to landscape, and navigate back, then viewDidLayoutSubviews will need to change all the sizes of the views, but still know what page to set the offset to)
    currentPageBeforeRotation = [self getCurrentDisplayedPage];
    
    
    //update the pagedots pagenuber
    if (!self.disableUIPageControl){
        //set the correct page on the pagedots
        pageControl.currentPage = currentPage;
    }
    
    /*Just do a quick check, that if the paging enabled property is YES (paging is enabled), the user should not define widthForPageOnSlidingPagesViewController on the datasource delegate because scrollviews do not cope well with paging being enabled for scrollviews where each subview is not full width! */
    if (self.pagingEnabled == YES && [self.dataSource respondsToSelector:@selector(widthForPageOnSlidingPagesViewController:atIndex:)]){
        
    }
    
    if ([self.delegate respondsToSelector:@selector(TTScrollSlidingPagesController:withIndex:)]) {
        [self.delegate TTScrollSlidingPagesController:self withIndex:currentPage];
    }
}

#pragma mark UIPageControl page changed listener we set up on it
-(void)pageControlChangedPage:(id)sender
{
    //if not already on the page and the page is within the bounds of the pages we have, scroll to the page!
    NSInteger page = (NSInteger)pageControl.currentPage;
    if ([self getCurrentDisplayedPage] != page && page < [bottomScrollView.subviews count]){
        [self scrollToPage:page animated:YES];
    }
}

#pragma mark property setters - for when need to do fancy things as well as set the value

-(void)setDataSource:(id<TTSlidingPagesDataSource>)dataSource{
    _dataSource = dataSource;
    if (self.view != nil){
        [self reloadPages];
    }
}

-(void)setPagingEnabled:(BOOL)pagingEnabled{
    _pagingEnabled = pagingEnabled;
    if (bottomScrollView != nil){
        bottomScrollView.pagingEnabled = pagingEnabled;
    }
}

-(void)setDisableUIPageControl:(BOOL)disableUIPageControl{
    
    _disableUIPageControl = disableUIPageControl;
}

@end
