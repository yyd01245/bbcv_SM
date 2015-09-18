/**
 * 存取二维码与流id，点播id的对应关系
 */
var wapi = require("./wapi");

var scene_id = 0;
//key: 流id_点播id_区域标识; value: 二维码图片地址
var cv_code = {};

//key: 二维码场景值; value: 流id与点播id和区域标识对象
var code_cv = {};

//key:微信二维码URL信息；value：流id与点播id和区域标识对象
var url_cv = {};

//key:用户id; value: 流id与点播id和区域标识对象
var user_cv = {};

var export_obj = {
    "getQRImg": function(channel, vodid, area, cb){
        var key = channel + "_" + vodid + "_" + area;
        var img_url = cv_code[key];
        if(img_url){
            cb(img_url);
        }
        else {
            scene_id++;
            wapi.qrcode(scene_id, function(result){
                if(!result){
                    cb();
                    //获取失败，撤销场景id
                    scene_id--;
                    return;
                }

                img_url = wapi.api.showQRCodeURL(result.ticket);

                cv_code[key] = img_url;
                var obj = {
                    channel: channel,
                    vodid: vodid,
                    area: area
                }
                code_cv[scene_id] = obj
                url_cv[result.url] = obj;

                cb(img_url);
            })
        }
    },
    "getQRMsg": function(sceneId){
        return code_cv[sceneId];
    },
    "getQRMsgByUrl": function(url){
        return url_cv[url];
    },
    "setUserCV": function(openid, sceneId){
        user_cv[openid] = export_obj.getQRMsg(sceneId) || export_obj.getQRMsgByUrl(sceneId);
    },
    "getUserCV": function(openid){
        return user_cv[openid];
    }
};

module.exports = export_obj;