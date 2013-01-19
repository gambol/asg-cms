<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${(C.fullName)!"biemian"}</title>
<meta name="description" content='${C.description!""}'/>
<meta name="keywords" content='${C.keywords!""}'/>
<link href="/template/biemian/css/base.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/common.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/index.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/tagChange.css" rel="stylesheet" type="text/css" />
<script src="/template/biemian/js/common.js" language="javascript" type="text/javascript"></script>
<script src="/template/biemian/js/flashImg.js" language="javascript" type="text/javascript"></script>
<script src="/template/biemian/js/google.js" language="javascript" type="text/javascript"></script>
</head>
       
<body>
<div id="container">
	<#include "/head.html" parse=false encoding="UTF-8">
<div id="main">
<div id="main-center">
         <div id="imageNews">
             <div class="flash">
            <script type="text/javascript">
           		CreateFlash("finance_photo", "/template/biemian/flash/imageNews.swf", 298, 250);
                var xml="";
                <#if flashs??>
                <#list flashs as flash>
                	xml=xml+"<p u='${flash.imageURL}' a='${flash.linkURL}' n='${flash.title}'/>";
                </#list>
                </#if>
                function getXml() {
                	return "<?xml version='1.0' encoding='utf-8'?><root>"+xml+"</root>";
                	}
            </script>
            </div>	  
		</div> 
        <div id="systemInfo">
           <div id="newNews">最新动态</div>
           <#if latestArticles?? >
		   <#list latestArticles as a>
	           <li>［${a.channel.name!""}］<a href="Article/${a.id?c}.html" 
	           title="${a.title}" target="_blank">${a.title!""}</a>
	           <span>[${a.releaseDate!""}]</span></li>  
           </#list> 
           </#if>
           
 		</div> 
        <!--informpart begin -->
        <div id="tagChange">
        	<#include "tagChange.ftl" encoding="UTF-8">
        </div> <!--informpart end -->
        <div id="systemAd">
        	<#include "picad.ftl" encoding="UTF-8">
        </div>
        
       <div id="right-piece1">
       <div class="rigth_title">系统简介</div>
       		<div id="right-piece1-content">
      			 ${C.introduce!""}
			</div>
       </div> 
</div><!--main-center end-->
<div id="main-left">


<#if channelArticleMaps??>
<#list channelArticleMaps as channelArticleMap>
  <div class="news">
     <#list channelArticleMap?keys as channelName > 
     <div class="newsTitle">&nbsp;&nbsp;${channelName}</div>
     <div>
        <ul>
           <#assign articles=channelArticleMap[channelName] > 
           <#if articles??>
       	   <#list articles as article>
	           <li><a href="Article/${article.id?c}.html" 
	           title='${article.title!""}' target="_blank">${article.title!""}</a>
	           <span>[${article.releaseDate!""}]</span></li> 
           </#list> 
           </#if>
    	</ul>
    </div>
    </#list>
 </div>
</#list> 
</#if>






</div><!--main_left end-->


<div id="main-right">

<div id="right-piece2" >
	<div class="rigth_title">服务与支持</div>
	<div id ="right-piece2-content">
		<img src="/template/biemian/images/qq.gif" align="middle" alt="QQ" />&nbsp;<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=${C.QQ}&amp;site=qq&amp;menu=yes"><img title="点击与我会话" border="0" alt="点击与我会话" src="/template/biemian/images/qq_online.gif" /></a>&nbsp;
		<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=304748568&amp;site=qq&amp;menu=yes"><img title="点击与我会话" border="0" alt="点击与我会话" src="http://wpa.qq.com/pa?p=2:304748568:41" /></a>
		<br/><br/>
		<img src="/template/biemian/images/tel.gif" align="middle" alt="电话"/><b>${C.mobile!""}</b><br/>
	</div>
</div>


<div id="imglink">
<!--友情链接开始-------------------------->
<div id="Google_fun">
  <div id="tb" align="center" style="z-index:-1;"></div>
</div>
<!--友情链接结束-------------------------->
</div>

</div><!--main1 end-->

<div id="copyright">
    <#include "/foot.html" parse=false encoding="UTF-8">
</div>
</div><!--main end -->



</div><!--container end-->

</body>
</html>