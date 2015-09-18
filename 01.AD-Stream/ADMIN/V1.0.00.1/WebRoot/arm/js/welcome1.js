/**
 * 2013-08-19
 */
Ext.onReady(function(){
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
	window.setTimeout("serverUpdate();",10000);
	var panelErr = new Ext.Panel({
		title : '错误码警告',
//		region : 'north',
//		layout : 'border',
//		border : false,
//		region : 'north',
		split: true,
		collapsible : true,
		collased : true,
		hidden : true,
		bodyStyle :'background:#CCDFEA;float:left;margin:5px;background:#BBD6E6;border:1px solid #D8E8F2;' +
		'border-radius:15px;box-shadow:0 0 7px #94B8D1 inset;padding:15px;',
		autoScroll : true,
		html : '<p style ="font-size : 14px;" id = "errorInfo"></p>',
		listeners : {
			render : function(){
				  queryError();
			}
		}
	});
	
	var panelHost = new Ext.Panel({
		title : '服务器状态警告',
		split: true,
		collapsible : true,
		collased : true,
		hidden : true,
		bodyStyle :'background:#CCDFEA;float:left;margin:5px;background:#BBD6E6;border:1px solid #D8E8F2;' +
		'border-radius:15px;box-shadow:0 0 7px #94B8D1 inset;padding:15px;',
		autoScroll : true,
		html : '<p style ="font-size : 14px;" id = "hostInfo"></p>',
		listeners : {
			render : function(){
				queryHost();
			}
		}
	});
	
	var panelRe = new Ext.Panel({
		title : 'URM资源重复警告',
//		region : 'west',
//		layout : 'border',
//		border : false,
		split: true,
		hidden : true,
		collapsible : true,
		collased : true,
		bodyStyle :'background:#CCDFEA;float:left;margin:5px;background:#BBD6E6;border:1px solid #D8E8F2;' +
		'border-radius:15px;box-shadow:0 0 7px #94B8D1 inset;padding:15px;',
		autoScroll : true,
		html : '<p style ="font-size : 14px;" id = "reResInfo"></p>',
		listeners : {
			render : function(){
					queryRe();
			}
		}
	});
	
	var panelExp = new Ext.Panel({
		title : '资源异常警告',
//		region : 'center',
//		layout : 'border',
//		border : false,
		split: true,
		hidden : true,
		collapsible : true,
		collased : true,
		bodyStyle :'background:#CCDFEA;float:left;margin:5px;background:#BBD6E6;border:1px solid #D8E8F2;' +
		'border-radius:15px;box-shadow:0 0 7px #94B8D1 inset;padding:15px;',
		autoScroll : true,
		html : '<p style ="font-size : 14px;" id = "expResInfo"></p>',
		listeners : {
			render : function(){
				queryExp();
			}
		}
	});
	
	var queryError = function(){
		Ext.Ajax.request({
			url : 'monitorAction.ered?reqCode=queryError',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(response){
				var t = eval('('+response.responseText+')');
				var errorInfo = document.getElementById("errorInfo");
				if(t.success){	
					errorInfo.innerHTML = "<h2>错误码数量已超出阈值，急需处理：<a  href='#' onclick='openErrTab()'>错误码处理</a></h2><p style='margin:2px;'>需处理的错误码：<br/><span>"+t.strChecked+"</span></p>";					
				}else{
					errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
				}
			},
			failure : function(response){
				var errorInfo = document.getElementById("errorInfo");
				errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
			}
		})
	};
	
	var queryHost = function(){
		Ext.Ajax.request({
			url : 'monitorAction.ered?reqCode=queryHost',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(response){
				var t = eval('('+response.responseText+')');
					if(t.success){
						var errorInfo = document.getElementById("hostInfo");
						errorInfo.innerHTML = "<h2>服务器状态出现异常，详情查看：<a href='#' onclick='openHostTab()'>服务器阈值设置</a></h2><p style='margin:2px;'>问题服务器IP：<br/><span>"+t.strChecked+"</span></p>";
					}else{
						var errorInfo = document.getElementById("hostInfo");
						errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
					}	
			},
			failure : function(response){
				var errorInfo = document.getElementById("hostInfo");
				errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
			}
		})
	};
	
	var queryRe = function(){
		Ext.Ajax.request({
			url : './cscs/sessionManage.ered?reqCode=hasReUrmRes',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(response){
				var t = eval('('+response.responseText+')');
				if(t.success){
						var errorInfo = document.getElementById("reResInfo");
						errorInfo.innerHTML = "<h2>出现URM资源重复使用，详情查看：<a href='#' onclick='openReTab()'>URM重复申请资源查询</a></h2>";	
				}else{
						var errorInfo = document.getElementById("reResInfo");
						errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
				}	
		},
		failure : function(response){
				var errorInfo = document.getElementById("reResInfo");
				errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
			}
		})
	};
	
	var queryExp = function(){
		Ext.Ajax.request({
//			url : 'monitorAction.ered?reqCode=queryExpRes',
			url : './cscs/sessionManage.ered?reqCode=hasExpRes',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(response){
				var t = eval('('+response.responseText+')');
				if(t.success){
					var errorInfo = document.getElementById("expResInfo");
					errorInfo.innerHTML = "<h2>出现资源异常，详情查看：<a href='#' onclick='openExpTab()'>异常资源处理</a></h2>";
				}else{
					var errorInfo = document.getElementById("expResInfo");
					errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
				}
			},
			failure : function(response){
				var errorInfo = document.getElementById("expResInfo");
				errorInfo.innerHTML = "<h2>无异常信息！</h2>" ;
			}
		})
	};

 	panelErr.show();
 	panelRe.show();
 	panelHost.show();
 	panelExp.show() ;
 	
	var　runner　=　new　Ext.util.TaskRunner ();
　　　　runner.start({　　　　　　//任务被调用的方法
　　　　　　　run:　 function(){
  				queryError();
　　　　　　　 },
　　　　　　　interval:　300000　//5分钟执行一次
　　　　});
	var　runner1　=　new　Ext.util.TaskRunner ();
　　　　runner1.start({　　　　　　//任务被调用的方法
　　　　　　　run:　 function(){
				queryRe();
　　　　　　　 },
　　　　　　　interval:　43000　//43秒执行一次
　　　　});
	var　runner2　=　new　Ext.util.TaskRunner ();
　　　　runner2.start({　　　　　　//任务被调用的方法
　　　　　　　run:　 function(){
				queryHost();
　　　　　　　 },
　　　　　　　interval:　41000　//41秒执行一次
　　　　});
	var　runner3　=　new　Ext.util.TaskRunner ();
　　runner3.start({　　　　　　//任务被调用的方法
　　　　　　　run:　 function(){
				queryExp();
　　　　　　　 },
　　　　　　　interval:　47000　//47秒执行一次
　　　　});

	var runner4 = new Ext.util.TaskRunner();
	
		runner4.start({
			run : function(){
				Ext.Ajax.request({
					url : './urm/qamDyAction.ered?reqCode=getD6R6Promt',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(response){
						var t = eval('('+response.responseText+')');
						if(t.success){
							new Ext.ux.ToastWindow({
						  title: 'IPQAM上报警告',
						  html: '<h1 align="center" style="font-size:16px;color:red">&nbsp;&nbsp;&nbsp;&nbsp;IPQAM上报的资源有变动！</h1><br/>' +
						  '<b>&nbsp;&nbsp;&nbsp;&nbsp;请尽快去【资源管理】->【IPQAM管理】->【IPQAM上报告警信息】查看详情，做相应的处理！</b>',
						  iconCls: 'error',
						  activate:true
						}).show(document);
						}
					},
					params : {
						oper_type : 'DEL',
						status:0
					}
				})
			},
			interval:1800000
		});

	var taskRunner;
	if(taskRunner!=null){
		taskRunner.stopAll();
	}
	var task = {
	    run: function(){
			serverUpdate();
	    },
	    interval: 60000 //60 second
	}
	taskRunner = new Ext.util.TaskRunner();
	taskRunner.start(task);
		
//	var viewport = new Ext.Viewport({
//		layout : 'anchor',
//		items:[panelErr,panelHost,panelRe,panelExp]
//	});
	var viewport = new Ext.Viewport({
		layout : 'border',
		bodyStyle :'background:#CCDFEA',
		items:[{
			region : 'north',
			split : true,
			height :150,
			width :'100%',
			bodyStyle :'background:#CCDFEA',
			items:[panelHost]
		},{
			region : 'center',
			layout : 'border',
			height :'70%',
			bodyStyle :'background:#CCDFEA',
			border:false,
			items : [{
				region : 'west',
				split : true,
				height:'100%',
				width:'30%',
				bodyStyle :'background:#CCDFEA',
				items:[panelErr]
			},{
				region : 'east',
				split : true,
				height:'100%',
				width:'30%',
				bodyStyle :'background:#CCDFEA',
				items:[panelRe]
			}, {
				region : 'center',
				split : true,
				height:'100%',
				width:'40%',
				bodyStyle :'background:#CCDFEA',
				items:[panelExp]
			}]
		}]
	});
});
	var openErrTab = function(){
		parent.addTab('monitor/platmentMenage.ered?reqCode=ProblemInit1','错误码处理','01141001','云视频服务平台 -> 监控管理 -> 系统告警 -> 错误码处理','tab_blank.png ');
	};
	
	var openHostTab = function(){
//		parent.addTab('rpm/serverMonitor.ered?reqCode=accHisInit','','mypage2','云视频服务平台 -> 监控管理 -> 主机服务器监控','tab_blank.png ');			
	 	parent.addTab('monitor/platmentMenage.ered?reqCode=toHostInfo','服务器阈值设置','01141004','云视频服务平台 -> 监控管理 -> 系统告警 -> 服务器阈值设置','tab_blank.png ');
	
	};
	var openReTab = function(){
		parent.addTab('cscs/sessionManage.ered?reqCode=toReQamSession','URM重复申请资源查询','011207','云视频服务平台 -> 会话管理 -> URM重复申请资源查询','tab_blank.png ');
	};
	var openExpTab = function(){
		parent.addTab('cscs/sessionManage.ered?reqCode=toExpSessionQuery','异常资源处理','011203','云视频服务平台 -> 会话管理 -> 异常资源处理','tab_blank.png ');
	};
	function serverUpdate(){
		Ext.Ajax.request({
		    url: 'crsm/crsmServerMonitor.ered?reqCode=queryServerByApiAndInsertDB',
		    success: function(response) {
		    }
		})
	}