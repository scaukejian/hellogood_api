package com.hellogood.constant;

public enum ResponseCode {

	SUCCESS("100", "成功"), AUTH_FAILED("101", "认证失败"),INVALID_CAPTCHA("103",
			"验证码非法"), CERTIFICATE_FAILED("104", "产生TOKEN失败"), INVALIDATED_TOKEN("105", "TOKEN不合法"), INVALIDATED_PASSWORD(
			"106", "密码不正确"), PASSWORD_REQUIRED("107", "密码必填"),INSERT_DB_FAILED("108","写入数据库失败"),NOT_FOUNT_USER_APPLY("109","未找到用户申请"),
			USERINFO_REQUIRED("110", "资料未完善"),USERINFO_NO_PASS("114", "资料未填写或者未通过"),USERINFO_IDCARD("111", "身份证未上传"),
			USERINFO_HEAD_PHOTO_NO_EXIST("112", "头像不存在"),USERINFO_SEX_EMPTY("000","性别不存在"),
			USER_NOT_FOUND("301", "未找到用户"), DUPLICATE_PHONE_NUMBER(
			"302", "手机号码重复"), UNRELIABLE_PHONE_NUMBER("303", "不能信赖的手机号码"), USER_ACCOUNT_MODIFY_RESTRICTED("304", "用户帐号不可以修改"), DUPLICATE_USER_ACCOUNT(
			"305", "用户帐号重复"), USER_NAME_REQUIRED("306", "用户昵称必填"),
			USERINFO_REFUSE_ZERO("1000", "资料相片视频不合法");

	private String  code;

	private String message;

	private ResponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
