package com.hellogood.service;

import com.hellogood.constant.Code;
import com.hellogood.domain.UserPhoto;
import com.hellogood.domain.UserPhotoExample;
import com.hellogood.mapper.UserPhotoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户相册Service
 * @author kejian
 *
 */
@Service
public class UserPhotoService {
	@Autowired
	private UserPhotoMapper userPhotoMapper;

	/**
	 * 获取别人的形象照,isThumbnail是否为缩略图 
	 * @param userId
	 * @param isThumbnail
	 * @return
	 */
	public String getUserPhotoName(Integer userId, Boolean isThumbnail){
		UserPhotoExample example = new UserPhotoExample();
		example.createCriteria().andUserIdEqualTo(userId).andHeadFlagEqualTo(Code.STATUS_VALID);
		example.setOrderByClause("update_time desc");
		List<UserPhoto> userPhotoList = userPhotoMapper.selectByExample(example);
		if(!userPhotoList.isEmpty()){
			if(isThumbnail) return userPhotoList.get(0).getThumbnailImgName();
			return userPhotoList.get(0).getImgName();
		}
		return null;
	}
	
}
