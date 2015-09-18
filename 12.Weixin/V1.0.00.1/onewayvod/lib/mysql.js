//甘肃数据
var util = require("util");
var mysql = require("mysql");

var cfg = require("./config");
var logger = require("./logger");

var table = "vod_resource_info";
var is_connect = false;
var update_fre = 60 * 60 * 1000;

var vod_list = {};

var sql = {
    "select_all": "SELECT * from %s",
    "select_all_with_condition": "SELECT * from %s where %s",
    "select_all_join_table": "SELECT * FROM kycms_archives LEFT JOIN kycms_vod ON kycms_archives.id=kycms_vod.aid"
};

var area_db_map = {
    "home": {
        "db": "kycms",
        "data_sql": sql.select_all_join_table,
        "host": "218.108.50.246",
        "user": "root",
        "password": "123456"
    }
};

function updateData(){
    var areas = Object.keys(area_db_map);
    areas.forEach(function(area){
        var connection = mysql.createConnection({
            host: area_db_map[area].host,
            user: area_db_map[area].user,
            password: area_db_map[area].password,
            database: area_db_map[area].db
        });

        connection.connect(function(err){
            if(err){
                logger.err( area + " mysql connection error: " + err.stack);
                return;
            }
            //is_connect = true;
            logger.info( area + " mysql connection success");

            get_all_info();
        });

        function query(sql, cb){
            connection.query(sql, cb);
        }

        function get_all_info(){
            var sql_str = area_db_map[area].data_sql;

            query(sql_str, function(err, results){
                if(err){
                    logger.err(area + " get vod info error: " + err.stack);
                    return;
                }

                var results = results.map(function(ele){
                    ele.tv_poster_path1 = regImgSrc(ele.tv_poster_path1)
                    ele.tv_poster_path2 = regImgSrc(ele.tv_poster_path2)
                    ele.tv_poster_path3 = regImgSrc(ele.tv_poster_path3)
                    ele.tv_poster_path4 = regImgSrc(ele.tv_poster_path4)

                    ele.mb_poster_path1 = regImgSrc(ele.mb_poster_path1)
                    ele.mb_poster_path2 = regImgSrc(ele.mb_poster_path2)
                    ele.mb_poster_path3 = regImgSrc(ele.mb_poster_path3)
                    ele.mb_poster_path4 = regImgSrc(ele.mb_poster_path4)

                    return ele;
                })

                vod_list[area] = results;

                connection.end();
            });
        }
    })

    setTimeout(updateData, update_fre);
}

updateData();

function find(arr, cb){
    if(!arr){
        return null;
    }
    for(var i = 0, len = arr.length; i < len; i++){
        if(cb(arr[i])){
            return arr[i];
        }
    }

    return null;
}

function regImgSrc(src){
    src = src.toString();
    var reg_img_src = /}(.+){/g;
    var result = reg_img_src.exec(src)
    if(result){
        return result[1].trim();
    }

    return src;
}

module.exports = {
    "check_info": function(area, vodid, cb){
        var result = find(vod_list["home"], function(ele){
            if(ele.id == vodid){
                return true;
            }

            return false;
        });

        cb(result);
    }
};