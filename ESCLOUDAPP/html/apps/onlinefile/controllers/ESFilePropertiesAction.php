<?php
/**
 * 文件属性管理
 * @author liqiubo
 *
 */
class ESFilePropertiesAction extends ESActionBase
{
	
	public function filePropMain(){
		$userId = $_POST['userId'];
		$jsonData = $this -> getOnlineFilefilePropLst(0,10,$userId,'');
		return $this->renderTemplate(array('total'=>$jsonData->total,'lst'=>$jsonData->Lst,'page'=>'1'),'ESFileProperties/filePropMain');
	}
	
	private  function getOnlineFilefilePropLst($start,$limit,$userId,$queryKeyWord){
		global $user;
		$queryPropInfo=array();
		$queryPropInfo['start']=$start;
		$queryPropInfo['limit']=$limit;
		$queryPropInfo['userId']=$userId;
		$queryPropInfo['queryKeyWord']=$queryKeyWord;
		$queryPropInfo['username']=$this->getUser()->getId();
		$queryPropInfo['ip']=$this->getClientIp();
		$queryPropInfo['companyId'] = $user->bigOrgId;
		$jsonData = json_encode($queryPropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$result=$proxy->getFilePropLst($jsonData);
		return $result;
	}
	
	public function getOneFileProp(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$fileId=$data['filePropId'];
		$filePropInfo=array();
		$filePropInfo['id']=$fileId;
		$jsonData = json_encode($filePropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$result=$proxy->getEditFileProp($jsonData);
		echo json_encode($result);
	}	
	
	public function getNextPageLst(){
		$pageNow = $_POST['pageNow'];
		$userId = $_POST['userId'];
		$result = $this -> getOnlineFilefilePropLst($pageNow*10,10,$userId,'');
		$jsonData = array('page'=>$pageNow+1,'total'=>$result->total,'lst'=>$result->Lst);
		echo json_encode($jsonData);
	}
	
	public function getEditFileProp(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$fileId=$data['fileId'];
		$filePropInfo=array();
		$filePropInfo['id']=$fileId;
		$jsonData = json_encode($filePropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$value=$proxy->getEditFileProp($jsonData);
		return $this->renderTemplate(array('isEdit'=>'1','title'=>$value->TITLE,'type'=>$value->TYPE,'isnull'=>$value->ISNULL,'length'=>$value->LENGTH,'dotlength'=>$value->DOTLENGTH,'description'=>$value->DESCRIPTION),'ESFileProperties/addFileProphtm');
	}
	
	public function addFileProp(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$userid = $data['userId'];
		$TITLE=$data['TITLE'];
		$TYPE=$data['TYPE'];
		$ISNULL=$data['ISNULL'];
		$LENGTH=$data['LENGTH'];
		$DOTLENGTH=$data['DOTLENGTH'];
		$DESCRIPTION=$data['DESCRIPTION'];
		$companyId=$data['companyId'];
		$filePropInfo=array();
		$filePropInfo['USERID']='1';
		$filePropInfo['TITLE']=$TITLE;
		$filePropInfo['TYPE']=$TYPE;
		$filePropInfo['ISNULL']=$ISNULL;
		$filePropInfo['LENGTH']=$LENGTH;
		$filePropInfo['DOTLENGTH']=$DOTLENGTH;
		$filePropInfo['DESCRIPTION']=$DESCRIPTION;
		$filePropInfo['COMPANYID']=$companyId;
		$filePropInfo['username']=$this->getUser()->getId();
		$filePropInfo['ip']=$this->getClientIp();
		$jsonData = json_encode($filePropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$result=$proxy->addFileProp($jsonData);
		echo json_encode($result);
	} 
	
	public function delFileProp(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$fileId=$data['fileId'];
		$companyId = $data['companyId'];
		$title = $data['title'];
		$filePropInfo=array();
		$filePropInfo['fileId']=$fileId;
		$filePropInfo['companyId']=$companyId;
		$filePropInfo['username']=$this->getUser()->getId();
		$filePropInfo['ip']=$this->getClientIp();
		$filePropInfo['title']=$title;
		$jsonData = json_encode($filePropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$result=$proxy->delFileProp($jsonData);
		echo json_encode($result);
	}
	
	public function editFileProp(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$ID=$data['ID'];
		$TITLE=$data['TITLE'];
		$TYPE=$data['TYPE'];
		$ISNULL=$data['ISNULL'];
		$LENGTH=$data['LENGTH'];
		$DOTLENGTH=$data['DOTLENGTH'];
		$DESCRIPTION=$data['DESCRIPTION'];
		$companyId=$data['companyId'];
		$filePropInfo=array();
		$filePropInfo['ID']=$ID;
		$filePropInfo['TITLE']=$TITLE;
		$filePropInfo['TYPE']=$TYPE;
		$filePropInfo['ISNULL']=$ISNULL;
		$filePropInfo['LENGTH']=$LENGTH;
		$filePropInfo['DOTLENGTH']=$DOTLENGTH;
		$filePropInfo['companyId']=$companyId;
		$filePropInfo['username']=$this->getUser()->getId();
		$filePropInfo['ip']=$this->getClientIp();
		$jsonData = json_encode($filePropInfo);
		$proxy=$this->exec('getProxy','onlinefile_filePropws');
		$result=$proxy->editFileProp($jsonData);
		echo json_encode($result);
	}
}
