<?php 
	$session_key = "authnum_session" ;
	if(isset($_POST['register']) || isset($_GET['register'])){
		$session_key = "authnum_session_register" ;
	}
	session_start();
	if(isset($_POST['code'])) {
        error_log("\n");
        error_log('isset($_POST[code]');
		$random = $_POST['random'] ;
		$code = $_POST['code'];

        error_log("session_key1=$session_key");
		error_log("random1=$random");
        error_log("code1=$code");
        

        if(!isset($_SESSION[$session_key.$random])){
            error_log("code2=null");
			echo "null" ;
		} else {
			error_log("code2=");
            error_log($_SESSION[$session_key.$random]);
			if($code == $_SESSION[$session_key.$random]){
				echo "true" ;
			} else {
				echo "false" ;
			}
        }
	} else {
        error_log("\n");
        error_log('isnotset($_POST[code]');
		$random = $_GET['random'] ;
		require './ValidateCode.class.php';  //先把类包含进来，实际路径根据实际情况进行修改。
		$_vc = new ValidateCode();  //实例化一个对象
		$_vc->doimg();
		$_SESSION[$session_key.$random] = $_vc->getCode();//验证码保存到SESSION中
 
        error_log("session_key2=$session_key");
        error_log("random2=$random");
        error_log("code1=");
        error_log($_vc->getCode());
     
	}
?>