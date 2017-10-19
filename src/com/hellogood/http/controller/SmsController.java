package com.hellogood.http.controller;

import com.hellogood.domain.Login;
import com.hellogood.domain.SmsCode;
import com.hellogood.exception.BusinessException;
import com.hellogood.service.LoginService;
import com.hellogood.service.SmsCodeService;
import com.hellogood.utils.DateUtil;
import com.hellogood.utils.RegexUtils;
import com.hellogood.utils.SmsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	public static final String CODE_TYPE_REGISTER = "register";
	public static final String CODE_TYPE_FORGET = "forget";
	
	/**
	 * 获取短信验证码
	 * @param mobile
	 * @param type
	 */
	@ResponseBody
	@RequestMapping(value = "/code.do")
	public Map<String, Object> getRegisterCode(@RequestParam("mobile") String mobile, @RequestParam("type") String type) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(mobile))
			throw new BusinessException("手机号码不能为空");
		if (!RegexUtils.isMobileExact(mobile))
			throw new BusinessException(RegexUtils.PHONE_MSG);
		if (StringUtils.isBlank(type))
			throw new BusinessException("验证码类型不能为空，类型只能为register和forget");
		//验证是否发送过于频繁
		smsCodeService.validOneMinuteLimit(mobile);
		Login login = loginService.getLoginByPhone(mobile);
		switch (type) {
			case CODE_TYPE_REGISTER :
				if(login != null){
					result.put(STATUS, STATUS_FAILED);
					result.put(MESSAGE, "此号码已经注册！");
					return result;
				}
				break;
			case CODE_TYPE_FORGET :
				if(login == null){
					result.put(STATUS, STATUS_FAILED);
					result.put(MESSAGE, "此号码尚未注册！");
					return result;
				}
				break;
		}
		int code = (int) ((Math.random() * 9 + 1) * 100000);
		String content = "您的验证码是：" + code + "。请不要把验证码泄露给其他人。";
		try {
			//发送短信
			int status = SmsUtil.sendSMS(mobile, content, "");
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

}
