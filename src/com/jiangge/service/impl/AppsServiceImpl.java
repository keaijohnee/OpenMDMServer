package com.jiangge.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jiangge.dao.AppsDao;
import com.jiangge.pojo.Apps;
import com.jiangge.service.AppsService;

@Component
public class AppsServiceImpl implements AppsService {
	
	private AppsDao appsDao;
	
	@Override
	public void save(Apps apps) {
		appsDao.add(apps);
	}
	
	@Override
	public Apps getAppsById(String id) {
		return appsDao.getAppsById(id);
	}

	@Override
	public List<Apps> getAllApps() {
		return appsDao.getAll();
	}

	public AppsDao getAppsDao() {
		return appsDao;
	}
	
	@Resource
	public void setAppsDao(AppsDao appsDao) {
		this.appsDao = appsDao;
	}

	@Override
	public Apps getAppsByHql(String queryString,Object... params) {
		return appsDao.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdtae(Apps apps) {
		if(null == apps.getId() || "".equals(apps.getId())){
			apps.setId(UUID.randomUUID().toString());
			appsDao.add(apps);
		}else{
			appsDao.saveOrUpdate(apps);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apps> pageQuery(String hql, Integer page, Integer size,Object... objects) {
		List<Apps> list = (List<Apps>)appsDao.pageQuery(hql, page, size, objects);
		return list;
	}

	@Override
	public Integer getCount(String hql) {
		return appsDao.count(hql);
	}

	@Override
	public void deleteAppsByDeviceId(String deviceId) {
		appsDao.deleteAppsByDeviceId(deviceId);
	}

	@Override
	public List<Apps> getAppsListByHql(String queryString) {
		return appsDao.getAppsByDevice(queryString);
	}

	@Override
	public void deleteAppsById(String id) {
		appsDao.deleteAppsById(id);
	}

	@Override
	public List<Apps> getAppsByDeviceId(String queryString,Object... params) {
		return appsDao.getAppsByHql(queryString, params);
	}
	
	
}
