/**
 * viewport嵌套复杂布局实例
 * 
 * @author XiongChun
 * @since 2010-11-27
 */
Ext.onReady(function() {


	//1. 定义表格 
	var cm = new Ext.grid.ColumnModel([ 
	{ dataIndex: 'id' }, 
	{ dataIndex: 'sex' }

	]); 
	//2. 创建数据源 
	var data = [ 
	['监控点名称：', '键值网关监控1'], 
	[ '监控类型', '负载量'] 

	['监控频率', '5分钟'], 
	['备注', 'CC'], 

	]; 
	
	var store = new Ext.data.Store({ 
		proxy: new Ext.data.MemoryProxy(data), 
		reader: new Ext.data.ArrayReader({}, [ 
		{ name: 'id' }, { name: 'sex' }
		]) 
		}); 
	store.load();
	var grid = new Ext.grid.GridPanel({
				region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
				 collapsible : false,
				// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
				//title : '<span style="font-weight:bold;">信令网关信息</span>',
				height : 200,
				
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
		height : 150,
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
											fieldLabel : '监控项', // 标签
											name : 'xmmc', // name:后台根据此name属性取值
											maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
											allowBlank : false,
											labelStyle : 'color:blue;',
											//fieldStyle:'color:red;',
											anchor : '100%',// 宽度百分比
										//	disabled : true, // 设置禁用属性
											value : '0011', // 初始值
											readOnly : true,
											//fieldClass : 'x-custom-field-disabled',															
											style:'background : none;border:0 solid;'
											
											

										}, {
										//	xtype : 'datefield',
											fieldLabel : '报警级别', // 标签
											name : 'ggsj', // name:后台根据此name属性取值
											//disabled : true, // 设置禁用属性
											labelStyle : 'color:blue;',
											anchor : '100%',
											readOnly : true,
											value : '3级', // 初始值
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
											fieldLabel : '统计周期',
											name : 'xmrj',
											maxLength : 20,
											anchor : '100%',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	labelStyle : 'color:green;',
										//	disabled : true, // 设置禁用属性
											value : '3', // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'color:green;background : none;border:0 solid;'

										},{
											fieldLabel : '重复次数',
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
									fieldLabel : '阀值',
									name : 'xmrj',
									readOnly : true,
									labelStyle : 'color:blue;',
								//	disabled : true, // 设置禁用属性
									value : '1000', // 初始值
								//	fieldClass : 'x-custom-field-disabled',
									style:'background : none;border:0 solid;',	
									anchor : '100%'
								
							
								},{
									fieldLabel : '统计方法',
									name : 'xmrj',
									readOnly : true,
									labelStyle : 'color:blue;',
								//	disabled : true, // 设置禁用属性
									value : '平均值', // 初始值
								//	fieldClass : 'x-custom-field-disabled',
									style:'background : none;border:0 solid;',	
									anchor : '100%'
								}]
							}]
				}, 
//				{
//					fieldLabel : '备注',
//					id : 'remark',
//					name : 'remark',
//					xtype : 'textarea',
//					maxLength : 100,
//					
//					anchor : '99%'
//				}
//				,
				{
					name : 'xmid',
					id : 'xmid',
					hidden : true,
					xtype : 'textfield' // 设置为数字输入框类型
				}]
//				,
//		buttons : [{
//					text : '保存',
//					iconCls : 'acceptIcon',
//					id : 'btnSave',
//					disabled : true,
//					handler : function() {
//						submitTheForm();
//					}
//				}]
	});
	
	 var panel_pie = new Ext.Panel({
		 width:'100%',
		 height:550,
		 contentEl:'myLineChart_div'
	       // applyTo:'my2DpChart_panel_div'
	     });
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [ {
									region : 'north',
									split : true,
									frame:true,
									//title : 'south',
								//	collapsible : true,
									height : 80,
								//	items : [ panel1,panel2,panel3]
									items : [myForm]
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
												
													
											}]
								}]
					});
	

});