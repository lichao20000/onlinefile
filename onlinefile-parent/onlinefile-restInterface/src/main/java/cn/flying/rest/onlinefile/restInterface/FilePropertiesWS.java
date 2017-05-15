package cn.flying.rest.onlinefile.restInterface;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import cn.flying.rest.platform.utils.MediaTypeEx;

public interface FilePropertiesWS {

	@POST
	@Path("getFilePropLst")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getFilePropLst(Map<String,String> paras);
	
	/**
	 * 本方法最后直接返回List<Map<String,String>>格式的json串
	 * 如果在方法中使用 请使用GSON工具类自行转换
	 * @param companyId
	 */
	@POST
	@Path("getFilePropLstByCompany")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<Map<String,String>> getFilePropLstByCompany(String companyId);
	
	@POST
	@Path("addFileProp")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> addFileProp(Map<String,String> filePropBeanMap);
	
	@POST
	@Path("delFileProp")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> delFileProp(Map<String,String> filePropBeanMap);
	
	@POST
	@Path("getFilePropById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> getFilePropById(Map<String,String> paras);
	
	@POST
	@Path("updateFilePropById")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> updateFilePropById(Map<String,String> paras);
	
}
