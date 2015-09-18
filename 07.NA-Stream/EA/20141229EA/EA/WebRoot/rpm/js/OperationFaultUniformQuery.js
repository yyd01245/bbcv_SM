var userTraceData;
var userTraceTotal;
var userTraceJson;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth = 360;
	var contextt='';
			var qForm = new Ext.form.FormPanel({
						region : 'north',
						title : '<span>查询条件<span>',
						collapsible : true,
						border : true,
						labelWidth : 100, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						buttonAlign : 'center',
						height : 70,
						items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : .34,
										layout : 'form',
										bodyStyle : 'padding:10 20 0', // 表单元素和表单面板的边距
										labelWidth : 80, // 标签宽度
										defaultType : 'textfield',
										border : false,
										items : [{
													fieldLabel : '机顶盒号',
													name : 'term_id',
													xtype : 'textfield',
													anchor : '100%',
													allowBlank:false,
													msgTarget:'side',
													blankText:'机顶盒号不能为空',
													minLength : 10,
													minLengthText:'机顶盒号必须是完整的',
													enableKeyEvents : true,
													listeners:{
														specialkey:function(field,e){
															if(e.getKey()==Ext.EventObject.ENTER){
																if(!qForm.getForm().isValid())
																		return; 
																queryBalanceInfo(qForm.getForm());
															}
														}
													}
												}]
							},{
								columnWidth :.5,
								defaultType : 'textfield',
								buttonAlign : 'right',
								layout:'form' ,
								border : false ,
								buttons : [{
									text : '查询',
									iconCls : 'previewIcon',
									handler : function() {
										if(!qForm.getForm().isValid())
											return;
										queryBalanceInfo(qForm.getForm());
									}
								}, {
									text : '重置',
									iconCls : 'tbar_synchronizeIcon',
									handler : function() {
										qForm.getForm().reset();
									}
								}]
							}]
						}]
						});
		
		
		//回调信息
			var myForm = new Ext.form.FormPanel({
						region : 'center',
//						title : '<span>激活信息<span>',
						collapsible : false,
						layout : 'border',
						border : false,
						labelWidth : 80, // 标签宽度
						// frame : true, //是否渲染表单面板背景色
						labelAlign : 'right', // 标签对齐方式
						bodyStyle : 'padding:5 5 0', // 表单元素和表单面板的边距
						buttonAlign : 'center',
						items : [{
							region : 'center',
							layout : 'border',
							height :'70%',
							bodyStyle :'background:#CCDFEA',
							border:false,
							items : [{
								region : 'center',
								height:'100%',
								layout : 'column',
								border : false,
//								autoScroll : true,
								bodyStyle :'background:#CCDFEA',
								items:[{
									region : 'center',
									split : true,
									height:'40%',
									width:'100%',
									title : '<span>终端信息<span>',
									bodyStyle :'background:#CCDFEA',
									collapsible : true,
									items : [{
												layout : 'form',
												labelWidth : 90, // 标签宽度
												defaultType : 'textfield',
												bodyStyle :'background:#CCDFEA',
												border : false,
												items : [{
															fieldLabel : '机顶盒型号', // 标签
															name : 'termType', // name:后台根据此name属性取值
															anchor : '90%',// 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}, {
															fieldLabel : '硬件版本号',
															name : 'hardVersion',
															xtype : 'textfield', // 设置为文字输入框类型
															anchor : '90%',
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '软件版本号', // 标签
															name : 'softVersion', // name:后台根据此name属性取值
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '高清标识', // 标签
															name : 'HDmark', // name:后台根据此name属性取值
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '行业标识', // 标签
															name : 'workMark', // name:后台根据此name属性取值
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '网络ID', // 标签
															name : 'regionID', // name:后台根据此name属性取值
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														}]
											}]
								},{
									region : 'south',
									split : true,
									height:'60%',
									width:'100%',
									title : '<span>平台信息<span>',
									bodyStyle :'background:#CCDFEA',
									items : [{
												layout : 'form',
												labelWidth : 90, // 标签宽度
												defaultType : 'textfield',
												bodyStyle :'background:#CCDFEA',
												border : false,
												items : [{
															fieldLabel : '机顶盒号', // 标签
															name : 'term_id', // name:后台根据此name属性取值
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															readOnly:true,
															anchor : '90%'// 宽度百分比
														},{
															fieldLabel : '终端类型',
															name : 'term_type',
															xtype : 'textfield', // 设置为文字输入框类型
															anchor : '90%',
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '终端能力', // 标签
															name : 'term_ability', // name:后台根据此name属性取值
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '运营区域',
															name : 'area_id',
															xtype : 'textfield', // 设置为文字输入框类型
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'
														},{
															fieldLabel : '网络区域', // 标签
															name : 'region_id', // name:后台根据此name属性取值
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															readOnly:true,
															anchor : '90%' // 宽度百分比
														},{
//															xtype : 'datefield',
															fieldLabel : '默认服务', // 标签
															name : 'server_type', // name:后台根据此name属性取值
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															readOnly:true,
															anchor : '90%' // 宽度百分比
														},  {
															fieldLabel : '验证TOKEN',
															name : 'AUTHTOKEN',
															readOnly:true,
															xtype : 'textfield', // 设置为文字输入框类型
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'
														},{
															fieldLabel : 'TsgUrl',
															name : 'tsgUrl',
															readOnly:true,
															xtype : 'textfield', // 设置为文字输入框类型
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;',
															anchor : '90%'
														},{
															fieldLabel : '激活地址', // 标签
															name : 'ActiveUrl', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
														},{
															fieldLabel : '门户地址', // 标签
															name : 'IndexUrl', // name:后台根据此name属性取值
															allowBlank : true, // 是否允许为空
															anchor : '90%', // 宽度百分比
															readOnly:true,
															style:'background : none;border:0 solid;border-bottom: #000000 1px solid;'
//														},{
//															fieldLabel : 'RETURN_URL', // 标签
//															name : 'user_agent', // name:后台根据此name属性取值
//															allowBlank : true, // 是否允许为空
//															anchor : '90%', // 宽度百分比
////															regex : /^(\S+)$/,
////															regexText : '不能包含空格',
//															maxLength:100,
//															maxLengthText : '用户代理的长度不能超过100',
//															prod.nebula.listeners:{
//																blur:function(obj){
//																	obj.setValue(Ext.util.Format.trim(obj.getValue()));
//																}
//															}
//														},{
//															fieldLabel : 'RETURN_URL', // 标签
//															name : 'user_agent', // name:后台根据此name属性取值
//															allowBlank : true, // 是否允许为空
//															anchor : '90%', // 宽度百分比
////															regex : /^(\S+)$/,
////															regexText : '不能包含空格',
//															maxLength:100,
//															maxLengthText : '用户代理的长度不能超过100',
//															prod.nebula.listeners:{
//																blur:function(obj){
//																	obj.setValue(Ext.util.Format.trim(obj.getValue()));
//																}
//															}
//														},{
//															fieldLabel : 'RETURN_URL', // 标签
//															name : 'user_agent', // name:后台根据此name属性取值
//															allowBlank : true, // 是否允许为空
//															anchor : '90%', // 宽度百分比
////															regex : /^(\S+)$/,
////															regexText : '不能包含空格',
//															maxLength:100,
//															maxLengthText : '用户代理的长度不能超过100',
//															prod.nebula.listeners:{
//																blur:function(obj){
//																	obj.setValue(Ext.util.Format.trim(obj.getValue()));
//																}
//															}
														}]
											}]
								}]
							}]
						}]
					});
		
			// 布局
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [qForm, {
							region:'center',
							layout : 'border',
							border : false,
							items:[myForm,{
								region : 'east',
								split : true,
								height:'100%',
								width:"70%",
								layout : 'column',
								border : false,
								bodyStyle :'background:#CCDFEA',
								items:[{
									region : 'center',
									width:'100%',
									split : true,
									title : '<span>各模块日志信息<span>',
									bodyStyle :'background:#CCDFEA',
									autoScroll : true,
									items:[{
										layout : 'form',
										labelWidth : 60, // 标签宽度
										defaultType : 'textarea',
										bodyStyle :'background:#CCDFEA',
										border : false,
										items:[{
											fieldLabel : 'CSCS',
											height:'30%',
											id:'sm',
											autoScroll : true,
											anchor : '100%',
											width:'100%',
											readOnly:true
										},{
											fieldLabel : 'URM',
											height:'30%',
											id:'urm',
											autoScroll : true,
											anchor : '100%',
											width:'100%',
											readOnly:true
										},{
											fieldLabel : 'AIM',
											height:'30%',
											id:'aim',
											autoScroll : true,
											anchor : '100%',
											width:'100%',
											readOnly:true
										}]
									}]
								}]
							}]	
						}]
					});	
			
			function queryBalanceInfo(pForm) {
				var params = pForm.getValues();
				var s = eval(params);
				var term_id = s["term_id"];			
				myForm.form.load({
					waitMsg : '正在处理数据,请稍候...',// 提示信息
					waitTitle : '提示',// 标题
					url: '../rpm/resourthData.ered?reqCode=OperationFaultUniformQuery&term_id='+term_id,
					params : params,
					success : function(form, action) {
						var t = action.result.data;
						if(!t.success){
							Ext.Msg.show({title:'提示',msg:'无法查到对应机顶盒相关信息！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
							return ;
						}
						document.getElementById("sm").innerHTML=t.cscsInfo;
						document.getElementById("urm").innerHTML=t.urmInfo;
						document.getElementById("aim").innerHTML=t.aimInfo;
					},
					failure : function(form,action){
					Ext.Msg.show({title:'提示',msg:'无法查到对应机顶盒相关信息！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.ERROR});				
					}
				})
			}
		});
	