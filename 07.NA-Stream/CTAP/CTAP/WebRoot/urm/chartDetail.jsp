
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>

<eRedG4:html title="应用状态监控" fcfEnabled="true">
<eRedG4:body>


	<eRedG4:flashReport type="L" dataVar="xmlString" width="99%"  id="myLineChart" align="center"
		visible="false" />
</eRedG4:body>
<script language="JavaScript" src="js/chartDetail.js">
</script>
</eRedG4:html>