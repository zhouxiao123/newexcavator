/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class DeployInfor {
	/**
	 * 活动
	 */
	public static final Integer TYPE_ACTIVITY = 1;
	/**
	 * 新闻
	 */
	public static final Integer TYPE_NEWS = 2;

	private Integer id;
	private String title;
	private String thumb_url;
	private String description;
	private String content;
	private Integer type;
	private Date create_time;
	private String media_thumb_id;
	private String media_id;
	private Integer issend;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getMedia_thumb_id() {
		return media_thumb_id;
	}

	public void setMedia_thumb_id(String media_thumb_id) {
		this.media_thumb_id = media_thumb_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIssend() {
		return issend;
	}

	public void setIssend(Integer issend) {
		this.issend = issend;
	}


}
