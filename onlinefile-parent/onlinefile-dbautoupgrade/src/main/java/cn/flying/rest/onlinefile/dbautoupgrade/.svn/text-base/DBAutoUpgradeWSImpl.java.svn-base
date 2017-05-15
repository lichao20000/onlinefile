package cn.flying.rest.onlinefile.dbautoupgrade;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Path;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.dbautoupgrade.driver.DBAutoUpgradeDao;
import cn.flying.rest.onlinefile.restInterface.DBAutoUpgradeWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@Path("/dbautoupgrade")
@Component
public class DBAutoUpgradeWSImpl extends BaseWS implements DBAutoUpgradeWS {
	
	private final String DTD_not_load = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private final String DTD_not_validation = "http://xml.org/sax/features/validation";
	
	@Resource
	private DBAutoUpgradeDao autoUpgradeDao;
	
	public DBAutoUpgradeDao getAutoUpgradeDao() {
		return autoUpgradeDao;
	}

	public void setAutoUpgradeDao(DBAutoUpgradeDao autoUpgradeDao) {
		this.autoUpgradeDao = autoUpgradeDao;
	}
	
	public void doStart(){
		this.excuteSql();
		
	}
	public boolean excuteSql(){
		String s =getClassPath();
		/**
		 * xml文件存放路径.
		 */
		String xmlFilePath = null ;
		String databaseUpdateDir = s.substring(0, s.lastIndexOf("/WEB-INF"))+"/databaseUpdate/";
		xmlFilePath = databaseUpdateDir+"sql.xml";
		String logFilePath = databaseUpdateDir+"sqllog.log";
		File file = new File(xmlFilePath) ;
		StringBuffer sb = new StringBuffer() ;
		//没有找到配置文件，直接返回
		if(!file.exists()){
			System.out.println("在 " + xmlFilePath + " 未找到升级文件，请加载...");
		}
		
		//判断系统表存在否 
		autoUpgradeDao.checkApp_Config();
		//
		long start = System.currentTimeMillis();
		int edition = autoUpgradeDao.getVersion();
		
		Date nowTime=new Date(System.currentTimeMillis());
		SimpleDateFormat cf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String rTime = cf.format(nowTime);
		
		SAXBuilder builder = new SAXBuilder() ;
		builder.setFeature(DTD_not_load, false);   
		builder.setFeature(DTD_not_validation,false);
		org.jdom.Document docs = null;
		
		
		try {
			docs = builder.build(file) ;
			
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		
		org.jdom.Element root = docs.getRootElement() ;
		String type = null ;//类型
		String sqlText = null ;//标签中的SQL语句
		List node = null ;
		List childNodeSql = null ;
		List childNodeFuction = null ;
		List childNodeProcedure = null ;
		
		
		node = root.getChildren("version");
		System.out.println("数据库升级中，请稍侯...");
		int maxVersion = edition;
		String editionResult = null ;
		for(Iterator it = node.iterator() ; it.hasNext() ; ){
			Element enode =(Element)it.next() ;//找节点sql,function,procedure
			
			int version =Integer.parseInt( ((org.jdom.Element) enode).getAttributeValue("edition") );
			if(version > edition){
				
				//最后把所有版本号中最大的一个更新的版本号保存
				
				maxVersion = maxVersion < version ? version : maxVersion ;//防止叠代所取数据不按版本号顺序取，即：从小到大，把最大的存起来。
				editionResult = String.valueOf(maxVersion) ;
				
				try {
					childNodeSql = enode.getChildren("sql");
					childNodeFuction = enode.getChildren("function");
					childNodeProcedure = enode.getChildren("procedure");
				} catch (Exception e1) {
					e1.printStackTrace() ;
				}
				sb.append("*******************************************************************\r\n") ;
				sb.append(rTime +"　　更新版本号为  【 version = " + version + " 】  日志信息 \r\n") ;
				//查找含有sql标签的sql语句
				for(Iterator itsql= childNodeSql.iterator() ; itsql.hasNext() ;){
					
					Element sql = (Element)itsql.next() ;
					type = "sql" ;
					sqlText = sql.getText() ;
					if(sqlText.trim() != null && !"".equals(sqlText.trim())){//处理标签中没有sql
						StringBuffer sf = autoUpgradeDao.dostart(sqlText.trim(),type, version, logFilePath);
						sb.append(sf);
					}
				}
				
				//查找含有function标签的函数
				for(Iterator itfun= childNodeFuction.iterator() ; itfun.hasNext() ;){
					Element fun = (Element)itfun.next() ;
					type = "function" ;
					sqlText = fun.getText() ;
					if(sqlText.trim() != null && !"".equals(sqlText.trim())){
						StringBuffer sf = autoUpgradeDao.dostart(sqlText.trim(),type, version, logFilePath);
						sb.append(sf);
//						}
					}
				}
				
				//查找含有procedure标签的存储过程
				for(Iterator itpro= childNodeProcedure.iterator() ; itpro.hasNext() ;){
					Element pro = (Element)itpro.next() ;
					type = "procedure" ;
					sqlText = pro.getText() ;
					if(sqlText.trim() != null && !"".equals(sqlText.trim())){//处理标签中没有sql
						StringBuffer sf = autoUpgradeDao.dostart(sqlText.trim(),type, version, logFilePath);
						sb.append(sf);
					}
				}
				
				
			}
			
		}
	
		if(editionResult != null){
			autoUpgradeDao.updateVersion(editionResult) ;
			//写日志文件
			writerIntoWorkLog(sb, logFilePath) ;
			long end = System.currentTimeMillis() ;
			System.out.println("数据库完成自动升级，消耗时间为：" + (end - start) + " 毫秒");
			
		}else{
			System.out.println("目前数据库版本为[ version = " + edition +" ] 已为最新，暂不需要更新！" );
		}
		
		return true;
			
		  }
	  private void writerIntoWorkLog(StringBuffer log, String logFilePath) {
		File f=new File(logFilePath);
		boolean flag = false ;
		flag = f.exists() ;//标识文件是否存在，存在则把日志信息追到文件后面，不存在则创建
		
		try{
			FileWriter outFile=new FileWriter(f,flag);
			BufferedWriter bufferOut=new BufferedWriter(outFile);
			bufferOut.write(log.toString());
			bufferOut.newLine();
			bufferOut.flush();
			bufferOut.close();
		} 
		
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	  }
		  
	  /**
	   * 得到本类物理路径所在文件夹
	   * @return
	   */
	  private String getClassPath(){ 
	      String strClassName = getClass().getName(); 
	      String strPackageName = ""; 
		  if(getClass().getPackage() != null) { 
		      strPackageName = getClass().getPackage().getName(); 
		  } 
		  String strClassFileName = ""; 
		  if(!"".equals(strPackageName)){
		      strClassFileName = strClassName.substring(strPackageName.length() + 1,strClassName.length()); 
		  } 
		  else { 
		      strClassFileName = strClassName; 
		  } 
		  URL url = null; 
		  url = getClass().getResource(strClassFileName + ".class"); 
		  String strURL = url.toString();
		  String middleString = System.getProperty("file.separator"); // 取得操作系统路径分割符
		  strURL = strURL.substring(strURL.indexOf( "/" ),strURL.lastIndexOf( "/" ));
		      return strURL; 
	  }



	public Boolean DBAutoUpgrade() {
		this.doStart();
	
		return true ;
	}
	
	
	
}
