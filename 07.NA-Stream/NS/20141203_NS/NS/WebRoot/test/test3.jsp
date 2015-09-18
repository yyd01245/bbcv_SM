<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>

<style>
body{margin:0; padding:0}
#to_top{width:30px; height:40px; padding:20px; font:14px/20px arial; text-align:center;  background:#06c; position:absolute; cursor:pointer; color:#fff}
</style>
<script>
window.onload = function(){
  var oTop = document.getElementById("to_top");
  var screenw = document.documentElement.clientWidth || document.body.clientWidth;
  var screenh = document.documentElement.clientHeight || document.body.clientHeight;
  oTop.style.left = screenw - oTop.offsetWidth +"px";
  oTop.style.top = screenh - oTop.offsetHeight + "px";
  window.onscroll = function(){
    var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
    oTop.style.top = screenh - oTop.offsetHeight + scrolltop +"px";
  }
  oTop.onclick = function(){
    document.documentElement.scrollTop = document.body.scrollTop =0;
  }
}  

</script>
<script language="javascript">
function onTopClick1() {  
	var c = document.getElementById("abc1");
        window.location.hash =  "#abc1"; ;   
    } 
function onTopClick2() {  
	var c = document.getElementById("abc2");
        window.location.hash =  "#abc2"; ;   
    } 
function onTopClick3() {  
	var c = document.getElementById("abc3");
        window.location.hash =  "#abc3"; ;   
    } 
function onTopClick4() {  
	var c = document.getElementById("abc4");
        window.location.hash =  "#abc4"; ;   
    } 
function onTopClick5() {  
	var c = document.getElementById("abc5");
        window.location.hash =  "#abc5"; ;   
    } 
</script>
  <input  type="button" name="Submit" value="提交"  onclick="javascript:onTopClick1();" />
  <input  type="button" name="Submit" value="提交"  onclick="javascript:onTopClick2();" />
  <input  type="button" name="Submit" value="提交"  onclick="javascript:onTopClick3();" />
    <input  type="button" name="Submit" value="提交"  onclick="javascript:onTopClick4();" />
      <input  type="button" name="Submit" value="提交"  onclick="javascript:onTopClick5();" />
      <h1>返回顶部</h1>


<div id="to_top">返回顶部</div>
<div id="abc1">问题解决后请在http:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！</div>
<div id="abc2">问题解决后请在问题问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！解问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！中发布您的解决问题方法,谢谢共享！</div>
<div id="abc3">问题解决后请在htt问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！p:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//ww问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！w.greensoftcoded.net中发布您的解决问题方法,谢谢共享！</div>
<div id="abc4">sa问题解决后请在h问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！t问问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！题解问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！tp://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！df</div>
<div id="abc5">s问题解决后请在htt问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！p:问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！//w问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！ww.greensoftcoded.net中发布您的解决问题方法,谢谢共享！问题解决后请在http://www.greensoftcoded.net中发布您的解决问题方法,谢谢共享！adf</div>