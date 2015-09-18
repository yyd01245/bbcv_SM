
var httpProxy = require('http-proxy');

var apiProxy = httpProxy.createProxyServer();

module.exports = function(app){
    app.get("/*", function(req, res){
        apiProxy.web(req, res, { target: 'http://127.0.0.1:8754' });
    });

    apiProxy.on('error', function(e){
        console.log('http-proxy: ' + err);
    })
}