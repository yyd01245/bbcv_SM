<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html lang="en"> 
<head> 
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"> 
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript"> 
//怎么获取网页的高度 让页面一出来就滚动条在最底端 
function myScroll() 
{ 
//前边是获取chrome等一般浏览器 如果获取不到就是ie了 就用ie的办法获取 
var x=document.body.scrollTop||document.documentElement.scrollTop; 
var timer=setInterval(function(){ 
x=x-500; 
if(x<100) 
{ 
x=0; 
window.scrollTo(x,x); 
clearInterval(timer); 
} 
window.scrollTo(x,x); 
},"550"); 
} 


</script> 

<script type="text/javascript"> 
$(document).ready(function(){ 
// Scroll page to the bottom 
$('a.scrollToBottom').click(function(){ 
$('html, body, .content').animate({scrollTop: $(document).height()}, 300); 
return false; 
}); 
}) 
</script> 
<title></title> 
</head> 
<body id="bd"> 


<!-- JiaThis Button BEGIN -->
<div class="jiathis_style">
	<span class="jiathis_txt">分享到：</span>
	<a class="jiathis_button_tools_1"></a>
	<a class="jiathis_button_tools_2"></a>
	<a class="jiathis_button_tools_3"></a>
	<a class="jiathis_button_tools_4"></a>
	
	
</div>
<script type="text/javascript">
var jiathis_config = {data_track_clickback:'true'};

</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1975055" charset="utf-8"></script>
<!-- JiaThis Button END -->
<!-- JiaThis Button END -->
<a href="#" class="scrollToBottom">Scroll to bottom</a>jQuery
<div id="to_top">返回顶部</div>
<div id="abc1">问题解决后请在http:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！</div>
<div id="abc2">问题解决后请在问题问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！解问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！中发布您的解决问题方法,谢谢共享！</div>
<div id="abc3">问题解决后请在htt问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！p:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//ww问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！w.greensoftcoded.net中发布您的解决问题方法,谢谢共享！</div>
<div id="abc4">sa问题解决后请在h问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！t问问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！题解问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！tp://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！df</div>
<div id="abc5">s问题解决后请在htt问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！p:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//w问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！ww.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！adf</div>

<div 
style="position:fixed;right:10px;bottom:50px;width:20px;height:70px;background-color:red;cursor:hand;" class="backtotop">返回顶端</div> 
</div> 
</body> 
</html> 