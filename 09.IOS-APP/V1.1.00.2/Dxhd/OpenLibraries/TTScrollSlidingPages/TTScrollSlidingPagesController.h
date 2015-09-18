//
//  TTSlidingPagesController.h
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

#import <UIKit/UIKit.h>
#import "TTSlidingPagesDataSource.h"

@class TTScrollViewWrapper;

@protocol TTSlidingPagesDelegate;

@interface TTScrollSlidingPagesController : UIViewController<UIScrollViewDelegate>{
    NSInteger currentPageBeforeRotation;
    bool viewDidLoadHasBeenCalled;
    UIPageControl *pageControl;
    UIScrollView *bottomScrollView;
}


-(void)didRotate;
/**Don't use anything above this. Feel free to use anything below!*/

-(void)reloadPages;
-(void)scrollToPage:(NSInteger)page animated:(BOOL)animated;
-(NSInteger)getCurrentDisplayedPage;
-(NSInteger)getXPositionOfPage:(NSInteger)page;


@property (nonatomic, retain) NSArray *insertControllers;

@property (nonatomic, strong) id<TTSlidingPagesDataSource> dataSource;
@property (nonatomic, assign) id<TTSlidingPagesDelegate> delegate;


/**  @property disableUIPageControl
 *   @brief Disables the UIPageControl (the page dots) at the top of the screen
 *   If set to YES the UIPageControl at the top of the screen will not be added. Default is NO. **/
@property (nonatomic) BOOL disableUIPageControl;

/**  @property initialPageNumber
 *   @brief Sets the scroller to scroll to a specific page initially
 *   Sets the scroller to scroll to a specific page initially (either on the first load, or afrer calling reloadPages). Default is 0. **/
@property (nonatomic) NSInteger initialPageNumber;

/**  @property pagingEnabled
 *   @brief Whether the content view "snaps" to each page. Dont set this to NO if you implement widthForPageOnSlidingPagesViewController:atIndex in the delegate as it won't work properly!
 *   Whether the content view "snaps" to each page (YES), or if the scroll is continous (NO). Default is YES. **/
@property (nonatomic) BOOL pagingEnabled;

/**  @property zoomOutAnimationDisabled
 *   @brief Whether the "zoom out" effect that happens as you scroll from page to page should be disabled. Default is NO.
 *   Whether the "zoom out" effect that happens as you scroll from page to page should be disabled. Default is NO **/
@property (nonatomic) bool zoomOutAnimationDisabled;



@end

@protocol TTSlidingPagesDelegate <NSObject>

- (void)TTScrollSlidingPagesController:(TTScrollSlidingPagesController *)controller withIndex:(NSInteger)index;

@end

