package com.jiangge.service;

import java.util.List;

import com.jiangge.pojo.Profile;


public interface ProfileService {
	
	void save(Profile device);
	
	Profile getProfileById(String id);
	
	List<Profile> getAllProfile();
	
	void deleteProfileById(String id);
	
	void deleteProfileByDeviceId(String deviceId);
	
	void deleteProfileByDeviceId(String deviceId,String ctype);
	
	Profile getProfileByHql(String queryString,Object... params);
	
	void saveOrUpdate(Profile profile);
	
    List<Profile> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects);
	
    Integer getCount(String hql);
	
}
