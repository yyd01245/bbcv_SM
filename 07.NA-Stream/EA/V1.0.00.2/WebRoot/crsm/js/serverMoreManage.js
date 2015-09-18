/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {
	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;
		
	/**************S   左侧选择树生成开始   S*****************/
			var root = new Ext.tree.TreeNode({
						text : '承载资源管理',
						expanded : true,
						id : '0'
					});
			var offLine = new Ext.tree.TreeNode({
				expanded:true,
				id:'register',
				iconCls : 'server_errorIcon',
				text:'未认证'
			});
			var onLine = new Ext.tree.TreeNode({
				expanded:true,
				id:'normal',
				iconCls : 'server_normalIcon',
				text:'在运行'
			});
			var offLined = new Ext.tree.TreeNode({
				expanded:true,
				id:'unused',
				iconCls : 'server_offlineIcon',
				text:'已下架'
			});
			var preOffLine = new Ext.tree.TreeNode({
				expanded:true,
				id:'perunused',
				iconCls : 'exclamationIcon',
				text:'预下架'
			});
			root.appendChild(offLine);
			root.appendChild(onLine);
			root.appendChild(offLined);
			root.appendChild(preOffLine);
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
			menuTree.on('click', function(node) {
				if(node.id=='register'){
					Ext.getCmp('makeSure').show();
					Ext.getCmp('online').hide();
					Ext.getCmp('offline').hide();
					Ext.getCmp('perOffline').hide();
				}else if(node.id=='normal'){
					Ext.getCmp('makeSure').hide();
					Ext.getCmp('online').hide();
					Ext.getCmp('offline').show();
					Ext.getCmp('perOffline').show();
				}else if(node.id=='unused'){
					Ext.getCmp('makeSure').hide();
					Ext.getCmp('online').show();
					Ext.getCmp('offline').hide();
					Ext.getCmp('perOffline').hide();
				}else{
					Ext.getCmp('makeSure').hide();
					Ext.getCmp('online').show();
					Ext.getCmp('offline').hide();
					Ext.getCmp('perOffline').hide();
				}
				store.proxy = new Ext.data.HttpProxy({url:'./crsmServerMonitor.ered?reqCode=queryServerByStatus&status='+node.id});
						store.load();
			});  
			menuTree.root.select();
	/**************E   左侧选择树生成结束   E*****************/
	/**************S   右侧详情生成开始   S*****************/
			/**
			 * 布局
			 */
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
				header : '序号',
				width : 40
			});
			// 定义列模型
			var sm = new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([ rownum, sm, {
				header : '服务器标识',
				dataIndex : 'server_id',
				width : 200,
				sortable : true
			},{
				header : 'cpu信息',
				dataIndex : 'cpu_info'
			},{
				header : '内存信息',
				dataIndex : 'mem_info'
			},{
				header : '支持最大路数',
				dataIndex : 'max_num'
			},{
				header : '当前在线路数',
				dataIndex : 'online_num'
			},{
				header : '当前流化路数',
				dataIndex : 'liuhua_num'
			}]);
			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
				// 获取数据的方式
				proxy : new Ext.data.HttpProxy({
					url : './crsmServerMonitor.ered?reqCode=queryServerByStatus&status=0'
				}),
				// 数据读取器
				reader : new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT', // 记录总数
					root : 'ROOT' // Json中的列表数据根节点
				}, [ {
					name : 'server_id'
				},{
					name : 'server_ip'
				}, {
					name : 'server_port'
				},{
					name : 'vtype'
				},{
					name : 'cpu_info'
				},{
					name : 'mem_info'
				},{
					name : 'max_num'
				},{
					name : 'online_num'
				},{
					name : 'liuhua_num'
				}])
			});
			var grid = new Ext.grid.GridPanel({
				region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
				// collapsible : true,
				// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
				title : '<span style="font-weight:bold;">承载批量管理</span>',
				autoScroll : true,
				frame : true,
				store : store, // 数据存储
				stripeRows : true, // 斑马线
				cm : cm, // 列模型
				sm : sm,
				bbar : [{
					text : '认证',
					iconCls : 'keyIcon',
					id:'makeSure',
					hidden:true,
					handler : function() {
						authFunction();
					}
				},{
					text : '上线',
					iconCls : 'acceptIcon',
					id:'online',
					hidden:true,
					handler : function() {
						onlineFunction();
					}
				},{
					text : '预下线',
					iconCls : 'deleteIcon',
					id:'perOffline',
					hidden:true,
					handler : function() {
						perOfflineFunction();
					}
				},{
					text : '强制下线',
					iconCls : 'server_offlineIcon',
					id:'offline',
					hidden:true,
					handler : function() {
						OfflineFunction();
					}
				},{
					text : '',
					handler : function() {
					}
				}],
				viewConfig : {
					// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
					forceFit : true
				},
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				}
			});
			/**************E   右侧详情生成结束   E*****************/
			
			/**************S      FUNCTION       S*****************/
			function onlineFunction(){//上线操作
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg: '请先选中要上线的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var server_id = jsArray2JsString(rows,'server_id');
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverOnlineOperate&server_ip='+server_id,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器上线事件已触发成功！',button:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
							}else{
								Ext.Msg.show({title:'提示',msg:'部分服务器上线事件触发失败！',button:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					},params : {
						server_id : server_id
					}
				})
			}
			function authFunction(){//激活操作
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg: '请先选中要激活的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var server_id = jsArray2JsString(rows,'server_id');
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverAuthOperate&server_ip='+server_id,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器激活事件已触发成功！',button:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
							}else{
								Ext.Msg.show({title:'提示',msg:'部分服务器激活事件触发失败！',button:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					},params : {
						server_id : server_id
					}
				})
			}
			function OfflineFunction(){//下线线操作
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg: '请先选中要下线的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var server_id = jsArray2JsString(rows,'server_id');
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverOfflineOperate&server_ip='+server_id,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器下线事件已触发成功！',button:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
							}else{
								Ext.Msg.show({title:'提示',msg:'部分服务器下线事件触发失败！',button:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					},params : {
						server_id : server_id
					}
				})
			}
			function perOfflineFunction(){//上线操作
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg: '请先选中要预下线的服务器!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var server_id = jsArray2JsString(rows,'server_id');
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverPerOfflineOperate&server_ip='+server_id,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器预下线事件已触发成功！',button:Ext.Msg.OK,icon:Ext.Msg.INFO});
								store.reload();
							}else{
								Ext.Msg.show({title:'提示',msg:'部分服务器预下线事件触发失败！',button:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					},params : {
						server_id : server_id
					}
				})
			}
			/**************E      FUNCTION       E*****************/
			/**************S   框架生成开始   S*****************/
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									title : '<span>承载状态</span>',
									iconCls : 'layout_contentIcon',
									collapsible : true,
									width : 200,
									minSize : 160,
									maxSize : 220,
									split : true,
									region : 'west',
									autoScroll : true,
									items : [menuTree]
								}, {
									region : 'center',
									layout : 'border',
									items : [grid]
								}]
					});
			/**************S   框架生成结束   S*****************/

});
	