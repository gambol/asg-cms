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

<link rel="stylesheet" href="/template/biemian/css/style.css" type="text/css" />
<script type="text/javascript" SRC="/template/biemian/j/jquery-1.4.2.min.js"></script>
<!-- Could be loaded remotely from Google CDN : <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> -->
<script type="text/javascript" SRC="/template/biemian/j/jquery.ui.core.min.js"></script>
<script type="text/javascript" SRC="/template/biemian/j/jquery.ui.widget.min.js"></script>
<script type="text/javascript" SRC="/template/biemian/j/jquery.ui.tabs.min.js"></script>
<!-- jQuery tooltips -->
<script type="text/javascript" SRC="/template/biemian/j/jquery.tipTip.min.js"></script>
<!-- Superfish navigation -->
<script type="text/javascript" SRC="/template/biemian/j/jquery.superfish.min.js"></script>
<script type="text/javascript" SRC="/template/biemian/j/jquery.supersubs.min.js"></script>
<!-- jQuery form validation -->
<script type="text/javascript" SRC="/template/biemian/j/jquery.validate_pack.js"></script>
<!-- jQuery popup box -->
<script type="text/javascript" SRC="/template/biemian/j/jquery.nyroModal.pack.js"></script>
<!-- jQuery graph plugins -->
<!--[if IE]><script type="text/javascript" src="/template/biemian/j/flot/excanvas.min.js"></script><![endif]-->
<script type="text/javascript" SRC="/template/biemian/j/flot/jquery.flot.min.js"></script>


</head>
       
<body>
<!-- Header -->
	<header id="top">
		<div class="wrapper">
			<div id="title"><img SRC="img/logo.png" alt="Administry" /><!--<span>Administry</span> demo--></div>
			<!-- Top navigation -->
			<div id="topnav">
			</div>
			<#include "/head.html" parse=false encoding="UTF-8">
	</header>
	<!-- End of Header -->
	<!-- Page title -->
	<div id="pagetitle">
		<div class="wrapper">
			<h1>first page</h1>
			<!-- Quick search box -->
			<form action="" method="get"><input class="" type="text" id="q" name="q" /></form>
		</div>
	</div>
	<!-- End of Page title -->
	
	<!-- Page content -->
	<div id="page">
		<!-- Wrapper -->
		<div class="wrapper">
				<!-- Left column/section -->
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
							           		<a href="Article/${article.id?c}.html" title='${article.title!""}' target="_blank">${article.title!""}</a>
							           	</td>
							           	<td class="ta-right">[${article.releaseDate!""}]</td>
							           </tr> 
						           </#list> 
						           </#if>
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
	

  <#include "/foot.html" parse=false encoding="UTF-8">
</body>
</html>