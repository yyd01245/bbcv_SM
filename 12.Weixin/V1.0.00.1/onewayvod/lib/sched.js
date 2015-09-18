var later = require("later");
var mail = require("./mail");
var wapi = require("./wapi");
var ejs = require("ejs");
var os = require("os");
var moment = require("moment");

//设为当前时区
later.date.localTime();
//每日信息统计,每日8点
var dailyInfo = later.parse.recur().on(8).hour();

var t_dailyInfo = later.setInterval(function(){
    var obj = {};
    obj.title = "微信接口调用报告";
    var keys = Object.keys(wapi.tongji);
    obj.list = [];
    var error_keys = Object.keys(wapi.tongji.error);
    error_keys.forEach(function(error){
        var e_obj = {}
        e_obj.type = "错误"
        e_obj.name = error;
        e_obj.num = wapi.tongji.error[error];
        obj.list.push(e_obj)
    })
    var success_keys = Object.keys(wapi.tongji.success);
    success_keys.forEach(function(success){
        var e_obj = {}
        e_obj.type = "成功";
        e_obj.name = success;
        e_obj.num = wapi.tongji.error[success];
        obj.list.push(e_obj)
    })
    obj.list.push({
        type: "接口调用总数",
        "name": "全部接口",
        "num": wapi.tongji.all
    })

    wapi.tongji = {
        "all": 0,
        "error": {},
        "success": {}
    }

    var api_str = headerTemp;
    api_str += ejs.render(dailyTemp, obj);
    api_str += systemInfo();
    api_str += '<h3>'+moment().format("YYYY-MM-DD HH.mm")+'</h3>'

    mail.send("单向点播微信后台"+moment().format("YYYY-MM-DD")+"接口报告", '', api_str);
}, dailyInfo);

var exception = 0;
process.on("uncaughtException", function(){
    exception++;
});

var headerTemp = '<h1>自昨日8点到今日8点的单向微信后台接口调用情况</h1>'
var dailyTemp = '<h2><%= title %></h2>'+
        '<table>'+
            '<tr>'+
                '<th>类型</th>'+
                '<th>接口</th>'+
                '<th>数量</th>'+
            '</tr>'+
            '<% list.forEach(function(obj){ %>'+
            '<tr>'+
                '<td><%= obj.type %></td>'+
                '<td><%= obj.name %></td>'+
                '<td><%= obj.num %></td>'+
            '</tr>'+
            '<% }) %>'+
        '</table>';

function systemInfo(){
    var str =  '<h2>系统信息</h2>'+
            '<table>'+
                '<tr>'+
                    '<td>hostname:</td><td>'+os.hostname()+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>type:</td><td>'+os.type()+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>platform:</td><td>'+os.platform()+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>arch:</td><td>'+os.arch()+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>release:</td><td>'+os.release()+'</td>'+
                '</tr>'+
            '</table>'
    var networkstr = '<h2>网络信息</h2>'+
        '<table>'+
            '<tr>'+
                '<th>name</th>'+
                '<th>address</th>'+
                '<th>family</th>'+
                '<th>internal</th>'+
            '</tr>';

    var network = os.networkInterfaces();
    var network_keys = Object.keys(network)
    network_keys.forEach(function(nkey){
        network[nkey].forEach(function(item){
            networkstr+= '<tr>'+
                    '<td>'+nkey+'</td>'+
                    '<td>'+item.address+'</td>'+
                    '<td>'+item.family+'</td>'+
                    '<td>'+item.internal+'</td>'+
                '</tr>'
        })
    })

    networkstr += '</table>'

    return str + networkstr;
}