/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class OrderList {
	private Integer id;
	private Integer quantity;
	private String address;
	private String phone;
	private String name;
	private Date create_time;
	private Integer machine_id;
	private String machine_name;
	private Integer type_id;
	private Integer checked;

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	public Integer getType_id() {
		return type_id;
	}

	public String getType() {
		return type_id == 1 ? "new" : type_id == 2 ? "old"
				: type_id == 3 ? "part" : "lease";
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public Integer getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(Integer machine_id) {
		this.machine_id = machine_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
