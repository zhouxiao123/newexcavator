package com.newexcavator.domain;

import java.util.Date;

public class SendTime {

	private Integer id;
	private Date send_time;
	public Date getSend_time() {
		return send_time;
	}
	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
