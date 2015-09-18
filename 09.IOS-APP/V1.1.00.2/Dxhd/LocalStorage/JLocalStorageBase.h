//
//  JLocalStorageBase.h
//  Dxhd
//
//  Created by j on 14-11-18.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FMDatabaseQueue.h"

#define USER_DBNAME (@"user.db")
#define USER_TABLENAME @"user_table"

#define JLSB ((JLocalStorageBase *)[JLocalStorageBase shareInstance])

@interface JLocalStorageBase : NSObject

@property (nonatomic, retain) FMDatabaseQueue *databaseQueue;
@property (nonatomic, copy) NSString *dbname;
@property (nonatomic, copy) NSString *userTablename;

+ (id)shareInstance;

- (void)initialize;

/***************************************************/
//数据库操作对象
- (id)createUserDBOperate;
/***************************************************/

/*************************用户数据**********************************/
//插入表
- (void)createUserTableWithName:(NSString *)tablename;
//插入用户数据
- (BOOL)insertUserInfo:(NSString *)username;
//删除用户数据
- (BOOL)deleteUserInfoByUserId:(NSString *)username;
//清除所有用户数据
- (BOOL)clearAllUserData;

//获取所有用户数据
- (NSArray *)getAllUserInfoData;
/***********************************************************/

@end
