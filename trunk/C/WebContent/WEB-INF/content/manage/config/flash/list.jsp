<%@ page contentType="text/html; charset=utf-8" language="java"  errorPage="" %>
<%@ include file="../../../util/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="/css/demo.css" type="text/css"/>
<script src="/js/entity.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<form name="frmAction" method="post" action="list">
<input name="id" type="hidden" id="id" value="0"/>
<table width="100%" class="toolBar" >
<tr >
   		<td>
			<input type="button" onClick="add()"  value=" 增 加 "/>&nbsp;&nbsp;
			<input type="button" onClick="edit()" value=" 修 改 "/>&nbsp;&nbsp;
			<input type="button" onClick="del()" value=" 删 除 "/>&nbsp;&nbsp;
       	</td>
   	</tr>
</table>

<table width="100%" border="0" cellpadding="3" cellspacing="1" class="toolTable">
	<!--此处为列描述，自己加入-->
  <tr align="center" class="colom">
		<td width="50" height="20"><input type="checkbox" name="selall" onClick="selectAll(this)" /></td>
		<td align='center'>ID</td>
		<td>标题</td>
        <td>图片缩影</td>
		<td>链接地址</td>
		<td>状态</td>
		<td>操作</td>
	</tr>
	
	<c:forEach var="entity" items="${pageView.records}" varStatus="status">
		<tr<c:if test="${status.count%2==1}">class='even'</c:if>  class='row'  >
			<td align="center"><input type='checkbox' name='ids'
				value='<c:out value="${entity.id}" />'/></td>
            <td align="center"><c:out value="${entity.id}"/></td>
			<td align="center"><c:out value="${entity.title}"/></td>
			<td align="center"><a href="${entity.imageURL}" title="点击查看大图" target="_blank"><img src="${entity.imageURL}" height="50px" border="0" alt="图片已经丢失,请重新上传"/></a></td>
            <td align="center"><a href="${entity.linkURL}" target="_blank"><c:out value="${entity.linkURL}"/></a></td>
            <td align="center"><c:if test="${entity.checkState=='pass'}">
				<img src="/images/yes.gif" title="已审核" />
			</c:if> <c:if test="${entity.checkState=='draft'}">
				<img src="/images/pic17.gif" title="草稿箱" />
			</c:if> <c:if test="${entity.checkState=='noCheck'}">
				<img src="/images/noCheck.gif" title="待审核" />
			</c:if> <c:if test="${entity.checkState=='noPass'}">
				<img src="/images/no.gif" title="未通过" />
			</c:if> <c:if test="${entity.checkState=='recycle'}">
				<img src="/images/recycle.gif" title="回收站" />
			</c:if></td>
            <td align="center"><input type="button" onClick="javascript:location.href='edit?id=${entity.id}'" value=" 修 改 "/>
			<input type="button" onClick="delSingle(${entity.id},'${entity.checkState}')" value=" 删 除 "/></td>
		</tr>
	</c:forEach>
	
</table>
<%@ include file="../../util/paging.jsp"%>
</form>
</body>
</html>
