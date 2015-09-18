/**
 * viewport嵌套复杂布局实例
 * 
 * @author XiongChun
 * @since 2010-11-27
 */
Ext.onReady(function() {
	var contentCSCS='';
	var contentMSG='';
	var contentDETAL='';
	var document_CTAS='';
	var document_CSCS='';
	var document_URM='';
	var document_AIM='';
	var status_CSCS;
	var status_CTAS;
	var status_URM;
	var status_AIM;
	var iii =1;
	 
	
	var queryValue="";
	var qForm = new Ext.form.FormPanel({});
//	function statusRender(value){
//		if(value == 1){
//		
//		//	document.getElementById('runstate1').value="--";
//			return "<span style= 'color:red;font-weight:bold;'>不可用</span>";
//		}					
//		else if(value == 0){
//			
//			return "<span style= 'color:green;font-weight:bold;'>可用</span>";
//		}
//		else{
//			return value;
//		}
//	}
	
	



	// 定义自动当前页行号
	var rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40,
		hidden:true,
		hideLabel:true
});

	var cm = new Ext.grid.ColumnModel([rownum,{
		header : '报警类型',
		dataIndex : 'advname'
		//renderer:linkRender
		
	},
//	{
//		header : '总数',
//		sortable : false,
//		dataIndex : 'advip',
//		renderer:linkRender
//	},{
//		header : '正常',
//		sortable : false,
//		dataIndex : 'advport',
//		renderer:linkRender
//		
//	},
	{
	
		header : '一级告警',
		sortable : false,
		dataIndex : 'remark',
		renderer:linkRender
	
		
	},{
		header : '二级告警',
		sortable : false,
		dataIndex : 'state',
		renderer:linkRender
	
		
	},{
		header : '三级告警',
		sortable : false,
		dataIndex : 'loadnum',
		renderer:linkRender
		
	
		
	}
	]);

	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
				// 获取数据的方式
			proxy : new Ext.data.HttpProxy({
						url : './MonitorHomePage.ered?reqCode=queryAds'
					}),
			// 数据读取器
			reader : new Ext.data.JsonReader({
						totalProperty : 'TOTALCOUNT', // 记录总数
						root : 'ROOT' // Json中的列表数据根节点
					}, [{
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
							
							])
			});

	function linkRender(value){
		var c = iii++;
		
		
		if(c==1){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=1&gateway=1"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==2){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=2&gateway=1"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==3){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=3&gateway=1"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==4){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=1&gateway=2"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==5){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=2&gateway=2"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==6){
			if(value==0){
				return 0;
			}
			
			
			return '<a href="./Report.ered?reqCode=pageInit&reportType=3&gateway=2"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==7){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=1&gateway=3"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==8){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=2&gateway=3"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
		if(c==9){
			if(value==0){
				return 0;
			}
			return '<a href="./Report.ered?reqCode=pageInit&reportType=3&gateway=3"><span style="color:blue;font-weight:bold;">'+value+'</span></a>';
		}
			
	}
	
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
				 collapsible : false,
				// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
				//title : '<span style="font-weight:bold;">信令网关信息</span>',
				height : 500,
				
			//	autoScroll : true,
				frame : true,
				store : store, // 数据存储
			//	stripeRows : true, // 斑马线
				cm : cm, // 列模型
				//sm : sm,
			//	tbar : [],
			//	bbar : bbar,// 分页工具栏
				viewConfig : {
					// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
					forceFit : true
				},
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				}
			});
	//grid.addListener('rowclick', go);

	  function go(){
		  
		
		
		alert("双击了strurl"+strurl);  
	  }
	
	// 查询表格数据
	function queryBalanceInfo(pForm) {
		var params = pForm.getValues();
//		var s = eval(params);
//		queryValue = s["area_id"];
		params.start = 0;
		params.limit = bbar.pageSize;
		store.load({
					params : params
				});
	}


	
var p=	new Ext.Panel({
		id:'pn',
	//	title:"新闻",
	//	renderTo:"divPanel",					
		width:'100%',
		height:'100%',
		layout:"table",
		bodyStyle:'padding:10 10 10 10',
		layoutConfig:{
			columns:4
		},
		defaults:{
			height:450,
			width:200,
			frame:true
		},
		
		items:[
			{
				title:'接入网关监控',
				html:'当前在线台数：<div><a href="javascript:void(0);" onclick="updateChart(1)" ><b>'+joinCountOnline+'</b> 台</a></div>'+
				'当前离线台数：<div><a href="javascript:void(0);" onclick="updateChart(2)" ><b style="color:orange">'+joinCountOffline+'</b> 台</a></div>'
//					'当前运行状态：<div><b style="color:green">正常</b></div>',
			
			//	bodyStyle:'padding:20'
			},
			{
				title:'键值网关监控',
			//	bodyStyle : "background-color:red;",  
				html:'当前在线台数：<div><a href="javascript:void(0);" onclick="updateChart(3)" ><b>'+KeyCountOnline+'</b> 台</a></div>'+
				'当前离线台数：<div><a href="javascript:void(0);" onclick="updateChart(4)" ><b style="color:grey">'+KeyCountOffline+'</b> 台</a></div>'+
				'未认证台数：<div><a href="javascript:void(0);" onclick="updateChart(5)" ><b style="color:orange">'+KeyCountNoAuth+'</b> 台</a></div>'
//				'当前运行状态：<div><a href="www.baidu.com" onclick=openWindow("www.baidu.com")><b style="color:red">出现异常</b></a></div>',
		
			//	bodyStyle:'padding:20'
			}
			,
			{
				title:'信令网关监控',
				
				// bodyStyle : "background-color:red;",  
				html:'当前在线台数：<div><a href="javascript:void(0);" onclick="updateChart(6)" ><b >'+signalingCountOnline+'</b> 台</a></div>'+
				'当前离线台数：<div><a href="javascript:void(0);" onclick="updateChart(7)" ><b style="color:grey">'+signalingCountOffline+'</b> 台</a></div>'+
				'未认证台数：<div><a href="javascript:void(0);" onclick="updateChart(8)" ><b style="color:orange">'+signalingCountNoAuth+'</b> 台</a></div>'
//				'当前运行状态：<div><b style="color:green">正常</b></div>',
		
			//	bodyStyle:'padding:20'
			}
			,
			{
				title:'报警信息(近10分钟的信息)',
				colspan:2,
				width:500,
				items : [qForm,grid]
				
			//	html:"监控内容："
					
			//	bodyStyle:'padding:20'
			}
//			,
//			{
//				title:'新闻组图',
//				colspan:2,
//				html:"第一个子panel(行：1,列：1)",
//				bodyStyle:'padding:20;',
//				width:770
//			}
		]
	});


	
	var panel1 = new Ext.Panel({
		region : 'center',
		title : '<span class="commoncss">面板1</span>', // 标题
		iconCls : 'book_previousIcon', // 图标
		collapsible : false, // 是否允许折叠
		width : 170,
		autoScroll : true,
		frame : false,
		height :100
		
	});
	
	var panel2 = new Ext.Panel({
		region : 'west',
		title : '<span class="commoncss">面板2</span>', // 标题
		iconCls : 'book_previousIcon', // 图标
		collapsible : false, // 是否允许折叠
		width : 170,
		autoScroll : true,
		frame : false,
		height :100
		
	});
	
	
	var panel3 = new Ext.Panel({
		region : 'east',
		title : '<span class="commoncss">面板3</span>', // 标题
		iconCls : 'book_previousIcon', // 图标
		collapsible : false, // 是否允许折叠
		width : 170,
		autoScroll : true,
		frame : false,
		height :100
		
	});
	
	var panel = new Ext.Panel({
		width:'100%',
		height:350,
	    contentEl:'my2DcChart_div'
	 //   applyTo:'my2DcChart_panel_div1'
	    });
	
	 var panel_pie = new Ext.Panel({
		 width:'100%',
		 height:350,
	     contentEl:'my2DpChart_div',
	       // applyTo:'my2DpChart_panel_div'
	     });
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [ {
									region : 'south',
									split : true,
									frame:true,
									//title : 'south',
								//	collapsible : true,
									height : 180,
								//	items : [ panel1,panel2,panel3]
									items : [p]
								//	html:'<div id="appPlant"></div>'
								}, {
									region : 'center',
									layout : 'border',
									border:false,
									items : [ {
												region : 'center',
												split : true,
											//	title : 'center-center',
												frame:true,
												items : [panel_pie]
											//	html : '这是center-center区域'
												
													
											},{
												region : 'west',
												split : true,
												frame:true,
											//	title : 'center-west',
												width : 600,
											//	collapsible : true,
												items : [panel]
												
												//html : '这是center-west区域'
											}]
								}]
					});
	
		

				queryBalanceInfo(qForm.getForm());
});



//function updateStatusHandler(){
//    new Ext.Window({
//         title: "form",
//         items: {
//              html: "<iframe scr='/keyConfig1_demo.jsp'/>"
//         }
//    }).show(); //不好意思，少了个显示
//};	return '<a href="./Report.ered?reqCode=pageInit" >'+value+" "+rows+'</a>';
function fnMyJs1(pName){
	  
	window.location.href="./Report.ered?reqCode=pageInit&reportType=1";
//	alert('1,' + pName + '\n你可以在这个JS函数里做任何你想做的事情 :)');
}
function fnMyJs2(pName){
	window.location.href="./Report.ered?reqCode=pageInit&reportType=2";
	//alert('2,' + pName + '\n你可以在这个JS函数里做任何你想做的事情 :)');
}
function fnMyJs3(pName){
	window.location.href="./Report.ered?reqCode=pageInit&reportType=3";
	//alert('3,' + pName + '\n你可以在这个JS函数里做任何你想做的事情 :)');
}

var myForm = new Ext.form.FormPanel({
	region : 'north',
	margins : '3 3 3 3',
	//title : '<span class="commoncss">网关信息<span>',
	collapsible : false,
	border : true,
	labelWidth : 60, // 标签宽度
	// frame : true, //是否渲染表单面板背景色
	labelAlign : 'right', // 标签对齐方式
	bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
	buttonAlign : 'center',
	height : 180,
	items : [{
				layout : 'column',
				border : false,
				items : [{
							columnWidth : .33,
							layout : 'form',
							labelWidth : 60, // 标签宽度
							defaultType : 'textfield',
							border : false,
							items : [{
										fieldLabel : '模块版本', // 标签
										name : 'xmmc', // name:后台根据此name属性取值
										maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
										allowBlank : false,
										labelStyle : 'color:blue;',
										//fieldStyle:'color:red;',
										anchor : '100%',// 宽度百分比
									//	disabled : true, // 设置禁用属性
										value : 'VS-0010', // 初始值
										readOnly : true,
										//fieldClass : 'x-custom-field-disabled',															
										style:'background : none;border:0 solid;'
										
										

									}, {
									//	xtype : 'datefield',
										fieldLabel : '进程名称', // 标签
										name : 'ggsj', // name:后台根据此name属性取值
										//disabled : true, // 设置禁用属性
										labelStyle : 'color:blue;',
										anchor : '100%',
										readOnly : true,
										value : 'Thread_NO1', // 初始值
										//fieldClass : 'x-custom-field-disabled',
										style:'background : none;border:0 solid;'
									}]
						}, {
							columnWidth : .33,
							layout : 'form',
							labelWidth : 60, // 标签宽度
							defaultType : 'textfield',
							border : false,
							items : [{
										fieldLabel : '运行状态',
										name : 'xmrj',
										maxLength : 20,
										anchor : '100%',
										readOnly : true,
										labelStyle : 'color:blue;',
									//	labelStyle : 'color:green;',
									//	disabled : true, // 设置禁用属性
										value : '正常', // 初始值
									//	fieldClass : 'x-custom-field-disabled',
										style:'color:green;background : none;border:0 solid;'

									},{
										fieldLabel : '负载量',
										name : 'xmrj',
										readOnly : true,
										labelStyle : 'color:blue;',
									//	disabled : true, // 设置禁用属性
										value : '2000', // 初始值
									//	fieldClass : 'x-custom-field-disabled',
										style:'background : none;border:0 solid;',	
										anchor : '100%'
									}]
						}, {
							columnWidth : .33,
							layout : 'form',
							labelWidth : 60, // 标签宽度
							defaultType : 'textfield',
							border : false,
							items : [{
								fieldLabel : '其他',
								name : 'xmrj',
								readOnly : true,
								labelStyle : 'color:blue;',
							//	disabled : true, // 设置禁用属性
								value : '其他信息', // 初始值
							//	fieldClass : 'x-custom-field-disabled',
								style:'background : none;border:0 solid;',	
								anchor : '100%'
							
						
							},{
								fieldLabel : '其他',
								name : 'xmrj',
								readOnly : true,
								labelStyle : 'color:blue;',
							//	disabled : true, // 设置禁用属性
								value : '其他信息', // 初始值
							//	fieldClass : 'x-custom-field-disabled',
								style:'background : none;border:0 solid;',	
								anchor : '100%'
							}]
						}]
			}, 
//			{
//				fieldLabel : '备注',
//				id : 'remark',
//				name : 'remark',
//				xtype : 'textarea',
//				maxLength : 100,
//				
//				anchor : '99%'
//			}
//			,
			{
				name : 'xmid',
				id : 'xmid',
				hidden : true,
				xtype : 'textfield' // 设置为数字输入框类型
			}]
//			,
//	buttons : [{
//				text : '保存',
//				iconCls : 'acceptIcon',
//				id : 'btnSave',
//				disabled : true,
//				handler : function() {
//					submitTheForm();
//				}
//			}]
});
function showGatewayInfo(pName){
	var firstWindow = new Ext.Window({
		title : '<span class="commoncss">该网关详细信息</span>', // 窗口标题
		iconCls : 'imageIcon',
		layout : 'fit', // 设置窗口布局模式
		width : 500, // 窗口宽度
		height : 300, // 窗口高度
		// tbar : tb, // 工具栏
		closable : true, // 是否可关闭
		closeAction : 'hide', // 关闭策略
		collapsible : false, // 是否可收缩
		maximizable : true, // 设置是否可以最大化
		modal : true,
		border : false, // 边框线设置
		pageY : 20, // 页面定位Y坐标
		pageX : document.body.clientWidth / 2 - 400 / 2, // 页面定位X坐标
		constrain : true,
		// 设置窗口是否可以溢出父容器
		buttonAlign : 'center',
		items : [myForm],
		buttons : [  {
			text : '关闭',
			iconCls : 'deleteIcon',
			handler : function() {
				firstWindow.hide();
			}
		} ]
	});
	firstWindow.show(); // 显示窗口
}

function updateChart(p) {
	//alert(p);
	Ext.Ajax.request({
				url : './MonitorHomePage.ered?reqCode=queryReportXmlDatas',
				success : function(response, opts) {
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					// Ext.Msg.alert('提示', resultArray.msg);
					var xmlstring = resultArray.xmlstring;
				//	var xmlstring1 = resultArray.xmlstring1;
					updateChartXML('my2DcChart', xmlstring);
				//	updateChartXML('my2DpChart', xmlstring1);
				},
				failure : function(response, opts) {
					Ext.MessageBox.alert('提示', '获取报表数据失败');
				},
				params : {
					product : p
				}
			});
	
	
	
	
	function DomUrl(value){ 
		var row = grid.getSelectionModel().selectRow(startrow);//选中当前行 
		var rownum = grid.getSelectionModel().getSelected();//获取当前行 
		startrow ++; 
		var strurl = "abc.jsp?id=" + rownum.get('advname');//获取当前选中行的值，并组织链接字符串 
		return "<a href='"+strurl+"'>"+value+"</a>"; 
		} 

	
	
	
	
	
	
}			