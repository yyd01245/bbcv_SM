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

//                              {
//								bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
//								columnWidth : .30,
//								layout : 'form',
//								labelWidth : 80, // 标签宽度
//								defaultType : 'textfield',
//								border : false,
//								items : [{
//											fieldLabel : '流化导航地址',
//											name : 'strNav_url',
//											xtype : 'textfield',
//											anchor : '100%',
//											regex : /[a-zA-z]+:\/\/[^\s]*/,
//											regexText : '导航地址格式不对',
//											listeners:{
//												specialkey:function(field,e){
//													if(e.getKey()== Ext.EventObject.ENTER)
//														queryBalanceInfo(qForm.getForm());
//												}
//											}
//										}]
//								},
								
									{
										bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
										columnWidth : .30,
										layout : 'form',
										labelWidth : 80, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
													fieldLabel : '频道号',
													name : 'iChannel_id',
													xtype : 'textfield',
													anchor : '100%',
													listeners:{
														specialkey:function(field,e){
															if(e.getKey()== Ext.EventObject.ENTER)
																queryBalanceInfo(qForm.getForm());
														}
													}
												}]
										},
								{
									columnWidth : .30,
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
				if(value == 1)
					return "<span style= 'color:blue;font-weight:bold;'>标清</span>";
				else if(value == 0){
					return "<span style= 'color:green;font-weight:bold;'>高清</span>";
				}
				else
					return "<span style= 'color:red;font-weight:bold;'>未知</span>";
			}
			
			function typeRender(value){
				if(value == 'A'||value == null){
					return "空闲";
				}else if(value == 'B'){
					return "广告";
				}else if(value == 'C'){
					return "导航";
				}else if(value == 'D'){
					return "等待";
				}else if(value == 'E'){
					return "点播";
				}
				else
					return "其他";
			}
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});

			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum,sm,{
				header : '流编号',
				dataIndex : 'istreamid'
			},{
				header : 'ipqamInfoId',
				dataIndex : 'ipqaminfoid'
				
			},{
				header : '区域ID',
				dataIndex : 'strnetregionnum'
				
//			},{
//				header : '导航地址',
//				dataIndex : 'strnav_url'
				
			},{
				header : '频道号',
				dataIndex : 'ichannel_id'
				
			}
			,{
				header : '高标清',
				dataIndex : 'strwhether_hd',
				sortable : true,
				renderer:statusRender
			}
			,{
				header : '流类型',
				dataIndex : 'strstreamtype',
				renderer:typeRender
			}
			
			
			]);

			var resolutionType_Store = new Ext.data.SimpleStore({
				fields : ['nameType', 'codeType'],
				data : [['高清', '0'], ['标清', '1']]
			});
			
			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
					proxy : new Ext.data.HttpProxy({
								url : './streamResourceManager.ered?reqCode=queryStreamResource'
							}),
					// 数据读取器
					reader : new Ext.data.JsonReader({
								totalProperty : 'TOTALCOUNT', // 记录总数
								root : 'ROOT' // Json中的列表数据根节点
							}, [{
							
								name : 'istreamid'
							},{
								
								name : 'ipqaminfoid'
								
							},{
								
								name : 'strnetregionnum'
								
//							},{
//								
//								name : 'strnav_url'
								
							},{
							
								name : 'ichannel_id'
								
							}
							,{
							
								name : 'strwhether_hd'
								
							}
							,{
								
								name : 'strstreamtype'
								
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
						title : '<span style="font-weight:bold;">流信息</span>',
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
			
			
			
	var areacode ,ipqamfrequency,network_area_id;		
	var addRoot  = new Ext.tree.AsyncTreeNode({
				id : area_id,
				ipqamfrequency:ipqamfrequency,
				network_area_id:network_area_id,
				text : 'ipqam信息'
				//expanded : true
			});
			var addDeptTree = new Ext.tree.TreePanel({
					loader : new Ext.tree.TreeLoader({
						baseAttrs : {},
						dataUrl : './streamResourceManager.ered?reqCode=queryAreas'
					}),
					root : addRoot ,
					autoScroll : true,
					animate : false,
					useArrows : false,
					border : false
			});
			//动态加载根节点
			addDeptTree.on('beforeload',function(){
				addRoot.setId(area_id);
				addRoot.setText("ipqam信息");
				
			});
			// 监听下拉树的节点单击事件
			addDeptTree.on('click', function(node) {
						comboxWithTree.setValue(node.id);
						
						//document.getElementById("ipqamnum_id").value=node.id;
						
						Ext.Ajax.request({
							url : './streamResourceManager.ered?reqCode=queryAreas&ipqamInfoId='+node.id,
							success : function(response) {
							var jsonData = eval('('+response.responseText+')');
						
						//	document.getElementById("iFreqPoint").value=jsonData[0].ipqamfrequency;
							document.getElementById("strNetRegionNum").value=jsonData[0].network_area_id;
							},
							failure : function(response) {
								Ext.Msg.show({title:'提示',msg:'查找失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							},
							params : {
								
							}
						});
						
					
						
						//areacode = node.id;
						comboxWithTree.collapse();
						//Ext.getCmp("formPanel").findById('deptid').setValue(node.attributes.id);
						
						
					});
			
			
			 var comboxWithTree = new Ext.form.ComboBox({
				name : 'ipqamInfoId',
				store : new Ext.data.SimpleStore({
							fields : [],
							data : [[]]
						}),
				editable : false,
				forceSelection: false,
				value : '',
				emptyText : '',
				fieldLabel : 'ipqamInfoId',
				
				anchor : '100%',
				mode : 'local',
				triggerAction : 'all',
				maxHeight : 390,
				// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
				tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
				allowBlank : false,
				editable : false,
				emptyText : '请选择..',
				onSelect : Ext.emptyFn
			});
			// 监听下拉框的下拉展开事件
			comboxWithTree.on('expand', function() {
					// 将UI树挂到treeDiv容器
					addDeptTree.rendered = false ;	
					addDeptTree.render('addDeptTreeDiv');
					addDeptTree.root.reload(); // 每次下拉都会加载数据
				});


			
			
			var areacode1 ,network_area_id1;		
			var addRoot1  = new Ext.tree.AsyncTreeNode({
						id : area_id,
						
						network_area_id:network_area_id1,
						text : 'ipqam信息'
						//expanded : true
					});
					var addDeptTree1 = new Ext.tree.TreePanel({
							loader : new Ext.tree.TreeLoader({
								baseAttrs : {},
								dataUrl : './streamResourceManager.ered?reqCode=queryAreas'
							}),
							root : addRoot1 ,
							autoScroll : true,
							animate : false,
							useArrows : false,
							border : false
					});
					//动态加载根节点
					addDeptTree1.on('beforeload',function(){
						addRoot.setId(area_id);
						addRoot.setText("ipqam信息");
						
					});
					// 监听下拉树的节点单击事件
					addDeptTree1.on('click', function(node) {
								comboxWithTree1.setValue(node.id);
								
								//document.getElementById("ipqamnum_id").value=node.id;
								
								Ext.Ajax.request({
									url : './streamResourceManager.ered?reqCode=queryAreas&ipqamInfoId='+node.id,
									success : function(response) {
									var jsonData = eval('('+response.responseText+')');
								
								//	document.getElementById("iFreqPoint").value=jsonData[0].ipqamfrequency;
									document.getElementById("strNetRegionNum1").value=jsonData[0].network_area_id;
									},
									failure : function(response) {
										Ext.Msg.show({title:'提示',msg:'查找失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									},
									params : {
										
									}
								});
								
							
								
								//areacode = node.id;
								comboxWithTree1.collapse();
								//Ext.getCmp("formPanel").findById('deptid').setValue(node.attributes.id);
								
								
							});
					
					
					 var comboxWithTree1 = new Ext.form.ComboBox({
						name : 'ipqaminfoid',
						store : new Ext.data.SimpleStore({
									fields : [],
									data : [[]]
								}),
						editable : false,
						forceSelection: false,
						value : '',
						emptyText : '',
						fieldLabel : 'ipqamInfoId',
						readOnly:true,
						fieldClass : 'x-custom-field-disabled',
						anchor : '100%',
						mode : 'local',
						triggerAction : 'all',
						maxHeight : 390,
						// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
						tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv1'></div></div></tpl>",
						allowBlank : false,
						editable : false,
						emptyText : '请选择..',
						onSelect : Ext.emptyFn
					});
					// 监听下拉框的下拉展开事件
					comboxWithTree1.on('expand', function() {
							// 将UI树挂到treeDiv容器
							addDeptTree1.rendered = false ;	
							addDeptTree1.render('addDeptTreeDiv1');
							addDeptTree1.root.reload(); // 每次下拉都会加载数据
						});

			
			
			
			
			
			
		var singleForm = new Ext.form.FormPanel({
				labelAlign : 'right', // 标签对齐方式
				bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
				buttonAlign : 'center',
				defaultType : 'textfield',
				id : 'singleForm',
				name : 'singleForm',
				items : [comboxWithTree,{
					id:'strNetRegionNum',
					fieldLabel : '区域id', // 标签
					name : 'strNetRegionNum', // name:后台根据此name属性取值
					allowBlank : false, // 是否允许为空
					readOnly : true,
					fieldClass : 'x-custom-field-disabled',
//					hideLabel: 'true',
//					hidden:true,
					anchor : '100%'// 宽度百分比

						
//				},{
//					
//					fieldLabel : '导航地址', // 标签
//					name : 'strNav_url', // name:后台根据此name属性取值
//					regex : /[a-zA-z]+:\/\/[^\s]*/,
//					regexText : '导航地址格式不对',
//                    allowBlank : false, // 是否允许为空
//					anchor : '100%'// 宽度百分比

						
				},
				{
					
					fieldLabel : '频道号', // 标签
					name : 'iChannel_id', // name:后台根据此name属性取值
					allowBlank : false, // 是否允许为空
					xtype : 'numberfield', // 设置为数字输入框类型
					allowDecimals : false, // 是否允许输入小数
					allowNegative : false, // 是否允许输入负数
					emptyText :'此处只能填写数字',
					anchor : '100%'// 宽度百分比

						
				},new Ext.form.ComboBox({
					hiddenName : 'strWhether_HD',
					fieldLabel : '分辨率',
					triggerAction : 'all',
					store : resolutionType_Store,
					displayField : 'nameType',
					valueField : 'codeType',
					mode : 'local',
					forceSelection : true,
					typeAhead : true,
					allowBlank : false,
					editable : false,
					emptyText : '请选择..',
					resizable : true,
					anchor : '100%'
				})
				
				
				]
			});

			var singleWindow = new Ext.Window({
				layout : 'fit',
				width : 500,
				height : 300,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>新增</span>',
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
						url : './streamResourceManager.ered?reqCode=saveStreamResource',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							refreshSessionTable();
							Ext.Msg.confirm('请确认', '添加成功,您是否继续添加?',
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
							Ext.Msg.show({title:'提示',msg:'该区域下频道号已存在或者现有ipqam资源数不足，请核对信息后再添加！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			
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
							
						
							name : 'istreamid', // name:后台根据此name属性取值
//									allowBlank : false, // 是否允许为空
							anchor : '100%',// 宽度百分比
								hidden:true
								
						},comboxWithTree1,{
							id:'strNetRegionNum1',
							fieldLabel : '区域号', // 标签
							name : 'strnetregionnum', // name:后台根据此name属性取值

							anchor : '100%',// 宽度百分比
//							hideLabel: 'true',
//							hidden:true,	
							allowBlank : false, // 是否允许为空
							readOnly:true,
							fieldClass : 'x-custom-field-disabled'
								
//						},{
//							
//							fieldLabel : '导航地址', // 标签
//							name : 'strnav_url', // name:后台根据此name属性取值
//							regex : /[a-zA-z]+:\/\/[^\s]*/,
//							regexText : '导航地址格式不对',
//							allowBlank : false, // 是否允许为空
//							anchor : '100%'// 宽度百分比

								
						}
						,{
							
							fieldLabel : '频道号', // 标签
							name : 'ichannel_id', // name:后台根据此name属性取值
							allowBlank : false, // 是否允许为空
							xtype : 'numberfield', // 设置为数字输入框类型
							allowDecimals : false, // 是否允许输入小数
							allowNegative : false, // 是否允许输入负数
							emptyText :'此处只能填写数字',
							anchor : '100%'// 宽度百分比

								
						},new Ext.form.ComboBox({
							hiddenName : 'strwhether_hd',
							fieldLabel : '分辨率',
							triggerAction : 'all',
							store : resolutionType_Store,
							displayField : 'nameType',
							valueField : 'codeType',
							mode : 'local',
							forceSelection : true,
							typeAhead : true,
							allowBlank : false,
							editable : false,
							emptyText : '请选择..',
							resizable : true,
							anchor : '100%'
						})
						]
						});

	editCodeWindow = new Ext.Window({
				layout : 'fit',
				width : 500,
				height : 300,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span>修改</span>',
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
			Ext.Msg.show({title:'提示',msg:'请先选中要修改的数据!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		if (record.get('strstreamtype') == 'E') {
			Ext.Msg.show({title:'提示',msg:'当前流记录处于点播状态，不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return
		}else if (record.get('strstreamtype') != 'A'&&!(record.get('strstreamtype') == ''||record.get('strstreamtype') == null)) {
			Ext.Msg.confirm('请确认', '您选中的记录当前处于非空闲状态,是否继续修改?',
					function(btn, text) {
						if (btn == 'yes') {
							editCodeWindow.show();
							editCodeFormPanel.getForm().loadRecord(record);
							var  strChecked = record.data.strnetregionnum;
						} else {
							return;
						}
					});
		}else{
			editCodeWindow.show();
			editCodeFormPanel.getForm().loadRecord(record);
			var  strChecked = record.data.strnetregionnum;
		}
		
		
		
	}
	/**
	 * 修改cums
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './streamResourceManager.ered?reqCode=updateStreamResource',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'流信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'改区域下频道号重复，请核对信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
		var flag=false;
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('strnetregionnum') + '<br>';
			}
			if (rows[i].get('strstreamtype') != 'A'&&!(rows[i].get('strstreamtype') == ''||rows[i].get('strstreamtype') == null)) {
				flag=true;
			}
			checked+=rows[i].get('istreamid')+",";
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg:'<b>您选中的包含如下系统内置的只读</b><br>' + fields
							+ '<font color=red>只读不能删除!</font>!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的信息!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'strnetregionnum');
		//alert("strChecked="+strChecked);
		if(flag){
			Ext.Msg.confirm('请确认', '您选择将要删除的流中有处于非空闲状态的,是否继续删除操作?', function(btn, text) {
				if (btn == 'yes') {
					Ext.Ajax.request({
						url : './streamResourceManager.ered?reqCode=deleteStreamResource',
						success : function(response) {
								Ext.Msg.show({title:'提示',msg:'流信息删除成功,请到《网络区域管理》中核对数据配置的有效性！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
						},
						failure : function(response) {
							Ext.Msg.show({title:'提示',msg:'处于点播状态的流不允许删除！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						},
						params : {
							strChecked : checked
						}
					});
				}
			});
		}else{
			Ext.Msg.confirm('请确认', '你真的要删除此条信息吗?', function(btn, text) {
				if (btn == 'yes') {
					Ext.Ajax.request({
						url : './streamResourceManager.ered?reqCode=deleteStreamResource',
						success : function(response) {
								Ext.Msg.show({title:'提示',msg:'流信息删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
						},
						failure : function(response) {
							Ext.Msg.show({title:'提示',msg:'处于点播状态的流不允许删除！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						},
						params : {
							strChecked : checked
						}
					});
				}
			});
		}
		
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
	