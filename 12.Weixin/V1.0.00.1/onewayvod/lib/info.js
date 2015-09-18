var pic_host = "http://218.108.50.246/onewayvod/";

var aqgy = {
    name: "爱情公寓",
    en: "aqgy",
    pic: pic_host+"aqgy-big.jpg",
    des: "《爱情公寓4》是都市爱情爆笑喜剧《爱情公寓》系列的第四部。该剧由汪远编剧、韦正导演，高格文化制作出品，陈赫、娄艺潇、孙艺洲、李金铭、王传君、邓家佳、李佳航担当主演，胡歌、何炅、杜海涛等友情客串。本剧讲述了一群不同身份背景的年轻男女在并不奢华的爱情公寓里，上演的一幕幕搞笑、离奇、浪漫、感人的有趣故事。",
    playurl: "rtsp://192.168.100.11:8845/yjy_ipqam/8080/dsj.ts"
}

var gyc = {
    name: "关云长",
    en: "gyc",
    pic: pic_host + "gyc-big.jpg",
    des: "《关云长》是由麦兆辉、庄文强共同执导，甄子丹、孙俪、姜文等演员主演的电影，主要讲述了在曹操大败刘备之后，二弟关云长为保护嫂嫂安危，被迫降曹，身在曹营心在汉的故事。",
    playurl: "rtsp://192.168.100.11:8845/yjy_ipqam/8081/gyc.ts"
}

var ypzjs = {
    name: "营盘镇警事",
    en: "ypzjs",
    pic: pic_host + "ypzjs-big.jpg",
    des: "《营盘镇警事》由中国电视剧制作中心有限责任公司、公安部金盾影视文化中心联合出品，马进执导，张嘉译领衔主演。",
    playurl: "rtsp://192.168.100.11:8845/yjy_ipqam/8081/ypzjs.ts"
}
var info = {
    2: aqgy,
    1: gyc,
    3: ypzjs,
    4: aqgy,
    5: aqgy,
    6: aqgy,
    7: aqgy,
    8: aqgy,
    9: aqgy,
    10: ypzjs,
    11: gyc,
    12: aqgy
}

module.exports = info;
