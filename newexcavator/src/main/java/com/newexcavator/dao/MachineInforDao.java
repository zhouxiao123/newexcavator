/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Brand;
import com.newexcavator.domain.City;
import com.newexcavator.domain.Commodity;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.MachineInfor;
import com.newexcavator.domain.MachinePic;
import com.newexcavator.domain.MachineThumb;
import com.newexcavator.domain.Order;
import com.newexcavator.domain.Person;
import com.newexcavator.domain.SendTime;
import com.newexcavator.domain.Version;


/**
 * @author randy
 *
 */
public interface MachineInforDao {
	public List<MachineInfor> queryMachineInforById(@Param(value="id") Integer id);
	
	public void delThumbById(@Param(value="id") Integer id);
	
	public MachineThumb queryMachineThumbById(@Param(value="id") Integer id);
	
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
	public List<Version> queryVersionByExcavatorTypeIdAndBrandId(@Param(value="tid")Integer tid,@Param(value="bid")Integer bid);
	
	
	/**
	 * 保存机械求购资料
	 * @param machine
	 */
	public void saveBuyMachine(Machine machine);
	
	
	public void savePerson(Person person);
	
	public void saveOrder(Order o);
	
	/**
	 * 保存机械资料
	 * @param machine
	 */
	public void saveMachine(Machine machine);
	
	/**
	 * 保存机械资料
	 * @param machine
	 */
	public void saveRentMachine(Machine machine);
	
	/**
	 * 保存机械图片
	 * @param machine
	 */
	public void insertMachinePic(MachinePic mp);
	
	/**
	 * 保存机械图片
	 * @param machine
	 */
	public void insertRentMachinePic(MachinePic mp);
	
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
	
	/**
	 * 跟新审核状态
	 * @param id
	 * @param verify
	 */
	public void updateMachineVerify(@Param(value="id")Integer id,@Param(value="verify")Integer verify);
	
	public void updateRentMachineVerify(@Param(value="id")Integer id,@Param(value="verify")Integer verify);
	
	public void updateOrderStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);
	
	/**
	 * 跟新审核状态
	 * @param id
	 * @param verify
	 */
	public void updateBuyMachineVerify(@Param(value="id")Integer id,@Param(value="verify")Integer verify);
	
	public void updatePersonVerify(@Param(value="id")Integer id,@Param(value="verify")Integer verify);
	
	/**
	 * 跟新关闭状态
	 * @param id
	 * @param verify
	 */
	public void updateMachineClose(@Param(value="id")Integer id,@Param(value="close")Integer close);
	
	public void updateRentMachineClose(@Param(value="id")Integer id,@Param(value="close")Integer close);
	
	/**
	 * 跟新关闭状态
	 * @param id
	 * @param verify
	 */
	public void updateBuyMachineClose(@Param(value="id")Integer id,@Param(value="close")Integer close);
	
	public void updatePersonClose(@Param(value="id")Integer id,@Param(value="close")Integer close);
	
	
	public void updateMachineCover(@Param(value="m_id")Integer m_id);
	
	public void deleteMachinePicById(@Param(value="id") Integer id);
	
	public MachinePic queryMachinePicById(@Param(value="id") Integer id);
	
	
public void updateRentMachineCover(@Param(value="m_id")Integer m_id);
	
	public void deleteRentMachinePicById(@Param(value="id") Integer id);
	
	public MachinePic queryRentMachinePicById(@Param(value="id") Integer id);
	
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
	
	public void updateMachineSendById(Integer id);
	
	public Person queryPersonById(Integer id);
}
