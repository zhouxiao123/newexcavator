/**
 * 
 */
package com.newexcavator.domain;

import java.util.Date;

/**
 * @author Administrator
 * 
 */
public class Message {
	private Integer id;
	private String open_id;
	private String content;
	private String msg_id;
	private String msg_type;
	private String media_id;
	private String pic_url;
	private Date create_time;
	private Integer replied;
	private String r_msg_type;

	public String getR_msg_type() {
		return r_msg_type;
	}

	public void setR_msg_type(String r_msg_type) {
		this.r_msg_type = r_msg_type;
	}

	public Integer getReplied() {
		return replied;
	}

	public void setReplied(Integer replied) {
		this.replied = replied;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
