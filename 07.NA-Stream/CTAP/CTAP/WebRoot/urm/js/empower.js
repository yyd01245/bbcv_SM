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
						title : '<span style="font-weight:bold;">授权信息</span>',
						width : 320,
					
					//	collapsible : true,
					//	split : true,
						region : 'west',
						source : jsonInfo
					});

			serverInfoGrid.on("beforeedit", function(e) {
						// e.cancel = true;
						// return false;
					});

			var jvmMemPanel = new Ext.Panel({
						title : '<span>编辑授权文件</span>',
						contentEl : 'jvmMemChart_div',
						region : 'center',
						
						bodyStyle:'5,5,5,5',
						autoScroll:true
					
					});

			var viewport = new Ext.Viewport({
						layout : 'border',
						bodyStyle :'background:#CCDFEA',
						items : [serverInfoGrid, jvmMemPanel]
					});

			
			
			
			
			var jvmtask = {
				run : function() {
					updateJVMChart();
				},
			//	interval : 3000
			};

			var taskRunner = new Ext.util.TaskRunner();
			taskRunner.start(jvmtask);

			function updateJVMChart() {}

			
			
			
			


			var panel = new Ext.form.FormPanel({
						layout : 'fit',
						frame : true,
						items : [{
									id : 'htmleditor',
									name : 'htmleditor',
									xtype : 'htmleditor'
								}]
					});

			var firstWindow = new Ext.Window({
						title : '<span class="commoncss">富文本输入</span>', // 窗口标题
						layout : 'fit', // 设置窗口布局模式
						width : 850, // 窗口宽度
						height : 470, // 窗口高度
						closable : false, // 是否可关闭
						collapsible : false, // 是否可收缩
						maximizable : true, // 设置是否可以最大化
						border : false, // 边框线设置
						constrain : true, // 设置窗口是否可以溢出父容器
						draggable : false,
						animateTarget : Ext.getBody(),
						pageY : 25, // 页面定位Y坐标
						pageX : 380, // 页面定位X坐标
						items : [panel], // 嵌入的表单面板
						buttons : [{ // 窗口底部按钮配置
							text : '保存', // 按钮文本
							iconCls : 'acceptIcon', // 按钮图标
							handler : function() { // 按钮响应函数
								var value = Ext.getCmp('htmleditor').getValue();
								// Ext.MessageBox.alert('提示', value);
								alert(value);
							}
						}, {	// 窗口底部按钮配置
									text : '重置', // 按钮文本
									iconCls : 'tbar_synchronizeIcon', // 按钮图标
									handler : function() { // 按钮响应函数
										panel.form.reset();
									}
								}]
					});
		//	firstWindow.show(); // 显示窗口

			Ext.getCmp('htmleditor').on('initialize', function() {
						Ext.getCmp('htmleditor').focus();
					})
		
			
			
		});