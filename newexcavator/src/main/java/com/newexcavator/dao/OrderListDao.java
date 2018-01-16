/**
 * 
 */
package com.newexcavator.dao;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.OrderList;

/**
 * @author Administrator
 *
 */
public interface OrderListDao {
	public void saveOrderList(OrderList orderList);
	public void updateCheck(@Param(value="id") Integer id);
}
