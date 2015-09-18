<%@ page contentType = "text/html" pageEncoding="UTF-8"%>
<%@ include file = "/common/include/taglib.jsp" %>
<eRedG4:html title = "网络运营信息" uxEnabled = "true" >
<eRedG4:import src = "/urm/js/queryQam.js" />
<eRedG4:ext.codeRender fields="QAMSTATUS,RESSTATUS"/>
<eRedG4:ext.codeStore fields="STATUS"/>
<eRedG4:body >
<eRedG4:div key = "menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>

