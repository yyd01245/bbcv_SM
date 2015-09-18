/**
 * 应用组信息管理
 * 
 * @author Sun Qiang
 * @since 2012-05-14
 */
//var vstatus;
var store;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
//	Ext.Ajax.request({				
//		  	   	url : './batchUpdate.ered?reqCode=queryServerStauts',
//		    		success: function(response) {
//						vstatus=eval("("+response.responseText+")");
//				}
//		  	 })
	// 准备本地数据
	var serverStatusStore = new Ext.data.SimpleStore({
				fields : ['text', 'value'],
				data : [['正常运行', '1'], ['进程异常', '2'], ['需要升级', '4']]
			});
	//设备队列
	var typeStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './batchUpdate.ered?reqCode=queryDeviceList'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'dev_type'
						        },{
									name : 'dev_name'
								}])
			});
	//组队列
	var groupStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './batchUpdate.ered?reqCode=queryGroupList&encodeType='+2
						}),
				reader : new Ext.data.JsonReader({
//							totalProperty : 'TOTALCOUNT',
//							root : 'ROOT'
						}, [ 	
						     	{
									name : 'group_id'
						        },{
									name : 'group_name'
								}])
			});
	//组队列
	var groupAllStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './batchUpdate.ered?reqCode=queryGroupList'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'group_id'
						        },{
									name : 'group_name'
								}])
			});
		var queryValue="";
	var qForm = new Ext.form.FormPanel({
region : 'north',
title : '<span>查询条件<span>',
collapsible : true,
border : true,
labelWidth : 80, // 标签宽度
// frame : true, //是否渲染表单面板背景色
labelAlign : 'right', // 标签对齐方式
bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
buttonAlign : 'center',
height : 100,
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
									name : 'dev_type',
									hiddenName : 'dev_type',
									fieldLabel : '设备类型',
									editable : false ,
									triggerAction : 'all',
									store : typeStore,
									displayField : 'dev_name',
									valueField:'dev_type',
									mode : 'remote',
									forceSelection : true,
									typeAhead : true,
									resizable : true,
									anchor : '95%',
									listeners:{
										'select':function(){
											queryBalanceInfo(qForm.getForm());
										}
									}
								})]
				},{
					columnWidth : .3,
					layout : 'form',
					labelWidth : 60, // 标签宽度
					defaultType : 'textfield',
					border : false,
					items : [new Ext.form.ComboBox({
									name : 'group_id',
									hiddenName : 'group_id',
									fieldLabel : '组名',
									editable : false ,
									triggerAction : 'all',
									store : groupAllStore,
									displayField : 'group_name',
									valueField:'group_id',
									mode : 'remote',
									forceSelection : true,
									typeAhead : true,
									resizable : true,
									anchor : '95%',
									listeners:{
										'select':function(){
											queryBalanceInfo(qForm.getForm());
										}
									}
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
											var status_now = s["status"];
											if(status_now==1){
												Ext.getCmp('stop').enable();
												Ext.getCmp('start').disable();
												Ext.getCmp('upload').disable();
											}else if(status_now==2){
												Ext.getCmp('start').enable();
												Ext.getCmp('stop').disable();
												Ext.getCmp('upload').disable();
											}else if(status_now==4){
												Ext.getCmp('upload').enable();
												Ext.getCmp('start').disable();
												Ext.getCmp('stop').enable();
											}else{
												Ext.getCmp('start').disable();
												Ext.getCmp('stop').disable();
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
							var params = qForm.getForm().getValues();
							var s = eval(params);
							var status_now = s["status"];
							if(status_now==1){
								Ext.getCmp('stop').enable();
								Ext.getCmp('start').disable();
								Ext.getCmp('upload').disable();
							}else if(status_now==2){
								Ext.getCmp('start').enable();
								Ext.getCmp('stop').disable();
								Ext.getCmp('upload').disable();
							}else if(status_now==4){
								Ext.getCmp('upload').enable();
								Ext.getCmp('start').disable();
								Ext.getCmp('stop').enable();
							}else{
								Ext.getCmp('start').disable();
								Ext.getCmp('stop').disable();
								Ext.getCmp('upload').disable();
							}
						}
					}, {
						text : '重置',
						iconCls : 'tbar_synchronizeIcon',
						handler : function() {
							qForm.getForm().reset();
							queryBalanceInfo(qForm.getForm());
							Ext.getCmp('start').disable();
							Ext.getCmp('stop').disable();
							Ext.getCmp('upload').disable();
						}
					}]
			});
					
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), sm,
	        {
				header : '承载编号',
//				width : 100,
				hidden:true,
				dataIndex : 'vncm_id'
			}, {
				header : '终端类型',
//				width : 200,
				dataIndex : 'vncm_type',
				renderer:ZIPTYPERender
			}, {
				header : '组编号',
//				width : 200,
				dataIndex : 'group_id',
				editor : new Ext.grid.GridEditor(new Ext.form.ComboBox({
							store : groupStore,
							mode : 'remote',
							triggerAction : 'all',
							valueField : 'group_id',
							displayField : 'group_name',
							allowBlank : false,
							forceSelection : true,
							typeAhead : true
						}))
			}, {
				header : '设备类型',
//				width : 200,
				dataIndex : 'dev_type',
				editor : new Ext.grid.GridEditor(new Ext.form.ComboBox({
							store : typeStore,
							mode : 'remote',
							triggerAction : 'all',
							valueField : 'dev_type',
							displayField : 'dev_name',
							allowBlank : false,
							forceSelection : true,
							typeAhead : true
						}))
			}, {
				header : '组名称',
//				width : 200,
				dataIndex : 'group_name'
			}, {
				header : '设备名称',
//				width : 200,
				dataIndex : 'dev_name'
			}, {
				header : 'IP',
				width : 200,
				dataIndex : 'vncm_ip'
			}, {
				header : 'PORT',
//				width : 200,
				dataIndex : 'vncm_port'
			}, {
				header : '当前版本',
//				width : 200,
				dataIndex : 'version'
			}, {
				header : '运行状态',
//				width : 200,
				dataIndex : 'status',
				renderer:addImage
			}]);
	
//		Ext.Ajax.request({				
//		  	   	url : './batchUpdate.ered?reqCode=queryServerStauts',
//		    		success: function(response) {
//						vstatus=eval("("+response.responseText+")");
//				}
//		  	 })
//		  	   		alert(vstatus);
	function addImage(value){
//		if(vstatus==3){
			if(value=='1'){
				return '<img src="server_normal.png">' 
			}else if(value=='2'){
				return '<img src="server_error.png">' 
			}else if(value=='3'){
				return '<img src="server_error.png">' 
			}else if(value=='4'){
				return '<img src="server_normal_upgrade.png">' 
			}else if(value=='5'){
				return '<img src="server_error.png">' 
			}else if(value=='6'){
				return '<img src="server_normal_upgrade.png">' 
			}else if(value=='7'){
				return '<img src="server_offline.png">' 
			}
//		}else if(vstatus==4){
//			if(value=='1'){
//				return '<img src="server_upgrade.png">' 
//			}else if(value=='2'){
//				return '<img src="server_error.png">' 
//			}else if(value=='3'){
//				return '<img src="server_error.png">' 
//			}else if(value=='4'){
//				return '<img src="server_upgrade.png">' 
//			}else if(value=='5'){
//				return '<img src="server_error.png">' 
//			}else if(value=='6'){
//				return '<img src="server_normal_upgrade.png">' 
//			}else if(value=='7'){
//				return '<img src="server_offline.png">' 
//			}
//		}else{
//			if(value=='1'){
//				return '<img src="server_normal.png">' 
//			}else if(value=='2'){
//				return '<img src="server_error.png">' 
//			}else if(value=='3'){
//				return '<img src="server_error.png">' 
//			}else if(value=='4'){
//				return '<img src="server_upgrade.png">' 
//			}else if(value=='5'){
//				return '<img src="server_error.png">' 
//			}else if(value=='6'){
//				return '<img src="server_normal_upgrade.png">' 
//			}else if(value=='7'){
//				return '<img src="server_offline.png">' 
//			}
//		}
		
	}

	store = new Ext.data.Store({
				pruneModifiedRecords : true,
				proxy : new Ext.data.HttpProxy({
							url : './batchUpdate.ered?reqCode=queryServerWithCondition'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [ 	
						     	{
									name : 'vncm_id'
						        },{
									name : 'group_id'
						        }, {
									name : 'dev_type'
								}, {
									name : 'vncm_ip'
								},{
									name : 'vncm_name'
								},{
									name : 'vncm_port'
								},{
									name : 'status'
								},{
									name : 'vncm_type'
								},{
									name : 'cpu_info'
								},{
									name : 'mem_info'
								},{
									name : 'vncm_total'
								},{
									name : 'vncm_online'
								},{
									name : 'version'
								},{
									name : 'vncm_message'
								},{
									name : 'group_name'
								},{
									name : 'dev_name'
								}])
			});

	store.on('beforeload', function() {
			this.baseParams = qForm.getForm().getValues();
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

	var grid = new Ext.grid.EditorGridPanel({
				title : '<span style="font-weight:bold;">服务器批量管理</span>',
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
				clicksToEdit : 1,
				tbar : [
						{
							text : '批量启动',
							iconCls : 'acceptIcon',
							id:'start',
							disabled:true,
							handler : function() {
								batchStart();
							}
						}, '-', {
							text : '批量停止',
							iconCls : 'deleteIcon',
							id:'stop',
							disabled:true,
							handler : function() {
								batchStop();
							}
						}, '-',{
							text : '批量升级',
							iconCls : 'uploadIcon',
							id:'upload',
							disabled:true,
							handler : function() {
								batchUpdate();
							}
//						}, '-',{
//							text : '批量回退',
//							iconCls : 'acceptIcon',
//							handler : function() {
//								batchRollBack();
//							}
//						},  '-', {
//							text : '批量下线',
//							iconCls : 'deleteIcon',
//							handler : function() {
//								batchOffline();
//							}
						},  '->', {
							text:'保存',
							iconCls:'database_refreshIcon',
							handler:function(){
								saveRow();
							}
//						},{
//							text : '刷新',
//							iconCls : 'arrow_refreshIcon',
//							handler : function() {
//								refreshCodeTable();
//								addImage(store);
//								
//							}
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
//	store.load({
//				params : {
//					start : 0,
//					limit : bbar.pageSize
//				}
//			});
			
	grid.on('cellclick', function(grid, rowIndex, event) {
		groupStore.removeAll();
		var record = grid.getSelectionModel().getSelected();
		var group_type =record.get('vncm_type');
		Ext.Ajax.request({
			url : './batchUpdate.ered?reqCode=queryGroupList&encodeType='+group_type,
			success: function(response) {
				var groupJson = eval('('+response.responseText+')');
				var chinaRoot = groupJson.ROOT;
				groupStore.loadData(chinaRoot,true);
				if(groupStore.getCount()==0){
					Ext.Msg.show({title:'提示',msg:'承载没有可分配组，请先到组信息管理中添加组！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				}
			}	
		})
	});
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
		var s = eval(params);
		queryValue = s["status"];
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
			Ext.Msg.show({title:'提示',msg: '请先选中要启动的主机!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'vncm_id');
		Ext.Msg.confirm('请确认', '你确定要启动吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchStartItems',
									success : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										if(resultArray.success){
											Ext.Msg.show({title:'提示',msg:'启动事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
											store.reload();
										}else{
											Ext.Msg.show({title:'提示',msg:'启动事件添加失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
											store.reload();
										}
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
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
	 * 批量回滚
	 */
	function batchRollBack(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg: '请先选中要回滚的主机!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'vncm_id');
		Ext.Msg.confirm('请确认', '你确定要回滚吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchRollBlockItems',
									success : function(response) {
										Ext.Msg.show({title:'提示',msg: '操作成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
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
	 * 批量停止
	 */
	function batchStop(){
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要停止的主机!');
			
			Ext.Msg.show({title:'提示',msg: '请先选中要启动的主机!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'vncm_id');
		Ext.Msg.confirm('请确认', '你确定要停止吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchStopItems',
									success : function(response) {
									var resultArray = Ext.util.JSON.decode(response.responseText);
									if(resultArray.success){
										Ext.Msg.show({title:'提示',msg:'停止事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
									}else{
										Ext.Msg.show({title:'提示',msg:'停止事件添加失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
										store.reload();
									}
										
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg:  resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
			Ext.Msg.show({title:'提示',msg:'请先选中要升级的主机!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'vncm_id');
		Ext.Msg.confirm('请确认', '你确定要升级吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchUpdateItems',
									success : function(response) {
										var resultArray = Ext.util.JSON.decode(response.responseText);
										if(resultArray.success){
											Ext.Msg.show({title:'提示',msg:'升级事件添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										}
										store.reload();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
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
	 * 批量下线
	 */
	function batchOffline(){
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg: '请先选中要下线的主机!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'vncm_id');
		Ext.Msg.confirm('请确认', '你确定要下线吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : './batchUpdate.ered?reqCode=batchOfflineItems',
									success : function(response) {
										Ext.Msg.show({title:'提示',msg: '操作成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
										//hiddenWaitMsg();
									},
									failure : function(response) {
										var resultArray = Ext.util.JSON
												.decode(response.responseText);
					
										Ext.Msg.show({title:'提示',msg: resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
				title : '<span>修改参数</span>',
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
							Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
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
			Ext.Msg.show({title:'提示',msg: '请先选中要修改的承载!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
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
						Ext.Msg.show({title:'提示',msg: '修改成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						var msg = action.result.msg;						
						Ext.Msg.show({title:'提示',msg: '代码对照表保存失败:<br>' + msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
//		Ext.Ajax.request({				
//		  	   	url : './batchUpdate.ered?reqCode=queryServerStauts',
//		    		success: function(response) {
//						vstatus=eval("("+response.responseText+")");
//					}
//		})
		store.load({
					params : {
						start : 0,
						limit : bbar.pageSize
					}
				});
		
	}
		function saveRow() {
				var m = store.modified.slice(0); // 获取修改过的record数组对象
				if (Ext.isEmpty(m)) {
					Ext.Msg.show({title:'提示',msg:'没有数据需要保存!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
//					Ext.MessageBox.alert('提示', '没有数据需要保存!');
					return;
				}
//				if (!validateEditGrid(m, 'dev_type')) {
//					Ext.Msg.alert('提示', '承载名称字段数据校验不合法,请重新输入!', function() {
//								grid.startEditing(0, 2);
//							});
//					return;
//				}
				var jsonArray = [];
				// 将record数组对象转换为简单Json数组对象
				Ext.each(m, function(item) {
							jsonArray.push(item.data);
						});
				// 提交到后台处理
				Ext.Ajax.request({
							url : 'batchUpdate.ered?reqCode=saveDirtyDatas',
							success : function(response) { // 回调函数有1个参数
								var resultArray = Ext.util.JSON.decode(response.responseText);
//								Ext.Msg.alert('提示', resultArray.success);
								store.reload();
								if(resultArray.success){
									Ext.Msg.show({title:'提示',msg:'应用分组成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								}else{
									Ext.Msg.show({title:'提示',msg:'应用分组失败，类型不匹配！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								}
							},
							failure : function(response) {							
								Ext.Msg.show({title:'提示',msg: '数据保存失败!',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								},
							params : {
								// 系列化为Json资料格式传入后台处理
								dirtydata : Ext.encode(jsonArray)
							}
						});
			}
	
//	}	
//})
});
//	var taskRunner;
//	if(taskRunner!=null){
//		taskRunner.stopAll();
//	}
//	var task = {
//			prod.nebula.run : function() {
//				window.setTimeout("reloads();",5000);
//			},
//		interval :10000
//	};
//
//	taskRunner = new Ext.util.TaskRunner();
//	taskRunner.start(task);
//	function reloads(){
//		queryBalanceInfo(qForm.getForm());
//		var params = qForm.getForm().getValues();
//		var s = eval(params);
//		var status_now = s["status"];
//		if(status_now==1){
//			Ext.getCmp('stop').enable();
//			Ext.getCmp('start').disable();
//			Ext.getCmp('upload').disable();
//			}else if(status_now==2){
//				Ext.getCmp('start').enable();
//				Ext.getCmp('stop').disable();
//				Ext.getCmp('upload').disable();
//			}else if(status_now==4){
//				Ext.getCmp('upload').enable();
//				Ext.getCmp('start').disable();
//				Ext.getCmp('stop').disable();
//			}else{
//				Ext.getCmp('start').disable();
//				Ext.getCmp('stop').disable();
//				Ext.getCmp('upload').disable();
//			}
//		}
//	function queryBalanceInfo(pForm) {
//		var params = pForm.getValues();
//		params.start = 0;
//		params.limit = bbar.pageSize;
//		store.load({
//					params : params
//				});
//	}