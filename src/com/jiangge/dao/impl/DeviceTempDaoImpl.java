package com.jiangge.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.jiangge.dao.DeviceTempDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.Device;
import com.jiangge.pojo.DeviceTemp;

@SuppressWarnings("all")
@Component
public class DeviceTempDaoImpl extends BaseDao implements DeviceTempDao {

	@Override
	public void add(DeviceTemp deviceTemp) {
		super.add(deviceTemp);
	}

	@Override
	public DeviceTemp getDeviceTempById(String id) {
		Object object = super.getById(Device.class, id);
		return (DeviceTemp)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceTemp> getAll() {
		List<DeviceTemp> list = (List<DeviceTemp>) super.getAll(DeviceTemp.class);
		return list;
	}
	
	public DeviceTemp getByHql(String queryString,Object... params){
		return (DeviceTemp)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(DeviceTemp deviceTemp) {
		super.saveOrUpdate(deviceTemp);
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
	public void deleteDeviceTempById(String id) {
		super.deleteById(DeviceTemp.class, id);
	}

	@Override
	public void deleteDeviceTempByDeviceId(String deviceId) {
		String sql = "delete from DeviceTemp where deviceId = '"+deviceId+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	
	
}
