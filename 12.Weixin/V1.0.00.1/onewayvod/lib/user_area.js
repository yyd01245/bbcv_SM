//微信用户的地域信息存表
var util = require("util");
var mysql = require("mysql");

var cfg = require("./config");
var logger = require("./logger");

var table = "user_area";

var is_connect = false;

var connection = mysql.createConnection({
    host: "218.108.50.246",
    user: "root",
    password: "123456",
    database: "wechat_owd"
});

connection.connect(function(err){
    if(err){
        logger.err("wechat user db connection error: " + err.stack);
        return;
    }
    is_connect = true;
    logger.info("wechat user db connection success");
});

function query(sql, cb){
    logger.info("start query " + sql);
    connection.query(sql, function(err, result){
        if(err){
            logger.err("Area Mysql Fail "+ err.stack);
        }

        cb(err, result);
    });
}

var sql = {
    "select_all_with_condition": "SELECT * from %s where %s",
    "update": "UPDATE %s SET area = '%s' Where username = '%s'",
    "insert": "Insert Into %s (username, area) Values ('%s', '%s')"
};

var export_obj = {
    "getArea": function(username, cb){
        var sql_str = util.format(sql.select_all_with_condition, table, "username = '"+username+"'");

        query(sql_str, cb);
    },
    "setArea": function(username, area, cb){
        var sql_str;
        export_obj.getArea(username, function(err, result){
            if(err){
                cb(err);
                return;
            }

            if(result.length){
                sql_str = util.format(sql.update, table, area, username);
            }
            else {
                sql_str = util.format(sql.insert, table, username, area);
            }
            query(sql_str, cb);
        })
    }
};

module.exports = export_obj;