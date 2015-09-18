<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'tanchu.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link rel="stylesheet" href="http://static.bshare.cn/css/bshare2.css?t=20140730.css" type="text/css" />
    <style type="text/css">
    	.top-menu-popup .bLink { color: #333; }
    </style>
    
    <link rel="icon" href="http://www.bshare.cn/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="http://www.bshare.cn/favicon.ico" type="image/x-icon" />

  
<style> 
  .black_overlay{  
  
  display: none;  
  position: absolute; 
   top: 0%;  
   left: 0%; 
   width: 100%;  
   height: 100%;
   background-color: black;
  z-index:1001;  -moz-opacity: 0.8; 
   opacity:.80;  
  filter: alpha(opacity=80);  } 




.white_content {
display: none;
position: absolute;
top: 25%;
left: 25%;
width: 31%;
height: 20%;
/* padding: 16px; */
border: 3px solid rgb(168, 163, 155);
background-color: white;
z-index: 1002;
overflow: auto;
}

.guanbi {

position: absolute;
top: 5%;
left: 50%;
width: 50%;
height: 50%;
 margin-top: 30px;
}

   </style> 
</head> 
<body> 


------------------------------
<a class="bshareDiv" href="http://www.bshare.cn/share">分享按钮</a><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#uuid=&amp;style=10&amp;bgcolor=Grey&amp;ssc=false&amp;pophcol=1"></script>
--------------------------

<a class="bshareDiv" href="http://www.bshare.cn/share">分享按钮</a>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#uuid=<你的UUID>&style=10&bp=qqxiaoyou,twitter,facebook,sohuminiblog,renren, ifengmb,neteasemb,sinaminiblog,kaixin001,tianya,qzone"></script>





-------------------------


<a class="bshareDiv" href="http://www.bshare.cn/share">分享按钮</a><script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#uuid=&amp;style=10&amp;bgcolor=Green"></script>
              














------------------------------------------------------
<p>可以根据自己要求修改css样式<a href="javascript:void(0)" onclick="document.getElementById('light').style.display='block';document.getElementById('fade').style.display='block'">点击这里打开窗口</a></p> 
<div id="light" class="white_content" > 
   
<div class="bshare-custom">
<div class="bsPromo bsPromo2"></div><a title="分享到" href="http://www.bShare.cn/" id="bshare-shareto" class="bshare-more">分享到</a><a title="分享到QQ空间" class="bshare-qzone">QQ空间</a><a title="分享到新浪微博" class="bshare-sinaminiblog">新浪微博</a><a title="分享到人人网" class="bshare-renren">人人网</a><a title="分享到腾讯微博" class="bshare-qqmb">腾讯微博</a><a title="分享到网易微博" class="bshare-neteasemb">网易微博</a><a href="#"></a></div>

<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=3&amp;lang=zh"></script>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=2&amp;lang=zh">
</script>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>

   
    <a class="guanbi" href="javascript:void(0)" onclick="document.getElementById('light').style.display='none';document.getElementById('fade').style.display='none'"> 
    关闭</a></div> 
<div id="fade" class="black_overlay"> 
</div> 
</body> 
</html>
