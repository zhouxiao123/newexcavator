/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class Answer {

	private Integer id;
	private Integer userid;
	private Integer ask_id;
	private String content;
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



	public Integer getAsk_id() {
		return ask_id;
	}

	public void setAsk_id(Integer ask_id) {
		this.ask_id = ask_id;
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



	


}
