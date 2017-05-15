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

import cn.flying.rest.platform.utils.MediaTypeEx;

	public interface WechatWS {
	/**
     * 获取社区帖子的信息
     * @param request
     * @param response
     */
    @GET
	@Path("getCommunitylist")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCommunitylist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    /**
     * 获取社区帖子的分类
     * @param request
     * @param response
     */
    @GET
	@Path("getCommunityTypelist")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCommunityTypelist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    /**
     * 发帖子的信息
     * @param request
     * @param response
     */
   // @GET
   // @Path("publishCommunitylist")
   // @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
   // public void publishCommunitylist(@Context HttpServletRequest request,@Context HttpServletResponse response);
    	
    /**
     * liuwei 20150530
     * 用户发帖
     * @param params
     * @return
     */
     @POST
	 @Path("publishCommunitylist")
     @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
 	 @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	 public Map<String,String> publishCommunitylist(Map<String, String> params);
    
    /**
     * 获取帖子内容
     * @param request
     * @param response
     */
    @GET
    @Path("getCommunityArticle")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void getCommunityArticle(@Context HttpServletRequest request,@Context HttpServletResponse response);
    /**
     * 获取用户评论
     * @param request
     * @param response
     */
    @GET
	@Path("getReplylist")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getReplylist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    /**
     * 用户发表评论
     * @param request
     * @param response
     */
    @POST
	@Path("showReplylist")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> showReplylist(Map<String, String> params);
    
    
    /**
     * 用户评论数
     * @param request
     * @param response
     */
    @POST
	@Path("getCommentTotal")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public String getCommentTotal(Map<String, String> params);
    
    
/*   @GET
	@Path("showReplylist")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void showReplylist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    */
    /**
     *liuwei 20150525 
     * 删除用户评论信息
     * @param request
     * @param response
     */
    @GET
    @Path("deleteComment")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void deleteComment(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    /**
     * 获取社区自己帖子的信息
     * @param request
     * @param response
     */
    /*@GET
	@Path("getCommunityUserlist")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCommunityUserlist(@Context HttpServletRequest request, @Context HttpServletResponse response);*/
    /**
     * 删除文章
     * @param request
     * @param response
     */
    @GET
	@Path("deleteCommunitytie")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void deleteCommunitytie(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    @POST
    @Path("getinfocommunity")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String getinfocommunity(Map<String,String> map);
    
    /**
     * 获取当前登录用户发送的所有帖子
     * @param request
     * @param response
     */
    @GET
    @Path("getMyCommunity")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void getMyCommunity(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    /**
     * 编辑用户发贴内容
     * @param request
     * @param response
     */
    @GET
    @Path("editComunityContext")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void editComunityContext(@Context HttpServletRequest request,@Context HttpServletResponse response);
	
    /**
     * 点赞/取消赞文件
     */
    @POST
    @Path("praiseCard")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String praiseCard(Map<String, String> params);
    
    /**
     * 获取新消息提示条数
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("getCountReplyInfo")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void getCountReplyInfo(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    
    /**
     * 获取新消息提
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("getCallBackNewMessage")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void getCallBackNewMessage(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    /**
     * 更新新消息状态
     * @param request
     * @param response
     */
    @GET
    @Path("updateMessageState")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void updateMessageState(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    /**
     * 更新全部消息状态为已读
     * @param request
     * @param response
     */
    @GET
    @Path("updateMessageAllState")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void updateMessageAllState(@Context HttpServletRequest request,@Context HttpServletResponse response);
    
    
    
    
    
    
}
