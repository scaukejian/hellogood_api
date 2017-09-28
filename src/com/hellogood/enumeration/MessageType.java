package com.hellogood.enumeration;

/**
 * Created by kejian on 17/9/23.
 */
public enum MessageType {

   SMS_CODE(300, "验证码");

    private Integer code;

    private String label;

    MessageType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

}
