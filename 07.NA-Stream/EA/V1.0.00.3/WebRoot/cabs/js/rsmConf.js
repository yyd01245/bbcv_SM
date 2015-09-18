/**
 * 高清承载管理配置
 * 
 */

	var area_id;
	var area_text;
Ext.onReady(function() {
	window.setTimeout("getArea();",1000);
			
		Ext.Msg.minWidth = 300 ;
		Ext.Msg.maxWidth = 360 ;
			var qForm = new Ext.form.FormPanel({
						region : 'north',
						title : '<span>查询条件<span>',
						collapsible : false,
						border : true,
						labelWidth : 80, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:3 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						height : 80,
						items : [{
							layout : 'column',
							border : false,
							items : [{
									columnWidth : .30,
									layout : 'form',
									labelWidth : 80, // 标签宽度
									defaultType : 'textfield',
									border : false,
									items : [{
										fieldLabel : 'VNCW地址',
										name : 'vncw_ip',
										regex:/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/,  
										regexText:'必须是合适的IP地址',
										allowBlank : false,
										blankText : 'VNCW地址不能为空',
										anchor : '90%'
									}]
								},{
										columnWidth : .70,
										layout : 'form',
										labelWidth : 140, // 标签宽度
										defaultType : 'textfield',
										border : false,
										buttonAlign : 'right',
										buttons : [{
											text : '获取参数',
											iconCls : 'previewIcon',
											handler : function() {
												myForm.getForm().reset();
												loadCallBack(qForm.getForm());
											}
										}, {
											text : '重置',
											iconCls : 'tbar_synchronizeIcon',
											handler : function() {
												qForm.getForm().reset();
												myForm.getForm().reset();
											}
										}, {
											text : '保存修改',
											iconCls : 'acceptIcon',
											handler : function() {
												submitTheForm(qForm.getForm());
											}
										}]
								}]
						}]
					});
			
	
	var activeCom = new Ext.form.ComboBox({
		fieldLabel : '激活地址',
		name : 'active_url',
		maxLength : 512,
		store :ACTIVEURLStore,
		displayField : 'text',
		valueField : 'text',
		maxLengthText : '激活地址长度不能超过512',
		allowBlank : false,
		mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
		forceSelection : false,
		typeAhead : true,
		triggerAction : 'all',
		resizable : true,
		editable : true,
		anchor : '100%',
		listeners:{
			blur:function(obj){
				obj.setValue(Ext.util.Format.trim(obj.getValue()));
			},
			'beforequery':function(e){                     
		    	 var combo = e.combo;  
		         combo.expand(); 
		         if(!e.forceAll){    
		            var input = e.query;    
		            // 检索的正则   
		            var regExp = new RegExp(".*" + input + ".*");  
		            // 执行检索   
		            combo.store.filterBy(function(record,id){    
		            // 得到每个record的项目名称值   
		            var text = record.get(combo.displayField);    
		            return regExp.test(text);   
		           });        
		          return false;  
		        }  
		    }
		}
	});
			
		/*服务名称下拉框*/
	var servidStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
					url : './serverManage.ered?reqCode=queryServer'
				}),
		reader : new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
				}, [{
							name : 'serv_type'
						}, {
							name : 'serv_name'
						}])
	});

	var servidCombo = new Ext.form.ComboBox({
			hiddenName : 'def_serv_type',
			fieldLabel : '默认服务',
			emptyText : '请选择...',
			triggerAction : 'all',
			store : servidStore,
			allowBlank : false,
			displayField : 'serv_name',
			valueField : 'serv_type',
			loadingText : '正在加载数据...',
			mode : 'remote', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
			forceSelection : true,
			typeAhead : true,
			resizable : true,
			editable : false,
			anchor : '100%'
		});
	//机顶盒类型队列
	var tremAbilityCombo = new Ext.form.ComboBox({
					hiddenName : 'term_ability',
					fieldLabel : '终端能力名称',
					emptyText : '请选择...',
					triggerAction : 'all',
					store : TERMABILITYStore,
					displayField : 'text',
					valueField : 'value',
					loadingText : '正在加载数据...',
					mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
					forceSelection : true,
					typeAhead : true,
					resizable : true,
					editable : false,
					allowBlank : false,
					anchor : '100%'
				});
			var tremAbilityCombo1 = new Ext.form.ComboBox({
					hiddenName : 'term_ability',
					fieldLabel : '终端能力名称',
					emptyText : '请选择...',
					triggerAction : 'all',
					store : TERMABILITYStore,
					displayField : 'text',
					valueField : 'value',
					loadingText : '正在加载数据...',
					mode : 'local', // 数据会自动读取,如果设置为local又调用了store.load()则会读取2次；也可以将其设置为local，然后通过store.load()方法来读取
					forceSelection : true,
					typeAhead : true,
					allowBlank : false,
					resizable : true,
					editable : false,
					anchor : '100%'
				});		
		var addRoot  = new Ext.tree.AsyncTreeNode({
				id : area_id,
				text : area_text,
				expanded : true
			});
		var addDeptTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					baseAttrs : {},
					dataUrl : '../demo/treeDemo.ered?reqCode=queryAreas'
				}),
				root : addRoot ,
				autoScroll : true,
				animate : false,
				useArrows : false,
				border : false
		});
		//动态加载根节点
		addDeptTree.on('beforeload',function(){
			addRoot.setId(area_id);
			addRoot.setText(area_text);
		});
		
		// 监听下拉树的节点单击事件
		addDeptTree.on('click', function(node) {
					var nodeID=node.id;
					comboxWithTree.setValue(node.id);
					comboxWithTree.collapse();
				});
		 var comboxWithTree = new Ext.form.ComboBox({
			name : 'area_id',
			store : new Ext.data.SimpleStore({
						fields : [],
						data : [[]]
					}),
			editable : true,
//			value : '',
			emptyText : '请选择...',
			fieldLabel : '运营区域',
			blankText : '运营区域不能为空',
			anchor : '100%',
			mode : 'local',
			forceSelection: false,
			triggerAction : 'all',
			maxHeight : 390,
			// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
			tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
			allowBlank : false,
			onSelect : Ext.emptyFn
		});
		 
		// 监听下拉框的下拉展开事件
		comboxWithTree.on('expand', function() {
			
				addDeptTree.rendered = false ;
				// 将UI树挂到treeDiv容器
				addDeptTree.render('addDeptTreeDiv');
				// addDeptTree.root.expand(); //只是第一次下拉会加载数据
				addDeptTree.root.reload(); // 每次下拉都会加载数据
	
		});
			var myForm = new Ext.form.FormPanel({
						region : 'center',
						title : '<span>RSM配置文件参数修改<span>',
						collapsible : false,
						border : true,
						labelWidth : 75, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
//						height : 300,
						items : [{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : .20,
												layout : 'form',
												labelWidth : 90, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
															fieldLabel : 'RSM.AREA_ID', // 标签
															name : 'RSM.AREA_ID', // name:后台根据此name属性取值
															allowBlank : false,
															labelStyle : 'color:blue;',
															anchor : '100%',// 宽度百分比
															blankText : 'RSM.AREA_ID不能为空'
														}, {
															fieldLabel : 'RSM.C_TYPE',
															name : 'RSM.C_TYPE',
															labelStyle : 'color:blue;',
															xtype : 'textfield', // 设置为文字输入框类型
															allowBlank : false,
															labelStyle : 'color:blue;',
															anchor : '100%',
															blankText : 'RSM.C_TYPE不能为空'
														},{
															fieldLabel : 'RSM.NETNAME', // 标签
															name : 'RSM.NETNAME', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '100%', // 宽度百分比
															labelStyle : 'color:blue;'
														},{
															fieldLabel : 'AIM.IP', // 标签
															name : 'AIM.IP', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															labelStyle : 'color:blue;',
															anchor : '100%' // 宽度百分比
														},{
															fieldLabel : 'AIM.PORT', // 标签
															name : 'AIM.PORT', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '100%', // 宽度百分比
//															readOnly : true,
//															fieldClass : 'x-custom-field-disabled',
															labelStyle : 'color:blue;'
														},{
															fieldLabel : 'VNCW.IP', // 标签
															name : 'VNCW.IP', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '100%' ,// 宽度百分比，
															labelStyle : 'color:blue;'
														},{
															fieldLabel : 'VNCW.PORT', // 标签
															name : 'VNCW.PORT', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '100%' ,// 宽度百分比，
//															readOnly : true,
//															fieldClass : 'x-custom-field-disabled',
															labelStyle : 'color:blue;'
														},{
														fieldLabel : 'RSM.NUM.HD', // 标签
														name : 'RSM.NUM.HD', // name:后台根据此name属性取值
														allowBlank : true, // 是否允许为空
														anchor : '100%' ,// 宽度百分比
														labelStyle : 'color:blue;'
													},{
														fieldLabel : 'RSM.NUM.SD', // 标签
														name : 'RSM.NUM.SD', // name:后台根据此name属性取值
														allowBlank : true, // 是否允许为空
														anchor : '100%',// 宽度百
														labelStyle : 'color:blue;'
													},{
														fieldLabel : 'RSM.VID.QUOTA',
														name : 'RSM.VID.QUOTA',
														xtype : 'textfield', // 设置为文字输入框类型
														allowBlank : true,
														anchor : '100%',
														labelStyle : 'color:blue;'
													},{
														fieldLabel : 'RSM.LOG_FILE', // 标签
														name : 'RSM.LOG_FILE', // name:后台根据此name属性取值
														allowBlank : true, // 是否允许为空
														anchor : '100%' ,// 宽度百分比
														labelStyle : 'color:blue;'
													},{
														fieldLabel : 'RSM.LOG_LEVEL', // 标签
														name : 'RSM.LOG_LEVEL', // name:后台根据此name属性取值
														allowBlank : true, // 是否允许为空
														anchor : '100%' ,// 宽度百分比
														labelStyle : 'color:blue;'
													}]
											}, {
												columnWidth : .35,
												layout : 'form',
												labelWidth : 180, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
													fieldLabel : 'RSM.AUDIO', // 标签
													name : 'RSM.AUDIO', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%', // 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.GOP.SIZE', // 标签
													name : 'RSM.GOP.SIZE', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.VID.RATE', // 标签
													name : 'RSM.VID.RATE', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'VNC.PORT', // 标签
													name : 'VNC.PORT', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													hidden:true,
													hideLabel: 'true',
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.PA.AMOUNT', // 标签
													name : 'RSM.PA.AMOUNT', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.UID', // 标签
													name : 'RSM.UID', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.LOG_FILE_PATH', // 标签
													name : 'RSM.LOG_FILE_PATH', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.VNCSERVER.PORT', // 标签
													name : 'RSM.VNCSERVER.PORT', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.KEYLISTEN.PORT', // 标签
													name : 'RSM.KEYLISTEN.PORT', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.AIM_LISTENTPORT', // 标签
													name : 'RSM.AIM_LISTENTPORT', // name:后台根据此name属性取值
													allowBlank : false, // 是否允许为空
													labelStyle : 'color:blue;',
//													readOnly : true,
//													fieldClass : 'x-custom-field-disabled',
													anchor : '100%' // 宽度百分比
												},{
													fieldLabel : 'RSM.VNC_LISTENTPORT', // 标签
													name : 'RSM.VNC_LISTENTPORT', // name:后台根据此name属性取值
													allowBlank : false, // 是否允许为空
//													readOnly : true,
//													fieldClass : 'x-custom-field-disabled',
													labelStyle : 'color:blue;',
													anchor : '100%' // 宽度百分比
												},{
													fieldLabel : 'RSM.PROCESS_THREADS_AIM', // 标签
													name : 'RSM.PROCESS_THREADS_AIM', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比，
													labelStyle : 'color:blue;'
												},{
													fieldLabel : 'RSM.PROCESS_THREADS_VNC', // 标签
													name : 'RSM.PROCESS_THREADS_VNC', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比，
													labelStyle : 'color:blue;'
												}]
											}, {
												columnWidth : .20,
												layout : 'form',
												labelWidth : 120, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
													fieldLabel : 'area_id',
													name : 'area_id',
													xtype : 'textfield', // 设置为文字输入框类型
													allowBlank : true,
													anchor : '100%',
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'maxcount', // 标签
													name : 'maxcount', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'sd_width', // 标签
													name : 'sd_width', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%', // 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'sd_height', // 标签
													name : 'sd_height', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'hd_width', // 标签
													name : 'hd_width', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'hd_height', // 标签
													name : 'hd_height', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'vncserverport', // 标签
													name : 'vncserverport', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'recordadd', // 标签
													name : 'recordadd', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'pa_amount', // 标签
													name : 'pa_amount', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'uid', // 标签
													name : 'uid', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'chrome_type', // 标签
													name : 'chrome_type', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'process_threads_aim', // 标签
													name : 'process_threads_aim', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												}]
											}, {
												columnWidth : .25,
												layout : 'form',
												labelWidth : 90, // 标签宽度
												defaultType : 'textfield',
												border : false,
												items : [{
													fieldLabel : 'vncname', // 标签
													name : 'vncname', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%',// 宽度百
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'TVnet', // 标签
													name : 'TVnet', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'key_ignore',
													name : 'key_ignore',
													xtype : 'textfield', // 设置为文字输入框类型
													allowBlank : true,
													anchor : '100%',
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'proxy', // 标签
													name : 'proxy', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'listenport', // 标签
													name : 'listenport', // name:后台根据此name属性取值
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													allowBlank : true, // 是否允许为空
													anchor : '100%',// 宽度百
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'keytimeoutport', // 标签
													name : 'keytimeoutport', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%', // 宽度百分比
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'keylistenport', // 标签
													name : 'keylistenport', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													readOnly : true,
													fieldClass : 'x-custom-field-disabled',
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'timeoutforecast', // 标签
													name : 'timeoutforecast', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'log_file_path', // 标签
													name : 'log_file_path', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'log_file', // 标签
													name : 'log_file', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'log_level', // 标签
													name : 'log_level', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' ,// 宽度百分比
													labelStyle : 'color:green;'
												},{
													fieldLabel : 'status', // 标签
													name : 'status', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													anchor : '100%' // 宽度百分比
												},{
													fieldLabel : 'busi_code', // 标签
													name : 'busi_code', // name:后台根据此name属性取值
													allowBlank : true, // 是否允许为空
													hidden:true,
													hideLabel: 'true',
													anchor : '100%' // 宽度百分比
												}]
											}]
								},{
									xtype : 'label',
									labelAlign : 'right',
									html : '<font color="red">备注：蓝色为resourcemag.config中参数，绿色为vncconfig.ini中参数,黑色为RSM的运行状态</font>',
									anchor : '100%'
								}]
					});
			


			// 布局
			// 如果把form作为center区域的话,其Height属性将失效。
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'center',
									layout : 'border',
									border : false,
									items : [qForm,myForm]
								}]
					});

			// 表单加载数据的回调函数
			function loadCallBack(qForm) {
				if (!qForm.isValid()){
					Ext.Msg.show({title:'警告',msg:'请填写VNCW的地址！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
					return;
				}else{
					var params = qForm.getValues();
				myForm.form.load({
							waitMsg : '正在处理数据,请稍候...',// 提示信息
							waitTitle : '提示',// 标题
							url : './appGroupManager.ered?reqCode=queryRsmConf',// 请求的url地址
							params : params,
							// method : 'GET',// 请求方式
							success : function(form, action) {// 加载成功的处理函数
							},
							failure : function(form, action) {// 加载失败的处理函数
								Ext.Msg.show({title:'提示',msg:'数据查询失败,请检测地址是否正确！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});
							}
						});
				}
			}


			/**
			 * 表单提交(表单自带Ajax提交)
			 */
			function submitTheForm(qForm) {
				if (!myForm.getForm().isValid())
					return;
				var params = qForm.getValues();
				var s = eval(params);
				var vncw_ip = s["vncw_ip"];
				myForm.form.submit({
							url : './appGroupManager.ered?reqCode=saveRsmConf&vncw_ip='+vncw_ip,
							waitTitle : '提示',
							method : 'POST',
							waitMsg : '正在处理数据,请稍候...',
							success : function(form, action) { // 回调函数有2个参数
								Ext.Msg.show({title:'提示',msg:'参数修改成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
								qForm.form.reset();
								myForm.form.reset();
				
							},
							failure : function(form, action) {
								Ext.Msg.show({title:'提示',msg:'参数修改失败！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});
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
//	    	alert(area_text);
	    }
	    }	
	})	
}
