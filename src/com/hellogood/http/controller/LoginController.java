package com.hellogood.http.controller;

import com.hellogood.constant.Code;
import com.hellogood.constant.ResponseCode;
import com.hellogood.constant.TokenConstants;
import com.hellogood.constant.UserConstants;
import com.hellogood.domain.*;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.LoginVO;
import com.hellogood.http.vo.RegisterVO;
import com.hellogood.service.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 用户登录Controller
 * @author kejian
 */
@Controller
@RequestMapping(value = "/auth")
public class LoginController extends BaseController{
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private SmsCodeService smsCodeService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ThirdPartyLoginService thirdPartyLoginService;
   
	/**
	 * 用户授权验证
	 * @param register
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/validateThirdParty.do", method = RequestMethod.POST)
	public Map<String, Object> validateThirdParty(@RequestBody RegisterVO register){
		logger.info(register.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		//网页第三方授权
		if(register.getCode() != null){
			register.setType("WEIXIN_UNION");
			register.setOpenId(loginService.getUnionIdByCode(register.getCode()));
		}
		if(register.getOpenId() == null || register.getType() == null)
    		throw new BusinessException("用户授权参数错误！");
		
		ThirdPartyLogin thirdPartyLogin = thirdPartyLoginService.isExist(register.getOpenId());
		if(thirdPartyLogin == null){//未绑定该第三方账号
			map.put("loginStatus", 0);
			map.put("openid", register.getOpenId());
			map.put("type", register.getType());
			map.put(STATUS, STATUS_SUCCESS);
			return map;
		}
		
		LoginExample example = new LoginExample();
		example.createCriteria().andPhoneEqualTo(thirdPartyLogin.getPhone());
		Login loginInfo = loginService.getUser(example);
		if(loginInfo == null){
			map.put(STATUS, STATUS_FAILED);
			map.put(MESSAGE, "账号或者密码错误");
			return map;
		}
		/**
		 * 第三方绑定了
		 */
		User user = userService.getUserByPhone(thirdPartyLogin.getPhone());
		//更新登录时间并记录登录信息
		LoginVO loginVO = new LoginVO();
		loginVO.setClientType(register.getType());
		String token = loginService.updateLoginAndRecord(loginInfo, loginVO);
		map.put(STATUS, STATUS_SUCCESS);
		map.put("userId", user.getId());
		map.put("loginStatus", 1);
		//用户前缀
		map.put("userCode", user.getUserCode());
		map.put("token", token);
		return map;
	}
	
	/**
	 * 用户注册
	 * @param register
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public Map<String, Object> register(@RequestBody RegisterVO register){
		logger.info(register.toString());
		String password = register.getPassword();
		String mobile = register.getMobile();
        if(!"invite".equals(register.getClientType())){
        	//ios是直接用md5加密传过来，没有rsa加密
        	if("Android".equals(register.getClientType()))
        		password = decrypt(password);
        }
        //直接去官网注册
        if("invite".equals(register.getClientType()) && register.getInviteUid() == null){
        	register.setClientType("official");
        }
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginService.isExist(register.getMobile())) {
			// 提示用户已存在
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "该号码已注册！");
			return result;
		}
		// 不存在则进入注册
		SmsCodeExample smsExample = new SmsCodeExample();
		smsExample.createCriteria().andPhoneEqualTo(mobile);
		//TODO 屏蔽校验码校验
		SmsCode sms = smsCodeService.select(smsExample);
		if (!StringUtils.equals(register.getSmsCode(), String.valueOf(sms.getCode()))) {
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "验证码错误！");
			result.put("timestamp", sms.getTimestamp());// 时间戳
			return result;
		}
        if(StringUtils.isBlank(register.getClientType())){
            throw new BusinessException("客户端类型不能为空");
        }
        Login login = new Login();
    	// MD5加密(h5,ios直接用md5传过来)
        if("IOS".equals(register.getClientType()) || "h5".equals(register.getClientType()))
        	login.setPassword(password);
        else
        	login.setPassword(md5Encrypt(password, mobile));
        login.setPhone(mobile);
        User user = loginService.saveAndUpdate(login, register);

		result.put(STATUS, STATUS_SUCCESS);
		result.put("userId", user.getId());
		//用户前缀
		result.put("userCode", user.getUserCode());
		result.put("token", tokenService.insertOrUpdate(user.getId(), TokenConstants.TOKEN_INVALID_INSERT_NEW));
		logger.info(result.toString());
		return result;
	}

	/**
	 * 用户登录
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody LoginVO loginVO) throws JSONException, IOException {
		logger.info(loginVO.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		String mobile = loginVO.getMobile();
		String password = loginVO.getPassword();
        if(loginVO.getOpenId() != null && loginVO.getType() != null){
        	ThirdPartyLogin thirdPartyLogin = thirdPartyLoginService.isExist(loginVO.getOpenId());
        	if(thirdPartyLogin != null && mobile.equals(thirdPartyLogin.getPhone())){
        		throw new BusinessException("该手机号已经绑定过其他账户了");
        	}
        }
		//解密（Ios没有经过rsa加密）
		if("Android".equals(loginVO.getClientType()))
			password = decrypt(password);
		
        LoginExample example = new LoginExample();
		example.createCriteria().andPhoneEqualTo(mobile);
		Login loginInfo = loginService.getUser(example);
		if(loginInfo == null){
			map.put(STATUS, STATUS_FAILED);
			map.put(MESSAGE, "账号或者密码错误");
			return map;
		}
		//md5（Ios，h5传过来就是MD5加密了）
		if("Android".equals(loginVO.getClientType()))
			password = md5Encrypt(password, mobile);
		
		if(!StringUtils.equals(password, loginInfo.getPassword())){
			map.put(STATUS, STATUS_FAILED);
			map.put(MESSAGE, "账号或者密码错误");
			return map;
		}
        
		User user = userService.getUser(loginInfo.getUserId());
		//更新登录时间并记录登录信息
		String token = loginService.updateLoginAndRecord(loginInfo, loginVO);
		
		//注册时绑定第三方账号
		if(loginVO.getType() != null && loginVO.getOpenId() != null){
			logger.info("已有账号时绑定第三方账号类型：" + loginVO.getType() + ",手机号：" + mobile + ",openId:" + loginVO.getOpenId());
			ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin(mobile, loginVO.getOpenId(), loginVO.getType(), loginVO.getMinaOpenId());
			thirdPartyLoginService.insert(thirdPartyLogin);
		}
		
		map.put("userId", user.getId());
		//用户前缀
		map.put("userCode", user.getUserCode());
		map.put("token", token);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 修改密码
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/changePassword.do", method = RequestMethod.POST)
	public Map<String, Object> setNewPassword(@RequestBody LoginVO loginVO) throws JSONException, IOException{
		logger.info(loginVO.toString());
		Map<String, Object> map = new HashMap<String, Object>();
        //RSA解密（ios暂时传过来的是md5加密密文）
		if("Android".equals(loginVO.getClientType())){
     	  loginVO.setOldPassword(decrypt(loginVO.getOldPassword()));
     	  loginVO.setNewPassword(decrypt(loginVO.getNewPassword()));
		}
        Login loginInfo = loginService.getLoginByUserId(loginVO.getUserId());
        
        //IOS已经是md5加密的
        String oldPassword = loginVO.getOldPassword();
        if("Android".equals(loginVO.getClientType()))
        	oldPassword = md5Encrypt(loginVO.getOldPassword(), loginInfo.getPhone());
		
        if(!StringUtils.equals(loginInfo.getPassword(), oldPassword)){
			map.put(STATUS, STATUS_FAILED);
			map.put(MESSAGE, "密码错误");
			return map;
		}
		//IOS已经是md5加密的
		String newPassword = loginVO.getNewPassword();
		if("Android".equals(loginVO.getClientType()))
			newPassword = md5Encrypt(loginVO.getNewPassword(), loginInfo.getPhone());
		
		loginInfo.setPassword(newPassword);
		loginService.updateLogin(loginInfo);
		map.put(STATUS, STATUS_SUCCESS);
		map.put(MESSAGE, "修改成功");
		return map;
	}
	
	/**
	 * 忘记密码重置密码
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/resetPassword.do", method = RequestMethod.POST)
	public Map<String, Object> resetPassword(@RequestBody LoginVO loginVO) throws JSONException, IOException{
		logger.info(loginVO.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		if(loginVO.getPassword() == null)
			throw new BusinessException("参数有误");
		
		Login loginInfo = resetValidate(loginVO);
		
		//IOS已经是md5加密的
		String newPassword = loginVO.getPassword();
		if("Android".equals(loginVO.getClientType())){
			newPassword = md5Encrypt(decrypt(newPassword), loginInfo.getPhone());
		}
		
		loginInfo.setPassword(newPassword);
		loginService.updateLogin(loginInfo);
		map.put(MESSAGE, "修改成功");
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 
	 * @param loginVO
	 * @return
	 */
	private Login resetValidate(LoginVO loginVO){
		logger.info(loginVO.toString());
		if(loginVO.getMobile() == null || loginVO.getCode() == null)
	        throw new BusinessException("参数有误");
		Login loginInfo = loginService.getLoginByPhone(loginVO.getMobile());
        if(loginInfo == null)
        	throw new BusinessException("该号码不存在");
        
        SmsCode smsCode = smsCodeService.getListByPhone(loginVO.getMobile());
        if(!smsCode.getCode().equals(loginVO.getCode()))
        	throw new BusinessException("验证码有误");
      
        //设置10分钟超时
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -10);
        if(cal.getTime().getTime() > smsCode.getTimestamp())
        	throw new BusinessException("验证码超时，请重新获取");
        
        return loginInfo;
	}
	

	/**
	 *  验证验证码是否正确
	 * @param loginVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validateCode.do", method = RequestMethod.POST)
	public Map<String, Object> validateCode(@RequestBody LoginVO loginVO){
		logger.info(loginVO.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		resetValidate(loginVO);
		result.put(STATUS, STATUS_SUCCESS);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCode/{userId}.do")
	public Map<String,Object> getCodeByUserId(@PathVariable Integer userId) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("edateNo", userService.getUser(userId).getUserCode());//返回易悦号
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 通过filter后转发的地址，给客户端返回错误信号
	 * author：kejian
	 */
	@ResponseBody
	@RequestMapping(value = "/error.do")
	public Map<String,Object> error(HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = (Integer) request.getAttribute("userId");
		map.put(MESSAGE, "验证失效，请重新登录！");
		if(userId != null){
			Token token = tokenService.getTokenByUserId(userId);
			switch (token.getResetType()) {
			case 1:
				map.put(MESSAGE, "您的账号在另外一台设备登陆！");
				break;
			case 2:
				map.put(MESSAGE, "您的资料审核没有通过，请重新填写资料！");
				break;
			case 3:
				map.put(MESSAGE, "系统升级，请更新版本重新登陆！");
				break;

			default:
				break;
			}
		}
		map.put(STATUS, STATUS_ERROR);
		map.put(MESSAGE_CODE, ResponseCode.INVALIDATED_TOKEN.getCode());
		return map;
	}
	
	/**
	 * 更新登录信息
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/updateLoginInfo.do", method = RequestMethod.POST)
	public Map<String, Object> updateLoginInfo(@RequestBody LoginVO loginVO) throws JSONException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		loginService.updateLoginByUserId(loginVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * app启动更新启动时间
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/updateBootUpTime.do", method = RequestMethod.POST)
	public Map<String, Object> updateBootUpTime(@RequestBody LoginVO loginVO) throws JSONException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info(loginVO.toString());
		loginService.updateBootUpTime(loginVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 用户登录
	 * @param loginVO
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/loginAndSave.do", method = RequestMethod.POST)
	public Map<String, Object> loginAndSave(@RequestBody LoginVO loginVO) throws JSONException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(loginVO.getOpenId() == null || loginVO.getType() == null
			|| loginVO.getCode() == null || loginVO.getMobile() == null){
			throw new BusinessException("参数有误");
		}
		
		//手机号跟验证码的验证
		if( !smsCodeService.isExist(loginVO.getMobile(), loginVO.getCode()) ){
			throw new BusinessException("验证码不存在,请重新输入");
		}
		
		//openId跟type
		ThirdPartyLogin thirdPartyLogin = thirdPartyLoginService.isExist(loginVO.getOpenId());
    	if(thirdPartyLogin != null && loginVO.getMobile().equals(thirdPartyLogin.getPhone())){
    		throw new BusinessException("该手机号已经绑定过其他账户了");
    	}
    	
		//判断手机是否注册了，没有就生成一个
    	User user = null;
    	Integer refuseStatus = 0;
    	if(!loginService.isExist(loginVO.getMobile())){
    		//根据手机号码生成一个
    		user = loginService.createUser(loginVO);
    	}else{
    		//判断是否黑名单或者拒绝状态
			Login loginInfo = loginService.getLoginByPhone(loginVO.getMobile());
			user = userService.getUser(loginInfo.getUserId());
    	}
    	
		//绑定
    	//更新登录时间并记录登录信息
    	String token = tokenService.insertOrUpdate(user.getId(), TokenConstants.TOKEN_INVALID_INSERT_NEW);
    	
		//注册时绑定第三方账号,被拒绝的不绑定
    	if(refuseStatus == 0){
			logger.info("已有账号时绑定第三方账号类型：" + loginVO.getType() + ",手机号：" + loginVO.getMobile() + ",openId:" + loginVO.getOpenId());
			thirdPartyLogin = new ThirdPartyLogin(loginVO.getMobile(), loginVO.getOpenId(), loginVO.getType(), loginVO.getMinaOpenId());
			thirdPartyLoginService.insert(thirdPartyLogin);
    	}
		
		map.put("userId", user.getId());
		//用户前缀
		map.put("imUserName", user.getUserCode());
		map.put("token", token);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
}
