<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>

<%@page import="prod.nebula.service.dto.*"%>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>高清TV详情页</title>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" style="text/css" href="css/style1.css"> 
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
<% String pageName=request.getParameter("pageName"); %>
<% String times=request.getParameter("times"); 

   request.setAttribute("times",times);
%>

 <%Logger logger = LoggerFactory.getLogger("");
 logger.info("【TV详情页日志】");
   logger.info("username="+username+"");
   logger.info("streamid="+streamid+"");
   logger.info("vodid="+vodid+"");
   logger.info("resolution="+0+"");
   logger.info("times="+times+"");
   %>

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










<%

logger.info("开始查询数据库……");
 RcmManager r = new RcmManager();


logger.info("查询数据库完成");
String name ="";
String tvPosterPath2 = "";
String ondata ="";
String actor = "";
String director ="";
String area = "";
String movieintro ="";

if(vodid==null||"".equals(vodid)){
	logger.info("【TV详情页面严重信息提示】没有传入资源ID");
	 movieintro = "没有传入资源ID";	
}else{
	VodResourceInfo vodres = r.getVod(vodid);
	if(vodres!=null){
		
		 name = vodres.getName();
		 tvPosterPath2 = vodres.getTvPosterPath2();
		 ondata = vodres.getYears();
		 actor = vodres.getActor();
		 director = vodres.getDirector();
		 area = vodres.getArea();
		 movieintro = vodres.getDescription();

		 if(name==null||ondata==null||actor==null||director==null||area==null||movieintro==null){
			 logger.info("【TV详情页面严重信息提示】没有成功加载资源信息");	 
		 }
		 
		 
		 if(name==null){
			
			 name="网速太慢,正在加载中……"; 
			 
				%>
				<script>
				window.location.href="<%=basePath%>subinter1.jsp?username=<%=username1%>&streamid=<%=streamid%>&vodid=<%=vodid%>";
					
				</script>
				<%	
		 }
		
		 if(ondata==null){
			
			 ondata="网速太慢,正在加载中……"; 
		 }
		
		 if(actor==null){
			 actor="网速太慢,正在加载中……"; 
		 }
		
		 if(director==null){
			 director="网速太慢,正在加载中……"; 
		 }
		
		 if(area==null){
			 area="网速太慢,正在加载中……"; 
		 }
		
		if(movieintro==null){
			movieintro = "网速太慢,正在加载中……";
			
		
			
		}else{
			
			if(movieintro.length()>82){
				movieintro=	movieintro.substring(0,82)+"...";
			}
		}	

	
	}else{
		logger.info("【TV详情页面严重信息提示】没有找到此影片的任何信息，请检查数据库中是否有该影片的信息");
		 movieintro = "没有找到此影片的任何信息，请检查数据库中是否有该影片的信息";
	}

}



%> 









<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
     <% base = base+"RCM"+"/"; %>

</head>
<body>
	<div class="main">
		<div class="movie_main">			
			<img src="<%=tvPosterPath2 %>"></img>
		</div>
		<div class="movie_introduce">
			<h1 class="title_main"><%=name %></h1>
			<span class="time">(<%=ondata%>)</span>
			<ul class="actor">
				<li>主演:  <%=actor%></li>
				<li>导演:  <%=director%></li>
				<li>国家:  <%=area%></li>
			</ul>
		<p class="boldp">剧情：</p>
		<div class="plot"><%=movieintro%></div>
		<div class="go_maintext">
			<marquee scrollamount=5 height=38>用户<span><%=username%></span>正在选择中</marquee>
		</div>
		</div>
	</div>

     
<script>

	var relocate = setTimeout(redirect,20000);
	function redirect(){
		window.location.href="<%=basePath%>subinter1.jsp?username=<%=username1%>&streamid=<%=streamid%>&vodid=<%=vodid%>";
	}
</script>
	




</body>
</html>