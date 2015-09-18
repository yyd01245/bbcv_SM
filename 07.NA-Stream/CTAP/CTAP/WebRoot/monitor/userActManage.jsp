<%@ page language = "java" contentType = "text/html;charset = utf-8" %>
<%@ include file = "/common/include/taglib.jsp" %>
<eRedG4:html title = "用户操作分析图表"  uxEnabled = "true" fcfEnabled= "true">
<eRedG4:import src = "/monitor/js/userActManage.js" />
<eRedG4:ext.myux uxType = "datatimefield" />
<eRedG4:body>
	<eRedG4:flashReport type="SCROLL_L" dataVar="xmlString" id="myLineMsChart" align="center" width = "900"
		visible="false" />
</eRedG4:body>
</eRedG4:html> 