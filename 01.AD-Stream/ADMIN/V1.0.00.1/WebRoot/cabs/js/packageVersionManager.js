/**
 * 升级包上传
 * 
 * @author shenhy	
 * @since 2012-5-11
 */
Ext.onReady(function() {
	
	Ext.Msg.minWidth  = 300 ;
	Ext.Msg.maxWidth = 360 ;
					 
//	var chinaStore = new Ext.data.Store({
//				proxy : new Ext.data.HttpProxy({
//							url : './appGroupManager.ered?reqCode=queryValidGroups'
//						}),
//				reader : new Ext.data.JsonReader({
//							totalProperty : 'TOTALCOUNT',
//							root : 'ROOT'
//						}, [{
//									name : 'group_name'
//								}, {
//									name : 'group_id'
//								}])
//			});	
//	
	
    var mode = "http";
	// 复选框
	var sm = new Ext.grid.CheckboxSelectionModel();

	// 定义自动当前页行号
	var rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});

	// 定义列模型
	var cm = new Ext.grid.ColumnModel([ rownum, sm, {
		header : '升级包编号', 
		dataIndex : 'version_id',
		width : 100
	},{
		header : '升级包版本号',
		dataIndex : 'update_version',
		width : 100
	},{
		header : '升级包类型',
		dataIndex : 'jar_type',
		renderer:ZIPTYPERender,
		width : 100
	},{
		header : '升级包名',
		dataIndex : 'file2',
		width : 100
//	},{
//		header : '升级脚本',
//		dataIndex : 'file2',
//		width : 100
	},{
		header : '升级包路径',
		dataIndex : 'targetaddr',
		sortable : true
	} ]);

	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : './packageVersion.ered?reqCode=queryFileDatas'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'TOTALCOUNT',
			root : 'ROOT'
		}, [ {
			name : 'version_id'
		}, {
			name : 'targetaddr'
		}, {
			name : 'update_version'
		}, {
			name : 'jar_type'
		}, {
			name : 'status'
		}, {
			name : 'jar_size'
		}, {
			name : 'file2'
//		}, {
//			name : 'file2'
		}])
	});

	// 每页显示条数下拉选择框
	var pagesize_combo = new Ext.form.ComboBox({
		name : 'pagesize',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : [ 'value', 'text' ],
			data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
					[ 100, '100条/页' ], [ 250, '250条/页' ], [ 500, '500条/页' ] ]
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
		items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
	});

	// 表格工具栏
	var tbar = new Ext.Toolbar({
		items : [ {
			text : '上传',
			iconCls : 'uploadIcon',
			handler : function() {
				mode = 'http';
				firstWindow.show();
			}
		}, '-',{
			text : '修改',
			iconCls : 'page_edit_1Icon',
			handler : function() {
				initEditCodeWindow();
			}
		}, '-', {
			text : '删除',
			iconCls : 'deleteIcon',
			handler : function() {
				delFiles();
			}
		} ]
	});

	// 表格实例
	var grid = new Ext.grid.GridPanel({
		title : '<span style="font-weight:bold;">升级包管理</span>',
		height : 500,
		frame : true,
		autoScroll : true,
		region : 'center', // 和VIEWPORT布局模型对应，充当center区域布局
		store : store, // 数据存储
		stripeRows : true, // 斑马线
		cm : cm, // 列模型
		sm : sm, // 复选框
		tbar : tbar, // 表格工具栏
		bbar : bbar,// 分页工具栏
		viewConfig : {
		// 不产横向生滚动条, 各列自动扩展自动压缩, 适用于列数比较少的情况
		// forceFit : true
		},
		loadMask : {
			msg : '正在加载表格数据,请稍等...'
		}
//		autoExpandColumn :'version_addr'
	});

	grid.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
		var store = pGrid.getStore();
		var record = store.getAt(rowIndex);
		var fieldName = pGrid.getColumnModel().getDataIndex(columnIndex);
		// columnIndex为小画笔所在列的索引,缩阴从0开始
		// 这里要非常注意!!!!!
		if (fieldName == 'download' && columnIndex == 2) {
			var fileid = record.get("jar_record_id");
			// 通过iFrame实现类ajax文件下载
			// 这个很重要
			var downloadIframe = document.createElement('iframe');
			downloadIframe.src = './packageVersion.ered?reqCode=downloadFile&fileid=' + fileid;
			downloadIframe.style.display = "none";
			document.body.appendChild(downloadIframe);
		}
	});

	// 页面初始自动查询数据
	store.load({
		params : {
			start : 0,
			limit : bbar.pageSize
		}
	});

	// 布局模型
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [ grid ]
	});

	var firstForm = new Ext.form.FormPanel({
		id : 'firstForm',
		name : 'firstForm',
		fileUpload : true, // 一定要设置这个属性,否则获取不到上传对象的
		labelWidth : 100,
		defaultType : 'textfield',
		labelAlign : 'right',
		bodyStyle : 'padding:5 5 5 5',
		items : [ {
			fieldLabel : '选择升级包',
			id : 'file1',
			name : 'file1', // 必须为file1/file2/file3/file4/file5.目前Web标准上传模式支持最多5个文件的批量上传
			xtype : 'fileuploadfield', // 上传字段
			allowBlank : false,
			anchor : '100%',
			regex: /^(.*\.tar\.gz)$/,
			buttonText:'浏览...',
			regexText : '升级包格式不正确，应该是".tar.gz"文件'
//		}, {
//			fieldLabel : '选择升级脚本',
//			id : 'file2',
//			name : 'file2', // 必须为file1/file2/file3/file4/file5.目前Web标准上传模式支持最多5个文件的批量上传
//			xtype : 'fileuploadfield', // 上传字段
//			allowBlank : false,
//			anchor : '100%'
		}, {
			fieldLabel : '升级包版本号',
			id : 'update_version',
			name : 'update_version',
			value : '0.0.00.0',
			allowBlank : false,
			anchor : '100%'
		},new Ext.form.ComboBox({
			name : 'jar_type',
			hiddenName : 'jar_type',
			store : ZIPTYPEStore,
			mode : 'local',
			triggerAction : 'all',
			valueField : 'value',
			displayField : 'text',
			//value : '1',
			fieldLabel : '升级包类型',
			emptyText : '请选择...',
			allowBlank : false,
			forceSelection : true,
			editable : false,
			typeAhead : true,
			anchor : '100%'
		})]
	});

	var firstWindow = new Ext.Window({
		title : '<span>上传升级包</span>', // 窗口标题
		layout : 'fit', // 设置窗口布局模式
		width : 400, // 窗口宽度
		height : 180, // 窗口高度
		closable : true, // 是否可关闭
		collapsible : true, // 是否可收缩
		maximizable : true, // 设置是否可以最大化
		closeAction : 'hide',
		animCollapse : true,
		animateTarget : Ext.getBody(),
		border : false, // 边框线设置
		constrain : true, // 设置窗口是否可以溢出父容器
		// pageY : 20, // 页面定位X坐标
		pageX : document.body.clientWidth / 2 - 500 / 2, // 页面定位Y坐标
		modal : true,
		items : [ firstForm ], // 嵌入的表单面板
		buttons : [ { // 窗口底部按钮配置
			text : '上传', // 按钮文本
			iconCls : 'uploadIcon', // 按钮图标
			handler : function() { // 按钮响应函数
				submitTheForm();
			}
		}, { // 窗口底部按钮配置
			text : '重置', // 按钮文本
			iconCls : 'tbar_synchronizeIcon', // 按钮图标
			handler : function() { // 按钮响应函数
				firstForm.form.reset();
			}
		} ]
	});
	
	var editCodeWindow, editCodeFormPanel;
	editCodeFormPanel = new Ext.form.FormPanel({
				id : 'editCodeFormPanel',
				name : 'editCodeFormPanel',
				fileUpload : true, // 一定要设置这个属性,否则获取不到上传对象的
				labelWidth : 100,
				defaultType : 'textfield',
				labelAlign : 'right',
				bodyStyle : 'padding:5 5 5 5',
				items : [{
					fieldLabel : '升级包编号',
					name : 'version_id',
					readOnly : true,
					fieldClass : 'x-custom-field-disabled',
					anchor : '100%'
				},{
					fieldLabel : '选择升级包',
					id : 'file2',
					name : 'file2', // 必须为file1/file2/file3/file4/file5.目前Web标准上传模式支持最多5个文件的批量上传
					xtype : 'fileuploadfield', // 上传字段
					allowBlank : false,
					anchor : '100%',
					buttonText:'浏览...',
					regex: /^(.*\.tar\.gz)$/,
					regexText : '升级包格式不正确，应该是".tar.gz"文件'
//				},{
//					fieldLabel : '选择升级脚本',
//					id : 'file2',
//					name : 'file2', // 必须为file1/file2/file3/file4/file5.目前Web标准上传模式支持最多5个文件的批量上传
//					xtype : 'fileuploadfield', // 上传字段
//					allowBlank : false,
//					anchor : '100%'
				}, {
					fieldLabel : '升级包版本号',
					name : 'update_version',
					allowBlank : false,
					anchor : '100%'
				},new Ext.form.ComboBox({
					name : 'jar_type',
					hiddenName : 'jar_type',
					store : ZIPTYPEStore,
					mode : 'local',
					triggerAction : 'all',
					valueField : 'value',
					displayField : 'text',
					//value : '1',
					fieldLabel : '升级包类型',
					emptyText : '请选择...',
					allowBlank : false,
					forceSelection : true,
					editable : false,
					typeAhead : true,
					readOnly : true,
					fieldClass : 'x-custom-field-disabled',
					anchor : '100%'
				})]
			});

	editCodeWindow = new Ext.Window({
				title : '<span>修改应用</span>', // 窗口标题
				layout : 'fit', // 设置窗口布局模式
				width : 400, // 窗口宽度
				height : 200, // 窗口高度
				closable : true, // 是否可关闭
				collapsible : true, // 是否可收缩
				maximizable : true, // 设置是否可以最大化
				closeAction : 'hide',
				animCollapse : true,
				animateTarget : Ext.getBody(),
				border : false, // 边框线设置
				constrain : true, // 设置窗口是否可以溢出父容器
				// pageY : 20, // 页面定位X坐标
				pageX : document.body.clientWidth / 2 - 500 / 2, // 页面定位Y坐标
				modal : true,
				items : [ editCodeFormPanel ], // 嵌入的表单面板
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						if (runMode == '0') {
							Ext.Msg.show({title:'提示',
									msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!',buttons:Ext.Msg.OK,
									icon:Ext.Msg.WARNING});
							return;
						}
						updateCodeItem();
					}
				}]

			});
	grid.addListener('rowdblclick', initEditCodeWindow);
	
	/**
	 * 初始化代码修改出口
	 */
	function initEditCodeWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
				Ext.Msg.show({title:'提示',msg:'请先选中要修改的升级包！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
//		record = grid.getSelectionModel().getSelected();
		var rows = grid.getSelectionModel().getSelections();
		record = rows[rows.length-1];
		if (record.get('editmode') == '0') {
			Ext.Msg.show({title:'提示',msg:'您选中的记录为系统内置的代码对照,不允许修改！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			
			return;
		}
		editCodeWindow.show();
		clearForm(editCodeFormPanel.getForm());
		editCodeFormPanel.getForm().loadRecord(record);
	}
	
	/**
	 * 表单提交(表单自带Ajax提交)
	 */
	function submitTheForm() {
		if (!firstForm.form.isValid()) {
			return;
		}
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
		
			return;
		}
		var requesturl = './packageVersion.ered?reqCode=doUpload';
		firstForm.form.submit({
			url : requesturl,
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在上传文件,请稍候...',
			timeout: 60000, // 60s
			success : function(form, action) {
				firstForm.form.reset();
				firstWindow.hide();
				
//				Ext.Ajax.request({
//				    url: './packageVersion.ered?reqCode=saveFileDatas',
//				    success: function(response) {
//						Ext.MessageBox.alert('提示', action.result.msg);
//					},
//					failure: function(response) {
//				    }
//				});
				
				if(mode=='http') queryFiles();
				Ext.Msg.show({title:'提示',msg:'升级包上传成功!',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
			},
			failure : function(response) {
				Ext.Msg.show({title:'提示',msg:'升级包上传失败，存在同名升级包！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
			}
		});
	}
	/**
	 * 修改表单
	 */
	function updateCodeItem() {
		if (!editCodeFormPanel.form.isValid()) {
			return;
		}
		editCodeFormPanel.form.submit({
					url : './packageVersion.ered?reqCode=updatePackage',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editCodeWindow.hide();
						store.reload();
						Ext.Msg.show({title:'提示',msg:'升级包更新成功！',buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
					},
					failure : function(form, action) {
						Ext.Msg.show({title:'提示',msg:'升级包更新失败，升级包重名！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					}
				});
	}

	// 查询表格数据
	function queryFiles() {
		store.load({
			params : {
				start : 0,
				limit : bbar.pageSize
			}
		});
	}

	// 获取选择行
	function getCheckboxValues() {
		// 返回一个行集合JS数组
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.MessageBox.show({title:'提示', msg:'您没有选中任何数据!',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		// 将JS数组中的行级主键，生成以,分隔的字符串
		var strChecked = jsArray2JsString(rows, 'version_id');
//		Ext.MessageBox.alert('提示', strChecked);
		// 获得选中数据后则可以传入后台继续处理
	}

	// 生成一个下载图标列
	function downloadColumnRender(value) {
		return "<a href='javascript:void(0);'><img src='" + webContext
				+ "/resource/image/ext/download.png'/></a>";
	}

	/**
	 * 删除文件
	 */
	function delFiles() {
		if (runMode == '0') {
			Ext.Msg.show({title:'提示',msg:'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
			return;
		}
		var rows = grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.Msg.show({title:'提示',msg:'请先选中要删除的升级包！',buttons:Ext.Msg.OK,icon:Ext.Msg.WARNING});
		
			return;
		}
		var strChecked = jsArray2JsString(rows, 'version_id');
		Ext.Msg.confirm('请确认', '你真的要删除选中的该升级包吗?', function(btn, text) {
			if (btn == 'yes') {
				showWaitMsg();
				Ext.Ajax.request({
					url : './packageVersion.ered?reqCode=delFiles',
					success : function(response) {
						var resultArray = Ext.util.JSON.decode(response.responseText);
						var msg =resultArray.ret_msg;
						if(resultArray.success){
							Ext.Msg.show({title:'提示',msg:msg,buttons:Ext.Msg.OK,icon:Ext.Msg.INFO});
						}else{
							Ext.Msg.show({title:'提示',msg:msg,buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
						}
						
						store.reload();
					},
					failure : function(response) {
						Ext.Msg.show({title:'提示',msg:'升级包删除失败！',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
					},
					params : {
						strChecked : strChecked
					}
				});
			}
		});
	}

});