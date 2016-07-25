package com.jiangge.dao;

import java.util.List;

import com.jiangge.pojo.Profile;


public interface ProfileDao {
	
	void add(Profile profile);
	
	Profile getProfileById(String id);
	
	List<Profile> getAll();
	
	void deleteProfileById(String id);
	
	void deleteProfileByDeviceId(String deviceId);
	
	void deleteProfileByDeviceId(String deviceId,String ctype);

	Profile getByHql(String queryString,Object... params);
	
	void saveOrUpdate(Profile profile);
	
	List<?> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects);
	
	List<?> pageQuery(String hql, Object... objects);
	
	int count(String hql);
}
