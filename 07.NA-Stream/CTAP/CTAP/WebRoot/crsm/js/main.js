var appPlantDoc='';
var finalDoc='';
var cabinet ='';
var cabinetDom='';//服务器列表生成
var cabinetDom2='';//服务器列表生成
var cabinetDom0='';
var data;

var contextt1='';
var contextt2='';

Ext.onReady(function() {


	
	var viewport = new Ext.Viewport({
						layout : 'border',
						
						items : [{
									region : 'center',
									
									//title:'服务器总览',
									autoScroll : true,
									bodyStyle :'background:#CCDFEA',
									html:'<div id="appPlant"></div>'
								}]

					});
window.setTimeout("serverUpdate();",1000);
window.setTimeout("synchronizateAllServer();",1333);
	
	
	
	
})
//页面刷新
var task = {
    run: function(){
		serverUpdate();
    },
    interval: 30000 //20 second
}
var runner = new Ext.util.TaskRunner();
runner.start(task);

var finalDocc='';
var title;

finalDoc='';
function serverUpdate(){

	finalDocc ='';
	appPlantDoc='';
	Ext.Ajax.request({
		url : 'urm/GetewayConfig.ered?reqCode=queryCagGatewayInfoList',//查询机柜列表
		waitTitle : '提示',
		method : 'POST',
	//	async:false,
		waitMsg : '正在加载数据,请稍候...',
	    success: function(response1) {
			/******************遍历机柜********************/
			 var groupJson = eval('('+response1.responseText+')');
	    	 var total = groupJson.TOTALCOUNT;//机柜总数
	    	 var groupRoot = groupJson.ROOT;//机柜信息
			/*****************查询每个机柜上的服务器******************/
	    
			 
			 
			 
			 for(var i=0;i<total;i++){
			
					 var s=i;
					
					 var cabinetDomm='';//服务器列表生成
					 var cabinetDomm2='';//服务器列表生成
					 var edit='';
					 var cag_id=groupRoot[s].cag_id;
					 var cag_ip=groupRoot[s].cag_ip;
					 var cag_port=groupRoot[s].cag_port;
				     var cag_version = groupRoot[s].version;
				     var cag_thread_id  =  groupRoot[s].thread_id;
				
				     
				     getCkgGateway(cag_id);
				     getCsgGateway(cag_id);
				     
				     var serverConfigAddr  ='<div style="display:inline;font-size:1px;font-weight:100;color:blue;"><a href="#_" onclick="config()">[配置</a></div>';
				     var serverManagerAddr ='<div style="display:inline;font-size:1px;font-weight:100;color:blue;"><a href="#_" onclick="manager()">管理]</a></div>';
				     
				     
					  // 方法
				     
					  title='<div class="head" style="color:blue;font-weight:800;font-size:15px;" ><a href="#_" onclick="go()">接入网关<div class="head" style="display:inline;font-size:1px;font-weight:10;">('+cag_ip+":"+cag_port+')'+'正常'+'</div><div style="display:inline;font-size:1px;font-weight:100;">&nbsp;'+serverConfigAddr+'&nbsp;&nbsp;'+serverManagerAddr+'<br>版本号：'+cag_version+',运行进程：'+cag_thread_id+''+'<br></div></a></div>';//抬头
				//	  finalDocc+='<div class="box" style="margin:5px 10px 10px 100px;width:350px;height:230px">'+ title+cabinetDom+cabinetDom2+'</div>';
				

		    	 	   var id1 = "ckg"+cag_id;
		    	 	   var id2 = "csg"+cag_id;
		    	 	   
		    	 	   
		    	 	   
		    	 	  finalDocc+='<div class="box" style="margin:5px 10px 10px 100px;width:350px;height:230px">'+
		    	 	  title+
//					  '<div class ="name">接入网关：IP:'+cag_ip+':'+cag_port+'版本：'+cag_version+'</div></a>' +
					  '<div  id="'+id2+'"></div>'+
					  '<div  id="'+id1+'"></div>'+
					  '</div>'	;
			
			
//				 cabinet='<div class="box" style="margin:5px 10px 10px 100px;width:350px;height:230px">'+ title+cabinetDom+cabinetDom2+'</div>';
//				//机柜图形
			 }
			/****************************************/
			appPlantDoc +='<div class="appMonitor">'
				+finalDocc
				+'</div>';
			document.getElementById("appPlant").innerHTML =appPlantDoc;
	    }
	})
}



//根据主机IP获取本机下所有应用
function getCkgGateway(cag_id){	
	
	Ext.Ajax.request({				
	  	   			url: 'urm/GetewayConfig.ered?reqCode=queryCkgGatewayInfoList&cag_id='+cag_id,
	    			success: function(response) {
						contextt2='<div class ="each clearfix">键值网关:</div>';
		    			var appJson = eval('('+response.responseText+')');
		    			appTotal = appJson.TOTALCOUNT;
		    			appData = appJson.ROOT;
		    			for(var j=0;j<appTotal;j++){
		    				var  img ='';
		    			
		    	
		    				if(appData[j].ckg_status==0){
		    					img ='<img src="crsm/server_error.png">';
		    				}else{
		    					 img ='<img src="crsm/server_normal.png">';
		    				}
		    			//	var s='<div class ="each clearfix" >'+img+'<a href="#_" onclick=openWindows("'+appData[j].app_name+'")><strong>'+appData[j].ckg_ip+":"+appData[j].ckg_port+'</strong></a></div>'
		    				var s = 
		    					   '<div class ="each clearfix">'
					    	 	   +'<a href="#_" onclick=editServer("'+appData[j].ckg_ip+'","'+appData[j].ckg_port+'")>'
					    	 	   +img
					    	 	   +appData[j].ckg_ip+':'+appData[j].ckg_port+'&nbsp;&nbsp;版本号：vs00'+cag_id+'&nbsp;&nbsp;负载量：5000'	    	 	
					    	 	   +'</a>'
					    	 	   +'</div>';
		    				contextt2 +=s;
		    			}
		    			var idd = "ckg"+cag_id;
		    			document.getElementById(idd).innerHTML=contextt2;
	    			}
	  	   		})
}


//根据主机IP获取本机下所有应用
function getCsgGateway(cag_id){	
	
	Ext.Ajax.request({				
	  	   			url: 'urm/GetewayConfig.ered?reqCode=queryCsgGatewayInfoList&cag_id='+cag_id,
	    			success: function(response) {
		             contextt1='<div class ="each clearfix">信令网关:</div>';
		    			var appJson = eval('('+response.responseText+')');
		    			appTotal = appJson.TOTALCOUNT;
		    			appData = appJson.ROOT;
		    			for(var j=0;j<appTotal;j++){
		    				
		    				var img ='';
		    				if(appData[j].csg_status==0){
		    					img ='<img src="crsm/server_error.png">';
		    				}else{
		    					img ='<img src="crsm/server_normal.png">';
		    				}
		    				
		    					    				
		    			//	var s='<div class ="each clearfix">'+img+'<a href="#_" onclick=openWindows("'+appData[j].csg_ip+'")><strong>'+appData[j].csg_ip+":"+appData[j].csg_port+'</strong></a></div>'
  				
		    				var s = 
		    					   '<div class ="each clearfix">'
					    	 	   +'<a href="#_" onclick=editServer("'+appData[j].csg_ip+'","'+appData[j].csg_port+'")>'
					    	 	   +img
					    	 	   +appData[j].csg_ip+':'+appData[j].csg_port+'&nbsp;&nbsp;版本号：v00'+cag_id+'&nbsp;&nbsp;负载量：5000'	     	 	
					    	 	   +'</a>'
					    	 	   +'</div>';
		    				contextt1 +=s;
		    			}
		    			var iddd = "csg"+cag_id;
		    			
		    			document.getElementById(iddd).innerHTML=contextt1;
	    			}
	  	   		})
}


var openErrTab = function(){
	parent.addTab('urm/AccessGatewayInfo.ered?reqCode=toManage','网关配置信息','011317060111','云终端接入平台 -> 配置管理 -> 终端接入配置管理 -> 网关配置','tab_blank.png ');
};

 function config(){
	parent.addTab('urm/GetewayConfig.ered?reqCode=toManage','网关配置','011323','云终端接入平台 -> 配置管理 -> 网关配置','tab_blank.png ');
 };

 function manager(){
	parent.addTab('urm/ServerManager.ered?reqCode=toManage','服务器管理','011324','云终端接入平台 -> 配置管理 -> 服务器管理','tab_blank.png ');
};

	function statusImg(value){
		if(value=='1'){
			return "crsm/server_error.png";
		}else if(value=='2'){
			return "crsm/server_normal.png";
		}else if(value=='3'){
			return "crsm/server_offline.png";
		}else{
		//	return "../server_none.png";
			return "crsm/server_normal.png";
		}
	}
	
	
	function go(){
		
		parent.addTab('urm/GetewayConfig.ered?reqCode=toManage','网关配置信息','011323','云终端接入平台 -> 配置管理 -> 终端接入配置管理 -> 网关配置','tab_blank.png ');
       
	}
	function changeType(value){
		if(value=='SD'){
			return "信息1";
		}else if(value=='HD'){
			//return "版本号：VS-001"+"&nbsp;运行状态：正常<br>"+"&nbsp;&nbsp;&nbsp;负载量：8080"+"&nbsp;进程名称：ProjectNO称称称";
			return "运行状态：正常"+"&nbsp;负载量：8080";
		}else{
			return null;
		}
	}
	/**********************机柜维护开始*****************************/
	var addCabinetFormPanel = new Ext.form.FormPanel({});

	var addCabinetWindow = new Ext.Window({});

			function editCabinet(CabinetId,CabinetContainer,listsize){}
			function updateCabinet(){}
			function deleteCabinet(){//删除机柜
				addCabinetFormPanel.form.submit({});
			}

	/**********************机柜维护结束*****************************/


	function newServer(k,CabinetId){}
	/*******************S 编辑服务器开始 S********************/
	/*******************S 非正常状态 S*************************************/
	var serverForm = new Ext.form.FormPanel({});
	var serverWindow = new Ext.Window({});
	/****************E 非正常结束 E********************/
	/****************S 正常开始 S**********************/
	function openWindow(tstatus, ip,port,CabinetID) {};
	/***************E 正常结束 E*****************/
	function editServer(serverIP,status,cabinet_id,serverPort){}
	
	
var taskRunner;
if(taskRunner!=null){
    taskRunner.stopAll();
}
var taskserver = {
    run: function(){
    	synchronizateAllServer();
    },
	interval: 59000 //60 second
}
taskRunner = new Ext.util.TaskRunner();
taskRunner.start(taskserver);

function synchronizateAllServer(){
	Ext.Ajax.request({
	    url: 'urm/DataSynchronizate.ered?reqCode=queryAllCagInfoAndUpdateDB',
	    success: function(response) {
	    }
	})
}