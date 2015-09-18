<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>tv_navigate_page.jsp</title>
 <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="css/style.css" />

	<script type="text/javascript" src="js/common.js"></script>
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="background: url(<%=basePath%>apps/images/1_right_1.jpg) repeat-x; padding: 3px 6px 0 6px;">
	<!--导航栏目   -->
	<table id="navTbl" width="100%" cellspacing="0" cellpadding="0"
		class="navTbl">
		<tr>
			<td width="10">
				<img src="images/1_ico1.gif" width="26" height="9">
			</td>
			<td class="navtd">
				当前位置：资源管理上传下载
			</td>
			<td align="right">
				<a  href="apps/tv_navigate_add_update.jsp?flag=add" rel="a"><img src="<%=basePath %>apps/images/ico/8.gif" alt="新增"
						border="0">
				</a>
				<a href="javascript:returnBack()"><img src="<%=basePath %>apps/images/ico/7.gif"
						alt="返回" border="0">
				</a>
				<a><img src="<%=basePath %>apps/images/ico/a6.gif" alt="帮助" border="0"> </a>
			</td>
			<td width="10"></td>
		</tr>
	</table>


	<% String info = request.getParameter("info");
	 String restype = request.getParameter("restype");
	   request.setAttribute("info",info);
	%>


<div align="center" style="top: 30%">
           <c:if test="${info=='success'}"><div style="color: red">上传成功</div> </c:if>
           <c:if test="${info=='error'}"><div style="color: red">上传失败</div> </c:if>
           <form id="forms" action="<%=basePath %>apps/ResUpDownLoad?flag=upload&restype=<%=restype%>" method="post" enctype="multipart/form-data" style="padding-top: 5%;padding-left: 3%;" >
			上传文件：<input type="file" id="fileid" name="file" />
			<input type="submit" name="Submit" value="确定上传" />
             </form>

          


</div>
<script type="text/javascript">

 


</script>





</body>
</html>
