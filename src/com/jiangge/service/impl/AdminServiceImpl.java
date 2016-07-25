package com.jiangge.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jiangge.dao.AdminDao;
import com.jiangge.pojo.Admin;
import com.jiangge.service.AdminService;

@Component
public class AdminServiceImpl implements AdminService {
	
	private AdminDao adminDao;
	
	@Override
	public void save(Admin admin) {
		adminDao.add(admin);
	}
	
	@Override
	public Admin getAdminById(String id) {
		return adminDao.getAdminById(id);
	}

	@Override
	public List<Admin> getAllAdmin() {
		return adminDao.getAll();
	}

	public AdminDao getAdminDao() {
		return adminDao;
	}
	
	@Resource
	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	@Override
	public Admin getAdminByHql(String queryString,Object... params) {
		return adminDao.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdtae(Admin admin) {
		if(null == admin.getId() || "".equals(admin.getId())){
			admin.setId(UUID.randomUUID().toString());
			adminDao.add(admin);
		}else{
			adminDao.saveOrUpdate(admin);
		}
	}
	
	
	
}
