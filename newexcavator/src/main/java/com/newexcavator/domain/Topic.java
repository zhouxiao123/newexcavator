package com.newexcavator.domain;

import java.util.Date;
import java.util.List;

public class Topic {

	private Integer id;

	private String topic;

	private Date createtime;

	private String content;

	private Integer createid;

	private String createname;

	private Integer topicid;

	private String favour;

	private Integer favour_count;

	private String head;

	private List<TopicImg> imgList;
	
	private List<TopicAns> topicans;

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public List<TopicImg> getImgList() {
		return imgList;
	}

	public void setImgList(List<TopicImg> imgList) {
		this.imgList = imgList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCreateid() {
		return createid;
	}

	public void setCreateid(Integer createid) {
		this.createid = createid;
	}

	public Integer getTopicid() {
		return topicid;
	}

	public void setTopicid(Integer topicid) {
		this.topicid = topicid;
	}

	public String getCreatename() {
		return createname;
	}

	public void setCreatename(String createname) {
		this.createname = createname;
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

	public List<TopicAns> getTopicans() {
		return topicans;
	}

	public void setTopicans(List<TopicAns> topicans) {
		this.topicans = topicans;
	}
}
