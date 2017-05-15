package cn.flying.rest.onlinefile.utils;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;

import cn.flying.rest.onlinefile.utils.JdbcUtil;




/**
 * @author liqiubo
 *
 * @param <T>
 * @param <ID>
 */
@Scope("prototype")
@Transactional
public  class BaseDaoHibernate<T extends Serializable,ID extends Serializable> implements BaseDao<T,ID>{



    protected Class<T> entity;

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;
    
    public BaseDaoHibernate(Class<T> entityClass){
        this.entity = entityClass;
    }

    public T save(T model) {
//        return (T)getSession().save(model);//maguochun 20120910 修改返回类型
    	getSession(false).save(model);
    	return model;
    }

    
    public void saveOrUpdate(T model) {
        getSession(false).saveOrUpdate(model);
    }

    
    public void update(T model) {
        getSession(false).update(model);
    }

    
    public void merge(T model) {
        getSession(true).merge(model);
    }

    
    public void delete(ID id) {
        getSession(false).delete(this.getForDelete(id));
    }


    
    public void deleteObject(T model) {
        getSession(false).delete(model);
    }

    @SuppressWarnings("unchecked")
	
    public T get(ID id) {
        return (T)getSession(true).get(this.entity,id);
    }
    
    @SuppressWarnings("unchecked")
    private T getForDelete(ID id) {
    	return (T)getSession(false).get(this.entity,id);
    }
    
	@SuppressWarnings("unchecked")
	
	public T getObject(ID id) {
		 return (T)getSession(true).get(this.entity,id);
	}
	
    
    public int countAll() {
        //return  (int)getSession().createCriteria(this.entity).list().size();//maguochun 20120830
    	//maguochun 20120828 获得总数
    	Number n = (Number) getSession(true).createCriteria(this.entity).setProjection(Projections.rowCount()).uniqueResult();
    	int c ;
    	if(n instanceof Long) {
			c = ((Long)n).intValue();
		} else {
			c = (Integer) n;
		}
    	return c;
    }

    @SuppressWarnings("unchecked")
	
    public List<T> listAll() {
        return (List<T>)getSession(true).createCriteria(this.entity).list();
    }

    @SuppressWarnings("unchecked")
	
    public List<T> listAll(int pageNum, int pageSize) {
        Criteria criteria = getSession(true).createCriteria(this.entity);
        criteria.setFirstResult( (pageNum-1) * pageSize );//maguochun 20120831
        criteria.setMaxResults( pageSize );
        return (List<T>)criteria.list();
    }


    
    public boolean exists(ID id) {
        return get(id)!=null;
    }

    
    public void flush() {
        getSession().flush();
    }

    
    public void clear() {
        getSession().clear();
    }
    
    @SuppressWarnings("deprecation")
    public boolean saveOrUpdate(String sqlStr){
        Session  session = this.getSession(false);
        Connection conn = session.connection() ;//此方法已被标注不推荐使用
        Statement st = null ;
		try{		
			st = conn.createStatement();
        	st.executeUpdate(sqlStr);
        	return true;
        }catch(Exception e){
        	e.printStackTrace();
        	throw new RuntimeException(e.getMessage(), e); //yanggaofei 20130507 将异常抛出去
        }finally{
        	JdbcUtil.close(st);
		}
    }   
    
    /**
     * 查询列表
     * @author longjunhao 20140528
     * @param query hql语句
     * @return
     */
    public List<T> find(String query) {
      return (List<T>) getSession(true).createQuery(query).list();
    }
   
    protected void optimizationSession(Session session){
    	if (session != null && session.isOpen()) {
			session.flush();
			session.clear();
		}
    }
    
    protected void closeSession(Session session){
    	if (session != null && session.isOpen()) {
			session.close();
		}
    }

    /**
     * @author liukaiyuan
     *
     * @param <boolean> isQuery  查询方法获取session时传入true，否则传入false
     * 只获取session，所有设置应该以service层事务为准（spring配置） zhanglei 20140226
     */
    protected Session getSession(boolean isQuery){
    	/*Session session = sessionFactory.getCurrentSession();
    	try {
    		Connection con=session.connection();
    		con.setReadOnly(isQuery);
    		con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			System.out.println(con.getTransactionIsolation() + " " + con.isReadOnly());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return session;*/
    	return sessionFactory.getCurrentSession();
    }
    
    public Session getSession(){
    	return sessionFactory.getCurrentSession();
    }
    /**
     * 使用过程中一定尽量使用getSession（）
     * 此方法是为了解决批量修改时候加事物
     * @return
     */
    public Session getOpenSession(){
    	return sessionFactory.openSession();
    }

}
