package cn.flying.rest.onlinefile.utils;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

@Repository("mongoManager")
public class MongoManager {
	
	String host ;// 主机名
	int port ;//端口
	int poolSize ;// 连接数量
	int blockSize ;//等待队列长度

	@Value("${flyingsoft.mongodb.host}")
	public void setHost(String host) {
		this.host = host;
	}
	
	@Value("${flyingsoft.mongodb.port}")
	public void setPort(int port) {
		this.port = port;
	}
	
	@Value("${flyingsoft.mongodb.poolSize}")
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	@Value("${flyingsoft.mongodb.blockSize}")
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	
	private Mongo mongo = null;
	 
	/**
	 * 根据名称获取DB，相当于是连接
	 * @param dbName
	 * @return
	 */
	public DB getDB(String dbName) {
		if (mongo == null) {
			init();
	    }
	    return mongo.getDB(dbName);
	}
	
	/**
	 * 初始化连接池参数
	 */
	private void init(){
		try {
			mongo = new Mongo(host, port);
			MongoOptions opt = mongo.getMongoOptions() ;
			opt.connectionsPerHost = poolSize ;
			opt.threadsAllowedToBlockForConnectionMultiplier = blockSize ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
