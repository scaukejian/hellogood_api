package com.hellogood.exception;

/**
 * 自定义异常类
 * @author fukangwen
 * @version 1.0
 * @date 2016-02-18
 *
 */
public class UserOperateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserOperateException() {
		super();
	}

	public UserOperateException(String message) {
		super(message);
	}

	public UserOperateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserOperateException(Throwable cause) {
		super(cause);
	}

}
