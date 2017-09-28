package com.hellogood.http.vo;

import java.lang.reflect.InvocationTargetException;

import com.hellogood.domain.Login;
import com.hellogood.utils.BeaUtils;

public class LoginVO extends BaseVO<Login>{
	private String mobile;
	private String password;
	private String oldPassword;
	private String newPassword;
	private Integer userId;
    private String clientType;
    private String apkVersion;
    private String clientInfo;
    private String loginIp;
    private String loginAddr;

    private String type;
    private String openId;
    private String code;
    private Integer shareTicketId;
    private String minaOpenId;
    private Integer businessChannelId;
    
	public Integer getBusinessChannelId() {
		return businessChannelId;
	}
	public void setBusinessChannelId(Integer businessChannelId) {
		this.businessChannelId = businessChannelId;
	}
	public String getMinaOpenId() {
		return minaOpenId;
	}
	public void setMinaOpenId(String minaOpenId) {
		this.minaOpenId = minaOpenId;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginAddr() {
		return loginAddr;
	}
	public void setLoginAddr(String loginAddr) {
		this.loginAddr = loginAddr;
	}
	public Integer getShareTicketId() {
		return shareTicketId;
	}
	public void setShareTicketId(Integer shareTicketId) {
		this.shareTicketId = shareTicketId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getApkVersion() {
		return apkVersion;
	}
	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}
	public String getClientInfo() {
		return clientInfo;
	}
	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
    
    
	@Override
	public String toString() {
		return "LoginVO [mobile=" + mobile + ", password=" + password
				+ ", oldPassword=" + oldPassword + ", newPassword="
				+ newPassword + ", userId=" + userId + ", clientType="
				+ clientType + ", apkVersion=" + apkVersion + ", clientInfo="
				+ clientInfo + "]";
	}
	@Override
	public void domain2Vo(Login t) {
		try {
			BeaUtils.copyProperties(this, t);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void vo2Domain(Login t) {
		try {
			BeaUtils.copyProperties(t, this);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
    
    
}
