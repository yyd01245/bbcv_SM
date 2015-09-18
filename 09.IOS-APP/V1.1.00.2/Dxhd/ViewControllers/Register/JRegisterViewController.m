//
//  JRegisterViewController.m
//  dphd
//
//  Created by j on 13-12-3.
//  Copyright (c) 2013年 宽云视讯. All rights reserved.
//

#import "JRegisterViewController.h"
#import "JConfigObject.h"
#import "JMessageHUD.h"
#import "NSString+Check.h"
#import "UIView+WhenTappedBlocks.h"
#import "JRegisterServer.h"

#import "VerifyViewController.h"
#import "SMS_SDK/SMS_SDK.h"
#import "SMS_SDK/CountryAndAreaCode.h"

@interface JRegisterViewController () <JRegisterServerDelegate>
{
    UIAlertView *_smsAlert;
}

//-(void)setTheLocalAreaCode;

@end

@implementation JRegisterViewController

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:SMS_SUCCESS_NOTIFICATION_NAME object:nil];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        //键盘的监听事件，获取高度
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(smsGetCodeSuccess) name:SMS_SUCCESS_NOTIFICATION_NAME object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self.passwordField setSecureTextEntry:YES];
    [self.surePasswordField setSecureTextEntry:YES];
    
    [self.registerScrollView whenTapped:^{
        for (UITextField *textField in self.registerScrollView.subviews) {
            [textField resignFirstResponder];
        }
        [self performSelector:@selector(keyboardWillHide:) withObject:nil];
    }];
    /*
    //设置本地区号
    [self setTheLocalAreaCode];
    //获取支持的地区列表
    [SMS_SDK getZone:^(enum SMS_ResponseState state, NSArray *array) {
        if (1==state)
        {
            NSLog(@"block 获取区号成功");
            //区号数据
            //_areaArray=[NSMutableArray arrayWithArray:array];
        }
        else if (0==state)
        {
            NSLog(@"block 获取区号失败");
        }
        
    }];*/
}
/*
-(void)setTheLocalAreaCode
{
    NSLocale *locale = [NSLocale currentLocale];
    
    NSDictionary *dictCodes = [NSDictionary dictionaryWithObjectsAndKeys:@"972", @"IL",
                               @"93", @"AF", @"355", @"AL", @"213", @"DZ", @"1", @"AS",
                               @"376", @"AD", @"244", @"AO", @"1", @"AI", @"1", @"AG",
                               @"54", @"AR", @"374", @"AM", @"297", @"AW", @"61", @"AU",
                               @"43", @"AT", @"994", @"AZ", @"1", @"BS", @"973", @"BH",
                               @"880", @"BD", @"1", @"BB", @"375", @"BY", @"32", @"BE",
                               @"501", @"BZ", @"229", @"BJ", @"1", @"BM", @"975", @"BT",
                               @"387", @"BA", @"267", @"BW", @"55", @"BR", @"246", @"IO",
                               @"359", @"BG", @"226", @"BF", @"257", @"BI", @"855", @"KH",
                               @"237", @"CM", @"1", @"CA", @"238", @"CV", @"345", @"KY",
                               @"236", @"CF", @"235", @"TD", @"56", @"CL", @"86", @"CN",
                               @"61", @"CX", @"57", @"CO", @"269", @"KM", @"242", @"CG",
                               @"682", @"CK", @"506", @"CR", @"385", @"HR", @"53", @"CU",
                               @"537", @"CY", @"420", @"CZ", @"45", @"DK", @"253", @"DJ",
                               @"1", @"DM", @"1", @"DO", @"593", @"EC", @"20", @"EG",
                               @"503", @"SV", @"240", @"GQ", @"291", @"ER", @"372", @"EE",
                               @"251", @"ET", @"298", @"FO", @"679", @"FJ", @"358", @"FI",
                               @"33", @"FR", @"594", @"GF", @"689", @"PF", @"241", @"GA",
                               @"220", @"GM", @"995", @"GE", @"49", @"DE", @"233", @"GH",
                               @"350", @"GI", @"30", @"GR", @"299", @"GL", @"1", @"GD",
                               @"590", @"GP", @"1", @"GU", @"502", @"GT", @"224", @"GN",
                               @"245", @"GW", @"595", @"GY", @"509", @"HT", @"504", @"HN",
                               @"36", @"HU", @"354", @"IS", @"91", @"IN", @"62", @"ID",
                               @"964", @"IQ", @"353", @"IE", @"972", @"IL", @"39", @"IT",
                               @"1", @"JM", @"81", @"JP", @"962", @"JO", @"77", @"KZ",
                               @"254", @"KE", @"686", @"KI", @"965", @"KW", @"996", @"KG",
                               @"371", @"LV", @"961", @"LB", @"266", @"LS", @"231", @"LR",
                               @"423", @"LI", @"370", @"LT", @"352", @"LU", @"261", @"MG",
                               @"265", @"MW", @"60", @"MY", @"960", @"MV", @"223", @"ML",
                               @"356", @"MT", @"692", @"MH", @"596", @"MQ", @"222", @"MR",
                               @"230", @"MU", @"262", @"YT", @"52", @"MX", @"377", @"MC",
                               @"976", @"MN", @"382", @"ME", @"1", @"MS", @"212", @"MA",
                               @"95", @"MM", @"264", @"NA", @"674", @"NR", @"977", @"NP",
                               @"31", @"NL", @"599", @"AN", @"687", @"NC", @"64", @"NZ",
                               @"505", @"NI", @"227", @"NE", @"234", @"NG", @"683", @"NU",
                               @"672", @"NF", @"1", @"MP", @"47", @"NO", @"968", @"OM",
                               @"92", @"PK", @"680", @"PW", @"507", @"PA", @"675", @"PG",
                               @"595", @"PY", @"51", @"PE", @"63", @"PH", @"48", @"PL",
                               @"351", @"PT", @"1", @"PR", @"974", @"QA", @"40", @"RO",
                               @"250", @"RW", @"685", @"WS", @"378", @"SM", @"966", @"SA",
                               @"221", @"SN", @"381", @"RS", @"248", @"SC", @"232", @"SL",
                               @"65", @"SG", @"421", @"SK", @"386", @"SI", @"677", @"SB",
                               @"27", @"ZA", @"500", @"GS", @"34", @"ES", @"94", @"LK",
                               @"249", @"SD", @"597", @"SR", @"268", @"SZ", @"46", @"SE",
                               @"41", @"CH", @"992", @"TJ", @"66", @"TH", @"228", @"TG",
                               @"690", @"TK", @"676", @"TO", @"1", @"TT", @"216", @"TN",
                               @"90", @"TR", @"993", @"TM", @"1", @"TC", @"688", @"TV",
                               @"256", @"UG", @"380", @"UA", @"971", @"AE", @"44", @"GB",
                               @"1", @"US", @"598", @"UY", @"998", @"UZ", @"678", @"VU",
                               @"681", @"WF", @"967", @"YE", @"260", @"ZM", @"263", @"ZW",
                               @"591", @"BO", @"673", @"BN", @"61", @"CC", @"243", @"CD",
                               @"225", @"CI", @"500", @"FK", @"44", @"GG", @"379", @"VA",
                               @"852", @"HK", @"98", @"IR", @"44", @"IM", @"44", @"JE",
                               @"850", @"KP", @"82", @"KR", @"856", @"LA", @"218", @"LY",
                               @"853", @"MO", @"389", @"MK", @"691", @"FM", @"373", @"MD",
                               @"258", @"MZ", @"970", @"PS", @"872", @"PN", @"262", @"RE",
                               @"7", @"RU", @"590", @"BL", @"290", @"SH", @"1", @"KN",
                               @"1", @"LC", @"590", @"MF", @"508", @"PM", @"1", @"VC",
                               @"239", @"ST", @"252", @"SO", @"47", @"SJ", @"963", @"SY",
                               @"886", @"TW", @"255", @"TZ", @"670", @"TL", @"58", @"VE",
                               @"84", @"VN", @"1", @"VG", @"1", @"VI", nil];
    
    NSString* tt=[locale objectForKey:NSLocaleCountryCode];
    NSLog(@"tt = %@",tt);
    
    NSString* defaultCode=[dictCodes objectForKey:tt];
    NSLog(@"defaultCode = %@",defaultCode);
    //_areaCodeField.text=[NSString stringWithFormat:@"+%@",defaultCode];
    
    NSString* defaultCountryName=[locale displayNameForKey:NSLocaleCountryCode value:tt];
    
    NSLog(@"defaultCountryName = %@",defaultCountryName);
    //_defaultCode=defaultCode;
    //_defaultCountryName=defaultCountryName;
}*/
/*
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    if (JCO.smsCode) {
        
    }
}*/

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//通知
- (void)smsGetCodeSuccess
{
    if (JCO.smsCode) {
        self.codeField.text = JCO.smsCode;
    }
}

- (void)back:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

- (void)backToLogin:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:^{
        [[NSNotificationCenter defaultCenter] postNotificationName:REGISTERTOLOGIN_NOTIFICATION_NAME object:nil];
    }];
}

//键盘事件
- (void)keyboardWillShow:(NSNotification *)notification
{
    if (self.registerScrollView.contentOffset.y == 0) {
        [self.registerScrollView setContentSize:CGSizeMake(self.registerScrollView.frame.size.width, self.registerScrollView.contentSize.height+DEFAULT_KEYBOARD_HEIGHT)];
        [self.registerScrollView setContentOffset:CGPointMake(0, 150.) animated:YES];
    }
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    if (self.registerScrollView.contentOffset.y != 0) {
        [self.registerScrollView setContentSize:CGSizeMake(self.registerScrollView.frame.size.width, self.registerScrollView.contentSize.height-DEFAULT_KEYBOARD_HEIGHT)];
    }
}

//获取验证码
- (IBAction)actionGetCode:(id)sender
{
    if (!self.phoneField.text || [self.phoneField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不能为空！"];
    }
    else if (![self.phoneField.text isPhoneNumber]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不符合！"];
    }
    else {
        [self.phoneField resignFirstResponder];
        J_LOG(@">>>获取验证码");
        //[[JMessageHUD shareInstance] showWithMessage:@"此功能暂未提供！"];
        
        NSString *message = [NSString stringWithFormat:@"我们将发送验证码短信到这个号码:%@", self.phoneField.text];
        _smsAlert = [[UIAlertView alloc] initWithTitle:@""
                                                      message:message
                                                     delegate:self
                                            cancelButtonTitle:@"取消"
                                            otherButtonTitles:@"确定", nil];
        [_smsAlert show];
    }
}

- (IBAction)actionRegister:(id)sender
{
    if (!self.phoneField.text || [self.phoneField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不能为空！"];
    }
    else if (![self.phoneField.text isPhoneNumber]) {
        [[JMessageHUD shareInstance] showWithMessage:@"手机号码不符合！"];
    }
    else if (!self.codeField.text || [self.codeField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"验证码不能为空！"];
    }
    else if (!self.passwordField.text || [self.passwordField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不能为空！"];
    }
    else if (![self.passwordField.text isConform]) {
        [[JMessageHUD shareInstance] showWithMessage:@"密码不符合！"];
    }
    else if (!self.surePasswordField.text || [self.surePasswordField.text isEqualToString:@""]) {
        [[JMessageHUD shareInstance] showWithMessage:@"请确认密码！"];
    }
    else if (![self.passwordField.text isEqualToString:self.surePasswordField.text]) {
        [[JMessageHUD shareInstance] showWithMessage:@"两次密码不一致！"];
    }
    else {
        JRegisterServer *server = [[JRegisterServer alloc] initWithDelegate:self];
        [server loadUsername:self.phoneField.text password:self.passwordField.text];
    }
}

- (IBAction)actionCancle:(id)sender
{
    [self performSelector:@selector(back:) withObject:nil];
}

- (void)loadRegisterModel:(JRegisterModel *)model
{
    if (model && model.return_code == 0) {
        JCO.username = self.phoneField.text;
        JCO.password = self.passwordField.text;
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:@"注册成功,是否登录？" delegate:self cancelButtonTitle:@"马上" otherButtonTitles:@"取消", nil];
        [alert show];
    }
    else {
        if (model.return_code == -1007) {
            [[JMessageHUD shareInstance] showWithMessage:@"该手机号已经注册过，请换一个！"];
        }
        else {
            [[JMessageHUD shareInstance] showWithMessage:@"注册失败，请检查信息是否符合！"];
        }
    }
}

- (void)loadRegisterFailed:(NSError *)error
{
    [[JMessageHUD shareInstance] showWithMessage:@"连接失败，请检查网络状况！"];
}

#pragma mark -JBaseServerDelegate
- (void)didLoadFromServer:(JBaseServer *)server {}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [alertView dismissWithClickedButtonIndex:0 animated:YES];
    
    if (buttonIndex == alertView.cancelButtonIndex) {
        if ([alertView isEqual:_smsAlert]) {
            
        }
        else {
            [self performSelector:@selector(backToLogin:) withObject:nil];
        }
    }
    else {
        if ([alertView isEqual:_smsAlert]) {//短信验证提示
            J_LOG(@">>>短信验证提示");
            VerifyViewController *verify = [[VerifyViewController alloc] init];
            //NSString* str2=[self.areaCodeField.text stringByReplacingOccurrencesOfString:@"+" withString:@""];
            [verify setPhone:self.phoneField.text AndAreaCode:@"86"];
            J_LOG(@"self.phoneField.text = %@", self.phoneField.text);
            //J_LOG(@"self.phoneField.text = %@", self.phoneField.text);
            
            [SMS_SDK getVerifyCodeByPhoneNumber:self.phoneField.text AndZone:@"86" result:^(enum SMS_GetVerifyCodeResponseState state) {
                
                J_LOG(@"state = %d", state);
                
                if (1 == state) {
                    J_LOG(@"block 获取验证码成功");
                    [self presentViewController:verify animated:YES completion:^{
                        ;
                    }];
                    
                }
                else if (0 == state)
                {
                    J_LOG(@"block 获取验证码失败");
                    NSString *str = [NSString stringWithFormat:NSLocalizedString(@"验证码发送失败 请稍后重试", nil)];
                    _smsAlert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"发送失败", nil) message:str delegate:self cancelButtonTitle:NSLocalizedString(@"确定", nil) otherButtonTitles:nil, nil];
                    [_smsAlert show];
                }
                else if (SMS_ResponseStateMaxVerifyCode == state)
                {
                    J_LOG(@"block 请求验证码超上限");
                    NSString *str = [NSString stringWithFormat:NSLocalizedString(@"请求验证码超上限 请稍后重试", nil)];
                    _smsAlert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"maxcode", nil) message:str delegate:self cancelButtonTitle:NSLocalizedString(@"确定", nil) otherButtonTitles:nil, nil];
                    [_smsAlert show];
                }
                else if (SMS_ResponseStateGetVerifyCodeTooOften == state)
                {
                    J_LOG(@"block 客户端请求发送短信验证过于频繁");
                    NSString *str = [NSString stringWithFormat:NSLocalizedString(@"客户端请求发送短信验证过于频繁", nil)];
                    _smsAlert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"提示", nil) message:str delegate:self cancelButtonTitle:NSLocalizedString(@"确定", nil) otherButtonTitles:nil, nil];
                    [_smsAlert show];
                }
            }];
        }
        else {
            [self performSelector:@selector(back:) withObject:nil];
        }
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
