package com.hellogood.http.vo;

import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.utils.BeaUtils;

import java.util.Date;

public class MinaUserVO extends BaseVO<User>{
   
	private Integer userId;

    private String openId;
    
    private String unionId;
    private String token;
    
    private String encryptedData;
    private String iv;
    private String code;

	private String userName;

	private String sex;

	private String phone;

	private Date birthday;

	private String characteristicSignature;

	private String imgUrl;

	private String userCode;

	private String qRCodeUrl;

	private String company;

	private String job;

	private String email;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getqRCodeUrl() {
		return qRCodeUrl;
	}

	public void setqRCodeUrl(String qRCodeUrl) {
		this.qRCodeUrl = qRCodeUrl;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCharacteristicSignature() {
		return characteristicSignature;
	}

	public void setCharacteristicSignature(String characteristicSignature) {
		this.characteristicSignature = characteristicSignature;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public void domain2Vo(User domain) {
		try {
			BeaUtils.copyProperties(this, domain);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取用户信息失败");
		}
	}
	@Override
	public void vo2Domain(User domain) {
		try {
			BeaUtils.copyProperties(domain, this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取用户信息失败");
		}
	}
    
}