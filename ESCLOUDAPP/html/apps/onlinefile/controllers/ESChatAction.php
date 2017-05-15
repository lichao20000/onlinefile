<?php
/**
 * 默认处理首页
 *
 */
class ESChatAction extends ESActionBase
{   
    const PROXY_NAME = "onlinefile_chatws";
	public function getHistoryMessage() {
		//$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$data ['companyId'] = $_POST['companyId'];
		$data ['receiver'] = $_POST['receiver'];
		$data ['username'] = $_POST['username'];
		$data ['isGroup'] = $_POST['isGroup'];
		$data ['limit'] = $_POST['limit'];
		$data ['page'] = $_POST['page'];
		$data ['skip'] = $_POST['skip'];
		$data ['keyword'] = $_POST['keyword'];
		$data ['joindate'] = $_POST['joindate'];
		$data ['jointime'] = $_POST['jointime'];
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->getHistoryMessage($postData);
		echo json_encode($result);
	}

}