//
//  JTestIntfViewController.m
//  Dxhd
//
//  Created by j on 14-9-16.
//  Copyright (c) 2014年 kysx. All rights reserved.
//

#import "JTestIntfViewController.h"
#import "JConfigObject.h"

#import "JRegisterServer.h"
#import "JAccessServer.h"
#import "JLoginServer.h"
#import "JBindServer.h"
#import "JQueryServer.h"
#import "JDemandServer.h"
#import "JKeyServer.h"

@interface JTestIntfViewController () <JRegisterServerDelegate>

@end

@implementation JTestIntfViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)actionRegister:(id)sender
{
    JRegisterServer *server = [[JRegisterServer alloc] initWithDelegate:self];
    [server loadUsername:@"123webg" password:@"12345"];
    //{"auth_code":"","cmd":"user_regist_req","passwd":"","region_code":"","return_code":0,"site_url":"","username":""}
}

- (void)loadRegisterModel:(JRegisterModel *)model
{
    
}

- (void)loadRegisterFailed:(NSError *)error
{
    
}

- (IBAction)actionAccess:(id)sender
{
    JAccessServer *accessServer = [[JAccessServer alloc] initWithDelegate:self];
    [accessServer loadUsername:@"123webg" password:@"12345" appname:APPNAME licence:LICENCE version:@"1.0"];
    //{"appname":"","cmd":"user_access_req","licence":"","passwd":"","return_code":0,"service_url":"192.168.100.11:18080","token":"D0WGS2TJVN","username":"","version":""}
}

//登录用接口的token
- (IBAction)actionLogin:(id)sender
{
    JLoginServer *server = [[JLoginServer alloc] initWithDelegate:self];
    [server loadUsername:@"123webg" token:JCO.token];
    //{"cmd":"user_login_req","message":"用户还未绑定频道...","new_token":"D0WGP1BI5N","return_code":0,"status":"1","url":"http://192.168.100.11/multiscreen/mobile/vod.html"}
    //{"cmd":"user_login_req","message":"正在观看VOD内容,绑定本地频道号0","new_token":"D13D6XW305","return_code":0,"status":"3","url":""}
}

//绑定用登录的token
- (IBAction)actionBind:(id)sender
{
    JBindServer *server = [[JBindServer alloc] initWithDelegate:self];
    [server loadUsername:@"123webg" token:JCO.token streamId:@"1" vodPage:@"d1"];
    //...
    //{"cmd":"user_bind_req","msg":"system error","return_code":-1000}
}


//查询用最新的token
- (IBAction)actionQuery:(id)sender
{
    JQueryServer *server = [[JQueryServer alloc] initWithDelegate:self];
    [server loadUsername:@"123webg" token:JCO.token];
    //{"cmd":"user_sessionquery_req","message":"用户还未绑定频道...","return_code":0,"status":"1"}
    //{"cmd":"user_sessionquery_req","message":"正在观看VOD内容,绑定本地频道号0","return_code":0,"status":"3"}
}

//点播用token
- (IBAction)actionDemand:(id)sender
{
    //JDemandServer *server = [[JDemandServer alloc] initWithDelegate:self];
    //[server loadUsername:@"123webg" token:JCO.token url:@""];
    //...
    //{"cmd":"user_vodplay_req","msg":"token is error!","return_code":-2}
}

- (IBAction)actionKey:(id)sender
{
    JKeyServer *server = [[JKeyServer alloc] initWithDelegate:self];
    [server loadUsername:@"123webg" token:JCO.token sequence:SEQUENCE keyType:KEYTYPE keyValue:@"0x1d"];
    //
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

@end
