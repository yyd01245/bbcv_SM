var net = require("net");
var util = require("util");
var eventEmitter = require("events").EventEmitter;

var _cfg = require("./config");
var logger = require("./logger")
var server;
var client = {};
var sequence = 10000;
var serial_cb_map = {};
var msi_name = "MAG";
var queue = [];

var message_pattern = /(.*?)XXEE/g;

function tcpServer(area){
    this.area = area;
}

tcpServer.emit = new eventEmitter();

tcpServer.prototype.send = function(obj, cb){
    if(client[this.area]){
        obj.sequence = sequence++;
        serial_cb_map[obj.sequence] = {
            "cb": function(data){
                cb && cb(data);
            },
            "body": obj,
            "area": this.area,
            "times": 0
        }

        write(obj, client[this.area]);
    }
    else {
        logger.stream("Drop", msi_name, obj.cmd, JSON.stringify(obj));
    }
};

function write(data, $client){
    var body = JSON.stringify(data) + "XXEE";
    logger.stream("Send", msi_name+":"+$client.remoteAddress+":"+$client.remotePort, data.cmd, body);
    var sign = $client.write(body, "utf-8");
    if(!sign){
        queue.push(data);
        logger.info("MSI CLIENT Kernel Buffer Filled");
    }
    return sign;
}

function createServer(){
    server = net.createServer();

    server.on("connection", function(c){
        logger.info("MSI Join: " + c.remoteAddress + ":" + c.remotePort);

        var receive = "";
        c.on("end", function(){
            client[c.area] = null;
        });

        c.on("data", function(chunk){
            chunk = chunk.toString();
            logger.stream("Receive", msi_name, c.remoteAddress+":"+c.remotePort, chunk);
            if(chunk === "socket client keeplives"){
                return;
            }
            receive += chunk;
            receive = analytic(receive, c);
        });

        c.on("drain", function(){
            var data;
            logger.info("MSI CLIENT Emit Drain");
            while(data = queue[c.area].shift()){
                if(!write(data, c)){
                    break;
                }
            }
        });

        c.on("timeout", function(){
            logger.err("MSI CLIENT Timeout");
        });

        c.on("error", function(error){
            logger.err("MSI CLIENT ERROR: " + JSON.stringify(error));
        });

        c.on("close", function(had_error){
            var log = "MSI CLIENT Disconnection";
            if(had_error){
                log += " with Error";
            }
            logger.err(log);
            if(client[c.area]){
                client[c.area] = null;
            }
        })
    });

    server.on("error", function(error){
        logger.err("TCP MSI ERROR: " + JSON.stringify(error));
    });

    server.listen(_cfg.tcp_port, function(){
        logger.info("TCP MSI LISTEN " + _cfg.tcp_port + " Success");
    });
}

function analytic(data, c){
    var result;
    while((result = message_pattern.exec(data)) !== null){
        data = data.slice(message_pattern.lastIndex);
        message_pattern.lastIndex = 0;

        handler(result[1], c);
    }
    return data;
}

function handler(data, c){
    try{
        data = JSON.parse(data);

        if(data.cmd === "login"){
            client[data.area] = c;
            c.area = data.area;
            tcpServer.emit.emit("login", data.area);
        }
        else if(serial_cb_map[data.sequence]){
            if(data.return_code.toString() === "-1"){
                if(serial_cb_map[data.sequence].times === 3){
                    logger.info("Over 3 Times retry:"+JSON.stringify(serial_cb_map[data.sequence].body))
                    return;
                }
                write(serial_cb_map[data.sequence].body, c);
                serial_cb_map[data.sequence].times++;
            }
            else {
                serial_cb_map[data.sequence].cb(data);
                delete serial_cb_map[data.sequence];
            }
        }
    }
    catch(e){
        logger.info("TCP MSI Parse Data ERROR: " + JSON.stringify(e));
        logger.info("Error String: " + data);
    }
}

createServer();

module.exports = tcpServer;