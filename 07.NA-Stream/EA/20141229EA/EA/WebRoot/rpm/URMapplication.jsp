<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<eRedG4:html title="CTAS应用" fcfEnabled="true">
<eRedG4:ext.codeRender fields="ENABLED,EDITMODE"/>
<eRedG4:ext.codeStore fields="ENABLED,EDITMODE"/>
<eRedG4:ext.codeRender fields="HAMSTATUS"/>
<eRedG4:ext.codeStore fields="HAMSTATUS"/>
<eRedG4:body>
    <eRedG4:flashReport type="L_MS" dataVar="xmlString" id="myLineMsChart" align="center"
		visible="false" />
	<eRedG4:div key="myLineMsChart_panel_div" />
	<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
<script language="JavaScript" src="js/URMapplication.js">
</script>
</eRedG4:html>