/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {
	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;
//	Ext.Ajax.request({
//	    url: './crsmServerMonitor.ered?reqCode=queryServerByApiAndInsertDB',
//	    success: function(response) {
//	    }
//	})
	/**************S   左侧选择树生成开始   S*****************/
	var treeBar = new Ext.Toolbar({  
           buttonAlign : 'center',  
           items : [{xtype : 'textfield',emptyText : '服务器IP过滤...',id:'filter_input',width : 180}]  
     });   
	Ext.Ajax.request({
	    url: './crsmServerMonitor.ered?reqCode=queryServerStatusDetailList',
	    success: function(response) {
		
			var root = new Ext.tree.TreeNode({
						text : 'cums资源管理',
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
					iconCls : 'pluginIcon'
				});
				
			
				if(groupJson.ROOT[i].vstatus=='1'){//在运行
					onLine.appendChild(node[i]);
				}else if(groupJson.ROOT[i].vstatus=='2'){//未认证
					offLine.appendChild(node[i]);
				}else if(groupJson.ROOT[i].vstatus=='3'){//预下架
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
				 	Ext.getCmp('authen').show();
			  	Ext.getCmp('authen').disable();
					Ext.getCmp('updateConfig').enable();
				}else if(node.parentNode.text=='未认证'){
				//	Ext.getCmp('online').hide();
				//	Ext.getCmp('offline').hide();
				//	Ext.getCmp('authen').enable();
					Ext.getCmp('authen').enable();
			    	Ext.getCmp('authen').show();
					Ext.getCmp('updateConfig').disable();
				}else{
				//	Ext.getCmp('online').show();
				//	Ext.getCmp('offline').hide();
				//	Ext.getCmp('authen').hide();
				//	Ext.getCmp('perOffline').hide();
				//	Ext.getCmp('updateConfig').disable();
				}
				myForm.form.load({
					waitMsg : '正在处理数据,请稍候...',// 提示信息
					waitTitle : '提示',// 标题
					url : './crsmServerMonitor.ered?reqCode=queryServerSingle&nodeName='+nodeName ,//获取单个服务器信息
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
				store.proxy = new Ext.data.HttpProxy({url:'./crsmServerMonitor.ered?reqCode=querySesionDetail&server_ip='+nodeName});
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
						title : '<span>网关信息<span>',
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
												columnWidth : .33,
												layout : 'form',
												labelWidth : 80, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : '模块版本', // 标签
															name : 'cpu_info', // name:后台根据此name属性取值
															allowBlank : true,
															readOnly:true,
															labelStyle : 'color:blue;',
//															style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;', 
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '100%'// 宽度百分比

														},{
															fieldLabel : '配置参数', // 标签
															name : 'mem_info', // name:后台根据此name属性取值
															allowBlank : true,
														//	readOnly:true,
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '100%'// 宽度百分比

														}
														,{
															fieldLabel : '配置参数',
															name : 'max_num',
															id:'total_num',
															xtype : 'textfield', // 设置为文字输入框类型
															//readOnly:true,
															anchor : '100%',
															labelStyle : 'color:green;',
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														,{
															fieldLabel : '配置参数',
															name : 'cabinet_address',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														]
											},{
												columnWidth : .33,
												layout : 'form',
												labelWidth : 80, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : '配置参数',
															name : 'sys_version',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
														//	id:'total_num',
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															allowBlank : true,
															anchor : '100%'
														}
												,{
															fieldLabel : '配置参数', // 标签
															name : 'vlc_version', // name:后台根据此name属性取值
															allowBlank : true,
															//readOnly:true,
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '100%'// 宽度百分比

														},{
															fieldLabel : '配置参数',
															name : 'online_num',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															labelStyle : 'color:green;',
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '配置参数',
															name : 'cabinet_card',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}
														]
											},{
												columnWidth : .33,
												layout : 'form',
												labelWidth : 100, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : '配置参数',
															name : 'chrome_version',
															//readOnly:true,
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															xtype : 'textfield', // 设置为文字输入框类型
															anchor : '100%'
														}
												,{
															fieldLabel : '配置参数',
															name : 'chromeplush_version',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															labelStyle : 'color:blue;',
//															//style:'background : blue;border:0 solid;border-bottom: #000000 1px solid;',
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
														},{
															fieldLabel : '配置参数',
															name : 'liuhua_num',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
															labelStyle : 'color:green;',
//															//style:'background : green;border:0 solid;border-bottom: #000000 1px solid;'
															//style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '配置参数',
															name : 'cabinet_ratepower',
															xtype : 'textfield', // 设置为文字输入框类型
														//	readOnly:true,
															anchor : '100%',
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
			var cardStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './crsmServerMonitor.ered?reqCode=querycard&cabinet_address='//查找机柜编号
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
								name : 'cabinet_card'
							},{
								name : 'cabinet_id'
							}])
			});

			var cardCombo = new Ext.form.ComboBox({
					hiddenName : 'cabinet_id',
					fieldLabel : '机柜编号',
					emptyText : '请选择...',
					triggerAction : 'all',
					store : cardStore,
					allowBlank : false,
					displayField : 'cabinet_card',
					valueField : 'cabinet_id',
					loadingText : '正在加载数据...',
					mode : 'remote', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
					forceSelection : true,
					typeAhead : true,
					resizable : true,
					editable : false,
					anchor : '90%',
			});
			
			
			var cabinetStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './crsmServerMonitor.ered?reqCode=querycabinet'//查找机柜crsmServerMonitor.ered
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
								name : 'cabinet_address'
							}])
			});
			cabinetStore.load();
			var cabinetCombo = new Ext.form.ComboBox({
					hiddenName : 'cabinet_address',
					fieldLabel : '机柜地址',
					emptyText : '请选择...',
					triggerAction : 'all',
					store : cabinetStore,
					allowBlank : false,
					displayField : 'cabinet_address',
					valueField : 'cabinet_address',
					loadingText : '正在加载数据...',
					mode : 'remote', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
					forceSelection : true,
					typeAhead : true,
					resizable : true,
					editable : false,
					anchor : '90%',
					listeners:{
						'select':function(obj){
							var cabinetID= cabinetCombo.getValue(); 
							cardStore.removeAll();
							cardCombo.setValue('请选择...');
							if(cabinetID!='请选择...')
								Ext.getCmp('addCabinetP').disable();
							var cabinet_add = encodeURI(cabinetID);
							cabinet_add = encodeURI(cabinet_add);
							cardStore.proxy = new Ext.data.HttpProxy({url:'./crsmServerMonitor.ered?reqCode=querycard&cabinet_address='+cabinet_add});
							cardStore.load();
						}						
					}	
			});
			var bandingFormPanel = new Ext.form.FormPanel({
						id : 'bindingFormPanel',
						name : 'bindingFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 110,
						frame : false,
						bodyStyle : 'padding:5 5 0',
						items : [cabinetCombo,cardCombo]
					});
			var bindingWindow = new Ext.Window({
						layout : 'fit',
						width : 420,
						height : 300,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>机柜绑定</span>',
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
						items : [bandingFormPanel],
						buttons : [{
							text : '新增机柜',
							iconCls : 'page_addIcon',
							id:'addCabinetP',
							handler : function() {
								bindingWindow.hide();
								addCabinet();
							}
						}, {
							text : '保存',
							iconCls : 'acceptIcon',
							handler : function() {
								saveBanding();
							}
//						}, {
//							text : '重置',
//							id : 'btnReset',
//							iconCls : 'tbar_synchronizeIcon',
//							handler : function() {
//								clearForm(bindingFormPanel.getForm());
//							}
						}, {
							text : '关闭',
							iconCls : 'deleteIcon',
							handler : function() {
								bindingWindow.hide();
							}
						}]
					});
			
			/**
			 * 新增机柜弹出框
			 */
			var addCabinetFormPanel = new Ext.form.FormPanel({
						id : 'addCabinetFormPanel',
						name : 'addCabinetFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 110,
						frame : false,
						bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : '机柜地址',
									name : 'cabinet_address',
									allowBlank : false,	
									anchor : '99%'
								},{
									fieldLabel : '机柜容量',
									name : 'card_num',
									allowBlank : false,
									anchor : '99%',
									xtype : 'numberfield', // 设置为数字输入框类型
									allowDecimals : false, // 是否允许输入小数
									allowNegative : false, // 是否允许输入负数
									minValue : 5,
									minValueText : '机柜最小容量为5个位置',
									maxValue : 14,
									maxValueText : '机柜最大容量为14个位置'
								},{
									fieldLabel : '额定功率',
									name : 'cabinet_ratePower',
									allowBlank : false,	
									anchor : '99%'
								}]
					});
			var addCabinetWindow = new Ext.Window({
						layout : 'fit',
						width : 420,
						height : 300,
						resizable : false,
						draggable : true,
						closeAction : 'hide',
						title : '<span>新增机柜</span>',
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
						items : [addCabinetFormPanel],
						buttons : [{
							text : '保存',
							iconCls : 'acceptIcon',
							handler : function() {
								saveCabinet();
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
								addCabinetWindow.hide();
							}
						}]
					});
			/****************S function S********************/
			function modifyConfig(){//修改RSM配置弹出框
				
				Ext.Msg.show({title:'提示',msg:'修改成功',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
			function bandingAction(){//机柜绑定弹出框
				cabinetStore.reload();
				Ext.getCmp('addCabinetP').enable();
				bandingFormPanel.getForm().reset();
				bindingWindow.show();
			}
			function saveBanding(){//保存机柜绑定信息到数据库
				if (bindingWindow.getComponent('bindingFormPanel').form.isValid()) {
						bindingWindow.getComponent('bindingFormPanel').form.submit({
						url : './crsmServerMonitor.ered?reqCode=savaBanding&server_id='+nodeName,//保存新增的机柜信息到数据库中
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							Ext.Msg.show({title:'提示',msg:'绑定机柜成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							bindingWindow.hide();
							myForm.form.load({
								waitMsg : '正在处理数据,请稍候...',// 提示信息
								waitTitle : '提示',// 标题
								url : './crsmServerMonitor.ered?reqCode=queryServerSingle&nodeName='+nodeName ,//获取单个服务器信息
			//					params : params,
								// method : 'GET',// 请求方式
								success : function(form, action) {
									var t = action.result.data;
									if(t.cabinet_id==null){
										Ext.getCmp('cabinetM').enable();
									}
			//							if(!t.success){
			//								Ext.Msg.show({title:'提示',msg:'用户激活失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
			//								return ;
			//							}
								},
								failure : function(form,action){
									Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
								}
							})
						},
						failure : function(form, action) {
						}
					});
				}
			}
			function addCabinet(){//新增机柜弹出框
				addCabinetFormPanel.getForm().reset();
				addCabinetWindow.show();
			}
			function saveCabinet(){//保存机柜信息到数据库
				if (addCabinetWindow.getComponent('addCabinetFormPanel').form.isValid()) {
						addCabinetWindow.getComponent('addCabinetFormPanel').form.submit({
						url : './crsmServerMonitor.ered?reqCode=savaCabinet',//保存新增的机柜信息到数据库中
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							Ext.Msg.show({title:'提示',msg:'机柜信息添加成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
							addCabinetWindow.hide();
						},
						failure : function(form, action) {
							Ext.Msg.show({title:'提示',msg:'机柜地址信息已有，请修改信息！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNNING});
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
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器下线事件已触发成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                                    if(btnId == 'ok'){
                                       location="javascript:location.reload()";
                                    }}});
							}else{
								Ext.Msg.show({title:'提示',msg:'服务器下线事件触发失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
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
				    url: './crsmServerMonitor.ered?reqCode=serverAuthOperate&server_ip='+nodeName,
				    success: function(response) {
						var t= eval('('+response.responseText+')');
							if(t.success){
								Ext.Msg.show({title:'提示',msg:'服务器认证事件已触发成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                                    if(btnId == 'ok'){
                                       location="javascript:location.reload()";
                                    }}});
								
							}else{
								Ext.Msg.show({title:'提示',msg:'服务器认证事件触发失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							}
				    },failure : function(response) {
				    	
					}
				})
			}
			
			function deleteSource(){//释放在线资源
				var rows = grid.getSelectionModel().getSelections();
				if (Ext.isEmpty(rows)) {
					Ext.Msg.show({title:'提示',msg:'请先选中要释放的资源!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
					return;
				}
				var strChecked1 = jsArray2JsString(rows, 'resid');
				showWaitMsg('正在杀死会话,请等待...');
				Ext.Ajax.request({
					url : './crsmServerMonitor.ered?reqCode=killSession',
					success : function(response) {
						var resultArray = Ext.util.JSON.decode(response.responseText);
//						store.reload();
						if(!resultArray.success){
							Ext.Msg.show({title:'提示',msg:'在线资源释放失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});
						}else{
							Ext.Msg.show({title:'提示',msg:'在线资源释放成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
                                    if(btnId == 'ok'){
                                       location="javascript:location.reload()";
                                    }}});
						}
					},
					failure : function(response) {
					},
					params : {
						strChecked1 : strChecked1
					}
				});
			}
			
			/****************E function E********************/
			
			
	/**************E   右侧详情生成结束   E*****************/
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
				header : 'vncid',
				dataIndex : 'vncid',
				width : 200,
				hidden :true
			},{
				header : '监控点名称',
				dataIndex : 'vnctype',
				hidden :true
			},{
				header : '监控类型',
				dataIndex : 'resid'
			},{
				header : '监控频率',
				dataIndex : 'sessionid'
			},{
				header : '状态',
				dataIndex : 'spid'
			},{
				header : '备注',
				dataIndex : 'operid'
			},{
				header : '操作',
				dataIndex : 'iip'
			}]);
			/**
			 * 数据存储
			 */
			var store = new Ext.data.Store({
				// 获取数据的方式
				proxy : new Ext.data.HttpProxy({
					url : './crsmServerMonitor.ered?reqCode=querySesionDetail'
				}),
				// 数据读取器
				reader : new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT', // 记录总数
					root : 'ROOT' // Json中的列表数据根节点
				}, [ {
					name : 'resid'
				},{
					name : 'vncid'
				},{
					name : 'sessionid'
				},{
					name : 'spid'
				},{
					name : 'operid'
				},{
					name : 'iip'
				},{
					name : 'iport'
				}])
			});
			var grid = new Ext.grid.GridPanel({
				region : 'south', // 和VIEWPORT布局模型对应，充当center区域布局
				// collapsible : true,
				// 表格面板标题,默认为粗体，我不喜欢粗体，这里设置样式将其格式为正常字体
				title : '<span style="font-weight:bold;">已创建的监控</span>',
				autoScroll : true,
				collapsible : true,
				collapsed: false,
				height:300,
				frame : true,
				store : store, // 数据存储
				stripeRows : true, // 斑马线
				cm : cm, // 列模型
				sm : sm,
				bbar : [{
					text : '释放资源',
					iconCls : 'deleteIcon',
					handler : function() {
						deleteSource();
					}
				}],
				viewConfig : {
					// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
//					forceFit : true
				},
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				}
			});
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
	