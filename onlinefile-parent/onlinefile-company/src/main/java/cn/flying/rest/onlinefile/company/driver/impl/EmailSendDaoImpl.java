package cn.flying.rest.onlinefile.company.driver.impl;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.company.driver.EmailSendDao;
import cn.flying.rest.onlinefile.utils.email.MyAuthenticator;

@Repository("emailSendDao")
public class EmailSendDaoImpl implements EmailSendDao {
	
 // 发送邮件的服务器的IP和端口    
	
    private String mailServerHost;  
	
    private String mailServerPort;    
    // 邮件发送者的地址    
	
    private String fromAddress;    
    // 登陆邮件发送服务器的用户名和密码

    private String userName; 
    private String password;    
    public String getMailServerHost() {
		return mailServerHost;
	}
    @Value("${flyingSoft.email.serverhost}")
	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}
	@Value("${flyingSoft.email.serverport}")
	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public String getFromAddress() {
		return fromAddress;
	}
	@Value("${flyingSoft.email.address}")
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
  
	public String getUserName() {
		return userName;
	}
	@Value("${flyingSoft.email.username}")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	@Value("${flyingSoft.email.password}")
	public void setPassword(String password) {
		this.password = password;
	}
	    /** *//**   
      * 获得邮件会话属性   
      */    
    public Properties getProperties(boolean validate ){ 
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.getMailServerHost());    
      p.put("mail.smtp.port", this.getMailServerPort());    
      p.put("mail.smtp.auth", validate ? "true" : "false");
      // longjunhao 20150129 add
      p.put("mail.smtp.starttls.enable", "true");
      return p;    
    }   
	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
    @Override
	public boolean sendTextMail(String toAddress,String subject,String content,boolean validate) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = this.getProperties(validate);
		if (validate) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(this.getUserName(),
					this.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(this.fromAddress);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(toAddress);
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = content;
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/** */
	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
    @Override
	public  boolean sendHtmlMail(String toAddress,String subject,String content,boolean validate) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = this.getProperties(validate);
		// 如果需要身份认证，则创建一个密码验证器
		if (validate) {
			authenticator = new MyAuthenticator(this.userName,
					this.password);
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(this.fromAddress);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(toAddress);
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
