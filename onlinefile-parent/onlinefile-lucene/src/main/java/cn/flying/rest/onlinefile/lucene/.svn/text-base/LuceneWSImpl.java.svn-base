package cn.flying.rest.onlinefile.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.flying.rest.file.IMainFileServer;
import cn.flying.rest.onlinefile.lucene.dao.LuceneDao;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.CompanyRegistWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueProducerWS;
import cn.flying.rest.onlinefile.restInterface.UserWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.LogUtils;
import cn.flying.rest.onlinefile.utils.PinYin;
import cn.flying.rest.onlinefile.utils.StringUtil;

@Path("/onlinefile_lucenews")
@Component
public class LuceneWSImpl extends BaseWS implements LuceneWS  {
	
	private IMainFileServer iMainFileServer;
	
	@Value("${flyingsoft.elasticsearch.ip}")
	private String ip;
	
	@Value("${flyingsoft.elasticsearch.port}")
	private int port;
	
	@Value("${flyingsoft.elasticsearch.cluster.name}")
	private String clusterName;
	
	@Value("${flyingsoft.mongodb.host}")
	private String host ;// 主机名
	
	@Value("${flyingsoft.mongodb.port}")
	private int mongoport ;//端口

	public static ExecutorService INDEXPOOL = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(1),new DiscardPolicy());
	
	@Autowired
	private LuceneDao ldao;
	
	private UserWS userws;
	
	private ChatWS chatws;
	
	private FilesWS filesws;
	
	private CompanyRegistWS companyRegistws;
	
	private ChatWS getChatWS(){
		if(chatws==null){
			synchronized (ChatWS.class) {
				if(chatws==null){
					chatws = this.getService(ChatWS.class);
				}
			}
		}
		return chatws;
	}
	
	private UserWS getUserWS(){
		if(userws==null){
			userws = this.getService(UserWS.class);
		}
		return userws;
	}
	
	public FilesWS getFilesWS() {
	  if(filesws==null){
	    filesws = this.getService(FilesWS.class);
       }
      return filesws;
    }

    public CompanyRegistWS getCompanyRegistWS(){
        if(companyRegistws==null){
          companyRegistws = this.getService(CompanyRegistWS.class);
        }
        return companyRegistws;
    }

	private IMainFileServer getMainFileServer(){
		if(iMainFileServer == null){
			iMainFileServer = this.getService(IMainFileServer.class) ;
		}
		return iMainFileServer ;
	}

	/** 声明消费者服务 */
    private MessageQueueConsumerWS consumerWS;
    
    public MessageQueueConsumerWS getconsumerWS() {
        if (consumerWS == null) {
            synchronized (MessageQueueConsumerWS.class) {
                if (consumerWS == null) {
                    consumerWS = this.getService(MessageQueueConsumerWS.class);
                }
            }
        }
        return consumerWS;
    }
    private MessageQueueProducerWS producerWS;
    
    public MessageQueueProducerWS getProducerWS() {
        if (producerWS == null) {
            synchronized (MessageQueueProducerWS.class) {
                if (producerWS == null) {
                    producerWS = this.getService(MessageQueueProducerWS.class);
                }
            }
        }
        return producerWS;
    }

  @Override
	protected void doStart() throws Exception {
		//初始化一下这个client
		this.getClient();
		//查看company0这个库是否存在，此库为默认的库
		/*String companyId = "0";
		if(!indexIsExits(LuceneWS.FILEINDEXNAME+companyId)){
			this.createEmptyIndex(LuceneWS.FILEINDEXNAME+companyId);
			this.addAlias(LuceneWS.FILEINDEXNAME+companyId);
			this.createFileIndexMapping(LuceneWS.FILEINDEXNAME+companyId);
			Map<String,String> map = new HashMap<String, String>();
			map.put("isGroup", "0");
			map.put("companyId", companyId);
			map.put("GROUPFLAG", "indexDbinit") ;
			map.put("FROMCNNAME", "系统初始化") ;
			map.put("FILEID", "0"); // 记录该条消息关联的文件
		    map.put("CHATFLAG", "0_0") ;
		    map.put("FROMUSER", "0") ;
		    map.put("CONTENT", "系统初始化");
		    map.put("DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
		    map.put("TIME", new SimpleDateFormat("HH:mm:ss").format(new Date())) ;
		    map.put("STYLE", "undefined") ;
		    getChatWS().initMongodbMsg(map);
		    map.put("isGroup", "1");
		    getChatWS().initMongodbMsg(map);
			//this.createMsgIndex(companyId, "0");
			//this.createMsgIndex(companyId, "1");
		}else{
		  if(!aliasIsExits(LuceneWS.FILEINDEXNAME+companyId+"_alias")){
		     this.addAlias(LuceneWS.FILEINDEXNAME+companyId);
		  }
		}*/
	}
	
	@Override
	protected void doShutdown() {
		if(getClient()!=null){
			getClient().close();
		}
	}
	
	
	public Client getClient(){
		return LuceneClient.getLuceneClient().getClient(ip,port,clusterName);
	}
	
//	public void flush(Client client, String indexName, String indexType) {
//		try{
//			client.admin().indices().flush(new FlushRequest(indexName.toLowerCase(), indexType)).actionGet();
//		}catch(Exception e){};
//	}
	
	/**
	 * 判断索引库是否存在
	 * @param indexName
	 * @return
	 */
	public boolean indexIsExits(String indexName){
    	IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[]{indexName.toLowerCase()});
		return getClient().admin().indices().exists(ier).actionGet().isExists();
	}
	/**
	 * 判断别名是否存在
	 */
	public boolean aliasIsExits(String indexNameAlias){
	    Client client = getClient();
	    GetAliasesRequest ier = new GetAliasesRequest().aliases(indexNameAlias);
	    return client.admin().indices().aliasesExist(ier).actionGet().isExists();
	  }
	
	public Map<String,String> createIndex(Map<String,String> map) {
		
		//日志---
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "创建索引fileId【"+map.get("fileId")+"】,公司ID【"+map.get("companyId")+"】");
		log.put("module", "索引");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("companyName",map.get("companyName"));
		log.put("operate", "索引");
		LogUtils.saveBaseLog(compLocator, log);
		
		Map<String,String> rtnMap = new HashMap<String, String>();
		//把信息存储到mongodb中
		if(this.insert(map)){
		    //建立索引
	        this.execThread();
		}
		return rtnMap;
	}
	
	public boolean updateIndex(Map<String,String> map) {
		
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "更新索引ID【"+map.get("ID")+"】,公司ID【"+map.get("companyId")+"】");
		log.put("module", "索引");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("companyName",map.get("companyName"));
		log.put("operate", "索引");
		LogUtils.saveBaseLog(compLocator, log);
		try{
		    String indexName = LuceneWS.FILEINDEXNAME + map.get("companyId");
		    String indexType = LuceneWS.FILEINDEXNAME + map.get("companyId");
		    List<String> typeList = this.getTypesByIndexAlias(LuceneWS.FILEINDEXNAME + map.get("companyId")+"_alias");
            if(typeList.size()>0){
              indexType = typeList.get(0);
            }
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(indexName+"_alias");
			updateRequest.type(indexType);
			updateRequest.id(map.get("ID"));
			updateRequest.doc(XContentFactory.jsonBuilder()
				.startObject()
				.field("idSeq", map.get("idSeq").replace(".", "_"))
				.field("openlevel", Integer.parseInt(map.get("openlevel")))
				.field("classId",Integer.parseInt(map.get("classId")))
				.endObject());
			getClient().update(updateRequest).get();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateIndexOfDel(Map<String,Object> map) {
		//elastcsearch方法现在不支持更新某一条件的所有文档，以后升级elasticsearch版本后，如果支持了，那就请修改一下
		//现在尽量使用批量的方式来处理，减少request请求的次数，提高速度
		try{
			@SuppressWarnings("unchecked")
            List<String> ids = (List<String>)map.get("ids");
			String isDel = map.get("isDel").toString();
			
			String indexName = LuceneWS.FILEINDEXNAME + map.get("companyId");
			String indexType = LuceneWS.FILEINDEXNAME + map.get("companyId");
            List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
            if(typeList.size()>0){
              indexType = typeList.get(0);
            }
			
			BulkRequestBuilder bulkRequest = getClient().prepareBulk();
			for(String id:ids){
				//业务对象
				UpdateRequest updateRequest = new UpdateRequest();
				updateRequest.index(indexName+"_alias");
				updateRequest.type(indexType);
				updateRequest.id(id);
				//isDel 1是删除  0是不删除
				updateRequest.doc(XContentFactory.jsonBuilder()
					.startObject()
					.field("isDelete", isDel)  
					.endObject());
				//getClient().update(updateRequest).get();
				//添加到builder中
				bulkRequest.add(updateRequest);
			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
			    // process failures by iterating through each bulk response item
				System.out.println(bulkResponse.buildFailureMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * xiewenda 
	 * 批量修改 索引(回收站的批量恢复功能)
	 */
	public boolean updateIndexForList(List<Map<String,String>> data) {

	  try{
	    
	    if(data.size()>0){
	    String indexName = LuceneWS.FILEINDEXNAME + data.get(0).get("companyId");
        String indexType = LuceneWS.FILEINDEXNAME + data.get(0).get("companyId");
          List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
          if(typeList.size()>0){
            indexType = typeList.get(0);
          }
	    BulkRequestBuilder bulkRequest = getClient().prepareBulk();
	    for (Map<String, String> map : data) {
          //业务对象
          UpdateRequest updateRequest = new UpdateRequest();
          updateRequest.index(indexName+"_alias");
          updateRequest.type(indexType);
          updateRequest.id(map.get("id"));
          //isDel 1是删除  0是不删除
          updateRequest.doc(XContentFactory.jsonBuilder()
              .startObject()
              .field("idSeq", map.get("idSeq"))
              .field("classId",map.get("classId") !=null ? Integer.parseInt(map.get("classId")):null)
              .field("isDelete","0")//恢复文件所以这里都改为0
              .endObject());
          //getClient().update(updateRequest).get();
          //添加到builder中
          bulkRequest.add(updateRequest);
        }
	    
	    BulkResponse bulkResponse = bulkRequest.execute().actionGet();
	    if (bulkResponse.hasFailures()) {
	      // process failures by iterating through each bulk response item
	      System.out.println(bulkResponse.buildFailureMessage());
	    }
	   }
	  }catch(Exception e){
	    e.printStackTrace();
	    return false;
	  }
	  return true;
	}
	
	/**
	 * wangwenshuo 批量更新索引  (设为私密文件  版本更新或者文件名更新)
	 */
	public boolean updateIndex4Unshare(List<Map<String,String>> data) {
		
		try{
			BulkRequestBuilder bulkRequest = getClient().prepareBulk();
			for (Map<String, String> map : data) {
			    String indexName = LuceneWS.FILEINDEXNAME + map.get("companyId");
			    String indexType = LuceneWS.FILEINDEXNAME + map.get("companyId");
	            List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
	            if(typeList.size()>0){
	              indexType = typeList.get(0);
	            }
				//业务对象
				UpdateRequest updateRequest = new UpdateRequest();
				updateRequest.index(indexName+"_alias");
				updateRequest.type(indexType);
				updateRequest.id(map.get("id"));
				//isDel 1是删除  0是不删除
				  updateRequest.doc(XContentFactory.jsonBuilder()
                      .startObject()
                      .field("openLevel", map.get("openlevel"))
                      .field("version",map.get("version")!=null?Integer.parseInt(map.get("version")):null)
                      .endObject());
				//getClient().update(updateRequest).get();
				//添加到builder中
				bulkRequest.add(updateRequest);
			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				// process failures by iterating through each bulk response item
				System.out.println(bulkResponse.buildFailureMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateIndexOfisLast(List<Map<String,String>> data,String companyId) {
	  String indexName = LuceneWS.FILEINDEXNAME + companyId;
      String indexType = LuceneWS.FILEINDEXNAME + companyId;
      try{
          BulkRequestBuilder bulkRequest = getClient().prepareBulk();
          for (Map<String, String> map : data) {
            List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
            if(typeList.size()>0){
              indexType = typeList.get(0);
            }
              //业务对象
              UpdateRequest updateRequest = new UpdateRequest();
              updateRequest.index(indexName+"_alias");
              updateRequest.type(indexType);
              updateRequest.id(map.get("id"));
                updateRequest.doc(XContentFactory.jsonBuilder()
                    .startObject()
                    .field("isLast", "0")
                    .endObject());
              //getClient().update(updateRequest).get();
              //添加到builder中
              bulkRequest.add(updateRequest);
          }
          
          BulkResponse bulkResponse = bulkRequest.execute().actionGet();
          if (bulkResponse.hasFailures()) {
              // process failures by iterating through each bulk response item
              System.out.println(bulkResponse.buildFailureMessage());
          }
      }catch(Exception e){
          e.printStackTrace();
          return false;
      }
      return true;
  }
	/**
	 * xiewenda 修改索引数据通用方法
	 * @param indexmap 保存要修改的索引数据 key 为companyid value 为要修改数据的数据集合(List<Map<String,String>>) 
	 * List中每一个map存储的key：要修改的索引映射(字段) value:要修改为的值
	 * @return
	 */
	public boolean updateIndexComment(Map<String,List<Map<String,String>>> indexmap) {
      
      try{
         for(String key: indexmap.keySet()){
           
          BulkRequestBuilder bulkRequest = getClient().prepareBulk();
          
          String indexName = LuceneWS.FILEINDEXNAME + key;
          String indexType = LuceneWS.FILEINDEXNAME + key;
          List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
          if(typeList.size()>0){
            indexType = typeList.get(0);
          }
          for (Map<String, String> map : indexmap.get(key)) {
              if(map.get("id")==null||"".equals(map.get("id"))) continue;
              //业务对象
              UpdateRequest updateRequest = new UpdateRequest();
              updateRequest.index(indexName+"_alias");
              updateRequest.type(indexType);
              updateRequest.id(map.get("id"));
              
              XContentBuilder xcb = XContentFactory.jsonBuilder();
              xcb.startObject();
              for(String field : map.keySet()){
                  if("id".equals(field))continue;
                  if("mysqlId".equals(field)||"classId".equals(field)||"creator".equals(key)||"owner".equals(field)||"soleNumber".equals(field)||"praiseCount".equals(field)){
                      xcb.field(field, Integer.parseInt(map.get(field)));
                  }else if("size".equals(field)){
                      xcb.field(field, Long.parseLong(map.get(field)));
                  }else if("fullFileName".equals(field)){
                      xcb.field(field, map.get(field).toLowerCase());
                  }else{
                      xcb.field(field, map.get(field));
                  }
              }
              xcb.endObject();
              updateRequest.doc(xcb);
            //  getClient().update(updateRequest).get();
              //添加到builder中
              bulkRequest.add(updateRequest);
          }
          
          BulkResponse bulkResponse = bulkRequest.execute().actionGet();
          if (bulkResponse.hasFailures()) {
              System.out.println(bulkResponse.buildFailureMessage());
          }
          bulkRequest=null;
         }
      }catch(Exception e){
          e.printStackTrace();
          return false;
      }
      return true;
  }
	
	public boolean mqUpdateIndexfileContent(Map<String,String> indexmap) {
      
      try{
        Map<String,Object> tempMap = new HashMap<String,Object>();
        tempMap.put("onlinefile", "onlinefile");
        tempMap.put("fileType", indexmap.get("fileType"));
        tempMap.put("fileId", indexmap.get("fileId"));
        String fileContent = this.getMainFileServer().getTextValForFile(tempMap);
          if("".equals(fileContent)) return false;
          String indexName = LuceneWS.FILEINDEXNAME + indexmap.get("companyId");
          String indexType = LuceneWS.FILEINDEXNAME + indexmap.get("companyId");
          List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
          if(typeList.size()>0){
            indexType = typeList.get(0);
          }
              //业务对象
          UpdateRequest updateRequest = new UpdateRequest();
          updateRequest.index(indexName+"_alias");
          updateRequest.type(indexType);
          updateRequest.id(indexmap.get("id"));
          XContentBuilder xcb = XContentFactory.jsonBuilder()
                          .startObject()
                          .field("fileContent",fileContent)
                          .endObject();
          updateRequest.doc(xcb);
          getClient().update(updateRequest).actionGet();
              //添加到builder中
      }catch(Exception e){
          e.printStackTrace();
          return false;
      }
      return true;
  }
	
	public boolean deleteIndex(String companyId,String fileId){
		
		 String indexName = LuceneWS.FILEINDEXNAME + companyId;
		 String indexType = LuceneWS.FILEINDEXNAME + companyId;
         List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
         if(typeList.size()>0){
           indexType = typeList.get(0);
         }
		DeleteResponse response = getClient().prepareDelete().setIndex(indexName+"_alias").setType(indexType).setId(fileId).execute().actionGet();  
		if(response.isFound()){
	      //日志---
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("ip", "");
        log.put("userid", "");
        log.put("loginfo", "删除索引fileId【"+fileId+"】,公司ID【"+companyId+"】");
        log.put("module", "索引");
        log.put("username","");
        log.put("type", "operation");
        log.put("operate", "索引");
        LogUtils.saveBaseLog(compLocator, log);
		}
        return true;
	}
	
	
	private List<Map<String,String>> executeSearch(QueryBuilder queryBuilder, String indexname, String type,int start,int limit,String searchtype,String isGroup,String keyWord,String sortField,SortOrder sortOrder){
		List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
		long a=System.currentTimeMillis();
		try{
		   //xiewenda 执行查询前判断别名是否存在
		    if(!aliasIsExits(indexname+"_alias")){
               this.addAlias(indexname);
            }
		    /** xiewenda
		     * 注意：这里在所有对索引操作的地方 都需要加入这段根据别名查别名下索引的类型
		     * 有些公司因为重建索引后索引和类型已经不是原来的名字了 所以要查一下,复制代码
		     * 千万不要丢掉这个判断 否则会出错！！
		     */
		    List<String> typeList = this.getTypesByIndexAlias(indexname+"_alias");
		    if(typeList.size()>0){
		      type = typeList.get(0);
		    }
			SearchRequestBuilder searchbuilder = getClient().prepareSearch(indexname+"_alias").setTypes(type)
			        .setQuery(queryBuilder).setFrom(start).setSize(limit);
			searchbuilder.addSort("isFile", sortOrder.ASC);
			if(sortOrder!=null){
              searchbuilder.addSort(sortField, sortOrder);
            }
			//高亮
			searchbuilder.addHighlightedField("fileContent").setHighlighterPreTags("<em>").setHighlighterPostTags("</em>")
			             .setHighlighterRequireFieldMatch(true).setHighlighterNumOfFragments(1).setHighlighterFragmentSize(1000);
//			if(!"1".equals(searchtype)){
//				searchbuilder.addSort("ORDERID", SortOrder.DESC);
//			}
			//System.out.println(searchbuilder.toString());
			SearchResponse searchResponse = searchbuilder.execute().actionGet();
	        SearchHits hits = searchResponse.getHits();
	        long b=System.currentTimeMillis();
	        System.out.println("----------调用检索使用时间------------"+(b-a));
	        Map<String,String> firstMap = new HashMap<String, String>();
	        if("1".equals(searchtype)){
	        	firstMap.put("total", hits.getTotalHits()+"");
	        	lst.add(firstMap);
	        	/*if(hits.getTotalHits() == 0L){
	        		ldao.insertKeyWord(keyWord);
	        	}*/
	        }
	        SearchHit[] searchHists = hits.getHits();
	        if(searchHists.length>0){
	            for(SearchHit hit:searchHists){
	            	Map<String,String> map = new HashMap<String, String>();
	            	this.getValues(map, hit, searchtype,isGroup);
	            	lst.add(map);
	            }
	        }
		}catch(Exception e){
			System.out.println("----------------------------------------------------------");
			e.printStackTrace();
			System.out.println("----------------------------------------------------------");
		}
		return lst;
	}
	
	private void getValues(Map<String,String> map,SearchHit hit,String searchType,String isGroup){
	    Map<String,Object>  source = hit.getSource();
	    
		if("1".equals(searchType)){
			String idseq = source.get("idSeq")+"";
			String id = source.get("mysqlId")+"";
			String fileContent = source.get("fileContent")==null ? "":source.get("fileContent")+"";
			String highLightFileContent="";
			if(!"".equals(fileContent)){
		       highLightFileContent = HighlightField("fileContent",hit);
		    if(highLightFileContent.length()==0){
		       if(fileContent.length()>1000){
		       highLightFileContent = fileContent.substring(0, 1000)+"...";
		       }else{
		       highLightFileContent =fileContent.substring(0);
		       }
		    }else if(highLightFileContent.length()<fileContent.length()){
		       highLightFileContent+="...";
		    }
			}
			map.put("id", id);
			map.put("mysqlId", id);
			map.put("fileName", (String)source.get("fileName"));
			map.put("fileId", (String)source.get("fileId"));
			map.put("type", (String)source.get("fileType"));
			map.put("fileContent",highLightFileContent);
			//map.put("userDefinedProp", (String)source.get("userDefinedProp"));
			map.put("idSeq", idseq+id+".");
			map.put("idSeqSrc", idseq);
			String rootIdseq = "1.";
			if(!"1.".equals(idseq)){
				rootIdseq = (idseq==null||idseq.length()==0)?"":idseq.substring(0, idseq.indexOf(".", 2))+".";
			}
			map.put("rootIdseq", rootIdseq);
			
			map.put("version",source.get("version").toString());
			map.put("classId", source.get("classId").toString());
			map.put("openlevel", source.get("openLevel").toString());
			map.put("filesize", source.get("size").toString());
			map.put("createtime", source.get("createTime").toString());
			map.put("isFile", source.get("isFile").toString());
			map.put("userId", source.get("creator").toString());
			map.put("owner", source.get("owner").toString());
		}else{/*
		    String CONTENT = source.get("CONTENT_IK")+"";
		    if(source.get("CONTENT_IK")==null){
		      CONTENT = source.get("CONTENT")+"" ;
		    }
			
			CONTENT = CONTENT.replaceAll(" ", "%20");
			String STYLE =  source.get("STYLE")==null?"":source.get("STYLE").toString() ;
			if(STYLE!=null && !"".equals(STYLE.trim()) && !"undefined".equals(STYLE.toLowerCase().trim()) ){
				CONTENT = "<span%20style='"+STYLE+"'>"+CONTENT+"</span>" ;
			}
			try {
				map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
			} catch (UnsupportedEncodingException e) {
				map.put("CONTENT", "");
				e.printStackTrace();
			}
			map.put("DATE",  source.get("DATE").toString()) ;
			map.put("TIME",  source.get("TIME").toString()) ;
			map.put("ID",  source.get("_id").toString()) ;
			String fromuser = source.get("FROMUSER").toString();
			map.put("FROMUSER",  getUserWS().getUserNameById(fromuser)) ;
			if("1".equals(isGroup)){
				map.put("FROMCNNAME", source.get("FROMCNNAME").toString()) ;
			}
		*/}
	}
	
	private String  HighlightField(String field, SearchHit hit) {
	  // 获取对应的高亮域
	    Map<String, HighlightField> result = hit.highlightFields();
	    // 从设定的高亮域中取得指定域
	    HighlightField titleField = result.get(field);
	    StringBuilder sb = new StringBuilder();
	    if(null!=titleField){
	    // 取得定义的高亮标签
	    Text[] texts = titleField.fragments();
	    // 为title串值增加自定义的高亮标签
	    for (Text text : texts) {
	         sb.append(text);
	    }
	    }
	    return sb.toString();
    }

  private QueryBuilder getQueryBuilder(String keyWord,String idSeq,String loginUserId){
		//Set<String> keyWords = this.getIKAnalyzerWords(keyWord);
         String[] keyWords = keyWord.split(" ");
		//Set<String> wordSet = new HashSet<String>();
		//keyWord = QueryParser.escape(keyWord);
		QueryBuilder queryBuilder = null;
		QueryBuilder q1 = null;
//		if(createrId != null && createrId.trim().length()>0){
//			q1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("creator", createrId));
//		}
		QueryBuilder q2 = null;
		if(idSeq !=null && idSeq.trim().length()>0){
			q2 = QueryBuilders.prefixQuery("idSeq", idSeq);
		}
		//这种检索，是将检索词分词后进行检索的，具体使用哪种，看业务需求吧，分词后检索比直接检索速度稍慢一点。
		//QueryStringQueryBuilder q3 = new QueryStringQueryBuilder(keyWord);  
		//q3.analyzer("ik").field("fileContent");
		BoolQueryBuilder q3 = QueryBuilders.boolQuery();
		for(String word:keyWords){
		   // xiewenda 词检索不支持大写 所以遇到全英文检索词 转化为小写 
		    // String word = keyWords[i];
		     word = QueryParser.escape(word);
		     word = word.toLowerCase();
			//模糊查询
			q3.should(QueryBuilders.wildcardQuery("fullFileName", "*"+word+"*"))
			   //.should(QueryBuilders.wildcardQuery("fullFileName", word+"*"))
			  // .should(QueryBuilders.termQuery("fullFileName_IK", word))
			   .should(QueryBuilders.termQuery("fileContent", word));
			//q3.should(QueryBuilders.matchQuery("fileName", oneKey).minimumShouldMatch("100%"))
			//  .should(QueryBuilders.matchQuery("fileContent", oneKey).minimumShouldMatch("100%"));
		}
		if(q1 != null){
			queryBuilder = QueryBuilders.boolQuery().must(q1);
		}
		if(q2 !=null){
			if(queryBuilder == null){
				queryBuilder = QueryBuilders.boolQuery().must(q2);
			}else{
				queryBuilder = QueryBuilders.boolQuery().must(queryBuilder).must(q2);
			}
		}
		if(queryBuilder==null){
			BoolQueryBuilder q31 = QueryBuilders.boolQuery().must(q3);
					//.must(QueryBuilders.boolQuery().should(QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("openLevel", "3")));
												  // .should(QueryBuilders.termQuery("creator", Integer.parseInt(loginUserId))));
			queryBuilder = QueryBuilders.boolQuery().must(q31).must(QueryBuilders.termQuery("isDelete", "0")).must(QueryBuilders.termQuery("isLast", "1"));
		}else{
			queryBuilder = QueryBuilders.boolQuery().must(queryBuilder).must(q3).must(QueryBuilders.termQuery("isDelete", "0")).must(QueryBuilders.termQuery("isLast", "1"));
		}
		
		return queryBuilder;
	}
	
	
	private QueryBuilder getQuery4Msg(String receiver, String username, String isGroup, String keyword,String fileFlag, String joindate, String jointime,String companyId){
		QueryBuilder queryBuilder = null;
		QueryBuilder q1 = null;
		if("1".equals(isGroup)){
		    if (joindate == null || "".equals(joindate)) {
		        q1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("GROUPFLAG", receiver.toLowerCase()));
		    } else {
		        /** 添加时间戳 **/
		        long timestamp = Long.parseLong(joindate.replaceAll("-", "")+jointime.replaceAll(":", "")) ;
		        q1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("GROUPFLAG", receiver.toLowerCase())).must(QueryBuilders.rangeQuery("TIMESTAMP").from(timestamp));
		    }
		}else{
			String CHATFLAG = this.getTwoPersonChatFlag(username, receiver) ;
			q1 = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("CHATFLAG",  QueryParser.escape(CHATFLAG.toLowerCase().replace("@", "%40"))));
		}
		BoolQueryBuilder q2 = null;
		if(fileFlag!=null && !"".equals(fileFlag.trim())){
			q2 = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("FILEFLAG", fileFlag));
		}
		
		BoolQueryBuilder q3 = null;
		if(keyword!=null && !"".equals(keyword.trim())){
		  //  keyword = keyword.trim().toLowerCase();
			q3 = QueryBuilders.boolQuery();
			//所有内容相关的搜索
			q3.should(QueryBuilders.wildcardQuery("CONTENT", "*"+keyword.toLowerCase()+"*"));
			q3.should(QueryBuilders.wildcardQuery("DATE", "*"+keyword+"*"));
			q3.should(QueryBuilders.wildcardQuery("FROMCNNAME", "*"+keyword+"*"));
			//如果用分词查询 我预留了ik分词字段 满足以后需求变更
			//q3.should(QueryBuilders.termQuery("CONTENT_IK", keyword));
			//q3.should(QueryBuilders.matchQuery("CONTENT", keyword.toLowerCase()).minimumShouldMatch("100%"));
			/*if(keyword.length()==10 && StringUtil.isValidDate(keyword)){
	                  q3.should(QueryBuilders.termQuery("DATE", keyword));
	           }else{
			        
	                }
			   q3.should(QueryBuilders.matchQuery("FROMCNNAME", keyword).minimumShouldMatch("100%"));*/
		}
		
		if(q2==null){
			if(q3 == null){
				queryBuilder = q1;
			}else{
				queryBuilder = QueryBuilders.boolQuery().must(q1).must(q3);
			}
		}else{
			if(q3 == null){
				queryBuilder = QueryBuilders.boolQuery().must(q1).must(q2);
			}else{
				queryBuilder = QueryBuilders.boolQuery().must(q1).must(q2).must(q3);
			}
		}
		return queryBuilder;
	}
	
	
	private String getTwoPersonChatFlag(String userId, String receiverId){
		String CHATFLAG = null ;
		if(Integer.parseInt(userId)>Integer.parseInt(receiverId)){
			CHATFLAG = userId+"_"+receiverId ;
		} else {
			CHATFLAG = receiverId+"_"+userId ;
		}
		return CHATFLAG ;
	}
	
	
	public List<Map<String,String>> search(Map<String,String> map){
		String keyWord = map.get("keyWord");
		String companyId = map.get("companyId");
		String start = map.get("start");
		String limit = map.get("limit");
		String idSeq = map.get("idSeq");
		//String createrId = map.get("createrId");
		
		String searchType = map.get("searchType");//1 是文件搜索，2是消息
		String receiver = map.get("receiver");
		String username = map.get("username");
		String isGroup = map.get("isGroup");
		String joindate = map.get("joindate");
		String jointime = map.get("jointime");
		String loginUserId = map.get("loginUserId");
		String orderField = map.get("orderField");
		String orderType = map.get("orderType");
		if("1".equals(orderField)){
			orderField = "fileName";
		}else if("2".equals(orderField)){
			orderField = "size";
		}else if("3".equals(orderField)){
			orderField = "createTime";
		}else{
			orderField = "createTime";
			orderType = "desc";
		}
		
		//日志---
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "搜索公司ID【"+companyId+"】，关键词【"+keyWord+"】");
		log.put("module", "索引");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("companyName",map.get("companyName"));
		log.put("operate", "索引");
		LogUtils.saveBaseLog(compLocator, log);
		if("1".equals(searchType)){
			if(keyWord!=null){
			    List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
			    int nodes = ((TransportClient)getClient()).connectedNodes().size();
			    if(nodes>0){
			        long a=System.currentTimeMillis();
			        QueryBuilder queryBuilder = this.getQueryBuilder(keyWord,idSeq,loginUserId);
	                SortOrder sortOrder = "asc".equals(orderType)?SortOrder.ASC:SortOrder.DESC ;
	                resultList =this.executeSearch(queryBuilder, LuceneWS.FILEINDEXNAME+companyId, LuceneWS.FILEINDEXNAME+companyId,  Integer.parseInt(start), Integer.parseInt(limit), searchType,isGroup,keyWord,orderField,sortOrder);
	                long b=System.currentTimeMillis();
	                System.out.println("-------es---调用检索使用时间------------"+(b-a));
			    }else{
			      long a=System.currentTimeMillis();
			      resultList = ldao.getFilesByCondition(map);
			      long b=System.currentTimeMillis();
			      System.out.println("-------mysql---调用检索使用时间------------"+(b-a));
			    }
				return resultList;
			}
		}else{
			// String fileFlag = map.get("fileFlag");
			 List<Map<String, String>> resultList = ldao.getMsgByCondition(map);
			 addDataForResult(resultList);
			 // QueryBuilder queryBuilder = this.getQuery4Msg(receiver, username, isGroup, keyWord, fileFlag, joindate, jointime,companyId);
			 //resultList = this.executeSearch(queryBuilder, LuceneWS.FILEINDEXNAME+companyId+"chatrecords"+isGroup, LuceneWS.FILEINDEXNAME+companyId+"chatrecords"+isGroup,  Integer.parseInt(start), Integer.parseInt(limit), searchType,isGroup,keyWord,"ORDERID",SortOrder.DESC);
			 return resultList;
		}
		return new ArrayList<Map<String,String>>();
	}
	

  private void addDataForResult(List<Map<String, String>> resultList) {
    try {
    for (Map<String, String> map2 : resultList) {
      String CONTENT = map2.get("CONTENT");
      CONTENT = CONTENT.replaceAll(" ", "%20");
      String STYLE = map2.get("STYLE");
      if(STYLE!=null && !"".equals(STYLE.trim()) && !"undefined".equals(STYLE.toLowerCase().trim()) ){
          CONTENT = "<span%20style='"+STYLE+"'>"+CONTENT+"</span>" ;
      }
      map2.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
      String fromuser = map2.get("FROMUSER");
      map2.put("FROMUSER",getUserWS().getUserNameById(fromuser));
    }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
	 * 建立索引库的方法
	 */
	private void doCreateIndexWork(){
		List<Map<String,String>> lst = ldao.findAll();
		if(lst!=null && lst.size()>0){
			for(Map<String,String> oneFile:lst){
			    Map<String,String> indexMap = new HashMap<String, String>();
			    String companyId = oneFile.get("COMPANYID");
			    String fileId = oneFile.get("ID");
			    indexMap = getFilesWS().getFileForIndex(companyId, fileId);
			    indexMap.put("COMPANYID", companyId);
			    this.createNewIndex(companyId);
				boolean bol = this.insertFileIndex(LuceneWS.FILEINDEXNAME + companyId,indexMap);
//				String jsondata = new Gson().toJson(indexMap);
//				this.createIndexResponse("file", "demo", jsondata);
				//建完后把这个记录从mongodb中删除
				if(bol){
					delete(oneFile.get("_id"));
				}
			}
			//this.doCreateIndexWork();
		}
		
	}
	
	/*
	 * 这个方法先写成废弃的，暂时不删除
	 * 待确定好建立空索引库的方式后再处理
	 */
//	@Deprecated
//	public void createIndexResponse(String indexname, String type, String jsondata){
//        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
//        IndexRequestBuilder requestBuilder = getClient().prepareIndex(indexname, type).setRefresh(true);
//        requestBuilder.setSource(jsondata).execute().actionGet();
//    }
	
	/**
	 * 创建一个空的索引库
	 * @param indexName
	 * @return
	 */
	private boolean createEmptyIndex(String indexName){
		try{
			getClient().admin().indices().prepareCreate(indexName).execute().actionGet();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 为新创建的索引添加别名
	 * @param indexname
	 * @param indexnamealias
	 * @return
	 */
	public boolean addAlias(String indexname){
	    Client client = getClient();
	    IndicesAliasesResponse response = client.admin().indices().prepareAliases().addAlias(indexname,indexname+"_alias").execute().actionGet();
	    boolean flag = response.isAcknowledged();
	    return flag;
	  }
	private boolean createMsgIndex(String companyId,String msgNum){
		try {
		    String indexName = "company"+companyId+"chatrecords"+msgNum;
			this.createEmptyIndex(indexName);
			IndexResponse result = null;
			//xiewenda 添加索引的别名
			if(this.addAlias(indexName)){
			if(createFileMsgMapping(indexName)){
		    result =getClient().prepareIndex("_river", indexName, "_meta")  
			.setSource(  
				XContentFactory.jsonBuilder().startObject()  
			        .field("type", "mongodb")  
			        .startObject("mongodb")  
			                .field("host",host)  
			                .field("port",mongoport)  
			                .field("db","company_"+companyId)  
			                .field("collection","chatrecords"+msgNum)  
			         .endObject()                                              
			         .startObject("index")  
			                .field("name",indexName)  
			                .field("type",indexName)  
			         .endObject()  
			    .endObject()  
			).execute().actionGet();
			 }
		}
		 return  result==null ? false: result.isCreated();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
     * 创建一个索引库的mapping  
     * @param IndexName  索引库名字必须要小写
     * @return
     */
	 public boolean createFileIndexMapping(String IndexName){
	    Client client = getClient();
	    try {
	        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(IndexName)  
	          .startObject("properties") 
	            //FILENAME
	            .startObject("fileName").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
	            //TYPE
	            .startObject("fileType").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
	            //fullFileName 附加字段 查询字段 文件名+类型
	            .startObject("fullFileName_IK").field("type", "string").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
	            //fileContent 附加字段 文件解析内容
	            .startObject("fileContent").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
	             //用户自定义属性
	            .startObject("userDefinedProp").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
           
	            
	            .startObject("fullFileName").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //FILEID 文件存储标识
	            .startObject("fileId").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //SERIALNUMBER
	            .startObject("serialNumber").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //pinyin 文件名拼音 附加字段 辅助检索
	            .startObject("pinyin").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //IDSEQ
	            .startObject("idSeq").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //md5
	            .startObject("fileMD5").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //CREATETIME
	            .startObject("createTime").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //UPDATETIME
	            .startObject("updateTime").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            
	            //ISFILE
	            .startObject("isFile").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //ISLAST
	            .startObject("isLast").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            //ISDELETE
	            .startObject("isDelete").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            // OPENLEVEL
	            .startObject("openLevel").field("type", "string").field("store", "yes").field("index", "not_analyzed").field("doc_values", "yes").endObject()
	            
	            //SIZE
	            .startObject("size").field("type", "long").field("store", "yes").field("doc_values", "yes").endObject()
	            
	            //id 逻辑主键
	            .startObject("mysqlId").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	            //CLASSID
	            .startObject("classId").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	            //VERSION
	            .startObject("version").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	            //CREATOR
	            .startObject("creator").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	            //OWNER
	            .startObject("owner").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	            //SOLENUMBER
	            .startObject("soleNumber").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	           //PRAISECOUNT
	            .startObject("praiseCount").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()
	           //COLLECTCOUNT
	            .startObject("collectCount").field("type", "integer").field("store", "yes").field("doc_values", "yes").endObject()         
	          .endObject()  
	        .endObject().endObject();
	        PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexName).type(IndexName).source(mapping);  
	        PutMappingResponse response = client.admin().indices().putMapping(mappingRequest).actionGet();
	        return response.isAcknowledged();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }  
	}
	
/*	/**
	 * 创建一个索引库的mapping  
	 * @param IndexName  索引库名字必须要小写
	 * @return
	 *//*
	private boolean createFileIndexMapping(String IndexName){
		try {
			XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(IndexName)  
				.startObject("properties")        
				    //文件名称
                    .startObject("fileName").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
					.startObject("fileName_ik").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
					//文件全名
					.startObject("fullFileName").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                    .startObject("fullFileName_ik").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
                    //文件的内容
                    .startObject("fileContent").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                    .startObject("fileContent_ik").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
					
                    //存文件名称的拼音，排序的时候使用
                    .startObject("pinyin").field("type", "string").field("index", "not_analyzed").endObject()
                    //md5
                    .startObject("fileMD5").field("type", "String").field("index", "not_analyzed").endObject()
                    //文件类型 例如.txt  不分词
                    .startObject("fileType").field("type", "string").field("index", "not_analyzed").endObject()  
                    //创建时间
                    .startObject("createtime").field("type", "String").field("index", "not_analyzed").endObject()
                    //是否被删除的标记，新加了回收站，故加这么个字段。1 是 0 否
                    .startObject("isDel").field("type", "String").field("index", "not_analyzed").endObject()
                    //文件ID  例如 1-42-asdlfkj23h239x  不分词
                    .startObject("fileId").field("type", "string").field("index", "not_analyzed").endObject() 
                    //存分组的，不分词
                    .startObject("idSeq").field("type", "string").field("index", "not_analyzed").endObject()
                    //判断是文件还是文件夹 1 是文件 0是文件夹 其实通过size大小也能够判断，加入这个字段，纯是为了各种奇葩检索要求,不至于上线后再改mapping，那就麻烦了。
                    .startObject("isFile").field("type", "String").field("index", "not_analyzed").endObject()
                    //自定义属性的内容
                    .startObject("userDefinedProp").field("type", "string").field("index", "not_analyzed").endObject()
                    
                    //文件数据库表中的ID  不分词
                    .startObject("mysqlId").field("type", "integer").field("store", "yes").endObject()
                    //文件夹ID  不分词
                    .startObject("classId").field("type", "integer").endObject() 
					//文件上传者id
					.startObject("createrId").field("type", "integer").endObject()
					//文件大小
					.startObject("size").field("type", "integer").endObject()
                    //文件版本号  不分词
                    .startObject("version").field("type", "integer").endObject()
                    //是否最新
                    .startObject("isLast").field("type", "integer").endObject()
                    //公开级别  不分词   文件夹存的都是0    对于文件  1是公司级别公开，2是分类内公开， 3是私有文档
                    .startObject("openlevel").field("type", "integer").endObject() 
			  
				.endObject()  
			.endObject().endObject();
			PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexName).type(IndexName).source(mapping);  
			getClient().admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}  
		return true;
	}*/
	
	
	private boolean createFileMsgMapping(String IndexName){
		try {
			XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(IndexName)  
				.startObject("properties") 
				//数字字段 基本可以不设置分析器 (standard es默认标准分词  如果是字符串字段 按字符分词)
				.startObject("_id").field("type", "long").field("store", "yes").endObject()
				.startObject("ORDERID").field("type", "long").endObject()
				.startObject("TIMESTAMP").field("type", "long").endObject()
				//字符串 全部设置不分词 满足模糊查询
				.startObject("FROMCNNAME").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("FROMUSER").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("GROUPFLAG").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("FILEFLAG").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("CHATFLAG").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("DATE").field("type", "string").field("index", "not_analyzed").endObject()
				.startObject("TIME").field("type", "string").field("index", "not_analyzed").endObject()
				//特殊字段 预留相同内容的ik分词字段 满足以后按分词检索
				.startObject("CONTENT").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
				.startObject("CONTENT_IK").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
				.endObject()  
			.endObject().endObject();
			PutMappingRequest mappingRequest = Requests.putMappingRequest(IndexName).type(IndexName).source(mapping);  
			getClient().admin().indices().putMapping(mappingRequest).actionGet();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}  
		return true;
	}
	
	/**
	 * 向文件索引库插入数据
	 * @param indexName
	 * @param map
	 * @return
	 */
	private boolean insertFileIndex(String indexName,Map<String,String> map){
	    if(!aliasIsExits(indexName+"_alias")){
          this.addAlias(indexName);
        }
	    String indexType = indexName;
	    List<String> typeList = this.getTypesByIndexAlias(indexName+"_alias");
        if(typeList.size()>0){
           indexType = typeList.get(0);
        }
        try {
          Client client = getClient();
          String id = map.get("ID");
          String fileName = map.get("FILENAME");
          String fileType = map.get("TYPE");
          // String serialNumber = map.get("SERIALNUMBER");
          String fullFileName = "";
          if("1".equals(map.get("ISFILE"))){
            fullFileName = fileName+"."+fileType;
          }else{
            fullFileName = fileName;
          }
          String fileId = map.get("FILEID");
          String filesize = map.get("SIZE")==null?"0":map.get("SIZE");
          Long size = Long.parseLong(filesize);
          String fileContent = "";
          Map<String,Object> tempMap = new HashMap<String,Object>();
          tempMap.put("onlinefile", "onlinefile");
          tempMap.put("fileType", fileType);
          tempMap.put("fileId", fileId);
          System.out.println("------"+map+"------");
          //类型不对或是文件超过50兆 不添加索引内容
          boolean flag = false;
          if(StringUtil.isValidFileTypeForIndex(fileType)){
            if(size<52428800){
                //fileContent = this.getFileContentString(fileId,fileType);
                fileContent = this.getMainFileServer().getTextValForFile(tempMap);
                // System.out.println("fileContent"+fileContent.length());
                //if("".equals(fileContent)) map.put("fileContent", "");
             }else{
                 tempMap.put("companyId", map.get("COMPANYID"));
                 tempMap.put("id", id);
                 fileContent = "文件太大正在努力解析文件内容...";
                 flag = true;
             }
          }
          String pinyin = new PinYin().getStringPinYin(fileName);
          if(pinyin.length()>100){
             pinyin = pinyin.substring(0,96);
          }
          XContentBuilder doc = XContentFactory.jsonBuilder()  
              .startObject()       
                .field("fileName", fileName)
                .field("fileType", fileType)
                .field("fullFileName", fullFileName.toLowerCase())
                .field("fullFileName_IK", fullFileName)
                .field("fileContent", fileContent)
                .field("userDefinedProp", "")
                
                .field("fileId", fileId)
                .field("pinyin", pinyin)
                .field("serialNumber", map.get("SERIALNUMBER"))
                .field("idSeq", map.get("IDSEQ"))
                .field("fileMD5", map.get("MD5"))
                .field("createTime", map.get("CREATETIME"))
                .field("updateTime", map.get("UPDATETIME"))
                .field("isFile", map.get("ISFILE"))
                .field("isLast", map.get("ISLAST"))
                .field("isDelete", map.get("ISDELETE"))
                .field("openLevel", map.get("OPENLEVEL"))
                
                .field("size", size)
                
                .field("mysqlId", Integer.parseInt(id))  
                .field("classId", map.get("CLASSID")!=null ? Integer.parseInt(map.get("CLASSID")):null)  
                .field("version", map.get("VERSION")!=null ? Integer.parseInt(map.get("VERSION")):null)  
                .field("creator", map.get("CREATOR")!=null ? Integer.parseInt(map.get("CREATOR")):null)  
                .field("owner", map.get("OWNER")!=null ? Integer.parseInt(map.get("OWNER")):null)  
                .field("soleNumber", map.get("SOLENUMBER")!=null ? Integer.parseInt(map.get("SOLENUMBER")):null)  
                .field("praiseCount",map.get("PRAISECOUNT")!=null ? Integer.parseInt(map.get("PRAISECOUNT")):null)  
                .field("collectCount", map.get("COLLECTCOUNT")!=null ? Integer.parseInt(map.get("COLLECTCOUNT")):null)  
                         
              .endObject();
          IndexResponse response = client.prepareIndex(indexName+"_alias",indexType).setSource(doc).setId(id).execute().actionGet();
          
          if(response.isCreated()&&flag){
            //开启任务队列获取文件内容 更新索引
            boolean bo = getProducerWS().doIndexSender("indexQueue", tempMap);
            if(bo){
                   getconsumerWS().doIndexReceiver("indexQueue");
            }
          }
          return true;

          } catch (Exception e) {
            e.printStackTrace();
            return false;
          } 
    }
	
	public Map<String,String> createNewIndex(String companyId){
		Map<String,String> map = new HashMap<String, String>();
	    String indexName = LuceneWS.FILEINDEXNAME+companyId;
		if(!this.indexIsExits(indexName)){
			if(this.createEmptyIndex(indexName)){
			  if(this.addAlias(indexName)){
				if(this.createFileIndexMapping(indexName)){
					/*//做同步
					if(companyId!=null && !companyId.contains("user_")){
						this.createMsgIndex(companyId, "0");
						this.createMsgIndex(companyId, "1");
					}*/
					map.put("success", "true");
				}else{
					map.put("success", "false");
					map.put("msg", "createFileIndexMapping");
				}
			  }else{
			    map.put("success", "false");
                map.put("msg", "createIndexAlisa");
			  }
			}else{
				map.put("success", "false");
				map.put("msg", "createEmptyIndex");
			}
		}else{
			map.put("success", "false");
			map.put("msg", "exits");
		}
		return map;
	}
	
	
	private boolean insert(Map<String,String> fileInfo){
		return ldao.insert(fileInfo);
	}
	
	private boolean delete(String id){
		return ldao.delete(id);
	}
	
	/**
	 * 执行线程，使用线程池
	 * 保证同一时刻只有一个进程在执行
	 * 线程池队列大小为1，采用拒绝策略，故不会影响系统内存的开销
	 * 线上如果单线程无法满足，可以改成每个公司一条线程
	 */
	public void execThread(){
		INDEXPOOL.execute(new Thread(){
			 @Override
			 public void run() {
				 doCreateIndexWork();
			 }
		});
	}
	
	public List<String> getTypesByIndexAlias(String idexNameAlias) {
	    Client client = this.getClient();
	    List<String> riverType = new ArrayList<String>(); 
	    GetMappingsResponse res = client.admin().indices().getMappings(new GetMappingsRequest().indices(idexNameAlias)).actionGet();
	    Iterator<ObjectObjectCursor<String,ImmutableOpenMap<String,MappingMetaData>>> mappings  = res.mappings().iterator();
	    //因为每个别名下 现在只有一个映射所以取一次
	    if(mappings.hasNext()){
	      ObjectObjectCursor<String,ImmutableOpenMap<String,MappingMetaData>> type = mappings.next();
	       riverType.add(type.key);
	    }
	    return riverType;
	  }
	
	  public Set<String> getIKAnalyzerWords(String str){
	      TokenStream ts = null;
	      Set<String> wordSet = new HashSet<String>();
	      try {
	        Analyzer analyzer = new IKAnalyzer(true);
	        ts = analyzer.tokenStream("", new StringReader(str));

	        //OffsetAttribute offset = (OffsetAttribute)ts.addAttribute(OffsetAttribute.class);

	        CharTermAttribute term = (CharTermAttribute)ts.addAttribute(CharTermAttribute.class);

	        //TypeAttribute type = (TypeAttribute)ts.addAttribute(TypeAttribute.class);

	        ts.reset();

	        while (ts.incrementToken()) {
	          //System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
	          wordSet.add(term.toString());
	        }

	        ts.end();
	      }catch (IOException e) {
	        e.printStackTrace();
	        if (ts != null)
	          try {
	            ts.close();
	          } catch (IOException e1) {
	            e1.printStackTrace();
	          }
	      }finally
	      {
	        if (ts != null)
	          try {
	            ts.close();
	          } catch (IOException e) {
	            e.printStackTrace();
	          }
	      }
	      return wordSet;
	  }
	  @Override
	  public Map<String, List<Map<String, String>>> searchAndroid(Map<String, String> map) {
	    // TODO Auto-generated method stub
	    return null;
	  }
	  
	  
//↓--------------------------- xiewenda 重建索引 ------------------------------↓
  @Override
  public void addTrigger() {
    //查询要重建的公司总数
    Map<String,Map<String,String>> companyMap = this.getCompanyRegistWS().getAllCompanyInfo();
    //创建线程池准备多线程重建索引
    //检测当前cpu空闲数，决定要创建线程的数量
    int cpus = Runtime.getRuntime().availableProcessors();
    int maxThreads = cpus * 1;
    //设置线程池最大10最小2
    maxThreads = maxThreads < 2 ? 2 : (maxThreads >10 ? 10 : maxThreads);
    ExecutorService executorService =
        new ThreadPoolExecutor(
            maxThreads, // 核心线程池大小
            maxThreads, // 最大线程池大小
            1, // 重置线程池的等待时间
            TimeUnit.MINUTES, 
            new ArrayBlockingQueue<Runnable>(maxThreads, true),
            new ThreadPoolExecutor.CallerRunsPolicy());//拒绝回调策略
    //循环公司id开始重建索引
    for(String id :companyMap.keySet()){
      @SuppressWarnings("unused")
      final String companyname= "company_"+id;
      executorService.submit(new Runnable() {
        @Override
        public void run() {
            try {
             //重建文件索引
             //reCreateFileIndex(companyname);
             //先只重建每个公司chatrecords1的聊天索引
             //reCreateRiver(companyname,"1");
            } catch (Exception ex) {
              ex.printStackTrace();
            }
        }
    });
      //结束线程服务,会等待线程池队列执行完毕
      executorService.shutdown();
      try {
          if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
              //等待线程池结束第一次
              executorService.shutdownNow();
          }
       
          if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
              // 等待线程池结束第二次
               executorService.shutdownNow();
          }
      } catch (InterruptedException ex) {
          executorService.shutdownNow();
          Thread.currentThread().interrupt();
      }
        
    }

   /* try {
      JobDetail jobDetail = new JobDetail();
      jobDetail.setName("helloWorldJob");//任务名称
      jobDetail.setGroup(Scheduler.DEFAULT_GROUP);//任务组名
      jobDetail.setJobClass(Class.forName("cn.flying.rest.onlinefile.lucene.LuceneJob"));//调用的任务类
    
     SimpleTrigger trigger = new SimpleTrigger("simpleTrigger",Scheduler.DEFAULT_GROUP);
     trigger.setStartTime(new Date());//开始时间
     trigger.setEndTime(null);//结束时间
     trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);//重复次数
     trigger.setRepeatInterval(1000);//执行间隔
     System.out.println(scheduler);
     this.scheduler.scheduleJob(jobDetail, trigger);
    } catch (Exception e) {
      e.printStackTrace();
    }  */
  }

  public void reCreateFileIndex(String indexnameBase){
    long start = System.currentTimeMillis();
    SimpleDateFormat time=new SimpleDateFormat("yyyyMMdd"); 
    String date = time.format(new Date());
    //索引创建的基础名
    // String indexnameBase = "mongodbdata";
    //索引创建的别名
    String indexnameAlias = indexnameBase+"_"+"alias";
    //此次要创建的索引名称
    String indexname = indexnameBase+"_"+date;
     try {
       //重建新索引
      //为了防止索引的重复 这里简单做个判断 找到不存在的索引名
      int num = 0;
      while(indexIsExits(indexname)){
        indexname = indexname+"_"+num;
        num++;
      }
      if(createEmptyIndex(indexname)){
       //建立索引映射
      if(createFileIndexMapping(indexname)){
        //查询此别名下的关联索引
        List<String> indexNameList = getIndexNameFromAliasName(indexnameAlias);
      //查询要复制的索引数据 全部加入新建的索引中
        if(scrollSearchInsert(indexname,indexnameAlias)){
         //切换索引别名 完成重建索引
          long start1 = System.currentTimeMillis();
          if(replaceAlias(indexnameBase,indexname,indexnameAlias)){
          long end1 = System.currentTimeMillis();
          System.out.println("切换索引时间："+(end1-start1)+"ms");
            //删除别名下原来的索引
            boolean flag = true;
            for(String index : indexNameList){
              flag = flag&this.deleteIndex(index);
             }
            if(flag){
              throw new RuntimeException("重建成功!");
            }else{
              throw new RuntimeException("删除索引时失败");
            }
           }
         }
       }
      }
     } catch (Exception e) {
       e.printStackTrace();
     } 
     long end = System.currentTimeMillis();
     System.out.println("执行时间："+(end-start)+"ms");
   }
  
  public boolean scrollSearchInsert(String indexName,String indexNameAlias){
    try {
    Client client = this.getClient();
   // QueryBuilder qb = QueryBuilders.matchAllQuery();
    SearchResponse scrollResp = client.prepareSearch(indexNameAlias)
        .setSearchType(SearchType.SCAN)
        .setScroll(new TimeValue(600000))
        .setSize(1000)
        .execute().actionGet(); //100 hits per shard will be returned for each scroll
      //建立批处理对象
      while (true) {
        scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
        //Break condition: No hits are returned
         if (scrollResp.getHits().getHits().length == 0) {
             break;
         }
       // System.out.println(scrollResp.getHits().getHits().length);
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        //遍历索引内容
       for (SearchHit hit : scrollResp.getHits().getHits()) {
         //构建索引source
         Map<String,Object> source = hit.getSource();
         XContentBuilder content = XContentFactory.jsonBuilder()
           .startObject()
           .field("loginfo", source.get("loginfo"))
           .field("log_year", source.get("log_year"))
           .field("log_quarter", source.get("loginfo"))
           .field("appId", source.get("appId"))
           .field("log_day", source.get("log_day"))
           .field("instanceId", source.get("instanceId"))
           .field("log_month", source.get("log_month"))
           .field("type", source.get("type"))
           .field("organpath", source.get("organpath"))
           .field("log_module", source.get("log_module"))
           .field("fileSubscribMsg", source.get("fileSubscribMsg"))
           .field("fileId", source.get("fileId"))
           .field("username", source.get("username"))
           .field("_id", source.get("_id"))
           .field("address", source.get("address"))
           .field("operate", source.get("operate"))
           .field("log_date", source.get("log_date"))
           .field("organfullname", source.get("organfullname"))
           .field("log_time", source.get("log_time"))
           .endObject();
         //System.out.println(hit.getId());
         //指定请求对象
         IndexRequestBuilder request = client.prepareIndex(indexName, indexName,hit.getId()).setSource(content);
         //加入批处理中
         bulkRequest.add(request);
        
        }
       System.out.println("---------------"+bulkRequest.numberOfActions());
       if(bulkRequest.numberOfActions()>0){
         BulkResponse bulkResponse = bulkRequest.execute().actionGet();
         if (bulkResponse.hasFailures()) {
             System.out.println(bulkResponse.buildFailureMessage());
         }
       }
         
      } 
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } 
    return true;
  }

  
  //切换别名
  public boolean replaceAlias(String indexnamebase,String indexname,String indexnamealias){
    Client client = this.getClient();
    IndicesAliasesResponse response = client.admin().indices().prepareAliases().removeAlias(indexnamebase+"*", indexnamealias).addAlias(indexname,indexnamealias).execute().actionGet();
    boolean flag = response.isAcknowledged();
    return flag;
  }

  //通过别名获得别名下的 所有索引名称
  public List<String> getIndexNameFromAliasName(String aliasName) {
    
    Client client = this.getClient();
    ImmutableOpenMap<String, AliasMetaData> indexToAliasesMap = client.admin().cluster()
            .state(Requests.clusterStateRequest())
            .actionGet()
            .getState()
            .getMetaData()
            .aliases().get(aliasName);
    List<String> indexNameList = new ArrayList<String>();
    if(indexToAliasesMap != null && !indexToAliasesMap.isEmpty()){
     String indexname = indexToAliasesMap.keys().iterator().next().value;
     indexNameList.add(indexname);
    }
    return indexNameList;
 }
  //删除索引
  public boolean deleteIndex(String indexName){
    Client client = this.getClient();
    DeleteIndexResponse response = client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
    client.admin().indices().flush(new FlushRequest(indexName)).actionGet();
    return response.isAcknowledged();
  }
  public boolean deleteIndexType(String type){
    Client client = this.getClient();
    DeleteMappingRequest typeMapping=new DeleteMappingRequest("_river").types(type);
    DeleteMappingResponse actionGet = client.admin().indices().deleteMapping(typeMapping).actionGet();
    return actionGet.isAcknowledged();
  }
  
  public void reCreateRiver(String company,String type){
    long start = System.currentTimeMillis();
    SimpleDateFormat time=new SimpleDateFormat("yyyyMMdd"); 
    String date = time.format(new Date());
    //重建索引类型
    // String type = "1";
    //索引创建的基础名
    String indexnameBase = company+"chatrecords"+type;
    //索引创建的别名
    String indexnameAlias = indexnameBase+"_alias";
    //此次要创建的索引名称
    String indexname = indexnameBase+"_"+date;
     try {
       //重建新索引
      //为了防止索引的重复 这里简单做个判断 找到不存在的索引名
      int num = 0;
      while(indexIsExits(indexname)){
        indexname = indexname+"_"+num;
        num++;
      }
      if(createEmptyIndex(indexname)){
        //建立索引映射
      if(createFileMsgMapping(indexname)){
        //查询此别名下的关联索引
        List<String> indexNameList = getIndexNameFromAliasName(indexnameAlias);
        //当前公司的river类型
        List<String> indexTypeList = getTypesByIndex(indexnameBase);
        //查询要复制的索引数据 全部加入新建的索引中
        if(createRiver(indexname,company,type)){
        //切换索引别名 完成重建索引
          long start1 = System.currentTimeMillis();
          if(replaceAlias(indexnameBase,indexname,indexnameAlias)){
          long end1 = System.currentTimeMillis();
          System.out.println("切换索引时间："+(end1-start1)+"ms");
          //先删除river类型
          for (String indexType : indexTypeList) {
                deleteIndexType(indexType);
             }
            //删除别名下原来的索引
            boolean flag = true;
            for(String index : indexNameList){
              flag = flag&this.deleteIndex(index);
             }
              if(flag){
                System.out.println("重建成功!");
              }else{
                System.out.println("删除索引时失败,请联系管理员进行维护！");
              }
           }
         }
       }
      }
     } catch (Exception e) {
       System.out.println("重建异常,请联系管理员！");
       e.printStackTrace();
     } 
     long end = System.currentTimeMillis();
     System.out.println("执行时间："+(end-start)+"ms");
   }
  //为消息数据重建river
  private boolean createRiver(String indexname,String company,String type){
    try {
    Client client = this.getClient();
    String typename = "chatrecords"+type;
    IndexResponse response = client.prepareIndex("_river", indexname, "_meta")  
      .setSource(  
          XContentFactory.jsonBuilder().startObject()  
              .field("type", "mongodb")  
              .startObject("mongodb")  
                      .field("host",host)  
                      .field("port",mongoport)  
                      .field("db",company)  
                      .field("collection",typename)  
               .endObject()                                              
               .startObject("index")  
                      .field("name",indexname)  
                      .field("type",indexname)  
               .endObject()  
          .endObject()  
      ).execute().actionGet();
      return response.isCreated();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  //根据索引查找索引下某一公司消息的所有类型
  public List<String> getTypesByIndex(String indexnameBase){
    Client client = this.getClient();
    List<String> riverType = new ArrayList<String>(); 
    GetMappingsResponse res = client.admin().indices().getMappings(new GetMappingsRequest().indices("_river")).actionGet();
    ImmutableOpenMap<String, MappingMetaData> mapping  = res.mappings().get("_river");
    if(mapping!=null && !mapping.isEmpty()){
      for (ObjectObjectCursor<String, MappingMetaData> type : mapping) {
          if(type.key!=null && type.key.startsWith(indexnameBase))
           riverType.add(type.key);
      }
    }
    return riverType;
  }

}
//↑---------------------------------xiewenda 重建索引  ----------------------------------------↑