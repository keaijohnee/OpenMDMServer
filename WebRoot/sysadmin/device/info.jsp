<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM—设备详情</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>设备信息&nbsp;[<a href="${sysadmin_ctx }/deviceInfo.do?deviceId=${device.deviceId }" target="rightFrame" style="color: #4285F4;font-size: 14px;">刷新</a>] <a href="${sysadmin_ctx }/deviceList.do" target="rightFrame" style="color: #4285F4;font-size: 14px;">&lt;&lt;返回</a></span></div>
      <div class="top1 clear"></div>
      <table width="100%" border="0" cellspacing="10">
          <tr>
            <td width="110" height="14" align="right">设备标签：</td><td style="color: red;">${device.deviceFlag }</td>
            <td width="110" height="14" align="right">UDID：</td><td>${device.udid }</td>
          </tr>
          
          <tr>
            <td width="110" height="14" align="right">设备类型：</td><td>${device.modelName } [ ${device.model } ]</td>
            <td width="110" height="14" align="right">设备编号：</td><td>${device.deviceId }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">设备序列号：</td><td>${device.serialNumber }</td>
            <td width="110" height="14" align="right">IMEI：</td><td>${device.imei }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">ICCID：</td><td>${device.iccid }</td>
            <td width="110" height="14" align="right">MEID：</td><td>${device.meid }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">Supervised模式：</td><td>${device.isSupervised }</td>
            <td width="110" height="14" align="right">IsDeviceLocatorServiceEnabled：</td><td>${device.isDeviceLocatorServiceEnabled }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">IsActivationLockEnabled：</td><td>${device.isActivationLockEnabled }</td>
            <td width="110" height="14" align="right">IsCloudBackupEnabled：</td><td>${device.isCloudBackupEnabled }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">WIFIMAC地址：</td><td>${device.wifimac }</td>
            <td width="110" height="14" align="right">BluetoothMAC：</td><td>${device.bluetoothMAC }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">设备UDID：</td><td>${device.udid }</td>
            <td width="110" height="14" align="right">更新时间：</td><td><fmt:formatDate value='${device.updateTime }' type="date" pattern="yyyy/MM/dd HH:mm:ss" /></td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">设备电量：</td><td>${device.batteryLevel }%</td>
            <td width="110" height="14" align="right">总存储大小：</td><td>${device.deviceCapacity }G</td>
          </tr>
          <tr>
            <td width="110" height="14" align="right">可用存储：</td><td>${device.availableDeviceCapacity }G</td>
            <td width="110" height="14" align="right">IOS版本：</td><td>${device.oSVersion }</td>
          </tr>
          <tr>
            <td width="110" height="14" align="left" colspan="2">
                                   操作：
             <a href="javascript:deviceCommand('${ctx }/device/info.do?deviceId=${device.deviceId }','设备信息');" target="rightFrame">更新设备信息</a> |
		     <a href="javascript:deviceAppsCommand('${ctx }/device/apps.do?deviceId=${device.deviceId }','全部APP管理');" target="rightFrame">更新APP列表</a> |
		     <a href="javascript:deviceCommand('${ctx }/device/ProfileList.do?deviceId=${device.deviceId }','获取描述文件');" target="rightFrame">更新描述文件</a> |
		     <a href="javascript:deviceCommand('${ctx }/device/ProvisioningProfileList.do?deviceId=${device.deviceId }','获取预置描述文件');" target="rightFrame">更新预置描述文件</a> |
		     <a href="javascript:deviceCommand('${ctx }/device/CertificateList.do?deviceId=${device.deviceId }','获取证书文件');" target="rightFrame">更新证书文件</a>
            </td>
            <td height="14" align="center" colspan="2">
                                  查看：
             <a href="${sysadmin_ctx }/profileInfo.do?deviceId=${device.deviceId }&ctype=ProfileList" target="rightFrame">查看描述文件</a> |
		     <a href="${sysadmin_ctx }/profileInfo.do?deviceId=${device.deviceId }&ctype=ProvisioningProfileList" target="rightFrame">查看预置描述文件</a> |
		     <a href="${sysadmin_ctx }/profileInfo.do?deviceId=${device.deviceId }&ctype=CertificateList" target="rightFrame">查看证书文件</a>
            </td>
          </tr>
      </table>
      <center>
      <div id="loading" style="display: none;">  
		 命令发送中,请稍等!  <img src="${images_ctx }/loading.gif" mce_src="${images_ctx }/loading.gif" alt="loading.." />  
	  </div> 
      </center>
      <table class="gridtable" width="100%" border="0" cellspacing="10">
       <tr>
         <th align="center" width="50px">序号</th>
         <th align="left">应用名称</th>
         <th align="left">identifier</th>
         <th align="center">获取时间</th>
         <th align="center">管理</th>
       </tr>
       <c:if test="${empty pageList.list}"><tr><td colspan="5" align="center">没有查询到数据!</td> </tr></c:if>
	   <c:if test="${!empty pageList.list}">
	   <c:forEach items="${pageList.list}" var="entity" varStatus="dovar">
	   <tr>
	    <td align="center">${dovar.index+1 }</td>
		<td>${entity.appName }</td>
		<td>${entity.identifier}</td>
		<td align="center"><fmt:formatDate value='${entity.createTime }' type="date" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		<td align="center">
		  <c:if test="${entity.managedAppsOnly eq '1'}">
		  <a href="javascript:deviceCommand('${ctx }/device/remove.do?deviceId=${entity.deviceId }&identifier=${entity.identifier }','APP卸载');" target="rightFrame">卸载</a>
		  </c:if>
		  <c:if test="${entity.managedAppsOnly eq '0'}">—</c:if><c:if test="${empty entity.managedAppsOnly}">—</c:if>
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
         <td align="center"><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/deviceInfo.do?1=1${searchText}">首页</a></c:if><c:if test="${pageList.pageIndex eq 1}">首页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex eq 1}">上一页</c:if><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/deviceInfo.do?pageIndex=${pageList.pageIndex-1 }${searchText}">上一页</a></c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/deviceInfo.do?pageIndex=${pageList.pageIndex+1 }${searchText}">下一页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">下一页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/deviceInfo.do?pageIndex=${pageList.pageSize }${searchText}">尾页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">尾页</c:if></td>
       </tr>
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

function deviceAppsCommand(url,command){
     if(confirm("是否确认'"+command+"'操作?"))  {
        $("#loading").show();  
        getAllApps(url);
        getManagedApps(url);
	 }
}

function getAllApps(url){
     $.ajax({ 
           type: "post", 
           url: url+"&ctype=All", 
           dataType: "json", 
           success: function (data) { 
              //do nothing
           }
     });
}
function getManagedApps(url){
     $.ajax({ 
           type: "post", 
           url: url+"&ctype=ManagedAppsOnly", 
           dataType: "json", 
           success: function (data) { 
             alert(data.msg);
             $("#loading").hide(); 
           }
     });
}
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>