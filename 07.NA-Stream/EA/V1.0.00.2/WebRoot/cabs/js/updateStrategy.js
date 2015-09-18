/**
 * 升级策略管理
 * 
 * @author shy
 * @since 2012-5-22
 */
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360 ;
			
	Ext.Ajax.request({
	    url: './appGroupManager.ered?reqCode=queryValidGroups',
	    success: function(response) {
			
			var root = new Ext.tree.TreeNode({
						expanded : true,
						text : '应用组',
						id : '0'
				});
			var node = new Array();
			var rootVNC = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'medal_silver_3Icon',
				text:'标清组'
			});
			var rootRSM = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'medal_gold_1Icon',
				text:'高清组'
			});
			root.appendChild(rootVNC);
//			root.appendChild(rootRSM);
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {
				node[i] = new Ext.tree.TreeNode({
					id : groupJson.ROOT[i].group_id,
					text : groupJson.ROOT[i].group_name
				});
				if(groupJson.ROOT[i].encodetype==1){
					rootVNC.appendChild(node[i]);
				}else{
					rootRSM.appendChild(node[i]);
				}				
			}
			
			var menuTree = new Ext.tree.TreePanel({
				root : root,
				rootVisible:false,
				title : '',
				applyTo : 'menuTreeDiv',
				autoScroll : false,
				animate : false,
				useArrows : false,
				border : false
			});
			var Node;
			menuTree.on("click", function(node, e) {
				Node=node;
				chinaStore.removeAll();
				if(node.parentNode.id!=0) {
					store.proxy = new Ext.data.HttpProxy({url:'./updateStrategy.ered?reqCode=getRecordIdByName&group_id='+ node.id});
					store.load({
				  	 params:{
						   start:0,
						   limit:bbar.pageSize
					  },
					  callback:function(r,options,success){
							var count = grid.getStore().getTotalCount();
							if(count>0){
								Ext.getCmp("add").disable();
							}else{
								Ext.getCmp("add").enable();
							}
					  }
				   });
					
					nodeName =  node.text;
					nodeId =  node.id;
					if(node.parentNode.text=='标清组'){
						nodeType=1;
					}else{
						nodeType=2;
					}
					Ext.Ajax.request({
						 url : './packageVersion.ered?reqCode=queryPackageList1&jar_type='+nodeType,
						 success: function(response) {
							var groupJson = eval('('+response.responseText+')');
							var chinaRoot = groupJson.ROOT;
							chinaStore.loadData(chinaRoot,true);
//							alert(chinaStore.getCount());
							if(chinaStore.getCount()==0){
								Ext.Msg.show({title:'提示',msg:'该组没有新的可升级程序包，请先到应用组！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
								Ext.getCmp("add").disable();
							}
						}	
					})
				}else{
					store.removeAll();
				}
			});
			menuTree.root.select();
			var rownum = new Ext.grid.RowNumberer({
				header : '序号',
				width : 50
			});
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([rownum, sm, {
						header : '关联编号',
						dataIndex : 'gv_id',
						hidden:true
//						width : 130
					},{
						header : '组编号',
						dataIndex : 'group_id',
						hidden:true
//						width : 130
					},{
						header : '升级包编号',
						dataIndex : 'version_id'
//						width : 130
					},{
						header : '升级包版本',
						dataIndex : 'update_version'
//						width : 130
					},{
						header : '升级包名称',
						dataIndex : 'tarname'
//						width : 130
					},{
						header : '升级包脚本',
						dataIndex : 'tarsh'
//						width : 130
					}]);
		
			/**
			 * 数据存储
			 */
			var store  = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : './updateStrategy.ered?reqCode=queryStrategyItems'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : 'TOTALCOUNT',
									root : 'ROOT'
								}, [{
											name : 'gv_id'
									}, {
											name : 'group_id'
									}, {
											name : 'version_id'
									}, {
											name : 'status'
									}, {
											name : 'create_time'
									}, {
											name : 'update_time'
									}, {
											name : 'update_version'
									}, {
											name : 'tarsh'
									}, {
											name : 'tarname'
									}])
					});
			var pagesize_combo = new Ext.form.ComboBox({
						name : 'pagesize',
						hiddenName : 'pagesize',
						typeAhead : true,
						triggerAction : 'all',
						lazyRender : true,
						mode : 'local',
						store : new Ext.data.ArrayStore({
									fields : ['value', 'text'],
									data : [[10, '10条/页'], [20, '20条/页'],
											[50, '50条/页'], [100, '100条/页'],
											[250, '250条/页'], [500, '500条/页']]
								}),
						valueField : 'value',
						displayField : 'text',
						value : '50',
						editable : false,
						width : 85
					});
			var number = parseInt(pagesize_combo.getValue());
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
		
			var bbar = new Ext.PagingToolbar({
						pageSize : number,
						store : store,
						displayInfo : true,
						displayMsg : '显示{0}条到{1}条,共{2}条',
						emptyMsg : "没有符合条件的记录",
						items : ['-', '&nbsp;&nbsp;', pagesize_combo]
					});
		
			var grid = new Ext.grid.GridPanel({
						title : '<span style="font-weight:bold;">升级包信息</span>',
						iconCls : 'application_view_listIcon',
//						height : 500,
						// width:600,
						autoScroll : true,
						region : 'center',
						store : store,
						loadMask : {
							msg : '正在加载表格数据,请稍等...'
						},
						stripeRows : true,
						frame : true,
//						autoExpandColumn : 'jar_record_id',
						cm : cm,
						sm : sm,
						tbar : [{
									text : '新增',
									iconCls : 'page_addIcon',
									id:'add',
									disabled:true,
									handler : function() {
										if(typeof(nodeId)=='undefined'||Node.parentNode.id=='0') Ext.MessageBox.show({title:'提示',msg: '请先选择左侧应用组',buttons : Ext.Msg.OK,icon:Ext.Msg.WARNING });
										else addInit(nodeName);
									}
//								}, '-', {
//									text : '删除',
//									iconCls : 'page_delIcon',
//									handler : function() {
//										if(typeof(nodeId)=='undefined') Ext.MessageBox.alert('提示', '请先选择左侧应用组' );
//										else deleteCodeItems();
//									}
							}],
						bbar : bbar
					});
		
			/**
			 * 布局
			 */
			var viewport = new Ext.Viewport({
				layout : 'border',
				items : [{
							title : '<span style = "font-weight:bold;">应用组信息</span>',
							iconCls : 'layout_contentIcon',
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
			 * 新增
			 */
			var chinaStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './packageVersion.ered?reqCode=queryPackageList&jar_type='+1
						}),
				reader : new Ext.data.JsonReader({
//							totalProperty : 'TOTALCOUNT',
//							root : 'ROOT'
						}, [{
							name : 'text'
							},{
							name : 'value'
							}]),
				baseParams : {
					areacode : ''
				}
			});
			var chinaCombo = new Ext.form.ComboBox({
				hiddenName : 'version_id',
				fieldLabel : '升级包',
				emptyText : '请选择...',
				triggerAction : 'all',
				store : chinaStore,
				displayField : 'text',
				valueField : 'value',
				loadingText : '正在加载数据...',
				mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
				forceSelection : true,
				allowBlank : false ,
				typeAhead : true,
				resizable : true,
				editable : false,
				anchor : '100%'
			});
			var addMenuFormPanel = new Ext.form.FormPanel({
				id : 'addMenuFormPanel',
				name : 'addMenuFormPanel',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 65,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [chinaCombo]
			});
			var addMenuWindow = new Ext.Window({
				layout : 'fit',
				width : 300,
				height : 100,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span style ="font-weight:bold;">关联升级包</span>',
				// iconCls : 'page_addIcon',
				modal : true,
				collapsible : true,
				titleCollapse : true,
				maximizable : false,
				buttonAlign : 'right',
				border : false,
				animCollapse : true,
				animateTarget : Ext.getBody(),
				constrain : true,
				items : [addMenuFormPanel],
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						saveInit();
					}
				}, {
					text : '重置',
					id : 'btnReset',
					iconCls : 'tbar_synchronizeIcon',
					handler : function() {
						clearForm(addMenuFormPanel.getForm());
					}
				}]
			});
			
			function saveInit() {
						if (addMenuWindow.getComponent('addMenuFormPanel').form.isValid()) {
								addMenuWindow.getComponent('addMenuFormPanel').form.submit({
								url : './updateStrategy.ered?reqCode=addStrategyItem&group_id='+nodeId,
								waitTitle : '提示',
								method : 'POST',
								waitMsg : '正在处理数据,请稍候...',
								success : function(form, action) {
									store.reload({
									  callback:function(r,options,success){
											var count = grid.getStore().getTotalCount();
											if(count>0){
												Ext.getCmp("add").disable();
											}else{
												Ext.getCmp("add").enable();
											}
									  }});
									addMenuWindow.hide();
									Ext.Msg.show({title:'提示',msg:'组和升级包关联成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
								},
								failure : function(form, action) {
//									var msg = action.result.msg;
//									Ext.MessageBox.alert('提示', '代码对照表保存失败:<br>' + msg);
//									addMenuWindow.getComponent('addMenuFormPanel').form.reset();
									Ext.Msg.show({title:'提示',msg:'组和升级包关联失败，该升级包已和组进行关联！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
								}
							});
						} else {
							// 表单验证失败
						}
			};
			function addInit(nodeName) {
					addMenuWindow.setTitle('<span>新增</span> '+nodeName+' <span>关联升级包</span>')
					addMenuWindow.show();
					clearForm(addMenuFormPanel.getForm());
			};
			
		/**
		 * 删除代码对照
		 */
		function deleteCodeItems() {
			if (runMode == '0') {
				Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.MSg.WARNING});
				return;
			}
			var rows = grid.getSelectionModel().getSelections();
			var fields = '';
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].get('editmode') == '0') {
					fields = fields + rows[i].get('gv_id') + '<br>';
				}
			}
			if (fields != '') {
				Ext.Msg.show({title:'提示',msg:'<b>您选中的关联中包含如下系统内置的只读关联</b><br>' + fields + '<font color=red>只读关联不能删除!</font>',
					buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			if (Ext.isEmpty(rows)) {
				Ext.Msg.show({title:'提示',msg:'请先选中要删除的关联！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
				return;
			}
			var strChecked = jsArray2JsString(rows, 'gv_id');
			Ext.Msg.confirm('请确认','你真的要删除关联吗?',	function(btn, text) {
					if (btn == 'yes') {
									// showWaitMsg();
							Ext.Ajax.request( {
								url : './updateStrategy.ered?reqCode=deleteStrategyItems',
								success : function(response) {
									store.reload({
									  callback:function(r,options,success){
											var count = grid.getStore().getTotalCount();
											if(count>0){
												Ext.getCmp("add").disable();
											}else{
												Ext.getCmp("add").enable();
											}
									  }
									});
									var resultArray = Ext.util.JSON.decode(response.responseText);
									if(resultArray.success){
										Ext.Msg.show({title:'提示',msg:'关联解除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
									}else{
										Ext.Msg.show({title:'提示',msg:'关联解除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
									}
									
									// hiddenWaitMsg();
								},
								failure : function(response) {
//											var resultArray = Ext.util.JSON.decode(response.responseText);
//											Ext.Msg.show({title:'提示',msg:'关联解除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
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