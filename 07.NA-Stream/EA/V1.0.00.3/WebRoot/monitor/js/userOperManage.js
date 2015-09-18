/**
 * 用户报表
 * 
 * Faustine
 * 
 * Since 2013-07-23
 */
var area_id;
var area_text;
Ext.onReady(function() {
	window.setTimeout("getArea();",1000);
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360;
	var addRoot2  = new Ext.tree.AsyncTreeNode({
			id : area_id,
			text : area_text,
			expanded : true
		});
	var addDeptTree2 = new Ext.tree.TreePanel({
			loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : '../demo/treeDemo.ered?reqCode=queryAreas'
			}),
			root : addRoot2 ,
			autoScroll : true,
			animate : false,
			useArrows : false,
			border : false
			
	});
	// 监听下拉树的节点单击事件
	var nodeeID ='';
	addDeptTree2.on('click', function(node) {
				nodeeID=node.id;
				comboxWithTree2.setValue(node.id);
				comboxWithTree2.collapse();
			});
	//动态加载根节点
		addDeptTree2.on('beforeload',function(){
			addRoot2.setId(area_id);
			addRoot2.setText(area_text);
		});
	 var comboxWithTree2 = new Ext.form.ComboBox({
		name : 'area_id',
		store : new Ext.data.SimpleStore({
					fields : [],
					data : [[]]
				}),
		editable : true,
		typeAhead:true ,
		value : '',
		emptyText : '',		
		forceSelection: false,
		fieldLabel : '运营区域',
		labelWidth : 60, // 标签宽度
		displayField : 'text',
		valueField : 'id',
		anchor : '100%',
		mode : 'local',
		triggerAction : 'all',
		maxHeight : 390,
		// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
		tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv2'></div></div></tpl>",
		onSelect : Ext.emptyFn,
		listeners:{
		 	specialkey:function(combobox,e){
		 	if(e.getKey() == Ext.EventObject.ENTER){
		 		queryBalanceInfo(qForm.getForm());
				qWindow.collapse();
			}
		 	}
		}
	});
	// 监听下拉框的下拉展开事件
	comboxWithTree2.on('expand', function() {
		
			addDeptTree2.rendered = false ;
			// 将UI树挂到treeDiv容器
			addDeptTree2.render('addDeptTreeDiv2');
			// addDeptTree.root.expand(); //只是第一次下拉会加载数据
			addDeptTree2.root.reload(); // 每次下拉都会加载数据

	});
	
	//扩展时间组件
	Ext.apply(Ext.form.VTypes,{
		checkTime : function(val,field){
			if(val!=null && val != ""){
				var begin = Ext.get("beginTime").getValue() ;
				var end = Ext.get("endTime").getValue();
				if(begin!=null && begin !=""){
					if(end!=null && end !="")
						return (end>=begin) ;
				}
			}
			return true ;
		},
		checkTimeText : '终止时间要晚于起始时间',
	});
	
	var textSearch = new Ext.form.TextField({
		fieldLabel : '终端号',
		name : 'term_id',
		labelWidth : 60, // 标签宽度
		id : 'term_id',
		xtype : 'textfield',
		anchor: '100%',
		listeners:{
		 	specialkey:function(field,e){
			if(e.getKey() == Ext.EventObject.ENTER){
		 		queryBalanceInfo(qForm.getForm());
				qWindow.collapse();
		 	}
			}
		}
	});
	var textSearchQam = new Ext.form.TextField({
		fieldLabel : 'QAM(IP:PORT)',
		name : 'ip_port',
		id :'ip_port',
		xtype : 'textfield',
		anchor : '100%',
		labelWidth : 80,
		listeners : {
			specialkey:function(filed,e){
			if(e.getKey() == Ext.EventObject.ENTER){
		 		queryBalanceInfo(qForm.getForm());
				qWindow.collapse();
			}
		  }
		}
	});
	
	var termCom = new Ext.form.ComboBox({
		fieldLabel : '终端类型',
		name : 'term_type',
		store : TERMTYPEStore,
		labelWidth : 60, // 标签宽度
		displayField : 'text',
		valueField : 'value',
		mode : 'local',
		triggerAction : 'all',
		editable : false,
		resizable : false,
		anchor : '100%',
		listeners:{
		 	specialkey:function(combobox,e){
			if(e.getKey() == Ext.EventObject.ENTER){
		 		queryBalanceInfo(qForm.getForm());
				qWindow.collapse();	
				}
		 	}
		}
	});
	
	//操作类型
	var operStore = new Ext.data.SimpleStore({
		fields :['name','value'],
		data:[['登陆','1'],['退出','2'],['切台',3]]
	});
	
	var operCom = new Ext.form.ComboBox({
		fieldLabel : '操作类型',
		hiddenName : 'oper_type',
		labelWidth : 60, // 标签宽度
		store :operStore,
		displayField : 'name',
		valueField : 'value',
		anchor :'100%',
		mode : 'local',
		triggerAction : 'all',
		editable : false,
		resizable : false,
		listeners:{
		 	specialkey:function(combobox,e){
				if(e.getKey() == Ext.EventObject.ENTER){
					queryBalanceInfo(qForm.getForm());
					qWindow.collapse();
				}
		 	}
		}
	});
	
	var textSearchVem = new Ext.form.TextField({
		fieldLabel : 'VEM(IP:PORT)',
		name : 'vemip_port',
		id : 'vemip_port',
		labelWidth : 80,
		anchor : '100%',
		xtype : 'textfield',
		listeners :{
			specialkey : function(field,e){
				if(e.getKey() == Ext.EventObject.ENTER){
					queryBalanceInfo(qForm.getForm());
					qWindow.collapse();
				}
			}
		}
	});
	var qForm = new Ext.form.FormPanel({
				border : false,
				labelAlign : 'right', // 标签对齐方式
				bodyStyle : 'padding:5 10 0', // 表单元素和表单面板的边距
				buttonAlign : 'center',
				height : 120,
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : .5,
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [textSearch,
											comboxWithTree2,{
											fieldLabel : '起始时间',
											name : 'beginTime',
							       			xtype : 'datetimefield',
											vtype : 'checkTime',
							       			id : 'beginTime',
							       			anchor : '100%',
											listeners :{
												specialkey : function(field,e){
												if(e.getKey() == Ext.EventObject.ENTER){
													queryBalanceInfo(qForm.getForm());
													qWindow.collapse();
												}
											}
										}
										}
											]
									}, {
										columnWidth : .5,
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [termCom,
											operCom,{
											xtype : 'datetimefield',
											fieldLabel : '终止时间',
											name : 'endTime',
											id : 'endTime',
											vtype : 'checkTime',
											anchor : '100%',
											listeners :{
												specialkey : function(field,e){
													if(e.getKey() == Ext.EventObject.ENTER){
														queryBalanceInfo(qForm.getForm());
														qWindow.collapse();
														}
												}
											}
										}
											]
									}]
						}
						,
						textSearchQam,
						textSearchVem
				]
			});

			var qWindow = new Ext.Window({
						title : '<span>查询条件</span>', // 窗口标题
						layout : 'fit', // 设置窗口布局模式
						width : 480, // 窗口宽度
						height : 210, // 窗口高度
						closable : false, // 是否可关闭
						closeAction : 'hide', // 关闭策略
						collapsible : true, // 是否可收缩
						maximizable : false, // 设置是否可以最大化
						border : true, // 边框线设置
						constrain : true,
						titleCollapse : true,
						animateTarget : Ext.getBody(),
						pageY : 30, // 页面定位Y坐标
						pageX : document.body.clientWidth / 2 - 480 / 2, // 页面定位X坐标
						// 设置窗口是否可以溢出父容器
						buttonAlign : 'center',
						items : [qForm],
						buttons : [{
									text : '查询',
									iconCls : 'previewIcon',
									handler : function() {
										queryBalanceInfo(qForm.getForm());
										qWindow.collapse();
									}
								}, {
									text : '重置',
									iconCls : 'tbar_synchronizeIcon',
									handler : function() {
										qForm.getForm().reset();
									}
								}, {
									text : '关闭',
									iconCls : 'deleteIcon',
									handler : function() {
										qWindow.hide();
									}
								}]
					});
			qWindow.show(); // 显示窗口

			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
						header : '序号',
						width : 40
					});
			//操作类型
			function typeRender(value){
				if(value == 1)
					return "登陆";
				else if(value==2)
					return "退出";
				else if(value==3)
					return "切台";
		}

			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum,sm,  {
				header : '终端号',
				dataIndex : 'term_id',
				width:230,
				sortable : true,
				editor: new Ext.form.TextField({
					readOnly :true
				}),
				sortable : true
			}, {
				header : '终端类型',
				dataIndex : 'term_type',
				sortable : true,
				renderer:TERMTYPERender
			}, {
				header : '运营区域',
				dataIndex : 'area_id',
				sortable : true,
//				hidden :true
			}, {
				header : '操作类型',
				dataIndex : 'oper_type',
				renderer : typeRender,
			},{
				header : '操作结果',
				dataIndex : 'oper_status',
				sortable : true
			},{
				header : '操作时间',
				dataIndex : 'oper_time'
			},{
				header : '错误码',
				dataIndex : 'error_code',
			},{
				header : 'IPQAMIP',
				dataIndex : "ipqam_ip",
			}, {
				header : 'IPQAMPORT',
				dataIndex : 'ipqam_port'
			},{
				header : 'VEMIP',
				dataIndex : 'vem_ip',
			}, {
				header : 'VEMPORT',
				dataIndex : 'vem_port'
			}, {
				header : '平台地址',
				dataIndex : 'platform_url'
			}]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// 获取数据的方式
						proxy : new Ext.data.HttpProxy({
									url : './reportManage.ered?reqCode=queryUserOper'
								}),
						// 数据读取器
						reader : new Ext.data.JsonReader({
									totalProperty : 'TOTALCOUNT', // 记录总数
									root : 'ROOT' // Json中的列表数据根节点
								}, [{
											name : 'term_id'
										},{
											name : 'term_type'
										},{
											name : 'oper_type'
										}, {
											name : 'platform_url'
										},{
											name : 'error_code'
										},{
											name : 'ipqam_ip'
										},{
											name : 'ipqam_port'
										},{
											name : 'vem_ip'
										},{
											name : 'vem_port'
										},{
											name : 'oper_status'
										},{
											name : 'oper_time'
										},{
											name : 'area_id'
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

			// 表格工具栏
			var tbar = new Ext.Toolbar({
			items : [{
							text : '导出数据',
							iconCls : 'page_excelIcon',
							handler : function() {
								exportExcel("./reportManage.ered?reqCode=exportExcel");
							}
						}]
			});

			// 表格实例
			var grid = new Ext.grid.EditorGridPanel({
						region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
						// collapsible : true,
						border : true,
						// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
						title : '<span style = "font-weight:bold;">用户报表</span>',
						// height : 500,
						autoScroll : true,
						frame : true,
						store : store, // 数据存储
						stripeRows : true, // 斑马线
						cm : cm, // 列模型
						sm :sm,
						tbar : tbar,
						bbar : bbar,// 分页工具栏
						viewConfig : {
							// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
//							forceFit : true
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

			// 查询表格数据
			function queryBalanceInfo(pForm) {
				var params = pForm.getValues();
				params.start = 0;
				params.limit = bbar.pageSize;
				store.load({
							params : params
						});
			}

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
	    }
	    }	
	})		
}