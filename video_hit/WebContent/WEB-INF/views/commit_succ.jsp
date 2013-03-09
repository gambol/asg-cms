<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%@include file="../common/appInclude.jsp"%>
<body>
	<%@include file="../common/header.jsp"%>

	<div id="pagetitle">
		<div class="wrapper">
		</div>
	</div>
	<div id="page">
		<!-- Wrapper -->
		<div class="wrapper">
			<section class="width6 column first">

				<h3><a href="/">回到首页</a> → <a href="${last_step_url }">${last_step_name }</a> → 提交成功</h3>
				<p>
				恭喜您，任务已经提交成功了。我们正在紧张的服务于您，请稍候查看视频链接:
				</p>
				<p>
					<a href="${url }" target="_blank">${url }</a>
				</p>
			</section>
			<%@include file="../common/sider.jsp"%>
		</div>
	</div>
	<!-- End of Page content -->
	<a id="totop" style="display: block;">^ scroll to top</a>
	<%@include file="../common/footer.jsp"%>

</body>
</html>
