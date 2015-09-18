<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
  <% String resolution = request.getParameter("resolution"); %>
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
    	
    	request.setAttribute("method","updateUrl");
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
	
    <script type="text/javascript" src="js/jquery.min.js"></script>

<script language="javascript" type="text/javascript" src="<%=basePath%>dist/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css" />

	<script type="text/javascript">	
	function saveConfig(vodid,ids,thisid){
		alert(vodid);
		alert(ids);
		alert(thisid);
		var pageDwellTime = document.getElementById(vodid+"pageDwellTime").value;
		var orderId = document.getElementById(vodid+"orderId").value;
		

		 if (document.getElementById(vodid).checked)
		 {alert("您选择择了"+vodid+" ,pageDwellTime ="+pageDwellTime+" ,orderId="+orderId);
		 addOrDelete = "add";

			if(pageDwellTime==''){
				alert("pageDwellTime 为空");

					}else{
						alert(pageDwellTime);	
					}
		 
		 }else{alert("您取消了"+vodid+" ,pageDwellTime ="+pageDwellTime+" ,orderId="+orderId);
		 addOrDelete = "delete";
		 }

	
	//apps/TvNavigateConfigServlet?method=updateState&resolution=${resolution}&id=${tv.id}'
	
	
// 如果选择了，就是增加操作       vodid , pageDwellTime,orderId ,resolution
// 如果取消了，就是删除操作      vodid , resolution

	 $.ajax({
             type: "GET",
             url: "<%=basePath%>apps/TvNavigateConfigServlet",
             data: {method:'updateState', resolution:'<%=resolution%>',id:vodid},
             dataType: "json",
             success: function(data){
                      //   $('#resText').empty();   //清空resText里面的所有内容
                      //   var html = ''; 
            	 		        if(data!=""&&"no"!=data.trim()){
            	 		        	 document.getElementById("vodname").innerHTML =data.trim();  	   
            	 		        }
            	 		        if(data.trim()=="no"){
            	 		        	alert("没有找到对应资源，请重新输入id");
            	 		        }
            	 		        else{
            	 		        	alert("aaaaaaaaaaaaaaaaa");
            	 		        }
            	 				      }});	
                      }
  
	
		     
<%--		     alert(1);--%>
<%--		 	$.ajax({ url: "http://www.baidu.com", context: document.body, success: function(data){--%>
<%--		 		--%>
<%--		        if(data!=""&&"no"!=data.trim()){--%>
<%--		        	 document.getElementById("vodname").innerHTML =data.trim();  	   --%>
<%--		        }--%>
<%--		        if(data.trim()=="no"){--%>
<%--		        	alert("没有找到对应资源，请重新输入id");--%>
<%--		        }--%>
<%--				      }});	--%>
<%----%>
<%--		 	  alert(2);--%>

	
	</script>


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
<c:if test="${method!='add'}">

 
   
   
   
   
   <form name="ydxmForm" method="post" action="apps/TvNavigateConfigServlet?method=${method}">
   	 <input type="hidden" name="id" value="<%=id %>">
   	  <input type="hidden" name="resolution" value="<%=resolution%>">
     <table width="70%"  cellpadding="0" cellspacing="0"
		 style="margin:10px 0; border:1px solid #BECFD6;" align="center" >




		<tr>
			<td nowrap class="tab02" align="right">
				vod资源id：
			<br/></td>
			<td class="tab01" >
				<input type="text" name="vodId" id="vodid" style="color: red" value="${tvNavigate.vodId}" readonly="readonly">
			<br/></td>
		</tr>
		<tr>
			<td nowrap class="tab02"  id="vodname" align="right">
				vod资源名称：
			<br/></td>
			<td class="tab01" >
				<input type="text"   value="${tvNavigate.name}" style="color: red" readonly="readonly">
			<br/></td>
		</tr>
		<tr>
			<td nowrap class="tab02" align="right">
				规则：
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
			<td colspan="6" nowrap align="center" >
              <input type="submit" value="确定" onclick="return check();">&nbsp;&nbsp;&nbsp;
              <input type="button" name="button1" value="返回" onclick="javaScript:window.history.go(-1);">
            </td>
        </tr>
		
	</table>
	</form>
</c:if>



<c:if test="${method=='add'}">
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
<%--					<td align="center" class="table_title" nowrap>评分</td>--%>
<%--					<td align="center" class="table_title" nowrap>导演</td>--%>
					<td align="center" class="table_title" nowrap>主演</td>
					<td align="center" class="table_title" nowrap>年份</td>
<%--					<td align="center" class="table_title" nowrap>时长</td>--%>
<%--					<td align="center" class="table_title" nowrap>类型</td>--%>
<%--					<td align="center" class="table_title" nowrap>地区</td>--%>
					
					
<%--					    <td align="center" class="table_title" nowrap>描述</td>--%>
<%--					<td align="center" class="table_title" nowrap>片花地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>大海报地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>小海报地址</td>--%>
<%--					<td align="center" class="table_title" nowrap>rtspUrl地址</td>--%>
					
					
<%--					<td align="center" class="table_title" nowrap>modelId</td>--%>
<%--					<td align="center" class="table_title" nowrap>其他信息</td>--%>
					<td align="center" class="table_title" nowrap>分辨率</td>

<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
<%--					<td align="center" class="table_title" nowrap>--%>
<%--						规则--%>
<%--					</td>--%>
					<td align="center" class="table_title" style="width: 30%;" nowrap>
						配置
					</td>
					<td align="center" class="table_title" style="width: 30%;" nowrap>
						操作
					</td>				
				</tr>
			</thead>
			<TBODY id="detail">
				
				<c:forEach items="${list}" var="tv" varStatus="tvn">
					<tr style="text-align: left;"
							onMouseOut="this.className='table_1'"
							onMouseOver="this.className='tab_over'">
                            <td class="table_1"><c:out value="${tvn.index+1}"/></td>
							<td class="table_1"><c:out value="${tv.id}"/></td>
							<td class="table_1"><c:out value="${tv.name}"/></td>
<%--							<td class="table_1"><c:out value="${tv.grade}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.director}"/></td>--%>
							<td class="table_1"><c:out value="${tv.actor}"/></td>
							<td class="table_1"><c:out value="${tv.years}"/></td>
<%--							<td class="table_1"><c:out value="${tv.runtime}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.type}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.area}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.description}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.path}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.bigPosterPath}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.littlePosterPath}"/></td>							--%>
<%--							<td class="table_1"><c:out value="${tv.rtspUrl}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.modelId}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.other}"/></td>--%>
							
			                  <td class="table_1">
			                  
			                  <c:if test="${tv.resolution=='0'}"><font color="red">高清</font></c:if>
                              <c:if test="${tv.resolution=='1'}"><font color="blue">标清</font></c:if>
			                  </td>
                              
							
<%--							<td class="table_1"><c:out value="${tv.state}"/></td>--%>
<%--							<td class="table_1"><c:out value="${tv.useState}"/></td>--%>

<%--                            <td class="table_1">--%>
			                  
			                 
			                 
<%--			                      <input name="vodid" type="checkbox" id="${tv.id}vodid"  <c:forEach items="${defoultlist}" var="dtv1" > <c:if test="${tv.id==dtv1.vodId}">checked="checked" onclick="saveConfig('${tv.id}vodid','${tv.id}','${dtv1.id}');"</c:if> <c:if test="${tv.id!=dtv1.vodId}"> onclick="saveConfig('${tv.id}vodid','${tv.id}','${dtv1.id}');"</c:if>  </c:forEach>   style="zoom:2;" value="" />--%>
			                 
			                
<%--			                  </td>--%>

                       <form  method="post" action="apps/TvNavigateConfigServlet?method=add">
   	                          <input type="hidden" name="vodId" value="${tv.id}">
   	                          <input type="hidden" name="resolution" value="0">
   	                          
							<td class="table_1" style="width:30%;" style="text-align: left;">
													
							
							播放时间：<input name="pageDwellTime"   type="text"  />(ms)<br>
							播放排序：<input name="orderId"  type="text"  />
							
							
							</td>
							<td class="table_1" align="center" >
													
					     	<input type="submit"  value="添加">
							</td>		
							</form>		
		            </tr>
                </c:forEach>	
				

			</TBODY>
			

		</TABLE>
		
	</c:if>
  </body>

</html>
