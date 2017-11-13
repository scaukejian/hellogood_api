package com.hellogood.http.controller.mina;

import com.hellogood.http.controller.BaseController;
import com.hellogood.http.vo.MinaUserVO;
import com.hellogood.http.vo.RequestTemplateVO;
import com.hellogood.service.mina.MinaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序Controller
 * @author kejian
 *
 */
@Controller
@RequestMapping(value = "/mina")
public class MinaUserController extends BaseController{
	Logger logger = LoggerFactory.getLogger(MinaUserController.class);
	
	@Autowired
	private MinaUserService minaUserService;
	/**
	 * 解密用户敏感数据
	 *
	 * @param encryptedData
	 *            明文,加密数据
	 * @param iv
	 *            加密算法的初始向量
	 * @param code
	 *            用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取
	 *            unionId
	 * @param unionId （选填）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/isExist", method = RequestMethod.POST)
	public Map<String, Object> isExist(@RequestBody MinaUserVO userVO, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DATA, minaUserService.getMinaUserData(userVO));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	
	/**
	 * 发送微信模板消息
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "sendTemplate.do", method = RequestMethod.POST)
	public Map<String, Object> sendTemplate(@RequestBody RequestTemplateVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		minaUserService.sendTemplate(vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
}
