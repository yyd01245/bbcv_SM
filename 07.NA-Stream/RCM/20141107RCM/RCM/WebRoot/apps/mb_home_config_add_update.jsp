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
    
    <% String flag = request.getParameter("flag");
    String id = request.getParameter("id");
    
    System.out.println("页面中flag="+flag);
    if("error".equals(flag)){
    	
    	request.setAttribute("info","error");
    	
    }
    
    if("add".equals(flag)){
    	
    	request.setAttribute("method","add");
    	request.setAttribute("methodStr","添加");
    }else{
    	
    	request.setAttribute("method","updateUrl");
    	request.setAttribute("methodStr","修改");	
    	
    }
    
    
    
    %>
    
    <title>手机栏目添加或修改</title>
 <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link rel="stylesheet" type="text/css" href="css/style.css" />
	<link rel="stylesheet" type="text/css" href="../dist/dwz-ria/themes/specter/css/specter.css" />
	<link rel="stylesheet" type="text/css" href="../dist/jquery-ui/css/ui-lightness/jquery-ui-1.8.11.custom.css"/>
	<script type="text/javascript" src="themes/jqueryui/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="themes/jqueryui/js/jquery-ui-1.8.19.custom.min.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
  </head>
  
  <body style="background: url(<%=basePath%>apps/images/1_right_1.jpg) repeat-x; padding: 3px 6px 0 6px;">
    <table id="navTbl" width="100%" cellspacing="0" cellpadding="0"
		class="navTbl">
		<tr>
		    <td width="10">
				<img src="images/1_ico1.gif" width="26" height="9">
			</td>
			<td class="navtd">

				当前位置：模板管理&gt;&gt;TV导航&gt;&gt;${methodStr}Tv导航模板</td>
			<td align="right">
				<a href="javaScript:window.history.go(-1);"><img src="<%=basePath%>apps/images/ico/7.gif" alt="返回" border="0"></a>
				<a><img src="<%=basePath%>apps/images/ico/a6.gif" alt="帮助" border="0">
				</a>
			</td>
			<td width="10"></td>
		</tr>
	</table>

  <!-- 显示信息-->	
    <c:if test="${info=='error'}">
	<div align="center">	
		<h3 style="cue: red">o(︶︿︶)o唉！无栏目可添加了,请先在"栏目配置"中配置或新增栏目后即可添加。
		</h3>		
	</div>
  <br>
   </c:if>
   <c:if test="${info!='error'}">
   <form name="ydxmForm" method="post" action="apps/MbHomeConfigServlet?method=${method}">
   	 <c:if test="${method!='add'}"><input type="hidden" name="mainPage" value="${tvNavigate.mainPage}"></c:if>
   	 <c:if test="${method!='add'}"><input type="hidden" name="modelId" value="${tvNavigate.modelId}"></c:if>
   	 <input type="hidden" name="resolution" value="${resolution}">
     <table width="70%"  cellpadding="0" cellspacing="0"
		 style="margin:10px 0; border:1px solid #BECFD6;" align="center" >



		<tr>
			<td nowrap class="tab02" align="right">
				栏目：
			<br/></td>
			<td class="tab01" >
	<select name="modelId"  id="totalItems" style="width:145px;" class="select" <c:if test="${method!='add'}">disabled="disabled"</c:if> >
	
	       <c:forEach items="${list}" var="tv" varStatus="tvn">
              <option value="${tv.bannerId}"><c:out value="${tv.bannerName}"/></option>
             </c:forEach>
	        
    </select>
				
			<br/></td>
		</tr>

		<tr>
			<td nowrap class="tab02" align="right">
			排序：
			<br/></td>
			<td class="tab01" >
			<input type="text" style="width: 300px;" name="orderId" value="${tvNavigate.orderId}">
			<br/></td>
		</tr>	
	
		<tr>
			<td colspan="6" nowrap align="center" >
              <input type="submit" value="确定" onclick="return check();">&nbsp;&nbsp;&nbsp;
              <input type="button" name="button1" value="返回" onclick="javaScript:window.history.go(-1);">
            </td>
        </tr>
		
	</table>
	</form>
   </c:if>
 
  
  </body>

</html>
