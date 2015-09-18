<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>

<%

   String reportType = (String) request.getAttribute("reportType");

String gateway = (String) request.getAttribute("gateway");
%>
<script type="text/javascript">
  var reportType= '<%=reportType%>';
  var gateway= '<%=gateway%>';

</script>
<style> 
a{ text-decoration:none} 
</style> 
<eRedG4:html title="报警管理页面" uxEnabled="true">
<!--<eRedG4:import src="/urm/js/qamUrl.js"/>-->
<eRedG4:import src="/urm/js/report.js"/>
<eRedG4:ext.codeRender fields="NETWORKSTATUS"/>
<eRedG4:ext.codeStore fields="NETWORKSTATUS"/>
<eRedG4:body>
<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>