<?php
/**
 * 集群设置管理
 * @author liqiubo
 *
 */
class ESClusterManageAction extends ESActionBase
{
	public function getDataList(){
		$page = isset($_POST['page']) ? $_POST['page'] : 1;
		$rp = isset($_POST['rp']) ? $_POST['rp'] : 20;
		$data['start'] = ($page-1)*$rp;
		$data['limit'] = $rp;
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", "escloud_syncConfigws");
		$start = ($page - 1) * $rp + 1;
		$returnData = $proxy->getOnlineFileClusterLst($postData);
		$jsonData = array('page'=>$page,'total'=> $returnData->total,'rows'=>array());
		foreach ($returnData->lst as $row){
			$entry = array("id"=>$row->ID,
					"cell"=>array(
							"rownum"=>$start,
							"editbtn"=>"<span class='editbtn' clusterId='".$row->ID."'>&nbsp;</span>&nbsp;&nbsp;<span class='delbtn' clusterId='".$row->ID."'>&nbsp;</span>",
							"name"=>$row->TITLE,
							"ip"=>$row->SERVER_IP,
							"port"=>$row->SERVER_PORT,
							"maxPerson"=>$row->MAX_USE_NUM,
							"enable"=>$row->ENABLE == "1"?"是":"否"
					),
			);
			$jsonData['rows'][] = $entry;
			$start++;
		}
		echo json_encode($jsonData);
	}
	
	public function delCluster(){
		$clusterInfo=array();
		$clusterInfo['id']=$_POST['clusterid'];
		$jsonData = json_encode($clusterInfo);
		$proxy=$this->exec('getProxy','escloud_syncConfigws');
		$result=$proxy->delCluster($jsonData);
		echo json_encode($result);
	}
	
	public function addClusterHtm(){
		return $this->renderTemplate(array('title'=>'','ip'=>'','port'=>'','maxPerson'=>'','enable'=>'0'),'ESClusterManage/createClusterHtm');
	}
	
	public function saveCluster(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$clusterInfo=array();
		$clusterInfo['title']=$data['TITLE'];
		$clusterInfo['ip']=$data['IP'];
		$clusterInfo['port']=$data['PORT'];
		$clusterInfo['maxusenum']=$data['MAXUSE'];
		$clusterInfo['used']=$data['USED'];
		$jsonData = json_encode($clusterInfo);
		$proxy=$this->exec('getProxy','escloud_syncConfigws');
		$result=$proxy->saveCluster($jsonData);
		echo json_encode($result);
	}
	
	public function editClusterHtm(){
		$clusterInfo=array();
		$clusterInfo['ID']=$_POST['clusterid'];
		$jsonData = json_encode($clusterInfo);
		$proxy=$this->exec('getProxy','escloud_syncConfigws');
		$result=$proxy->getClusterById($jsonData);
		return $this->renderTemplate(array('title'=>$result->TITLE,'ip'=>$result->SERVER_IP,'port'=>$result->SERVER_PORT,'maxPerson'=>$result->MAX_USE_NUM,'enable'=>$result->ENABLE),'ESClusterManage/createClusterHtm');
	}
	
	public function updateCluster(){
		$request=$this->getRequest();
		$data=$request->getPost();
		$ID=$data['ID'];
		$TITLE=$data['TITLE'];
		$IP=$data['IP'];
		$PORT=$data['PORT'];
		$MAXUSE=$data['MAXUSE'];
		$USED=$data['USED'];
		$clusterInfo=array();
		$clusterInfo['id']=$ID;
		$clusterInfo['title']=$TITLE;
		$clusterInfo['ip']=$IP;
		$clusterInfo['port']=$PORT;
		$clusterInfo['maxusenum']=$MAXUSE;
		$clusterInfo['used']=$USED;
		$jsonData = json_encode($clusterInfo);
		$proxy=$this->exec('getProxy','escloud_syncConfigws');
		$result=$proxy->updateCluster($jsonData);
		echo json_encode($result);
	}
	
	
}
