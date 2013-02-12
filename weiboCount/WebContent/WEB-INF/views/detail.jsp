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
        <title>< c:out value="${serverInfo.name}"/> , 新开<c:out value="${categoryName}"/>私服, 阿土游戏,传奇私服,新开传奇私服,传奇私服发布网,魔兽私服,新开各种私服</title>
        <meta name="description" content="新开<c:out value="${categoryName}"/>私服, ${serverInfo.name}, <c:out value="${fn:substring(serverInfo.description, 0, 200)}" escapeXml="true"/>" />
        <meta name="keywords" content="Atugame, 阿土游戏, 私服, 私服排名, 传奇私服, 魔兽私服, 热血传奇, 游戏排名, 私服发布" />
        <!-- Favicons --> 
        <link rel="shortcut icon" type="image/png" HREF="/img/favicons/small-logo.png"/>
        <link rel="icon" type="image/png" HREF="/img/favicons/small-logo.png"/>

        <!-- Main Stylesheet --> 
        <link rel="stylesheet" href="/styles/css/style.css" type="text/css" />

        <!--swfobject - needed only if you require <video> tag support for older browsers -->
        <!--  <script type="text/javascript" SRC="js/swfobject.js"></script> -->
        <!-- jQuery with plugins -->
        <script type="text/javascript" SRC="/js/jquery-1.7.2.min.js"></script>


        <script type="text/javascript">

            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-33734422-1']);
            _gaq.push(['_setDomainName', 'atugame.com']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();

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
                            <input class="search-text" type="text" id="keyword" name="keyword" placeholder="按标题搜索"/>
                        </form>
                    </div>
                    <div>
                        <div class="colgroup">
                            <div class="box box-info">${categoryName}私服 >> < c:out value="${serverInfo.name}"/> </div>
                            <div style="padding:5px" class="colgroup">
                                <div class="width3 first"  style="float:left">
                                <p>
                                    <fmt:formatDate value="${serverInfo.publishTime}" pattern="yyyy年MM月dd日"/>发布<br/>
                                    网站线路:<c:out value="${serverInfo.line}"/>  <br/>
                                    赞:${serverSysInfo.voteIn}</p><br/>
                                </p>
                                </div>
                                <div class="width2" style="float:left; text-align: center">
                                    <a href="${serverInfo.url}" class="big-btn btn-blue">点击进入</a>
                                </div>
                                   <div class="first"/>
                                <div class="box box-info">私服描述:</div>
                                <p><c:out value="${serverInfo.description}" escapeXml="true"/></p>

                                <p class="box box-info"> 私服介绍</p>
                                严格意义上的所谓sf是指一个没有得到网络游戏的制作商法定许可而私自存在幵运营的朋务器
                                它在技术和朋务实力上都和正式的官方朋务器存在可比性.因为这些技术和朋务的存在就是合法的。sf存在的主要目的同官方朋务器是一样的,
                                都是向玩家收费以获利。有些sf幵对外开放也以收费盈利为目的. 如部分网吧在吧内局域网架设sf.
                                这类sf规模较小,一般仅限于网吧内部。其目的也仅以娱乐为主.对玩家收费
                            </div>



                        </div>
                    </div>
                    <div class="clearfix"></div>



                </section>
                <!-- End of Left column/section -->

                <!-- Right column/section -->
                <aside class="colgroup width2"  id="asider">
                    <div class="clean-content-box">                  
                        <section class="notes-total">
                            <img src="/img/atu.png" width="166px" height="42px" alt="阿土游戏"/>
                            <a class="big-btn btn-yellow" href="/user/publish.htm">免费发布新站</a>
                        </section>
                    </div>
                    <hr>
                </aside>
                <!-- End of Right column/section -->

                <!-- End of Wrapper -->
            </div>
            <!-- End of Page content -->
            <a id="totop" style="display: block;">^ scroll to top</a>
            <%@include file="../common/footer.jsp" %>

    </body>
</html>
