package com.hellogood.http.vo;

import java.util.Map;

public class RequestTemplateVO {
	private String touser;
	private String template_id;
	private String page;
	private String form_id;
	private Map<String, Template> data;
	private String emphasis_keyword;
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public Map<String, Template> getData() {
		return data;
	}

	public void setData(Map<String, Template> data) {
		this.data = data;
	}

	public String getEmphasis_keyword() {
		return emphasis_keyword;
	}

	public void setEmphasis_keyword(String emphasis_keyword) {
		this.emphasis_keyword = emphasis_keyword;
	}

}
