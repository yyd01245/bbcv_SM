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
    
    if("add".equals(flag)){
    	
    	request.setAttribute("method","add");
    	request.setAttribute("methodStr","添加");
    }else{
    	
    	request.setAttribute("method","updateAd");
    	request.setAttribute("methodStr","修改");	
    	
    }
    
    
    
    %>
    
    <title>tv导航模板添加或修改</title>
   <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    
    



<script language="javascript" type="text/javascript" src="<%=basePath%>dist/My97DatePicker/WdatePicker.js"></script>




	    <script type="text/javascript" src="js/common.js"></script>
	    
    <link rel="stylesheet" type="text/css" href="css/style.css" />


	

  </head>
  
  <body style="background: url(<%=basePath%>apps/images/1_right_1.jpg) repeat-x; padding: 3px 6px 0 6px;">
    <table id="navTbl" width="100%" cellspacing="0" cellpadding="0"
		class="navTbl">
		<tr>
		    <td width="10">
				<img src="images/1_ico1.gif" width="26" height="9">
			</td>
			<td class="navtd">

				当前位置：点播资源&gt;&gt;VOD资源&gt;&gt;${methodStr}VOD资源</td>
			<td align="right">
				<a href="javaScript:window.history.go(-1);"><img src="<%=basePath%>apps/images/ico/7.gif" alt="返回" border="0"></a>
				<a><img src="<%=basePath%>apps/images/ico/a6.gif" alt="帮助" border="0">
				</a>
			</td>
			<td width="10"></td>
		</tr>
	</table>
  
  <!-- 显示信息-->	
	<div align="left">	
		<font color="red"  size="2">
		</font>		
	</div>
  <br>
   
   <form name="ydxmForm" method="post" action="apps/TvNavigateConfigServlet?method=${method}">
   	 <input type="hidden" name="id" value="<%=id %>">
     <table width="70%"  cellpadding="0" cellspacing="0"
		 style="margin:10px 0; border:1px solid #BECFD6;" align="center" >

		<tr>
			<td nowrap class="tab02"  id="vodname" align="right">
				分辨率：
			<br/></td>
			<td class="tab01" >
			<select name="resolution"   style="width:150px;" class="select">
			   <option value="0"  <c:if test="${tvNavigate.resolution=='0'}">selected="selected"</c:if>>高清</option>
			   <option value="1" <c:if test="${tvNavigate.resolution=='1'}">selected="selected"</c:if>>标清</option>
			</select>
				
			<br/></td>
		</tr>


		<tr>
			<td nowrap class="tab02" align="right">
				名称：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="name" value="${tvNavigate.name}">
			<br/></td>
		</tr>
		<tr>
			<td nowrap class="tab02" align="right">
				地址：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="adVideoUrl" value="${tvNavigate.adVideoUrl}">
			<br/></td>
		</tr>
		<tr>
			<td nowrap class="tab02" align="right">
				播放时间：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="pageDwellTime" value="${tvNavigate.pageDwellTime}">
			<br/></td>
		</tr>
		<tr>
			<td nowrap class="tab02" align="right">
				排序：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="orderId" value="${tvNavigate.orderId}">
			<br/></td>
		</tr>

				<tr>
			<td nowrap class="tab02" align="right">
				上传时间：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="upData"  onFocus="WdatePicker({isShowClear:false,readOnly:true})"   value="${tvNavigate.upData}">
		
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
  </body>

</html>
