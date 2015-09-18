<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>

<% base = base+"RCM"+"/"; %>

 <% String  username = request.getParameter("username"); %>
  <% String  streamid = request.getParameter("streamid"); %>
    <% String  resolution = request.getParameter("resolution"); %>

 <%Logger logger = LoggerFactory.getLogger("");
 
 
 logger.info("【手机栏目页日志】"); 
 logger.info("username="+username+""); 
 logger.info("streamid="+streamid+""); 
 logger.info("resolution="+resolution+"");

 %>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=0" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>点播列表页</title>
<link href="css/base.css" rel="stylesheet" type="text/css" media="all" />
<link href="css/layout.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>


 <%
    String dataFile = "";
    if("0".equals(resolution)){dataFile="hdmbdata.js";}
    if("1".equals(resolution)){dataFile="sdmbdata.js";}
 %>
<script type="text/javascript" src="<%= base%>res/data/<%=dataFile%>" ></script>


</head>

<script type="text/javascript">


$(document).ready(function(e) {


	$("#header h1.back").click(function(){
		window.location.href = "index.jsp?username=<%=username%>&streamid=<%= streamid%>&resolution=<%=resolution%>";
	})
;


});  


</script>


<body>
<div id="header" >
		<h1 class="back">电影</h1>
		<h1 class="search logo"><a href="#" onclick="pageCtrl('search')"></a></h1>        
</div>
<div class="movielist">
	<div class="item clone">
		<a href="voddetail.html?name=ypzjs">
			<div class="img"><img src="images/ypzjs.jpg"/></div>
			<h2>营盘镇警事</h2>
			<p class="actor">主演： 张嘉译 / 丁海峰</p>
			<p class="director">导演：马进</p>
			<p class="ondate">2012-09</p>
			<p class="star">评分：5.6分</p>
		</a>
	</div>
</div> 
<script>
	var fclass=decodeurl(getQueryStringByName("class"));
	$("#header h1.back").text(fclass);
	$(document).ready(function(){
		initdata();		
	})
	function initdata(){
		var nodesource;
		$.each(data,function(i,ele){
			if(ele.class==fclass){
				nodesource=ele.resource;
			}
			else{
				return;
			}
		})
		console.log(nodesource);
		nodesource.forEach(function(s){
			var newEle = $(".movielist .item.clone").clone().removeClass("clone");
			newEle.find("a").attr("href","<%=basePath%>mobile/ShowVodInfo?flag=set&username=<%=username%>&streamid=<%=streamid%>&vodid="+s.id+"&name="+s.id+"&beforepage=vodjsp&bannername="+fclass.trim()+"&resolution="+<%=resolution%>);
			newEle.find("img").attr("src",s.poster);
			newEle.find("a > h2").text(s.name);
			newEle.find("p.actor").text("主演："+s.actor);
			newEle.find("p.director").text("导演："+s.director);
			newEle.find("p.ondate").text("上映："+s.ondata);
			newEle.find("p.star").text("评分："+s.star+"分");
			newEle.appendTo($(".movielist"));
		})		
	}
</script>
</body>
</html>

