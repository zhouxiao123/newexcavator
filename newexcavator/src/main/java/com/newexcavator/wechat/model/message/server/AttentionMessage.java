/**
 * 
 */
package com.newexcavator.wechat.model.message.server;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 
 */
public class AttentionMessage {
	private Integer errcode;
	private Integer total;
	private Integer count;
	private Map<String, List<String>> data;
	private String next_openid;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, List<String>> getData() {
		return data;
	}

	public void setData(Map<String, List<String>> data) {
		this.data = data;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
}
