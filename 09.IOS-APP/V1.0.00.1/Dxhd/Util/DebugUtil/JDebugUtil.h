//
//  JDebugUtil.h
//  health
//
//  Created by j on 13-11-4.
//  Copyright (c) 2013年 linkhealth. All rights reserved.
//

#import "JDefineUtil.h"

#define _DEBUG_
#ifdef  _DEBUG_

#define J_LOG(...)		NSLog(__VA_ARGS__)
#define J_LOGRECT(r)	NSLog(@"rect = (%.1fx%.1f)-(%.1fx%.1f)", r.origin.x, r.origin.y, r.size.width, r.size.height)
#define J_LOGFUN		NSLog(@"Class Name:%@##%@",NSStringFromClass([self class]),NSStringFromSelector(_cmd))

#else
#define J_LOG(...)
#define J_LOGRECT(r)
#define J_LOGFUN
#endif
