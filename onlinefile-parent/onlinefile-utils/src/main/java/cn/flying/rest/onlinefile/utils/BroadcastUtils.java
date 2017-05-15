package cn.flying.rest.onlinefile.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * @author xiaoxiong
 * 广播公共处理类
 */
public class BroadcastUtils {

	private static XMPPConnection con = null ;
	
	public static String  openfireIp ;
	
	public static String openfirePort ;
	
	public static String openfireServerName ;
	
	public static String openfireServerPort;
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String http_schema ;
	
	/**
	 * 匿名登录openfire
	 */
	public static void login(){
		try {
			if (con == null) {
				ConnectionConfiguration config = new ConnectionConfiguration(BroadcastUtils.openfireIp, Integer.parseInt(BroadcastUtils.openfirePort));
				con = new XMPPConnection(config);
				con.loginAnonymously();//匿名登陆。
				System.out.println("连接openfire成功！");
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("连接openfire失败，请确认openfire服务是否已启动！");
		}
	}
	
	/***
	 * 向一个组发送广播
	 * @param username    
	 * @param broadcastTo 组名
	 * @param body        消息主体
	 */
	public static void broadcast(String username, String broadcastTo, String body, Message.Type type, String fromCnName){
		login();
		if (con == null) {
			System.out.println("连接openfire失败，请确认openfire服务是否已启动！");
			return ;
		}
		Date dt = new Date();
        String time = df.format(dt);
        String[] date = time.split(" ") ;
        
		Message m = new Message();
		m.setBody(body);
		m.setTo(broadcastTo);//all@broadcast.im.flying.cn 为所有用户
		m.setType(type);
		m.setDate(date[0]);
		m.setTime(date[1]);
		m.setFullname(fromCnName);
		con.sendPacket(m);
	}
}
