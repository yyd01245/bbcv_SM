var express = require('express');
var router = express.Router();

var fs = require("fs");
var path = require("path");

var wapi = require("./wapi");

var menu_path = path.join(__dirname, "../data/menu.json");
router.get("/menu/update", function(req, res){
    fs.readFile(menu_path, function(err, result){
        wapi.api.createMenu(JSON.parse(result), function(err, result){
            if(err){
                res.send("创建失败")
            }
            else {
                res.send("创建成功")
            }
        })
    })
});

module.exports = router;