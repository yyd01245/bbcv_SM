/**
 * 日志查询
 * 
 * @author shy
 * @since 2012-5-28
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
			var queryValue="";
			var qForm = new Ext.form.FormPanel({
						region : 'north',
						title : '<span>查询条件<span>',
						collapsible : true,
						border : true,
						labelWidth : 100, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 80,
						items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : .25,
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [
//										{
//									        xtype : 'datetimefield',
//											fieldLabel : '起始时间', // 标签
//											//maxValue : new Date('2011-08-11 17:55:04'), //允许选择的最大时间    
//											//maxValue : new Date(2011, 8, 11, 17, 0),
////											name : 'time1', // name:后台根据此name属性取值 
//											anchor : '100%' // 宽度百分比
//										}
										{
									        xtype : 'datefield',
											fieldLabel : '起始时间', // 标签
											name : 'time1', // name:后台根据此name属性取值 
											format:'Y-m-d', //日期格式化
//											value:new Date(),
											anchor : '100%' // 宽度百分比
										}
										]
									},{
										columnWidth : .25,
										layout : 'form',
										labelWidth : 80, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [
//										{
//									        xtype : 'datetimefield',
//											fieldLabel : '终止时间', // 标签
//											//maxValue : new Date('2011-08-11 17:55:04'), //允许选择的最大时间    
//											//maxValue : new Date(2011, 8, 11, 17, 0),
//											name : 'time2', // name:后台根据此name属性取值 
//											anchor : '100%' // 宽度百分比
//										}
										{
									        xtype : 'datefield',
											fieldLabel : '终止时间', // 标签
											name : 'time2', // name:后台根据此name属性取值 
											format:'Y-m-d', //日期格式化
//											value:new Date(),
											anchor : '100%' // 宽度百分比
										}
										]
									},{
										columnWidth : .5 ,
										defaultType : 'textfield',
										buttonAlign : 'right' ,
										layout : 'form' ,
										border : false ,
										buttons:[{
											text : '查询',
											iconCls : 'previewIcon',
											handler : function() {
												queryBalanceInfo(qForm.getForm());
											}
										},{
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
			var cm = new Ext.grid.ColumnModel([rownum,sm,{
				header : '编号',
				dataIndex : 'id',
				hidden : true
			},{
				header : '应用名称',
				dataIndex : 'appname'
			},{
				header : '版本号',
				dataIndex : 'version'
			},{
				header : '主机IP',
				dataIndex : 'hostip'
			},{
				header : 'URL',
				dataIndex : 'url'
			},{
				header : '时间',
				width : 130,
				dataIndex : 'boottime'
			}]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
						proxy : new Ext.data.HttpProxy({
									url : './resourthData.ered?reqCode=queryLogList'
								}),
						// 数据读取器
						reader : new Ext.data.JsonReader({
									totalProperty : 'TOTALCOUNT', // 记录总数
									root : 'ROOT' // Json中的列表数据根节点
								}, [{
											name : 'id'
										},{
											name : 'hostip'
										},{
											name : 'appname'
										}, {
											name : 'version'
										}, {
											name : 'url'
										}, {
											name : 'boottime'
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
						title : '<span style = "font-weight : bold ;">启动日志信息</span>',
						height : 300,
						autoScroll : true,
						frame : true,
						store : store, // 数据存储
						stripeRows : true, // 斑马线
						cm : cm, // 列模型
						sm : sm,
//						tbar : [{
//									text : '查询',
//									iconCls : 'previewIcon',
//									handler : function() {
//										queryBalanceInfo(qForm.getForm());
//									}
//								},'-', {
//									text : '重置',
//									iconCls : 'tbar_synchronizeIcon',
//									handler : function() {
//										qForm.getForm().reset();
//									}
//								}],
						bbar : bbar,// 分页工具栏
						viewConfig : {
							// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
							forceFit : true
						},
						loadMask : {
							msg : '正在加载表格数据,请稍等...'
						}
					});	
		

			// 布局
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [qForm,grid]
					});
			
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
});
	