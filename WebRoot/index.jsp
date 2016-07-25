<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>Apple iOS及OS X设备注册－移动互联百科</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <script type="text/javascript"  src="${assets_ctx }/js/jquery.min.js"></script>
  <script type="text/javascript">
	function userRegister(){
       var userEmail = $("#userEmail").val();
       if(userEmail==""){
           alert("亲!! 邮箱账户不能为空!");
       }else{
	       $.ajax({ 
	           type: "post", 
	           url: "${ctx}/user/register.do?email="+userEmail, 
	           dataType: "json", 
	           beforeSend:function(XMLHttpRequest){
	        	   $("#registerBtn").html("激活中，请稍等...").attr("disable",true);
	           }, 
	           success: function (data) { 
	              alert(data.msg);
	              $("#userEmail").val("");
	              $("#registerBtn").html("&nbsp;&nbsp;邮箱激活&nbsp;&nbsp;").attr("disable",false);
	           },
	           error:function(){
	              alert("呜呜!! 有点问题,麻烦亲再戳一下这个按钮!");
	              $("#userEmail").val("");
	              $("#registerBtn").html("&nbsp;&nbsp;邮箱激活&nbsp;&nbsp;").attr("disable",false);
	           }
	       });
       }
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
    <h1>Apple iOS及OS X设备注册</h1>
    <p>Mobile Device Management <br/>方便、快捷地对iOS及OS X设备进行管理</p>
  </div>
  <hr />
</div>
<div class="am-g">
  <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
    <form method="post" class="am-form" action="${ctx }/user/register.do">
      <label for="deviceFlag">请输入您的邮箱地址?</label>
      <input type="email" name="userEmail" id="userEmail" value="" placeholder="输入可用的邮箱地址">
      <br>
      <div class="am-cf">
        <button type="button" class="am-btn am-btn-primary btn-loading-example" onclick="userRegister();" id="registerBtn">&nbsp;&nbsp;邮箱激活&nbsp;&nbsp;</button>
      </div>
    </form>
    <hr>
    <p>© 2016 移动互联百科. Licensed under Apache license.&nbsp;<a href="${ctx }/sysadmin/login.jsp" style="color:#333;font-size: 14px;text-decoration: underline;">账号登陆</a></p>
  </div>
</div>
<div id="tb">
    <div id="result">
    </div>
</div>
</body>
</html>