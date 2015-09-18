/**
 * 应用监控
 * 
 * @author shy
 * @since 2012-8-24
 */
var allstore = new Array();
var groupJson;
var total;
var ex=0;
var appnames ; //array
var contentCSCS='';
var contentMSG='';
var contentDETAL='';
var document_CTAS='';
var document_CSCS='';
var document_URM='';
var document_AIM='';
var status_CSCS;
var status_CTAS;
var status_URM;
var status_AIM;
var msg;
var msgShow='';
var j=1;
var p=1;
var k=0;
var l=0;
var m=0;
var sum;
Ext.onReady(function() {
	Ext.Msg.minWidth = 300 ;
	Ext.Msg.maxWidth =360 ;
	var viewport = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'west',
									split : true,
									width: '50%',
									height: '100%',
									autoScroll : true,
									bodyStyle :'background:#CCDFEA',
									html:'<div id="appPlant"></div>'
								},{
									region : 'center',
									split : true,
									//autoScroll : true,
									bodyStyle :'background:#CCDFEA',
									items :[{
												region : 'center',
												split : true,
												height: 200,
												autoScroll : true,
												border: false,
												height: 250,
												autoScroll : true,
												border: false,
												title : '总体信息显示',
												bodyStyle :'background:#CCDFEA',
												html:'<div id="appMSG"></div>'
											}, {
												region : 'south',
												split : true,
												height:'auto',
												border: false,
												title : '日志信息提示',
												bodyStyle :'background:#CCDFEA',
												html:'<div id="appDETAL"></div>'}]
								}]

					});
	window.setTimeout("msgUpdate();",1000);
	
});
//页面即时刷新
var task = {
    run: function(){
		requestUpdate();
    },
    interval: 3000 //1 second
}
var task1 = {
    run: function(){
		msgUpdate();
    },
    interval: 20000 //20 second
}
var runner = new Ext.util.TaskRunner();
var runner1 = new Ext.util.TaskRunner();
runner.start(task);
runner1.start(task1);


function openWindow(app_name) {
	var apps = app_name.toLowerCase();
	var a = apps.split("-");	
	var appName = a[0];
	window.location.href='applicationMonitor.ered?reqCode='+appName+'Init&app_name='+app_name;
	
};

	
var msgUpdate = function(){	
	Ext.Ajax.request({
	    url: './applicationMonitor.ered?reqCode=getMsgByMsgType',
	    success: function(response) {
	    var groupJson1 = eval('('+response.responseText+')');
	    var totall = groupJson1.TOTALCOUNT;
	    msg = groupJson1.ROOT;
	    msgShow='';
	    for(var i=0;i<totall;i++){
	    	msgShow+=msg[i]+'<br>';
	    }
	    }	
	})
	contentDETAL ='<marquee scrollamount=2 behavior=scroll direction =up onmouseover="this.stop()" onmouseout="this.start()">'+msgShow+'</marquee>';
	document.getElementById("appDETAL").innerHTML = contentDETAL;
	
}		
var requestUpdate = function() {
	Ext.Ajax.request({
	    url: './applicationMonitor.ered?reqCode=getCountByMsgType',
	    success: function(response) {
	    var count = eval('('+response.responseText+')');
	    sum = count==0?20000:count;
	    }
	})
	Ext.Ajax.request({
	    url: './applicationMonitor.ered?reqCode=getAllApps',
	    success: function(response) {
		 contentCSCS='';
	   	document_CSCS='';
	   	document_CTAS='';
	   	document_AIM='';
	   	document_URM='';
	   	ex=k=l=m=0
	    groupJson = eval('('+response.responseText+')');
	    total = groupJson.TOTALCOUNT;
	    appnames = groupJson.ROOT;
	    for(var i=0;i<total;i++){
		    var appname= appnames[i].app_name;
		    var status = appnames[i].status;
		    var a = appname.split("-");
		    var platNames =a[0];
		    if(status!="1"){
		    	ex++;
		    	if(platNames=="CTAS"){
		    	
		    		document_CTAS+='<div class ="each clearfix"><img src="../rpm/server_error.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
	 				status_CTAS ='<img src="../rpm/2.gif"/>';
	 				
		    }else if(platNames=="CSCS"){
		    		k++;
		    		document_CSCS+='<div class ="each clearfix"><img src="../rpm/server_error.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_CSCS ='<img src="../rpm/2.gif"/>';
		    		
		    }else if(platNames=="URM"){
		    		l++;
		    		document_URM+='<div class ="each clearfix"><img src="../rpm/server_error.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_URM ='<img src="../rpm/2.gif"/>';
		    		
		    }else if(platNames=="AIM"){
		    		m++;
		    		document_AIM+='<div class ="each clearfix"><img src="../rpm/server_error.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_AIM ='<img src="../rpm/2.gif"/>';
		    }
		    }else{
		    	if(platNames=="CTAS"){
		    	
		    		document_CTAS+='<div class ="each clearfix"><img src="../rpm/server_normal.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
	 				status_CTAS ='<img src="../rpm/1.gif"/>';
		    		
		    }else if(platNames=="CSCS"){
		    		k++;
		    		document_CSCS+='<div class ="each clearfix"><img src="../rpm/server_normal.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_CSCS ='<img src="../rpm/1.gif"/>';
		    		
		    }else if(platNames=="URM"){
		    		l++;
		    		document_URM+='<div class ="each clearfix"><img src="../rpm/server_normal.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_URM ='<img src="../rpm/1.gif"/>';
		    		
		    }else if(platNames=="AIM"){
		    		m++;
		    		document_AIM+='<div class ="each clearfix"><img src="../rpm/server_normal.png"/><a href="#_" onclick=openWindow("'+appname+'")><strong>'+appname+'('+j+'/'+j+')</strong></a></div>';
		  			status_AIM ='<img src="../rpm/1.gif"/>'; 
		    }
		    }
		    
	    }
	    contentCSCS = '<div class="appMonitor">' +
									'			<div class="border clearfix">' +
									'				<div class="box">' +
									'					<div class ="name">云终端接入平台(CTAS)</div>' +	
									document_CTAS+
									'          		</div>'	+
	    							'				<div class="box">' +
									'					<div class ="name">云会话控制平台(CSCS)</div>' +
									document_CSCS+
									'				</div>' +								
									'				<div class="box">' +
									'					<div class ="name">网络资源调度平台(URM)</div>' +
									document_URM+
									'				</div>' +										
									'				<div class="box">' +
									'					<div class ="name">云应用接入平台(AIM)</div>' +
									document_AIM+
									'				</div>' +
									'				<a href="../cabs/serverMonitor.ered?reqCode=pageInit">' +								
									'				<div class="box">' +									
									'					<div class ="name">云应用承载平台(VNCM)</div>' +
									'				</div></a>' +	
									'				<a href="../cabs/serverMonitor.ered?reqCode=pageInit">' +								
									'				<div class="box">' +									
									'					<div class ="name">高清状态监控平台(RSM)</div>' +
									'				</div></a>' +
									'       	</div>' +
									'       </div>';
	    	document.getElementById("appPlant").innerHTML = contentCSCS;
	    	contentMSG = '       	<div class ="show">' +
									'				<strong class="strong">当前状况：</strong><br>    						应用总数：'+total+     							'<br>正常：'+(total-ex)+     							'<br><h1 class="red">异常：'+ex+'</h1>' +
									'				<strong class="strong">在线用户：</strong>'+sum+
									'       	</div>' ;
	    	document.getElementById("appMSG").innerHTML = contentMSG;	
			

	      var store = new Array();
	      for(var i=0; i<total; i++){
	    	  store[i] = new Ext.data.JsonStore({
	  	   		url: './serverMonitor.ered?reqCode=getMsgByApp&app_name'+appname,
		   		root: 'ROOT',
		   		fields: [
		   			'host_ip','','app_name','msg','msg_type'
		   		]
		   	  });
		   	  allstore[i] = store[i];
	    	  store[i].load();
	      }
	    },
	    failure: function(response) {
	    }
	   
	});
	
		}