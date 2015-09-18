<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>




  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
       <meta http-equiv="content-type" content="text/html; charset=GBK">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="css/style3.css"></link>
	<script type="text/javascript" src="js/jquery.min.js"></script>





 <script>
 
 //定义详情id;
 var vodid=1;

 function queryStatus(){    

     var steam_id =1; 
     var username = 187;
     $.post("<%=basePath%>mobile/ShowVodInfo?flag=get&streamid="+steam_id+"&vodid="+vodid+"&username"+username, function(data) {
    	   if(data!=""&&"error"!=data.trim()){
    		   document.getElementById("ids").innerHTML = data;		   
    	        }	
 	});
		
   
	}	
<%--jQuery(function(){--%>
<%--	  var mode = !!document.createElement('canvas').getContext ? 'canvas' : 'table';--%>
<%--	  jQuery('#qrcode').qrcode({render:mode,text:"<%=basePath%>a?id=1&vp=1",width:400,height:400});--%>
<%-- })--%>
// queryStatus();
setInterval("queryStatus();",1000); 
    
</script>
  </head>
  
  <body >
  
  <span style='color:darkorange' id="ids" >节目即将播放...</span>
   
  </body>
</html>
