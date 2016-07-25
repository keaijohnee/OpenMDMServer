package com.jiangge.service;

import java.util.List;

import com.jiangge.pojo.Command;


public interface CommandService {
	
	void save(Command command);
	
	Command getCommandById(String id);
	
	List<Command> getAllCommand();
	
	void deleteCommandById(String id);
	
	void deleteCommandByDeviceId(String deviceId);
	
	Command getCommandByHql(String queryString,Object... params);
	
	void saveOrUpdate(Command command);
	
    List<Command> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects);
	
    Integer getCount(String hql);
    
    List<Command> getAllCommandByHql(String queryString,Object... params);
}
