<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM-密码修改</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>密码修改&nbsp;[<a href="${sysadmin_ctx }/changePwdInput.do" target="rightFrame" style="color: #4285F4;font-size: 14px;">刷新</a>]</span></div>
      <div class="top1 clear"></div>
      <form action="${sysadmin_ctx }/changePwd.do" method="post" id="submitForm" name="submitForm">
      <input type="hidden" name="uid" value="${sysadmin.id }"/>
      <table width="100%" border="0" cellspacing="10">
          <tr><td width="110" height="14" align="right">用户名：</td><td>${sysadmin.email }</td></tr>
          <tr><td width="110" height="14" align="right">密码：</td><td><input type="text" name="password" value="" style="width: 200px; height: 20px;"/><font color="red">[留空则不修改]</font></td></tr>
          <tr><td width="110" height="14" align="right"><input type="button" onclick="javascript:setPwdForm();" value="提交请求" style="width: 65px; height: 22px;" /></td><td>&nbsp;</td></tr>
      </table>
      </form>
    </div>
    <!--主题部分的左边-->
    </td>
  </tr>
</table>
</div>
<div class="footer1"></div>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
  function setPwdForm(){
       $.ajax({ 
           type: "post", 
           url: "${sysadmin_ctx }/changePwd.do", 
           dataType: "json", 
           data: $('#submitForm').serialize(),
           success: function (data) { 
              alert(data.msg);
              window.location.href = "${sysadmin_ctx }/changePwdInput.do";
           }
       });
  }
</script>
</body>
</html>