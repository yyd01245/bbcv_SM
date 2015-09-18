<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<% String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";%>


<% base = base+"RCM"+"/"; %>

 <% String  username = request.getParameter("username"); %>
 <% String  streamid = request.getParameter("streamid"); %>
 <% String  beforepage = request.getParameter("beforepage"); %>
 <% String  bannername = (String)request.getAttribute("bannername");%>

 <% String  resolution = request.getParameter("resolution"); %>
 

 
 <%Logger logger = LoggerFactory.getLogger("");
  logger.info("【手机详情页日志】"); 
 logger.info("username="+username+""); 
 logger.info("streamid="+streamid+""); 
 logger.info("影视类别：bannername="+bannername); 
 logger.info("高清[0],标清[1] ：resolution="+resolution);
 %>




<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>电影详情</title>
<link href="css/layout.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>

 <%
    String dataFile = "";
 
    if("0".equals(resolution)){dataFile="hdmbdata.js";}
    if("1".equals(resolution)){dataFile="sdmbdata.js";}
    
 %>
<script type="text/javascript" src="<%= base%>res/data/<%=dataFile%>" ></script>



</head>

  
 <script type="text/javascript">


 function onTopClick(a) {  
		var c = document.getElementById(a);
	        window.location.hash =  "#"+a;  
	    } 

 $(document).ready(function(e) {

         var beforepage ='<%=beforepage%>';
		$("#header h1.back").click(function(){
			if(beforepage=='vodjsp'){
				window.location.href = "vod.jsp?class=<%=bannername%>&username=<%=username%>&streamid=<%= streamid%>&resolution=<%=resolution%>";
			}else{
				window.location.href = "index.jsp?username=<%=username%>&streamid=<%= streamid%>&resolution=<%=resolution%>";
			}
		
		})
	;
		$("#header h1.mainpage").click(function(){
			window.location.href = "index.jsp?username=<%=username%>&streamid=<%= streamid%>&resolution=<%=resolution%>";
		})

	});   
 </script>

<body class="detail">
	<div id="header" >
		<div class="hover">
			<h1 class="back" id="moviename">${vod.name}</h1>
			<h1 class="mainpage logo"></h1>  
		</div>
	</div>
    <div id="detailInfo">
    	<img id="poster" src="${vod.mbPosterPath3}" width="120" class="img" />
        <div>
			<ul>
				<li>主演：<span id="actor">${vod.actor}</span> </li>
				<li>导演：<span id="director">${vod.director}</span> </li>   
				<li>类型：<span id="type">${vod.type}</span></li>
				<li>地区：<span id="ondate">${vod.area}</span></li>
			</ul>
			<a href="#" class="play" id="playbtn" style="width: 120.938;height: 40px;">播放</a>
			
		</div>
		
    </div>

	<div id="detailBt">			
<%--		<span class="xqlogo logo" onclick="javascript:onTopClick('detailintro');"></span>--%>
<%--		<span class="pllogo logo" onclick="javascript:onTopClick('uyan_frame');"></span>--%>
<%--		<span class="fxlogo logo" onclick="javascript:onTopClick('fx');"></span>--%>
<%--		<span class="sclogo logo" onclick="javascript:onTopClick('uyan_frame');"></span>--%>

	    <span class="xqlogo logo"></span>
		<span class="pllogo logo"></span>
		<span class="fxlogo logo"></span>
		<span class="sclogo logo"></span>
	</div>    
    <div id="detail">
		    <div class="item">
		        <h3 class="title">剧情简介</h3>
		        <div id="detailintro" class="de" >${vod.description}</div>
		    </div>
  
    		<div class="item">
				<h3 class="title">
				<!-- UY BEGIN -->
                   <div id="uyan_frame"></div>
                   <script type="text/javascript" src="http://v2.uyan.cc/code/uyan.js" charset="utf-8"></script>
                <!-- UY END -->
				</h3>
				<div  class="de"></div>
		    </div>   
			<div class="item">
				<h3 class="title">
				
				 <!-- JiaThis Button BEGIN -->
					<div class="jiathis_style" id="fx" >
						<span class="jiathis_txt" style="font-size: 15px">分享到：</span>
						<a class="jiathis_button_tools_1"></a>
						<a class="jiathis_button_tools_2"></a>
						<a class="jiathis_button_tools_3"></a>
						<a class="jiathis_button_tools_4"></a>	
						
					</div>
                  <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js" charset="utf-8"></script>
                <!-- JiaThis Button BEGIN -->
				</h3>
				<div  class="de"></div>
			</div>   
			<div class="item">
				<h3 class="title">收藏《<span id="movieConllection"></span>》成功</h3>
				<div  class="de"></div>
			</div> 
   
   
     </div>
    

<script type="text/javascript">
var jiathis_config = {data_track_clickback:'true'};
</script>

<!-- JiaThis Button END -->   










<%--<div id="elevator_item">--%>
<%--  <a id="elevator" onclick="return false;" title="回到顶部"></a>--%>
<%--</div>--%>
  
  
  
  
  
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

<script>
var moviename=getQueryStringByName("name"),currentEle,index=-1,timer,movieList=["gyc","aqgy","ypzjs"],rstp,jsonStr;
$(function(){
	changeMovie(moviename);
	$("#detailBt span.logo").bind("hover click",function(){
		var index = $(this).index();

			$("#detail .item").hide().eq(index).show();

		if(index==3){
			var content="收藏成功";
			collection(content);
			//$("#detail .item").show();
		//	alert("收藏成功");
		}
	})
	//timer=setInterval("changeMovie(checkIndex())",90000);
})

function collection(str){

	kysxcollection(str);
}


function checkIndex(){
	index>1?index=0:index++;
	return movieList[index];
}
var flag =true;
var ii = 0;
function changeMovie(moviename){


	while(flag){
	//	alert("第 "+ii+" 次 循环moviename="+moviename);
		$.each(data[ii].resource,function(i,ele){

		//	alert("内部循环"+i+" ele.name="+ele.name);
			
			if(ele.id==moviename){
			//	alert(ele.name);
				currentEle=ele;
				if(index == -1){
					index = parseInt(i);
				}
			
				flag=false;
				return;
			}else{
				
			}
			
		})
		ii = ii+1;    
	}
<%--	$.each(data[0].resource,function(i,ele){--%>
<%--		if(ele.id==moviename){--%>
<%--			currentEle=ele;--%>
<%--			if(index == -1){--%>
<%--				index = parseInt(i);--%>
<%--			}--%>
<%--			return;--%>
<%--		}--%>
<%--	})--%>
	if(currentEle){
		$("#movieConllection").html(currentEle.name);
		$("#moviename").html(currentEle.name);
		$("#director").html(currentEle.director);//导演
		$("#actor").html(currentEle.actor);//主演：
		$("#type").html(currentEle.type);//"类型："+
		$("#ondate").html(currentEle.area);//"地区："+
		$("#poster").attr("src",currentEle.mbPosterPath3)
		//$("#movie").html(currentEle.name)
		rstp = currentEle.rstp;

		
		$("#playbtn").click(function(){
			console.log(rstp);

		$.ajax( {
        url: "<%=basePath%>TransitionServlet?thisState=1&username=<%=username%>&vodname="+currentEle.name+"&rstp="+rstp, //这里是静态页的地址
        type: "GET", //静态页用get方法，否则服务器会抛出405错误
        success: function(data){}
		});
		
		// setTimeout("window.avodplay.toAndroid(rstp);", 2000);

		// var vodname=currentEle.name;
<%--		var vodname="关云长";--%>
<%--		 var posterurl=currentEle.poster;--%>
<%--		 --%>
<%--		 alert("ceshi");--%>
<%--	// setTimeout("kysxplay(rstp,vodname,posterurl);", 2000);--%>
<%--		 --%>
     //  setTimeout("kysxplay(rstp);",2000);
		
		
	   var rstp1 = "rstp:"+rstp;
	   var vodname = "vodname:"+currentEle.name;
	   var posterurl = "posterurl:"+currentEle.tvPosterPath3;

	    jsonStr = "{"+rstp1+","+vodname+","+posterurl+"}";
 
	    setTimeout("kysxplay(jsonStr);", 2000);
			
			return false;
		}) 
		$("#detailintro").html(currentEle.movieintro);	
	}

}

</script>
</body>
</html>
