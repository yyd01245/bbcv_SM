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
	var treeBar = new Ext.Toolbar({  
           buttonAlign : 'center',  
           items : [{xtype : 'textfield',emptyText : '服务器IP过滤...',id:'filter_input',width : 180}]  
     });   
	Ext.Ajax.request({
	    url: './KeyGetewayConfig.ered?reqCode=queryGatewayConfigList',
	    success: function(response) {
		
			var root = new Ext.tree.TreeNode({
						text : '键值网关配置',
						expanded : true,
						id : '0'
					});
			var offLine = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'server_errorIcon',
				text:'未认证'
			});
//			var offLined = new Ext.tree.TreeNode({
//				expanded:true,
//				iconCls : 'server_offlineIcon',
//				text:'已下架'
//			});
			var onLine = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'server_normalIcon',
				text:'在运行'
			});
			var preOffLine = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'exclamationIcon',
				text:'离线'
			});
			root.appendChild(offLine);
			root.appendChild(onLine);
		//	root.appendChild(offLined);
			root.appendChild(preOffLine);
			var node = new Array();
			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {
				node[i] = new Ext.tree.TreeNode({
					id : groupJson.ROOT[i].server_id,
					text : groupJson.ROOT[i].server_id,
					leaf : true,
					iconCls : 'imageIcon'
				});
				
			
				if(groupJson.ROOT[i].field3=='1'){//在运行
					onLine.appendChild(node[i]);
				}else if(groupJson.ROOT[i].field3=='2'){//未认证
					offLine.appendChild(node[i]);
				}else if(groupJson.ROOT[i].field3=='3'){//预下架
					preOffLine.appendChild(node[i]);
				}else{//已下架   unused
					//offLined.appendChild(node[i]);
					onLine.appendChild(node[i]);
				}
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
			var nodeName;
			var nodeId;
			menuTree.on('click', function(node) {
				Ext.getCmp('cabinetM').disable();
				Ext.getCmp('updateConfig').disable();
				Node = node;
				nodeName =  node.text;
				nodeId =  node.id;
				if(nodeName!='在运行'&&nodeName!='已下架'&&nodeName!='预下架'&&nodeName!='未认证') {	
					if(node.parentNode.text=='已下架'){
					Ext.getCmp('online').show();
					Ext.getCmp('offline').hide();
					Ext.getCmp('authen').hide();
					Ext.getCmp('perOffline').hide();
					Ext.getCmp('updateConfig').disable();
				}else if(node.parentNode.text=='在运行'){
//					Ext.getCmp('online').hide();
//					Ext.getCmp('offline').show();
//					Ext.getCmp('perOffline').show();
			//	 	Ext.getCmp('authen').show();
			     	Ext.getCmp('authen').hide();
					Ext.getCmp('updateConfig').enable();
				}else if(node.parentNode.text=='未认证'){
				//	Ext.getCmp('online').hide();
				//	Ext.getCmp('offline').hide();
				//	Ext.getCmp('authen').enable();
					Ext.getCmp('authen').enable();
			    	Ext.getCmp('authen').show();
					Ext.getCmp('updateConfig').enable();
				}else{
				//	Ext.getCmp('online').show();
				//	Ext.getCmp('offline').hide();
				//	Ext.getCmp('authen').hide();
				//	Ext.getCmp('perOffline').hide();
				//	Ext.getCmp('updateConfig').disable();
					Ext.getCmp('updateConfig').enable();
				}
				myForm.form.load({
					
					waitMsg : '正在处理数据,请稍候...',// 提示信息
					waitTitle : '提示',// 标题
					url : './KeyGetewayConfig.ered?reqCode=queryGatewayConfig&nodeName='+nodeName ,//获取单个服务器信息
					success : function(form, action) {
						var t = action.result.data;
						if(t.cabinet_id==null){
							Ext.getCmp('cabinetM').enable();
						}
					},
					failure : function(form,action){
						Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
					}
					
				})
//				store.proxy = new Ext.data.HttpProxy({url:'./crsmServerMonitor.ered?reqCode=querySesionDetail&server_ip='+nodeName});
						store.load();
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
	/**************E   左侧选择树生成结束   E*****************/
	/**************S   右侧详情生成开始   S*****************/
			var myForm = new Ext.form.FormPanel({
						region : 'center',
						title : '<span>网关配置信息<span>',
						collapsible : true,
						bodyStyle :'background:#CCDFEA',
						border : true,
						labelWidth : 80, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						items : [{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : .40,
												layout : 'form',
												labelWidth : 110, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
													fieldLabel : 'id', // 标签
													name : 'id', // name:后台根据此name属性取值
													allowBlank : true,
													readOnly:true,
													hidden:true,
																																																																																																																																																																																																																																																																																																																																																										hideLabel:true,
													labelStyle : 'color:blue;',
//													style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;', 
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
													anchor : '90%'// 宽度百分比

												},{
															fieldLabel : '版本号', // 标签
															name : 'field1', // name:后台根据此name属性取值
															allowBlank : true,
															readOnly:true,
															labelStyle : 'color:blue;',
//															style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;', 
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'// 宽度百分比

														},{
															fieldLabel : '服务工作IP', // 标签...本服务监听终端接入的端口
															name : 'field2', // name:后台根据此name属性取值
															allowBlank : true,
															readOnly:true,
															emptyText : '本服务工作IP(与终端交互的IP),处理多网卡情况',
														//	labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'// 宽度百分比

														}
														,{
															fieldLabel : '监听终端UDP端口',
															name : 'field3',
															id:'total_num',
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '90%',
															readOnly:true,
															emptyText : '本服务监听终端的UDP端口',
															labelStyle : 'color:blue;'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														,{
															fieldLabel : 'CDS的IP',
															name : 'field4',
															xtype : 'textfield', // 设置为文字输入框类型
															//labelStyle : 'color:blue;',
														//	readOnly:true,
															anchor : '90%'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : 'CDS端口',
															name : 'field4',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															emptyText : '',
														//	labelStyle : 'color:blue;',
															anchor : '90%'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '接入网关的IP',
															name : 'field5',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
														//	id:'total_num',
														//	labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															allowBlank : true,
															anchor : '90%'
														}
												,{
															fieldLabel : '接入网关端口', // 标签        交互网关在接入网关的
															name : 'field6', // name:后台根据此name属性取值
															allowBlank : true,
															//readOnly:true,
															emptyText : '',
														//	labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'// 宽度百分比

														}
												,{
													fieldLabel : '交互网关的HID键值表配置目录',
													name : 'field3',
													id:'total_num',
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : '交互网关的HID键值表配置目录',
													readOnly:true,
													labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												}	,{
													fieldLabel : '处理线程数',
													name : 'field3',
													id:'total_num',
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : '终端接入的处理线程数',
													readOnly:true,
													labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												}	,{
													fieldLabel : 'redis的ip地址',
													name : 'field3',
													id:'total_num',
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : 'redis server 的ip地址',
												//	labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												}	,{
													fieldLabel : 'redis 的端口',
													name : 'field3',
													id:'total_num',
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : 'redis server 的端口，需和CKG相同'
												//	labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												}	,{
													fieldLabel : 'redis的密码',
													name : 'field3',
													id:'total_num',
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : 'redis server 的密码'
												//	labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												}	,{
													fieldLabel : 'redis连接池数目',
													name : 'field3',
												
													xtype : 'textfield', // 设置为文字输入框类型
													//readOnly:true,
													anchor : '90%',
													emptyText : 'redis 连接池的数目(不能小于50)',
													readOnly:true,
													labelStyle : 'color:blue;'
//													//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
												},{//的ip和监听的端口(可以有多个)，达到多机负载
													fieldLabel : '看门狗的IP',
													name : 'field9',
													//readOnly:true,
													labelStyle : 'color:blue;',
													readOnly:true,
													emptyText : '',
//													//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
													xtype : 'textfield', // 设置为文字输入框类型
													anchor : '90%'
												}
										,{
													fieldLabel : '看门狗的端口',
													name : 'field10',
													xtype : 'textfield', // 设置为文字输入框类型
												//	readOnly:true,
													anchor : '90%',//cag.log.path
													readOnly:true,
													labelStyle : 'color:blue;'
//													//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
													//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
												}]
											},{
												columnWidth : .50,
												layout : 'form',
												labelWidth : 200, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : '按键处理服务端口',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															emptyText : '系统按键处理服务端口',
															labelStyle : 'color:blue;',
															readOnly:true,
															anchor : '100%'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														,{
															fieldLabel : '交互网关向接入网关发送心跳频率',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															labelStyle : 'color:blue;',
															emptyText : '交互网关向接入网关发送心跳频率(单位:ms)',
															readOnly:true,
															anchor : '100%'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '终端向交互网关发送心跳频率',
															name : 'field7',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															
															labelStyle : 'color:blue;',
															readOnly:true,
															emptyText : '终端向交互网关发送心跳频率(单位:s)'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '终端向交互网关发送心跳超时次数',
															name : 'field8',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;',
															emptyText : '终端向交互网关发送心跳超时次数(单位:次)'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '日志文件路径',
															name : 'field3',
															
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
															emptyText : 'redis 连接池的数目(不能小于50)',
															readOnly:true,
															labelStyle : 'color:blue;',
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '日志文件',
															name : 'field3',
															
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '日志文件大小',
															name : 'field3',
														
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;'
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '日志文件最大数目',
															name : 'field3',
															readOnly:true,
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
													
															labelStyle : 'color:blue;'
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '日志级别',
															name : 'field3',
															readOnly:true,
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
														
															labelStyle : 'color:blue;'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '双击时间间隔',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															emptyText : '按键双击时间间隔(单位:ms)',
															labelStyle : 'color:blue;',
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														
														]
											}]
//								},{
//									xtype : 'label',
//									labelAlign : 'right',
//									html : '<font color="red">备注：蓝色为服务器详情信息，绿色为服务器运行信息,黑色为服务器物理信息</font>',
//									anchor : '100%'
								}],
								buttons : [{
									text : '保存配置',
									id:'updateConfig',
									disabled:true,
									iconCls : 'page_edit_1Icon',
									handler : function() {
										modifyConfig(); 
									}
								},{
									text : '创建监控',
									id:'cabinetM',
									hidden :true,
									disabled:true,
									iconCls : 'buildingIcon',
									handler : function() {
										bandingAction();
									}
								},{
								text : '上线',
									iconCls : 'acceptIcon',
									id:'online',
								
									hidden:true,
									handler : function() {
										onlineFunction();
									}
								}, {
									text : '下线',
									id:'offline',
									hidden:true,
									iconCls : 'server_offlineIcon',
									handler : function() {
										offlineFunction();
									}
								}, {
									text : '预下线',
									id:'perOffline',
									hidden:true,
									iconCls : 'exclamationIcon',
									handler : function() {
										perOfflineFunction();
									}
								}, {
									text : '认证',
									id:'authen',
									hidden:true,
									iconCls : 'keyIcon',
									handler : function() {
										authFunction();
									}
								}]
					});
			
			/**
			 * 修改配置弹出框
			 */
			var modifyConfigFormPanel = new Ext.form.FormPanel({
						id : 'modifyConfigFormPanel',
						name : 'modifyConfigFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 110,
						frame : false,
						bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : '服务器标识',
									name : 'server_ip',
									id:'server_ip',
									allowBlank : false,	
									readOnly:true,
									fieldClass : 'x-custom-field-disabled',
									anchor : '99%'
								},new Ext.form.ComboBox({
									name : 'maxnum',
									hiddenName : 'maxnum',
									store : CFGStore,
									mode : 'local',
									triggerAction : 'all',
									valueField : 'text',
									displayField : 'text',
//									value : '1',
									fieldLabel : '修改项',
									emptyText : '请选择...',
									allowBlank : false,
									forceSelection : true,
									editable : false,
									typeAhead : true,
									anchor : '100%'
								}),{
									fieldLabel : '最大路数',
									name : 'max_line',
									id:'max_line',
									allowBlank : false,
									anchor : '99%',
									regex : /^([1-9]\d*)$/,
									regexText : '最大路数必须为数值类型',
									minLength : 1,
									maxLength : 2,
									maxLengthText : '最大路数目前不得超过百'
								}]
					});
			var modifyConfigWindow = new Ext.Window({
						layout : 'fit',
						width : 280,
						height : 150,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>修改服务器配置</span>',
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
						items : [modifyConfigFormPanel],
						buttons : [{
							text : '保存',
							iconCls : 'acceptIcon',
							handler : function() {
								saveConfig();
							}
//						}, {
//							text : '重置',
//							id : 'btnReset',
//							iconCls : 'tbar_synchronizeIcon',
//							handler : function() {
//								clearForm(modifyConfigFormPanel.getForm());
//							}
						}, {
							text : '关闭',
							iconCls : 'deleteIcon',
							handler : function() {
								modifyConfigWindow.hide();
							}
						}]
					});
			
			/**
			 * 绑定机柜弹出框
			 */
//			var cardStore = new Ext.data.Store({});
//
//			var cardCombo = new Ext.form.ComboBox({});
//			
//			
//			var cabinetStore = new Ext.data.Store({});
//			cabinetStore.load();
//			var cabinetCombo = new Ext.form.ComboBox({});
//			var bandingFormPanel = new Ext.form.FormPanel({});
//			var bindingWindow = new Ext.Window({});
			
			/**
			 * 新增机柜弹出框
			 */
//			var addCabinetFormPanel = new Ext.form.FormPanel({});
//			var addCabinetWindow = new Ext.Window({});
			/****************S 修改配置 S********************/
			function modifyConfig(){
				
				if (myForm.form.isValid()) {
					myForm.form.submit({
					url : './KeyGetewayConfig.ered?reqCode=updateAccessGatewayConfig',//调用crsm接口，保存服务器配置
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						
						Ext.Msg.show({title:'提示',msg:'更新配置成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					//	alert("更新成功");
					//	location.reload();
					//	modifyConfigWindow.hide();
//						myForm.form.load({
//							waitMsg : '正在处理数据,请稍候...',// 提示信息
//							waitTitle : '提示',// 标题
//							url : './AccessGetewayConfig.ered?reqCode=queryGatewayConfigList'+nodeName ,//获取单个服务器信息
//		//					params : params,
//							// method : 'GET',// 请求方式
//							success : function(form, action) {
//							Ext.Msg.show({title:'提示',msg:'更新配置成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
//							},
//							failure : function(form,action){
//							}
//						})
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'更新配置失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						modifyConfigWindow.hide();
					}
				});
			}
			//	Ext.Msg.show({title:'提示',msg:'修改成功',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//				modifyConfigFormPanel.getForm().reset();
//				Ext.getCmp('max_line').setValue(Ext.getCmp('total_num').getValue());
//				Ext.getCmp('server_ip').setValue(nodeName);
//				modifyConfigWindow.show();
			}
			function saveConfig(){//保存RSM配置
				if (modifyConfigWindow.getComponent('modifyConfigFormPanel').form.isValid()) {
						modifyConfigWindow.getComponent('modifyConfigFormPanel').form.submit({
						url : './crsmServerMonitor.ered?reqCode=savaConfigById',//调用crsm接口，保存服务器配置
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							Ext.Msg.show({title:'提示',msg:'调用crsm接口，保存服务器配置成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							modifyConfigWindow.hide();
							myForm.form.load({
								waitMsg : '正在处理数据,请稍候...',// 提示信息
								waitTitle : '提示',// 标题
								url : './crsmServerMonitor.ered?reqCode=queryServerSingle&nodeName='+nodeName ,//获取单个服务器信息
			//					params : params,
								// method : 'GET',// 请求方式
								success : function(form, action) {
								},
								failure : function(form,action){
								}
							})
						},
						failure : function(form, action) {
							Ext.Msg.show({title:'提示',msg:'调用crsm接口，保存服务器配置失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							modifyConfigWindow.hide();
						}
					});
				}
			}

			function onlineFunction(){//上线操作
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverOnlineOperate&server_ip='+nodeName,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器上线事件已触发成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                                    if(btnId == 'ok'){
                                       location="javascript:location.reload()";
                                    }}});
							}else{
								Ext.Msg.show({title:'提示',msg:'服务器上线事件触发失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					}
				})
			}
			function offlineFunction(){//下线操作
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverOfflineOperate&server_ip='+nodeName,
				    success: function(response) {
					
					
					 location="javascript:location.reload()";
//						var t= eval('('+response.responseText+')');
//							if(t.success){
//								Ext.Msg.show({title:'提示',msg:'服务器下线事件已触发成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
//                                    if(btnId == 'ok'){
//                                       location="javascript:location.reload()";
//                                    }}});
//							}else{
//								Ext.Msg.show({title:'提示',msg:'服务器下线事件触发失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							}
				    },failure : function(response) {
				    	Ext.Msg.show({title:'提示',msg:'认证失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				})
			}
			
			function perOfflineFunction(){//预下线操作
				Ext.Ajax.request({
				    url: './crsmServerMonitor.ered?reqCode=serverPerOfflineOperate&server_ip='+nodeName,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器预下线事件已触发成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                                    if(btnId == 'ok'){
                                       location="javascript:location.reload()";
                                    }}});
							}else{
								Ext.Msg.show({title:'提示',msg:'服务器预下线事件触发失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					}
				})
			}
			
			function authFunction(){//认证操作
				Ext.Ajax.request({
				    url: './AccessGetewayConfig.ered?reqCode=authAccessGatewayConfig&server_ip='+nodeName,
				    success: function(response) {
					Ext.Msg.show({title:'提示',msg:'认证成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                      if(btnId == 'ok'){
                         location="javascript:location.reload()";
                      }}});
				//	Ext.Msg.show({title:'提示',msg:'认证成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					//location="javascript:location.reload()";
				//	location.reload();
//						var t= eval('('+response.responseText+')');
//							if(t.success){
//								Ext.Msg.show({title:'提示',msg:'认证成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
//                                    if(btnId == 'ok'){
//                                       location="javascript:location.reload()";
//                                    }}});
//								
//							}else{
//								Ext.Msg.show({title:'提示',msg:'认证失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
//							}
				    },failure : function(response) {
				    	Ext.Msg.show({title:'提示',msg:'认证失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				})
			}
			
			function deleteSource(){}
			
			/****************E function E********************/
			
			
	/**************E   右侧详情生成结束   E*****************/

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									title : '<span>服务器标识</span>',
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
									items : [myForm]
								}]
					});
		},
	    failure: function(response) {
	    }
	});

});
	