<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>
<% base = base+"RCM"+"/"; %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%response.setCharacterEncoding("UTF-8");%>
<%request.setCharacterEncoding("UTF-8");%>
<title>TV高清导航页</title>


<script type="text/javascript" src="js/jquery.min.js"></script>

<link rel="stylesheet" href="css/style3.css"></link>


<%--<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>--%>
<%--获取流id--%>
<% String  streamid = request.getParameter("id"); %>
<%--打印日志--%>

 <%Logger logger = LoggerFactory.getLogger("【导航页日志】");logger.info("流化TV导航页面【streamid="+streamid+"】"); %>


 <script>
 
 //定义详情id;
 var vodid;

 function queryStatus(){    

     var steam_id = <%=streamid%>
     $.post("skipServlet?streamid="+steam_id+"&vodid="+vodid, function(data) {
    	   if(data!=""&&"error"!=data.trim()){
    	     	  window.location.href=data;   	   
    	        }	
 	});
		
   //tvvoddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod
	}	
<%--jQuery(function(){--%>
<%--	  var mode = !!document.createElement('canvas').getContext ? 'canvas' : 'table';--%>
<%--	  jQuery('#qrcode').qrcode({render:mode,text:"<%=basePath%>a?id=1&vp=1",width:400,height:400});--%>
<%-- })--%>
// queryStatus();
setInterval("queryStatus();",1000); 
		    
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

<div class="container animated">
	<div class="logo">单向终端云点播</div>
	<div class="date"></div>
    <div id="star-area">
        <img class="star" src="img/bg/star1.png" />
        <img class="star" src="img/bg/star2.png" />
        <img class="star" src="img/bg/star3.png" />
        <img class="star" src="img/bg/star4.png" />
        <img class="star" src="img/bg/star5.png" />
        <img class="star" src="img/bg/star6.png" />
    </div>
	<div class="left">		
		<div class="video">
			<h1 id="movie">关云长</h1>
			<video id="video" src="video/gyc.mp4" autoplay="true" loop width="743" height="432"></video>
			<div class="info_bg">		
				<div class="movieinfo">
					<div class="infowin">
						<ul>
							<li>评分：★★★★★</li>
							<li id="director">导演：麦兆辉 / 庄文强</li>
							<li id="actor">主演: 詹妮弗·劳伦斯</li>
							<li id="type">类型：动作/冒险/科幻/惊悚</li>
							<li id="ondate">上映：2013年11月21日</li>
						</ul>
						<p id="detail" class="text">刘备家眷被困曹营，关云长为存忠义甘作俘虏，被迫为曹军上阵。一场大战，关云长单人匹马斩杀敌方大将，技惊四座，而深受曹操赏识。可是关云长却“身在曹营心在汉”。对曹操所有礼遇均做出婉辞。后曹操查知关云长心仪刘备未过门的新妾“绮兰”，只因一个“义”字而未敢表白。曹操暗施狡计，送上催情毒酒，欲陷关羽于不义！关羽悬崖勒马，从而带着对此充满恨意的绮兰杀出曹营。一路上，关羽连毙曹操大将，但是，威武的背后，饱受感情折磨。曹操为稳定军心，而亲自出征，誓要将关羽擒拿！几经艰苦，终于来到黄河渡口，平静河边已布下曹军亲兵的十面埋伏！生死关头，死敌曹操竟一再以礼相待，忠义如关羽亦不禁动摇！身旁的绮兰苦苦相谏，关云长必须在“情”、“义”之间作出抉择。
						</p>
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
<script src="<%= base%>res/data/tvdata.js" ></script>
<script>


var id=getQueryStringByName("name"),currentEle,index=-1,timer,movieList=[],$star_area,tt,flag=0;;
var	timerule ;


$.each(data,function(i,ele){

	movieList.push(ele.id.trim());

	if(ele.id.trim()==id.trim()){
		timerule = ele.timerule.trim();
		//alert("第一次 ："+id+"  "+timerule);
	}
	// alert(i);   
  //   alert(ele.id.trim());
 // alert("哈哈"+ele.timerule.trim());
	//alert(movieList.length);
	//alert(movieList);
})

var $descHeight, $winHeight,maxScroll,$videoBg,$win= $(".movieinfo"),$videoBg = $(".info_bg"),scrollTimer,direction=1;
$(function(){
	setClock();
	changeMovie(id);

<%--//	$("#qrcode").qrcode({width:400,height:400,text:"<%=basePath%>a?id=<%=streamid%>&vp=id"});		--%>
	
	updataMaxScroll();
	scrollTimer = setInterval(autoScroll,1000);
	
	//  tt=self.setInterval("",3000);
//changeMovie(checkIndex())
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
			if(maxScroll-scrollTop >120 ){
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
			scrollTop: $win.scrollTop() - 120
		}, 300, "linear");
	}
	function keyDown(){
		 $win.stop(true, true).animate({
               scrollTop: $win.scrollTop() + 120
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
	
	//tt=window.clearInterval(tt);
	
	$.each(data,function(i,ele){
		if(ele.id==moviename){
			currentEle=ele;
			if(index == -1){
				index = parseInt(i);
			}
			return;
		}
	})
	if(flag==0){
		$(".container").addClass("fadeInLeftBig");
		flag=1;
	}
	else{
		$(".container").removeClass("fadeInLeftBig").addClass("fadeOutRightBig");
		
		setTimeout('$(".container").removeClass("fadeOutRightBig").addClass("fadeInLeftBig")',1000)
	}
	if(currentEle){

		
		timerule = currentEle.timerule.trim();
         vodid = currentEle.id;
		$("#timerule").html(currentEle.timerule.trim());	
		$("#movie").html(currentEle.name);
		$("#video")[0].src = currentEle.videosrc;
		$("#detail").html(currentEle.movieintro);	
		$("#director").html("导演："+currentEle.director);
		$("#actor").html("主演："+currentEle.actor);
		$("#type").html("类型："+currentEle.type);
		$("#ondate").html("上映："+currentEle.ondata);
		$("#qrcode").html("  <div  id='qrcode1'></div>");
	     $("#qrcode1").qrcode({width:340,height:340,text:"<%=basePath%>a?id=<%=streamid%>&vp="+currentEle.id});	
 
 

	}
 	 setTimeout(function(){changeMovie(checkIndex());},parseInt(timerule));
//	

	//  setTimeout("changeMovie(checkIndex())", timerule);
	// tt=self.setInterval("changeMovie(checkIndex())",timerule); 
	// tt=window.clearInterval(tt);
	
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