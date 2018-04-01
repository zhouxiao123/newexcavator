/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Ask;



/**
 * @author randy
 * 
 */
public interface AskDao {
	public void delAskByid(@Param(value = "id") Integer id);
	public void saveAsk(Ask a);
	public void updateAsk(Ask a);
	public List<Ask> queryAskListByMid(@Param(value = "mid")Integer mid);
	public Ask queryAskById(@Param(value = "id")Integer id);
	
}
