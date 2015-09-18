/**
 * 承载服务器管理
 * 
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	
	function QTYPERender(value){
		if(value == 0)
			return "全部";
		else if(value==1)
			return "支持门户";
		else if(value==2)
			return "电视上网";
		else if(value==4)
			return "支持音频功能";
	}
	
	var qForm = new Ext.form.FormPanel({
						region : 'north',
						title : '<span>查询条件<span>',
						collapsible : true,
						border : true,
						labelWidth : 100, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 100,
						items : [{
									layout : 'column',
									border : false,
									items : [{
//												columnWidth : .22,
//												layout : 'form',
//												labelWidth : 60, // 标签宽度
//												defaultType : 'textfield',
//												border : false,
//												items : [new Ext.form.ComboBox({
//																name : 'vtype',
//																hiddenName : 'vtype',
//																fieldLabel : '承载类型',
//																editable : false ,
//																triggerAction : 'all',
//																value : '1',
//																store : VTYPEStore,
//																displayField : 'text',
//																valueField:'value',
//																mode : 'local',
//																forceSelection : true,
//																typeAhead : true,
//																resizable : true,
//																anchor : '95%',
//																listeners:{
//																	'select':function(){
//																		queryBalanceInfo(qForm.getForm());																			
//																	}
//																}
//															})]
//											}, {
												columnWidth : .22,
												layout : 'form',
												labelWidth :60, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [new Ext.form.ComboBox({
																name : 'qtype',
																hiddenName : 'qtype',
																fieldLabel : '查询类型',
																editable : false ,
																triggerAction : 'all',
																value : '0',
																store : CHECKTYPEStore,
																displayField : 'text',
																valueField:'value',
																mode : 'local',
																forceSelection : true,
																typeAhead : true,
																resizable : true,
																anchor : '95%',
																listeners:{
																	'select':function(field,e){
																		queryBalanceInfo(qForm.getForm());	
																	}
																}
															})]
											}, {
												columnWidth : .22,
												layout : 'form',
												labelWidth : 60, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [new Ext.form.ComboBox({
																name : 'ltype',
																hiddenName : 'ltype',
																fieldLabel : '承载状态',
																editable : false ,
																triggerAction : 'all',
																value : '1',
																store : VSTATUSStore,
																displayField : 'text',
																valueField:'value',
																mode : 'local',
																forceSelection : true,
																typeAhead : true,
																resizable : true,
																anchor : '95%',
																listeners:{
																	'select':function(){
																		queryBalanceInfo(qForm.getForm());
																		var params = qForm.getForm().getValues();
																		var s = eval(params);
																		var status_now = s["ltype"];
																		if(status_now==1){
																			Ext.getCmp('download').disable();
																			Ext.getCmp('upload').enable();
																		}else if(status_now==2){
																			Ext.getCmp('download').enable();
																			Ext.getCmp('upload').disable();
																		}else{
																			Ext.getCmp('download').disable();
																			Ext.getCmp('upload').disable();
																		}
																	}
																}
															})]
											}]
								}],
								buttons : [{
													text : '查询',
													iconCls : 'previewIcon',
													handler : function() {
														queryBalanceInfo(qForm.getForm());
													}
												}, {
													text : '重置',
													iconCls : 'tbar_synchronizeIcon',
													handler : function() {
														qForm.getForm().reset();
														queryBalanceInfo(qForm.getForm());
														Ext.getCmp('download').disable();
														Ext.getCmp('upload').enable();
													}
												}]
					});
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
						header : 'NO',
						width : 28
					});
			var sm = new Ext.grid.CheckboxSelectionModel();
			// 定义列模型
			var cm = new Ext.grid.ColumnModel([rownum,sm,{
				header : '承载标识',
				dataIndex : 'vncid',
				sortable : true
			},{
				header : '承载类型',
				dataIndex : 'vtype',
				renderer:VTYPERender
			},{
				header : '查询类型',
				dataIndex : 'qtype',
				renderer:QTYPERender
			},{
				header : '承载状态',
				dataIndex : 'ltype',
				renderer:VSTATUSRender
			},{
				header : '序列号',
				dataIndex : 'serialno'
			},{
				header : '返回码',
				dataIndex : 'retcode'
			}]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
						proxy : new Ext.data.HttpProxy({
									url : './appGroupManager.ered?reqCode=aimQuery'
								}),
						// 数据读取器
						reader : new Ext.data.JsonReader({
									totalProperty : 'TOTALCOUNT', // 记录总数
									root : 'ROOT' // Json中的列表数据根节点
								}, [{
											name : 'vncid' // Json中的属性Key值
										},{
											name : 'vtype'
										},{
											name :'qtype'
										},{
											name :'serialno'
										},{
											name :'retcode'
										},{
											name :'ltype'
										}])
					});

			// 翻页排序时带上查询条件
			store.on('beforeload', function() {
						this.baseParams = qForm.getForm().getValues();
					});
			// 每页显示条数下拉选择框
			var pagesize_combo = new Ext.form.ComboBox({
						name : 'pagesize',
						triggerAction : 'all',
						mode : 'local',
						store : new Ext.data.ArrayStore({
									fields : ['value', 'text'],
									data : [[10, '10条/页'], [20, '20条/页'], [50, '50条/页'], [100, '100条/页'], [250, '250条/页'], [500, '500条/页']]
								}),
						valueField : 'value',
						displayField : 'text',
						value : '20',
						editable : false,
						width : 85
					});

			// 改变每页显示条数reload数据
			pagesize_combo.on("select", function(comboBox) {
						bbar.pageSize = parseInt(comboBox.getValue());
						number = parseInt(comboBox.getValue());
						store.reload({
									params : {
										start : 0,
										limit : bbar.pageSize
									}
								});
					});
			var number = parseInt(pagesize_combo.getValue());
			// 分页工具栏
			var bbar = new Ext.PagingToolbar({
						pageSize : number,
						store : store,
						displayInfo : true,
						displayMsg : '显示{0}条到{1}条,共{2}条',
						plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
						emptyMsg : "没有符合条件的记录",
						items : ['-', '&nbsp;&nbsp;', pagesize_combo]
					});

			// 表格实例
			var grid = new Ext.grid.GridPanel({
						region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
						// collapsible : true,
						// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
						title : '<span style="font-weight:bold;">承载服务器信息</span>',
						// height : 500,
						autoScroll : true,
						frame : true,
						store : store, // 数据存储
						stripeRows : true, // 斑马线
						cm : cm, // 列模型
						sm : sm,
						tbar : [{
							text : '承载服务器预下线',
							iconCls : 'deleteIcon',
							id:'download',
							disabled:true,
							handler : function() {
								if (runMode == '0') {
									Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
									return;
								}
								killSession();
							}
						},{
							text : '承载服务器上线',
							iconCls : 'uploadIcon',
							id:'upload',
//							disabled:true,
							handler : function() {
								if (runMode == '0') {
									Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
								
									return;
								}
								updownService();
							}
//						},'-', {
//							text : '刷新',
//							iconCls : 'arrow_refreshIcon',
//							handler : function() {
//								refreshSessionTable();
//							}
						}],
//						bbar : bbar,// 分页工具栏
						viewConfig : {
							// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
							forceFit : true
						},
						loadMask : {
							msg : '正在加载表格数据,请稍等...'
						}
					});

			// 布局
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [qForm, grid]
					});

			// 查询表格数据
			function queryBalanceInfo(pForm) {
				var params = pForm.getValues();
				params.vtype=1;
//				params.start = 0;
//				params.limit = bbar.pageSize;
				store.load({
							params : params
						});
			}
			/**
			 * 杀死会话
			 */
			function killSession() {
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
						Ext.Msg.show({title:'提示',msg: '请先选中要下架的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
								
					return;
				}
				var strChecked1 = jsArray2JsString(rows, 'vncid');
				var strChecked2 = jsArray2JsString(rows, 'vtype');
				var strChecked3 = jsArray2JsString(rows, 'ltype');
				Ext.Msg.confirm('请确认', '你确定要下架所选服务器吗?', function(btn, text) {
					if (btn == 'yes') {
						Ext.Ajax.request({
									url : './appGroupManager.ered?reqCode=vncShutdown',
									success : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										var t = eval('('+response.responseText+')');
										if(!t.success){
												Ext.Msg.show({title:'提示',msg: '承载服务器预下架失败或该服务器已下架！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								
										}else{
											Ext.Msg.show({title:'提示',msg: '承载服务器预下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								
										}
										store.reload();
									},
									params : {
										strChecked1 : strChecked1,
										strChecked2 : strChecked2,
										strChecked3 : strChecked3
									}
								});
						}
					})
			}
			
			/***********************S 承载服务器上架开始 S********************************/
			function updownService() {
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg: '请先选中要上架的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var strChecked1 = jsArray2JsString(rows, 'vncid');
				var strChecked2 = jsArray2JsString(rows, 'vtype');
				var strChecked3 = jsArray2JsString(rows, 'ltype');
				Ext.Ajax.request({
							url : './appGroupManager.ered?reqCode=vncUpload',
							success : function(response) {
								var resultArray = Ext.util.JSON.decode(response.responseText);
								var t = eval('('+response.responseText+')');
								if(!t.success){
									Ext.Msg.show({title:'提示',msg: '承载服务器上架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								
								}else{
									Ext.Msg.show({title:'提示',msg: '承载服务器上架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								
								}		
								store.reload();
							},
							params : {
								strChecked1 : strChecked1,
								strChecked2 : strChecked2,
								strChecked3 : strChecked3
							}
						});
			}

			/**
			 * 刷新会话监控表格
			 */
			function refreshSessionTable() {
				store.load({
							params : {
								start : 0,
								limit : bbar.pageSize
							}
						});
			}
		});