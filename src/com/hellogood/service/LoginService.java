package com.hellogood.service;

import com.edate.ws.constant.Code;
import com.edate.ws.constant.InviteRegisterConstants;
import com.edate.ws.constant.TokenConstants;
import com.edate.ws.constant.UserInfoConstants;
import com.edate.ws.domain.*;
import com.edate.ws.exception.BusinessException;
import com.edate.ws.exception.UserRegisterOperateException;
import com.edate.ws.http.vo.EaseMobUserVO;
import com.edate.ws.http.vo.LoginVO;
import com.edate.ws.http.vo.RegisterVO;
import com.edate.ws.mapper.LoginMapper;
import com.edate.ws.utils.HttpClientUtil;
import com.edate.ws.utils.StaticFileUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 登录Service
 * @author fzh
 *
 */
@Service
public class LoginService {
	@Autowired
	private LoginMapper loginMapper;
	@Autowired
	private LoginRecordsService loginRecordsService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserFireService userFireService;
	@Autowired
	private InviteRegisterService inviteRegisterService;
	@Autowired
	private EaseMobService easeMobService;
	@Autowired
	private ThirdPartyLoginService thirdPartyLoginService;
	@Autowired
	UserActiveService userActiveService;
	@Autowired
	private NoticeFlagService noticeFlagService;
	@Autowired
	private UserLabelService userLabelService;
	@Autowired
	private UserPlanService userPlanService;
	@Autowired
	UserCacheManager userCacheManager;

	public static Logger logger = LoggerFactory.getLogger(LoginService.class);

	/**
	 * 保存
	 * @param login
	 */
	public void save(Login login){
		try {
			loginMapper.insert(login);
		} catch (DuplicateKeyException e) {
			logger.info("用户注册时重复提交");
			throw new UserRegisterOperateException("您已注册");
		}
		
	}
	
	/**
	 * 保存
	 * @param login
	 */
	public UserInfo saveAndUpdate(Login login, RegisterVO register){
				LoginVO loginVO = new LoginVO();
				loginVO.domain2Vo(login);
				loginVO.setMobile(login.getPhone());
				loginVO.setClientType(register.getClientType());
				loginVO.setLoginIp(register.getLoginIp());
				loginVO.setLoginAddr(register.getLoginAddr());
				
				UserInfo userInfo = this.createUser(loginVO);
				
		        //生成邀请注册信息
		        if(register.getInviteUid() != null){
		            InviteRegister inviteRegister = new InviteRegister();
		            inviteRegister.setCreateTime(new Date());
		            inviteRegister.setStatus(Code.STATUS_INVALID);
		            inviteRegister.setUserId(userInfo.getId());
		            inviteRegister.setInviteUid(register.getInviteUid());
		            inviteRegister.setInviteType(register.getInviteType());
		            if(StringUtils.isEmpty(register.getFunctionType())){
		            	inviteRegister.setFunctionType(InviteRegisterConstants.INVITEREGISTER_FUNCTION_PERSONAL);
		            }else{
		            	inviteRegister.setFunctionType(register.getFunctionType());
		            }
		            //兼容旧版, 旧版没有来源ID
		            if(register.getSourceId() == null){
						register.setSourceId(register.getInviteUid());
					}
					inviteRegister.setSourceId(register.getSourceId());
		            inviteRegisterService.add(inviteRegister);
		        }
				
				//注册时绑定第三方账号
				if(register.getType() != null && register.getOpenId() != null){
					logger.info("注册时绑定第三方账号类型：" + register.getType() + ",手机号：" + userInfo.getPhone() + ",openId:" + register.getOpenId());
					ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin(userInfo.getPhone(), register.getOpenId(), register.getType(), null);
					thirdPartyLoginService.insert(thirdPartyLogin);
				}
				return userInfo;
				
	}
	
	/**
	 * 查询用户
	 * @param example
	 * @return
	 */
	public Login getUser(LoginExample example){
		List<Login> login = loginMapper.selectByExample(example);
		if(login !=null && login.size()>0)
			return login.get(0);
		return null;
	}
	
	public Login getLoginByPhone(String phone){
		Login login = null;
		LoginExample example = new LoginExample();
		example.createCriteria().andPhoneEqualTo(phone);
		List<Login> list = loginMapper.selectByExample(example);
		if(!list.isEmpty()){
			login = list.get(0);
		}
		return login;
	}
	
	public Login getLoginByUserId(Integer userId){
		LoginExample example = new LoginExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Login> list = loginMapper.selectByExample(example);
		if(list.isEmpty()){
			throw new BusinessException("获取用户登录信息失败");
		}
		return list.get(0);

	}

	/**
	 * 获取客户端版本号
	 * @param userId
	 * @return
     */
	public String getCacheClientVersion(Integer userId){
		String clientVersion = userCacheManager.get(userId, UserCacheManager.UserField.APK_VERSION);
		if(clientVersion == null){
			Login login = getLoginByUserId(userId);
			if(login == null){
				logger.error("找不到用户[userId="+userId+"]账户信息");
			}else {
				clientVersion = login.getApkVersion();
				userCacheManager.updateApkVersion(userId, clientVersion);
			}
		}
		return clientVersion;
	}
	
	public Login getLoginByUserIdWithOutThrow(Integer userId){
		LoginExample example = new LoginExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Login> list = loginMapper.selectByExample(example);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);

	}
	
	/**
	 * 更新
	 * @param login
	 */
	public void updateLogin(Login login){
		loginMapper.updateByPrimaryKey(login);
	}
	
	public void updateBootUpTime(LoginVO loginVO) {
		if(loginVO.getUserId() != null && loginVO.getUserId() > 0){
			Login login = this.getLoginByUserId(loginVO.getUserId());
			login.setLastBootUpTime(new Date());
			loginMapper.updateByPrimaryKey(login);
			//记录活跃度
			UserActive userActive = new UserActive();
			userActive.setDeviceType(loginVO.getClientType());
			userActive.setUserId(loginVO.getUserId());
			userActiveService.add(userActive);
		}
		
	}
	
	/**
	 * 更新
	 * @param loginVO
	 */
	public void updateLoginByUserId(LoginVO loginVO){
		Login login = this.getLoginByUserId(loginVO.getUserId());
		if(login != null){
			login.setApkVersion(loginVO.getApkVersion());
			login.setClientInfo(loginVO.getClientInfo());
			login.setLastBootUpTime(new Date());
			loginMapper.updateByPrimaryKey(login);

			//更新缓存
			userCacheManager.updateApkVersion(loginVO.getUserId(), loginVO.getApkVersion());
		}
	}
	
	/**
	 * 更新并增加登陆记录表并返回新生成的token
	 * @param login
	 */
	public String updateLoginAndRecord(Login login, LoginVO loginVO){
		login.setLastLogintime(new Date());
		this.updateLogin(login);
		
		loginRecordsService.add(login.getId(), loginVO);
		return tokenService.insertOrUpdate(login.getUserId(), TokenConstants.TOKEN_INVALID_LOGIN_OTHER_DEVICE);
	}
	
	/**
	 * 是否存在用户
	 * @param phone
	 * @return
	 */
	public boolean isExist(String phone){
		LoginExample example = new LoginExample();
		example.createCriteria().andPhoneEqualTo(phone);
		List<Login> list = loginMapper.selectByExample(example);
		if(list !=null && list.size()>0){
			return true;
		}
		return false;
	}


	
	/**
	 * 返回环信的账号密码，没有就生成
	 * @param userId
	 * @return
	 */
	public  EaseMobUserVO getEaseMobUserVO(Integer userId){
		EaseMobUserVO vo = new EaseMobUserVO();
		UserInfo userInfo = userInfoService.getUserInfo(userId);
		Login login = this.getLoginByUserId(userId);
		if(StringUtils.isEmpty(login.getEasemobPassword())){
			vo = this.createImUser(userInfo.getId(), userInfo.getUserCode());
		}
		else{
			vo.setUserName(userInfo.getUserCode());
			vo.setPassword(login.getEasemobPassword());
		}
		return vo;
	}
	/**
	 * 创建环信账号
	 * @param userId
	 * @return
	 */
	public EaseMobUserVO createImUser(Integer userId, String userCode){
		EaseMobUserVO vo = new EaseMobUserVO();
		Login login = this.getLoginByUserId(userId);
		String easemobPassword = this.md5Encrypt(userCode);
		ObjectNode objectNode = easeMobService.createNewIMUserSingle(userCode, easemobPassword, null);
		if(objectNode.get("statusCode") != null && (objectNode.get("statusCode").intValue() == 200
				|| "duplicate_unique_property_exists".equals(objectNode.get("error").asText()))){
			login.setEasemobPassword(easemobPassword);
			this.updateLogin(login);
			vo.setUserName(userCode);
			vo.setPassword(login.getEasemobPassword());
		}else{
			return null;
		}
		return vo;
	}
	
	
	
	/**
	 * md5加密
	 * @param password
	 * @param code
	 * @return
	 */
	public String md5Encrypt(String userCode){
		return DigestUtils.md5Hex(userCode);
	}
	
	/**
	 * 判断是否黑名单
	 * @param userId
	 * @return
	 */
	public Boolean isBlackList(Integer userId){
		Login login = this.getLoginByUserId(userId);
		if(login.getBlacklist() == 1)
			return true;
		return false;
	}

	/**
	 * 网页授权，根据code获取unionid
	 * @param code
	 * @return
	 */
	public String getUnionIdByCode(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + StaticFileUtil.getProperty("webLicense", "appid"));
		sb.append("&secret=" + StaticFileUtil.getProperty("webLicense", "secret"));
		sb.append("&code=" + code);
		sb.append("&grant_type=authorization_code");
		String respone =HttpClientUtil.sendGet(url, sb.toString());
		JSONObject data = new JSONObject(respone);
	    if(data.isNull("unionid") || data.get("unionid") == null){
	    	throw new BusinessException("网页授权返回参数错误");
	    }
		return (String) data.get("unionid");
	}
	/**
	 * 网页授权，根据code获取openId
	 * @param code
	 * @return
	 */
	public String getOpenIdByCode(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + StaticFileUtil.getProperty("webLicense", "appid"));
		sb.append("&secret=" + StaticFileUtil.getProperty("webLicense", "secret"));
		sb.append("&code=" + code);
		sb.append("&grant_type=authorization_code");
		String respone =HttpClientUtil.sendGet(url, sb.toString());
		JSONObject data = new JSONObject(respone);
		if(data.isNull("openid") || data.get("openid") == null){
			throw new BusinessException("网页授权返回参数错误");
		}
		return (String) data.get("openid");
	}
	
	/**
	 * 绑定第三方没有账号，生成一个
	 * @param phone
	 */
	public UserInfo createUser(LoginVO loginVO){
		//生成用户信息
		UserInfo userInfo = new UserInfo();
        userInfo.setRegistTime(new Date());   
		userInfo.setPhone(loginVO.getMobile());
		userInfo.setCheckStatus(UserInfoConstants.USERINFO_REGISTER);//默认为已注册
		userInfo.setShowStatus(0);
		userInfo.setSearchStatus(0);
		userInfo.setTrystStatus(UserInfoConstants.USERINFO_TRYST_STATUS_MAKE_FRIEND);
		userInfo.setOnlineStatus(UserInfoConstants.USERINFO_ONLINE);
		userInfoService.add(userInfo);
		
		//生成登录信息
		Login login = new Login();
		loginVO.vo2Domain(login);
		login.setPhone(loginVO.getMobile());
		login.setPhoneClient(loginVO.getClientType());
		login.setUserId(userInfo.getId());
		login.setBlacklist(Code.STATUS_INVALID);// 不是黑名单
		login.setCreateTime(new Date());
		if(loginVO.getPassword() == null)
			loginVO.setPassword(DigestUtils.md5Hex(loginVO.getMobile().substring(5).concat(loginVO.getMobile())));
		login.setPassword(loginVO.getPassword());
		login.setLastLogintime(new Date());
		this.save(login);// 保存登录表
		
		loginRecordsService.add(login.getId(), loginVO);
		
		//生成用户的点灯表
		userFireService.insert(userInfo.getId());
		
		//生成用户通知表
		noticeFlagService.create(userInfo.getId());
		
		//生成标签表
		userLabelService.add(userInfo.getId());

		//生成计划表
		userPlanService.create(userInfo.getId());
		
		return userInfo;
	}
	
	/**
	 * 
	 * @param toUid
	 * @return
	 */
	public String getPhoneClient(Integer userId) {
		Login login = this.getLoginByUserId(userId);
		LoginRecords record = loginRecordsService.getNewestRecord(login.getId());
		
		if(record == null)
			return null;
		return record.getClientType();
	}
}
