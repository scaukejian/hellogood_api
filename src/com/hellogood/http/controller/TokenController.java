package com.hellogood.http.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hellogood.constant.TokenConstants;
import com.hellogood.service.TokenService;

/**
 * Token Controller
 * @author fukangwen
 *
 */
@Controller
@RequestMapping(value = "/token")
public class TokenController extends BaseController{

	@Autowired
	private TokenService tokenService;


	/**
	 * @Description: 重置token
	 * @param @param request
	 * @param @param response
	 * @author fukangwen
	 * @throws IOException 
	 */
	@RequestMapping(value = "/reset/{userId}.do")
	@ResponseBody
	public Map<String, Object> resetToken(@PathVariable Integer userId) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		tokenService.insertOrUpdate(userId,TokenConstants.TOKEN_INVALID_NOT_PASS);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	

}
