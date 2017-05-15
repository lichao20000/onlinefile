package cn.flying.rest.onlinefile.wechat.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.flying.rest.onlinefile.utils.BaseDao;

@SuppressWarnings("rawtypes")
public interface weChatDao extends BaseDao {
	/**
	 * 返回所有帖子
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, String>> getCommnunityMessage(int page,int limit);
	/**
	 * 发布帖子
	 * @param param
	 * @return 
	 */
	public boolean publisCommnunity(String username,String time,String title,String userinfo,String realtitle,String pubtype);
	/**
	 * 获得每个帖子的内容
	 * @param param
	 * @return 
	 */
	public Map<String,String> getCommunityArticle(String user);
	/**
	 * 获得每类帖子的信息
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, String>> getCommnunityTypeMessage(String type,int page,String username,int limit);
	/**
	 * 添加评论
	 * @param param
	 * @return 
	 */
	public boolean showReplylist(String pl_context_id,String pl_name,String pl_info,String pl_userid);
	/**
	 * 获得评论信息
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, String>> getReplylist(String pl_context_id,int page ,int limit);
	/**
	 * 删除用户评论
	 * @param pl_id 评论id
	 * @return
	 */
	public boolean deleteComment(String pl_id,String userName,int contextId);
	/**
	 * 获得每人自己帖子的信息
	 * @param param
	 * @return 
	 */
//	public List<HashMap<String, String>> getCommnunityuserlist(String username);
	/**
	 * 删除每条帖子的信息
	 * @param param
	 * @return 
	 */
	public boolean deleteCommunityuserinfo(String userid);
	
	/**
	 * 编辑用户发帖的内容
	 * @param userid 帖子id
	 * @return
	 */
	public List<HashMap<String,String>>editComunityContext (int userid);
	
	/**
	 * 获取用户最后评论名称
	 * @param userids
	 */
	public List<Map<String, String>> getLastReplyName(List<String> userids);
	
	/**
	 * 更新赞数量
	 * @param cardId
	 * @param userId
	 * @param status true:赞 false : 取消赞
	 * @return
	 */
	public boolean praiseCountUpdate(String cardId, String userId,boolean status);
	
	/**
	 * 点赞取消赞
	 * @param cardId 帖子id
	 * @param userId 用户id
	 * @param status true:赞  false:取消赞
	 * @return
	 */
	public boolean praiseCard(String cardId, String userId, boolean status);
	
	/**
	 * 删除赞
	 * @param user
	 * @return
	 */
	public boolean deleteCommunityPraise(String user);
	
	/**
	 * 更新用户帖子
	 * @param cardId  贴子id
	 */
	public boolean updateCard(String cardId, String title,String userinfo);
	/**
	 * 更新用户贴子详细信息
	 * @param title
	 * @param date
	 * @return
	 */
	public boolean updateCommunity(String cardId,String title, String date,String publishType);
	/**
	 * 统计评论新消息
	 * @param pl_context_id 帖子id
	 */
	public int getCountReplyInfo(String username);
	
	/**
	 * 获取最新消息内容
	 * @param userId
	 * @param page 
	 * @return
	 */
	public List<Map<String, String>> getCallBackNewMessage(int page,int limit,String userName);
	/**
	 * 更新信息提示状态
	 * @param user
	 * @param plId 
	 * @param userName 
	 */
	public void updateCommunityMessageState(String user, String plId, String userName);
	
	/**
	 * 判断用户是否已经点赞
	 * @param user
	 * @param plId
	 * @return
	 */
	public boolean getUserIsPraise(String user, String plId);
	
	/**
	 * 获取评论数
	 * @param cardId
	 * @return
	 */
	public String getCommentTotal(String cardId);
	/**
	 * 根据帖子id获取发帖人id
	 * @param cardId
	 */
	public String getUserIdByCardId(String cardId);
	/**
	 * 获取当前登录用户发送的所有帖子
	 * @param username 登录用户名
	 * @return
	 */
//	public List<HashMap<String,String>> getMyCommunity(String username,int pageNo);
	
	/**
	 * 获取用户评论统计数
	 * @param userName
	 * @return
	 */
	public int getcommentCount(String userName);
	
}
