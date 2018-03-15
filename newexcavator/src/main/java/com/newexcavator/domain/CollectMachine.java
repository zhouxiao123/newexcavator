package com.newexcavator.domain;

import java.util.Date;
import java.util.List;

public class CollectMachine {
	
	private Long id;
	private Integer userid;
	private Integer m_id;
	private Date createtime;
	private Machine mi;
	
	
	public Machine getMi() {
		return mi;
	}
	public void setMi(Machine mi) {
		this.mi = mi;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getM_id() {
		return m_id;
	}
	public void setM_id(Integer m_id) {
		this.m_id = m_id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	

}
