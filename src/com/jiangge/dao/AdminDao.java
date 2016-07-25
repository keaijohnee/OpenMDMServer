package com.jiangge.dao;

import java.util.List;

import com.jiangge.pojo.Admin;


public interface AdminDao {
	
	void add(Admin admin);
	
	Admin getAdminById(String id);
	
	List<Admin> getAll();

	Admin getByHql(String queryString,Object... params);
	
	void saveOrUpdate(Admin admin);
}
