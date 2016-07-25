<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM-Top</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="head1">
<table width="100%" height="60" border="0">
  <tr>
    <td width="10"></td>
    <td width="230" style="font-size: 24px;">移动设备管理(MDM)</td>
    <td width="250" valign="bottom" style="padding-bottom: 10px;">${sysadmin.email } [<a href="javascript:logout();" >注销</a>]</td>
    <td>&nbsp;</td>
    <td width="265">Mobile Device Management</td>
    <td width="10"></td>
  </tr>
</table>
</div>
<script type="text/javascript">
  function logout(){
     if(confirm("是否确认退出登陆?"))  {  
	   window.parent.location.href='${sysadmin_ctx}/logout.do';
	 }
  }
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>