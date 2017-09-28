package com.hellogood.enumeration;

/**
 * Created by yanyuan on 2017/4/19.
 */
public enum ApiName {
    LOGIN("login", "登录"),
    UPDATE_PASSWORD("updatePassword", "修改密码"),
    TICKET_PAGE_QUERY("TICKET_PAGE_QUERY", "券分页查询"),
    TICKET_GET("TICKET_GET", "获取券"),
    CUSTOMER_SERVICE_MENU_QUERY_ALL_MENU("CustomerServiceMenuController.queryAllMenu", "获取易悦客服菜单"),
    CONTENT_COLLECTION_GET("ContentCollectionController.get", "获取合辑详情"),
    MOMENT_LIST_INTERACTION("MomentController.listInteraction", "互动消息列表");
    private String code;
    private String desc;

    ApiName(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
