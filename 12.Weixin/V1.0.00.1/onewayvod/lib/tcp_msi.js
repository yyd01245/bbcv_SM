var _tcp_server = require("./tcp_server");
var _wAPI = require("./wapi");
var _tip = require("./tip");
var _cfg = require("./config");
var logger = require("./logger");

var token_cache = {};
var password = "123456";

var tcp_map = {};

_tcp_server.emit.on("login", function(area){
    tcp_map[area] = new tcpMSI(area);
});

function tcpMSI(area){
    this.area = area;
    this.server = new _tcp_server(area);
}

tcpMSI.register = function(openid){
    var keys = Object.keys(tcp_map);
    keys.forEach(function(ele){
        if(tcp_map[ele]){
            tcp_map[ele].register(openid);
        }
    })
}

tcpMSI.nickname = function(openid, obj, cb){
    var keys = Object.keys(tcp_map);
    keys.forEach(function(ele){
        if(tcp_map[ele]){
            tcp_map[ele].nickname(openid, obj, cb);
        }
    })
}

tcpMSI.retrieve = function(area){
    return tcp_map[area];
}

tcpMSI.prototype.join = function(openid, cb){
    var msg = {
        "username": openid,
        "passwd": password,
        "version": "v1.0.00.1",
        "appname": "KYSX",
        "licence": "KYSX1234",
        "cmd": "user_access_req"
    };
    var _that = this;

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0") {
            token_cache[openid] = result.token;
            cb();
        }
        else if(return_code === "-2010"){
            logger.info("用户不存在，发起注册");
            _that.register(openid, function(result2){
                if(result2["return_code"].toString() === "0"){
                    logger.info("用户注册成功，重新接入");
                    _that.join(openid, cb);
                }
                else {
                    logger.info("自动注册失败，放弃注册，提示失败");
                    _wAPI.sendText(openid, _tip.register_fail)
                }
            })
        }
        else {
            _wAPI.sendText(openid, _tip.join_fail)
        }
    });
};

tcpMSI.prototype.register = function(openid, cb){
    var msg = {
        "username": openid,
        "passwd": password,
        "cmd": "user_regist_req"
    }

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0" || return_code === "-1007"){
            _wAPI.sendText(openid, _tip.register_success);
            cb && cb(result);
        }
        else {
            //_wAPI.sendText(openid, _tip.register_fail)
            cb && cb(result, true);
        }
    });
}

tcpMSI.prototype.nickname = function(openid, obj, cb){
    var msg = {
        "username": openid,
        "passwd": password,
        "nickname": obj.nickname,
        "cmd": "user_updateNickname_req"
    }

    this.server.send(msg, cb)
}

tcpMSI.prototype.bind = function(openid, channel, vod_page, cb){
    var msg = {
        "username": openid,
        "token": token_cache[openid],
        "stream_id": channel,
        "vod_page": vod_page,
        "cmd": "user_bind_req"
    }

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0"){
            token_cache[openid] = result.new_token;
            _wAPI.sendText(openid, _tip.bind_success);
            cb && cb(result)
        }
        else {
            _wAPI.sendText(openid, _tip.bind_fail)
        }
    });
}

tcpMSI.prototype.play = function(openid, info, cb){
    var msg = {
        "username": openid,
        "token": token_cache[openid],
        "url": info["rtsp_url"],
        "vodname": info["title"],
        "posterurl": info["tv_poster_path1"],
        "cmd": "user_vodplay_req"
    }

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0"){
            token_cache[openid] = result.new_token;
            _wAPI.sendText(openid, _tip.play_success(info.title));
            cb && cb(result);
        }
        else {
            _wAPI.sendText(openid, _tip.play_fail);
            cb && cb();
        }
    });
}

tcpMSI.prototype.keyboard = function(openid, key_value, cb){
    var msg = {
        "key_type": _cfg.key_type,
        "key_value": key_value,
        "username": openid,
        "token": token_cache[openid],
        "cmd": "key_send_req"
    };

    this.server.send(msg, cb)
}

tcpMSI.prototype.unbind = function(openid, cb){
    var msg = {
        "username": openid,
        "token": token_cache[openid],
        "cmd": "user_unbind_req"
    }

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0"){
            token_cache[openid] = result.new_token;
            _wAPI.sendText(openid, "解绑成功");
            cb && cb();
        }
        else {
            _wAPI.sendText(openid, "解绑失败")
        }
    });
}

tcpMSI.prototype.status = function(openid, cb){
    if( !token_cache[openid] ){
        cb(null, {
            status: 1
        });
        return;
    }

    var msg = {
        "username": openid,
        "token": token_cache[openid],
        "cmd": "user_sessionquery_req"
    }

    this.server.send(msg, function(result){
        var return_code = result["return_code"].toString();
        if(return_code === "0"){
            cb(null, result);
        }
        else {
            cb(true, result);
        }
    });
}

module.exports = tcpMSI;