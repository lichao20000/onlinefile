package cn.flying.rest.onlinefile.restInterface;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cn.flying.rest.platform.utils.MediaTypeEx;

/***
 * 数据库自动升级服务接口
 * @author xiaoxiong
 */
public interface DBAutoUpgradeWS {

	/***
	 * 数据库自动升级
	 * @return
	 */
	@GET
	@Path("DBAutoUpgrade")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public Boolean DBAutoUpgrade();
	
}
