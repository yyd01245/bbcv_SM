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
//						         {
//								bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
//								columnWidth : .30,
//								layout : 'form',
//								labelWidth : 100, // 标签宽度
//								defaultType : 'textfield',
//								border : false,
//								items : [comboxWithTreee]
//							},
                              {
								bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
								columnWidth : .30,
								layout : 'form',
								labelWidth : 140, // 标签宽度
								defaultType : 'textfield',
								border : false,
								items : [{
											fieldLabel : '网络区域名称',
											name : 'strNetRegionName',
											xtype : 'textfield',
											anchor : '100%',
											listeners:{
												specialkey:function(field,e){
													if(e.getKey()== Ext.EventObject.ENTER)
														queryBalanceInfo(qForm.getForm());
												}
											}
										}]
								},{
									columnWidth : .30,
									layout : 'form',
									labelWidth : 140, // 标签宽度
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
			

			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});

			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum,sm,
			                                   {
				header : '网络区域编号',
				dataIndex : 'strnetregionnum'
			},
			{
				header : '网络区域名称',
				dataIndex : 'strnetregionname'
			},{
				header : '高清导航数量',
				dataIndex : 'inavgationstreamnum'
			},{
				header : '标清导航数量',
				dataIndex : 'sdinavgationstreamnum'
				
			},{
				header : '高清广告数量',
				dataIndex : 'iadvertisementstreamnum'
			},{
				header : '标清广告数量',
				dataIndex : 'sdiadvertisementstreamnum'
			},{
				header : '备注',
				dataIndex : 'strnetworkcomment'
			}]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
					proxy : new Ext.data.HttpProxy({
								url : './networkManager.ered?reqCode=queryNetworks'
							}),
					// 数据读取器
					reader : new Ext.data.JsonReader({
								totalProperty : 'TOTALCOUNT', // 记录总数
								root : 'ROOT' // Json中的列表数据根节点
							}, [{
										name : 'strnetregionnum'
									},{
										name : 'strnetregionname'
									},{
										name : 'inavgationstreamnum'
									},{
										name : 'sdinavgationstreamnum'
									},{
										name : 'iadvertisementstreamnum'
									},{
										name : 'sdiadvertisementstreamnum'
									},{
										name : 'strnetworkcomment'
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
						title : '<span style="font-weight:bold;">网络区域信息</span>',
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


			
		var singleForm = new Ext.form.FormPanel({
				labelAlign : 'right', // 标签对齐方式
				bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
				buttonAlign : 'center',
				defaultType : 'textfield',
				id : 'singleForm',
				name : 'singleForm',
				items : [{
					fieldLabel : '网络区域名称', // 标签
					name : 'strNetRegionName', // name:后台根据此name属性取值
                    allowBlank : false, // 是否允许为空
					anchor : '100%', // 宽度百分比
					maxLength:1000,
					maxLengthText : '网络区域标识的长度不能超过1000'
				},{
					fieldLabel : '高清导航数量',
					name : 'iNavgationStreamNum',
					allowBlank : false,
					anchor : '100%',
					value:0,
					readOnly:true,
					fieldClass : 'x-custom-field-disabled',
					xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					listeners:{
						blur:function(obj){
								obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}			
				},
//				comboxWithTree,
				{
					fieldLabel : '标清导航数量',
					name : 'sdiNavgationStreamNum',
					anchor : '100%',
					xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					value:0,
					readOnly:true,
					fieldClass : 'x-custom-field-disabled',
					allowBlank : false,
					listeners:{
						blur:function(obj){
								obj.setValue(Ext.util.Format.trim(obj.getValue()));
						}
					}	
				},{
					fieldLabel : '高清广告数量', // 标签
					name : 'iAdvertisementStreamNum', // name:后台根据此name属性取值
                    allowBlank : false, // 是否允许为空
					anchor : '100%', // 宽度百分比
					xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					value:0,
					readOnly:true,
					fieldClass : 'x-custom-field-disabled'
				},
				{
					fieldLabel : '标清广告数量', // 标签
					name : 'sdiAdvertisementStreamNum', // name:后台根据此name属性取值
                    allowBlank : false, // 是否允许为空
        			xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					anchor : '100%', // 宽度百分比
					value:0,
					readOnly:true,
					fieldClass : 'x-custom-field-disabled'

				},
				{
					fieldLabel : '备注',
					name : 'strNetworkComment',
					xtype : 'textarea',
					height : 50, // 设置多行文本框的高度
//							emptyText : '默认初始值', // 设置默认初始值
					anchor : '100%',
					maxLength : 1000,
					maxLengthText : '备注的长度不能超过1000',
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
				height : 300,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>新增网络区域</span>',
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
						url : './networkManager.ered?reqCode=saveNetworks',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							refreshSessionTable();
							Ext.Msg.confirm('请确认', '网络区域添加成功,您是否继续添加新网络区域?',
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
							Ext.Msg.show({title:'提示',msg:'该区域已存在或重名，请核对信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			
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
							fieldLabel : '网络区域编号', // 标签
							name : 'strnetregionnum', // name:后台根据此name属性取值
							allowBlank : false, // 是否允许为空
							hidden:true,
							hideLabel:true,
							anchor : '100%'// 宽度百分比
							
						},
						         
						         
						{
							fieldLabel : '网络区域名称', // 标签
							name : 'strnetregionname', // name:后台根据此name属性取值
						//			allowBlank : false, // 是否允许为空
							anchor : '100%', // 宽度百分比
							maxLength:1000,
							readOnly:true,
							fieldClass : 'x-custom-field-disabled',
							maxLengthText : '网络区域标识的长度不能超过1000'
						},{
							fieldLabel : '高清导航数量',
							name : 'inavgationstreamnum',
							allowBlank : false,
							anchor : '100%',
							xtype : 'numberfield', // 设置为数字输入框类型
							allowDecimals : false, // 是否允许输入小数
							allowNegative : false, // 是否允许输入负数
							listeners:{
								blur:function(obj){
										obj.setValue(Ext.util.Format.trim(obj.getValue()));
								}
							}			
						},
						//comboxWithTree,
						{
							fieldLabel : '标清导航数量',
							name : 'sdinavgationstreamnum',
							anchor : '100%',
							allowBlank : false,
							xtype : 'numberfield', // 设置为数字输入框类型
							allowDecimals : false, // 是否允许输入小数
							allowNegative : false, // 是否允许输入负数
							allowBlank : false,
								listeners:{
								blur:function(obj){
										obj.setValue(Ext.util.Format.trim(obj.getValue()));
								}
							}	
						},{
							fieldLabel : '高清广告数量', // 标签
							name : 'iadvertisementstreamnum', // name:后台根据此name属性取值
							allowBlank : false,
							anchor : '100%',
							xtype : 'numberfield', // 设置为数字输入框类型
							allowDecimals : false, // 是否允许输入小数
							allowNegative : false, // 是否允许输入负数
						},
						{
							fieldLabel : '标清广告数量', // 标签
							name : 'sdiadvertisementstreamnum', // name:后台根据此name属性取值
							allowBlank : false,
							anchor : '100%',
							xtype : 'numberfield', // 设置为数字输入框类型
							allowDecimals : false, // 是否允许输入小数
							allowNegative : false, // 是否允许输入负数
						},
						{
							fieldLabel : '备注',
							name : 'strnetworkcomment',
							xtype : 'textarea',
							height : 50, // 设置多行文本框的高度
						//			emptyText : '默认初始值', // 设置默认初始值
							anchor : '100%',
							maxLength : 1000,
							maxLengthText : '备注的长度不能超过1000',
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
				height : 300,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>修改网络区域</span>',
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
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的网络区域!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		var  strChecked = record.data.strnetregionnum;
		
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
		
//		Ext.Ajax.request({
//			url :'./networkManager.ered?reqCode=queryResByNetwork&key='+strChecked,
//			success : function(response) {
//				var t = eval('('+response.responseText+')');
//				if(t.success){
//					Ext.Msg.confirm('请确认', '该网络区域下有资源正在使用，需要先将该区域下的用户进行下线再修改，还要继续操作？', function(btn, text) {
//						if (btn == 'yes') {
//							editCodeWindow.show();
//							editCodeFormPanel.getForm().loadRecord(record);
//						}
//					});
//				}else{
//					
//					if(t.msg){
//						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//						return ;
//					}else{
//						editCodeWindow.show();
//						editCodeFormPanel.getForm().loadRecord(record);
//					}
//				}
//			},
//			failure :function(){
//				editCodeWindow.show();
//				editCodeFormPanel.getForm().loadRecord(record);
//			}
//		});	
		
	}
	/**
	 * 修改cums
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './networkManager.ered?reqCode=updateNetworks',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'网络区域信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'请确保高标清导航和广告总数和不大于流化页面配置的高标清路数或查看导航流信息是否配置!',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
				fields = fields + rows[i].get('strnetregionnum') + '<br>';
			}
			checked+=rows[i].get('area_id')+"_"+rows[i].get('sg_id')+",";
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的网络区域中包含如下系统内置的只读网络区域</b><br>' + fields
							+ '<font color=red>只读网络区域不能删除!</font>!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的网络区域!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'strnetregionnum');
		//alert("strChecked="+strChecked);
		Ext.Msg.confirm('请确认', '你真的要删除网络区域吗?', function(btn, text) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : './networkManager.ered?reqCode=deleteNetwork',
					success : function(response) {
							Ext.Msg.show({title:'提示',msg:'网络区域删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							store.reload();
					},
					failure : function(response) {
						Ext.Msg.show({title:'提示',msg:'网络区域删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					},
					params : {
						strChecked : strChecked
					}
				});
			}
		});
	//	var area_id = jsArray2JsString(rows,'area_id');
//		if(area_id=='99999'){
//			Ext.Msg.show({title:'提示',msg:'中心网络区域不允许删除！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//		}else{
//			Ext.Ajax.request({
//				url :'./networkManager.ered?reqCode=queryResByNetwork&key='+checked,
//				success : function(response) {
//					var t = eval('('+response.responseText+')');
//					if(t.success){
//						Ext.Msg.confirm('请确认', '该网络区域下有资源正在使用，需要先将该区域下的用户进行下线再删除，还要继续操作？', function(btn, text) {
//							if (btn == 'yes') {
//								
//								alert("shanchu la la ");
//								Ext.Ajax.request({
//											url : './networkManager.ered?reqCode=deleteNetwork',
//											success : function(response) {
//												var resultArray = Ext.util.JSON.decode(response.responseText);
//												if(resultArray.success){
//													Ext.Msg.show({title:'提示',msg:'网络区域删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//													store.reload();
//												}else{
//													Ext.Msg.show({title:'提示',msg:'中心网络区域不允许删除！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//													store.reload();
//												}																		
//											},
//											failure : function(response) {
////																	var resultArray = Ext.util.JSON.decode(response.responseText);
//												Ext.Msg.show({title:'提示',msg:'网络区域删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//											},
//											params : {
//												strChecked : strChecked
//											}
//								});
//							}
//						});
//					}else{
//						
//						if(t.msg){
//							Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							return ;
//						}else{
//							
//							Ext.Ajax.request({
//								url : './networkManager.ered?reqCode=queryResourceByNetwork',
//								success : function(response) {
//									var jsonData = eval('('+response.responseText+')');
//									var total = jsonData.TOTALCOUNT;
//									if(total>0){
//										Ext.Msg.confirm('请确认', '该网络区域下有资源，还要继续删除操作吗?', function(btn, text) {
//											if (btn == 'yes') {
//												Ext.Ajax.request({
//													url : './networkManager.ered?reqCode=deleteNetwork',
//													success : function(response) {
//														var resultArray = Ext.util.JSON.decode(response.responseText);
//														if(resultArray.success){
//															Ext.Msg.show({title:'提示',msg:'网络区域删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//															store.reload();
//														}else{
//															Ext.Msg.show({title:'提示',msg:'中心网络区域不允许删除！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//															store.reload();
//														}																		
//													},
//													failure : function(response) {
//		//																	var resultArray = Ext.util.JSON.decode(response.responseText);
//														Ext.Msg.show({title:'提示',msg:'网络区域删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//													},
//													params : {
//														strChecked : strChecked
//													}
//												});
//											}
//										});
//									}else{
//										Ext.Msg.confirm('请确认', '你真的要删除网络区域吗?', function(btn, text) {
//											if (btn == 'yes') {
//												Ext.Ajax.request({
//													url : './networkManager.ered?reqCode=deleteNetwork',
//													success : function(response) {
//															Ext.Msg.show({title:'提示',msg:'网络区域删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//															store.reload();
//													},
//													failure : function(response) {
//														Ext.Msg.show({title:'提示',msg:'网络区域删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//													},
//													params : {
//														strChecked : strChecked
//													}
//												});
//											}
//										});
//									}	
//							},
//							failure : function(response) {
//								var resultArray = Ext.util.JSON.decode(response.responseText);
//								Ext.Msg.show({title:'提示',msg:resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
//			
//							},
//							params : {
//								strChecked : strChecked
//							}
//						});
//					}
//					}														
//				},
//				failure : function(response) {
//					var resultArray = Ext.util.JSON.decode(response.responseText);
//					Ext.Msg.show({title:'提示',msg:resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//
//				}
//			});
//		}
					
	}
	/**********************************S 全量同步缓存开始 S*******************************************/
	queryBalanceInfo(qForm.getForm());
	
	
	var formPanel = new Ext.form.FormPanel({
				id : 'codeForm',
				name : 'codeForm',
				defaultType : 'textfield',
				labelAlign : 'right',
				frame : false,
				bodyStyle : 'padding:15 40 0 0',
				items : [{
							fieldLabel : '操作密码',
							name : 'password',
							allowBlank : false,
							inputType : 'password',
							labelStyle : micolor,
							anchor : '100%'
						}]
			});

	
	
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
	