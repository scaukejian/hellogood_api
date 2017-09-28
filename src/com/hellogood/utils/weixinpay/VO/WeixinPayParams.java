package com.hellogood.utils.weixinpay.VO;

public class WeixinPayParams {
	public String appid;
	public String noncestr;
	public String packageValue;
	public String partnerid;
	public String prepayid;
	public String signType;
	public String timestamp;
	public String sign;
	public String openid;
	public String paySign;
	
	
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPackageValue() {
		return packageValue;
	}
	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "WeixinPayParams [appid=" + appid + ", noncestr=" + noncestr
				+ ", packageValue=" + packageValue + ", partnerid=" + partnerid
				+ ", prepayid=" + prepayid + ", timestamp=" + timestamp
				+ ", sign=" + sign + ", openid=" + openid + ", signType="
				+ signType + ", paySign=" + paySign + "]";
	}
}
