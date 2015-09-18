
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>

<%  

String joinCountOnline =(String) request.getAttribute("joinCountOnline");
String joinCountOffline =(String) request.getAttribute("joinCountOffline");


String KeyCountOnline =(String) request.getAttribute("KeyCountOnline");
String KeyCountOffline =(String) request.getAttribute("KeyCountOffline");
String KeyCountNoAuth =(String) request.getAttribute("KeyCountNoAuth");

String signalingCountOnline =(String) request.getAttribute("signalingCountOnline");
String signalingCountOffline =(String) request.getAttribute("signalingCountOffline");
String signalingCountNoAuth =(String) request.getAttribute("signalingCountNoAuth");
%>
<script type="text/javascript">
   var joinCountOnline = <%=joinCountOnline%>;
   var joinCountOffline = <%=joinCountOffline%>;
   
   var KeyCountOnline = <%=KeyCountOnline%>;
   var KeyCountOffline = <%=KeyCountOffline%>;
   var KeyCountNoAuth = <%=KeyCountNoAuth%>;

   var signalingCountOnline = <%=signalingCountOnline%>;
   var signalingCountOffline = <%=signalingCountOffline%>;
   var signalingCountNoAuth = <%=signalingCountNoAuth%>;
   
</script>

<style> 
a{ text-decoration:none} 
</style> 
<eRedG4:html title="应用状态监控" fcfEnabled="true">
<eRedG4:ext.codeRender fields="ENABLED,EDITMODE"/>
<eRedG4:ext.codeStore fields="ENABLED,EDITMODE"/>
<eRedG4:ext.codeRender fields="HAMSTATUS"/>
<eRedG4:ext.codeStore fields="HAMSTATUS"/>
<eRedG4:body>


<eRedG4:flashReport type="2DC" dataVar="xmlString"  height="300" width="500" id="my2DcChart" align="center"
		visible="false" />
<%--	<eRedG4:div key="my2DcChart_panel_div" />--%>
	
	
	<eRedG4:flashReport type="2DP" dataVar="xmlString1"  height="300" width="500" id="my2DpChart" align="center"
		visible="false" />
	
	
	
<%--	--%>
<%--    <eRedG4:flashReport type="L_MS" dataVar="xmlString" id="myLineMsChart" align="center"--%>
<%--		visible="false" />--%>
<%--	<eRedG4:div key="myLineMsChart_panel_div" />--%>
<%--	<eRedG4:div key="menuTreeDiv"></eRedG4:div>--%>
<%--		<eRedG4:div key="divPanel"></eRedG4:div>--%>
	
	
	
</eRedG4:body>
<script language="JavaScript" src="js/monitorHomePage.js">
</script>
</eRedG4:html>