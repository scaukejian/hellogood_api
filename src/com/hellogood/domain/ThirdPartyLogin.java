package com.hellogood.domain;

import com.hellogood.constant.Code;

import java.util.Date;

public class ThirdPartyLogin {
    private Integer id;

    private String phone;

    private String openId;

    private String type;

    private Date createTime;

    private Integer validStatus;

    private Date unbindingTime;

    private String minaOpenId;

    public ThirdPartyLogin(){
    }

    public ThirdPartyLogin(String phone,  String openId, String type, String minaOpenId){
        this.phone = phone;
        this.openId = openId;
        this.type = type;
        this.validStatus = Code.STATUS_VALID;
        this.createTime = new Date();
        this.minaOpenId = minaOpenId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Date getUnbindingTime() {
        return unbindingTime;
    }

    public void setUnbindingTime(Date unbindingTime) {
        this.unbindingTime = unbindingTime;
    }

    public String getMinaOpenId() {
        return minaOpenId;
    }

    public void setMinaOpenId(String minaOpenId) {
        this.minaOpenId = minaOpenId == null ? null : minaOpenId.trim();
    }
}