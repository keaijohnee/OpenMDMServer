package com.jiangge.vo;

import java.util.LinkedHashMap;
import java.util.Map;


public enum CommandTypeEnum {
	
	Lock("DeviceLock","锁屏"),
	Erase("EraseDevice","清除数据"),
	Info("DeviceInformation","设备信息"),
	Apps("InstalledApplicationList","APP列表"),
	Clear("ClearPasscode","清除密码"),
	Install("InstallApplication","安装APP"),
	Remove("RemoveApplication","卸载APP"),
	ProfileList("ProfileList","获取描述文件"),
	ProvisioningProfileList("ProvisioningProfileList","获取预置描述文件"),
	CertificateList("CertificateList","获取证书文件");
	
	private String code;
	private String name;
	
	CommandTypeEnum(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static Map<String,String> getAllType(){
        Map<String,String> map = new LinkedHashMap<String,String>();
        for(CommandTypeEnum p: CommandTypeEnum.values()){
           map.put(p.code,p.name);
        }
        return map;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
