/**
 * 代码表管理
 * 
 * @author XiongChun
 * @since 2010-02-13
 *  { name: 'lastChange', type: 'date', dateFormat: 'n/j h:ia' }
 */
Ext.onReady(function() {
	
    // create the data store
    var store = new Ext.data.ArrayStore({
        fields: [
           { name: 'mean' },
           { name: 'key' },
           { name: 'value' },
           { name: 'remark' }
           
         
       ]
    });


    // sample static data for the store
    var myData = [
['版本号', 'version', 'vs-001','备注说明'],
['IP地址','192.168.100.11', '8080','备注说明'],
['端口号', 'port', '9/1 12:00am',''],
['负载量', 'capacity', '5000','备注说明'],
['redis服务IP', 'redis_IP', '192.168.200.93','备注说明'],


];

// manually load local data
store.loadData(myData);

//var tpl = new Ext.XTemplate('<tpl for=".">',
//                                    '<div class="thumb-wrap">',
//                                       '<table><th>1</th><th>2</th><th>3</th><tr>',
//                                       '<td>{company}</td><td>{price}</td><td>{lastChange}</td>',
//                                      '</tr></table>',
//                                    '</div>',
//                                '</tpl>',
//                               '<div class="x-clear"></div>');

//var tpl = new Ext.XTemplate( 
//        '<table width="100%" cellpadding="0" cellspacing="0"><tr><td>编号</td><td>中文含义</td><td>key</td><td>value</td></tr>', 
//        <span style="color: rgb(255, 0, 0);">'<tpl for=".">',</span> 
//        '<tr>', 
//        <span style="color: rgb(255, 0, 0);">'<td>{#}</td><td>{company}</td><td>{price}</td><td>{lastChange}</td></tr>'</span>, 
//        <span style="color: rgb(255, 0, 0);">'</tpl></table>'</span> 
//    );

var tpl = new Ext.XTemplate(  
        '<table border=1 cellpadding=0 cellspacing = 0>',  
                '<tr><td colspan=4 align=center>网关信息</td></tr>' ,  
          '<tr >',  
            '<td width=90 style="color:blue;">编号</td>', 
            '<td width=90 style="color:blue;">中文含义</td>', 
            '<td width=90 style="color:blue;">key</td>', 
            '<td width=90 style="color:blue;">value</td>',  
          '</tr>',  
         '<tpl for=".">',  
          '<tr>',  
            '<td>{#}</td>',  
            '<td>{mean}</td>',  
            '<td>{key}</td>',  
            '<td><input type="text" value="{value}"></td>',  
          '</tr>',  
         '</tpl>',  
        '<tr><td colspan=4 align=center><input type="button" value="保存"></td></tr></table>'  
    ); 

var panel = new Ext.Panel({
        id: 'images-view',
        frame: true,
        width: 500,
      //  autoWidth: true,
        autoHeight: true,
        collapsible: true,
        layout: 'fit',
        autoHeight:true,
        title: '网关信息',

        items: new Ext.DataView({
            store: store,
            tpl: tpl,
            autoHeight: true,
            multiSelect: true,
            overClass: 'x-view-over',
            itemSelector: 'div.thumb-wrap',
            emptyText: 'No images to display'
        })
    });
panel.render(document.body);

});
	