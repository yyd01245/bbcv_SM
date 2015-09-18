<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>subinter1.jsp</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style>
*{	padding:0;
	margin:0;}
body{
	margin:0;
	padding:0;
	font-size:28px;	
}
.container{
	width:1280px;
	height:720px;
	position:relative;
	background:#00f url("img/ad.jpg") no-repeat;
	overflow:hidden;
}
.container p.text{
	width: 1280px;
	height: 100px;
	position:absolute;
	left:0;
	bottom:0;
	text-align: center;	
	text-shadow: 0 1px 1px #000;
}
.container p.text a{
	color: #fff;
	font-weight: 900;
	font-size: 40px;
	text-decoration: none;
	<!--box-shadow: 0 0 20px 5px #fff;-->	
}
</style>
  </head>
  
  <body>
<div class="container">
	<p class="text">

		<a href="#"><span style='color:red'>绑定超时！</span></a>
	</p>
</div>



  </body>
</html>
