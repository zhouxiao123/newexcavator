package com.newexcavator.wechat.model.message.server;

import java.util.Map;

public class DeployMessage {
	/**
	 * 填写图文消息的接收者，一串OpenID列表，OpenID最少1个，最多10000个
	 */
	private String touser;
	/**
	 * 用于设定即将发送的图文消息
	 */
	private Map<String, Object> mpnews;
	
	/**
	 * 用于设定即将发送的图文消息
	 */
	private Map<String, Object> filter;
	
	/**
	 * 群发的消息类型，图文消息为mpnews
	 */
	private String msgtype;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public Map<String, Object> getMpnews() {
		return mpnews;
	}

	public void setMpnews(Map<String, Object> mpnews) {
		this.mpnews = mpnews;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

}
