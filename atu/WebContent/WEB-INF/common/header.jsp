<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script>
    function selectMenu(menuId) {
        $('#' + menuId).addClass("current");
    }
</script>
<!-- Header -->
<header id="top">
    <div class="wrapper">
        <!-- Title/Logo - can use text instead of image -->
        <div id="title"><a href="/index.htm" ><img src="/img/logo.png" width="223px" height="59px" alt="阿土游戏"></a></div>
        <!-- Top navigation -->
        <%
            String user = (String) request.getSession().getAttribute("username");
            pageContext.setAttribute("username", user);
        %>
        <div id="topnav">
            <c:choose>
                <c:when test="${not empty username}">
                    <!--
                    <a href="#"><img class="avatar" SRC="img/user_32.png" alt="" /></a>
                    -->
                    Hi <b><c:out value="${username}"/></b>
                    <span>|</span> <a href="/user/logout.htm">注销</a><br />
                </c:when>
                <c:otherwise>
                   <a href="/user/regi.htm"> 注册 </a> | <a href="/user/login.htm"> 登录 </a>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- End of Top navigation -->
        <!-- Main navigation -->
        <nav id="menu">
            <ul class="sf-menu">
                <li id="menu-mainPage"><a HREF="/index.htm">首页</a></li>
                <c:if test="${not empty username}">
                    <li id="menu-userCenter"><a HREF="/user/sitemanage.htm">个人中心</a></li>
                </c:if>
                <li id="menu-aboutUs"><a HREF="/aboutus.htm">关于我们</a></li>                

            </ul>
        </nav>
    </div>
</header>