package com.jiangge.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jiangge.pojo.Apps;
import com.jiangge.pojo.Command;
import com.jiangge.pojo.Device;
import com.jiangge.pojo.DeviceTemp;
import com.jiangge.pojo.Profile;
import com.jiangge.service.AppsService;
import com.jiangge.service.CommandService;
import com.jiangge.service.DeviceService;
import com.jiangge.service.DeviceTempService;
import com.jiangge.service.ProfileService;
import com.jiangge.utils.ConfigUtils;
import com.jiangge.utils.MDMTaskUtils;
import com.jiangge.utils.MdmUtils;
import com.jiangge.utils.PushUtils;
import com.jiangge.utils.StringUtils;
import com.jiangge.vo.HttpConst;
import com.jiangge.vo.InstallTypeEnum;
import com.jiangge.vo.MobileBean;
import com.jiangge.vo.PageBean;

@SuppressWarnings("all")
@Controller
@RequestMapping("/api")
public class APIController {
	
	private DeviceService deviceService;
	private CommandService commandService;
	private AppsService appsService;
	private ProfileService profileService;
	private DeviceTempService deviceTempService;
	
	/**
	 * 获取mobileconfig下载地址
	 */
	@ResponseBody
	@RequestMapping("/getMobileconfig")
	public MobileBean getMobileconfig(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
	    System.out.println("-------------------getMobileconfig Start---------------");
        String deviceFlag = request.getParameter("deviceFlag")==null?"":request.getParameter("deviceFlag").trim();
        String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
		try{
			if(StringUtils.isEmpty(deviceFlag) || StringUtils.isEmpty(callBack)){
	        	bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc("设参数不完整!");
				bean.setData("");
	        }else{
	        	String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	        	String url = ConfigUtils.getConfig("BASE_URL")+ConfigUtils.getConfig("DOWN_MOBILECONFIG");
	            String mobileconfig = MessageFormat.format(url,uuid);
	            bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				Map<String,String> params = new HashMap<String,String>();
				params.put("url", mobileconfig);
				params.put("deviceId", uuid);
				bean.setData(params);
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
	        }
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
		System.out.println("-------------------getMobileconfig End---------------");
	    return bean;  
	}
	
	/**
     * 获取设备的ProfileList
     */
    @ResponseBody
	@RequestMapping("/profileList")
    public MobileBean profileList(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                         bean.setStatus(HttpConst.HTTP_STATUS_200);
         			 	 bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
         				 bean.setData("发送获取设备的ProfileList命令成功!");
                     } else {
                         bean.setStatus(HttpConst.HTTP_STATUS_403);
              			 bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
              		     bean.setData("发送获取设备的ProfileList命令失败!");
                     }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
    /**
     * 获取设备的ProvisioningProfileList
     */
    @ResponseBody
	@RequestMapping("/provisioningProfileList")
    public MobileBean provisioningProfileList(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                         bean.setStatus(HttpConst.HTTP_STATUS_200);
         			 	 bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
         				 bean.setData("发送获取设备的ProvisioningProfileList命令成功!");
                     } else {
                         bean.setStatus(HttpConst.HTTP_STATUS_403);
              			 bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
              		     bean.setData("发送获取设备的ProvisioningProfileList命令失败!");
                     }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
    /**
     * 获取设备的CertificateList
     */
    @ResponseBody
	@RequestMapping("/certificateList")
    public MobileBean certificateList(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                         bean.setStatus(HttpConst.HTTP_STATUS_200);
         			 	 bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
         				 bean.setData("发送获取设备的CertificateList命令成功!");
                     } else {
                         bean.setStatus(HttpConst.HTTP_STATUS_403);
              			 bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
              		     bean.setData("发送获取设备的CertificateList命令失败!");
                     }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
	
	 /**
     * 设备锁屏功能
     */
    @ResponseBody
	@RequestMapping("/deviceLock")
    public MobileBean deviceLock(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                         bean.setStatus(HttpConst.HTTP_STATUS_200);
         			 	 bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
         				 bean.setData("发送设备锁屏命令成功!");
                     } else {
                         bean.setStatus(HttpConst.HTTP_STATUS_403);
              			 bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
              		     bean.setData("发送设备锁屏命令失败!");
                     }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }

    /**
     * 擦除设备数据功能
     */
    @ResponseBody
    @RequestMapping("/deviceErase")
    public MobileBean deviceErase(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                        bean.setStatus(HttpConst.HTTP_STATUS_200);
        			 	bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送擦除设备数据命令成功!");
                    } else {
                        bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             		    bean.setData("发送擦除设备数据命令失败!");
                    }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }

    /**
     * 获取设备信息
     */
    @ResponseBody
    @RequestMapping("/getInfo")
    public MobileBean getInfo(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
    		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
    		String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
	        /**检查参数是否完整**/
	        if(StringUtils.isNotEmpty(deviceId)){
	        	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
	            if(null==device){
	            	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
	                    bean.setStatus(HttpConst.HTTP_STATUS_200);
        			 	bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送查询设备信息命令成功!");
	                } else {
	                    bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             		    bean.setData("发送查询设备信息命令失败!");
	                }
	            }
	        }else{
	        	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
	        }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }

    /**
     * 清除设备密码功能
     */
    @ResponseBody
    @RequestMapping("/clearPasscode")
    public MobileBean clearPasscode(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                         bean.setStatus(HttpConst.HTTP_STATUS_200);
         			 	 bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
         				 bean.setData("发送清除设备密码命令成功!");
                     } else {
                         bean.setStatus(HttpConst.HTTP_STATUS_403);
              			 bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
              			 bean.setData("发送清除设备密码命令失败!");
                     }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }

    /**
     * 获取设备已经安装的app信息
     */
    @ResponseBody
    @RequestMapping("/getApps")
    public MobileBean getApps(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
                }else{
                    String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                    String pemPath = request.getRealPath("mdmtool") + pemFile;
                    int pushState = PushUtils.singleMDMPush(pemPath, device);
                    if (pushState == 1) {
                        Command command = new Command();
                        command.setCommand(MdmUtils.Apps);
                        command.setDeviceId(deviceId);
                        command.setDoIt("0");
                        command.setCtype("All");
                        command.setCallBack(callBack);
                        commandService.saveOrUpdate(command);
                        bean.setStatus(HttpConst.HTTP_STATUS_200);
        				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送获取APP应用列表命令成功!");
                    } else {
                        bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             			bean.setData("发送获取APP应用列表命令失败!");
                    }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
    /**
     * 获取设备已经安装的可管控的app信息
     */
    @ResponseBody
    @RequestMapping("/getManagedApps")
    public MobileBean getManagedApps(HttpServletRequest request,HttpServletResponse response){
    	MobileBean bean = new MobileBean();
    	try{
            String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null==device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
                }else{
                    String pemFile = ConfigUtils.getConfig("APNS_P12MDM");
                    String pemPath = request.getRealPath("mdmtool") + pemFile;
                    int pushState = PushUtils.singleMDMPush(pemPath, device);
                    if (pushState == 1) {
                        Command command = new Command();
                        command.setCommand(MdmUtils.Apps);
                        command.setDeviceId(deviceId);
                        command.setDoIt("0");
                        command.setCtype("ManagedAppsOnly");
                        command.setCallBack(callBack);
                        commandService.saveOrUpdate(command);
                        bean.setStatus(HttpConst.HTTP_STATUS_200);
        				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送获取APP应用列表命令成功!");
                    } else {
                        bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             			bean.setData("发送获取APP应用列表命令失败!");
                    }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
    
    
    /**
     * 设备安装APP
     */
    @ResponseBody
    @RequestMapping("/installApp")
    public MobileBean installApp(HttpServletRequest request,HttpServletResponse response) {
    	MobileBean bean = new MobileBean();
    	try{
    		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String ctype = request.getParameter("ctype")==null?"":request.getParameter("ctype").trim();
            String cvalue = request.getParameter("cvalue")==null?"":request.getParameter("cvalue").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            String newctype = InstallTypeEnum.getCode(ctype);
            /**检查参数是否完整**/
            if(StringUtils.isNotEmpty(deviceId) && StringUtils.isNotEmpty(newctype) && StringUtils.isNotEmpty(cvalue)){
                Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null == device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                        bean.setStatus(HttpConst.HTTP_STATUS_200);
        				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送安装APP应用命令成功!");
                    } else {
                        bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             			bean.setData("发送安装APP应用命令失败!");
                    }
                }
            }else{
            	bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
  
    
    /**
     * 设备卸载APP
     */
    @ResponseBody
    @RequestMapping("/removeApp")
    public MobileBean removeApp(HttpServletRequest request,HttpServletResponse response) {
    	MobileBean bean = new MobileBean();
    	try{
    		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
            String identifier = request.getParameter("identifier")==null?"":request.getParameter("identifier").trim();
            String callBack = request.getParameter("callBack")==null?"":request.getParameter("callBack").trim();
            if(StringUtils.isNotEmpty(deviceId) && StringUtils.isNotEmpty(identifier)){
            	Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
                if(null == device){
                	bean.setStatus(HttpConst.HTTP_STATUS_403);
         			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
         			bean.setData("设备数据不存在!");
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
                        bean.setStatus(HttpConst.HTTP_STATUS_200);
        				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
        				bean.setData("发送卸载APP应用命令成功!");
                    } else {
                        bean.setStatus(HttpConst.HTTP_STATUS_403);
             			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
             			bean.setData("发送卸载APP应用命令失败!");
                    }
                }
            }else{
                bean.setStatus(HttpConst.HTTP_STATUS_403);
    			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
    			bean.setData("请求参数不完整!");
            }
    	}catch(Exception e){
    		e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
    	}
        return bean;
    }
    
    
    /********************************以下是获取设备本地数据*********************************/
    
    /**
	 * 获取消息日志详情
	 */
	@ResponseBody
	@RequestMapping("/getLogInfo")
	public MobileBean getLogInfo(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String id = request.getParameter("id")==null?"":request.getParameter("id");
			if(StringUtils.isNotEmpty(id)){
				Command command = commandService.getCommandById(id);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(command);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("id不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 删除消息日志
	 */
	@ResponseBody
	@RequestMapping("/deleteLog")
	public MobileBean deleteLog(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String id = request.getParameter("id")==null?"":request.getParameter("id");
			if(StringUtils.isNotEmpty(id)){
				commandService.deleteCommandById(id);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(null);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("id不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 分页获取命令日志列表
	 */
	@ResponseBody
	@RequestMapping("/getLogList")
	public MobileBean getLogList(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String hql = "from Command order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Command> list = commandService.pageQuery(hql, pageIndex, pageSize);
			int count = commandService.getCount("select count(A.id) as number from Command A");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,list);
			bean.setStatus(HttpConst.HTTP_STATUS_200);
			bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
			bean.setData(pageList);
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 根据设备ID获取命令日志列表
	 */
	@ResponseBody
	@RequestMapping("/getLogListByDeviceId")
	public MobileBean getLogListByDeviceId(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String hql = "from Command where deviceId = ? order by createTime desc";
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId").trim();
			List<Command> list = commandService.getAllCommandByHql(hql, deviceId);
			bean.setStatus(HttpConst.HTTP_STATUS_200);
			bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
			bean.setData(list);
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 更改设备标签
	 */
	@ResponseBody
	@RequestMapping("/updateDeviceFlag")
	public MobileBean updateDeviceFlag(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			String deviceFlag = request.getParameter("deviceFlag")==null?"":request.getParameter("deviceFlag");
			if(StringUtils.isNotEmpty(deviceId)){
				String hql = "from Device where deviceId = ?";
				Device device = deviceService.getDeviceByHql(hql, deviceId);
				device.setDeviceFlag(deviceFlag);
				deviceService.saveOrUpdate(device);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(null);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId或者deviceFlag不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
		return bean;
		
	}
	
	/**
	 * 设备移除
	 */
	@ResponseBody
	@RequestMapping("/removeDevice")
	public MobileBean removeDevice(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			if(StringUtils.isNotEmpty(deviceId)){
				deviceService.deleteDeviceByDeviceId(deviceId);
				appsService.deleteAppsByDeviceId(deviceId);
				commandService.deleteCommandByDeviceId(deviceId);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(null);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
		return bean;
	}
	
	
	/**
	 * 设备详情
	 */
	@ResponseBody
	@RequestMapping("/getDeviceInfo")
	public MobileBean getDeviceInfo(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			if(StringUtils.isNotEmpty(deviceId)){
				Map<String,Object> values = new HashMap<String,Object>();
				String hql = "from Device where deviceId = ?";
				Device device = deviceService.getDeviceByHql(hql, deviceId);
				List<Apps> appList = appsService.getAppsListByHql("from Apps where deviceId = '"+deviceId+"' order by createTime desc");
				values.put("device", device);
				values.put("appList", appList);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(values);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 分页获取设备数据列表
	 */
	@ResponseBody
	@RequestMapping("/getDeviceList")
	public MobileBean getDeviceList(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String hql = "from Device order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Device> list = deviceService.pageQuery(hql, pageIndex, pageSize);
			int count = deviceService.getCount("select count(A.id) as number from Device A");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,list);
			bean.setStatus(HttpConst.HTTP_STATUS_200);
			bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
			bean.setData(pageList);
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 设备的ProfileList
	 */
	@ResponseBody
	@RequestMapping("/getDeviceProfileList")
	public MobileBean getDeviceProfileList(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			if(StringUtils.isNotEmpty(deviceId)){
				String hql = "from Profile where deviceId = ? and ctype = ?";
				Profile profile = profileService.getProfileByHql(hql, deviceId , MdmUtils.ProfileList);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(profile);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 设备的ProvisioningProfileList
	 */
	@ResponseBody
	@RequestMapping("/getDeviceProvisioningProfileList")
	public MobileBean getDeviceProvisioningProfileList(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			if(StringUtils.isNotEmpty(deviceId)){
				String hql = "from Profile where deviceId = ? and ctype = ?";
				Profile profile = profileService.getProfileByHql(hql, deviceId , MdmUtils.ProvisioningProfileList);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(profile);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
	/**
	 * 设备的CertificateList
	 */
	@ResponseBody
	@RequestMapping("/getDeviceCertificateList")
	public MobileBean getDeviceCertificateList(HttpServletRequest request,HttpServletResponse response){
		MobileBean bean = new MobileBean();
		try{
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			if(StringUtils.isNotEmpty(deviceId)){
				String hql = "from Profile where deviceId = ? and ctype = ?";
				Profile profile = profileService.getProfileByHql(hql, deviceId , MdmUtils.CertificateList);
				bean.setStatus(HttpConst.HTTP_STATUS_200);
				bean.setDesc(HttpConst.HTTP_STATUS_SUCCESS);
				bean.setData(profile);
			}else{
				bean.setStatus(HttpConst.HTTP_STATUS_403);
				bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
				bean.setData("deviceId不能为空");
			}
		}catch(Exception e){
			e.printStackTrace();
			bean.setStatus(HttpConst.HTTP_STATUS_403);
			bean.setDesc(HttpConst.HTTP_STATUS_FAILURE);
			bean.setData("出错了:"+e.getMessage());
		}
	    return bean;  
	}
	
    /****************************************************************/
	public DeviceService getDeviceService() {
		return deviceService;
	}
	@Resource
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	public DeviceTempService getDeviceTempService() {
		return deviceTempService;
	}
	@Resource
	public void setDeviceTempService(DeviceTempService deviceTempService) {
		this.deviceTempService = deviceTempService;
	}
	
	public CommandService getCommandService() {
		return commandService;
	}
	@Resource
	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
	public AppsService getAppsService() {
		return appsService;
	}
	@Resource
	public void setAppsService(AppsService appsService) {
		this.appsService = appsService;
	}
	public ProfileService getProfileService() {
		return profileService;
	}
	@Resource
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}

}
