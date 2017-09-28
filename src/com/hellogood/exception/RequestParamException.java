package com.hellogood.exception;

/**
 * Created by yanyuan on 2016/11/16.
 */
public class RequestParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequestParamException() {
        super();
    }

    public RequestParamException(String message) {
        super(message);
    }

    public RequestParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParamException(Throwable cause) {
        super(cause);
    }
}
