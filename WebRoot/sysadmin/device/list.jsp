<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM—设备列表</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>设备列表&nbsp;[<a href="${sysadmin_ctx }/deviceList.do" target="rightFrame" style="color: #4285F4;font-size: 14px;">刷新</a>]</span></div>
      <div class="top1 clear"></div>
      <table class="gridtable" width="100%" border="0" cellspacing="10">
       <tr>
         <th align="center" width="50px">序号</th>
         <th align="left">标签</th>
         <th align="left">版本</th>
         <th align="left">设备ID</th>
         <th align="left">设备类型</th>
         <th align="left">设备状态</th>
         <th align="center">控制</th>
         <th align="center">查看</th>
         <th align="center">操作</th>
       </tr>
       <c:if test="${empty pageList.list}"><tr><td colspan="9" align="center">没有查询到数据!</td> </tr></c:if>
	   <c:if test="${!empty pageList.list}">
	   <c:forEach items="${pageList.list}" var="entity" varStatus="dovar">
	   <tr>
	    <td align="center">${dovar.index+1 }</td>
	    <td><a href="${sysadmin_ctx }/deviceInfo.do?deviceId=${entity.deviceId }" title="查看设备信息">${entity.deviceFlag }</a>&nbsp;&nbsp;<a href="${sysadmin_ctx }/updateFlagInput.do?deviceId=${entity.deviceId }" title="点击修改标签"><img src="${images_ctx }/edit.gif" width="12px"/></a></td>
	    <td>${entity.oSVersion }</td>
	    <td>${entity.deviceId }</td>
		<td>${entity.modelName }</td>
		<td><c:forEach items="${deviceStateMap}" var="entry"><c:if test="${entry.key eq entity.control}">${entry.value}</c:if></c:forEach></td>
		<td align="center">
		  <a href="javascript:deviceCommand('${ctx }/device/lock.do?deviceId=${entity.deviceId }','设备锁屏');" target="rightFrame">设备锁屏</a> |
		  <a href="javascript:deviceCommand('${ctx }/device/clear.do?deviceId=${entity.deviceId }','清除密码');" target="rightFrame">清除密码</a> |
		  <a href="javascript:deviceCommand('${ctx }/device/erase.do?deviceId=${entity.deviceId }','清除数据');" target="rightFrame">清除数据</a> |
		  <a href="${sysadmin_ctx }/installAppInput.do?deviceId=${entity.deviceId }" target="rightFrame">安装APP</a>
		</td>
		<td align="center">
		  <a href="${sysadmin_ctx }/deviceInfo.do?deviceId=${entity.deviceId }" target="rightFrame">设备信息</a>
		</td>
		<td align="center">
		  <a href="javascript:removeDevice('${sysadmin_ctx }/removeDevice.do?deviceId=${entity.deviceId }');" target="rightFrame">移除</a>
		</td>
	   </tr>
	   </c:forEach>
	   </c:if>
      </table>
      <br/>
      <div class="top1 clear"></div>
      <table width="350" border="0" cellpadding="0" cellspacing="0" style="float: right">
       <tr>
         <td height="20" align="center">共 ${ pageList.totalCount} 条数据，页次:${pageList.pageIndex }/${ pageList.pageSize} 页</td>
         <td align="center"><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/deviceList.do?1=1${searchText}">首页</a></c:if><c:if test="${pageList.pageIndex eq 1}">首页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex eq 1}">上一页</c:if><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/deviceList.do?pageIndex=${pageList.pageIndex-1 }${searchText}">上一页</a></c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/deviceList.do?pageIndex=${pageList.pageIndex+1 }${searchText}">下一页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">下一页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/deviceList.do?pageIndex=${pageList.pageSize }${searchText}">尾页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">尾页</c:if></td>
       </tr>
      </table>
    </div>
    <!--主题部分的左边-->
    </td>
  </tr>
</table>
</div>
<div class="footer1"></div>
<div id="loading" style="display: none;">  
 命令发送中,请稍等!  <img src="${images_ctx }/loading.gif" mce_src="${images_ctx }/loading.gif" alt="loading.." />  
</div> 
<script type="text/javascript">
function deviceCommand(url,command){
     if(confirm("是否确认'"+command+"'操作?"))  {
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
function removeDevice(url){
     if(confirm("是否确认移除设备操作?"))  {
	    $.ajax({ 
           type: "post", 
           url: url, 
           dataType: "json", 
           success: function (data) { 
              alert(data.msg);
              window.location.href = "${sysadmin_ctx }/deviceList.do";
           }
        });
	 }
}
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>