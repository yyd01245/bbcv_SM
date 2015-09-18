//
//  JScanBindCell.h
//  Dxhd
//
//  Created by j on 14-9-25.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <UIKit/UIKit.h>

#define SCAN_BIND_CELL_HEIGHT 56.
/*
 绑定后显示
 */
@interface JScanBindCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *leftLabel;
@property (nonatomic, strong) IBOutlet UILabel *rightLabel;
@property (nonatomic, strong) IBOutlet UILabel *channelLabel;

//载入频道数
- (void)loadChannelNumber:(NSString *)number;

@end
