
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
		
	var diagram = new Ext.Panel({
		region : 'center',
		split : true,
		title:'<span>chrome插件升级选择<span>',//标题
		collapsible:true,//右上角的收缩按钮，设为false则不显示
		html :'<p style="text-align:center; padding:20px 0;"><img src="update.png" onclick="formWindowInit()" style="margin:80px 60px;cursor:pointer;width:50px;height:100px;" /></p>'
	});
	
	var logPanel = new Ext.Panel({
		region : 'south' ,
		split : true ,
		height : 160,
		title : '升级日志',
		autoScroll : true,
		collaspile : true ,
		collased : true,
		bodyStyle :'background:#CCDFEA',
		html:'<div id="logData"></div>'
	});
	// 布局
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [diagram,logPanel]
	});
});
				

	var serverStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						 url :'./crsmServerMonitor.ered?reqCode=queryServerListNoStatus'
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : 'TOTALCOUNT',
						root : 'ROOT'
					}, [{
								name : 'server_id'
							}, {
								name : 'server_ip'
							}])
		});
//	 var serverSelect = new Ext.form.MultiSelect({
//                  renderTo: Ext.getBody(),
//                  width: 200,
//                  editable: false,
//                  store: serverStore,
//                  valueField: 'name',
//                  displayField: "name",
//                  mode: 'local',
//                  triggerAction: 'all',
//                  allowBlank: false,
//                  emptyText: '请选择',
//                  maxHeight: 200 //下拉框的最大高度
//              });

var serverCombo = new Ext.form.ComboBox({   
		hiddenName : 'server_ip',
         store : serverStore,
         displayField:'server_ip',   //store也可以动态的去加载，大家自己改一下就OK
         valueField:'server_id', 
         mode : 'remote',
         allowBlank : false,
         typeAhead: true,  
//         width : 150,
         fieldLabel : '服务器IP',
         tpl:'<tpl for="."><div class="x-combo-list-item"><span><input type="checkbox" {[values.check?"checked":""]} value="{[values.server_id]}" /></span><span >{server_ip}</span></div></tpl>',   
         triggerAction: 'all',   
         emptyText:'请选择...',   
         selectOnFocus:true,   
         onSelect : function(record, index){   
             if(this.fireEvent('beforeselect', this, record, index) != false){   
                 record.set('check',!record.get('check'));   
                var str=[];//页面显示的值   
                 var strvalue=[];//传入后台的值   
                 this.store.each(function(rc){ 
                     if(rc.get('check')){   
                         str.push(rc.get('server_id'));   
                         strvalue.push(rc.get('server_ip'));   
                     }   
                 });   
                this.setValue(str.join());   
                 this.value=strvalue.join();//赋值
                this.fireEvent('select', this, record, index);   
             }
         },
         listeners:{
         expand : function(value){//监听下拉事件
        this.store.each(function(rc){
         if(value.value==rc.get('server_id')){
         rc.set('check',true);//选中
        }
                 });
         }
         }
     }); 


var formpanel = new Ext.form.FormPanel({
		id : 'formpanel',
		name : 'formpanel',
		defaultType : 'textfield',
		labelAlign : 'right',
		labelWidth : 60,
		frame : true,
		fileUpload : true,
		items : [{
					fieldLabel : '升级文件',
					id : 'file1',
					name : 'file1', // 必须为file1/file2/file3/file4/file5.目前Web标准上传模式支持最多5个文件的批量上传
					xtype : 'fileuploadfield', 
					regex: /^(.*\.tar.gz)$/,
					regexText : '导入文件格式不正确，只支持‘.tar.gz’文件',
					allowBlank : false,
					anchor : '99%'
					
				},serverCombo]
});

var formWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 180,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '升级chrome插件',
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
					text : '升级',
					iconCls : 'acceptIcon',
					handler : function() {
				if (!formWindow.getComponent('formpanel').form.isValid())
					return
						var theFile = Ext.getCmp('file1').getValue();
//						if (Ext.isEmpty(theFile)) {
//							Ext.Msg.show({title:'提示',msg:'请先选择您要升级的chrome相关文件!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
//							return;
//						}
//						if (theFile.substring(theFile.length - 7, theFile.length) == ".tar.gz") {
//							Ext.Msg.show({title:'提示',msg:'您选择的文件格式不对,系统只支持.tar.gz文件,请重新选择文件!',buttons:Ext.MessageBox.OK,icon:Ext.Msg.WARNING});
//							return;
//						}
						formpanel.form.submit({
									url : './crsmServerMonitor.ered?reqCode=updateChromePlush',
									waitTitle : '提示',
									method : 'POST',
									waitMsg : '正在处理数据,请稍候...',
									success : function(form, action) {
										formWindow.hide();
										var obj = Ext.decode(action.response.responseText) ;
										var result = "<br /><p style = 'font-size: 16px;font-weight : bold'>******升级成功******</p><Br />";
										result += showResult(obj);;
										document.getElementById('logData').innerHTML=result;
										form.reset();

									},
									failure : function(form, action) {
//										var msg = action.result.msg;
//										Ext.MessageBox.alert('提示', '参数数据保存失败<br>' );
										formWindow.hide();
										var obj = Ext.decode(action.response.responseText) ;
										var result = "<br /><p style = 'font-size: 16px;font-weight : bold'>******升级失败******</p><Br />";
										result += showResult(obj);
										document.getElementById('logData').innerHTML=result;
									}
								});

					}
				}]
});

function showResult(object){
	var result = "<B>"+object.result+"</B>" ;
	return result ;
}

function formWindowInit() {
	formWindow.show();
	formpanel.getForm().reset();

}
