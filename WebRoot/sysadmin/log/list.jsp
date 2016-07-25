<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MDM—设备日志</title>
<link href="${css_ctx }/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main1">
  <table width="100%" border="0">
  <tr>
    <td align="left" valign="top">
    <div class="right1"> 
      <div class="top"><span>日志列表&nbsp;[<a href="${sysadmin_ctx }/logList.do" target="rightFrame" style="color: #4285F4;font-size: 14px;">刷新</a>]</span></div>
      <div class="top1 clear"></div>
      <table class="gridtable" width="100%" border="0" cellspacing="10">
       <tr>
         <th align="center" width="50px">序号</th>
         <th align="left">设备标签</th>
         <th align="left">设备ID</th>
         <th align="left">命令类型</th>
         <th align="left">执行情况</th>
         <th align="center">发送时间</th>
         <th align="left">操作</th>
       </tr>
       <c:if test="${empty pageList.list}"><tr><td colspan="7" align="center">没有查询到数据!</td> </tr></c:if>
	   <c:if test="${!empty pageList.list}">
	   <c:forEach items="${pageList.list}" var="entity" varStatus="dovar">
	   <tr>
	    <td align="center">${dovar.index+1 }</td>
	    <td><a href="${sysadmin_ctx }/deviceInfo.do?deviceId=${entity.deviceId }" target="rightFrame" title="点击查看">${entity.deviceFlag }</a></td>
		<td>${entity.deviceId }</td>
		<td><c:forEach items="${commandTypeMap}" var="entry"><c:if test="${entry.key eq entity.command}">${entry.value}</c:if></c:forEach></td>
		<td><c:forEach items="${doTypeMap}" var="entry"><c:if test="${entry.key eq entity.doIt}">${entry.value}</c:if></c:forEach></td>
		<td align="center"><fmt:formatDate value='${entity.createTime }' type="date" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		<td align="left">
		 <a href="javascript:dellog('${sysadmin_ctx }/dellog.do?id=${entity.id }');">删除</a>
		 <c:if test="${entity.command eq 'InstallApplication'}">
		   <a href="javascript:showError('Detail_${entity.deviceId }');">详情</a>
		   <div style="display: none;width: 300px;height: 60px" id="Detail_${entity.deviceId }">
		   类型：${entity.ctype }<br/>
		   地址：${entity.cvalue }
		   </div>
		 </c:if>
		 
		 <c:if test="${entity.doIt eq '3'}">
		   <a href="javascript:showError('ID_${entity.deviceId }');">查看错误</a>
		   <div style="display: none;width: 300px;height: 200px" id="ID_${entity.deviceId }">${entity.result }</div>
		 </c:if>
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
         <td align="center"><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/logList.do?1=1${searchText}">首页</a></c:if><c:if test="${pageList.pageIndex eq 1}">首页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex eq 1}">上一页</c:if><c:if test="${pageList.pageIndex ne 1}"><a href="${sysadmin_ctx}/logList.do?pageIndex=${pageList.pageIndex-1 }${searchText}">上一页</a></c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/logList.do?pageIndex=${pageList.pageIndex+1 }${searchText}">下一页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">下一页</c:if></td>
         <td align="center"><c:if test="${pageList.pageIndex lt pageList.pageSize}"><a href="${sysadmin_ctx}/logList.do?pageIndex=${pageList.pageSize }${searchText}">尾页</a></c:if><c:if test="${pageList.pageIndex eq pageList.pageSize}">尾页</c:if></td>
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
  function dellog(url){
     if(confirm("是否确认删除该日志?"))  {  
	    $.ajax({ 
           type: "post", 
           url: url, 
           dataType: "json", 
           success: function (data) { 
              alert(data.msg);
              window.location.href = "${sysadmin_ctx }/logList.do";
           }
     });
	 }
  }
  
  function showError(id){
     $("#"+id).toggle();
  }
</script>
<script type="text/javascript" src="${js_ctx }/jquery-1.8.2.min.js"></script>
</body>
</html>