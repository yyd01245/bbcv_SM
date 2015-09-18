/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {
	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;
		
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
					regex: /^(.*\.xls)$/,
					regexText : '导入文件格式不正确，只能导入‘.xls’文件',
					anchor : '99%'
				}]
	});
	
	var treeBar = new Ext.Toolbar({  
           buttonAlign : 'center',  
           items : [{xtype : 'textfield',emptyText : '网络区域名过滤...',id:'filter_input',width : 180}]  
     });   

	var formWindow = new Ext.Window({
		layout : 'fit',
		width : 380,
		height : 100,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '导入Excel',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [formpanel],
		buttons : [{
					text : '导入',
					iconCls : 'acceptIcon',
					handler : function() {
						if(!formpanel.form.isValid())
							return ;
						var theFile = Ext.getCmp('theFile').getValue();
						if (Ext.isEmpty(theFile)) {
							Ext.Msg.show({title:'提示',msg: '请先选择您要导入的xls文件！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							return;
						}
						if (theFile.substring(theFile.length - 4, theFile.length) != ".xls") {
							Ext.Msg.show({title:'提示',msg: '导入的文件格式有误！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
							return;
						}
						formpanel.form.submit({
							url : './qamManager.ered?reqCode=importExcel&network_code='+nodeId,
							waitTitle : '提示',
							method : 'POST',
							waitMsg : '正在处理数据,请稍候...',
							success : function(form, action) {
								Ext.MessageBox.show({title:'提示',msg:"批量导入成功！",buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								formWindow.hide();
								store.reload();
							},failure : function(form, action) {
								var msg = action.result.msg;
								Ext.MessageBox.show({title:'提示',msg: '批量导入失败，'+msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//								formWindow.hide();
							}
						});

					}
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
	var ipqamLevel_Store = new Ext.data.SimpleStore({
				fields : ['nameLevel', 'codeLevel'],
				data : [['边缘', '1'], ['区域', '2'], ['中心', '3']]
			});
	
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './networkManager.ered?reqCode=queryNetworkList'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
								name : 'ipqaminfoid'
							}, {
								name : 'ipqaminfoname'
							}, {
								name : 'ipqamip'
							}, {
								name : 'ipqamport'
							}, {
								name : 'ipqamfrequency'
							},{
								name : 'network_area_id'
							},{
								name : 'state'
							},{
								name : 'remark'
							},{
								name : 'usedcount'
							}])
		});
	Ext.Ajax.request({
	    url: './networkManager.ered?reqCode=queryNetworkList',
	    success: function(response) {
		
			var root = new Ext.tree.TreeNode({
						text : '网络区域管理',
						expanded : true,
						id : '0'
					});
			var area = new Ext.tree.TreeNode({
						id : 'center',
						expanded : true,
						iconCls : 'app_boxesIcon',
						text : '网络区域'
				});
//			var area2 = new Ext.tree.TreeNode({
//						id : 'domain',
//						expanded : true,
//						iconCls : 'app_boxesIcon',
//						text : '区域'
//				});
//			var area3 = new Ext.tree.TreeNode({
//						id : 'border',
//						expanded : true,
//						iconCls : 'app_boxesIcon',
//						text : '边缘'
//				});
			root.appendChild(area);
//			root.appendChild(area2);
//			root.appendChild(area3);
			var node = new Array();
			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {
				node[i] = new Ext.tree.TreeNode({
					id : groupJson.ROOT[i].strnetregionnum,
					text : groupJson.ROOT[i].strnetregionname,
					leaf : true,
					iconCls : 'pluginIcon'
				});
				area.appendChild(node[i]);
//				if(groupJson.ROOT[i].area_id=='99999'){
//					area.appendChild(node[i]);
//				}else if(groupJson.ROOT[i].sg_id==''&&groupJson.ROOT[i].area_id!='99999'){
//					area2.appendChild(node[i]);
//				}else{
//					area3.appendChild(node[i]);
//				}
			}
			
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
				Node = node;
				nodeName =  node.text;
				nodeId =  node.id;
				if(!node.hasChildNodes()) {	
						store.proxy = new Ext.data.HttpProxy({url:'./qamManager.ered?reqCode=queryItemsByNetworkCode&network_area_id='+ node.id});
						store.load({
							params:{
							   start:0,
							   limit:bbar.pageSize
							 }
						 });
					}
			});  
         var filter = new Ext.tree.TreeFilter(menuTree, {  
                clearBlank : true,  
                autoClear : true  
            });  
          
         // 保存上次隐藏的空节点  
         var hiddenPkgs = [];  
         var field = Ext.get('filter_input');  
         field.on('keyup', function(e) {  
            var text = field.dom.value;  
  
                // 先要显示上次隐藏掉的节点  
                Ext.each(hiddenPkgs, function(n) {  
                        n.ui.show();  
                 });  
  
                // 如果输入的数据不存在，就执行clear()  
                if (!text) {  
                    filter.clear();  
                    return;  
                }  
                menuTree.expandAll();  
  
                // 根据输入制作一个正则表达式，'i'代表不区分大小写  
                var re = new RegExp(Ext.escapeRe(text), 'i');  
                filter.filterBy(function(n) {  
                            // 只过滤叶子节点，这样省去枝干被过滤的时候，底下的叶子都无法显示  
                    return !n.isLeaf() || re.test(n.text);  
                 });  
  
                // 如果这个节点不是叶子，而且下面没有子节点，就应该隐藏掉  
                hiddenPkgs = [];  
                menuTree.root.cascade(function(n) {  
                    if (!n.isLeaf() && n.ui.ctNode.offsetHeight < 3) {  
                        n.ui.hide();  
                        hiddenPkgs.push(n);  
                    }  
                });  
         });  
//			menuTree.root.select();
			function statusRender(value){
				if(value == 1)
					return "<span style= 'color:red;font-weight:bold;'>无效</span>";
				else if(value == 0){
					return "有效";
				}
				else
					return value;
			}
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
					header : '序号',
					width : 40
			});
			
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum, sm,
			                                   {
						header : '编号',
						dataIndex : 'ipqaminfoid',
						hidden :true
					},
					{
						header : '	IPQAM名称',
						dataIndex : 'ipqaminfoname'
					}, {
						header : 'QAMIP',
						sortable : true,
						dataIndex : 'ipqamip'
					}, {
						header : 'IPQAM端口',
						sortable : true ,
						dataIndex : 'ipqamport'
					},{
						header : '频点',
						sortable : true ,
						dataIndex : 'ipqamfrequency'
					}
//					,{
//						header : 'network_area_id',
//						sortable : true ,
//						dataIndex : 'network_area_id'
//					}
					,{
						id : 'state',
						header : '状态',
						dataIndex : 'state',
						sortable : true,
						renderer:statusRender
					},{
						id : 'usedcount',
						header : '资源使用数',
						dataIndex : 'usedcount',
					},{
						id : 'remark',
						header : '备注',
						dataIndex : 'remark'
					}]);
		
			
			// 翻页排序时带上查询条件
			store.on('beforeload', function() {
						this.baseParams = {
							queryParam : Ext.getCmp('queryParam').getValue()
						}
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
						title : '<span style="font-weight:bold;">IPQAM信息</span>',
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
							//alert("sdfsdfsdfsdf");
										if(typeof(nodeId)=='undefined'||Node.parentNode.id=='0')Ext.Msg.show({title:'提示',msg:'请先选择左侧网络区域！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
										else addInit();
									}
//								}, '-', {
//									text : '修改',
//									iconCls : 'page_edit_1Icon',
//									handler : function() {
//										ininEditCodeWindow();
//									}
								}, '-', {
									text : '删除',
									iconCls : 'page_delIcon',
									handler : function() {
										deleteCodeItems();
									}
//								}, '-', {
//									text : 'Excel模板下载',
//									iconCls : 'downloadIcon',
//									handler : function() {
//										if(typeof(nodeId)=='undefined'||Node.parentNode.id=='0')Ext.Msg.show({title:'提示',msg:'请先选择左侧NETWORK！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING}); 
//										else document.location.href = "QAMmodel.xls";
//									}
//								}, '-', {
//									text : '批量导入',
//									iconCls : 'addIcon',
//									handler : function() {
//										if(typeof(nodeId)=='undefined'||Node.parentNode.id=='0')Ext.Msg.show({title:'提示',msg:'请先选择左侧NETWORK！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING}); 
//										else formWindowInit();
//									}
//								}, '->', {
//									text : '下架',
//									iconCls : 'page_delIcon',
//									handler : function() {
//										download();
//									}
//								}, '-', {
//									text : '预下架',
//									iconCls : 'page_delIcon',
//									handler : function() {
//										redownload();
//									}
//								}, '-', {
//									text : '上架',
//									iconCls : 'uploadIcon',
//									handler : function() {
//										upload();
//									}
								},'-',new Ext.form.TextField({
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
										})],
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
			
			var addMenuFormPanel = new Ext.form.FormPanel({
						id : 'addMenuFormPanel',
						name : 'addMenuFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 110,
						frame : false,
						bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : 'IPQAM名称',
									name : 'ipqamInfoName',
									allowBlank : false,
									anchor : '99%',
									maxLength:100,
									maxLengthText : 'IPQAM名称的长度不能超过100',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}			
								},{
									fieldLabel : 'IP地址',
									name : 'ipqamIp',
									allowBlank : false,	
									regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
									regexText:'必须是合适的IP地址',
									anchor : '99%'
									
								},{
									fieldLabel : 'IPQAM端口',
									name : 'ipqamPort',
									allowBlank : false,
									anchor : '99%',
									regex : /^([1-9]\d{0,4})$/,
									regexText : 'IPQAM端口格式不正确',
									listeners:{
										blur:function(obj){
											obj.setValue(Ext.util.Format.trim(obj.getValue()));
										}
									}	
								},{
							
										fieldLabel : '频点',
										name : 'ipqamFrequency',
										allowBlank : false,
										anchor : '99%',
										maxLength:200,
										maxLengthText : '备注的长度不能超过200',
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
						height : 300,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>新增IPQAM</span>',
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
			var editCodeFormPanel = new Ext.form.FormPanel({
						labelAlign : 'right',
						labelWidth : 80,
						defaultType : 'textfield',
						frame : false,
						bodyStyle : 'padding:5 5 0',
						id : 'editCodeFormPanel',
						name : 'editCodeFormPanel',
						items : [{
							fieldLabel : 'Ipqam编号', // 标签
							name : 'ipqaminfoid', // name:后台根据此name属性取值
							allowBlank : false, // 是否允许为空
							hidden:true,
							hideLabel:true,
							anchor : '100%'// 宽度百分比
							
						},{
							fieldLabel : 'IPQAM名称',
							name : 'ipqaminfoname',
							allowBlank : false,
							anchor : '99%',
							maxLength:100,
							maxLengthText : 'IPQAM名称的长度不能超过100',
							listeners:{
								blur:function(obj){
									obj.setValue(Ext.util.Format.trim(obj.getValue()));
								}
							}			
						},{
							fieldLabel : 'IP地址',
							name : 'ipqamip',
							allowBlank : false,	
							regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
							regexText:'必须是合适的IP地址',
							anchor : '99%'
							
						},{
							fieldLabel : 'IPQAM端口',
							name : 'ipqamport',
							allowBlank : false,
							anchor : '99%',
							regex : /^([1-9]\d{0,4})$/,
							regexText : 'IPQAM端口格式不正确',
							listeners:{
								blur:function(obj){
									obj.setValue(Ext.util.Format.trim(obj.getValue()));
								}
							}	
						},{
					
								fieldLabel : '频点',
								name : 'ipqamfrequency',
								allowBlank : false,
								anchor : '99%',
								maxLength:200,
								maxLengthText : '备注的长度不能超过200',
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

			var editCodeWindow = new Ext.Window({
						layout : 'fit',
						width : 420,
						height : 300,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>修改IPQAM</span>',
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
									Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
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
									title : '<span>NETWORK</span>',
									iconCls : 'layout_contentIcon',
									collapsible : true,
									width : 210,
									minSize : 160,
									maxSize : 280,
									split : true,
									region : 'west',
									autoScroll : true,
									items : [menuTree]
								}, {
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
						url : './qamManager.ered?reqCode=saveQamItem&network_area_id='+nodeId,
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							store.reload();
							Ext.Msg.confirm('请确认', 'IPQAM信息添加成功,您是否继续添加新IPQAM信息?',
									function(btn, text) {
										if (btn == 'yes') {
											addMenuFormPanel.form.reset();
										} else {
										  addMenuWindow.hide();
										}
									});
							
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.Msg.show({title:'提示',msg:'该网络区域下已存在相同的QAM或重名，请检查信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							addMenuWindow.getComponent('addMenuFormPanel').form.reset();
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
			Ext.Msg.show({title:'提示',msg: '请先选中要修改的IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示', msg:'您选中的记录为系统内置的代码对照,不允许修改!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
	
		var  strChecked = record.data.ipqam_id;
		
		editCodeWindow.show();
		editCodeFormPanel.getForm().loadRecord(record);
		
//		Ext.Ajax.request({
//			url : './qamManager.ered?reqCode=queryItemByQamID&strChecked='+strChecked,
//			success : function(response) {
//				var t = eval('('+response.responseText+')');
//				if(t.success){
//					Ext.Msg.confirm('请确认', '该IPQAM下有资源正在使用，需要先将该QAM下的用户下线再进行修改，还要继续修改操作吗?', function(btn, text) {
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
					url : './qamManager.ered?reqCode=updateQamItem',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'IPQAM信息更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						editCodeWindow.hide();
						store.reload();
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'IPQAM信息更新失败,QAM重名或是该区域已存在该IP，请查阅！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		var flag = true;
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('editmode') == '0') {
				fields = fields + rows[i].get('ipqaminfoid')+ '<br>';
			}
			if(rows[i].get('usedcount')>0) {
				flag = false;
			}
		}
		if (fields != '') {
			Ext.Msg.show({title:'提示',msg: '<b>您选中的IPQAM中包含如下系统内置的只读IPQAM</b><br>' + fields
							+ '<font color=red>只读IPQAM不能删除!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg: '请先选中要删除的IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var strChecked = jsArray2JsString(rows,'ipqaminfoid');
		
		//alert(strChecked);
		if(!flag){
			Ext.Msg.show({title:'警告',msg: '你选择要删除的IPQAM中有资源正在使用，不能删除!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		
		Ext.Msg.confirm('请确认', '确定要删除吗?', function(btn, text) {
		if (btn == 'yes') {	
			Ext.Ajax.request({
			url : './qamManager.ered?reqCode=deleteQamItems',
			success : function(response) {
				Ext.Msg.show({title:'提示',msg:'IPQAM删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
				store.reload();
			},
			failure : function(response) {
//									var resultArray = Ext.util.JSON
//											.decode(response.responseText);
				Ext.Msg.show({title:'提示',msg:'IPQAM删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			},
			params : {
				strChecked : strChecked
			}
});
		}
	});
		

//			Ext.Ajax.request({
//				url : './qamManager.ered?reqCode=queryItemByQamID&strChecked='+strChecked,
//				success : function(response) {
//					var t = eval('('+response.responseText+')');
//					if(t.success){
//						Ext.Msg.confirm('请确认', '该QAM下有资源正在使用，需要先将该QAM下的用户下线再进行删除，还要继续删除操作吗?', function(btn, text) {
//							if (btn == 'yes') {
//								Ext.Ajax.request({
//											url : './qamManager.ered?reqCode=deleteQamItems',
//											success : function(response) {
//												Ext.Msg.show({title:'提示',msg:'IPQAM删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//												store.reload();
//											},
//											failure : function(response) {
////																	var resultArray = Ext.util.JSON
////																			.decode(response.responseText);
//												Ext.Msg.show({title:'提示',msg:'IPQAM删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//											},
//											params : {
//												strChecked : strChecked
//											}
//								});
//							}
//						});
//					}else{
//						if(t.msg){
//							Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							return ;
//						}else{
//							
//							
//							
//							
//						Ext.Ajax.request({
//							url : './qamResourceManager.ered?reqCode=queryItemsByQamIDs&ipqam_id='+strChecked,
//							success : function(response) {
//								var jsonData = eval('('+response.responseText+')');
//								var total = jsonData.TOTALCOUNT;
//								if(total>0){
//									Ext.Msg.confirm('请确认', '该IPQAM下有资源，还要继续删除操作吗?', function(btn, text) {
//										if (btn == 'yes') {
//											Ext.Ajax.request({
//												url : './qamManager.ered?reqCode=deleteQamItems',
//												success : function(response) {
//													Ext.Msg.show({title:'提示',msg:'IPQAM删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//													store.reload();
//												},
//												failure : function(response) {
//													Ext.Msg.show({title:'提示',msg:'IPQAM删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//												},
//												params : {
//													strChecked : strChecked
//												}
//											});
//										}
//									});
//								}else{
//									Ext.Msg.confirm('请确认', '你真的要删除QAM吗?', function(btn, text) {
//										if (btn == 'yes') {
//											Ext.Ajax.request({
//												url : './qamManager.ered?reqCode=deleteQamItems',
//												success : function(response) {
//													Ext.Msg.show({title:'提示',msg:'IPQAM删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//													store.reload();
//												},
//												failure : function(response) {
//													Ext.Msg.show({title:'提示',msg:'IPQAM删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//												},
//												params : {
//													strChecked : strChecked
//												}
//											});
//										}
//									});
//								}														
//							},
//							failure : function(response) {
//								var resultArray = Ext.util.JSON.decode(response.responseText);
//								Ext.Msg.show({title:'提示',msg: resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							},
//							params : {
//								strChecked : strChecked
//							}
//						});
//						}
//					}														
//				},
//				failure : function(response) {
//					var resultArray = Ext.util.JSON.decode(response.responseText);
//					Ext.Msg.show({title:'提示',msg: resultArray.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//				}
//			});
		}
		
			/**
			 * 新增菜单初始化
			 */
			function addInit() {
				clearForm(addMenuFormPanel.getForm());
				Ext.getCmp('btnReset').hide();
				addMenuWindow.show();
				addMenuWindow.setTitle('<span style="font-weight:bold;">新增IPQAM信息</span>');
			
			}
			
			
		/************************S  强制下线开始  S****************************/	
		function download() {
			if (runMode == '0') {
				Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var rows = grid.getSelectionModel().getSelections();
			var fields = '';
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].get('editmode') == '0') {
					fields = fields + rows[i].get('ipqam_id')+ '<br>';
				}
			}
			if (fields != '') {
				Ext.Msg.show({title:'提示',msg: '<b>您选中的IPQAM中包含如下系统内置的只读IPQAM</b><br>' + fields
								+ '<font color=red>只读IPQAM不能下架!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			if (Ext.isEmpty(rows)) {
				Ext.Msg.show({title:'提示',msg: '请先选中要下架的IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var strChecked = jsArray2JsString(rows,'ipqam_id');
			Ext.Ajax.request({
				url : './qamManager.ered?reqCode=queryItemByQamID&strChecked='+strChecked,
				success : function(response) {
					var t = eval('('+response.responseText+')');
					if(t.success){
						Ext.Msg.confirm('请确认', '该IPQAM下有资源正在使用，还要继续进行下架操作吗?', function(btn, text) {
							if (btn == 'yes') {
								Ext.Ajax.request({
										url : './qamManager.ered?reqCode=downloadQamItems',
										success : function(response) {
											Ext.Msg.show({title:'提示',msg:'IPQAM下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
											store.reload();
										},
										failure : function(response) {
	//																	var resultArray = Ext.util.JSON
	//																			.decode(response.responseText);
											Ext.Msg.show({title:'提示',msg:'IPQAM下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
										},
										params : {
											strChecked : strChecked
										}
									});
							}
						});
					}else{
						if(t.msg){
							Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							return ;
						}else{
							Ext.Msg.confirm('请确认', '你真的要下架QAM吗?', function(btn, text) {
							if (btn == 'yes') {
								Ext.Ajax.request({
											url : './qamManager.ered?reqCode=downloadQamItems',
											success : function(response) {
												Ext.Msg.show({title:'提示',msg:'IPQAM下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
												store.reload();
											},
											failure : function(response) {
		//																	var resultArray = Ext.util.JSON
		//																			.decode(response.responseText);
												Ext.Msg.show({title:'提示',msg:'IPQAM下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
											},
											params : {
												strChecked : strChecked
											}
										});
							}
						});
					}
					}
				},
				failure :function(){
					if(t.msg){
						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						return ;
					}else{
						Ext.Msg.confirm('请确认', '你真的要下架QAM吗?', function(btn, text) {
							if (btn == 'yes') {
								Ext.Ajax.request({
											url : './qamManager.ered?reqCode=downloadQamItems',
											success : function(response) {
												Ext.Msg.show({title:'提示',msg:'IPQAM下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
												store.reload();
											},
											failure : function(response) {
		//																	var resultArray = Ext.util.JSON
		//																			.decode(response.responseText);
												Ext.Msg.show({title:'提示',msg:'IPQAM下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
											},
											params : {
												strChecked : strChecked
											}
										});
							}
						});
					}
				}
			});
		
		}
		/************************S  预下线开始  S****************************/	
		function redownload() {
			if (runMode == '0') {
				Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var rows = grid.getSelectionModel().getSelections();
			var fields = '';
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].get('editmode') == '0') {
					fields = fields + rows[i].get('ipqam_id')+ '<br>';
				}
			}
			if (fields != '') {
				Ext.Msg.show({title:'提示',msg: '<b>您选中的IPQAM中包含如下系统内置的只读IPQAM</b><br>' + fields
								+ '<font color=red>只读IPQAM不能预下架!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			if (Ext.isEmpty(rows)) {
				Ext.Msg.show({title:'提示',msg: '请先选中要预下架的IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var strChecked = jsArray2JsString(rows,'ipqam_id');
			
			
			Ext.Ajax.request({
			url : './qamManager.ered?reqCode=queryItemByQamID&strChecked='+strChecked,
			success : function(response) {
				var t = eval('('+response.responseText+')');
				if(t.success){
					Ext.Msg.confirm('请确认', '该IPQAM下有资源正在使用，还要继续预下架操作吗?', function(btn, text) {
						if (btn == 'yes') {
							Ext.Ajax.request({
								url : './qamManager.ered?reqCode=redownloadQamItems',
								success : function(response) {
									Ext.Msg.show({title:'提示',msg:'IPQAM预下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
									store.reload();
								},
								failure : function(response) {
//																	var resultArray = Ext.util.JSON
//																			.decode(response.responseText);
									Ext.Msg.show({title:'提示',msg:'IPQAM预下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								},
								params : {
									strChecked : strChecked
								}
							});
						}
					});
				}else{
					if(t.msg){
						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						return ;
					}else{
						Ext.Msg.confirm('请确认', '你真的要预下架QAM吗?', function(btn, text) {
							if (btn == 'yes') {
								Ext.Ajax.request({
									url : './qamManager.ered?reqCode=redownloadQamItems',
									success : function(response) {
										Ext.Msg.show({title:'提示',msg:'IPQAM预下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
									},
									failure : function(response) {
//																	var resultArray = Ext.util.JSON
//																			.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg:'IPQAM预下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									},
									params : {
										strChecked : strChecked
									}
								});
							}
						});
					}
				}
			},
			failure :function(){
				if(t.msg){
						Ext.Msg.show({title:'提示',msg:t.msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						return ;
				}else{
				Ext.Msg.confirm('请确认', '你真的要预下架QAM吗?', function(btn, text) {
							if (btn == 'yes') {
								Ext.Ajax.request({
									url : './qamManager.ered?reqCode=redownloadQamItems',
									success : function(response) {
										Ext.Msg.show({title:'提示',msg:'IPQAM预下架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
									},
									failure : function(response) {
//																	var resultArray = Ext.util.JSON
//																			.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg:'IPQAM预下架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									},
									params : {
										strChecked : strChecked
									}
								});
							}
						});
				}
			}
		});
					
		}
		/************************S  上线开始  S****************************/	
		function upload() {
			if (runMode == '0') {
				Ext.Msg.show({title:'提示',msg: '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var rows = grid.getSelectionModel().getSelections();
			var fields = '';
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].get('editmode') == '0') {
					fields = fields + rows[i].get('ipqam_id')+ '<br>';
				}
			}
			if (fields != '') {
				Ext.Msg.show({title:'提示',msg: '<b>您选中的IPQAM中包含如下系统内置的只读IPQAM</b><br>' + fields
								+ '<font color=red>只读IPQAM不能上架!</font>',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			if (Ext.isEmpty(rows)) {
				Ext.Msg.show({title:'提示',msg: '请先选中要上架的IPQAM!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var strChecked = jsArray2JsString(rows,'ipqam_id');
				Ext.Msg.confirm('请确认', '你真的要上架IPQAM吗?', function(btn, text) {
					if (btn == 'yes') {
						Ext.Ajax.request({
									url : './qamManager.ered?reqCode=uploadQamItems',
									success : function(response) {
										Ext.Msg.show({title:'提示',msg:'IPQAM上架成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
										store.reload();
									},
									failure : function(response) {
//																	var resultArray = Ext.util.JSON
//																			.decode(response.responseText);
										Ext.Msg.show({title:'提示',msg:'IPQAM上架失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									},
									params : {
										strChecked : strChecked
									}
								});
					}
				});
		}			
		},
	    failure: function(response) {
	    }
	});

});
	