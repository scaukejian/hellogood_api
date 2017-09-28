package com.hellogood.enumeration;

/**
 * Created by kejian on 2017/10/28.
 */
public enum BaseDataType {

    INDUSTRY("industry", "行业"), JOB("job", "职位"),
    DEGREE("degree",  "学历"), FAMILY("family", "家庭情况"),
    ASSET("asset", "资产"),NATION("nation", "民族"),
    MARRY("marry", "婚姻情况"), OWNNESS("ownness", "个人状态"),
    SMS_CHANNEL("smsChannel", "短信渠道"), SMS_NOTICE("smsNotice", "短信通知"),
    GIVEN_TYPE("givenType", "打赏"), MOUTH_SALARY("mouthSalary", "月薪"),
    DATING_TOPIC("datingTopic", "约会主题"), INTEREST_TABS("interestTabs", "兴趣标签"), IOS_VERSION("iosVersion","ios版本号"),
    OFFICIAL_NICKNAME("officialNickname","官方私信称呼"), CUSTOMER_SERVICE("customerService", "在线客服"), CUSTOMER_DEFAULTCODE("customerDefaultCode", "易约客服默认号"),
    MOMENT_TYPE("momentType","随感类型"),IS_NEED_SEND_VIP("isNeedSendVip","是否需要赠送vip体验"),
    CLIENT_PRO_VERSION("clientProVersion","易悦精英客户端版本"),BIRTHDAY_MONEY("birthdayMoney","给生日用户的红包金额"),
    BIRTHDAY_TICKET_MONEY("birthdayTicketAmount","给生日用户的代金券金额"),BIRTHDAY_SWITCH("birthdaySwitch","是否需要发送生日礼包"),
    BIRTHDAY_USER_FRESH("birthdayUserFresh","是否刷新生日用户列表");
    
    private String code;

    private String name;

    BaseDataType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    /**
     * 查询枚举
     * @param code
     * @return
     */
    public static BaseDataType get(String code) {
        for (BaseDataType baseDataType : BaseDataType.values()) {
            if (baseDataType.getCode().equals(code)) {
                return baseDataType;
            }
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println(BaseDataType.get("degree"));
    }
}
