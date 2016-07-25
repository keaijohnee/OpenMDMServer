package com.jiangge.service;

import java.util.List;

import com.jiangge.pojo.User;


public interface UserService {
	
	void save(User user);
	
	User getUserById(String id);
	
	List<User> getAllUser();
	
	User getUserByHql(String queryString,Object... params);
	
	void saveOrUpdtae(User user);
}
