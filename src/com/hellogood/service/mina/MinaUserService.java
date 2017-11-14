package com.hellogood.service.mina;

import com.google.gson.Gson;
import com.hellogood.constant.Code;
import com.hellogood.constant.TokenConstants;
import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.MinaUserVO;
import com.hellogood.http.vo.RequestTemplateVO;
import com.hellogood.http.vo.UserVO;
import com.hellogood.service.*;
import com.hellogood.service.redis.RedisCacheManger;
import com.hellogood.utils.AesCbcUtil;
import com.hellogood.utils.HttpRequest;
import com.hellogood.utils.StaticFileUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序Service
 * @author kejian
 *
 */
@Service
public class MinaUserService {

	public static Logger logger = LoggerFactory.getLogger(MinaUserService.class);
	@Autowired
	private ThirdPartyLoginService thirdPartyLoginService;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RedisCacheManger redisCacheManger;
	 //小程序唯一标识   (在微信小程序管理后台获取)
    public static String wxspAppid = StaticFileUtil.getProperty("mini", "weixin_mina_appid");
    //小程序的 app secret (在微信小程序管理后台获取)
    public static String wxspSecret = StaticFileUtil.getProperty("mini", "weixin_mina_secret");
    //授权（必填）
    public static String grant_type = StaticFileUtil.getProperty("mini", "weixin_mina_grant_type");

    /**
	 * @param userVO
	 */
	public MinaUserVO getMinaUserData(MinaUserVO userVO) {
		//1.基础验证
		commonCheck(userVO);

		//2、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		JSONObject json = this.requestOpenIdAndSessionKey(userVO.getCode());
		//获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		//用户的唯一标识（openid）
		String openId = (String) json.get("openid");
		String name = null;
		String imgUrl = null;
		//String unionId = null;
		JSONObject userInfoJSON = null;
		//3、对encryptedData加密数据进行AES解密
		try {
			String result = AesCbcUtil.decrypt(userVO.getEncryptedData(), session_key, userVO.getIv(), "UTF-8");
			logger.info(result);

			if (null != result && result.length() > 0) {
				userInfoJSON = new JSONObject(result);
				if(userInfoJSON.get("nickName") == null)
					throw new BusinessException("获取用户信息失败");

				name = (String)userInfoJSON.get("nickName");
				imgUrl = (String)userInfoJSON.get("avatarUrl");
				//3用户的唯一标识（unionid）
				//unionId = (String)userInfoJSON.get("unionId");
			}else{
				throw new BusinessException("用户数据解密失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("用户数据解密失败");
		}
		//4.如果用户不存在，新增
		User user = userService.getUserByOpenIdForUpdate(openId);

		if(user == null){
			user = userService.insert(openId, name, imgUrl);
		}

		if(user.getValidStatus() == Code.STATUS_INVALID){
			throw new BusinessException("你已经被加入黑名单");
		}

		//更新登录时间并记录登录信息
		String token = tokenService.insertOrUpdate(user.getId(), TokenConstants.TOKEN_INVALID_INSERT_NEW);

		userVO.setUserId(user.getId());
		userVO.setToken(token);
		userVO.setBirthday(user.getBirthday());
		userVO.setCharacteristicSignature(user.getCharacteristicSignature());
		userVO.setImgUrl(user.getImgUrl());
		userVO.setOpenId(openId);
		userVO.setPhone(user.getPhone());
		userVO.setSex(user.getSex());
		userVO.setUserName(user.getUserName());
		//userVO.setUnionId(unionId);

        return userVO;
	}

	/**
	 * 向微信服务器 使用登录凭证 code session_key
	 * @param code
	 * @return
	 */
	public JSONObject requestOpenIdAndSessionKey(String code){

	    String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = new JSONObject(sr);
        if(json.get("session_key") == null)
        	throw new BusinessException("通信出了点小问题");

        return json;
	}

	/**
	 * 基础验证
	 * @param minaUserVO
	 */
	private void commonCheck(MinaUserVO minaUserVO) {
		//登录凭证不能为空
		if(StringUtils.isEmpty(minaUserVO.getCode()) ||
				StringUtils.isEmpty(minaUserVO.getEncryptedData()) ||
				StringUtils.isEmpty(minaUserVO.getIv())){
		   throw new BusinessException("登录凭证不能为空");
		}
	}

	/**
	 * @param vo
	 */
	public void sendTemplate(RequestTemplateVO vo) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + this.getAccessToken();
		Gson gson = new Gson();
		//发送请求
        String sr = HttpRequest.sendPost(url, gson.toJson(vo));
        //解析相应内容（转换成json对象）
        JSONObject returnJson = new JSONObject(sr);
        logger.info(returnJson.toString());

	}

	 /**
	 * 按类型缓存
	 * @refresh 是否刷新缓存,如果第一页，则立马刷新
	 * @return
	 */
	public String getAccessToken() {
		String accessToken = "EDATE_MINA_ACCESS_TOKEN";
		//是否刷新缓存
		String jsonStr = redisCacheManger.getRedisCacheInfo(accessToken);
		if(StringUtils.isNotBlank(jsonStr)){
			return jsonStr;
		}else {
			jsonStr = this.getAccessTokenRequest();
			redisCacheManger.setRedisCacheInfo(accessToken, RedisCacheManger.REDIS_CACHE_HOUR_EXPIRE_DEFAULT, jsonStr);
		}
		return jsonStr;
	}

	/**
	 *
	 * @return
	 */
	private String getAccessTokenRequest() {
		String params = "grant_type=client_credential&appid=" + wxspAppid + "&secret=" + wxspSecret;
		//发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
        //解析相应内容（转换成json对象）
        JSONObject json = new JSONObject(sr);
        if(json.get("access_token") == null)
        	throw new BusinessException("通信出了点小问题");
        return json.get("access_token").toString() ;
	}
}
