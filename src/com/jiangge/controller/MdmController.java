package com.jiangge.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
import com.jiangge.utils.MDMTaskUtils;
import com.jiangge.utils.MdmUtils;

@Controller
@RequestMapping("/mdm")
public class MdmController {
	
	private DeviceService deviceService;
	private CommandService commandService;
	private AppsService appsService;
	private ProfileService profileService;
	private DeviceTempService deviceTempService;
	
	/**
     * 设备认证和注册功能
     * @throws Exception
     */
	@RequestMapping(value="/checkin",method=RequestMethod.PUT)
    public void checkIn(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
        /**获取当期设备的编号和设备信息**/
        Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
        String info = MdmUtils.inputStream2String(request.getInputStream());
        /**Device认证方法调用、Device回传Token方法调用**/
        if (info.toString().contains(MdmUtils.Authenticate)) {
            System.out.println("-------------------Authenticate start---------------");
            System.out.println("Device->Server Authenticate:\n"+info.toString());
            /**保存返回的Token、PushMagic数据**/
            Map<String, String> plistMap = MdmUtils.parseAuthenticate(info.toString());
            String Topic = plistMap.get(MdmUtils.Topic);
            String UDID = plistMap.get(MdmUtils.UDID);
            device = deviceService.getDeviceByHql("from Device where udid = ? ", UDID);
            if (device == null) { device = new Device();}
            device.setDeviceId(deviceId);
            device.setUdid(UDID);
            device.setTopic(Topic);
            device.setControl("1");
            /**查询初始数据**/
            DeviceTemp deviceTemp = deviceTempService.getDeviceTempByHql("from DeviceTemp where deviceId = ?", deviceId);
            if(null != deviceTemp){
            	device.setDeviceFlag(deviceTemp.getDeviceFlag());
            	/**异步通知第三方更新**/
            	MDMTaskUtils.sendCallBack(deviceTemp.getCallBack(), deviceId, "1");
            }
            deviceService.saveOrUpdate(device);
            /**返回一个空的pList格式的文件**/
            String blankPList = MdmUtils.getBlankPList();
            System.out.println("Server->Device:\n"+blankPList);
            response.setHeader("content-type", "application/xml;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String configTitle = "MDMApp_EraseDevice";
            response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
            PrintWriter sos = response.getWriter();
            System.out.println("-------------------Authenticate end---------------");
            sos.write(blankPList);
            sos.flush();
            sos.close();
        } else if (info.toString().contains(MdmUtils.TokenUpdate)) {
            System.out.println("-------------------TokenUpdate start---------------");
            System.out.println("Device->Server TokenUpdate:\n"+info.toString());
            /**保存返回的数据**/
            Map<String, String> plistMap = MdmUtils.parseTokenUpdate(info.toString());
            String UnlockToken = MdmUtils.parseUnlockToken(info.toString());
            String UDID = plistMap.get(MdmUtils.UDID);
            String Topic = plistMap.get(MdmUtils.Topic);
            String OriToken = plistMap.get(MdmUtils.Token);
            String PushMagic = plistMap.get(MdmUtils.PushMagic);
            device = deviceService.getDeviceByHql("from Device where udid = ? ", UDID);
            if (device == null) { device = new Device(); }
            device.setDeviceId(deviceId);
            device.setUdid(UDID);
            device.setTopic(Topic);
            device.setControl("2");
            device.setUnlockToken(UnlockToken);
            /**组装新的Token数据**/
            String Token = MdmUtils.parseToken(OriToken);
            device.setToken(Token);
            device.setPushMagic(PushMagic);
            /**查询初始数据**/
            DeviceTemp deviceTemp = deviceTempService.getDeviceTempByHql("from DeviceTemp where deviceId = ?", deviceId);
            if(null != deviceTemp){
            	device.setDeviceFlag(deviceTemp.getDeviceFlag());
            	/**异步通知第三方更新**/
            	MDMTaskUtils.sendCallBack(deviceTemp.getCallBack(), deviceId, "2");
            }
            deviceService.saveOrUpdate(device);
            /**异步加设备信息**/
            MDMTaskUtils.syncDeviceInfo(deviceId);
            /**空返回**/
            System.out.println("Server->Device:\n The HTTP state 200, the content is empty");
            System.out.println("-------------------TokenUpdate end---------------");
            response.setContentType("text/plain;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (info.toString().contains(MdmUtils.CheckOut)) {
            System.out.println("Device->Server CheckOut:\n"+info.toString());
            System.out.println("-------------------CheckOut start---------------");
            if (device != null) {
            	device.setControl("-1");
            	/**查询初始数据**/
                DeviceTemp deviceTemp = deviceTempService.getDeviceTempByHql("from DeviceTemp where deviceId = ?", deviceId);
                if(null != deviceTemp){
                	/**异步通知第三方更新**/
                	MDMTaskUtils.sendCallBack(deviceTemp.getCallBack(), deviceId, "-1");
                }
                deviceService.saveOrUpdate(device);
            }
            System.out.println("Server->Device:\n Don't need to return");
            System.out.println("-------------------CheckOut end---------------");
        }
    }


    /**
     * 操作状态回执
     * @throws Exception
     */
    @RequestMapping(value="/server",method=RequestMethod.PUT)
    public void serverUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
    	System.out.println("deviceId:"+deviceId);
        /**获取当期设备的编号**/
        Device device = deviceService.getDeviceByHql("from Device where deviceId = ? ", deviceId);
        String info = MdmUtils.inputStream2String(request.getInputStream());
        /**设备空闲状态,可以发送相关命令**/
        if (info.contains(MdmUtils.Idle)) {
            /**执行命令**/
            Command command = (Command)commandService.getCommandByHql("from Command where deviceId=? and doIt=? order by createTime asc", deviceId, "0");
            if (command != null) {
                if (command.getCommand().equals(MdmUtils.Lock)) {
                    System.out.println("-------------------DeviceLock Start---------------");
                    /**发送锁屏命令**/
                    String commandString = MdmUtils.getCommandPList(MdmUtils.Lock, command.getId());
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    System.out.println("Server->Device Lock:\n"+commandString);
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_DeviceLock";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------DeviceLock End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Erase)) {
                    System.out.println("-------------------EraseDevice Start---------------");
                    /**发送清除谁命令**/
                    String commandString = MdmUtils.getCommandPList(MdmUtils.Erase, command.getId());
                    System.out.println("Server->Device Erase:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_EraseDevice";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------EraseDevice End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Info)) {
                    System.out.println("-------------------DeviceInformation Start---------------");
                    /**发送获取设备信息命令**/
                    String commandString = MdmUtils.getCommandInfoPList(MdmUtils.Info, command.getId());
                    System.out.println("Server->Device DeviceInformation:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_DeviceInformation";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------DeviceInformation End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Apps)) {
                    System.out.println("-------------------InstalledApplicationList Start---------------");
                    /**发送获取设备信息命令**/
                    String commandString = "";
                    if(command.getCtype().equals("ManagedAppsOnly")){
                    	commandString = MdmUtils.getAppsCommandPList(MdmUtils.Apps, command.getId());
                    }else{
                    	commandString = MdmUtils.getCommandPList(MdmUtils.Apps, command.getId());
                    }
                    System.out.println("Server->Device InstalledApplicationList:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_InstalledApplicationList";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------InstalledApplicationList End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Clear)) {
                    System.out.println("-------------------ClearPasscode Start---------------");
                    /**发送清除设备密码命令**/
                    String commandString = MdmUtils.getClearPassCodePList(MdmUtils.Clear, command.getId(),device);
                    System.out.println("Server->Device ClearPasscode:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_ClearPasscode";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------ClearPasscode End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Install)) {
                    System.out.println("-------------------InstallApplication Start---------------");
                    /**发送安装APP命令**/
                    String commandString = MdmUtils.getInstallApplication(MdmUtils.Install, command.getId(), command.getCtype(), command.getCvalue());
                    System.out.println("Server->Device InstallApplication:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_InstallApplication";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------InstallApplication End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                } else if (command.getCommand().equals(MdmUtils.Remove)) {
                    System.out.println("-------------------RemoveApplication Start---------------");
                    /**发送卸载APP命令**/
                    String commandString = MdmUtils.getRemoveApplication(MdmUtils.Remove, command.getId(), command.getCvalue());
                    System.out.println("Server->Device RemoveApplication:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_RemoveApplication";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------RemoveApplication End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                }else if (command.getCommand().equals(MdmUtils.ProfileList)) {
                    System.out.println("-------------------ProfileList Start---------------");
                    /**发送获取描述文件列表命令**/
                    String commandString = MdmUtils.getCommandPList(MdmUtils.ProfileList, command.getId());
                    System.out.println("Server->Device ProfileList:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_ProfileList";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------ProfileList End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                }else if (command.getCommand().equals(MdmUtils.ProvisioningProfileList)) {
                    System.out.println("-------------------ProvisioningProfileList Start---------------");
                    /**发送获取预置描述文件列表命令**/
                    String commandString = MdmUtils.getCommandPList(MdmUtils.ProvisioningProfileList, command.getId());
                    System.out.println("Server->Device ProvisioningProfileList:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_ProvisioningProfileList";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------ProvisioningProfileList End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                }else if (command.getCommand().equals(MdmUtils.CertificateList)) {
                    System.out.println("-------------------CertificateList Start---------------");
                    /**发送获取证书文件命令**/
                    String commandString = MdmUtils.getCommandPList(MdmUtils.CertificateList, command.getId());
                    System.out.println("Server->Device CertificateList:\n"+commandString);
                    command.setDoIt("1");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "1", command.getId());
                    response.setHeader("content-type", "application/xml;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    String configTitle = "MDMApp_CertificateList";
                    response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".plist");
                    PrintWriter sos = response.getWriter();
                    System.out.println("-------------------CertificateList End---------------");
                    sos.write(commandString);
                    sos.flush();
                    sos.close();
                }
            }
        } else if (info.contains(MdmUtils.Acknowledged)) {
            if (info.contains(MdmUtils.QueryResponses)) {
                System.out.println("-------------------DeviceInformation Start---------------");
                System.out.println("Device->Server DeviceInformation:\n"+info.toString());
                Map<String, String> plistMap = MdmUtils.parseInformation(info);
                String CommandUUID = plistMap.get("CommandUUID");
                System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if (command != null) {
                    command.setResult(MdmUtils.Acknowledged);
                    command.setDoIt("2");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                /**更新服务器设备状态**/
                device.setModelName(plistMap.get("ModelName"));
                device.setModel(plistMap.get("Model"));
                device.setBatteryLevel(plistMap.get("BatteryLevel"));
                device.setDeviceCapacity(plistMap.get("DeviceCapacity"));
                device.setAvailableDeviceCapacity(plistMap.get("AvailableDeviceCapacity"));
                device.setoSVersion(plistMap.get("OSVersion"));
                /**客户新增**/
                device.setSerialNumber(plistMap.get("SerialNumber"));
                device.setImei(plistMap.get("IMEI"));
                device.setIccid(plistMap.get("ICCID"));
                device.setMeid(plistMap.get("MEID"));
                device.setIsSupervised(plistMap.get("IsSupervised"));
                device.setIsDeviceLocatorServiceEnabled(plistMap.get("IsDeviceLocatorServiceEnabled"));
                device.setIsActivationLockEnabled(plistMap.get("IsActivationLockEnabled"));
                device.setIsCloudBackupEnabled(plistMap.get("IsCloudBackupEnabled"));
                device.setWifimac(plistMap.get("WiFiMAC"));
                device.setBluetoothMAC(plistMap.get("BluetoothMAC"));
                device.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                deviceService.saveOrUpdate(device);
                System.out.println("-------------------DeviceInformation End---------------");
            }else if(info.contains(MdmUtils.InstalledApplicationList)) {
                System.out.println("-------------------InstalledApplicationList Start---------------");
                System.out.println("Device->Server InstalledApplicationList:\n"+info.toString());
                Map<String, Map<String, String>> plistMap = MdmUtils.parseInstalledApplicationList(info);
                String CommandUUID = plistMap.get(MdmUtils.InstalledApplicationList).get("CommandUUID");
                System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if(command.getCtype().equals("ManagedAppsOnly")){
                	Apps app = null;
                    for (String key : plistMap.keySet()) {
                        if(!MdmUtils.InstalledApplicationList.equals(key)){
                            Map<String, String> map =  plistMap.get(key);
                            app = appsService.getAppsByHql("from Apps where deviceId = ? and identifier = ?", deviceId, map.get("Identifier"));
                            if(null != app){
                            	app.setManagedAppsOnly("1");
                                appsService.saveOrUpdtae(app);
                            }else{
                            	app = new Apps();
                                app.setDeviceId(deviceId);
                                app.setAppName(map.get("Name"));
                                app.setBundleSize(map.get("BundleSize"));
                                app.setDynamicSize(map.get("DynamicSize"));
                                app.setIdentifier(map.get("Identifier"));
                                app.setShortVersion(map.get("ShortVersion"));
                                app.setVersion(map.get("Version"));
                                app.setManagedAppsOnly("1");
                                appsService.saveOrUpdtae(app);
                            }
                        }
                    }
                }else{
                	/**删除原有APP数据**/
                    appsService.deleteAppsByDeviceId(deviceId);
                    /**保存处理后的APP列表数据 start**/
                    Apps app = null;
                    for (String key : plistMap.keySet()) {
                        if(!MdmUtils.InstalledApplicationList.equals(key)){
                            Map<String, String> map =  plistMap.get(key);
                            app = new Apps();
                            app.setDeviceId(deviceId);
                            app.setAppName(map.get("Name"));
                            app.setBundleSize(map.get("BundleSize"));
                            app.setDynamicSize(map.get("DynamicSize"));
                            app.setIdentifier(map.get("Identifier"));
                            app.setShortVersion(map.get("ShortVersion"));
                            app.setVersion(map.get("Version"));
                            app.setManagedAppsOnly("0");
                            appsService.saveOrUpdtae(app);
                        }
                    }
                }
                /**保存处理后的APP列表数据 end**/
                if (command != null) {
                    command.setResult(MdmUtils.InstalledApplicationList);
                    command.setDoIt("2");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                System.out.println("-------------------InstalledApplicationList End---------------");
            } else if(info.contains(MdmUtils.ProvisioningProfileList)) {
                System.out.println("-------------------ProvisioningProfileList Start---------------");
                System.out.println("Device->Server ProvisioningProfileList:\n"+info.toString());
                Map<String, String> plistMap = MdmUtils.parseProvisioningProfileList(info);
                for(String key:plistMap.keySet()){
                	System.out.println(key+":"+plistMap.get(key));
                }
            	String CommandUUID = plistMap.get("CommandUUID");
            	profileService.deleteProfileByDeviceId(deviceId,MdmUtils.ProvisioningProfileList);
            	Profile profile = new Profile();
            	profile.setCtype(MdmUtils.ProvisioningProfileList);
            	profile.setDeviceId(deviceId);
            	profile.setResult(info.toString());
            	profileService.saveOrUpdate(profile);
            	System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if (command != null) {
                    command.setResult(MdmUtils.ProvisioningProfileList);
                    command.setDoIt("2");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                System.out.println("-------------------ProvisioningProfileList End---------------");
            } else if(info.contains(MdmUtils.ProfileList)) {
                System.out.println("-------------------ProfileList Start---------------");
                System.out.println("Device->Server ProfileList:\n"+info.toString());
                Map<String, String> plistMap = MdmUtils.parseProfileList(info);
                for(String key:plistMap.keySet()){
                	System.out.println(key+":"+plistMap.get(key));
                }
            	String CommandUUID = plistMap.get("CommandUUID");
            	profileService.deleteProfileByDeviceId(deviceId,MdmUtils.ProfileList);
            	Profile profile = new Profile();
            	profile.setCtype(MdmUtils.ProfileList);
            	profile.setDeviceId(deviceId);
            	profile.setResult(info.toString());
            	profileService.saveOrUpdate(profile);
            	System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if (command != null) {
                    command.setResult(MdmUtils.ProfileList);
                    command.setDoIt("2");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                System.out.println("-------------------ProfileList End---------------");
            } else if(info.contains(MdmUtils.CertificateList)) {
                System.out.println("-------------------CertificateList Start---------------");
                System.out.println("Device->Server CertificateList:\n"+info.toString());
                Map<String, String> plistMap = MdmUtils.parseCertificateList(info);
                for(String key:plistMap.keySet()){
                	System.out.println(key+":"+plistMap.get(key));
                }
            	String CommandUUID = plistMap.get("CommandUUID");
            	profileService.deleteProfileByDeviceId(deviceId,MdmUtils.CertificateList);
            	Profile profile = new Profile();
            	profile.setCtype(MdmUtils.CertificateList);
            	profile.setDeviceId(deviceId);
            	profile.setResult(info.toString());
            	profileService.saveOrUpdate(profile);
            	System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if (command != null) {
                    command.setResult(MdmUtils.CertificateList);
                    command.setDoIt("2");
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                System.out.println("-------------------CertificateList End---------------");
            } else {
                System.out.println("-------------------OtherResult Start---------------");
                System.out.println("Device->Server Others:\n"+info.toString());
                Map<String, String> plistMap = MdmUtils.parseCommand(info);
                for(String key:plistMap.keySet()){
                	System.out.println(key+":"+plistMap.get(key));
                }
                String CommandUUID = plistMap.get("CommandUUID");
                System.out.println("CommandUUID:"+CommandUUID);
                Command command = commandService.getCommandById(CommandUUID);
                if (command != null) {
                    command.setResult(MdmUtils.Acknowledged);
                    command.setDoIt("2");
                    /**将设备标记成已经移除**/
                    String commandstr = command.getCommand()==null?"":command.getCommand();
                    if(commandstr.equals(MdmUtils.Erase)){
                    	device.setControl("-1");
                    	deviceService.saveOrUpdate(device);
                    }
                    commandService.saveOrUpdate(command);
                    /**异步通知第三方更新**/
                	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "2", command.getId());
                }
                System.out.println("-------------------OtherResult End---------------");
            }
        } else if (info.contains(MdmUtils.CommandFormatError)) {
            System.out.println("-------------------CommandFormatError Start---------------");
            Map<String, String> plistMap = MdmUtils.parseCommand(info);
            for(String key:plistMap.keySet()){
            	System.out.println(key+":"+plistMap.get(key));
            }
            String CommandUUID = plistMap.get("CommandUUID");
            System.out.println("CommandUUID:"+CommandUUID);
            Command command = commandService.getCommandById(CommandUUID);
            if (command != null) {
                command.setDoIt("3");
                command.setResult(info);
                commandService.saveOrUpdate(command);
                /**异步通知第三方更新**/
            	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "3", command.getId());
            }
            System.out.println("-------------------CommandFormatError End---------------");
        } else if (info.contains(MdmUtils.Error)) {
            System.out.println("-------------------Error Start---------------");
            Map<String, String> plistMap = MdmUtils.parseCommand(info);
            for(String key:plistMap.keySet()){
            	System.out.println(key+":"+plistMap.get(key));
            }
            String CommandUUID = plistMap.get("CommandUUID");
            System.out.println("CommandUUID:"+CommandUUID);
            Command command = commandService.getCommandById(CommandUUID);
            if (command != null) {
                command.setDoIt("3");
                command.setResult(info);
                commandService.saveOrUpdate(command);
                /**异步通知第三方更新**/
            	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "3", command.getId());
            }
            System.out.println("-------------------Error End---------------");
        } else if (info.contains(MdmUtils.NotNow)) {
            System.out.println("-------------------NotNow Start---------------");
            Map<String, String> plistMap = MdmUtils.parseCommand(info);
            for(String key:plistMap.keySet()){
            	System.out.println(key+":"+plistMap.get(key));
            }
            String CommandUUID = plistMap.get("CommandUUID");
            System.out.println("CommandUUID:"+CommandUUID);
            Command command = commandService.getCommandById(CommandUUID);
            if (command != null) {
                command.setResult("NotNow");
                commandService.saveOrUpdate(command);
                /**异步通知第三方更新**/
            	MDMTaskUtils.sendCommandCallBack(command.getCallBack(), "0", command.getId());
            }
            System.out.println("-------------------NotNow End---------------");
        }
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
	public DeviceTempService getDeviceTempService() {
		return deviceTempService;
	}
	@Resource
	public void setDeviceTempService(DeviceTempService deviceTempService) {
		this.deviceTempService = deviceTempService;
	}
}
