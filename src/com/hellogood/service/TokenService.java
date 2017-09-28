package com.hellogood.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.hellogood.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellogood.domain.Token;
import com.hellogood.domain.TokenExample;
import com.hellogood.mapper.TokenMapper;

/**
 * Token操作@Service
 * @author kejian
 *
 */
@Service
public class TokenService {
	@Autowired
	private TokenMapper tokenMapper;
	
	/**
	 * 生成token
	 * @param userId
	 * @return
	 */
	public String getToken(Integer userId){
		return UUID.randomUUID()+"_"+userId +"_" + new Date().getTime();
	}
	
	/**
	 * 生成token
	 * @param userId
	 * @return
	 */
	public String getTokenBySource(String source){
		return UUID.randomUUID()+"_"+source +"_" + new Date().getTime();
	}
	

	/**
	 * 获取token
	 * @param userId
	 * @return
	 */
	public Token getTokenByUserId(Integer userId){
		TokenExample example = new TokenExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.isEmpty())
			return null;
		return tokenList.get(0);
	}
	
	/**
	 * 新增或者更新token数据表
	 * @param userId
	 */
	public String insertOrUpdate(Integer userId, Integer resetType){
		String tokenStr = getToken(userId);
		TokenExample example = new TokenExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.size() < 1){
			Token token = new Token();
			token.setUserId(userId);
			token.setTime(new Date());
			token.setToken(tokenStr);
			tokenMapper.insert(token);
		}else{
			Token token = tokenList.get(0);
			token.setPreToken(token.getToken());
			token.setToken(tokenStr);
			token.setTime(new Date());
			token.setResetType(resetType);
			tokenMapper.updateByPrimaryKey(token);
		}
		return tokenStr;
	}
	

	/**
	 * 验证token跟userId是否一致
	 * @param userId
	 * @param tokenStr
	 * @return
	 */
	private Boolean tokenAndUserIdValidate(String tokenStr, Integer userId){
		TokenExample example = new TokenExample();
		example.createCriteria().andTokenEqualTo(tokenStr).andUserIdEqualTo(userId);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.size() < 1)
			return false;
		return true;
	}

	/**
	 * 验证token跟userId是否一致
	 * @param userId
	 * @param tokenStr
	 * @return
	 */
	public void checkUserToken(String tokenStr, Integer userId){
		if(!tokenAndUserIdValidate(tokenStr, userId)){
			throw new BusinessException("非法请求");
		}
	}
	
	/**
	 * 验证token是否可用
	 * @param userId
	 * @param tokenStr
	 * @return
	 */
	public Token getToken(String tokenStr){
		TokenExample example = new TokenExample();
		example.createCriteria().andTokenEqualTo(tokenStr);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.size() < 1)
			return null;
		return tokenList.get(0);
	}

	public String insertOrUpdateBySource(String source) {
		String tokenStr = getTokenBySource(source);
		TokenExample example = new TokenExample();
		example.createCriteria().andSourceEqualTo(source);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.isEmpty()){
			Token token = new Token();
			token.setSource(source);
			token.setTime(new Date());
			token.setToken(tokenStr);
			tokenMapper.insert(token);
		}else{
			tokenStr = tokenList.get(0).getToken();
		}
		return tokenStr;
	}

	public Token getPreToken(String tokenStr) {
		TokenExample example = new TokenExample();
		example.createCriteria().andPreTokenEqualTo(tokenStr);
		List<Token> tokenList = tokenMapper.selectByExample(example);
		if(tokenList == null || tokenList.isEmpty())
			return null;
		return tokenList.get(0);
	}
}
