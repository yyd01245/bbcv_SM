var util = require("util");

var map = {
    "-6": "该频道已被其他用户绑定",
    "-5": "不合法的二维码",
    "-7": "参数错误",
    "-8": "内部异常",
    "-1001": "未找到相应的点播信息",
    "-1002": "用户信息错误",
    "-1003": "服务不存在"
};

module.exports = function(code){
    return {
        retcode: code,
        msg: map[code]
    }
};
