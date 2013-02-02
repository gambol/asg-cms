<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${(C.fullName)!"biemian"}</title>
<meta name="description" content='${C.description!""}'/>
<meta name="keywords" content='${C.keywords!""}'/>
<!--
<link href="/template/biemian/css/base.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/common.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/index.css" rel="stylesheet" type="text/css" />
<link href="/template/biemian/css/tagChange.css" rel="stylesheet" type="text/css" />
<script src="/template/biemian/js/common.js" language="javascript" type="text/javascript"></script>
<script src="/template/biemian/js/flashImg.js" language="javascript" type="text/javascript"></script>
<script src="/template/biemian/js/google.js" language="javascript" type="text/javascript"></script>
-->

<link rel="stylesheet" href="/template/biemian/css/style.css" type="text/css" />

</head>
       
<body>
<!-- Header -->
	<header id="top">
		<div class="wrapper">
			<div id="title"><img SRC="/template/biemian/images/logo.png" alt="biemian" /></div>
			<div id="topnav">
			</div>
			<#include "/head.html"  encoding="UTF-8">
	</header>

	<div id="pagetitle">
		<div class="wrapper">
			<h1>首页</h1>
			<form action="" method="get"><input class="" type="text" id="q" name="q" /></form>
		</div>
	</div>

	<div id="page">
		<div class="wrapper">
				<section class="column width6 first">
					<#if channelArticleMaps??>
						<#list channelArticleMaps as channelArticleMap>
							<#if channelArticleMap_index % 2 == 0 >
								<div class="colgroup leading">
								<div class="column width3 first">
							<#else>
								<div class="column width3">
							</#if>
						  
						     <#list channelArticleMap?keys as channelName > 
						     <h4>&nbsp;&nbsp;${channelName}</h4>
						     <table class="no-style full">
						     	<tbody>
						     	 <#assign articles=channelArticleMap[channelName] > 
						           <#if articles??>
						       	   <#list articles as article>
							           <tr>
							           	<td> 
							           		<a href="article?id=${article.id?c}" title='${article.title!""}' target="_blank"><#if article.title?length gt 20>${article.title[0..20]}<#else>${article.title!""}</#if></a>
							           	</td>
							           	<td class="ta-right">[${article.sysDate?string("yyyy-MM-dd")}]</td>
							           </tr> 
						           </#list> 
				`		           </#if>
						     	</tbody>
						     </table>
						 	</#list>
						 	</div>
							
							<#if channelArticleMap_index % 2 == 1 >
								</div>
							</#if>
						  
						</#list>
					</#if>
					
				
					<div class="clear">&nbsp;</div>
				
				</section>
				<!-- End of Left column/section -->
				
				<!-- Right column/section -->
				<aside class="column width2">
					<#include "tagChange.ftl" encoding="UTF-8">
				</aside>
				<!-- End of Right column/section -->
				
		</div>
		<!-- End of Wrapper -->
	</div>
	<!-- End of Page content -->
	

  <#include "/foot.html"  encoding="UTF-8">
</body>
</html>