/**
 * 云用户批量导入
 * 
 * @author shy
 * @since 2012-5-28
 */
	
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
		
	var diagram = new Ext.Panel({
		region : 'center',
		split : true,
		title:'<span>授权文件查看及编辑<span>',//标题
		width:200,
		height : 500,
	//	background: 'url(../resource/image/shouquanbj.jpg) no-repeat #00FFFF',  
		collapsible:true,//右上角的收缩按钮，设为false则不显示
		html :'<p style="text-align:center; padding:140px 0;"><a href="./Edit.ered?reqCode=pageInit" style="margin:0 20px" title="编辑授权文件"><img src="../resource/image/bianji.jpg" alt="编辑授权文件"/></a><a href="./EmpowerFile.ered?reqCode=pageInit" title="查看授权文件" style="margin:0 120px"><img src="../resource/image/chakan.jpg"  style="margin:0 30px;cursor:pointer;" /></a></p>'
	});
	
	var logPanel = new Ext.Panel({
		region : 'sorch' ,
		split : true ,
		title : '导入日志',
		autoScroll : true,
		collaspile : true ,
		collased : true,
		bodyStyle :'background:#CCDFEA',
		html:'<div id="logData"></div>'
	});
	
	// 布局
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [diagram]
	});
});
					 
	
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
					allowBlank : true,
					anchor : '99%'
					
				}]
});

var formWindow = new Ext.Window({
		layout : 'fit',
		width : 380,
		height : 100,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '授权文件查看及编辑',
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
						var theFile = Ext.getCmp('theFile').getValue();
						if (Ext.isEmpty(theFile)) {
							Ext.Msg.show({title:'提示',msg:'请先选择您要导入的xls文件!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
							return;
						}
						if (theFile.substring(theFile.length - 4, theFile.length) != ".xls") {
							Ext.Msg.show({title:'提示',msg:'您选择的文件格式不对,只能导入.xls文件!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
							return;
						}
						formpanel.form.submit({
									url : './userManage.ered?reqCode=importExcel',
									waitTitle : '提示',
									method : 'POST',
									waitMsg : '正在处理数据,请稍候...',
									success : function(form, action) {
										Ext.Msg.show({title:'提示',msg:'批量导入成功！',buttons:Ext.MessageBox.OK,icon:Ext.Msg.INFO});
										formWindow.hide();
										var obj = Ext.decode(action.response.responseText) ;
										var result = "<br /><p style = 'font-size: 16px;font-weight : bold'>******导入成功******</p><Br />";
										result += showResult(obj);;
										document.getElementById('logData').innerHTML=result;
										store.load({
													params : {
														start : 0,
														limit : bbar.pageSize
													}
												});
										form.reset();

									},
									failure : function(form, action) {
//										var msg = action.result.msg;
//										Ext.MessageBox.alert('提示', '参数数据保存失败<br>' );
										formWindow.hide();
										var obj = Ext.decode(action.response.responseText) ;
										var result = "<br /><p style = 'font-size: 16px;font-weight : bold'>******导入失败******</p><Br />";
										result += showResult(obj);
										//document.getElementById("logData").innerHtml = result ;
										document.getElementById('logData').innerHTML=result;
									}
								});

					}
				}]
});

function showResult(object){
	var result = "<B>"+object.result+"</B>" ;
	result += "<B>"+object.importResult+"</B><br/>"
	return result ;
}

function formWindowInit() {
	formWindow.show();
	formpanel.getForm().reset();
		// 准备本地数据
//	var ipqamType_Store = new Ext.data.SimpleStore({
//				fields : ['nameType', 'codeType'],
//				data : [['MP2T_UNICAST', 'MP2T_UNICAST'], ['H264 _UNICAST', 'H264 _UNICAST']]
//			});
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './userManage.ered?reqCode=getUserManageByKey'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'subscriber_id'
								}, {
									name : 'user_no'
								}, {
									name : 'subscriber_source'
								}, {
									name : 'user_status'
								}, {
									name : 'password'
								}, {
									name : 'cust_name'
								}, {
									name : 'region_id'
								}, {
									name : 'org_id'
								}, {
									name : 'def_serv_id'
								}, {
									name : 'create_date'
								}, {
									name : 'connect_no'
								}, {
									name : 'connect_type'
								}])
			});

}
