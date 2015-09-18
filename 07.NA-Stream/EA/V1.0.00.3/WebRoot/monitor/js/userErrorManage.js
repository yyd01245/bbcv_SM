/**
 * 用户错误分析图表
 * 
 * Faustine
 * 
 * Since 2013-08-7
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
		 		updateChart();
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
 var panel = new Ext.Panel({
	   	contentEl:'myLineMsChart_div',
	    title : '<span>云平台用户错误记录图表显示</span>',
		region : 'center'
    });
 
 var unitStore = new Ext.data.SimpleStore({
	fields : ['name','value'],
	data : [['月','0'],['周','1'],['日','2'],['时','3']]
 });
 
 var unitCom = new Ext.form.ComboBox({
	fieldLabel : '时间单位',
	store : unitStore,
	displayField : 'name',
	valueField : 'value',
	triggerAction : 'all',
	mode : 'local',
	editable : false,
	resizable : false,
	hiddenName : 'fid',
	disabled : true,
	anchor :'100%',
	listeners:{
	 	specialkey:function(combobox,e){
	 	if(e.getKey() == Ext.EventObject.ENTER){
	 		updateChart();
		}
	 }
	}
 });


 	var quickStore = new Ext.data.SimpleStore({
 		fields:['text','value'],
 		data:[['今天','0'],['昨天','1'],['最近一周','2'],['最近一个月','3'],['自定义时间','4']]
 	});
 	var quickCom = new Ext.form.ComboBox({
 		fieldLabel :'时间范围',
 		store:quickStore,
 		mode : 'local',
 		triggerAction:'all',
 		editable:false,
 		resizable:false,
 		allowBlank : false,
 		displayField:'text',
 		valueField:'value',
 		anchor:'100%',
 		hiddenName:'Qtime',
 		listeners:{
		 	specialkey:function(combobox,e){
		 	if(e.getKey() == Ext.EventObject.ENTER){
		 		updateChart();
			}
	 }
	}
 	}) ;
 	
 	quickCom.on("select",function(com){
 	
 		var value = parseInt(com.getValue());
 		if(value==4){
 			Ext.getCmp("beginTime").enable();
 			Ext.getCmp("endTime").enable();
 			unitCom.enable();
 		}else{
 			Ext.getCmp("beginTime").disable();
 			Ext.getCmp("endTime").disable();
 			unitCom.disable();
 		}
 	})
 
	
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
			items : [
				{
				columnWidth : .2,
				layout : 'form',
				border : false,
				items:[quickCom]
			},{
//				columnWidth : .2,
//				layout : 'form',
//				border : false,
//				items:[comboxWithTree2]
//			},{
				columnWidth : .15,
				layout : 'form',
				labelWidth : 60, // 标签宽度
				defaultType : 'textfield',
				border : false,
				items : [{
					fieldLabel : '起始时间',
					name : 'beginTime',
					id : 'beginTime',
	       			xtype : 'datetimefield',
					vtype : 'checkTime',
	       			id : 'beginTime',
	       			disabled : true,
	       			allowBlank : false,
	       			anchor : '100%',
					listeners :{
						specialkey : function(field,e){
						if(e.getKey() == Ext.EventObject.ENTER){
							updateChart();
						}
					}
				}
			}]
			}, {
				columnWidth : .2,
				layout : 'form',
				labelWidth : 60, // 标签宽度
				defaultType : 'textfield',
				border : false,
				items : [{
					xtype : 'datetimefield',
					fieldLabel : '终止时间',
					name : 'endTime',
					id: 'endTime',
					disabled:true,
					allowBlank : false,
					id : 'endTime',
					vtype : 'checkTime',
					anchor : '100%',
					listeners :{
						specialkey : function(field,e){
							if(e.getKey() == Ext.EventObject.ENTER){
								updateChart();
							}
						}
					}
				}]
			},{
				columnWidth : .2,
				layout : 'form',
				border :false,
				labelWidth : 80,
				items : [unitCom]
			}]
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
						url : 'reportManage.ered?reqCode=updateUserErrorChart',
						success : function(response, opts) {
							var resultArray = Ext.util.JSON
									.decode(response.responseText);
							if(!resultArray.success)
								Ext.MessageBox.show({title :'提示',msg: '获取报表数据失败',buttons:Ext.Msg.OK,icon :Ext.Msg.ERROR});
							
							var xmlstring = resultArray.xmlstring;
							updateChartXML('myLineMsChart', xmlstring);
						},
						failure : function(response, opts) {
							Ext.MessageBox.show({title :'提示',msg: '获取报表数据失败',buttons:Ext.Msg.OK,icon :Ext.Msg.ERROR});
						},
						params : qForm.form.getValues()
					});
		}

		var viewport = new Ext.Viewport({
			layout : 'border',
			items : [qForm, panel]
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
	    }
	    }	
	})		
}
