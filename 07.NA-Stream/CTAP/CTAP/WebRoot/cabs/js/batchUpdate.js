/**
 * 应用组信息管理
 * 
 * @author Sun Qiang
 * @since 2012-05-14
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	// 准备本地数据
	var serverStatusStore = new Ext.data.SimpleStore({
				fields : ['text', 'value'],
				data : [['正常运行', '1'], ['进程异常', '2'],['主机宕机', '3'],['升级中', '4'], ['需要升级', '6']]
			});
	var typeStore = new Ext.data.SimpleStore({
				fields : ['text', 'value'],
				data : [['标清', '1'], ['高清', '2']]
			});
	
	var qForm = new Ext.form.FormPanel({
						region : 'north',
						title : '<span class="commoncss">查询条件<span>',
						collapsible : true,
						border : true,
						labelWidth : 80, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 80,
						items : [{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : .3,
												layout : 'form',
												labelWidth : 60, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [new Ext.form.ComboBox({
																name : 'term_type',
																hiddenName : 'term_type',
																fieldLabel : '视频规格',
																editable : false ,
																triggerAction : 'all',
																store : typeStore,
																displayField : 'text',
																valueField:'value',
																value:'1',
																mode : 'local',
																forceSelection : true,
																typeAhead : true,
																resizable : true,
																anchor : '95%'
															})]
											},{
												columnWidth : .3,
												layout : 'form',
												labelWidth : 60, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [new Ext.form.ComboBox({
																name : 'status',
																hiddenName : 'status',
																fieldLabel : '主机状态',
																editable : false ,
																triggerAction : 'all',
																store : serverStatusStore,
																displayField : 'text',
																valueField:'value',
																value:'1',
																mode : 'local',
																forceSelection : true,
																typeAhead : true,
																resizable : true,
																anchor : '95%'
															})]
											},{
												columnWidth : .4 ,
												layout : 'form',
												defaultType : 'textfield',
												border : false ,
												buttonAlign : 'right',
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
													}
												}]
											}]
								}]
					});
					
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), sm,
	        {
				header : '终端编号',
				width : 100,
				dataIndex : 'term_record_id'
			}, {
				header : 'IP',
				width : 200,
				dataIndex : 'term_ip'
			}, {
				header : '机房位置',
				width : 200,
				dataIndex : 'term_desc'
			}, {
				header : '当前版本',
				width : 200,
				dataIndex : 'current_version'
			}, {
				header : '运行状态',
				width : 200,
				dataIndex : 'status',
				renderer:addImage
			}]);
	
	function addImage(value){
		if(value=='1'){
			return '<img src="server_normal.png">' 
		}else if(value=='2'){
			return '<img src="server_error.png">' 
		}else if(value=='3'){
			return '<img src="server_error.png">' 
		}else if(value=='4'){
			return '<img src="server_upgrade.png">' 
		}else if(value=='5'){
			return '<img src="server_error.png">' 
		}else if(value=='6'){
			return '<img src="server_normal_upgrade.png">' 
		}else if(value=='7'){
			return '<img src="server_offline.png">' 
		}
	}

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './batchUpdate.ered?reqCode=queryServerWithCondition'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'term_record_id'
						        },{
									name : 'term_ip'
						        }, {
									name : 'term_desc'
								}, {
									name : 'current_version'
								},{
									name : 'status'
								}])
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
				title : '<span class="commoncss">服务器批量管理</span>',
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
				tbar : [
						{
							text : '批量启动',
							iconCls : 'acceptIcon',
							handler : function() {
								batchStart();
							}
						}, '-', {
							text : '批量停止',
							iconCls : 'acceptIcon',
							handler : function() {
								batchStop();
							}
						}, '-',{
							text : '批量升级',
							iconCls : 'uploadIcon',
							handler : function() {
								batchUpdate();
							}
						}, '-',{
							text : '批量回退',
							iconCls : 'acceptIcon',
							handler : function() {
								batchRollBack();
							}
						},  '-', {
							text : '批量下线',
							iconCls : 'deleteIcon',
							handler : function() {
								batchOffline();
							}
						}],
				bbar : bbar
			});
	// 翻页排序时带上查询条件
	/*store.on('beforeload', function() {
				this.baseParams = {
					//queryParam : Ext.getCmp('queryParam').getValue(),
						status : Ext.getCmp('status_id').getValue()
				};
			});*/
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
	 * 布局
	 */
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [qForm,grid]
			});
			
	// 查询表格数据
	function queryBalanceInfo(pForm) {
		var params = pForm.getValues();
		params.start = 0;
		params.limit = bbar.pageSize;
		store.load({
					params : params
				});
	}
	
	/**
	 * 批量启动
	 */
	function batchStart(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要启动的主机!');
			return;
		}
		var strChecked = jsArray2JsString(rows,'term_record_id');
		Ext.Msg.confirm('请确认', '你确定要启动吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchStartItems',
									success : function(response) {
										Ext.Msg.alert('提示', "操作成功!");
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.alert('提示', resultArray.msg);
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}
	/**
	 * 批量回滚
	 */
	function batchRollBack(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要回滚的主机!');
			return;
		}
		var strChecked = jsArray2JsString(rows,'term_record_id');
		Ext.Msg.confirm('请确认', '你确定要回滚吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchRollBlockItems',
									success : function(response) {
										Ext.Msg.alert('提示', "操作成功!");
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.alert('提示', resultArray.msg);
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}
	
	/**
	 * 批量停止
	 */
	function batchStop(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要停止的主机!');
			return;
		}
		var strChecked = jsArray2JsString(rows,'term_record_id');
		Ext.Msg.confirm('请确认', '你确定要停止吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchStopItems',
									success : function(response) {
										Ext.Msg.alert('提示', "操作成功!");
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.alert('提示', resultArray.msg);
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}
	/**
	 * 批量升级
	 */
	function batchUpdate(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要升级的主机!');
			return;
		}
		var strChecked = jsArray2JsString(rows,'term_record_id');
		Ext.Msg.confirm('请确认', '你确定要升级吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchUpdateItems',
									success : function(response) {
										Ext.Msg.alert('提示', "操作成功!");
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.alert('提示', resultArray.msg);
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}
	
	/**
	 * 批量下线
	 */
	function batchOffline(){
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要下线的主机!');
			return;
		}
		var strChecked = jsArray2JsString(rows,'term_record_id');
		Ext.Msg.confirm('请确认', '你确定要下线吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchOfflineItems',
									success : function(response) {
										Ext.Msg.alert('提示', "操作成功!");
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.alert('提示', resultArray.msg);
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
	}	
	
	/** *****************修改代码对照*********************** */
	var statusCombo_E = new Ext.form.ComboBox({
		id : "status_id",
		name : 'status',
		fieldLabel : '运行状态',
		hiddenName : 'status',
		store : serverStatusStore,
		mode : 'local',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		forceSelection : true,
		fieldClass : 'x-custom-field-disabled',
		readOnly : true,
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
				items : [statusCombo_E, 
				   {
					fieldLabel : 'IP',
					name : 'term_ip',
					anchor : '100%',
					fieldClass : 'x-custom-field-disabled',
					readOnly : true
				}, {
					fieldLabel : '机房位置',
					name : 'term_desc',
					anchor : '100%',
					allowBlank : false
				}, {
					fieldLabel : '当前版本',
					name : 'current_version',
					anchor : '100%',
					fieldClass : 'x-custom-field-disabled',
					readOnly : true
				},{
					fieldLabel : '终端编号',
					name : 'term_record_id',
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
				title : '<span class="commoncss">修改参数</span>',
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
							Ext.Msg.alert('提示',
									'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
							return;
						}
						updateCodeItem();
					}
				}]

			});
	
	/**
	 * 初始化代码修改出口
	 */
	function ininEditCodeWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.alert('提示', '请先选中要修改的项目');
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.alert('提示', '您选中的记录为系统内置的代码对照,不允许修改');
			return;
		}
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
	}

	/**
	 * 修改机柜位置
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './batchUpdate.ered?reqCode=updateLocation',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.MessageBox.alert("修改成功!");
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '代码对照表保存失败:<br>' + msg);
					}
				});
	}
	
	/**
	 * 根据条件查询应用
	 */
	/*function queryCodeItem() {
		store.load({
					params : {
						start : 0,
						limit : bbar.pageSize,
						status : Ext.getCmp('status_id').getValue()
					}
				});
	}*/

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