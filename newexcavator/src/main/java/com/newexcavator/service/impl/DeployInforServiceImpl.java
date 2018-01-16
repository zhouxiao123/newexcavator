/**
 * 
 */
package com.newexcavator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newexcavator.dao.DeployInforDao;
import com.newexcavator.domain.DeployInfor;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
@Service(value = "deployInforService")
public class DeployInforServiceImpl extends AbstractModuleSuport implements DeployInforService {

	@Autowired
	private DeployInforDao deployInforDao;
	
	/* (non-Javadoc)
	 * @see com.excavator.service.DeployInforService#saveDeployInfor(com.excavator.domain.DeployInfor)
	 */
	@Override
	public Integer saveDeployInfor(DeployInfor deployInfor) {
		if (deployInfor.getId() != null && deployInfor.getId().intValue() > 0) {
			this.update("com.excavator.dao.DeployInforDao.modDeployInfor", deployInfor);
		} else {
			this.save("com.excavator.dao.DeployInforDao.saveDeployInfor", deployInfor);
			System.out.println("ID=============>>>" + deployInfor.getId());
		}
		return deployInfor.getId();
	}

	/* (non-Javadoc)
	 * @see com.excavator.service.DeployInforService#queryDeployInforById(java.lang.Integer)
	 */
	@Override
	public DeployInfor queryDeployInforById(Integer id) {
		return deployInforDao.queryDeployInforById(id);
	}

	/* (non-Javadoc)
	 * @see com.excavator.service.DeployInforService#queryDeployInforByTypeId(java.lang.Integer)
	 */
	@Override
	public List<DeployInfor> queryDeployInforByTypeId(Integer type_id, Map<String, Object> param, PageSupport pageSupport) {
		if (param == null)
			param = new HashMap<String, Object>();
		param.put("type_id", type_id);
		
		return this.getListPageSupportByManualOperation("com.excavator.dao.DeployInforDao.queryDeployInforByTypeId", "com.excavator.dao.DeployInforDao.queryDeployInforByTypeId_count", param, pageSupport);
	}

	@Override
	public void delDeployInfor(List<Integer> ids) {
		for (Integer id : ids) {
			DeployInfor di = this.queryDeployInforById(id);
			DataUtil.deleteByUpload2Img(di.getThumb_url());
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.delete("com.excavator.dao.DeployInforDao.delDeployInfor", param);
	}

	@Override
	public DeployInfor queryLastestDeployInforByTypeId(Integer type_id) {
		return deployInforDao.queryLastestDeployInforByTypeId(type_id);
	}

	@Override
	public void updateMediaIdDeployInfor(DeployInfor deployInfor) {
		deployInforDao.modMediaIdDeployInfor(deployInfor);
	}

}
