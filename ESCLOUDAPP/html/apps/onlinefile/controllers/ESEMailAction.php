<?php
/**
 * 默认处理首页
 *
 */
class ESEMailAction extends ESActionBase
{
	//首页渲染图片
	public function index()
	{
		return $this->renderTemplate();
	}

	public function addEmail(){
		$map = array();
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['password'] = isset($_POST['password'])?$_POST['password']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->addEmail(json_encode($map));
		echo json_encode ( $data );
	}
	
	public function addEmailManual(){
		$map = array();
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['password'] = isset($_POST['password'])?$_POST['password']:"";
		$map['popServerInput'] = isset($_POST['popServerInput'])?$_POST['popServerInput']:"";
		$map['popSSLPortInput'] = isset($_POST['popSSLPortInput'])?$_POST['popSSLPortInput']:"";
		$map['smtpServerInput'] = isset($_POST['smtpServerInput'])?$_POST['smtpServerInput']:"";
		$map['smtpSSLPortInput'] = isset($_POST['smtpSSLPortInput'])?$_POST['smtpSSLPortInput']:"";
		
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->addEmailManual(json_encode($map));
		echo json_encode ( $data );
	}
	
	public function deleteEmail(){
		$map = array();
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->deleteEmail(json_encode($map));
		echo json_encode ( $data );
	}
	
	public function deleteAllEmail(){
		$map = array();
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->deleteAllEmail(json_encode($map));
		echo json_encode ( $data );
	}
	
	public function updateEmail(){
		$map = array();
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->updateEmail(json_encode($map));
		echo $data;
	}
	
	public function searchEmailAttachment(){
		$map = array();
		$map['searchKeyWord'] = isset($_POST['searchKeyWord'])?$_POST['searchKeyWord']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->searchEmailAttachment(json_encode($map));
		echo $data;
	}
	

	public function getEmailList(){
		$map = array();
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->getEmailList(json_encode($map));
		echo json_encode($data);
	}

	public function getEmailSettingByEmail(){
	
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->getEmailSettingByEmail(json_encode($map));
		echo json_encode($data);
	}
	
	public function saveEmailSetting(){
	
		$map['id'] = isset($_POST['id'])?$_POST['id']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['password'] = isset($_POST['password'])?$_POST['password']:"";
		$map['receiverserver'] = isset($_POST['receiverserver'])?$_POST['receiverserver']:"";
		$map['receiveserverport'] = isset($_POST['receiveserverport'])?$_POST['receiveserverport']:"";
		$map['sendserver'] = isset($_POST['sendserver'])?$_POST['sendserver']:"";
		$map['sendserverport'] = isset($_POST['sendserverport'])?$_POST['sendserverport']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->saveEmailSetting(json_encode($map));
		echo json_encode($data);
	}
	
	public function setAsDefaultEmail(){
	
		$map['id'] = isset($_POST['id'])?$_POST['id']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->setAsDefaultEmail(json_encode($map));
		echo json_encode($data);
	}
	
	public function cacheAllEmailAttachments(){
		$map['id'] = isset($_POST['id'])?$_POST['id']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->cacheAllEmailAttachments(json_encode($map));
		echo json_encode($data);
	}
	
	public function getDefaultAttachmentByEmail(){
		$map['id'] = isset($_POST['id'])?$_POST['id']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$map['pageIndex'] = isset($_POST['pageIndex'])?$_POST['pageIndex']:"";
		$map['pageLimit'] = isset($_POST['pageLimit'])?$_POST['pageLimit']:"";
		$map['windowWidth'] = isset($_POST['windowWidth'])?$_POST['windowWidth']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->getDefaultAttachmentByEmail(json_encode($map));
		echo json_encode($data);
	}
	
	public function searchEmialAttachsByKeyWord(){
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$map['searchKeyWord'] = isset($_POST['searchKeyWord'])?$_POST['searchKeyWord']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['pageIndex'] = isset($_POST['pageIndex'])?$_POST['pageIndex']:"";
		$map['pageLimit'] = isset($_POST['pageLimit'])?$_POST['pageLimit']:"";
		$map['windowWidth'] = isset($_POST['windowWidth'])?$_POST['windowWidth']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->searchEmialAttachsByKeyWord(json_encode($map));
		echo json_encode($data);
	}
	
	public function isSynEmailAttachMent(){
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$map['email'] = isset($_POST['email'])?$_POST['email']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->isSynEmailAttachMent(json_encode($map));
		echo json_encode($data);
	}
	
	public function getAttachmentByEmail(){
		$map['userid'] = isset($_POST['userid'])?$_POST['userid']:"";
		$map['username'] = isset($_POST['username'])?$_POST['username']:"";
		$map['companyName'] = isset($_POST['companyName'])?$_POST['companyName']:"";
		$map['emailAddress'] = isset($_POST['emailAddress'])?$_POST['emailAddress']:"";
		$map['pageIndex'] = isset($_POST['pageIndex'])?$_POST['pageIndex']:"";
		$map['pageLimit'] = isset($_POST['pageLimit'])?$_POST['pageLimit']:"";
		$map['windowWidth'] = isset($_POST['windowWidth'])?$_POST['windowWidth']:"";
		$map['ip'] = isset($_POST['ip'])?$_POST['ip']:"";
		$proxy=$this->exec('getProxy','onlinefile_emailws');
		$data=$proxy->getAttachmentByEmail(json_encode($map));
		echo json_encode($data);
	}
	

}