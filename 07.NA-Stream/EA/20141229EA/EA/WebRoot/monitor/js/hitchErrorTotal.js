/**
 * 错误码统计管理
 * Since 2013-08-13
 */
Ext.onReady(function() {		
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	function queryData(){
		store.load({
			params : {
				start : 0,
				limit : bbar.pageSize,
				queryParam : Ext.getCmp('queryParam').getValue()
			}
		});
	}
	
	
		// 定义自动当前页行号
		var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
				});

		// 定义列模型
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel([rownum,sm,{
			header : '故障编号',
			dataIndex : 'hitch_id',
			sortable : true,
			hidden :true
		},{
			header : '机顶盒号',
			dataIndex : 'stb_id',
			sortable : true,
			width:230
		},{
			header : '错误码',
			dataIndex : 'error_code',
			sortable : true
		},{
			header : '串号',
			dataIndex : 'serialno',
			width:230
		},{
			header : '故障创建时间',
			dataIndex : 'hitch_create_time',
			width:145,
			sortable : true
		},{
			header : '故障更新时间',
			dataIndex : 'hitch_update_time',
			width:145,
			sortable : true
//			},{
//				header : '是否发送',
//				dataIndex : 'send',
//				renderer : ISSENDRender
		},{
			header : '状态',
			dataIndex : 'status',
			sortable : true,
			renderer : HITCHSTATUSRender
		}]);		
			
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
		// 获取数据的方式
		proxy : new Ext.data.HttpProxy({
					url : './platmentMenage.ered?reqCode=queryTotalHitch12'
				}),
		// 数据读取器
		reader : new Ext.data.JsonReader({
				totalProperty : 'TOTALCOUNT', // 记录总数
				root : 'ROOT' // Json中的列表数据根节点
			}, [{
					name : 'hitch_id'
				},{	name : 'stb_id'
				},{	name : 'hitch_create_time'
				},{	name : 'error_code'
				},{	name : 'serialno'
				},{	name : 'send'
				},{	name : 'status'
				},{	name : 'hitch_update_time'
				}])
	});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
						this.baseParams = {
							queryParam : Ext.getCmp('queryParam').getValue()
						};
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
			
	var grid = new Ext.grid.EditorGridPanel({
			region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
			// collapsible : true,
			// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
			title : '<span style="font-weight:bold;">错误码处理</span>',
			autoScroll : true,
			frame : true,
			store : store, // 数据存储
			stripeRows : true, // 斑马线
//						plugins : expander, // 行级展开
			cm : cm, // 列模型
			sm:sm,
			tbar : [new Ext.form.TextField({
					id : 'queryParam',
					name : 'queryParam',
					emptyText : '机顶盒|错误码',
					enableKeyEvents : true,
					hidden :false
				}),{
					text : '查询',
					iconCls : 'previewIcon',
					handler : function() {
						queryData() ;
					}
				},'-',{
				text : '错误处理',
				iconCls : 'page_edit_1Icon',
				handler : function() {
					ininEditCodeWindow();
				}
			},'-',{
				text : '批量处理',
				iconCls : 'page_addIcon',
				handler : function(){
					handlerError() ;
				}
			}],
			bbar : bbar,// 分页工具栏
			viewConfig : {
				// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
//							forceFit : true,
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
		});
	
	// 布局
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});
	grid.addListener('rowdblclick', ininEditCodeWindow);
			grid.on('rowdblclick', function(grid, rowIndex, event) {
				ininEditCodeWindow();
			});
	
	/*******************************S 修改部分代码 S***************************************/
	var editCodeWindow, editCodeFormPanel;
	editCodeFormPanel = new Ext.form.FormPanel({
		labelAlign : 'right', // 标签对齐方式
		bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
		buttonAlign : 'center',
		defaultType : 'textfield',
		autoScroll : true,
		id : 'editCodeFormPanel',
		name : 'editCodeFormPanel',
		items : [{
				fieldLabel : '故障标识', // 标签
				name : 'hitch_id', // name:后台根据此name属性取值
				allowBlank : false, // 是否允许为空
				anchor : '90%',// 宽度百分比
				hideLabel: 'true', 
				hidden:'true'
			},{
				fieldLabel : '机顶盒号',
				name : 'stb_id',
				allowBlank : false,
				anchor : '90%',
				disabled:true
			},{
				fieldLabel : '错误码',
				name : 'error_code',
				allowBlank : false,
				anchor : '90%',
				disabled:true
			},{
				fieldLabel : '故障创建时间', // 标签
				name : 'hitch_create_time', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%' ,// 宽度百分比
				disabled:true
			},new Ext.form.ComboBox({
				hiddenName : 'status',
				fieldLabel : '状态', // 标签
				name : 'status', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%', // 宽度百分比
				triggerAction : 'all',
				mode : 'local',
				valueField : 'value',
				displayField : 'text',
				store:HITCHSTATUSStore
			})]
	});

	editCodeWindow = new Ext.Window({
			layout : 'fit',
			width : 350,
			height : 200,
			resizable : false,
			draggable : true,
			closeAction : 'hide',
			title : '<span>故障处理</span>',
			modal : true,
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			animateTarget : Ext.getBody(),
			constrain : true,
			items : [editCodeFormPanel],
			buttons : [{
				text : '处理',
				iconCls : 'acceptIcon',
				handler : function() {
					if (runMode == '0') {
						Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
						return;
					}
					updateCodeItem();
				}
			}, {
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					editCodeWindow.hide();
				}
			}]

	});
	
	/**
	 * 初始化代码修改出口
	 */
	function ininEditCodeWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要处理的故障！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
			return;
		}
		record = grid.getSelectionModel().getSelected();
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
			return;
		}
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
	}
	
	/**
	 * 修改用户
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './platmentMenage.ered?reqCode=updateHitch12',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'故障处理成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					}
					,
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.Msg.show({title:'提示',msg:'故障处理失败',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});
						editCodeWindow.hide();
						store.reload();
					}
				});
	}
	
	/*******************************E 修改部分 E***************************************/
	/*****************************S 批量处理 S***********************************************/
	function handlerError(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要处理的错误!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'hitch_id');
		Ext.Msg.confirm('请确认', '你真的要批量处理错误吗?', function(btn, text) {
				if (btn == 'yes') {
					//	showWaitMsg();
					Ext.Ajax.request({
							url : './platmentMenage.ered?reqCode=updateBatHitch12',
							success : function(response) {
									store.reload();
									Ext.Msg.show({title:'提示',msg:'批量处理成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
							},
							params : {
								strChecked : strChecked
							}
						});
				}
		});
	}
	/*****************************E 批量处理 E***********************************************/
	
});	