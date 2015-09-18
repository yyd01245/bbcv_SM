<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<% String username=request.getParameter("username"); %>
<% String streamid=request.getParameter("streamid"); %>
<% String vodid=request.getParameter("vodid"); %>

 <%Logger logger = LoggerFactory.getLogger("");
 logger.info("【TV详情页日志】");
   logger.info("username="+username+"");
   logger.info("streamid="+streamid+"");
   logger.info("vodid="+vodid+"");
   logger.info("resolution="+0+"");
   %>
<html>
<head>
<script>

 function queryStatus(){    

     $.post("<%=basePath%>mobile/ShowVodInfo?flag=getnow&streamid=<%=streamid%>&vodid=<%=vodid%>&username=<%=username%>&resolution=0", function(returndata) {
    	   if(returndata!=""&&"error"!=returndata.trim()&&returndata.trim()!=""){
    		    window.location.href=returndata;
    	        }	
 	});	
   
	}	
// queryStatus();
setInterval("queryStatus();",1000); 
    
</script>
<meta http-equiv="Content-Type" content="text/html charset=utf-8">
<%--<script src="js/jquery-1.11.0.min.js" language="javascript" type="text/javascript"></script>--%>

<script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>

<style>
body,div,h1,h2,i,span,ul,li,a,p{margin:0px;padding:0px;}
body{	
	wi dth:720px;
	height:576px;
	overflow: hidden;
	font-size: 20px;
	font-family: "黑体";
	color:#000;
}
	.main{
	width:720px;
	height:576px;
	background:url("images/replace_bg.jpg") no-repeat;
	background-size: 720px 576px;
	overflow: hidden;
}
.movie_main{
	width:178px;
	height:379px;
	margin: 103px 0px 0px 123px;
	float:left;
}
.movie_main img{
	width:178px;
	height:383px;
	border-radius: 6px;
	 -webkit-box-reflect: below 8px -webkit-linear-gradient(top,rgba(250,250,250,0),rgba(250,250,250,.0) 70%,rgba(250,250,250,0.3));
	 border:6px solid #fff;
	 margin: -6px;

}
.movie_introduce{
	margin:94px 0px 0px 28px;
	float:left;
}
.title_main{
	font-size: 32px;
	float: left;
	margin: 0px;
	height:40px;
	font-weight: 2;
}
.time{
	font-size: 22px;
	padding-top: 12px;
	float:right;
	font-weight: bold;
	text-indent: 10px;
	top:0px;
	right:26px;
}
.actor{
	line-height:26px;
	padding-top: 8px;
	clear:both;
}
.actor li{
	list-style: none;
	font-size: 16px;
}
.boldp{
	font-size: 20px;
	font-weight: bold;
	clear:both;
	padding-top: 12px;
	height:24px;
}
.plot{
	text-indent: 3em;
	font-size: 16px;
	line-height: 24px;
	margin-top: 5px;
	height:150px;
	width:278px;
}
.go_maintext{
	height:44px;
	font-size: 35px;
	width:260px;
	color:#ff4747;
	overflow:hidden;
	line-height:44px;
}
.tit{
	width:190px;
	height:40px;
	overflow:hidden;
}
#do{
	float:left;
	margin-left:190px;
}
</style>

  	<%  	
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
</head>
<body>
	<div class="main">
		<div class="movie_main">
			<img></img>
		</div>
		
		<div class="movie_introduce">
		<span class="time"></span>
		<div class="tit">
			<div class="scroll_ornot">
				<h1 class="title_main"></h1>
			</div>
		</div>
			<ul class="actor">
				<li></li>
				<li></li>
				<li></li>
			</ul>
		<p class="boldp">剧情：</p>
		<div class="plot"></div>
		<div class="go_maintext">
			<div id="scroll" style="width:900px;margin-left:200px;">用户<span><%=username%></span>正在选择中</div>
		</div>
		</div>
	</div>
	<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
     <% base = base+"RCM"+"/"; %>
     <script type="text/javascript" src="<%= base%>res/data/sdmbdata.js" ></script>
	<script type="text/javascript">

	var index=-1;
	var currentEle;
	var flag =true;
	var ii = 0;
	var strid = <%=vodid%>;
	$(function(){
		changeMovie(strid);
		//timer=setInterval("changeMovie(checkIndex())",90000);
	})

function changeMovie(moviename){

		
		while(flag){
			//alert("第"+(ii+1)+"次循环");
			$.each(data[ii].resource,function(i,ele){

				//alert("内部第"+(i+1)+"次循环 "+ele.name);
				if(ele.id==moviename){
					currentEle=ele;
					if(index == -1){
						index = parseInt(i);
					}
				
					flag=false;
					return;
				}else{
					
				}
			})
			ii = ii+1;     
		}

		if(currentEle){

			$("h1").html(currentEle.name);
			$(".movie_main").find("img").attr("src",currentEle.poster)
			$(".time").html("("+(currentEle.ondata).substr(0,4)+")");
			$(".actor li:first").html("主演:  "+currentEle.actor);
			$(".actor li:eq(1)").html("导演:  "+currentEle.director);
			$(".actor li:eq(2)").html("国家:  "+currentEle.area);
			$(".plot").html((currentEle.movieintro).substr(0,82)+"...");
			

		}

	}














	
<%--	var web="js/movie.js"--%>
<%--	$.getJSON(web).then(function(json){--%>
<%--	 insert(json);--%>
<%--	},--%>
<%--	function(){--%>
<%--	console.log(arguments)--%>
<%--	});	--%>
<%--	function insert(json){--%>
<%--		$(json).each(function(index){--%>
<%--				$("h1").html(json[0].name);--%>
<%--				$(".movie_main").find("img").attr("src",json[0].poster)--%>
<%--				$(".time").html("("+(json[0].ondata).substr(0,4)+")");--%>
<%--				$(".actor li:first").html("主演:  "+json[0].actor);--%>
<%--				$(".actor li:eq(1)").html("导演:  "+json[0].director);--%>
<%--				$(".actor li:eq(2)").html("国家:  "+json[0].area);--%>
<%--				$(".plot").html((json[0].movieintro).substr(0,82)+"...");--%>
<%--			})--%>
<%--			var lenvel=$("h1").html().length--%>
<%--			if(lenvel>=6){--%>
<%--				$(".scroll_ornot").attr("id","do");--%>
<%--				$(".time").css({--%>
<%--					"float":"right",--%>
<%--					"position":"relative"--%>
<%--				})	--%>
<%--			}--%>
<%--			else{--%>
<%--				$('.time').insertAfter('h1');--%>
<%--				$(".tit").css("width","250px")--%>
<%--				$(".time").css({--%>
<%--					"float":"left"--%>
<%----%>
<%--				})	--%>
<%--				--%>
<%--			}--%>
<%--	}	--%>
	var tmpDelay=25000,$tmTip=$("#scroll");
	$(function(){
		dodo();
		marquee();
		setInterval(dodo)
		setInterval(marquee,tmpDelay);
		
	})
	function marquee(){
		$tmTip.fadeIn(function() {
			$tmTip.animate({marginLeft: '-600'}, tmpDelay, 'linear',function() {
				$tmTip.css("marginLeft","250px");
			});
		});
	}
	function dodo(){
		$("#do").fadeIn(function() {
			$("#do").animate({marginLeft: '-300'}, tmpDelay, 'linear',function() {
				$("#do").css("marginLeft","190px");
			});
		});
	}

	var relocate = setTimeout(redirect,20000);
	function redirect(){
		window.location.href="subinter1.jsp?username=<%=username1%>&streamid=<%=streamid%>&vodid=<%=vodid%>";
	}
	
	</script>
</body>
</html>