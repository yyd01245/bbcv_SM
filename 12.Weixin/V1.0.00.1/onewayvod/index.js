var qs = require("querystring");
var wechat = require('wechat');
var express = require("express");
var cors = require("cors");

var bodyParser = require("body-parser");
var wapi = require("./lib/wapi");
var cfg = require("./lib/config");
var updateUser = require("./lib/updateUser");
var user_area = require("./lib/user_area");
var logger = require("./lib/logger");
var menu = require("./lib/menu");
var mysql = require("./lib/mysql");
var qrcode = require("./lib/qrcode");
var sched = require("./lib/sched");
var _msi = require("./lib/msi");
var _news_format = require("./lib/news_format");
var app = express();

app.use(cors());
app.use(bodyParser.json());

app.use(express.query());

var route = require("./lib/route");

app.use("/wechat/oneway", route);

app.use("/wechat/oneway", menu);

app.use('/wechat', wechat('bbcvision').text(function (message, req, res, next) {
    res.send("");
    loggerWeChat(message);
}).image(function (message, req, res, next) {
    // TODO
}).voice(function (message, req, res, next) {
    // TODO
}).video(function (message, req, res, next) {
    // TODO
}).location(function (message, req, res, next) {
    // TODO
}).link(function (message, req, res, next) {
    // TODO
}).event(function (message, req, res, next) {
    res.send("");
    loggerWeChat(message);
    var username = message.FromUserName;
        if( message.Event === "subscribe" ){
            _msi.register(username);

            if(message.EventKey){
                var num = parseInt(message.EventKey.replace("qrscene_", ""));
                if(num){
                    scan(message, num);
                }
            }
        }
        else if( message.Event.toLowerCase() === "scan" ){
            var serial = parseInt(message.EventKey);
            scan(message, serial);
        }
        else {
            user_area.getArea(username, function(err, result) {
                if (err || result.length === 0) {
                    wapi.sendText(username, "-_- 您的信息识别出错了，试试扫一下电视上的二维码")
                    return;
                }

                var msi = _msi.retrieve(result[0].area);
                if(!msi){
                    wapi.sendText(username, ">_< 您所在地区的暂不支持点播服务");
                    return;
                }
                if( message.Event === "scancode_waitmsg" ){
                    var scan_wait_info = message.ScanCodeInfo.ScanResult;
                    scan(message, scan_wait_info);
                }
                else if( message.Event.toLowerCase() === "click" ){
                    if( message.EventKey === "unbind" ){
                        msi.unbind(username);
                    }
                    else if(message.EventKey === "keyboard"){
                        msi.status(username, function(err, body){
                            if(err){
                                wapi.sendText(username, "-_- 哇哦，系统忙不过来了，要不，您等会儿~~");
                            }
                            else {
                                var status = body.status.toString();
                                if( status === "1" ){
                                    wapi.sendText(username, "^_^ 要先扫电视上面的二维码哦");
                                }
                                else if(status === "2") {
                                    wapi.sendText(username, "^_^ 您目前没有电影控制权呢");
                                }
                                else if(status === "3") {
                                    _news_format.keyboard(username);
                                }
                            }
                        })
                    }
                    else if(message.EventKey === "movielist"){
                        msi.status(username, function(err, body){
                            if(err){
                                wapi.sendText(username, "-_- 哇哦，系统忙不过来了，要不，您等会儿~~");
                            }
                            else {
                                var status = body.status.toString();
                                if( status === "1" ){
                                    wapi.sendText(username, "^_^ 要先扫电视上面的二维码哦");
                                }
                                else if(status === "2" || status === "3") {
                                    _news_format.movielist(username);
                                }
                            }
                        })
                    }
                }
            });
        }
}).middlewarify());

function scan(message, serial){
    var _info = qrcode.getQRMsg(serial) || qrcode.getQRMsgByUrl(serial);
    var username = message.FromUserName;
    logger.info("scan Info: " + JSON.stringify(_info));
    if(_info){
        user_area.setArea(username, _info.area, function(err, result){
            if(err){
                wapi.sendText(username, ">_< 伤不起，好像出故障了呢");
                return;
            }
            var msi = _msi.retrieve(_info.area);
            if(!msi){
                wapi.sendText(username, ">_< 您所在地区的暂不支持点播服务");
                return;
            }
            var result = util.format(cfg["app_"+_info.area+"_url"], _info.channel, _info.vodid, "0");
            //var result = "http://192.168.100.11:8882/NSCS/a?id="+_info.channel+"&vp="+_info.vodid+"&r=0";
            mysql.check_info(_info.area, _info.vodid, function(info){
                if( !info ){
                    wapi.sendText(username, ">_< 请扫描电视机上二维码哦");
                    return;
                }
                logger.info("video Info of " + _info.vodid + " :" + JSON.stringify(info));
                msi.join(username, function(){
                    qrcode.setUserCV(username, serial);
                    msi.bind(username, _info.channel, result, function(){
                        _news_format.bind(username, info);
                    })
                    updateUser(username);
                });
            });
        })
    }
    else {
        wapi.sendText(username, ">_< 给跪了，这码，我们认不出啊");
    }
}

function loggerWeChat(message){
    logger.info("【Receive】【WeChat】【"+message.MsgType+"】:"+JSON.stringify(message));
}

function loggerObj(key, value){
    logger.info("【Parse】:"+key+" = "+value)
}

app.listen(cfg.port);
logger.info('OWD listened '+cfg.port+' port');

process.title = "nodeOWD";