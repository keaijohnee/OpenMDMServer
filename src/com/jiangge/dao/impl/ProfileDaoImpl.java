package com.jiangge.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.jiangge.dao.ProfileDao;
import com.jiangge.dao.common.BaseDao;
import com.jiangge.pojo.Profile;

@SuppressWarnings("all")
@Component
public class ProfileDaoImpl extends BaseDao implements ProfileDao {

	@Override
	public void add(Profile profile) {
		super.add(profile);
	}

	@Override
	public Profile getProfileById(String id) {
		Object object = super.getById(Profile.class, id);
		return (Profile)object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getAll() {
		List<Profile> list = (List<Profile>) super.getAll(Profile.class);
		return list;
	}
	
	public Profile getByHql(String queryString,Object... params){
		return (Profile)super.getByHql(queryString, params);
	}

	@Override
	public void saveOrUpdate(Profile profile) {
		super.saveOrUpdate(profile);
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
	public void deleteProfileById(String id) {
		super.deleteById(Profile.class, id);
	}

	@Override
	public void deleteProfileByDeviceId(String deviceId) {
		String sql = "delete from Profile where deviceId = '"+deviceId+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	@Override
	public void deleteProfileByDeviceId(String deviceId,String ctype) {
		String sql = "delete from Profile where deviceId = '"+deviceId+"' and ctype = '"+ctype+"'";
		Query query =super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	
}
