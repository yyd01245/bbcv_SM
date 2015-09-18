var area_id;
var area_text;
Ext.onReady(function() {	
	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;
	
	
			var queryValue="";
			var qForm = new Ext.form.FormPanel({
					region : 'north',
					title : '<span>查询条件<span>',
					collapsible : true,
					border : true,
					labelWidth : 100, // 标签宽度
					// frame : true, //是否渲染表单面板背景色
					labelAlign : 'right', // 标签对齐方式
					buttonAlign : 'center',
					height : 70,
					items : [{
						layout : 'column',
						border : false,
						items : [

                              {
								bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
								columnWidth : .20,
								layout : 'form',
								labelWidth : 80, // 标签宽度
								defaultType : 'textfield',
								border : false,
								items : [{
											fieldLabel : '信令网关名称',
											name : 'advname',
											xtype : 'textfield',
											anchor : '100%',
											listeners:{
												specialkey:function(field,e){
													if(e.getKey()== Ext.EventObject.ENTER)
														queryBalanceInfo(qForm.getForm());
												}
											}
										}
								]
								},
								   {
									bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
									columnWidth : .20,
									layout : 'form',
									labelWidth : 80, // 标签宽度
									defaultType : 'textfield',
									border : false,
									items : [{
												fieldLabel : 'IP',
												name : 'advip',
												xtype : 'textfield',
												anchor : '100%',
												listeners:{
													specialkey:function(field,e){
														if(e.getKey()== Ext.EventObject.ENTER)
															queryBalanceInfo(qForm.getForm());
													}
												}
											}
									]
									},
									   {
										bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
										columnWidth : .20,
										layout : 'form',
										labelWidth : 80, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
													fieldLabel : '端口',
													name : 'advport',
													xtype : 'textfield',
													anchor : '100%',
													listeners:{
														specialkey:function(field,e){
															if(e.getKey()== Ext.EventObject.ENTER)
																queryBalanceInfo(qForm.getForm());
														}
													}
												}
										]
										},
								
								{
									columnWidth : .20,
									layout : 'form',
									labelWidth : 80, // 标签宽度
									defaultType : 'textfield',
									border : false,
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
			function statusRender(value){
				if(value == 1){
				
				//	document.getElementById('runstate1').value="--";
					return "<span style= 'color:red;font-weight:bold;'>不可用</span>";
				}					
				else if(value == 0){
					
					return "<span style= 'color:green;font-weight:bold;'>可用</span>";
				}
				else{
					return value;
				}
			}
			
			
		
			
			function runstateRender(value){
				if(value == 2)
					return "<span style= 'color:red;font-weight:bold;'>异常</span>";
				else if(value == 1){
					return "<span style= 'color:green;font-weight:bold;'>正常</span>";
				}else if(value == 3){
					return "<span style= 'color:grey;font-weight:bold;'>离线</span>";
				}
				else
					return value;
			}

			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});

			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum,sm,{
				header : '信令网关名称',
				dataIndex : 'advname'
			},{
				header : '对应IP',
				dataIndex : 'advip'
			},{
				header : '对应端口',
				dataIndex : 'advport'
				
			},{
				id : "runstate1",
				header : '当前状态',
				dataIndex : 'runstate',
				sortable : true,
				renderer:runstateRender
			
				
			},{
				header : '是否可用',
				dataIndex : 'state',
				sortable : true,
				renderer:statusRender
				
			},{
				header : '负载量',
				dataIndex : 'loadnum',
				sortable : true,
				renderer:statusRender
				
			},{
				header : '备注',
				dataIndex : 'remark'
				
			}
			]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
					proxy : new Ext.data.HttpProxy({
								url : './SignalingGatewayDistribution.ered?reqCode=queryAds'
							}),
					// 数据读取器
					reader : new Ext.data.JsonReader({
								totalProperty : 'TOTALCOUNT', // 记录总数
								root : 'ROOT' // Json中的列表数据根节点
							}, [{
										name : 'id'
									},{
										name : 'advname'
									},{
										name : 'advip'
									},{
										name : 'advport'
									}
									,{
										name : 'state'
									},{
										name : 'remark'
									},{
										name : 'loadnum'
									}
									,{
										name : 'runstate'
									}
									])
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
						title : '<span style="font-weight:bold;">信令网关信息</span>',
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
								singleForm.getForm().reset();
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
						
						}, '-', {
							text : '设置为可用',
							iconCls : 'edit1Icon',
							handler : function() {
								CanUseCodeItems();
							}
						
						}
						
						],
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
	
		
		var singleForm = new Ext.form.FormPanel({
				labelAlign : 'right', // 标签对齐方式
				bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
				buttonAlign : 'center',
				defaultType : 'textfield',
				id : 'singleForm',
				name : 'singleForm',
				items : [{
					fieldLabel : '信令网关名称', // 标签
					name : 'advname', // name:后台根据此name属性取值
					allowBlank : false, // 是否允许为空
					anchor : '100%', // 宽度百分比
					//maxLength:32,
					//maxLengthText : '信令网关名称的长度不能超过32'
				},{
					fieldLabel : '对应IP',
					name : 'advip',
					allowBlank : false,
					anchor : '100%',
					regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,  
					regexText:'必须是合适的IP地址',					
					listeners:{
						blur:function(obj){
								obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}			
				},
//				comboxWithTree,
				{
					fieldLabel : '对应端口',
					name : 'advport',
					anchor : '100%',
					xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					regex : /^([1-9]\d{0,4})$/,
					regexText : '端口格式不正确',
					//emptyText :'（1-100）此处填写的是百分比',
					allowBlank : false,
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
				height : 250,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>新增信令网关</span>',
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

			// 布局
			var viewport = new Ext.Viewport({
					layout : 'border',
					items : [qForm,grid]
			});
			function singleFormSave() {
		
				if (singleWindow.getComponent('singleForm').form.isValid()) {
						singleWindow.getComponent('singleForm').form.submit({
						url : './adsManager.ered?reqCode=saveAds',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							refreshSessionTable();
							Ext.Msg.confirm('请确认', '信令网关添加成功,您是否继续添加新信令网关?',
									function(btn, text) {
										if (btn == 'yes') {
											singleWindow.getComponent('singleForm').form
													.reset();
										} else {
											singleWindow.hide();
										}
									});
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.Msg.show({title:'提示',msg:'该信令网关已存在或重名，请核对信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			
//							singleWindow.hide();
//							singleWindow.getComponent('singleForm').form.reset();
						}
					});
					
				}
			}
			
			
			// 查询表格数据
			function queryBalanceInfo(pForm) {
				var params = pForm.getValues();
//				var s = eval(params);
//				queryValue = s["area_id"];
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
			/** *****************修改代码对照*********************** */
	var editCodeWindow, editCodeFormPanel;
	editCodeFormPanel = new Ext.form.FormPanel({
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						defaultType : 'textfield',
						id : 'editCodeFormPanel',
						name : 'editCodeFormPanel',
						items : [{
							fieldLabel : '信令网关编号', // 标签
							name : 'id', // name:后台根据此name属性取值
							allowBlank : false, // 是否允许为空
							hidden:true,
							hideLabel:true,
							anchor : '100%'// 宽度百分比
							
						},
						         
						         
{
	fieldLabel : '信令网关名称', // 标签
	name : 'advname', // name:后台根据此name属性取值
    allowBlank : false, // 是否允许为空
	anchor : '100%', // 宽度百分比
	maxLength:32,
	maxLengthText : '信令网关长度不能超过32'
},{
	fieldLabel : '对应IP',
	name : 'advip',
	anchor : '100%',
	allowBlank : false,
	regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
	regexText:'必须是合适的IP地址',
	listeners:{
		blur:function(obj){
				obj.setValue(Ext.util.Format.trim(obj.getValue()));
		}
	}			
},
//comboxWithTree,
{
	fieldLabel : '对应端口',
	name : 'advport',
	anchor : '100%',
	xtype : 'numberfield', // 设置为数字输入框类型
	allowDecimals : false, // 是否允许输入小数
	allowNegative : false, // 是否允许输入负数
	regex : /^([1-9]\d{0,4})$/,
	regexText : '端口格式不正确',
	allowBlank : false,
		listeners:{
		blur:function(obj){
				obj.setValue(Ext.util.Format.trim(obj.getValue()));
		}
	}	
}
]
						});

	editCodeWindow = new Ext.Window({
				layout : 'fit',
				width : 400,
				height : 250,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>修改信令网关</span>',
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
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的信令网关!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
		
	}
	/**
	 * 修改cums
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './adsManager.ered?reqCode=updateAds',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'信令网关信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'信令网关配置重复或重名，请核对信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
		var checked = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('id') + '<br>';
			}
			checked+=rows[i].get('id')+",";
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的网络区域中包含如下系统内置的只读网络区域</b><br>' + fields
							+ '<font color=red>只读网络区域不能删除!</font>!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的信令网关!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var id = jsArray2JsString(rows,'id');
		//alert("strChecked="+strChecked);
		Ext.Msg.confirm('请确认', '你真的要删除此条信息吗?', function(btn, text) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : './adsManager.ered?reqCode=deleteAds',
					success : function(response) {
							Ext.Msg.show({title:'提示',msg:'信令网关删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							store.reload();
					},
					failure : function(response) {
						Ext.Msg.show({title:'提示',msg:'信令网关删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					},
					params : {
						strChecked : checked
					}
				});
			}
		});

					
	}
	
	
	/**
	 * 设置可用
	 */
	function CanUseCodeItems() {
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		
		var fields = '';
		var checked = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('id') + '<br>';
			}
			checked+=rows[i].get('id')+",";
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的网络区域中包含如下系统内置的只读网络区域</b><br>' + fields
							+ '<font color=red>只读网络区域不能删除!</font>!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中信令网关!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var id = jsArray2JsString(rows,'id');
		//alert("strChecked="+strChecked);
		Ext.Msg.confirm('请确认', '确定吗?', function(btn, text) {
			if (btn == 'yes') {
				
				Ext.Msg.show({title:'提示',msg:'成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
				store.reload();
//				Ext.Ajax.request({
//					url : './adsManager.ered?reqCode=deleteAds',
//					success : function(response) {
//							Ext.Msg.show({title:'提示',msg:'信令网关删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//							store.reload();
//					},
//					failure : function(response) {
//						Ext.Msg.show({title:'提示',msg:'信令网关删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//					},
//					params : {
//						strChecked : checked
//					}
//				});
			}
		});

					
	}
	
	/**********************************S 全量同步缓存开始 S*******************************************/

	queryBalanceInfo(qForm.getForm());

});
//获取登陆者所属区域
var getArea = function(){
	Ext.Ajax.request({
	    url: '../index.ered?reqCode=getAreaByUser',
	    success: function(response) {
	    var groupJson1 = eval('('+response.responseText+')');
	    var totall = groupJson1.TOTALCOUNT;
	    var userData = groupJson1.ROOT;
	    for(var i=0;i<totall;i++){
	    	area_id = userData[i].deptid;
	    	area_text = userData[i].deptname;
//	    	alert(area_text);
	    }
	    }	
	})	
}
	