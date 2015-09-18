<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="js/jquery.min.js"></script>

<title>jQuery实现带二维码的返回顶部特效 - 素材家园（www.sucaijiayuan.com）</title>
<style>
#elevator_item {
	width: 60px;
	height: 100px;
	position: fixed;
	right: 15px;
	bottom: 15px;
	-webkit-transition: opacity .4s ease-in-out;
	-moz-transition: opacity .4s ease-in-out;
	-o-transition: opacity .4s ease-in-out;
	opacity: 1;
	z-index: 100020;
	display: none;
}
#elevator_item.off {
	opacity: 0;
	visibility: hidden
}
#elevator {
	display: block;
	width: 60px;
	height: 50px;
	background: url(img/img/icon_top.png) center center no-repeat;
	background-color: #444;
	background-color: rgba(0,0,0,.6);
	border-radius: 2px;
	box-shadow: 0 1px 3px rgba(0,0,0,.2);
	cursor: pointer;
	margin-bottom: 10px
}
#elevator:hover {
	background-color: rgba(0,0,0,.7)
}
#elevator:active {
	background-color: rgba(0,0,0,.75)
}
#elevator_item .qr {
	display: block;
	width: 60px;
	height: 40px;
	border-radius: 2px;
	box-shadow: 0 1px 3px rgba(0,0,0,.2);
	cursor: pointer;
	background: url(img/icon_code.png) center center no-repeat;
	background-color: #444;
	background-color: rgba(0,0,0,.6)
}
#elevator_item .qr:hover {
	background-color: rgba(0,0,0,.7)
}
#elevator_item .qr:active {
	background-color: rgba(0,0,0,.75)
}
#elevator_item .qr-popup {
	width: 170px;
	height: 200px;
	background: #fff;
	box-shadow: 0 1px 8px rgba(0,0,0,.1);
	position: absolute;
	left: -180px;
	bottom: 0;
	border-radius: 2px;
	display: none;
	text-align: center
}
#elevator_item .qr-popup .code-link {
	display: block;
	margin: 10px;
	color: #777
}
#elevator_item .qr-popup .code {
	display: block;
	margin-bottom: 10px
}
#elevator_item .qr-popup .arr {
	width: 6px;
	height: 11px;
	background: url(img/code_arrow.png) 0 0 no-repeat;
	position: absolute;
	right: -6px;
	bottom: 14px
}
</style>
</head>
<body style="height:2000px; background:#CCCCCC">
<h1 style="text-align:center;padding:20px;">滚动条下拉后看右下角查看效果！</h1>
<div></div>
<div id="elevator_item">
  <a id="elevator" onclick="return false;" title="回到顶部"></a>
</div>
  
<script type="text/javascript">
$(function() {
	$(window).scroll(function(){
		var scrolltop=$(this).scrollTop();		
		if(scrolltop>=200){		
			$("#elevator_item").show();
		}else{
			$("#elevator_item").hide();
		}
	});		
	$("#elevator").click(function(){
		$("html,body").animate({scrollTop: 0}, 500);	
	});		
	
});
</script>
</body>
</html>
