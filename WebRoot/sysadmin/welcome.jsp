<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM—Welcome</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>欢迎使用</span></div>
      <div class="top1 clear"></div>
      <table width="100%" border="0" cellspacing="15" style="height: 250px; border-bottom: 1px solid #F4F4F4;border-right: 1px solid #F4F4F4;border-left: 1px solid #F4F4F4;">
          <tr><td>&nbsp;</td></tr>
          <tr><td><img src="${images_ctx }/welcome.png" width="500px" style="margin-left: 50px"/></td></tr>
      </table>
    </div>
    <!--主题部分的左边-->
    </td>
  </tr>
</table>
</div>
<div class="footer1"></div>
<script type="text/javascript">
  function logout(){
     if(confirm("是否确认退出登陆?"))  {  
	   location.href='${sysadmin_ctx}/logout';
	 }
  }
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>