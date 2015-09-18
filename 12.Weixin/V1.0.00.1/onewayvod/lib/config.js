var path = require("path");
var host = "218.108.50.246";
var ns_host = "10.169.8.29:8882";


module.exports = {
    key_type: "1001",
    host: host,
    port: 10208,

    "default_area": "home",

    "tcp_port": 20208,

    keyboard_url: "http://"+host+"/onewayvod/mobile/keyboard.html",
    movielist_url: "http://"+host+"/onewayvod/vod/index.html",

    app_gansu_url: "http://"+ns_host+"/NS/a?id=%s&vp=%s&r=%s",
    app_home_url: "http://192.168.100.11:8882/NS/a?id=%s&vp=%s&r=%s",

    info_log: path.join(__dirname, "../log/oneway.log"),
    exception_log: path.join(__dirname, "../log/exception.log"),

    "mail_sender": "nodejs@bbcvision.com",
    "mail_passwd": "kuanyun001",
    "mail_to": "guanyx@bbcvision.com"
}
