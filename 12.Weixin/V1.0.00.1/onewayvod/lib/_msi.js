var request = require("superagent");
var token_cache = {};
var service_url = "";
var service_name = "UCMS";

var wapi = require("./wapi");
var cfg = require("./config");
var logger = require("./logger");
var _tip = require("./tip");

var msi_host = "218.108.50.254:18080";
var msi_name = "MAG";
var password = "123456";

var export_obj = {
    join: function(openid, callback){
        post("http://"+msi_host+"/msi/user_access_req.do", {
            username: openid,
            passwd: password,
            version: "v1.0.00.1",
            appname: "KYSX",
            licence: "KYSX1234"
        },function(err, body){
            if( err ){
                if(body && body.return_code.toString() === "-2010"){
                    export_obj.register(openid, function(body, err){
                        if(err){
                            return;
                        }
                        export_obj.join(openid, callback);
                    })
                }
                else {
                    wapi.sendText(openid, _tip.join_fail)
                }
            }
            else {
                token_cache[openid] = body.token;
                service_url = body.service_url;
                callback();
            }
        }, "access", msi_name, msi_host);
    },
    register: function(openid, callback){
        post("http://"+msi_host+"/msi/user_regist_req.do",{
            username: openid,
            passwd: password
        }, function(err, body){
            if( err ){
                if(body && body.return_code.toString() === "-1007"){
                    wapi.sendText(openid, _tip.register_success);
                    callback && callback(body);
                }
                else {
                    callback && callback(body, err);
                    wapi.sendText(openid, _tip.register_fail)
                }
            }
            else {
                wapi.sendText(openid, _tip.register_success);
                callback && callback(body);
            }
        }, "regist", msi_name, msi_host)
    },
    nickname: function(openid, obj, cb){
        post("http://"+msi_host+"/msi/user_updateNickname_req.do", {
            username: openid,
            passwd: password,
            nickname: obj.nickname
        }, cb, "updateNickname", msi_name, msi_host)
    },
    bind: function(openid, channel, vod_page, callback){
        post("http://"+service_url+"/msi/user_bind_req.do", {
            username: openid,
            token: token_cache[openid],
            stream_id: channel,
            vod_page: vod_page
        }, function(err, body){
            if( err ){
                wapi.sendText(openid, _tip.bind_fail)
            }
            else {
                token_cache[openid] = body.new_token;
                wapi.sendText(openid, _tip.bind_success)
                callback && callback(body)
            }
        }, "bind");
    },
    play: function(openid, info, callback){
        post("http://"+service_url+"/msi/user_vodplay_req.do", {
            username: openid,
            token: token_cache[openid],
            url: info.rtsp_url,
            vodname: info.name,
            posterurl: info.tv_poster_path1
        }, function(err, body){
            if(err){
                wapi.sendText(openid, _tip.play_fail)
                callback && callback();
            }
            else {
                token_cache[openid] = body.new_token;
                wapi.sendText(openid, _tip.play_success(info.name));
                callback && callback(body);
            }
        }, "vodplay");
    },
    keyboard: function(openid, key_value, callback){
        post("http://"+service_url+"/msi/key_send_req.do", {
            key_type: cfg.key_type,
            key_value: key_value,
            username: openid,
            token: token_cache[openid]
        }, function(err, body){
            callback(body);
        }, "key_send")
    },
    unbind: function(openid, callback){
        post("http://"+service_url+"/msi/user_unbind_req.do", {
            username: openid,
            token: token_cache[openid]
        }, function(err, body){
            if( err ){
                wapi.sendText(openid, "解绑失败")
            }
            else {
                token_cache[openid] = body.new_token;
                wapi.sendText(openid, "解绑成功");
                callback && callback();
            }
        }, "unbind")
    },
    status: function(openid, callback){
        if( !token_cache[openid] ){
            callback(null, {
                status: 1
            });
            return;
        }
        post("http://"+service_url+"/msi/user_sessionquery_req.do", {
            username: openid,
            token: token_cache[openid]
        }, callback, "sessionquery")
    }
};

module.exports = export_obj;

function serialno(){
    return Date.now() + "" + Math.round((Math.random() * 9999));
}

function post(url, obj, callback, type, name, host ){
    name = name || service_name;
    host = host || service_url;

    loggerSend(name, host, type, obj);

    request.post(url)
        .type('form')
        .send(obj)
        .send({sequence: serialno()})
        .end(function(res){
            var body = res.body;
            loggerReturn(name, host, type, body);
            if( body.return_code.toString() === "0" ){
                loggerStatus(name, host, type, "Success")
                callback && callback(null, body);
            }
            else {
                loggerStatus(name, host, type, "Fail");
                callback && callback(true, body);
            }
        })
}

function loggerReturn(name, host, type, msg){
    logger.info("【Return】【"+name+"】【"+host+"】【"+type+"】:"+JSON.stringify(msg));
}

function loggerStatus(name, host, type, status){
    logger.info("【Send】【"+name+"】【"+host+"】【"+type+"】:"+status);
}

function loggerSend(name, host, type, msg){
    logger.info("【Send】【"+name+"】【"+host+"】【"+type+"】:"+JSON.stringify(msg));
}