package com.hellogood.http.controller;

import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.UserVO;
import com.hellogood.service.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料Controller
 * @author kejian
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	/**
	 * 按id查询用户自己的资料
	 * @param userId
	 * @throws JSONException
	 * @throws IOException
	 * @author kejian
	 */
	@RequestMapping(value = "/getMyInfoById/{userId}.do")
	@ResponseBody
	public Map<String, Object> getMyInfoById(@PathVariable Integer userId, HttpServletRequest request) {
		logger.info("UserController userId:"+userId);
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.getUser(userId);
		
		if(user == null){
			map.put(STATUS, STATUS_ERROR);
			map.put(MESSAGE, "该用户不存在");
			return map;
		}
		
		tokenService.checkUserToken((String)request.getAttribute("token"), userId);
		
		Map<String, Object> userDetail = userService.getMyData(userId);

		map.put(DATA, userDetail);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 保存用户（完善资料）
	 * @param userVO
	 * @throws JSONException
	 * @throws IOException
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public Map<String, Object> save(@RequestBody UserVO userVO, HttpServletRequest request) throws JSONException, IOException {
		userVO.setToken((String)request.getAttribute("token"));
        logger.info("用户信息保存：" + userVO);
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			userService.save(userVO);
		}catch(BusinessException ex){
			ex.printStackTrace();
			throw ex;
		}catch(Exception ex){
			ex.printStackTrace();
			map.put(STATUS, STATUS_ERROR);//新增记录失败
			map.put(MESSAGE,"行业身份输入不能为表情");
			return map;
		}
		map.put(STATUS, STATUS_SUCCESS);
		return map;	
	}
}
