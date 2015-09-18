var VNCMstroe;
var RSMstroe;
var tabs;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	var tpl = new Ext.XTemplate(
	  	   		'<tpl for=".">',
//	  	   			'<tpl if="tstatus==1">',
//	  	   				'<a href="#_" onclick="openWindow(6,{term_record_id})" class="serverBox"><span class="img"><img src="6.gif"></span><span class="name">{term_ip}</span></a>',
//	  	   			'</tpl>',
//	  	   			'<tpl if="!(tstatus==1)">',
	  	   				'<a href="#_" onclick="openWindow({tstatus},{term_record_id})" class="serverBox"><span class="img"><img src="{tstatus}.gif"></span><span class="name">{term_ip}</span></a>',
//	  	   			'</tpl>',
	  	   		'</tpl>'
	  	   	);
	 tabs = new Ext.TabPanel({
	  	   		region:'center',
	  	   		enableTabScroll : true,
	  	   		activeTab:0,
	  	   		autoScroll : true,
//	  	   		autoLoad :reloads,
	  	   		//autoWidth : true,
	  	   		height : 'auto',
	  	   		footerCfg  : ['<p class="serverIconBox"><em class="server_normalIcon">服务器运行</em> <em class="server_normal_upgradeIcon">服务器正常可升级</em> <em class="server_errorIcon">服务器停止</em> <em class="server_upgradeIcon">服务器升级</em><em class="server_offlineIcon">服务器下线</em></p>']
	  	   	});
	 RSMstroe = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url: './serverMonitor.ered?reqCode=queryServerStatusDetailList'
								}),
						reader : new Ext.data.JsonReader({}, [{
											name : 'term_record_id'
										}, {
											name : 'term_name'
										},{
											name : 'term_type'
										}, {
											name : 'term_ip'
										},{
											name : 'tstatus'
										}, {
											name : 'term_port'
										},{
											name : 'term_id'
										}, {
											name : 'cpu_info'
										},{
											name : 'mem_info'
										}, {
											name : 'vnc_total'
										},{
											name : 'vnc_used'
										}, {
											name : 'vnc_free'
										}]),
						baseParams : {
							areacode : ''
						}
					});
//	 .load();
	 VNCMstroe = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url: './serverMonitor.ered?reqCode=queryServerStatusDetailList'
								}),
						reader : new Ext.data.JsonReader({}, [{
											name : 'term_record_id'
										}, {
											name : 'term_name'
										},{
											name : 'term_type'
										}, {
											name : 'term_ip'
										},{
											name : 'tstatus'
										}, {
											name : 'term_port'
										},{
											name : 'term_id'
										}, {
											name : 'cpu_info'
										},{
											name : 'mem_info'
										}, {
											name : 'vnc_total'
										},{
											name : 'vnc_used'
										}, {
											name : 'vnc_free'
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
	    		var termType= model[k].term_type;
	    		var thismodel ='{"vnc_used":'+model[k].vnc_used+',"term_port":"'+model[k].term_port+'","vnc_free":'+model[k].vnc_free+',"mem_info":"'+model[k].mem_info+'","vnc_total":'+model[k].vnc_total+',"tstatus":'+model[k].tstatus+',"cpu_info":"'+model[k].cpu_info+'","term_ip":"'+model[k].term_ip+'","term_record_id":'+model[k].term_record_id+',"term_type":'+model[k].term_type+',"term_name":"'+model[k].term_name+'"}';
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
	  	   		title : '<span class="commoncss">标清</span>',
	  	   		items:[
	  	   			 new Ext.DataView({
	  	   				store: VNCMstroe,
	  	   				tpl: tpl,
	  	   				autoHeight:true,
	  	   				multiSelect: true
	  	   			})
	  	   		],
	  	   		iconCls : 'normalIcon'// 图标
	  	   	},{
	  	   		title : '<span class="commoncss">高清</span>',
	  	   		items:[
	  	   			 new Ext.DataView({
	  	   				store: RSMstroe,
	  	   				tpl: tpl,
	  	   				autoHeight:true,
	  	   				multiSelect: true
	  	   			})
	  	   		],
	  	   		iconCls : 'normalIcon'// 图标
	  	   	});
	 tabs.activate(tabs); 
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
				window.setTimeout("s();",5500);
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
	    		var termType= model[k].term_type;
	    		var thismodel ='{"vnc_used":'+model[k].vnc_used+',"term_port":"'+model[k].term_port+'","vnc_free":'+model[k].vnc_free+',"mem_info":"'+model[k].mem_info+'","vnc_total":'+model[k].vnc_total+',"tstatus":'+model[k].tstatus+',"cpu_info":"'+model[k].cpu_info+'","term_ip":"'+model[k].term_ip+'","term_record_id":'+model[k].term_record_id+',"term_type":'+model[k].term_type+',"term_name":"'+model[k].term_name+'"}';
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
	    		var termType= model[k].term_type;
	    		var thismodel ='{"vnc_used":'+model[k].vnc_used+',"term_port":"'+model[k].term_port+'","vnc_free":'+model[k].vnc_free+',"mem_info":"'+model[k].mem_info+'","vnc_total":'+model[k].vnc_total+',"tstatus":'+model[k].tstatus+',"cpu_info":"'+model[k].cpu_info+'","term_ip":"'+model[k].term_ip+'","term_record_id":'+model[k].term_record_id+',"term_type":'+model[k].term_type+',"term_name":"'+model[k].term_name+'"}';
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
     url: './serverMonitor.ered?reqCode=queryServerDetail&term_record_id='+id,
     success: function(response){
     	current = "";
     	//启动，停止，升级,true为禁止
     	flags = [true,true,true];
     	
     	if(tstatus==6){ //可停止，可升级
			flags=[true,false,false];
			current = "正常运行,可以升级!";
		}
     	if(tstatus==1){
     		flags=[true,false,true];
			current = "正常运行!";
     	}
		if(tstatus==2){ //可启动
			flags=[false,true,true];
			current = "程序不正常!";
		}
		if(tstatus==3){ //宕机
			flags=[true,true,true];
			current = "宕机!";
		}
		if(tstatus==4){ //升级中
			flags=[true,true,true];
			current = "升级中!";
		}
		if(tstatus==7){ //升级中
			flags=[true,true,true];
			current = "服务器丢失!";
		}
		jsonData = eval('('+response.responseText+')');
		
     	message += jsonData.term_ip+"<br><br> 服务器位置:"+jsonData.term_desc;
     	
     	message += "<br><br>"+"当前状态:"+current;
     	
     	message += "<br><br>"+"当前版本:"+jsonData.term_name;
     	
     	message +="<br><br>"+"CPU使用状况:"+jsonData.cpu_info;
     	
     	message += "<br><br>"+"内存使用状况:"+jsonData.mem_info;
     	
     	message += "<br><br>"+"VNC总数:"+jsonData.vnc_total;
     	
     	message += "<br><br>"+"VNC已使用数:"+jsonData.vnc_used;
     	
     	message += "<br><br>"+"VNC剩余数:"+jsonData.vnc_free;
     	if(jsonData.term_type==2){
     		message += "<br><br>"+"会话ID:"+jsonData.session_id;
     		message += "<br><br>"+"vid_rate:"+jsonData.vid_rate;
     		message += "<br><br>"+"VNC端口:"+jsonData.vnc_port;
     		message += "<br><br>"+"QAMIP:"+jsonData.ipqam_ip;
     		message += "<br><br>"+"QAM端口:"+jsonData.ipqam_port;
     	}
		
		var firstWindow = new Ext.Window({
			title : '<span class="commoncss">服务器信息</span>', // 窗口标题
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
					//Ext.MessageBox.alert('提示','启动');
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=startServer&term_record_id='+id,
					     success: function(response){
								firstWindow.hide();
					     		Ext.MessageBox.alert('提示',"本机程序启动成功！");
					     		reflesh();
					     }
					});
				}
					
			}, {
				text : '停止',
				disabled: flags[1],
				iconCls : 'acceptIcon',
				handler : function() {
					// TODO
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=stopServer&term_record_id='+id,
					     success: function(response){
								firstWindow.hide();
					     		Ext.MessageBox.alert('提示',"本机程序已停止！");
					     		reflesh();
					     }
					});
				}
			},{
				text : '升级',
				disabled: flags[5],
				iconCls : 'acceptIcon',
				handler : function() {
					// TODO
					Ext.Ajax.request({
					     url: './serverMonitor.ered?reqCode=updateServer&term_record_id='+id,
					     success: function(response){
								firstWindow.hide();
					     		Ext.MessageBox.alert('提示',"本机程序升级成功！");
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