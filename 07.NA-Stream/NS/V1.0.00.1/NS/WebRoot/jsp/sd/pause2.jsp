<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
<% base = base+"RCM"+"/"; %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html charset=utf-8">
<script src="js/jquery-1.11.0.min.js" language="javascript" type="text/javascript"></script>

<% String  vodname =request.getParameter("vodname");%>
<% String  posterurl =request.getParameter("posterurl");%>


	  	<% String  username =request.getParameter("username");	
		String  poneOrWeiXin =request.getParameter("username");
 	String  username1 =username; 	
	if(username==null){
		username="";
	}
	%>
<style>
body,div,h1,h2,i,span,ul,li,a,p{margin:0px;padding:0px;}
body{	
	width:720px;
	height:576px;
	overflow: hidden;
	font-size: 20px;
	font-family: "黑体";
	color:#000;
}
	.advert_page{
	width:720px;
	height:576px;
	background:url("images/advert_bg.jpg") no-repeat;
	overflow: hidden;
	background-size:720px 576px;
}
.contair{
	height:348px;
	margin:102px 0px 0px 65px;
}
.play_advert{
	width:438px; 
	height:358px;
	float:left;
}
.ing_player{
	float:left;
	width:164px;
	margin-left:22px;
	height:358px;
	overflow:hidden;
}
.title{
	text-align:center;
	white-space:nowrap;
	font-size: 28px;
	color:#fff;
	line-height:52px;
	height:52px;
	
}

.poster{
	width:136px;
	height:203px;
	margin-left: 30px;
}
.poster img{
	width:103px;
	height:220px;
}

.advert{
	width:438px;
	height:356px;
}
.marquee{
	margin:18px 0px 0px 25px;
	width:400px;
	height:45px;
	font-size: 38px;
	color:#fff;
	overflow: hidden;
	
}
.advert video{
	width:436px;
	height:285px;
}
.runtext img{
	margin-bottom: -6px;
	width:26px;
	height:26px;
}
.tit{
	height:58px;
	margin:42px 0px 16px 0px;
	margin-left:150px;
}
.tits{
	height:58px;
	margin:42px 0px 16px 0px;
}
</style>
</head>
<body>
	<div class="advert_page">
		<div class="contair">
			<div class="play_advert">
				<div class="advert">
					<video  autoplay="true" ></video>
				</div>
				<div class="marquee">
					<div class="runtext">
						<div id ="scroll" style="width:1200px;margin-left:100px;"><span id="userType"></span>暂停操作中,请按<img src="images/button.png">继续操作(<span id="endtime"><span style="color:darkorange">30</span></span>)</div>
					</div>
				</div>
			</div>
			<div class="ing_player">
				<h1 class="title"></h1>
					<div class="poster">
						<img/>
					</div>
			</div>
		</div>
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
		
	
<script src="<%= base%>res/data/hdtvpausedata.js" ></script>
<script type="text/javascript">
	var web="<%= base%>res/data/pause_ad_data.js"
	$.getJSON(web).then(function(json){
	 insert(json);
	},
	function(){
	console.log(arguments)
	});	

	var vodname = "<%=vodname%>";
	var posterurl ="<%=posterurl%>";

	var vodeosrc = data;
	
	function insert(json){
		$(json).each(function(index){
				$("h1").html("《"+vodname+"》");
				$(".advert").find("video").attr("src",vodeosrc);
				$(".poster").find("img").attr("src",posterurl)
			})
			if($(".title").html().length>=7)
				$("h1").wrap("<div class='tit'/>")
			else
				$("h1").wrap("<div class='tits'/>")
				
	}
	
	var tmpDelay=20000,$tmTip=$("#scroll");
	$(function(){
		marquee();
		tit()
		setInterval(tit,600)
		setInterval(marquee,tmpDelay);
		
	})
	function marquee(){
		$tmTip.fadeIn(function() {
			$tmTip.animate({marginLeft: '-910'}, tmpDelay, 'linear',function() {
				$tmTip.css("marginLeft","400px");
			});
		});
	}
	function tit(){
		$(".tit").fadeIn(function(){
			$(".tit").animate({marginLeft:"-450"},20000,function(){
				$(".tit").css("margin-left","150px")
			})
		})
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