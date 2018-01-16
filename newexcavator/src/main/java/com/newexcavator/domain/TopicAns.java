package com.newexcavator.domain;

import java.util.Date;

public class TopicAns {
	private Integer id;

	private Integer topicid;

	private String ansname;

	private Integer ansid;

	private Date anstime;

	private String anscontent;

	private String favour;

	private Integer favour_count;

	private String head;

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTopicid() {
		return topicid;
	}

	public void setTopicid(Integer topicid) {
		this.topicid = topicid;
	}

	public String getAnsname() {
		return ansname;
	}

	public void setAnsname(String ansname) {
		this.ansname = ansname;
	}

	public Integer getAnsid() {
		return ansid;
	}

	public void setAnsid(Integer ansid) {
		this.ansid = ansid;
	}

	public Date getAnstime() {
		return anstime;
	}

	public void setAnstime(Date anstime) {
		this.anstime = anstime;
	}

	public String getAnscontent() {
		return anscontent;
	}

	public void setAnscontent(String anscontent) {
		this.anscontent = anscontent;
	}

	public String getFavour() {
		return favour;
	}

	public void setFavour(String favour) {
		this.favour = favour;
	}

	public Integer getFavour_count() {
		return favour_count;
	}

	public void setFavour_count(Integer favour_count) {
		this.favour_count = favour_count;
	}
}
