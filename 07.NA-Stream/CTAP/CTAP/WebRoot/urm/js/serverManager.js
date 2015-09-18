/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 */
Ext.onReady(function() {
	
	var gatewayName ='';
	var gatewayIp ='';
	var gatewayPort='';
	
	Ext.Msg.minWidth = 300;
	Ext.Msg.maxWidth = 360;

	/**************S   左侧选择树生成开始   S*****************/
	var treeBar = new Ext.Toolbar({  
           buttonAlign : 'center',  
           items : [{xtype : 'textfield',emptyText : '服务器IP过滤...',id:'filter_input',width : 180}]  
     });   
	
	
	
	var root = new Ext.tree.TreeNode({
				text : '服务器管理',
				expanded : true,
				id : '0'
			});

	
	var unAuthen = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_errorIcon',
		text:'不可用',
		id : '1'
			
	});
	
//	var unAuthenCag = new Ext.tree.TreeNode({
//		expanded:true,
//		iconCls : 'server_errorIcon',
//		text:'接入网关'
//	});
	var unAuthenCkg = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_errorIcon',
		text:'键值网关',
		id : '2'
	});
	var unAuthenCsg = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_errorIcon',
		text:'信令网关',
		id : '3'
	});
	
//	unAuthen.appendChild(unAuthenCag);
	unAuthen.appendChild(unAuthenCkg);
	unAuthen.appendChild(unAuthenCsg);
	root.appendChild(unAuthen);
	
	var authened = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_normalIcon',
		text:'可用',
		id : '4'
	});
	
//	var authenedCag = new Ext.tree.TreeNode({
//		expanded:true,
//		iconCls : 'server_errorIcon',
//		text:'接入网关'
//	});
	var authenedCkg = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_normalIcon',
		text:'键值网关',
		id : '5'
	});
	var authenedCsg = new Ext.tree.TreeNode({
		expanded:true,
		iconCls : 'server_normalIcon',
		text:'信令网关',
		id : '6'
	});
	
//	authened.appendChild(authenedCag);
	authened.appendChild(authenedCkg);
	authened.appendChild(authenedCsg);			
	

	
	
	
	Ext.Ajax.request({
	    url: './ServerManager.ered?reqCode=queryCkgGatewayInfoList',
	    success: function(response) {
			var node = new Array();			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {
				node[i] = new Ext.tree.TreeNode({
					id : groupJson.ROOT[i].ckg_id+groupJson.ROOT[i].ckg_port,
					text : groupJson.ROOT[i].ckg_ip+":"+groupJson.ROOT[i].ckg_port,
					leaf : true,
					iconCls : 'imageIcon'
				});			
				if(groupJson.ROOT[i].ckg_status=='1'){//可用，即已认证
				    authenedCkg.appendChild(node[i]);
				}
				else{			
					unAuthenCkg.appendChild(node[i]);
				}
			}
	},
    failure: function(response) {
    }
});		
		
	Ext.Ajax.request({
	    url: './ServerManager.ered?reqCode=queryCsgGatewayInfoList',
	    success: function(response) {
			var node = new Array();			
			var groupJson = eval('('+response.responseText+')');
	    	var total = groupJson.TOTALCOUNT;
			for(var i=0;i<total;i++) {
				node[i] = new Ext.tree.TreeNode({
					id : groupJson.ROOT[i].csg_id+groupJson.ROOT[i].csg_port,
					text : groupJson.ROOT[i].csg_ip+":"+groupJson.ROOT[i].csg_port,
					leaf : true,
					iconCls : 'imageIcon'
				});			
				if(groupJson.ROOT[i].csg_status=='1'){//在运行
					authenedCsg.appendChild(node[i]);
				
				}else{				
					unAuthenCsg.appendChild(node[i]);
				}
			}
	},
    failure: function(response) {
    }
});		
		
	root.appendChild(authened);
	
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
				Ext.getCmp('cabinetM').hide();
				Ext.getCmp('updateConfig').hide();
				Node = node;
				nodeName =  node.text;
				nodeId =  node.id;
				
				
			
				
				if(node.parentNode.id==2){			//不可用键值网关	
					
					
					
					Ext.getCmp('authen').enable();
			    	Ext.getCmp('authen').show();
			    	var ckg_ip =node.text.split(":")[0];			    	
			    	var ckg_port=node.text.split(":")[1];
			    	 gatewayName = "ckg";
			    	 gatewayIp =ckg_ip;
			    	 gatewayPort=ckg_ip;
			    	 
					myForm.form.load({						
						waitMsg : '正在处理数据,请稍候...',// 提示信息
						waitTitle : '提示',// 标题
						url : './ServerManager.ered?reqCode=queryCkgGatewayInfoByIpPort&ckg_ip='+ckg_ip+'&ckg_port='+ckg_port ,//获取单个服务器信息
						success : function(form, action) {},
						failure : function(form,action){
						//	Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
						}
						
					})
						//	store.load();
				}
		       if(node.parentNode.id==3){				//不可用信令网关	
		    	  
		    		var csg_ip =node.text.split(":")[0] ;			    	
			    	var csg_port=node.text.split(":")[1];
		    	   
			    	 gatewayName = "csg";
		    	 gatewayIp =csg_ip;
		    	 gatewayPort=csg_port;
			    	
		    		Ext.getCmp('authen').enable();
			    	Ext.getCmp('authen').show();
					myForm.form.load({
						
						waitMsg : '正在处理数据,请稍候...',// 提示信息
						waitTitle : '提示',// 标题
						url : './ServerManager.ered?reqCode=queryCsgGatewayInfoByIpPort&csg_ip='+csg_ip+'&csg_port='+csg_port ,//获取单个服务器信息
						success : function(form, action) {},
						failure : function(form,action){
							//Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
						}
						
					})
						//	store.load();		
				}
				
				if(node.parentNode.id==5){				//可用键值网关	
					Ext.getCmp('authen').hide();
					
					
					var ckg_ip =node.text.split(":")[0] ;			    	
			    	var ckg_port=node.text.split(":")[1];
					myForm.form.load({
						
						waitMsg : '正在处理数据,请稍候...',// 提示信息
						waitTitle : '提示',// 标题
						url : './ServerManager.ered?reqCode=queryCkgGatewayInfoByIpPort&ckg_ip='+ckg_ip+'&ckg_port='+ckg_port ,//获取单个服务器信息
						success : function(form, action) {},
						failure : function(form,action){
						//	Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
						}
						
					})
//					store.proxy = new Ext.data.HttpProxy({url:'./crsmServerMonitor.ered?reqCode=querySesionDetail&server_ip='+nodeName});
						//	store.load();
								
				}
		       if(node.parentNode.id==6){				//可用信令网关				
		    	   Ext.getCmp('authen').hide();
		    	   
		    	   var csg_ip =node.text.split(":")[0] ;			    	
			    	var csg_port=node.text.split(":")[1];
					myForm.form.load({
						
						waitMsg : '正在处理数据,请稍候...',// 提示信息
						waitTitle : '提示',// 标题
						url : './ServerManager.ered?reqCode=queryCsgGatewayInfoByIpPort&csg_ip='+csg_ip+'&csg_port='+csg_port ,//获取单个服务器信息
						success : function(form, action) {},
						failure : function(form,action){
						//	alert("shibai");
						//	Ext.Msg.show({title:'提示',msg:'获取服务器信息失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
						}
						
					})
						//	store.load();
							
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
						collapsible : false,
						bodyStyle :'background:#CCDFEA',
						border : false,
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
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
													fieldLabel : '模块版本', // 标签
													name : 'version', // name:后台根据此name属性取值
													maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
													allowBlank : false,
													labelStyle : 'color:blue;',
													//fieldStyle:'color:red;',
													anchor : '100%',// 宽度百分比
												//	disabled : true, // 设置禁用属性
													value : global_version, // 初始值
													readOnly : true,
													//fieldClass : 'x-custom-field-disabled',															
													style:'background : none;border:0 solid;'
													
													

												}, {
												//	xtype : 'datefield',
													fieldLabel : '进程名称', // 标签
													name : 'thread_id', // name:后台根据此name属性取值
													//disabled : true, // 设置禁用属性
													labelStyle : 'color:blue;',
													anchor : '100%',
													readOnly : true,
													value : global_thread_id, // 初始值
													//fieldClass : 'x-custom-field-disabled',
													style:'background : none;border:0 solid;'
												}, {
												//	xtype : 'datefield',
													fieldLabel : 'id', // 标签
													name : 'id', // name:后台根据此name属性取值
													//disabled : true, // 设置禁用属性
													labelStyle : 'color:blue;',
													anchor : '100%',
													readOnly : true,
													value : global_id, // 初始值
													//fieldClass : 'x-custom-field-disabled',
													style:'background : none;border:0 solid;'
												}]
									}, {
										columnWidth : .33,
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
													fieldLabel : '运行状态',
													name : 'status',
													maxLength : 20,
													anchor : '100%',
													readOnly : true,
													labelStyle : 'color:blue;',
												//	labelStyle : 'color:green;',
												//	disabled : true, // 设置禁用属性
													value : global_status, // 初始值
												//	fieldClass : 'x-custom-field-disabled',
													style:'color:green;background : none;border:0 solid;'

												},{
													fieldLabel : 'GPU',
													name : 'gpu_info',
													readOnly : true,
													labelStyle : 'color:blue;',
												//	disabled : true, // 设置禁用属性
													value : global_gpu_info, // 初始值
												//	fieldClass : 'x-custom-field-disabled',
													style:'background : none;border:0 solid;',	
													anchor : '100%'
												},{
													fieldLabel : 'cag_id',
													name : 'cag_id',
													readOnly : true,
													labelStyle : 'color:blue;',
												//	disabled : true, // 设置禁用属性
													value : global_cag_id, // 初始值
												//	fieldClass : 'x-custom-field-disabled',
													style:'background : none;border:0 solid;',	
													anchor : '100%'
												}]
									}, {
										columnWidth : .33,
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
											fieldLabel : '内存',
											name : 'mem_info',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	disabled : true, // 设置禁用属性
											value : global_mem_info, // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;',	
											anchor : '100%'
										
									
										},{
											fieldLabel : 'CUP',
											name : 'cpu_info',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	disabled : true, // 设置禁用属性
											value : global_cpu_info, // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;',	
											anchor : '100%'
										},
										{
											fieldLabel : '硬盘',
											name : 'capacity_size',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	disabled : true, // 设置禁用属性
											value : global_capacity_size, // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;',	
											anchor : '100%'
										}]
									}]
						}, 
//						{
//							fieldLabel : '备注',
//							id : 'remark',
//							name : 'remark',
//							xtype : 'textarea',
//							maxLength : 100,
//							
//							anchor : '99%'
//						}
//						,
						{
							name : 'xmid',
							id : 'xmid',
							hidden : true,
							xtype : 'textfield' // 设置为数字输入框类型
						}],
								buttons : [
								           {
									text : '保存配置',
									id:'updateConfig',
									disabled:true,
									hidden :true,
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
									disabled:true,
									//hidden:true,
									
									iconCls : 'keyIcon',
									handler : function() {
										authFunction();
									}
								}]
					});
			var userInfoForm = new Ext.form.FormPanel({
				region : 'center',
				title : '<span>用户信息<span>',
				collapsible : false,
				bodyStyle :'background:#CCDFEA',
				border : false,
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
								labelWidth : 60, // 标签宽度
								defaultType : 'textfield',
								border : false,
								items : [{
											fieldLabel : '盒子号', // 标签
											name : 'version1', // name:后台根据此name属性取值
											maxLength : 20, // 可输入的最大文本长度,不区分中英文字符
											allowBlank : false,
											labelStyle : 'color:blue;',
											//fieldStyle:'color:red;',
											anchor : '100%',// 宽度百分比
										//	disabled : true, // 设置禁用属性
											value : '信息', // 初始值
											readOnly : true,
											//fieldClass : 'x-custom-field-disabled',															
											style:'background : none;border:0 solid;'
											
											

										}, {
										//	xtype : 'datefield',
											fieldLabel : '信息', // 标签
											name : 'thread_id1', // name:后台根据此name属性取值
											//disabled : true, // 设置禁用属性
											labelStyle : 'color:blue;',
											anchor : '100%',
											readOnly : true,
											value : '信息', // 初始值
											//fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;'
										}, {
										//	xtype : 'datefield',
											fieldLabel : 'id1', // 标签
											name : 'id1', // name:后台根据此name属性取值
											//disabled : true, // 设置禁用属性
											labelStyle : 'color:blue;',
											anchor : '100%',
											readOnly : true,
											value : '信息', // 初始值
											//fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;'
										}]
							}, {
								columnWidth : .33,
								layout : 'form',
								labelWidth : 60, // 标签宽度
								defaultType : 'textfield',
								border : false,
								items : [{
											fieldLabel : '运行状态',
											name : 'status1',
											maxLength : 20,
											anchor : '100%',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	labelStyle : 'color:green;',
										//	disabled : true, // 设置禁用属性
											value : '信息', // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'color:green;background : none;border:0 solid;'

										},{
											fieldLabel : '信息',
											name : 'gpu_info1',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	disabled : true, // 设置禁用属性
											value : '信息', // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;',	
											anchor : '100%'
										},{
											fieldLabel : '信息',
											name : 'cag_id1',
											readOnly : true,
											labelStyle : 'color:blue;',
										//	disabled : true, // 设置禁用属性
											value : '信息', // 初始值
										//	fieldClass : 'x-custom-field-disabled',
											style:'background : none;border:0 solid;',	
											anchor : '100%'
										}]
							}, {
								columnWidth : .33,
								layout : 'form',
								labelWidth : 60, // 标签宽度
								defaultType : 'textfield',
								border : false,
								items : [{
									fieldLabel : '信息',
									name : 'mem_info1',
									readOnly : true,
									labelStyle : 'color:blue;',
								//	disabled : true, // 设置禁用属性
									value : '信息', // 初始值
								//	fieldClass : 'x-custom-field-disabled',
									style:'background : none;border:0 solid;',	
									anchor : '100%'
								
							
								},{
									fieldLabel : '信息',
									name : 'cpu_info1',
									readOnly : true,
									labelStyle : 'color:blue;',
								//	disabled : true, // 设置禁用属性
									value : '信息', // 初始值
								//	fieldClass : 'x-custom-field-disabled',
									style:'background : none;border:0 solid;',	
									anchor : '100%'
								},
								{
									fieldLabel : '信息',
									name : 'capacity_size1',
									readOnly : true,
									labelStyle : 'color:blue;',
								//	disabled : true, // 设置禁用属性
									value : '信息', // 初始值
								//	fieldClass : 'x-custom-field-disabled',
									style:'background : none;border:0 solid;',	
									anchor : '100%'
								}]
							}]
				}, 
//				{
//					fieldLabel : '备注',
//					id : 'remark',
//					name : 'remark',
//					xtype : 'textarea',
//					maxLength : 100,
//					
//					anchor : '99%'
//				}
//				,
				{}],
						buttons : []
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
				    url: './ServerManager.ered?reqCode=authGateway&gatewayName='+gatewayName+'&gatewayIp='+gatewayIp+'&gatewayPort='+gatewayPort,
				    success: function(response) {
					Ext.Msg.show({title:'提示',msg:'数据已成功提交到后台处理',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO,fn: function(btnId,text,opt){
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
				    	Ext.Msg.show({title:'提示',msg:'数据提交失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
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
									items : [{
									
										region : 'center',
										layout : 'border',
								
										height:500,
										items : [myForm]
									}, {
										region : 'north',
										height:120,
										layout : 'border',
										items : [userInfoForm]
									}]
								}]
					});
	

});
	