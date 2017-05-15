package cn.flying.rest.onlinefile.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 文件处理相关工具类
 * 
 * @author longjunhao 20151009
 *
 */
public class FileTool {
	
	private static String hostname;
	
	private static int port;
	
	private static String userName; // 匿名登录 anonymous
	
	private static String password;
	
	private static String path;
	
	static {
		Map<String, Object> config = PropertiesUtils.getInstance().getPropertiesMap();
		hostname = config.get("ftp.staticresource.hostname").toString();
		port = Integer.parseInt(config.get("ftp.staticresource.port").toString());
		userName = config.get("ftp.staticresource.username").toString();
		password = config.get("ftp.staticresource.password").toString();
		path = config.get("ftp.staticresource.path").toString();
	}
	
	/**
	 * 向FTP服务器上传文件
	 * @author longjunhao 20151009
	 * @param fileName 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功 true，失败 false
	 */
	public static boolean uploadFileByFTP(String fileName, InputStream input) {
		return uploadFileByFTP(hostname, port, userName, password, path, fileName, input);
	}
	
	/**
	 * 将本地文件上传到FTP服务器上
	 * @author longjunhao 20151009
	 * @param fileName
	 * @param orginfilename
	 * @return
	 */
	public static boolean uploadFromProduction(String fileName, String orginfilename) {
		return uploadFromProduction(hostname, port, orginfilename, orginfilename, orginfilename, fileName, orginfilename);
	}

	/**
	 * 向FTP服务器上传文件
	 * 
	 * @author longjunhao 20151009
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param userName
	 *            FTP登录帐号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            保存目录
	 * @param fileName
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功 true，失败 false
	 */
	public static boolean uploadFileByFTP(String url, int port,
			String userName, String password, String path, String fileName,
			InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.connect(url, port);
//			System.out.println("连接到ftp服务器：" + url + " 成功..开始登录");  
			if (ftp.login(userName, password)) {
				reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftp.disconnect();
					System.err.println("FTP server refused connection.");  
					return success;
				}
				ftp.setControlEncoding("GBK");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//				ftp.makeDirectory(path);
				ftp.changeWorkingDirectory(path);
				success = ftp.storeFile(fileName, input);
				input.close();
				ftp.logout();
			} else {
				System.err.println("FTP login fail.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}

	/**
	 * 将本地文件上传到FTP服务器上
	 * 
	 * @param hostname
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param orginfilename
	 *            输入流文件名
	 */
	public static boolean uploadFromProduction(String hostname, int port,
			String userName, String password, String path, String fileName,
			String orginfilename) {
		boolean success = false;
		try {
			File file = new File(orginfilename);
			if (!file.exists()) {
				System.out.println("The file ["+orginfilename+"] is not found.");
				return false;
			}
			FileInputStream in = new FileInputStream(new File(orginfilename));
			success = uploadFileByFTP(hostname, port, userName, password, path, fileName, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	// 测试
	public static void main(String[] args) {
		uploadFromProduction("10.211.55.3", 21, "vsftpuser", "vsftpuser", "/var/ftp/pub/20151009",
				"long01.md", "/tmp/test");
	}
}
