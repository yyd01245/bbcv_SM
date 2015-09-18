/**
 * qam资源管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */

Ext.onReady(function() {
	
Ext.Msg.minWidth = 300;
Ext.Msg.maxWidth =360;
	
var formpanel = new Ext.form.FormPanel({
		id : 'formpanel',
		name : 'formpanel',
		defaultType : 'textfield',
		labelAlign : 'right',
		labelWidth : 99,
		frame : true,
		fileUpload : true,
		items : [{
					fieldLabel : '请选择导入文件',
					name : 'theFile',
					id : 'theFile',
					inputType : 'file',
					allowBlank : true,
					anchor : '99%'
				}]
});



function formWindowInit() {
	formWindow.show();
	formpanel.getForm().reset();
}
		// 准备本地数据
	var ipqamType_Store = new Ext.data.SimpleStore({
				fields : ['nameType', 'codeType'],
				data : [['MP2T_UNICAST', 'MP2T_UNICAST'], ['H264 _UNICAST', 'H264 _UNICAST']]
			});
	var rfStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url :'./qamResourceManager.ered?reqCode=queryRfIdByIpqamId&ipqamResId='+1
								}),
						reader : new Ext.data.JsonReader({}, [{
											name : 'value'
										}, {
											name : 'text'
										}]),
						baseParams : {
							areacode : ''
						}
					});
	
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './UserManage.ered?reqCode=queryUser'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'ipqamresid'
								}, {
									name : 'ipqamresport'
								}, {
									name : 'ipqamrespid'
								}, {
									name : 'ipqam_info_id'
								}, {
									name : 'state'
								}, {
									name : 'remark'
								}])
			});
	var addRfCombo = new Ext.form.ComboBox({
				hiddenName : 'rf_code',
				fieldLabel : '频点号',
				emptyText : '请选择...',
				triggerAction : 'all',
				store : rfStore,
				displayField : 'text',
				valueField : 'value',
				loadingText : '正在加载数据...',
				mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
				forceSelection : true,
				allowBlank : false,
				blankText:"频点不能为空！",
				typeAhead : true,
				resizable : true,
				editable : false,
				// value : '530101',
				anchor : '100%'
				});
			var updateRfCombo = new Ext.form.ComboBox({
				hiddenName : 'rf_code',
				fieldLabel : '频点号',
				emptyText : '请选择...',
				triggerAction : 'all',
				store : rfStore,
				displayField : 'text',
				valueField : 'value',
				loadingText : '正在加载数据...',
				mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				resizable : true,
				editable : false,
				// value : '530101',
				anchor : '100%'
				});
	Ext.Ajax.request({
	    url: './networkManager.ered?reqCode=queryNetworkList',
	    success: function(response) {
		
			var root = new Ext.tree.TreeNode({
					text : 'cums资源管理',
					expanded : true,
					id : '0'
				});
			var area = new Ext.tree.TreeNode({
					id : 'center',
					expanded : true,
				//	leaf:true,
					iconCls : 'app_boxesIcon',
					text : '网络区域'
			});
//			var area2 = new Ext.tree.TreeNode({
//					id : 'domain',
//					expanded : true,
//					iconCls : 'app_boxesIcon',
//					text : '区域'
//			});
//			var area3 = new Ext.tree.TreeNode({
//					id : 'border',
//					expanded : true,
//					iconCls : 'app_boxesIcon',
//					text : '边缘'
//			});

//			root.appendChild(area2);
//			root.appendChild(area3);
			
			root.appendChild(area);			
			var node = new Array();
			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) 
				{(function(){
					var k = i;
					node[k] = new Ext.tree.TreeNode({
						expanded : true,
					id : groupJson.ROOT[k].strnetregionnum,
					text : groupJson.ROOT[k].strnetregionname,
				//	leaf : true,
					//expanded : true,
					iconCls : 'pluginIcon'
				});
					
					
					area.appendChild(node[k]);
//				if(groupJson.ROOT[i].area_id=='99999'){
//					area.appendChild(node[i]);
//				}else if(groupJson.ROOT[i].sg_id==''&&groupJson.ROOT[i].area_id!='99999'){
//					area2.appendChild(node[i]);
//				}else{
//					area3.appendChild(node[i]);
//				}
					
				Ext.Ajax.request({
					
						url:'./qamManager.ered?reqCode=queryIPQAMListBykey&network_area_id='+node[k].id,
						success: function(response) {
							var nodee = new Array();
							var groupJson1 = eval('('+response.responseText+')'); 
							var total1 = groupJson1.TOTALCOUNT;
							for(var j=0;j<total1;j++) {
								nodee[j] = new Ext.tree.TreeNode({
									id : groupJson1.ROOT[j].ipqaminfoid,
									text : groupJson1.ROOT[j].ipqaminfoname,
									leaf : false,
									iconCls : 'pluginIcon-allIcon'
								});
								
								node[k].appendChild(nodee[j]);
							}	
						}
					});	
				})();							
			}
			var treeBar = new Ext.Toolbar({
				buttonAlign : 'center',
				items : [{
					xtype : 'textfield',
					emptyText : 'QAM名称过滤...',
					id : 'filter_input',
					width : 180
				}]
			});
			var menuTree = new Ext.tree.TreePanel({
						root : root,
						rootVisible:false,
						title : '',
						tbar : treeBar,
						applyTo : 'menuTreeDiv',
						autoScroll : false,
						animate : false,
						useArrows : false,
						border : false
					});
			var Node;
			menuTree.on('click', function(node) {
			
				rfStore.removeAll() ;
				Node = node;
				nodeName =  node.text;
				nodeId =  node.id;
				if(!node.hasChildNodes()) {	
					if(node.parentNode.id =='center'||node.parentNode.id =='domain'||node.parentNode.id =='border'||node.id =='center'||node.id =='domain'||node.id =='border'){
					//	return;
					//	alert("有");
					}else{
						
						
						//alert("node.id"+node.id);
						store.proxy = new Ext.data.HttpProxy({url:'./qamResourceManager.ered?reqCode=queryItemsByQamID&ipqamInfoId='+ node.id});
						store.load({
							params:{
								start:0,
								limit:bbar.pageSize
						   }
					    });
						
//						Ext.Ajax.request({
//		   					 url :'./qamResourceManager.ered?reqCode=queryItemsByQamID&ipqam_info_id='+node.id,
//		    				 success: function(response) {
//							
//			    				 var groupJson = eval('('+response.responseText+')');
//			    				 var rfRoot = groupJson.ROOT;					   	
//							   	 rfStore.loadData(rfRoot,true);
//							   	 if(rfStore.getCount()==0){
//							   		Ext.Msg.show({title:'提示',msg:'该网络区域下无可用频点，请先到频点管理中添加可用频点！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
//							   	 }
//						 	   }	
//						})
					}
						
				}										
			});		
			
			var filter = new Ext.tree.TreeFilter(menuTree,{
				clearBlank : true,
				autoClear: true
			});
			var field = Ext.get("filter_input");
			var hidenPkgs = [] ;
			field.on("keyup",function(e){
				var text = field.dom.value;
				
				Ext.each(hidenPkgs,function(n){
					n.ui.show();
				});
				
				if(!text){
					filter.clear();
					return ;
				}
				
				menuTree.expandAll() ;
				
//				var re = new RegExp(Ext.escapeRe(text),'i');
//				filter.filterBy(function(n){	
//					alert("--------------------------------------------------------");
//					return  !n.isLeaf() || re.test(n.text);  
//				});
//				
//				hidenPkgs = [] ;
//				menuTree.root.cascade(function(n){
//					alert("--------------------------------------------------------");
//					if(!n.isLeaf() && n.ui.ctNode.offsetHeight<3){
//						n.ui.hide();
//						hidenPkgs.push(n);
//					}
//				});	
				
			});
			function statusRender(value){
				if(value == 1)
					return "<span style= 'color:red;font-weight:bold;'>无效</span>";
				else if(value == 0){
					return "有效";
				}
				else
					return value;
			}
			menuTree.root.select();
			
			
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum, sm,
//			                                   {
//						header : 'ipqam资源编号',
//						dataIndex : 'ipqamresid',
//					
//					},
					{
						header : '数据输出端口',
						
						dataIndex : 'ipqamresport'
					}, {
						header : 'pid',
					
						dataIndex : 'ipqamrespid'
					},
//					{
//						header : '资源表id',
//					
//						dataIndex : 'ipqam_info_id'
//					},
					 {
						header : '状态',
					
						dataIndex : 'state',
							sortable : true,
							renderer:statusRender
					},
					{
						id:'remark',
						header : '备注',
					
						dataIndex : 'remark'
					}]);
		
			
			// 翻页排序时带上查询条件
			store.on('beforeload', function() {
						this.baseParams = {
							queryParam : Ext.getCmp('queryParam').getValue()
						};
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
			
			
			
			var grid = new Ext.grid.GridPanel({
						title : '<span style="font-weight:bold;">资源信息</span>',
						iconCls : 'application_view_listIcon',
						height : 500,
						// width:600,
						autoScroll : true,
						region : 'center',
						store : store,
						loadMask : {
							msg : '正在加载表格数据,请稍等...'
						},
						stripeRows : true,
						frame : true,
						autoExpandColumn : 'remark',
						cm : cm,
						sm : sm,
						tbar : [{
									text : '新增',
									iconCls : 'page_addIcon',
									handler : function() {
										if(typeof(nodeId)=='undefined'||typeof(nodeId)=='')
											Ext.Msg.show({title:'提示',msg:'请先选择左侧IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										
										else addInit();
									}
								}, '-', {
									text : '修改',
									iconCls : 'page_edit_1Icon',
									handler : function() {
										if(typeof(nodeId)=='undefined'||typeof(nodeId)=='') 
											Ext.Msg.show({title:'提示',msg:'请先选择左侧IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										else ininEditCodeWindow();
									}
								}, '-', {
									text : '删除',
									iconCls : 'page_delIcon',
									handler : function() {
										if(typeof(nodeId)=='undefined'||typeof(nodeId)=='') 
											Ext.Msg.show({title:'提示',msg:'请先选择左侧IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										else deleteCodeItems();
									}
								}, '-', {
									text : 'Excel模板下载',
									iconCls : 'downloadIcon',
									handler : function() {
										if(typeof(nodeId)=='undefined'||Node.parentNode.id =='center'||Node.parentNode.id =='domain'||Node.parentNode.id =='border'||Node.id =='center'||Node.id =='domain'||Node.id =='border') 
											Ext.Msg.show({title:'提示',msg:'请先选择左侧IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										else document.location.href = "batchRes.xls";
									}
								}, '-', {
									text : '批量导入',
									iconCls : 'addIcon',
									handler : function() {
										if(typeof(nodeId)=='undefined'||Node.parentNode.id =='center'||Node.parentNode.id =='domain'||Node.parentNode.id =='border'||Node.id =='center'||Node.id =='domain'||Node.id =='border') 
											Ext.Msg.show({title:'提示',msg:'请先选择左侧IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										else formWindowInit();
									}
								}, '->', new Ext.form.TextField({
											id : 'queryParam',
											name : 'queryParam',
											emptyText : '请输入关键字',
											enableKeyEvents : true,
											hidden :true,
											listeners : {
												specialkey : function(field, e) {
													if (e.getKey() == Ext.EventObject.ENTER) {
														queryMenuItem();
													}
												}
											},
											width : 130
										})
//								, {
//									text : '查询',
//									iconCls : 'previewIcon',
//									handler : function() {
//										queryMenuItem();
//									}
//								}, '-', {
//									text : '刷新',
//									iconCls : 'arrow_refreshIcon',
//									handler : function() {
//										store.reload();
//									}
//								}
								],
						bbar : bbar
					});
			grid.addListener('rowdblclick', ininEditCodeWindow);
			grid.on('rowdblclick', function(grid, rowIndex, event) {
						ininEditCodeWindow();
					});
			grid.on('sortchange', function() {
						// grid.getSelectionModel().selectFirstRow();
					});
		
			bbar.on("change", function() {
						// grid.getSelectionModel().selectFirstRow();
					});
	
			var appStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : './qamResourceManager.ered?reqCode=getQamInfoByName'
				}),
				reader : new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
				},[{
					name : 'ipqamInfoName'
				}])
			});
			
			var addAppCom = new Ext.form.ComboBox({
				hiddenName : 'app_name',
				fieldLabel : '外部应用名称',
				store : appStore,
				displayField : 'ipqaminfoname',
				valueField : 'ipqamInfoName',
				mode : 'remote',
				triggerAction : 'all',
				forceSelection : true,
				allowBlank : false,
				editable : false,
				resizable : false,
				typeAhead : true,
				anchor : '100%'
			});
			
			var addMenuFormPanel = new Ext.form.FormPanel({
						id : 'addMenuFormPanel',
						name : 'addMenuFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 110,
						frame : false,
						bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : '数据输出端口',
									name : 'ipqamResPort',
									allowBlank : false,
									xtype : 'numberfield', // 设置为数字输入框类型
									allowDecimals : false, // 是否允许输入小数
									allowNegative : false, // 是否允许输入负数
									blankText : '数据输出端口不能为空，切必须为数字',
									anchor : '99%',
									maxLength:5,
									maxLengthText : '数据输出端口的长度不能超过5',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								},{
									fieldLabel : 'Pid',
									name : 'ipqamResPid',
									allowBlank : false,
									xtype : 'numberfield', // 设置为数字输入框类型
									allowDecimals : false, // 是否允许输入小数
									allowNegative : false, // 是否允许输入负数
									blankText : 'serverId不能为空，切必须为数字',
									anchor : '99%',
									maxLength:9,
									maxLengthText : 'serverId的长度不能超过9',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								},{
									fieldLabel : '备注',
									name : 'remark',
									anchor : '99%',
									xtype : 'textarea',
									height : 50, // 设置多行文本框的高度
									maxLength:200,
									maxLengthText : '备注的长度不能超过200',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								}
//								,{
//									xtype : 'label',
//									text : '备注：1Mbps = 1000Kbps  = 1000000 bps',
//									anchor : '100%'
//								}
								]
					});
			var addMenuWindow = new Ext.Window({
						layout : 'fit',
						width : 420,
						height : 340,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>新增Ipqam资源</span>',
						// iconCls : 'page_addIcon',
						modal : true,
						collapsible : true,
						titleCollapse : true,
						maximizable : false,
						buttonAlign : 'right',
						border : false,
						animCollapse : true,
						pageY : 20,
						pageX : document.body.clientWidth / 2 - 420 / 2,
						animateTarget : Ext.getBody(),
						constrain : true,
						items : [addMenuFormPanel],
						buttons : [{
							text : '保存',
							iconCls : 'acceptIcon',
							handler : function() {
								saveMenuItem();
							}
						}, {
							text : '重置',
							id : 'btnReset',
							iconCls : 'tbar_synchronizeIcon',
							handler : function() {
								clearForm(addMenuFormPanel.getForm());
							}
						}, {
							text : '关闭',
							iconCls : 'deleteIcon',
							handler : function() {
								addMenuWindow.hide();
							}
						}]
					});
			
			var editAppCom = new Ext.form.ComboBox({
				hiddenName : 'ipqamInfoName',
				store : appStore,
				displayField : 'ipqamInfoName',
				valueField : 'ipqamInfoName',
				fieldLabel : '外部应用名称',
				mode : 'remote',
				triggerAction : 'all',
				allowBlank : false,
				editable : false,
				resizable : false,
				typeAhead : true,
				anchor : '100%'
			});
			var editCodeFormPanel = new Ext.form.FormPanel({
						labelAlign : 'right',
						labelWidth : 80,
						defaultType : 'textfield',
						frame : false,
						bodyStyle : 'padding:5 5 0',
						id : 'editCodeFormPanel',
						name : 'editCodeFormPanel',
						items : [{
									fieldLabel : 'ipqamResId',
									name : 'ipqamresid',
									allowBlank : false,
									xtype : 'hidden',
									anchor : '99%'
								},{
									fieldLabel : '数据输出端口',
									name : 'ipqamresport',
									allowBlank : false,
									xtype : 'numberfield', // 设置为数字输入框类型
									allowDecimals : false, // 是否允许输入小数
									allowNegative : false, // 是否允许输入负数
									blankText : '数据输出端口不能为空，切必须为数字',

									anchor : '99%',
									maxLength:5,
									maxLengthText : '数据输出端口的长度不能超过5',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								},{
									fieldLabel : 'Pid',
									name : 'ipqamrespid',
									allowBlank : false,
//									xtype : 'numberfield', // 设置为数字输入框类型
//									allowDecimals : false, // 是否允许输入小数
//									allowNegative : false, // 是否允许输入负数
									blankText : 'serverId不能为空，切必须为数字',
									anchor : '99%',
									maxLength:9,
									maxLengthText : 'serverId的长度不能超过9',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								},{
									fieldLabel : '备注',
									name : 'remark',
									anchor : '99%',
									maxLength:200,
									xtype : 'textarea',
									height : 50, // 设置多行文本框的高度
									maxLengthText : '备注的长度不能超过200',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}
								}
								]
			});

			var editCodeWindow = new Ext.Window({
						layout : 'fit',
						width : 420,
						height : 340,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>修改资源</span>',
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
									Ext.Msg.show({title:'提示',
											msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',
											buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
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
			 * 布局
			 */

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									title : '<span>NETWORK-IPQAM</span>',
									iconCls : 'layout_contentIcon',
									collapsible : true,
									width : 210,
									minSize : 160,
									maxSize : 280,
									split : true,
									region : 'west',
									autoScroll : true,
									items : [menuTree]
								},{
									region : 'center',
									layout : 'fit',
									border : false,
									items : [grid]
								}]
					});
			/**
			 * 保存菜单数据
			 */
			function saveMenuItem() {
				
				if (addMenuWindow.getComponent('addMenuFormPanel').form.isValid()) {
						addMenuWindow.getComponent('addMenuFormPanel').form.submit({
						url : './qamResourceManager.ered?reqCode=saveQamRes&ipqamInfoId='+nodeId,
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							store.reload();
							Ext.Msg.confirm('请确认', '资源添加成功,您是否继续添加资源?',
									function(btn, text) {
										if (btn == 'yes') {
											addMenuWindow.getComponent('addMenuFormPanel').form
													.reset();
										} else {
											addMenuWindow.hide();
										}
									});
						},failure : function(form, action) {
							var msg = action.result.msg;
							Ext.Msg.show({title:'提示',msg:'该资源已存在于此IPQAM下，无需重复添加！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							addMenuWindow.hide();
						}
					});
				} else {
					// 表单验证失败
				}
			}
	/**
	 * 初始化代码修改出口
	 */
	function ininEditCodeWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.show({title:'提示',msg: '请先选中要修改的资源!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg: '您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
//		var  strChecked = record.data.ipqamresid;
//		Ext.Ajax.request({
//			url : './qamResourceManager.ered?reqCode=queryItemByQamRes&strChecked='+strChecked,
//			success : function(response) {
//				var t = eval('('+response.responseText+')');
//				if(t.success){
//					Ext.Msg.confirm('请确认', '该IPQAM资源正在使用，还要继续修改操作吗?', function(btn, text) {
//						if (btn == 'yes') {
//							editCodeWindow.show();
//							editCodeFormPanel.getForm().loadRecord(record);
//						}
//					});
//				}else{
//					if(t.msg){
//						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//						return ;
//					}else{
//						editCodeWindow.show();
//						editCodeFormPanel.getForm().loadRecord(record);
//					}
//				}
//			},
//			failure :function(){
//				editCodeWindow.show();
//				editCodeFormPanel.getForm().loadRecord(record);
//			}
//		});
	}
		/**
	 * 修改QAM
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './qamResourceManager.ered?reqCode=updateQamRes',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editCodeWindow.hide();
						Ext.Msg.show({title:'提示',msg:'IPQAM资源更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'IPQAM资源更新失败，请查看信息是否重复！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				});
	}

			/**
			 * 根据条件查询菜单
			 */
			function queryMenuItem() {
				store.load({
							params : {
								start : 0,
								limit : bbar.pageSize,
								queryParam : Ext.getCmp('queryParam').getValue()
							}
						});
			}
		/**
	 * 删除代码对照
	 */
	function deleteCodeItems() {
//		if (runMode == '0') {
//			Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
//			return;
//		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		if (fields != '') {
			Ext.Msg.show({title:'提示', msg:'<b>您选中的资源中包含如下系统内置的只读资源</b><br>' + fields
							+ '<font color=red>只读资源不能删除!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg: '请先选中要删除的资源!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'ipqamresid');
		
		
		Ext.Msg.confirm('请确认', '你真的要删除该IPQAM资源吗?', function(btn, text) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : './qamResourceManager.ered?reqCode=deleteQamRes',
					success : function(response) {
						Ext.Msg.show({title:'提示',msg:'IPQAM资源删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						store.reload();
					},
					failure : function(response) {
						Ext.Msg.show({title:'提示',msg:'IPQAM资源删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					},params : {
						strChecked : strChecked
					}
			});
		}
	});
		
//		Ext.Ajax.request({
//			url : './qamResourceManager.ered?reqCode=queryItemByQamRes&strChecked='+strChecked,
//			success : function(response) {
//				var t = eval('('+response.responseText+')');
//				if(t.success){
//					Ext.Msg.confirm('请确认', '该IPQAM资源正在使用，还要继续修改操作吗?', function(btn, text) {
//						if (btn == 'yes') {
//							Ext.Ajax.request({
//								url : './qamResourceManager.ered?reqCode=deleteQamRes&ipqam_id='+nodeId,
//								success : function(response) {
//									Ext.Msg.show({title:'提示',msg:'IPQAM资源删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//									store.reload();
//								},
//								failure : function(response) {
//									Ext.Msg.show({title:'提示',msg:'IPQAM资源删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//								},params : {
//									strChecked : strChecked
//								}
//							});
//						}
//					});
//				}else{
//					if(t.msg){
//						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//						return ;
//					}else{
//					
//					Ext.Msg.confirm('请确认', '你真的要删除该IPQAM资源吗?', function(btn, text) {
//						if (btn == 'yes') {
//							Ext.Ajax.request({
//								url : './qamResourceManager.ered?reqCode=deleteQamRes&ipqam_id='+nodeId,
//								success : function(response) {
//									Ext.Msg.show({title:'提示',msg:'IPQAM资源删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//									store.reload();
//								},
//								failure : function(response) {
//									Ext.Msg.show({title:'提示',msg:'IPQAM资源删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//								},params : {
//									strChecked : strChecked
//								}
//						});
//					}
//				});
//				}
//				}
//			},
//			failure :function(){
//				var resultArray = Ext.util.JSON.decode(response.responseText);
//				Ext.Msg.show({title:'提示',msg: resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//			}
//		});
		
								

	}
		
			/**
			 * 新增菜单初始化
			 */
			function addInit() {
				
				Ext.getCmp('btnReset').hide();
				addMenuWindow.show();
				addMenuWindow.setTitle('<span style="font-weight:bold;">新增IPQAM资源</span>');
				clearForm(addMenuFormPanel.getForm());
			}
			
			/**
			 * 批量导入
			 */
			function batchImport(){
				
			}
		
			/**
			 * 修改菜单数据
			 */
			function updateMenuItem() {
				if (!addMenuFormPanel.form.isValid()) {
					return;
				}
				var parentid = Ext.getCmp('parentid').getValue();
				var parentid_old = Ext.getCmp('parentid_old').getValue();
				var count = Ext.getCmp('count').getValue();
				if (parentid != parentid_old) {
					if (count != '0') {
						Ext.Msg.confirm('请确认', '此菜单已经做过权限分配,修改上级菜单属性将导致其权限信息丢失,继续保存吗?',
								function(btn, text) {
									if (btn == 'yes') {
										update();
									} else {
										return;
									}
								});
					} else {
						update();
					}
				} else {
					update();
				}
		
			}
		
			/**
			 * 更新
			 */
			function update() {
				var parentid = Ext.getCmp('parentid').getValue();
				var parentid_old = Ext.getCmp('parentid_old').getValue();
				addMenuFormPanel.form.submit({
							url : './resource.ered?reqCode=updateMenuItem',
							waitTitle : '提示',
							method : 'POST',
							waitMsg : '正在处理数据,请稍候...',
							success : function(form, action) {
								addMenuWindow.hide();
								store.reload();
								refreshNode(parentid);
								if (parentid != parentid_old) {
									refreshNode(parentid_old);
								}
								form.reset();
								Ext.MessageBox.show({title:'提示',msg: action.result.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							},
							failure : function(form, action) {
								var msg = action.result.msg;
								Ext.MessageBox.show({title:'提示',msg: '菜单数据修改失败:<br>' + msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
						});
			}
		},
	    failure: function(response) {
	    }
	});

});
	