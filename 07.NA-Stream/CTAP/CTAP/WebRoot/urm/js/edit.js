/**
 * 综合实例：数据维护
 * 
 * @author XiongChun
 * @since 2010-11-20
 */
Ext.onReady(function() {
			var qForm = new Ext.form.FormPanel({
						region : 'north',
						margins : '3 3 3 3',
						title : '<span class="commoncss">查询条件<span>',
						collapsible : false,
						border : true,
						labelWidth : 50, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:3 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 100,
						items : [{
									fieldLabel : '标识',
									name : 'xmid',
									
									value:'192.168.200.93:8080',
									emptyText : '备注信息.(输入ip和端口，例如：192.168.200.93:8080)',
									xtype : 'textfield', // 设置为数字输入框类型
								//	labelStyle : 'color:blue;',
									//allowBlank : false,
//									enableKeyEvents : true,
//											listeners : {
//												specialkey : function(field, e) {
//													if (e.getKey() == Ext.EventObject.ENTER) {
//														myForm.getForm().reset();
//														loadCallBack();
//													}
//												}
//											},
									anchor : '45%'
								}],
						buttons : [{
									text : '查询',
									iconCls : 'previewIcon',
									handler : function() {
										myForm.getForm().reset();
										loadCallBack();
									}
								}, {
									text : '重置',
									iconCls : 'tbar_synchronizeIcon',
									handler : function() {
										qForm.getForm().reset();
										myForm.getForm().reset();
										Ext.getCmp('btnSave').disable();
									}
								}]
					});
			
			function statusRender(){
				
			return 	 "<span style= 'color:green;font-weight:bold;'>正常</span>";
			}
			
			var myForm = new Ext.form.FormPanel({
						region : 'north',
						margins : '3 3 3 3',
						title : '<span class="commoncss">编辑授权信息<span>',
						collapsible : false,
						border : true,
						labelWidth : 60, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 480,
						items : [{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : .90,
												layout : 'form',
												labelWidth : 60, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : '授权码', // 标签
															name : 'xmmc', // name:后台根据此name属性取值
															maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
														//	allowBlank : false,
															labelStyle : 'color:blue;',
															//fieldStyle:'color:red;',
															anchor : '30%'// 宽度百分比
														//	disabled : true, // 设置禁用属性
														//	value : 'VS-0010', // 初始值
															//readOnly : true
															//fieldClass : 'x-custom-field-disabled',															
														//	style:'background : none;border:0 solid;'
															
															

														},
														{
															fieldLabel : '授权时间', // 标签
															name : 'xmmc', // name:后台根据此name属性取值
															maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
														//	allowBlank : false,
														//	heigth:400,
															labelStyle : 'color:blue;',
															//fieldStyle:'color:red;',
															anchor : '30%'// 宽度百分比
														//	disabled : true, // 设置禁用属性
														//	value : 'VS-0010', // 初始值
															//readOnly : true
															//fieldClass : 'x-custom-field-disabled',															
														//	style:'background : none;border:0 solid;'
															
															

														},
														{
															fieldLabel : '内容编辑', // 标签
															name : 'xmmc1', // name:后台根据此name属性取值
														//	maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
															xtype : 'htmleditor',
														//	allowBlank : false,
														
														//	width : 1250, // 窗口宽度
														//	height : 470, // 窗口高度
															labelStyle : 'color:blue;',
															//fieldStyle:'color:red;',
															anchor : '70%'// 宽度百分比
														//	disabled : true, // 设置禁用属性
														//	value : 'VS-0010', // 初始值
															//readOnly : true
															//fieldClass : 'x-custom-field-disabled',															
														//	style:'background : none;border:0 solid;'
															
															

														}]
											}
											
											]
								}]
								,
						buttons : [{
									text : '保存',
									iconCls : 'acceptIcon',
									id : 'btnSave',
								//	disabled : true,
									handler : function() {
										submitTheForm();
									}
								},{
									text : '返回',
								//	iconCls : 'acceptIcon',
									//id : 'btnSave',
								//	disabled : true,
									handler : function() {
									history.back();
									}
								}]
					});

	

			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});

			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum,sm,{
				header : '异常类型',
				dataIndex : 'advname'
					
			},{
				header : '异常详情',
				dataIndex : 'advip'
			},{
				header : '发生时间',
				dataIndex : 'advport'
				
			}]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
					proxy : new Ext.data.HttpProxy({
								url : './adsManager.ered?reqCode=queryAds'
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
						title : '<span style="font-weight:bold;">日志信息</span>',
						height : 270,
						autoScroll : true,
						frame : true,
						store : store, // 数据存储
						stripeRows : true, // 斑马线
						cm : cm, // 列模型
						sm : sm,
						tbar : [],
						bbar : bbar,// 分页工具栏
						viewConfig : {
							// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
							forceFit : true
						},
						loadMask : {
							msg : '正在加载表格数据,请稍等...'
						}
					});
		//	grid.addListener('rowdblclick', ininEditCodeWindow);
		//	grid.addListener("rowclick", function(grid,rowIndex, e){
		    	//alert(123);
		//    });
			// 布局
			// 如果把form作为center区域的话,其Height属性将失效。
			
			
			var logPanel = new Ext.Panel({
				region : 'south' ,
				split : true ,
			//	title : '异常日志',
				autoScroll : true,
				collaspile : true ,
				collased : true,
				height : 280,
				bodyStyle :'background:#CCDFEA',
				items : [grid]
			});
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'center',
									layout : 'border',
									border : false,
									items : [myForm, {
												region : 'center'
											}]
								}]
					});

			// 表单加载数据的回调函数
			function loadCallBack() {
				var params = qForm.getForm().getValues();
				myForm.form.load({
							waitMsg : '正在处理数据,请稍候...',// 提示信息
							waitTitle : '提示',// 标题
							url : 'integrateDemo.do?reqCode=querySfxm',// 请求的url地址
							params : params,
							// method : 'GET',// 请求方式
							success : function(form, action) {// 加载成功的处理函数
								var msg = action.result.data.msg;
								if (msg == 'ok') {
									Ext.getCmp('btnSave').enable();
									return;
								}
								Ext.Msg.alert('提示', msg);
								Ext.getCmp('btnSave').disable();
							},
							failure : function(form, action) {// 加载失败的处理函数
								Ext.Msg.alert('提示', '数据查询失败,错误类型:' + action.failureType);
							}
						});
			}

			/**
			 * 表单提交(表单自带Ajax提交)
			 */
			function submitTheForm() {
				if (!myForm.getForm().isValid())
					return;
				myForm.form.submit({
							url : 'integrateDemo.do?reqCode=updateSfxm',
							waitTitle : '提示',
							method : 'POST',
							waitMsg : '正在处理数据,请稍候...',
							success : function(form, action) { // 回调函数有2个参数
								Ext.MessageBox.alert('提示', action.result.msg);
							},
							failure : function(form, action) {
							//	Ext.Msg.alert('提示', '数据保存失败,错误类型:' + action.failureType);
							Ext.MessageBox.alert('提示', "保存成功");
							}
						});
			}

		});