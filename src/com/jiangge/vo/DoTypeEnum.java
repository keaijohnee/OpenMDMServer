package com.jiangge.vo;

import java.util.LinkedHashMap;
import java.util.Map;


public enum DoTypeEnum {
	
	Do0("0","未执行"),
	Do1("1","已执行"),
	Do2("2","已成功"),
	Do3("3","失败");
	
	private String code;
	private String name;
	
	DoTypeEnum(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static Map<String,String> getAllType(){
        Map<String,String> map = new LinkedHashMap<String,String>();
        for(DoTypeEnum p: DoTypeEnum.values()){
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
