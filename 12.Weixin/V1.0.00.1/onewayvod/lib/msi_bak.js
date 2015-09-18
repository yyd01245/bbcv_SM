/**
 * TCP服务
 * 监听MSI接入
 * 如有服务接入，将微信用户的请求行为转换成报文发给MSI
 */
var net = require("net");
var cfg = require("./config");
var logger = require("./logger");
var wapi = require("./wapi");
var tip = require("./tip");

var message_pattern = /(.*?)XXEE/g;

var server = net.createServer();
//链接上的MSI客户端
var client = null;

var queue = [];

server.on("connection", function(c){
    client = c;
    logger.info("MSI Join: " + client.remoteAddress + ":" + client.remotePort);

    var receive = "";
    client.on("end", function(){
        client = null;
    });

    client.on("data", function(chunk){
        chunk = chunk.toString();
        logger.stream("Receive", msi_name, client.remoteAddress+":"+client.remotePort, chunk);
        if(chunk === "socket client keeplives"){
            return;
        }
        receive += chunk;
        receive = analytic(receive);
    });

    client.on("drain", function(){
        var data;
        logger.info("MSI CLIENT Emit Drain");
        while(data = queue.shift()){
            if(!write(data)){
                break;
            }
        }
    });

    client.on("timeout", function(){
        logger.err("MSI CLIENT Timeout");
    });

    client.on("error", function(error){
        logger.err("MSI CLIENT ERROR: " + JSON.stringify(error));
    });

    client.on("close", function(had_error){
        var log = "MSI CLIENT Disconnection";
        if(had_error){
            log += " with Error";
        }
        logger.err(log);
        if(client){
            client = null;
        }
    })
});

server.on("error", function(error){
    logger.err("TCP MSI ERROR: " + JSON.stringify(error));
});

server.listen(cfg.tcp_port, function(){
    logger.info("TCP MSI LISTEN " + cfg.tcp_port + " Success");
});

function analytic(data){
    var result;
    while((result = message_pattern.exec(data)) !== null){
        data = data.slice(message_pattern.lastIndex);
        message_pattern.lastIndex = 0;

        handler(result[1]);
    }
    return data;
}

function handler(data){
    try{
        data = JSON.parse(data);

        if(serial_cb_map[data.sequence]){
            if(data.return_code.toString() === "-1"){
                write(serial_cb_map[data.sequence].body);
            }
            else {
                serial_cb_map[data.sequence].cb(data);
                delete serial_cb_map[data.sequence];
            }
        }
    }
    catch(e){
        logger.info("TCP MSI Parse Data ERROR: " + JSON.stringify(e));
        logger.info("Error String: " + data);
    }
}

function write(data){
    if(client){
        var body = JSON.stringify(data) + "XXEE";
        logger.stream("Send", msi_name+":"+client.remoteAddress+":"+client.remotePort, data.cmd, body);
        var sign = client.write(body, "utf-8");
        if(!sign){
            queue.push(data);
            logger.info("MSI CLIENT Kernel Buffer Filled");
        }
        return sign;
    }
    else {
        logger.stream("Drop", msi_name, data.cmd, JSON.stringify(data));
    }
    return true;
}

function serialno(){
    return Date.now() + "" + Math.round((Math.random() * 9999));
}

//一下为msi逻辑报文部分
var msi_name = "MAG";
var passwd = "123456";

var serial_cb_map = {};
var token_cache = {};

function commonWrite(api, body, cb){
    body.sequence = serialno();
    serial_cb_map[body.sequence] = {
        "cb": cb,
        "body": body
    };
    //body = JSON.stringify(body) + "XXEE";

    write(body);
}

var export_obj = {
    "join": function(openid, cb){
        var msg = {
            "username": openid,
            "passwd": passwd,
            "version": "v1.0.00.1",
            "appname": "KYSX",
            "licence": "KYSX1234",
            "cmd": "user_access_req"
        };

        commonWrite("join", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0") {
                token_cache[openid] = result.token;
                cb();
            }
            else if(return_code === "-2010"){
                logger.info("用户不存在，发起注册");
                export_obj.register(openid, function(result2){
                    if(result2["return_code"].toString() === "0"){
                        logger.info("用户注册成功，重新接入");
                        export_obj.join(openid, cb);
                    }
                    else {
                        logger.info("自动注册失败，放弃注册，提示失败");
                    }
                })
            }
            else {
                wapi.sendText(openid, tip.join_fail)
            }
        });
    },
    "register": function(openid, cb){
        var msg = {
            "username": openid,
            "passwd": passwd,
            "cmd": "user_regist_req"
        }

        commonWrite("register", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0" || return_code === "-1007"){
                wapi.sendText(openid, tip.register_success);
                cb && cb(result);
            }
            else {
                wapi.sendText(openid, tip.register_fail)
                cb && cb(result, true);
            }
        });
    },
    "nickname": function(openid, obj, cb){
        var msg = {
            "username": openid,
            "passwd": passwd,
            "nickname": obj.nickname,
            "cmd": "user_updateNickname_req"
        }

        commonWrite("nickname", msg, cb)
    },
    "bind": function(openid, channel, vod_page, cb){
        var msg = {
            "username": openid,
            "token": token_cache[openid],
            "stream_id": channel,
            "vod_page": vod_page,
            "cmd": "user_bind_req"
        }

        commonWrite("bind", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0"){
                token_cache[openid] = result.new_token;
                wapi.sendText(openid, tip.bind_success);
                cb && cb(result)
            }
            else {
                wapi.sendText(openid, tip.bind_fail)
            }
        });
    },
    "play": function(openid, info, cb){
        var msg = {
            "username": openid,
            "token": token_cache[openid],
            "url": info["rtsp_url"],
            "vodname": info["name"],
            "posterurl": info["tv_poster_path1"],
            "cmd": "user_vodplay_req"
        }

        commonWrite("play", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0"){
                token_cache[openid] = result.new_token;
                wapi.sendText(openid, tip.play_success(info.name));
                cb && cb(result);
            }
            else {
                wapi.sendText(openid, tip.play_fail);
                cb && cb();
            }
        });
    },
    "keyboard": function(openid, key_value, cb){
        var msg = {
            "key_type": cfg.key_type,
            "key_value": key_value,
            "username": openid,
            "token": token_cache[openid],
            "cmd": "key_send_req"
        };

        commonWrite("keyboard", msg, cb)
    },
    "unbind": function(openid, cb){
        var msg = {
            "username": openid,
            "token": token_cache[openid],
            "cmd": "user_unbind_req"
        }

        commonWrite("unbind", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0"){
                token_cache[openid] = result.new_token;
                wapi.sendText(openid, "解绑成功");
                cb && cb();
            }
            else {
                wapi.sendText(openid, "解绑失败")
            }
        });
    },
    "status": function(openid, cb){
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

        commonWrite("status", msg, function(result){
            var return_code = result["return_code"].toString();
            if(return_code === "0"){
                cb(null, result);
            }
            else {
                cb(true, result);
            }
        });
    }
};

module.exports = export_obj;