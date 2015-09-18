//
//  JRemotePageControl.h
//  kysx
//
//  Created by j on 13-7-23.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIControl.h>
#import <UIKit/UIKitDefines.h>

typedef NS_ENUM(NSInteger, JPageControlType) {
    JPageControlTypeOnFullOffFull = 0,
	JPageControlTypeOnFullOffEmpty,
	JPageControlTypeOnEmptyOffFull,
	JPageControlTypeOnEmptyOffEmpty
};

@interface JRemotePageControl : UIControl
{
	NSInteger numberOfPages;
	NSInteger currentPage;
}

@property (nonatomic, assign) NSInteger numberOfPages;
@property (nonatomic, assign) NSInteger currentPage;

@property (nonatomic) BOOL hidesForSinglePage;
@property (nonatomic) BOOL defersCurrentPageDisplay;

- (void)updateCurrentPageDisplay;

- (CGSize)sizeForNumberOfPages:(NSInteger)pageCount;

@property (nonatomic, assign) JPageControlType type;

@property (nonatomic, retain) UIColor *onColor;
@property (nonatomic, retain) UIColor *offColor;

@property (nonatomic, assign) CGFloat indicatorDiameter;
@property (nonatomic, assign) CGFloat indicatorSpace;

@end
