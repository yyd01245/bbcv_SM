<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>download page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0;" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style>
*{
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
}
body{
	margin:0;
	padding:0;
	background:#fff;
	color:#333;
}
.col333{color:#333;}
.col666{color:#666;}
.col999{color:#999;}
.container{
	background:#ccc;
}
.left{
	float:left;
	width:35%;
	padding:85px 0 0 25px;
}
.left img,.left .img{
	width:80px;
	height:80px;
}
.left .title{
	
}
.right{
	margin-left:35%;
	position:relative;
	padding:10px;
}
p.text{
	letter-spacing: 2px;
	line-height: 1.2;
}
</style>
	
	
  </head>
  

<script type="text/javascript">
window.onload = function () {

var u = navigator.userAgent;
if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {//安卓手机

	document.getElementById("andiv").style.display="";
	document.getElementById("apdiv").style.display="none";

} else if (u.indexOf('iPhone') > -1) {//苹果手机
	document.getElementById("apdiv").style.display="";
	document.getElementById("andiv").style.display="none";

} else if (u.indexOf('Windows Phone') > -1) {//winphone手机

}
}

   function androiddownload(){
   
   window.location.href="<%=basePath%>download/单向终端云点播客户端.apk"; 
   
   }

   function iosdownload(){
   
	   window.location.href="http://www.pgyer.com/T2CY"; 
   
   }


</script>




  <body>
  <div class="container">
	<div class="left">
		<div class="img">
			<img src="img/logo.png"/>			
		</div>
		<p class="text "><h3 class="title">宽讯云</h3></p>
	</div>
	<div class="right">
		<div class="intro">			
			<p class="text "><span class="col999">类别</span><span class="detail col666"> 客户端</span></p>
			<p class="text "><span class="col999">更新日期</span><span class="detail col666"> 2014年10月28日</span></p>
			<p class="text "><span class="col999">版本</span><span class="detail col666"> 1.01</span></p>
			<p class="text "><span class="col999">大小</span><span class="detail col666"> 5.3MB</span></p>
			<p class="text "><span class="col999">语言</span><span class="detail col666"> 中文</span></p>
			<p class="text "><span class="col999">开发商</span><span class="detail col666"> 杭州宽云视讯科技有限公司</span></p>
			
<div id="apdiv" style="display: none" >  
	<input type="button" onclick="iosdownload();" value="iPhone下载"/>

</div>
 <div id="andiv" style="display: none" > 
 
 	<input type="button" onclick="androiddownload();" value="android下载"/> </div>
			
		
			<h4>内容提要</h4>
			<p class="text">单向终端云点播简介：</p>
		</div>
	</div>
</div>


<script>

</script>
</body>
</html>