/**
 * 
 */
package com.newexcavator.domain;

/**
 * @author Administrator
 * 
 */
public class MachineThumb {
	private Integer id;
	private String thumb_url;
	
	private Integer machine_id;
	
	public Integer getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(Integer machine_id) {
		this.machine_id = machine_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

}
