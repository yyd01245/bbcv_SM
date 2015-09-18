var util = require("util");

var express = require('express');
var router = express.Router();

var logger = require("./logger");
var errmap = require("./errmap");
var cfg = require("./config");
var mysql = require("./mysql");
var wapi = require("./wapi");
var qrcode = require("./qrcode");
var user_area = require("./user_area");
var _msi = require("./msi");

router.use(function(req, res, next){
    loggerReq(req.ip, req.url);

    next();
})

router.get("/play", function(req, res){
    var vodId = req.query.vodid;
    var user = req.query.user;

    user_area.getArea(user, function(err, result) {
        if (err || result.length === 0) {
            res.jsonp(errmap("-1002"));
            return;
        }

        mysql.check_info(result[0].area, vodId, function(info) {
            if (!info) {
                res.jsonp(errmap("-1001"));
                return;
            }

            var msi = _msi.retrieve(result[0].area);
            if(!msi){
                res.jsonp(errmap("-1003"))
                return;
            }
            msi.play(user, info, function(body){
                if(!body){
                    res.jsonp(errmap("-1001"));
                    return;
                }

                res.jsonp({
                    retcode: 0,
                    url: 'http://'+cfg.host+"/onewayvod/mobile/keyboard.html?openid="+user
                })
            })
        });
    })
});

router.get("/keyboard", function(req, res){
    var openid = req.query.openid;
    var key_value = req.query.keyvalue;

    user_area.getArea(openid, function(err, result) {
        if (err || result.length === 0) {
            res.jsonp(errmap("-1002"));
            return;
        }

        var msi = _msi.retrieve(result[0].area);
        if(!msi){
            res.jsonp(errmap("-1003"))
            return;
        }

        msi.keyboard(openid, key_value, function(body){
            res.jsonp(body);
        })
    });
});

router.get("/detail", function(req, res){
    var vodid = req.query.vodid;
    var openid = req.query.openid;

    if(vodid && openid){
        user_area.getArea(openid, function(err, result) {
            if (err || result.length === 0) {
                res.jsonp(errmap("-1002"));
                return;
            }

            mysql.check_info(result[0].area, vodid, function(result){
                if(result){
                    res.jsonp({
                        retcode: "0",
                        info: result
                    })
                }
                else {
                    res.jsonp(errmap("-1001"))
                }
            })
        })
    }
    else {
        res.jsonp(errmap("-1001"))
    }
})

router.get("/qrcode", function(req, res){
    var channel = parseInt(req.query.channel);
    var vodid = parseInt(req.query.vodid);
    var area = req.query.area || cfg.default_area;
    if(isNaN(channel) || isNaN(vodid)){
        res.jsonp(errmap("-7"))
        return;
    }
    qrcode.getQRImg(channel, vodid, area, function(url){
        if(!url){
            res.jsonp(errmap("-8"))
            return;
        }

        res.jsonp({
            "retcode": 0,
            "url": url
        })
    })
});

router.get("/urlmapqr", function(req, res){
    var url = req.query.url;
    if(url){
        var msg = qrcode.getQRMsgByUrl(url);
        if(msg){
            //var result = util.format("http://218.108.50.246/bbcvcms/uploads/plus/view.php?aid=%s&assigntypeid=9&streamid=%s&resolution=0", msg.vodid, msg.channel);
            var result = util.format(cfg["app_"+msg.area+"_url"], msg.channel, msg.vodid, "0");
            //var result = "http://10.169.8.29:8882/NS/a?id="+msg.channel+"&vp="+msg.vodid+"&r=0";
            res.jsonp({
                "retcode": 0,
                "msg": msg,
                "url": result,
                "vod_page": result
            })
            return;
        }
    }
    res.jsonp(errmap("-7"))
})

router.get("/openidmapstream", function(req, res){
    var openid = req.query.openid;
    if(openid){
        var info = qrcode.getUserCV(openid);
        if(info){
            res.jsonp({
                "retcode": 0,
                "info": info
            })
            return;
        }
    }
    res.jsonp(errmap("-7"))
})

function loggerReq(ip, url){
    logger.info("【Receive】【Request】【"+ip+"】:"+url);
}

module.exports = router;