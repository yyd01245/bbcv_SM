<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>pause1.jsp page</title>
    
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
	background:url(exit.jpg);
	overflow:hidden;
}
p{
    padding-top: 0px;

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
.playbtn{
	display: inline-block;
	width: 30px;
	height: 30px;
	background: url(img/playbtn.png) left center no-repeat;
	background-size: 30px 30px;
	margin:0px 5px;
	vertical-align: text-top;
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
     	String 	 username2 =username;	
 
  	
	if(username==null){
		username="";
	}else{
		
		if(username.length()==11){
			  
				String str = username.substring(0, 3);				
				String str1 = username.substring(7,11);
				username= str+"****"+str1;
						
		}
		
	}
	
	%>
	<p align="center" style="font-size: 20;padding-left: 5px;padding-top: 540px;"><span id="endtime">9</span>秒后自动结束播放</p>
<%--	<p align="center" style="--%>
<%--    padding-top: 0px;">按退出键退出，按其他键继续观看内容</p>--%>



</div>

<script type="text/javascript">
var CID = "endtime";
if(window.CID != null)
{
    var iTime = document.getElementById(CID).innerText;
    var Account;
    RemainTime();
}
function RemainTime()
{

    var sSecond="",sTime="";
    if (iTime >= 0)
    {
    
      
        if (iTime >= 0){
            sSecond = iTime;
            sTime="<span style='color:darkorange'>" + sSecond + "</font>";
        }

        if(iTime==0){
            clearTimeout(Account);
        	<% String  streamid =request.getParameter("streamid");%> 
        	  window.location.href="QuitServlet?streamid=<%=streamid%>&username=<%=username2%>"; 

          }
        else
        {
            Account = setTimeout("RemainTime()",1000);
        }
        iTime=iTime-1;
    }
    else
    {
            sTime="<span style='color:red'>倒计时结束！</span>";
    }
    document.getElementById(CID).innerHTML = sTime;
}


</script>
</body>
</html>