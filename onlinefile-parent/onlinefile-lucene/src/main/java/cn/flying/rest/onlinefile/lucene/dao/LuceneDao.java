package cn.flying.rest.onlinefile.lucene.dao;

import java.util.List;
import java.util.Map;

public interface LuceneDao {

	public boolean insert(Map<String,String> fileInfo);
	
	public boolean delete(String id);
	
	public List<Map<String,String>> findAll();
	
	public boolean insertKeyWord(String keyWord);

    public List<Map<String, String>> getFilesByCondition(Map<String, String> map);

    public List<Map<String, String>> getMsgByCondition(Map<String, String> map);
}
