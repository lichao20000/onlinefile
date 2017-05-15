package cn.flying.rest.onlinefile.restInterface;

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

import org.apache.http.HttpRequest;

import cn.flying.rest.platform.utils.MediaTypeEx;


public interface CompanyRegistWS {
	
	  @POST
	  @Path("sendEmail")
	  @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	  @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	  public Boolean sendEmail(Map<String, String> temp);
	  
	  @POST
	  @Path("saveCompany")
	  @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	  @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	  public String saveCompany(Map<String, String> params);
	  
	  @GET
	  @Path("test")
	  @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	  public Boolean test();

	  
	/** 
	 * liuwei 20150518
	 * 根据公司id获取公司详细信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("getCampanyInfoById")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> getCampanyInfoById(Map<String, String> params);
	
	/** 
	 * liuwei 20150518
	 * 修改公司信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("editCampanyInfo")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> editCampanyInfo(Map<String, String> params);
	/**
	 * liuwei 20150518
	 * 根据用户公司名称获得他公司logo路径
	 * @param params
	 * @return
	 */
	/*@POST
	@Path("getImagePath")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getImagePath(Map<String, String> params);*/
	/**
	 * liuwei 20150518 
	 * 保存公司咯logo图片
	 * @param params
	 * @return
	 */
	@POST
	@Path("saveHeadImage")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> saveHeadImage(Map<String, String> params);
	/**
	 * 安卓修改公司LOGO
	 * @param 
	 * @param 
	 */
	@POST
	@Path("uploadHeadImage")
	@Consumes(MediaTypeEx.MULTIPART_FORM_DATA)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> uploadHeadImage(@Context HttpServletRequest request); 

   @GET
   @Path("validateUrl/{start}")
   @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
   public Boolean validateUrl(@PathParam("start") String start);
   
   /**
	 * liuwei 20150518 
	 * 保存公司咯logo图片
	 * @param params
	 * @return
	 */
	@POST
	@Path("getAllCompanyInfo")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Map<String,String>> getAllCompanyInfo();
	  

	/**获取推荐信息**/
	@GET
	@Path("getCompanyReferrer")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getCompanyReferrer(@Context HttpServletRequest request,@Context HttpServletResponse response);  

	@POST
	@Path("registerNewCampany")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> registerNewCampany(Map<String, String> params);
	
	@POST
	@Path("getCompanyReferrer")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> getCompanyReferrer(Map<String, String> param);
	
	 /**20151020 xiayongcai
	  * @param params 企业名称
	  * @return 判断企业名称是否存在
	  * */
	 @POST
	 @Path("isCompayNameExist")
	 @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	 @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	 public Boolean isCompayNameExist(String compayName);
	 
	 
	 /**20151020 xiayongcai
	  * @param params 用户名ID，企业ID
	  * @return succees：true ，false
	  * msg 
	  * */
	@POST
	@Path("switchoverCompany")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> switchoverCompany(Map<String, String> param);
	/**
	 * 注销企业
	 */
	@POST
	@Path("cancelCompany")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> cancelCompany(Map<String, String> param);
	
	/**
	 *移交公司
	 */
	@POST
	@Path("getCompanyUserForTransfer")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> getCompanyUserForTransfer(Map<String, String> param);
	/**
	 *移交公司
	 */
	@POST
	@Path("transferCompany")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> transferCompany(Map<String, String> param);
}
