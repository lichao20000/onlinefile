package cn.flying.rest.onlinefile.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import cn.flying.rest.onlinefile.entity.MailServerAddressEntity;

/**
 * @author Administrator Email Util类
 */
public class EmailUtil {

	/** SSL 安全连接地址包 **/
	final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	/**
	 * 此处作为分流处理，将各个邮箱统一做规划
	 * 
	 * @param emailAddress
	 */
	public EmailUtil() {

	}
	
	public static String emailUploadUrl;
	
	public static String fileUploadUrl;
	
//	private static TrustManager myX509TrustManager = new X509TrustManager() { 
//
//	    @Override 
//	    public X509Certificate[] getAcceptedIssuers() { 
//	        return null; 
//	    } 
//
//	    @Override 
//	    public void checkServerTrusted(X509Certificate[] chain, String authType) 
//	    throws CertificateException { 
//	    } 
//
//	    @Override 
//	    public void checkClientTrusted(X509Certificate[] chain, String authType) 
//	    throws CertificateException { 
//	    } 
//	};

	public static String getEmailType(String emailAddress) {
		return paraseEmailAddressType(emailAddress);
	}

	/**
	 * 解析邮箱地址
	 * 
	 * @param emailAddress
	 * @return
	 */
	private static String paraseEmailAddressType(String emailAddress) {
		int count1 = emailAddress.indexOf("@");
		String tmpStr = emailAddress.substring(count1 + 1,
				emailAddress.length() - 1);
		int count2 = tmpStr.indexOf(".");
		String lastString = tmpStr.substring(0, count2);
		return lastString;
	}

	public static void main(String[] args) {
//		StringBuffer aaa = new StringBuffer("123<style>aasdaddasd</style>456");
//		System.out.println(aaa);
//		aaa = EmailUtil.dropCssStyle(aaa);
//		System.out.println(aaa);
	}

	/**
	 * @param receiverMailBoxAddress
	 *            POP3地址
	 * @param username
	 *            邮箱用户名
	 * @param password
	 *            邮箱密码
	 */
	// public static void receive(String receiverMailBoxAddress, String
	// username,
	// String password) {
	// // 本人用的是yahoo邮箱，故接受邮件使用yahoo的pop3邮件服务器
	// // String host = "pop.mail.yahoo.com.cn";
	// String host = receiverMailBoxAddress;
	// try {
	// // 连接到邮件服务器并获得邮件
	// Properties prop = new Properties();
	// prop.put("mail.pop3.host", host);
	//
	// Properties props = System.getProperties();
	//
	// props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	// props.setProperty("mail.pop3.socketFactory.fallback", "false");
	// props.setProperty("mail.pop3.port", "995");
	// props.setProperty("mail.pop3.host", host);
	//
	// Session session = Session.getInstance(props);
	// Store store = session.getStore("pop3");
	// store.connect(host, username, password);
	//
	// Folder inbox = store.getDefaultFolder().getFolder("INBOX");
	// // 设置inbox对象属性为可读写，这样可以控制在读完邮件后直接删除该附件
	// inbox.open(Folder.READ_WRITE);
	//
	// Message[] msg = inbox.getMessages();
	//
	// FetchProfile profile = new FetchProfile();
	// profile.add(FetchProfile.Item.ENVELOPE);
	//
	//
	// List<Map<String,String>> attenchMentsList = new
	// ArrayList<Map<String,String>>();
	//
	// if (msg.length == 0) {
	// System.out.println("没有邮件可以读取！");
	// return;
	// }
	// int attenchMentIndex = 0;
	// for (int i = 0; i < msg.length; i++) {
	// handleMultipart(msg[i],attenchMentsList,attenchMentIndex);
	// System.out.println("****************************" + i);
	// System.out.println("附件总个数:"+attenchMentsList.size());
	// }
	// if (inbox != null) {
	// // 参数为true表明阅读完此邮件后将其删除，更多的属性请参考mail.jar的API
	// inbox.close(true);
	// }
	// if (store != null) {
	// store.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// 处理Multipart邮件，包括了保存附件的功能
	/**
	 * @author Administrator
	 * @param msg
	 *            收件箱单一一条信息集合
	 * @param attachMentList
	 *            附件集合
	 * @param attacheMentIndex
	 *            附件索引
	 * @throws Exception
	 *             异常信息
	 */
	private static void handleMultipart(Message msg,
			List<Map<String, String>> attachMentList, int attacheMentIndex)
			throws Exception {
		String disposition;
		BodyPart part;
		try {
			Multipart mp = (Multipart) msg.getContent();
			// Miltipart的数量,用于除了多个part,比如多个附件
			int mpCount = mp.getCount();
			long startTime = System.currentTimeMillis(); // 获取开始时间
			for (int m = 0; m < mpCount; m++) {
				handle(msg);
				part = mp.getBodyPart(m);
				disposition = part.getDisposition();
				// 判断是否有附件,目前只处理附件
				if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
					// 这个方法负责保存附件
					System.out.println("附件");
					Map<String, String> attachMentPropertites = new HashMap<String, String>();
					String temp = part.getFileName();
					String s = temp.substring(8, temp.indexOf("?="));
					// 文件名经过了base64编码,下面是解码
					String fileName = base64Decoder(s);

					attachMentPropertites.put("fileName", fileName);

					// saveAttach(part);

					// 单个部件 注意：单个部件有可能又为一个Multipart，层层嵌套
					// BodyPart part = mp.getBodyPart(m);
					// 单个部件类型
					String type = part.getContentType().split(";")[0];
					/**
					 * 类型众多，逐一判断，其中TEXT、HTML类型可以直接用字符串接收，其余接收为内存地址
					 * 可能不全，如有没判断住的，请自己打印查看类型，在新增判断
					 */
					if (type.equals("multipart/alternative")) { // HTML
																// （文本和超文本组合）
						System.out.println("超文本:"
								+ part.getContent().toString());
					} else if (type.equals("text/plain")) { // 纯文本
						System.out.println("纯文本:"
								+ part.getContent().toString());
					} else if (type.equals("text/html")) { // HTML标签元素
						System.out.println("HTML元素:"
								+ part.getContent().toString());
					} else if (type.equals("multipart/related")) { // 内嵌资源
																	// (包涵文本和超文本组合)
						System.out.println("内嵌资源:"
								+ part.getContent().toString());
					} else if (type.contains("application/")) { // 应用附件
																// （zip、xls、docx等）
						System.out.println("应用文件："
								+ part.getContent().toString());
					} else if (type.contains("image/")) { // 图片附件
															// （jpg、gpeg、gif等）
						System.out.println("图片文件："
								+ part.getContent().toString());
					}
					attachMentPropertites.put("fileType", type);

					attachMentList.add(attachMentPropertites);
					attacheMentIndex++;
				} else {
					// 不是附件，就只显示文本内容
					// System.out.println(part.getContent());
				}
			}
			long endTime = System.currentTimeMillis(); // 获取开始时间
			// System.out.println("本次运行时间："+(endTime-startTime));
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
		}

	}

	// 处理任何一种邮件都需要的方法
	private static void handle(Message msg) throws Exception {
		System.out.println("邮件主题:" + msg.getSubject());
		System.out.println("邮件作者:"
				+ mimeDecodeString(msg.getFrom()[0].toString()));
		System.out.println("发送日期:" + msg.getSentDate());
	}

	private static void saveAttach(BodyPart part) throws Exception {
		// 得到未经处理的附件名字
		String temp = part.getFileName();
		// 除去发送邮件时，对中文附件名编码的头和尾，得到正确的附件名
		// （请参考发送邮件程序SendMail的附件名编码部分）
		if (temp.contains("?=")) {
			String s = temp.substring(8, temp.indexOf("?="));
			// 文件名经过了base64编码,下面是解码
			String fileName = base64Decoder(s);
			System.out.println("有附件:" + fileName);

			InputStream in = part.getInputStream();
			FileOutputStream writer = new FileOutputStream(new File("D:\\data"
					+ "\\" + fileName));
			byte[] content = new byte[255];
			int read = 0;
			while ((read = in.read(content)) != -1) {
				writer.write(content);
			}
			writer.close();
			in.close();
		}
	}

	// base64解码
	@SuppressWarnings("restriction")
	private static String base64Decoder(String s) throws Exception {
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		byte[] b = decoder.decodeBuffer(s);
		return (new String(b));
	}

	public static String mimeDecodeString(String res) {
		if (res != null) {
			res = res.trim();
			try {
				if (res.startsWith("=?GB") || res.startsWith("=?gb")
						|| res.startsWith("=?UTF") || res.startsWith("=?utf")) {
					res = MimeUtility.decodeText(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}
		return null;
	}

	public static List<Map<String, String>> search(String host,
			String emailName, String passWord, String searchKeyWord,
			int pageIndex, int pageLimit,String port) throws Exception {
		System.out.println("查询开始");
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱
		// 搜索发件人为 test_hao@sina.cn 和主题为"测试1"的邮件

		// SearchTerm st = new OrTerm(new FromStringTerm(searchKeyWord),
		// new SubjectTerm(searchKeyWord));
		// SearchTerm st = new AndTerm(new FromStringTerm("红云"),
		// new SubjectTerm(searchKeyWord));

		// SearchTerm st = new ;

		SearchTerm st = new SubjectTerm(searchKeyWord);
		// Message [] messages = folder.getMessages();
		long start = System.currentTimeMillis();
		System.out.println("++++++++++++++查询中+++++++++");
		Message[] messages = folder.search(st);

		long end = System.currentTimeMillis();
		System.out.println("查询结果：" + messages.length);
		System.out.println("查询耗时：" + (end - start));
		return parseMessage(messages, pageIndex, pageLimit);
		// return
		// parseMessageForSearch(messages,pageIndex,pageLimit,searchKeyWord);
	}

	/**
	 * 接收所有邮件
	 */
	public static List<Map<String, String>> receiveAll(String host,
			String emailName, String passWord,String port) throws Exception {
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);

		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱

		// System.out.println("未读邮件数: " + folder.getUnreadMessageCount());
		// System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
		// System.out.println("新邮件: " + folder.getNewMessageCount());
		// System.out.println("邮件总数: " + folder.getMessageCount());

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();

		List<Map<String, String>> emailAttachMents = parseMessageAll(messages);
		// 释放资源
		folder.close(true);
		store.close();
		return emailAttachMents;
	}

	/**
	 * 比较当前日期与服务器日期的大小
	 * 
	 * @param emailDate
	 * @return 返回比较结果，如果相差90天以上那么为false,在三个月以内则为true
	 */
	public static boolean compaireMonth(MimeMessage msg) {
		Date currentDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		boolean result = true;
		try {
			Date d1 = df.parse(df.format(msg.getSentDate()));
			Date d2 = df.parse(df.format(currentDate));

			long diff = d2.getTime() - d1.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			// System.out.println("相差" + days + "天");
			if (days > 90) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析邮件
	 * 
	 * @param messages
	 *            要解析的邮件列表
	 */
	public static List<Map<String, String>> parseMessageAll(Message[] messages)
			throws MessagingException, IOException {
		List<Map<String, String>> emailLists = new ArrayList<Map<String, String>>();
		Map<String, String> emailMap = null;
		if (messages == null || messages.length < 1) {
			return emailLists;
		}
		int MaxCounter = 1000;
		if (messages.length <= 1000) {
			MaxCounter = messages.length;
		}

		/** 获取当前日期 (默认缓存3个月的邮件) **/
		// System.out.println("++++++++邮箱附件开始解析+++++++++");
		// long start = System.currentTimeMillis();
		StringBuffer content = null;
		// 解析所有邮件
		for (int i = 0; i < MaxCounter; i++) {
			// System.out.println("解析到了"+i);
			emailMap = new HashMap<String, String>();
			MimeMessage msg = (MimeMessage) messages[messages.length - i - 1];
			if (compaireMonth(msg)) {
				String tmpSubJect = getSubject(msg);
				emailMap.put("emailIndex",
						String.valueOf(messages.length - i - 1));
				emailMap.put("subjectTitle", tmpSubJect);
				emailMap.put("sender", getFrom(msg));
				emailMap.put("sendTime", getSentDate(msg, null));
				content = new StringBuffer(30);
				getMailTextContent(msg, content);
				emailMap.put("mailText", dropCssStyle(content.toString()));
				emailMap.put("size",getSize(msg));
				
				Map<String,Integer> map = isContainAttachment(msg);
				int attachmentCount = map.get("attachmentCount");
				emailMap.put("containAttachMent", attachmentCount==0?"false":"true");
				emailMap.put("partCount", String.valueOf(map.get("partCount")));
				emailMap.put("attachmentCount", String.valueOf(attachmentCount));
				emailLists.add(emailMap);
			} else {
				break;
			}
		}
		// long end = System.currentTimeMillis();
		// System.out.println("总查询邮件中是否有附件耗时"+(end-start));
		// System.out.println("++++++++邮箱附件解析结束+++++++++");
		return emailLists;
	}
	
	public static String getSize(MimeMessage msg){
		String sizeString = "0";
		try {
			sizeString =  String.valueOf(msg.getSize()/1024);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sizeString;
	}

	/**
	 * 接收邮件
	 */
	public static List<Map<String, String>> receive(String host,
			String emailName, String passWord, int pageIndex, int pageLimit,String port)
			throws Exception {
		// List<Map<String, String>> emailLists = new
		// ArrayList<Map<String,String>>();
		// 准备连接服务器的会话信息
		Properties props = new Properties();
		// String host = "pop.163.com";
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
//		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);

		// props.setProperty("mail.store.protocol", "pop3"); // 协议
		// props.setProperty("mail.pop3.port", "110"); // 端口
		// props.setProperty("mail.pop3.host", "pop.163.com"); // pop3服务器

		Session session = Session.getInstance(props);
		
		URLName urln = new URLName("pop3", host, Integer.valueOf(port), null,   
				emailName, passWord);  
		
		
		Store store = session.getStore(urln);
		store.connect();
		// 创建Session实例对象
		// Session session = Session.getInstance(props);
		// Store store = session.getStore("pop3");
		// store.connect("pop.163.com","liuezeng08@163.com", "******");

		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		/*
		 * Folder.READ_ONLY：只读权限 Folder.READ_WRITE：可读可写（可以修改邮件的状态）
		 */
		folder.open(Folder.READ_WRITE); // 打开收件箱

		// 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
		//System.out.println("未读邮件数: " + folder.getUnreadMessageCount());

		// 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
		//System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
		//System.out.println("新邮件: " + folder.getNewMessageCount());

		// 获得收件箱中的邮件总数
//		System.out.println("邮件总数: " + folder.getMessageCount());

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();

		List<Map<String, String>> emailAttachMents = parseMessage(messages,
				pageIndex, pageLimit);
		// 释放资源
		folder.close(true);
		store.close();
		return emailAttachMents;
	}

	public static int getCountAll(String host, String emailName, String passWord,String port)
			throws Exception {
		// 准备连接服务器的会话信息
		int countAll = 0;
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);
		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱
		countAll = folder.getMessageCount();
		// 释放资源
		folder.close(true);
		store.close();
		return countAll;
	}
	
	public static String getMailText(String host, String emailName, String passWord,int emailIndex,String port)
			throws Exception {
		// 准备连接服务器的会话信息
		String mailText = "";
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);
		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱
		Message[] messages = folder.getMessages();
		StringBuffer content = new StringBuffer(30);
		getMailTextContent(messages[emailIndex], content);
		mailText = dropCssStyle(content.toString());
		// 释放资源
		folder.close(true);
		store.close();
		return mailText;
	}

	/**
	 * 解析邮件
	 * 
	 * @param messages
	 *            要解析的邮件列表
	 */
	public static List<Map<String, String>> parseMessageForSearch(
			Message[] messages, int pageIndex, int pageLimit,
			String searchKeyWord) throws MessagingException, IOException {
		List<Map<String, String>> emailLists = new ArrayList<Map<String, String>>();
		Map<String, String> emailMap = null;
		if (messages == null || messages.length < 1) {
			return emailLists;
		}

		int pagestart = (pageIndex - 1) * pageLimit;

		pageLimit = pagestart + pageLimit;

		// int limitCounter = 1;
		// if((pagestart+pageLimit)>messages.length){
		// limitCounter = messages.length;
		// }else{
		// limitCounter = pagestart+pageLimit;
		// }
		StringBuffer content = null;
		// 解析所有邮件
		for (int i = 0; i < messages.length; i++) {
			if (pagestart > pageLimit) {
				break;
			}
			emailMap = new HashMap<String, String>();
			MimeMessage msg = (MimeMessage) messages[i];
			String tmpSubJect = getSubject(msg);
			System.out.println(pagestart);
			if (tmpSubJect.indexOf(searchKeyWord) > -1) {
				// if(tmpSubJect.contains(searchKeyWord) ){
				emailMap.put("subject", getSubJectForShow(tmpSubJect));
				emailMap.put("subjectTitle", tmpSubJect);
				emailMap.put("sender", getFrom(msg));
				emailMap.put("receiver", getReceiveAddress(msg, null));
				emailMap.put("sendTime", getSentDate(msg, null));
				emailMap.put("containAttachMent",
						String.valueOf(isContainAttachment(msg)));
				emailMap.put("size",getSize(msg));
				content = new StringBuffer(30);
				getMailTextContent(msg, content);
				emailMap.put("mailText", dropCssStyle(content.toString()));
				emailLists.add(emailMap);
				pagestart++;
			}
		}
		// long end = System.currentTimeMillis();
		// System.out.println("解析"+messages.length+"邮件耗时："+(end-start));
		return emailLists;
	}

	/**
	 * 解析邮件
	 * 
	 * @param messages
	 *            要解析的邮件列表
	 */
	public static List<Map<String, String>> parseMessage(Message[] messages,
			int pageIndex, int pageLimit) throws MessagingException,
			IOException {
		List<Map<String, String>> emailLists = new ArrayList<Map<String, String>>();
		Map<String, String> emailMap = null;
		if (messages == null || messages.length < 1) {
			return emailLists;
		}
		int pagestart = (pageIndex - 1) * pageLimit;

		int limitCounter = 20;
		if (limitCounter > messages.length) {
			limitCounter = messages.length;
		}
		// if((pagestart+pageLimit)>messages.length){
		// limitCounter = messages.length;
		// }else{
		// limitCounter = pagestart+pageLimit;
		// }

		// 解析所有邮件
		StringBuffer content = null;
		for (int i = 0; i < limitCounter; i++) {
			// long start = System.currentTimeMillis();
			// for (int i = 0; i < messages.length; i++) {
			emailMap = new HashMap<String, String>();
			MimeMessage msg = (MimeMessage) messages[messages.length - i - 1];
			// System.out.println("------------------解析第" +
			// msg.getMessageNumber() + "封邮件-------------------- ");
			// System.out.println("主题: " + getSubject(msg));
			// System.out.println("发件人: " + getFrom(msg));
			// System.out.println("收件人：" + getReceiveAddress(msg, null));
			// System.out.println("发送时间：" + getSentDate(msg, null));
			// System.out.println("是否已读：" + isSeen(msg));
			// System.out.println("邮件优先级：" + getPriority(msg));
			// System.out.println("是否需要回执：" + isReplySign(msg));
			// System.out.println("邮件大小：" + msg.getSize()/1024 + "Mb");
			// boolean isContainerAttachment = isContainAttachment(msg);
			// System.out.println("是否包含附件：" + isContainerAttachment);
			// if (isContainerAttachment) {
			// System.out.println("#########正在解析" + i+"封邮件#############");
			// getAttachments(msg, emailLists);
			// System.out.println("##########" + i+"封邮件解析完毕############");
			// saveAttachment(msg, "D:\\mailtmp\\"+msg.getSubject() + "_");
			// //保存附件
			// }
			// StringBuffer content = new StringBuffer(30);
			// getMailTextContent(msg, content);
			// System.out.println("邮件正文：" + (content.length() > 100 ?
			// content.substring(0,100) + "..." : content));
			// System.out.println("------------------第" + msg.getMessageNumber()
			// + "封邮件解析结束-------------------- ");
			// // System.out.println();
			if (compaireMonth(msg)) {
				String tmpSubJect = getSubject(msg);
				emailMap.put("subject", getSubJectForShow(tmpSubJect));
				emailMap.put("subjectTitle", tmpSubJect);
				emailMap.put("sender", getFrom(msg));
				emailMap.put("receiver", getReceiveAddress(msg, null));
				emailMap.put("sendTime", getSentDate(msg, null));
				emailMap.put("size",getSize(msg));
				content = new StringBuffer(30);
				getMailTextContent(msg, content);
				emailMap.put("mailText", dropCssStyle(content.toString()));
				emailMap.put("containAttachMent",String.valueOf(isContainAttachment(msg)));
				emailMap.put("type", tmpSubJect.substring(tmpSubJect.lastIndexOf(".")+1, tmpSubJect.length()));
				emailLists.add(emailMap);
			}

		}
		// long end = System.currentTimeMillis();
		// System.out.println("解析"+messages.length+"邮件耗时："+(end-start));
		return emailLists;
	}

	/**
	 * 获得邮件主题
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 解码后的邮件主题
	 */
	public static String getSubject(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		try {
			return MimeUtility.decodeText(msg.getSubject());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 获得邮件主题
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 解码后的邮件主题
	 */
	public static String getSubJectForShow(String msg)
			throws UnsupportedEncodingException, MessagingException {
		String tmpMsg = "";
		if (String_length(msg) > 16 && msg.length() > 12) {
			tmpMsg = msg.substring(0, 12);
		} else {
			tmpMsg = msg;
		}
		return tmpMsg;
	}

	/**
	 * 获得邮件主题
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 解码后的邮件主题
	 */
	public static String getSubJectForAttachMent(String msg)
			throws UnsupportedEncodingException, MessagingException {
		String tmpMsg = "";
		if (String_length(msg) > 12 && msg.length() > 10) {
			tmpMsg = msg.substring(0, 10);
		} else {
			tmpMsg = msg;
		}
		return tmpMsg;
	}

	/**
	 * 判断字符串长度
	 * 
	 * @param value
	 *            字符串
	 * @return 返回字符串长度
	 */
	public static int String_length(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 获得邮件发件人
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 姓名 <Email地址>
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getFrom(MimeMessage msg) throws MessagingException,
			UnsupportedEncodingException {
		String from = "";
		try {
			Address[] froms = msg.getFrom();
			if (froms.length < 1)
				throw new MessagingException("没有发件人!");

			InternetAddress address = (InternetAddress) froms[0];
			String person = address.getPersonal();
			if (person != null) {
				person = MimeUtility.decodeText(person) + " ";
			} else {
				person = "";
			}
			/** 发送者暂时先显示为姓名 **/
			from = person + " &lt;" + address.getAddress() + " &gt;";
			//from = person;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return from;
	}

	/**
	 * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
	 * <p>
	 * Message.RecipientType.TO 收件人
	 * </p>
	 * <p>
	 * Message.RecipientType.CC 抄送
	 * </p>
	 * <p>
	 * Message.RecipientType.BCC 密送
	 * </p>
	 * 
	 * @param msg
	 *            邮件内容
	 * @param type
	 *            收件人类型
	 * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
	 * @throws MessagingException
	 */
	public static String getReceiveAddress(MimeMessage msg,
			Message.RecipientType type) throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresss = null;
		try {
			if (type == null) {
				addresss = msg.getAllRecipients();
			} else {
				addresss = msg.getRecipients(type);
			}

			if (addresss == null || addresss.length < 1)
				throw new MessagingException("没有收件人!");
			for (Address address : addresss) {
				InternetAddress internetAddress = (InternetAddress) address;
				receiveAddress.append(internetAddress.toUnicodeString())
						.append(",");
			}

			receiveAddress.deleteCharAt(receiveAddress.length() - 1); // 删除最后一个逗号
		} catch (Exception e) {
			e.printStackTrace();
		}

		return receiveAddress.toString();
	}

	/**
	 * 获得邮件发送时间
	 * 
	 * @param msg
	 *            邮件内容
	 * @return yyyy年mm月dd日 星期X HH:mm
	 * @throws MessagingException
	 */
	public static String getSentDate(MimeMessage msg, String pattern)
			throws MessagingException {
		try {
			Date receivedDate = msg.getSentDate();
			if (receivedDate == null)
				return "";

			if (pattern == null || "".equals(pattern))
				pattern = "yyyy/MM/dd HH:mm ";

			return new SimpleDateFormat(pattern).format(receivedDate);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 判断邮件中是否包含附件
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 返回 邮件体数 和  附件数 
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static Map<String, Integer> isContainAttachment(Part part)
			throws MessagingException, IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		if (part.isMimeType("multipart/*")) {
			int attachmentCount = 0;
			MimeMultipart multipart = (MimeMultipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				String disp = bodyPart.getDisposition();
				if (disp != null&& (null!=bodyPart.getFileName() && bodyPart.getFileName()!="")
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					attachmentCount++;
				}
				// else if (bodyPart.isMimeType("multipart/*")) {
				// flag = isContainAttachment(bodyPart);
				// } else {
				// String contentType = bodyPart.getContentType();
				// if (contentType.indexOf("application") != -1) {
				// flag = true;
				// }
				//
				// if (contentType.indexOf("name") != -1) {
				// flag = true;
				// }
				// // }
				//
				// // if (flag) break;
			}
			map.put("partCount", partCount);
			map.put("attachmentCount", attachmentCount);
		} else if (part.isMimeType("message/rfc822")) {
			map = isContainAttachment((Part) part.getContent());
		}else{
			map.put("partCount", 1);
			map.put("attachmentCount", 0);
		}
		return map;
	}

	/**
	 * 判断邮件是否已读
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 如果邮件已读返回true,否则返回false
	 * @throws MessagingException
	 */
	public static boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}

	/**
	 * 判断邮件是否需要阅读回执
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 需要回执返回true,否则返回false
	 * @throws MessagingException
	 */
	public static boolean isReplySign(MimeMessage msg)
			throws MessagingException {
		boolean replySign = false;
		String[] headers = msg.getHeader("Disposition-Notification-To");
		if (headers != null)
			replySign = true;
		return replySign;
	}

	/**
	 * 获得邮件的优先级
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 1(High):紧急 3:普通(Normal) 5:低(Low)
	 * @throws MessagingException
	 */
	public static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "普通";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1
					|| headerPriority.indexOf("High") != -1)
				priority = "紧急";
			else if (headerPriority.indexOf("5") != -1
					|| headerPriority.indexOf("Low") != -1)
				priority = "低";
			else
				priority = "普通";
		}
		return priority;
	}

	public static String dropCssStyle(String htmlBody){
		if(null != htmlBody && htmlBody.indexOf("<style")>-1){
			int startIndex = htmlBody.indexOf("<style");
			int endIndex = htmlBody.indexOf("/style>");
			if(endIndex>startIndex){
				htmlBody =htmlBody.substring(0,startIndex)+htmlBody.substring(endIndex+7,htmlBody.length());
			}
		}
		return htmlBody;
	}
	
	/**
	 * 获得邮件文本内容
	 * 
	 * @param part
	 *            邮件体
	 * @param content
	 *            存储邮件文本内容的字符串
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void getMailTextContent(Part part, StringBuffer content)
			throws MessagingException, IOException {
		// 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
		//将"text/*" 改为"text/html"  只获取html文本的正文内容
//		if (part.isMimeType("text/plain") && !isContainTextAttach) {
		
//		 if (part.isMimeType("text/plain") && !isContainTextAttach) {
//	            // text/plain 类型
//			 content.append((String) part.getContent());
//	        } else
	        	if (part.isMimeType("text/html") && !isContainTextAttach) {
	            // text/html 类型
	        	content.append((String) part.getContent());
	        } 
	        else if (part.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) part.getContent();
				int partCount = multipart.getCount();
				for (int i = 0; i < partCount; i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					getMailTextContent(bodyPart, content);
				}
			}
//		if (part.isMimeType("text/*") && !isContainTextAttach) {
//			content.append(part.getContent().toString().replaceAll(" ", ""));
//		} else if (part.isMimeType("message/rfc822")) {
//			getMailTextContent((Part) part.getContent(), content);
//		} else if (part.isMimeType("multipart/*")) {
//			Multipart multipart = (Multipart) part.getContent();
//			int partCount = multipart.getCount();
//			for (int i = 0; i < partCount; i++) {
//				BodyPart bodyPart = multipart.getBodyPart(i);
//				getMailTextContent(bodyPart, content);
//			}
//		}
	}

	public static List<Map<String, String>> getEmailAttachMentInfoByEmail(
			String host, String emailName, String passWord, int pageIndex,
			int pageLimit, int emailIndex,int partCount,String port) throws Exception {
		List<Map<String, String>> attatchMents = new ArrayList<Map<String, String>>();
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);

		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱

		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();
		if (messages.length > emailIndex) {
			getAttachments(messages[emailIndex], attatchMents, emailIndex, partCount);
		}
		return attatchMents;
	}

	/**
	 * 提取一个收件箱中一条信息的所有附件
	 * 
	 * @param part
	 *            邮件中多个组合体中的其中一个组合体
	 * @param destDir
	 *            附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void getAttachments(Part part,
			List<Map<String, String>> emailLists, int emailIndex,int partCount)
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		Map<String, String> emailMap = null;
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			//int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null && (null!= bodyPart.getFileName() && bodyPart.getFileName() != "" )
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					// InputStream is = bodyPart.getInputStream();
					// saveFile(is, destDir,
					// decodeText(bodyPart.getFileName()));
					emailMap = new HashMap<String, String>();
					MimeMessage msg = (MimeMessage) part;
					String tmpSubJect = decodeText(bodyPart.getFileName());
					// String tmpSubJect = getSubject(msg);
					// emailMap.put("subject",getSubJectForShow(tmpSubJect));
					emailMap.put("subject", getSubJectForAttachMent(tmpSubJect));
					emailMap.put("subjectTitle", tmpSubJect);
					emailMap.put("emailIndex", String.valueOf(emailIndex));
					emailMap.put("attachMentIndex", String.valueOf(i));
					emailMap.put("sender", getFrom(msg));
					emailMap.put("receiver", getReceiveAddress(msg, null));
					emailMap.put("sendTime", getSentDate(msg, null));
					emailMap.put("type", tmpSubJect.substring(tmpSubJect.lastIndexOf(".")+1, tmpSubJect.length()));
					emailLists.add(emailMap);
				}
				// else if (bodyPart.isMimeType("multipart/*")) {
				// getAttachments(bodyPart,emailLists);
				// } else {
				// String contentType = bodyPart.getContentType();
				// if (contentType.indexOf("name") != -1 ||
				// contentType.indexOf("application") != -1) {
				// emailMap = new HashMap<String, String>();
				// MimeMessage msg = (MimeMessage)part;
				// String tmpSubJect = decodeText(bodyPart.getFileName());
				// // String tmpSubJect = getSubject(msg);
				// // emailMap.put("subject",getSubJectForShow(tmpSubJect));
				// emailMap.put("subject",getSubJectForShow(tmpSubJect));
				// emailMap.put("subjectTitle", tmpSubJect);
				// emailMap.put("sender", getFrom(msg));
				// emailMap.put("receiver", getReceiveAddress(msg, null));
				// emailMap.put("sendTime", getSentDate(msg, null));
				// emailLists.add(emailMap);
				// // saveFile(bodyPart.getInputStream(), destDir,
				// decodeText(bodyPart.getFileName()));
				// }
				// }
			}
		} else if (part.isMimeType("message/rfc822")) {
			getAttachments((Part) part.getContent(), emailLists, emailIndex,partCount);
		}
	}

	/**
	 * 保存附件
	 * 
	 * @param part
	 *            邮件中多个组合体中的其中一个组合体
	 * @param destDir
	 *            附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveAttachment(Part part, String destDir)
			throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					InputStream is = bodyPart.getInputStream();
					saveFile(is, destDir, decodeText(bodyPart.getFileName()));
				}
				// else if (bodyPart.isMimeType("multipart/*")) {
				// saveAttachment(bodyPart,destDir);
				// } else {
				// String contentType = bodyPart.getContentType();
				// if (contentType.indexOf("name") != -1 ||
				// contentType.indexOf("application") != -1) {
				// saveFile(bodyPart.getInputStream(), destDir,
				// decodeText(bodyPart.getFileName()));
				// }
				// }
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent(), destDir);
		}
	}

	/**
	 * 保存附件
	 * 
	 * @param part
	 *            邮件中多个组合体中的其中一个组合体
	 * @param destDir
	 *            附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String saveAttachmentByIndex(Part part, String emailUploadUrl,
			int attachMentIndex) throws UnsupportedEncodingException,
			MessagingException, FileNotFoundException, IOException {
	    String result = "";
		// if (part.isMimeType("multipart/*")) {
		Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
		// 复杂体邮件包含多个邮件体
		// int partCount = multipart.getCount();
		// for (int i = 0; i < partCount; i++) {
		// 获得复杂体邮件中其中一个邮件体
		BodyPart bodyPart = multipart.getBodyPart(attachMentIndex);
		// 某一个邮件体也有可能是由多个邮件体组成的复杂体
		String disp = bodyPart.getDisposition();
		if (disp != null &&( null!=bodyPart.getFileName() && bodyPart.getFileName() != "" )
				&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
						.equalsIgnoreCase(Part.INLINE))) {
			InputStream is = bodyPart.getInputStream();
			try {
				result = formUpload(emailUploadUrl, decodeText(bodyPart.getFileName()), is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
	 * 
	 * @param url
	 *            请求地址 form表单url地址
	 * @param filePath
	 *            文件在服务器保存路径
	 * @return String url的响应信息返回值
	 * @throws Exception 
	 */
	public static String formUpload(String url, String fileName, InputStream is)
			throws Exception {

		String result = null;
		/**
		 * 第一部分
		 */
		/**  由于云端采用的https协议，然后上传时候需要走http,所以需要在此处修改一下协议！    **/
//		url = url.replace("https", "http");
//		url = url.replace("8443", "8080");
		URL urlObj = new URL(url);
		System.out.println(url);
////		// 连接
//		
//	    HttpClient client = new DefaultHttpClient(WebClientDevWrapper.wrapClient());
//	    ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor(client) ;
//	    HttpPost httppost = new HttpPost(url); 
//	    File targetFile = new File(is.toString());
//	    org.apache.commons.httpclient.methods.multipart.Part[] parts = { new FilePart(fileName, targetFile) };
//	    PostMethod filePost = new PostMethod(url);  
//	    MultipartRequestEntity reqEntity = new MultipartRequestEntity(parts, filePost.getParams());
//	    executor.
//		HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
//		 SSLContext sslcontext = SSLContext.getInstance("TLS"); 
//		 sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
//        // 创建SSLContext对象，并使用我们指定的信任管理器初始化 
//        TrustManager[] tm = { new MyX509TrustManager() }; 
//        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE"); 
//        sslContext.init(null, tm, new java.security.SecureRandom()); 
//        // 从上述SSLContext对象中得到SSLSocketFactory对象 
//        SSLSocketFactory ssf = sslcontext.getSocketFactory(); 
//        // 创建URL对象 
//        URL myURL = new URL(url); 
//        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象 
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection(); 
		/**
		 * 设置关键值
		 */
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setReadTimeout(50000);// post方式不能使用缓存

		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ BOUNDARY);

		// 请求正文信息

		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
				+ fileName + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);

		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(is);
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}

		}

		return result;

	}


	/**
	 * 读取输入流中的数据保存至指定目录
	 * 
	 * @param is
	 *            输入流
	 * @param fileName
	 *            文件名
	 * @param destDir
	 *            文件存储目录
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void saveFile(InputStream is, String destDir, String fileName)
			throws FileNotFoundException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(destDir + fileName)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
	}

	/**
	 * 文本解码
	 * 
	 * @param encodeText
	 *            解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeText(String encodeText)
			throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	

	public static boolean checkEmailServer(String email) {
		boolean result = false ;
		if (null != email && !email.equals("")) {
			email = email.substring(email.indexOf("@") + 1, email.length() - 1);
			email = email.substring(0, email.indexOf("."));
		}
		if(email.equals("163")
		        || email.equals("263")
				|| email.toLowerCase().equals("flyingsoft")
				|| email.toLowerCase().equals("sina")
				|| email.toLowerCase().equals("189")
				|| email.toLowerCase().equals("sohu")
				|| email.equalsIgnoreCase("126")
				|| email.equalsIgnoreCase("qq")
				){
			result = true;
		}
		return result;
	}
	
	public static boolean checkEmailServerForManul(String email,String popServerAddress) {
		boolean result = false ;
		if (null != email && !email.equals("")) {
			email = email.substring(email.indexOf("@") + 1, email.length() - 1);
			email = email.substring(0, email.indexOf("."));
		}
		
		if (null != popServerAddress && !popServerAddress.equals("")) {
			popServerAddress = popServerAddress.substring(popServerAddress.indexOf(".") + 1, popServerAddress.length());
			popServerAddress = popServerAddress.substring(0, popServerAddress.indexOf("."));
		}
		
		if(email.equals("163") || email.equals("263")
				|| email.toLowerCase().equals("flyingsoft")|| email.toLowerCase().equals("sina")|| email.toLowerCase().equals("189")|| email.toLowerCase().equals("sohu")||popServerAddress.equals("263xmail")){
			result = true;
		}
		
		return result;
	}

	/**
	 * 分享到群组方法
	 * 
	 * @param emailIndex
	 * @param attachMentIndex
	 * @return
	 */
	public static String shareAttachMentToGroup(int emailIndex,
			int attachMentIndex, String host, String emailName,
			String passWord, String emailUploadUrl,String port) throws Exception {
		String result = "";
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", port);
		props.setProperty("mail.pop3.host", host);
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(host, emailName, passWord);
		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱
		Message[] messages = folder.getMessages();
		try {
		    result = saveAttachmentByIndex(messages[emailIndex], emailUploadUrl,
					attachMentIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 登录验证邮箱
	 * 
	 * @param emailName
	 *            邮箱地址
	 * @param passWord
	 *            密码
	 * @param host
	 *            接受服务器
	 * @param popPort
	 *            接受服务器端口
	 * @return
	 * @throws Exception
	 */
	public static boolean checkEmail(String emailName, String passWord,String host,String popPort
			) throws Exception {
		
		boolean flag = false;
		// 准备连接服务器的会话信息
		Properties props = new Properties();
//		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
//		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", popPort);
		props.setProperty("mail.store.protocol", "pop3"); 
		props.setProperty("mail.pop3.host", host);

		// 设置超时时间3秒 超时认为邮箱登录失败
		// props.setProperty("mail.pop3.connectiontimeout", "3000");
		props.setProperty("mail.pop3.timeout", "2000");

		Session session = Session.getInstance(props);

		URLName urln = new URLName("pop3", host, Integer.valueOf(popPort), null,   
				emailName, passWord);  
		Store store;
		store = session.getStore(urln);
		try {
			store.connect();
		} catch (Exception e) {
			System.out.println("验证超时：Read timed out");
		}
		if (store.isConnected()) {
			flag = true;
			store.close();
		}
		return flag;
	}
	
	public static boolean getQQMailProtol(String imapserver,String email,String passWord)throws Exception{
		boolean flag = false;
		Properties prop = new Properties();
		prop.put("mail.store.protocol", "imap");
        prop.put("mail.imap.host",imapserver);
        if(imapserver.indexOf("qq") > 0) {
             prop.put("mail.imap.port", "993");  
             prop.put("mail.imap.ssl.enable", "true");
             prop.put("mail.imap.auth.plain.disable","true");
//             prop.put("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//             prop.put("mail.imap.socketFactory.fallback","false");
        } else {
             prop.put("mail.imap.auth.plain.disable","true");
        }
        Session session = Session.getInstance(prop);

		URLName urln = new URLName("imap", imapserver, Integer.valueOf(993), null,   
				email, passWord);  
		Store store;
		store = session.getStore("imap");
		store.connect(imapserver,email, passWord);
		if (store.isConnected()) {
			flag = true;
			store.close();
		}
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE); // 打开收件箱
		Message[] messages = folder.getMessages();
		System.out.println("邮箱总数为"+messages.length);
		return flag;
	}
	
	public static Map<String,String> getEmailServerPropertitesByAddress(String emailAddress){
		Map<String,String> emailPropertites = new HashMap<String, String>();
		
		/**  提取出来邮箱地址的类型 例如163等 然后抓取邮箱服务地址跟端口  **/
		String emailAddressType = paraseEmailAddressType(emailAddress);
		/**163**/
		if(emailAddressType.equals("163")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_163_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.MAIL_163_RECEIVE_PORT);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_163_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.MAIL_163_SEND_PORT);
		}else if(emailAddressType.equals("263")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_263_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_263_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.equals("126")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_126_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_126_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.toLowerCase().equals("qq")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_QQ_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_QQ_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.toLowerCase().equals("flyingsoft")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_263CN_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_263CN_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.toLowerCase().equals("sohu")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_SOHU_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_SOHU_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.toLowerCase().equals("sina")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_SINA_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_SINA_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}else if(emailAddressType.toLowerCase().equals("189")){
			emailPropertites.put("receiveAddress", MailServerAddressEntity.MAIL_189_RECEIVE);
			emailPropertites.put("receivePort", MailServerAddressEntity.receiveDefaultPort);
			emailPropertites.put("sendAddress", MailServerAddressEntity.MAIL_189_SEND);
			emailPropertites.put("sendPort", MailServerAddressEntity.sendDefaultPort);
		}
		
		return emailPropertites;
	}
}
