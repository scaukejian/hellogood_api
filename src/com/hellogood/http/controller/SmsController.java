package com.hellogood.http.controller;

import com.hellogood.domain.Login;
import com.hellogood.domain.SmsCode;
import com.hellogood.exception.BusinessException;
import com.hellogood.service.LoginService;
import com.hellogood.service.SmsCodeService;
import com.hellogood.service.UserService;
import com.hellogood.utils.DateUtil;
import com.hellogood.utils.SmsUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送控制类
 * @author kejian
 */
@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController{
	@Autowired
	private LoginService loginService;
	@Autowired
	private SmsCodeService smsCodeService;
	@Autowired
	private UserService userService;
	
	public static final String CODE_TYPE_REGISTER = "register";
	public static final String CODE_TYPE_FORGET = "forget";
	
	/**
	 * 获取短信验证码
	 * @param mobile
	 * @param type
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/code.do")
	public Map<String, Object> getRegisterCode(@RequestParam("mobile") String mobile, @RequestParam("type") String type){
		Map<String, Object> result = new HashMap<String, Object>();
		Login login = null;
		String content = null;
		int status = 0;
		int code = (int) ((Math.random()*9+1)*100000);
		//验证是否发送过于频繁
		smsCodeService.validOneMinuteLimit(mobile);
		switch(type){
			case CODE_TYPE_REGISTER :
				login = loginService.getLoginByPhone(mobile);

				if(login != null){
					result.put(STATUS, STATUS_FAILED);
					result.put(MESSAGE, "此号码已经注册！");
					return result;
				}
				break;
			case CODE_TYPE_FORGET :
				login = loginService.getLoginByPhone(mobile);
				if(login == null){
					result.put(STATUS, STATUS_FAILED);
					result.put(MESSAGE, "此号码尚未注册！");
					return result;
				}
				break;
		}
		content = "您的验证码是：" + code + "。请不要把验证码泄露给其他人。";
		try {
			//发送短信
			status = SmsUtil.sendSMS(mobile, content, "");
			//存库
			SmsCode sms = new SmsCode();
			sms.setCode(String.valueOf(code));
			sms.setStatus(status);
			sms.setTimestamp(DateUtil.getTime());
			sms.setPhone(mobile);
			smsCodeService.saveToRedis(sms, content);
			result.put("timestamp", sms.getTimestamp());
			result.put(STATUS, STATUS_SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "发送验证码失败! ");
		}
		return result;
	}
	
	/**
	 * 获取忘记密码短信验证码
	 * @param mobile
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getForgetCode.do")
	public Map<String, Object> getForgetCode(@RequestParam("mobile") String mobile){
		Map<String, Object> result = new HashMap<String, Object>();
		Login login = loginService.getLoginByPhone(mobile);
		
		if(login == null){
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "此号码还未注册！");
			return result;
		}
		
		int status = 0;
		int code = (int) ((Math.random()*9+1)*100000);
		String content = "您的验证码是：" + code + "。请不要把验证码泄露给其他人。";
		
		//验证是否发送过于频繁
		smsCodeService.validOneMinuteLimit(mobile);
		
		try {
			//发送短信
			status = SmsUtil.sendSMS(mobile, content, "");
			//存库
			SmsCode sms = new SmsCode();
			sms.setCode(String.valueOf(code));
			sms.setStatus(status);
			sms.setTimestamp(DateUtil.getTime());
			sms.setPhone(mobile);
			smsCodeService.saveToRedis(sms, content);
			result.put("timestamp", sms.getTimestamp());
			result.put(STATUS, STATUS_SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "发送验证码失败! ");
		}
		return result;
	}
	

	/**
	 * 获取忘记密码短信验证码
	 * @param mobile
	 * @throws JSONException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getCode.do")
	public Map<String, Object> getCode(@RequestParam("mobile") String mobile){
		Map<String, Object> result = new HashMap<String, Object>();
		
		int status = 0;
		int code = (int) ((Math.random()*9+1)*100000);
		String content = "您的验证码是：" + code + "。请不要把验证码泄露给其他人。";
		
		//验证是否发送过于频繁
		smsCodeService.validOneMinuteLimit(mobile);
		
		try {
			//发送短信
			status = SmsUtil.sendSMS(mobile, content, "");
			//存库
			SmsCode sms = new SmsCode();
			sms.setCode(String.valueOf(code));
			sms.setStatus(status);
			sms.setTimestamp(DateUtil.getTime());
			sms.setPhone(mobile);
			smsCodeService.saveToRedis(sms, content);
			result.put("isExist", userService.isExistByPhone(mobile));
			result.put("timestamp", sms.getTimestamp());
			result.put(STATUS, STATUS_SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put(STATUS, STATUS_FAILED);
			result.put(MESSAGE, "发送验证码失败! ");
		}
		return result;
	}

}
