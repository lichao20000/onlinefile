<?php
/**
 * 文档中心
 *
 */
class ESDocumentCenterAction extends ESActionBase {
	
	const PROXY_NAME = "onlinefile_filesws";
	
	public function setFileProp() {
		$filename = $_GET['filename'];
		return $this->renderTemplate(array('filename'=>$filename));
	}
	
	/**
	 * 分享一个文件给某个用户（允许其查看和下载文件）
	 */
	public function shareFileToUser() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$toUserId = isset($_POST['toUserId']) ? $_POST['toUserId'] : '';
		$msgid= isset($_POST['msgid']) ? $_POST['msgid'] : '';
		$test= isset($_POST['test']) ? $_POST['test'] : '';
		$button= isset($_POST['button']) ? $_POST['button'] : '';
		$receiver= isset($_POST['receiver']) ? $_POST['receiver'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		//$fromUserName= isset($_POST['fromUserName']) ? $_POST['fromUserName'] : '';
		$toUserFullName= isset($_POST['receiverFullName']) ? $_POST['receiverFullName'] : '';
		$data['fromUserName'] = $this->getUser()->getId();
		$data['fileId'] = $fileId;
		$data['toUserId'] = $toUserId;
		$data['msgid'] = $msgid;
		$data['test'] = $test;
		$data['button'] = $button;
		$data['receiver'] = $receiver;
		$data['companyId'] = $companyId;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$data['toUserFullName'] = $toUserFullName;
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->shareFileToUser($postData);
		echo $result;
	}
	/**
	 * 取消分享一个文件给某个用户（允许其查看和下载文件）
	 */
	public function unShareFileToUser() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$toUserId = isset($_POST['toUserId']) ? $_POST['toUserId'] : '';
		$msgid= isset($_POST['msgid']) ? $_POST['msgid'] : '';
		$test= isset($_POST['test']) ? $_POST['test'] : '';
		$button= isset($_POST['button']) ? $_POST['button'] : '';
		$receiver= isset($_POST['receiver']) ? $_POST['receiver'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['toUserId'] = $toUserId;
		$data['msgid'] = $msgid;
		$data['test'] = $test;
		$data['button'] = $button;
		$data['receiver'] = $receiver;
		$data['companyId'] = $companyId;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$data['fromUserName'] = $this->getUser()->getId();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->unShareFileToUser($postData);
		echo $result;
	}
	
	/**
	 * 分享多个文件给某个用户（允许其查看和下载文件）
	 */
	public function shareFilesToUser() {
		$fileIds = isset($_POST['fileIds']) ? $_POST['fileIds'] : '';
		$toUserName = isset($_POST['toUserName']) ? $_POST['toUserName'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		//$fromUserName= isset($_POST['fromUserName']) ? $_POST['fromUserName'] : '';
		//$data['fromUserName'] = $fromUserName;
		$data['fromUserName'] = $this->getUser()->getId();;
		$data['fileIds'] = $fileIds;
		$data['toUserName'] = $toUserName;
		$data['companyId'] = $companyId;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->shareFilesToUser($postData);
		echo $result;
	}
	
	/**
	 * 分享一个文件给某个用户或联系人群组（允许其查看和下载文件）
	 */
	public function shareFileToGroupOrUser() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$receiver = isset($_POST['receiver']) ? $_POST['receiver'] : '';
		$isGroup = isset($_POST['isGroup']) ? $_POST['isGroup'] : '0';
		//$companyId = $this->getUser()->getBigOrgId();
		$companyId= isset($_POST['companyId']) ? $_POST['companyId'] : '';
		$fromCompanyId= isset($_POST['fromCompanyId']) ? $_POST['fromCompanyId'] : '';
		$accessRight= isset($_POST['accessRight']) ? $_POST['accessRight'] : '';
		$toUserFullName= isset($_POST['receiverFullName']) ? $_POST['receiverFullName'] : '';
		$data['fromUserName'] = $this->getUser()->getId();;
		$data['fileId'] = $fileId;
		$data['receiver'] = $receiver;
		$data['isGroup'] = $isGroup;
		$data['companyId'] = $companyId;
		$data['fromCompanyId'] = $fromCompanyId;
		$data['accessRight'] = $accessRight;
		$data['toUserFullName'] = $toUserFullName;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->shareFileToGroupOrUser($postData);
		echo $result;
	}
	
	/**
	 * 取消分享
	 */
	public function unshareFile() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$classId = isset($_POST['classId']) ? $_POST['classId'] : '';
		$isExist = isset($_POST['isExist']) ? $_POST['isExist'] : '';
		$unshareType = isset($_POST['unshareType']) ? $_POST['unshareType'] : '';
		$newFileName = isset($_POST['newFileName']) ? $_POST['newFileName'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['userId'] = $userId;
		$data['classId'] = $classId;
		$data['companyId'] = $companyId;
		$data['isExist'] = $isExist;
		$data['unshareType'] = $unshareType;
		$data['newFileName'] = $newFileName;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->unshareFile($postData);
		echo $result;
	}
	
	/**
	 * 点赞/取消赞文件
	 */
	public function praiseFile() {
		$companyId = $this->getUser()->getBigOrgId();
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$status = isset($_POST['status']) ? $_POST['status'] : 'false';
		$data['companyId'] = $companyId;
		$data['fileId'] = $fileId;
		$data['userId'] = $userId;
		$data['status'] = $status;
		$data['remoteAddr'] = $this->getClientIp();
		$data['userName'] = $this->getUser()->getId();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->praiseFile($postData);
	}
	
	/**
	 * 收藏/取消收藏文件
	 */
	public function collectFile() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$toUserId = isset($_POST['toUserId']) ? $_POST['toUserId'] : '';
		$status = isset($_POST['status']) ? $_POST['status'] : 'false';
		$data['fileId'] = $fileId;
		$data['toUserId'] = $toUserId;
		$data['status'] = $status;
		$data['userId'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
// 		$returnDate = $proxy->collectFile($postData);
	}
	
	/**
	 * 逻辑删除文件
	 */
	public function deleteFile() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$isShow = isset($_POST['isShow']) ? $_POST['isShow'] : '';
		$companyId = isset($_POST['companyId']) ? $_POST['companyId'] : '';
		$data['fileId'] = $fileId;
		$data['userId'] = $userId;
		$data['isShow'] = $isShow;
		$data['companyId'] = $companyId;
		$data['remoteAddr'] = $this->getClientIp();
		$data['userName'] = $this->getUser()->getId();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$returnDate = $proxy->deleteFile($postData);
		echo $returnDate;
	}
	
	/**
	 * 取消一个文件的分享
	 */
	public function cancelShareFile() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$data['fileId'] = $fileId;
		$data['userId'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
// 		$returnDate = $proxy->cancelShareFile($postData);
	}
	
	/**
	 * 打开文件编辑窗口
	 */
	public function openEditFileWin() {
		$companyId = $this->getUser()->getBigOrgId();
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$proxy=$this->exec('getProxy',self::PROXY_NAME);
		$result=$proxy->getFileOtherProp($companyId,$fileId,$this->getClientIp(),$this->getUser()->getId());
		return $this->renderTemplate(array('html'=>$result), "ESDocumentCenter/edit_fileInfo");
	}
	
	public function editFileOtherProp() {
		$companyId = $this->getUser()->getBigOrgId();
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$KeyLst = isset($_POST['KeyLst']) ? $_POST['KeyLst'] : '';
		$valueLst = isset($_POST['valueLst']) ? $_POST['valueLst'] : '';
		$data['companyId'] =$companyId;
		$data['fileId'] =$fileId;
		$data['KeyLst'] =$KeyLst;
		$data['valueLst'] =$valueLst;
		$data['remoteAddr'] = $this->getClientIp();
		$data['userName'] = $this->getUser()->getId();
		$postData = json_encode($data);
		$proxy=$this->exec('getProxy',self::PROXY_NAME);
		$result=$proxy->editFileOtherProp($postData);
		echo $result;
	}
	
	public function addSubScribeMsgByUserId(){
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$chekedUsers = isset($_POST['chekedUsers']) ? $_POST['chekedUsers'] : '';
		$data['userId'] =$userId;
		$data['chekedUsers'] =$chekedUsers;
		$postData = json_encode($data);
		$proxy=$this->exec('getProxy',self::PROXY_NAME);
		$result=$proxy->addSubScribeMsgByUserId($postData);
		echo json_encode($result);
	}
	
	public function addSubScribeMsgByDocs(){
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$chekedUsers = isset($_POST['chekedUsers']) ? $_POST['chekedUsers'] : '';
		$data['userId'] =$userId;
		$data['chekedUsers'] =$chekedUsers;
		$postData = json_encode($data);
		$proxy=$this->exec('getProxy',self::PROXY_NAME);
		$result=$proxy->addSubScribeMsgByDocs($postData);
		echo json_encode($result);
	}
	
	public function delSubScribeMsgByUserId(){
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$chekedUsers = isset($_POST['chekedUsers']) ? $_POST['chekedUsers'] : '';
		$data['userId'] =$userId;
		$data['chekedUsers'] =$chekedUsers;
		$postData = json_encode($data);
		$proxy=$this->exec('getProxy',self::PROXY_NAME);
		$result=$proxy->delSubScribeMsgByUserId($postData);
		echo json_encode($result);
	}
	
	/**
	 * 文件分享到某个路径下
	 */
	public function shareToFolderPath() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$folderId = isset($_POST['folderId']) ? $_POST['folderId'] : '';
		$folderName = isset($_POST['folderName']) ? $_POST['folderName'] : '';
		$openlevel = isset($_POST['openlevel']) ? $_POST['openlevel'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['folderName'] = $folderName;
		$data['folderId'] = $folderId;
		$data['openlevel'] = $openlevel;
		$data['companyId'] = $companyId;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->shareToFolderPath($postData);
		echo $result;
	}
	/**
	 * wangwenshuo 20151020 复制到 
	 */
	public function copyToFolderPath() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$folderId = isset($_POST['folderId']) ? $_POST['folderId'] : '';
		$folderName = isset($_POST['folderName']) ? $_POST['folderName'] : '';
		$openlevel = isset($_POST['openlevel']) ? $_POST['openlevel'] : '';
		$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['folderName'] = $folderName;
		$data['folderId'] = $folderId;
		$data['openlevel'] = $openlevel;
		$data['companyId'] = $companyId;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->copyToFolderPath($postData);
		echo json_encode($result);
	}
	
	/**
	 * 文件恢复
	 */
	public function fileRestore() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		//$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['companyId'] = $_POST['companyId'];
		$data['trashId'] = $_POST['trashId'];
		$data['userId'] = $_POST['userId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->fileRestore($postData);
		echo $result;
	}
	
	/**
	 * 文件彻底删除
	 */
	public function fileDestroy() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		//$companyId = $this->getUser()->getBigOrgId();
		$data['fileId'] = $fileId;
		$data['companyId'] = $_POST['companyId'];
		$data['userId'] = $_POST['userId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->fileDestroy($postData);
		echo $result;
	}
	
	/**
	 * 清空回收站  wangwenshuo 20150906
	 */
	public function cleanRecycleBin() {
		$companyId = $this->getUser()->getBigOrgId();
		$data['companyId'] = $companyId;
		$data['userId'] = $_POST['userId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->cleanRecycleBin($postData);
		echo $result;
	}
	
	/**
	 * 标记/取消标记为常用分类
	 */
	public function starClass() {
		$classId = isset($_POST['classId']) ? $_POST['classId'] : '';
		$status = isset($_POST['status']) ? $_POST['status'] : 'true';
		$companyId = $this->getUser()->getBigOrgId();
		$data['classId'] = $classId;
		$data['companyId'] = $companyId;
		$data['status'] = $status;
		$data['userId'] = $_POST['userId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->starClass($postData);
		echo $result;
	}
	
	/**
	 * 撤销分享一个文件给某个用户或联系人群组
	 */
	public function backoutFile() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$receiver = isset($_POST['receiver']) ? $_POST['receiver'] : '';
		$isGroup = isset($_POST['isGroup']) ? $_POST['isGroup'] : '0';
		$companyId = $this->getUser()->getBigOrgId();
		$companyId= isset($_POST['companyId']) ? $_POST['companyId'] : '';
		$fromCompanyId= isset($_POST['fromCompanyId']) ? $_POST['fromCompanyId'] : '';
		$accessRight= isset($_POST['accessRight']) ? $_POST['accessRight'] : '';
		$data['fileId'] = $fileId;
		$data['receiver'] = $receiver;
		$data['isGroup'] = $isGroup;
		$data['companyId'] = $companyId;
		$data['fromCompanyId'] = $fromCompanyId;
		$data['accessRight'] = $accessRight;
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->backoutFile($postData);
		echo $result;
	}
	
	/**
	 * 发表文件评论
	 */
	public function newFileComment() {
		$data['companyId'] = $this->getUser()->getBigOrgId();
		$data['fileFlag'] = $_POST['fileFlag'];
		$data['version'] = $_POST['version'];
		$data['content'] = isset($_POST['content']) ? $_POST['content'] : '';
		$data['portrait'] = $_POST['portrait'];
		$data['fullName'] = $_POST['fullName'];
		$data['userId'] = $_POST['userId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->newFileComment($postData);
		echo $result;
	}
	
	public function deleteComment() {
		$data['companyId'] = $this->getUser()->getBigOrgId();
		$data['commentId'] = $_POST['commentId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->deleteComment($postData);
		echo $result;
	}
	
	/**
	 * 上传文件成功后调用，往db里插入file数据
	 */
	public function insertNewFileAfterUpload() {
		$data['classId'] = $_POST['classId'];
		$data['fileName'] = $_POST['fileName'];
		$data['size'] = $_POST['size'];
		$data['md5'] = $_POST['md5'];
		$data['type'] = $_POST['type'];
		$data['openLevel'] = $_POST['openLevel'];
		$data['fileId'] = $_POST['fileId'];
		$data['userId'] = $_POST['userId'];
//		$data['companyId'] = $this->getUser()->getBigOrgId();
		$data['companyId'] = $_POST['companyId'];
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->insertNewFileAfterUpload($postData);
		echo $result;
	}
	
	
	public  function getClassStarList(){
		$data['userId'] = $_POST['userId'];
		$data['companyId'] =$_POST['companyId'];
		$data['username'] = $_POST['username'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->getClassStarList(json_encode($data));
		echo  json_encode($result);
	}
	public  function getClassList(){
		$data['userId'] = $_POST['userId'];
		$data['companyId'] =$_POST['companyId'];
		$data['username'] = $_POST['username'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->getClassList(json_encode($data));
		echo  json_encode($result);
	}
	public function getFileCommentList(){
		$data['userName'] = $_POST['userName'];
		$data['companyId'] =$_POST['companyId'];
		$data['userId'] = $_POST['userId'];
		$data['fileFlag'] = $_POST['fileFlag'];
		$data['version'] = $_POST['version'];
		$data['page'] = $_POST['page'];
		$data['pageSize'] = $_POST['pageSize'];
		$data['versions'] = $_POST['versions'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->getFileCommentList(json_encode($data));
		echo  json_encode($result);
		
	}
	public function getFileList4Version(){
		$data['userName'] = $_POST['userName'];
		$data['companyId'] =$_POST['companyId'];
		$data['classId'] = $_POST['classId'];
		$data['fileId'] = $_POST['fileId'];
		$data['fileName'] = $_POST['fileName'];
		$data['fileType'] = $_POST['fileType'];
		$data['userId'] = $_POST['userId'];
		$data['remoteAddr'] = $this->getClientIp();
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->getFileList4Version(json_encode($data));
		echo  json_encode($result);
		
	}
	
	/**
	 * 用户登录时，初始化左侧分类列表
	 */
	public function initClassList(){
		$data['userId'] = $_POST['userId'];
		$data['companyId'] =$_POST['companyId'];
		$data['username'] = $_POST['username'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->initClassList(json_encode($data));
		echo  json_encode($result);
	}
	
	public function setFileOpenlevel(){
		$data['userId'] = $_POST['userId'];
		$data['remoteAddr'] = $this->getClientIp();
		$data['username'] = $_POST['username'];
		$data['companyId'] =$_POST['companyId'];
		$data['fileId'] =$_POST['fileId'];
		$data['fileName'] =$_POST['fileName'];
		$data['openlevel'] =$_POST['openlevel'];
		$data['classId'] = $_POST['classId'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->setFileOpenlevel(json_encode($data));
		echo  json_encode($result);
	}
	
	//发送评论
	public function onSendComment(){
		$data ['content'] = $_POST['content'];
		$data ['userId'] = $_POST['userId'];
		$data ['fileId'] = $_POST['fileId'];
		$data ['authorName'] = $_POST['authorName'];
		$data ['fullName'] = $_POST['fullName'];
		$data ['companyId'] = $_POST['companyId'];
		$postData = json_encode($data);
		$Proxy=$this->exec('getProxy',self::PROXY_NAME);
		$return=$Proxy->onSendComment($postData);
		echo json_encode($return);
	}
	//发送回复
	public function onReplyComment(){
		$data ['content'] = $_POST['content'];
		$data ['userId'] = $_POST['userId'];
		$data ['fullName'] = $_POST['fullName'];
		$data ['fileId'] = $_POST['fileId'];
		//messageId是评论回复的里面的ID，不是文件的ID，是指回复那条记录的前面的ID，有可能是当前评论，也有可能是评论里面的回复内容
		$data ['messageId'] = $_POST['messageId'];
		//parentID是该ID的父级ID，意思是属于哪个评论消息下面的，以为目前回复系统里面就有两级
		$data ['parentId'] = $_POST['parentId'];
		$data ['companyId'] = $_POST['companyId'];
		$data ['authorName'] = $_POST['authorName'];
		$postData = json_encode($data);
		$Proxy=$this->exec('getProxy',self::PROXY_NAME);
		$return=$Proxy->onReplyComment($postData);
		echo json_encode($return);
	}
	
	public function getFileCommentsListByFileId(){
		$data ['userId'] = $_POST['userId'];
		$data ['fileId'] = $_POST['fileFlag'];
		$data ['companyId'] = $_POST['companyId'];
		$postData = json_encode($data);
		$Proxy=$this->exec('getProxy',self::PROXY_NAME);
		$return=$Proxy->getFileCommentsListByFileId($postData);
		echo json_encode($return);
	}
	
	public function getMoreReplyComments(){
		$data ['parentId'] = $_POST['parentId'];
		$data ['companyId'] = $_POST['companyId'];
		$postData = json_encode($data);
		$Proxy=$this->exec('getProxy',self::PROXY_NAME);
		$return=$Proxy->getMoreReplyComments($postData);
		echo json_encode($return);
	}
	
	public function checkFileDeleteAndOpenLevel(){
		$data ['companyId'] = $_POST['companyId'];
		$data ['fileId'] = $_POST['fileId'];
		$postData = json_encode($data);
		$Proxy=$this->exec('getProxy',self::PROXY_NAME);
		$return=$Proxy->checkFileDeleteAndOpenLevel($postData);
		echo json_encode($return);
	}
	
	/**
	 * 我的文档文件分享到分类
	 */
	public function fileShareToClass() {
		$data['userName'] = $this->getUser()->getId();
		$data['remoteAddr'] = $this->getClientIp();
		$data ['fromCompanyId'] = $_POST['fromCompanyId'];
		$data ['fromFileId'] = $_POST['fromFileId'];
		$data ['toCompanyId'] = $_POST['toCompanyId'];
		$data ['toFolderIds'] = $_POST['toFolderIds'];
		$data ['toFolderNames'] = $_POST['toFolderNames'];
		$data ['userId'] = $_POST['userId'];
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->fileShareToClass($postData);
		echo json_encode($result);
	}
	
	public function searchFile() {
		$data['remoteAddr'] = $this->getClientIp();
		$data ['companyId'] = $_POST['companyId'];
		$data ['loginUserId'] = $_POST['loginUserId'];
		$data ['classId'] = $_POST['classId'];
		$data ['className'] = $_POST['className'];
		$data ['userName'] = $_POST['userName'];
		$data ['idSeq'] = $_POST['idSeq'];
		$data ['query'] = $_POST['query'];
		$data ['page'] = $_POST['page'];
		$data ['pageSize'] = $_POST['pageSize'];
		$data ['orderField'] = $_POST['orderField'];
		$data ['orderType'] = $_POST['orderType'];
		$data ['type'] = $_POST['type'];
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->searchFile($postData);
		echo json_encode($result);
	}
	
	public function dragFileToDocumnet(){
		$data ['companyId'] = $_POST['companyId'];
		$data ['sourceFileId'] = $_POST['sourceFileId'];
		$data ['sourceFileTitle'] = $_POST['sourceFileTitle'];
		$data ['targetDocIdSeq'] = $_POST['targetDocIdSeq'];
		$data ['targetDocId'] = $_POST['targetDocId'];
		$data ['isMyDocmentFlag'] = $_POST['isMyDocmentFlag'];
		$data ['userId'] = $_POST['userId'];
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->dragFileToDocumnet($postData);
		echo json_encode($result);
	}
	
	/**
	 * lujixiang 20151210 新增文件删除,并更新版本号
	 */
	public function deleteFileAndUpdateVersion() {
		$fileId = isset($_POST['fileId']) ? $_POST['fileId'] : '';
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$companyId = isset($_POST['companyId']) ? $_POST['companyId'] : '';
		$data['fileId'] = $fileId;
		$data['userId'] = $userId;
		$data['companyId'] = $companyId;
		$data['remoteAddr'] = $this->getClientIp();
		$data['userName'] = $this->getUser()->getId();
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$returnData = $proxy->deleteFileAndUpdateVersion($postData);
		echo $returnData;
	}
	
}