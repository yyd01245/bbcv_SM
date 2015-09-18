
Ext.onReady(function() {								
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
				header : '故障名称',
				dataIndex : 'hitch_name',
				sortable : true,
				width:230
			},{
				header : '故障等级',
				dataIndex : 'hitch_level',
				renderer :HITCHLEVELRender,
				sortable : true
			},{
				header : '修复允许天数',
				dataIndex : 'end_data'
			}]);		
			
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
		// 获取数据的方式
		proxy : new Ext.data.HttpProxy({
					url : './platmentMenage.ered?reqCode=queryHitch'
				}),
		// 数据读取器
		reader : new Ext.data.JsonReader({
				totalProperty : 'TOTALCOUNT', // 记录总数
				root : 'ROOT' // Json中的列表数据根节点
			}, [{
					name : 'hitch_id'
				},{	name : 'hitch_name'
				},{	name : 'hitch_level'
				},{	name : 'end_data'
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
			title : '<span>故障类别</span>',
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
					emptyText : '请输入关键字',
					enableKeyEvents : true,
					hidden :true
				}),{
					text : '查询',
					iconCls : 'previewIcon',
					handler : function() {
						queryData() ;
					}
				},{
					text : '新增',
					iconCls : 'previewIcon',
					handler : function() {
						firstFormPanel.getForm().reset();
						firstWindow.show();
					}
//				},'-',{
//				text : '修改',
//				iconCls : 'page_edit_1Icon',
//				handler : function() {
//					ininEditCodeWindow();
//				}
				},'-', {
				text : '删除',
				iconCls : 'page_delIcon',
				handler : function() {
					deleteCodeItems();
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
	/****************************S 故障类别添加开始 S***********************************/
	
	var firstFormPanel = new Ext.form.FormPanel({
		labelAlign : 'right', // 标签对齐方式
		bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
		buttonAlign : 'center',
		defaultType : 'textfield',
		autoScroll : true,
		id : 'firstFormPanel',
		name : 'firstFormPanel',
		items : [{
				fieldLabel : '故障名称', // 标签
				name : 'hitch_name', // name:后台根据此name属性取值
				allowBlank : false, // 是否允许为空
				anchor : '90%', // 宽度百分比
				regex : /^(\S+)$/,
				regexText : '故障名称不能有空格'
			},new Ext.form.ComboBox({
				name : 'hitch_level',
				hiddenName : 'hitch_level',
				store : HITCHLEVELStore,
				mode : 'local',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				//value : '1',
				fieldLabel : '故障等级',
				emptyText : '请选择...',
				allowBlank : false,
				editable : false,
				forceSelection : true,
				typeAhead : true,
				anchor : '90%'
			}),{
				fieldLabel : '修复允许天数',
				name : 'end_data',
				allowBlank : false,
				anchor : '90%',
				xtype : 'numberfield',
				allowDecimals :false,//是否允许输入小数
				allowNegative :false,//是否允许输入负数
				baseChars :'12345',//基本输入参数
				maxLength :1,
				maxLengthText : '修复允许天数不允许超过5个工作日',
				nanText :'输入的格式不正确',
				regex : /^(\S+)$/,
				regexText : '修复允许天数不能有空格'
			}]
	});

	var firstWindow = new Ext.Window({
			layout : 'fit',
			width : 450,
			height : 200,
			resizable : false,
			draggable : true,
			closeAction : 'hide',
			title : '<span>故障添加</span>',
			modal : true,
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			animateTarget : Ext.getBody(),
			constrain : true,
			items : [firstFormPanel],
			buttons : [{
				text : '保存',
				iconCls : 'acceptIcon',
				handler : function() {
					addHitch();
				}
			}, {
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					firstWindow.hide();
				}
			}]

	});
	function addHitch(){
		if (firstWindow.getComponent('firstFormPanel').form.isValid()) {
					firstWindow.getComponent('firstFormPanel').form.submit({
						url : './platmentMenage.ered?reqCode=saveHitch',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
								store.reload();
							Ext.Msg.confirm('请确认', '故障定义添加成功,您是否继续添加故障定义?',
									function(btn, text) {
										if (btn == 'yes') {
											firstWindow.getComponent('firstFormPanel').form
													.reset();
										} else {
											firstWindow.hide();
										}
									});
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.Msg.show({title:'警告',msg:'数据有误！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							firstWindow.hide();
						}
					});
		}
	}
	
	/****************************E 故障类别添加结束 E***********************************/
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
				fieldLabel : '云用户编号', // 标签
				name : 'cloud_user_id', // name:后台根据此name属性取值
				allowBlank : false, // 是否允许为空
				anchor : '90%',// 宽度百分比
				hideLabel: 'true', 
				hidden:'true'
				
			},{
				fieldLabel : '机顶盒号', // 标签
				name : 'term_id', // name:后台根据此name属性取值
				allowBlank : false, // 是否允许为空
				anchor : '90%', // 宽度百分比
				xtype:'hidden',
			},{
				fieldLabel : '网络区域编号',
				name : 'region_id',
				allowBlank : false,
				anchor : '90%',
				xtype : 'numberfield',
				allowDecimals :false,//是否允许输入小数
				allowNegative :false,//是否允许输入负数
				baseChars :'0123456789',//基本输入参数
				maxLength :64,
				maxLengthText : '网络区域编号的长度不能超过64',
				nanText :'输入的格式不正确',
				regex : /^(\S+)$/,
				regexText : '网络区域编号不能有空格',
				listeners:{
					change : function(field,newValue,oldValue){
						  if(newValue=='')
							  return;
			       	}
				}
			},{
				fieldLabel : '机顶盒厂商', // 标签
				name : 'term_model', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%', // 宽度百分比
				maxLength: 128,
				maxLengthText : '机顶盒厂商的长度不能超过128'
			},{
				fieldLabel : '验证码', // 标签
				name : 'auth_key', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%' ,// 宽度百分比
				maxLength : 256,
				maxLengthText : '验证码的长度不能超过256'
				
			},{
				fieldLabel : '流水号', // 标签
				name : 'auth_seq', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%', // 宽度百分比
				maxLength : 128,
				maxLengthText : '流水号的长度不能超过128'
			},{
				fieldLabel : '门户地址', // 标签
				name : 'index_url', // name:后台根据此name属性取值
				allowBlank : true, // 是否允许为空
				anchor : '90%', // 宽度百分比
				maxLength : 128,
				maxLengthText : '门户地址的长度不能超过128'
			}]
	});

	editCodeWindow = new Ext.Window({
			layout : 'fit',
			width : 450,
			height : 320,
			resizable : false,
			draggable : true,
			closeAction : 'hide',
			title : '<span>修改用户</span>',
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
				text : '保存',
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
//			Ext.Msg.alert('提示', '请先选中要修改的用户');
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的用户！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
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
					url : './userManage.ered?reqCode=updateUserItem',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'用户数据更新成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					}
					,
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.Msg.show({title:'提示',msg:'用户数据更新失败，该区域下已存在相同的机顶盒号！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});
						editCodeWindow.hide();
						store.reload();
					}
				});
	}
	
	/*******************************E 修改部分 E***************************************/
	
	/*******************************S 删除部分 S***************************************/
	/**
	 * 删除代码对照
	 */
	function deleteCodeItems() {
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('hitch_id') + '->' + '<br>';
			}
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的用户中包含如下系统内置的只读用户</b><br>' + fields
							+ '<font color=red>只读用户不能删除!</font>',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的故障类别!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'hitch_id');
		Ext.Msg.confirm('请确认', '你真的要删除故障类别吗?', function(btn, text) {
				if (btn == 'yes') {
					Ext.Ajax.request({
							url : './platmentMenage.ered?reqCode=deleteHitch',
							success : function(response) {
									store.reload();
									Ext.Msg.show({title:'提示',msg:'故障类别删除成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
							}
							,
//									failure : function(response) {
//										var resultArray = Ext.util.JSON
//												.decode(response.responseText);
//										Ext.Msg.alert('提示', resultArray.msg);
//									},
							params : {
								strChecked : strChecked
							}
						});
				}
		});
	}
		
	
	/*******************************E 删除部分 E***************************************/
	
});	