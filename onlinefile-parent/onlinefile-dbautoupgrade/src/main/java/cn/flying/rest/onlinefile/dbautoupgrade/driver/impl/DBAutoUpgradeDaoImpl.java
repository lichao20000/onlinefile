package cn.flying.rest.onlinefile.dbautoupgrade.driver.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.dbautoupgrade.driver.DBAutoUpgradeDao;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;

@Repository
public class DBAutoUpgradeDaoImpl extends BaseDaoHibernate implements DBAutoUpgradeDao{

	public DBAutoUpgradeDaoImpl() {
		super(DBAutoUpgradeDaoImpl.class);
	}

	public int getVersion() {
		 Session session =this.getSession(false);
		 Connection conn = session.connection();
		 Statement st = null;
		 ResultSet rs = null;
		 int i = 0;
		try {
			String sql ="SELECT APPCONFIGVALUE FROM ESS_APPCONFIG WHERE APPCONFIGKEY='VERSIONKEY'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				i = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs,st, conn);
		}
		return i; 
	}
	


	public StringBuffer dostart(String sql,String type,int version,String logPath) {
		 Session session = null;
		 Connection conn = null;
		 StringBuffer buffer = new StringBuffer() ;
		 Statement st = null;
		 try {
			session = this.getOpenSession();
			conn = session.connection();
			conn.setAutoCommit(false);
			st = conn.createStatement();  //提交事务的时候出错
			if("sql".equals(type)){
				if(sql.indexOf(";")>=0)
		    	{
		    		String[] ls = sql.split(";");
		    		for(int i=0;i<ls.length;i++)
		    		{
		    			st.executeUpdate(ls[i]) ;session.flush();conn.commit();
		    		}
		    	}
		    	else{
		    		st.executeUpdate(sql) ;session.flush();conn.commit();
		    	}
			}else if("function".equals(type)){
				st.executeUpdate(sql) ;session.flush();conn.commit();
			}else if("procedure".equals(type)){
				st.executeUpdate(sql) ;session.flush();conn.commit();
			}
//			session.flush();
//			tx.commit();
			
			buffer.append("sql:\r\n     " + sql + "\r\nStates：  *** update success ！！***\r\n" ) ;
			buffer.append("---------------------------\r\n") ;
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
//			e.printStackTrace();
			System.out.println("【更新[version="+version+"]出现一个异常sql，原因可能是您的数据库版本号有问题或sql语句错误】 详细请查看日志文件："+logPath);
			buffer.append("Error sql:\r\n     " + sql + "\r\n" + "\r\n【States】：" + e.getMessage()+"\r\n" ) ;
			buffer.append("---------------------------\r\n") ;
			return buffer;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JdbcUtil.close(null,st, conn,session);
		}
		return buffer;
	}

	public void updateVersion(String editionResult) {
		 Session session =getSession(true);
		 Connection conn = session.connection();
		 Statement st = null;
		 String sql = "update ess_appconfig set APPCONFIGVALUE="+editionResult+" WHERE APPCONFIGKEY='VERSIONKEY'";
		 try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    JdbcUtil.close(st, conn);
		}
	}

	public void checkApp_Config(){
		 Session session =null;
		 Connection conn =null;
		 Statement st = null;
		 ResultSet rs = null;
		 int index = -1;
		 String sql = "show tables like 'ess_appconfig'";
		 try {
			 session =getSession(true);
			 conn = session.connection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()){
				String createsql = "CREATE TABLE `ess_appconfig` ("
					 +" `ID` int(8) NOT NULL AUTO_INCREMENT,"
					 +" `TITLE` varchar(128) DEFAULT NULL,"
					 +"  `APPCONFIGKEY` varchar(128) DEFAULT NULL,"
					 +" `APPCONFIGVALUE` varchar(512) DEFAULT NULL,"
					+"  `DESCRIPTION` varchar(512) DEFAULT NULL,"
					 +" `VALUETYPE` varchar(20) DEFAULT NULL,"
					 +" PRIMARY KEY (`ID`)"
					+") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;";
				st.executeUpdate(createsql);
				String insertsql = "INSERT INTO ess_appconfig (ID, TITLE, APPCONFIGKEY, APPCONFIGVALUE, DESCRIPTION, VALUETYPE) VALUES ('1', '数据库版本号', 'VERSIONKEY', '0', '标示当前数据库的版本号', 'APP_NUMBER');";
				index = st.executeUpdate(insertsql);
				if(index>0){
					System.out.println("第一次启动本系统，正在创建系统所需表,请稍等...");
				}else{
					System.out.println("系统表创建失败，请联系管理员");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
		    JdbcUtil.close(rs,st, conn);
		}
	}
}
