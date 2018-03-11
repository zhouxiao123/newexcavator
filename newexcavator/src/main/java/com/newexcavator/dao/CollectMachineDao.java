/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Category;
import com.newexcavator.domain.CollectMachine;

/**
 * @author Administrator
 *
 */
public interface CollectMachineDao {
	
	public CollectMachine findByUseridAndMid(@Param(value="userid")Integer userid,@Param(value="m_id")Integer mid);
	
	public CollectMachine saveCollectMachine(CollectMachine cm);
	
	public void deleteCollectMachine(CollectMachine cm);
	
}
