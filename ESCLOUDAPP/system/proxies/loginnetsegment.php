<?php

class ProxyLoginnetsegment extends AgentProxyAbstract {
	
	const SERVICE_NAME = "loginNetSegmentWS";
	
	
	/**
	 * 获取目标登录IP
	 */
	public function getDataByCilentIP($postData){
		$urlParam = array('getDataByCilentIP');
		$url = implode('/', $urlParam);
		return $this->post(self::SERVICE_NAME, $url,$postData,"application/json;charset=UTF-8");
	}
	
	
	
	
}

?>