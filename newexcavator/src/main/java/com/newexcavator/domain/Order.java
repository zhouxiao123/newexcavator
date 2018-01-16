package com.newexcavator.domain;

import java.util.Date;

public class Order {

	private Integer id;
	private Integer user_id;
	private Integer co_id;
	private Integer co_point;
	private String name;
	private String phone;
	private String address;
	private Date create_time;
	private Date over_time;
	private Integer status;
	private String co_name;
	private String co_description;
	private String co_path;
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
	public Integer getCo_id() {
		return co_id;
	}
	public void setCo_id(Integer co_id) {
		this.co_id = co_id;
	}
	public Integer getCo_point() {
		return co_point;
	}
	public void setCo_point(Integer co_point) {
		this.co_point = co_point;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getOver_time() {
		return over_time;
	}
	public void setOver_time(Date over_time) {
		this.over_time = over_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCo_name() {
		return co_name;
	}
	public void setCo_name(String co_name) {
		this.co_name = co_name;
	}
	public String getCo_description() {
		return co_description;
	}
	public void setCo_description(String co_description) {
		this.co_description = co_description;
	}
	public String getCo_path() {
		return co_path;
	}
	public void setCo_path(String co_path) {
		this.co_path = co_path;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
