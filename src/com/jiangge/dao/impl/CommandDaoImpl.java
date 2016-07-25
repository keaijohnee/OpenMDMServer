package com.jiangge.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.jiangge.dao.CommandDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.Command;

@SuppressWarnings("all")
@Component
public class CommandDaoImpl extends BaseDao implements CommandDao {

	@Override
	public void add(Command command) {
		super.add(command);
	}

	@Override
	public Command getCommandById(String id) {
		Object object = super.getById(Command.class, id);
		return (Command)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Command> getAll() {
		List<Command> list = (List<Command>) super.getAll(Command.class);
		return list;
	}

	@Override
	public void deleteCommandById(String id) {
		super.deleteById(Command.class, id);
	}
	
	public Command getByHql(String queryString,Object... params){
		return (Command)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(Command command) {
		super.saveOrUpdate(command);
	}

	@Override
	public void deleteCommandByDeviceId(String deviceId) {
		String sql = "delete from Command where deviceId = '"+deviceId+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	/**
	 * 分页查询
	 * @param hql
	 * @param page
	 * @param size
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<?> pageQuery(final String hql, final Integer page, final Integer size, final Object... objects) {
		return super.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (objects != null)
					for (int i = 0; i < objects.length; i++){
						query.setParameter(i, objects[i]);
					}
				if (page != null && size != null)
					query.setFirstResult((page - 1) * size).setMaxResults(size);
				return query.list();
			}
		});
	}
	

	/**
	 * 分页查询
	 * @param hql
	 * @param objects
	 * @return
	 */
	public List<?> pageQuery(String hql, Object... objects) {
		return pageQuery(hql, null, null, objects);
	}

	@Override
	public int count(String hql) {
       Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);  
       int num = ((Number)query.iterate().next()).intValue();  
       return num;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Command> getAllByHql(String queryString, Object... params) {
		return (List<Command>)super.getListByHql(queryString, params);
	}
	
	
}
