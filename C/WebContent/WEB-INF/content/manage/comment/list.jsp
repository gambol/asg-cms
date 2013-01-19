<%@ page contentType="text/html; charset=utf-8" language="java"  errorPage="" %>
<%@ include file="../../util/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="/css/demo.css" type="text/css"/>
<script language="javascript">
	function add() 
	{
   		location.href = "add";
	}
	function edit() 
	{
   		 var id;
    	 var count = 0;
   		 var chk = document.all["checkID"];
   	 	 if(chk == null) 
   	 	 {
       	 alert("请选择要编辑的留言！");
         return false;
    	 }
    		if(chk[0] == null) 
    		{
        		if(chk.checked) 
        		{
            	id = chk.value;
            	count++;
        		}
    		}
    		else 
    		{
        		for(i=0;i<chk.length;i++) 
        		{
            		if(chk[i].checked) 
            		{
               			 count++;
                		 id = chk[i].value;
            		}
        		}
    		}
   		 if(count==0) 
   		 {
        	alert("请选择要编辑的留言！");
       		 return false;
    	 }
    	 if(count>1) 
    	 {
       		 alert("请仅选择一个留言编辑！");
        	 return  false;
    	 }
   			 //alert(id);
    		location.href = "edit?id="+id;
	}
	function query() 
	{
		//alert("edit!"+value);
   		// location.href = "query.jsp";
   		document.all("frmAction").submit();
	}
	function del() 
	{
    	var count = 0;
    	var chk = document.all["checkID"];
    	if(chk == null) 
    	{
        alert("请选择要删除的留言！");
        return  false;
    	}
    	if(chk[0] == null) 
    	{
        	if(chk.checked) 
        	{
            count++;
        	}
   	 	}
   		 else 
    	{
        	for(i=0;i<chk.length;i++) 
        	{
            	if(chk[i].checked) 
            	{
                	count++;
            	}
        	}
    	}
    	if(count==0) 
   		{
        	alert("请选择要删除的留言！");
        	return  false;
   	 	}
   	 	if(confirm("操作不可恢复,您确定删除吗？"))
    	{
    		document.frmAction.action = "delete";
    		document.frmAction.submit();
    	}
	}

	function delSingle(id) 
	{
    	if(confirm("操作不可恢复,您确定删除吗？"))
    	{
        	location.href='delete?checkID='+id;
    	}
	}

	function selectAll(allSelect) 
	{
    	var chk = document.all["checkID"];
    	if(chk == null) 
    	{
        	return  false;
    	}
    	if(chk[0] == null) 
    	{
       		chk.checked = allSelect.checked;
    	}
    	else 
    	{
        	for(i=0;i<chk.length;i++) 
        	{
           	 	chk[i].checked = allSelect.checked;
        	}
    	}
	}	
	function pageQuery(page) 
	{
		document.all("pageView.currentpage").value=page;   
		document.all("frmAction").submit();
	}
</script>
</head>
<body>
<form name="frmAction" method="post" action="list">
<table width="100%" class="toolBar" >
<tr >
   		<td nowrap="nowrap">
          ID
   		  <input name="entity.id" type="text" id="entity.id" value="${entity.id}"/>
          <c:if test="${entity.id==0}">
           <script>document.all["entity.id"].value=""</script>
          </c:if>
          <input type="submit" name="button" id="button" value="直接定位" />
   		  状态
   		  <select name="entity.state" id="entity.state">
   		   <option value="1">全部</option>
           <option value="未审核">未审核</option>
           <option value="已审核">已审核</option>
           <option value="禁用">禁用</option>
		  </select>
		  <script>
		   document.getElementById("entity.state").value ='${entity.state}';
		  </script>
      <input type="submit" name="button" id="button" value="查询"/>
      </td>
		<td id="addbutton" align="right">
			<input type="button" onClick="add()"  value="回复"/>&nbsp;&nbsp;
          <input type="button" onClick="del()" value="删除"/>&nbsp;&nbsp;
       	</td>
   	</tr>
</table>

<table width="100%" border="0" cellpadding="3" cellspacing="1" class="toolTable">
	<tr align="center" class="colom">
		<td width="50"><input type="checkbox" name="selall" onClick="selectAll(this)" /></td>
		<td align='center'>ID</td>
		<td>留言用户</td>
        <td>文章标题</td>
        <td>日期</td>
		<td>留言内容</td>		
        <td>留言状态</td>
        <td>操作</td>
	</tr>
	
	<c:forEach var="entity" items="${pageView.records}" varStatus="status">
		<tr<c:if test="${status.count%2==1}">class='even'</c:if>  class='row'  >
			<td align="center"><input type='checkbox' name='checkID'
				value='<c:out value="${entity.id}" />'/></td>
            <td align="center"><c:out value="${entity.id}"/></td>
			<td align="center">
            <a href="../../user?id=${entity.id}" target="_blank">
            <c:out value="${entity.username}"/></a></td>
            <td align="center"><c:out value="${entity.article.title}"/></td>
            <td align="center"><c:out value="${entity.content}"/></td>
            <td align="center"><c:out value="${entity.state}"/></td>
			<td align="center">${entity.state}
			</td>
			<td align="center"><input type="button" onClick="javascript:location.href='edit?id=${entity.id}'" value="回复"/>
			<input type="button" onClick="delSingle(${entity.id})" value="删除"/></td>
		</tr>
	</c:forEach>
	
</table>
<%@ include file="../util/paging.jsp"%>
</form>
</body>
</html>
