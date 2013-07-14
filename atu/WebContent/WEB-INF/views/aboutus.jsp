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
        <script>
            selectMenu("menu-aboutUs");
        </script>
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">

                <!-- Login form -->

                <div class="full" style="padding-top:10px;">					

                    <div>
                       <img src="/img/aboutUs.png" alt="aboutus">                        
                    </div>

                   

                </div>
                <!-- End of login form -->

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
