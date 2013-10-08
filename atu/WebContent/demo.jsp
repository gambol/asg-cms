<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String htmlData = request.getParameter("content1") != null ? request.getParameter("content1") : "";
%>
<!doctype html>
<html>
<head>
	<title>KindEditor JSP</title>
	<link rel="stylesheet" href="/styles/css/kindEditor-thems/default/default.css" />
	<script charset="utf-8" src="/js/kind/kindeditor.js"></script>
	<script charset="utf-8" src="/js/kind/zh_CN.js"></script>
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="content1"]', {
				allowFileManager : false,
					width : '40%',
				  filterMode :true,
				 htmlTags:{    //以下为不需要过滤过的字符
		            font : ['color'],
		            span : ['/'],  
		            br : ['/'],
		            p : [''],
		            strong : []
		        },
				items : [
						'bold', 'removeformat'
						]
			});
		//	prettyPrint();
		});
	</script>
</head>
<body>
	<%=htmlData%>
	<form name="example" method="post" action="demo.jsp">
		<textarea name="content1"  style="width:100px;height:200px;visibility:hidden;" ><%=htmlspecialchars(htmlData)%></textarea>
		<br />
		<input type="submit" name="button" value="提交内容" /> (提交快捷键: Ctrl + Enter)
	</form>
</body>
</html>
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>
