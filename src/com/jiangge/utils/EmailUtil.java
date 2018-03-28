package com.jiangge.utils;

public class EmailUtil {

	/**
	 * 以后需要两个参数：接收方地址 、 内容
	 * @param subject
	 * @param toaddress
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static boolean send(String subject, String toaddress, String content) throws Exception {
        boolean sendOK = false;
		try{
			String hostName = ReadPropertity.getProperty("emailsmtp");
			String fromAddress = ReadPropertity.getProperty("emailaddress");
			String fromAPass = ReadPropertity.getProperty("emailpass");
			MyEmailHandle emailHandle = new MyEmailHandle(hostName);
			emailHandle.setFrom(fromAddress);
			emailHandle.setNeedAuth(true);
			emailHandle.setSubject(subject);
			emailHandle.setBody(content);
			emailHandle.setTo(toaddress);
			emailHandle.setNamePass(fromAddress, fromAPass);
			emailHandle.sendEmail();
			sendOK = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return sendOK;
	}

	public static void main(String[] args) {
		try {
			EmailUtil.send("带附件的邮件测试1", "lijiang@xfc123.com","测试内容<a href='http://www.crazyiter.com'>疯狂的IT人</a>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
