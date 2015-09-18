var wechat = require('wechat');
var mail = require("./mail");

var API = require('wechat-api');
var api = new API("wxc6b3871a8239d66f", "db148f05fa2e8e05c473851f710e96ee", function(callback){
    //获取token的方法
    callback(null, access_token);
},function(token, callback){
    //设置token的方法
    access_token = token;
    mail.send("微信接入秘钥", JSON.stringify(token));
    callback(null)
});

var tongji = {
    "all": 0,
    "error": {},
    "success": {}
};

var logger = require("./logger");

var access_token;
api.getLatestToken(function(err, token){
    loggerStream("Return", "getLatestToken", token);
});

var export_obj = {
    sendText: function(openid, text){
        commonWeChatRequest("sendText", [openid, text], function(err, result){});
    },
    sendNews: function(openid, articles){
        var retried = 0;

        function retry(){
            retried++;
            if(retried === 3){
                return;
            }
            commonWeChatRequest("sendNews", [openid, articles], function(err, result){
                if(err){
                    retry();
                }
            });
        }

        retry();
    },
    getUser: function(openid, callback){
        commonWeChatRequest("getUser", [openid], function(err, result){
            if(result){
                callback(result);
            }
            else {
                callback();
            }
        });
    },
    "qrcode": function(sceneId, callback){
        commonWeChatRequest("createLimitQRCode", [sceneId], function(err, result){
            if(result){
                callback(result);
            }
            else {
                callback();
            }
        });
    },
    api: api,
    tongji: tongji
};

function commonWeChatRequest(aname, options, cb){
    loggerStream("Send", aname, options);
    tongji.all++;
    var new_cb = function(err, result){
        if(err){
            loggerError("Return", aname, err);
            tongji.error[aname] ? tongji.error[aname]++ : tongji.error[aname] = 1;
            cb(err);
            return;
        }
        tongji.success[aname] ? tongji.success[aname]++ : tongji.success[aname] = 1;
        loggerStream("Return", aname, result);
        cb(err, result);
    };

    options.push(new_cb);
    api[aname].apply(api, options);
}

function loggerStream(flow, api, body){
    logger.stream(flow, "WeChat", api, body);
}

function loggerError(flow, api, err){
    logger.error(flow, "WeChat", api, err);
}

module.exports = export_obj;