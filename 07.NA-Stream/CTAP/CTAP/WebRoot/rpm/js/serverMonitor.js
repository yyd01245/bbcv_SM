var ex="";
var hostData;
var total;
var appTotal;
var appData;
var contextt;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
Ext.Msg.maxWidth =360 ;
//	Ext.Ajax.request({
//	    url: './serverMonitor.ered?reqCode=getAllHosts',
//	    success: function(response) {
//	    	var hostJson = eval('('+response.responseText+')');
//	    	total = hostJson.TOTALCOUNT;
//	    	hostData = hostJson.ROOT;
//	    	var demo='';
//	  	   	for(var i=0;i<total;i++){
//	  	   		var host_ip=hostData[i].host_ip;
//	  	   		var pic = "1.gif";
//	  	   		if(hostData[i].status=='-1')
//	  	   			pic = "2.gif";
//	  	   		getApps(host_ip);  
//	  	   			demo+='<div class="box"><a href="#_" onclick=openWindow("'+host_ip+'")>' +
//						  '<span class=img>'+'<img src='+pic+'></span>' +
//						  '<div class ="name">'+host_ip+'</div></a>' +	
//						  contextt+
//						  '</div>'	;
//	  	  		}
//	  	   		ex += '<div class="hostMonitor">' +
//						'<div class="border clearfix">' +
//						demo+
//						'</div>' +
//					  '</div>';
//	  	   		document.getElementById("host").innerHTML = ex;
//	    },
//	    failure: function(response) {
//	    }
//	 });
		var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'center',
									split : true,
									autoScroll : true,
									bodyStyle :'background:#CCDFEA',
									html:'<div id="host"></div>'
								}]

		});	
});	 

//根据主机IP获取本机下所有应用
function getApps(host_ip){	
	
	Ext.Ajax.request({				
	  	   			url: './serverMonitor.ered?reqCode=getAppsByHost&host_ip='+host_ip,
	    			success: function(response) {
						contextt='';
		    			var appJson = eval('('+response.responseText+')');
		    			appTotal = appJson.TOTALCOUNT;
		    			appData = appJson.ROOT;
		    			for(var j=0;j<appTotal;j++){
		    				var img ='<img src="server_normal.png">';
		    				if(appData[j].status=='-1'){
		    					img ='<img src="server_error.png">';
		    				}
		    				var s='<div class ="each clearfix">'+img+'<a href="#_" onclick=openWindows("'+appData[j].app_name+'")><strong>'+appData[j].app_name+":"+appData[j].version+'</strong></a></div>'
		    				contextt +=s;
		    			}
		    			document.getElementById(host_ip).innerHTML=contextt;
	    			}
	  	   		})
}

//显示主机信息
function openWindows(app_name) {
	var apps = app_name.toLowerCase();
	var a = apps.split("-");	
	var appName = a[0];
	if(appName=="VNCM"||appName=="RSM"){
		window.location.href='../cabs/serverMonitor.ered?reqCode=pageInit'
	}else{
		window.location.href='applicationMonitor.ered?reqCode='+appName+'Init&app_name='+app_name;
	}
	
};
	function addStatus(value){
		if(value=='1'){
			return "<font color='green'>正常</font>";
		}else if(value=='-1'){
			return "<font color='red'>异常</font>";
		}
	} 

function openWindow(host_ip){
		var ip;
		var app_name;
		var showLogPanel,showLogWindow;
     	var showMsg = '';
     	var state;
     	ip = host_ip;
     	var cm1 = new Ext.grid.ColumnModel([ 
              {header:'IP',dataIndex:'host_ip',width:120},  
              {header:'主机名称',dataIndex:'host_name',width:80}  
          ]);  
     	var cm2 = new Ext.grid.ColumnModel([
     		{header:'应用名称',dataIndex:'app_name',width:120},  
            {header:'状态',dataIndex:'status',width:120,renderer:addStatus}
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
								}])
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

	function updateServer(){
		ex="";
		var demo='';
		Ext.Ajax.request({
	    url: './serverMonitor.ered?reqCode=getAllHosts',
	    success: function(response) {
	    	var hostJson = eval('('+response.responseText+')');
	    	total = hostJson.TOTALCOUNT;
	    	hostData = hostJson.ROOT;
	    		    	
	  	   	for(var i=0;i<total;i++){
	  	   		var host_ip=hostData[i].host_ip;
	  	   		var cpuInfo=hostData[i].cpu_info;
	  	   		var memInfo=hostData[i].mem_info;
	  	   		var statusInfo=hostData[i].status;
//	  	   		var pic = "1.gif";
//	  	   		if(hostData[i].status=='-1')
//	  	   			pic = "2.gif";
	  	   		getApps(host_ip); 
	  	   			demo+='<div class="box"><a href="#_" onclick=openWindow("'+host_ip+'")>' +
//						  '<span class=img>'+'<img src='+pic+'></span>' +
						  '<div class ="name">主机状态：'+addStatus(statusInfo)+'<br>IP:'+host_ip+'<br>CPU占用：'+cpuInfo+'%<br>内存占用：'+memInfo+'%</div></a>' +
						  '<div class ="name" id="'+host_ip+'"></div>'+
						  '</div>'	;
		    	
	  	   			
	  	   			
	  	  		}
	  	   	
	  	   	ex += '<div class="hostMonitor">' +
						'<div class="border clearfix">' +
						demo+
						'</div>' +
					  '</div>';
	  	   		document.getElementById("host").innerHTML = ex;
	  	   		
	    },
	    failure: function(response) {
	    }
	 });
	}


	

//     	
	 	startApp = function(ip,app_name){
	 		Ext.Ajax.request({
			     url: './serverMonitor.ered?reqCode=control&cmd_type=1&host_ip='+ip+'&app_name='+app_name,
			     success: function(response){
			     		Ext.MessageBox.show({title:'提示',msg:"启动成功!",buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
			     }
			});
	 	}
	 	stopApp = function(ip,app_name){
	 		Ext.Ajax.request({
			     url: './serverMonitor.ered?reqCode=control&cmd_type=2&host_ip='+ip+'&app_name='+app_name,
			     success: function(response){
			     		Ext.MessageBox.show({title:'提示',msg:"关闭成功!",buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
			     }
			});
	 		
	 	}
//页面刷新
var task = {
    run: function(){
		updateServer();
//		requestUpdate();
    },
    interval: 2000 //2 second
}
var runner = new Ext.util.TaskRunner();
runner.start(task);
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
							Ext.MessageBox.show({title:'提示', msg:'获取监控数据失败!',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						},
						params : {}
					});
}