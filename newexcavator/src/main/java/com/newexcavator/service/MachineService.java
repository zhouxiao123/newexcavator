/**
 * 
 */
package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import com.newexcavator.domain.Brand;
import com.newexcavator.domain.Category;
import com.newexcavator.domain.City;
import com.newexcavator.domain.Commodity;
import com.newexcavator.domain.ExcavatorType;
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
public interface MachineService {
	public List<Category> queryCategoryByModuleId(Integer moduleId);
	public Integer saveMachineInfor(MachineInfor machineInfor);
	public void saveOrder(Order o);
	public MachineInfor queryMachineInforById(Integer id);
	public List<MachineInfor> queryMachineInforByTypeId(Integer type_id, Map<String, Object> param, PageSupport pageSupport);
	public void delMachineInfor(List<Integer> ids);
	public void delCommodity(List<Integer> ids);
	public void delRentMachineInfor(List<Integer> ids);
	public void delBuyMachineInfor(List<Integer> ids);
	
	public void delPerson(List<Integer> ids);
	
	public void delMachinePic(Integer id);
	
	public void delRentMachinePic(Integer id);
	
	public void delThumbById(Integer id);
	
	/**
	 * 获取机械类型
	 * @return
	 */
	public List<ExcavatorType> queryExcavatorType();
	
	
	/**
	 * 根据parent_id获取城市
	 * @param pid
	 * @return
	 */
	public List<City> queryCityByPid(Integer pid);
	
	/**
	 * 根据机械型号id查询品牌
	 * @param tid
	 * @return
	 */
	public List<Brand> queryBrandByExcavatorTypeId(Integer tid);
	
	/**
	 * 根据机械型号和品牌查询机械型号
	 * @param tid
	 * @param bid
	 * @return
	 */
	public List<Version> queryVersionByExcavatorTypeIdAndBrandId(Integer tid,Integer bid);
	
	/**
	 * 保存机械资料
	 * @param machine
	 */
	public void saveMachine(Machine machine);
	
	public void saveCommodity(Commodity co);
	
	public void savePerson(Person person);
	
	public void saveRentMachine(Machine machine);
	/**
	 * 保存机械求购资料
	 * @param machine
	 */
	public void saveBuyMachine(Machine machine);
	
	/**
	 * 根据条件获取机械类表
	 * @param param
	 * @param pageSupport
	 * @return
	 */
	public List<Machine> queryMachine(Map<String, Object> param, PageSupport pageSupport);
	
	public List<Order> queryOrder(Map<String, Object> param, PageSupport pageSupport);
	
	public List<Commodity> queryCommodity(Map<String, Object> param, PageSupport pageSupport);
	
	public List<Machine> queryRentMachine(Map<String, Object> param, PageSupport pageSupport);
	
	public List<Person> queryPerson(Map<String, Object> param, PageSupport pageSupport);
	
	/**
	 * 根据条件获取机械求购类表
	 * @param param
	 * @param pageSupport
	 * @return
	 */
	public List<Machine> queryBuyMachine(Map<String, Object> param, PageSupport pageSupport);
	
	/**
	 * 根据id查询机械详情
	 * @param id
	 * @return
	 */
	public Machine queryMachineById(Integer id);
	
	public Commodity queryCommodityById(Integer id);
	
	public Machine queryRentMachineById(Integer id);
	
	/**
	 * 根据id查询机械详情
	 * @param id
	 * @return
	 */
	public Machine queryBuyMachineById(Integer id);
	
	public Person queryPersonById(Integer id);
	
	/**
	 * 根据多个id查询机械详情
	 * @param id
	 * @return
	 */
	public List<Machine> queryMachineByIds(Integer[] ids);
	
	
	
	/**
	 * 跟新审核状态
	 * @param id
	 * @param verify
	 */
	public void updateMachineVerify(Integer id,Integer verify);
	
	public void updateRentMachineVerify(Integer id,Integer verify);
	
	public void updateOrderStatus(Integer id,Integer status);
	
	/**
	 * 跟新审核状态
	 * @param id
	 * @param verify
	 */
	public void updateBuyMachineVerify(Integer id,Integer verify);
	
	public void updatePersonVerify(Integer id,Integer verify);
	
	/**
	 * 跟新关闭状态
	 * @param id
	 * @param verify
	 */
	public void updateMachineClose(Integer id,Integer close);
	
	public void updateRentMachineClose(Integer id,Integer close);
	
	/**
	 * 跟新关闭状态
	 * @param id
	 * @param verify
	 */
	public void updateBuyMachineClose(Integer id,Integer close);
	
	public void updatePersonClose(Integer id,Integer close);
	
	/**
	 * 查看今日是否发布
	 * @param id
	 * @param verify
	 */
	public SendTime querySendTime();
	
	/**
	 * 跟新发布时间
	 * @param id
	 * @param verify
	 */
	public void updateSendTime();
	
	/**
	 * 跟新发布状态
	 * @param id
	 * @param verify
	 */
	public void updateMachinesSend(Integer[] ids);
}
