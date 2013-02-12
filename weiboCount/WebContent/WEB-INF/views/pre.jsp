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
       <div id="pagetitle">
			<div class="wrapper">
				<h1>微博信息</h1>
				<!-- Quick search box 
				<form action="" method="get"><input class="" id="q" name="q" type="text"></form>
				-->
			</div>
		</div>
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <section class="width6 column first">					
               
                    <form id="countForm" action="/count.htm" method="post">
						<fieldset>
							<legend>您的信息</legend>
							<p>
								<label for="input1">微博地址</label>
								<br>
								<input  class="full" type="text" name="url" value="">
							</p>
								<p class="box">
									<input type="submit" value="提交" class="btn btn-green big"> 
								</p>
						</fieldset>
						
                    </form>
                </section>
                <!-- End of Left column/section -->

                <!-- Right column/section -->
                <aside class="column width2">
                    <div id="rightmenu">                  
							<header>
								欢迎使用无敌微博统计
							</header>
							<dl>
								我们志在在做最权威，最准备的免费微博数据统计。
							</dl>
                    </div>
                </aside>
                <!-- End of Right column/section -->

                <!-- End of Wrapper -->
            </div>
            <!-- End of Page content -->
            <a id="totop" style="display: block;">^ scroll to top</a>
            <%@include file="../common/footer.jsp" %>

    </body>
</html>
