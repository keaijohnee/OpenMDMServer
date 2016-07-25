package com.jiangge.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jiangge.dao.ProfileDao;
import com.jiangge.pojo.Profile;
import com.jiangge.service.ProfileService;

@Component
public class ProfileServiceImpl implements ProfileService {
	
	private ProfileDao profileDao;
	
	@Override
	public void save(Profile profile) {
		profileDao.add(profile);
	}
	
	@Override
	public Profile getProfileById(String id) {
		return profileDao.getProfileById(id);
	}

	@Override
	public List<Profile> getAllProfile() {
		return profileDao.getAll();
	}

	public ProfileDao getProfileDao() {
		return profileDao;
	}
	
	@Resource
	public void setProfileDao(ProfileDao profileDao) {
		this.profileDao = profileDao;
	}

	@Override
	public Profile getProfileByHql(String queryString,Object... params) {
		return profileDao.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(Profile profile) {
		if(null == profile.getId() || "".equals(profile.getId())){
			profile.setId(UUID.randomUUID().toString());
			profileDao.add(profile);
		}else{
			profileDao.saveOrUpdate(profile);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> pageQuery(String hql, Integer page, Integer size,Object... objects) {
		List<Profile> list = (List<Profile>)profileDao.pageQuery(hql, page, size, objects);
		return list;
	}

	@Override
	public Integer getCount(String hql) {
		return profileDao.count(hql);
	}

	@Override
	public void deleteProfileById(String id) {
		profileDao.deleteProfileById(id);
	}

	@Override
	public void deleteProfileByDeviceId(String deviceId) {
		profileDao.deleteProfileByDeviceId(deviceId);
	}

	@Override
	public void deleteProfileByDeviceId(String deviceId, String ctype) {
		profileDao.deleteProfileByDeviceId(deviceId,ctype);
	}
	
	
}
