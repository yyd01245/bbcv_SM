<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<eRedG4:html title="键值网关监控" uxEnabled="true">
<!--<eRedG4:import src="/urm/js/qamUrl.js"/>-->
<style> 
a{ text-decoration:none} 
</style> 
<eRedG4:import src="/urm/js/keyGatewayDistribution_monitor.js"/>
<eRedG4:ext.codeRender fields="NETWORKSTATUS"/>
<eRedG4:ext.codeStore fields="NETWORKSTATUS"/>
<eRedG4:body>
<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>