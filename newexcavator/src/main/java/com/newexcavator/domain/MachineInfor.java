/**
 * 
 */
package com.newexcavator.domain;

import java.util.List;

/**
 * @author Administrator
 * 
 */
public class MachineInfor {
	private String checkboxHtml = "<input name=\"id\" type=\"checkbox\" value=\"#{id}\" />";
	private Integer id;
	private String name;
	private Integer type_id;
	private Integer cat_id;
	private String version;
	private String phone;
	private String thumb_url;
	private String description;
	private Float jine;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private List<MachineThumb> thumbList;

	public Float getJine() {
		return jine;
	}

	public void setJine(Float jine) {
		this.jine = jine;
	}

	public List<MachineThumb> getThumbList() {
		return thumbList;
	}

	public void setThumbList(List<MachineThumb> thumbList) {
		this.thumbList = thumbList;
	}

	public String getCheckboxHtml() {
		return checkboxHtml;
	}

	public void setCheckboxHtml(String checkboxHtml) {
		this.checkboxHtml = checkboxHtml;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		checkboxHtml = checkboxHtml.replace("#{id}", id + "");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public Integer getCat_id() {
		return cat_id;
	}

	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
