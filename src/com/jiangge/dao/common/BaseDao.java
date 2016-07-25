package com.jiangge.dao.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Component
public class BaseDao {
	
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 根据ID获取对象
	 * @param c
	 * @param id
	 * @return
	 */
	public Object getByHql(String queryString,Object... params) {
		List<?> list = hibernateTemplate.find(queryString, params);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据ID获取对象
	 * @param c
	 * @param id
	 * @return
	 */
	public List<?> getListByHql(String queryString,Object... params) {
		List<?> list = hibernateTemplate.find(queryString, params);
		return list;
	}

	/**
	 * 添加
	 * @param o
	 */
	public void add(Object o) {
		hibernateTemplate.save(o);
	}

	/**
	 * 修改
	 * @param o
	 */
	public void update(Object o) {
		hibernateTemplate.update(o);
	}
	
	/**
	 * 修改或者添加
	 * @param o
	 */
	public void saveOrUpdate(Object o) {
		hibernateTemplate.saveOrUpdate(o);
	}
	

	/**
	 * 修改(在session中已存在这个对象的修改)
	 * @param o
	 */
	public void merge(Object o) {
		hibernateTemplate.merge(o);
	}

	/**
	 * 根据ID获取对象
	 * @param c
	 * @param id
	 * @return
	 */
	public Object getById(Class<?> c, Serializable id) {
		return hibernateTemplate.get(c, id);
	}

	/**
	 * 删除对象
	 * @param o
	 */
	public void delete(Object o) {
		hibernateTemplate.delete(o);
	}

	/**
	 * 根据ID删除对象
	 * @param c
	 * @param id
	 */
	public void deleteById(Class<?> c, Serializable id) {
		delete(getById(c, id));
	}

	/**
	 * 获取所有的记录
	 * @param c
	 * @return
	 */
	public List<?> getAll(Class<?> c) {
		return hibernateTemplate.find("from " + c.getName());
	}

	/**
	 * 批量修改
	 * @param hql
	 * @param objects
	 */
	public void bulkUpdate(String hql, Object... objects) {
		hibernateTemplate.bulkUpdate(hql, objects);
	}

	/**
	 * 得到唯一值
	 * @param hql
	 * @param objects
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public Object getUnique(final String hql, final Object... objects) {
		return hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (objects != null)
					for (int i = 0; i < objects.length; i++)
						query.setParameter(i, objects[i]);
				return query.uniqueResult();
			}
		});
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
		return hibernateTemplate.executeFind(new HibernateCallback() {

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

	/**
	 * 保存
	 * @param o
	 */
	public void save(Object o) {
		if (o != null)
			hibernateTemplate.saveOrUpdate(o);
	}
	
	/**
	 * 更新
	 * @param hql
	 * @param objects
	 */
	@SuppressWarnings({ "unchecked" })
	public void update(final String hql, final Object... objects){
		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (objects != null)
					for (int i = 0; i < objects.length; i++)
						query.setParameter(i, objects[i]);
				return query.executeUpdate();
			}
		});
	}

	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
