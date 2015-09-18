<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="page-view-size" content="640*526"/>
<title>vod play</title>
<style type="text/css">
#exit_tips_text{
	font-size: 18px;
	color: #250B36;
}
.text16{
	font-size: 16px;
	color: #250B36;
}
.text18{
	font-size: 18px;
	color: #250B36;
}
.text20{
	font-size: 20px;
	color: #250B36;
}
.text22{
	font-size: 22px;
	color: #250B36;
}
.text24{
	font-size: 24px;
	color: #250B36;
}
.error_bg{
	background-image:url(vodImages.gif);
	background-position: 0px -158px;
}
.network_error{
	background-image:url(vodImages.gif);
	background-position: -465px -245px;
}
.ad_forward{
	background-image:url(vodImages.gif);
	background-position: -443px -120px;
}
.left_bg{
	background-image:url(vodImages.gif);
	background-position: -454px 0px;
}
.middle_bg{
	background-image:url(vodImages.gif);
	background-position: 0px -437px;
}
.right_bg{
	background-image:url(vodImages.gif);
	background-position: -470px 0px;
}
.backward{
	background-image:url(vodImages.gif);
	background-position: -506px 0px;
}
.forward{
	background-image:url(vodImages.gif);
	background-position: -493px 0px;
}
.wasulogo{
	background-image:url(vodImages.gif);
	background-position: -463px -74px;
}
.seekBar_bg{
	background-image:url(vodImages.gif);
	background-position: 0px -313px;
}
.marquee_bg{
	background-image:url(vodImages.gif);
	background-position: 0px -138px;
}
.pause1{
	background-image:url(vodImages.gif);
	background-position: -524px 0px;
}
.continue-play1{
	background-image:url(vodImages.gif);
	background-position: -194px 0px;
}
.continue-play2{
	background-image:url(vodImages.gif);
	background-position: -194px -33px;
}
.hidden-ad1{
	background-image:url(vodImages.gif);
	background-position: -299px 0px;
}
.hidden-ad2{
	background-image:url(vodImages.gif);
	background-position: -299px -33px;
}
.yes{
	background-image:url(vodImages.gif);
	background-position: 0px 0px;
}
.yes1{
	background-image:url(vodImages.gif);
	background-position: 0px -39px;
}
.no{
	background-image:url(vodImages.gif);
	background-position: -92px 0px;
}
.no1{
	background-image:url(vodImages.gif);
	background-position: -92px -39px;
}
.volume_value{
	background-image:url(vodImages.gif);
	background-position: 0px -81px;
}
.left_jd{
	background-image:url(vodImages.gif);
	background-position: -416px 0px;
}
.middle_jd{
	background-image:url(vodImages.gif);
	background-position: 0px -428px;
}
.right_jd{
	background-image:url(vodImages.gif);
	background-position: -432px 0px;
}
.volume{
	background-image:url(vodImages.gif);
	background-position: 0px -105px;
}
</style>
<script>
/***********************iPanel.enterMode(mode,mask)ARGUMENTS***********/
var EPG_MASK_ACTUAL_PF			= 0x01;
var EPG_MASK_ACTUAL_SCHEDULE 	= 0x02;
var EPG_MASK_OTHER_PF			= 0x04;
var EPG_MASK_OTHER_SCHEDULE		= 0x08;
var EPG_MASK_CURRENT_TS			= 0x00;
var EPG_MASK_ALL_TS				= 0x10;

var EPG_MODE_WATCHTV 			= "watchTV";
var EPG_MODE_EPG				= "EPG";
var EPG_MODE_NVOD				= "NVOD";
var EPG_MODE_SEARCHPROGRAM		= "searchProgram";

function Event(code, args){
	this.code = code;
	this.args = args;
}

var iPanelReturnFlag = false;


document.onsystemevent = grabEvent;
document.onkeypress = grabEvent;
document.onirkeypress = grabEvent;
function grabEvent(event){
	iPanelReturnFlag = false;
	var keycode = event.which;
	iPanel.debug("[vod_key_event]---keycode = " + keycode);
	if(keycode < 58 && keycode > 47){//数字键
		var args = new Object();
		args.value = keycode - 48;
		event_handler(new Event("NUMERIC", args));
		if(iPanelReturnFlag) return 1;
		else return 0;
	}
	else{
		var code = null;
		var args = new Object();
		args.modifiers = 0;
		args.value = keycode;
		switch(keycode){
			case 1://up
				code = "UP";
				break;
			case 2://down
				code = "DOWN";
				break;
			case 3://left
				code = "LEFT";
				break;
			case 4://right
				code = "RIGHT";
				break;
			case 13://enter
				code = "SELECT";
				break;
			case 339:
				code = "EXIT";
				break;
			case 340://后退（back）
				code = "BACK";
				break;
			case 372://page up
			case 1029:
				code = "BACKWARD";//快退
				break;
			case 373://page down
			case 1028:
				code = "FORWARD";//快进
				break;
			case 561:
			case 565:
				code="LANGUAGE";//中/英（语言）
				break;
			case 1038://切换
				code="TV_SYSTEM";
				iPanelReturnFlag = false;
				break;
			case 567://信息
				code = "INFO";
				break;
			case 595://音量加
				code = "VOLUME_UP";
				break;
			case 596://音量减
				code = "VOLUME_DOWN";
				break;
			case 597://静音
				code = "VOLUME_MUTE";
				break;
			case 832://red
				code = "RED";
				break;
			case 833://green
				code = "GREEN";
				break;
			case 834://yellow
				code = "YELLOW";
				break;
			case 835://blue
				code = "BLUE";
				break;
			case 8330:
				code = "DVB_PROGRAM_READY_OPEN";
				iPanelReturnFlag = false;
				break;
			case 8001:	//这个消息不能去掉，如果进入IPQAM点播没有插cable，会导致不能播放
			case 5550:
				code = "DVB_CABLE_CONNECT_SUCCESS";
				iPanel.setGlobalVar("cable_connect_success", "1");
				iPanel.setGlobalVar("init_cable","1");
				break;
			case 8002:		
			case 5551:
				code = "DVB_CABLE_CONNECT_FAILED";
				iPanel.setGlobalVar("cable_connect_success", "0");
				break;
			case 5350://Ca_message_open
				code = "CA_MESSAGE_OPEN";
				 args.modifiers = event.modifiers;
				break;
			case 5351://Ca_message_close
				code = "CA_MESSAGE_CLOSE";
				iPanel.delGlobalVar("ca_message_type");
				break;
			case 5202:
				code = "EIS_VOD_PREPAREPLAY_SUCCESS";
				break;
			case 5203:
				code = "EIS_VOD_CONNECT_FAILED";
				break;
			case 5204:
				code = "EIS_VOD_DVB_SERACH_FAILED";
				break;
			case 5205:
				code = "EIS_VOD_PLAY_SUCCESS";
				break;
			case 5206:
				code = "EIS_VOD_PLAY_FAILED";
				break;
			case 5209:
				code = "EIS_VOD_PROGRAM_BEGIN";
				break;
			case 5210:
				code = "EIS_VOD_PROGRAM_END";
				break;
			case 5211:
				code="EIS_VOD_CLOSE_SUCCESS";
				break;
			case 5212:
				code="EIS_VOD_CLOSE_FAILED";
				break;
			case 5213:
				code = "EIS_VOD_TSTVREQUEST_FAILED";
				break;
			case 5500:
				code="EIS_IP_NETWORK_CONNECT";
				iPanel.setGlobalVar("netlink_status","1");
				//iPanelReturnFlag = true;//一般让此消息流到eventFrame处理
				break;
			case 5501:
				code="EIS_IP_NETWORK_DISCONNECT";
				iPanel.setGlobalVar("netlink_status","0");
				//iPanelReturnFlag = true;//一般让此消息流到eventFrame处理
				break;
			case 5502:
				code="EIS_IP_NETWORK_READAY";
				//iPanelReturnFlag = true;//一般让此消息流到eventFrame处理
				break;
			case 5222://底层开始缓冲数据，等待播放
				code="EIS_VOD_START_BUFF";
				break;
			case 5223://缓冲数据结束，已开始播放
				code="EIS_VOD_STOP_BUFF";
				break;
			case 5224://传入的时间超出有效范围
				code="EIS_VOD_OUT_OF_RANGE";
				break;
			case 5225://用户自定义错误
				code = "EIS_VOD_USER_EXCEPTION";
				args.modifiers = event.modifiers;
				break;
			case 5207://收取servcie时移节目列表成功
				code = "EIS_VOD_PREPARE_PROGRAMS_SUCCESS";
				args.modifiers = event.modifiers;
				break;
			case 5221://VOD启动失败
				code="EIS_VOD_START_FAILED";
				args.modifiers = event.modifiers;
				break;
			case 5972://进入待机状态消息
				code = "EIS_MISC_ENTER_STANDBY";
				media.AV.close();
				//iPanel.enterStandby();
				iPanel.mainFrame.location.href = "ui://standby.htm";
				break;
			case 258://standay
				media.AV.close();
				//iPanel.enterStandby();
				iPanel.mainFrame.location.href = "ui://standby.htm";
				return 0;
				break;
			case 4012: //EIS_STDMESSAGE_SOCKET_CONNECT_FAILED SOCKET连接失败
			case 4404: //EIS_STDMESSAGE_NOT_FOUND 无法找到指定位置的资源
			case 4405:
			case 4408: //EIS_STDMESSAGE_REQUEST_TIMEOUT 在服务器许可的等待时间内，客户一直没有发出任何请求。客户可以在以后重复同一请求。
				iPanel.debug("4404 p2="+p2);
				var p2 = event.modifiers;
				if (p2 == 1400)
				{
					return;
				}

				/*sunny change*/
				if (ip_flag == 1)
				{
					clearTimeout(ip_timeout);
					iPanel.eventFrame.flag = 1;
					window.location.href = "ui://index.htm";
				}
				else
				{
					iPanel.setGlobalVar("page_not_found_status",1);
					iPanel.overlayFrame.location.href = "ui://page_not_found.htm";
				}
				return 0;
				break;
			case 4021:
				/*sunny change*/
				if (ip_flag == 1)
				{
					clearTimeout(ip_timeout);
					iPanel.eventFrame.flag = 1;
					window.location.href = "ui://index.htm";
				}
				else
				{
					iPanel.setGlobalVar("page_not_found_status",1);
					iPanel.overlayFrame.location.href = "ui://page_not_found.htm?ip地址设置错误";
				}
				return 0;
				break;
		}
		if(code){
			event_handler(new Event(code, args));
		}
		if(iPanelReturnFlag) return 1;
		else return 0;
	}
}


function check_error_code(error_code)
{
	switch (error_code)
	{
	case 201:
		return "已经创建";//已经创建
	     break;
	case 250:
		return "存储空间不足";//存储空间不足
	     break;
	case 300:
		return "多种选择";//多种选择
	     break;
	case 301:
		return "永久转向";//永久转向
	     break;
	case 302:
		return "临时转向";//临时转向
	     break;
	case 303:
		return "查看其它位置"; //查看其它位置
	     break;
	case 304:
		return "没有改变"; //没有改变
	     break;
	case 305:
		return "使用代理"; //使用代理
	     break;
	case 400:
		return "错误请求"; //错误请求
	     break;
	case 401:
		return "未授权"; //未授权
	     break;
	case 402:
		return "需要付费"; //需要付费
	     break;
	case 403:
		return "禁止访问"; //禁止访问
	     break;
	case 404:
		return "未找到"; //播放源未找到
	     break;
	case 405:
		return "不允许的方法"; //不允许的方法
	     break;
	case 406:
		return "无法接受"; //无法接受
	     break;
	case 407:
		return "代理需要授权"; //代理需要授权
	     break;
	case 408:
		return "请求超时"; //请求超时
	     break;
	case 410:
		return "连接丢失"; //连接丢失
	     break;
	case 411:
		return "需要长度"; //需要长度
	     break;
	case 412:
		return "前提条件未满足"; //前提条件未满足
	     break;
	case 413:
		return "请求实体过大"; //请求实体过大
	     break;
	case 414:
		return "请求的URI太长"; //请求的URI太长
	     break;
	case 415:
		return "不支持的媒体类型";//不支持的媒体类型
	     break;
	case 451:
		return "无效参数"; //无效参数
	     break;
	case 452:
		return "找不到会话"; //找不到会话？？？
	     break;
	case 453:
		return "带宽不足"; //带宽不足
	     break;
	case 454:
		return "找不到会话"; //找不到会话
	     break;
	case 455:
		return "目前状态下该方法无效"; //目前状态下该方法无效
	     break;
	case 456:
		return "资源头无效"; //资源头无效
	     break;
	case 457:
		return "超出范围"; //超出范围
	     break;
	case 458:
		return "参数只读"; //参数只读
	     break;
	case 459:
		return "不允许聚合"; //不允许聚合
	     break;
	case 460:
		return "仅允许聚合"; //仅允许聚合
	     break;
	case 461:
		return "不支持的传输"; //不支持的传输
	     break;
	case 462:
		return "目标无法到达"; //目标无法到达
	     break;
	case 500:
		return "服务器内部错误"; //服务器内部错误
	     break;
	case 501:
		return "未实现"; //未实现
	     break;
	case 502:
		return "错误的网关"; //错误的网关
	     break;
	case 503:
		return "服务不可用"; //服务不可用
	     break;
	case 504:
		return "网关超时"; //网关超时
	     break;
	case 505:
		return "版本不支持"; //版本不支持
	     break;
	case 551:
		return "选项不支持"; //选项不支持
	     break;
	case 2101:
		return "读取机顶盒号失败";
		break;
	case 2102:
		return "无效的播放参数";
		break;
	case 2103:
		return "缓冲数据超时";
		break;
	case 2104:
		return "流时间戳异常，无法修复";
		break;
	case 2105:
		return "不能连接服务器";
		break;
	case 2106:
		return "连接异常，断开会话";
		break;
	case 2201:
		return "管理员踢除";
		break;
	case 2202:
		return "播放源错误";
		break;
	case 2203:
		return "会话连接错误";
		break;
	case 2204:
		return "登陆错误";
		break;
	case 2205:
		return "传输错误";
		break;
	case 2206:
		return "信道不足";
		break;
	case 2207:
		return "会话代理错误";
		break;
	case 2208:
		return "启动超时";
		break;
	case 2209:
		return "服务器返回错误：暂停超时";
		break;
	case 2210:
		return "退出超时";
		break;
	case 2211:
		return "保活超时";
		break;
	case 2212:
		return "播放结束";
		break;
	case 2401:
		return "连接错误";
		break;
	case 4000:
		return "服务器返回错误：暂停超时";
	     break;
	case 4201:
		return "服务器返回错误：已创建";
	     break;
	case 4250:
		return "服务器返回错误：存储空间低";
	     break;
	case 4300:
		return "服务器返回错误：多种选择";
	     break;
	case 4301:
		return "服务器返回错误：永久定向";
	     break;
	case 4302:
		return "服务器返回错误：临时定向";
	     break;
	case 4303:
		return "服务器返回错误：参见其他";
	     break;
	case 4305:
		return "服务器返回错误：使用代理";
	     break;
	case 4400:
		return "服务器返回错误：无效请求";
	     break;
	case 4401:
		return "服务器返回错误：未经授权"; 
	     break;
	case 4402:
		return "服务器返回错误：要求付费";
	     break;
	case 4403:
		return "服务器返回错误：禁止";
	     break;
	case 4404:
		return "服务器返回错误：未找到";
	     break;
	case 4405:
		return "服务器返回错误：方法不允许";
	     break;
	case 4406:
		return "服务器返回错误：不可接受的";
	     break;
	case 4407:
		return "服务器返回错误：要求代理认证";
	     break;
	case 4408:
		return "服务器返回错误：请求超时";
	     break;
	case 4410:
		return "服务器返回错误：消失";
	     break;
	case 4411:
		return "服务器返回错误：所需长度";
	     break;
	case 4412:
		return "服务器返回错误：前提条件失败";
	     break;
	case 4413:
		return "服务器返回错误：请求实体太大";
	     break;
	case 4414:
		return "服务器返回错误：请求URI太长";
	     break;
	case 4415:
		return "服务器返回错误：不支持的媒体类型";
	     break;
	case 4451:
		return "服务器返回错误：参数不理解";
	     break;
	case 4452:
		return "服务器返回错误：会议未发现";
	     break;
	case 4453:
		return "服务器返回错误：没有足够的带宽";
	     break;
	case 4454:
		return "服务器返回错误：未找到会话";
	     break;
	case 4455:
		return "服务器返回错误：方法不适用于该国";
	     break;
	case 4456:
		return "服务器返回错误：头场无效资源";
	     break;
	case 4457:
		return "服务器返回错误：无效范围";
	     break;
	case 4458:
		return "服务器返回错误：参数只读";
	     break;
	case 4459:
		return "服务器返回错误：不允许的选项";
	     break;
	case 4460:
		return "服务器返回错误：只允许总结选项";
	     break;
	case 4461:
		return "服务器返回错误：不支持的传输";
	     break;
	case 4462:
		return "服务器返回错误：目标不可达";
	     break;
	case 4500:
		return "服务器返回错误：内部服务器错误";
	     break;
	case 4501:
		return "服务器返回错误：未实现";
	     break;
	case 4502:
		return "服务器返回错误：错误网关";
	     break;
	case 4503:
		return "服务器返回错误：服务不可用";
	     break;
	case 4504:
		return "服务器返回错误：网关超时";
	     break;
	case 4505:
		return "服务器返回错误：不支持的rtsp版本";
	     break;
	case 4551:
		return "服务器返回错误：该选项不支持";
	     break;
	default:
		return "连接失败";  //未知错误
	     break;
	}
}
</script>
<script language="javascript">
var version = "hangzhou_mp2_20130617_01";
ipanel.debug("version = "+version);
	
hardware.STB.frontPanel(2,"0000",0,0,100);
hardware.STB.statusLight(2,0,0,100);//显示数据载入指示灯
hardware.STB.statusLight(31,0,0,100);//显示vod指示灯
hardware.STB.statusLight(30,0,0,0);//灭掉浏览器指示灯
iPanel.misc.enableIconLoading=0;
iPanel.overlayFrame.close();

/*****************************解析广告/点播rtsp*********************************/
/*
	插入位置：(start 片头  middle 片中 end 片尾 vedio 正片)
	插入时间：600 (单位S)
	视频时长：30  (单位S) 
	播放地址：play-url
*/
//var adVodUrl = "vedio,正片,时长,rtsp://125.210.188.13:555/san01/1/m06090900030000001_z.mp4;start,0,30,rtsp://125.210.188.13:555/san01/1/f720kfctjjt_30.mp4;middle,30,30,rtsp://125.210.188.13:555/san01/1/f720kfctjjt_30.mp4";
//var adVodUrl = "vedio,正片,时长,rtsp://125.210.188.13:1555/san01/1/2010112234291096.ts;start,0,30,rtsp://125.210.188.13:1555/san01/1/f720kfc01.ts;middle,60,30,rtsp://125.210.188.13:1555/san01/1/f720kfc01.ts;";
//var adVodUrl = iPanel.getGlobalVar("ads-url");
//rtsp://125.210.227.234:5541/mpeg2_ipqam/icms_icds_pub03/opnewsps03/Video/2013/10/07/14/20131107015340_haixiaoqijijianji_750185230_750193176.ts?Contentid=CP23010020131104101623&isHD=0&isIpqam=1&token=A309F58BB207F34E862D05BDD28117F419432618EF58CEABC8EBF359B550288F0CD7FF79912BC4C2446537D395ADB8E38D78F48D1D1340C47484F1A34249FDB44C4A2FB137D67B10052BB63D3BC35EB39C707AD81DDFB52D1164998DEFA9C4EE382B6DD98919AF4846635DCFA60E143F1DA5DA2C8F35B48C60BB4DF310C57D4C59CEEDF56FFE73A39A15DEC9819438274B1C9BDC8F40C5B9D836356879A257AC299A872491A46DDFA5FAF403A06B1B47ECCAAB3E7E6596DC61CD30E6121135CEE72BE97564D4E266A060EF494B7D581C78C1C60C51B7926589130A3A621A2D3AE25C4990608387&jishu=0&assetId=125449846&assetType=36&folderCode=213900&programId=125449846&vip=0&movieId=125450862&conVod=true
//var tempUrl = adVodUrl.split(";");
var startAdDuration = 0;
var startAdUrl = "";
var middleAdDuration = 0;
var middleAdInsertTime = 0;
var middleAdUrl = "";
var vedioUrl = "rtsp://125.210.227.234:5541/mpeg2_ipqam/icms_icds_pub03/opnewsps03/Video/2013/10/07/14/20131107015340_haixiaoqijijianji_750185230_750193176.ts?Contentid=CP23010020131104101623&isHD=0&isIpqam=1&token=A309F58BB207F34E862D05BDD28117F419432618EF58CEABC8EBF359B550288F0CD7FF79912BC4C2446537D395ADB8E38D78F48D1D1340C47484F1A34249FDB44C4A2FB137D67B10052BB63D3BC35EB39C707AD81DDFB52D1164998DEFA9C4EE382B6DD98919AF4846635DCFA60E143F1DA5DA2C8F35B48C60BB4DF310C57D4C59CEEDF56FFE73A39A15DEC9819438274B1C9BDC8F40C5B9D836356879A257AC299A872491A46DDFA5FAF403A06B1B47ECCAAB3E7E6596DC61CD30E6121135CEE72BE97564D4E266A060EF494B7D581C78C1C60C51B7926589130A3A621A2D3AE25C4990608387&jishu=0&assetId=125449846&assetType=36&folderCode=213900&programId=125449846&vip=0&movieId=125450862&conVod=true";
/*for(var i=0; i<tempUrl.length; i++){
	var item = tempUrl[i].split(",");
	if(item[0] == "vedio"){
		vedioUrl = item[3];
	}else if(item[0] == "start"){
		startAdDuration = parseInt(item[2]);
		startAdUrl = item[3];
	}else if(item[0] == "middle"){
		middleAdInsertTime = parseInt(item[1]);
		middleAdDuration = parseInt(item[2]);
		middleAdUrl = item[3];
	}
}*/
var middleAdEndSeekTime = "";
var middleAdEndFlag = false;
var isAdPlay = true;
var adPlayError = false;
var checkAdElapsedTimeFlag = -1;
var checkVedioElapsedTimeFlag = -1;
var adFlag = "start";
var adDuration = 0;
/******************************************************************************/

var mute_status = (typeof(iPanel.getGlobalVar("mute_status")) != "undefined" && iPanel.getGlobalVar("mute_status")!= '') ? iPanel.getGlobalVar("mute_status") : '0'; //0表示未静音，1表示静音
var ethernetItem = network.ethernets[0];
var net_status = ethernetItem.LANStatus; //1：表示目前处于连接状态   0：表示没有连接上网络
if(net_status==0||(net_status!=3&&network.ethernets[0].DHCPEnable==1)){
	net_status="0";
}else{
	net_status="1";
}
var cable_connect_flag = iPanel.getGlobalVar("cable_connect_success");
if(cable_connect_flag){
	cable_connect_flag="1";
}
var vod_mode;
var wasu_url=iPanel.getGlobalVar("url_parm");
var rtsp_url = "";

var link_tips=true;
var can_volume=true;
var can_ward=true;
var can_info=true;
var currOriginVolume = media.sound.value;
var currVolume = currOriginVolume;
var has_set_sound=false;

var ward_visible=false;
var ward_timeout=-1;
var volumeBarVisible=false;
var volumeBarTimeout = -1;
//var exit_tips_visible=false;
var pause_tips_visible=false;
var network_tips_visible=false;
var network_timeout=-1;
var f3_visible=false;
var f3_timeout=-1;
var info_visible=false;
var info_timeout=-1;
var information_visible = false;
var information_timeout = -1;
var exit_remind_visible = false;

var first_play=true;
var connect_failed=false;
var has_stop=false;

var butPosition = 1;
var duration = 0;
var elapsed_time=0;
var is_now_seek=false;
var seek_flag=false;//判断是不是处于调出进度条并按下确定键后到缓冲提示弹出之前的状态之中
var continuePlayFlag = true;	//是否继续播放。默认暂停
var time_position=0;
var time_array=new Array();
var show_front_time_timeout=-1;

var NETWORK_CONNECT=0;
var NETWORK_DISCONNECT=0;
var load_page_timeout = -1;
var socket_error = false;//当拔网线超时后会发送一个5225消息，p2为2106，收到此消息后应用关闭vod并重新建立连接，这个变量用来记录此状态
var socket_error_seek_time = "";
var check_net_status_timeout = -1;
var loadAdRequest;//回传广告请求


function loadAd(adType){
	var hVersion = hardware.STB.hVersion;
	var sVersion = hardware.STB.sVersion;
	//var ad = document.getElementById('wasu_ad');
	var STBID = hardware.STB.serial;
	//ad.src = "http://218.108.243.4:9090/a.html?ad="+adType+"&type=1&stb=" + STBID;*/
	var loadAdURL="http://218.108.243.4:9090/a.html?ad="+adType+"&type=1&stb=" + STBID;
	iPanel.debug("loadAdURL=="+loadAdURL);
	//if(net_status!="0"||(hVersion=="100130C9"&&sVersion=="15710151")||(hVersion=="100130c9"&&sVersion=="05710138")||(hVersion=="100130c9"&&sVersion=="05710148")||sVersion=="00.00.00.02")){	//无网络不回传数据
	if(net_status!="0"){	//无网络不回传数据
		//loadAdRequest.onreadystatechange = readyGetText;
		if(hVersion=="100130c9"&&sVersion=="15710151"){
			
		}else if(hVersion=="100130c9"&&sVersion=="05710138"){
			
		}else if(hVersion=="100130c9"&&sVersion=="05710148"){
			
		}else if(sVersion=="00.00.00.02"){
			
		}else if(isAdPlay==true){
			
		}else{
			loadAdRequest = new XMLHttpRequest();
			loadAdRequest.open('GET',loadAdURL,true);
			loadAdRequest.send(null);
		}

		iPanel.debug("loadAdRequest is call end.");
	}
}
function event_handler(event_obj){
//	alert("1111---"+event_obj.code);
	var key_value=event_obj.args.value;
	if(connect_failed&&(event_obj.code!="EXIT"&&event_obj.code!="EIS_IP_NETWORK_CONNECT")) return;
	if((link_tips&&event_obj.code=="EXIT")||(link_tips&&event_obj.code=="BACK")){
		if(typeof(wasu_url)!="undefined"&&wasu_url!=""){
      		return;
		}
		has_stop=true;
		exit_page();
		return;
	}
	if(link_tips&&key_value<5000) return;
	if(seek_flag&&event_obj.code=="SELECT"&&exit_remind_visible){//正在缓冲时按退出后再按确定需要在这里处理，否则按键会被拦截
		if(butPosition){
			has_stop=true;
			close_widget();
			hide_pause_tips();
			media.AV.close();
			iPanel.overlayFrame.location.href="thank_look.htm";		
		}else{
			hide_exitRemind();	
		}
		return 0;
	}
	if(seek_flag&&event_obj.code=="SELECT") return;//调出进度条并按下确定键后到缓冲提示弹出之前不响应确定键
	if((seek_flag||is_now_seek)&&key_value!=339&&(key_value==5222||key_value<5000)) return;//正在seek之中时只响应退出键和消息事件,不响应缓冲开始的消息
	if (has_stop&&key_value>5000&&event_obj.code!="EIS_IP_NETWORK_CONNECT") return;//页面调用media.AV.stop()后就不再响应任何消息事件
	if (has_stop&&(key_value==567||key_value==339)) return;//调用stop后就不响应信息键、退出键
	iPanelReturnFlag = false;
	switch(event_obj.code){
		case "VOLUME_MUTE":
			mute_status = (typeof(iPanel.getGlobalVar("mute_status")) != "undefined" && iPanel.getGlobalVar("mute_status") != '') ? iPanel.getGlobalVar("mute_status") : '0'; 
			if (mute_status == "0"){
					show_mute_icon();
			}else if (mute_status == "1"){
				hide_mute_icon();
			}
			iPanelReturnFlag = false;
			break;
		case "VOLUME_UP":
			if(can_volume&&!f3_visible){
				volumeAdjust(5);	
			}
			iPanelReturnFlag = false;
			break;
		case "VOLUME_DOWN":
			if(can_volume&&!f3_visible){
				volumeAdjust(-5);		
			}
			iPanelReturnFlag = false;
			break;
		case "LEFT":
			if(pause_tips_visible&&!socket_error){//显示广告条的时候 update fengjh
				$("continue-play").style.class="continue-play2";
				$("hidden-ad").style.class="hidden-ad1";
				continuePlayFlag = true;
			}else{
				if(exit_remind_visible){
					if(butPosition){
						butPosition = 0;
						$("yesPosition").style.class = "yes1";		
						$("noPosition").style.class = "no";			
					}else{
						butPosition = 1;
						$("yesPosition").style.class = "yes";		
						$("noPosition").style.class = "no1";
					}	
				}else if(can_volume&&!f3_visible){
					volumeAdjust(-5);
				}else if(f3_visible){
					window.clearTimeout(f3_timeout);
					change_time_focus(-1);
					f3_timeout=window.setTimeout("hide_f3_tips()",10000);
				}
			}
			break;
		case "RIGHT":
			if(pause_tips_visible&&!socket_error){//显示广告条的时候 update fengjh
				$("continue-play").style.class="continue-play1";
				$("hidden-ad").style.class="hidden-ad2";
				continuePlayFlag = false;
			}else{
				if(exit_remind_visible){
					if(butPosition){
						butPosition = 0;
						$("yesPosition").style.class = "hidden";		
						$("noPosition").style.class = "no";			
					}else{
						butPosition = 1;
						$("yesPosition").style.class = "yes";		
						$("noPosition").style.class = "no1";
					}	
				}else if(can_volume&&!f3_visible){
					volumeAdjust(5);			
				}else if (f3_visible){
					window.clearTimeout(f3_timeout);
					change_time_focus(1);
					f3_timeout=window.setTimeout("hide_f3_tips()",10000);
				}
			}
			break;
		case "UP":
			if(f3_visible&&!network_tips_visible){
				window.clearTimeout(f3_timeout);
				set_time(1);
				f3_timeout=window.setTimeout("hide_f3_tips()",10000);
			}
			break;
		case "DOWN":
			if(f3_visible&&!network_tips_visible){
				window.clearTimeout(f3_timeout);
				set_time(-1);
				f3_timeout=window.setTimeout("hide_f3_tips()",10000);
			}	
			break;
		case "NUMERIC":
			if(f3_visible){
				window.clearTimeout(f3_timeout);
				set_time1(event_obj.args.value);
				change_time_focus(1);
				f3_timeout=window.setTimeout("hide_f3_tips()",10000);
			}
		    break;
		case "FORWARD"://快进
		  if(isAdPlay) return;
			if(typeof(wasu_url)!="undefined"&&wasu_url!=""){
				return;
			}
			if(net_status=="0"&&vod_mode=="DVB"){
				$("tips_text").innerText="请插上网线";
				show_tips();
				return;
			}
			if(can_ward&&!f3_visible&&media.AV.status!="pause"&&!socket_error){
				if(volumeBarVisible) hideVolumeBar();
				if (ward_visible){
					window.clearTimeout(ward_timeout);
					if(elapsed_time>=duration){
						ward_timeout=window.setTimeout("hide_ward()",5000);
					}else{
						elapsed_time+=60;
						if (elapsed_time>duration) elapsed_time=duration;
						write_elapsed_time(elapsed_time);
						show_ward();
					}
				}else{
					elapsed_time=media.AV.elapsed;
					if(!elapsed_time) elapsed_time=0;
					write_elapsed_time(elapsed_time);
					show_ward();
				}
			}
			break;
		case "BACKWARD"://快退
			if(isAdPlay) return;
			if(typeof(wasu_url)!="undefined"&&wasu_url!=""){
				return;
			}
			if (net_status=="0"&&vod_mode=="DVB"){
				$("tips_text").innerText="请插上网线";
				show_tips();
				return;
			}
			if (can_ward&&!f3_visible&&media.AV.status!="pause"&&!socket_error){
				if(volumeBarVisible) hideVolumeBar();
				if (ward_visible){
					window.clearTimeout(ward_timeout);
					if(elapsed_time<=0){
						ward_timeout=window.setTimeout("hide_ward()",5000);
					}else{
						elapsed_time-=60;
						if (elapsed_time<0) elapsed_time=0;
						write_elapsed_time(elapsed_time);
						show_ward();
					}
				}else{
					elapsed_time=media.AV.elapsed;
					if(!elapsed_time) elapsed_time=0;
					write_elapsed_time(elapsed_time);
					show_ward();
				}
			}
			break;
		case "SELECT":	  
			if (typeof(wasu_url)!="undefined"&&wasu_url!=""){
				var my_wasu_widget  = iPanel.pageWidgets.getByName("wasu_widget");
				var widget_url = my_wasu_widget.location.href;
				if(widget_url.indexOf("key=0") == -1){
					return 0;
				}
			}
			if(network_tips_visible){
				has_stop=true;
				hide_exit();
				close_widget();
				hide_pause_tips();
				media.AV.close();
				exit_page();
			}else if(ward_visible&&!socket_error){
				window.clearTimeout(ward_timeout);
				hide_ward();
				if(elapsed_time>=duration){
					has_stop=true;
					close_widget();
					media.AV.close();
					iPanel.overlayFrame.location.href="thank_look.htm";
					return;
				}
				var hour=Math.floor(elapsed_time/3600);
				var minute=Math.floor((elapsed_time-hour*3600)/60);
				var second=elapsed_time-hour*3600-minute*60;
				hour=hour>9?hour:"0"+hour;
				minute=minute>9?minute:"0"+minute;
				second=second>9?second:"0"+second;
				var goto_time=hour+":"+minute+":"+second;
				media.AV.seek(goto_time);
				seek_flag=true;
			}else if(exit_remind_visible){
				if(butPosition){
					has_stop=true;
					close_widget();
					hide_pause_tips();
					media.AV.close();
					iPanel.overlayFrame.location.href="thank_look.htm";
				}else{
					if(net_status=="0"&&vod_mode=="DVB"){
						var remindText = "网络异常,插上网线继续播放,“确认”键退出！";
						show_exit(remindText);
						network_tips_visible = true;
						return;
					}
					if(!socket_error){
						media.AV.play();
					}
					seek_flag = false;
				}
				hide_exitRemind();
			}else if(!volumeBarVisible&&!exit_remind_visible&&!info_visible&&!network_tips_visible&&!f3_visible){
				/***liyq add 20110322走cable的点播，在拔掉网线的时候，就不响应暂停和取消暂停的操作了，避免出问题***/
				if(net_status=="0"&&vod_mode=="DVB"){
					$("tips_text").innerText="请插上网线";
					show_tips();
					return;
				}
				if(pause_tips_visible&&!socket_error){
					if(continuePlayFlag){//隐藏广告，继续播放
						hide_pause_tips();
						media.AV.play();
						show_front_play_status();     //修改vod暂停播放后面板灯显示
						//continuePlayFlag=true;
					}else{	//隐藏广告至右上方，暂停播放						
						hide_pause_tips();
						show_rightHiddenAd();	
					}
				}else if(media.AV.status!="pause"&&!socket_error){//弹出广告图片，暂时节目
					elapsed_time=media.AV.elapsed;
					if(!elapsed_time) elapsed_time=0;
					write_elapsed_time(elapsed_time);
					media.AV.pause();
					show_pause_tips();
					show_front_play_status();				
				}else if(media.AV.status=="pause"&&!socket_error&&!pause_tips_visible){//隐藏右上方的DIV，继续播放节目
					pause_tips_visible=false;
					hidden_rightHiddenAd();
					media.AV.play();
					show_front_play_status();     //修改vod暂停播放后面板灯显示
				}
			}else if(volumeBarVisible){
				hideVolumeBar();
			}else if(info_visible){
				window.clearTimeout(info_timeout);
				hide_info();
			}else if(f3_visible){
				window.clearTimeout(f3_timeout);
				seek_f3_time();
			}
			var widgetObjNum= iPanel.pageWidgets.length;
       iPanel.debug("widget的个数===="+widgetObjNum);
       for(var i=0;i<widgetObjNum;i++)
       {
       	  var widgetObj= iPanel.pageWidgets.getAt(i);
       	  iPanel.debug("widget.name ==="+widgetObj.name);
          widgetObj.minimize();
       }
			return 0;
			break;
		case "EXIT":
		//alert("exit_remind_visible="+exit_remind_visible+"||f3_visible="+f3_visible+"||seek_flag="+seek_flag+"||socket_error="+socket_error);
			
			if(typeof(wasu_url)!="undefined"&&wasu_url!=""){
				close_widget();
				exit_page();
				return;
			}
			if(is_now_seek){
				has_stop=true;
				close_widget();
				media.AV.close();
				iPanel.overlayFrame.location.href="thank_look.htm";
			}else if(network_tips_visible){
				has_stop=true;
				close_widget();
				media.AV.close();
				//iPanel.overlayFrame.location.href="thank_look.htm";
				exit_page();
			}else if(ward_visible){
				window.clearTimeout(ward_timeout);
				hide_ward();
			}else if(volumeBarVisible){
				hideVolumeBar();
			}else if(info_visible){
				window.clearTimeout(info_timeout);
				hide_info();
			}else if(connect_failed){
				hide_exit();
				has_stop=true;
				close_widget();
				media.AV.close();
				exit_page();
			}else if(!exit_remind_visible&&!f3_visible){
				if(pause_tips_visible){
					hide_pause_tips();
				}
				show_exitRemind();
				if(seek_flag&&!socket_error){
				  setTimeout("media.AV.pause()",500);     //解决seek后立即按退出问题
			  }else{
					if(!socket_error){
						media.AV.pause();	
					}
				}
			}else if(f3_visible){
				window.clearTimeout(f3_timeout);
				hide_f3_tips();
			}
			return 0;
			break;
		case "BACK":
			if(typeof(wasu_url)!="undefined"&&wasu_url!=""){
				close_widget();
				exit_page();
				return;
			}
			if(ward_visible){
				window.clearTimeout(ward_timeout);
				hide_ward();
			}else if(f3_visible){
				window.clearTimeout(f3_timeout);
				hide_f3_tips();
			}else if(info_visible){
				window.clearTimeout(info_timeout);
				hide_info();
			}
			break;
		case "INFO":
			if(isAdPlay) return;
			if(can_info&&!f3_visible){
				if(!info_visible){
					show_info();
				}else{
					window.clearTimeout(info_timeout);
					hide_info();
				}	
			}
			break;
		/*case "RED":
			if(net_status=="0"&&vod_mode=="DVB"){
				$("tips_text").innerText="请插上网线";
				show_tips();
				return;
			}
			if(!information_visible&&!f3_visible&&!volumeBarVisible&&!exit_remind_visible&&!info_visible&&!network_tips_visible&&!pause_tips_visible&&!seek_flag&&!ward_visible){
				show_informationBar();
			}else if(information_visible){
				hide_informationBar();
			}
			return 0;
			break;*/
		case "GREEN":
			if(isAdPlay) return;
			if(net_status=="0"&&vod_mode=="DVB"){
				$("tips_text").innerText="请插上网线";
				show_tips();
				return;
			}
			if(!volumeBarVisible&&!exit_remind_visible&&!info_visible&&!network_tips_visible&&!pause_tips_visible&&!seek_flag&&!ward_visible){
				show_f3_tips();
			}else if(f3_visible&&!network_tips_visible){
				window.clearTimeout(f3_timeout);
				f3_timeout=window.setTimeout("hide_f3_tips()",10000);
			}
			break;	
		case "EIS_VOD_PREPAREPLAY_SUCCESS"://5202
			if(socket_error){
				media.AV.seek(socket_error_seek_time);
				seek_flag=true;
				first_play = true;
				ward_speed = 1;
			}else{
				media.AV.play();
			}
			return 0;
			break;
		case "EIS_VOD_CONNECT_FAILED"://5203
		if(isAdPlay){
				adPlayError = true;
				clearTimeout(checkAdElapsedTimeFlag);
				media.AV.close();
				rtsp_url = vedioUrl;
				isAdPlay = false;
				first_play = true;
				if(adFlag == "middle"){
					middleAdEndFlag = true;	
				}
				if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
					iPanel.debug("EIS_VOD_CONNECT_FAILED");
					setTimeout("checkVedioElapsedTime();",2000);
				}
				media.AV.open(rtsp_url,"VOD");
				return;
			}
			media.AV.close();
			hide_load_tips();
			connect_failed=true;
			var remindText = "连接异常,按“退出”键退出播放!";
			show_exit(remindText);
			first_play=false;
			close_widget();
			link_tips=false;
			clearTimeout(load_page_timeout);
			break;
		case "EIS_VOD_PLAY_SUCCESS"://5205
			if(middleAdEndFlag){
				middleAdEndFlag = false;
				media.AV.seek(middleAdEndSeekTime);
				seek_flag = true;
			}
			if(first_play){
				if(mute_status=="0"){
					media.sound.value=parseInt(currOriginVolume);
					has_set_sound=true;
				}
				$("load_tips").style.visibility="hidden";
				link_tips=false;
				first_play=false;
				duration = media.AV.duration;
				if(!duration){
					duration=0;
				}
				elapsed_time=media.AV.elapsed;
				if(!elapsed_time){
					elapsed_time=0;
				}
				write_duration_time();
				if(typeof(wasu_url)!="undefined"&&wasu_url!=""&&!socket_error){
					var my_wasu_widget  = iPanel.pageWidgets.getByName("wasu_widget");
					my_wasu_widget.location.href = wasu_url;
					my_wasu_widget.show();
				}
				if(pause_tips_visible && socket_error){
					media.AV.pause();
				}
				socket_error = false;
			}else if(is_now_seek){
				hide_load_tips();
				is_now_seek=false;
			}	
			setTimeout("refreshPlayTime()", 5000);
			seek_flag=false;
			clearTimeout(load_page_timeout);
			show_front_play_status();
			break;
		case "EIS_VOD_DVB_SERACH_FAILED":
			if(vod_mode=="DVB"){
				if(isAdPlay){
					adPlayError = true;
					clearTimeout(checkAdElapsedTimeFlag);
					media.AV.close();
					rtsp_url = vedioUrl;
					isAdPlay = false;
					first_play = true;
					if(adFlag == "middle"){
						middleAdEndFlag = true;	
					}
					if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
						iPanel.debug("EIS_VOD_DVB_SERACH_FAILED");
						setTimeout("checkVedioElapsedTime();",2000);
					}
					media.AV.open(rtsp_url,"VOD");
					return;
				}
				hide_load_tips();
				connect_failed=true;
				var remindText = "无信号,按“退出”键退出播放!";
				show_exit(remindText);
				first_play=false;
			}
			break;
		case "EIS_VOD_START_FAILED":
			if(isAdPlay){
				adPlayError = true;
				clearTimeout(checkAdElapsedTimeFlag);
				media.AV.close();
				rtsp_url = vedioUrl;
				isAdPlay = false;
				first_play = true;
				if(adFlag == "middle"){
					middleAdEndFlag = true;	
				}
				if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
					iPanel.debug("EIS_VOD_START_FAILED");
					setTimeout("checkVedioElapsedTime();",2000);
				}
				media.AV.open(rtsp_url,"VOD");
				return;
			}
			hide_load_tips();
			connect_failed=true;
			var remindText = "不能连接服务器,按“退出”键退出播放!";
			show_exit(remindText);
			first_play=false;
			break;
		case "EIS_VOD_PLAY_FAILED"://5206,暂定不使用这个消息
			cable_connect_flag = iPanel.getGlobalVar("cable_connect_success");
			if(typeof(cable_connect_flag)!="undefined"&&cable_connect_flag=="1"&&vod_mode=="DVB"){ 
				if(isAdPlay){
					adPlayError = true;
					clearTimeout(checkAdElapsedTimeFlag);
					media.AV.close();
					rtsp_url = vedioUrl;
					isAdPlay = false;
					first_play = true;
					if(adFlag == "middle"){
						middleAdEndFlag = true;	
					}
					if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
						iPanel.debug("EIS_VOD_PLAY_FAILED");
						setTimeout("checkVedioElapsedTime();",2000);
					}
					media.AV.open(rtsp_url,"VOD");
					return;
				}
				$("load_tips_text").innerText="连接失败！";
				close_widget();
				setTimeout("exit_page()", 2000);
			 }
			break;
		case "EIS_VOD_PROGRAM_BEGIN":
			break;
		case "EIS_VOD_PROGRAM_END"://5210
			if(info_visible){
				hide_info();
			}
			has_stop=true;
			close_widget();
			media.AV.close();
			iPanel.overlayFrame.location.href="thank_look.htm";
			break;
		case "EIS_IP_NETWORK_READAY":
			if(typeof(rtsp_url)!="undefined" && socket_error){
				window.clearTimeout(check_net_status_timeout);
				media.AV.open(rtsp_url,'VOD');
			}
			break;
		case "EIS_IP_NETWORK_CONNECT":
			NETWORK_DISCONNECT=0;
			NETWORK_CONNECT++;
			if (NETWORK_CONNECT>1) return;
			net_status="1";
		  if(connect_failed) return;//5203消息弹出提示后，只关闭请插入网线的提示，其他的正常显示
			if(vod_mode=="IP"){
				if(!pause_tips_visible&&!link_tips){
					if(exit_remind_visible){
						media.AV.play();//liyq add 20110328这里要考虑按了退出然后拔掉网线弹提示，插上网线后的播放问题
						hide_exitRemind();
					}
					show_front_play_status();
				}
				hide_exit();
				window.clearTimeout(network_timeout);
				network_tips_visible=false;
			}else if(vod_mode=="DVB"){//liyq add 20110322 高清点播在拔掉网线退出后再按退出继续播放的时候，提示网络异常，插上网线后调用play播放
				if(network_tips_visible){
					network_tips_visible=false;
					media.AV.play();
					hide_exit();
					if(pause_tips_visible){
						hide_pause_tips();//如果重新去play的时候，如果之前是显示了暂停图标的，需要隐藏掉
					}
					if(exit_remind_visible){
						hide_exitRemind();	
					}
				}
				$("net_tips").style.visibility="hidden";	
			}	
			if(socket_error){
				check_net_status_timeout = window.setTimeout("check_net_status()",200);
			}
			break;
		case "EIS_IP_NETWORK_DISCONNECT":
			NETWORK_DISCONNECT++;
			NETWORK_CONNECT=0;
			if(NETWORK_DISCONNECT>1) return;
			net_status="0";
			if(link_tips){
				has_stop=true;
				close_widget();
				media.AV.close();
				exit_page();
				return;
			}
			if(vod_mode=="IP"){
				if(!first_play){
					elapsed_time=media.AV.elapsed;
					var hour=Math.floor(elapsed_time/3600);
					var minute=Math.floor((elapsed_time-hour*3600)/60);
					var second=elapsed_time-hour*3600-minute*60;
					hour=hour>9?hour:"0"+hour;
					minute=minute>9?minute:"0"+minute;
					second=second>9?second:"0"+second;
					socket_error_seek_time = hour+":"+minute+":"+second;
				}
				if(f3_visible){
					window.clearTimeout(f3_timeout);
					hide_f3_tips();
				}
				network_tips_visible=true;
				var remindText = "网络不通，按“确认”键结束播放，插上网线可继续播放.";
				show_exit(remindText);
			}else if(vod_mode=="DVB"){
				$("net_tips_text").innerText="请插上网线";
				$("net_tips").style.visibility="visible";
			}
			if(socket_error){
				window.clearTimeout(check_net_status_timeout);
			}
			break;
		case "DVB_CABLE_CONNECT_SUCCESS":
			if(vod_mode=="DVB"){
				if(!pause_tips_visible&&!link_tips&&media.AV.status=="pause"){
					media.AV.play();
				}
				network_tips_visible=false;
				hide_exit();
				if(net_status=="0"){//网络没插上的话，保留网线的提示
					$("net_tips_text").innerText="请插上网线";
					$("net_tips").style.visibility="visible";
				}else{
					$("net_tips").style.visibility="hidden";
				}
			}
			break;
		case "DVB_CABLE_CONNECT_FAILED":
			if(vod_mode=="DVB"){
				if(link_tips){
					$("net_tips_text").innerText="无信号";
					$("net_tips").style.visibility="visible";
					return;
				}
				if(media.AV.status!="pause"&&!link_tips&&net_status=="1"){
					media.AV.pause();
					show_front_play_status();
				}
				network_tips_visible=true;
				var remindText = "没有信号,插上cable线继续播放,按“确认”键结束播放";
				show_exit(remindText);
			}
			break;
		case "EIS_VOD_START_BUFF"://底层开始缓冲数据，等待播放
			show_load_tips();
			is_now_seek=true;
			seek_flag=false;
			break;
		/*case "EIS_VOD_STOP_BUFF"://缓冲数据结束，已开始播放,暂定不使用这个消息
			break;*/
		case "EIS_VOD_OUT_OF_RANGE"://传入的时间超出有效范围
			break;
		case "EIS_VOD_USER_EXCEPTION"://用户自定义错误,包括缓冲超时
			if(isAdPlay){
				adPlayError = true;
				clearTimeout(checkAdElapsedTimeFlag);
				media.AV.close();
				rtsp_url = vedioUrl;
				isAdPlay = false;
				first_play = true;
				if(adFlag == "middle"){
					middleAdEndFlag = true;	
				}
				if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
					iPanel.debug("EIS_VOD_USER_EXCEPTION");
					setTimeout("checkVedioElapsedTime();",2000);
				}
				media.AV.open(rtsp_url,"VOD");
				return;
			}
			user_error_code=event_obj.args.modifiers;
			var ethernetItem = network.ethernets[0];
			net_status = ethernetItem.LANStatus; 
			if(user_error_code == 2106 && (net_status==0||(net_status!=3&&network.ethernets[0].DHCPEnable==1))){
				socket_error = true;
				media.AV.close();
			}else{
				hide_load_tips();
				seek_flag=false;
				if(is_now_seek){
					is_now_seek=false;
					can_ward=false;
					can_info=false;
				}
				has_stop=true;
				hide_exit();
				var error_tips=check_error_code(user_error_code);
				iPanel.overlayFrame.location.href="error_tips1.htm?"+error_tips;
			}
			break;
		case "EIS_VOD_CLOSE_SUCCESS"://5211 close成功
			break;
		case "EIS_VOD_CLOSE_FAILED"://5212 close失败
			break;
	}
}


function init(){
DVB.stopAV();
	iPanel.misc.startOtherApp("VOD");
	
	//----------是否优先使用全局变量---------------
	var isGlobalFlag="";
	var locationUrl = iPanel.mainFrame.location.href;
	if(locationUrl.indexOf("&conVod=")!= -1){
		isGlobalFlag = locationUrl.split("&conVod=")[1].split("&")[0];
	}
	iPanel.debug("isGlobalFlag=="+isGlobalFlag);
	if(isGlobalFlag == "true"){
			vedioUrl = locationUrl.split("rtspUrl=")[1];
	}
	if(startAdUrl){
		isAdPlay = true;
		rtsp_url=startAdUrl;
		if(typeof(rtsp_url)!="undefined"){
			media.AV.open(rtsp_url,'VOD');
			adFlag = "start";
			adDuration = startAdDuration;
			setTimeout("checkAdElapsedTime();",2000);
		}
	}else{
		isAdPlay = false;
		rtsp_url = vedioUrl;
		if(typeof rtsp_url != "undefined"){
			media.AV.open(rtsp_url,'VOD');
			if(middleAdUrl){
				iPanel.debug("init()");
				setTimeout("checkVedioElapsedTime();",2000);
			}
		}
	}
  media.video.fullScreen();
  if(rtsp_url.indexOf(".ts")!=-1){
		iPanel.eventFrame.server_mode = "DVB";
	}else{
		iPanel.eventFrame.server_mode = "IP";
	}
  vod_mode= iPanel.eventFrame.server_mode;

	if(cable_connect_flag == "0"&&vod_mode=="DVB"){
		network_tips_visible=true;
		var remindText = "没有信号,插上cable线继续播放,按“确认”键结束播放";
		show_exit(remindText);
		return;	    
	}	
	if(link_tips) {
		$("load_tips").style.visibility="visible";
		load_page_timeout = setTimeout("show_page_load_timeout()", 12000);
	}
	var ethernetItem = network.ethernets[0];
	var net_status = ethernetItem.LANStatus; //1：表示目前处于连接状态   0：表示没有连接上网络
	if (net_status==0||(net_status!=3&&network.ethernets[0].DHCPEnable==1)){
		$("net_tips").style.visibility="visible";
	}
	if (mute_status != "0"){
		iPanel.misc.showWindow("ui://muteIcon.gif",558,43,44,49);//以防万一，显示一下吧
	}
	write_elapsed_time(elapsed_time);
}

function checkAdElapsedTime(){//检测广告的播放结束点
	var elapsedTime = media.AV.elapsed;
	//alert("tudou-----elapsedTime=="+elapsedTime+"||adDuration=="+adDuration+"||adPlayError=="+adPlayError);
	iPanel.debug("tudou-----elapsedTime=="+elapsedTime+"||adDuration=="+adDuration+"||adPlayError=="+adPlayError);
	if(elapsedTime >= (adDuration-5)&&!adPlayError){
		iPanel.debug("clear------checkAdElapsedTime()");
		clearTimeout(checkAdElapsedTimeFlag);
		media.AV.close();
		iPanel.debug("tudou------vedioUrl=="+vedioUrl+"||adFlag=="+adFlag);
		rtsp_url = vedioUrl;
		isAdPlay = false;
		first_play = true;
		if(adFlag == "middle"){
			middleAdEndFlag = true;	
		}
		iPanel.debug("tudou-----middleAdUrl=="+middleAdUrl+"||middleAdEndFlag=="+middleAdEndFlag);
		if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
			setTimeout("checkVedioElapsedTime();",2000);
		}
		setTimeout(function (){
			media.AV.open(rtsp_url,"VOD");
		},300);
	}else{
		checkAdElapsedTimeFlag = setTimeout(function(){
			checkAdElapsedTime();
		},1000);
	}
}

function checkVedioElapsedTime(){//检测片中广告的插播点
	var elapsedTime = media.AV.elapsed;
	if(elapsedTime >= middleAdInsertTime){
		iPanel.debug("clear----checkVedioElapsedTime()");
		clearTimeout(checkVedioElapsedTimeFlag);
		isAdPlay = true;
		middleAdEndSeekTime=time_to_str(elapsedTime,1);
		media.AV.close();
		rtsp_url = middleAdUrl;
		setTimeout(function (){
			media.AV.open(rtsp_url,'VOD');
		},300);
		adFlag = "middle";
		adDuration = middleAdDuration;
		middleAdEndFlag = false;
		setTimeout("checkAdElapsedTime();",2000);
	}else{
		checkVedioElapsedTimeFlag = setTimeout(function(){
			checkVedioElapsedTime();
		},1000);	
	}
}

function show_page_load_timeout() {
	if(isAdPlay){
		iPanel.debug("clear-----show_page_load_timeout()");
		clearTimeout(checkAdElapsedTimeFlag);
		media.AV.close();
		rtsp_url = vedioUrl;
		isAdPlay = false;
		first_play = true;
		if(adFlag == "middle"){
			middleAdEndFlag = true;	
		}
		if(middleAdUrl&&!middleAdEndFlag){//有片中广告还没播放时
			iPanel.debug("show_page_load_timeout()");
			setTimeout("checkVedioElapsedTime();",2000);
		}
		media.AV.open(rtsp_url,"VOD");
		return;
	}
	$("load_tips_text").innerText="连接失败！";
	setTimeout("exit_page()", 2000);
	close_widget();  //解决第一次进华数眼失败再进VOD会显示华数眼控制云台
}

function write_duration_time(){
	$("seek_duration_time").innerText = time_to_str(duration,1);
	$("f3_duration_time").innerText = time_to_str(duration,1);
	$("pause_duration_time").innerText = time_to_str(duration,1);
	$("info_duration_time").innerText=time_to_str(duration,1);
}

function write_now_data(){
	var d= new Date();
	var year = d.getYear();
	var month = d.getMonth() + 1;
	var date = d.getDate();
	var hour = d.getHours();
	var minute = d.getMinutes();
	var second = d.getSeconds();
	if(parseInt(month) < 10) month = "0" + month;
	if(parseInt(date) < 10) date = "0" + date;
	if(parseInt(hour) < 10) hour = "0" + hour;
	if(parseInt(minute) < 10) minute = "0" + minute;
	if(parseInt(second) < 10) second = "0" + second;
	var date_str = month+"/"+date+"&nbsp;&nbsp;"+hour+":"+minute+":"+second;
	$("seek_now_date").innerHTML = date_str;
	$("f3_now_date").innerHTML = date_str; 
	$("volume_now_date").innerHTML = date_str;
}

function write_elapsed_time(_elapsed_time){
	if (_elapsed_time < 0){
		_elapsed_time=0;
	}else if(_elapsed_time > duration){
		_elapsed_time=duration;
	}
	$("seek_now_time").innerText=time_to_str(_elapsed_time,1);
	$("pause_now_time").innerText = time_to_str(_elapsed_time,1);
}

function exit_page(){
	media.AV.close();
	if(ward_visible){
		hide_ward();
	}
	hide_load_tips();
	hide_exit();
	
	iPanel.misc.enableIconLoading=1;
	iPanel.setGlobalVar("url_parm","");//这里要清除一下url_parm，避免上次未正常打开华数眼时，下次普通全屏点播是url_parm有残留
	
	/**从高清点播退出来，置为回第三方VOD**/
	if(vod_mode=="DVB"){	
  	VOD.changeServer("sihua_3rd","dvb");
	}
	if(mute_status == "1"){
		iPanel.misc.showWindow("ui://muteIcon.gif",558,43,44,49);
	}
	iPanel.overlayFrame.close();
	var ethernet_item = network.ethernets[0];
	if(ethernet_item.LANStatus==0 ||(ethernet_item.LANStatus!=3&&network.ethernets[0].DHCPEnable==1)){
		iPanel.setGlobalVar("netlink_status", "0");
		iPanel.eventFrame.flag = 1;
		hardware.STB.frontPanel(2,"0000",0,0,0);
		setTimeout('window.location.href = "ui://index.htm"', 50);
	}else{
		iPanel.setGlobalVar("netlink_status", "1");
		iPanel.eventFrame.flag = 2;
		history.go(-3);
		hardware.STB.frontPanel(2,"IE::",0,0,100);
		setTimeout("go_index()",7000);
	}
}

function go_index(){
	iPanel.eventFrame.flag = 1;
	hardware.STB.frontPanel(2,"0000",0,0,0);
	iPanel.mainFrame.location.href = 'ui://index.htm';
}

function volumeAdjust(offset){
	if(!volumeBarVisible){
		showVolumeBar();
	}
	iPanel.debug("clear----volumeAdjust(offset)");
	clearTimeout(volumeBarTimeout);
	volumeBarTimeout = setTimeout("hideVolumeBar()", 4000);
	if((currVolume == 0 && offset < 0) || (currVolume == 100 && offset > 0)){
		$("volumeValue").innerText = currVolume;
		generateVolume(currVolume);
		return;
	}
	currVolume += offset;
	if (currVolume < 5) currVolume = 0;
	else if (currVolume > 100) currVolume = 100;
	media.sound.value = currVolume;
	$("volumeValue").innerText = currVolume;
	generateVolume(currVolume);
}

function generateVolume(num) {
	var volumeNumber = parseInt(num/5)*240;
//	var str = "";
//	for (var i = 0; i < volumeNumber; i++){
//		str += "<img src='volume_value.jpg' width='12', height='18'/>";
//	}
	$("volume").style.width = volumeNumber+"px";
}

function showVolumeBar(){
	write_now_data();
	volumeBarVisible = true;
	$("volumeBar").style.visibility = "visible";
	var volumeAdWidget = iPanel.pageWidgets.getByName("volume_ad");
	volumeAdWidget.withoutFocus = 1;
	volumeAdWidget.show();
	var marqueeAdWidget = iPanel.pageWidgets.getByName("marquee_ad");
	marqueeAdWidget.iconize();

}

function hideVolumeBar(){
	volumeBarVisible = false;
	$("volumeBar").style.visibility = "hidden";
	var volumeAdWidget = iPanel.pageWidgets.getByName("volume_ad");
	volumeAdWidget.iconize();
	var marqueeAdWidget = iPanel.pageWidgets.getByName("marquee_ad");
	marqueeAdWidget.withoutFocus = 1;
	marqueeAdWidget.show();
	loadAd(51);//音量
}


function show_mute_icon() {
	if(volumeBarVisible){
		hideVolumeBar();
	}
	iPanel.setGlobalVar("mute_status", "1");
	media.sound.mute();
	mute_status = "1";
	iPanel.misc.showWindow("ui://muteIcon.gif",558,43,44,49);
}

function hide_mute_icon(){
	if (!has_set_sound){
		var currOriginVolume = media.sound.value;
		media.sound.value=parseInt(currOriginVolume);
		has_set_sound=true;
	}
	media.sound.resume();
	mute_status="0";
	iPanel.setGlobalVar("mute_status", "0");
	iPanel.misc.hideWindow();
}

function show_ward(){
	write_now_data();
	var temp_width=Math.floor((elapsed_time/duration)*308);
	$("process_bar").style.width=temp_width+"px";
	$("process_barPot").style.left=(temp_width+6)+"px";
	$("ward_bar").style.visibility="visible";
	ward_visible=true;
	can_volume=false;
	can_info=false;
	ward_timeout=window.setTimeout("hide_ward()",5000);
}

function hide_ward(){
	$("ward_bar").style.visibility="hidden";
	ward_visible=false;
	can_volume=true;
	can_info=true;
	loadAd(101161);//快进快退
}

function show_pause_tips(){
	//alert("show"+media.AV.elapsed);
	var temp_width=(elapsed_time/duration)*431;
	$("pause_process_bar").style.width=temp_width+"px";
	$("pause_process_barPot").style.left=(temp_width+6)+"px";
	$("pause_tips").style.visibility="visible";
	$("continue-play").style.class="continue-play2";
	$("hidden-ad").style.class="hidden-ad1";
	pause_tips_visible = true;

	if(!isAdPlay){
			loadAd(50);//暂停
	}
}

function hide_pause_tips(){
	//alert("hide"+media.AV.elapsed);
	continuePlayFlag=true;
	pause_tips_visible=false;
	$("pause_tips").style.visibility="hidden";
}

//隐藏广告图片，在右上角显示暂停标识
function show_rightHiddenAd(){
	$("rightHiddenAd").style.visibility="visible";
}
  
function hidden_rightHiddenAd(){
	//continuePlayFlag=true;
	$("rightHiddenAd").style.visibility="hidden";
}


function show_exit(_remindText){
	$("exit_tips_text").innerHTML = _remindText;
	$("exit_tips").style.visibility="visible";
	can_volume=false;
	can_ward=false;
	can_info=false;
}

function hide_exit(){
	$("exit_tips").style.visibility="hidden";
	can_volume=true;
  can_ward=true;
	can_info=true;
}
function show_exitRemind(){
	butPosition = 1;
	hidden_rightHiddenAd();
	$("yesPosition").style.class = "yes";
	$("noPosition").style.class = "no1";
	$("exit_remind").style.visibility="visible";
	exit_remind_visible=true;
	can_volume=false;
	can_ward=false;
	can_info=false;
	
}

function hide_exitRemind(){
	$("exit_remind").style.visibility="hidden";
	exit_remind_visible=false;
	can_volume=true;
  can_ward=true;
	can_info=true;
	loadAd(101159);//退出
}

function show_info(){
	info_visible=true;
	show_elapsed_time();
	$("info").style.visibility="visible";
	can_volume=false;
  can_ward=false;
	info_timeout=window.setTimeout("hide_info()",5000);
}

function hide_info(){
	$("info").style.visibility="hidden";
	info_visible=false;
	can_volume=true;
	can_ward=true;
}

function show_informationBar(){
	$("informationBar").style.visibility = "visible";
	$("informationBar").style.left = "30px";
	information_visible = true;
	iPanel.debug("clear----show_informationBar()");
  clearTimeout(information_timeout);
	information_timeout = window.setTimeout("hide_informationBar();",10000);		
}

function hide_informationBar(){
	iPanel.debug("clear----hide_informationBar()");
	clearTimeout(information_timeout);
	$("informationBar").style.left = "-283px";
	information_visible = false;
}


function show_elapsed_time(){
	var __elapsed_time=media.AV.elapsed;
	$("info_now_time").innerText=time_to_str(__elapsed_time,1);
	if(info_visible){
		window.setTimeout("show_elapsed_time()",1000);
	}
}

function show_load_tips(){
	$("load_tips_text").innerText="正在缓冲，请稍候";
	$("load_tips").style.visibility="visible";	
}


function hide_load_tips(){
	$("load_tips").style.visibility="hidden";
	loadAd(101163);//loading
}


function time_to_str(_time,flag){
	if (_time < 0){
		_time = 0;
	}
	var hour = Math.floor(_time/3600);
	var minute = Math.floor((_time-3600*hour)/60);
	var second = _time-3600*hour-minute*60;
	hour = hour > 9 ? hour : "0"+hour;
	minute = minute > 9 ? minute : "0"+minute;
	second = second > 9 ? second : "0"+second;
	if(flag == 1){
		var _str=hour+":"+minute+":"+second;
	}else{
		var _str=""+hour+minute+second;
	}
	return _str;
}

function show_tips(){
	$("tips").style.visibility="visible";
	window.setTimeout("hide_tips()",3000);
}

function hide_tips(){
	$("tips").style.visibility="hidden";
}

function show_f3_tips(){
	write_now_data();
	window.clearTimeout(f3_timeout);
	var elapsed_time=media.AV.elapsed;
	var __time_str=time_to_str(elapsed_time,2);
	time_array=__time_str.split("");
	for (var i=0;i<6;i++){
		$("f3_time"+i).innerText=time_array[i];
	}
	f3_visible=true;
	$("f3_time"+time_position).style.backgroundColor="#FF9900";
	$("f3").style.visibility="visible";
	f3_timeout=window.setTimeout("hide_f3_tips()",10000);
}

function hide_f3_tips(){
	$("f3").style.visibility="hidden";
	f3_visible=false;
	loadAd(101161);  //快进快退
}

function set_time1(__num){
	switch (time_position){
		case 2:
		case 4:
			if(__num>=6) return;
			break;
	}
	time_array[time_position]=__num;
	$("f3_time"+time_position).innerText=__num;
}

function set_time(__num){
	switch (time_position){
		case 2:
		case 4:
			write_time(0,5);
			break;
		case 0:
		case 1:
		case 3:
		case 5:
			write_time(0,9);
			break;
	}

	function write_time(__min,__max){
		time_array[time_position]=parseInt(time_array[time_position])+__num;
		if (time_array[time_position]>__max){
			time_array[time_position]=__min;
		}else if (time_array[time_position]<__min){
			time_array[time_position]=__max;
		}
		$("f3_time"+time_position).innerText=time_array[time_position];
	}
}

function seek_f3_time(){
	var f3_seek_time=(parseInt(time_array[0])*10+parseInt(time_array[1]))*60*60+(parseInt(time_array[2])*10+parseInt(time_array[3]))*60+parseInt(time_array[4])*10+parseInt(time_array[5]);
	if (f3_seek_time>duration){
		$("tips_text").innerText="超出选时范围，按F3重新输入。";
		show_tips();
		return;
	}else if(f3_seek_time==duration){
		hide_f3_tips();
		has_stop=true;
		close_widget();
		media.AV.close();
		iPanel.overlayFrame.location.href="thank_look.htm";
		return;
	}else{
		hide_f3_tips();
		var f3_seek_time=""+time_array[0]+time_array[1]+":"+time_array[2]+time_array[3]+":"+time_array[4]+time_array[5];
		media.AV.seek(f3_seek_time);
		seek_flag=true;
	}
}

function change_time_focus(__num){
	$("f3_time"+time_position).style.backgroundColor="#FFFFFF";
	time_position+=__num;
	if (time_position>5)
	   time_position=0;
	else if(time_position<0)
		time_position=5;
	$("f3_time"+time_position).style.backgroundColor="#FF9900";
}

function $(id){
	return document.getElementById(id);
}

function close_widget(){
	if (typeof(wasu_url)!="undefined"&&wasu_url!=""){
		var my_wasu_widget  = iPanel.pageWidgets.getByName("wasu_widget");
		my_wasu_widget.iconize();
		my_wasu_widget.location.href = "ui://blank.htm";
		iPanel.setGlobalVar("url_parm","");
	}
}

function refreshPlayTime() {
	showCurrPlayTime();
	setTimeout("refreshPlayTime()", 30000);
}

function showCurrPlayTime() {
	var progress = time_to_str(media.AV.elapsed, 1);
	var currPlayTime = progress.substring(0, 5);
	hardware.STB.frontPanel(2,currPlayTime,0,0,100);
}

function show_front_play_status(){//显示前面板播放状态
	for (var i=60;i<64;i++){
		hardware.STB.statusLight(i,0,0,0);
	}
	var status=media.AV.status;
	switch (status){
		case "play":
			hardware.STB.statusLight(60,0,0,100);
			break;
		case "pause":
			hardware.STB.statusLight(61,0,0,100);
			break;
		case "fastforward":
			hardware.STB.statusLight(62,0,0,100);
			break;
		case "backward":
			hardware.STB.statusLight(63,0,0,100);
			break;
	}
}

function check_net_status(){
	var ethernet_item = network.ethernets[0];
	if(ethernet_item.LANStatus==0 ||(ethernet_item.LANStatus!=3&&network.ethernets[0].DHCPEnable==1)){
		iPanel.debug("clear----check_net_status()1");
		window.clearTimeout(check_net_status_timeout);
		check_net_status_timeout = window.setTimeout("check_net_status()",200);
	}else{
		if(typeof(rtsp_url)!="undefined" && socket_error){
			iPanel.debug("clear----check_net_status()2");
			window.clearTimeout(check_net_status_timeout);
			media.AV.open(rtsp_url,'VOD');
		}
	}
}
</script>
</head>

<body leftmargin="0" text="#FFFFFF" topmargin="0" bgcolor="transparent" onLoad="init()" onunLoad="close_widget()">
<!--loading-->
<div id="load_tips" style="position:absolute; width:496px; height: 311px; background-image:url(loading_bg.gif); left: 72px; top: 107px; visibility:hidden; z-index:1;">
	<div style="position:absolute; width:462px; height:183px; background-image:url(ad_pause.gif); left: 18px; top:17px;"></div>
	<div style="position:absolute; width:119px; height:46px; background-image:url(loading.gif); left: 26px; top: 233px;"></div>
	<div id="load_tips_text" style="position:absolute; width:327px; height:39px; left:153px; top:242px; color: #250B36; font-size: 24px;">正在连接，敬请稍候...</div>
</div>
<!--exit_tips-->
<div id="exit_tips" class="error_bg" style="position:absolute; width:343px; height:145px; left:148px; top:190px; visibility:hidden; z-index:3;">
	<div style="position:absolute; width:312px; height:91px; left: 16px; top: 37px;">
    	<div style="position:absolute; left:0px; top:0px; width:89px; height:91px;">
        	<div class="network_error" style="position:absolute; left:18px; top:22px; width:53px; height:47px;"></div>
        </div>
        <div id="exit_tips_text" style="position:absolute; left:89px; top:0px; width:223px; height:91px;">
        	<table width="223" height="91" border="0" cellpadding="0" cellspacing="0">
                <tr class="text20">
                    <td id="exit_tips_text" width="223" class="text24">连接错误。。。。</td>
                </tr>
            </table>
		</div>
	</div>
</div>
<!--net_tips-->
<div id="net_tips" class="error_bg" style="position:absolute; width:343px; height:145px; left:148px; top:190px; visibility:hidden; z-index:3;">
	<div style="position:absolute; width:312px; height:91px; left: 16px; top: 37px;">
    	<div style="position:absolute; left:0px; top:0px; width:89px; height:91px;">
        	<div class="network_error" style="position:absolute; left:18px; top:22px; width:53px; height:47px;"></div>
        </div>
        <div id="exit_tips_text" style="position:absolute; left:89px; top:0px; width:223px; height:91px;">
            <table width="223" height="91" border="0" cellpadding="0" cellspacing="0">
                <tr class="text20">
                <td id="net_tips_text" width="223" class="text20">请插上网线</td>
                </tr>
            </table>
		</div>
	</div>
</div>
<!--tips-->
<div id="tips" class="error_bg" style="position:absolute; width:343px; height:145px; left:148px; top:190px; visibility:hidden; z-index:3;">
	<div style="position:absolute; width:312px; height:91px; left: 16px; top: 37px;">
    	<div style="position:absolute; left:0px; top:0px; width:89px; height:91px;">
        	<div class="network_error" style="position:absolute; left:18px; top:22px; width:53px; height:47px;"></div>
        </div>
        <div id="exit_tips_text" style="position:absolute; left:89px; top:0px; width:223px; height:91px;">
            <table width="223" height="91" border="0" cellpadding="0" cellspacing="0">
                <tr class="text20">
                	<td id="tips_text" width="223" class="text20">连接错误。。。。</td>
                </tr>
            </table>
		</div>
	</div>
</div>
<!--wardBar-->
<div id="ward_bar" class="seekBar_bg" style="position:absolute; width:581px; height:106px; left:29px; top:410px; visibility:hidden; z-index:1;">
	<div id="seek_now_date" style="position:absolute; width:138px; height:18px; left:431px; top:8px; color: #FFFFFF; font-size: 16px;">09/21&nbsp;&nbsp;10:48:11</div>
	<div style="position:absolute; width:322px; height:7px; left: 105px; top: 58px; z-index:2;">
		<div class="left_jd" style="position:absolute; top:0px; width:6px; height:7px; z-index:3;"></div>
        <div id="process_bar" class="middle_jd" style="position:absolute; top:0px; left:6px; width:307px; height:7px; z-index:2;"></div>
        <div id="process_barPot" class="right_jd" style="position:absolute; top:0px; left:313px; width:6px; height:7px;"></div>
	</div>
	<div style="position:absolute; width:324px; height:41px; left: 103px; top: 29px;">
	  <table width="323" height="40" border="0" cellpadding="0" cellspacing="0">
	  	<tr style=" font-size:18px; color: #250B36; ">
				<td width="150" height="26">&nbsp;</td>
				<td id="seek_now_time" width="85" align="right">09:20:30</td>
				<td width="11">/</td>
				<td id="seek_duration_time" width="78" align="left">10:30:20</td>
      </tr>
      <tr>
				<td colspan="4"></td>
      </tr>
    </table>
  </div>
	<div style="position:absolute; width:391px; height:20px; left: 13px; top: 74px; font-size: 16px; color: #250B36;">
    <table width="391" height="20" border="0" cellpadding="0" cellspacing="0">
    	<tr style=" font-size:16px; color: #250B36; ">
			<td>&nbsp;&nbsp;按</td><td class="backward" width="11" height="20"></td><td class="forward" width="11" height="20"></td><td>快进快退，按&nbsp;<span class="text18">[确认键]</span>&nbsp;继续播放</td>
        </tr>
    </table>
	</div>
</div>
<!--exitRemind-->
<div id="exit_remind" style="position:absolute; width:496px; height: 311px; background-image:url(loading_bg.gif); left: 72px; top: 107px; visibility:hidden; z-index:1;">
	<div style="position:absolute; width:464px; height:187px; background-image:url(ad_pause.gif); left: 17px; top:17px;"></div>
	<div class="wasulogo" style="position:absolute; width:118px; height:46px; left: 28px; top: 231px;"></div>
	<div style="position:absolute; width:155px; height:30px; left:147px; top:247px; color: #20002F; font-size: 22px;">是否结束播放？</div>
	<div id="yesPosition" class="yes" style="position:absolute; width:80px; height:35px; left: 300px; top: 242px;"></div>
	<div id="noPosition" class="no1" style="position:absolute; width:80px; height:35px; left: 392px; top: 242px;"></div>
</div>
<!--infoRemind-->
<div id="info" class="error_bg" style="position:absolute; width:343px; height:145px; left:148px; top:190px; visibility:hidden; z-index:2;">
	<div style="position:absolute; width:312px; height:92px; left: 16px; top: 38px;">
	  <table width="312" height="90" border="0" cellpadding="0" cellspacing="0">
        <tr class="text18">
          <td width="184" align="center" >当前时间：</td>
          <td width="185" id="info_now_time">&nbsp;</td>
        </tr>
        <tr class="text18">
          <td align="center">影片总长：</td>
          <td id="info_duration_time">&nbsp;</td>
        </tr>
      </table>
  </div>
</div>
<!--volumeBar-->
<div id="volumeBar" class="seekBar_bg" style="position:absolute; width:581px; height:106px; left:29px; top:410px; visibility:hidden; z-index:1;">
	<div id="volume_now_date" style="position:absolute; width:138px; height:18px; left:431px; top:8px; color: #FFFFFF; font-size: 16px;">09/21&nbsp;&nbsp;10:48:11</div>
	<div class="marquee_bg" style="position:absolute; width:315px; height:18px; left: 108px; top: 33px;">
		<marquee behavior="scroll" width="310">
			<span class="text16">华数TV提醒您，适度音量，保护听力。</span>
		</marquee>
  </div>
	<div class="ad_forward" style="position:absolute; width:137px; height:61px; left: 430px; top: 27px;"></div>
	<div class="volume" style="position:absolute; width:315px; height:30px; left: 106px; top: 60px; font-size: 24px; color: #250B36;">
		<div id="volumeValue" style="position:absolute; width:22px; height:17px; left: 28px; top: 10px; color: #FFFFFF; font-size: 16px;">35</div>
		<div style="position:absolute; width:240px; height:21px; left:67px; top: 10px;">
			<div id="volume" class="volume_value" style="position:absolute; width:12px; height:18px;"></div>
		</div>
  </div>
</div>
<!--pause_tips-->
<div id="pause_tips" style="position:absolute; width:496px; height: 311px; left: 72px; top: 107px; background-image:url(loading_bg.gif); visibility:hidden; z-index:1;">
	<div style="position:absolute; width:464px; height:187px; background-image:url(ad_pause.gif); left: 17px; top:17px;"></div>
	<div style="position:absolute; width:457px; height:80px; left:22px; top:217px;">
		<table width="457" height="80" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="53" height="31" class="text22">暂停</td>
				<td width="87"><span class="pause1" style="position:absolute; top:5px; width:25px; height:24px;"></span></td>
				<td width="181" align="right"><span id="continue-play" class="continue-play2" style="position:absolute; left:140px; top:5px; width:92px; height:28px;"></span></td>
				<td>&nbsp;</td>	
				<td align="left">&nbsp;<span id="hidden-ad" class="hidden-ad1" style="position:absolute; left:340px; top:5px; width:92px; height:28px;"></span></td>		
			</tr>
			<tr><td height="18" colspan="5">&nbsp;</td>
			</tr>
			<tr align="right">
				<td align="right" id="pause_now_time" valign="bottom" class="text20" colspan="3">21:30:30</td>
				<td width="12" valign="bottom" class="text22">/</td>
				<td width="124" id="pause_duration_time" valign="bottom" class="text20" align="left">21:30:30</td>
			</tr>
			
	  </table>
  </div>
	<div style="position:absolute; width:442px; height:8px; left:28px; top:259px; ">
		<div class="left_jd" style="position:absolute; left:0px; top:0px; width:6px; height:7px; z-index:3"></div>
		<div id="pause_process_bar" class="middle_jd" style="position:absolute; top:0px; left:6px; width:431px; height:7px; z-index:2;"></div>
		<div id="pause_process_barPot" class="right_jd" style="position:absolute; top:0px; left:437px; width:6px; height:7px;"></div>
	</div>
</div>
<!--增加右上角标识　fengjh-->
<div style="position:absolute; width:140px; height:70px; left:530px; top:20px; visibility:visible;" id="rightHiddenAd">
	<table height="70" cellpadding="0" cellspacing="0"  style="font-size:18px; color: #FFFFFF;">
		<tr height="40" align="center">
			<td><span class="pause1" style="position:absolute; top:7px; left:6px; width:25px; height:24px;" width="24" height="24"></span>&nbsp;<span style="font-size:24px; vertical-align:top; ">暂&nbsp;停</span></td>
		</tr>
		<tr height="5">
			<td align="center"><hr size="2" color="#FFFFFF" width="100"></td>
		</tr>
		<tr height="20" align="center">
			<td>按[确认]播放</td>
		</tr>
  </table>
</div>
<!--F3Remind-->
<div id="f3" class="seekBar_bg" style="position:absolute; width:581px; height:106px; left:29px; top:410px; visibility:hidden; z-index:1;">
	<div id="f3_now_date" style="position:absolute; width:138px; height:18px; left:431px; top:7px; color: #FFFFFF; font-size: 16px;">09/21&nbsp;&nbsp;10:48:11</div>
	<div style="position:absolute; width:328px; height:22px; left: 102px; top: 44px;">
	  <table width="325" height="20" border="0" cellpadding="0" cellspacing="0">
        <tr style=" font-size:18px; color: #250B36; ">
          <td width="140">选择播放时间:</td>
          <td width="10" align="right" bgcolor="#FFFFFF" id="f3_time0">0</td>
          <td width="15" align="center" bgcolor="#FFFFFF" id="f3_time1">0</td>
          <td width="15" bgcolor="#FFFFFF">:</td>
          <td width="15" align="center" bgcolor="#FFFFFF" id="f3_time2">0</td>
          <td width="15" align="center" bgcolor="#FFFFFF" id="f3_time3">0</td>
          <td width="15" bgcolor="#FFFFFF">:</td>
          <td width="15" align="center" bgcolor="#FFFFFF" id="f3_time4">0</td>
          <td width="10" align="left" bgcolor="#FFFFFF" id="f3_time5">0</td>
          <td width="10" align="center">/</td>
          <td width="80" id="f3_duration_time">00:00:00</td>
        </tr>
      </table>
  </div>
	<div style="position:absolute; width:442px; height:21px; left: 13px; top: 72px; font-size: 16px; color: #250B36;">
	  &nbsp;&nbsp;操作&nbsp;<span class="text18">[数字键]</span>&nbsp;选择时间，按&nbsp;<span class="text18">[确认键]</span>&nbsp;播放
	</div>
</div>
</body>
</html>
 
