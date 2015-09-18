var qs = require("querystring");
var util = require("util");

var _wAPI = require("./wapi");
var _cfg = require("./config");
var export_obj = {
    "bind": function(openid, info, cb){
        var articles = [
            {
                "title": "点击图片进入："+info.title,
                "description": info.intro,
                "url":util.format("http://218.108.50.246/bbcvcms/uploads/plus/view.php?aid=%s&assigntypeid=20&username=%s&resolution=%s", info.id, openid,"0"),
                "picurl":"http://"+_cfg.host+info.mb_poster_path1
            },
            {
                "picurl": util.format("http://%s/bbcvcms/appdownload/more.png", _cfg.host),
                "url": util.format("http://218.108.50.246/bbcvcms/uploads/plus/list.php?tid=20&username=%s&resolution=%s", openid, "0"),
                "title": "进入列表，选择更多内容",
                "description": "进入列表，选择更多内容"
            },
            {
                "picurl": util.format("http://%s/bbcvcms/appdownload/app.png", _cfg.host),
                "url": "http://218.108.50.246/bbcvcms/appdownload/app-download.html",
                "title": "下载APP，点播精彩生活",
                "description": "下载APP，点播精彩生活"
            }
        ];
        _wAPI.sendNews(openid, articles)
    },
    "register": function(openid){
        /*var articles = [
            {
                "title": "打开遥控器",
                "url":"http://218.108.50.246/bbcvcms/appdownload/app-download.html"
            }
        ]

        _wAPI.sendNews(openid, articles)*/
    },
    "keyboard": function(openid){
        var articles = [
            {
                "title": "打开遥控器",
                "url": _cfg.keyboard_url + "?" + qs.stringify({"openid": openid})
            }
        ]

        _wAPI.sendNews(openid, articles)
    },
    "movielist": function(openid){
        var articles = [
            {
                "title": "进入电影列表",
                "url": _cfg.movielist_url + "?" + qs.stringify({"openid": openid})
            }
        ]

        _wAPI.sendNews(openid, articles)
    }
}

module.exports = export_obj;