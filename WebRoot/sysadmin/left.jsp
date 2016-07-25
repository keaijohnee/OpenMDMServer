<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>贵阳移动销售助手—Left</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="nav_main">
<ul class="nav_ul">
      <li>
	      <table width="100%" border="0">
	        <tr>
	          <td width="15" height="36" align="center">&nbsp;</td>
	          <td><a href="${sysadmin_ctx }/deviceList.do" target="rightFrame">设备管理</a></td>
	        </tr>
	      </table>
	  </li>
	  <li>
	      <table width="100%" border="0">
	        <tr>
	          <td width="15" height="36" align="center">&nbsp;</td>
	          <td><a href="${sysadmin_ctx }/logList.do" target="rightFrame">命令日志</a></td>
	        </tr>
	      </table>
	  </li>
	  <li>
	      <table width="100%" border="0">
	        <tr>
	          <td width="15" height="36" align="center">&nbsp;</td>
	          <td><a href="${sysadmin_ctx }/changePwdInput.do" target="rightFrame">密码修改</a></td>
	        </tr>
	      </table>
	  </li>
</ul>
</div>
<!--主题部分的右边-->
</body>
</html>