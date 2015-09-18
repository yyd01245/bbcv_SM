/**
 * 2013-08-19
 */
//Ext.onReady(function(){
//	Ext.Msg.minWidth = 300 ;
//	Ext.Msg.maxWidth =360 ;
//	window.setTimeout("serverUpdate();",10000);
//
//
//	var runner4 = new Ext.util.TaskRunner();
//	
//		runner4.start({
//			run : function(){
//				Ext.Ajax.request({
//					url : './urm/qamDyAction.ered?reqCode=getD6R6Promt',
//					waitTitle : '提示',
//					method : 'POST',
//					waitMsg : '正在处理数据,请稍候...',
//					success : function(response){
//						var t = eval('('+response.responseText+')');
//						if(t.success){
//							new Ext.ux.ToastWindow({
//						  title: 'IPQAM上报警告',
//						  html: '<h1 align="center" style="font-size:16px;color:red">&nbsp;&nbsp;&nbsp;&nbsp;IPQAM上报的资源有变动！</h1><br/>' +
//						  '<b>&nbsp;&nbsp;&nbsp;&nbsp;请尽快去【资源管理】->【IPQAM管理】->【IPQAM上报告警信息】查看详情，做相应的处理！</b>',
//						  iconCls: 'error',
//						  activate:true
//						}).show(document);
//						}
//					},
//					params : {
//						oper_type : 'DEL',
//						status:0
//					}
//				})
//			},
//			interval:1800000
//		});
//
//	var taskRunner;
//	if(taskRunner!=null){
//		taskRunner.stopAll();
//	}
//	var task = {
//	    run: function(){
//			serverUpdate();
//	    },
//	    interval: 60000 //60 second
//	}
//	taskRunner = new Ext.util.TaskRunner();
//	taskRunner.start(task);
//		
////	var viewport = new Ext.Viewport({
////		layout : 'anchor',
////		items:[panelErr,panelHost,panelRe,panelExp]
////	});
//	var viewport = new Ext.Viewport({
//		layout : 'border',
//		bodyStyle :'background:#CCDFEA',
//		items:[]
//			
//	});
//});

	function serverUpdate(){
		Ext.Ajax.request({
		    url: 'crsm/crsmServerMonitor.ered?reqCode=queryServerByApiAndInsertDB',
		    success: function(response) {
		    }
		})
	}