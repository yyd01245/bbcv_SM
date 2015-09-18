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
				<a  href="apps/vod_resource_add_update.jsp?flag=add" rel="a"><img src="<%=basePath %>apps/images/ico/8.gif" alt="新增"
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
						<select name="ascendName" style="width:100" class="select"><option value="parentId">总类名称</option>
							<option value="operDate">更新时间</option></select>
						<select name="ascending" class="select"><option value="true">升序</option>
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
					<td class="td-left">
						项目类别:
					</td>
					<td class="td-right">
						<select name="parentId" id="totalItems" style="width:145px;" class="select"><option value="0">---选择项目类别---</option>
							<option value="-1">查询1</option>
							<option value="-2">查询2</option>
						</select>	
					</td>
					<td class="td-left">
						x名称:
					</td>
					<td class="td-right">
						<input type="text" name="itemName" value="">
					</td>
					<td class="td-left">
						排序方法:
					</td>
					<td class="td-right">
						<select name="recordOrdering" style="width:145px;" class="select"><option value="">--排序方法--</option>
							<option value="asc">a数据升序排</option>
							<option value="desc">b数据降序排</option></select>
					</td>
				</tr>
			</table>
		</div>
	</form>

	<!--明细   -->

		<TABLE width="99%" border="0" align="center" cellspacing="1"
			bgcolor="#BFC7CA" style="margin-top:2px;">
			<thead>
				<tr align="center">
	            <td align="center" class="table_title" nowrap>序号</td>				
					<td align="center" class="table_title" nowrap>编号</td>
					<td align="center" class="table_title" nowrap>名称</td>
					<td align="center" class="table_title" nowrap>评分</td>
					<td align="center" class="table_title" nowrap>导演</td>
					<td align="center" class="table_title" nowrap>主演</td>
					<td align="center" class="table_title" nowrap>年份</td>
					<td align="center" class="table_title" nowrap>时长</td>
					<td align="center" class="table_title" nowrap>类型</td>
					<td align="center" class="table_title" nowrap>地区</td>
					
					
<%--					    <td align="center" class="table_title" nowrap>描述</td>--%>
<%--					<td align="center" class="table_title" nowrap>片花地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>大海报地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>小海报地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>rtspUrl地址</td>--%>
					
					
<%--					<td align="center" class="table_title" nowrap>modelId</td>--%>
					<td align="center" class="table_title" nowrap>其他信息</td>
					<td align="center" class="table_title" nowrap>分辨率</td>
<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
					<td align="center" class="table_title" style="width: 2px;" nowrap>
						操作
					</td>
				
				</tr>
			</thead>
			<TBODY id="detail">
				
				<c:forEach items="${list}" var="tv" varStatus="tvn">
					<tr style="text-align: center;"
							onMouseOut="this.className='table_1'"
							onMouseOver="this.className='tab_over'">
                            <td class="table_1"><c:out value="${tvn.index+1}"/></td>
							<td class="table_1"><c:out value="${tv.id}"/></td>
							<td class="table_1"><c:out value="${tv.name}"/></td>
							<td class="table_1"><c:out value="${tv.grade}"/></td>
							<td class="table_1"><c:out value="${tv.director}"/></td>
							<td class="table_1"><c:out value="${tv.actor}"/></td>
							<td class="table_1"><c:out value="${tv.years}"/></td>
							<td class="table_1"><c:out value="${tv.runtime}"/></td>
							<td class="table_1"><c:out value="${tv.type}"/></td>
							<td class="table_1"><c:out value="${tv.area}"/></td>
<%--							<td class="table_1"><c:out value="${tv.description}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.path}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.bigPosterPath}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.littlePosterPath}"/></td>							--%>
<%--							<td class="table_1"><c:out value="${tv.rtspUrl}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.modelId}"/></td>--%>
							<td class="table_1"><c:out value="${tv.other}"/></td>
							
			                  <td class="table_1">
			                  
			                  <c:if test="${tv.resolution=='0'}"><font color="red">高清</font></c:if>
                              <c:if test="${tv.resolution=='1'}"><font color="blue">标清</font></c:if>
			                  </td>
                              
							
<%--							<td class="table_1"><c:out value="${tv.state}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.useState}"/></td>--%>
							<td class="table_1" style="width:40%;">
							
							 <c:if test="${tv.resolution=='0'}"><input type="button"  class="table_title" value="克隆为标清" style="color: red;cursor: hand;" onclick="location.href='apps/VodResourceServlet?method=preUpdateChange&id=${tv.id}'"></c:if>
                              <c:if test="${tv.resolution=='1'}"><input type="button" class="table_title" value="克隆为高清" style="color: blue;cursor: hand;"  onclick="location.href='apps/VodResourceServlet?method=preUpdateChange&id=${tv.id}'"></c:if>
                              
                              
							    <input type="button" value="详情"style="cursor: hand;" class="table_title" onclick="location.href='apps/VodResourceServlet?method=vodDetail&id=${tv.id}'">	
								<input type="button" value="修改"style="cursor: hand;" class="table_title" onclick="location.href='apps/VodResourceServlet?method=preUpdateUrl&id=${tv.id}'">															
								<input type="button" value="下架"style="cursor: hand;" class="table_title"  onclick="javascript:if(confirm('确认下架吗?'))window.location='apps/VodResourceServlet?method=updateState&id=${tv.id}&resolution=${tv.resolution}'">
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
			
				<a href="apps/VodResourceServlet?method=list&currentPage=1"><font title="首页">首页</font>&nbsp;</a>
				<a href="apps/VodResourceServlet?method=list&currentPage=${page.currentPage-1}"><font title="前一页"><c:if test="${page.currentPage-1>1 || page.currentPage-1==1}">前一页</c:if></font></a>
			
			
			<span style="color: #FF0000">[ ${page.currentPage} ]</span>
			
				<a href="apps/VodResourceServlet?method=list&currentPage=${page.currentPage+1}"><font title="下一页"><c:if test="${page.currentPage+1<page.totalPages || page.currentPage+1==page.totalPages }">下一页</c:if></font></a>&nbsp;
				<a href="apps/VodResourceServlet?method=list&currentPage=${page.totalPages}"><font title="最后一页">最后一页</font></a>
			
			
		</td>
		<td width="1%"></td>
	</tr>
</table>
</body>
</html>
