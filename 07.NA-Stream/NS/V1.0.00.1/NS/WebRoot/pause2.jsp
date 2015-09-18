<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
<% base = base+"RCM"+"/"; %>
<html>
<head>
    <title>高清暂停页</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" style="text/css" href="css/style1.css"> 
<script src="voddetail/js/jquery-1.11.0.min.js" language="javascript"  type="text/javascript"></script>

<% String  vodname =request.getParameter("vodname");%>
<% String  posterurl =request.getParameter("posterurl");%>




</head>
	  	<% String  username =request.getParameter("username");	
	  	String  poneOrWeiXin =request.getParameter("username");
 	String  username1 =username; 	
	if(username==null){
		username="";
	}
	%>


 <%Logger logger = LoggerFactory.getLogger("");
 
 
 logger.info("【高清暂停页日志】"); 
 logger.info("username="+username1+""); 
 logger.info("vodname="+vodname+""); 
 logger.info("posterurl="+posterurl+""); 
 %>

<body>
	
	<div class="advert_page">
		<div class="contair">
			<div class="play_advert">
				<div class="advert">
					<video  autoplay="true" ></video>
				</div>
				<div class="marquee">
					<div class="runtext">
							<marquee scrollamount=2 height=38><span id="userType"></span>暂停操作中,请按<img src="images/button.png">继续操作(<span id="endtime"><span style="color:darkorange">300</span></span>)</marquee>
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
				if($(".title").html().length>=9)
				$("h1").wrap("<div class='tit'/>")
		else
				$("h1").wrap("<div class='tits'/>")
	}

	var tmpDelay=10000,$tmTip=$("#scroll");
	$(function(){
		marquee();
		tit()
		setInterval(tit,60)
		setInterval(marquee,tmpDelay);
		
	})
	function marquee(){
		$tmTip.fadeIn(function() {
			$tmTip.animate({marginLeft: '-700'}, tmpDelay, 'linear',function() {
				$tmTip.css("marginLeft","700px");
			});
		});
	}
	function tit(){
		$(".tit").fadeIn(function(){
			$(".tit").animate({marginLeft:"-400"},10000,function(){
				$(".tit").css("margin-left","288px")
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