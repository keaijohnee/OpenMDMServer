package com.jiangge.dao;

import java.util.List;

import com.jiangge.pojo.User;


public interface UserDao {
	
	void add(User user);
	
	User getUserById(String id);
	
	List<User> getAll();

	User getByHql(String queryString,Object... params);
	
	void saveOrUpdate(User user);
}
