package com.hellogood.service;

import com.hellogood.constant.Code;
import com.hellogood.domain.ThirdPartyLogin;
import com.hellogood.domain.ThirdPartyLoginExample;
import com.hellogood.domain.ThirdPartyLoginExample.Criteria;
import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.exception.RequestParamException;
import com.hellogood.http.vo.LoginVO;
import com.hellogood.mapper.ThirdPartyLoginMapper;
import com.hellogood.domain.ThirdPartyLogin;
import com.hellogood.mapper.ThirdPartyLoginMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Service 第三方登陆接口服务类
 * @author kejian
 *
 */
@Service
public class ThirdPartyLoginService {
	@Autowired
	private ThirdPartyLoginMapper thirdPartyLoginMapper;
	@Autowired
	private UserService userService;
	
	/**
	 * 新增记录
	 */
	public void insert(ThirdPartyLogin domain){
		thirdPartyLoginMapper.insert(domain);
	}
	
	/**
	 * 判断是否已经绑定了,如果没有记录返回null,else返回一个实体对象
	 * @param openId
	 * @return
	 */
	public ThirdPartyLogin isExist(String openId){
		ThirdPartyLoginExample example = new ThirdPartyLoginExample();
		example.createCriteria().andOpenIdEqualTo(openId).andValidStatusEqualTo(Code.STATUS_VALID);
		List<ThirdPartyLogin> list = thirdPartyLoginMapper.selectByExample(example);
		if(list.isEmpty())
			return null;
		return list.get(0);
	}
	/**
	 * 判断手机号码是否绑定过该类型的第三方
	 * @param type
	 * @param phone
	 * @return
	 */
	public Boolean isPhoneAndTypeExist(String phone, String type){
		ThirdPartyLoginExample example = new ThirdPartyLoginExample();
		example.createCriteria().andPhoneEqualTo(phone).andTypeEqualTo(type).andValidStatusEqualTo(Code.STATUS_VALID);
		List<ThirdPartyLogin> list = thirdPartyLoginMapper.selectByExample(example);
		if(list.isEmpty())
			return false;
		return true;
	}
	
	/**
	 * 返回
	 * @param userId
	 * @return
	 */
	public Map<String, Boolean> getStatus(Integer userId) {
		User user = userService.getUser(userId);
		ThirdPartyLoginExample example = new ThirdPartyLoginExample();
		example.createCriteria().andPhoneEqualTo(user.getPhone()).andValidStatusEqualTo(Code.STATUS_VALID);
		List<ThirdPartyLogin> list = thirdPartyLoginMapper.selectByExample(example);
		Map<String, Boolean> map = new HashMap<String, Boolean>(){
			{
				put(Type.WEIXIN.toString(), false);
				put(Type.WEIBO.toString(), false);
				put(Type.LINKEDIN.toString(), false);
			}
		};
		
		for(ThirdPartyLogin domain : list){
			if(Type.WEIXIN_UNION.toString().equals(domain.getType())){
				map.put(Type.WEIXIN.toString(), true);
				continue;
			}
			map.put(domain.getType(), true);
		}
		return map;
	}
	
	public enum Type{
		WEIXIN,WEIBO,LINKEDIN,WEIXIN_UNION;
		
		public static Boolean isContain(String type){
			Boolean isContain = false;
			if(type == null)
				return isContain;
			for(Type record : Type.values()){
				if(type.equals(record.toString())){
					isContain = true;
					break;
				}
			}
			return isContain;
		}
	}

	/**
	 * 解绑该用户该类型的第三方绑定
	 * @param userId
	 * @param type
	 */
	public void unbinding(Integer userId, String type) {
		if(userId == null || !Type.isContain(type))
			throw new RequestParamException("参数错误");
		
		User user = userService.getUser(userId);
		if(user == null)
			return;
		List<ThirdPartyLogin> list = this.getByTypeAndPhone(type, user.getPhone());
		for(ThirdPartyLogin domain : list){
			domain.setValidStatus(Code.STATUS_INVALID);
			domain.setUnbindingTime(new Date());
			thirdPartyLoginMapper.updateByPrimaryKey(domain);
		}
		
	}
	/**
	 * 绑定第三方
	 * @param loginVO
	 */
	public void binding(LoginVO loginVO) {

		if(loginVO.getOpenId() == null || loginVO.getType() == null){
	    	throw new RequestParamException("参数错误");
	    }

		User user = userService.getUser(loginVO.getUserId());
		
		if(user == null)
			throw new RequestParamException("参数错误");
		
		//如果手机号码或者账号已经存在
		ThirdPartyLogin domain = isExist(loginVO.getOpenId());
		if(domain != null)
			throw new BusinessException("第三方账号已经绑定过其他账号");
		
		if(isPhoneAndTypeExist(user.getPhone(), loginVO.getType()))
			throw new BusinessException("账号已经绑定其他第三方了");
		
		//绑定第三方账号
		ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin(user.getPhone(), loginVO.getOpenId(), loginVO.getType(), loginVO.getMinaOpenId());
		this.insert(thirdPartyLogin);

	}

	/**
	 * 
	 * @param type
	 * @param phone
	 * @return
	 */
	public List<ThirdPartyLogin> getByTypeAndPhone(String type, String phone){
		ThirdPartyLoginExample example = new ThirdPartyLoginExample();
		Criteria criteria = example.createCriteria();
		criteria.andPhoneEqualTo(phone).andValidStatusEqualTo(Code.STATUS_VALID);
		if(Type.WEIXIN.toString().equals(type)){
			criteria.andTypeIn(new ArrayList<String>(){
				{
					add(Type.WEIXIN.toString());
					add(Type.WEIXIN_UNION.toString());
				}
			});
		}else{
			criteria.andTypeEqualTo(type);
		}
		example.setOrderByClause("create_time desc");
		
		List<ThirdPartyLogin> list = thirdPartyLoginMapper.selectByExample(example);
		return list;
	}

	public void update(String unionId, String minaOpenId) {
		ThirdPartyLogin thirdPartyLogin = this.isExist(unionId);
		if(thirdPartyLogin != null && StringUtils.isBlank(thirdPartyLogin.getMinaOpenId())){
			thirdPartyLogin.setMinaOpenId(minaOpenId);
			thirdPartyLoginMapper.updateByPrimaryKey(thirdPartyLogin);
		}
	}

	/**
	 * 根据用户获取minaOpenId
	 * @param userId
	 */
	public String getMinaOpenId(Integer userId) {
		User user = userService.getUser(userId);
		if(user == null)
			throw new BusinessException("获取用户信息失败");
		List<ThirdPartyLogin> list = this.getByTypeAndPhone(Type.WEIXIN_UNION.toString(), user.getPhone());
		if(list.isEmpty())
			throw new BusinessException("获取用户信息失败");
		
		return list.get(0).getMinaOpenId();
				
	}
}
