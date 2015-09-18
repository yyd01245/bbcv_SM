/**
 * QAM厂商管理
 * 
 * Faustine
 * 
 * Since 2013-12-13
 */
Ext.onReady(function(){
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
	var qForm = new Ext.form.FormPanel({
		title : '查询条件',
		region : 'north',
		collapsible : true,
		border : true,
		labelWidth : 100,
		height : 60 ,
		buttonAlign : 'center',
		labelAlign : 'right',
		items:[{
			layout : 'column',
			border : false,
			items:[{
				columnWidth : .3,
				border : false,
				layout : 'form',
				bodyStyle : 'padding:5 5 0',
				items:[new Ext.form.ComboBox({
				fieldLabel : '设备厂商',
				hiddenName : 'device_firm',
				store : DEVICEFIRMStore,
				displayField : 'text',
				valueField : 'text'	,
				triggerAction : 'all',
				mode : 'local',
				forceSelection : true,
				typeAhead : true,
				editable : false,
				emptyText : '请选择..',
				anchor : '100%',
				listeners:{
					specialkey:function(field,e){
						if(e.getKey()== Ext.EventObject.ENTER)
							queryData(qForm.form) ;
					}
				}
			})]
			},{
				columnWidth : .6,
				border : false,
				layout : 'form',
				buttonAlign : 'right',
				buttons:[{
					text :'查询',
					iconCls : 'previewIcon',
					handler : function(){
						queryData(qForm.form) ;
					}
				},{
					text : '重置',
					iconCls : 'tbar_synchronizeIcon',
					handler : function(){
						qForm.form.reset() ;
					}
				}]
			}]
		}
		
		]
	});
	
	var rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([rownum,sm,{
		header : '设备厂商',
		dataIndex : 'device_firm'
	},{
		header : '厂商名称',
		dataIndex : 'firm_name'
	},{
		header :'rtsp端口',
		dataIndex : 'rtsp_port'
	},{
		header : '设备型号',
		dataIndex : 'device_type'
	},{
		header : '硬件版本',
		dataIndex : 'hardware_version'
	},{
		header : '软件版本',
		dataIndex : 'software_version'
	},{
		header : '端口步长',
		dataIndex : 'port_step'
	},{
		header : '服务步长',
		dataIndex : 'service_step'
	},{
		header : 'PID计算规则',
		dataIndex : 'pid_rule'
	},{
		header : 'R6报文厂商',
		dataIndex : 'r6_firm'
	},{
		header : '切台方式',
		dataIndex : 'switch_type',
		renderer : SWITCHTYPERender
	},{
		header : '频点类型',
		dataIndex : 'rf_type'
	},{
		header : '状态',
		dataIndex : 'status',
		sortable : true ,
		renderer:USERSTATUSRender
	},{
		header : '备注',
		dataIndex : 'remark',
		hidden : true
	}]);
	
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url :'./qamDyAction.ered?reqCode=queryQamDevice',
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'TOTALCOUNT',
			root : 'ROOT'
		},[{
			name : 'conf_id' 
		},{
			name : 'firm_name'
		},{
			name : 'rtsp_port'
		},{
			name : 'device_firm'
		},{
			name : 'device_type'
		},{
			name : 'hardware_version'
		},{
			name : 'software_version'
		},{
			name : 'status'
		},{
			name : 'status_time'
		},{
			name : 'port_step'
		},{
			name : 'service_step'
		},{
			name : 'pid_rule'
		},{
			name : 'r6_firm'
		},{
			name : 'switch_type'
		},{
			name : 'rf_type'
		},{
			name : 'remark'
		}])
	});
	
	store.on('beforeload',function(){
		this.baseParams = qForm.form.getValues() ;
	});
	
	/*每页显示的条数*/
	var pageSize = new Ext.form.ComboBox({
		name : 'pagesize',
		mode : 'local',
		triggerAction : 'all',
		store : new Ext.data.ArrayStore({
			fields : ['value','text'],
			data : [[10 , '10条/页'] , [20 , '20条/页'] , [50 , '50条/页'] , [100 , '100条/页'] , [250 , '250条/页'] , [500 , '500条/页']]
		}),
		valueField : 'value',
		displayField : 'text',
		value : '20',
		editable : false,
		width : 85
	});
	
	// 改变每页显示条数reload数据
	pageSize.on("select", function(comboBox) {
		bbar.pageSize = parseInt(comboBox.getValue());
		number = parseInt(comboBox.getValue());
		store.reload({
			params : {
				start : 0,
				limit : bbar.pageSize
			}
		});
	});
	var number = parseInt(pageSize.getValue());
	// 分页工具栏
	var bbar = new Ext.PagingToolbar({
		pageSize : number,
		store : store,
		displayInfo : true,
		displayMsg : '显示{0}条到{1}条,共{2}条',
		plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		emptyMsg : "没有符合条件的记录",
		items : ['-', '&nbsp;&nbsp;', pageSize]
	});

	var grid = new Ext.grid.GridPanel({
			region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
			// collapsible : true,
			title : '<span style="font-weight:bold;">IPQAM厂商信息</span>',
			height : 300,
			autoScroll : true,
			frame : true,
			store : store, // 数据存储
			stripeRows : true, // 斑马线
			cm : cm, // 列模型
			sm : sm,
			tbar : [{
				text : '新增',
				iconCls : 'page_addIcon',
				handler : function() {
					singleWindow.show();
					singleForm.form.reset();
				}
			}, '-', {
				text : '修改',
				iconCls : 'page_edit_1Icon',
				handler : function() {
					ininEditCodeWindow();
				}
			}],
			bbar : bbar,// 分页工具栏
			viewConfig : {
				// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
				forceFit : true
			},
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			}
	});
	grid.addListener('rowdblclick', ininEditCodeWindow);
	
	/************************************* S 数据新增 S  ***************************************/
	var singleForm = new Ext.form.FormPanel({
			labelAlign : 'right', // 标签对齐方式
			bodyStyle : 'padding:5 10 0', // 表单元素和表单面板的边距
			buttonAlign : 'center',
			defaultType : 'textfield',
			id : 'singleForm',
			name : 'singleForm',
			items : [{
				fieldLabel : '厂商名称',
				name : 'firm_name',
				defaultType : 'textfield',
				maxLength :100,
				maxLengthText : '厂商名称的长度不能超过100',
				allowBlank : false,
				anchor : '100%',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : 'RTSP端口',
				name : 'rtsp_port',
				allowBlank : false,
				defaultType : 'textfield',
				anchor : '100%',
				regex : /^[1-9]\d{0,4}$/,
				regexText : 'RTSP端口是必须为数值类型，长度不能超过5',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},new Ext.form.ComboBox({
				fieldLabel : '设备厂商',
				hiddenName : 'device_firm',
				store : DEVICEFIRMStore,
				displayField : 'text',
				valueField : 'text'	,
				triggerAction : 'all',
				mode : 'local',
				forceSelection : true,
				typeAhead : true,
				allowBlank : false,
				editable : false,
				emptyText : '请选择..',
				anchor : '100%'
			}),{
				fieldLabel : '设备型号',
				name : 'device_type',
				defaultType : 'textfield',
				allowBlank : false,
				anchor : '100%',
				maxLength :80,
				maxLengthText : '设备型号的长度不能超过80',
				allowBlank : false,
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '硬件版本',
				name : 'hardware_version',
				defaultType : 'textfield',
				anchor : '100%',
				maxLength :200,
				maxLengthText : '硬件版本的长度不能超过80',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '软件版本',
				name : 'software_version',
				defaultType : 'textfield',
				anchor : '100%',
				maxLength :200,
				maxLengthText : '软件版本的长度不能超过200',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '端口步长',
				name : 'port_step',
				defaultType : 'textfield',
				anchor : '100%',
				allowBlank : false,
				regex : /^[1-9]\d{0,8}$/,
				regexText : '端口步长是10位以内的数值',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '服务步长',
				name : 'service_step',
				anchor : '100%',
				allowBlank : false,
				regex : /^[1-9]\d{0,8}$/,
				regexText : '服务步长是10位以内的数值',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
				
			},new Ext.form.ComboBox({
				fieldLabel : 'PID计算规则',
				hiddenName : 'pid_rule',
				store : PIDRULEStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : 'R6报文厂商',
				hiddenName : 'r6_firm',
				store : R6FIRMStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : '切台方式',
				hiddenName : 'switch_type',
				store : SWITCHTYPEStore,
				valueField : 'value',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : '频点类型',
				hiddenName : 'rf_type',
				store : RFTYPEStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
			}),{
				fieldLabel : '备注',
				name : 'remark',
				xtype : 'textarea',
				height : 20,
				anchor : '100%',
				maxLength : 200,
				maxLengthText : '备注的长度不能超过200',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			}] 
	});
	
	var singleWindow = new Ext.Window({
		layout : 'fit',
		width : 400,
		height : 420,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span>新增IPQAM厂商信息</span>',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [singleForm], // 嵌入的表单面板
		buttons : [{
				text : '保存',
				iconCls : 'acceptIcon',
				handler : function() {
					singleFormSave();
				}
			},{ // 窗口底部按钮配置
				text : '重置', // 按钮文本
				iconCls : 'tbar_synchronizeIcon', // 按钮图标
				handler : function() { // 按钮响应函数
					singleForm.form.reset();
				}
			}]
	});
	
	function singleFormSave() {
		if (singleWindow.getComponent('singleForm').form.isValid()) {
				singleWindow.getComponent('singleForm').form.submit({
				url : './qamDyAction.ered?reqCode=saveQAMDevice',
				waitTitle : '提示',
				method : 'POST',
				waitMsg : '正在处理数据,请稍候...',
				success : function(form, action) {
					refreshSessionTable();
						Ext.Msg.confirm('请确认', 'IPQAM厂商信息添加成功,您是否继续添加新IPQAM厂商信息?',
							function(btn, text) {
								if (btn == 'yes') {
									singleForm.form.reset();
								} else {
									singleWindow.hide();
								}
							});
				},
				failure : function(form, action) {
					Ext.Msg.show({title:'提示',msg:'IPQAM厂商信息名称重复或者该厂商已存在，添加失败！',buttons : Ext.Msg.OK,icon:Ext.Msg.ERROR});
				}
			});
			
		} 
	}
	
			
	/************************************* E 数据新增 E  ***************************************/
	
	/************************************* S 数据修改 S  ***************************************/
	var editCodeWindow, editCodeFormPanel;

	editCodeFormPanel = new Ext.form.FormPanel({
			labelAlign : 'right', // 标签对齐方式
			bodyStyle : 'padding:10 10 0', // 表单元素和表单面板的边距
			buttonAlign : 'center',
			defaultType : 'textfield',
			id : 'editCodeFormPanel',
			name : 'editCodeFormPanel',
			items : [{
				fieldLabel : '编号',
				name : 'conf_id',
				anchor : '100%',
				xtype : 'hidden'
			},{
				fieldLabel : '厂商名称',
				name : 'firm_name',
				defaultType : 'textfield',
				maxLength :100,
				maxLengthText : '厂商名称的长度不能超过100',
				allowBlank : false,
				anchor : '100%',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : 'RTSP端口',
				name : 'rtsp_port',
				defaultType : 'textfield',
				allowBlank : false,
				anchor : '100%',
				regex : /^[1-9]\d{0,4}$/,
				regexText : 'RTSP端口是必须为数值类型，长度不能超过5',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '设备型号',
				name : 'device_type',
				defaultType : 'textfield',
				allowBlank : false,
				anchor : '100%',
				maxLength :80,
				maxLengthText : '设备型号的长度不能超过80',
				allowBlank : false,
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '硬件版本',
				name : 'hardware_version',
				defaultType : 'textfield',
				anchor : '100%',
				maxLength :200,
				maxLengthText : '硬件版本的长度不能超过80',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '软件版本',
				name : 'software_version',
				defaultType : 'textfield',
				anchor : '100%',
				maxLength :200,
				maxLengthText : '软件版本的长度不能超过200',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '端口步长',
				name : 'port_step',
				allowBlank : false,
				defaultType : 'textfield',
				regex : /^[1-9]\d{0,8}$/,
				regexText : '端口步长是10位以内的数值',
				anchor : '100%',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			},{
				fieldLabel : '服务步长',
				name : 'service_step',
				anchor : '100%',
				regex : /^[1-9]\d{0,8}$/,
				allowBlank : false,
				regexText : '服务步长是10位以内的数值',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
				
			},new Ext.form.ComboBox({
				fieldLabel : 'PID计算规则',
				hiddenName : 'pid_rule',
				store : PIDRULEStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : 'R6报文厂商',
				hiddenName : 'r6_firm',
				store : R6FIRMStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : '切台方式',
				hiddenName : 'switch_type',
				store : SWITCHTYPEStore,
				valueField : 'value',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
				
			}),new Ext.form.ComboBox({
				fieldLabel : '频点类型',
				hiddenName : 'rf_type',
				store : RFTYPEStore,
				valueField : 'text',
				displayField : 'text',
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				editable : false,
				anchor : '100%'
			}),new Ext.form.ComboBox({
				name : 'status',
				hiddenName : 'status',
				store : USERSTATUSStore,
				mode : 'local',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				fieldLabel : '状态',
				emptyText : '请选择...',
				allowBlank : false,
				forceSelection : true,
				editable : false,
				typeAhead : true,
				anchor : '100%'
			}) ,{
				fieldLabel : '备注',
				name : 'remark',
				xtype : 'textarea',
				height : 20,
				anchor : '100%',
				maxLength : 200,
				maxLengthText : '备注的长度不能超过200',
				listeners:{
					blur:function(obj){
						obj.setValue(Ext.util.Format.trim(obj.getValue()));
					}
				}
			}] 
		});


	editCodeWindow = new Ext.Window({
			layout : 'fit',
			width : 400,
			height : 420,
			resizable : false,
			draggable : true,
			closeAction : 'hide',
			title : '<span>修改IPQAM厂商信息</span>',
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
						Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
		
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
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的NGOD频点信息!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		
		var  strChecked = record.data.info_id;
		var status = record.data.status;

		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
		
	}
	/**
	 * 修改ngod频点信息
	 */
	function updateCodeItem() {
		
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './qamDyAction.ered?reqCode=updateQAMDevice',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'IPQAM厂商信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'IPQAM厂商信息名称或者厂商重复，更新失败，请核对信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				});
	}
	/************************************* E 数据修改 E  ***************************************/
	
	/*页面布局*/
	var viewPort = new Ext.Viewport({
		layout : 'border' ,
		items : [qForm,grid]
	});
	
		
	// 查询表格数据
	function queryData(pForm) {
		var params = pForm.getValues();
		params.start = 0;
		params.limit = bbar.pageSize;
		store.load({
				params : params
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