/**
 * 
 */
package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import com.newexcavator.domain.DeployInfor;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
public interface DeployInforService {
	public Integer saveDeployInfor(DeployInfor deployInfor);
	public DeployInfor queryDeployInforById(Integer id);
	public List<DeployInfor> queryDeployInforByTypeId(Integer type_id, Map<String, Object> param, PageSupport pageSupport);
	
	public void delDeployInfor(List<Integer> ids);
	public DeployInfor queryLastestDeployInforByTypeId(Integer type_id);
	public void updateMediaIdDeployInfor(DeployInfor deployInfor);
}
