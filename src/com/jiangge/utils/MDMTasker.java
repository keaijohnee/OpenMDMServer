package com.jiangge.utils;

import java.text.MessageFormat;
import java.util.Map;

public class MDMTasker {
	
	public void createMobileconfig(Map<String, String> taskParam){
		try {
			/**生成签名的MobileConfig配置文件的三个证书文件**/
	        System.out.println("----------------------生成证书文件等的路径 start---------------------");
	        String configPath =  taskParam.get("configPath");
	        String tempPath =  taskParam.get("tempPath");
	        String deviceId =  taskParam.get("deviceId");
	        String crtPath = configPath + ConfigUtils.getConfig("APNS_CRT");
	        String keyPath = configPath + ConfigUtils.getConfig("APNS_KEY");
	        String pemPath = configPath + ConfigUtils.getConfig("APNS_PEM");
	        System.out.println("----------------------生成证书文件等的路径 end---------------------");
	        /**创建未签名的文件和已签名的MobileConfig文件**/
	        System.out.println("----------------------生成未签名的mobileconfig文件 start---------------------");
	        String oldPath =  tempPath + "/" + deviceId + ".mobileconfig";
	        String newPath =  tempPath + "/" + deviceId + "Signed.mobileconfig";
	        String content = MdmUtils.readConfig(configPath).replaceAll("#deviceId#", deviceId);
	        boolean createSuccess = MdmUtils.createMobileConfigFile(oldPath,content);
	        System.out.println("----------------------生成未签名的mobileconfig文件 end---------------------");
	        /**签名和认证过程**/
	        if(createSuccess){
	            System.out.println("----------------------签名mobileconfig文件 start---------------------");
	            String oldCmd = "openssl smime -sign -in {0} -out {1} -signer {2} -inkey {3} -certfile {4} -outform der -nodetach";
	            String newCmd = MessageFormat.format(oldCmd,oldPath,newPath,crtPath,keyPath,pemPath);
	            System.out.println("OpenSSL：\n" + newCmd);
	            Runtime.getRuntime().exec(newCmd);
	            System.out.println("----------------------签名mobileconfig文件 end---------------------");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void syncDeviceInfo(Map<String, String> taskParam){
		try {
			Thread.sleep(2000);
			String url = ConfigUtils.getConfig("BASE_URL")+"/device/info.do";
			MDMHttpUtils.sendGet(url, taskParam);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void syncAppList(Map<String, String> taskParam){
		try {
			Thread.sleep(2000);
			String url = ConfigUtils.getConfig("BASE_URL")+"/device/apps.do";
			MDMHttpUtils.sendGet(url, taskParam);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCallBack(Map<String, String> taskParam){
		try {
			String url = taskParam.get("callBack")==null?"":taskParam.get("callBack");
			if(StringUtils.isNotEmpty(url)){
				taskParam.remove("callBack");
				MDMHttpUtils.sendGet(url, taskParam);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendCommandCallBack(Map<String, String> taskParam){
		try {
			String url = taskParam.get("callBack")==null?"":taskParam.get("callBack");
			if(StringUtils.isNotEmpty(url)){
				taskParam.remove("callBack");
				MDMHttpUtils.sendGet(url, taskParam);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
