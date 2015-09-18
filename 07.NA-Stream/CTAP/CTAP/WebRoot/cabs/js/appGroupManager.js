/**
 * 应用组信息管理
 * 
 * @author Sun Qiang
 * @since 2012-05-14
 */
Ext.onReady(function() {
		Ext.Msg.minWidth = 300 ;
		Ext.Msg.maxWidth = 360 ;
	var rownumber = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	})
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([rownumber, sm, {
				header : '组编号',
				width : 100,
				hidden:true,
				dataIndex : 'group_id'
			}, {
				header : '组名称',
				width : 100,
				dataIndex : 'group_name'
			}, {
				header : '组类型',
				width : 100,
				dataIndex : 'encodetype',
				renderer:APPLICATIONNAMERender
			}, {
				header : '操作系统版本',
				dataIndex : 'operate_version'
			},{
				header : '组信息',
				width : 100,
				dataIndex : 'group_desc'
			}, {
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
							url : './appGroupManager.ered?reqCode=queryAppGroupManagerItems'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'group_id'
						        },{
									name : 'group_desc'
								}, {
									name : 'group_name'
								}, {
									name : 'status'
								},{
									name : 'encodetype'
								},{
									name : 'create_time'
								}, {
									name : 'update_time'
								},{
									name : 'operate_version'
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
				title : '<span style="font-weight:bold;">承载服务器分组配置</span>',
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
//						, {
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
	/**
	 * 新增代码对照表
	 */

	var formPanel = new Ext.form.FormPanel({
				id : 'codeForm',
				name : 'codeForm',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 100,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
					fieldLabel : '组名称',
					name : 'group_name',
					anchor : '100%',
					maxLength : 64,
					maxLengthText : '组名称的长度不能超过64',
					allowBlank : false,
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '操作系统版本',
					anchor : '100%',
					maxLength:64,
					maxLengthText : '操作系统版本信息长度不能超过64',
					name : 'operate_version',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '组信息',
					name : 'group_desc',
					maxLength : 256,
					maxLengthText : '组信息的长度不能超过256',
					anchor : '100%',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},new Ext.form.ComboBox({
							name : 'encodeType',
							hiddenName : 'encodeType',
							store : APPLICATIONNAMEStore,
							mode : 'local',
							triggerAction : 'all',
							valueField : 'value',
							displayField : 'text',
							//value : '1',
							fieldLabel : '组类型',
							emptyText : '请选择...',
							allowBlank : false,
							editable : false,
							forceSelection : true,
							typeAhead : true,
							anchor : '100%'
						})]
			});

	var codeWindow = new Ext.Window({
		layout : 'fit',
		width : 350,
		height : 200,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span>新增应用组</span>',
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
					Ext.Msg.show({title:'提示', msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				if (codeWindow.getComponent('codeForm').form.isValid()) {
					codeWindow.getComponent('codeForm').form.submit({
						url : './appGroupManager.ered?reqCode=saveAppGroupManagerItem',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							refreshCodeTable();
							Ext.Msg.confirm('请确认', '应用添加成功,您是否继续添加新应用组?',
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
							Ext.Msg.show({title:'提示',msg:'应用组重名，请重新命名！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
	var statusCombo_E = new Ext.form.ComboBox({
		name : 'status',
		hiddenName : 'status',
		store : STATUSStore,
		mode : 'local',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		//value : '1',
		fieldLabel : '状态',
		emptyText : '请选择...',
		allowBlank : false,
		forceSelection : true,
		editable : false,
		typeAhead : true,
		anchor : '100%'
	});

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
					fieldLabel : '组名称',
					name : 'group_name',
					anchor : '100%',
					allowBlank : false,
					maxLength : 64,
					maxLengthText : '组名称的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '操作系统版本',
					name : 'operate_version',
					anchor : '100%',
					maxLength : 64,
					maxLengthText : '操作心态版本信息的长度不能超过64',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},{
					fieldLabel : '组信息',
					name : 'group_desc',
					anchor : '100%',
					maxLength : 256,
					maxLengthText : '组信息的长度不能超过256',
					listeners:{
						blur:function(obj){
							obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}
				},new Ext.form.ComboBox({
							name : 'encodetype',
							hiddenName : 'encodetype',
							store : APPLICATIONNAMEStore,
							mode : 'local',
							triggerAction : 'all',
							valueField : 'value',
							displayField : 'text',
							//value : '1',
							fieldLabel : '组类型',
							emptyText : '请选择...',
							allowBlank : false,
							editable : false,
							forceSelection : true,
							typeAhead : true,
							readOnly:true,
							anchor : '100%'
						}),{
					fieldLabel : '组编号',
					name : 'group_id',
					anchor : '100%',
					hidden : true,
					hideLabel : true
				}]
			});

	editCodeWindow = new Ext.Window({
				layout : 'fit',
				width : 350,
				height : 200,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>修改应用</span>',
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
							Ext.Msg.show({title:'提示',
									msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							return;
						}
						updateCodeItem();
					}
				}]

			});
	refreshCodeTable();
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
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的应用组!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
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
					url : './appGroupManager.ered?reqCode=updateAppGroupManagerItem',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editCodeWindow.hide();
						store.reload();
						Ext.Msg.show({title:'提示',msg:'组信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'组信息更新失败，组名重名，请查阅信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				});
	}

	/**
	 * 删除代码对照
	 */
	function deleteCodeItems() {
		if (runMode == '0') {
			Ext.Msg.show({title:'提示', msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('group_id') + '->'
						+ rows[i].get('status') + '<br>';
			}
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示', msg:'<b>您选中的应用组中包含如下系统内置的只读应用组</b><br>' + fields
							+ '<font color=red>只读应用组不能删除!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的应用组!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'group_id');
		Ext.Msg.confirm('请确认', '你真的要删除应用组吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './appGroupManager.ered?reqCode=deleteAppGroupManagerItems',
									success : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										if(resultArray.success){
											Ext.Msg.show({title:'提示',msg:'应用组删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										}else{
											Ext.Msg.show({title:'提示',msg:'应用组删除失败，组下有承载或关联，请先删除对应关系！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
										}
										
										store.reload();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg: resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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