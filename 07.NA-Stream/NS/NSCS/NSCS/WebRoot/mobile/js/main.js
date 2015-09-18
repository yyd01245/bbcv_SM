var id = getQueryStringByName( 'id' );
var stbid = getQueryStringByName( 'stbid' );
var statuTimer;
var ifVod=false,ifYun=false;

function setTab(name,cursel,n){
 for(i=1;i<=n;i++){
  var menu=document.getElementById(name+i);
  var con=document.getElementById("con_"+name+"_"+i);
  menu.className=i==cursel?"focus":"";
  con.style.display=i==cursel?"block":"none";
 }
}
//跳转到直播详情页面
function vodDetail(index){
	window.location.href="./dvbDetail/"+index+'.html';
	}

//不同的栏目跳转到对应的VOD详情页面
function vodChange(index){
	window.location.href='voddetail.jsp?name='+index;
	}


//跳转到正在建设中页面
function onConstr(para){
	if(para==1){
	this.className=='blue';
		}
	window.location.href="./underCon.html";
	}


function sendMsg( msg ){
//	isVod();
//	isYun();
	if( !ifVod && !ifYun ){
		if( stbid === '' ){
			alert( '请重新绑定' );
		} else {
			msg = encode64( JSON.stringify(msg) );
			$.ajax("http://218.108.0.92:8090/mtap/change?msg="+msg+"&stbid="+stbid+"&id="+id,{async:false,type:'GET'});
		}
	}
	
	//$.ajax("http://10.99.81.213:19090/mtap/change?msg="+msg+"&id="+id,{async:false,type:'GET'});
}

function sendVodMsg( msg ){
	sendMsg( msg );
	localStorage.setItem( 'isVod', '1' );
	openVodKeyboard();
}


function getStatu(){
	$.ajax("http://218.108.0.92:8090/mtap/query?device=1&stbid="+stbid+"&id="+id,{type:'GET',success: function( data ){
		var result = JSON.parse( data );
		if( result.bind === '0' ){
			alert("已解除绑定");
			$( "#unbind" ).hide();
			statuTimer = null;
		} else {
			//statuTimer = setTimeout( getStatu, 10000);
		}
	}});
}

function getQueryStringByName(name){ 
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i")); 
	if(result == null || result.length < 1){ 
		return ""; 
	} 
	return result[1]; 
} 

function encodeurl(para){
	return encodeURIComponent(para);
}
function decodeurl(para){
	return decodeURIComponent(para);
}
function directVod(){
	document.location.href = "./vod.jsp?id="+id+"&stbid="+stbid;
}

function directDvb(){
	document.location.href = "./dvb.html?id="+id+"&stbid="+stbid;
}

function openKeyboard(){
	document.location.href = "./vMouse/index.html?id="+id+"&stbid="+stbid+"&inKey=1";
}

function openVodKeyboard(){
	document.location.href = "./vMouse/index.html?id="+id+"&stbid="+stbid+"&inKey=1";
}

function unbindConnect(){
	$.get( "http://218.108.0.92:8090/mtap/bind?stbid="+stbid+"&isbind=0&id="+id, function( data ) {
		var result = JSON.parse( data );
		if( result.bind === '0' ){
			alert("已解除绑定");
			$( "#unbind" ).hide();
			statuTimer = null;
			$( "#stbid" ).html( "" ).hide();
		}
	});
}

function goBack(){
	history.back();
}

function cloudPlatform(){
	sendMsg({"header":{"opcode":"pushToPlay"},"body":{"key":"2","param":"http://218.108.50.250:8090/ccbn/index/index.html"}});
	localStorage.setItem( 'isYun', '1' );
	openKeyboard();
}

$(document).ready(function(e) {
	init();
//	isVod();
//	isYun();

	localStorage.setItem( 'stbid', stbid );
	if( stbid !== '' ){
		$( "#stbid" ).html('已绑定电视：'+stbid).show();
	}
});

function isVod(){
	if( getQueryStringByName("inKey") != '1' ){
		if( localStorage.getItem( 'stbid' ) !== "" && localStorage.getItem( 'stbid' ) === stbid ){
			if( localStorage.getItem( 'isVod' ) === '1' ){
				ifVod = true;
				alert('请先退出点播');
				navigateToUrl("./vMouse/vodkeyboard.html?id="+id+"&stbid="+stbid+"&inKey=1");
			}
		}
	}
}

function isYun(){
	if( getQueryStringByName("inKey") != '1' ){
		if( localStorage.getItem( 'stbid' ) !== "" && localStorage.getItem( 'stbid' ) === stbid ){
			if( localStorage.getItem( 'isYun' ) === '1' ){
				ifYun = true;
				alert('请先退出云平台');
				navigateToUrl("./vMouse/index.html?id="+id+"&stbid="+stbid+"&inKey=1");
			}
		}
	}
}

function init(){
	if( getQueryStringByName( 'index' ) == '1' ){
		localStorage.setItem( 'isVod', '0' );
		localStorage.setItem( 'isYun', '0' );
		localStorage.setItem( 'stbid', stbid );
	}
}

function navigateToUrl(url) {
    var f = document.createElement("FORM");
    f.action = url;

    var indexQM = url.indexOf("?");
    if (indexQM>=0) {
        // the URL has parameters => convert them to hidden form inputs
        var params = url.substring(indexQM+1).split("&");
        for (var i=0; i<params.length; i++) {
            var keyValuePair = params[i].split("=");
            var input = document.createElement("INPUT");
            input.type="hidden";
            input.name  = keyValuePair[0];
            input.value = keyValuePair[1];
            f.appendChild(input);
        }
    }

    document.body.appendChild(f);
    f.submit();
}

