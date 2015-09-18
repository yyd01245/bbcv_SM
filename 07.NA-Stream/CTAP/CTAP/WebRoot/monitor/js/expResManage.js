/**
 * 重复URM资源统计
 * 
 * @author shy
 * @since 2012-5-25
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;

	// 定义自动当前页行号
	var rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	// 定义列模型
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([ rownum, sm,{
		header : 'QAMIP',
		dataIndex : 'ipqam_ip',
		hidden:false,
	},{
		header:'QAMPORT',
		dataIndex : 'ipqam_port',
		sortable : true
	}, {
		header : '资源编号',
		dataIndex : 'res_id',
		sortable : true,
	},{
		header : '运营区域',
		dataIndex : 'area_id',
	},{
		header : '网络区域',
		dataIndex : 'region_id'
	},{
		header : '输出端口',
		dataIndex :'exp_port'
	},{
//		header : 'VEMIP',
		header :'承载IP',
		dataIndex : 'vem_ip'
	},{
//		header : 'VEMPORT',
		header : '承载PORT',
		dataIndex : 'vem_port'
	}]);

	/**
		 * 数据存储
		 */
		var store = new Ext.data.Store({
			// 获取数据的方式
			proxy : new Ext.data.HttpProxy({
				url : './platmentMenage.ered?reqCode=queryExpRes'
			}),
			// 数据读取器
			reader : new Ext.data.JsonReader({
				totalProperty : 'TOTALCOUNT', // 记录总数
				root : 'ROOT' // Json中的列表数据根节点
			}, [ {
				name : 'res_id'
			},{
				name : 'ipqam_ip'
			},{
				name : 'ipqam_port'
			},{
				name : 'exp_port'
			},{
				name : 'vem_port'
			},{
				name : 'vem_id'
			},{
				name : 'vem_port'
			},{
				name : 'area_id'
			},{
				name : 'region_id'
			},{
				name : 'id'
			}])
		});

			// 翻页排序时带上查询条件
			store.on('beforeload', function() {
				//this.baseParams = qForm.getForm().getValues();
//					this.baseParams={
//						start : 0,
//						limit : bbar.pageSize
//					}
			});
			// 每页显示条数下拉选择框
			var pagesize_combo = new Ext.form.ComboBox({
				name : 'pagesize',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
					fields : [ 'value', 'text' ],
					data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
							[ 100, '100条/页' ], [ 250, '250条/页' ],
							[ 500, '500条/页' ] ]
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
				items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
			});

			// 表格实例
			var grid = new Ext.grid.GridPanel({
				region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
				// collapsible : true,
				// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
				title : '<span style = "font-weight:bold;">异常资源</span>',
				// height : 500,
				autoScroll : true,
				frame : true,
				store : store, // 数据存储
				stripeRows : true, // 斑马线
				cm : cm, // 列模型
				sm : sm,
				tbar : [{
					text : '查询',
					iconCls : 'previewIcon',
					handler : function() {
						queryBalanceInfo();
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
			
			
			// 布局
			var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

			function queryBalanceInfo() {
//				store.reload();
				store.reload({
					params : {
						start : 0,
						limit : bbar.pageSize
					}
				});
			}
			queryBalanceInfo();
		
		});