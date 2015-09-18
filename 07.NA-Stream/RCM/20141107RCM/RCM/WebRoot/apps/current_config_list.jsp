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
				当前位置：模板管理&gt;&gt;TV导航
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


	<!-- 显示出错 -->
	

	<!--查询条件  -->
	<form name="ydxmForm" method="post" action="apps/TvNavigateServlet?method=search">
		<div align="center">
			<table width="100%" style="font-size: 12px; margin-top:2px;">
				<tr>
					<td colspan="6" nowrap="nowrap">
						&nbsp;设置排序字段
						<select name="ascendName" style="width:100" class="select">
							<option value="id">id</option></select>
						<select name="ascending" class="select">
						    <option value="true">升序</option>
							<option value="false" selected="selected">降序</option></select>
						<input type="submit" name="queryBtn" value="查询" class="btn">
						<input type="button" name="submit2" value="清空"
							onclick="doReset();" class="btn">

					</td>
				</tr>
			</table>
			<table width="99%" border="0" align="center" cellpadding="2"
				cellspacing="0" bgcolor="#F7F7F7"
				style="border: 1px solid #BFC7CA;">
				<tr>
<%--					<td class="td-left">--%>
<%--						查询类别:--%>
<%--					</td>--%>
<%--					<td class="td-right">--%>
<%--						<select name="parentId" id="totalItems" style="width:145px;" class="select"><option value="0">---查询---</option>--%>
<%--							<option value="-1">查询1</option>--%>
<%--							<option value="-2">查询2</option>--%>
<%--						</select>	--%>
<%--					</td>--%>
					<td >
						url:&nbsp;&nbsp;<input type="text" name="urlkey" >
					</td>
<%--					<td >--%>
<%--						<input type="text" name="itemName" value="">--%>
<%--					</td>--%>
<%--					<td class="td-left">--%>
<%--						排序方法:--%>
<%--					</td>--%>
<%--					<td class="td-right">--%>
<%--						<select name="recordOrdering" style="width:145px;" class="select"><option value="">--排序方法--</option>--%>
<%--							<option value="asc">a数据升序排</option>--%>
<%--							<option value="desc">b数据降序排</option></select>--%>
<%--					</td>--%>
				</tr>
			</table>
		</div>
	</form>

	<!--明细   -->

		<TABLE width="99%" border="0" align="center" cellspacing="1"
			bgcolor="#BFC7CA" style="margin-top:2px;">
			<thead>
				<tr align="center">
					<td align="center" class="table_title" nowrap>
						序号
					</td>				
					<td align="center" class="table_title" nowrap>
						模板名称
					</td>
					<td align="center" class="table_title" nowrap>
						模板编号
					</td>
					<td align="center" class="table_title" nowrap>
						地址
					</td>
<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
					<td align="center" class="table_title" nowrap>
						操作
					</td>
				
				</tr>
			</thead>
			<TBODY id="detail">
				
				<c:forEach items="${list}" var="tv" varStatus="tvn">
					<tr style="cursor: hand; text-align: center;"
							onMouseOut="this.className='table_1'"
							onMouseOver="this.className='tab_over'">
                            <td class="table_1"><c:out value="${tvn.index+1}"/></td>
                            <td class="table_1"><c:out value="${tv.name}"/></td>
							<td class="table_1"><c:out value="${tv.id}"/></td>
							<td class="table_1"><c:out value="${tv.url}"/></td>
<%--							<td class="table_1"><c:out value="${tv.state}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.useState}"/></td>--%>
							<td class="table_1" style="width:40%;">
								<input type="button" value="修改" onclick="location.href='apps/TvNavigateServlet?method=preUpdateUrl&id=${tv.id}'">
								
		
							</td>
							
		            </tr>
                </c:forEach>	
				
				
			</TBODY>
		</TABLE>
		<!-- 分页 -->
		

<script type="text/javascript">
	function gotoPage(pagenum){ 
		var el=document.createElement("input"); 
		el.type="hidden"; 
		el.name="pageno";
		el.value=pagenum;
		
		form=document.forms[0];
		form.appendChild(el);
		form.submit(); 
		return ; 
	} 
</script>







<table width="100%" border="0" cellpadding="0" cellspacing="0"
	background="<%=basePath %>apps/image/S2.jpg">
	<tr>
		<td height="2" colspan="4"></td>
	</tr>
	<tr style="font-size: 12px; font-family: '宋体'; height: 23px">
		<td width="0.5%" height='35'></td>
		<td nowrap>
			<span>[总数: ${page.totalRecords} 条]</span>
			<span>[每页: 10 条]</span> [页次:
			<span
				title='当前第1页'
				style="color: #FF0000"> ${page.currentPage} </span>/
			${page.totalPages}
			]
		</td>
		<td width="70%"></td>
		<td align="right" nowrap>
			
				<a href="apps/TvNavigateServlet?method=list&currentPage=1"><font title="首页">首页</font>&nbsp;</a>
				<a href="apps/TvNavigateServlet?method=list&currentPage=${page.currentPage-1}"><font title="前一页"><c:if test="${page.currentPage-1>1 || page.currentPage-1==1}">前一页</c:if></font></a>
			
			
			<span style="color: #FF0000">[ ${page.currentPage} ]</span>
			
				<a href="apps/TvNavigateServlet?method=list&currentPage=${page.currentPage+1}"><font title="下一页"><c:if test="${page.currentPage+1<page.totalPages || page.currentPage+1==page.totalPages }">下一页</c:if></font></a>&nbsp;
				<a href="apps/TvNavigateServlet?method=list&currentPage=${page.totalPages}"><font title="最后一页">最后一页</font></a>
			
			
		</td>
		<td width="1%"></td>
	</tr>
</table>
</body>
</html>
