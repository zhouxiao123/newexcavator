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
import com.newexcavator.dao.CollectMachineDao;
import com.newexcavator.dao.MachineInforDao;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.Category;
import com.newexcavator.domain.City;
import com.newexcavator.domain.CollectMachine;
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
import com.newexcavator.service.CollectMachineService;
import com.newexcavator.service.MachineService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;

@Service(value = "collectMachineService")
public class CollectMachineServiceImpl extends AbstractModuleSuport implements CollectMachineService {
	

	
	@Autowired
	private CollectMachineDao collectMachineDao;

	@Override
	public CollectMachine findByUseridAndMid(Integer userid, Integer mid) {
		// TODO Auto-generated method stub
		return collectMachineDao.findByUseridAndMid(userid, mid);
	}

	@Override
	public CollectMachine saveCollectMachine(CollectMachine cm) {
		// TODO Auto-generated method stub
		return collectMachineDao.saveCollectMachine(cm);
	}

	@Override
	public void deleteCollectMachine(CollectMachine cm) {
		// TODO Auto-generated method stub
		collectMachineDao.deleteCollectMachine(cm);
	}

	@Override
	public List<CollectMachine> queryCollectMachineByUserid(Integer userid, PageSupport pageSupport) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userid", userid);
		return this.getListPageSupportByManualOperation("com.excavator.dao.CollectMachineDao.queryCollectMachineByUserid", "com.excavator.dao.CollectMachineDao.queryCollectMachineByUserid_count", param, pageSupport);
	}
	
	
}
