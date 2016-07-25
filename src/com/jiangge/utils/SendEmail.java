package com.jiangge.utils;

import java.util.Map;

public class SendEmail {
	
	public static void main(String[] args) throws Exception{
		/**发送邮件**/
		String content = "lijiang@xfc123.com你好：<br/>描述文件下载地址："
				+ "<a href='http://mdm.mbaike.net/user/download.do?uid="+111+"' target='_blank'>http://mdm.mbaike.net/user/download.do?uid="+111+"</a><br/>"
				+ "你在平台的登录账号是：lijiang@xfc123.com,登录密码是：" + 111 + "</br>"
				+ "<br/>感谢支持！http://www.mbaike.net/";
		EmailUtil.send("MDM测试激活邮件","lijiang@xfc123.com",content);
	}

	/**
	 * 向指定手机号发送短信
	 * @param phone
	 * @param content
	 */
	public void send(Map<String, String> taskParam){
		/**获取手机号和发送的内容**/
		String smtp = taskParam.get("smtp");
		String fromAddress = taskParam.get("fromAddress");
		String fromPass = taskParam.get("fromPass");
		String toAddress = taskParam.get("toAddress");
		String subject = taskParam.get("subject");
		String content = taskParam.get("content");
		EmailUtils.send(smtp, fromAddress, fromPass, toAddress, subject, content);
	}
}
