<%@page import="com.bieshao.web.common.SessionConst"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script>
    function selectMenu(menuId) {
        $('#' + menuId).addClass("current");
    }
    
    function logout() {
    	
    }
</script>
<!-- Header -->
<header id="top">
    <div class="wrapper">
        <!-- Title/Logo - can use text instead of image -->
        <div id="title"><a href="/index.htm" ><img src="/img/logo.png" alt="无敌微博统计"></a></div>
        <div id="topnav">  
			掌控你的微博,无敌微博统计 
			<%if (session.getAttribute(SessionConst.AT) != null) {  %>				
				 | <a href="/logout.htm">登出</a>
			<%} %>
        </div>

    </div>
</header>