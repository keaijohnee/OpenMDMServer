package com.jiangge.apns4j.demo;

import java.io.InputStream;
import java.util.List;

import com.jiangge.apns4j.IApnsService;
import com.jiangge.apns4j.impl.ApnsServiceImpl;
import com.jiangge.apns4j.model.ApnsConfig;
import com.jiangge.apns4j.model.Feedback;
import com.jiangge.apns4j.model.Payload;

/**
 * @author RamosLi
 *
 */
public class Apns4jDemo {
	private static IApnsService apnsService;
	
	private static IApnsService getApnsService() {
		if (apnsService == null) {
			ApnsConfig config = new ApnsConfig();
			InputStream is = Apns4jDemo.class.getClassLoader().getResourceAsStream("MDMPush.p12");
			config.setKeyStore(is);
			config.setDevEnv(false);
			config.setPassword("abcd1234");
			config.setPoolSize(3);
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}
	
	public static void main(String[] args) {
		IApnsService service = getApnsService();
		String token = "5b6ed4a86313f66a91d73512278fc078a0ded3b41835a265c0a9a100eca1fa50";
		Payload payload = new Payload();
		payload.addParam("mdm", "07FC052F-6ED7-41C8-9824-81572CA58F0A");
		service.sendNotification(token, payload);
		// get feedback
		List<Feedback> list = service.getFeedbacks();
		if (list != null && list.size() > 0) {
			for (Feedback feedback : list) {
				System.out.println(feedback.getDate() + " " + feedback.getToken());
			}
		}
	}
}
