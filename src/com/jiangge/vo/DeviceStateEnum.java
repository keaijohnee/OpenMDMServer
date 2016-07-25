package com.jiangge.vo;

import java.util.LinkedHashMap;
import java.util.Map;


public enum DeviceStateEnum {
	
	Authenticate("1","权限认证"),
	Control("2","设备可控"),
	Remove("-1","不可控");
	
	private String code;
	private String name;
	
	DeviceStateEnum(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static Map<String,String> getAllType(){
        Map<String,String> map = new LinkedHashMap<String,String>();
        for(DeviceStateEnum p: DeviceStateEnum.values()){
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
