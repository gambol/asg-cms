<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../util/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/css/demo.css" rel=stylesheet  type="text/css"/>

<script language="javascript" src="/js/post.js" type="text/javascript"></script>
<script language="javascript" src="/admin/js/news.js" type="text/javascript"></script>
</head>
<body>
<div>
  <table width="100%" height="100%" border="0" align="center" cellpadding="1" cellspacing="1">
    <tr>
      <td height="27" align="middle" background="/images/menu_bg.jpg"></td>
    </tr>
    <tr><td>    
          <form action="update" method="post">
          <input  type="hidden" name="config.id" id="config.id" value="${config.id}" />
          <input  type="hidden" name="config.uuid" id="config.uuid" value="${empty config.uuid?C.uuid:config.uuid}" />
          <input  type="hidden" name="config.ip" id="config.ip" value="${empty config.ip?C.ip:config.ip}" />
            <table  border="0" style="font-size:12px;">     
			   <tr>
                <td align="right" width="200">程序版本:</td>
                <td width="400"><input  size="40"   type="text" name="config.version" id="config.version" value="${config.version}" />&nbsp;&nbsp;<iframe src="/manage/getLatestVersion" marginheight="2px" marginwidth="0" height="1px" width="1px" scrolling="no" frameborder="0"  ></iframe></td> 
               </tr>
               <tr>
                <td align="right" width="200">网站域名:</td>
                <td width="400"><input  size="40"   type="text" name="config.domain" id="config.domain" value="${config.domain}" /></td> 
               </tr>
              <tr>
                <td align="right">网站简称:</td>
                <td><input  size="40"   type="text" name="config.name" id="config.name" value="${config.name}" /></td>
              </tr>
              <tr>
                <td align="right">网站全称:</td>
                <td><input  size="40"   type="text" name="config.fullName" id="config.fullName" value="${config.fullName}" /></td>
              </tr>             
              <tr>
                <td align="right">网站关键字:</td>
                <td><input  size="40"   type="text" name="config.keywords" id="config.keywords" value="${config.keywords}" maxlength="50"/></td>
              </tr>             
              <tr>
                <td align="right">网站描述:</td>
                <td><input  size="40"   type="text" name="config.description" id="config.description" value="${config.description}" maxlength="200" /></td>
              </tr>            
              <tr>
                <td align="right">负责人:</td>
                <td><input  size="40"   type="text" name="config.ceo" id="config.ceo" value="${config.ceo}" /></td>
              </tr>
              <tr>
                <td align="right">邮箱:</td>
                <td><input  size="40"   type="text" name="config.email" id="config.email" value="${config.email}" /></td>
              </tr>
              <tr>
                <td align="right">Q Q:</td>
                <td><input  size="40"   type="text" name="config.QQ" id="config.QQ" value="${config.QQ}" /></td>
              </tr>
              <tr>
                <td align="right">电话:</td>
                <td><input  size="40"   type="text" name="config.tel" id="config.tel" value="${config.tel}" /></td>
              </tr>
              <tr>
                <td align="right">手机:</td>
                <td><input  size="40"   type="text" name="config.mobile" id="config.mobile" value="${config.mobile}" /></td>
              </tr>
              <tr>
                <td align="right">公司名称:</td>
                <td><input  size="40"  type="text" name="config.company" id="config.company" value="${config.company}" /></td>
              </tr>
              <tr>
                <td align="right">版权信息:</td>
                <td><input  size="40"   type="text" name="config.copyright" id="config.copyright"  value="${config.copyright}"/></td>
              </tr>
              <tr>
                <td align="right">备案号:</td>
                <td><input  size="40" type="text" name="config.recordCode" id="config.recordCode" value="${config.recordCode}" /></td>
              </tr>
              <tr>
                <td align="right">模版路径</td>
                <td><input  size="40" type="text" name="config.template" id="config.template"  value="${config.template}"/></td>
              </tr>
              <tr>
                <td align="right">程序目录:</td>
                <td><input  size="40" type="text" name="config.file" id="config.file"  value="${config.file}"/>
                 </td>
              </tr>
              <tr>
                <td align="right">站长统计:</td>
                <td><input  size="40" type="text" name="config.statistics" id="config.statistics"  value="${config.statistics}"/></td>
              </tr>
              <tr>
              	<td align="right">网站介绍:</td>
              	<td><textarea name="config.introduce" id="config.introduce"cols="80" rows="6">${config.introduce}</textarea></td>
              </tr> 
              <tr>
                <td height="21" align="right">&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td height="21" align="right">&nbsp;</td>
                <td><input type="submit" name="button" id="button" value=" 保  存  " /></td>
              </tr>
            </table>
          
          </form>      	
          </td>
        </tr>
  </table>
</div>
 <p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>
</body>
</html>