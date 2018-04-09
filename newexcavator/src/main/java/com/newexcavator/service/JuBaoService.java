/**
 * 
 */
package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Brand;
import com.newexcavator.domain.Category;
import com.newexcavator.domain.City;
import com.newexcavator.domain.CollectMachine;
import com.newexcavator.domain.Commodity;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.JuBao;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.MachineInfor;
import com.newexcavator.domain.Order;
import com.newexcavator.domain.Person;
import com.newexcavator.domain.SendTime;
import com.newexcavator.domain.Version;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
public interface JuBaoService {
	
	public JuBao findByUseridAndMid(Integer userid,Integer mid);
	public void saveJuBao(JuBao jb);
	public void deleteJuBao(JuBao jb);
	
	public List<JuBao> queryJuBaoMachineList( PageSupport pageSupport);
}
