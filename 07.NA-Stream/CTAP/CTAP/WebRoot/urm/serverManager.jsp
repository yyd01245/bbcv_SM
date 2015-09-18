<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>


<script type="text/javascript">

var  global_id ;
var  global_cag_id ;
var  global_ip;
var  global_port;
var  global_cpu_info;
var  global_mem_info;
var  global_gpu_info;
var  global_status;
var  global_capacity_size;
var  global_version;
var  global_thread_id;

</script>
<eRedG4:html title="接入网关配置" uxEnabled="true">
<eRedG4:import src="/urm/js/serverManager.js" />
<eRedG4:ext.codeRender fields="CFG"/>
<eRedG4:ext.codeStore fields="CFG"/>
<eRedG4:body>
<eRedG4:div key="menuTreeDiv"></eRedG4:div>
</eRedG4:body>
</eRedG4:html>