package com.hellogood.http.vo;

import com.hellogood.domain.Folder;
import com.hellogood.exception.BusinessException;
import com.hellogood.utils.BeaUtils;

import java.util.Date;

public class FolderVO {
    private Integer id;

    private String name;

    private Date createTime;

    private Integer validStatus;

    private Integer systemFolder;

    private Integer userId;

    private Date updateTime;

    private String ids;

    private Integer page;

    private Integer pageSize;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Integer getSystemFolder() {
        return systemFolder;
    }

    public void setSystemFolder(Integer systemFolder) {
        this.systemFolder = systemFolder;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void vo2Domain(Folder domain) {
        try {
            BeaUtils.copyProperties(domain, this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取文件夹信息失败");
        }
    }

    public void domain2Vo(Folder domain) {
        try {
            BeaUtils.copyProperties(this, domain);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取文件夹信息失败");
        }
    }

    @Override
    public String toString() {
        return "FolderVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", validStatus=" + validStatus +
                ", systemFolder=" + systemFolder +
                ", userId=" + userId +
                ", updateTime=" + updateTime +
                '}';
    }
}