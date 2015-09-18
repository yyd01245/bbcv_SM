/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {

			Ext.Msg.minWidth = 300;
			Ext.Msg.maxWidth = 360;
			
			
			var addr,appname;

			/** ************S 左侧选择树生成开始 S**************** */
			var treeBar = new Ext.Toolbar( {
				buttonAlign : 'center',
				items : [ {
					xtype : 'textfield',
					emptyText : '服务器IP过滤...',
					id : 'filter_input',
					width : 180
				} ]
			});
			Ext.Ajax.request( {
						url : './GetewayConfig.ered?reqCode=queryCagGatewayInfoList',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在加载数据,请稍候...',
						success : function(response) {

							var root = new Ext.tree.TreeNode( {
								text : '网关配置',
								expanded : true,
								id : '0'
							});

							var node = new Array();

							var groupJson = eval('(' + response.responseText + ')');
							var total = groupJson.TOTALCOUNT;
							for ( var i = 0; i < total; i++) {(function(){
								var k = i;
								node[k] = new Ext.tree.TreeNode( {
									id : groupJson.ROOT[k].cag_id,
									text : "接入网关"+"("+groupJson.ROOT[k].cag_ip+":"+groupJson.ROOT[k].cag_port+")",
									// leaf : true,
									iconCls : 'imageIcon'
								});

							
//								var timestamp_cag1=new Date().getTime();
//								var cag_config = new Ext.tree.TreeNode( {
//									id : k+timestamp_cag1,
//									text : '本网关配置',
//									iconCls : 'collapse-allIcon'
//									
//								});
								
								
								Ext.Ajax.request({

									url : './GetewayConfig.ered?reqCode=queryGatewayConfigFileList&config_ip='+groupJson.ROOT[k].cag_ip+"&config_port="+groupJson.ROOT[k].cag_port+"&config_name=cag",
									waitTitle : '提示',
									method : 'POST',
									waitMsg : '正在加载数据,请稍候...',
									success: function(response) {
										var node_cag = new Array();
										var groupJson_cag_config = eval('('+response.responseText+')'); 
										var total_cag = groupJson_cag_config.TOTALCOUNT;
										var timestamp_cag=new Date().getTime();
										for(var j1=0;j1<total_cag;j1++) {
											node_cag[j1] = new Ext.tree.TreeNode({
												id : groupJson_cag_config.ROOT[j1].id+"0"+timestamp_cag+Math.ceil(Math.random()*90)+Math.ceil(Math.random()*10),
												
												text : "配置文件"+"("+groupJson_cag_config.ROOT[j1].config_name+")",
												//text : "配置文件"+"("+groupJson_ckg_config.ROOT[jjj].server_ip+":"+groupJson_ckg_config.ROOT[jjj].server_port+")",
												//leaf : false,
												iconCls : 'collapse-allIcon'
											});
											
											node[k].appendChild(node_cag[j1]);
										}	
									}
								});	
								
								//node[k].appendChild(cag_config);
							
								
								var timestamp_cag2=new Date().getTime();
								var ckg_config = new Ext.tree.TreeNode( {
									id : k+k+timestamp_cag2,
									text : '键值网关配置',
									iconCls : 'application_doubleIcon'
									
								});
								
								
								Ext.Ajax.request({

									url : './GetewayConfig.ered?reqCode=queryCkgGatewayInfoList&cag_id='+node[i].id,
									waitTitle : '提示',
									method : 'POST',
									waitMsg : '正在加载数据,请稍候...',
									success: function(response1) {
										var nodee = new Array();
										var groupJson1 = eval('('+response1.responseText+')'); 
										var total1 = groupJson1.TOTALCOUNT;
										var timestamp1=new Date().getTime();
										for(var j=0;j<total1;j++) {(function(){
											
											var k_ckg = j;
											nodee[j] = new Ext.tree.TreeNode({
												id : groupJson1.ROOT[j].server_port+timestamp1+Math.ceil(Math.random()*90+Math.ceil(Math.random()*10)),
												text : "键值网关"+"("+groupJson1.ROOT[j].ckg_ip+":"+groupJson1.ROOT[j].ckg_port+")",
											//	leaf : false,
												iconCls : 'imageIcon'
											});
											ckg_config.appendChild(nodee[j]);
											//node[k].appendChild(nodee[j]);
											
											
										
											
											Ext.Ajax.request({

												url : './GetewayConfig.ered?reqCode=queryGatewayConfigFileList&config_ip='+groupJson1.ROOT[j].ckg_ip+"&config_port="+groupJson1.ROOT[j].ckg_port+"&config_name=ckg",
												waitTitle : '提示',
												method : 'POST',
												waitMsg : '正在加载数据,请稍候...',
												success: function(response3) {
													var node_ckg = new Array();
													var groupJson_ckg_config = eval('('+response3.responseText+')'); 
													var total2 = groupJson_ckg_config.TOTALCOUNT;
													var timestamp2=new Date().getTime();
													for(var jjj=0;jjj<total2;jjj++) {
														node_ckg[jjj] = new Ext.tree.TreeNode({
															id : groupJson_ckg_config.ROOT[jjj].id+"1"+timestamp2,
															
															text : "配置文件"+"("+groupJson_ckg_config.ROOT[jjj].config_name+")",
															//text : "配置文件"+"("+groupJson_ckg_config.ROOT[jjj].server_ip+":"+groupJson_ckg_config.ROOT[jjj].server_port+")",
															//leaf : false,
															iconCls : 'collapse-allIcon'
														});
														
														nodee[k_ckg].appendChild(node_ckg[jjj]);
													}	
												}
											});	
											
											
											
											
										})();	
										}	
									}
								});	
						
								node[k].appendChild(ckg_config);
								
								
								var timestamp_cag3=new Date().getTime();
								var csg_config = new Ext.tree.TreeNode( {
									id : k+k+timestamp_cag3,
									text : '信令网关配置',
									iconCls : 'application_doubleIcon'
									
								});
								
								Ext.Ajax.request({

									url : './GetewayConfig.ered?reqCode=queryCsgGatewayInfoList&cag_id='+node[i].id,
									waitTitle : '提示',
									method : 'POST',
									waitMsg : '正在加载数据,请稍候...',
									success: function(response2) {
										var nodeee = new Array();
										var groupJson2 = eval('('+response2.responseText+')'); 
										var total2 = groupJson2.TOTALCOUNT;
										var timestamp3=new Date().getTime();
										for(var jj=0;jj<total2;jj++) {(function(){
											
											var k_csg = jj;
											nodeee[jj] = new Ext.tree.TreeNode({
												id : groupJson2.ROOT[jj].server_port+""+timestamp3+Math.ceil(Math.random()*90+Math.ceil(Math.random()*10)),
												text : "信令网关"+"("+groupJson2.ROOT[jj].csg_ip+":"+groupJson2.ROOT[jj].csg_port+")",
												//leaf : false,
												iconCls : 'imageIcon'
											});
											csg_config.appendChild(nodeee[jj]);
										//	node[k].appendChild(nodeee[jj]);
											
											
											Ext.Ajax.request({

												url : './GetewayConfig.ered?reqCode=queryGatewayConfigFileList&config_ip='+groupJson2.ROOT[jj].csg_ip+"&config_port="+groupJson2.ROOT[jj].csg_port+"&config_name=csg",
												waitTitle : '提示',
												method : 'POST',
												waitMsg : '正在加载数据,请稍候...',
												success: function(response4) {
													var node_csg = new Array();
													var groupJson_csg_config = eval('('+response4.responseText+')'); 
													var total2 = groupJson_csg_config.TOTALCOUNT;
													var timestamp4=new Date().getTime();
													for(var jjjj=0;jjjj<total2;jjjj++) {
														node_csg[jjjj] = new Ext.tree.TreeNode({
															id : groupJson_csg_config.ROOT[jjjj].id+"2"+timestamp4,
															//text : "配置文件"+"("+groupJson_csg_config.ROOT[jjjj].config_name+")",
															text : "配置文件"+"("+groupJson_csg_config.ROOT[jjjj].config_name+")",
															//leaf : false,
															iconCls : 'collapse-allIcon'
														});
														
														nodeee[k_csg].appendChild(node_csg[jjjj]);
													}	
												}
											});	
											
										})();	
										}	
									}
								});	
							
								node[k].appendChild(csg_config);
								root.appendChild(node[i]);		

							})();
							}
							

							var menuTree = new Ext.tree.TreePanel( {
								root : root,
								rootVisible : false,
								title : '',
								tbar : treeBar,
								applyTo : 'menuTreeDiv',
								autoScroll : false,
								animate : false,
								useArrows : false,
								border : false
							});
							var Node;
							var nodeName;
							var nodeId;
							menuTree.on('click',function(node) {
								
								if(!node.hasChildNodes()) {	
					
                                 
                                  var fdStart = node.text.indexOf("配置文件");
                                  if(fdStart == 0){
                                	  
                                	var ip=   node.parentNode.text.split("(")[1].split(")")[0].split(":")[0];
                                	var port=   node.parentNode.text.split("(")[1].split(")")[0].split(":")[1];
                                	var config_name =   node.text.split("(")[1].split(")")[0];
                                	  
                                	
                                	addr = ip+":"+port;
                                	appname = config_name;
                                //	alert(ip+"  "+port+" "+config_name);
                              		store.proxy = new Ext.data.HttpProxy({url:'./KeyConfig.ered?reqCode=queryKeys&config_ip='+ip+"&config_name="+config_name+"&config_port="+port});
									store.load({
										params:{
											start:0,
											limit:bbar.pageSize
									   }
								    });
                                	}else if(fdStart == -1){
                                	  
                                	}            							
								}else{
									
//									var fdStart1=	 node.text.indexOf("接入网关");
//									  if(fdStart1 == 0){
//										 
//									  }
//									 
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
											var re = new RegExp(Ext
													.escapeRe(text), 'i');
											filter.filterBy(function(n) {
												// 只过滤叶子节点，这样省去枝干被过滤的时候，底下的叶子都无法显示
													return !n.isLeaf()
															|| re.test(n.text);
												});

											// 如果这个节点不是叶子，而且下面没有子节点，就应该隐藏掉
											hiddenPkgs = [];
											menuTree.root
													.cascade(function(n) {
														if (!n.isLeaf()
																&& n.ui.ctNode.offsetHeight < 3) {
															n.ui.hide();
															hiddenPkgs.push(n);
														}
													});
										});
							// menuTree.root.select();
							/** ************E 左侧选择树生成结束 E**************** */
							/** ************S 右侧详情生成开始 S**************** */
							var myForm = new Ext.form.FormPanel(
									{});


							/** **************E function E******************* */

							// 定义自动当前页行号
							var rownum = new Ext.grid.RowNumberer( {
								header : 'NO',
								width : 28
							});
							var sm = new Ext.grid.CheckboxSelectionModel();
							// 定义列模型
							var boolean = false;
							function statusRender(value){
								if(value == 1){
									return "<span style= 'color:red;font-weight:bold;'>否</span>";
								}else if(value == 0){
									boolean = true;
									return "是";
								}
								else
									return value;
							}
							
							function editRender(boolean){
								alert(boolean);
								if(boolean){
									return new Ext.grid.GridEditor(new Ext.form.TextField({
										// 只对原有数据编辑有效,对新增一行的场景无效
										allowBlank : false
									}));
								}else {
									return null;
								}
									
							}
							var sm = new Ext.grid.CheckboxSelectionModel();
							// 定义列模型
							var cm = new Ext.grid.ColumnModel([rownum,sm, {
								header : 'ID', // 列标题
								dataIndex : 'id', // 数据索引:和Store模型对应
								sortable : true
									// 是否可排序
								}, {
								header : '键值含义',
								dataIndex : 'key_mean',
								sortable : true,
//								editor : new Ext.grid.GridEditor(new Ext.form.TextField({
//											// 只对原有数据编辑有效,对新增一行的场景无效
//											allowBlank : false
//										})),
								editor : editRender,
								width : 200
							}, {
								header : '键值名称',
								dataIndex : 'key_name',
//								editor : new Ext.grid.GridEditor(new Ext.form.TextField({}))
								editor : editRender
							}, {
								header : '键值',
								dataIndex : 'key_value',
//								editor : new Ext.grid.GridEditor(new Ext.form.TextField({}))
								editor : editRender
							}, {
								header : '可编辑',
								dataIndex : 'state',
								sortable : true,
								renderer:statusRender,
								width : 200
							}
							
							]);

							/**
							 * 数据存储
							 */
							var store = new Ext.data.Store( {
								
								pruneModifiedRecords : true,
						
								proxy : new Ext.data.HttpProxy( {
									url : './KeyConfig.ered?reqCode=queryKeys'
								}),
								// 数据读取器
								reader : new Ext.data.JsonReader( {
									totalProperty : 'TOTALCOUNT', // 记录总数
									root : 'ROOT' // Json中的列表数据根节点
								}, [ {
									name : 'key_mean' // Json中的属性Key值
									}, {
										name : 'key_name'
									}, {
										name : 'key_value'
									},{
										name : 'state'
									}, {
										name : 'id'
									} ])
							});

							// 定义一个Record
							var MyRecord = Ext.data.Record.create( [ {
								name : 'id',
								type : 'string'
							}, {
								name : 'key_name',
								type : 'string'
							}, {
								name : 'key_mean',
								type : 'string'
							}, {
								name : 'key_value',
								type : 'string'
							}, {
								name : 'gg',
								type : 'string'
							}, {
								name : 'dw',
								type : 'string'
							}, {
								name : 'qybz',
								type : 'string'
							}, {
								name : 'ggsj',
								type : 'data'
							} ]);

							/**
							 * 翻页排序时候的参数传递
							 */
							// 翻页排序时带上查询条件
							store.on('beforeload', function() {
								this.baseParams = {
									xmmc : Ext.getCmp('xmmc').getValue()
								};
							});
							// 每页显示条数下拉选择框
							var pagesize_combo = new Ext.form.ComboBox( {
								name : 'pagesize',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore( {
									fields : [ 'value', 'text' ],
									data : [ [ 10, '10条/页' ], [ 20, '20条/页' ],
											[ 50, '50条/页' ], [ 100, '100条/页' ],
											[ 250, '250条/页' ],
											[ 500, '500条/页' ] ]
								}),
								valueField : 'value',
								displayField : 'text',
								value : '20',
								editable : false,
								width : 85
							});
							var number = parseInt(pagesize_combo.getValue());
							// 改变每页显示条数reload数据
							pagesize_combo.on("select", function(comboBox) {
								bbar.pageSize = parseInt(comboBox.getValue());
								number = parseInt(comboBox.getValue());
								store.reload( {
									params : {
										start : 0,
										limit : bbar.pageSize
									}
								});
							});

							// 分页工具栏
							var bbar = new Ext.PagingToolbar( {
								pageSize : number,
								store : store,
								displayInfo : true,
								displayMsg : '显示{0}条到{1}条,共{2}条',
								plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
								emptyMsg : "没有符合条件的记录",
								items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
							});

							// 表格工具栏
							var tbar = new Ext.Toolbar(
									{
										items : [
												{
													xtype : 'textfield',
													id : 'xmmc',
													name : 'key_mean',
													emptyText : '键值含义',
													width : 150,
													enableKeyEvents : true,
													// 响应回车键
													listeners : {
														specialkey : function(
																field, e) {
															if (e.getKey() == Ext.EventObject.ENTER) {
																queryCatalogItem();
															}
														}
													}
												},
												// {
												// text : '查询',
												// iconCls : 'page_findIcon',
												// handler : function() {
												// queryCatalogItem();
												// }
												// },

												{
													text : '清空',
													iconCls : 'page_findIcon',
													handler : function() {
														resetQueryCatalogItem();
													}
												},
												{
													text : '刷新',
													iconCls : 'page_refreshIcon',
													handler : function() {
														store.reload();
													}
												},
												'-',
												{
													text : '新增一行',
													iconCls : 'addIcon',
													handler : function() {
														var row = new MyRecord({});
														row.set('qybz', '1'); // 赋初值
														grid.stopEditing();
														store.insert(0, row);
														grid.startEditing(0, 2);
													}
												}, {
													text : '保存',
													iconCls : 'acceptIcon',
													handler : function() {
														saveRow();
													}
												}, {
													text : '删除一行',
													iconCls : 'deleteIcon',
													handler : function() {
														deleteCodeItems();
												}
												} ]
									});

							// 表格实例
							var grid = new Ext.grid.EditorGridPanel(
									{
										// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
										title : '<span class="commoncss">键值映射列表</span>',
										height : 500,
										autoScroll : true,
										frame : true,
										region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
										margins : '3 3 3 3',
										store : store, // 数据存储
										stripeRows : true, // 斑马线
										cm : cm, // 列模型
										sm : sm,
										tbar : tbar, // 表格工具栏
										bbar : bbar,// 分页工具栏
										clicksToEdit : 1, // 单击、双击进入编辑状态
										viewConfig : {
											// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
										forceFit : true
									},
									listeners : {
										beforeedit : function(obj) {
//											var row = obj.row;
											var selectRecord = obj.record;
											var status = selectRecord.get('state');
											if(status=='1'){
												return false;
											}
										}
									},
									loadMask : {
										msg : '正在加载表格数据,请稍等...'
									}
									});

							// 是否默认选中第一行数据
							bbar.on("change", function() {
								// grid.getSelectionModel().selectFirstRow();

								});

							// 页面初始自动查询数据
							// store.load({params : {start : 0,limit :
							// bbar.pageSize}});

							// 布局模型
							// var viewport = new Ext.Viewport({
							// layout : 'border',
							// items : [grid]
							// });

							// 查询表格数据
							function queryCatalogItem() {
								store.load( {
									params : {
										start : 0,
										limit : bbar.pageSize,
										key_mean : Ext.getCmp('xmmc')
												.getValue()
									}
								});
							}

							function resetQueryCatalogItem() {

								store.load( {
									params : {
										start : 0,
										limit : bbar.pageSize

									}
								});
								Ext.getCmp('xmmc').setValue() = "";
							}

							var field1 = Ext.get('xmmc');
							// field1.on('keyup', function(e) {
							// queryCatalogItem()
							// });

							// 保存
							function saveRow() {
   
							
								var m = store.modified.slice(0); // 获取修改过的record数组对象
								if (Ext.isEmpty(m)) {
									Ext.MessageBox.alert('提示', '没有数据需要保存!');
									return;
								}
								if (!validateEditGrid(m, 'key_mean')) {
									Ext.Msg.alert('提示', '键值含义', function() {
												grid.startEditing(0, 2);
											});
									return;
								}
								var jsonArray = [];
								// 将record数组对象转换为简单Json数组对象
								Ext.each(m, function(item) {
											jsonArray.push(item.data);
										});
								// 提交到后台处理
								Ext.Ajax.request({
											url : './KeyConfig.ered?reqCode=saveOrUpadateKey&addr='+addr+'&appname='+appname,
											success : function(response) { // 回调函数有1个参数
												var resultArray = Ext.util.JSON.decode(response.responseText);
												Ext.Msg.show({title:'提示',msg:'数据已成功提交至后台',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
												store.reload();
											
												//grid.reconfigure(store, cm); 
											},
											failure : function(response) {
												Ext.Msg.show({title:'提示',msg:'数据提交失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
											},
											params : {
												// 系列化为Json资料格式传入后台处理
												dirtydata : Ext.encode(jsonArray)
											}
										});
							
							}

							/**
							 * 删除代码对照
							 */
							function deleteCodeItems() {

								if (runMode == '0') {
									Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
									return;
								}
								var rows = grid.getSelectionModel().getSelections();
								
								var fields = '';
								var checked = '';
								for (var i = 0; i < rows.length; i++) {
									if (rows[i].get('editmode') == '0') {
										fields = fields + rows[i].get('id') + '<br>';
									}
									checked+=rows[i].get('id')+",";
								}
							
								if (Ext.isEmpty(rows)) {
									Ext.Msg.show({title:'提示',msg:'请先选中要删除 的信息!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
									return;
								}
								var id = jsArray2JsString(rows,'id');
								//alert("strChecked="+strChecked);
								Ext.Msg.confirm('请确认', '你真的要删除此条信息吗?', function(btn, text) {
									if (btn == 'yes') {
										Ext.Ajax.request({
											url : './KeyConfig.ered?reqCode=deleteKeyConfigs',
											success : function(response) {
													Ext.Msg.show({title:'提示',msg:'删除成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
													store.reload();
											},
											failure : function(response) {
												Ext.Msg.show({title:'提示',msg:'删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
											},
											params : {
												strChecked : checked
											}
										});
									}
								});

											
							

							}

							// 检查新增行的可编辑单元格数据合法性
							function validateEditGrid(m, colName) {
								for ( var i = 0; i < m.length; i++) {
									var record = m[i];
									var rowIndex = store.indexOfId(record.id);
									var value = record.get(colName);
									if (Ext.isEmpty(value)) {
										// Ext.Msg.alert('提示', '数据校验不合法');
										return false;
									}
									var colIndex = cm.findColumnIndex(colName);
									var editor = cm.getCellEditor(colIndex).field;
									if (!editor.validateValue(value)) {
										// Ext.Msg.alert('提示', '数据校验不合法');
										return false;
									}
								}
								return true;
							}
						//	queryCatalogItem();

							/** ************E 右侧详情生成结束 E**************** */

							var viewport = new Ext.Viewport( {
								layout : 'border',
								items : [ {
									title : '<span>服务器标识</span>',
									iconCls : 'layout_contentIcon',
									collapsible : true,
									width : 250,
									minSize : 160,
									maxSize : 280,
									split : true,
									region : 'west',
									autoScroll : true,
									items : [ menuTree ]
								}, {
									region : 'center',
									layout : 'border',
									items : [ grid ]
								} ]
							});
						},
						failure : function(response) {
						}
					});

		});
