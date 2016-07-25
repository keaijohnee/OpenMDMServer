package com.jiangge.utils;

import java.util.HashMap;
import java.util.Map;

import com.jiangge.utils.task.TaskEntity;

public class MDMTaskUtils {
	
	/**
	 * 同步设备信息到数据库
	 * @param deviceId
	 * @param pemPath
	 */
	public static void syncDeviceInfo(String deviceId){
		try{
			Map<String, String> taskParam = new HashMap<String, String>();
			taskParam.put("deviceId", deviceId);
			TaskEntity task1 = new TaskEntity(MDMTasker.class,"syncDeviceInfo",taskParam);
			TaskUtils.addTask(task1);
			TaskEntity task2 = new TaskEntity(MDMTasker.class,"syncAppList",taskParam);
			TaskUtils.addTask(task2);
		}catch(Exception e){
			System.out.println("MDMTaskUtils->syncDeviceInfo:" + e.getMessage());
		}
	}
	
	/**
	 * 通知第三方服务器更新
	 * @param deviceId
	 * @param pemPath
	 */
	public static void sendCallBack(String callBack,String deviceId,String state){
		if(StringUtils.isNotEmpty(callBack)){
			try{
				Map<String, String> taskParam = new HashMap<String, String>();
				taskParam.put("callBack", callBack);
				taskParam.put("deviceId", deviceId);
				taskParam.put("state", state);
				TaskEntity task = new TaskEntity(MDMTasker.class,"sendCallBack",taskParam);
				TaskUtils.addTask(task);
			}catch(Exception e){
				System.out.println("MDMTaskUtils->sendCallBack:" + e.getMessage());
			}
		}
	}
	
	/**
	 * 命令执行后通知第三方服务器更新
	 * @param callBack
	 */
	public static void sendCommandCallBack(String callBack,String state,String commandId){
		if(StringUtils.isNotEmpty(callBack)){
			try{
				Map<String, String> taskParam = new HashMap<String, String>();
				taskParam.put("callBack", callBack);
				taskParam.put("state", state);
				taskParam.put("commandId", commandId);
				TaskEntity task = new TaskEntity(MDMTasker.class,"sendCommandCallBack",taskParam);
				TaskUtils.addTask(task);
			}catch(Exception e){
				System.out.println("MDMTaskUtils->sendCommandCallBack:" + e.getMessage());
			}
		}
	}
	
	/**
	 * 生成Mobileconfig描述文件
	 * @param configPath
	 * @param tempPath
	 * @param deviceId
	 */
	public static void createMobileconfig(String configPath,String tempPath,String deviceId){
		try{
			Map<String, String> taskParam = new HashMap<String, String>();
			taskParam.put("configPath", configPath);
			taskParam.put("tempPath", tempPath);
			taskParam.put("deviceId", deviceId);
			TaskEntity task = new TaskEntity(MDMTasker.class,"createMobileconfig",taskParam);
			TaskUtils.addTask(task);
		}catch(Exception e){
			System.out.println("MDMTaskUtils->createMobileconfig:" + e.getMessage());
		}
	}

}
