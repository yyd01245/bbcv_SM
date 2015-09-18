/**
 * viewport自适应布局实例
 * 
 * @author XiongChun
 * @since 2010-11-27
 */
Ext.onReady(function() {
			var viewport = new Ext.Viewport({
						layout : 'border',
						items : [ {
									region : 'west',
									split : true,
									title : 'west',
									collapsible : true,
									width:550,
									html : '这是west区域'
								},{
									region : 'center'
								//	collapsible : true,
									//width:0
								//	html : '这是center区域'
								}]
					});
		});