package cn.flying.rest.onlinefile.documentclass.driver;

import java.util.List;
import java.util.Map;

public interface FilePropDao {

	public Map<String,String> addFileProp(Map<String, String> filePropBeanMap);
	
	public Map<String,Object> getFilePropLst(String userId,int strat,int limit,String queryStr);
	
	public boolean delFileProp(Map<String, String> filePropBeanMap);
	
	public Map<String,String> getFilePropById(String id);
	
	public boolean updateFilePropById(Map<String,String> paras);
	
	public int getFilePropCountByTitle(String title,String companyId);
	
	public boolean delColumn(String tableName,String colName);

	public boolean addColumn(String sql);
	
	public List<Map<String,String>> getFilePropLstByCompany(String companyId);
}
