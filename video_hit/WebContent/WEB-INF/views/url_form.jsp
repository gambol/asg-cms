<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html lang="en">
<%@include file="../common/appInclude.jsp"%>
<script type="text/javascript" SRC="/js/jquery-validate.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// validate form on keyup and submit
		var validator = $("#countForm").validate({
			rules : {
				url : {
					required : true,
					url : true,
				},
				captcha : {
					required : true,
					remote : "/validateImage.htm"
				}
			},
			messages : {
				url : {
					required : "输入站点url",
					url : "url错误"
				},
				captcha : {
					required : "输入验证码",
					remote : "请输入正确的验证码"
				}
			},
			// the errorPlacement has to take the layout into account
			errorPlacement : function(error, element) {
				error.insertAfter(element.parent().find('input:first'));
				//error.appendTo (element.next());
			},
			success : function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("ok");
				/*
				var a = "label.error[for='"+label.attr("for")+"']";
				alert(a);
				$("label.error[for='"+label.attr("for")+"']").classname('');
				$("label.error[for='server_name']").classname('');
				 */
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			/*
			submitHandler : function() {
				alert("Data submitted!");
			}
			*/

		});
	});

	function refreshImage() {
		$('#captchaImg').hide().attr('src',
				"/generateImage.htm?" + Math.floor(Math.random() * 100))
				.fadeIn();
	}
</script>


<body>
	<%@include file="../common/header.jsp"%>

	<div id="pagetitle">
		<div class="wrapper"></div>
	</div>
	<div id="page">
		<!-- Wrapper -->
		<div class="wrapper">
			<section class="width6 column first">
				<h3>
					<a href="/">回到首页</a> → ${last_step_name }
				</h3>
				<form id="countForm" action="${post_url }" method="post">
					<table class="url_form">
					<tbody>
						<tr>
							<th>视频链接:
							</th>
							<td><input style="width:400px" type="text" name="url" value=""></td>
						</tr>
						<tr>
							<th>增加数量:</th>
							<td>
								<a href="${deleteUrl}" title="减少500次"> 
									<img width="16" height="16" src="/img/delete.png">
								</a>&nbsp; 
								<input	type="text" readonly value="${num }次" size="5" name="num">
								<a href="${addUrl }" title="增加500次，最多不超过3000次">
								 	<img width="16" height="16" src="/img/add.png">
								</a>
							</td>
						</tr>
						<tr>
							<th></th>
							<td><img id='captchaImg' alt="验证码" src="/generateImage.htm">
								<a href="javascript:void(0)" onclick="refreshImage()">看不清?
									换一张</a></td>
						</tr>
						<tr>
							<th>验&nbsp;证&nbsp;码:</th>
							<td><input type="text" name="captcha" id="captcha"
								class="three" /></td>
						</tr>

						<tr>
							<td></td>
							<td><input type="submit" value="刷！"
								class="btn btn-green big"></td>
						</tr>
						</tbody>
					</table>

				</form>
			</section>
			<%@include file="../common/sider.jsp"%>
		</div>
	</div>

	<%@include file="../common/footer.jsp"%>

</body>
</html>
