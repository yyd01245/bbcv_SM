var msi = require("./msi");
var wapi = require("./wapi");

var updateCache = {};
var expire = 1 * 24 * 60 * 60 * 1000;

function check(user){
    return (Date.now() - user) < expire
}

function storeUser(openid, cb){
    wapi.getUser(openid, function(result){
        if(!result){
            cb(true);
            return;
        }

        msi.nickname(openid, result, function(err, body){
            if(err){
                return;
            }
            updateCache[openid] = Date.now();
        })
    })
}

module.exports = function(openid, cb){
    var user = updateCache[openid];
    if( user && check(user) ){
        cb && cb();
    }
    else {
        storeUser(openid, retry)
    }
    var times = 0;
    function retry(result){
        times++;
        if(result.code == -1 || result.code == 40001 ){
            if( times < 3 ){
                storeUser(openid, retry);
            }
        }
    }
};