<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/template/biemian/css/head.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/cddh.css" rel="stylesheet" type="text/css" />
<script src="/template/biemian/js/common.js" language="javascript" type="text/javascript"></script>
  <div id="head">    
    	<div class="banner"></div>
  		<div id="top">
          <ul>
          <li><a href="/">首页</a></li>
          <#if channelsDisplay??>
          <#list channelsDisplay as channel>
          	<li><a href="/${channel.id}.html">${channel.name}</a></li>
          </#list>
          </#if>
          </ul>    
        </div>
</div><!--head end-->	

