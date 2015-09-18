var _tcp_msi = require("./tcp_msi");

var export_obj = {
    "register": function(openid){
        _tcp_msi.register(openid);
    },
    "nickname": function(openid, result, cb){
        _tcp_msi.nickname(openid, result, cb);
    },
    "retrieve": function(area){
        return _tcp_msi.retrieve(area);
    },
    "call": function(area, api, arg){
        var server = export_obj.retrieve(area);
        if(server){
            server[api].apply(server, arg);
        }
    }
};

module.exports = export_obj;