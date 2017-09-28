package com.hellogood.domain;

import java.util.Date;

public class Token {
    private Integer id;

    private String source;

    private Integer userId;

    private String token;

    private Date time;

    private Integer resetType;

    private String preToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getResetType() {
        return resetType;
    }

    public void setResetType(Integer resetType) {
        this.resetType = resetType;
    }

    public String getPreToken() {
        return preToken;
    }

    public void setPreToken(String preToken) {
        this.preToken = preToken == null ? null : preToken.trim();
    }
}