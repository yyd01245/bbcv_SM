<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>高清暂停页</title>
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
	font-size:38px;	
	color:#fff;
	text-shadow:0 1px #000;
	font-weight:900
}
.container{
	width:1280px;
	height:720px;
	background:url(sad.jpg);
	overflow:hidden;
}
p{
	padding: 200px 0 0 150px;
	/* width: 350px; */
	line-height: 250%;
	letter-spacing: 2px;
}
.logo{
	position: absolute;
	left: 10px;
	top: 20px;
	font-size: 26px;
	font-weight:bold;
	color:#fff;
	//text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #fff, 0 0 40px #ff00de, 0 0 70px #ff00de;
	text-shadow: 1px 1px rgba(197, 223, 248,0.8),2px 2px rgba(197, 223, 248,0.8),3px 3px rgba(197, 223, 248,0.8),4px 4px rgba(197, 223, 248,0.8),5px 5px rgba(197, 223, 248,0.8),6px 6px rgba(197, 223, 248,0.8);
}
.video{
	position: absolute;
	left: 375px;
	top: 162px;
	width:532px;
	height:366px;
}
.video video{
	width:532px;
	height:366px;
}
</style>
  </head>
  
 <body>

<div class="container">
<div class="logo">单向终端云点播</div>
<!--<div class="video">
	<video src="video/gyc.mp4" autoplay="true" loop poster="img/vposter.jpg"></video>
</div>-->
  	<% String  username =request.getParameter("username");	
  	String  poneOrWeiXin =request.getParameter("username");
     	String 	 username2 =username;	
 
  	
	if(username==null){
		username="";
	}
	%>
	<p style="padding-left: 81px;"></p>
     <h5 align="center"><span id="userType"></span>暂停超时，你可在<span id="endtime"></span>之前再次免费点播</h5>
</div>

<script type="text/javascript">
 
  var reg = /^(13[0-9]|14(5|7)|15(0|1|2|3|5|6|7|8|9)|18[0-9])\d{8}$/;
  var poneOrWeiXin = "<%=poneOrWeiXin%>";
  
  if(reg.test(poneOrWeiXin)){// 手机用户
     var mphone =poneOrWeiXin.substring(0,3);   
     var mphone1 =poneOrWeiXin.substring(7,11);
     var u = mphone+"****"+mphone1;
	  document.getElementById("userType").innerHTML = "手机用户("+u+")";  
   }else{
	  document.getElementById("userType").innerHTML = "微信用户(<%=username%>)";
  }
   
</script>

<script type="text/javascript">
var CID = "endtime";
document.getElementById(CID).innerHTML = getDay(5);


function getDay(day){
	   var today = new Date();
	   
	   var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;        

	   today.setTime(targetday_milliseconds); //注意，这行是关键代码  
	   
	   var tYear = today.getFullYear();
	   var tMonth = today.getMonth();
	   var tDate = today.getDate();
	   tMonth = doHandleMonth(tMonth + 1);
	   tDate = doHandleMonth(tDate);
	   return tMonth+"月"+tDate+"号";
}
function doHandleMonth(month){
	   var m = month;
	   if(month.toString().length == 1){
	      m = "0" + month;
	   }
	   return m;
}


function RemainTime(){
     	<% String  streamid =request.getParameter("streamid");%> 
         window.location.href="PauseServlet?streamid=<%=streamid%>&username=<%=username2%>"; 
}

setTimeout("RemainTime()",4000);
</script>
</body>
</html>