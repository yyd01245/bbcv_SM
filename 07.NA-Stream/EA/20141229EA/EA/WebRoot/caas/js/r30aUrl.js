/**
 * 应用管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), sm, {
				header : 'API地址',
				dataIndex : 'api_ip'
			}, {
				header : '端口号',
				dataIndex : 'api_port'
			}, {
				header : 'API版本号',
				dataIndex : 'api_version'
			}, {
				header : '机器信息',
				dataIndex : 'pc_info'
			}, {
				header : '区域',
				dataIndex : 'area'
			}]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './urlManage.ered?reqCode=queryItems'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'api_ip'
								}, {
									name : 'api_port'
								},{
									name : 'api_version'
								}, {
									name : 'pc_info'
								}, {
									name : 'area'
								}])
			});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
				this.baseParams = {
					queryParam : Ext.getCmp('queryParam').getValue()
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
				title : '<span style="font-weight:bold;">R30A服务地址管理</span>',
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
				viewConfig : {
					// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
					forceFit : true
				},
				tbar : [{
							text : '新增',
							iconCls : 'page_addIcon',
							handler : function() {
								addInit();
							}
						}, '-', {
							text : '修改',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								editInit();
							}
						}, '-', {
							text : '删除',
							iconCls : 'page_delIcon',
							handler : function() {
								deleteCodeItems();
							}
						}, '->',
						new Ext.form.TextField({
									id : 'queryParam',
									name : 'queryParam',
									emptyText : '请输入关键字',
									enableKeyEvents : true,
									listeners : {
										specialkey : function(app_name, e) {
											if (e.getKey() == Ext.EventObject.ENTER) {
												queryCodeItem();
											}
										}
									},
									width : 130
								}), {
							text : '查询',
							iconCls : 'previewIcon',
							handler : function() {
								queryCodeItem();
							}
						}, '-', {
							text : '刷新',
							iconCls : 'arrow_refreshIcon',
							handler : function() {
								store.reload();
							}
						}],
				bbar : bbar
			});
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize
				}
			});

	grid.addListener('rowdblclick', editInit);
	grid.on('sortchange', function() {
				// grid.getSelectionModel().selectFirstRow();
			});

	bbar.on("change", function() {
				// grid.getSelectionModel().selectFirstRow();
			});
	/**
	 * 新增代码对照表
	 */
	var addRoot  = new Ext.tree.AsyncTreeNode({
			id : '00',
			text : '中国',
			expanded : true
		});
	var addDeptTree = new Ext.tree.TreePanel({
			loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : '../demo/treeDemo.ered?reqCode=queryAreas'
			}),
			root : addRoot ,
			autoScroll : true,
			animate : false,
			useArrows : false,
			border : false
	});
	// 监听下拉树的节点单击事件
	addDeptTree.on('click', function(node) {
				comboxWithTree.setValue(node.text);
				//Ext.getCmp("formPanel").findById('deptid').setValue(node.attributes.id);
				comboxWithTree.collapse();
			});
	 var comboxWithTree = new Ext.form.ComboBox({
		name : 'area_area',
		store : new Ext.data.SimpleStore({
					fields : [],
					data : [[]]
				}),
		editable : false,
		emptyText : '请选择...',
		fieldLabel : '区域',
		anchor : '100%',
		mode : 'local',
		triggerAction : 'all',
		maxHeight : 390,
		// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
		tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
		allowBlank : true,
		onSelect : Ext.emptyFn
	});
	// 监听下拉框的下拉展开事件
	comboxWithTree.on('expand', function() {
				// 将UI树挂到treeDiv容器
				addDeptTree.render('addDeptTreeDiv');
				// addDeptTree.root.expand(); //只是第一次下拉会加载数据
				addDeptTree.root.reload(); // 每次下拉都会加载数据

			});

	var formPanel = new Ext.form.FormPanel({
				id : 'codeForm',
				name : 'codeForm',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
							fieldLabel : 'API地址',
							name : 'api_ip',
							anchor : '100%',
							allowBlank : false
						},{
							fieldLabel : 'API端口号',
							name : 'api_port',
							anchor : '100%',
							allowBlank : false,
							xtype : 'numberfield'
							
						}, {
							fieldLabel : 'API版本号',
							name : 'api_version',
							anchor : '100%',
							allowBlank : true,
							emptyText : 'v0.3'
						},{
							fieldLabel : '机器信息',
							name : 'pc_info',
							anchor : '100%',
							allowBlank : true
						},comboxWithTree]
			});

	var formWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 250,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span>新增R30A服务地址</span>',
		// iconCls : 'page_addIcon',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
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
							Ext.Msg.show({title:'提示',
									msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							return;
						}
						var mode = Ext.getCmp('windowmode').getValue();
						if (mode == 'add') {
							saveCodeItem();
						}
						else
							updateCodeItem();
					}
				}, {
					text : '重置',
					id : 'btnReset',
					iconCls : 'tbar_synchronizeIcon',
					handler : function() {
						clearForm(formPanel.getForm());
					}
				},{
					text : '关闭',
					iconCls : 'deleteIcon',
					handler : function() {
						formWindow.hide();
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
	function addInit(){
		Ext.getCmp('btnReset').hide();
		clearForm(formPanel.getForm());
		formWindow.show();
		formWindow.setTitle('<span>新增R30A服务地址</span>');
		Ext.getCmp('windowmode').setValue('add');
		comboxWithTree.setDisabled(false);
		// Ext.getCmp('btnReset').show();
	}

	function saveCodeItem() {
				if (formWindow.getComponent('codeForm').form.isValid()) {
					formWindow.getComponent('codeForm').form.submit({
						url : './urlManage.ered?reqCode=saveItem',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							Ext.MessageBox.show({title:'提示',msg:'应用地址保存成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							store.reload();
							formWindow.hide();
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.show({title:'提示',msg:'应用地址保存失败:<br>' + msg,buttons:Ext.Msg.ERROR});
							formWindow.getComponent('codeForm').form.reset();
						}
					});
				} else {
					// 表单验证失败
				}
	};
	/**
	 * 修改应用
	 */
	function editInit() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.show({title:'提示',msg:'请先选择要修改的项目!' ,buttons:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		formPanel.getForm().loadRecord(record);
		formWindow.show();
		formWindow.setTitle('<span>修改R30A服务地址</span>');
		Ext.getCmp('windowmode').setValue('edit');
		Ext.getCmp('btnReset').hide();
	}
	function updateCodeItem() {		
		if (!formPanel.form.isValid()) {
			return;
		}
		formPanel.form.submit({
			url : './urlManage.ered?reqCode=updateItem',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(form, action) {
				formWindow.hide();
				store.reload();
				form.reset();
			},
			failure : function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.show({title:'提示',msg:'角色数据修改失败:<br>' + msg ,buttons:Ext.Msg.ERROR});
			
			}
		});
	}
	/**
	 * 删除代码对照
	 */
	function deleteCodeItems() {
		if (runMode == '0') {
			Ext.MessageBox.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!' ,buttons:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('app_link') + '->'
						+ rows[i].get('app_code') + '<br>';
			}
		}
		if (fields != '') {
			Ext.MessageBox.show({title:'提示',msg:'<b>您选中的项目中包含如下系统内置的只读项目</b><br>' + fields
							+ '<font color=red>只读项目不能删除!</font>' ,buttons:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.MessageBox.show({title:'提示',msg:'请先选中要删除的项目!' ,buttons:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows, 'app_id');
		Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
			if (btn == 'yes') {
				//	showWaitMsg();
				Ext.Ajax.request({
							url : './urlManage.ered?reqCode=deleteItems',
							success : function(response) {
								store.reload();
								//hiddenWaitMsg();
							},
							failure : function(response) {
								var resultArray = Ext.util.JSON
										.decode(response.responseText);
								Ext.MessageBox.show({title:'提示',msg:resultArray.msg ,buttons:Ext.Msg.ERROR});
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
						queryParam : Ext.getCmp('queryParam').getValue()
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