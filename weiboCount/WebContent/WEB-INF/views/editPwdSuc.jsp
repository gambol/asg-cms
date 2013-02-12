<!--<!DOCTYPE html>-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<%@page  language="java" contentType="text/html" pageEncoding="UTF-8"%>
<html>   
    <%@include file="../common/appInclude.jsp" %>
    <body>

        <!-- Header -->
        <%@include file="../common/header.jsp" %>
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
                            <li ><a href="/user/sitemanage.htm">发布记录</a></li>
                            <li><a href="/user/publish.htm">新站发布</a></li>
                            <li class="current"><a href="/user/editPassword.htm">修改密码</a></li>
                            <!--<li><a href="#">修改密码</a></li> -->

                        </ul>
                    </div>  
                </section>
                <!-- Login form -->
                <section class="width5">					
                    <div class="pagetitle">
                        <header class="blue">
                            <h3>修改密码</h3>
                        </header>

                    </div>
                    <div class="box box-success"  > 您已经成功修改密码. </div>
                </section>

                <!-- End of login form -->
                <div class="first"></div>

            </div>
            <!-- End of Wrapper -->
        </div>
        <!-- End of Page content -->

        <!-- Page footer -->
        <%@include file="../common/footer.jsp" %>
        <!-- End of Animated footer -->

        <!-- Scroll to top link -->
        <a href="#" id="totop">^ scroll to top</a>

        <!-- User interface javascript load -->
    </body>
</html>
