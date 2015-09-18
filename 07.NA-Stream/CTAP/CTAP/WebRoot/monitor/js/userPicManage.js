/**
 * 用户图表
 * 
 * Faustine
 * 
 * Since 2013-07-23
 */

Ext.onReady(function() {
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;
 var panel = new Ext.Panel({
	   	contentEl:'myLineMsChart_div',
	    title : '<span>云平台用户操作记录图表显示</span>',
		region : 'center'
    });
 
 	//扩展时间组件
//	Ext.apply(Ext.form.VTypes,{
//		checkTime : function(val,field){
//			if(val!=null && val != ""){
//				var begin = Ext.get("beginTime").getValue() ;
//				var end = Ext.get("endTime").getValue();
//				if(begin!=null && begin !=""){
//					if(end!=null && end !="")
//						return (end>=begin) ;
//				}
//			}
//			return true ;
//		},
//		checkTimeText : '终止时间要晚于起始时间',
//	});
	
 	var qForm = new Ext.form.FormPanel({
		region : 'north',
		width : 580,
		height : 100,
		title : '<span>查询条件<span>',
		collapsible : true,
		border : true,
		labelWidth : 100, // 标签宽度
		// frame : true, //是否渲染表单面板背景色
		labelAlign : 'right', // 标签对齐方式
		bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
		buttonAlign : 'center',
		height :100,
		items : [{
			layout : 'column',						
		 	border : false,
			items : [{
					columnWidth : .35,
					layout : 'form',
					border : false ,
					labelWidth : 60,
					items : [{
						fieldLabel : '日期',
						name : 'beginTime',
		       			xtype : 'datefield',
		       			format:'Y-m-d',
//						vtype : 'checkTime',
		       			id : 'beginTime',
		       			allowBlank : true,
						listeners :{
							specialkey : function(field,e){
								if(e.getKey()== Ext.EventObject.ENTER){
									updateChart();
								}
							}
						}
					}]
				}
//			,{
//					columnWidth : .3,
//					layout : 'form',
//					labelWidth : 60 ,
//					border :false,
//					items :[{
//						xtype : 'datetimefield',
//						fieldLabel : '终止时间',
//						name : 'endTime',
//						id : 'endTime',
//						allowBlank : false,
//						vtype : 'checkTime',
//						prod.nebula.listeners :{
//							specialkey : function(field,e){
//								if(e.getKey()== Ext.EventObject.ENTER){
//									updateChart
//								}
//							}
//						}
//					}]}
		]
			}],
			buttons : [{
					text : '更新',
					iconCls : 'previewIcon',
					handler : function() {
						updateChart();
					}
				}, {
					text : '重置',
					iconCls : 'tbar_synchronizeIcon',
					handler : function() {
						qForm.getForm().reset();
					}
				}]
		});
 	
 		function updateChart() {
 			if(!qForm.form.isValid())
 				return ;
			Ext.Ajax.request({
						url : 'reportManage.ered?reqCode=updateChart',
						success : function(response, opts) {
							var resultArray = Ext.util.JSON
									.decode(response.responseText);
							if(!resultArray.success)
								Ext.MessageBox.show({title :'提示',msg: '获取报表数据失败',buttons:Ext.Msg.OK,icon :Ext.Msg.ERROR});
							// Ext.Msg.alert('提示', resultArray.msg);
							var xmlstring = resultArray.xmlstring;
							updateChartXML('myLineMsChart', xmlstring);
						},
						failure : function(response, opts) {
							Ext.MessageBox.show({title :'提示',msg: '获取报表数据失败',buttons:Ext.Msg.OK,icon :Ext.Msg.ERROR});
						},
						params : {
							beginTime : Ext.getCmp("beginTime").getValue()
						}
					});
		}

	var viewport = new Ext.Viewport({
						layout : 'border',
						items : [qForm, panel]
					});
});

