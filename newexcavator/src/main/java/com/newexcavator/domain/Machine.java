package com.newexcavator.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Machine {

	private Integer id;
	private Integer user_id;
	private Integer m_type;
	private Integer big_type;
	private String type_name;
	private Integer brand;
	private String brand_name;
	private Integer version;
	private String version_name;
	private Integer used_time;
	private Float price;
	private Float price2;
	private Integer change_price;
	private Integer place_p;
	private String p_name;
	private Integer place_c;
	private String c_name;
	private Integer imported;
	private Integer qualified;
	private Integer receipt;
	private Date production_date;
	private Date buy_date;
	private String code_no;
	private Integer modified;
	private Integer fixed;
	private Integer old_level;
	private Integer use;
	private Integer current_status;
	private String description;
	private String link_name;
	private String phone;
	private String qq;
	private Date create_time;
	private Integer close;
	private Date close_time;
	private Integer deleted;
	private Date deleted_time;
	private Integer verify;
	private String cover_path;
	private List<MachinePic> mp = new ArrayList<MachinePic>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getM_type() {
		return m_type;
	}
	public void setM_type(Integer m_type) {
		this.m_type = m_type;
	}
	public Integer getBrand() {
		return brand;
	}
	public void setBrand(Integer brand) {
		this.brand = brand;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getUsed_time() {
		return used_time;
	}
	public void setUsed_time(Integer used_time) {
		this.used_time = used_time;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getPlace_p() {
		return place_p;
	}
	public void setPlace_p(Integer place_p) {
		this.place_p = place_p;
	}
	public Integer getPlace_c() {
		return place_c;
	}
	public void setPlace_c(Integer place_c) {
		this.place_c = place_c;
	}
	public Integer getImported() {
		return imported;
	}
	public void setImported(Integer imported) {
		this.imported = imported;
	}
	public Integer getQualified() {
		return qualified;
	}
	public void setQualified(Integer qualified) {
		this.qualified = qualified;
	}
	public Integer getReceipt() {
		return receipt;
	}
	public void setReceipt(Integer receipt) {
		this.receipt = receipt;
	}
	public Date getProduction_date() {
		return production_date;
	}
	public void setProduction_date(Date production_date) {
		this.production_date = production_date;
	}
	public Date getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(Date buy_date) {
		this.buy_date = buy_date;
	}
	public String getCode_no() {
		return code_no;
	}
	public void setCode_no(String code_no) {
		this.code_no = code_no;
	}
	public Integer getModified() {
		return modified;
	}
	public void setModified(Integer modified) {
		this.modified = modified;
	}
	public Integer getFixed() {
		return fixed;
	}
	public void setFixed(Integer fixed) {
		this.fixed = fixed;
	}
	public Integer getOld_level() {
		return old_level;
	}
	public void setOld_level(Integer old_level) {
		this.old_level = old_level;
	}
	public Integer getUse() {
		return use;
	}
	public void setUse(Integer use) {
		this.use = use;
	}
	public Integer getCurrent_status() {
		return current_status;
	}
	public void setCurrent_status(Integer current_status) {
		this.current_status = current_status;
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
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
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
	public List<MachinePic> getMp() {
		return mp;
	}
	public void setMp(List<MachinePic> mp) {
		this.mp = mp;
	}
	
	public void addMp(MachinePic m){
		this.mp.add(m);
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getCover_path() {
		return cover_path;
	}
	public void setCover_path(String cover_path) {
		this.cover_path = cover_path;
	}
	public Integer getChange_price() {
		return change_price;
	}
	public void setChange_price(Integer change_price) {
		this.change_price = change_price;
	}
	public Float getPrice2() {
		return price2;
	}
	public void setPrice2(Float price2) {
		this.price2 = price2;
	}
	public Integer getBig_type() {
		return big_type;
	}
	public void setBig_type(Integer big_type) {
		this.big_type = big_type;
	}
	
}
