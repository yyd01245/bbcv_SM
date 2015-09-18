Ext.onReady(function(){
	window.setTimeout("serverUpdate();",10000);
	
	var taskRunner;
	if(taskRunner!=null){
		taskRunner.stopAll();
	}
	var task = {
	    run: function(){
			serverUpdate();
	    },
	    interval: 60000 //60 second
	}
	taskRunner = new Ext.util.TaskRunner();
	taskRunner.start(task);
})

function serverUpdate(){
		Ext.Ajax.request({
		    url: 'crsm/crsmServerMonitor.ered?reqCode=queryServerByApiAndInsertDB',
		    success: function(response) {
		    }
		})
	}