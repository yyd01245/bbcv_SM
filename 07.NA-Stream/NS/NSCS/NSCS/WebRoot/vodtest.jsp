<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   <meta http-equiv="content-type" content="text/html; charset=GBK">
<title>vod1页面</title>
<link rel="stylesheet" href="css/style31.css"></link>
<script type="text/javascript" src="js/jquery.min.js"></script>
<%--<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>--%>
<%--获取流id--%>
<% String  streamid = request.getParameter("id"); %>
<%--打印日志--%>


    <%Logger logger = LoggerFactory.getLogger("");logger.info("用户进入TV导航页面。streamid="+streamid); %>




 <script>

 function queryStatus(){    

     var steam_id = <%=streamid%>
		$.ajax({ url: "skipServlet?streamid="+steam_id, context: document.body, success: function(data){
	
        if(data!=""&&"error"!=data.trim()){
     	  window.location.href=data;   	   
        }
        if(data.trim()=="error"){
        	
        }
		      }});	
   
	}	
<%--jQuery(function(){--%>
<%--	  var mode = !!document.createElement('canvas').getContext ? 'canvas' : 'table';--%>
<%--	  jQuery('#qrcode').qrcode({render:mode,text:"<%=basePath%>a?id=1&vp=1",width:400,height:400});--%>
<%-- })--%>
// queryStatus();
setInterval("queryStatus();",2000); 
		    
</script>


<%----%>
<%--<script type="text/javascript">--%>
<%--function t() {--%>
<%--      --%>
<%--        window.location.href="vod2.jsp"; --%>
<%--}--%>
<%----%>
<%--setInterval("t();",3000); --%>
<%--</script>--%>


</head>


<body>

<div class="container">
	<div class="logo">单向互动</div>
	<div class="logo1" style="color:darkorange;"></div>
	
    <div id="star-area">
        <img class="star" src="img/bg/star1.png" />
        <img class="star" src="img/bg/star2.png" />
        <img class="star" src="img/bg/star3.png" />
        <img class="star" src="img/bg/star4.png" />
        <img class="star" src="img/bg/star5.png" />
        <img class="star" src="img/bg/star6.png" />
    </div>
<div class="left" style="
    padding-top: 85px;
">		
		<div class="video" style="
    width: 414px;
    height: 441px;">
			<h1 id="movie">MPEG2流化效果示例</h1>
<video id="video" src="video/xc.mp4" autoplay="true" loop="" width="909" height="714" style="
    width: 439px;
    height: 349px;
    padding-left: 0px;
"></video>
			<div class="info_bg">		
				<div class="movieinfo">
					<div class="infowin">
						<ul>
							<li>影片评分：★★★★★</li>
							<li id="director">影片导演：麦兆辉 / 庄文强</li>
							<li id="actor">影片主演: 詹妮弗·劳伦斯</li>
							
						
						</ul>
						
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<div class="right">
		<div class="intro">
			<h2>点播说明</h2>
			<p class="instro">
				使用APP扫描右下二维码进入点播导航
				（如未安装APP，使用微信等第三方软件扫描后自动进入下载页面）
			</p>
			</div>
		<div class="erweima" id="qrcode">
			
		</div>
	</div>
</div>






<script src='js/jquery-latest.js'></script>
<script src="js/jquery.qrcode.min.js"></script>
<script src="js/data.js"></script>
<script>


var id=getQueryStringByName("name"),currentEle,index=-1,timer,movieList=[],$star_area;
var	timerule ;


$.each(data,function(i,ele){

	movieList.push(ele.id.trim());

	if(ele.id.trim()==id.trim()){
		timerule = ele.timerule.trim();
		//alert("第一次 ："+id+"  "+timerule);
	}
	// alert(i);   
  //   alert(ele.id.trim());
   //  alert(ele.timerule.trim());
	//alert(movieList.length);
	//alert(movieList);
})

var $descHeight, $winHeight,maxScroll,$videoBg,$win= $(".movieinfo"),$videoBg = $(".info_bg"),scrollTimer,direction=1;
$(function(){
	setClock();
	changeMovie(id);
<%--//	$("#qrcode").qrcode({width:400,height:400,text:"<%=basePath%>a?id=<%=streamid%>&vp=id"});		--%>
	
	//updataMaxScroll();
	
 //  scrollTimer = setInterval(autoScroll,3000);
	
//	timer=setInterval("changeMovie(checkIndex())",30000);
	//alert(" 当前规则："+timerule);


	$win.scroll(function() {
		var $this, scrollTop;
		$this = $(this);
		scrollTop = $this.scrollTop();
		if (scrollTop === 0) {
			$videoBg.removeClass("before");
		} else {
			$videoBg.addClass("before");
		}
		if (scrollTop > maxScroll) {
			return $videoBg.removeClass("after");
		} else {
			return $videoBg.addClass("after");
		}
	});
	
	function autoScroll(){
		var scrollTop = $win.scrollTop();
		if(direction == 1){
			if(maxScroll-scrollTop >130 ){
				keyDown();	
			}
			else{
				direction=-1;
			}
			
		}
		if(direction == -1){
			if(scrollTop > 0){
				keyUp();	
			}
			else{
				direction=1;
			}
			
		}
	}
	document.onkeydown = function(event) {
		switch (event.keyCode) {
			  case 38:
				keyUp();
				break;

			  case 40:
				keyDown();
				break;
			}
		}
	
	function keyUp(){
		 $win.stop(true, true).animate({
			scrollTop: $win.scrollTop() - 140
		}, 300, "linear");
	}
	function keyDown(){
		 $win.stop(true, true).animate({
               scrollTop: $win.scrollTop() + 140
         }, 300, "linear");
	}
	/*星星效果*/
	$star_area = $("#star-area");
	$("#star-area .star").each(function(index, star){
		blink_star($(star));
		$(star).on("webkitAnimationEnd", function(){
			$(star).removeClass("blink-star");
			var nextshow = Math.random() * 10;
			nextshow = nextshow < 3 ? 3 : nextshow;
			setTimeout(function(){
				blink_star($(star));
			}, parseInt(nextshow * 1000));
		})
	})

})
	
function blink_star(jqobj){
	var duration = Math.random() * 3;
	duration = duration < 1 ? 1 : duration;
	var left_px = parseInt(Math.random() * $star_area.width());
	var top_px = parseInt(Math.random() * $star_area.height());
	jqobj.css({"webkitAnimationDuration": duration+"s", left: left_px+"px", top: top_px+"px"});
	jqobj.addClass("blink-star");
}
function getQueryStringByName(name){ 


	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i")); 

	if(result == null || result.length < 1){ 
		return ""; 
	} 
	return result[1]; 
}
function checkIndex(){
	//alert(index);
	index>movieList.length-2?index=0:index++;
		/*初始化滚动*/
	updataMaxScroll();
	$win.stop(true, true).animate({
               scrollTop: 0
         }, 300, "linear");
	return movieList[index];
}

function updataMaxScroll(){
	$winHeight = $win.height();
	$descHeight = $(".infowin").height();
	if ($descHeight > $winHeight) {
		$videoBg.addClass("after");
		maxScroll =   $descHeight - $winHeight ;
	}
}
function changeMovie(moviename){
	
	$.each(data,function(i,ele){
		if(ele.id==moviename){
			currentEle=ele;
			if(index == -1){
				index = parseInt(i);
			}
			return;
		}
	})
	if(currentEle){
		timerule = currentEle.timerule;
		//alert("规则："+timerule);
		$("#movie").html(currentEle.name);
	//	$("#name").html(currentEle.name);
		$("#video")[0].src = currentEle.videosrc;
		$("#detail").html(currentEle.movieintro);	
		/*添加影片相关信息*/
		$("#director").html("导演："+currentEle.director);
		$("#actor").html("主演："+currentEle.actor);
		$("#type").html("类型："+currentEle.type);
		$("#ondate").html("上映："+currentEle.ondata);
		$("#qrcode").html("  <div  id='qrcode1'></div>");
		//alert(currentEle.name);
		//http://192.168.100.11:8181/NS,
	     $("#qrcode1").qrcode({width:340,height:340,text:"<%=basePath%>a?id=<%=streamid%>&vp="+currentEle.id});	
	   //  alert(currentEle.name);	
	}


}

function setClock(){
    var date = new Date()
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    hour = fillTwo(hour)
    minute = fillTwo(minute)
    second = fillTwo(second)
    $(".date").text( hour + ':' + minute+ ':' +second);
    setTimeout(setClock, 60-second);
}

function fillTwo(num){
    if( num < 10 ){
        num = '0' + num;
    }
    return num;
}

</script>
</body>
</html>