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
	username:<input type="text" value="ock_dfedse" id="username" /><br />
	nickname:<input type="text" value="正太" id="nickname" /><br />
	passwd:<input type="text" value="123456" id="passwd" /><br />
	token:<input type="text"  id="token" /><br />
	<HR width="100%" color="#987cb9" SIZE=5 />
	
	<input type="button" value="用户注册" onclick="javascript:test_user_register_req();"/>
	result:<textarea rows="5" cols="150" id="user_register_req_res"></textarea><br /><br />
	
	<input type="button" value="修改昵称" onclick="javascript:test_user_updateNickname_req();"/>
	result:<textarea rows="5" cols="150" id="user_updateNickname_req_res"></textarea><br /><br />
	
	<input type="button" value="用户接入" onclick="javascript:test_user_access_req();"/>
	result:<textarea rows="5" cols="150" id="user_access_req_res"></textarea><br /><br />
</body>
</html>