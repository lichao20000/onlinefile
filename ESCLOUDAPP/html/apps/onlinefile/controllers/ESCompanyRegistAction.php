<?php
/**
 * @author guolanrui 20150225
 * 
 * 企业注册的页面
 *
 */
class ESCompanyRegistAction extends ESActionBase
{
	
	public function index()
	{
		return $this->renderTemplate();
	}
	
	/**获取公司信息**/
	public function campanyInfo(){
		global $user;
	//	$data['username'] = $this->getUser()->getId();
	//	$data['ip'] = $this->getClientIp();
 		$data=array();
 		$data['companyId'] = $_POST['companyId'];
  		$Proxy=$this->exec('getProxy','onlinefile_campanyinfows');
 		$returnInfo=$Proxy->getCampanyInfoById(json_encode($data));
 		return $this->renderTemplate(array('campanyInfo'=>$returnInfo, 'campanyLogo'=>$user->IMGURL)); 
	}
	
	/**修改公司信息**/
	public function editCampanyInfo(){
		$ip=$this->getClientIp();
		global $user ;
		$dataname=array();
		$data['companyid'] = $_POST['companyid'];
		$data['companyname'] = $_POST['companyname'];
		$data['addresses'] = $_POST['addresses'];
		$data['companyphone'] = $_POST['companyphone'];
		$data['postcode'] = $_POST['postcode'];
		$data['companyfax'] = $_POST['companyfax'];
		$Proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$returnInfo=$Proxy->editCampanyInfo(json_encode($data));
		$datas['success']=$returnInfo->success;
		$datas['companyname'] = $returnInfo->success?$data['companyname']:$user->COMPANYNAME;
		$user->COMPANYNAME = $returnInfo->success?$data['companyname']:$user->COMPANYNAME;
//		$user->COMPANYNAME = $data['companyname'];
		echo json_encode($datas);
	}
	
	/**上传公司logo**/
	public function uploadImage1(){
		global $user;
	 	//$username=$this->getUser()->getId();
		$companyid=$user->bigOrgId; 
		$data = array();
		$maxsize=2097152; //2M限制
		$Proxy=$this->exec('getProxy','onlinefile_campanyinfows');
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
					if(move_uploaded_file($_FILES['myfile']['tmp_name'] , "files/logoimage/" . $randname.".".$hz)){
						//获取公司id
						$parems['companyid'] = $companyid;

						//从缓存中查询当前用户是否有头像 代替上面方法  提高效率
						if(file_exists($user->IMGURL)){
							unlink($user->IMGURL);
						} 
						//图片路径保存到数据库
						//$data ['username'] = $username;
						$data ['companyid'] = $companyid;
						$data ['path'] = "files/logoimage/".$randname.".".$hz;
						//$data['ip'] = $this->getClientIp();
						$return=$Proxy->saveHeadImage(json_encode($data));
						//替换原来的路径
						$user->IMGURL = $data ['path']; 
						if($return->success){
							$success['success'] = true;
							$success['msg'] = '上传成功!';
							$success['logosrc'] = "files/logoimage/".$randname.".".$hz;
						}else{
							$success['success'] = false;
							$success['msg'] = '同步失败!';
							$success['imagepath'] =$imagepath;
						}
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
	
	/** 注册新企业(团队) **/
	function registerNewCampany(){
		global $user;
		$data['companyName'] = $_POST['companyName'];
		$data['email'] = $_POST['email'];
		$data['userid'] = $_POST['userid'];
		$Proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$returnInfo=$Proxy->registerNewCampany(json_encode($data));
		$user->ownCompanyId = isset($returnInfo->ownCompanyId)?$returnInfo->ownCompanyId:"-1"; /** 公司名  **/
		echo json_encode($returnInfo) ;
	}
	
	function getCompanyReferrer(){
	    $username = $_POST['username'];
	    $start = isset($_POST['start']) ? $_POST['start']: 1;
	    $limit = isset($_POST['limit']) ? $_POST['limit']:10;
		$param = array(
			'username'=>$username,
			'start'=>$start,
			'limit'=>$limit
		);
		$proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$result = $proxy->getCompanyReferrer(json_encode($param));
		echo   json_encode($result);
	}
	/** 20151020 切换新企业 **/
	function switchoverCompany(){
	    $userId = $_POST['userId'];
	    $companyId = $_POST['companyId'];
		$param = array('userId'=>$userId,'companyId'=>$companyId);
		$proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$result = $proxy->switchoverCompany(json_encode($param));
		echo  json_encode($result);
	}
	function cancelCompany(){
		$userId = $_POST['userId'];
		$companyId = $_POST['companyId'];
		$data['userId'] = $userId;
		$data['companyId'] = $companyId;
		$proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$result = $proxy->cancelCompany(json_encode($data));
		echo  json_encode($result);
	}
	
	function getCompanyUserForTransfer(){
		$userId = $_POST['userid'];
		$companyId = $_POST['companyid'];
		$data['userId'] = $userId;
		$data['companyId'] = $companyId;
		$proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$result = $proxy->getCompanyUserForTransfer(json_encode($data));
		echo  json_encode($result);
	}
	function transferCompany(){
		
		$data['userId'] = $_POST['userid'];
		$data['userName'] = $_POST['username'];
		$data['ip'] =$this->getClientIp();;
		$data['companyId'] = $_POST['companyid'];
		$data['companyName'] = $_POST['companyname'];
		$data['touserName'] = $_POST['tousername'];
		$data['touserId'] = $_POST['touserid'];
		$proxy=$this->exec('getProxy','onlinefile_campanyinfows');
		$result = $proxy->transferCompany(json_encode($data));
		if($result->isOk){
			global $user;
			$user->isAdmin ="0";
		}
		echo  json_encode($result);
	}
	
}


?>