/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class Ask {

	private Integer id;
	private Integer userid;
	private Integer m_id;
	private String content;
	private Integer answercount;
	private Date createtime;
	
	private SysUsers user;
	
	

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public SysUsers getUser() {
		return user;
	}

	public void setUser(SysUsers user) {
		this.user = user;
	}

	public Integer getAnswercount() {
		return answercount;
	}

	public void setAnswercount(Integer answercount) {
		this.answercount = answercount;
	}

	


}
