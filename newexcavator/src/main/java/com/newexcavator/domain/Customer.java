/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class Customer {
	private Integer id;
	private Integer total_number;
	private Integer sn;
	private Date sale_date;
	private Date vei_date;
	private String name;
	private String type;
	private String type_attribute;
	private String machine_number;
	private String engine_number;
	private String phone_number;
	private String sale_area;
	private String sale_rep;
	private String customer_area;
	private String IDCard;
	private String customer_type;
	private String comments;
	private Float price;
	private Float freight;
	private String payment_method;
	private Float contract_deposit;
	private Float vender_deposit;
	private Float need_deposit;
	private Float actual_deposit;
	private Float debt;

	public Integer getTotal_number() {
		return total_number;
	}

	public void setTotal_number(Integer total_number) {
		this.total_number = total_number;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

	public Date getVei_date() {
		return vei_date;
	}

	public void setVei_date(Date vei_date) {
		this.vei_date = vei_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType_attribute() {
		return type_attribute;
	}

	public void setType_attribute(String type_attribute) {
		this.type_attribute = type_attribute;
	}

	public String getMachine_number() {
		return machine_number;
	}

	public void setMachine_number(String machine_number) {
		this.machine_number = machine_number;
	}

	public String getEngine_number() {
		return engine_number;
	}

	public void setEngine_number(String engine_number) {
		this.engine_number = engine_number;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getSale_area() {
		return sale_area;
	}

	public void setSale_area(String sale_area) {
		this.sale_area = sale_area;
	}

	public String getSale_rep() {
		return sale_rep;
	}

	public void setSale_rep(String sale_rep) {
		this.sale_rep = sale_rep;
	}

	public String getCustomer_area() {
		return customer_area;
	}

	public void setCustomer_area(String customer_area) {
		this.customer_area = customer_area;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getFreight() {
		return freight;
	}

	public void setFreight(Float freight) {
		this.freight = freight;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public Float getContract_deposit() {
		return contract_deposit;
	}

	public void setContract_deposit(Float contract_deposit) {
		this.contract_deposit = contract_deposit;
	}

	public Float getVender_deposit() {
		return vender_deposit;
	}

	public void setVender_deposit(Float vender_deposit) {
		this.vender_deposit = vender_deposit;
	}

	public Float getNeed_deposit() {
		return need_deposit;
	}

	public void setNeed_deposit(Float need_deposit) {
		this.need_deposit = need_deposit;
	}

	public Float getActual_deposit() {
		return actual_deposit;
	}

	public void setActual_deposit(Float actual_deposit) {
		this.actual_deposit = actual_deposit;
	}

	public Float getDebt() {
		return debt;
	}

	public void setDebt(Float debt) {
		this.debt = debt;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", total_number=" + total_number
				+ ", sn=" + sn + ", sale_date=" + sale_date + ", vei_date="
				+ vei_date + ", name=" + name + ", type=" + type
				+ ", type_attribute=" + type_attribute + ", machine_number="
				+ machine_number + ", engine_number=" + engine_number
				+ ", phone_number=" + phone_number + ", sale_area=" + sale_area
				+ ", sale_rep=" + sale_rep + ", customer_area=" + customer_area
				+ ", IDCard=" + IDCard + ", customer_type=" + customer_type
				+ ", comments=" + comments + ", price=" + price + ", freight="
				+ freight + ", payment_method=" + payment_method
				+ ", contract_deposit=" + contract_deposit
				+ ", vender_deposit=" + vender_deposit + ", need_deposit="
				+ need_deposit + ", actual_deposit=" + actual_deposit
				+ ", debt=" + debt + "]";
	}

	
}
