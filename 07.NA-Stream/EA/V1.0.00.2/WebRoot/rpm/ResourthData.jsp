<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<eRedG4:html title="源痕迹查询页面" uxEnabled="true">
<!--<eRedG4:import src="/urm/js/qamUrl.js"/>-->
<eRedG4:import src="/rpm/js/resourthData.js"/>
<eRedG4:ext.codeRender fields="RESOURSTATUS,RESTYPE"/>
<eRedG4:ext.codeStore fields="RESOURSTATUS,RESTYPE"/>
<eRedG4:body>
<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>