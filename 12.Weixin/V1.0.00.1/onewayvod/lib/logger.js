var util = require("util");

var moment = require("moment");
var winston = require("winston");

var cfg = require("./config");

var maxsize = 20 * 1024;

var maxFiles = 10;

//消息流向，消息类型，消息接口, 消息主体
var stream_logger_format = "[%s] [%s] [%s]: [%s]";

winston.add(winston.transports.File, {
    filename: cfg.info_log,
    timestamp: timestamp,
    json: false,
    maxsize: maxsize,
    maxFiles: maxFiles
});
winston.remove(winston.transports.Console);

winston.handleExceptions(new winston.transports.File({
    filename: cfg.exception_log,
    timestamp: timestamp,
    json: false
}));

winston.exitOnError = false;

var export_obj = {
    err: function(text){
        winston.error(text);
    },
    info: function(text){
        winston.info(text);
    },
    "stream": function(flow, type, api, body){
        if(typeof body === "object"){
            body = JSON.stringify(body);
        }
        export_obj.info(util.format(stream_logger_format, flow, type, api, body));
    },
    "error": function(flow, type, api, err){
        if(typeof body === "object"){
            err = JSON.stringify(err);
        }
        export_obj.err(util.format(stream_logger_format, flow, type, api, err));
    }
};

module.exports = export_obj;

function timestamp(){
    return moment().format("YYYY-MM-DD HH.mm.ss,SSS")
}
