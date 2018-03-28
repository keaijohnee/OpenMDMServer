package com.jiangge.utils;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Decoder.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.jiangge.pojo.Device;

/**
 * 和IOS的MDM相关的工具方法
 * @author jiang.li
 *
 */
@SuppressWarnings("all")
public class MdmUtils {

    /**定义pList中的相关key常量**/
    public static final String MessageType = "MessageType";
    public static final String Topic = "Topic";
    public static final String UDID = "UDID";
    public static final String PushMagic = "PushMagic";
    public static final String UnlockToken = "UnlockToken";
    public static final String Token = "Token";
    public static final String Identifier = "Identifier";

    /**定义checkIn两种请求路径**/
    public static final String Authenticate = "Authenticate";
    public static final String TokenUpdate = "TokenUpdate";
    public static final String CheckOut = "CheckOut";
    public static final String Repay = "Repay";




    /**定义pList字符串解析正则式**/
    public static final String DATA = "\\<data>(.*?)\\</data>";
    public static final String STRING = "\\<string>(.*?)\\</string>";
    public static final String KEY = "\\<key>(.*?)\\</key>";
    public static final String DICT = "\\<dict>(.*?)\\</dict>";
    public static final String ARRAY = "\\<array>(.*?)\\</array>";


    /**定义命令类型**/
    public static final String Lock = "DeviceLock";
    public static final String Erase = "EraseDevice";
    public static final String Info = "DeviceInformation";
    public static final String Apps = "InstalledApplicationList";
    public static final String Clear = "ClearPasscode";
    public static final String Online = "Online";
    public static final String Install = "InstallApplication";
    public static final String Remove = "RemoveApplication";
    public static final String ProfileList = "ProfileList";
    public static final String ProvisioningProfileList = "ProvisioningProfileList";
    public static final String CertificateList = "CertificateList";

    /**MDM请求服务器端状态**/
    public static final String Idle = "Idle";
    public static final String Acknowledged = "Acknowledged";
    public static final String CommandFormatError = "CommandFormatError";
    public static final String Error = "Error";
    public static final String NotNow = "NotNow";
    public static final String QueryResponses = "QueryResponses";
    public static final String InstalledApplicationList = "InstalledApplicationList";


    /**
     * 获取Information的pList文件Map数据
     * @param pList
     * @return
     */
    public static String parseUnlockToken(String pList){
        /**组装查询结果中重要的数据（一）**/
        int dataStart = pList.lastIndexOf("<data>")+6;
        int dataEnd = pList.lastIndexOf("</data>");
        String strBlank =  pList.substring(dataStart,dataEnd);
        return strBlank;
    }



    /**
     * 将通过TokenUpdate获取的原始Token转化成16进制新的Token
     * @param OriToken
     * @return
     * @throws IOException
     */
    public static String parseToken(String OriToken) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(OriToken);
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < decodedBytes.length; ++i) {
            buf.append(String.format("%02x", decodedBytes[i]));
        }
        String Token = buf.toString();
        return  Token;
    }


    /**
     * 获取Information的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseInformation(String pList){
        /**组装查询结果中重要的数据（一）**/
        String strBlank = replaceBlank(pList);
        strBlank =  strBlank.replace("<key>QueryResponses</key><dict>","");
        strBlank =  strBlank.replace("</dict><key>Status</key>","<key>Status</key>");
        strBlank =  strBlank.replace("<real>","<string>");
        strBlank =  strBlank.replace("</real>","</string>");
        strBlank =  strBlank.replace("<true/>","<string>true</string>");
        strBlank =  strBlank.replace("<false/>","<string>false</string>");
        Map<String, String> plistMap = new HashMap<String, String>();
        /**获取key、string列表数据**/
        List<String> keyList = getList(KEY,strBlank);
        List<String> stringList = getList(STRING,strBlank);
        /**组装数据称plistMap**/
        for(int i=0;i<stringList.size();i++){
            plistMap.put(keyList.get(i), stringList.get(i));
        }
        return plistMap;
    }


    /**
     * 获取InstalledApplicationList的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, Map<String, String>> parseInstalledApplicationList(String pList){
    	pList = pList.replace("<array/>","<array></array>").
    			replaceAll("<true/>", "<string>true</string>").
    			replaceAll("<false/>", "<string>false</string>");
        String strBlank  = replaceBlank(pList);
        /**(1)、组装APP列表数据**/
        String newBlank =  getList(ARRAY,strBlank).get(0);
        List<String> dictList = getList(DICT,newBlank);

        Map<String,Map<String,String>> dictMapList = new HashMap<String ,Map<String, String>>();
        Map<String,String> dictMap = null;
        for(String dict : dictList){
            dictMap = new HashMap<String,String>();
            dict = dict.replace("integer","string");
            List<String> keyList = getList(KEY,dict);
            List<String> stringList = getList(STRING,dict);
            for(int num=0;num<keyList.size();num++){
                dictMap.put(keyList.get(num),stringList.get(num));
            }
            dictMapList.put(dictMap.get(Identifier),dictMap);
        }
        /**(2)、组装其他数据**/
        String otherStr = strBlank.replace(newBlank,"").replace("<array>","").
        		replace("</array>","").
        		replace("<key>InstalledApplicationList</key>","");
        List<String> keyList = getList(KEY,otherStr);
        List<String> stringList = getList(STRING,otherStr);
        dictMap = new HashMap<String,String>();
        for(int num=0;num<keyList.size();num++){
            dictMap.put(keyList.get(num),stringList.get(num));
        }
        dictMapList.put(InstalledApplicationList,dictMap);
        String ss = JSON.toJSONString(dictMapList);
        System.out.println(ss);
        return  dictMapList;
    }


    /**
     * 获取Information的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseOtherInformation(String pList){
        /**组装查询结果中重要的数据（一）**/
        String strBlank = replaceBlank(pList);
        int startNum = strBlank.lastIndexOf("<key>QueryResponses</key>");
        int endNum = strBlank.indexOf("</dict>",1)+7;
        String nowStr = strBlank.substring(startNum,endNum);
        nowStr = strBlank.replace(nowStr,"");
        Map<String, String> plistMap = new HashMap<String, String>();
        /**获取key、string列表数据**/
        List<String> keyList = getList(KEY,nowStr);
        List<String> stringList = getList(STRING,nowStr);
        /**组装数据称plistMap**/
        for(int i=0;i<stringList.size();i++){
            plistMap.put(keyList.get(i), stringList.get(i));
        }
        /**组装查询结果中重要的数据（二）**/
        return plistMap;
    }


    /**
     * Java读取MobileConfig配置描述文件
     * @return
     * @throws IOException
     */
    public static String readConfig(String path) throws IOException {
        String config=  ConfigUtils.getConfig("APNS_CONFIG");
        InputStream fis = new FileInputStream(path + config);        
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }


    /**
     * 获取返回数据Command的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseCommand(String pList){
        String strBlank = replaceBlank(pList);
        Map<String, String> plistMap = new HashMap<String, String>();
        /**获取key、string列表数据**/
        List<String> keyList = getList(KEY,strBlank);
        List<String> stringList = getList(STRING,strBlank);
        /**组装数据称plistMap**/
        for(int i=0;i<stringList.size();i++){
            plistMap.put(keyList.get(i), stringList.get(i));
        }
        return plistMap;
    }

    /**
     * 发送命令的pList格式的模板文件
     * @return
     */
    public static String getCommandPList(String command,String commandUUID){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append("<key>RequestType</key>");
        backString.append("<string>");
        backString.append(command);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>");
        backString.append(commandUUID);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }
    
    /**
     * 发送命令的pList格式的模板文件
     * @return
     */
    public static String getAppsCommandPList(String command,String commandUUID){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append("<key>RequestType</key>");
        backString.append("<string>");
        backString.append(command);
        backString.append("</string>");
        backString.append("<key>ManagedAppsOnly</key>");
        backString.append("<true/>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>");
        backString.append(commandUUID);
        backString.append("</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }


    /**
     * 发送清除设备密码命令的pList格式的模板文件
     * @return
     */
    public static String getClearPassCodePList(String command,String commandUUID,Device mdm){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \n");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
        backString.append("<plist version=\"1.0\">\n");
        backString.append("<dict>\n");
        backString.append("<key>Command</key>\n" );
        backString.append("<dict>\n");
        backString.append("<key>RequestType</key>\n");
        backString.append("<string>"+command+"</string>\n");
        backString.append("<key>UnlockToken</key>\n");
        backString.append("<data>"+mdm.getUnlockToken()+"</data>\n");
        backString.append("</dict>\n");
        backString.append("<key>CommandUUID</key>\n");
        backString.append("<string>"+commandUUID+"</string>\n");
        backString.append("</dict>\n");
        backString.append("</plist>");
        return backString.toString();
    }
    
    /**
     * 发送安装APP的pList格式的模板文件
     * @return
     */
    public static String getInstallApplication(String command,String commandUUID,String ctype,String cvalue){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \n");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
        backString.append("<plist version=\"1.0\">\n");
        backString.append("<dict>\n");
        backString.append("<key>Command</key>\n" );
        backString.append("<dict>\n");
        backString.append("<key>RequestType</key>\n");
        backString.append("<string>"+command+"</string>\n");
        backString.append("<key>"+ctype+"</key>\n");
        if(ctype.equals("iTunesStoreID")){
        	backString.append("<integer>"+cvalue+"</integer>\n");
        }else{
        	backString.append("<string>"+cvalue+"</string>\n");
        }
        backString.append("<key>ManagementFlags</key>\n");
        backString.append("<integer>4</integer>\n");
        backString.append("</dict>\n");
        backString.append("<key>CommandUUID</key>\n");
        backString.append("<string>"+commandUUID+"</string>\n");
        backString.append("</dict>\n");
        backString.append("</plist>");
        return backString.toString();
    }
    
    /**
     * 发送移除APP的pList格式的模板文件
     * @return
     */
    public static String getRemoveApplication(String command,String commandUUID,String Identifier){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \n");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
        backString.append("<plist version=\"1.0\">\n");
        backString.append("<dict>\n");
        backString.append("<key>Command</key>\n" );
        backString.append("<dict>\n");
        backString.append("<key>RequestType</key>\n");
        backString.append("<string>"+command+"</string>\n");
        backString.append("<key>Identifier</key>\n");
        backString.append("<string>"+Identifier+"</string>\n");
        backString.append("</dict>\n");
        backString.append("<key>CommandUUID</key>\n");
        backString.append("<string>"+commandUUID+"</string>\n");
        backString.append("</dict>\n");
        backString.append("</plist>");
        
        
        return backString.toString();
    }

    /**
     * 发送命令的pList格式的模板文件（获取设备信息）
     * @return
     */
    public static String getCommandInfoPList(String command,String commandUUID){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>Command</key>");
        backString.append("<dict>");
        backString.append("<key>RequestType</key>");
        backString.append("<string>");
        backString.append(command);
        backString.append("</string>");
        backString.append("<key>Queries</key>");
        backString.append("<array>");
        backString.append("<string>ModelName</string>");
        backString.append("<string>Model</string>");
        backString.append("<string>BatteryLevel</string>");
        backString.append("<string>DeviceCapacity</string>");
        backString.append("<string>AvailableDeviceCapacity</string>");
        backString.append("<string>OSVersion</string>");
        backString.append("<string>SerialNumber</string>");
        backString.append("<string>IMEI</string>");
        backString.append("<string>ICCID</string>");
        backString.append("<string>MEID</string>");
        backString.append("<string>IsSupervised</string>");
        backString.append("<string>IsDeviceLocatorServiceEnabled</string>");
        backString.append("<string>IsActivationLockEnabled</string>");
        backString.append("<string>IsCloudBackupEnabled</string>");
        backString.append("<string>WiFiMAC</string>");
        backString.append("<string>BluetoothMAC</string>");
        backString.append("</array>");
        backString.append("</dict>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>");
        backString.append(commandUUID);
        backString.append("</string>");
        backString.append("</dict></plist>");
        return backString.toString();
    }

    /**
     * 空的pList格式的文件（用户checkIn认证时候的返回）
     * @return
     */
    public static String getBlankPList(){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }


    /**
     * 获取Authenticate的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseAuthenticate(String pList){
    	pList = pList.replace("<array/>","<array></array>").
    			replaceAll("<true/>", "<string>true</string>").
    			replaceAll("<false/>", "<string>false</string>");
        String strBlank = replaceBlank(pList);
        Map<String, String> plistMap = new HashMap<String, String>();
        /**获取key、string列表数据**/
        List<String> keyList = getList(KEY,strBlank);
        List<String> stringList = getList(STRING,strBlank);
        /**组装数据称plistMap**/
        for(int i=0;i<stringList.size();i++){
            plistMap.put(keyList.get(i), stringList.get(i));
        }
        return plistMap;
    }


    /**
     * 获取TokenUpdate的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseTokenUpdate(String pList){
    	pList = pList.replace("<array/>","<array></array>").
    			replaceAll("<true/>", "<string>true</string>").
    			replaceAll("<false/>", "<string>false</string>");
        String strBlank  = replaceBlank(pList);
        Map<String, String> plistMap = new HashMap<String, String>();
        /**获取key、string、data列表数据**/
        List<String> keyList = getList(KEY,strBlank);
        List<String> stringList = getList(STRING,strBlank);
        List<String> dataList = getList(DATA,strBlank);
        /**组装数据称plistMap**/
        int stringNum = 0;
        for(int i=0;i<keyList.size();i++){
            if(keyList.get(i).equals(Token)){
                plistMap.put(Token, dataList.get(0));
            }else if(keyList.get(i).equals(UnlockToken)){
                plistMap.put(UnlockToken, dataList.get(1));
            }else{
                plistMap.put(keyList.get(i), stringList.get(stringNum));stringNum++;
            }
        }
        return plistMap;
    }
    
    /**
     * 获取ProfileList的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseProfileList(String pList){
    	Map<String, String> profileList = new HashMap<String, String>();
    	pList = replaceBlank(pList);
    	pList = pList.replace("<array/>","<array></array>").
      			replaceAll("<true/>", "<string>true</string>").
      			replaceAll("<false/>", "<string>false</string>").
      			replaceAll("<array>", "").replaceAll("</array>", "").
      			replaceAll("<integer>", "<string>").replaceAll("</integer>", "</string>").
      			replaceAll("<dict>", "").replaceAll("</dict>", "").
      			replaceAll("</data><data>", "").
      			replaceAll("<data>", "<string>").replaceAll("</data>", "</string>").
      			replaceAll("<key>ProfileList</key>", "").replaceAll("\\*", "");
        String strBlank  = replaceBlank(pList);
        /**获取key、string列表数据**/
        List<String> topkeyList = getList(KEY,strBlank);
        List<String> topstringList = getList(STRING,strBlank);
        for(int i=0;i<topstringList.size();i++){
        	profileList.put(topkeyList.get(i), topstringList.get(i));
        }
        return profileList;
    }
    
    /**
     * 获取ProvisioningProfileList的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseProvisioningProfileList(String pList){
    	  Map<String, String> provisioningProfileList = new HashMap<String, String>();
    	  pList  = replaceBlank(pList);
    	  pList = pList.replace("<array/>","<array></array>").
      			replaceAll("<true/>", "<string>true</string>").
      			replaceAll("<false/>", "<string>false</string>").
      			replaceAll("<array>", "").replaceAll("</array>", "").
      			replaceAll("<dict>", "").replaceAll("</dict>", "").
      			replaceAll("</data><data>", "").
      			replaceAll("<data>", "<string>").replaceAll("</data>", "</string>").
      			replaceAll("<key>ProvisioningProfileList</key>", "").replaceAll("\\*", "");
    	  String strBlank  = replaceBlank(pList);
          /**获取key、string列表数据**/
          List<String> topkeyList = getList(KEY,strBlank);
          List<String> topstringList = getList(STRING,strBlank);
          for(int i=0;i<topstringList.size();i++){
        	  provisioningProfileList.put(topkeyList.get(i), topstringList.get(i));
          }
          return provisioningProfileList;
    }
    
    /**
     * 获取CertificateList的pList文件Map数据
     * @param pList
     * @return
     */
    public static Map<String, String> parseCertificateList(String pList){
    	Map<String, String> certificateList = new HashMap<String, String>();
    	pList  = replaceBlank(pList);
    	pList = pList.replaceAll("<array/>","<array></array>").
    			replaceAll("<true/>", "<string>true</string>").
    			replaceAll("<false/>", "<string>false</string>").
    			replaceAll("<array>", "").replaceAll("</array>", "").
    			replaceAll("<dict>", "").replaceAll("</dict>", "").
    			replaceAll("</data><data>", "").
    			replaceAll("<data>", "<string>").replaceAll("</data>", "</string>").
    			replaceAll("<key>CertificateList</key>", "").replaceAll("\\*", "");
    	String strBlank  = replaceBlank(pList);
        /**获取key、string列表数据**/
        List<String> topkeyList = getList(KEY,strBlank);
        List<String> topstringList = getList(STRING,strBlank);
        for(int i=0;i<topstringList.size();i++){
        	certificateList.put(topkeyList.get(i), topstringList.get(i));
        }
        return certificateList;
    }
    

    /**
     * 获取字符串列表数据
     * @param pattern
     * @param pList
     * @return
     */
    public static List<String> getList(String pattern,String pList){
        /**获取data列表数据**/
        List<String> dataList = new ArrayList<String>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pList);
        while(m.find()) {
            dataList.add(m.group(1));
        }
        return dataList;
    }


    /**
     * java去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String strBlank = "";
        if (str!=null) {
        	str = str.replace("<false/>", "<string>false</string>");
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            strBlank = m.replaceAll("");
        }
        return strBlank;
    }
    
    /**
     * 创建MobileConfig配置文件
     * @param filePath
     * @param content
     * @return
     * @throws Exception
     */
    public static boolean createMobileConfigFile(String filePath,String content)throws Exception {
        boolean createSuccess = false;
        File filename = new File(filePath);
        RandomAccessFile mm = null;
        try {
        	/**去掉XML头部的BOM Start**/
			if(null != content && !"".equals(content)){ if(content.indexOf("<") != -1 && content.lastIndexOf(">") != -1 && content.lastIndexOf(">") > content.indexOf("<"))    content = content.substring(content.indexOf("<"), content.lastIndexOf(">") + 1);  }
			content = content.trim().replaceFirst("^([\\W]+)<","<");
			Matcher junkMatcher = (Pattern.compile("^([\\W]+)<")).matcher(content.trim());
			content = junkMatcher.replaceFirst("<");
			/**去掉XML头部的BOM End**/
            mm = new RandomAccessFile(filename,"rw");
            mm.write(content.getBytes("UTF-8"));
            mm.close();
            createSuccess = true;
        } catch (IOException e1) {
            System.out.println("出错了："+e1.getMessage());
        }
        return createSuccess;
    }
    
    /**
     * 将inputStream转化成为String
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException{ 
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    	int i = -1; 
    	while((i=is.read())!=-1){ 
    	    baos.write(i); 
    	} 
    	byte[] lens = baos.toByteArray();
    	String result = new String(lens,"UTF-8");
    	return result;
    } 
    
    
    public static String getXML(){
        StringBuffer backString = new StringBuffer();
        backString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        backString.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"");
        backString.append("\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        backString.append("<plist version=\"1.0\">");
        backString.append("<dict>");
        backString.append("<key>CertificateList</key>");
        backString.append("<array>");
        backString.append("<dict>");
        backString.append("</dict>");
        backString.append("</array>");
        backString.append("<key>CommandUUID</key>");
        backString.append("<string>6461cb57-4521-4db4-ac30-8c035e5732c8</string>");
        backString.append("<key>Status</key>");
        backString.append("<string>Acknowledged</string>");
        backString.append("<key>UDID</key>");
        backString.append("<string>e398d42c11d0dee935be9c3c39d4b777a6340bc5</string>");
        backString.append("</dict>");
        backString.append("</plist>");
        return backString.toString();
    }

    public static void main(String[] ss){
    	String strBlank = MdmUtils.getXML();
    	Map<String, String> certificateList = parseCertificateList(strBlank);
    	for(String key : certificateList.keySet()){
    		System.out.println(key + ":" + certificateList.get(key));
    	}
    }

}
