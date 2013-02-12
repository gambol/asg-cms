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
      
       <div id="pagetitle">
			<div class="wrapper">
				<h1>url分析</h1>
				<!-- Quick search box 
				<form action="" method="get"><input class="" id="q" name="q" type="text"></form>
				-->
			</div>
		</div>
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <section class="width6 column first">

				<h3>url分析</h3>
				<div class="box box-info">url: ${url }</div>
				<table class="stylized full">
					<tr>
						<th>项目</th>
						<th>数目（人)</th>
					</tr>
					<tr>
						<td>转发数</td>
						<td>${wcr.forwardNum}</td>
					</tr>
					<tr>
						<td>覆盖人数</td>
						<td>${wcr.coverNum}</td>
					</tr>
				</table>
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
