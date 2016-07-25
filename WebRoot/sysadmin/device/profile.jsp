<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM—设备描述文件</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>设备文件&nbsp;[<a href="${sysadmin_ctx }/profileInfo.do?deviceId=${profile.deviceId }&ctype=${ctype }" target="rightFrame" style="color: #4285F4;font-size: 14px;">刷新</a>] <a href="${sysadmin_ctx }/deviceInfo.do?deviceId=${deviceId}" target="rightFrame" style="color: #4285F4;font-size: 14px;">&lt;&lt;返回</a></span></div>
      <div class="top1 clear"></div>
      <center>
      <div id="loading" style="display: none;">  
		 命令发送中,请稍等!  <img src="${images_ctx }/loading.gif" mce_src="${images_ctx }/loading.gif" alt="loading.." />  
	  </div> 
      </center>
      <table width="100%" border="0" cellspacing="10">
         <c:if test="${empty profile.deviceId}">
         <tr><td>未查询到数据，请先执行<a href="javascript:deviceCommand('${ctx }/device/${ctype }.do?deviceId=${deviceId }','查询');" target="rightFrame">查询</a>命令后刷新!</td></tr>
         </c:if>
         <c:if test="${!empty profile.deviceId}">
         <tr><td height="14" align="left">更新时间：<fmt:formatDate value='${profile.updateTime }' type="date" pattern="yyyy-MM-dd HH:mm:ss" /> &nbsp;&nbsp;</td></tr>
         <tr><td>${profile.result }</td></tr>
         </c:if>
      </table>
    </div>
    <!--主题部分的左边-->
    </td>
  </tr>
</table>
</div>
<div class="footer1"></div>
<script type="text/javascript">
function deviceCommand(url,command){
     if(confirm("是否确认执行查询操作?"))  {
        $("#loading").show();  
	    $.ajax({ 
           type: "post", 
           url: url, 
           dataType: "json", 
           success: function (data) { 
              $("#loading").hide();  
              alert(data.msg);
           }
        });
	 }
}
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>