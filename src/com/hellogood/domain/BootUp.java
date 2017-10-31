package com.hellogood.domain;

import java.util.Date;

public class BootUp {
    private Integer id;

    private String phoneUniqueCode;

    private Integer userId;

    private Date createTime;

    private Date updateTime;

    private String phoneClient;

    private String apkVersion;

    private String clientId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneUniqueCode() {
        return phoneUniqueCode;
    }

    public void setPhoneUniqueCode(String phoneUniqueCode) {
        this.phoneUniqueCode = phoneUniqueCode == null ? null : phoneUniqueCode.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPhoneClient() {
        return phoneClient;
    }

    public void setPhoneClient(String phoneClient) {
        this.phoneClient = phoneClient == null ? null : phoneClient.trim();
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion == null ? null : apkVersion.trim();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }
}