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

import cn.flying.rest.onlinefile.entity.EmailAttachment;
import cn.flying.rest.onlinefile.entity.EmailEntity;
import cn.flying.rest.platform.utils.MediaTypeEx;

public interface EmailWS {

	/**
	 * 添加邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("addEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> addEmail(Map<String, String> map);
	
	/**
	 * 手动添加邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("addEmailManual")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> addEmailManual(Map<String, String> map);

	/**
	 * 删除邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("deleteEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> deleteEmail(Map<String, String> map);
	
	/**
	 * 清空邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("deleteAllEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> deleteAllEmail(Map<String, String> map);

	/**
	 * 修改邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("updateEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> updateEmail(Map<String, String> map);

	/**
	 * 查询邮箱附件
	 * 
	 * @author liuhezeng 20150403
	 * @param 参数
	 * @return 
	 */
	@GET
	@Path("getEmailList")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getEmailList(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 获取所有邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("searchEmailAttachment")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> searchEmailAttachment(Map<String, String> map);
	
	
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("getAttachmentByEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getAttachmentByEmail(Map<String,String> map);
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("getDefaultAttachmentByEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getDefaultAttachmentByEmail(Map<String, String> map);
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("getAllAttachments")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<Map<String, String>> getAllAttachments(Map<String, String> map);
	
	/**
	 * 根据邮箱地址获取邮箱配置信息
	 * 
	 * @author liuhezeng 20150320
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("getEmailSettingByEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getEmailSettingByEmail(Map<String, String> map);
	
	
	
	
	/**
	 * 保存邮箱设置
	 * 
	 * @author liuhezeng 20150320
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("saveEmailSetting")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> saveEmailSetting(Map<String, String> map);
	
	/**
	 * 设为常用邮箱
	 * 
	 * @author liuhezeng 20150413
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("setAsDefaultEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> setAsDefaultEmail(Map<String, String> map);
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("cacheAllEmailAttachments")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> cacheAllEmailAttachments(Map<String, String> map);
	
	/**
	 * 缓存默认邮箱
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	@GET
	@Path("cacheDefaultAllEmailAttachments")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void cacheDefaultAllEmailAttachments(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 是否同步邮箱
	 * 
	 * @author liuhezeng 20150414
	 * @param 参数
	 * @return
	 */
	@POST
	@Path("isSynEmailAttachMent")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> isSynEmailAttachMent(Map<String,String> map);
	
	/**
	 * 获取邮件内容（html）
	 * 
	 * @author liuhezeng 20150414
	 * @param 参数
	 * @return
	 */
	@GET
	@Path("getEmailAttachMentInfoByEmail")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getEmailAttachMentInfoByEmail(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 获取邮件附件
	 * 
	 * @author wangwenshuo 20150507
	 * @param 参数
	 * @return
	 */
	@GET
	@Path("getEmailAttachMentsByEmail")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getEmailAttachMentsByEmail(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 获取默认邮箱
	 * 
	 * @author liuhezeng 20150414
	 * @param 参数
	 * @return
	 */
	@GET
	@Path("getDefaultEmail")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getDefaultEmail(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	@GET
	@Path("downloadAttachMent")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void downloadAttachMent(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
}
