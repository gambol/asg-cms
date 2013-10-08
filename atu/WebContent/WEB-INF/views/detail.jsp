<%-- 
    Document   : detail
    Created on : 2012-9-8, 10:04:47
    Author     : zhenbao.zhou
--%>

<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>< c:out value="${serverInfo.name}"/> , 新开<c:out value="${categoryName}"/>私服, 传奇私服发布网  </title>
        <meta name="description" content="新开<c:out value="${categoryName}"/>私服, ${serverInfo.name}, <c:out value="${fn:substring(serverInfo.description, 0, 200)}" escapeXml="true"/>" />
        <meta name="keywords" content="传奇私服 传奇私服发布网 传奇世界私服 热血传奇私服 新开传奇网站 新开传奇私服 传奇外挂 传奇外传私服 传奇私服外挂 传奇私服网 网通传奇私服 传奇私服网站 传奇发布网" />
        <!-- Favicons --> 
        <link rel="shortcut icon" type="image/png" HREF="/img/favicons/small-logo.png"/>
        <link rel="icon" type="image/png" HREF="/img/favicons/small-logo.png"/>

        <!-- Main Stylesheet --> 
        <link rel="stylesheet" href="/styles/css/style.css" type="text/css" />

        <!--swfobject - needed only if you require <video> tag support for older browsers -->
        <!--  <script type="text/javascript" SRC="js/swfobject.js"></script> -->
        <!-- jQuery with plugins -->
        <script type="text/javascript" SRC="/js/jquery-1.7.2.min.js"></script>
              <script>
            $(document).ready(function(){
                $(".like-it-button").click(function(){
                    var ajaxUrl = $(this).attr("aurl");
                    var btn = this;
                    $.get(ajaxUrl, {
                        Action:"get"
                    }, function (data){
                        
                        $(btn).after(data);
                        if (data == "+1") {
                            var num = $("#vote_in_num").html();
                            var c = Number(num) + 1;
                            $('#vote_in_num').html("<span style='background-color:#ffec00 !important'>" + c + "</span>");
                        }
                        });
                    });
    
                });
        </script>
    </head>
    <body>
        <%@include file="../common/header.jsp" %>
        <script>
            selectMenu("menu-mainPage");
        </script>
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <!-- Left column/section -->
                <section class="column width1 first">
                    <div id="left-nav">
                        <ul>
                            <c:forEach var="category" items="${categorys.pageList }">
                                <li><a href="/index_${category.id}.htm" <c:if test="${serverInfo.categoryId == category.id}"> class="current"</c:if>>${category.name}</a></li>
                            </c:forEach>

                        </ul>
                    </div>  
                </section>
                <section class="width5">					
                    <div class="pageline">
                        <form action="" method="get">
                            <input type="hidden" name="categoryId" value="${serverInfo.categoryId}"/>
                            <input class="search-button" type="submit" id="button" value=""  name="search"/>
                            <input class="search-text" type="text" id="keyword" name="keyword" placeholder="按标题或者url搜索"/>
                        </form>
                    </div>
                    <div>
                        <div class="colgroup">
                        	<div class="column first" style="padding-left:15px">
                             <h3>                             
        
                             	<c:out value="${serverInfo.name}" />                      	
                             	<a class="small-btn btn-yellow like-it-button" title="每分钟只能赞一次哦" aurl="/vote.htm?id=${serverInfo.id }">赞</a>
                             </h3>

                            </div>
                            
                            <div style="padding:10px" class="colgroup">
                                <div class="width3 first"  style="float:left">
	                           	 <table class="no-style full">
									<tbody>
										<tr>
											<td>发布时间</td>
											<td><fmt:formatDate value="${serverInfo.publishTime}" pattern="yyyy年MM月dd日"/>发布<br/></td>								
										</tr>
										<tr>
											<td>网站线路</td>
											<td><c:out value="${serverInfo.line}"/> </td>
										</tr>
										<tr>
											<td>赞美数</td>
											<td> <div id="vote_in_num">${serverSysInfo.voteIn} </div> 
											</td>
										</tr>
									</tbody>
								</table>
                               
                                </div>
                                <div class="width2" style="float:right; text-align: center">
                                    <a href="#" class="big-btn btn-blue" target="_blank" onclick="return openSite('${serverInfo.url}')">点击进入</a> <br/>

                                </div>
                               
                                <div class="first column full"/>
                                <h3>私服描述</h3>
                                <hr/>
                                <p><c:out value="${serverInfo.description}" escapeXml="false"/></p>
								<p></p>
								
								<c:if test="${not empty serverInfo.bannerUrl and  serverInfo.bannerUrl != 'http://'}">
								 	<h3>私服截图:</h3>
								  	<p><img src='<c:out value="${serverInfo.bannerUrl}"/>'/></p>
								  	<p></p>
								</c:if>
								
                               
								 
                                <h3> 私服介绍</h3>
                                 <hr/>
                                严格意义上的所谓sf是指一个没有得到网络游戏的制作商法定许可而私自存在幵运营的朋务器
                                它在技术和朋务实力上都和正式的官方朋务器存在可比性.因为这些技术和朋务的存在就是合法的。sf存在的主要目的同官方朋务器是一样的,
                                都是向玩家收费以获利。有些sf幵对外开放也以收费盈利为目的. 如部分网吧在吧内局域网架设sf.
                                这类sf规模较小,一般仅限于网吧内部。其目的也仅以娱乐为主.对玩家免费
                            </div>



                        </div>
                    </div>
                    <div class="clearfix"></div>



                </section>
                <!-- End of Left column/section -->

                <!-- Right column/section -->
                <aside class="colgroup width2"  id="asider">
                      <div class="content-box box">                  
                        <section class="notes-total">                            
                            <a class="big-btn btn-yellow" href="/user/publish.htm">免费发布新站</a>
                        </section>
  					</div>
  					
  					<hr>
 					<div class="content-box box">      
 						<header style="cursor: s-resize;">
							<h3>最新热门推荐</h3>
						</header>      
						
						<section class="newserver">
							<dl>
								  <c:forEach var="newManServer" items="${newManServers.pageList }">
								 	 <dt> <fmt:formatDate pattern="[MM-dd]" value="${newManServer.createDate}" type="both"/><a target="_blank" href="/detail_${newManServer.id}.htm">
								   		<c:out value="${fn:substring(newManServer.name, 0, 10)}" escapeXml="true"/> </a></dt>
								   <dd>
								   	</dd>
                           		 </c:forEach>
							</dl>
						</section>
					</div>
					
  					
                    <hr>
                    
                    	<div class="content-box box">      
 						<header style="cursor: s-resize;">
							<h3>最新发布</h3>
						</header>      
						
						<section class="newserver">
							<dl>
								  <c:forEach var="newServer" items="${newServers.pageList }">
								 	 <dt> <fmt:formatDate pattern="[MM-dd]" value="${newServer.createDate}" type="both"/><a target="_blank" href="/detail_${newServer.id}.htm">
								   		<c:out value="${fn:substring(newServer.name, 0, 10)}" escapeXml="true"/> </a></dt>
								   <dd>
								   	</dd>
                           		 </c:forEach>
							</dl>
						</section>
					</div>
					
					
                </aside>
                <!-- End of Right column/section -->

                <!-- End of Wrapper -->
            </div>
            <!-- End of Page content -->
            <a id="totop" style="display: block;">^ scroll to top</a>
            <%@include file="../common/footer.jsp" %>

    </body>
    <script>
    function openSite(o){
		win = window.open(o,"_blank");
		win.opener = null;
		return false;
	}

    </script>
</html>
