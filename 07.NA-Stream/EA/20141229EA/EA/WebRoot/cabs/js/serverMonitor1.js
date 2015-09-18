var VNCMstroe;
var RSMstroe;
var tabs;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
//	var stroe = new Ext.data.JsonStore({
//	  	   		url: './serverMonitor.ered?reqCode=queryServerStatus',
//		   		root: 'ROOT',
//		   		fields: [
//		   			'term_record_id','term_ip','tstatus','term_port','term_id','vstatus','cpu_info','mem_info',
//		   			'vnc_total','vnc_used','vnc_free'
//		   		]
//		   	  });
	     var tpl = new Ext.XTemplate(
	  	   		'<tpl for=".">',
					'<a href="#_" onclick="openWindow({status},{vncm_id})" class="serverBox"><span class="img"><img src="{status}.gif"></span><span class="name">{group_name}<br>{vncm_ip}</span></a>',
	  	   		'</tpl>'
	  	   	);
	 tabs = new Ext.TabPanel({
	  	   		region:'center',
	  	   		enableTabScroll : true,
	  	   		activeTab:0,
	  	   		autoScroll : true,
	  	   		height : 'auto',
	  	   		footerCfg  : ['<p class="serverIconBox"><em class="server_normalIcon">服务器运行</em> <em class="server_normal_upgradeIcon">服务器正常可升级</em> <em class="server_errorIcon">服务器停止</em> </p>']
	  	   	});
	 RSMstroe = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url: './serverMonitor.ered?reqCode=queryServerStatusDetailList'
								}),
						reader : new Ext.data.JsonReader({}, [{
											name : 'vncm_id'
										}, {
											name : 'vncm_ip'
										},{
											name : 'vncm_name'
										}, {
											name : 'vncm_port'
										},{
											name : 'status'
										}, {
											name : 'vncm_type'
										}, {
											name : 'cpu_info'
										},{
											name : 'mem_info'
										}, {
											name : 'vncm_total'
										},{
											name : 'vncm_online'
										}, {
											name : 'version'
										},{
											name : 'vncm_message'
										},{
											name : 'group_name'
										}]),
						baseParams : {
							areacode : ''
						}
					});
	 VNCMstroe = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url: './serverMonitor.ered?reqCode=queryServerStatusDetailList'
								}),
						reader : new Ext.data.JsonReader({}, [{
											name : 'vncm_id'
										}, {
											name : 'vncm_ip'
										},{
											name : 'vncm_name'
										}, {
											name : 'vncm_port'
										},{
											name : 'status'
										}, {
											name : 'vncm_type'
										}, {
											name : 'cpu_info'
										},{
											name : 'mem_info'
										}, {
											name : 'vncm_total'
										},{
											name : 'vncm_online'
										}, {
											name : 'version'
										},{
											name : 'vncm_message'
										},{
											name : 'group_name'
										}]),
						baseParams : {
							areacode : ''
						}
					});
	RSMstroe.load();
	VNCMstroe.load();
	 Ext.Ajax.request({
	    url: './serverMonitor.ered?reqCode=queryServerStatusDetailList',
	    success: function(response) {
		RSMstroe.removeAll();
		VNCMstroe.removeAll();
	    var groupJson = eval('('+response.responseText+')');
	    var total = groupJson.TOTALCOUNT;
	    var model = groupJson.ROOT;
	    var bq=new Array();
	    var gq = new Array();
	    for(var i=0;i<total;i++){
	    	(function(){
	    		var k = i;
	    		var termType= model[k].vncm_type;
	    		var thismodel ='{"group_name":"'+model[k].group_name+'","vncm_id":'+model[k].vncm_id+',"vncm_ip":"'+model[k].vncm_ip+'","vncm_name":"'+model[k].vncm_name+'","vncm_port":'+model[k].vncm_port+',"status":'+model[k].status+',"vncm_type":'+model[k].vncm_type+',"cpu_info":"'+model[k].cpu_info+'","mem_info":"'+model[k].mem_info+'","vncm_total":'+model[k].vncm_total+',"vncm_online":'+model[k].vncm_online+',"version":"'+model[k].version+'"}';
		    	var modell = eval('('+thismodel+')');
	    		if(termType==2){
	    			gq.push(modell);
		    	}else{
		    		bq.push(modell);
		    	}
	    	})();
	    }
		    RSMstroe.loadData(gq,true);
		    VNCMstroe.loadData(bq,true);
	    }	
	});
	  
	tabs.add({
				layout:'fit',
	  	   		title : '<span>承载服务器管理</span>',
	  	   		items:[
	  	   			 new Ext.DataView({
	  	   				store: VNCMstroe,
	  	   				tpl: tpl,
	  	   				autoScroll:true,
	  	   				multiSelect: true
	  	   			})
	  	   		],
	  	   		iconCls : 'normalIcon'// 图标
//	  	   	},{
//	  	   		layout:'fit',
//	  	   		title : '<span>高清</span>',
//	  	   		items:[
//	  	   			 new Ext.DataView({
//	  	   				store: RSMstroe,
//	  	   				tpl: tpl,
//	  	   				autoScroll:true,
//	  	   				multiSelect: true
//	  	   			})
//	  	   		],
//	  	   		iconCls : 'normalIcon'// 图标
	  	   	});
//	 tabs.activate(tabs);
	 tabs.setActiveTab(0)
	 var viewport = new Ext.Viewport({
	   		layout : 'border',
	   		items : [tabs]
	   	});
	
})
	var taskRunner;
	if(taskRunner!=null){
		taskRunner.stopAll();
	}
	var task = {
			run : function() {
				window.setTimeout("reloads();",5000);
				window.setTimeout("s();",8000);
			},
		interval :10000
	};

	taskRunner = new Ext.util.TaskRunner();
	taskRunner.start(task);
	
	function reloads(){
		Ext.Ajax.request({
	    url: './serverMonitor.ered?reqCode=queryServerStatusDetailList',
	    success: function(response) {
		VNCMstroe.removeAll();
	    var groupJson = eval('('+response.responseText+')');
	    var total = groupJson.TOTALCOUNT;
	    var model = groupJson.ROOT;
	    var bqt=new Array();
	    var gqt = new Array();
	    for(var i=0;i<total;i++){
	    	(function(){
	    		var k = i;
	    		var termType= model[k].vncm_type;
				var thismodel ='{"group_name":"'+model[k].group_name+'","vncm_id":'+model[k].vncm_id+',"vncm_ip":"'+model[k].vncm_ip+'","vncm_name":"'+model[k].vncm_name+'","vncm_port":'+model[k].vncm_port+',"status":'+model[k].status+',"vncm_type":'+model[k].vncm_type+',"cpu_info":"'+model[k].cpu_info+'","mem_info":"'+model[k].mem_info+'","vncm_total":'+model[k].vncm_total+',"vncm_online":'+model[k].vncm_online+',"version":"'+model[k].version+'"}';
	    		var modell = eval('('+thismodel+')');
	    		if(termType==2){
	    			gqt.push(modell);
		    	}else{
		    		bqt.push(modell);
		    	}
	    	})();
	    }
		    VNCMstroe.loadData(bqt,true);
	    }
	}); 
   }
	
	function s(){
		Ext.Ajax.request({
	    url: './serverMonitor.ered?reqCode=queryServerStatusDetailList',
	    success: function(response) {
		RSMstroe.removeAll();
	    var groupJson = eval('('+response.responseText+')');
	    var total = groupJson.TOTALCOUNT;
	    var model = groupJson.ROOT;
	    var bqt=new Array();
	    var gqt = new Array();
	    for(var i=0;i<total;i++){
	    	(function(){
	    		var k = i;
	    		var termType= model[k].vncm_type;
				var thismodel ='{"group_name":"'+model[k].group_name+'","vncm_id":'+model[k].vncm_id+',"vncm_ip":"'+model[k].vncm_ip+'","vncm_name":"'+model[k].vncm_name+'","vncm_port":'+model[k].vncm_port+',"status":'+model[k].status+',"vncm_type":'+model[k].vncm_type+',"cpu_info":"'+model[k].cpu_info+'","mem_info":"'+model[k].mem_info+'","vncm_total":'+model[k].vncm_total+',"vncm_online":'+model[k].vncm_online+',"version":"'+model[k].version+'"}';
	    		var modell = eval('('+thismodel+')');
	    		if(termType==2){
	    			gqt.push(modell);
		    	}else{
		    		bqt.push(modell);
		    	}
	    	})();
	    }
		    RSMstroe.loadData(gqt,true);
	    }
	}); 
	}
	
function openWindow(tstatus, id) {
	var jsonData ;
	var message = "IP为";
	Ext.Ajax.request({
     url: './serverMonitor.ered?reqCode=queryServerDetail&vncm_id='+id,
     success: function(response){
     	current = "";
     	//启动，停止，升级,true为禁止
     	flags = [true,true,true];
     	
     	if(tstatus==1){
     		flags=[true,false,true];
			current = "正常运行!";
     	}
		if(tstatus==2){ //可启动
			flags=[false,true,true];
			current = "程序不正常!";
		}
		if(tstatus==4){ //升级中
			flags=[true,false,false];
			current = "可升级!";
		}
		jsonData = eval('('+response.responseText+')');
		
		message += jsonData.vncm_ip+"<br><br> 服务器端口:"+jsonData.vncm_port;
		
     	message +="<br><br> 服务器名称:"+jsonData.vncm_name;
     	
     	message +="<br><br> 服务器信息:"+jsonData.vncm_message;
     	
     	message += "<br><br>"+"当前状态:"+current;
     	
     	message += "<br><br>"+"当前版本:"+jsonData.version;
     	
     	message +="<br><br>"+"CPU使用状况:"+jsonData.cpu_info;
     	
     	message += "<br><br>"+"内存使用状况:"+jsonData.mem_info;
     	
     	message += "<br><br>"+"支持路数:"+jsonData.vncm_total;
     	
     	message += "<br><br>"+"在线路数:"+jsonData.vncm_online;
     	
//     	if(jsonData.vncm_type==2){
//     		message += "<br><br>"+"会话ID:"+jsonData.session_id;
//     		message += "<br><br>"+"vid_rate:"+jsonData.vid_rate;
//     		message += "<br><br>"+"VNC端口:"+jsonData.vnc_port;
//     		message += "<br><br>"+"QAMIP:"+jsonData.ipqam_ip;
//     		message += "<br><br>"+"QAM端口:"+jsonData.ipqam_port;
//     	}
		
		var firstWindow = new Ext.Window({
			title : '<span>服务器信息</span>', // 窗口标题
			html: '<div style="padding:10px; font-size:12px;">'+message+'</div>',
			iconCls : 'imageIcon',
			autoScroll : true,
			layout : 'fit', // 设置窗口布局模式
			width : 350, // 窗口宽度
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
			buttons : [ {
				text : '启动',
				disabled: flags[0],
				iconCls : 'acceptIcon',
				handler : function() {
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=startServer&vncm_id='+id,
					     success: function(response){
								firstWindow.hide();
								var resultArray = Ext.util.JSON.decode(response.responseText);
								if(resultArray.success){
									Ext.Msg.show({title:'提示',msg:'启动事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								}else{
									Ext.Msg.show({title:'提示',msg:'启动事件添加失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								}
					     		reflesh();
					     }
					});
				}
					
			}, {
				text : '停止',
				disabled: flags[1],
				iconCls : 'acceptIcon',
				handler : function() {
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=stopServer&vncm_id='+id,
					     success: function(response){
								firstWindow.hide();
					     		var resultArray = Ext.util.JSON.decode(response.responseText);
								if(resultArray.success){
									Ext.Msg.show({title:'提示',msg:'停止事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								}else{
									Ext.Msg.show({title:'提示',msg:'停止事件添加失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								}
					     		reflesh();
					     }
					});
				}
			},{
				text : '升级',
				disabled: flags[2],
				iconCls : 'acceptIcon',
				handler : function() {
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=updateServer&vncm_id='+id,
					     success: function(response){
								firstWindow.hide();
					     		Ext.Msg.show({title:'提示',msg:'升级事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					     		reflesh();
					     }
					});
				}
			},
				{
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					firstWindow.hide();
				}
			} ]
		});
		firstWindow.show(); // 显示窗口
	}
});
	
};