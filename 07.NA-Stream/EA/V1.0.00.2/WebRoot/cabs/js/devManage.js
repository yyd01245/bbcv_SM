/**
 * 设备信息管理
 * 
 * @author Sun Qiang
 * @since 2012-05-14
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), sm, {
				header : '设备编号',
				width : 100,
				hidden:true,
				dataIndex : 'dev_id'
			}, {
				header : '设备名',
				width : 100,
				dataIndex : 'dev_name'
			},
			{
				header : '设备类型',
				width : 100,
				dataIndex : 'dev_type'
//				renderer :VNCDEVICETYPERender
			},
			{
				header : '设备厂商',
				width : 100,
				dataIndex : 'dev_maker'
			}, {
				header : '设备型号',
				dataIndex :'dev_version'
			},{
				header : '创建日期',
				width : 200,
				dataIndex : 'create_time'
			}, {
				header : '更新日期',
				width : 200,
				dataIndex : 'update_time'
			}]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './deviceManage.ered?reqCode=querydevManagerItems'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'dev_id'
						        },{
									name : 'dev_type'
								}, {
									name : 'dev_version'
								}, {
									name : 'dev_maker'
								},{
									name : 'status'
								},{
									name : 'dev_name'
								},{
									name : 'create_time'
								}, {
									name : 'update_time'
								}])
			});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
				this.baseParams = {
					queryParam : Ext.getCmp('status_id').getValue()
				};
			});

	var pagesize_combo = new Ext.form.ComboBox({
				name : 'pagesize',
				hiddenName : 'pagesize',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['value', 'text'],
							data : [[10, '10条/页'], [20, '20条/页'],
									[50, '50条/页'], [100, '100条/页'],
									[250, '250条/页'], [500, '500条/页']]
						}),
				valueField : 'value',
				displayField : 'text',
				value : '50',
				editable : false,
				width : 85
			});
	var number = parseInt(pagesize_combo.getValue());
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

	var bbar = new Ext.PagingToolbar({
				pageSize : number,
				store : store,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo]
			})

	var grid = new Ext.grid.GridPanel({
				title : '<span style="font-weight:bold;" >承载服务器型号配置</span>',
				iconCls : 'application_view_listIcon',
				height : 510,
				store : store,
				region : 'center',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
				cm : cm,
				sm : sm,
				tbar : [{
							text : '新增',
							iconCls : 'page_addIcon',
							handler : function() {
								codeWindow.getComponent('codeForm').form.reset();
								codeWindow.show();
							}
						}, '-', {
							text : '修改',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								ininEditCodeWindow();
							}
						}, '-', {
							text : '删除',
							iconCls : 'page_delIcon',
							handler : function() {
								deleteCodeItems();
							}
						}, '->',
						new Ext.form.ComboBox({
							id : 'status_id',
							hiddenName : 'status',
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
										fields : ['value', 'text'],
										data : [[1, '有效'], [0, '无效']]
									}),
							displayField : 'text',
							valueField : 'value',
							mode : 'local',
							listWidth : 120, // 下拉列表的宽度,默认为下拉选择框的宽度
							forceSelection : true,
							typeAhead : true,
							emptyText : '应用状态',
							// editable : false,
							resizable : true,
							hidden:true,
							width : 100,
							listeners:{
								specialkey:function(field,e){
									if(e.getKey()== Ext.EventKey.ENTER)
										queryCodeItem();
								}
							}
						})
//						,{
//							text : '查询',
//							iconCls : 'previewIcon',
//							handler : function() {
//								queryCodeItem();
//							}
//						}, '-', {
//							text : '刷新',
//							iconCls : 'arrow_refreshIcon',
//							handler : function() {
//								store.reload();
//							}
//						}
						],
				bbar : bbar
			});
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize
				}
			});

	grid.addListener('rowdblclick', ininEditCodeWindow);
	grid.on('sortchange', function() {
				// grid.getSelectionModel().selectFirstRow();
			});

	bbar.on("change", function() {
				// grid.getSelectionModel().selectFirstRow();
			});
	refreshCodeTable();
	/**
	 * 新增代码对照表
	 */

	var formPanel = new Ext.form.FormPanel({
				id : 'codeForm',
				name : 'codeForm',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
					fieldLabel : '设备名称',
					name : 'dev_name',
					anchor : '100%',
					allowBlank : false,
					maxLength : 255,
					maxLengthText : '设备名称的长度不能超过255',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备类型',
					name : 'dev_type',
					anchor : '100%',
					allowBlank : false,
					regex : /^([0-9]\d*)$/,
					regexText : '设备类型输入的代码为数值类型',
					maxLength : 11,
					maxLengthText : '设备类型的长度不能超过11',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备厂商',
					name : 'dev_maker',
					anchor : '100%',
					maxLength : 64,
					maxLengthText : '设备厂商的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备型号',
					name : 'dev_version',
					anchor : '100%',
//					allowBlank : false,
					maxLength : 64,
					maxLengthText : '设备型号的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				}
//				,new Ext.form.ComboBox({
//							name : 'dev_type',
//							hiddenName : 'dev_type',
//							store : VNCDEVICETYPEStore,
//							mode : 'local',
//							triggerAction : 'all',
//							valueField : 'value',
//							displayField : 'text',
//							//value : '1',
//							fieldLabel : '设备类型',
//							emptyText : '请选择...',
//							allowBlank : false,
//							editable : false,
//							forceSelection : true,
//							typeAhead : true,
//							anchor : '100%'
//						}}
				]
			});

	var codeWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 200,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span>新增设备</span>',
		// iconCls : 'page_addIcon',
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
		items : [formPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				if (runMode == '0') {
					Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',
						buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				if (codeWindow.getComponent('codeForm').form.isValid()) {
					codeWindow.getComponent('codeForm').form.submit({
						url : './deviceManage.ered?reqCode=saveDevManagerItem',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							queryCodeItem();
							Ext.Msg.confirm('请确认', '应用添加成功,您是否继续添加新设备?',
									function(btn, text) {
										if (btn == 'yes') {
											codeWindow.getComponent('codeForm').form
													.reset();
										} else {
											codeWindow.hide();
										}
									});
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.Msg.show({title:'提示',msg:'设备重名或设备类型重复，请查看！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						}
					});
				} else {
					// 表单验证失败
				}
			}
		}, {
			text : '重置',
			iconCls : 'tbar_synchronizeIcon',
			handler : function() {
				codeWindow.getComponent('codeForm').form.reset();
			}
		}]
	});

	/** *****************修改代码对照*********************** */

	var editCodeWindow, editCodeFormPanel;
	editCodeFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				frame : false,
				bodyStyle : 'padding:5 5 0',
				id : 'editCodeFormPanel',
				name : 'editCodeFormPanel',
				items : [{
					fieldLabel : '设备名称',
					name : 'dev_name',
					anchor : '100%',
					allowBlank : false,
					maxLength : 255,
					maxLengthText : '设备名称的长度不能超过255',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备类型',
					name : 'dev_type',
					anchor : '100%',
					allowBlank : false,
					regex : /^([0-9]\d*)$/,
					regexText : '设备类型输入的代码为数值类型',
					maxLength : 11,
					maxLengthText : '设备类型的长度不能超过11',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备厂商',
					name : 'dev_maker',
					anchor : '100%',
					maxLength : 64,
					maxLengthText : '设备厂商的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '设备型号',
					name : 'dev_version',
					anchor : '100%',
					maxLength : 64,
					maxLengthText : '设备型号的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				}
//				,new Ext.form.ComboBox({
//							name : 'dev_type',
//							hiddenName : 'dev_type',
//							store : VNCDEVICETYPEStore,
//							mode : 'local',
//							triggerAction : 'all',
//							valueField : 'value',
//							displayField : 'text',
//							//value : '1',
//							fieldLabel : '设备类型',
//							emptyText : '请选择...',
//							allowBlank : false,
//							editable : false,
//							forceSelection : true,
//							typeAhead : true,
//							readOnly:true,
//							anchor : '100%'
//						})
				,{
					fieldLabel : '设备编号',
					name : 'dev_id',
					anchor : '100%',
					hidden : true,
					hideLabel : true
				}]
			});

	editCodeWindow = new Ext.Window({
				layout : 'fit',
				width : 300,
				height : 200,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>修改设备</span>',
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
				items : [editCodeFormPanel],
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						if (runMode == '0') {
							Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!'
								,buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							return;
						}
						updateCodeItem();
					}
				}]

			});
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

	/**
	 * 初始化代码修改出口
	 */
	function ininEditCodeWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.show({title:'提示', msg:'请先选中要修改的设备!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
//		record = grid.getSelectionModel().getSelected();
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg: '您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
	}

	/**
	 * 修改应用
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './deviceManage.ered?reqCode=updateDevManagerItem',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editCodeWindow.hide();
						store.reload();
						Ext.Msg.show({title:'提示',msg:'设备更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'设备更新失败，设备名重名或设备类型重复，请查阅信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				});
	}

	/**
	 * 删除代码对照
	 */
	function deleteCodeItems() {
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('dev_id')  + '<br>';
			}
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的设备中包含如下系统内置的只读设备</b><br>' + fields
							+ '<font color=red>只读设备不能删除!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的设备!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'dev_id');
		Ext.Msg.confirm('请确认', '你真的要删除设备吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './deviceManage.ered?reqCode=deleteDevManagerItems',
									success : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										if(resultArray.success){
											Ext.Msg.show({title:'提示',msg:'设备删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										}else{
											Ext.Msg.show({title:'提示',msg:'设备删除失败，设备下有承载，请先删除对应关系！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
										}
										
										store.reload();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg:resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}

	/**
	 * 根据条件查询应用
	 */
	function queryCodeItem() {
		store.load({
					params : {
						start : 0,
						limit : bbar.pageSize,
						status : Ext.getCmp('status_id').getValue()
					}
				});
	}

	/**
	 * 刷新应用
	 */
	function refreshCodeTable() {
		store.load({
					params : {
						start : 0,
						limit : bbar.pageSize
					}
				});
	}
});