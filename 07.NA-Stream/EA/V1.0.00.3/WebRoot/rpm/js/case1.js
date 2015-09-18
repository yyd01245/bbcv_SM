var ips = [["192.168.1.1","192.168.1.2","192.168.1.3","192.168.1.4","192.168.1.5","192.168.1.6","192.168.1.7","192.168.1.8"],
	["192.168.2.1","192.168.2.2","192.168.2.3","192.168.2.4","192.168.2.5","192.168.2.6","192.168.2.7","192.168.2.8"]];
var ex1 = '';
var ex2 = '';
for(var i=0; i<8;i++){
		status = 1;
		ex1 = ex1+'<a href="#" onclick="openWindow('+i+','+status+')"><img src="1.gif"/></a>';
		if(i<=6){
			for(var j=0;j<20;j++){
				ex1 = ex1 + "&nbsp;";
			}
		}
		if(i==7){
			ex1 = ex1 + '<br>';
			for(var k=0;k<8; k++){
				ex1 = ex1 + ips[0][k]; 
				if(k<=6){
					for(var m=0;m<18;m++){
						ex1 = ex1 + "&nbsp;";
					}
				}
			}
		}
	}
for(var i=0; i<8;i++){
		status = 2;
		if(i==3||i==7){
			ex2 = ex2+'<a href="#" onclick="openWindow('+i+','+status+')"><img src="2.gif"/></a>';
		}else{
			ex2 = ex2+'<a href="#" onclick="openWindow('+i+','+status+')"><img src="1.gif"/></a>';
		}
		if(i<=6){
			for(var j=0;j<20;j++){
				ex2 = ex2 + "&nbsp;";
			}
		}
		if(i==7){
			ex2 = ex2 + '<br>';
			for(var k=0;k<8; k++){
				ex2 = ex2 + ips[1][k]; 
				if(k<=6){
					for(var m=0;m<18;m++){
						ex2 = ex2 + "&nbsp;";
					}
				}
			}
		}
	}
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
	var expander = new Ext.grid.RowExpander({
		tpl :'<p style=margin-left:70px; name="expander"><br>'+ex1+'<br></p>',
		// 屏蔽双击事件
		expandOnDblClick : false
	});
//	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
			expander, {
				header : '组名',
				dataIndex : 'groupname'
			}, {
				id : 'status',
				header : '状态',
				dataIndex : 'status',
				renderer:addImage
			}, {
				header : '',
				dataIndex : 'paramid',
				hidden : false,
				hidden : true,
				width : 80,
				sortable : true
			}, {
				id : 'remark',
				header : '',
				dataIndex : 'remark'
			}]);
	function addImage(value){
		if(value=='1'){
			return '<img src="server_normal.png">'
		}else if(value=='2'){
			return '<img src="server_error.png">' 
		}
	} 
	expander.on("expand",function(d,r,body,rowIndex){ 

      //d为rowpander对象，r为单击是当前表格行,body为返回对象，可不用,rowIndex为当前表格是第几行 
        var data = Ext.getCmp("grid").getStore().getAt(rowIndex).get("status");  //通过表格的store获取当前表格行里detail属性的数据 
        //alert(data);
        var h = document.getElementsByName("expander"); //获取rowpander里的div，控制div（返回的是一个div数组) 
        if(data==1){
        	return;
        }else{
        	h[rowIndex].innerHTML = '<br>'+ex2+'<br>';
			return;
        }
     });
 
	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'terms.json'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'groupname'
								}, {
									name : 'status'
								}, {
									name : 'paramvalue'
								}, {
									name : 'remark'
								}])
			});

	var pagesize_combo = new Ext.form.ComboBox({
				name : 'pagesize',
				hiddenName : 'pagesize',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['value', 'text'],
							data : [[10, '10条/页'], [20, '20条/页'],
									[50, '50条/页'], [100, '100条/页'],
									[250, '250条/页'], [500, '500条/页']]
						}),
				valueField : 'value',
				displayField : 'text',
				value : '50',
				editable : false,
				width : 85
			});
	var number = parseInt(pagesize_combo.getValue());
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

	var bbar = new Ext.PagingToolbar({
				pageSize : number,
				store : store,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo]
			});

	var grid = new Ext.grid.GridPanel({
				title : '<span style="font-weight:bold;">主机服务器监控</span>',
				iconCls : 'configIcon',
				height : 500,
				//width:600,
				autoScroll : true,
				id:'grid',
				region : 'center',
				store : store,
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
				autoExpandColumn : 'remark',
//				sm : sm,
				cm : cm,
				plugins : expander,
				tbar : [
				],
				bbar : bbar
			});
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize
				}
			});
	grid.on('rowdblclick', function(grid, rowIndex, event) {
				editInit();
			});
	grid.on('sortchange', function() {
				// grid.getSelectionModel().selectFirstRow();
			});

	bbar.on("change", function() {
				// grid.getSelectionModel().selectFirstRow();
			});
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});
});
function openWindow(id,status){
		var ip,position,normaltime;
		var name,state,runtime;
		var flag=false;
		
		if(status==1){ 
			if(id==0){
				ip = "192.168.1.1";
				position = "第1排第1列";
				}
			else if(id==1){
				ip = "192.168.1.2";
				position = "第1排第2列";
				}
			else if(id==2){
				ip = "192.168.1.3";
				position = "第1排第3列";
				}
			else if(id==3){
				ip = "192.168.1.4";
				position = "第1排第4列";
				}
			else if(id==4){
				ip = "192.168.1.5";
				position = "第1排第5列";
				}
			else if(id==5){
				ip = "192.168.1.6";
				position = "第1排第6列";
				}
			else if(id==6){
				ip = "192.168.1.7";
				position = "第1排第7列";
				}
			else if(id==7){
				ip = "192.168.1.8";
				position = "第1排第8列";
				}
		}
		
		if(status==2){
			if(id==0){
				ip = "192.168.2.1";
				position = "第2排第1列";
				}
			else if(id==1){
				ip = "192.168.2.2";
				position = "第2排第2列";
				}
			else if(id==2){
				ip = "192.168.2.3";
				position = "第2排第3列";
				}
			else if(id==3){
				ip = "192.168.2.4";
				position = "第2排第4列";
				}
			else if(id==4){
				ip = "192.168.2.5";
				position = "第2排第5列";
				}
			else if(id==5){
				ip = "192.168.2.6";
				position = "第2排第6列";
				}
			else if(id==6){
				ip = "192.168.2.7";
				position = "第2排第7列";
				}
			else if(id==7){
				ip = "192.168.2.8";
				position = "第2排第8列";
				}	
		}
     	
     	normaltime = "8小时56分45秒";
     	
     	if(status==2){
     		if(id==3||id==7){
     			state = "<font color='red'>异常</font>";
     		}else{
     			state = "<font color='green'>正常</font>";
     			flag = true;
     			
     		}
     	}else{
     		state = "<font color='green'>正常</font>";
     		flag = true;
     	}
     	
     	runtime = "3天8小时56分45秒";
     	
     	var taskRunner;
     	
     	var cm1 = new Ext.grid.ColumnModel([  
              {header:'IP',dataIndex:'ip'},  
              {header:'位置',dataIndex:'position',width:90},  
              {header:'正常运行时间',dataIndex:'normaltime',width:110}  
          ]);  
     	
     	var cm2 = new Ext.grid.ColumnModel([  
              {header:'名称',dataIndex:'name',width:90},  
              {header:'状态',dataIndex:'state',width:90},  
              {header:'运行时间',dataIndex:'runtime',width:120}  
          ]);  
     	                                
     	var data1 = [  
              [ip,position,normaltime] 
          ];  
     	var data2;
     	if(flag){
     		data2 = [
     	      ["CAAS",state,runtime],
     	      ["CABS",state,runtime],
     	      ["CSCS",state,runtime],
     	      ["CUMS",state,runtime],
     	      ["URM",state,runtime]
     		     	   ];
     	}else{
     		data2 = [
     	      ["CAAS",state,runtime],
     	      ["CABS","<font color='green'>正常</font>",runtime],
     	      ["CSCS",state,runtime],
     	      ["CUMS",state,runtime],
     	      ["URM","<font color='green'>正常</font>",runtime]
     	       			];
     	}
     	                                
      var storeInfo1 = new Ext.data.Store({  
          proxy: new Ext.data.MemoryProxy(data1),  
          reader: new Ext.data.ArrayReader({},[  
              {name:'ip'},  
              {name:'position'},
              {name:'normaltime'}
          ])  
      });  
      var storeInfo2 = new Ext.data.Store({
    	  proxy: new Ext.data.MemoryProxy(data2),  
          reader: new Ext.data.ArrayReader({},[     
             {name:'name'},  
             {name:'state'},
             {name:'runtime'}
          ]) 
      });
      
      storeInfo1.load(); 
      storeInfo2.load();
      

     	var listView1 = new Ext.grid.GridPanel({
     	    title: '机器信息',
     	    collapsible:true,//右上角上的那个收缩按钮，设为false则不显示
     	    autoHeight:true,
     	    store: storeInfo1,
     	    cm:cm1
     	});
     	
     	var listView2 = new Ext.grid.GridPanel({
     	    title: '服务状态',
     	    collapsible:true,//右上角上的那个收缩按钮，设为false则不显示
     	    autoHeight:true,
     	    store: storeInfo2,
     	    cm:cm2
     	});
     	
     	var panel = new Ext.Panel({
	        contentEl:'my2Dc_MsChart_div',
	        applyTo:'my2Dc_MsChart_panel_div'
        });
        
        if(taskRunner!=null){
			taskRunner.stopAll();
		}
		requestUpdate();
		var task = {
				run : function() {
					requestUpdate();
				},
				interval : 3000
			};

		taskRunner = new Ext.util.TaskRunner();
		taskRunner.start(task);
		
		var firstWindow = new Ext.Window({
			title : '<span>监控信息</span>', // 窗口标题
			iconCls : 'imageIcon',
			layout : 'border', // 设置窗口布局模式
			width : 900, // 窗口宽度
			height : 430, // 窗口高度
			// tbar : tb, // 工具栏
			closable : false, // 是否可关闭
			closeAction : 'hide', // 关闭策略
			bodyStyle : 'background-color:#FFFFFF',
			collapsible : true, // 是否可收缩
			maximizable : true, // 设置是否可以最大化
			modal : true,
			animateTarget : Ext.getBody(),
			border : true, // 边框线设置
			pageY : 20, // 页面定位Y坐标
			pageX : 50, // 页面定位X坐标
			constrain : true,
			// 设置窗口是否可以溢出父容器
			items:[
			{
							title : '<span>主机信息</span>',
							iconCls : 'layout_contentIcon',
							collapsible : true,
							layout:'fit',
							//split : true,
							width : 305,
							region : 'west',
							//autoScroll : true,
							//items : [msgPanel]
							items : [listView1,listView2]
						}, {
							region : 'center',
							width:595,
							title : '主机监控图',
							iconCls : 'application_view_listIcon',
							collapsible : true,
							titleCollapse : false,
							//下拉层的动画效果必须关闭,否则将出现Flash图标下拉动画过场异常的现象
							animCollapse : false,
							maximizable : true,
							border : false,
							closable : false,
							items : [panel]
						}
			],
			buttonAlign : 'center',
			buttons : [ 
				{
				text : '关闭',
				iconCls : 'deleteIcon',
				handler : function() {
					firstWindow.hide();
					taskRunner.stopAll();
					taskRunner==null;
				}
			} ]
		});
		firstWindow.show(); // 显示窗口
	}
var requestUpdate = function() {
				Ext.Ajax.request({
							url : './serverMonitor.ered?reqCode=getData',
							success : function(response, opts) {
								var resultArray = Ext.util.JSON
										.decode(response.responseText);
								var xmlstring = resultArray.xmlstring;
								updateChartXML('my2Dc_MsChart', xmlstring);
							},
							failure : function(response, opts) {
								Ext.MessageBox.show({title:'提示',msg: '获取监控数据失败',buttons:Ext.Msg.OK,icon:Ext.Msg.ERROR});
							},
							params : {}
						});
		}