<?php
class ESUserCommunityAction extends ESActionBase{
	
	const PROXY_NAME = "onlinefile_publishCommunity";
	public function onlineCommnunity() {
		return $this->renderTemplate();
	}
	public function fileinfoCommunity() {
		$keyWord = 	isset($_POST['nameid']) ? $_POST['nameid'] : '';
		$flag = 	isset($_POST['flag']) ? $_POST['flag'] : '';
		return $this->renderTemplate(array('nameid'=>$keyWord,'flag'=>$flag));
	}
	/**用户发帖**/
	public function publishCommunityCard(){
		$data['usertitle'] = $_POST['usertitle'];
		$data['username'] = $_POST['username'];
		$data['userinfo'] = $_POST['userinfo'];
		$data['realtitle'] = $_POST['realtitle'];
		$data['thtype'] = $_POST['thtype'];
		$data['cardId'] = $_POST['cardId'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME) ;
		$return = $proxy->publishCommunityCard(json_encode($data));
		echo  json_encode($return);
	}
	
	/**用户评论**/
	public function showReplylist(){
		$data['pl_context_id'] = $_POST['pl_context_id'];
		$data['pl_info'] = $_POST['pl_info'];
		$data['pl_name'] = $_POST['pl_name'];
		$data['replyUserName'] = $_POST['replyUserName'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME) ;
		$return = $proxy->showReplylist(json_encode($data));
		echo  json_encode($return);
	}
	
	/**获取用户评论数**/
	public function getCommentTotal(){
		$data['cardId'] = $_POST['cardId'];
		$proxy = $this->exec("getProxy", self::PROXY_NAME) ;
		$return = $proxy->getCommentTotal(json_encode($data));
		echo  json_encode($return);
	}
	
	/**用户点赞**/
	public function praiseCard(){
		$cartdId = isset($_POST['cardId']) ? $_POST['cardId'] : '';
		$userId = isset($_POST['userId']) ? $_POST['userId'] : '';
		$status = isset($_POST['status']) ? $_POST['status'] : 'false';
		$data['cardId'] = $cartdId;
		$data['userId'] = $userId;
		$data['status'] = $status;
		$postData = json_encode($data);
		$proxy = $this->exec("getProxy", self::PROXY_NAME);
		$result = $proxy->praiseCard($postData);
	}
	
/* 	public function unlodeimgforCommunity(){
		$file_max_size=2097152;
		$num=count($_FILES['files']['name']);
		 for ($i = 0; $i < $num; $i++) {
		 	$randname=date("Y").date("m").date("d").date("H").date("i").date("s").rand(100, 999);
		 	$last=explode(".", $_FILES["files"]["name"][$i]);
		 	$suffx = $last[count($last)-1];
		 	if((($_FILES['files']['type'][$i] == 'image/jpg'
				or $_FILES['files']['type'][$i] == 'image/gif'
				or $_FILES['files']['type'][$i] == 'image/jpeg'
				or $_FILES['files']['type'][$i] == 'image/png'
				or $_FILES['files']['type'][$i] == 'image/bmp')
				&& $_FILES['files']['size'][$i] < $file_max_size)){
		 		if ($_FILES["files"]["error"][$i] > 0)
		 		{
		 			echo "Return Code: " . $_FILES["files"]["error"][$i] . "<br />";
		 			break;
		 		}else {
		 			if (file_exists("files/communityimage/" . $randname.".".$suffx))
		 			{
		 				echo $randname.".".$suffx;
		 			}else{
		 				if($suffx == "jpg" || $suffx == "jpeg" ){
		 					$uploadedfile = $_FILES['files']['tmp_name'][$i];
		 					$src = imagecreatefromjpeg($uploadedfile);
		 					list($width,$height)=getimagesize($uploadedfile);
		 					if($width>630){
		 						$newwidth=630;
		 						$newheight=($height/$width)*$newwidth;
		 						$tmp=imagecreatetruecolor($newwidth,$newheight);
		 						imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
		 						$filename = "files/communityimage/" . $randname.".".$suffx;
		 						imagejpeg($tmp,$filename,100);
		 						imagedestroy($src);
		 						imagedestroy($tmp);
		 					}else{
		 						move_uploaded_file($_FILES["files"]["tmp_name"][$i],$filename = "files/communityimage/" . $randname.".".$suffx);
		 					}
		 					echo $randname.".".$suffx."-";
		 				}else{
		 					$uploadedfile = $_FILES['files']['tmp_name'][$i];
		 					$src = $this->imagecreatefrombmp($uploadedfile);
		 					list($width,$height)=getimagesize($uploadedfile);
		 					if($width>630){
		 						$newwidth=630;
		 						$newheight=($height/$width)*$newwidth;
		 						$tmp=imagecreatetruecolor($newwidth,$newheight);
		 						imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
		 						$filename = "files/communityimage/" . $randname.".".$suffx;
		 						imagejpeg($tmp,$filename,100);
								imagedestroy($src);
								imagedestroy($tmp);
		 					}else{
		 						move_uploaded_file($_FILES["files"]["tmp_name"][$i],$filename = "files/communityimage/" . $randname.".".$suffx);
		 					}
		 					echo $randname.".".$suffx."-";
		 				}
		 			}
		 		}
		 	}else {
		 		echo "Invalid file";
		 	}
		}
	} */
	
	/**上传图片**/
/* 	  public function unlodeimgforCommunity() {
		$file_max_size=2097152;
		$last = explode(".", $_FILES["file"]["name"]);
		$suffx = $last[count($last)-1];
		$randname=date("Y").date("m").date("d").date("H").date("i").date("s").rand(100, 999);
		if((($_FILES['file']['type'] == 'image/jpg'
				or $_FILES['file']['type'] == 'image/gif'
				or $_FILES['file']['type'] == 'image/jpeg'
				or $_FILES['file']['type'] == 'image/png'
				or $_FILES['file']['type'] == 'image/bmp')
				&& $_FILES['file']['size'] < $file_max_size))
		{
			if ($_FILES["file"]["error"] > 0)
			{
				echo "Return Code: " . $_FILES["file"]["error"] . "<br />";
			}
			else
			{
				if (file_exists("files/communityimage/" . $randname.".".$suffx))
				{
					echo "files/communityimage/" . $randname.".".$suffx;
				}
				else
				{
					if($suffx == "jpg" || $suffx == "jpeg" ){
						$uploadedfile = $_FILES['file']['tmp_name'];
						$src = imagecreatefromjpeg($uploadedfile);
						list($width,$height)=getimagesize($uploadedfile);
						if($width>700){
							$newwidth=700;
							$newheight=($height/$width)*$newwidth;
							$tmp=imagecreatetruecolor($newwidth,$newheight);
							imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
							$filename = "files/communityimage/" . $randname.".".$suffx;
							imagejpeg($tmp,$filename,100);
							imagedestroy($src);
							imagedestroy($tmp);
						}else{
							move_uploaded_file($_FILES["file"]["tmp_name"],$filename = "files/communityimage/" . $randname.".".$suffx);
						}
					}else{
						$uploadedfile = $_FILES['file']['tmp_name'];
						$src = $this->imagecreatefrombmp($uploadedfile);
						list($width,$height)=getimagesize($uploadedfile);
						if($width>700){
							$newwidth=700;
							$newheight=($height/$width)*$newwidth;
							$tmp=imagecreatetruecolor($newwidth,$newheight);
							imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
							$filename = $filename = "files/communityimage/" . $randname.".".$suffx;
							imagejpeg($tmp,$filename,100);
							imagedestroy($src);
							imagedestroy($tmp);
						}else{
							move_uploaded_file($_FILES["file"]["tmp_name"],$filename = "files/communityimage/" . $randname.".".$suffx);
						}

					}
					echo $randname.".".$suffx;
				}
			}
		}
		else
		{
			echo "Invalid file";
		}
	}   */

	/**
	 * BMP 创建函数
	 * @author simon
	 * @param string $filename path of bmp file
	 * @example who use,who knows
	 * @return resource of GD
	 */
	public function imagecreatefrombmp( $filename ){
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

}
?>
