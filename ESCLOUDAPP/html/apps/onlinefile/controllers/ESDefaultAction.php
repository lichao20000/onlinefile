<?php
/**
 * 默认处理首页
 *
 */
class ESDefaultAction extends ESActionBase
{
	// 获取用户信息
	public function GetUserInfo()
	{
		$uid = $this->getUser()->getId();
		$userInfo=$this->exec("getProxy", "user")->getUserInfo($uid);
		$info = array(
				'userId' => $userInfo->userinfo->userid,
				'displayName' => $userInfo->userinfo->displayName
		);
		return $info;
	}
	//首页渲染图片
	public function index()
	{
		/** xiaoxiong 20150407 将获取到的值全部存到全局变量user中，只在登陆后获取一次，刷新界面时不再重新获取 **/
		global $user;
		if(!(isset($user) && isset($user->id) && $user->id != U_ANONYMOUS)){
			$ssoAppId = AopConfig::get('sso.appid');
			$successUrl = urlencode(AopConfig::get('sso.loginurl', url('user/token', true)));
			$errorUrl = urlencode(AopConfig::get('sso.error'));
			$baseurl = AopConfig::get('sso.baseurl') ;
			$returnUrl = isset($_SERVER['HTTP_REFERER']) ? urlencode($_SERVER['HTTP_REFERER']) : '';
			gotoUrl(AopConfig::get('sso.login'));
		}
		$openfireservername = AopConfig::get('openfireservername') ;
		$openfirebaseurl = AopConfig::get('openfirebaseurl') ;
		
		/*wangwenshuo 20160223 添加判断条件 “$user->isLoginDo=='1'” 
		 * 	isLoginDo登录成功后跳转到主页是1      主页刷新是0   
		 * 			主页刷新不需要重新获取用户信息
		 * 			登录成功需要重新获取用户信息
		 */
		if(!isset($user->dataId) || isset($_GET['companyid']) || $user->isLoginDo=='1'){ 
			$user->isLoginDo='0'; //获取用户信息后  刷新不在重新获取
			/** xiaoxiong 20150319 获取访问后台的REST服务的根URL **/
			$proxy = $this->exec("getProxy", "onlinefile_default") ;
// 			$baseUrl = $proxy->getBaseUrl() ;
			
			// longjunhao 20150902 add
			$baseUrl = AopConfig::get('sso.onlinefile');
			
			$siteUrl = substr($baseUrl, 0, strpos($baseUrl,"onlinefile") - 1) ;
			$user->siteUrl = $siteUrl;
			
			$baseUrl = substr($baseUrl, 0, strpos($baseUrl,"onlinefile")+10) ;
			$user->baseUrl = $baseUrl ;
			if(isset($_GET['companyid']))$user->bigOrgId = $_GET['companyid'] ;
			if($user->bigOrgId == '-1'){
				/**验证用户是否首次登陆 **/
				$id=$this->getUser()->getId();
				$userProxy = $this->exec('getProxy','onlinefile_userinfows');
				$returnInfo = $userProxy->getUserInitInfo($user->bigOrgId, $id);
				$returnUserInfo = $returnInfo->user ;
				$user->userCompanys = isset($returnInfo->userCompanys)?$returnInfo->userCompanys:"" ;
				$user->userCompanySize = isset($returnUserInfo->userCompanySize)?$returnUserInfo->userCompanySize:"" ;
				$user->isFirst ="0";
				$user->dataId = isset($returnUserInfo->ID)?$returnUserInfo->ID:"" ;/** 用户的唯一数据ID **/
				$user->isAdmin ="0";/** 标示用户是否为管理员 **/
				$user->COMPANYNAME ="东方飞扬云平台"; /** 公司名  **/
				$user->PORTRAIT = isset($returnUserInfo->PORTRAIT)?$returnUserInfo->PORTRAIT:"" ;/** 用户头像**/
				$user->IMGURL ="apps/onlinefile/templates/ESDefault/index/flyingsoftloginlogo.png"; /** 公司logo **/
				$user->FULLNAME = isset($returnUserInfo->FULLNAME)?$returnUserInfo->FULLNAME:""; /** 姓名 **/
				/**获取上传文件地址**/
				$ip=$this->getClientIp();
				$user->ip = $ip ;
				$user->uploadurl = "" ;
				$user->isrefresh = "1" ;
				$user->usernamemd5code  = isset($returnUserInfo->usernamemd5code)?$returnUserInfo->usernamemd5code:"";
				$user->ISUPREMIND ="1";
				$user->ISDOWREMIND ="1";
				$user->isOpenSpace ="1";
				$user->isOpenGroup ="1";
				$user->isEnterSend ="1";
				$user->setid =isset($returnUserInfo->ID)?$returnUserInfo->ID:"" ;
				$data = array('companyName'=>$user->COMPANYNAME,'companyId'=>"-1",'ip'=>$user->ip,'baseUrl'=>$user->baseUrl,'isFirst'=>$user->isFirst, 'userId'=>$user->dataId,'uploadUrl'=>$user->uploadurl, 'openfireservername'=>$openfireservername, 'openfirebaseurl'=>$openfirebaseurl, 'isAdmin'=>$user->isAdmin, 'PORTRAIT'=>$user->PORTRAIT, 'fullname'=>$user->FULLNAME, 'usernamemd5code'=>$user->usernamemd5code,'IMGURL'=>$user->IMGURL, 'defaultusrl'=>AopConfig::get('sso.login'),'ISUPREMIND'=>$user->ISUPREMIND,'ISDOWREMIND'=>$user->ISDOWREMIND, 'userCompanySize'=>$user->userCompanySize,'siteUrl'=>$user->siteUrl,'ownCompanyId'=>$user->ownCompanyId,'isOpenSpace'=>$user->isOpenSpace,'isOpenGroup'=>$user->isOpenGroup,'isEnterSend'=>$user->isEnterSend);
				return $this->renderTemplate($data);
			}else{
				/**验证用户是否首次登陆 **/
				$id=$this->getUser()->getId();
				$userProxy = $this->exec('getProxy','onlinefile_userinfows');
				$returnInfo = $userProxy->getUserInitInfo($user->bigOrgId, $id);
				$returnUserInfo = $returnInfo->user ;
				$user->userCompanys = isset($returnInfo->userCompanys)?$returnInfo->userCompanys:"" ;
				$user->userCompanySize = isset($returnUserInfo->userCompanySize)?$returnUserInfo->userCompanySize:"" ;
				$user->isFirst =isset($returnUserInfo->FIR_LOGIN )?$returnUserInfo->FIR_LOGIN :"" ;
				$user->dataId = isset($returnUserInfo->ID)?$returnUserInfo->ID:"" ;/** 用户的唯一数据ID **/
				$user->isAdmin =  isset($returnUserInfo->ISADMIN)?$returnUserInfo->ISADMIN:"";/** 标示用户是否为管理员 **/
				$user->COMPANYNAME =  isset($returnUserInfo->COMPANYNAME)?$returnUserInfo->COMPANYNAME:""; /** 公司名  **/
				$user->PORTRAIT = isset($returnUserInfo->PORTRAIT)?$returnUserInfo->PORTRAIT:"" ;/** 用户头像**/
				$user->IMGURL =  isset($returnUserInfo->IMGURL)?$returnUserInfo->IMGURL:""; /** 公司logo **/
				$user->FULLNAME = isset($returnUserInfo->FULLNAME)?$returnUserInfo->FULLNAME:""; /** 姓名 **/
				/**获取上传文件地址**/
				$ip=$this->getClientIp();
				$user->ip = $ip ;
	// 			$urlData["clientIP"] = $ip;
	// 			$urlData["isOnlinefile"] = '1';
	// 			$proxy = $this->exec('getProxy','onlinefile_userinfows');
	// 			$url = $proxy->getOnlineUploadUrl(json_encode($urlData));
				$user->uploadurl = "" ;
				$user->isrefresh = "0" ;
				$user->usernamemd5code  = $returnUserInfo->usernamemd5code ;
				$user->ISUPREMIND = isset($returnUserInfo->ISUPREMIND)?$returnUserInfo->ISUPREMIND:"1" ;
				$user->ISDOWREMIND = isset($returnUserInfo->ISDOWREMIND)?$returnUserInfo->ISDOWREMIND:"1" ;
				$user->isOpenSpace = isset($returnUserInfo->isOpenSpace)? $returnUserInfo->isOpenSpace:"1";
				$user->isOpenGroup = isset($returnUserInfo->isOpenGroup)? $returnUserInfo->isOpenGroup:"1";
				$user->isEnterSend = isset($returnUserInfo->isEnterSend)? $returnUserInfo->isEnterSend:"1";
				$user->setid = isset($returnUserInfo->setid)?$returnUserInfo->setid:"0";
			}
		} else {
			$user->isFirst = "0" ;
			$user->isrefresh = "1" ;
		}
		$data = array('companyName'=>$user->COMPANYNAME,'companyId'=>$user->bigOrgId,'ip'=>$user->ip,'baseUrl'=>$user->baseUrl,'isFirst'=>$user->isFirst, 'userId'=>$user->dataId,'uploadUrl'=>$user->uploadurl, 'openfireservername'=>$openfireservername, 'openfirebaseurl'=>$openfirebaseurl, 'isAdmin'=>$user->isAdmin, 'PORTRAIT'=>$user->PORTRAIT, 'fullname'=>$user->FULLNAME, 'usernamemd5code'=>$user->usernamemd5code,'IMGURL'=>$user->IMGURL, 'defaultusrl'=>AopConfig::get('sso.login'),'ISUPREMIND'=>$user->ISUPREMIND,'ISDOWREMIND'=>$user->ISDOWREMIND, 'userCompanySize'=>$user->userCompanySize,'siteUrl'=>$user->siteUrl,'ownCompanyId'=>$user->ownCompanyId,'isOpenSpace'=>$user->isOpenSpace,'isOpenGroup'=>$user->isOpenGroup,'isEnterSend'=>$user->isEnterSend);
		return $this->renderTemplate($data);
	}
	
	public function getUserCompanys(){
		global $user;
		if(isset($_POST['reload'])){
			$userProxy = $this->exec('getProxy','onlinefile_userinfows');
			$user->userCompanys = $userProxy->getUserCompanys($user->dataId);
			$jsonData['userCompanys'] = $user->userCompanys;
			echo json_encode($jsonData) ;
		} else {
			$jsonData['userCompanys'] = $user->userCompanys;
			echo json_encode($jsonData) ;
		}
	}

     /**
	 * 根据当前登录用户获得功能菜单
	 * fangjixiang 20130724
	 * @param userId
	 * @return
	 */
	public function getArchiveAuthMenu()
	{
		$result = array();
		$menu = array();
		$userId = $this->getUser()->getId();
		$proxy = $this->exec("getProxy", "escloud_menuservice");
		$result = $proxy->getArchiveAuthMenu($userId);
		//print_r($result);die;

		if(isset($_SESSION['navMenu'])){
			
			$menu = $_SESSION['navMenu'];
			
		}else{
				
			foreach ($result as $value)
			{
				$menu[] = array(
						'id'=>$value->id,
						'pId'=>$value->pId,
						'name'=>$value->name,
						'controller'=>$value->controller,
						'action'=>$value->action
				);
			}
				
			$menu = json_encode($menu);
				
			$_SESSION['navMenu'] = $menu;
		}
		
		echo $menu;
	}
	
	/**
	 * xiaoxiong 20150310 创建群组
	 * @return string
	 */
	public function createGroup(){
		$username = isset($_POST['username']) ? $_POST['username'] : '';
		global $user;
		$data = array('username'=>$username, 'userId'=>$user->dataId);
		return $this->renderTemplate($data);
	}
	
	/**
	 * xiaoxiong 20150310 创建群组
	 * @return string
	 */
	public function editGroup(){
		$groupid = isset($_POST['groupid']) ? $_POST['groupid'] : '';
		$groupname = isset($_POST['groupname']) ? $_POST['groupname'] : '';
		$remark = isset($_POST['remark']) ? $_POST['remark'] : '';
		global $user;
		$data = array('groupid'=>$groupid, 'groupname'=>$groupname, 'remark'=>$remark, 'userId'=>$user->dataId);
		return $this->renderTemplate($data);
	}
	
	public function saveNotSeeMessage(){
		$data['companyId'] = $_POST['companyId'];
		$data['username'] = $_POST['username'];
		$data['from'] = $_POST['from'];
		$data['content'] = $_POST['content'];
		$data['date'] = $_POST['date'];
		$data['time'] = $_POST['time'];
		$data['groupFlag'] = isset($_POST['groupFlag'])?$_POST['groupFlag']:"";
		$data['isGroup'] = $_POST['isGroup'];
		$data['fromCnName'] = $_POST['fromCnName'];
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->saveNotSeeMessage(json_encode($data));
		echo $return;
	}
	
	public function saveHistoryMessage(){
		$data['companyId'] = $_POST['companyId'];
		$data['username'] = $_POST['username'];
		$data['from'] = $_POST['from'];
		$data['content'] = $_POST['content'];
		$data['date'] = $_POST['date'];
		$data['time'] = $_POST['time'];
		$data['isGroup'] = $_POST['isGroup'];
		$data['fromCnName'] = $_POST['fromCnName'];
		$data['styleTpl'] = $_POST['styleTpl'];
		$data['fileFlag'] = isset($_POST['fileFlag'])?$_POST['fileFlag']:"";
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->saveHistoryMessage(json_encode($data));
		echo json_encode($return);
	}
	
	/** lujixiang 20151113   将申请文件的消息发送方式改为post，修复乱码 **/
	public function saveHistoryMessageReturnID(){
		$data['companyId'] = $_POST['companyId'];
		$data['username'] = $_POST['username'];
		$data['from'] = $_POST['from'];
		$data['content'] = $_POST['content'];
		$data['date'] = $_POST['date'];
		$data['time'] = $_POST['time'];
		$data['isGroup'] = $_POST['isGroup'];
		$data['fromCnName'] = $_POST['fromCnName'];
		$data['styleTpl'] = $_POST['styleTpl'];
		$data['fileFlag'] = isset($_POST['fileFlag'])?$_POST['fileFlag']:"";
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->saveHistoryMessageReturnID(json_encode($data));
		echo json_encode($return);
		
	}
	
	
	
	/**
	 * 移交分类之  更换分类管理员
	 */
	public function changeGroupAdmin(){
		$data['companyId'] = $_POST['companyId'];
		$data['username'] = $_POST['username'];
		$data['userid'] = $_POST['userid'];
		$data['tousername'] = $_POST['tousername'];
		$data['touserid'] = $_POST['touserid'];
		$data['groupid'] = $_POST['groupid'];
		$data['classId'] = $_POST['classId'];
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->changeGroupAdmin(json_encode($data));
		echo json_encode($return);
		
	}
	/**
	 * 申请分类之 获得分类的管理员信息
	 */
	public function getGroupUserIsAdminInfo(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['groupid'] = $_POST['groupid'];
		$data['groupflag'] = $_POST['groupflag'];
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->getGroupUserIsAdminInfo(json_encode($data));
		echo json_encode($return);
		
	}
	public function deleteGroup(){//userid:window.userId,groupflag:flag,groupid:groupid
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['userid'] = $_POST['userid'];
		$data['groupid'] = $_POST['groupid'];
		$data['ip'] = $this->getClientIp();
		$data['groupflag'] = $_POST['groupflag'];
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->deleteGroup(json_encode($data));
		echo json_encode($return);
	}
	public function getCompanyUsersForGroupSet(){//userid:window.userId,groupid:groupid
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['userid'] = $_POST['userid'];
		$data['flag'] = $_POST['flag'];
		$data['groupid'] = $_POST['groupid'];
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->getCompanyUsersForGroupSet(json_encode($data));
		echo json_encode($return);
	}
	public function getCompanyUsersForGroupSetAndNotJoin(){//classId:classId,flag:flag
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['userid'] = $_POST['userid'];
		$data['groupid'] = $_POST['groupid'];
		$data['flag'] = $_POST['flag'];
		$data['classId'] = $_POST['classId'];
		$data['ip'] = $this->getClientIp();
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->getCompanyUsersForGroupSetAndNotJoin(json_encode($data));
		echo json_encode($return);
	}
	/**
	 * 退出分类
	 */
	public function outGroup(){//groupflag:flag,fullname
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['userid'] = $_POST['userid'];
		$data['groupid'] = $_POST['groupid'];
		$data['groupflag'] = $_POST['groupflag'];
		$data['fullname'] = $_POST['fullname'];
		$data['data_idseq'] = isset($_POST['data_idseq'])?$_POST['data_idseq']:"";
		$data['groupsOwnerId'] = isset($_POST['groupsOwnerId'])?$_POST['groupsOwnerId']:"";
		$data['ip'] = $this->getClientIp();
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->outGroup(json_encode($data));
		echo json_encode($return);
	}
	/**
	 * resetGroup 修改
	 */
	public function resetGroup(){//
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['manageruserid'] = $_POST['manageruserid'];
		$data['groupid'] = $_POST['groupid'];
		$data['groupflag'] = $_POST['groupflag'];
		$data['fullname'] = $_POST['fullname'];
		$data['groupname'] = $_POST['groupname'];
		$data['addgroupuserids'] = $_POST['addgroupuserids'];
		$data['deletegroupuserids'] = $_POST['deletegroupuserids'];
		$data['groupremark'] = $_POST['groupremark'];
		$data['changeusers'] = $_POST['changeusers'];
		$data['changeitems'] = $_POST['changeitems'];
		$data['data_idseq'] = isset($_POST['data_idseq'])?$_POST['data_idseq']:"";
		$data['ip'] = $this->getClientIp();
		$proxy = $this->exec("getProxy", "onlinefile_default") ;
		$return = $proxy->resetGroup(json_encode($data));
		echo json_encode($return);
	}
	
	// 文件浏览参数
	public function getViewUrl(){
		$fileId = isset($_POST["fileId"])?$_POST["fileId"]:'';
		$parms = array();
		$parms["result"] = "ok";
		$parms["type"] = "swf";
		$parms["fileId"] = $fileId;//54ae799b33f14231933fd31c2a632f49.swf  61a60215838c4727b70be32e9edd831a.swf
		$parms["Scale"] = 0.73;//1-5-0eb9f49071461032bb960cc47a0c0610.pdf.swf   //bca158729c2048af82c2ff9d0084107d.swf
		$parms["SwfFile"] = 'http://168.168.169.100/apps/onlinefile/templates/ESDefault/images/33.swf';//文件浏览地址
		$parms["codeImageUrl"] = 'http://168.168.169.100/apps/onlinefile/templates/ESDefault/images/bf09b80ea0c441c8a0b85634fa53333b.jpg';//二维码地址
		$parms["ReadOnly"] = false; //只读控制
		$parms["CanPrint"] = true; //打印控制
		$parms["WatermarkEnabled"] = false; //水印控制
		$parms["WatermarkText"] = '水印内容'; //水印内容
		$parms["WatermarkSize"] = 54; //水印内容
		$parms["WatermarkRotation"] = -45; //水印旋转角度
		// 原文下载控制
		$parms["CanDownload"] = true; //下载控制
		$parms["DownloadUrl"] = '';
		$parms["FileName"] = '文件名';
		echo json_encode($parms);
	}
}
