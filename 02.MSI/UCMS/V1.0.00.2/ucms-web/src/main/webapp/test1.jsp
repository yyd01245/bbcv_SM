<%@ page contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="测试" />
	<script type="text/javascript">
		var _basePath = '<%=basePath %>msi/';
	</script>
	<script src="<%=basePath %>js/jquery-1.7.1.min.js" type=text/javascript></script>
	<script src="<%=basePath %>js/util/md5.js" type=text/javascript></script>
	<script src="<%=basePath %>js/util/json2.js" type=text/javascript></script>
	<script src="<%=basePath %>js/msi1.js" type=text/javascript></script>
	<script src="<%=basePath %>js/test1.js" type=text/javascript></script>
	<title>测试</title>
</head>
<body>
	移动接入平台接口测试：<br /><br />
	公共参数：<br />
	app_name:<input type="text" value="KYSX" id="app_name" /><br />
	licence:<input type="text" value="KYSX1234" id="licence" /><br />
	version:<input type="text" value="1" id="version" /><br />
	username:<input type="text" value="13812345678" id="username" /><br />
	passwd:<input type="text" value="123456" id="passwd" /><br />
	vod_page:<input type="text" value="http://192.168.100.11/multiscreen/mobile/vodDetail/voddetail1.html?name=aqgy" id="vod_page" /><br />
	stream_id:<input type="text" value="1" id="stream_id" /><br />
	rtsp_url:<input type="text" value="rtsp://192.168.100.11:8845/yjy_ipqam/8081/ypzjs.ts" id="rtsp_url" /><br />
	vodname:<input type="text" value="营盘镇警事" id="vodname" /><br />
	posterurl:<input type="text" value="/home/bbcv/ypzjs.jpg" id="posterurl" /><br />
	token:<input type="text"  id="token" /><br />
	ret_code:<input type="text"  id="ret_code" /><br />
	new_token:<input type="text"  id="new_token" /><br />
	
	<HR width="100%" color="#987cb9" SIZE=5 />
	<!--  
	<input type="button" value="用户注册" onclick="javascript:test_user_register_req();"/>
	result:<textarea rows="5" cols="150" id="user_register_req_res"></textarea><br /><br />
	
	<input type="button" value="用户接入" onclick="javascript:test_user_access_req();"/>
	result:<textarea rows="5" cols="150" id="user_access_req_res"></textarea><br /><br />
	-->
	<input type="button" value="用户登陆" onclick="javascript:test_user_login_req();"/>
	result:<textarea rows="5" cols="150" id="user_login_req_res"></textarea><br /><br />
	
	<input type="button" value="频道绑定" onclick="javascript:test_user_bind_req();"/>
	result:<textarea rows="5" cols="150" id="user_bind_req_res"></textarea><br /><br />
	
	<input type="button" value="主动绑定" onclick="javascript:test_user_unbind_req();"/>
	result:<textarea rows="5" cols="150" id="user_unbind_req_res"></textarea><br /><br />
	
	<input type="button" value="VOD点播" onclick="javascript:test_user_vodplay_req();"/>
	result:<textarea rows="5" cols="150" id="user_vodplay_req_res"></textarea><br /><br />
	
	<input type="button" value="用户状态查询" onclick="javascript:test_user_sessionquery_req();"/>
	result:<textarea rows="5" cols="150" id="user_sessionquery_req_res"></textarea><br /><br />
	
	<HR width="100%" color="#985479" SIZE=2 />
	开始时间：<input type="text" id="begintime"/>
	<input type="button" value="选时播放测试" onclick="javascript:test_user_choosetime_req();"/>
	result:<textarea rows="5" cols="150" id="user_choosetime_req_res"></textarea><br /><br />
	
	<HR width="100%" color="#987cb9" SIZE=5 />
	
<!-- 	<input type="button" value="键值下发" onclick="javascript:test_key_send_req();"/> -->
	键值下发:
	键值类型：<input type="text" value="1001" id="key_key_type" size="20"/>
	<input type="button" value="上" onclick="javascript:test_key_send_req('0x00');"/>
	<input type="button" value="下" onclick="javascript:test_key_send_req('0x01');"/>
	<input type="button" value="左" onclick="javascript:test_key_send_req('0x03');"/>
	<input type="button" value="右" onclick="javascript:test_key_send_req('0x02');"/><br />
	<input type="button" value="待机" onclick="javascript:test_key_send_req('0x0a');"/>
	<input type="button" value="静音" onclick="javascript:test_key_send_req('0x0c');"/>
	<input type="button" value="确定" onclick="javascript:test_key_send_req('0x1f');"/>
	<input type="button" value="退出" onclick="javascript:test_key_send_req('0x1d');"/><br />
	
	<input type="button" value="快退" onclick="javascript:test_key_send_req('0x04');"/>
	<input type="button" value="快进" onclick="javascript:test_key_send_req('0x08');"/><br />
	
	<input type="button" value="音量减小" onclick="javascript:test_key_send_req('0x0b');"/>
	<input type="button" value="音量增加" onclick="javascript:test_key_send_req('0x06');"/><br />
	
	
	<input type="button" value="1" onclick="javascript:test_key_send_req('0x11');"/>
	<input type="button" value="2" onclick="javascript:test_key_send_req('0x12');"/>
	<input type="button" value="3" onclick="javascript:test_key_send_req('0x13');"/><br />
	
	<input type="button" value="4" onclick="javascript:test_key_send_req('0x14');"/>
	<input type="button" value="5" onclick="javascript:test_key_send_req('0x15');"/>
	<input type="button" value="6" onclick="javascript:test_key_send_req('0x16');"/><br />
	
	<input type="button" value="7" onclick="javascript:test_key_send_req('0x17');"/>
	<input type="button" value="8" onclick="javascript:test_key_send_req('0x18');"/>
	<input type="button" value="9" onclick="javascript:test_key_send_req('0x19');"/><br />
	
	<input type="button" value="0" onclick="javascript:test_key_send_req('0x10');"/><br />
	result:<textarea rows="5" cols="150" id="key_send_req_res"></textarea><br /><br />
	<HR width="100%" color="#987cb9" SIZE=5 />
</body>
</html>