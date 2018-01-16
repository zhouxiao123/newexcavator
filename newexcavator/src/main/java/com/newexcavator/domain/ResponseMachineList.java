/**
 * 
 */
package com.newexcavator.domain;

import java.util.List;

/**
 * @author Administrator
 * 
 */
public class ResponseMachineList {
	private Integer sEcho;
	private Integer iTotalRecords;
	private Integer iTotalDisplayRecords;
	private List<MachineInfor> aaData;

	public Integer getsEcho() {
		return sEcho;
	}

	public void setsEcho(Integer sEcho) {
		this.sEcho = sEcho;
	}

	public Integer getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(Integer iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public Integer getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<MachineInfor> getAaData() {
		return aaData;
	}

	public void setAaData(List<MachineInfor> aaData) {
		this.aaData = aaData;
	}

}
