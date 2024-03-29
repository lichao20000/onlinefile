<?php
class ProxySecurity extends AgentProxyAbstract {

  /**
   *
   * Config服务名称
   * @var const
   */
  const SECURITY_SERVICE_NAME = 'authservice';

  /**
   *
   * 构造函数
   * @param array $appInfo
   * @return return_type
   */
  public function __construct(array $appInfo) {
    parent::__construct($appInfo);
  }

  /**
   * 生成用户Token
   * Enter description here ...
   * @param unknown_type $userId
   * @param unknown_type $password
   * @return return_type
   * @GET
   * @Path("createUserAuthToken/{userId}/{password}")
   * public AuthToken createUserAuthToken(@PathParam("userId") String userId,
                                         @PathParam("password") String password);
   */
  public function createUserToken($userId, $password) {
    $urlParam = array();
    array_push($urlParam, 'createUserAuthToken', $userId, $password);
    $url = implode('/', $urlParam);
    return $this->get(self::SECURITY_SERVICE_NAME, $url);
  }

  /**
   *
   * 是否是有效的用户Token
   * @param unknown_type $userId
   * @param unknown_type $token
   * @return return_type
   * @GET
   * @Path("isValidUserToken/{userId}/{token}")
   * public boolean isValidUserToken(@PathParam("userId") String userId,
   * 								 @PathParam("token") String token);
   */
  public function isValidUserToken($userId, $usertoken) {
    $urlParam = array();
    array_push($urlParam, 'isValidUserToken', $userId, $usertoken);
    $url = implode('/', $urlParam);
    $result = $this->get(self::SECURITY_SERVICE_NAME, $url);
    return (boolean)$result;
  }

  /**
   *
   * 是否是有效的EIP app Token
   * @param unknown_type $appId
   * @param unknown_type $token
   * @return return_type
   * @GET
   * @Path("isValidAppToken/{appId}/{token}")
   * public boolean isValidAppToken(@PathParam("appId") String appId,
   * 								@PathParam("token") String token);
   */
  public function isValidAppToken($appId, $appToken) {
    $urlParam = array();
    array_push($urlParam, 'isValidAppToken', $appId, $appToken);
    $url = implode('/', $urlParam);
    $result = $this->get(self::SECURITY_SERVICE_NAME, $url);
    return (boolean)$result;
  }

  /**
   *
   * 验证用户是否具有访问权限
   * @param unknown_type $userId
   * @param unknown_type $resourceUrl
   * @return return_type
   * @GET
   * @Path("userHasPrivilege/{userId}/{resourceUrl}")
   * public AuthResult userHasPrivilege(@PathParam("userId") String userId,
   * 									@PathParam("resourceUrl") String resourceUrl);
   */
  public function userHasPrivilege($userId, $resourceUrl) {
    $urlParam = array();
    array_push($urlParam, 'userHasPrivilege', $userId, $resourceUrl);
    $url = implode('/', $urlParam);
    $result = $this->get(self::SECURITY_SERVICE_NAME, $url);
    return $result ? json_decode($result) : $result;
  }

  /**
   *
   * 验证EIP App 是否具有访问权限
   * @param unknown_type $appId
   * @param unknown_type $resourceUrl
   * @return mixed
   * @GET
   * @Path("appHasPrivilege/{appId}/{resourceUrl}")
   * public AuthResult appHasPrivilege(@PathParam("appId") String appId,
   * 								   @PathParam("resourceUrl") String resourceUrl);
   */
  public function appHasPrivilege($appId, $resourceUrl) {
    $urlParam = array();
    array_push($urlParam, 'appHasPrivilege', $appId, $resourceUrl);
    $url = implode('/', $urlParam);
    $result = $this->get(self::SECURITY_SERVICE_NAME, $url);
    return $result ? json_decode($result) : $result;
  }

  /**
   *
   * 合并了检查用户身份以及授权验证的逻辑
   * @param unknown_type $appId
   * @param unknown_type $resourceUrl
   * @return return_type
   * @GET
   * @Path("authUser/{appId}/{userToken}/{resourceUrl}")
   * public AuthResult authUser(@PathParam("userId") String userId,
   * 							@PathParam("userToken") String userToken,
   * 							@PathParam("resourceUrl") String resourceUrl);
   */
  public function authUser($appId, $userToken, $resourceUrl) {
    $urlParam = array();
    array_push($urlParam, 'authUser', $appId, $userToken, $resourceUrl);
    $url = implode('/', $urlParam);
    return $this->get(self::SECURITY_SERVICE_NAME, $url);
  }

  /**
   * 合并了检查App身份以及授权验证的逻辑
   * @param unknown_type $appId
   * @param unknown_type $userToken
   * @param unknown_type $resourceUrl
   * @return return_type
   * @GET
   * @Path("authApp/{appId}/{appToken}/{resourceUrl}")
   * public AuthResult authApp(@PathParam("appId") String appId,
   * 						   @PathParam("appToken") String appToken,
   * 						   @PathParam("resourceUrl") String resourceUrl);
   */
  public function authApp($appId, $appToken, $resourceUrl) {
    $urlParam = array();
    array_push($urlParam, 'authApp', $appId, $appToken, $resourceUrl);
    $url = implode('/', $urlParam);
    return $this->get(self::SECURITY_SERVICE_NAME, $url);
  }
}