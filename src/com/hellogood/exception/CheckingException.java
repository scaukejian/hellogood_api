package com.hellogood.exception;

import com.hellogood.enumeration.ErrorCode;

/**
 * 校验异常,返回错误代码
 * Created by kejian on 2017/11/21.
 */
public class CheckingException extends RuntimeException {

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CheckingException() {
        super();
    }
    public CheckingException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }


}
