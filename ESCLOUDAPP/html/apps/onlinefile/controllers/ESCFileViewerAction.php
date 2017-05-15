<?php
/**
 * 文件在线浏览
 */
class ESCFileViewerAction extends ESActionBase
{
	const PROXY_NAME = "onlinefile_fileviewerws";
	
	function getOnlineViewUrl(){
		$proxy = $this->exec("getProxy", self::PROXY_NAME) ;
		$fileId = $_POST['fileId'];
		$fileType = $_POST['fileType'];
		$data['fileId'] = $fileId;
		$data['fileType'] = $fileType;
		$postData = json_encode($data);
		$result = $proxy->getOnlineViewUrl($postData);
		echo json_encode($result);
	}
	
	//图片在线浏览
	function imgOnlineBrowse(){
		$proxy = $this->exec("getProxy", self::PROXY_NAME) ;
		$fileId = $_POST['fileId'];
		$data['fileId'] = $fileId;
		$postData = json_encode($data);
		$result = $proxy->imgOnlineBrowse($postData);
		echo json_encode($result);
	}
}

?>