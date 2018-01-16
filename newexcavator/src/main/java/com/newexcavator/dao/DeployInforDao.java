/**
 * 
 */
package com.newexcavator.dao;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.DeployInfor;


/**
 * @author randy
 *
 */
public interface DeployInforDao {
	public DeployInfor queryDeployInforById(@Param(value="id") Integer id);
	public DeployInfor queryLastestDeployInforByTypeId(@Param(value="type_id") Integer type_id);
	public void modMediaIdDeployInfor(DeployInfor deployInfor);
}
