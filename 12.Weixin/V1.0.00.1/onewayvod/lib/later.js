/**
 * Created by bbcv on 2014/12/5.
 */
var later = require("later");

var basic = {h: [21], m: [15,45]};;

var schedule = {
    schedules: [
        basic
    ]
}

console.log(later.schedule(schedule).next(5));