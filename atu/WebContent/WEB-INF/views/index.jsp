<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
    <%@include file="../common/appInclude.jsp" %>
    <body>
        <%@include file="../common/header.jsp" %>
        <script>
            selectMenu("menu-mainPage");
            
        </script>
        <script>
            $(document).ready(function(){
                $(".like-it-button").click(function(){
                    var ajaxUrl = $(this).attr("aurl");
                    var btn = this;
                    $.get(ajaxUrl, {
                        Action:"get"
                    }, function (data){
                        
                        $(btn).after('<div id="space">'+data+'</div>');
                        });
                    });
    
                });
        </script>
        <!-- End of Header -->
        <!-- Page title -->
        <!--
            <div id="pagetitle">
                    <div class="wrapper">
                            <h1>Form examples</h1>
        <!-- Quick search box -->
        <!--
                    <form action="" method="get"><input class="" type="text" id="q" name="q" /></form>
            </div>
    </div>
        -->
        <!-- End of Page title -->
        <c:choose>
            <c:when test="${param.categoryId > 0}">
                <c:set var="categoryId" value="${param.categoryId}"/>   
            </c:when>
            <c:otherwise>
                <c:set var="categoryId" value="1"/>
            </c:otherwise>              
        </c:choose>
        <!-- Page content -->
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <!-- Left column/section -->
                <section class="column width1 first">
                    <div id="left-nav">
                        <ul>
                            <c:forEach var="category" items="${categorys.pageList }">
                                <li><a href="/index_${category.id}.htm" <c:if test="${categoryId == category.id}"> class="current"</c:if>>${category.name}</a></li>
                            </c:forEach>

                        </ul>
                    </div>  
                </section>
                <section class="width5">					
                    <div class="pageline">
                        <form action="" method="get">
                            <input type="hidden" name="categoryId" value="${categoryId}"/>
                            <input class="search-button" type="submit" id="button" value=""  name="search"/>
                            <input class="search-text" type="text" id="keyword" name="keyword" placeholder="按标题搜索"/>

                        </form>
                    </div>
                    <pg:pager items="${serverInfos.totalCount}" maxPageItems="20" maxIndexPages="7" 
                              url="/index.htm"   export="pageNo=pageNumber" scope="request">
                        <pg:param name="categoryId" />
                        <pg:param name="keyword"/>
                        <c:forEach var="serverInfoDetail" items="${serverInfos.pageList}" varStatus="status">
                            <div class="box colgroup middle-div">
                                <div class="rank-column column first">${status.count + param['pager.offset']}</div>
                                <div class="desc-column column width4">
                                    <div class="font-14">
                                        <span class="server-title first"><a target="_blank" href="/detail_${serverInfoDetail.serverInfo.id}.htm">${serverInfoDetail.serverInfo.name}</a></span>
                                        <span class="right-span">${serverInfoDetail.serverInfo.line}</span>
                                    </div>
                                    <div class="desc">
                                        <c:out value="${fn:substring(serverInfoDetail.serverInfo.description, 0, 150)}" escapeXml="true"/>
                                    </div>
                                </div>
                                <div class="like-it-column column">
                                    <!-- <span class="number">239</span> -->
                                    <a class="small-btn btn-yellow like-it-button"  title="每分钟只能赞一次哦" aurl="/vote.htm?id=${serverInfoDetail.serverInfo.id}">赞</a>

                                </div>
                            </div>
                            <div class="clearfix"></div>

                        </c:forEach>


                        <pg:index>
                            <div class="pager">
                                <pg:prev>
                                    <a class="prev" href="${pageUrl}">上一页</a>
                                </pg:prev>
                                <pg:first unless="current">
                                    <a href="${pageUrl }" class="corners">首页</a>
                                </pg:first>
                                <pg:pages>
                                    <c:choose>
                                        <c:when test="${pageNumber == pageNo }">
                                            <span class="current corners">${pageNumber }</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageUrl }" class="corners">${pageNumber }</a>
                                        </c:otherwise>
                                    </c:choose>
                                </pg:pages>
                                <pg:last unless="current">
                                    <a href="${pageUrl }"  class="corners">尾页</a>
                                </pg:last>
                                <pg:next>
                                    <a class="next" href="${pageUrl}">下一页</a>
                                </pg:next>
                            </div>
                        </pg:index>
                    </pg:pager>

                </section>
                <!-- End of Left column/section -->

                <!-- Right column/section -->
                <aside class="colgroup width2"  id="asider">
                    <div class="clean-content-box">                  
                        <section class="notes-total">
                            <img src="/img/atu.png" width="166px" height="42px" alt="17173游戏发布站"/>
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
