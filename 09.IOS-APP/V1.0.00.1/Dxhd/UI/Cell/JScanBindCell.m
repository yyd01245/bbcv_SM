//
//  JScanBindCell.m
//  Dxhd
//
//  Created by j on 14-9-25.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JScanBindCell.h"
#import "UIFont+Default.h"

@implementation JScanBindCell

- (void)awakeFromNib
{
    // Initialization code
    self.leftLabel.font = [UIFont defaultGraphBoldFontWithSize:17.];
    self.rightLabel.font = [UIFont defaultGraphBoldFontWithSize:17.];
    self.channelLabel.font = [UIFont defaultGraphBoldFontWithSize:27.];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

//载入频道数
- (void)loadChannelNumber:(NSString *)number
{
    if (number) {
        self.channelLabel.text = number;
    }
}

@end
