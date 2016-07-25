<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>移动设备管理(MDM)—管理登陆</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <script type="text/javascript"  src="${assets_ctx }/js/jquery.min.js"></script>
  <script type="text/javascript">
	function login(obj){
	   if (obj.email.value==""){alert('你还没有输入邮箱账号！');obj.email.focus();return false;} 
	   if (obj.password.value==""){alert('你还没有输入登陆密码！');obj.password.focus();return false;} 
	   obj.submit();
	}
  </script>
  <link rel="alternate icon" type="image/png" href="${assets_ctx }/i/favicon.png">
  <link rel="stylesheet" href="${assets_ctx }/css/amazeui.min.css"/>
  <style>
    .header {
      text-align: center;
    }
    .header h1 {
      font-size: 200%;
      color: #333;
      margin-top: 30px;
    }
    .header p {
      font-size: 14px;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="am-g">
    <h1>移动设备(MDM)-管理登陆</h1>
    <p>Mobile Device Management <br/>方便、快捷地对iOS及OS X设备进行管理</p>
  </div>
  <hr />
</div>
<div class="am-g">
  <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
    <form method="post" class="am-form" action="${ctx }/user/login.do" id="loginForm">
      <label for="account">邮箱:</label>
	  <input type="email" name="email" id="email" value="" placeholder="激活邮箱">
	  <br>
	  <label for="account">密码:</label>
	      <input type="password" name="password" id="password" value="" placeholder="初始密码">
	  <br />
	  <label for="remember-me">
        <input id="remember-me" type="checkbox"> 记住密码
      </label>
      <div class="am-cf">
        <input type="button" onmousedown="return login(loginForm);"  name="" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl">
        <a href="${ctx }/index.jsp"><input type="button" name="" value="邮箱激活 ? " class="am-btn am-btn-default am-btn-sm am-fr"></a>
      </div>
    </form>
    <hr>
    <p>© 2016 移动互联百科. Licensed under Apache license.</p>
  </div>
</div>
<c:if test="${!empty msg }">
<script>
  $(document).ready(function(){
     alert("${msg}");
  });
</script>
<c:remove var="msg"/>
</c:if>
</body>
</html>