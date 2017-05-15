package cn.flying.rest.onlinefile.restInterface;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import cn.flying.rest.platform.utils.MediaTypeEx;

public interface DocumentClassWS{

	@POST
	@Path("getCateList")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map getCateList(Map<String,String> params);

	@POST
	@Path("addCate")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map addCate(Map<String,String> params);

	@POST
	@Path("delCate")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map delCate(Map<String,String> params);

	@POST
	@Path("editCate")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map editCate(Map<String,String> params);

	@POST
	@Path("getCateById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map getCateById(Map<String,String> params);
	/*------------------------------------------------------*/
	
	@POST
	@Path("getUserListClass")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List getUserListClass(Map<String,String> params);
	@POST
	@Path("addClassByName")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map addClassByName(Map<String,String> params);
	@POST
	@Path("deleteFileClassById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map deleteFileClassById(Map<String,String> params);
	@POST
	@Path("deleteClassById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map deleteClassById(Map<String,String> params);
	@POST
	@Path("reClassNameById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map reClassNameById(Map<String,String> params);
	@POST
	@Path("gerClassList")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List gerClassList(Map<String,String> params);
	@POST
	@Path("editClassId")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map editClassId(Map<String,String> params);
	@POST
	@Path("addClassByNameAndCreateGroup")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map addClassByNameAndCreateGroup(Map<String,String> params);
	@POST
	@Path("getClassInfo")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map getClassInfo(Map<String,String> params);
	@POST
	@Path("editClassInfo")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map editClassInfo(Map<String,String> params);
	
	//模板请求
	@GET
	@Path("getClassInfo")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getClassInfo(@Context HttpServletRequest request, @Context HttpServletResponse response);

	/**
     * 分组移交
     * @param request
     * @param response
     */
	@POST
	@Path("changeGroupAdmin")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> changeGroupAdmin(Map<String,String> params);
	
	
	
	/** lujixiang 20150810  批量删除文件夹和文件
     * @param request
     * @param response
     */
	@GET
    @Path("deleteFilesAndFolders")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void deleteFilesAndFolders(@Context HttpServletRequest request, @Context HttpServletResponse response);
}
