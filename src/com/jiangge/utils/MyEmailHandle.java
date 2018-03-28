package com.jiangge.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author jiang.li
 * @date 2013-12-19 14:08
 */
public class MyEmailHandle {

	private MimeMessage mimeMsg; 
	private Session session; 
	private Properties props;
	private String sendUserName;
	private String sendUserPass; 
	private Multipart mp;
	private List<FileDataSource> files = new LinkedList<FileDataSource>();

	
	public MyEmailHandle(String smtp) {
		sendUserName = "";
		sendUserPass = "";
		setSmtpHost(smtp);
		createMimeMessage();
	}

	private void setSmtpHost(String hostName) {
		if (props == null){
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.port", "465");
	}

	public boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null);
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		
	}

	/**
	 */
	public void setNeedAuth(boolean need) {
		if (props == null){ props = System.getProperties();}
		if (need){
			props.put("mail.smtp.auth", "true");
		}else{
			props.put("mail.smtp.auth", "false");
		}
			
	}

	/**
	 */
	public void setNamePass(String name, String pass) {
		sendUserName = name;
		sendUserPass = pass;
	}

	/**
	 * 
	 * @param mailSubject
	 * @return
	 */
	public boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * @param mailBody
	 * @return
	 */
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody, "text/html;charset=UTF-8");
			mp.addBodyPart(bp);
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean addFileAffix(String filename) {
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8",null)); 
			mp.addBodyPart(bp);
			files.add(fileds);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		
	}

	/**
	 * @return
	 */
	public boolean delFileAffix() {
		try {
			FileDataSource fileds = null;
			for (Iterator<FileDataSource> it = files.iterator(); it.hasNext();) {
				fileds = it.next();
				if (fileds != null && fileds.getFile() != null) {
					fileds.getFile().delete();
				}
			}
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		
	}

	/**
	 * @return
	 */
	public boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return
	 */
	public boolean setTo(String to) {
		try {
			if (to == null){ return false;}
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param copyto
	 * @return
	 */
	public boolean setCopyTo(String copyto) {
		try {
			if (copyto == null){return false;}
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC,InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return
	 */
	public boolean sendEmail() throws Exception {
		mimeMsg.setContent(mp);
		mimeMsg.saveChanges();
		Session mailSession = Session.getInstance(props, null);
		Transport transport = mailSession.getTransport("smtp");
		transport.connect((String) props.get("mail.smtp.host"),Integer.parseInt(props.getProperty("mail.smtp.port")), sendUserName, sendUserPass);
		transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
		transport.close();
		return true;
	}
}