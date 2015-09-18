<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<eRedG4:html title="FCF_3d柱状综合图" fcfEnabled="true">
<eRedG4:import src="/monitor/js/3dTest.js"/>
<eRedG4:ext.myux uxType = "datatimefield" />
<eRedG4:body>
	<eRedG4:flashReport type="STACK_C" dataVar="xmlString" id="my3Dc_MsChart" align="center" width="900"
		visible="false" />
	<eRedG4:div key="my3Dc_MsChart_panel_div" />
</eRedG4:body>

</eRedG4:html>