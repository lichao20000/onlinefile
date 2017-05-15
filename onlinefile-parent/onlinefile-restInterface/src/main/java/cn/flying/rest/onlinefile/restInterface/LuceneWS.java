package cn.flying.rest.onlinefile.restInterface;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import cn.flying.rest.platform.utils.MediaTypeEx;

public interface LuceneWS {

	public static final String FILEINDEXNAME = "company_";
	
	@POST
    @Path("createIndex")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> createIndex(Map<String,String> map);
	
	@GET
    @Path("createNewIndex/{indexName}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> createNewIndex(@PathParam("indexName") String indexName);
	
	@POST
    @Path("updateIndex")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean updateIndex(Map<String,String> map);
	
	@POST
    @Path("updateIndexOfDel")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public boolean updateIndexOfDel(Map<String,Object> map) ;
	
	@GET
    @Path("deleteIndex/{companyId}/{fileId}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public boolean deleteIndex(@PathParam("companyId")String companyId,@PathParam("fileId")String fileId);
	
	@POST
    @Path("searchAndroid")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String ,List<Map<String,String>>> searchAndroid(Map<String,String> map);
	
	@POST
    @Path("search")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<Map<String,String>> search(Map<String,String> map);
	
	
	@GET
	@Path("addTrigger")
	public void addTrigger();
	
	@POST
    @Path("updateIndexForList")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public boolean updateIndexForList(List<Map<String,String>> list) ;
	
	/**
	 * 用于设置私密文件 wangwenshuo 20150917
	 * @param list
	 * @return
	 */
	@POST
	@Path("updateIndex4Unshare")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public boolean updateIndex4Unshare(List<Map<String,String>> list) ;
	
	/**
	 * 设置索引文件的 islast字段
	 * @param list
	 * @return
	 */
	@POST
	@Path("updateIndexOfisLast")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public boolean updateIndexOfisLast(List<Map<String,String>> data,String companyId);
	
	public boolean mqUpdateIndexfileContent(Map<String,String> data);
	
	public boolean updateIndexComment(Map<String,List<Map<String,String>>> indexmap);
}
