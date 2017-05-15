package cn.flying.rest.onlinefile.company.driver;



/** *//**   
* 发送邮件需要使用的基本信息 
*author by wangfun
http://www.5a520.cn 小说520  
*/    
public interface EmailSendDao {    

	public boolean sendTextMail(String toAddress,String subject,String content,boolean validate);

	/** */
	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public  boolean sendHtmlMail(String toAddress,String subject,String content,boolean validate);
}   
