package com.jiangge.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.jiangge.dao.DeviceDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.Device;

@SuppressWarnings("all")
@Component
public class DeviceDaoImpl extends BaseDao implements DeviceDao {

	@Override
	public void add(Device device) {
		super.add(device);
	}

	@Override
	public Device getDeviceById(String id) {
		Object object = super.getById(Device.class, id);
		return (Device)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAll() {
		List<Device> list = (List<Device>) super.getAll(Device.class);
		return list;
	}
	
	public Device getByHql(String queryString,Object... params){
		return (Device)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(Device device) {
		super.saveOrUpdate(device);
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

	@Override
	public void deleteDeviceById(String id) {
		super.deleteById(Device.class, id);
	}

	@Override
	public void deleteDeviceByDeviceId(String deviceId) {
		String sql = "delete from Device where deviceId = '"+deviceId+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	
	
}
