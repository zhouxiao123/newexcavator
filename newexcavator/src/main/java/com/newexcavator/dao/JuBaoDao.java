/**
 * 
 */
package com.newexcavator.dao;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.CollectMachine;
import com.newexcavator.domain.JuBao;
import com.newexcavator.domain.OrderList;

/**
 * @author Administrator
 *
 */
public interface JuBaoDao {
	public JuBao findByUseridAndMid(@Param(value="userid")Integer userid,@Param(value="m_id")Integer mid);
	public void saveJuBao(JuBao jb);
	public void deleteJuBao(JuBao jb);
}
