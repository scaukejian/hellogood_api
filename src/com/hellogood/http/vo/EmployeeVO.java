package com.hellogood.http.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hellogood.domain.Employee;
import com.hellogood.exception.BusinessException;
import com.hellogood.utils.BeaUtils;

/**
 * 
 * @author kejian time 2017-3-10
 *
 */
public class EmployeeVO implements Serializable {

	private static final long serialVersionUID = 3889383137451410629L;
	private Long id;
	private String name;
	private String account;
	private String password;
	private String position;// 职务
	private String email;
	private String mobilePhone;
	private Integer city;
	private Integer status;// 员工状态
	private String telephone;// 固定电话
	private String organizationId;
	private String nation;// 民族
	private Date createTime;
	private String userCode;
	private List<String> role;// 用户角色名
	
	private Long defaultId;
	
	private String defaultUserCode;

	public Long getDefaultId() {
		return defaultId;
	}

	public void setDefaultId(Long defaultId) {
		this.defaultId = defaultId;
	}

	public String getDefaultUserCode() {
		return defaultUserCode;
	}

	public void setDefaultUserCode(String defaultUserCode) {
		this.defaultUserCode = defaultUserCode;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	private Date updatetime;


	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
    }

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void vo2Domain(Employee domain) {
		try {
			BeaUtils.copyProperties(domain, this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取员工信息失败");
		}
	}

	public void domain2Vo(Employee domain) {
		try {
			BeaUtils.copyProperties(this, domain);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("获取员工信息失败");
		}
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Override
	public String toString() {
		return "EmployeeVO [id=" + id + ", name=" + name + ", account=" + account + ", password=" + password
				+ ", position=" + position + ", email=" + email + ", mobilePhone=" + mobilePhone + ", city=" + city
				+ ", status=" + status + ", telephone=" + telephone + ", organizationId=" + organizationId + ", nation="
				+ nation + ", createTime=" + createTime + ", userCode=" + userCode + ", role=" + role + ", updatetime="
				+ updatetime + "]";
	}

}