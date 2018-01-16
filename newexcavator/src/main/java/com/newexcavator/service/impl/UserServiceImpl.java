/**
 * 
 */
package com.newexcavator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newexcavator.dao.OrderListDao;
import com.newexcavator.dao.SysUsersDao;
import com.newexcavator.domain.Customer;
import com.newexcavator.domain.OrderList;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.service.UserService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
@Service(value="userService")
public class UserServiceImpl extends AbstractModuleSuport implements UserService {
	
	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Autowired
	private OrderListDao orderListDao;
	
	@Override
	public Customer queryCustomerById(Integer id) {
		return sysUsersDao.queryCustomerById(id);
	}

	@Override
	public void updatePassword(String password, Integer id) {
		sysUsersDao.updatePassword(password, id);
	}

	@Override
	public List<Customer> queryCustomer(Map<String, Object> param, PageSupport pageSupport) {
		if (pageSupport != null)
			return this.getListPageSupportByManualOperation("com.newexcavator.dao.SysUsersDao.queryCustomer", "com.newexcavator.dao.SysUsersDao.queryCustomer_count", param, pageSupport);
		else 
			return this.getList("com.newexcavator.dao.SysUsersDao.queryCustomer", param);
	}

	@Override
	public void updateSysUsers(SysUsers sysUsers) {
		sysUsersDao.updateSysUsers(sysUsers);
	}

	@Override
	public void delCustomer(List<Integer> ids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.delete("com.excavator.dao.SysUsersDao.delCustomer", param);
	}

	@Override
	public Integer querySysUserByName(String name, Integer id) {
		return sysUsersDao.querySysUserByName(name, id);
	}
	
	@Override
	public Integer queryCountSysUserByUsername(String username) {
		return sysUsersDao.queryCountSysUserByUsername(username);
	}

	@Override
	public void saveCustomer(Customer customer) {
		if (customer.getId() != null && customer.getId() > 0) {
			sysUsersDao.updateCustomer(customer);
		} else {
			sysUsersDao.saveCustomer(customer);
			log.debug("saveSysUser=ID=>" + customer.getId());
		}
	}

	@Override
	public SysUsers querySysUserByUsername(String username, String pwd) {
		return sysUsersDao.querySysUserByUsername(username, pwd);
	}

	@Override
	public void saveUser(SysUsers sysUsers) {
		sysUsersDao.saveSysUser(sysUsers);
	}

	@Override
	public SysUsers querySysUserByOpenid(String openid) {
		return sysUsersDao.querySysUserByOpenid(openid);
	}

	@Override
	public void saveOrderList(OrderList orderList) {
		orderListDao.saveOrderList(orderList);
	}

	@Override
	public List<OrderList> queryOrderList(Map<String, Object> param,
			PageSupport pageSupport) {
		if (pageSupport != null)
			return this.getListPageSupportByManualOperation("com.excavator.dao.OrderListDao.queryOrderList", "com.excavator.dao.OrderListDao.queryOrderList_count", param, pageSupport);
		else 
			return this.getList("com.excavator.dao.OrderListDao.queryOrderList", param);
	}

	@Override
	public void delOrderList(List<Integer> ids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		this.delete("com.excavator.dao.OrderListDao.delOrderList", param);
	}

	@Override
	public Integer queryCustomerByName(String name, Integer id) {
		return sysUsersDao.queryCustomerByName(name, id);
	}

	@Override
	public void updateCheck(Integer id) {
		orderListDao.updateCheck(id);
	}

	@Override
	public Integer queryOne() {
		return sysUsersDao.queryOne();
	}

	@Override
	public List<SysUsers> querySysUsers(Map<String, Object> param,
			PageSupport pageSupport) {
		// TODO Auto-generated method stub
		if (pageSupport != null)
			return this.getListPageSupportByManualOperation("com.newexcavator.dao.SysUsersDao.querySysUsers", "com.newexcavator.dao.SysUsersDao.querySysUsers_count", param, pageSupport);
		else 
			return this.getList("com.newexcavator.dao.SysUsersDao.querySysUsers", param);
	}

	@Override
	public void removeUserPermission(Integer id) {
		// TODO Auto-generated method stub
		sysUsersDao.removeUserPermission(id);
	}

	@Override
	public SysUsers querySysUserByid(Integer id) {
		// TODO Auto-generated method stub
		return sysUsersDao.querySysUserByid(id);
	}

	@Override
	public void updatePermission(Map<String, Object> param) {
		// TODO Auto-generated method stub
		sysUsersDao.updatePermission(param);
	}

	@Override
	public SysUsers queryTopicUserByOpenid(String openid) {
		// TODO Auto-generated method stub
		return sysUsersDao.queryTopicUserByOpenid(openid);
	}

	@Override
	public List<String> queryTopicUserOpenid() {
		// TODO Auto-generated method stub
		return sysUsersDao.queryTopicUserOpenid();
	}

	@Override
	public void updateSysUsersById(SysUsers sysUsers) {
		sysUsersDao.updateSysUsersById(sysUsers);
	}
	
	@Override
	public SysUsers querySysUserByPhone(String phone) {
		return sysUsersDao.querySysUserByPhone(phone);
	}

	@Override
	public void updateLoginInfor(Integer id) {
		sysUsersDao.updateLoginInfor(id,null);
	}

	@Override
	public void addPoint(Integer id, Integer point) {
		sysUsersDao.addPoint(id,point);
	}

	@Override
	public void subPoint(Integer id, Integer point) {
		sysUsersDao.subPoint(id,point);
		
	}

	@Override
	public List<SysUsers> querySysUserRank(Map<String, Object> param,
			PageSupport pageSupport) {
		if (pageSupport != null)
			return this.getListPageSupportByManualOperation("com.newexcavator.dao.SysUsersDao.querySysUserRank", "com.newexcavator.dao.SysUsersDao.querySysUserRank_count", param, pageSupport);
		else 
			return this.getList("com.newexcavator.dao.SysUsersDao.querySysUserRank", param);
	}

	@Override
	public void updateShareTime(Integer id) {
		sysUsersDao.updateShareTime(id);
	}

	@Override
	public Point queryPoint() {
		
		return sysUsersDao.queryPoint();
	}

	@Override
	public void updatePoint(Integer point) {
		sysUsersDao.updatePoint(point);
		
	}
	
}
