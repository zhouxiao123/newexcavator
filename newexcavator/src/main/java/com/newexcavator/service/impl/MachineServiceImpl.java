package com.newexcavator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newexcavator.dao.CategoryDao;
import com.newexcavator.dao.MachineInforDao;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.Category;
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
import com.newexcavator.service.MachineService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;

@Service(value = "machineService")
public class MachineServiceImpl extends AbstractModuleSuport implements MachineService {
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private MachineInforDao machineInforDao;
	
	@Override
	public List<Category> queryCategoryByModuleId(Integer moduleId) {
		return categoryDao.queryCategoryByModuleId(moduleId);
	}

	@Override
	public Integer saveMachineInfor(MachineInfor machineInfor) {
		if (machineInfor.getId() != null && machineInfor.getId().intValue() > 0) {
			this.update("com.excavator.dao.MachineInforDao.modMachineInfor", machineInfor);
		} else {
			this.save("com.excavator.dao.MachineInforDao.saveMachineInfor", machineInfor);
			System.out.println("ID=============>>>" + machineInfor.getId());
		}
		
		if (!CollectionUtils.isEmpty(machineInfor.getThumbList())) {
			for (MachineThumb fileName : machineInfor.getThumbList()) {
				fileName.setMachine_id(machineInfor.getId());
				this.save("com.excavator.dao.MachineInforDao.saveThumb", fileName);
			}
		}
		return machineInfor.getId();
	}

	public MachineInfor queryMachineInforById(Integer id) {
		List<MachineInfor> milist = machineInforDao.queryMachineInforById(id);
		return CollectionUtils.isEmpty(milist) ? null : milist.get(0);
	}

	@Override
	public List<MachineInfor> queryMachineInforByTypeId(Integer type_id, Map<String, Object> param, PageSupport pageSupport) {
		if (param == null)
			param = new HashMap<String, Object>();
		param.put("type_id", type_id);
		return this.getListPageSupportByManualOperation("com.excavator.dao.MachineInforDao.queryMachineInforByTypeId", "com.excavator.dao.MachineInforDao.queryMachineInforByTypeId_count", param, pageSupport);
	}

	@Override
	public void delMachineInfor(List<Integer> ids) {
		for (Integer id : ids) {
			Machine m = this.queryMachineById(id);
			for (MachinePic mt : m.getMp()) {
				DataUtil.deleteByUpload2Img(mt.getPath());
			}
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.update("com.newexcavator.dao.MachineInforDao.delMachine", param);
	}
	
	@Override
	public void delCommodity(List<Integer> ids) {
		for (Integer id : ids) {
			Commodity c = this.queryCommodityById(id);
			DataUtil.deleteByUpload2Img(c.getPath());
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.update("com.newexcavator.dao.MachineInforDao.delCommodity", param);
	}
	
	
	@Override
	public void delRentMachineInfor(List<Integer> ids) {
		for (Integer id : ids) {
			Machine m = this.queryRentMachineById(id);
			for (MachinePic mt : m.getMp()) {
				DataUtil.deleteByUpload2Img(mt.getPath());
			}
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.update("com.newexcavator.dao.MachineInforDao.delRentMachine", param);
	}
	
	@Override
	public void delBuyMachineInfor(List<Integer> ids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.update("com.newexcavator.dao.MachineInforDao.delBuyMachine", param);
	}
	
	@Override
	public void delPerson(List<Integer> ids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.update("com.newexcavator.dao.MachineInforDao.delPerson", param);
	}

	@Override
	public void delThumbById(Integer id) {
		MachineThumb mt = machineInforDao.queryMachineThumbById(id);
		if (mt != null) {
			DataUtil.deleteByUpload2Img(mt.getThumb_url());
			machineInforDao.delThumbById(id);
		}
	}

	@Override
	public List<ExcavatorType> queryExcavatorType() {
		return machineInforDao.queryExcavatorType();
	}

	@Override
	public List<City> queryCityByPid(Integer pid) {
		return machineInforDao.queryCityByPid(pid);
	}

	@Override
	public List<Brand> queryBrandByExcavatorTypeId(Integer tid) {
		return machineInforDao.queryBrandByExcavatorTypeId(tid);
	}

	@Override
	public List<Version> queryVersionByExcavatorTypeIdAndBrandId(Integer tid,
			Integer bid) {
		return machineInforDao.queryVersionByExcavatorTypeIdAndBrandId(tid, bid);
	}

	@Override
	public void saveMachine(Machine machine) {
		machineInforDao.saveMachine(machine);
		for(MachinePic mp:machine.getMp()){
			mp.setM_id(machine.getId());
			machineInforDao.insertMachinePic(mp);
		}
	}
	
	@Override
	public void saveRentMachine(Machine machine) {
		machineInforDao.saveRentMachine(machine);
		for(MachinePic mp:machine.getMp()){
			mp.setM_id(machine.getId());
			machineInforDao.insertRentMachinePic(mp);
		}
	}
	
	@Override
	public void saveBuyMachine(Machine machine) {
		machineInforDao.saveBuyMachine(machine);
	}

	@Override
	public List<Machine> queryMachine(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryMachine", "com.newexcavator.dao.MachineInforDao.queryMachine_count", param, pageSupport);
	}
	
	@Override
	public List<Machine> queryRentMachine(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryRentMachine", "com.newexcavator.dao.MachineInforDao.queryRentMachine_count", param, pageSupport);
	}

	
	@Override
	public List<Machine> queryBuyMachine(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryBuyMachine", "com.newexcavator.dao.MachineInforDao.queryBuyMachine_count", param, pageSupport);
	}

	@Override
	public Machine queryMachineById(Integer id) {
		return machineInforDao.queryMachineById(id);
	}
	
	@Override
	public Machine queryRentMachineById(Integer id) {
		return machineInforDao.queryRentMachineById(id);
	}
	
	@Override
	public Machine queryBuyMachineById(Integer id) {
		return machineInforDao.queryBuyMachineById(id);
	}
	
	@Override
	public Person queryPersonById(Integer id) {
		return machineInforDao.queryPersonById(id);
	}

	@Override
	public void updateMachineVerify(Integer id, Integer verify) {
		machineInforDao.updateMachineVerify(id,verify);
	}
	
	@Override
	public void updateRentMachineVerify(Integer id, Integer verify) {
		machineInforDao.updateRentMachineVerify(id,verify);
	}
	
	@Override
	public void updateBuyMachineVerify(Integer id, Integer verify) {
		machineInforDao.updateBuyMachineVerify(id,verify);
	}
	
	@Override
	public void updatePersonVerify(Integer id, Integer verify) {
		machineInforDao.updatePersonVerify(id,verify);
	}

	@Override
	public void updateMachineClose(Integer id, Integer close) {
		machineInforDao.updateMachineClose(id,close);
	}
	
	@Override
	public void updateRentMachineClose(Integer id, Integer close) {
		machineInforDao.updateRentMachineClose(id,close);
	}
	
	@Override
	public void updateBuyMachineClose(Integer id, Integer close) {
		machineInforDao.updateBuyMachineClose(id,close);
	}
	
	@Override
	public void updatePersonClose(Integer id, Integer close) {
		machineInforDao.updatePersonClose(id,close);
	}

	@Override
	public void delMachinePic(Integer id) {
		MachinePic mp = machineInforDao.queryMachinePicById(id);
		DataUtil.deleteByUpload2Img(mp.getPath());
		machineInforDao.deleteMachinePicById(id);
		if(mp.getCover()==1){
			machineInforDao.updateMachineCover(mp.getM_id());
		}
	}
	
	@Override
	public void delRentMachinePic(Integer id) {
		MachinePic mp = machineInforDao.queryRentMachinePicById(id);
		DataUtil.deleteByUpload2Img(mp.getPath());
		machineInforDao.deleteRentMachinePicById(id);
		if(mp.getCover()==1){
			machineInforDao.updateRentMachineCover(mp.getM_id());
		}
	}

	@Override
	public List<Machine> queryMachineByIds(Integer[] ids) {
		List<Machine> ms = new ArrayList<Machine>();
		for(Integer id:ids){
			Machine m = machineInforDao.queryMachineById(id);
			ms.add(m);
		}
		return ms;
	}

	@Override
	public SendTime querySendTime() {
		return machineInforDao.querySendTime();
	}

	@Override
	public void updateSendTime() {
		machineInforDao.updateSendTime();
	}

	@Override
	public void updateMachinesSend(Integer[] ids) {
		for(Integer id:ids){
			machineInforDao.updateMachineSendById(id);
		}
	}

	@Override
	public void savePerson(Person person) {
		machineInforDao.savePerson(person);
	}

	@Override
	public List<Person> queryPerson(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryPerson", "com.newexcavator.dao.MachineInforDao.queryPerson_count", param, pageSupport);
	}

	@Override
	public void saveCommodity(Commodity co) {
		if (co.getId() != null && co.getId().intValue() > 0) {
			if(!StringUtils.isBlank(co.getPath())){
				Commodity c = machineInforDao.queryCommodityById(co.getId());
				if(!StringUtils.isBlank(c.getPath())){
					DataUtil.deleteByUpload2Img(c.getPath());
				}
			}
			this.update("com.newexcavator.dao.MachineInforDao.updateCommodity", co);
		} else {
			this.save("com.newexcavator.dao.MachineInforDao.insertCommodity", co);
			
		}
	}

	@Override
	public Commodity queryCommodityById(Integer id) {
		return machineInforDao.queryCommodityById(id);
	}

	@Override
	public List<Commodity> queryCommodity(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryCommodity", "com.newexcavator.dao.MachineInforDao.queryCommodity_count", param, pageSupport);
	}

	@Override
	public void saveOrder(Order o) {
		machineInforDao.saveOrder(o);
	}

	@Override
	public List<Order> queryOrder(Map<String, Object> param,
			PageSupport pageSupport) {
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.MachineInforDao.queryOrder", "com.newexcavator.dao.MachineInforDao.queryOrder_count", param, pageSupport);
	}

	@Override
	public void updateOrderStatus(Integer id, Integer status) {
		machineInforDao.updateOrderStatus(id,status);
		
	}
}
