//
//  JLocalStorageBase.m
//  Dxhd
//
//  Created by j on 14-11-18.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JLocalStorageBase.h"
#import "FMDatabase.h"
#import "FMDatabaseAdditions.h"

@implementation JLocalStorageBase

- (id)init
{
    self = [super init];
    if (self) {
        // 沙盒Doc目录
        NSString * docDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES) objectAtIndex:0];
        NSString *path = [docDirectory stringByAppendingString:[NSString stringWithFormat:@"/%@",USER_DBNAME]];
        self.databaseQueue = [FMDatabaseQueue databaseQueueWithPath:path];
    }
    return self;
}

+ (id)shareInstance
{
    static JLocalStorageBase *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[JLocalStorageBase alloc] init];
    });
    return instance;
}

- (void)initialize
{
    
}

/***************************************************/
//数据库操作对象
- (id)createUserDBOperate
{
    [self createUserTableWithName:USER_TABLENAME];
    return self;
}
/***************************************************/

/*************************用户数据**********************************/
//插入表
- (void)createUserTableWithName:(NSString *)tablename
{
    if (!tablename || !self.databaseQueue) {
        return;
    }
    
    self.userTablename = tablename;
    
    [self.databaseQueue inDatabase:^ (FMDatabase *db) {
        [db open];
        
        FMResultSet *set = [db executeQuery:[NSString stringWithFormat:@"select count(*) from sqlite_master where type ='table' and name = '%@'", self.userTablename]];
        
        if (set) {
            [set next];
            
            NSInteger count = [set intForColumnIndex:0];//J_LOG(@"创建 count = %ld", count);
            
            if (count == 0) {
                NSString *sql = [NSString stringWithFormat:@"CREATE TABLE %@ (user_name VARCHAR(50), login_flag INTEGER, login_date LONG)", self.userTablename];
                
                BOOL res = [db executeUpdate:sql];
                if (res) {
                    J_LOG(@">>>数据库创建成功");
                }
                else {
                    J_LOG(@">>>数据库创建失败");
                }
            }
        }
        
        [db close];
    }];
}

//插入用户数据
- (BOOL)insertUserInfo:(NSString *)username
{
    if (username) {
        __block BOOL ret = NO;
        [self.databaseQueue inDatabase:^(FMDatabase *db) {
            [db open];
            
            NSString *sql = [NSString stringWithFormat:@"INSERT INTO \"%@\" (\"user_name\",\"login_flag\", \"login_date\") VALUES(?,?,?)", self.userTablename];
            
            NSMutableArray *arguments = [NSMutableArray arrayWithCapacity:3];
            [arguments addObject:username];
            [arguments addObject:[NSNumber numberWithInteger:0]];
            [arguments addObject:[NSNumber numberWithLong:0]];
            
            ret = [db executeUpdate:sql withArgumentsInArray:arguments];
            
            [db close];
        }];
        return ret;
    }
    return NO;
}

//删除用户数据
- (BOOL)deleteUserInfoByUserId:(NSString *)username
{
    if (username) {
        __block BOOL ret = NO;
        
        [self.databaseQueue inDatabase:^(FMDatabase *db) {
            [db open];
            
            NSString *sql = [NSString stringWithFormat:@"DELETE FROM \"%@\" where \"user_name\" = \"%@\"",self.userTablename, username];
            ret = [db executeUpdate:sql];
            
            [db close];
        }];
        return ret;
    }
    return NO;
}

//清除所有用户数据
- (BOOL)clearAllUserData
{
    __block BOOL result = YES;
    
    [self.databaseQueue inDatabase:^ (FMDatabase *db) {
        [db open];
        
        BOOL ret = NO;
        
        NSString *sql = [NSString stringWithFormat:@"DELETE FROM %@", USER_TABLENAME];
        ret = [db executeUpdate:sql];
        if (!ret) {
            result = NO;
        }
        
        [db close];
    }];
    
    return result;
}

//获取所有用户数据
- (NSArray *)getAllUserInfoData
{
    __block NSMutableArray *mutableArray = [NSMutableArray array];
    
    [self.databaseQueue inDatabase:^(FMDatabase *db) {
        [db open];
        
        NSString *sql = [NSString stringWithFormat:@"SELECT * FROM \"%@\" ORDER BY \"login_date\" desc",self.userTablename];
        
        FMResultSet *set = [db executeQuery:sql];
        
        while ([set next]) {
            NSString *userName = [set stringForColumn:@"user_name"];
            if (userName) {
                [mutableArray addObject:userName];
            }
        }
        [set close];
        
        [db close];
    }];
    
    return (NSArray *)mutableArray;
}
/***********************************************************/

@end
