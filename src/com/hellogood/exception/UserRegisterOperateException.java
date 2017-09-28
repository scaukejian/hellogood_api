package com.hellogood.exception;

/**
 * 自定义异常类
 * @author kejian
 * @version 1.0
 * @date 2017-09-14
 *
 */
public class UserRegisterOperateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserRegisterOperateException() {
		super();
	}

	public UserRegisterOperateException(String message) {
		super(message);
	}

	public UserRegisterOperateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserRegisterOperateException(Throwable cause) {
		super(cause);
	}

}
