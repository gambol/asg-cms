
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@include file="../common/appInclude.jsp" %>
    <body>
        <%@include file="../common/header.jsp" %>
        <script>
            selectMenu("menu-userCenter");
        </script>
        <script type="text/javascript" src="/js/sitemanage.js"></script>
        <!-- End of Header -->
        <!-- Page title -->
        <!--
            <div id="pagetitle">
                    <div class="wrapper-login"></div>
            </div>
        <!-- End of Page title -->

        <!-- Page content -->
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <section class="column width1 first">
                    
                    <div class="site-nav">
                        <ul>
                            <li class="current"><a href="/user/sitemanage.htm">发布记录</a></li>
                            <li><a href="/user/publish.htm">新站发布</a></li>
                            <li><a href="/user/editPassword.htm">修改密码</a></li>
                            <!--<li><a href="#">修改密码</a></li> -->

                        </ul>
                    </div>  
                </section>
                <!-- Login form -->

                <section class="width5">					
                    
                    <pg:pager items="${serverInfos.totalCount}" maxPageItems="20" maxIndexPages="10" 
                              url="/sitemanage.htm"   export="pageNo=pageNumber" scope="request">
                        <table class="full stylized">

                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>站点</th>
                                    <th>站点地址</th>
                                    <th>操作</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${serverInfos.currentSize == 0}">
                                    <td colspan="4">
                                        <div class="nodata"><p>你还没有发布站点</p></div>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="serverInfo" items="${serverInfos.pageList }">
                                        <tr>
                                            <td>${serverInfo.id}</td>
                                            <td><c:out value="${serverInfo.name}" escapeXml="true" /></td>
                                            <td><a href="${serverInfo.url}" target="_blank"> ${serverInfo.url} </a></td>
                                            <td><a href="/publish.htm?id=${serverInfo.id}" /> 编辑 </a> &nbsp;&nbsp;
                                                <c:choose>
                                                    <c:when test="${serverInfo.status == 'online'}">
                                                        <a href="#" aurl="/user/sitemanage/changeStatus.htm?id=${serverInfo.id}" class="changeStatus"> 隐藏 </a>
                                                    </c:when>
                                                    <c:when test="${serverInfo.status == 'hidden'}">
                                                        <a href="#" aurl="/user/sitemanage/changeStatus.htm?id=${serverInfo.id}" class="changeStatus"> 显示</a>
                                                    </c:when>
                                                </c:choose>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        
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
                <!-- End of login form -->
                <div class="first"></div>
                     
            </div>
            <!-- End of Wrapper -->
        </div>
        <!-- End of Page content -->
        <%@include file="../common/footer.jsp" %>

    </body>
</html>

