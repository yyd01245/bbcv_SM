var util = require("util")
var export_obj = {
    "join_fail": "咦~系统好像有点忙哦",
    "register_success": "^_^ 欢迎您使用点播服务",
    "register_fail": "额~系统好像有点忙哦",
    "bind_success": "^_^ 眼疾手快，播控由我",
    "bind_fail": "呵呵~这个节目好像被人绑走了",
    "play_success": function(name){
        return util.format("^_^ 您挑选的%s准备开始播放了", name)
    },
    "play_fail": "唉~系统放片鸭梨大，您稍等再播吧"
}

module.exports = export_obj;