/**
 * 获取服务器信息及内存CPU实时监控
 * 
 * @author XiongChun
 * @since 2010-11-27
 */
Ext.onReady(function() {

	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
			var serverInfoGrid = new Ext.grid.PropertyGrid({
						title : '<span style="font-weight:bold;">服务器信息</span>',
						width : 320,
						collapsible : true,
						split : true,
						region : 'west',
						source : jsonInfo
					});

			serverInfoGrid.on("beforeedit", function(e) {
						// e.cancel = true;
						// return false;
					});

			var jvmMemPanel = new Ext.Panel({
						title : '<span>JVM内存监控视图</span>',
						contentEl : 'jvmMemChart_div',
						region : 'center',
						bodyStyle:'5,5,5,5',
						autoScroll:true
					});

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [serverInfoGrid, jvmMemPanel]
					});

			var jvmtask = {
				run : function() {
					updateJVMChart();
				},
				interval : 3000
			};

			var taskRunner = new Ext.util.TaskRunner();
			taskRunner.start(jvmtask);

			function updateJVMChart() {
				Ext.Ajax.request({
							url : 'serverInfo.ered?reqCode=updateJvmChart',
							//url : 'serverInfo.ered?reqCode=updateHostMemChart',
							success : function(response, opts) {
								var resultArray = Ext.util.JSON
										.decode(response.responseText);
								var xmlstring = resultArray.xmlstring;
								updateChartXML('jvmMemChart', xmlstring);
							},
							failure : function(response, opts) {
								Ext.MessageBox.show({title:'提示', msg:'获取监控数据失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							},
							params : {}
						});
			}

		});