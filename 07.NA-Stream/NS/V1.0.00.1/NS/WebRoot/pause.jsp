<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>pause.jsp page</title>
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
	color:#000;
	text-shadow:0 1px #000;
	font:900 30px "Microsoft YaHei"
}
.container{
	width:1280px;
	height:720px;
	background:url(img/newbg2.jpg);
	overflow:hidden;
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
	left: 187px;
	top: 78px;
	width:894px;
	height:576px;
}
.video video{
	width:894px;
	height:530px;
}
.video div{
	width: 780px;
	margin: 0px 0 0 30px;
	letter-spacing: 2px;
	overflow:hidden;
	height:38px;
	line-height:38px;
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
.video div #info{
	height: 38px;
	margin-left:780px;
}
</style>
  </head>
  
 <body>
 
 <% String  vodname =request.getParameter("vodname");	%>
 <% String  posterurl =request.getParameter("posterurl");	%>
 
 <div class="container">
<!--<div class="logo">单向终端云点播</div>-->
<div class="video">
	<video src="video/philips.mp4" autoplay="true" loop poster="img/newposter.jpg"></video>
	  	<% String  username =request.getParameter("username");	 	
 	String  username1 =username; 	
	if(username==null){
		username="";
	}else{	
		if(username.length()==11){			  
				String str = username.substring(0, 3);				
				String str1 = username.substring(7,11);
				username= str+"****"+str1	;				
		}		
	}	
	%>
	
	<marquee scrollamount="5" width="780" style="
    padding-left: 77px;
    padding-right: 0px;
    border-left-width: 0px;
    margin-left: 18px;
">用户(<%=username%>)暂停中，vodname=<%=vodname %>,posterurl=<%=posterurl%>请按<i class="playbtn"></i>键继续操作(<span id="endtime"><span style="color:darkorange">300</span></span>)</marquee>


</div>	
</div>
 
 
 
<%-- <div class="container">--%>
<%--<div class="logo">手机点播</div>--%>
<%--<div class="video">--%>
<%--	<video src="video/gg.mp4" autoplay="autoplay"  loop poster="img/vposter.jpg"></video>--%>
<%--</div>--%>
<%--	<p class="text">--%>
<%--  	<% String  username =request.getParameter("username");	 	--%>
<%-- 	String  username1 =username; 	--%>
<%--	if(username==null){--%>
<%--		username="";--%>
<%--	}else{	--%>
<%--		if(username.length()==11){			  --%>
<%--				String str = username.substring(0, 3);				--%>
<%--				String str1 = username.substring(7,11);--%>
<%--				username= str+"****"+str1;				--%>
<%--		}		--%>
<%--	}	--%>
<%--	%>--%>
<%--		<a href="#">用户（<%=username%>）暂停中，请按OK继续(<span id="endtime">300</span>)</a>--%>
<%--	</p>--%>
<%--</div>--%>


<script src='js/jquery-latest.js'></script>
<script type="text/javascript">



var tmpDelay=16000,$tmTip=$("#info");

$(function(){
	marquee();
	setInterval(marquee,tmpDelay);
})
/*循环提示不同的问题在内容*/
function marquee(){
		$tmTip.fadeIn(function() {
			$tmTip.animate({marginLeft: '-780'}, tmpDelay, 'linear',function() {
				$tmTip.css("marginLeft","780px");
			});
		});
}


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
            sSecond = iTime + "秒";
            sTime="<span style='color:darkorange'>" + sSecond + "</font>";
        }

        if(iTime==0){
            clearTimeout(Account);
        	<% String  streamid =request.getParameter("streamid");%> 

         window.location.href="pause1.jsp?streamid=<%=streamid%>&username=<%=username1%>";
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