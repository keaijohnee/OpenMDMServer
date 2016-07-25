package com.jiangge.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.jiangge.dao.AppsDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.Apps;

@SuppressWarnings("all")
@Component
public class AppsDaoImpl extends BaseDao implements AppsDao {

	@Override
	public void add(Apps apps) {
		super.add(apps);
	}

	@Override
	public Apps getAppsById(String id) {
		Object object = super.getById(Apps.class, id);
		return (Apps)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apps> getAll() {
		List<Apps> list = (List<Apps>) super.getAll(Apps.class);
		return list;
	}
	
	public Apps getByHql(String queryString,Object... params){
		return (Apps)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(Apps apps) {
		super.saveOrUpdate(apps);
	}
	
	/**
	 * 分页查询
	 * @param hql
	 * @param page
	 * @param size
	 * @param objects
	 * @return
	 */
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
	public void deleteAppsByDeviceId(String deviceId) {
		String sql = "delete from Apps where deviceId = '"+deviceId+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apps> getAppsByDevice(String queryString) {
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);
		return (List<Apps>)query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apps> myPage(String hql,int currentPage,int pageSize) {
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		query.setFirstResult((currentPage - 1)* pageSize);
		query.setMaxResults(pageSize);			
		return (List<Apps>)query.list();
	}

	@Override
	public void deleteAppsById(String id) {
		super.deleteById(Apps.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apps> getAppsByHql(String queryString, Object... params) {
		List<Apps> list = (List<Apps>)super.getListByHql(queryString, params);
		return list;
	}
	
	
	
}
