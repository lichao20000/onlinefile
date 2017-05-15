<?php
/**
 * @author guolanrui 20150309
 * 
 * 文档分类界面
 *
 */
class ESDocumentClassAction extends ESActionBase
{
	public function index()
	{
		return $this->renderTemplate();
	}
	
	public function getCateList(){
		$companyId = isset ( $_GET ['companyId'] ) ? $_GET ['companyId'] : '1';
		$keyWord = isset ( $_GET ['keyWord'] ) ? $_GET ['keyWord'] : '';
		$page = isset ( $_POST ['page'] ) ? $_POST ['page'] : 1;
		$rp = isset ( $_POST ['rp'] ) ? $_POST ['rp'] : 20;
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$data['startNo'] = ($page - 1) * $rp;
		$data['limit'] = $rp;
		$data['companyId'] = $companyId;
		$data['userIp'] = $this->getClientIp();
		$data['userId'] = $this->getUser()->getId();
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->getCateList ( $postData );
		$rows = $returnDate->data;
		$total = $returnDate->count;//这是条目总数，需要从库里查询，这里现在下边写死
// 		$total = 10;
		$jsonData = array('page'=>$page,'total'=>$total,'rows'=>array());
		$start = ($page - 1) * $rp + 1;
		foreach ($rows as $row){
			$entry = array("id"=>$row->id,
					"cell"=>array(
							"rownum"=>$start,
							"id"=>"<input type='checkbox' name='id'>",
							"editbtn"=>"<span class='editbtn'>&nbsp;</span>",
							"companyId"=>$row->companyId,
							"className"=>$row->className
					),
			);
			$jsonData['rows'][] = $entry;
			$start++;
		}
		
		echo json_encode($jsonData);
	}
	public function edit()
	{
		$cateId = isset ( $_POST ['cateId'] ) ? $_POST ['cateId'] : '';
		$className = isset ( $_POST ['className'] ) ? $_POST ['className'] : '';
		return $this->renderTemplate(array('id'=>$cateId,'className'=>$className));
	}
	public function add()
	{
		return $this->renderTemplate();
	}
	
	
	public function addCate()
	{
		$companyId = isset ( $_POST ['companyId'] ) ? $_POST ['companyId'] : '1';
		$className = isset ( $_POST ['className'] ) ? $_POST ['className'] : '';
		
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		
		$data['companyId'] = $companyId;
		$data['className'] = $className;
		$data['userIp'] = $this->getClientIp();
		$data['userId'] = $this->getUser()->getId();
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->addCate ( $postData );
		
	}
	
	public function delCate()
	{
		
		$id = isset ( $_POST ['id'] ) ? $_POST ['id'] : '';
		
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		
		$data['id'] = $id;
		$data['userIp'] = $this->getClientIp();
		$data['userId'] = $this->getUser()->getId();
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->delCate ( $postData );
		
	}
	
	public function editCate()
	{
		parse_str ( $_POST ['data'], $out );
// 		$id = isset ( $_POST ['id'] ) ? $_POST ['id'] : '';
// 		$className = isset ( $_POST ['className'] ) ? $_POST ['className'] : '';
		
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		
// 		$data['id'] = $id;
// 		$data['className'] = $className;
		$out['userIp'] = $this->getClientIp();
		$out['userId'] = $this->getUser()->getId();
		$postData = json_encode ( $out );
		$returnDate = $documentCateProxy->editCate ( $postData );
		echo $returnDate->success;
		
	}
	
	
	//liumingchao
	/* 分类中所有用户*/
	public function getUserListClass(){
		$data['username'] = $this->getUser()->getId();
		$data ['className'] = $_POST['className'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->getUserListClass ( $postData );
		
	}
	//添加分类
	public function addClassByName(){
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data ['className'] = $_POST['className'];
		$data ['companyId'] = $_POST['companyId'];
		$data ['classId'] = $_POST['classId'];
		$data ['idSeq'] = $_POST['idSeq'];
		$data ['userId'] = $_POST['userId']; //表id
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->addClassByName ( $postData );
		echo json_encode($returnDate);
	}
	//删除分类
	public function deleteFileClassById(){
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data ['classId'] = $_POST['classId'];
		$data ['companyId'] = $_POST['companyId'];
		$data ['userId'] = $_POST['userId'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->deleteFileClassById ( $postData );
		echo json_encode($returnDate);
	}
	//删除分类
	public function deleteClassById(){
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data ['classId'] = $_POST['classId'];
		$data['folderId'] = $_POST['folderId'];
		$data ['companyId'] = $_POST['companyId'];
		$data ['userId'] = $_POST['userId'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->deleteClassById ( $postData );
		echo json_encode($returnDate);
	}
	//改名
	public function reClassNameById(){
		$data['username'] = $this->getUser()->getId();
		$data['ip'] = $this->getClientIp();
		$data['itemId'] = $_POST['itemId'];
		$data['className'] = $_POST['className'];
		$data['oldCLassName'] = $_POST['oldCLassName'];
		$data['fatherClassId'] = $_POST['fatherClassId'];
		$data ['companyId'] = $_POST['companyId'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->reClassNameById ( $postData );
		echo json_encode($returnDate);
	}
	//改名  无用
	public function choiseClass(){
		$data['username'] = $this->getUser()->getId();
		$data ['companyId'] = $_POST['companyId'];
// 		$data ['className'] = $_POST['className'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->gerClassList ( $postData );
		echo json_encode($returnDate);
		return $this->renderTemplate(array('classlist'=>json_encode($returnDate)));
	}
	//所有分类  无用
	public function gerClassList(){
		$data['username'] = $this->getUser()->getId();
		$data ['companyId'] = $_POST['companyId'];
// 		$data ['className'] = $_POST['className'];
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->gerClassList ( $postData );
		echo json_encode($returnDate);
	}
	public function editUser(){
		$groupid = isset($_POST['groupid']) ? $_POST['groupid'] : '';
		$groupname = isset($_POST['groupName']) ? $_POST['groupName'] : '';
		$flag = isset($_POST['flag']) ? $_POST['flag'] : '';
		global $user;
		$data = array('groupid'=>$groupid,'groupname'=>$groupname, 'flag'=>$flag, 'userId'=>$user->dataId);
// 		return $this->renderTemplate($data);
		echo json_encode($data);
	}
	//修改分组字段
	public function editClassId(){
		$data['flag'] = isset($_POST['flag']) ? $_POST['flag'] : '';
		$data['id'] = isset($_POST['id']) ? $_POST['id'] : '';
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->editClassId ( $postData );
		echo json_encode($returnDate);
	}
	//创建分组及修改字段
// 	public function addClassByNameAndCreateGroup(){
// 		$data['className'] = isset($_POST['className']) ? $_POST['className'] : '';
// 		$data['ip'] = $this->getClientIp();
// 		$data['companyId'] = isset($_POST['companyId']) ? $_POST['companyId'] : '';
// 		$data['classId'] = isset($_POST['classId']) ? $_POST['classId'] : '';
// 		$data['username'] = isset($_POST['username']) ? $_POST['username'] : '';
// 		$data ['userId'] =isset($_POST['userId']) ? $_POST['userId'] : ''; //表id
// 		$data['groupuserids'] = isset($_POST['groupuserids']) ? $_POST['groupuserids'] : '';
// 		$data['manageruserid'] = isset($_POST['manageruserid']) ? $_POST['manageruserid'] : '';
// 		$data['groupname'] = isset($_POST['groupname']) ? $_POST['groupname'] : '';
// 		$data['groupremark'] = isset($_POST['groupremark']) ? $_POST['groupremark'] : '';
// 		$data['ip'] = $this->getClientIp();
// 		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
// 		$postData = json_encode ( $data );
// 		$returnDate = $documentCateProxy->addClassByNameAndCreateGroup ( $postData );
// 		echo json_encode($returnDate);
// 	}

	//创建分组及修改字段
	public function addClassByNameAndCreateGroup(){
		$data['className'] = isset($_POST['groupname']) ? $_POST['groupname'] : '';
		$data['ip'] = $this->getClientIp();
		$data['companyId'] = isset($_POST['companyId']) ? $_POST['companyId'] : '';
		$data['classId'] = "1";
		$data['username'] = $this->getUser()->getId();
		$data ['userId'] =isset($_POST['userId']) ? $_POST['userId'] : ''; //表id
		$data['groupuserids'] = isset($_POST['groupuserids']) ? $_POST['groupuserids'] : '';
		$data['manageruserid'] = isset($_POST['manageruserid']) ? $_POST['manageruserid'] : '';
		$data['groupname'] = isset($_POST['groupname']) ? $_POST['groupname'] : '';
		$data['groupremark'] = isset($_POST['groupremark']) ? $_POST['groupremark'] : '';
		$data['ip'] = $this->getClientIp();
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->addClassByNameAndCreateGroup ( $postData );
		echo json_encode($returnDate);
	}
	
	
	public function editClassInfo(){
		$data['groupid'] = isset($_POST['groupid']) ? $_POST['groupid'] : '';
		$data['groupName'] = isset($_POST['groupName']) ? $_POST['groupName'] : '';
		$data['flag']= isset($_POST['flag']) ? $_POST['flag'] : '';
		$data['userId'] = isset($_POST['userId']) ? $_POST['userId'] : '';
		$data['classId'] = isset($_POST['classId']) ? $_POST['classId'] : '';
		$data['ip'] = $this->getClientIp();
		$data['companyId'] = $this->getUser()->getBigOrgId();
		$data['mark'] = isset($_POST['mark']) ? $_POST['mark'] : '';
		$data['username'] = $this->getUser()->getId();
		$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
		$postData = json_encode ( $data );
		$returnDate = $documentCateProxy->editClassInfo ( $postData );
		echo json_encode($returnDate);
	}
	
	/*------------------------20150810 改为post请求-------------------------------*/
		public function getClassInfo(){
			$data['groupid'] = isset($_POST['groupid']) ? $_POST['groupid'] : '';
			$data['groupname'] = isset($_POST['groupname']) ? $_POST['groupname'] : '';
			$data['flag']= isset($_POST['flag']) ? $_POST['flag'] : '';
			$data['userId'] = isset($_POST['userId']) ? $_POST['userId'] : '';
			$data['ip'] = $this->getClientIp();
			$data['companyId'] = $this->getUser()->getBigOrgId();
			$documentCateProxy = $this->exec ( "getProxy", 'onlinefile_documentclassws' );
			$postData = json_encode ( $data );
			$returnDate = $documentCateProxy->getClassInfo ( $postData );
			echo json_encode($returnDate);
		}
}

?>