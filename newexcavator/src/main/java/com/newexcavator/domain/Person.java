package com.newexcavator.domain;

import java.util.Date;

public class Person {

	private Integer id;
	private Integer user_id;
	private Integer m_type;
	private String type_name;
	private Float salary;
	private Integer place_p;
	private String p_name;
	private Integer place_c;
	private String c_name;
	private String job;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getM_type() {
		return m_type;
	}
	public void setM_type(Integer m_type) {
		this.m_type = m_type;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public Float getSalary() {
		return salary;
	}
	public void setSalary(Float salary) {
		this.salary = salary;
	}
	public Integer getPlace_p() {
		return place_p;
	}
	public void setPlace_p(Integer place_p) {
		this.place_p = place_p;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public Integer getPlace_c() {
		return place_c;
	}
	public void setPlace_c(Integer place_c) {
		this.place_c = place_c;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink_name() {
		return link_name;
	}
	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Integer getClose() {
		return close;
	}
	public void setClose(Integer close) {
		this.close = close;
	}
	public Date getClose_time() {
		return close_time;
	}
	public void setClose_time(Date close_time) {
		this.close_time = close_time;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Date getDeleted_time() {
		return deleted_time;
	}
	public void setDeleted_time(Date deleted_time) {
		this.deleted_time = deleted_time;
	}
	public Integer getVerify() {
		return verify;
	}
	public void setVerify(Integer verify) {
		this.verify = verify;
	}
	private String description;
	private String link_name;
	private String phone;
	private Date create_time;
	private Integer close;
	private Date close_time;
	private Integer deleted;
	private Date deleted_time;
	private Integer verify;
}
