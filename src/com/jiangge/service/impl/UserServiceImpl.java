package com.jiangge.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jiangge.dao.UserDao;
import com.jiangge.pojo.User;
import com.jiangge.service.UserService;

@Component
public class UserServiceImpl implements UserService {
	
	private UserDao userDao;
	
	@Override
	public void save(User user) {
		userDao.add(user);
	}
	
	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}

	@Override
	public List<User> getAllUser() {
		return userDao.getAll();
	}

	public UserDao getUserDao() {
		return userDao;
	}
	
	@Resource
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUserByHql(String queryString,Object... params) {
		return userDao.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdtae(User user) {
		if(null == user.getId() || "".equals(user.getId())){
			user.setId(UUID.randomUUID().toString());
			userDao.add(user);
		}else{
			userDao.saveOrUpdate(user);
		}
	}
	
	
	
}
