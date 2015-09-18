<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
<% base = base+"RCM"+"/"; %>

<% response.setCharacterEncoding("UTF-8"); %>

<% response.setHeader("content-type","text/html;charset=UTF-8"); %>

 <% String  username = request.getParameter("username"); %>
  <% String  streamid = request.getParameter("streamid"); %>
   <% String  resolution = request.getParameter("resolution"); %> 


 <%Logger logger = LoggerFactory.getLogger("");
 
 
 logger.info("【手机主页日志】"); 
 logger.info("username="+username+""); 
 logger.info("streamid="+streamid+""); 
 logger.info("resolution="+resolution+""); 
 %>

<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<title>点播首页</title>
<link href="css/layout.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>

 <%
    String dataFile = "";
    String spFile ="";
    if("0".equals(resolution)){dataFile="hdmbdata.js";spFile="hdmbspdata.js";}
    if("1".equals(resolution)){dataFile="sdmbdata.js";spFile="sdmbspdata.js";}
 %>
<script type="text/javascript" src="<%= base%>res/data/<%=dataFile%>" ></script>
<script type="text/javascript" src="<%= base%>res/data/<%=spFile%>" ></script>
</head>

<body>
<%--<div id="header" >--%>
<%--		<h1 class="menu logo">目录</h1>--%>
<%--		<h1 class="usr logo">首页</h1>        --%>
<%--</div>--%>
<!--slider-begin-->
<div class="flexslider">
	<ul class="slides">    
		<!--<li class=""><a href="voddetail.html?name=gyc"><img alt="" src="images/gyc-big.jpg"/></a></li>
		<li class=""><a href="voddetail.html?name=aqgy"><img alt="" src="images/aqgy-big.jpeg"/></a></li>
		<li class=""><a href="voddetail.html?name=ypzjs"><img alt="" src="images/ypzjs-big.jpg"/></a></li>-->
	</ul>
</div>
<!--slider-over-->

 <section class="homesection clone">
	 <div class="sectionheader">
			<h3>推荐</h3>
			<a class="more" href="#"></a>
	 </div>
	 <ul class="gallery">
<!--		 <li><a href="#" onclick="vodChange('gyc')"><img src="images/gyc.jpg"><h3>关云长</h3><h3><small>迷失的刀郎 之 狼爱上羊</small></h3></a></li>-->
	 </ul>
 </section>

 <script src="js/jquery.flexslider-min.js"></script>
 <script>
	$(document).ready(function() {
		//添加电影内容
		$.each(data,function(i,ele){
			var newfilm = $(".homesection.clone").clone().removeClass("clone");
			newfilm.find(".sectionheader h3").text(ele.class);
			newfilm.find(".sectionheader a.more").attr("href",ele.morelink+"&username=<%=username%>&streamid=<%=streamid%>&resolution=<%=resolution%>");
			var source = ele.resource,htmlcontent="";
			source.forEach(function(element){
				htmlcontent+='<li><a href="<%=basePath%>mobile/ShowVodInfo?flag=set&resolution=<%=resolution%>&username=<%=username%>&streamid=<%=streamid%>&vodid='+element.id+'&name='+element.id+'"+"><img src="'+element.poster+'"><h3>'+element.name+'</h3><h3><small>迷失的刀郎 之 狼爱上羊</small></h3></a></li>';
			})	
			newfilm.find(".gallery").append($(htmlcontent));
			$("body").append(newfilm);			
		})
	
		//添加跑马灯内容i.id
		var sliderhtml="";
		sliderdata.forEach(function(i){
			sliderhtml+='<li class=""><a href="<%=basePath%>mobile/ShowVodInfo?flag=set&resolution=<%=resolution%>&username=<%=username%>&streamid=<%=streamid%>&name='+i.id+'&vodid='+i.id+'"><img alt="'+i.name+'" src="'+i.sliderposter+'"/></a></li>';
		})		
		$('.flexslider .slides').empty().html(sliderhtml);
		
		/*添加跑马灯效果*/	
		$('.flexslider').flexslider({		
			animation: "slide",              //String: Select your animation type, "fade" or "slide"
			selector: ".slides > li",       //{NEW} Selector: Must match a simple pattern. '{container} > {slide}' -- Ignore pattern at your own peril
			startAt: 0,                     //Integer: The slide that the slider should start 
			slideshowSpeed: 5000,           //Integer: Set the speed of the slideshow cycling, in milliseconds
			animationSpeed: 800             //Integer: Set the speed of animations, in milliseconds
		});
	})
 </script>
</body>
</html>
