package com.jiangge.service;

import java.util.List;

import com.jiangge.pojo.Admin;


public interface AdminService {
	
	void save(Admin admin);
	
	Admin getAdminById(String id);
	
	List<Admin> getAllAdmin();
	
	Admin getAdminByHql(String queryString,Object... params);
	
	void saveOrUpdtae(Admin admin);
}
