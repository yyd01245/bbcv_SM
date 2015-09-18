<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" style="text/css" href="voddetail/css/style.css">
<script src="js/jquery-1.11.0.min.js" language="javascript" type="text/javascript"></script>
<script src="voddetail/js/movie.js" language="javascript" type="text/javascript"></script>


<% String username=request.getParameter("username"); %>
<% String streamid=request.getParameter("streamid"); %>
<% String vodid=request.getParameter("vodid"); %>
<% String vod=request.getParameter("vod"); %>

 <%Logger logger = LoggerFactory.getLogger("");
 logger.info("【TV详情页日志】");
   logger.info("username="+username+"");
   logger.info("streamid="+streamid+"");
   logger.info("vodid="+vodid+"");
   logger.info("vod="+vod+"");
   %>

 <script>
	var data=<%=vod%>;


<%--	{--%>
<%--		id:"gyc",--%>
<%--		name:"关云长asdfasdfsad",--%>
<%--		movieintro:"刘备家眷被困曹营，关云长为存忠义甘作俘虏，被迫为曹军上阵。一场大战，关云长单人匹马斩杀敌方大将，技惊四座，而深受曹操赏识。可是关云长却“身在曹营心在汉”。对曹操所有礼遇均做出婉辞。后曹操查知关云长心仪刘备未过门的新妾“绮兰”，只因一个“义”字而未敢表白。曹操暗施狡计，送上催情毒酒，欲陷关羽于不义！关羽悬崖勒马，从而带着对此充满恨意的绮兰杀出曹营。一路上，关羽连毙曹操大将，但是，威武的背后，饱受感情折磨。曹操为稳定军心，而亲自出征，誓要将关羽擒拿！几经艰苦，终于来到黄河渡口，平静河边已布下曹军亲兵的十面埋伏！生死关头，死敌曹操竟一再以礼相待，忠义如关羽亦不禁动摇！身旁的绮兰苦苦相谏，关云长必须在“情”、“义”之间作出抉择。",--%>
<%--		videosrc:"video/gyc.mp4",	--%>
<%--		sliderposter:"images/gyc.jpg"--%>
<%--	};--%>

 function queryStatus(){    

     $.post("<%=basePath%>ShowVodInfo?flag=getnow&streamid=<%=streamid%>&vodid=<%=vodid%>&username=<%=username%>", function(returndata) {
    	   if(returndata!=""&&"error"!=returndata.trim()&&returndata.trim()!=""){
      //  var d = returndata;
        
    		//   alert(returndata.trim().length);
    
        	   
        //	   alert(returndata.trim());
    		 //   data=returndata;
    	//returndata=	 {id:"2",name:"aaavvv",movieintro:"sdfdasdf",videosrc:"http://192.168.30.237:8080/NS/video/gyc.mp4",sliderposter:"http://192.168.30.237:8080/RCM/res/images/gyc.jpg"};
    		  //  updatedata(d);   
    		    window.location.href=returndata;
    	        }	
 	});
		
   
	}	
<%--jQuery(function(){--%>
<%--	  var mode = !!document.createElement('canvas').getContext ? 'canvas' : 'table';--%>
<%--	  jQuery('#qrcode').qrcode({render:mode,text:"<%=basePath%>a?id=1&vp=1",width:400,height:400});--%>
<%-- })--%>
// queryStatus();
setInterval("queryStatus();",1000); 
    
</script>


</head>
<body>



	<div class="content">
		<div class="leftbar">
			<h1></h1>
			<div class="playimg">
				<img/>
			</div>
			<div class="button">即将开始播放</div>
		</div>
		<div class="rightbar">
			<div class="player">
				<video  autoplay="true" ></video>
			</div>
			<div class="text">
				<h2>剧情:</h2>
				<span></span>
			</div>
		</div>
			
	</div>
	<script type="text/javascript">

	inset(data);
	var relocate = setTimeout(redirect,20000);
	//tiaoyong
	function updatedata(newdata){
		//clearInterval(relocate);
		clearTimeout(relocate);
		inset(newdata);
		relocate= setTimeout("redirect()",20000);
	}


	function inset(data){
			//$(moviedata).each(function(index,ele){
				$("h1").html(data.name);
				$(".text span").html((data.movieintro).substr(0,108)+"...");
				$(".playimg").find("img").attr("src",data.sliderposter);
				$(".player").find("video").attr("src",data.videosrc);
			//})
		
	}
	function redirect(){
		window.location.href="subinter1.jsp?username=<%=username%>&streamid=<%=streamid%>&vodid=<%=vodid%>";
	}
	</script>
</body>
</html>