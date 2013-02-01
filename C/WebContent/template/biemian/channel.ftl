<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${entity.title!""}-${(C.fullName)!"biemian"}</title>
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

<script language="javascript">
function pageQuery(page) {
	 location.href = "/channel?id=${entity.id}&title=${title!""}&page="+page;
	//location.href = "/Channel/${entity.id!}_"+page+".html";
}
</script>

</head>
       
<body>
<!-- Header -->
	<header id="top">
		<div class="wrapper">
			<div id="title"><img SRC="img/logo.png" alt="biemian" /></div>
			<!-- Top navigation -->
			<div id="topnav">
			</div>
			<#include "/head.html" parse=false encoding="UTF-8">
	</header>
	<!-- End of Header -->
	<!-- Page title -->
	<div id="pagetitle">
		<div class="wrapper">
			<h1> <a href="/">首页</a> -> <a href="channel?id=${entity.id?c}">${entity.name!""}</a>&nbsp; <#if title??&&title!=''>[关键字:<font class="red_normal">${title}</font>]</#if></h1>
			<!-- Quick search box -->
			<form action="" method="get"><input class="" type="text" id="q" name="q" /></form>
		</div>
	</div>
	<!-- End of Page title -->
	
	<!-- Page content -->
	<div id="page">
		<!-- Wrapper -->
		<div class="wrapper">
					<div class="column width6 first">
                    <ul>
                    <#list pageView.records as article>
                     <li>
                     <a href="article?id=${article.id?c}" title="${article.title}" target="_blank">${(article.title)!""}</a>
                     <span>[${(article.releaseDate)!""}]</span></li>
                     </#list>
                    </ul>
                    
                    	 <table  align="center" >
                    <tr>
                    <td align="center">页次&nbsp;<font class="red_normal">${pageView.currentpage}/${(pageView.totalpage)?c}</font>
                    &nbsp;&nbsp;每页&nbsp;<font class="red_normal">${pageView.maxresult}</font>&nbsp;条&nbsp;&nbsp;共&nbsp;
                    <font class="red_normal">${(pageView.totalrecord)?c}</font>&nbsp;条</td>
                    <td align="center">
                    <!--
                   <#if (pageView.currentpage>1)>
                   	<a  href='#' onClick='pageQuery(1)' >最前页</a>
                    <a  href='#' onClick="pageQuery(${pageView.currentpage-1})"  >上一页</a>
                   </#if>
                  <#if pageView.currentpage==1>
                   		 最前页&nbsp;上一页
                  </#if>
                  <#if pageView.currentpage<pageView.totalpage>
                   <a  href='#' onClick='pageQuery(${pageView.currentpage+1})' >下一页</a>
                   <a  href='#' onClick="pageQuery(${pageView.totalpage})"  >最后页</a>
                  </#if>
                  <#if pageView.currentpage==pageView.totalpage>
    			              下一页&nbsp;最后页
       			  </#if>
                    -->
                    
                   <#if (pageView.currentpage>1)>
                   	<a  href='/channel?id=${entity.id?c}&title=${title!}&page=1' target="_blank">最前页</a>
                    <a  href='/channel?id=${entity.id?c}&title=${title!}&page=${pageView.currentpage-1}'  target="_blank">上一页</a>
                   </#if>
                  <#if (pageView.currentpage==1)>
                   		 最前页&nbsp;上一页
                  </#if>
                  <#if (pageView.currentpage < pageView.totalpage)>
                  	 	<a  href='/channel?id=${entity.id?c}&title=${title!}&page=${pageView.currentpage+1}' target="_blank">下一页</a>
                   		<a  href='/channel?id=${entity.id?c}&title=${title!}&page=${pageView.totalpage?c}' target="_blank">最后页</a>
                  </#if>
                  <#if pageView.currentpage==pageView.totalpage>
    			           下一页&nbsp;最后页
       			  </#if>
                    
                    </td>
                    </tr>
                   </table>
                 </div>
           
                
				
       		
				<div class="clear">&nbsp;</div>

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
