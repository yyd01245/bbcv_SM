<%@ page language="java" import="java.util.*,org.slf4j.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
    <meta http-equiv="Content-Language" content="zh" />
    <meta name="description" content="bShare是中国功能最强大,覆盖面最广的社交分享工具!提供一键分享到新浪微博,分享到博客,淘宝,人人网,QQ空间等各大社会化网站;支持视频,图片,网页收藏按钮代码功能,并业内最完善的分享数据追踪服务." />
    <meta name="keywords" content="分享代码,分享到代码,分享工具" />
	<meta name="author" content="Buzzinate" />

    <title>分享代码,分享按钮,网页分享-中国最强大的社会化图文分享工具！</title>

    <!-- Be aware: This will be replaced in build script -->
    <link rel="stylesheet" href="http://static.bshare.cn/css/bshare2.css?t=20140730.css" type="text/css" />
    <style type="text/css">
    	.top-menu-popup .bLink { color: #333; }
    </style>
    
    <link rel="icon" href="http://www.bshare.cn/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="http://www.bshare.cn/favicon.ico" type="image/x-icon" />
	
    <script type="text/javascript" charset="utf-8">
		var BSHARE_WEBSITE_HOST = "http://www.bshare.cn/";
		var BSHARE_BUTTON_HOST = "http://b.bshare.cn/";
		var BSHARE_STATIC_HOST = "http://static.bshare.cn/"; // used by bshare-web-common.js?v=20140730 and unitpngfix.js
		var bookmarkletOff = false;
	</script>
	<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/libs/jquery-1.7.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/libs/jquery.tools.ui.min.1.2.6.js"></script>
	<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/libs/jquery.bgiframe.min.js"></script>
	<!-- Be aware: This will be replaced in build script -->
	<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/bshare-web-common.js?v=20140730"></script>
	<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/ajaxLogout.js"></script>
	<!--[if lt IE 7]>
    <script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/js/libs/unitpngfix.js"></script>
    <![endif]-->
    
    <style type="text/css">
    	.identifyImg { width: 14px; margin-bottom: -3px; }
    	
    	
    	.element.style {
display: block;
left: 50%;
top: 80%;
margin-left: -274px;
margin-top: -204px;
position: fixed;
height: 408px;
width: 548px;
}
    	
    	
    	
    </style>
</head>

<body style="background:#f5f5f5;">
---------------------------------




<div class="bshare-custom">
<a title="分享到QQ空间" class="bshare-qzone"></a>
<a title="分享到新浪微博" class="bshare-sinaminiblog"></a>
<a title="分享到人人网" class="bshare-renren"></a>
<a title="分享到腾讯微博" class="bshare-qqmb"></a>
<a title="分享到网易微博" class="bshare-neteasemb"></a>
<a title="更多平台" class=" bshare-more-icon more-style-addthis"></a>
<span class="BSHARE_COUNT bshare-share-count">0</span></div>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=2&amp;lang=zh">
</script>
<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>








-------------------------------------

	<div class="container-center">
		<noscript>
			<span style="color:red;"><strong>此页面使用Javascript。请在您的浏览器中启用Javascript，以浏览全页面。谢谢！</strong></span>
			<div class="spacer10"></div>
		</noscript>
	</div>
    


<style type="text/css">
#topMenu { border-bottom: 1px solid #e5e5e5; background: #f5f5f5; box-shadow: 1px 1px 3px #efefef; font-size: 12px; line-height: 30px; }
</style>

<div id="topMenu">
    <div class="container-center" style="overflow:hidden;">
        <div class="topSubSite left">
            <a title="微积分" class="bLinkDark" href="http://points.bshare.cn" target="_blank">微积分</a> |
            <a title="乐知推荐插件" class="bLinkDark" href="http://www.lezhi.me" target="_blank">乐知推荐插件</a>
        </div>
        
        <div id="mainTopNavDiv" style="background:#f5f5f5;overflow:hidden;padding:0 15px;color:#000;font-size:12px;text-align:right;line-height:28px;height:28px;">
            
            
                <div class="right">
                    <table class="topMain">
                        <tr>
                            <td><a title="立即注册" class="bLink" href="/register" 
                                style="color:red;">立即注册</a></td>
                            <td>|</td>
                            <td><a title="帮助" class="bLink" href="/help" 
                                style="color:#000;">帮助</a></td>
                            <td>|</td>
                            <td><a title="登录" class="bLink" href="/login" 
                                style="color:#000;font-size:14px;font-weight:bold;">登录</a></td>
                        </tr>
                    </table>
                </div>
            
            <div class="clear"></div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="clear"></div>

<!-- AUTHORITIES POPUP -->

<!-- AUTHORITIES-POPUP-END -->

<!-- SCRIPTS-START -->
<script type="text/javascript" charset="utf-8">
    function oauthLoginSuccess(type) {
        window.location.href = "loginAction";
    }
    
    $(function() {
        $("#bshare_logout").live("click", function(e) {
            passport_ajax_logout("http://one.bshare.cn");
            window.location.href='/logout';
            return false;
        });
        
        
    });
</script>

	
    <div class="clear"></div>
    

























<style type="text/css">
.top-menu-popup .bLink { color: #333; }
</style>

<div style="background:#fff;min-width:980px;">
    <div class="spacer30"></div>
    <div class="container-center">
        <div class="left" style="width:200px;">
            <a title="bShare.cn" href="http://www.bshare.cn">
                
                
                
                    <img alt="bShare.cn" src="http://static.bshare.cn/images/bshare-logo-main.gif"/>
                
            </a>
            <div class="clear"></div>
        </div>
        
		<div class="right" style="padding-top:10px;">
		    <div class="right" style="margin-left:5px;">
		        <a class="top-button-single" href="http://www.bshare.cn/blog" target="_blank">博客</a>
		    </div>
		    <div class="right" style="margin-left:5px;">
		        <div class="top-button" onclick="document.location='/help';">
		            <div>帮助</div>
		            <div class="arrow-down"></div>
		        </div>
		        <div class="top-menu-popup">
		            <div><a title="注册" class="bLink" href="/help/register">注册</a></div>
		            <div><a title="安装bShare" class="bLink" href="/help/installImage">安装bShare</a></div>
		            <div><a title="分享按钮自定义" class="bLink" href="/help/apiJsParams">分享按钮自定义</a></div>
		            <div><a title="bShare API" class="bLink" href="/help/apiClientJsPage">bShare API</a></div>
		            <div><a title="分享平台代码参考" class="bLink" href="/help/platformNames">分享平台代码参考</a></div>
		            <div><a title="分享优化功能设置" class="bLink" href="/help/shareSettings">分享优化功能设置</a></div>
		            <div><a title="常见问题" class="bLink" href="/help/general">常见问题</a></div>
		
		            <div><a title="使用建议" class="bLink" 
		                href="/bestPractices">使用建议</a></div>
		            <div><a title="范例" class="bLink" 
		                href="/integrations">范例</a></div>
		            <!-- Partner Links -->
		            <div><a title="站长资讯" class="bLink" href="http://www.admin5.com/browse/177/index.shtml" target="_blank">站长资讯
		                <img src="http://static.bshare.cn/images/icon_new_window.gif" style="margin:0 0 3px 3px;">
		            </a></div>
		        </div>
		    </div>
		    <div class="right" style="margin-left:5px;">
		        <a class="top-button-single" 
		            href="/analytics">数据统计</a>
		        
		        
		    </div>
		    <div class="right" style="margin-left:5px;">
		        <div class="top-button" onclick="document.location='/products';">
		            <div>bShare产品</div>
		            <div class="arrow-down"></div>
		        </div>
		        <div class="top-menu-popup">
		            <div><a title="bShare" class="bLink" href="/products/bshare">bShare</a></div>
		            <div><a title="bShare高级" class="bLink" href="/products/premiumServices">bShare高级</a></div>
		            <div><a title="bShare电商" class="bLink" href="http://ec.bshare.cn" target="_blank">bShare电商</a></div>
		            <div><a title="微积分" class="bLink" href="http://points.bshare.cn" target="_blank">微积分</a></div>
		            <div><a title="乐知推荐插件" class="bLink" href="http://lz.bshare.cn" target="_blank">乐知推荐插件</a></div>
		            <div><a title="bShare图片分享" class="bLink" href="/products/imageShare">bShare图片分享</a></div>
		            <div><a title="bShare划词分享" class="bLink" href="/products/bshareDrag">bShare划词分享</a></div>
		            <div><a title="bShare视频分享" class="bLink" href="/products/videoShare">bShare视频分享</a></div>
		            <div><a title="bShare书签" class="bLink" href="/products/bookmark">bShare书签</a></div>
		            <div><a title="bShare一键通" class="bLink" href="/products/bsync">bShare一键通</a></div>
		            <div><a title="bShare Mobile SDK" class="bLink" href="/products/bshareMobileSDK">bShare Mobile SDK</a></div>
		            <div><a title="bURL" class="bLink" href="/products/burl">bURL</a></div>
		            <!-- <div><a title="bShare热文推荐" class="bLink" href="/products/bshareRecomm">bShare热文推荐</a></div> -->
		            <!-- <div><a title="乐知" class="bLink" href="/products/lezhi">乐知</a></div>  -->
		            <!-- 
		            <div><a title="bShare Pro" class="bLink" href="/bsharePro">bShare Pro</a></div>
		            <div><a title="bShare企业版" class="bLink" href="/bshareEnterprise">bShare企业版</a></div>
		            -->
		        </div>
		    </div>
		    <div class="right" style="margin-left:5px;">
		        <div class="top-button" onclick="document.location='/intro';">
		            <div>bShare介绍</div>
		            <div class="arrow-down"></div>
		        </div>
		        <div class="top-menu-popup">
		            <div><a title="什么是bShare?" class="bLink" href="/intro">什么是bShare?</a></div>
		            <div><a title="bShare特色" class="bLink" href="/intro/reasons">bShare特色</a></div>
		            <div><a title="支持的平台" class="bLink" href="/intro/services">支持的平台</a></div>
		            <!-- <div><a title="谁在使用bShare?" class="bLink" href="/intro/bshareSites">谁在使用bShare?</a></div> -->
		            <div><a title="合作伙伴" class="bLink" href="/partners">合作伙伴</a></div>
		            <div><a title="展览厅" class="bLink" href="/gallery">展览厅</a></div>
		            <div class="hidden"><a title="实验室" class="bLink" href="/labs">实验室</a></div>
		            <!-- 
		            <div><a title="自定义按钮" class="bLink" href="/introCustomize">自定义按钮</a></div>
		            -->
		        </div>
		    </div>
		    <div class="right">
		        <div class="top-button" onclick="document.location='/help/install';">
		            <div>bShare安装</div>
		            <div class="arrow-down"></div>
		        </div>
		        <div class="top-menu-popup">
		            <div><a title="一般网站" class="bLink" href="/help/install">一般网站</a></div>
		            <div><a title="BBS" class="bLink" href="/help/installBbs">BBS</a></div>
		            <div><a title="CMS" class="bLink" href="/help/installCms">CMS</a></div>
		            <div><a title="电子商务" class="bLink" href="/help/installEc">电子商务</a></div>
		            <div><a title="淘宝" class="bLink" href="/help/installTaobao">淘宝</a></div>
		            <div class="hidden"><a title="电子商务" class="bLink" href="/help/installEcomm">电子商务</a></div>
		            <div><a title="博客" class="bLink" href="/help/installBlog">博客</a></div>
		            <div><a title="邮件模板" class="bLink" href="/help/installEmail">邮件模板</a></div>
		            <div><a title="乐知推荐插件" class="bLink" href="http://www.lezhi.me/get?uuid=nologin" target="_blank">乐知推荐插件</a></div>
		        </div>
		    </div>
		    <div class="clear"></div>
		</div>
		<div id="top-bookmarklet" style="position:absolute;top:65px;display:none;"><a href="javascript:var%20d=document;var%20b=d.body;if(b&&!document.xmlVersion){if(!d.getElementById('bsBox')){void(y=document.createElement('script'));void(y.src='http://static.bshare.cn/b/bookmark.js#lang=zh');void(b.appendChild(y));}else{bShare.show();}}" 
		    style="text-indent:-9999px;width:80px;height:120px;display:block;background:url(http://static.bshare.cn/images/top-bookmarklet.png) no-repeat;">bShare书签</a></div>
        
		<!-- SCRIPTS -->
		<script type="text/javascript" charset="utf-8">
		    $(function() {
		        if (!$.browser.msie && !bookmarkletOff) {
		            $(window).resize(function() {
		                var ww = $(window).width();
		                if (ww < 1180) {
		                    $("#top-bookmarklet").hide();
		                } else {
		                    $("#top-bookmarklet").show();
		                    $("#top-bookmarklet").css("right", (ww-980)/2 - 100);
		                }
		            }).resize();
		        }
		    });
		</script>
        
        <div class="clear spacer20"></div>
    </div>
</div>

	<div class="clear"></div>

	
		<style>
		    #bannerDiv { height: 350px; position: relative; }
		    .wrapper-banner { height: 350px; overflow: hidden; }
			.wrapper-banner a { display: block; }
			.wrapper-tabs { position: absolute; z-index: 9999; left: 450px; bottom: 10px; width: 90px; *width: 95px; }
			.wrapper-tabs a { width: 11px; height: 11px; float: left; *display: inline; margin: 3px; background: url(http://static.bshare.cn/images/banners/banner-dots.png) 0 -11px no-repeat;
			 display: block; font-size: 1px; }
			.wrapper-tabs a:hover { background-position: 0 0; }
			.wrapper-tabs a.current { background-position: 0 0; }
		</style>
        <script type="text/javascript" charset="utf-8">
            $(function() {
                 $(".wrapper-tabs").tabs(".wrapper-banner > a", {
                    effect: 'fade',
                    fadeOutSpeed: 1000,
                    fadeInSpeed: 1000,
                    rotate: true
                }).slideshow({
                    interval: 5000,
                    autoplay: true
                }); 
                //$(".wrapper-tabs").data("slideshow").play();
            });
        </script>
        <div class="container-center" id="bannerDiv">
            <div class="wrapper-banner" >
<a href="http://www.bshare.cn/intro"  target="_blank"  style="display:block" ><img src="http://static.bshare.cn/repository/pointsImage/banner/G/Gn7GkcufpHtJYlZE.jpg"  alt="bShare产品介绍"  title="bShare产品介绍" /></a>
</div>
<div class="wrapper-tabs" ><a href="#"></a></div>

        </div>
		<div class="clear"></div>
		

























<!-- STATUS MESSAGES START -->
<div id="status-messages-pdiv" style="visibility:hidden;height:26px;margin:0px auto 5px;width:850px;z-index:100;position:relative;" align="center">
    <div id="status-messages" class="status-messages bMessages"></div>
    <div id="status-messages-div" style="display:none;" class="bMessages">
	    
	    
	</div>
	<div id="field-error-messages-div" style="display:none;" class="bMessages">
	    
	</div>
	<div class="clear"></div>
</div>
<!-- STATUS MESSAGES END -->
	
		





























<meta name="description" content="bShare是中国功能最强大,覆盖面最广的社交分享工具!提供一键分享到新浪微博,分享到博客,淘宝,人人网,QQ空间等各大社会化网站;支持视频,图片,网页收藏按钮代码功能,并业内最完善的分享数据追踪服务." />
<meta name="keywords" content="分享代码,分享到代码,分享工具" />

<link href="http://static.bshare.cn/css/libs/scrollable.css" rel="stylesheet" type="text/css" />
<style>
    #floatColorDiv img { margin-right: 3px; *margin-right: 2px; }
    .verticalradio-spacer{padding-top:14px;}
</style>

<div style="background:#fff;box-shadow:0px 10px 20px 0px #ddd;-moz-box-shadow:0px 10px 20px 0px #ddd;-webkit-box-shadow:0px 10px 20px 0px #ddd;">
<div class="container-center" style="margin-top:-30px;*margin-top:-30px;_margin-top:-15px;">
<div>

    <!-- <span class="heading1 left floatFix" style="font-size:30px;color:#666;margin-top:17px;padding-right:20px;background:url(http://static.bshare.cn/images/arrow-down-orange2.png) no-repeat 100% 50%;">获取<span style="font-size:30px;vertical-align:bottom;"><font style="color:#f60;">b</font>Share</span></span>
    -->
    <img class="left" style="margin-top:25px;" src="http://static.bshare.cn/images/homepage-getbshare.png" alt="获取bShare"/>
     
    <div class="right" style="padding-top:25px;*width:320px;">
        <div>
            <div class="left" style="color:#333;font-size:14px;font-weight:bold;line-height:24px;margin-right:5px;">试一下:&nbsp;&nbsp;</div>
            <div class="bshare-custom left icon-medium"><a title="分享到一键通" class="bshare-bsharesync"></a><a title="分享到QQ空间" class="bshare-qzone"></a><a title="分享到新浪微博" class="bshare-sinaminiblog"></a><a title="分享到人人网" class="bshare-renren"></a><a title="分享到腾讯微博" class="bshare-qqmb"></a><a title="分享到网易微博" class="bshare-neteasemb"></a><a title="更多平台" class="bshare-more bshare-more-icon more-style-addthis"></a><span class="BSHARE_COUNT bshare-share-count">0</span></div>
            <script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=f8a4a53f-438a-4ffa-939f-7f313a7e2b05&amp;pophcol=2&amp;lang=zh"></script>
            <script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
        </div>
        
        <div class="clear"></div>
        <div class="left" style="margin:5px 5px 0 100px;"><img src="http://static.bshare.cn/images/hand.gif"/></div>
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
</div>
<div class="clear"></div>

        
<form id="getButtonAction" name="getButtonAction" onsubmit="return getButtonSubmit();" action="/help/installAction" method="POST" class="mp0" style="margin-top:-20px;">
<div class="heading3" style="font-size:14px;">请选择一款按钮样式：</div>
<div class="clear spacer20"></div>

<div class="left" style="width:650px;">
    <div class="left" style="width:480px;">
        
        
            <style>
                .verticalradio-spacer{padding-top:9px;}
            </style>
            <input type="radio" name="buttonStyle" id="buttonStylebsync:C1:true:blue" value="bsync:C1:true:blue" tabindex="6" style="vertical-align:top;"/><label for="buttonStylebsync:C1:true:blue"><img src="http://static.bshare.cn/images/buttons/bsync-c1-b.gif"/></label>
<div class="verticalradio-spacer"></div>
<input type="radio" name="buttonStyle" id="buttonStylebsync:C4:true:orange" value="bsync:C4:true:orange" tabindex="6" style="vertical-align:top;"/><label for="buttonStylebsync:C4:true:orange"><img src="http://static.bshare.cn/images/buttons/button-bsharesync-orange.gif"/></label>
<div class="verticalradio-spacer"></div>
<input type="radio" name="buttonStyle" id="buttonStyleC1:true:addthis" value="C1:true:addthis" tabindex="6" style="vertical-align:top;"/><label for="buttonStyleC1:true:addthis"><img src="http://static.bshare.cn/images/buttons/homepage/custom-c1-addthis-c.gif"/></label>
<div class="verticalradio-spacer"></div>
<input type="radio" name="buttonStyle" id="buttonStyleC2:true:addthis" value="C2:true:addthis" tabindex="6" style="vertical-align:top;"/><label for="buttonStyleC2:true:addthis"><img src="http://static.bshare.cn/images/buttons/homepage/custom-c2-addthis-c.gif"/></label>
<div class="verticalradio-spacer"></div>
<input type="radio" name="buttonStyle" id="buttonStyleC2P:true:addthis" value="C2P:true:addthis" tabindex="6" style="vertical-align:top;"/><label for="buttonStyleC2P:true:addthis"><img src="http://static.bshare.cn/images/buttons/homepage/custom-c2p-addthis-c.gif"/></label>
<div class="verticalradio-spacer"></div>
<input type="radio" name="buttonStyle" id="buttonStyleC1:x:addthis" value="C1:x:addthis" tabindex="6" style="vertical-align:top;"/><label for="buttonStyleC1:x:addthis"><img src="http://static.bshare.cn/images/buttons/homepage/custom-c1-addthis-n.gif"/></label>
<div class="verticalradio-spacer"></div>

        
    </div>
    <div id="floatColorDiv" class="left" style="width:170px; margin-top: 20px;">
        <input type="radio" name="buttonStyle" id="buttonStyle3:DarkOrange:x" value="3:DarkOrange:x" tabindex="6" style="vertical-align:top;" style="vertical-align:top;"/><label for="buttonStyle3:DarkOrange:x" style="vertical-align:top;"><img src="http://static.bshare.cn/frame/images/button_custom3-zh-DarkOrange.gif"/></label>
<input type="radio" name="buttonStyle" id="buttonStyle3:Blue:x" value="3:Blue:x" tabindex="6" style="vertical-align:top;" style="vertical-align:top;"/><label for="buttonStyle3:Blue:x" style="vertical-align:top;"><img src="http://static.bshare.cn/frame/images/button_custom3-zh-Blue.gif"/></label>
<input type="radio" name="buttonStyle" id="buttonStyle3:Green:x" value="3:Green:x" tabindex="6" style="vertical-align:top;" style="vertical-align:top;"/><label for="buttonStyle3:Green:x" style="vertical-align:top;"><img src="http://static.bshare.cn/frame/images/button_custom3-zh-Green.gif"/></label>

        <div class="clear"></div>
    </div>
    <div class="clear spacer10"></div>
</div>

<div class="left floatFix ie6floatfix" style="margin:20px 20px 0;*margin:20px 20px 0;width:47px;height:107px;background:url(http://static.bshare.cn/images/arrow-right-large.png) no-repeat;">
    <!-- <img alt=">" style="vertical-align:middle;padding:0 30px;*padding:0 25px;*width:47px;" src="http://static.bshare.cn/images/arrow-right-large.png"/>-->
</div>
<div class="right floatFix ie6floatfix" style="text-align:center;">
    <button type="submit" id="getBshareButton" class="bButton-blue" style="margin-top:23px;padding:15px 40px;font-weight:bold;font-size:18px;">免费获取分享代码</button>
    <div class="clear spacer5"></div>
    <a class="bLink" target="_blank" style="text-align:center;color:#0068FF;" href="/moreStyles">&gt;&gt;&nbsp;更多按钮样式</a>
    <div class="clear"></div>
</div>
</form>




<div class="clear"></div>
</div>
<div class="clear spacer15"></div>
</div>
<div class="clear spacer20"></div>

<div class="container-center" style="padding:10px 0;">
<div class="heading3" style="color:#666;font-size:14px;">谁在使用bShare</div>
<div class="clear"></div>

<style>
    .sitescr { width: 112px; padding: 0 12px; }
    .sitescr div.spritesite { height: 47px; width: 112px; background: url(http://static.bshare.cn/images/logos-scroll2/sprite-index-sites.gif) no-repeat 0 0; }
    .sitescr div.sprite-07073{ background-position: 0 0; } 
    .sitescr div.sprite-313{ background-position: 0 -46px; } 
    .sitescr div.sprite-53kf{ background-position: 0 -92px; } 
    .sitescr div.sprite-91{ background-position: 0 -139px; } 
    .sitescr div.sprite-HOLA{ background-position: 0 -186px; } 
    .sitescr div.sprite-abang{ background-position: 0 -232px; } 
   
    .sitescr div.sprite-blogbus{ background-position: 0 -325px; } 
    .sitescr div.sprite-booen{ background-position: 0 -371px; } 
    .sitescr div.sprite-boosj{ background-position: 0 -417px; } 
   
    .sitescr div.sprite-chinadaily{ background-position: 0 -510px; } 
    .sitescr div.sprite-cnfol{ background-position: 0 -557px; } 
    .sitescr div.sprite-discuz{ background-position: 0 -603px; } 
   
   
    .sitescr div.sprite-fairyfish{ background-position: 0 -742px; } 
    .sitescr div.sprite-feiku{ background-position: 0 -788px; } 
    .sitescr div.sprite-hc360{ background-position: 0 -835px; } 
    .sitescr div.sprite-huasheng{ background-position: 0 -881px; } 
    .sitescr div.sprite-huisou{ background-position: 0 -928px; } 
    .sitescr div.sprite-i10086{ background-position: 0 -974px; } 
    .sitescr div.sprite-ifeng{ background-position: 0 -1021px; } 
  
    .sitescr div.sprite-jimdo{ background-position: 0 -1115px; } 
   
    .sitescr div.sprite-luobo{ background-position: 0 -1208px; } 
    .sitescr div.sprite-mbaobao{ background-position: 0 -1254px; } 
    .sitescr div.sprite-meishichina{ background-position: 0 -1300px; } 
    .sitescr div.sprite-newshainan{ background-position: 0 -1392px; } 
    .sitescr div.sprite-p12580{ background-position: 0 -1439px; } 
   
    .sitescr div.sprite-to8to{ background-position: 0 -1532px; } 
   
    .sitescr div.sprite-woshao{ background-position: 0 -1625px; } 
  
    .sitescr div.sprite-yesky{ background-position: 0 -1718px; } 
    .sitescr div.sprite-yixieshi{ background-position: 0 -1765px; } 
    .sitescr div.sprite-zhiyin{ background-position: 0 -1812px; } 
</style>
            
<div class="sitebar">
    <a class="prev browse left" style="margin:30px 10px 30px 0;"></a>
    <div class="hscrollable-sites" id="sites-scroller" style="padding:22px 0;width:869px;height:48px;">
        <!-- <div class="items" style="font-size:14px;cursor:pointer;" onclick="window.open('/help/bshareSites');"> -->
        <div class="items" style="font-size:14px;">
            <div>                               
				<div class="left sitescr"><img alt="中新网" src="http://static.bshare.cn/images/logos-sites/chinanews.gif"/></div>
				<div class="left sitescr"><img alt="中华网" src="http://static.bshare.cn/images/logos-sites/china.gif"/></div>				
				<div class="left sitescr"><img alt="天涯" src="http://static.bshare.cn/images/logos-sites/tianya.gif"/></div>
				<div class="left sitescr"><img alt="淘宝网" src="http://static.bshare.cn/images/logos-sites/taobao.gif"/></div>
				<div class="left sitescr"><img alt="站长之家" src="http://static.bshare.cn/images/logos-sites/chinaz.gif"/></div>
				<div class="left sitescr"><img alt="Admin5" src="http://static.bshare.cn/images/logos-sites/admin5.gif"/></div>		
				<div class="left sitescr"><div class="spritesite sprite-ifeng"></div></div> 
			 </div>
            
            <div>               
               <div class="left sitescr"><div class="spritesite sprite-discuz"></div></div>
               <div class="left sitescr"><div class="spritesite sprite-abang"></div></div>
               <div class="left sitescr"><div class="spritesite sprite-yesky"></div></div>
               <div class="left sitescr"><div class="spritesite sprite-mbaobao"></div></div>
               <div class="left sitescr"><div class="spritesite sprite-chinadaily"></div></div>
              <div class="left sitescr"><div class="spritesite sprite-i10086"></div></div>
               <div class="left sitescr"><div class="spritesite sprite-to8to"></div></div>
            </div>
            <div>                       
                <div class="left sitescr"><div class="spritesite sprite-p12580"></div></div>    
                <div class="left sitescr"><div class="spritesite sprite-boosj"></div></div>                              
                <div class="left sitescr"><div class="spritesite sprite-meishichina"></div></div> 
                <div class="left sitescr"><div class="spritesite sprite-cnfol"></div></div>                    
                <div class="left sitescr"><div class="spritesite sprite-07073"></div></div> 
                <div class="left sitescr"><div class="spritesite sprite-blogbus"></div></div>                  
                <div class="left sitescr"><div class="spritesite sprite-luobo"></div></div>           
            </div>
            <div>                             
                <div class="left sitescr"><div class="spritesite sprite-fairyfish"></div></div> 
                <div class="left sitescr"><div class="spritesite sprite-woshao"></div></div> 
                <div class="left sitescr"><div class="spritesite sprite-313"></div></div>
                <div class="left sitescr"><div class="spritesite sprite-booen"></div></div> 
                <div class="left sitescr"><div class="spritesite sprite-huisou"></div></div>  
                <div class="left sitescr"><div class="spritesite sprite-zhiyin"></div></div> 
               <div class="left sitescr"><div class="spritesite sprite-yixieshi"></div></div>  
            </div>
           <div>                
                <div class="left sitescr"><div class="spritesite sprite-53kf"></div></div>                              
                <div class="left sitescr"><div class="spritesite sprite-newshainan"></div></div>
                 <div class="left sitescr"><div class="spritesite sprite-feiku"></div></div>
                  <div class="left sitescr"><div class="spritesite sprite-jimdo"></div></div>
                <div class="left sitescr"><img alt="DNSPod" src="http://static.bshare.cn/images/logos-scroll2/dnspod.gif"/></div> 
                 <div class="left sitescr"><img alt="卢松松博客" src="http://static.bshare.cn/images/logos-sites/lusongsong.gif"/></div>  
                <div class="left sitescr"><img alt="深圳之窗" src="http://static.bshare.cn/images/logos-sites/sznet.gif"/></div>                  
            </div>
             <div>                           
                          
                 <div class="left sitescr"><img alt="FocusSend" src="http://static.bshare.cn/images/logos-sites/focussend.gif"/></div> 
                 <div class="left sitescr"><img alt="美橙互联" src="http://static.bshare.cn/images/logos-sites/cndns.gif"/></div> 
                 <div class="left sitescr"><img alt="建站之星" src="http://static.bshare.cn/images/logos-sites/sitestar.gif"/></div>
                 <div class="left sitescr"><img alt="CMSTOP" src="http://static.bshare.cn/images/logos-sites/cmstop.gif"/></div>
                 <div class="left sitescr"><img alt="聚橙网" src="http://static.bshare.cn/images/logos-sites/juooo.gif"/></div>
                  <div class="left sitescr"><img alt="Ckplayer" src="http://static.bshare.cn/images/logos-sites/ckplayer.gif"/></div>
                  <div class="left sitescr"><img alt="米壳网" src="http://static.bshare.cn/images/logos-sites/it224.gif"/></div> 
            </div>
            <div>                                           
                 <div class="left sitescr"><img alt="每日通贩" src="http://static.bshare.cn/images/logos-sites/meiribuy.gif"/></div> 
                 
            </div>
        
        </div>
        <div class="clear"></div>
    </div>
    <a class="next browse right" style="margin:30px 0 30px 10px;"></a>
    <div class="clear"></div>
    <!-- <a style="margin:-20px 10px 0 0;font-size:14px;" class="right bLinkOrange" target="_blank" href="/help/bshareSites">&gt;&gt;&nbsp;更多</a> -->
    <div class="clear"></div>
</div>
<div class="clear spacer5"></div>

<div>
    <div class="div-rounded-5">
        
        
            <style>
                .partnerscr { width: 50px; text-align: center; overflow: hidden; padding: 0 9px; }
                .partnerscr div.sprite { height: 50px; width: 50px; background: url(http://static.bshare.cn/frame/images/sprite-index-platforms.gif) no-repeat 0 0; }
                .partnerscr div.sprite-douban{ background-position: 0 0;  } 
                .partnerscr div.sprite-kaixin001{ background-position: 0 -50px;  } 
                .partnerscr div.sprite-neteasemb{ background-position: 0 -100px;  } 
                .partnerscr div.sprite-peoplemb{ background-position: 0 -150px;  } 
                .partnerscr div.sprite-qqmb{ background-position: 0 -200px;  } 
                .partnerscr div.sprite-qqxiaoyou{ background-position: 0 -250px;  } 
                .partnerscr div.sprite-qzone{ background-position: 0 -300px;  } 
                .partnerscr div.sprite-renren{ background-position: 0 -350px;  } 
                .partnerscr div.sprite-shouji{ background-position: 0 -400px;  } 
                .partnerscr div.sprite-sinaminiblog{ background-position: 0 -450px;  } 
                .partnerscr div.sprite-sohuminiblog{ background-position: 0 -500px;  } 
                .partnerscr div.sprite-southmb{ background-position: 0 -550px;  } 
                .partnerscr div.sprite-taojianghu{ background-position: 0 -600px;  } 
                .partnerscr div.sprite-tianya{ background-position: 0 -650px;  } 
            </style>
            <div class="left" style="width:49%;">
                <div class="heading3" style="color:#666;font-size:14px;">官方合作平台</div>
                <div class="clear spacer20"></div>
                <div style="cursor:pointer;height:125px;" onclick="window.open('/intro/services');">
                    <div class="left partnerscr"><div class="sprite sprite-qqmb"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-qzone"></div></div>
                    <div class="left partnerscr"><div class="sprite sprite-qqxiaoyou"></div></div>
                    <div class="left partnerscr"><div class="sprite sprite-sinaminiblog"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-renren"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-sohuminiblog"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-neteasemb"></div></div>
                    <div class="clear spacer20"></div>
                    <div class="left partnerscr"><div class="sprite sprite-taojianghu"></div></div>
                    <div class="left partnerscr"><div class="sprite sprite-kaixin001"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-douban"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-tianya"></div></div> 
                    <div class="left partnerscr"><div class="sprite sprite-southmb"></div></div>
                    <div class="left partnerscr"><div class="sprite sprite-peoplemb"></div></div>
                    <div class="left partnerscr"><div class="sprite sprite-shouji"></div></div>
                    <div class="clear"></div>
                </div>
                
                <div class="clear spacer5"></div>
                <a class="right bLinkOrange" target="_blank" style="margin-right:12px;" href="/help/services">&gt;&gt;&nbsp;更多</a>
                <div class="clear"></div>
            </div>
            <div class="right" style="width:49%;">
                <div class="heading3" style="color:#666;font-size:14px;">网站更新</div>
                <div class="clear spacer20"></div>

                <div id="newsbox" style="height:125px;">
                    <div class="newsbox-item" style="padding-top:0;">bShare推出<a class="bLinkU" href="/products/bshareMobileSDK">iOS和Android开发SDK</a>，让你的应用插上分享的翅膀。</div>
                    <div class="newsbox-item">更多更详细的站长数据统计和分析，快去<a class="bLinkU" href="/userDashboard">数据中心</a>看看吧。</div>

                    <!-- <div class="newsbox-item" style="padding-bottom:0;border-bottom:0;">bshare.updates5</div> -->
                </div>
                
                <div class="clear spacer5"></div>
                <a class="right bLinkOrange" target="_blank" style="margin-right:12px;" href="http://www.bshare.cn/blog">&gt;&gt;&nbsp;更多</a>
                <div class="clear"></div>
            </div>
        
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
</div>

<div class="clear"></div>
<div class="text-darkgrey hidden" id="BSHARE_HTML">
    <div class="heading2"><a class="link-16-dark heading2" target="_blank" href="http://www.bShare.cn/">bShare.cn</a></div>
    <br/><img alt="bShare.cn" width="870px" src="http://static.bshare.cn/images/bshare-midbg.gif"/><br/>
    <p id="BSHARE_TEXT"><br/>我正在使用 <a target="_blank" class="bLinkU" href="http://www.bShare.cn">bShare</a>.<br/>bShare是个便捷的网络分享服务，让用户能够更轻易地将网络上看到的精彩内容分享到许多国内的社会化网络上，如开心网，人人网及新浪微博！<br/><br/>如果你也跟我一样喜欢分享网络文章、新闻，推荐你也快来加入bShare吧。<br/><br/>如果你是网站的站长，在站上装上bShare，还能让您的读者用户将你创造的精彩内容散播在整个网络上，而且还能追踪数据。<br/>加载轻松简单，而且完全免费！<br/><br/>有兴趣，就到官网了解更多<a target="_blank" class="bLinkU" href="http://www.bShare.cn/register">http://www.bShare.cn/register</a><br/><br/></p>
</div>
</div>

<script type="text/javascript" charset="utf-8">
	
	
	bShare.addEntry({
        title:"bShare分享快乐",
        summary:"中国最强大的社会化分享分享工具，邀请您来体验！",
	    vUid:'' , vEmail:'', vTag:'BSHARE'});
	
	
    var isSubmitting = false;
    function getButtonSubmit() {
    	if ($('input[name=buttonStyle]:checked').val().indexOf("bsync") == 0) {
    		$("#getButtonAction").attr("action", "help/installBsync");
    	}
        if (isSubmitting) return false;
        isSubmitting = true;
        
        displayStatusMessage('请稍后...', "info");
        $("#getBshareButton").html('请稍后...');
        
        return true;
    }
    
    var maxNewsCount = 4;
    var totalNewsCount = 0;
    function loadBuzzNews() {
        // init blog entries:
        $.post('php/rssFetcher.php?url=http://www.bShare.cn/blog/feed/', 
            {}, 
            function(results) {
                if (results.title) {
                    for (var i = 0; i < results.items.length && totalNewsCount < maxNewsCount; i++) {
                        var displayTitle = results.items[i].title;
                        if (displayTitle.length > 25) {
                            displayTitle = displayTitle.substring(0, 24) + "...";
                        }
                        var itemHtml = "bShare博客: <a class=\"bLinkU\" title=\"" + results.items[i].title + "\"target=\"_blank\" href=\"" + results.items[i].link 
                            + "\">" + displayTitle + "</a>";
                        var ni = $("<div class='newsbox-item'></div>").html(itemHtml).appendTo($("#newsbox"));
                        if (totalNewsCount == 0) {
                            ni.css("padding-top", "0");
                        } else if (totalNewsCount == (maxNewsCount-1)) {
                            ni.css("padding-bottom", "0").css("border-bottom", "0");
                        }
                        totalNewsCount++;
                    }
                }
            },
            "json"
        );
    }

    $(function() {      
        // sites using bShare
        var hscr2 = $("#sites-scroller").scrollable({ circular: true, speed: 600, easing: "linear"})
            .navigator()
            .autoscroll({ interval: 4000, autoplay: true});

        $("ul.tabs").tabs("div.panes > div.panediv", {});
        $('input[name=buttonStyle]').click(function() {
        	var moreStylesLink = $("#getBshareButton").parent().find("a.bLink");
        	if ($(this).val().indexOf("bsync") == 0) {
        		moreStylesLink.hide();
        	} else {
        		moreStylesLink.show();
        	}
        });
        $('input[name=buttonStyle]').each(function() {
        	if ($(this).attr("id").indexOf("buttonStylebsync") < 0) {
        	    $(this).trigger("click");
                return false;
        	}
        });
        
        if ($.browser.msie && $.browser.version=="6.0") {
            // need to remove status messages div from ie6, or it will hide the top text.
            $("#status-messages-pdiv").remove();
        }
        
        // newsbox
        totalNewsCount = $("#newsbox > div").size();
        if (totalNewsCount < maxNewsCount) {
            loadBuzzNews();
        }
    });
</script>

	

	<div class="clear"></div>
	
		<div style="background:#e5e5e5;min-width:980px;">
			

























<style>
    .bottomnav .bLink { color: #333; }
    
    .follow-sinaminiblog, .follow-qqmb, .follow-sohuminiblog, .follow-neteasemb { background: url(http://static.bshare.cn/frame/images/logos/m2/sinaminiblog-off.gif); width:24px; height:24px; margin-right:5px; }
    .follow-qqmb { background: url(http://static.bshare.cn/frame/images/logos/m2/qqmb-off.gif); }
    .follow-sohuminiblog { background: url(http://static.bshare.cn/frame/images/logos/m2/sohuminiblog-off.gif); }
    .follow-neteasemb { background: url(http://static.bshare.cn/frame/images/logos/m2/neteasemb-off.gif); }
    .follow-sinaminiblog:hover { background: url(http://static.bshare.cn/frame/images/logos/m2/sinaminiblog.gif); }
    .follow-qqmb:hover { background: url(http://static.bshare.cn/frame/images/logos/m2/qqmb.gif); }
    .follow-sohuminiblog:hover { background: url(http://static.bshare.cn/frame/images/logos/m2/sohuminiblog.gif); }
    .follow-neteasemb:hover { background: url(http://static.bshare.cn/frame/images/logos/m2/neteasemb.gif); }
</style>

<div class="container-center">
	<div class="bottomnav">
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">什么是bShare?</div>
			<div><a title="bShare介绍" class="bLink" href="/intro">bShare介绍</a></div>
			<div><a title="bShare特色" class="bLink" href="/intro/reasons">bShare特色</a></div>
			<div><a title="支持的平台" class="bLink" href="/intro/services">支持的平台</a></div>
			<!-- <div><a title="谁在用bShare?" class="bLink" href="/intro/bshareSites">谁在用bShare?</a></div> -->
			<div class="clear"></div>
		</div>
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">为何用bShare?</div>
			<div><a title="数据统计" class="bLink" href="/analytics">数据统计</a></div>
			<div><a title="自定义按钮" class="bLink" href="/introCustomize">自定义按钮</a></div>
			<div><a title="bShare一键通" class="bLink" href="/products/bsync">bShare一键通</a></div>
			<div><a title="bURL" class="bLink" href="/products/burl">bURL</a></div>
			<!--
			<div><a title="社交网络优化 (SMO)" class="bLink" href="/smo">社交网络优化 (SMO)</a></div>
			<div><a title="API概述" class="bLink" href="/apiOverview">API概述</a></div>
			-->
			<div class="clear"></div>
		</div>
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">获取bShare!</div>
			<div><a title="一般网站" class="bLink" href="/help/install">一般网站</a></div>
			<div><a title="更多样式" class="bLink" href="/moreStyles">更多样式</a></div>
			<div><a title="bShare图片分享" class="bLink" href="/products/imageShare">bShare图片分享</a></div>
			<div><a title="bShare高级" class="bLink" href="/products/premiumServices">bShare高级</a></div>
			<!-- 
			<div><a title="bShare企业版" class="bLink" href="/bshareEnterprise">bShare企业版</a></div>
			<div><a title="Blogbus用户" class="bLink" href="/blogbusRegister">Blogbus用户</a></div>
			<div><a title="Discuz!用户" class="bLink" href="/discuzx1Register">Discuz!用户</a></div>
			<div><a title="Wordpress用户" class="bLink" href="/wordpressRegister">Wordpress用户</a></div>
			-->
			<div class="clear"></div>
		</div>
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">浏览器插件</div>
			<div><a title="bShare书签" class="bLink" href="/products/bookmark">bShare书签</a></div>
			<div><a title="浏览器插件" class="bLink" href="/browserPlugins">浏览器插件</a></div>
			
			<!--
			<div><a title="IE插件" class="bLink" href="/browserPlugins">IE插件</a></div>
			<div><a title="Firefox插件" class="bLink" href="/browserPlugins">Firefox插件</a></div>
			<div><a title="Chrome插件" class="bLink" href="/browserPlugins">Chrome插件</a></div>
			<div><a title="Other插件" class="bLink" href="/browserPlugins">Other插件</a></div>
			-->

			<div class="heading4" style="padding:13px 0 10px;">开放平台</div>
			<div><a title="展览厅" class="bLink" href="/gallery">展览厅</a></div>
			<div class="hidden"><a title="实验室" class="bLink" href="/labs">实验室</a></div>

			<div class="clear"></div>
		</div>
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">关于我们</div>
			<div><a title="关于bShare" class="bLink" href="/about">关于bShare</a></div>
			<!-- 
			<div><a title="关于buzzinate" class="bLink" target="_blank" href="http://www.buzzinate.com/aboutus_company.php">关于buzzinate</a><img style="margin:0 0 3px 3px;" src="http://static.bshare.cn/images/icon_new_window.gif"/></div>
			-->
			<div class="hidden"><a title="江湖" class="bLink" href="http://jianghu.bshare.cn">江湖</a><img style="margin:0 0 3px 3px;" src="http://static.bshare.cn/images/icon_new_window.gif"/></div>
			<div><a title="联系我们" class="bLink" href="/contact">联系我们</a></div>
			<div><a title="招贤纳士" class="bLink" href="/career">招贤纳士</a></div>
			<div><a title="bShare博客" class="bLink" target="_blank" href="http://www.bshare.cn/blog">bShare博客</a><img style="margin:0 0 3px 3px;" src="http://static.bshare.cn/images/icon_new_window.gif"/></div>
			
			<div class="heading4" style="padding:13px 0 10px;">关注bShare</div>
            <div>
                <a class="follow-sinaminiblog inlineBlock" href="http://weibo.com/bshare" target="_blank"></a>
                <a class="follow-qqmb inlineBlock" href="http://t.qq.com/bShare" target="_blank"></a>
                <a class="follow-sohuminiblog inlineBlock" href="http://t.sohu.com/u/8642674" target="_blank"></a>
                <a class="follow-neteasemb inlineBlock" href="http://t.163.com/4104388906" target="_blank" style="margin-right:0;"></a>
            </div>
			
			<div class="clear"></div>
		</div>
		<div class="bottomnav-inner">
			<div class="heading4" style="padding-bottom:10px;">支持</div>
			<div><a title="FAQ" class="bLink" href="/help">帮助</a></div>
			<div><a title="问题反馈" class="bLink" href="/feedback">问题反馈</a></div>
			<div><a title="使用条款" class="bLink" href="/terms">使用条款</a></div>
			<div><a title="隐私条款" class="bLink" href="/privacy">隐私条款</a></div>
			<div><a title="网站地图" class="bLink" href="http://www.bshare.cn/sitemap.html" target="_blank">网站地图</a></div>
                  <div id="langLinkDiv" class="hidden"><a id="languageLink" class="bLink" href="javascript:;">Language</a><img src="http://static.bshare.cn/images/lang-icon.png" style="margin:0 0 -2px 3px;"/></div>
			<div class="clear"></div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
</div>

			
			<div style="background:#e5e5e5;">
				<div class="clear spacer10"></div>
				



























<div class="container-center" style="font-size:10px;text-align:center;width:auto;">
	
		<span><a target="_blank" title="沪ICP备09026724号" style="text-decoration:none;color:#666;" href="http://www.miitbeian.gov.cn/">沪ICP备09026724号</a></span>
		<div class="clear spacer2"></div>
	
	<span style="color:#666;">&copy; 2009-2014 <a title="擘纳" href="http://www.buzzinate.com" target="_blank" class="bLinkOrangeU">擘纳</a>（上海）信息科技有限公司。版权所有。</span>
    <div class="clear spacer15"></div>
    <div style="text-align: center;">
        <a style="margin-right:10px;" target="_blank" href="http://www.sgs.gov.cn/lz/licenseLink.do?method=licenceView&entyId=20120703161312773">
        <img src="http://static.bshare.cn/images/sgs_license1.gif" border=0 /></a>
        <a id='___szfw_logo___' href='https://search.szfw.org/cert/l/CX20130327002375002858' target='_blank'><img src='http://static.bshare.cn/images/cx_license.gif'></a>
<script type='text/javascript'>(function(){document.getElementById('___szfw_logo___').oncontextmenu = function(){return false;}})();</script>
    </div>
    <div class="clear"></div>
</div>
<div class="right" style="margin:-11px 15px 0 0;"><a target="_blank" title="Buzzinate" href="http://www.buzzinate.com/"><img alt="a buzzinate company" title="Buzzinate" src="http://static.bshare.cn/images/buzzinate-company-grey.png"/></a></div>
<div class="clear"></div>
				<div class="clear spacer5"></div>
			</div>
			<div class="clear"></div>
		</div>
	
	<div class="div-gradient-dark-top" style="height:50px;min-width:980px;"></div>
	
	<!-- FLOATING QQ SERVICE START -->
	<a id="qqservicetab" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=800087176&site=qq&menu=yes"></a>
    <!-- <img src="http://static.bshare.cn/images/tab-service-zh.gif"/> -->
	<!-- FLOATING QQ SERVICE START -->
	
	



























<!-- LANGUAGE POPUP -->
<div id="languagemenupopup" class="language-menu-popup">
	<span></span>
	<ul>
	    <li class="popupliheader">Choose your language</li>
	    <li style="padding-top:10px;"><img style="margin-bottom:-1px;" alt="en" src="http://static.bshare.cn/images/lang-en.gif"/>&nbsp;<a title="English" class="bLinkU" href="/index.jsp?request_locale=en">English</a></li>
	    <li><img style="margin-bottom:-1px;" alt="zh" src="http://static.bshare.cn/images/lang-zh.gif"/>&nbsp;<a title="简体中文" class="bLinkU" href="/index.jsp?request_locale=zh">简体中文</a></li>
	    <li><img style="margin-bottom:-1px;" alt="zh_TW" src="http://static.bshare.cn/images/lang-zh_TW.gif"/>&nbsp;<a title="繁體中文" class="bLinkU" href="/index.jsp?request_locale=zh_TW">繁體中文</a></li>
	</ul>
</div>
<!-- LANGUAGE POPUP END -->

<script type="text/javascript" charset="utf-8">
	$(function() {
		$("#languageLink").tooltip({
			tip: "#languagemenupopup",
			position: "top right",
			delay: 1000,
			effect: "fade"
		});
		
		
			var showLang1 = false;
			var showLang2 = false;
			$(document).keypress(function(e) {
				if (e.keyCode == 108) {
					showLang1 = true;
					showLang2 = false;
					return;
			    }
				if (showLang1 && e.keyCode == 110) {
					if (showLang2) {
						showLang1 = false;
						showLang2 = false;
						return false;
					}
					showLang2 = true;
					return;
			    }
				if (showLang1 && showLang2 && e.keyCode == 103) {
					$("#langLinkDiv").show();
			    }
				showLang1 = false;
				showLang2 = false;
			});
		
	});
</script>
	
	<!-- Global floating notification system -->
	<!-- Global floating notification start -->
<div id="notifications" class="hidden">
	<div class="notification-bar-container">
		<div class="notification-bar-bkg"></div>
		<div class="notification-bar">
			<div class="notification-bar-contents">
				<div class="notification-msg heading1"></div>
			</div>
		</div>
	</div>
</div>
<!-- Global floating notification END -->
	<!-- Global floating notification system -->
	
	<!-- SCRIPTS -->
	<script type="text/javascript" charset="utf-8">
	    $(function() {
		    // temp fix for chrome HTML5 validation
	    	$('form').attr('novalidate', 'novalidate');
		    
	    	// for leftMenus
	    	var s = "index.jsp".replace("/", "-");
			var rel = $(".leftMenu-" + s).addClass("active").parent().parent().attr("id");
			var toCl = $("div[rel=" + rel + "]").addClass("active");
			if (typeof leftMenuHelp !== "undefined") toCl.click();
			
			$(".leftMenuTitle-index.jsp").addClass("bLeftMenuTitleActive");
	    });
	</script>

	<script type="text/javascript" charset="utf-8">
		var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
		document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript" charset="utf-8">
		try {
			var pageTracker = _gat._getTracker("UA-11228114-1");
			pageTracker._setDomainName("www.bshare.cn");
			pageTracker._trackPageview();
		} catch(err) {}
	</script>
	<!-- SCRIPTS-END -->
</body>
</html>
