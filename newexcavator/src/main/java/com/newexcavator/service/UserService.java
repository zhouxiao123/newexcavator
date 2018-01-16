/**
 * 
 */
package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import com.newexcavator.domain.Customer;
import com.newexcavator.domain.OrderList;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 * 
 */
public interface UserService {
	public Customer queryCustomerById(Integer id);
	
	public Point queryPoint();
	
	public void updatePoint(Integer point);

	public void updatePassword(String password, Integer id);

	public List<Customer> queryCustomer(Map<String, Object> param, PageSupport pageSupport);
	
	public void updateSysUsers(SysUsers sysUsers);
	
	public void updateSysUsersById(SysUsers sysUsers);
	
	public void updateLoginInfor(Integer id);
	
	public void delCustomer(List<Integer> ids);
	
	public void delOrderList(List<Integer> ids);
	
	public Integer querySysUserByName(String name, Integer id);
	
	public List<SysUsers> querySysUserRank(Map<String, Object> param, PageSupport pageSupport);
	
	public Integer queryCountSysUserByUsername(String username);
	
	public void saveCustomer(Customer customer);
	
	public SysUsers querySysUserByUsername(String username, String pwd);
	
	public void saveUser(SysUsers sysUsers);
	
	public SysUsers querySysUserByOpenid(String openid);
	
	public void saveOrderList(OrderList orderList);
	
	public List<OrderList> queryOrderList(Map<String, Object> param, PageSupport pageSupport);
	
	public Integer queryCustomerByName(String name, Integer id);
	
	public void updateCheck(Integer id);
	
	public Integer queryOne();
	
	public List<SysUsers> querySysUsers(Map<String, Object> param, PageSupport pageSupport);
	
	public void removeUserPermission(Integer id);
	
	public SysUsers querySysUserByid(Integer id);
	
	public void updatePermission(Map<String, Object> param);
	
	public SysUsers queryTopicUserByOpenid(String openid);
	
	public List<String> queryTopicUserOpenid();
	
	public SysUsers querySysUserByPhone(String phone);
	
	public void addPoint(Integer id,Integer point);
	
	public void subPoint(Integer id,Integer point);
	
	public void updateShareTime(Integer id);
}
