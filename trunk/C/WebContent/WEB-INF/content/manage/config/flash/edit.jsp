<%@ page contentType="text/html; charset=utf-8" language="java"  errorPage=""%>
<%@ include file="../../../util/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel=stylesheet href="/css/demo.css" type="text/css"/>
<link href="/css/bodystyle.css" rel="stylesheet" type="text/css"/>
<script>
function checkForm() {
	if(document.all("entity.title").value.length==0) {
		alert("标题不能为空!");
		document.all("entity.title").focus();
		return false;
	}
	if(document.all("entity.imageURL").value.length==0) {
		alert("图片地址不能为空!");
		document.all("entity.imageURL").focus();
		return false;
	}
	if(document.all("entity.linkURL").value.length==0) {
		alert("链接地址不能为空!");
		document.all("entity.linkURL").focus();
		return false;
	}
	document.all("frmAction").submit();
}
function back() {
	history.go(-1);
}
</script>
</head>
<body>
<form name="frmAction" method="post" action="update">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
	<tr >
		<td height="27px" colspan="3" background="/images/menu_bg.jpg">&nbsp;&nbsp;
        <b class="title">修改FLASH图片:${entity.id}</b>
        <input name="entity.id" type="hidden" id="entity.id" value="${entity.id}"/>
        </td>
	</tr>
	<tr>
		<td height="25px" align="right">状态：</td>
		<td colspan="2">
		<select name="entity.checkState" id="entity.checkState">
	      <option value="draft" selected="selected">草稿箱</option>
	      <option value="noCheck">待审核</option>
	      <option value="pass">已审核</option>
	      <option value="noPass">未通过</option>
	      <option value="recycle">回收站</option>
	    </select>
        <script>document.getElementById("entity.checkState").value ='${entity.checkState}';</script>
		</td>
	</tr>
	<tr>
		<td height="25px" align="right">标题：</td>
		<td colspan="2"><input name="entity.title" value="${entity.title}" type="text" id="entity.title"
			size="40" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td height="25px" align="right">图片地址：</td>
		<td colspan="2"><input name="entity.imageURL" value="${entity.imageURL}" type="text" id="entity.imageURL"
			size="40" maxlength="300"/>
		</td>
	</tr>
	<tr>
		<td height="25px" align="right">链接地址：</td>
		<td colspan="2"><input name="entity.linkURL" value="${entity.linkURL}" type="text" id="entity.linkURL"
			size="40" maxlength="300"/>
		</td>
	</tr>
	<tr> <td></td>
		<td colspan="2" height="50px">
		<input  type="button"  value=" 保 存 " onClick="javascript:checkForm()" />
        <input  type="button" value=" 返 回 " onclick='javascript: history.go(-1)' /> 																	        
		</td>
	</tr>
</table>
</form>
<br/>
<br/>
</body>
</html>