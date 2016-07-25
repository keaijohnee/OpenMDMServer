package com.jiangge.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiangge.dao.UserDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.User;

@Component
public class UserDaoImpl extends BaseDao implements UserDao {

	@Override
	public void add(User user) {
		super.add(user);
	}

	@Override
	public User getUserById(String id) {
		Object object = super.getById(User.class, id);
		return (User)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		List<User> list = (List<User>) super.getAll(User.class);
		return list;
	}
	
	public User getByHql(String queryString,Object... params){
		return (User)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(User user) {
		super.saveOrUpdate(user);
	}
	
	
	
}
