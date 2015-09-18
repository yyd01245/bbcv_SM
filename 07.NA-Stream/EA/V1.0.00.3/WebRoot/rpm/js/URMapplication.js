/**
 * 应用监控
 * 
 * @author shy
 * @since 2012-8-24
 */
Ext.Msg.minWidth = 300 ;
Ext.Msg.maxWidth =360 ;
var allstore = new Array();
var total;
var text;
function openWindow(appname,hostip) {
	var jsonData ;
	var message = "IP为";
	Ext.Ajax.request({
     url: './serverMonitor.ered?reqCode=getMsgByApp&app_name='+appname+'&host_ip='+hostip,
     success: function(response){
		jsonData = eval('('+response.responseText+')');
		var app = jsonData.ROOT[0][0];
     	message += app.host_ip+" 应用名:"+appname;
     	
     	message +="<br><br>"+"消息:";
     	
		
		var firstWindow = new Ext.Window({
			title : '<span>应用信息</span>', // 窗口标题
			html: '<div style="padding:10px; font-size:14px; font-weight: bold;">'+message+'</div><div style="padding:10px; font-size:12px; overflow:hidden;">'+app.msg+'</div>',
			iconCls : 'imageIcon',
			layout : 'fit', // 设置窗口布局模式
			width : 400, // 窗口宽度
			height : 300, // 窗口高度
			// tbar : tb, // 工具栏
			closable : true, // 是否可关闭
			closeAction : 'hide', // 关闭策略
			bodyStyle : 'background-color:#FFFFFF',
			collapsible : true, // 是否可收缩
			maximizable : true, // 设置是否可以最大化
			animateTarget : Ext.getBody(),
			border : true, // 边框线设置
			pageY : 100, // 页面定位Y坐标
			pageX : document.body.clientWidth / 2 - 600 / 2, // 页面定位X坐标
			constrain : true,
			// 设置窗口是否可以溢出父容器
			buttonAlign : 'center',
			buttons : [ 
				{
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					firstWindow.hide();
				}
			} ]
		});
		firstWindow.show(); // 显示窗口
	},failure : function(response, opts) {
			Ext.MessageBox.show({title:'提示', msg:'获取应用详细信息失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
		},
		params : {}
})
};
startApp = function(id,hostip,appname){
	 		Ext.Ajax.request({
			     url: './serverMonitor.ered?reqCode=control&cmd_type=1&host_ip='+hostip+'&app_name='+appname,
			     success: function(response){
			     	Ext.MessageBox.show({title:'提示', msg:'启动成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
			     }
			});
	 	}
	 	stopApp = function(id,hostip,appname){
	 		Ext.Ajax.request({
			     url: './serverMonitor.ered?reqCode=control&cmd_type=2&host_ip='+hostip+'&app_name='+appname,
			     success: function(response){
			     	Ext.MessageBox.show({title:'提示', msg:'关闭成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			     }
			});	 		
	 	}

function startORstop(id,hostip,appname){
	if(document.getElementById(id).textContent=="启动服务"){
		startApp(id,hostip,appname);
		text="停止服务";
	}else{
		stopApp(id,hostip,appname);
		text="启动服务";
	}	
	document.getElementById(id).innerHTML=text;
}
function refresh(){
	var jsonData ;
	var CONTENT;
	var total;
	var k =0;
	var documenttt='';
	var apphead
	Ext.Ajax.request({
	 url:'./applicationMonitor.ered?reqCode=getAllApps',
     success: function(response){
		jsonData = eval('('+response.responseText+')');
		total = jsonData.TOTALCOUNT;
			var random = Math.ceil(Math.random()*100);
		for(var i=0;i<total;i++){
			var app = jsonData.ROOT[i];
			var appname = app.app_name;
			var hostip = app.host_ip;
			var version = app.version;
			
			apphead = appname.split("-")[0];
		 	if(apphead=="URM"){
		 	var appStatus = app.status;
			if(appStatus=="1"){
				text="停止服务";
			}else{
				text="启动服务";
			}
		 		k++; 	
			
				documenttt+='<tr>' +
				'<td class="each"><strong class="name">'+appname+'-'+hostip+'</strong></td>' +
				'<td class="each"><span class="info">'+ version+'</span></td>' +
				'<td class="each"><a href="#_" onclick=openWindow("'+appname+'","'+hostip+'") class=bt1>详 细</a></td>' +
				'<td class="each"><a href="#_" class=bt2 id='+(random+i)+' onclick=startORstop('+(random+i)+',"'+hostip+'","'+appname+'")>'+text+'</a></td>' +
				'<td class="each"><a href="#_" onclick=openServerWindow("'+hostip+'") class=bt3>切换到所属服务器</a></td></tr>';	
				
		 	}
	}
		CONTENT ='<div class="appMonitor2">' +
									'			<div class="padding"><div class="box2">' +
									'<table width="100%" cellpadding="0" cellspacing="0"  class="table">' +
									'<th width="25%">应用名称</th><th width="25%">应用版本信息</th><th width="10%">详情</th><th width="10%">启停服务</th><th width="30%">对应主机</th>' +
									documenttt +
									'</table>'+
									
//									'				<h3 class=title> 云终端接入平台<strong>('+k+')</strong>'+'</h3>' +
//									'				<div class=detail><p class="clearfix each"><strong class="name">应用名称' +									
//				'</strong><span class="info">| 用户在线信息  </span></p>' +
//													documentt+
//									'				</div>' +
									
									
									'				<div align ="center"><a href="#_" onclick=refresh() class="link" >刷新</a>' +
									'				<a href="applicationMonitor.ered?reqCode=appInit" class="link" >返回应用</a>' +
									'				<a href="serverMonitor.ered?reqCode=accHisInit" class="link" >返回服务</a>' +
									'			</div></div></div>' +
									'		</div>';
		document.getElementById("monitor").innerHTML=CONTENT;
		}
	});
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'center',
									split : true,
									autoScroll : true,				
									id:'monitor'
								}]
					});
}
Ext.onReady(function() {
	var jsonData ;
	var CONTENT;
	var total;
	var k =0;
	var documenttt='';
	var apphead
	Ext.Ajax.request({
	 url:'./applicationMonitor.ered?reqCode=getAllApps',
     success: function(response){
		jsonData = eval('('+response.responseText+')');
		total = jsonData.TOTALCOUNT;
		var random = Math.ceil(Math.random()*100);
		for(var i=0;i<total;i++){
			var app = jsonData.ROOT[i];
			var appname = app.app_name;
			var hostip = app.host_ip;
			var version = app.version;
			apphead = appname.split("-")[0];
		 	if(apphead=="URM"){
		 	var appStatus = app.status;
			if(appStatus=="1"){
				text="停止服务";
			}else{
				text="启动服务";
			}
		 		k++; 	
				documenttt+='<tr>' +
				'<td class="each"><strong class="name">'+appname+'-'+hostip+'</strong></td>' +
				'<td class="each"><span class="info"> '+version+'</span></td>' +
				'<td class="each"><a href="#_" onclick=openWindow("'+appname+'","'+hostip+'") class=bt1>详 细</a></td>' +
				'<td class="each"><a href="#_" class=bt2 id='+(random+i)+' onclick=startORstop('+(random+i)+',"'+hostip+'","'+appname+'")>'+text+'</a></td>' +
				'<td class="each"><a href="#_" onclick=openServerWindow("'+hostip+'") class=bt3>切换到所属服务器</a></td></tr>';	
				
		 	}
	}
		CONTENT ='<div class="appMonitor2">' +
									'			<div class="padding"><div class="box2">' +
									'<table width="100%" cellpadding="0" cellspacing="0"  class="table">' +
									'<th width="25%">应用名称</th><th width="25%">应用版本信息</th><th width="10%">详情</th><th width="10%">启停服务</th><th width="30%">对应主机</th>' +
									documenttt +
									'</table>'+
//									'				<h3 class=title> 云终端接入平台<strong>('+k+')</strong>'+'</h3>' +
//									'				<div class=detail><p class="clearfix each"><strong class="name">应用名称' +									
//				'</strong><span class="info">| 用户在线信息  </span></p>' +
//													documentt+
//									'				</div>' +
									'				<div align ="center"><a href="#_" onclick=refresh() class="link" >刷新</a>' +
									'				<a href="applicationMonitor.ered?reqCode=appInit" class="link" >返回应用</a>' +
									'				<a href="serverMonitor.ered?reqCode=accHisInit" class="link" >返回服务</a>' +
									'			</div></div></div>' +
									'		</div>';
		document.getElementById("monitor").innerHTML=CONTENT;
		}
	});
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'center',
									split : true,
									autoScroll : true,				
									id:'monitor'
								}]
					});
		});

function addStatus(value){
		if(value=='1'){
			return "<font color='green'>正常</font>";
		}else if(value=='-1'){
			return "<font color='red'>异常</font>";
		}
	} 

function openServerWindow(host_ip){
		var ip;
		var app_name;
		var showLogPanel,showLogWindow;
     	var showMsg = '';
     	var cname;
     	var state;
     	ip = host_ip;
     	var button="<a href='applicationMonitor.ered?reqCode=ctasInit' class='submitBt'>"+"跳转到应用"+"</a>";
     	Ext.Ajax.request({
	    url: './serverMonitor.ered?reqCode=getHostInfoByHostip&host_ip='+ip,
	    success: function(response) {
	    	var hostJson = eval('('+response.responseText+')');
	    	hostData = hostJson.ROOT;
	    	cname=hostData[0].host_name;
	    	}
     	});
     	var cm1 = new Ext.grid.ColumnModel([ 
              {header:'IP',dataIndex:'host_ip',width:120},  
              {header:'服务名称',dataIndex:'host_name',width:80}  
          ]);  
     	var cm2 = new Ext.grid.ColumnModel([
     		{header:'应用名称',dataIndex:'app_name',width:120},  
            {header:'状态',dataIndex:'status',width:120,renderer:addStatus},
            {header:'操作',dataIndex:'button',width:120}
          ]);  
  
     	var hostStore = new Ext.data.Store({
    	  proxy : new Ext.data.HttpProxy({
							url : './serverMonitor.ered?reqCode=getHostInfoByHostip&host_ip='+ip
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'host_ip'
								}, {
									name : 'host_name'
								}])
      });
      var appStore = new Ext.data.Store({
    	  proxy : new Ext.data.HttpProxy({
							url : './serverMonitor.ered?reqCode=getAppsByHost&host_ip='+ip
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'app_name'
								}, {
									name : 'status'
								},{name:'button'}])
      });
      
      hostStore.load(); 
      appStore.load();

     	var listView1 = new Ext.grid.GridPanel({
     	    title: '主机信息',
     	    collapsible:true,//右上角上的那个收缩按钮，设为false则不显示
     	    autoHeight:true,
     	    store: hostStore,
     	    cm:cm1
     	});
     	
     	var listView2 = new Ext.grid.GridPanel({
     	    title: '应用状态',
     	    collapsible:true,//右上角上的那个收缩按钮，设为false则不显示
     	    autoHeight:true,
     	    store: appStore,
     	    cm:cm2
     	});
     	
     	listView2.addListener('rowdblclick', logWindow);

     	
     	
     	var panel = new Ext.Panel({
	        contentEl:'myLineMsChart_div',
	        applyTo:'myLineMsChart_panel_div'
        });
        
		var firstWindow = new Ext.Window({
			title : '<span>监控信息</span>', // 窗口标题
			iconCls : 'imageIcon',
			layout : 'border', // 设置窗口布局模式
			width : 900, // 窗口宽度
			height : 430, // 窗口高度
			// tbar : tb, // 工具栏
			closable : false, // 是否可关闭
			closeAction : 'hide', // 关闭策略
			bodyStyle : 'background-color:#FFFFFF',
			collapsible : true, // 是否可收缩
			maximizable : true, // 设置是否可以最大化
			modal : true,
			animateTarget : Ext.getBody(),
			border : true, // 边框线设置
			pageY : 20, // 页面定位Y坐标
			pageX : 50, // 页面定位X坐标
			constrain : true,
			// 设置窗口是否可以溢出父容器
			items:[
			{
							title : '<span>主机信息</span>',
							iconCls : 'layout_contentIcon',
							collapsible : true,
							layout:'fit',
							width : 330,
							region : 'west',
							items : [listView1,listView2]
						}, {
							region : 'center',
							width:570,
							title : '主机监控图',
							iconCls : 'application_view_listIcon',
							collapsible : true,
							titleCollapse : false,
							//下拉层的动画效果必须关闭,否则将出现Flash图标下拉动画过场异常的现象
							animCollapse : false,
							maximizable : true,
							border : false,
							closable : false,
							items : [panel]
						}
			],
			buttonAlign : 'center',
			buttons : [ 
				{
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					firstWindow.hide();
				}
			} ]
		});
		firstWindow.show(); // 显示窗口
		showLogPanel = new Ext.Panel({
			frame : false,
			bodyStyle : 'padding:5 5 0',
			id : 'showLogPanel',
			name : 'showLogPanel',
			autoScroll:true,
			html : showMsg
		});
     	
     	showLogWindow = new Ext.Window({
			layout : 'fit',
			width : 450,
			height : 350,
			resizable : false,
			draggable : true,
			closeAction : 'hide',
			title : '<span>查看消息</span>',
			modal : true,
			collapsible : true,
			titleCollapse : true,
			minimizable  : false,
			maximizable : true,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			animateTarget : Ext.getBody(),
			constrain : true,
			items : [showLogPanel],
			buttons : [
				{
				text : '启动',
				iconCls : 'acceptIcon',
				handler : function() {
					startApp(ip,app_name);
				}
				},
				{
				text : '停止',
				iconCls : 'acceptIcon',
				handler : function() {
					stopApp(ip,app_name);
				}
				},
				{
				text : '关闭窗口',
				iconCls : 'deleteIcon',
				handler : function() {
					showLogWindow.hide();
				}
			}]

		});
     	function logWindow() {
     		var record = listView2.getSelectionModel().getSelected();
     		app_name = record.get('app_name');
     		var status = record.get('status');
     		Ext.Ajax.request({
			    url: './serverMonitor.ered?reqCode=getMsgByApp&host_ip='+ip+"&app_name="+app_name,
			    success: function(response) {
     			var hostJson = eval('('+response.responseText+')');
     			var hostRoot = hostJson.ROOT;
			    	if(status!=1)
			    		showMsg = '<font color="green">'+hostRoot[0][0].msg+'</font>';
			    	else
			    		showMsg = '<font color="red">'+hostRoot[0][0].msg+'</font>';
			    	showLogWindow.show();
		     		showLogPanel = Ext.getCmp('showLogPanel');
		     		showLogPanel.body.update(showMsg);
    			}
	    	});
     		
     	}
     	window.setTimeout("requestUpdate(\""+ip+"\");",1000);
	}
var task1 = {
    run: function(){
		requestUpdate();
    },
    interval: 10000 //2 second
}
var runner1 = new Ext.util.TaskRunner();
runner1.start(task1);
var requestUpdate = function(ip) {
			Ext.Ajax.request({
						url : './serverMonitor.ered?reqCode=getData&host_ip='+ip,
						success : function(response, opts) {
							var resultArray = Ext.util.JSON
									.decode(response.responseText);
							var xmlstring = resultArray.xmlstring;
							updateChartXML('myLineMsChart', xmlstring);
						},
						failure : function(response, opts) {
							Ext.MessageBox.show({title:'提示', msg:'获取监控数据失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						},
						params : {}
					});
}

