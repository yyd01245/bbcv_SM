<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
	<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
     <% base = base+"RCM"+"/"; %>
<html>
<head>

  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" style="text/css" href="voddetail/css/style.css"> 
<script type="text/javascript" src="mobile/js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="mobile/js/main.js"></script>
<% response.setCharacterEncoding("UTF-8"); %>

<%--<% response.setHeader("content-type","text/html;charset=UTF-8"); %>--%>
<%--<% request.setCharacterEncoding("UTF-8"); %>--%>
<%request.setCharacterEncoding("UTF-8"); %>
<%response.setHeader("Charset","UTF-8");%>




<% String username=request.getParameter("username"); %>
<% String streamid=request.getParameter("streamid"); %>
<% String vodid=request.getParameter("vodid"); %>
<% String vod=request.getParameter("vod"); %>

 <%Logger logger = LoggerFactory.getLogger("");
 logger.info("【TV详情页日志】");
   logger.info("username="+username+"");
   logger.info("streamid="+streamid+"");
   logger.info("vodid="+vodid+"");
   logger.info("vod="+vod+"");
   %>

<script>

 function queryStatus(){    

     $.post("<%=basePath%>mobile/ShowVodInfo?flag=getnow&streamid=<%=streamid%>&vodid=<%=vodid%>&username=<%=username%>", function(returndata) {
    	   if(returndata!=""&&"error"!=returndata.trim()&&returndata.trim()!=""){
    		    window.location.href=returndata;
    	        }	
 	});	
   
	}	
// queryStatus();
setInterval("queryStatus();",1000); 
    
</script>









</head>
<body>
	<div class="main">
		<div class="movie_main">			
			<img></img>
		</div>
		<div class="movie_introduce">
			<h1 class="title_main"></h1>
			<span class="time"></span>
			<ul class="actor">
				<li></li>
				<li></li>
				<li></li>
			</ul>
		<p class="boldp">剧情：</p>
		<div class="plot"></div>
		<div class="go_maintext">
			<marquee scrollamount=5 height=38>用户<span><%=username%></span>正在选择中</marquee>
		</div>
		</div>
	</div>

     
	<script type="text/javascript" src="<%= base%>res/data/mbdata.js" ></script>
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



	var relocate = setTimeout(redirect,20000);
	function redirect(){
		window.location.href="subinter1.jsp?username=<%=username%>&streamid=<%=streamid%>&vodid=<%=vodid%>";
	}
	
	</script>
</body>
</html>