<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<eRedG4:html title="升级策略管理页面">
<eRedG4:import src="/cabs/js/updateStrategy.js"/>
<eRedG4:ext.codeRender fields="MENUTYPE,LEAF,EXPAND"/>
<eRedG4:ext.codeStore fields="EXPAND"/>
<eRedG4:body>
<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>