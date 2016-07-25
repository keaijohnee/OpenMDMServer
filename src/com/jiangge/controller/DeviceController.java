package com.jiangge.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiangge.pojo.Command;
import com.jiangge.pojo.Device;
import com.jiangge.pojo.DeviceTemp;
import com.jiangge.service.CommandService;
import com.jiangge.service.DeviceService;
import com.jiangge.service.DeviceTempService;
import com.jiangge.utils.ConfigUtils;
import com.jiangge.utils.MDMTaskUtils;
import com.jiangge.utils.MdmUtils;
import com.jiangge.utils.PushUtils;
import com.jiangge.utils.StringUtils;
import com.jiangge.vo.InstallTypeEnum;

@Controller
@RequestMapping("/device")
public class DeviceController {
	
	private DeviceService deviceService;
	private CommandService commandService;
	private DeviceTempService deviceTempService;
	
	/**
     * 获取设备Code
     * @throws Exception
     */
	@SuppressWarnings("deprecation")
	@ResponseBody
    @RequestMapping("/getCode")
    public Map<String, String> getCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("-------------------getCode Start---------------");
        Map<String, String> map  = new HashMap<String, String>();
        String deviceFlag = request.getParameter("deviceFlag")==null?"":request.getParameter("deviceFlag").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        if(StringUtils.isEmpty(deviceFlag)){
        	map.put("state", "0");
            map.put("msg", "设备标签不能为空!");
        }else{
        	String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        	String url = ConfigUtils.getConfig("BASE_URL")+ConfigUtils.getConfig("DOWN_MOBILECONFIG");
            String mobileconfig = MessageFormat.format(url,uuid);
            map.put("mobileconfig", mobileconfig);
            /**保存数据**/
			DeviceTemp deviceTemp = new DeviceTemp();
			deviceTemp.setDeviceFlag(deviceFlag);
			deviceTemp.setCallBack(callBack);
			deviceTemp.setDeviceId(uuid);
			deviceTemp.setId(UUID.randomUUID().toString());
			deviceTempService.save(deviceTemp);
			/**生成描述文件**/
		    String configPath =  request.getRealPath("mdmtool");
		    String tempPath =  request.getRealPath("mdmtool/down");
			MDMTaskUtils.createMobileconfig(configPath, tempPath, uuid);
            System.out.println("-------------------getCode End---------------");
        }
        return map;
    }
	
	 /**
     * 下载设备控制描述文件功能
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @RequestMapping("/down")
    public void downConfig(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("-------------------Download MobileConfig File Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
        String tempPath =  request.getRealPath("mdmtool/down");
        String newPath =  tempPath + "/" + deviceId + "Signed.mobileconfig";
        response.setHeader("content-type", "application/x-apple-aspen-config");
        response.setCharacterEncoding("UTF-8");
        String configTitle = "MDMApp_"+deviceId;
        response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".mobileconfig");
        /**获取配置文件动态组装参数**/
        /**写入文件**/
        java.io.FileInputStream fis = new java.io.FileInputStream(newPath);
        java.io.OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        int i = 0;
        while ((i = fis.read(b)) > 0) { os.write(b, 0, i); }
        System.out.println("-------------------Download MobileConfig File End---------------");
        fis.close();
        os.flush();
        os.close();
    }
	
    /**
     * 设备锁屏功能
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
	@RequestMapping("/lock")
    public Map<String, String> deviceLock(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------Lock Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                 String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                 String pemPath = request.getRealPath("mdmtool") + pemFile;
                 int pushState = PushUtils.singleMDMPush(pemPath, device);
                 if (pushState == 1) {
                     Command command = new Command();
                     command.setCommand(MdmUtils.Lock);
                     command.setDeviceId(deviceId);
                     command.setDoIt("0");
                     command.setCallBack(callBack);
                     commandService.saveOrUpdate(command);
                     map.put("state", "1");
                     map.put("msg", "send lock command success!");
                     map.put("msg", "发送设备锁屏命令成功!");
                 } else {
                     map.put("state", "0");
                     map.put("msg", "发送设备锁屏命令失败!");
                 }
                 System.out.println("-------------------Lock End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }

    /**
     * 擦除设备数据功能
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/erase")
    public Map<String, String> deviceErase(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------Erase Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.Erase);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送擦除设备数据命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送擦除设备数据命令失败!");
                }
                System.out.println("-------------------Erase End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }

    /**
     * 获取设备信息
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/info")
    public Map<String, String> deviceInformation(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------Information Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.Info);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送查询设备信息命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送查询设备信息命令失败!");
                }
                System.out.println("-------------------Information End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }

    /**
     * 清除设备密码功能
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/clear")
    public Map<String, String> clearPasscode(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------ClearPasscode Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                 String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                 String pemPath = request.getRealPath("mdmtool") + pemFile;
                 int pushState = PushUtils.singleMDMPush(pemPath, device);
                 if (pushState == 1) {
                     Command command = new Command();
                     command.setCommand(MdmUtils.Clear);
                     command.setDeviceId(deviceId);
                     command.setDoIt("0");
                     command.setCallBack(callBack);
                     commandService.saveOrUpdate(command);
                     map.put("state", "1");
                     map.put("msg", "发送清除设备密码命令成功!");
                 } else {
                     map.put("state", "0");
                     map.put("msg", "发送清除设备密码命令失败!");
                 }
                 System.out.println("-------------------ClearPasscode End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
       
    }

    /**
     * 获取设备已经安装的app信息
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/apps")
    public Map<String, String> deviceInstalledApplicationList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------InstalledApplicationList Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String ctype = request.getParameter("ctype")==null?"":request.getParameter("ctype").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.Apps);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCtype(ctype);
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送获取APP应用列表命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送获取APP应用列表命令失败!");
                }
                System.out.println("-------------------InstalledApplicationList End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
    
    
    
    
    /**
     * 设备安装APP
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/install")
    public Map<String, String> deviceInstallApplication(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------InstallApplication Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String ctype = request.getParameter("ctype")==null?"":request.getParameter("ctype").trim();
        String cvalue = request.getParameter("cvalue")==null?"":request.getParameter("cvalue").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        String newctype = InstallTypeEnum.getCode(ctype);
        /**检查参数是否完整**/
        if(StringUtils.isNotEmpty(deviceId) && StringUtils.isNotEmpty(newctype) && StringUtils.isNotEmpty(cvalue)){
            Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
                map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.Install);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCtype(ctype);
                    command.setCvalue(cvalue);
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送安装APP应用命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送安装APP应用命令失败!");
                }
                System.out.println("-------------------InstallApplication End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
    
  
    
    /**
     * 设备卸载APP
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/remove")
    public Map<String, String> deviceRemoveApplication(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------RemoveApplication Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String identifier = request.getParameter("identifier")==null?"":request.getParameter("identifier").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        if(StringUtils.isNotEmpty(deviceId) && StringUtils.isNotEmpty(identifier)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.Remove);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCvalue(identifier);
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送卸载APP应用命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送卸载APP应用命令失败!");
                }
                System.out.println("-------------------RemoveApplication End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
    
    /**
     * 获取描述文件列表
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/ProfileList")
    public Map<String, String> getProfileList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------ProfileList Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.ProfileList);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送获取描述文件列表命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送获取描述文件列表命令失败!");
                }
                System.out.println("-------------------ProfileList End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
    
    /**
     * 获取预置描述文件列表
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/ProvisioningProfileList")
    public Map<String, String> getProvisioningProfileList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------ProvisioningProfileList Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.ProvisioningProfileList);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送获取预置描述文件列表命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送获取预置描述文件列表命令失败!");
                }
                System.out.println("-------------------ProvisioningProfileList End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
    
    /**
     * 获取证书文件列表
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/CertificateList")
    public Map<String, String> getCertificateList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
        System.out.println("-------------------CertificateList Start---------------");
        String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
        if(StringUtils.isNotEmpty(deviceId)){
        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
            if(null==device){
            	map.put("state", "0");
            	map.put("msg", "设备数据不存在!");
            }else{
                String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                String pemPath = request.getRealPath("mdmtool") + pemFile;
                int pushState = PushUtils.singleMDMPush(pemPath, device);
                if (pushState == 1) {
                    Command command = new Command();
                    command.setCommand(MdmUtils.CertificateList);
                    command.setDeviceId(deviceId);
                    command.setDoIt("0");
                    command.setCallBack(callBack);
                    commandService.saveOrUpdate(command);
                    map.put("state", "1");
                    map.put("msg", "发送获取证书文件命令成功!");
                } else {
                    map.put("state", "0");
                    map.put("msg", "发送获取证书文件命令失败!");
                }
                System.out.println("-------------------CertificateList End---------------");
            }
        }else{
        	map.put("state", "0");
            map.put("msg", "请求参数不完整!");
        }
        return map;
    }
	
	/****************************************************************/
	public DeviceService getDeviceService() {
		return deviceService;
	}
	@Resource
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	public CommandService getCommandService() {
		return commandService;
	}
	@Resource
	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
	
	public DeviceTempService getDeviceTempService() {
		return deviceTempService;
	}
	@Resource
	public void setDeviceTempService(DeviceTempService deviceTempService) {
		this.deviceTempService = deviceTempService;
	}
	
}
