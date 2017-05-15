<?php 
/**
 *
 * @author liumingchao
 * @Date 20150225
 */

class ESUserInfoAction extends ESActionBase{
	public function user_perfect(){
		global $user;
		//获得该注册用户的id
		$id=$this->getUser()->getId();
		$data['username'] = $id;
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$orgList=$Proxy->getOrgByUserId(json_encode($data));
		$returnInfo=$Proxy->getUserInfoByUserName(json_encode($data));
		echo json_encode($returnInfo);
		
	}
	
	public function accountAndLog(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['userId'] = $_POST['userId'];
		$data['ip'] = $this->getClientIp();
		$data['username'] = $this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->getUserInfoByUserId(json_encode($data));
		return $this->renderTemplate(array('userinfo'=>$returnInfo,'companyId'=>$user->bigOrgId,'PORTRAIT'=>$user->PORTRAIT, 'ISUPREMIND'=>$user->ISUPREMIND, 'ISDOWREMIND'=>$user->ISDOWREMIND,'isOpenSpace'=>$user->isOpenSpace,'isOpenGroup'=>$user->isOpenGroup,'isEnterSend'=>$user->isEnterSend));
	}
	
	//去往安全日志
	public function safetyLog(){
		return $this->renderTemplate();
	}
	
	//查出单个用户的xinx
	public function getSingleUserInfo(){
		global $user;
		//获得该注册用户的id
		$id=$_POST['username'];
		$data['username'] = $id;
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$orgList=$Proxy->getOrgByUserId(json_encode($data));
		$returnInfo=$Proxy->getUserInfoByUserName(json_encode($data));
		echo json_encode($returnInfo);
	
	}
	
	
	public function editUserInfo(){
		$ip=$this->getClientIp();
		global $user ;
		if(isset($_POST['param'])){
			parse_str($_POST['param'],$data);
		} else {
			//修改按钮 
			$data['fullname'] = $_POST['fullname'];
			$data['signature'] = $_POST['signature'];
			$data['mobilephone'] = $_POST['mobilephone'];
			$data['telephone'] = $_POST['telephone'];
			$data['email'] = $_POST['email'];
			$data['position'] = $_POST['position'];
			$data['fax'] = $_POST['fax'];
		}
		$data['username'] = $user->id;
		$data['id'] = $user->dataId;
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $ip;
		if(!isset($data['fullname'])){
			$data['fullname'] = $user->userName;
		}
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->editUserInfo(json_encode($data));
		$datas['success']=$returnInfo->success;
		if($returnInfo->success == true){
			$user->userName = $data['fullname'];
		}
		echo json_encode($datas);
	}
	//个性设置
    public function do_singleSetUser(){
		global $user;
		$curuserId = $this->getUser()->getId ();//获得userid
		$ip=$this->getClientIp();
		$data['companyId'] = $user->bigOrgId;
		$data ['id'] = $curuserId;
		$data ['ip'] = $ip;
		$data ['isUpRemind'] = $_POST['isUpRemind'];
		$data ['isDownRemind'] = $_POST['isDownRemind'];
		$data ['isOpenSpace'] = $_POST['isOpenSpace']; //添加左侧显示
		$data ['isOpenGroup'] = $_POST['isOpenGroup']; //添加左侧显示
		$data ['userid'] = $_POST['userid'];
		$data ['isupdate'] = $user->setid;
		$postDa = json_encode ( $data );
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$Proxy->singleSetUser($postDa);
		$user->ISUPREMIND = $_POST['isUpRemind'];
		$user->ISDOWREMIND =$_POST['isDownRemind'];
		$user->isOpenSpace =$_POST['isOpenSpace'];
		$user->isOpenGroup =$_POST['isOpenGroup'];
		$user->setid = "1" ;
		echo json_encode($return);
	}
	/**文件评论回车设置**/
	public function commentEnterSet(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data ['isEnterSend'] = $_POST['isEnterSend']; //是否enter发送
		$data ['userid'] = $_POST['userid'];
		$postDa = json_encode ( $data );
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$Proxy->commentEnterSet($postDa);
		$user->isEnterSend =$_POST['isEnterSend'];
		echo json_encode($return);
	}
	
	
	//获取个性设置
    public function do_getSingleSet(){
		global $user;
		$curuserId = $this->getUser()->getId ();//获得userid
		$ip=$this->getClientIp();
		$data['companyId'] = $user->bigOrgId;
		$data ['id'] = $curuserId;
		$data ['ip'] = $ip;
		$data ['userid'] = $_POST['userid'];
		$postDa = json_encode ( $data );
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$Proxy->getSingleSet($postDa);
		echo json_encode($return);
	}
	
	
	
	//点击修改密码按钮
	public function changepassword(){
		return $this->renderTemplate();
	}
	
	//修改密码
	public function do_changepassword(){
		global $user;
		$curuserId = $this->getUser()->getId ();//获得userid
		$ip=$this->getClientIp();
		/**
			把参数封装起来
		 */
		$data['companyId'] = $user->bigOrgId;
		$data ['id'] = $curuserId;
		$data ['ip'] = $ip;
		$data ['oldPassword'] = $_POST['oldPassword'];
		$data ['newPassword'] = $_POST['newPassword'];
		$data ['userId'] = $_POST['userId'];
		$postData = json_encode ( $data );
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$Proxy->modifyPassword($postData);
		echo json_encode($return);
	}
	
	//上传图片按钮
	public function getUploadURL(){
		$ip=$this->getClientIp();
		$data = "C:\Users\admin\Desktop\新建文件夹";
		echo $ip;
	}
	
	//更改在线离线状态
	public function changeStatus(){
		$id=$this->getUser()->getId();
		$status=$_POST['status'];
		$success['ischange']='true';
		$success['status']='0';
		echo json_encode($success);
	}
	//进入首页查看是否在线
	public function isOnline(){
		$curuserId = $this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$curuserInfo=$Proxy->getUserByUserid($curuserId);
		//---------------
// 		$param = json_encode(array('userId'=>$curuserId));没获得到头像
// 		$success['icon'] = $proxy->getIconByUserId($param);
		//---------------
		$success['id']=$curuserInfo->id;
		$success['userid']=$curuserInfo->userid;
		$success['lastname']=$curuserInfo->lastname;
		$success['firstname']=$curuserInfo->firstname;
		$success['mobtel']=$curuserInfo->mobtel;
		$success['emailaddress']=$curuserInfo->emailaddress;
		$success['status']='1';
		echo json_encode($success);
	}
	//是不是admin
	public function adduser(){
		global $user;
		$id = $this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $id;
		$data['ip'] = $this->getClientIp();
		//查询当前用户所在公司的所有机构
		$orgList=$Proxy->getOrgByUserId(json_encode($data));
		return $this->renderTemplate(array('success'=>$orgList));
	}
	//点击邀请后
	public function do_addUser(){
		global $user;
		$id = $this->getUser()->getId();
// 		$data = $_POST['d'];
// 		$parem = array('data'=>$data);
		parse_str($_POST['d'],$param);
		$param['ID'] = $id;  //拿到当前用的id   方便后台查公司id
		$param['ip'] = $this->getClientIp();
		$param['companyid'] = $user->bigOrgId;
		$param['ipandport'] = $_SERVER['HTTP_HOST'];
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->adduser(json_encode($param));
		echo json_encode($returnInfo);
	}
	//点击邀请后(2)
	public function do_inviteAddUser(){
		global $user;
		$id = $this->getUser()->getId();
// 		$data = $_POST['d'];
// 		$parem = array('data'=>$data);
		parse_str($_POST['d'],$param);
		$param['ID'] = $id;  //拿到当前用的id   方便后台查公司id
		$param['ip'] = $this->getClientIp();
		$param['companyid'] = $user->bigOrgId;
		$param['ipandport'] = $_SERVER['HTTP_HOST'];
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->inviteAddUser(json_encode($param));
		echo json_encode($returnInfo);
	}
	
	//输入完username后验证此用户名是否存在
	public function userIsExist(){
		$id = $this->getUser()->getId();
		$username = $_POST['username'];
		$param['ID'] = $id;  //拿到当前用的id   方便后台查公司id
		$param['USERNAME'] = $username;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->userIsExist(json_encode($param));
		echo json_encode($returnInfo);
	}
	//开启邀请链接
	public function openUrl(){
		global $user;
		$data['classId'] = $_POST['classId'];
		$data['groupid'] = $_POST['groupid'];
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data['ipandport'] = $_SERVER['HTTP_HOST'];
		$data['companyId'] = $user->bigOrgId;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->openUrl(json_encode($data));
		echo json_encode($returnInfo);
	}
	
	//跳转到激活页面  
	public function toActive(){
		$openfirebaseurl = AopConfig::get('openfirebaseurl') ;
		$id = $_GET['id'];
		//$fullname = urldecode($_GET['fullname']);
		$companyid = $_GET['companyid'];
		$code = $_GET['code'];
		$email = $_GET['email'];
		$classId = $_GET['classId'];
		$sendUserName = urldecode(isset($_GET['sendUserName'])?$_GET['sendUserName']:"");
		$sendTime = isset($_GET['sendTime'])?$_GET['sendTime']:"";
		$className = urldecode($_GET['className']);
		//$companyName = urldecode($_GET['companyName']);
		$data = array('openfirebaseurl'=>$openfirebaseurl, 'email'=>$email, 'id'=>$id, 'classId'=>$classId, 'companyid'=>$companyid, 'code'=>$code,'className'=>$className,'sendUserName'=>$sendUserName, 'sendTime'=>$sendTime);
		return $this->renderTemplate($data);
	}
	//跳转到激活页面  
	public function toActiveByLink(){
		$openfirebaseurl = AopConfig::get('openfirebaseurl') ;
		$companyid = $_GET['companyid'];
		$classId = $_GET['classId'];
		$className = urldecode($_GET['className']);
		$sendUserName = urldecode($_GET['sendUserName']);
		//$companyName = urldecode($_GET['companyName']);
		$data = array('openfirebaseurl'=>$openfirebaseurl,  'classId'=>$classId, 'companyid'=>$companyid, 'className'=>$className,'sendUserName'=>$sendUserName);
		return $this->renderTemplate($data);
	}
	//点击激活时
	public function activateAccount(){
		
		$id = $_POST['id'];
		$fullname = $_POST['fullname'];
		$password = $_POST['password'];
		$username = $_POST['username'];
		$companyid = $_POST['companyid'];
// 		$companyName = $_POST['companyName'];
		$className = $_POST['className'];
		$classId = $_POST['classId'];
		$sendTime = $_POST['sendTime'];
		$param['id'] = $id;
		$param['fullname'] = $fullname;
		$param['password'] = $password;
		$param['username'] = $username;
		$param['companyid'] = $companyid;
// 		$param['companyName'] = $companyName;
		$param['className'] = $className;
		$param['classId'] = $classId;
		$param['sendTime'] = $sendTime;
		$param['sendUserName'] = $_POST['sendUserName'];
		$param['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->activateAccount(json_encode($param));
		global $user;
		$user = null;
		echo json_encode($returnInfo);
	}
	//点击激活时
	public function activateAccountByLink(){
		$fullname = $_POST['fullname'];
		$password = $_POST['password'];
		$username = $_POST['username'];
		$companyid = $_POST['companyid'];
		$className = $_POST['className'];
		$classId = $_POST['classId'];
		$param['fullname'] = $fullname;
		$param['password'] = $password;
		$param['username'] = $username;
		$param['companyid'] = $companyid;
		$param['className'] = $className;
		$param['classId'] = $classId;
		$param['sendUserName'] = $_POST['sendUserName'];
		$param['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->activateAccountByLink(json_encode($param));
		
		echo json_encode($returnInfo);
	}
	//去往用户列表
	public function toUserList(){
		return $this->renderTemplate();
	}
	//在线用户界面跳转
	public function onlineUsers(){
		return $this->renderTemplate();
	}
	
	//用户列表
	public function userListBycompanyId(){
		$page = isset($_POST['page']) ? $_POST['page'] : 1;
		$rp = isset($_POST['rp']) ? $_POST['rp'] : 10;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data ['startNo'] = ($page - 1) * $rp;
		$data ['limit'] = $rp;
		//$data['userIp'] = $this->getClientIp();
		global $user;
		$data['username'] = $this->getUser()->getId();
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $this->getClientIp();
		$rows=$Proxy->userList(json_encode($data));
		$total = $Proxy->getCountAll (json_encode($data));
		$start = 1;
		$jsonData = array (
				'startNo' => ($page - 1) * $rp+1,
				'endNo' => ($total>$page * $rp?$page * $rp:$total),
				'soPage' => ($page <= ceil($total/$rp)?$page:ceil($total/$rp)),
				'pagecount' => ceil($total/$rp),
				'total' => $total,
				'rows' => $rows
		);
		echo json_encode ( $jsonData );
	}
	//检索用户列表
	public function userListByUserName(){
		$page = isset($_POST['page']) ? $_POST['page'] : 1;
		$pageS = $page > 0 ? $page : 1;
		$rp = isset($_POST['rp']) ? $_POST['rp'] : 10;
		$selectuserflg = $_POST['selectuserflg'];
		$searchusername = $_POST['searchusername'];
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data ['startNo'] = ($pageS - 1) * $rp;
		$data ['limit'] = $rp;
		//$data['userIp'] = $this->getClientIp();
		global $user;
		$data['username'] = $this->getUser()->getId();
		$data['selectuserflg'] = $selectuserflg;
		$data['searchusername'] = $searchusername;
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $this->getClientIp();
		$total = $Proxy->getCountAll (json_encode($data));
		//$data ['startNo'] = ($pageS < (ceil($total/$rp) )?(($pageS - 1) * $rp):0);
		$rows=$Proxy->userListByUserName(json_encode($data));
		$start = 1;
		$jsonData = array (				
				'startNo' => ($pageS <= (ceil($total/$rp) )?(($pageS - 1) * $rp +1):0),
				'endNo' => ($total>$pageS * $rp?$pageS * $rp:$total),
				'soPage' => ($pageS < ceil($total/$rp)?$pageS:ceil($total/$rp)),
				'pagecount' => ceil($total/$rp),
				'total' => $total,
				'rows' => $rows
		);
		echo json_encode ( $jsonData );
	}
	//删除多个用户
	public function deleteUserList(){
		global $user;
		$data['username'] = $this->getUser()->getId();
		$data['companyId'] = $user->bigOrgId;
		$data['userId'] = $_POST['userId'];
		$data ['usernames'] = $_POST['usernames'];
		$data ['ids'] = $_POST['ids'];
		$data['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$success = $Proxy->deleteUserList(json_encode ( $data ));
		echo json_encode($success);
	}
	
	
	//判断是否首次登录
	public function isFirstLogin(){
		$id=$this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$num = $Proxy->isFirstLogin($id);
		echo $num;
	}
	
	//激活页面展现时查询数据
	public function showActivate(){
		$data['companyId'] =$_POST['companyId'];
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data ['id'] = $_POST['id'];
		$data ['code'] = $_POST['code'];
		$data ['className'] = $_POST['className'];
		$data ['classId'] = $_POST['classId'];
		$data ['sendTime'] = $_POST['sendTime'];
		$postData = json_encode ( $data );
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$Proxy->showActivate($postData);
		echo json_encode($return);
	}
	
	//用户启用禁用状态变更
	public function changeIsEnableStatus(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['ip'] = $this->getClientIp();
		$data['username'] = $this->getUser()->getId();
		$data ['userIds'] = $_POST['userIds'];
		$data ['userId'] = $_POST['userId'];
		$data ['userNames'] = $_POST['userNames'];
		$data ['fullNames'] = $_POST['fullNames'];
		$data ['emails'] = $_POST['emails'];
		$data ['userstatus'] = $_POST['userstatus'];
		$id=$this->getUser()->getId();
		$data ['loginUser'] = $id;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$success = $Proxy->changeIsEnableStatus(json_encode ( $data ));
		echo json_encode($success);
	}
	
	//重新发送邮件
	public function reSendMail(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data ['userId'] = $_POST['userId'];
		$data ['emails'] = $_POST['emails'];
		$data['ipandport'] = $_SERVER['HTTP_HOST'];
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$success = $Proxy->reSendMail(json_encode ( $data ));
		echo json_encode($success);
	}
	
	//渲染编辑页面
	public function edituser(){
		global $user;
		$data['companyId'] = $user->bigOrgId;
		$data['userId'] = $_POST['userId'];
		$data['ip'] = $this->getClientIp();
		$data['username'] = $this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->getUserInfoByUserId(json_encode($data));
		$data['ID'] = $returnInfo->ID;
		$data['USERNAME'] = $returnInfo->USERNAME;
		$data['FULLNAME'] = $returnInfo->FULLNAME;
		$data['TELEPHONE'] = $returnInfo->TELEPHONE;
		$data['EMAIL'] = $returnInfo->EMAIL;
		$data['MOBILEPHONE'] = $returnInfo->MOBILEPHONE;
		$data['FAX'] = $returnInfo->FAX;
		$data['SIGNATURE'] = $returnInfo->SIGNATURE;
		$data['POSITION'] = $returnInfo->POSITION;
		$data['ORGID'] = $returnInfo->ORGID;
		$data['ORGNAME'] = $returnInfo->ORGNAME;
		$data['COMPID'] = $returnInfo->COMPID;
		$data['COMPNAME'] = $returnInfo->COMPNAME;
		//查询当前用户所在公司的所有机构
		
		$id = $this->getUser()->getId();
		$data['companyId'] = $user->bigOrgId;
		$data['username'] = $id;
		$data['ip'] = $this->getClientIp();
		//查询当前用户所在公司的所有机构
		$orgList=$Proxy->getOrgByUserId(json_encode($data));
		return $this->renderTemplate(array('userinfo'=>$data,'success'=>$orgList));
	}
	//修改用户的保存操作
	public function do_editUser(){
		parse_str($_POST['edituser'],$param);
		$param['ip'] = $this->getClientIp();
		$param['username'] = $this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$success=$Proxy->editUser(json_encode($param));
		echo json_encode($success);
	}
	//输入完邮箱后验证此邮箱是否存在
	public function do_verifyMailbox(){
		global $user;
		$id = $this->getUser()->getId();
		$email = $_POST['email'];
		$param['EMAIL'] = $email;
		$param['companyId'] = $user->bigOrgId;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->verifyMailbox(json_encode($param));
		echo json_encode($returnInfo);
	}
	
	//输入完邮箱后验证此邮箱是否存在
	public function do_verifyMailboxS(){
		global $user;
		$id = $this->getUser()->getId();
		$emails = $_POST['emails'];
		$param['EMAIL'] = $emails;
		$param['companyId'] = $user->bigOrgId;
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$verifyMailboxS=$Proxy->verifyMailboxS(json_encode($param));
		echo json_encode($verifyMailboxS);
	}
	
	//渲染导入页面
	public function importStep1()	{
		$userid = $this->getUser()->getId();
		return $this->renderTemplate(array('userId'=>$userid));
	}
	//校验并上传文件
	public function validateAndParseExcel()	{
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data=$proxy->validateAndParseExcel();
		echo $data;
	}
	//渲染页面
	public function importStep2()	{
		return $this->renderTemplate();
	}
	//获取上传文件的结构信息，展示页面
	public function initImportStep2ByPage(){
		global $user;
		$userid = $this->getUser()->getId();
		$para['nowPageStep2'] = $_POST['nowPageStep2'];
		$para['userid'] = $userid;
		$para['ip'] = $this->getClientIp();
		$para['companyId'] = $user->bigOrgId;
		$para['ipandport'] = $_SERVER['HTTP_HOST'];
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data=$proxy->initImportStep2ByPage(json_encode($para));
		$rows = json_decode(json_encode($data),true);
		$startNo = isset($rows['startNo'])?$rows['startNo']:0;
		$endNo = isset($rows['endNo'])?$rows['endNo']:0;
		$total = isset($rows['total'])?$rows['total']:0;
		$isImport = isset($rows['isImport'])?$rows['isImport']:"false";
		$pageCount = isset($rows['pageCount'])?$rows['pageCount']:100;
		$column = $rows['headerList'];
		$jsonData = array('startNo'=>$startNo,'endNo'=>$endNo,'total'=>$total,'pageCount'=>$pageCount,'isImport'=>$isImport,'lists'=>array());//创建一个数组
		foreach ( $rows ['list'] as $row ) {
			$entry = array ('cell' => array ());
			$entry['startNo'] = $startNo;
			for($j = 0; $j < count ( $column ); $j ++) {
				$nameUse = $column[$j]['name'];
				if (array_key_exists ($nameUse, $row)) {
					if($nameUse == '用户姓名'){
						$entry['FULLNAME'] = $row [$nameUse];
					}else if($nameUse == '邮箱'){
						$entry['EMAIL'] = $row [$nameUse];
					}else if($nameUse == '手机'){
						$entry['MOBILEPHONE'] = $row [$nameUse];
					}else if($nameUse == '公司电话'){
						$entry['TELEOHONE'] = $row [$nameUse];
					}else if($nameUse == '传真'){
						$entry['FAX'] = $row [$nameUse];
					}else if($nameUse == '职位'){
						$entry['POSITION'] = $row [$nameUse];
					} 
				}
			}
			$jsonData ['lists'] [] = $entry;
		}
		echo json_encode($jsonData);
	}
	//真正导入
	public function realImport(){
		$userid = $this->getUser()->getId();
		$para['list'] = $_POST['list'];
		$para['ipandport'] = $_SERVER['HTTP_HOST'];
		$para['userid'] = $userid;
		$para['ip'] = $this->getClientIp();
		$para['username'] = $this->getUser()->getId();
		$para['fromfullname'] = $_POST['fromfullname'];
		$para['companyid'] = $_POST['companyid'];  
		$para['nowPageStep2'] = $_POST['nowPageStep2'];  
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data=$proxy->realImport(json_encode($para));
		echo json_encode($data);
	}
	//导入页的关闭，清缓存
	public function closeImport(){
		$userid = $this->getUser()->getId();
		$para['userid'] = $userid;
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data=$proxy->closeImport(json_encode($para));
		echo json_encode($data);
	}
	
public function uploadImage1(){
		global $user;
		$maxsize=2097152; //2M限制
		$id=$this->getUser()->getId();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$last = explode(".", $_FILES["myfile"]["name"]);
		$hz = $last[count($last)-1];
		$randname=date("Y").date("m").date("d").date("H").date("i").date("s").rand(100, 999);
		$success['success'] = null;
		if($_FILES['myfile']['name'] != '') {
			if($_FILES['myfile']['error'] > 0) {
				$success['success'] = false;
				$success['msg'] = '上传出错!';
			}else {
				if(($_FILES['myfile']['type'] == 'image/gif' or $_FILES['myfile']['type'] == 'image/jpg' or $_FILES['myfile']['type'] == 'image/jpeg' or $_FILES['myfile']['type'] == 'image/pjpeg' or $_FILES['myfile']['type'] == 'image/gif' or $_FILES['myfile']['type'] == 'image/png' or $_FILES['myfile']['type'] == 'image/bmp')  && $_FILES['myfile']['size'] < $maxsize) {
					if(move_uploaded_file($_FILES['myfile']['tmp_name'] , "files/headimage/" . $randname.".".$hz)){
						if($_POST['oldimage']!=$user->PORTRAIT){
							if(file_exists($_POST['oldimage'])){
								unlink($_POST['oldimage']);
							}
						}
						$success['success'] = true;
						$success['touxiang'] = "files/headimage/".$randname.".".$hz;
						$arr = getimagesize($success['touxiang']);
						$success['realW'] = $arr[0];
						$success['realH'] = $arr[1];
						$user->tempuserportrait = $success['touxiang'] ;
					} else {
						$success['success'] = false;
						$success['msg'] = '图片上传失败，请联系管理员，谢谢!';
					}
				}else{
					$success['success'] = false;
					$success['msg'] = '图片超出大小或类型错误!';
				}
				
				
			}
		}else {
			$success['success'] = false;
			$success['msg'] = '请上传文件!';
		}
		
 		echo json_encode($success) ;
	}
	
	/**
	 * BMP 创建函数
	 * @author simon
	 * @param string $filename path of bmp file
	 * @example who use,who knows
	 * @return resource of GD
	 */
	function imagecreatefrombmp( $filename ){
		if ( !$f1 = fopen( $filename, "rb" ) )
			return FALSE;
			
		$FILE = unpack( "vfile_type/Vfile_size/Vreserved/Vbitmap_offset", fread( $f1, 14 ) );
		if ( $FILE['file_type'] != 19778 )
			return FALSE;
			
		$BMP = unpack( 'Vheader_size/Vwidth/Vheight/vplanes/vbits_per_pixel' . '/Vcompression/Vsize_bitmap/Vhoriz_resolution' . '/Vvert_resolution/Vcolors_used/Vcolors_important', fread( $f1, 40 ) );
		$BMP['colors'] = pow( 2, $BMP['bits_per_pixel'] );
		if ( $BMP['size_bitmap'] == 0 )
			$BMP['size_bitmap'] = $FILE['file_size'] - $FILE['bitmap_offset'];
		$BMP['bytes_per_pixel'] = $BMP['bits_per_pixel'] / 8;
		$BMP['bytes_per_pixel2'] = ceil( $BMP['bytes_per_pixel'] );
		$BMP['decal'] = ($BMP['width'] * $BMP['bytes_per_pixel'] / 4);
		$BMP['decal'] -= floor( $BMP['width'] * $BMP['bytes_per_pixel'] / 4 );
		$BMP['decal'] = 4 - (4 * $BMP['decal']);
		if ( $BMP['decal'] == 4 )
			$BMP['decal'] = 0;
			
		$PALETTE = array();
		if ( $BMP['colors'] < 16777216 ){
			$PALETTE = unpack( 'V' . $BMP['colors'], fread( $f1, $BMP['colors'] * 4 ) );
		}
			
		$IMG = fread( $f1, $BMP['size_bitmap'] );
		$VIDE = chr( 0 );
			
		$res = imagecreatetruecolor( $BMP['width'], $BMP['height'] );
		$P = 0;
		$Y = $BMP['height'] - 1;
		while( $Y >= 0 ){
			$X = 0;
			while( $X < $BMP['width'] ){
				if ( $BMP['bits_per_pixel'] == 32 ){
					$COLOR = unpack( "V", substr( $IMG, $P, 3 ) );
					$B = ord(substr($IMG, $P,1));
					$G = ord(substr($IMG, $P+1,1));
					$R = ord(substr($IMG, $P+2,1));
					$color = imagecolorexact( $res, $R, $G, $B );
					if ( $color == -1 )
						$color = imagecolorallocate( $res, $R, $G, $B );
					$COLOR[0] = $R*256*256+$G*256+$B;
					$COLOR[1] = $color;
				}elseif ( $BMP['bits_per_pixel'] == 24 )
				$COLOR = unpack( "V", substr( $IMG, $P, 3 ) . $VIDE );
				elseif ( $BMP['bits_per_pixel'] == 16 ){
					$COLOR = unpack( "n", substr( $IMG, $P, 2 ) );
					$COLOR[1] = $PALETTE[$COLOR[1] + 1];
				}elseif ( $BMP['bits_per_pixel'] == 8 ){
					$COLOR = unpack( "n", $VIDE . substr( $IMG, $P, 1 ) );
					$COLOR[1] = $PALETTE[$COLOR[1] + 1];
				}elseif ( $BMP['bits_per_pixel'] == 4 ){
					$COLOR = unpack( "n", $VIDE . substr( $IMG, floor( $P ), 1 ) );
					if ( ($P * 2) % 2 == 0 )
						$COLOR[1] = ($COLOR[1] >> 4);
					else
						$COLOR[1] = ($COLOR[1] & 0x0F);
					$COLOR[1] = $PALETTE[$COLOR[1] + 1];
				}elseif ( $BMP['bits_per_pixel'] == 1 ){
					$COLOR = unpack( "n", $VIDE . substr( $IMG, floor( $P ), 1 ) );
					if ( ($P * 8) % 8 == 0 )
						$COLOR[1] = $COLOR[1] >> 7;
					elseif ( ($P * 8) % 8 == 1 )
					$COLOR[1] = ($COLOR[1] & 0x40) >> 6;
					elseif ( ($P * 8) % 8 == 2 )
					$COLOR[1] = ($COLOR[1] & 0x20) >> 5;
					elseif ( ($P * 8) % 8 == 3 )
					$COLOR[1] = ($COLOR[1] & 0x10) >> 4;
					elseif ( ($P * 8) % 8 == 4 )
					$COLOR[1] = ($COLOR[1] & 0x8) >> 3;
					elseif ( ($P * 8) % 8 == 5 )
					$COLOR[1] = ($COLOR[1] & 0x4) >> 2;
					elseif ( ($P * 8) % 8 == 6 )
					$COLOR[1] = ($COLOR[1] & 0x2) >> 1;
					elseif ( ($P * 8) % 8 == 7 )
					$COLOR[1] = ($COLOR[1] & 0x1);
					$COLOR[1] = $PALETTE[$COLOR[1] + 1];
				}else
					return FALSE;
				imagesetpixel( $res, $X, $Y, $COLOR[1] );
				$X++;
				$P += $BMP['bytes_per_pixel'];
			}
			$Y--;
			$P += $BMP['decal'];
		}
		fclose( $f1 );
			
		return $res;
	}
	
	/**
	 * 创建bmp格式图片
	 *
	 * @author: legend(legendsky@hotmail.com)
	 * @link: http://www.ugia.cn/?p=96
	 * @description: create Bitmap-File with GD library
	 * @version: 0.1
	 *
	 * @param resource $im          图像资源
	 * @param string   $filename    如果要另存为文件，请指定文件名，为空则直接在浏览器输出
	 * @param integer  $bit         图像质量(1、4、8、16、24、32位)
	 * @param integer  $compression 压缩方式，0为不压缩，1使用RLE8压缩算法进行压缩
	 *
	 * @return integer
	 */
	function imagebmp(&$im, $filename = '', $bit = 8, $compression = 0)
	{
		if (!in_array($bit, array(1, 4, 8, 16, 24, 32)))
		{
			$bit = 8;
		}
		else if ($bit == 32) // todo:32 bit
		{
			$bit = 24;
		}
	
		$bits = pow(2, $bit);
	
		// 调整调色板
		imagetruecolortopalette($im, true, $bits);
		$width  = imagesx($im);
		$height = imagesy($im);
		$colors_num = imagecolorstotal($im);
	
		if ($bit <= 8)
		{
			// 颜色索引
			$rgb_quad = '';
			for ($i = 0; $i < $colors_num; $i ++)
			{
			$colors = imagecolorsforindex($im, $i);
			$rgb_quad .= chr($colors['blue']) . chr($colors['green']) . chr($colors['red']) . "\0";
			}
	
					// 位图数据
					$bmp_data = '';
	
			// 非压缩
			if ($compression == 0 || $bit < 8)
			{
			if (!in_array($bit, array(1, 4, 8)))
			{
			$bit = 8;
		}
	
		$compression = 0;
	
				// 每行字节数必须为4的倍数，补齐。
				$extra = '';
				$padding = 4 - ceil($width / (8 / $bit)) % 4;
				if ($padding % 4 != 0)
				{
				$extra = str_repeat("\0", $padding);
			}
	
			for ($j = $height - 1; $j >= 0; $j --)
				{
				$i = 0;
				while ($i < $width)
				{
				$bin = 0;
				$limit = $width - $i < 8 / $bit ? (8 / $bit - $width + $i) * $bit : 0;
	
				for ($k = 8 - $bit; $k >= $limit; $k -= $bit)
				{
				$index = imagecolorat($im, $i, $j);
				$bin |= $index << $k;
				$i ++;
				}
	
					$bmp_data .= chr($bin);
				}
	
				$bmp_data .= $extra;
					}
					}
					// RLE8 压缩
					else if ($compression == 1 && $bit == 8)
					{
					for ($j = $height - 1; $j >= 0; $j --)
					{
					$last_index = "\0";
					$same_num   = 0;
					for ($i = 0; $i <= $width; $i ++)
						{
							$index = imagecolorat($im, $i, $j);
							if ($index !== $last_index || $same_num > 255)
							{
							if ($same_num != 0)
							{
							$bmp_data .= chr($same_num) . chr($last_index);
					}
	
					$last_index = $index;
					$same_num = 1;
					}
					else
					{
					$same_num ++;
				}
				}
	
				$bmp_data .= "\0\0";
			}
	
			$bmp_data .= "\0\1";
			}
	
			$size_quad = strlen($rgb_quad);
			$size_data = strlen($bmp_data);
			}
			else
			{
				// 每行字节数必须为4的倍数，补齐。
				$extra = '';
				$padding = 4 - ($width * ($bit / 8)) % 4;
				if ($padding % 4 != 0)
				{
				$extra = str_repeat("\0", $padding);
				}
	
				// 位图数据
				$bmp_data = '';
	
				for ($j = $height - 1; $j >= 0; $j --)
				{
				for ($i = 0; $i < $width; $i ++)
				{
					$index  = imagecolorat($im, $i, $j);
					$colors = imagecolorsforindex($im, $index);
	
						if ($bit == 16)
						{
						$bin = 0 << $bit;
	
						$bin |= ($colors['red'] >> 3) << 10;
						$bin |= ($colors['green'] >> 3) << 5;
						$bin |= $colors['blue'] >> 3;
	
						$bmp_data .= pack("v", $bin);
				}
				else
				{
				$bmp_data .= pack("c*", $colors['blue'], $colors['green'], $colors['red']);
				}
	
				// todo: 32bit;
				}
	
				$bmp_data .= $extra;
			}
	
			$size_quad = 0;
			$size_data = strlen($bmp_data);
			$colors_num = 0;
			}
	
			// 位图文件头
			$file_header = "BM" . pack("V3", 54 + $size_quad + $size_data, 0, 54 + $size_quad);
	
				// 位图信息头
				$info_header = pack("V3v2V*", 0x28, $width, $height, 1, $bit, $compression, $size_data, 0, 0, $colors_num, 0);
	
				// 写入文件
					if ($filename != '')
					{
						$fp = fopen("test.bmp", "wb");
	
					fwrite($fp, $file_header);
					fwrite($fp, $info_header);
					fwrite($fp, $rgb_quad);
					fwrite($fp, $bmp_data);
					fclose($fp);
	
					return 1;
					}
	
						// 浏览器输出
					header("Content-Type: image/bmp");
					echo $file_header . $info_header;
					echo $rgb_quad;
					echo $bmp_data;
	
					return 1;
	}
	
	public function uploadImageOk(){
		global $user;
		$targ_w = $targ_h = 100;
		$src = $_POST['image'];
		$test = explode('.', $src);
		$test = end($test);
		$test = strtolower($test);
		if($test == "jpeg"||$test == "jpg"){
			$img_r = imagecreatefromjpeg($src);
		} else if($test == "png"){
			$img_r = imagecreatefrompng($src);
		} else if($test == "bmp"){
			$img_r = $this->imagecreatefrombmp($src);
		}
		$dst_r = ImageCreateTrueColor( $targ_w, $targ_h );
		imagecopyresampled($dst_r,$img_r,0,0,$_POST['x'],$_POST['y'],
		$targ_w,$targ_h,$_POST['w'],$_POST['h']);
		$randname=date("Y").date("m").date("d").date("H").date("i").date("s").rand(100, 999);
		$newpath = "files/headimage/" . $randname."_h.jpeg" ;
// 		if($test == "jpeg"||$test == "jpg"){
			$ret = imagejpeg($dst_r,$newpath,90) ;
// 		} else if($test == "png"){
// 			$ret = imagepng($dst_r,$newpath,9) ;
// 		} else if($test == "bmp"){
// 			$ret = imagepng($dst_r,$newpath,5) ;
// 		}
		if($ret){
			$androidImg = "files/headimage/".md5($this->getUser()->getId()).".jpeg";
			imagejpeg($dst_r,$androidImg,90);
			$success['touxiang'] = $newpath ;
			if(file_exists($user->PORTRAIT)){
				unlink($user->PORTRAIT);
				$user->tempuserportrait = "";
			}
			if(file_exists($src)){
				unlink($src);
			}
			//图片路径保存到数据库
			$data ['username'] = $this->getUser()->getId();
			$data ['path'] = $newpath;
			$data ['companyid'] = $user->bigOrgId;
			$data['ip'] = $this->getClientIp();
			$Proxy=$this->exec('getProxy','onlinefile_userinfows');
			$return=$Proxy->saveHeadImage(json_encode($data));
			if($return->success){
				$success['success'] = true;
				$success['msg'] = '修改头像成功!';
				$success['touxiang'] = $newpath;
				$user->PORTRAIT = $newpath ;/** 用户头像**/
			}else{
				$success['success'] = false;
				$success['msg'] = '修改头像失败!';
			}
		}
		echo json_encode($success) ;
	}
	
	//查看用户名和邮箱是否存在存在则发送邮件
	public function forgetpassword(){
		$data['username'] = $_POST['username'];
		$data['email'] = $_POST['email'];
		$data['ipandport'] = $_SERVER['HTTP_HOST'];
		$data['ip'] = $this->getClientIp();
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$data=$proxy->forgetpassword(json_encode($data));
	}
	
	public function toforgetpassword1(){
		$data['username'] = $_POST['username'];
		$data['email'] = $_POST['email'];
		$data['id'] = $_POST['id'];
		$data['companyid'] = $_POST['companyid'];
		$data['ip'] = $this->getClientIp();
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$proxy->toforgetpassword(json_encode($data));
		echo  json_encode($return);
	}
	
	public function fromforgetpasswordtochange(){
		$data['username'] = $_POST['username'];
		$data['email'] = $_POST['email'];
		$data['id'] = $_POST['id'];
		$data['companyid'] = $_POST['companyid'];
		$data['password'] = $_POST['password'];
		$data['ip'] = $this->getClientIp();
		$proxy=$this->exec('getProxy','onlinefile_userinfows');
		$return=$proxy->fromforgetpasswordtochange(json_encode($data));
		echo  json_encode($return);
	}
	
	
	
	/**
	 * 
	 * $im=imagecreatefromjpeg("files/headimage/20150418133902631.jpg");//参数是图片的存方路径
		$maxwidth="45";//设置图片的最大宽度
		$maxheight="45";//设置图片的最大高度
		$name="123";//图片的名称，随便取吧
		$filetype=".jpg";//图片类型
		self::resizeImage($im, $maxwidth, $maxheight, $name, $filetype);
	 */
	public function resizeImage($im,$maxwidth,$maxheight,$name,$filetype){
		
		$pic_width = imagesx($im);
		$pic_height = imagesy($im);
		
		if(($maxwidth && $pic_width > $maxwidth) || ($maxheight && $pic_height > $maxheight)){
			if($maxwidth && $pic_width>$maxwidth){
				$widthratio = $maxwidth/$pic_width;
				$resizewidth_tag = true;
			}
			if($maxheight && $pic_height>$maxheight){
				$heightratio = $maxheight/$pic_height;
				$resizeheight_tag = true;
			}
			if($resizewidth_tag && $resizeheight_tag){
				if($widthratio<$heightratio)
					$ratio = $widthratio;
				else
					$ratio = $heightratio;
			}
			if($resizewidth_tag && !$resizeheight_tag){
				$ratio = $widthratio;
			}
			if($resizeheight_tag && !$resizewidth_tag){
				$ratio = $heightratio;
			}
			$newwidth = $pic_width * $ratio;
			$newheight = $pic_height * $ratio;
			
			if(function_exists("imagecopyresampled")){
				$newim = imagecreatetruecolor($newwidth,$newheight);//PHP系统函数
				imagecopyresampled($newim,$im,0,0,0,0,$newwidth,$newheight,$pic_width,$pic_height);//PHP系统函数
			}else {
				$newim = imagecreate($newwidth,$newheight);
				imagecopyresized($newim,$im,0,0,0,0,$newwidth,$newheight,$pic_width,$pic_height);
			}
			$name = $name.$filetype;
			imagejpeg($newim,$name);
			imagedestroy($newim);
		}else {
			$name = $name.$filetype;
			imagejpeg($im,$name);
		}
	}
	
	public function invitationCampany(){
		$param['recommend_url'] = $_POST['recommend_url'];
		$param['subject'] = $_POST['subject'];
		$param['email'] = $_POST['email'];
		$param['emailcontent'] = $_POST['emailcontent'];
		$param['ip'] = $this->getClientIp();
		global $user;
		$param['username'] = $this->getUser()->getId();
		$param['companyid'] = $user->bigOrgId;
		$param['ipandport'] = $_SERVER['HTTP_HOST'];
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->invitationCampany(json_encode($param));
		echo json_encode($returnInfo);
	}
	
	public function checkPasswordIsRight(){
		global $user;
		$param['companyId'] = $user->bigOrgId;
		$param['username'] = $this->getUser()->getId();
		$param['userid'] = $_POST['userid'];
		$param['password'] = $_POST['password'];
		$param['ip'] = $this->getClientIp();
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->checkPasswordIsRight(json_encode($param));
		echo json_encode($returnInfo);
		
	}

	public function getLoginLog(){
		$userid = $_POST['userid'];
	    $start = isset($_POST['start']) ? $_POST['start']: 1;
	    $limit = isset($_POST['limit']) ? $_POST['limit']:10;
		$param = array(
			'userid'=>$userid,
			'start'=>$start,
			'limit'=>$limit
		);
		$Proxy=$this->exec('getProxy','onlinefile_userinfows');
		$returnInfo=$Proxy->getLoginLog(json_encode($param));
		echo json_encode($returnInfo);
	}
}
?>