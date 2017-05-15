package cn.flying.rest.onlinefile.utils;

import java.util.List;

/**
 * @author liukaiyuan
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseDao<T,ID> {
    
    public T save(T model);

    public void saveOrUpdate(T model);
    
    public void update(T model);
    
    public void merge(T model);

    public void delete(ID id);

    public void deleteObject(T model);

    public T get(ID id);
    
    public T getObject(ID id);
    
    public int countAll();

    public List<T> listAll();

    public List<T> listAll(int pageNum, int pageSize);
    
    boolean exists(ID id);
    
    public void flush();
    
    public void clear();
    
    /**
     * @see 增加直接操作sql语句的方法
     * @author zhangwenbin 20120829 
     * @param sqlStr 传入的sql语句
     * @return 标识是否成功
     */
    public boolean saveOrUpdate(String sqlStr);
    
    /**
     * 查询列表
     * @author longjunhao 20140528
     * @param query hql语句
     * @return
     */
    public List<T> find(String query);
    
}
