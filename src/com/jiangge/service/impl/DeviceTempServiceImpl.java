package com.jiangge.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jiangge.dao.DeviceTempDao;
import com.jiangge.pojo.DeviceTemp;
import com.jiangge.service.DeviceTempService;

@Component
public class DeviceTempServiceImpl implements DeviceTempService {
	
	private DeviceTempDao deviceTempDao;
	
	@Override
	public void save(DeviceTemp deviceTemp) {
		deviceTempDao.add(deviceTemp);
	}
	
	@Override
	public DeviceTemp getDeviceTempById(String id) {
		return deviceTempDao.getDeviceTempById(id);
	}

	@Override
	public List<DeviceTemp> getAllDeviceTemp() {
		return deviceTempDao.getAll();
	}

	public DeviceTempDao getDeviceTempDao() {
		return deviceTempDao;
	}
	
	@Resource
	public void setDeviceTempDao(DeviceTempDao deviceTempDao) {
		this.deviceTempDao = deviceTempDao;
	}

	@Override
	public DeviceTemp getDeviceTempByHql(String queryString,Object... params) {
		return deviceTempDao.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(DeviceTemp deviceTemp) {
		if(null == deviceTemp.getId() || "".equals(deviceTemp.getId())){
			deviceTemp.setId(UUID.randomUUID().toString());
			deviceTempDao.add(deviceTemp);
		}else{
			deviceTempDao.saveOrUpdate(deviceTemp);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceTemp> pageQuery(String hql, Integer page, Integer size,Object... objects) {
		List<DeviceTemp> list = (List<DeviceTemp>)deviceTempDao.pageQuery(hql, page, size, objects);
		return list;
	}

	@Override
	public Integer getCount(String hql) {
		return deviceTempDao.count(hql);
	}

	@Override
	public void deleteDeviceTempById(String id) {
		deviceTempDao.deleteDeviceTempById(id);
	}

	@Override
	public void deleteDeviceTempByDeviceId(String deviceId) {
		deviceTempDao.deleteDeviceTempByDeviceId(deviceId);
	}
	
	
}
