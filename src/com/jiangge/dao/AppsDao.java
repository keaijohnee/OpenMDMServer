package com.jiangge.dao;

import java.util.List;

import com.jiangge.pojo.Apps;


public interface AppsDao {
	
	void add(Apps apps);
	
	Apps getAppsById(String id);
	
	List<Apps> getAll();
	
	List<Apps> getAppsByDevice(String queryString);
	
	List<Apps> getAppsByHql(String queryString,Object... params);

	Apps getByHql(String queryString,Object... params);
	
	void saveOrUpdate(Apps apps);
	
	List<?> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects);
	
	List<?> pageQuery(String hql, Object... objects);
	
	int count(String hql);
	
	void deleteAppsById(String id);
	
	void deleteAppsByDeviceId(String deviceId);
	
	List<Apps> myPage(String hql,int currentPage,int pageSize);
}
