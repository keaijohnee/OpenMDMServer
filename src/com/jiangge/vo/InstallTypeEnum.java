package com.jiangge.vo;

import java.util.LinkedHashMap;
import java.util.Map;


public enum InstallTypeEnum {
	
	iTunesStoreID("iTunesStoreID","AppStore商店APP ID"),
	ManifestURL("ManifestURL","PList安装");
	
	private String code;
	private String name;
	
	InstallTypeEnum(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	/**
	 * 判断Code是否存在
	 * @param ctype
	 * @return
	 */
	public static String getCode(String ctype){
		String code = "";
		for(InstallTypeEnum p: InstallTypeEnum.values()){
			if(p.code.equals(ctype)){
				code = p.code;
			}
	    }
		return code;
	} 
	
	/**
	 * 获取全部类型
	 * @return
	 */
	public static Map<String,String> getAllType(){
        Map<String,String> map = new LinkedHashMap<String,String>();
        for(InstallTypeEnum p: InstallTypeEnum.values()){
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
