var area_id;
var area_text;
Ext.onReady(function(){	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth =360;
	window.setTimeout("getArea();",1000);
		var areacode;		
		var addRoott  = new Ext.tree.AsyncTreeNode({
				id : area_id,
				text : area_text,
				expanded : true
			});
		
		var addDeptTreee = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					baseAttrs : {},
					dataUrl : '../demo/treeDemo.ered?reqCode=queryAreas'
				}),
				root : addRoott ,
				autoScroll : true,
				animate : false,
				useArrows : false,
				border : false
		});
		//动态加载根节点
			addDeptTreee.on('beforeload',function(){
				addRoott.setId(area_id);
				addRoott.setText(area_text);
			});

		// 监听下拉树的节点单击事件
		addDeptTreee.on('click', function(node) {
					comboxWithTreee.setValue(node.id);
					areacode = node.id;
					//Ext.getCmp("formPanel").findById('deptid').setValue(node.attributes.id);
					comboxWithTreee.collapse();
		});

		 var comboxWithTreee = new Ext.form.ComboBox({
			name : 'area_id',
			store : new Ext.data.SimpleStore({
						fields : [],
						data : [[]]
				}),
			editable : true,
			forceSelection: false,
			value : '',
			emptyText : '',
			fieldLabel : '运营区域',
			anchor : '100%',
			mode : 'local',
			triggerAction : 'all',
			maxHeight : 390,
			// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
			tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDivv'></div></div></tpl>",
			allowBlank : true,
			onSelect : Ext.emptyFn,
			listeners:{
				specialkey:function(field,e){
					if(e.getKey()== Ext.EventObject.ENTER)
						queryInfo(queryForm.getForm());
				}
			}
		});
		// 监听下拉框的下拉展开事件
		comboxWithTreee.on('expand', function() {
				addDeptTreee.rendered = false ;
			//alert(1);
				// 将UI树挂到treeDiv容器
				addDeptTreee.render('addDeptTreeDivv');
//				 addDeptTreee.root.expand(); //只是第一次下拉会加载数据
				addDeptTreee.root.reload(); // 每次下拉都会加载数据
				var node = new Ext.tree.TreeNode({
					text:'中心',
					id: '99999'});
				addRoott.appendChild(node);
			});

	var queryValue = "" ;
	var queryForm = new Ext.form.FormPanel({
		region:'north' ,
		title:'<span>查询条件 </span>',
		collapsible:true ,
		border : true ,
		labelWidth : 100,
		labelAlign : 'right',
		bodyStyle:"padding: 5 5 0" ,
		height : 80 ,
		items : [{
			layout :'column',
			border : false ,
			items :[{
				columnWidth : .22 ,
				layout:'form',
				labelWidth:60,
				defaultType : 'textfield',
				border :false,
				items:[comboxWithTreee]
			},{
			columnWidth : .25,
			layout : 'form',
			labelWidth :60,
			border:false ,
			defaultType : 'textfield' ,
			items:[{
				fieldLabel : 'QAMIP',
				name : 'ipqam_ip',
				xtype : 'textfield',
				anchor : '100%',
				regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
				regexText : 'QAMIP的格式不正确',
				listeners:{
					specialkey:function(field,e){
						if(e.getKey()== Ext.EventObject.ENTER)
							queryInfo(queryForm.getForm());
					}
				}
			}]
			
		},{
			columnWidth : .25,
			layout : 'form',
			labelWidth :60,
			border:false ,
			defaultType : 'textfield' ,
			items:[{
				fieldLabel : '网络区域',
				name : 'sg_id',
				xtype : 'textfield',
				anchor : '100%',
				listeners:{
					specialkey:function(field,e){
						if(e.getKey()== Ext.EventObject.ENTER)
							queryInfo(queryForm.getForm());
					}
				}
			}]
			
		},{
			columnWidth : .28,
			layout: 'form' ,
			buttonAlign : 'right',
			border : false ,
			defaultType : 'button' ,
			buttons:[{
				text:'查询',
				iconCls:'previewIcon',
				handler:function(){
					queryInfo(queryForm.getForm());
				}
			}, {
				text : '重置',
				iconCls : 'tbar_synchronizeIcon',
				handler : function() {
					queryForm.getForm().reset();
				}
			}]
		}]
	}]
	});
	
	
	//定义当前行页号
	var rowNum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	
	//定义模型  查询条件：area_id 、ipqam_ip
	var sm = new Ext.grid.CheckboxSelectionModel() ;
	var cm = new Ext.grid.ColumnModel([rowNum,sm,{
		header : '运营商区域编号',
		sortable : true ,
		dataIndex : 'area_id',
		sortable : true
	},{
		header : '默认服务编号' ,
		dataIndex : 'server_id',
		sortable : true
	},{
		header : '网络区域',
		sortable:true ,
		dataIndex : 'sg_id'
	},{
		header : 'QAMIP',
		sortable: true,
		dataIndex : 'ipqam_ip'
	},{
		header : 'QAM状态',
		dataIndex : 'status',
		sortable : true,
		renderer:QAMSTATUSRender
	},{
		header : '资源编号',
		sortable : true,
		dataIndex : 'ipqam_res_id'
	},{
		header : '资源端口',
		sortable : true,
		dataIndex : 'exp_port'
	},{
		header : '资源状态',
		dataIndex : 'res_status',
		renderer:RESSTATUSRender
	},{
		header : '频点',
		sortable : true,
		dataIndex : 'rf_id'
	}]);
	
	var store = new Ext.data.Store({
		proxy:new Ext.data.HttpProxy({
			url:'./queryQam.ered?reqCode=queryItemsByQamIp'
		}),
		reader:new Ext.data.JsonReader({
			totalProperty:'TOTALCOUNT',
			root:'ROOT'
		},[{name:'area_id'},
			{name:'sg_id'},
			{name:'ipqam_ip'},
			{name:'status'},
			{name:'ipqam_res_id'},
			{name:'exp_port'},
			{name:'res_status'},
			{name:'rf_id'},
			{
				name:'server_id'
			}])
	});
	
	//查询条件
	store.on('beforeload',function(){
		
		this.baseParams = queryForm.getForm().getValues();
		
	});
	
	//页面可显示的条数下拉选择框
	var pageSize_combo = new Ext.form.ComboBox({
		name:'pagesize',
		mode:'local',
		triggerAction:'all',
		valueField:'value',
		displayField:'text',
		value:'20',
		width:85,
		editable:false,
		store:new Ext.data.ArrayStore({
			fields:['value','text'],
			data:[[10,'10条/页'],[20,'20条/页'],[50,'50条/页'],[100,'100条/页'],[250,'250条/页'],[500,'500条/页']]
		})
	});
	
	var number = parseInt(pageSize_combo.getValue());
	pageSize_combo.on('select',function(comboBox){
		bbar.pageSize = parseInt(comboBox.getValue());
		number = parseInt(comboBox.getValue());
		store.reload({
			params:{
				start:0,
				limit:bbar.pageSize
			}
		});
	});
	
	var bbar = new Ext.PagingToolbar({
		pageSize:number,
		store:store,
		displayInfo:true,
		displayMsg:'显示{0}条到{1}条，共{2}条',
		emptyMsg:'没有符合的数据',
		items:['-', '&nbsp;&nbsp;', pageSize_combo],
		plugins:new Ext.ux.ProgressBarPager() //分页进度条
		
	});
		
	var grid = new Ext.grid.GridPanel({
			region : 'center', 
			title : '<span style="font-weight:bold;">网络区域信息</span>',
			height : 300,
			autoScroll : true,
			frame : true,
			store : store, // 数据存储
			stripeRows : true, // 斑马线
			cm:cm,
			sm:sm,	
			tbar:[{
				text : '批量资源同步缓存' ,
				iconCls : 'page_addIcon' ,
				handler:function(){
					batchSychronous() ;
				}
//			},'-',{
//				text:'刷新',
//				iconCls:'arrow_refreshIcon',
//				handler:function(){
//					refreshSessionTable();
//				}
			}],
			bbar : bbar,// 分页工具栏
			viewConfig : {
				// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
				forceFit : true
			},
			loadMask:{
				msg:'正在加载表格数据，请稍等...'}
			});

	
	var queryInfo= function(formData){
		if(!formData.isValid())
			return ;
		var params = formData.getValues();
		params.start = 0;
		params.limit = bbar.pageSize;
		store.load({
			params:params
		});
	}
	
	var refreshSessionTable= function(){
		store.load({
			params:{
				start:0,
				limit:bbar.pageSize
			}
		});
	}
	var viewPort = new Ext.Viewport({	
		layout:'border',
		items:[queryForm,grid]	
	});
	
	var batchSychronous = function(){
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',
				buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('ipqam_res_id');
			}
		}
		if (fields != '') {
				Ext.Msg.show({title:'提示',msg:'<b>您选中的项目中包含如下系统内置的只读项目</b><br>' + fields
								+ '<font color=red>只读项目不能删除!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
		}
		
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要同步的资源!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}

		var strChecked = jsArray2JsString(rows,'ipqam_res_id');
		var strChecked1 = jsArray2JsString(rows,'res_status');
		Ext.Msg.confirm('请确认', '你真的要进行同步操作吗?', function(btn, text) {
					if (btn == 'yes') {
						//	showWaitMsg();
						Ext.Ajax.request({
									url : 'queryQam.ered?reqCode=batchQAMSychronous',
								
									success : function(response) {
									var t=eval('('+response.responseText+')');
									Ext.MessageBox.alert(t);
										if(!t.success){
											Ext.Msg.show({title:'提示',msg:'IPQAM资源同步失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
										}else{
											Ext.Msg.show({title:'提示',msg:'IPQAM资源同步成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										}
											store.reload();
									},
									failure : function(response) {
										Ext.Msg.show({title:'提示',msg:'同步失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			
									},
									params : {
								
										strChecked : strChecked,
										strChecked1 : strChecked1
									}
								});
					}
					
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