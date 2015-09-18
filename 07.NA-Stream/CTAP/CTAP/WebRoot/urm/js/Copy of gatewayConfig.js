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
	    url: './GetewayConfig.ered?reqCode=queryGatewayConfigList',
	    waitTitle : '提示',
		method : 'POST',
		waitMsg : '正在加载数据,请稍候...',
	    success: function(response) {
		
			var root = new Ext.tree.TreeNode({
						text : '网关配置',
						expanded : true,
						id : '0'
					});
			
	
			var access1 = new Ext.tree.TreeNode({
				expanded:true,
				iconCls : 'imageIcon',
				id : '1',
				leaf : true,
				text:'接入网关1(192.168.100.11:8080)'
			});
//			var offLined = new Ext.tree.TreeNode({
//				expanded:true,
//				iconCls : 'server_offlineIcon',
//				text:'已下架'
//			});
			var access2 = new Ext.tree.TreeNode({
				expanded:true,
				id : '2',
				leaf : true,
				iconCls : 'imageIcon',
				text:'接入网关2(192.168.100.12:8181)'
			});
			var access3 = new Ext.tree.TreeNode({
				expanded:true,
				id : '3',
				leaf : true,
				iconCls : 'imageIcon',
				text:'接入网关3(192.168.100.13:8080)'
			});
			root.appendChild(access1);
			root.appendChild(access2);
			root.appendChild(access3);
			var node = new Array();
			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {				
	
				
				
				var file1 = new Ext.tree.TreeNode({
					expanded:true,
				//	id : groupJson.ROOT[i].server_id,
					id : 1,
					leaf : true,
				//	iconCls : 'imageIcon',
					text:'配置文件1'
				});
				var file3 = new Ext.tree.TreeNode({
					expanded:true,
					id : 2,
					leaf : true,
				//	iconCls : 'imageIcon',
					text:'配置文件2'
				});
				
				var file4 = new Ext.tree.TreeNode({
					expanded:true,
					id : 3,
					leaf : true,
				//	iconCls : 'imageIcon',
					text:'配置文件3'
				});
				
				var file2 = new Ext.tree.TreeNode({
					expanded:true,
				//	iconCls : 'imageIcon',
					leaf : true,
					id : 4,
					text:'配置文件3'
				});
				
				node[i] = new Ext.tree.TreeNode({
				id : i,
				text : '信令网关(192.168.100.13:8080)',
			//	leaf : true,
				iconCls : 'imageIcon'
			});
			
			
				
				node[i].appendChild(file1);
				node[i].appendChild(file3);
				node[i].appendChild(file4);
				
			//	node[i+1].appendChild(file2);
				
				
			//	access1.appendChild(node[i]);
			//	access2.appendChild(node[i]);
			//	access3.appendChild(node[i]);
				
				
				if(groupJson.ROOT[i].field3=='1'){//在运行
					access2.appendChild(node[i]);
				
				}else if(groupJson.ROOT[i].field3=='2'){//未认证
					//access2.appendChild(node[i+1]);
					access2.appendChild(node[i]);
				}else if(groupJson.ROOT[i].field3=='3'){//预下架
					access3.appendChild(node[i]);
				//	access2.appendChild(node[i+1]);
				}else{//已下架   unused
					//offLined.appendChild(node[i]);
					access2.appendChild(node[i]);
				
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
					Ext.getCmp('access1').hide();
					Ext.getCmp('authen').hide();
					Ext.getCmp('peraccess1').hide();
					Ext.getCmp('updateConfig').disable();
				}else if(node.parentNode.text=='在运行'){
//					Ext.getCmp('online').hide();
//					Ext.getCmp('access1').show();
//					Ext.getCmp('peraccess1').show();
				// 	Ext.getCmp('authen').show();
			//  	Ext.getCmp('authen').disable();
					Ext.getCmp('updateConfig').enable();
				}else if(node.parentNode.text=='未认证'){
				//	Ext.getCmp('online').hide();
				//	Ext.getCmp('access1').hide();
				//	Ext.getCmp('authen').enable();
				//	Ext.getCmp('authen').enable();
			   // 	Ext.getCmp('authen').show();
					Ext.getCmp('updateConfig').disable();
				}else{
				//	Ext.getCmp('online').show();
				//	Ext.getCmp('access1').hide();
				//	Ext.getCmp('authen').hide();
				//	Ext.getCmp('peraccess1').hide();
				//	Ext.getCmp('updateConfig').disable();
					Ext.getCmp('updateConfig').enable();
				}
				myForm.form.load({
					
					waitMsg : '正在处理数据,请稍候...',// 提示信息
					waitTitle : '提示',// 标题
					url : './GetewayConfig.ered?reqCode=queryGatewayConfig&nodeName='+"192.168.100.11:1000" ,//获取单个服务器信息
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
															fieldLabel : '监听终端接入端口', // 标签...本服务监听终端接入的端口
															name : 'field2', // name:后台根据此name属性取值
															allowBlank : true,
															readOnly:true,
															emptyText : '本服务监听终端接入的端口',
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'// 宽度百分比

														}
														,{
															fieldLabel : '监听交互网关端口',
															name : 'field3',
															id:'total_num',
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '90%',
															readOnly:true,
															labelStyle : 'color:blue;'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														,{
															fieldLabel : '看门狗的IP',
															name : 'field4',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '90%',
															readOnly:true,
															labelStyle : 'color:blue;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}	,{
															fieldLabel : '处理线程数',
															name : 'field4',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															emptyText : '终端接入的处理线程数',
															anchor : '90%',
															readOnly:true,
															labelStyle : 'color:blue;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '看门狗PORT',
															name : 'field5',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
														//	id:'total_num',
															readOnly:true,
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															allowBlank : true,
															anchor : '90%'
														}
												,{
															fieldLabel : '超时时间(ms)', // 标签        交互网关在接入网关的
															name : 'field6', // name:后台根据此name属性取值
															allowBlank : true,
															//readOnly:true,
															readOnly:true,
															emptyText : '交互网关在接入网关的超时时间(ms)',
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'// 宽度百分比

														}
														
														
														]
											},{
												columnWidth : .40,
												layout : 'form',
												labelWidth : 110, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{//的ip和监听的端口(可以有多个)，达到多机负载
															fieldLabel : '其他接入网关',
															name : 'field9',
															//readOnly:true,
														//	labelStyle : 'color:blue;',
															emptyText : '其他接入网关的ip和监听的端口(可以有多个)，达到多机负载',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															xtype : 'textfield', // 设置为文字输入框类型
															anchor : '100%'
														}
												,{
															fieldLabel : '日志路径',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															readOnly:true,
															anchor : '100%',//cag.log.path
															labelStyle : 'color:blue;'
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
														},{
															fieldLabel : '日志文件名称',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;'
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '日志文件大小',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														,{
															fieldLabel : '最大日志文件数',
															name : 'field10',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '日志级别',
															name : 'field7',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;',
															emptyText : ''
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '监听交互网关心跳',
															name : 'field8',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															readOnly:true,
															labelStyle : 'color:blue;',
															emptyText : '监听其他接入网关转发的交互网关心跳'
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
					url : './AccessGetewayConfig.ered?reqCode=updateAccessGatewayConfig',//调用crsm接口，保存服务器配置
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
			

			
			
			
			// 定义自动当前页行号
			var rownum = new Ext.grid.RowNumberer({
						header : 'NO',
						width : 28
					});
			var sm = new Ext.grid.CheckboxSelectionModel();
			// 定义列模型
			var cm = new Ext.grid.ColumnModel([rownum,sm, 
//			     {
//				header : 'ID', // 列标题
//				dataIndex : 'id', // 数据索引:和Store模型对应
//				sortable : true
//					// 是否可排序
//				},
				{
				header : 'key含义',
				dataIndex : 'key_mean',
				sortable : true,
				editor : new Ext.grid.GridEditor(new Ext.form.TextField({
							// 只对原有数据编辑有效,对新增一行的场景无效
							allowBlank : false
						})),
				width : 200
			}, {
				header : 'key',
				dataIndex : 'key_name',
				editor : new Ext.grid.GridEditor(new Ext.form.TextField({}))
			}, {
				header : 'value',
				dataIndex : 'key_value',
				editor : new Ext.grid.GridEditor(new Ext.form.TextField({}))
			}
//			, {
//				header : '单位',
//				dataIndex : 'dw',
//				editor : new Ext.grid.GridEditor(new Ext.form.TextField({})),
//				width : 60
//			}, {
//				header : '启用状态',
//				dataIndex : 'qybz',
//				// 演示render的用法(代码转换,该render由<G4Studio:ext.codeRender/>标签生成)
//				renderer : QYBZRender,
//				editor : new Ext.grid.GridEditor(new Ext.form.ComboBox({
//							store : QYBZStore,
//							mode : 'local',
//							triggerAction : 'all',
//							valueField : 'value',
//							displayField : 'text',
//							allowBlank : false,
//							forceSelection : true,
//							typeAhead : true
//						})),
//				width : 80
//			}, {
//				header : '更改时间',
//				dataIndex : 'ggsj',
//				renderer : Ext.util.Format.dateRenderer('Y-m-d'),
//				editor : new Ext.grid.GridEditor(new Ext.form.DateField({
//							format : 'Y-m-d'
//						})),
//				width : 140
//			}
//			
			
			]);

			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
						// true to clear all modified record information each
						// time the store is loaded
						pruneModifiedRecords : true,
						// 获取数据的方式./ServiceUrlDistribution.ered?reqCode=queryAds
						//url : './SignalingGatewayDistribution.ered?reqCode=queryAds'
						proxy : new Ext.data.HttpProxy({
									url : './KeyConfig.ered?reqCode=queryKeys'
								}),
						// 数据读取器
						reader : new Ext.data.JsonReader({
									totalProperty : 'TOTALCOUNT', // 记录总数
									root : 'ROOT' // Json中的列表数据根节点
								}, [{
											name : 'key_mean' // Json中的属性Key值
										}, {
											name : 'key_name'
										}, {
											name : 'key_value'
										}, {
											name : 'id'
										}, {
											name : 'key_name'
										}, {
											name : 'key_name'
										}, {
											name : 'qybz'
										}, {
											name : 'ggsj',
											type : 'date',
											dateFormat : 'Y-m-d'
										}])
					});

			// 定义一个Record
			var MyRecord = Ext.data.Record.create([{
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
					}]);

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
			var number = parseInt(pagesize_combo.getValue());
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

			// 表格工具栏
			var tbar = new Ext.Toolbar({
						items : [{
									xtype : 'textfield',
									id : 'xmmc',
									name : 'key_mean',
									emptyText : '键值含义',
									width : 150,
									enableKeyEvents : true,
									// 响应回车键
									listeners : {
										specialkey : function(field, e) {
											if (e.getKey() == Ext.EventObject.ENTER) {
												queryCatalogItem();
											}
										}
									}
								}, 
//								{
//									text : '查询',
//									iconCls : 'page_findIcon',
//									handler : function() {
//										queryCatalogItem();
//									}
//								}, 
								
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
								}, '-', {
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
//										var sm1 = grid.getSelectionModel();
//										var cell = sm1.getSelectedCell();
//										if (Ext.isEmpty(cell)) {
//											Ext.Msg.alert('提示', '你没有选中行');
//										}
//										var record = store.getAt(cell[0]);
//										Ext.MessageBox.alert('提示', '项目ID:' + record.get('xmid'))
//										store.remove(record);
									}
								}]
					});

			// 表格实例
			var grid = new Ext.grid.EditorGridPanel({
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
						sm:sm,
						tbar : tbar, // 表格工具栏
						bbar : bbar,// 分页工具栏
						clicksToEdit : 1, // 单击、双击进入编辑状态
						viewConfig : {
							// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
							forceFit : true
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
			// store.load({params : {start : 0,limit : bbar.pageSize}});

			// 布局模型
//			var viewport = new Ext.Viewport({
//						layout : 'border',
//						items : [grid]
//					});

			// 查询表格数据
			function queryCatalogItem() {
				store.load({
							params : {
								start : 0,
								limit : bbar.pageSize,
								key_mean : Ext.getCmp('xmmc').getValue()
							}
						});
			}

			function resetQueryCatalogItem() {
				
				store.load({
							params : {
								start : 0,
								limit : bbar.pageSize
								
							}
						});
				Ext.getCmp('xmmc').setValue()="";
			}
 			
		        var field1 = Ext.get('xmmc');  	  
//		         field1.on('keyup', function(e) {	    
//		         queryCatalogItem()		          
//		         });  
 
		         
			// 保存
			function saveRow() {}

			/**
			 * 删除代码对照
			 */
			function deleteCodeItems() {}
			
			
			// 检查新增行的可编辑单元格数据合法性
			function validateEditGrid(m, colName) {
				for (var i = 0; i < m.length; i++) {
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
		queryCatalogItem();
			
	/**************E   右侧详情生成结束   E*****************/

			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									title : '<span>服务器标识</span>',
									iconCls : 'layout_contentIcon',
									collapsible : true,
									width : 250,
									minSize : 160,
									maxSize : 280,
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
		},
	    failure: function(response) {
	    }
	});

});
	