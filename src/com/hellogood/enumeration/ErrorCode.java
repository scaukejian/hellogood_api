package com.hellogood.enumeration;

/**
 * 错误代码
 * Created by yanyuan on 15/11/21.
 */
public enum  ErrorCode {

    /**
     * code
     * 前两位
     *      11 : 用户-基本资料, 12 : 用户-照片, 13 :用户-视频, 14 : 用户-证件, 15 :用户-余额, 16 : 用户-抵用券,
     *      21 : 订单, 31 : 公益晚餐,41 : 邀请函, 51 : 排行榜
     *
     * 后两位 表示错误代码
     *
     */
    ACCOUNT_INSUFFICIENT("1501", "易悦币不足, 请充值");

    private String code;
    private String message;

    private ErrorCode(String code, String message) {
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
